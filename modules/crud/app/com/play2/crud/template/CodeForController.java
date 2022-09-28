package com.play2.crud.template;

import play.data.Form;

import com.play2.crud.beanprocessor.BeanField;
import com.play2.crud.beanprocessor.ScaffoldableBean;
import com.play2.crud.helper.NameProcessor;

import static com.play2.crud.helper.Constants.*;

public class

        CodeForController extends CrudController {

	private String obj;

	public CodeForController(ScaffoldableBean sb) {
		super(sb);

		obj = NameProcessor.getInitials(sb.className);
	}

	@Override
	public String actionCreateForm() {
		StringBuilder actn = new StringBuilder();

		actn.append("\tpublic static Result " + ACTION_CREATE_FORM + " {" + NEW_LINE);
		actn.append("\t\treturn ok(" + sb.viewPackage() + ".create.render(" + sb.formName() + "));" + NEW_LINE);
		actn.append("\t}");

		return actn.toString();
	}

	@Override
	public String actionCreate() {
		StringBuilder actn = new StringBuilder();

		actn.append("\tpublic static Result " + ACTION_CREATE + " {" + NEW_LINE);
		actn.append("\t\tForm<" + sb.dummyClassName() + "> frm = " + sb.formName() + ".bindFromRequest();" + DOUBLE_NEW_LINE);
		actn.append("\t\tif (frm.hasErrors()) {" + NEW_LINE);
		actn.append("\t\t\treturn badRequest(" + sb.viewPackage() + ".create.render(frm));" + NEW_LINE);
		actn.append("\t\t} else {" + NEW_LINE);
		actn.append("\t\t\t" + sb.className + " " + obj + " = frm.get().createReal();" + NEW_LINE);
		actn.append("\t\t\t" + obj + ".insert();" + DOUBLE_NEW_LINE);
		actn.append("\t\t\tflash(FLASH_KEY_SUCCESS, \"A new " + sb.label + " has been created successfully.\");" + NEW_LINE);
		actn.append("\t\t\treturn redirect(" + sb.routesDetails() + "(" + obj + ".id));" + DOUBLE_NEW_LINE);
		actn.append("\t\t}" + NEW_LINE);
		actn.append("\t}");

		return actn.toString();
	}

	@Override
	public String actionEditForm() {
		StringBuilder actn = new StringBuilder();

		actn.append("\tpublic static Result " + ACTION_EDIT_FORM + "(" + sb.idClassType + " id) {" + NEW_LINE);
		actn.append("\t\t" + sb.className + " " + obj + " = " + sb.className + ".get(id);" + NEW_LINE);
		actn.append("\t\tif(" + obj + " == null) {" + NEW_LINE);
		actn.append("\t\t\treturn notFoundError();" + NEW_LINE);
        actn.append("\t\t};" + DOUBLE_NEW_LINE);
		actn.append("\t\treturn ok(" + sb.viewPackage() + ".edit.render(" + sb.formName() + ".fill(" + sb.dummyClassName() + ".fromReal(" + obj + "))));" + NEW_LINE);
		actn.append("\t}");

		return actn.toString();
	}

	@Override
	public String actionEdit() {
		StringBuilder actn = new StringBuilder();

		actn.append("\tpublic static Result " + ACTION_EDIT + " {" + NEW_LINE);
		actn.append("\t\tForm<" + sb.dummyClassName() + "> frm = " + sb.formName() + ".bindFromRequest();" + DOUBLE_NEW_LINE);
		actn.append("\t\tif (frm.hasErrors()) {" + NEW_LINE);
		actn.append("\t\t\treturn badRequest(" + sb.viewPackage() + ".edit.render(frm));" + NEW_LINE);
		actn.append("\t\t} else {" + NEW_LINE);
		actn.append("\t\t\t" + sb.dummyClassName() + " frmObj = frm.get();" + NEW_LINE);
        actn.append("\t\t\t" + sb.className + " " + obj + ";" + NEW_LINE);
        actn.append("\t\t\ttry {" + NEW_LINE);
        actn.append("\t\t\t\t" + obj + " = frmObj.toReal();" + NEW_LINE);
        actn.append("\t\t\t} catch (javax.persistence.OptimisticLockException e) {" + NEW_LINE);
        actn.append("\t\t\t\tflash(FLASH_KEY_ERROR, \"The " + sb.label + " is changed by another user\");" + NEW_LINE);
        actn.append("\t\t\t\treturn redirect(" + sb.routesList() + ");" + NEW_LINE);
        actn.append("\t\t\t}" + DOUBLE_NEW_LINE);
        actn.append("\t\t\tif(" + obj + " == null) {" + NEW_LINE);
        actn.append("\t\t\t\treturn notFoundError();" + NEW_LINE);
        actn.append("\t\t\t}" + DOUBLE_NEW_LINE);
        actn.append("\t\t\t"+ obj +".isActive = false;" + NEW_LINE);
        actn.append("\t\t\t"+ obj + ".isApproved = false;" + NEW_LINE);
        actn.append("\t\t\t"+ obj +".approvalStage = ("+ obj +".approvalStage == ApprovalStage.RESENT_TO_MAKER) ?" + NEW_LINE);
        actn.append("\t\t\t\t\tApprovalStage.RESENT_TO_CHECKER : ApprovalStage.SENT_TO_CHECKER;" + NEW_LINE);
        actn.append("\t\t\t"+ obj +".lastUpdatedBy = JUtils.getCurrentUsername();" + NEW_LINE);
        actn.append("\t\t\t"+ obj +".lastUpdateDate = new Date();" + DOUBLE_NEW_LINE);
		actn.append("\t\t\t" + obj + ".update();" + DOUBLE_NEW_LINE);
		actn.append("\t\t\tflash(\"success\", \"The " + sb.label + " has been updated successfully.\");" + NEW_LINE);
		actn.append("\t\t\treturn redirect(" + sb.routesDetails() + "(" + obj+ ".id));" + DOUBLE_NEW_LINE);
		actn.append("\t\t}" + NEW_LINE);
		actn.append("\t}");

		return actn.toString();
	}

	@Override
	public String actionList() {
		StringBuilder actn = new StringBuilder();

		actn.append("\tpublic static Result " + ACTION_LIST + " {" + NEW_LINE);
		actn.append("\t\treturn ok(" + sb.viewPackage() + ".list.render(" + sb.className + ".all()));" + NEW_LINE);
		actn.append("\t}");

		return actn.toString();
	}

	@Override
	public String actionDetails() {
		StringBuilder actn = new StringBuilder();

		actn.append("\tpublic static Result " + ACTION_DETAILS + "(" + sb.idClassType + " id) {" + NEW_LINE);
		actn.append("\t\t" + sb.className + " " + obj + " = " + sb.className + ".get(id);" + NEW_LINE);
        actn.append("\t\tif(" + obj + " == null) {" + NEW_LINE);
        actn.append("\t\t\treturn notFoundError();" + NEW_LINE);
        actn.append("\t\t}" + DOUBLE_NEW_LINE);
		actn.append("\t\treturn ok(" + sb.viewPackage() + ".details.render(" + obj + "));" + NEW_LINE);
		actn.append("\t}");

		return actn.toString();
	}

    @Override
	public String actionCheckerQueueForm() {
		StringBuilder actn = new StringBuilder();

		actn.append("\tpublic static Result " + ACTION_CHECKER_QUEUE_FORM + " {" + NEW_LINE);
        actn.append("\t\treturn ok(" + sb.viewPackage() + ".checkerQueue.render(" + sb.className + ".checkerQueueList()));" + NEW_LINE);
        actn.append("\t}");

        return actn.toString();
	}

    @Override
	public String actionCheckerQueue() {
		StringBuilder actn = new StringBuilder();

        actn.append("\tpublic static Result " + ACTION_CHECKER_QUEUE + "{" + NEW_LINE);
        actn.append("\t\tForm<IdList> frm = listForm.bindFromRequest();" + DOUBLE_NEW_LINE);
        actn.append("\t\tif (frm.hasErrors()) {" + NEW_LINE);
        actn.append("\t\t\treturn badRequest(" + sb.viewPackage() + ".checkerQueue.render(" + sb.className + ".checkerQueueList()));" + NEW_LINE);
        actn.append("\t\t} else {" + NEW_LINE);
        actn.append("\t\t\tIdList idList = frm.get();" + NEW_LINE);
        actn.append("\t\t\tfor(Integer id : idList.rowId) {" + NEW_LINE);
        actn.append("\t\t\t\t" + sb.className + " " + obj + " = " + sb.className + ".get(id);" + NEW_LINE);
        actn.append("\t\t\t\tif (" + obj + "!=null){" + NEW_LINE);
        actn.append("\t\t\t\t\t" + obj + ".approvalStage = idList.actionType;" + NEW_LINE);
        actn.append("\t\t\t\t\t" + obj + ".remarks = JUtils.genApprovalRemarks(idList.remarks, " + obj + ".remarks);" + DOUBLE_NEW_LINE);
        actn.append("\t\t\t\t\tif(idList.actionType == ApprovalStage.CHECKER_APPROVED){" + NEW_LINE);
        actn.append("\t\t\t\t\t\t" + obj + ".isApproved = true;" + NEW_LINE);
        actn.append("\t\t\t\t\t\t" + obj + ".isActive = true;" + NEW_LINE);
        actn.append("\t\t\t\t\t\t" + obj + ".approvedBy = JUtils.getCurrentUsername();" + NEW_LINE);
        actn.append("\t\t\t\t\t\t" + obj + ".approvalDate = new Date();" + NEW_LINE);
        actn.append("\t\t\t\t\t} else if(ApprovalStage.SENT_BACK_TO_MAKER==idList.actionType && ApprovalStage.RESENT_TO_CHECKER==" + obj + ".approvalStage){" + NEW_LINE);
        actn.append("\t\t\t\t\t\t" + obj + ".approvalStage = ApprovalStage.RESENT_TO_MAKER;" + NEW_LINE);
        actn.append("\t\t\t\t\t}" + DOUBLE_NEW_LINE);
        actn.append("\t\t\t\t\t" + obj + ".update();" + NEW_LINE);
        actn.append("\t\t\t\t}" + NEW_LINE);
        actn.append("\t\t\t}" + NEW_LINE);
        actn.append("\t\t}" + DOUBLE_NEW_LINE);
        actn.append("\t\treturn redirect(" + sb.routesList() + ");" + NEW_LINE);
        actn.append("\t}");

        return actn.toString();
	}

    @Override
	public String actionMakerQueueForm() {
		StringBuilder actn = new StringBuilder();

		actn.append("\tpublic static Result " + ACTION_MAKER_QUEUE_FORM + " {" + NEW_LINE);
        actn.append("\t\treturn ok(" + sb.viewPackage() + ".makerQueue.render(" + sb.className + ".makerQueueList()));" + NEW_LINE);
        actn.append("\t}");

        return actn.toString();
	}


	@Override
	public String getControllerBody(String... actions) {
		StringBuilder ctrl = new StringBuilder();

		ctrl.append("package controllers;" + DOUBLE_NEW_LINE);
		ctrl.append("import java.util.Date; " + DOUBLE_NEW_LINE);
		ctrl.append("import play.*; " + NEW_LINE);
		ctrl.append("import play.mvc.*;" + NEW_LINE);
		ctrl.append("import play.data.*;" + DOUBLE_NEW_LINE);
        ctrl.append("import services.ApprovalStage;" + NEW_LINE);
        ctrl.append("import services.utils.JUtils;" + NEW_LINE);
        ctrl.append("import static services.settings.AppConst.FLASH_KEY_ERROR;" + NEW_LINE);
        ctrl.append("import static services.settings.AppConst.FLASH_KEY_SUCCESS;" + DOUBLE_NEW_LINE);
        ctrl.append("import models.dummy.IdList;" + NEW_LINE);
        ctrl.append("import " + sb.fullDummyClassName() + ";" + NEW_LINE);
		ctrl.append("import " + sb.fullClassName() + ";" + TRIPPLE_NEW_LINE);
		ctrl.append("public class " + sb.controllerName() + " extends Controller {" + DOUBLE_NEW_LINE);
		//ctrl.append("static Form<" + sb.className + "> " + sb.formName() + " = Form.form(" + sb.className + ".class);" + DOUBLE_NEW_LINE);
		ctrl.append("\tstatic Form<" + sb.dummyClassName() + "> " + sb.formName() + " = Form.form(" + sb.dummyClassName() + ".class);" + NEW_LINE);
		ctrl.append("\tstatic Form<IdList> listForm = Form.form(IdList.class);" + DOUBLE_NEW_LINE);
		for (String a : actions) {
			ctrl.append(a + TRIPPLE_NEW_LINE);
		}


        ctrl.append("// ** used when the object's ID is not found in DB for edit, detail, etc." + NEW_LINE);
        ctrl.append("\tprivate static Result notFoundError(){" + NEW_LINE);
        ctrl.append("\t\tflash(FLASH_KEY_ERROR, \"The " + sb.label + " could not be found\");" + NEW_LINE);
        ctrl.append("\t\treturn redirect(" + sb.routesList() + ");" + NEW_LINE);
        ctrl.append("\t}");

		ctrl.append("}");

		return ctrl.toString();
	}

}
