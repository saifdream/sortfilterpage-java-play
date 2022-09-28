package com.play2.crud;

import play.Application;

import java.util.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Objects;

import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import play.data.format.Formats.DateTime;

import com.play2.crud.annotation.Scaffold;
import com.play2.crud.annotation.Scaffoldable;
import com.play2.crud.annotation.ManyToManyBridge;
import com.play2.crud.beanprocessor.BeanField;
import com.play2.crud.beanprocessor.ScaffoldableBean;
//import com.play2.crud.enumeration.DatePickerType;
import com.play2.crud.enumeration.HtmlElementType;

import static com.play2.crud.helper.Constants.*;

import com.play2.crud.helper.NameProcessor;
import com.play2.crud.helper.Utils;
import com.play2.crud.helper.html.HtmlElement;
import com.play2.crud.template.CodeForController;
import com.play2.crud.template.CodeForHtmlViews;


/**
 * A Play plugin that automatically scaffolds desired classes
 */
public class CrudGenerator {

	private static Set<Class<?>> crudClasses = new HashSet();
	private static List<ScaffoldableBean> beans = new ArrayList();

	private final Application application = play.Play.application();
	private final String packages;

	public CrudGenerator(String packages) {
		this.packages = packages;
	}

	/*
	public String getCrudPackage() {
		return application.configuration().getString(Constants.CONF_KEY_CRUD_PKG, Constants.DEFAULT_CRUD_PKG);
	}
	*/

	/**
	 * Start process
	 */
	public void process() {
		selectClasses();
		processClasses();
		//loadFields();
		writeFiles();
	}

	private void selectClasses() {
		String[] packageList = packages.split(CRUD_PKG_NAME_DELIM);
		for (String cls : packageList) {
			cls = cls.trim();

			try {
				if (cls.endsWith(".*")) {
					/*
					for (String c : play.libs.Classpath.getTypesAnnotatedWith(application, cls.substring(0, cls.length() - 2), Scaffoldable.class)) {
						Class<?> clazz = Class.forName(c);
						crudClasses.add(clazz);
					}
					*/

                    String pkg = cls.substring(0, cls.length() - 2);
                    Reflections reflection = new Reflections(
                            new ConfigurationBuilder()
                                    .addUrls(ClasspathHelper.forPackage(pkg, application.classloader()))
                                    .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(pkg + ".")))
                                    .setScanners(new TypeElementsScanner(), new TypeAnnotationsScanner(), new SubTypesScanner()));

                    //play.libs.Classpath.getReflections(application, pkg).setScanners(new TypeElementsScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());
					//for (Class<?> clazz : play.libs.Classpath.getTypesAnnotatedWith(application, pkg, Scaffoldable.class)) {
					for (Class<?> clazz : reflection.getTypesAnnotatedWith(Scaffoldable.class)) {
                        crudClasses.add(clazz);
					}

				} else {
					Class<?> clazz = Class.forName(cls);
					Scaffoldable sc = clazz.getAnnotation(Scaffoldable.class);
					if (sc != null)
						crudClasses.add(clazz);
				}
			} catch (ClassNotFoundException e) {
				continue;
			}
		}
	}


	private void processClasses() {
		for(Class<?> clazz: crudClasses) {
			ScaffoldableBean sb = new ScaffoldableBean();
			Scaffoldable sc = clazz.getAnnotation(Scaffoldable.class);
			if (sc == null)
				continue;

			sb.className = clazz.getSimpleName();
			sb.packageName = clazz.getPackage().getName();
			sb.label = NameProcessor.getLabel(sc.label(), sb.className);
			sb.beanFields = processFields(clazz, sb);
			beans.add(sb);
		}
	}


	private BeanField[] processFields(Class<?> clazz, ScaffoldableBean sb) {
		Field[] fields = clazz.getFields();
		List<BeanField> beanFields = new ArrayList<BeanField>();

		for (Field f : fields) {
			Scaffold sc = f.getAnnotation(Scaffold.class);
			if (sc == null)
				continue;

			BeanField bf = new BeanField();
			bf.isId = (f.getAnnotation(javax.persistence.Id.class) != null);
			bf.isVersion = (f.getAnnotation(javax.persistence.Version.class) != null);
			bf.isPrimitive = (f.getType().isPrimitive() || f.getType().getPackage().getName().equals("java.lang"));
			bf.typePackageName = bf.isPrimitive ? null : f.getType().getPackage().getName();
			bf.typeClassName = f.getType().getSimpleName();
			bf.fieldName = f.getName();
			//bf.isDropDown = isDropDown(bf.typeClassName, bf.typePackageName);
			bf.htmlElementType = (sc.htmlElementType() == HtmlElementType.NONE) ?
					HtmlElement.getHtmlElementTypeByField(f) : sc.htmlElementType();
			bf.sortOrder = sc.sortOrder();
			bf.showInCreate = sc.showInCreate();
			bf.showInEdit = sc.showInEdit();
			bf.editable = sc.editable();
			bf.showInList = sc.showInList();
			bf.showInDetails = sc.showInDetails();
			bf.label = NameProcessor.getLabel(sc.label(), bf.fieldName);
			if(bf.htmlElementType==HtmlElementType.DROP_DOWN && (sc.label()==null || "".equals(sc.label()))) {
				Class<?> refClass = f.getType();
				Scaffoldable classSc = refClass.getAnnotation(Scaffoldable.class);
				if(classSc!=null && classSc.label()!=null &&!"".equals(classSc.label()))
					bf.label = NameProcessor.getLabel(classSc.label(), bf.fieldName);
			}

            if(bf.htmlElementType == HtmlElementType.DATE_PICKER_CALENDAR || bf.htmlElementType == HtmlElementType.DATE_PICKER_DROP_DOWN) {
                DateTime dt = f.getAnnotation(DateTime.class);
                if(dt != null && dt.pattern()!=null && !dt.pattern().isEmpty())
                    bf.dateFormat = Utils.dateFormatJavaToJs(dt.pattern());

            }

            /*ManyToManyBridge mtm = f.getAnnotation(ManyToManyBridge.class);
            if(mtm != null) {
            	System.out.println("e ah a");
                bf.manyToManyBridgeClass = mtm.otherClass();
                sb.manyToManyBridgeFields.add(bf);
            }*/

            beanFields.add(bf);

			if(bf.isId) {
				sb.idFieldLabel = bf.label;
				sb.idFieldName = bf.fieldName;
				sb.idClassType = ("long".equals(bf.typeClassName) || "Long".equals(bf.typeClassName)) ? ID_TYPE_LONG : ID_TYPE_INTEGER;
			}
			else if(bf.isVersion)
				sb.versionFieldName = bf.fieldName;

			//System.out.println(bf.htmlElementType.name());
			//System.out.println(f.getAnnotations().toString());
		}

		Collections.sort(beanFields);

		return beanFields.toArray(new BeanField[beanFields.size()]);
	}


	private void writeFiles() {

		StringBuilder routeContent = new StringBuilder();
		routeContent.append(NEW_LINE);
		routeContent.append("# Automatically created route entries" + NEW_LINE);
		routeContent.append("# ===================================" + NEW_LINE);

		for(ScaffoldableBean sb: beans) {
			try {
				if (Files.notExists(sb.viewPath()))
					Files.createDirectory(sb.viewPath());
			} catch (IOException e) {
				e.printStackTrace();
			}

			CodeForHtmlViews cfhv = new CodeForHtmlViews(sb);

			// ** create page
			writeFile(cfhv.htmlCreate(), "create.scala.html", sb.viewPath());
			// ** create page
			writeFile(cfhv.htmlEdit(), "edit.scala.html", sb.viewPath());
			// ** list page
			writeFile(cfhv.htmlList(), "list.scala.html", sb.viewPath());
			// ** details page
			writeFile(cfhv.htmlDetails(), "details.scala.html", sb.viewPath());
			// ** checker queue page
			writeFile(cfhv.htmlCheckerQueue(), "checkerQueue.scala.html", sb.viewPath());
			// ** maker queue page
			writeFile(cfhv.htmlMakerQueue(), "makerQueue.scala.html", sb.viewPath());

			// ** Controller
			CodeForController cfc = new CodeForController(sb);
			String ctrlContent = cfc.getControllerBody(cfc.actionCreateForm(), cfc.actionCreate(), cfc.actionEditForm(), cfc.actionEdit(),
					cfc.actionDetails(), cfc.actionList(), cfc.actionCheckerQueueForm(), cfc.actionCheckerQueue(), cfc.actionMakerQueueForm());
			writeFile(ctrlContent, sb.controllerName() + ".java", CONTROLLER_PATH);

			// ** append to routes
			// GET    /crud-test               controllers.Application.crudTest
			routeContent.append("GET\t\t/" + NameProcessor.toUrlCase(sb.label) + "/create\t\t" + sb.controllerCreateForm() + NEW_LINE);
			routeContent.append("POST\t\t/" + NameProcessor.toUrlCase(sb.label) + "/create\t\t" + sb.controllerCreate() + NEW_LINE);
			routeContent.append("GET\t\t/" + NameProcessor.toUrlCase(sb.label) + "/edit/:id\t\t" + sb.controllerEditForm()
																+ "(id: " + Utils.scalaType(sb.idClassType) + ")" + NEW_LINE);
			routeContent.append("POST\t\t/" + NameProcessor.toUrlCase(sb.label) + "/edit\t\t" + sb.controllerEdit() + NEW_LINE);
			routeContent.append("GET\t\t/" + NameProcessor.toUrlCase(sb.label) + "/details/:id\t\t" + sb.controllerDetails()
																+ "(id: " + Utils.scalaType(sb.idClassType) + ")" + NEW_LINE);
			routeContent.append("GET\t\t/" + NameProcessor.toUrlCase(sb.label) + "/list\t\t" + sb.controllerList() + NEW_LINE);
			routeContent.append("GET\t\t/" + NameProcessor.toUrlCase(sb.label) + "/checker-queue\t\t" + sb.controllerCheckerQueueForm() + NEW_LINE);
			routeContent.append("POST\t\t/" + NameProcessor.toUrlCase(sb.label) + "/checker-queue\t\t" + sb.controllerCheckerQueue() + NEW_LINE);
			routeContent.append("GET\t\t/" + NameProcessor.toUrlCase(sb.label) + "/maker-queue\t\t" + sb.controllerMakerQueueForm() + DOUBLE_NEW_LINE);
		}

		try {
			com.google.common.io.Files.append(routeContent.toString(), ROUTES_PATH.toFile(), Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private static void writeFile(String content, String fileName, Path filePath) {
		Path path = FileSystems.getDefault().getPath(filePath.toString(), fileName);

		try {
			Files.write(path, content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private boolean isDropDown(String typeClassName, String typePackageName){
		boolean result = false;

		for(ScaffoldableBean sb: beans) {
			if(sb.className.equals(typeClassName) && sb.packageName.equals(typePackageName))
				result = true;
		}

		return result;
	}

	/**
	 * Helper method that generates the required evolution to properly run
	 * Ebean.
	 */
	/*
	public static String generateCrudScript(EbeanServer server, ServerConfig config) {
		SpiEbeanServer s = (SpiEbeanServer) server;
		for (BeanDescriptor b : s.getBeanDescriptors()) {
			System.out.println(NameProcessor.toLabelFromCamel(b.getName())); // ClassName
			// System.out.println(b.getBaseTable()); // through NameProcessor
			System.out.println("-------------------------------");

			for (com.avaje.ebeaninternal.server.deploy.BeanProperty bp : b.propertiesNonTransient()) {
				System.out.println(NameProcessor.toLabelFromCamel(bp.getName())); // fieldName
				// System.out.println(bp.getDbColumn()); // through
				// NameProcessor
			}
			System.out.println("\r\n");
		}

		// CrudGenerator cg = new CrudGenerator();
		// cg.setup((SpiEbeanServer) server, config.getDatabasePlatform(),
		// config);
		// return cg.generateCreateDdl();
		return "Test";
	}
	*/

}
