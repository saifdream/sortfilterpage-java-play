package com.play2.crud.beanprocessor;

import static com.play2.crud.helper.Constants.*;

import java.lang.Class;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.play2.crud.enumeration.HtmlElementType;
import com.play2.crud.helper.Constants;
import com.play2.crud.helper.NameProcessor;


public class ScaffoldableBean {
	
	public String packageName;
	public String className;
	public String label;
	public String form;
    public String idClassType = Constants.ID_TYPE_INTEGER;
	public String idFieldLabel;
	public String idFieldName;
	public String versionFieldName;
    public List<BeanField> manyToManyBridgeFields = new ArrayList<>();
	public BeanField[] beanFields;
	
	public String fullClassName(){
		return packageName + "." + className;
	}

    public String dummyClassName(){
		return Constants.DUMMY_MODEL_CLASS_PREFIX + className;
	}

    public String fullDummyClassName(){
		return Constants.DUMMY_MODEL_PACKAGE_NAME + "." + dummyClassName();
	}

	public String controllerName(){
		return className + "Controller";
	}
	
	public String routesCreateForm(){
		return ROUTES + "." + controllerName() + "." + ACTION_CREATE_FORM;
	}
	
	public String routesCreate(){
		return ROUTES + "." + controllerName() + "." + ACTION_CREATE;
	}
	
	public String routesEditForm(){
		return ROUTES + "." + controllerName() + "." + ACTION_EDIT_FORM;
	}
	
	public String routesEdit(){
		return ROUTES + "." + controllerName() + "." + ACTION_EDIT;
	}
	
	public String routesDetails(){
		return ROUTES + "." + controllerName() + "." + ACTION_DETAILS;
	}
	
	public String routesList(){
		return ROUTES + "." + controllerName() + "." + ACTION_LIST;
	}

	public String routesCheckerQueue(){
		return ROUTES + "." + controllerName() + "." + ACTION_CHECKER_QUEUE;
	}

	public String controllerCreateForm(){
		return CONTROLLERS + "." + controllerName() + "." + ACTION_CREATE_FORM;
	}
	
	public String controllerCreate(){
		return CONTROLLERS + "." + controllerName() + "." + ACTION_CREATE;
	}
	
	public String controllerEditForm(){
		return CONTROLLERS + "." + controllerName() + "." + ACTION_EDIT_FORM;
	}
	
	public String controllerEdit(){
		return CONTROLLERS + "." + controllerName() + "." + ACTION_EDIT;
	}
	
	public String controllerDetails(){
		return CONTROLLERS + "." + controllerName() + "." + ACTION_DETAILS;
	}
	
	public String controllerList(){
		return CONTROLLERS + "." + controllerName() + "." + ACTION_LIST;
	}

	public String controllerCheckerQueueForm(){
		return CONTROLLERS + "." + controllerName() + "." + ACTION_CHECKER_QUEUE_FORM;
	}

	public String controllerCheckerQueue(){
		return CONTROLLERS + "." + controllerName() + "." + ACTION_CHECKER_QUEUE;
	}

	public String controllerMakerQueueForm(){
		return CONTROLLERS + "." + controllerName() + "." + ACTION_MAKER_QUEUE_FORM;
	}

	public String formName() {
		return NameProcessor.getInitials(className) + "Form";
	}
	
	public Path viewPath() {
		return FileSystems.getDefault().getPath(BASE_PATH.toString(), "app", "views", NameProcessor.toCamelCase(className));
	}
	
	public String viewPackage() {
		return "views.html." + NameProcessor.toCamelCase(className);
	}
	
	
	public BeanField[] dropDownBeanFields() {
		List<BeanField> bfList = new ArrayList<>();
		for (BeanField bf: beanFields) {
			if(bf.htmlElementType == HtmlElementType.DROP_DOWN)
				bfList.add(bf);
		}
		
		return bfList.toArray(new BeanField[bfList.size()]);
	}

    public String getManyToManyRoutes(BeanField bf) {
        if(bf.manyToManyBridgeClass==null || bf.manyToManyBridgeClass.isEmpty())
            return "#";

        return "@" + ROUTES + "." + bf.typeClassName + "Controller.show" + bf.manyToManyBridgeClass + "OfSingle" + this.className+ "EditForm()";
    }

}
