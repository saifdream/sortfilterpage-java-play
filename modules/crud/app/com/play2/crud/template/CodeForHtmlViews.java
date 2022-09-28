package com.play2.crud.template;

import com.play2.crud.beanprocessor.BeanField;
import com.play2.crud.beanprocessor.ScaffoldableBean;
import com.play2.crud.enumeration.HtmlElementType;
import com.play2.crud.helper.NameProcessor;

import java.lang.Double;

import static com.play2.crud.helper.Constants.*;


public class CodeForHtmlViews extends CrudView {

	public CodeForHtmlViews(ScaffoldableBean sb) {
		super(sb);
	}

	@Override
	public String htmlCreate() {

		StringBuilder html = new StringBuilder();
		//html.append("@(" + sb.formName() + ": play.api.data.Form[" + sb.fullClassName() + "])\r\n");
		html.append("@(" + sb.formName() + ": play.data.Form[" + sb.fullDummyClassName() + "])" + DOUBLE_NEW_LINE);
		//html.append("@import " + sb.fullClassName()  + NEW_LINE);
		//html.append("@import helper._" + NEW_LINE);
		//html.append("@import helper.twitterBootstrap._" + DOUBLE_NEW_LINE);
		//html.append("@import tags._" + NEW_LINE);
		//html.append("@import helper.b3.vertical.fieldConstructor" + NEW_LINE);
		html.append("@import helper.b3._" + NEW_LINE);
		html.append("@implicitConstructor = @{ helper.b3.horizontal.fieldConstructor(\"col-md-4\", \"col-md-6\") }" + DOUBLE_NEW_LINE);
		//html.append("@implicitField = @{ helper.FieldConstructor(bootstrap3.bootstrapFieldConstructorTemplate.f) }" + DOUBLE_NEW_LINE);
		html.append("@main(\"Create new " + sb.label + "\"){"  + NEW_LINE);
		html.append("  @form(" + sb.routesCreate() + "){" + NEW_LINE);
		for(BeanField bf: sb.beanFields) {
			if(bf.showInCreate && !bf.isId && !bf.isVersion)
				html.append("    " + bf.getFieldHtml(sb.formName()) + NEW_LINE);
		}
        //html.append(NEW_LINE + "    <br />" + NEW_LINE);
		//html.append("    <input type=\"submit\" class=\"btn btn-primary\" value=\"Create\">" + NEW_LINE);
		//html.append(NEW_LINE + "    <hr class=\"hr\">" + NEW_LINE);
		html.append(NEW_LINE + "    <br>" + NEW_LINE);
		html.append("    <div class=\"col-md-6 col-md-offset-3\">" + NEW_LINE);
		html.append("      <input type=\"submit\" class=\"btn btn-sm btn-block btn-info\" value=\"Create\">" + NEW_LINE);
		html.append("    </div>" + NEW_LINE);
		html.append("    <br>" + NEW_LINE);
		html.append("  }" + NEW_LINE);
		html.append("}");

		return html.toString();
	}


	@Override
	public String htmlEdit() {
		StringBuilder html = new StringBuilder();
		//html.append("@(" + sb.formName() + ": play.data.Form[" + sb.fullClassName() + "])" + DOUBLE_NEW_LINE);
		html.append("@(" + sb.formName() + ": Form[" + sb.fullDummyClassName() + "])" + DOUBLE_NEW_LINE);
		//html.append("@import " + sb.fullClassName()  + NEW_LINE);
        html.append("@import services.utils.SUtils.formatRemarksForEdit" + NEW_LINE);
        html.append("@import helper.b3._" + NEW_LINE);
        html.append("@implicitConstructor = @{ helper.b3.horizontal.fieldConstructor(\"col-md-4\", \"col-md-6\") }" + DOUBLE_NEW_LINE);
		html.append("@main(\"Edit " + sb.label + "\"){"  + NEW_LINE);
		html.append("  @form(" + sb.routesEdit() + "){" + NEW_LINE);

		html.append("    @hiddens((\"" + sb.idFieldName + "\", " + sb.formName() + "(\"" + sb.idFieldName + "\").value)");
		if(sb.versionFieldName!=null && !sb.versionFieldName.isEmpty())
			html.append(", (\"" + sb.versionFieldName + "\", " + sb.formName() + "(\"" + sb.versionFieldName+ "\").value)");
		html.append(")" + NEW_LINE);

		for(BeanField bf: sb.beanFields) {
			if(bf.showInEdit && !bf.isId && !bf.isVersion) {
				if (bf.editable)
					html.append("    " + bf.getFieldHtml(sb.formName()) + NEW_LINE);
				else
					html.append("    " + bf.getFieldHtmlAsComment(sb.formName(), "    ") + NEW_LINE);
			}
		}
        html.append("    @formComment(" + sb.formName() + "(\"existingRemarks\"), '_label->\"Previous Remarks\", 'value -> formatRemarksForEdit(" + sb.formName() + "(\"existingRemarks\").value) )" + NEW_LINE);
        //html.append("    @textarea(" + sb.formName() + "(\"remarks\"), '_label->\"Remarks\")" + NEW_LINE);
		//html.append(NEW_LINE + "    <br />" + NEW_LINE);
		//html.append("    <input type=\"submit\" class=\"btn btn-primary\" value=\"Update\">" + NEW_LINE);
		html.append(NEW_LINE + "    <hr class=\"hr\">" + NEW_LINE);
		html.append("    <div class=\"col-md-6 col-md-offset-3\">" + NEW_LINE);
		html.append("      <input type=\"submit\" class=\"btn btn-sm btn-block btn-primary\" value=\"Update\">" + NEW_LINE);
		html.append("    </div>" + NEW_LINE);
		html.append("    <br>" + NEW_LINE);
		html.append("  }" + DOUBLE_NEW_LINE);

        for(BeanField bf : sb.manyToManyBridgeFields) {
            //html.append("    <input type=\"submit\" class=\"btn btn-primary\" value=\"" + bf.label + "\">" + NEW_LINE);
            html.append("    <a href=\"@" + sb.getManyToManyRoutes(bf) + "\" class=\"btn btn-primary active\" role=\"button\">" + "" + "</a>" + NEW_LINE);
			html.append("    <br>" + NEW_LINE);
        }
		html.append("}");

		return html.toString();
	}


	@Override
	public String htmlList() {
		String obj = NameProcessor.getInitials(sb.className);
		String list = obj + "List";

		StringBuilder html = new StringBuilder();
		html.append("@(" + list + ": List[" + sb.fullClassName() + "])" + DOUBLE_NEW_LINE);
		html.append("@import services.utils.JUtils" + NEW_LINE);
		html.append("@import services.utils.SUtils.formatRemarksForView" + NEW_LINE);
		html.append("@import " + sb.fullClassName() + DOUBLE_NEW_LINE);
		//html.append("@import helper._" + NEW_LINE);
		//html.append("@import helper.twitterBootstrap._" + DOUBLE_NEW_LINE);
		//html.append("@implicitField = @{ FieldConstructor(bootstrap3.bootstrapFieldConstructorTemplate.f) }" + DOUBLE_NEW_LINE);
		html.append("@main(\"" + sb.label + " List\"){" + NEW_LINE);
		html.append("  <table class=\"table table-striped table-bordered\">" + NEW_LINE);
		html.append("    <tr>" + NEW_LINE);
		html.append("      <th>#</th>" + NEW_LINE);
		html.append("      <th>" + sb.idFieldLabel + "</th>" + NEW_LINE);
		for (BeanField bf : sb.beanFields) {
			if(bf.showInList && !bf.isId && !bf.isVersion)
				html.append("      <th>" + bf.label + "</th>" + NEW_LINE);
		}
		html.append("    </tr>" + NEW_LINE);

		html.append("    @for((" + obj + ", idx) <- " + list + ".zipWithIndex){" + NEW_LINE);
		html.append("      <tr>" + NEW_LINE);
		html.append("        <td>@(idx+1)</td>" + NEW_LINE);
        html.append("        <td><a href=\"@" + sb.routesDetails() + "(" + obj + "." + sb.idFieldName + ")\">@" + obj + "." + sb.idFieldName + "</a></td>" + NEW_LINE);
		for (BeanField bf : sb.beanFields) {
			if(bf.showInList && !bf.isId && !bf.isVersion) {
				if(bf.htmlElementType == HtmlElementType.CHECK_BOX) {
					html.append("        <td><input type=\"checkbox\" disabled @if(" + obj + "." + bf.fieldName + "){checked} /></td>" + NEW_LINE);
				} else if(bf.htmlElementType == HtmlElementType.DATE_PICKER_CALENDAR || bf.htmlElementType == HtmlElementType.DATE_PICKER_DROP_DOWN) {
					html.append("        <td>@JUtils.formatDate(" + obj + "." + bf.fieldName + ")</td>" + NEW_LINE);
				} else if("remarks".equals(bf.fieldName)) {
					html.append("@formatRemarksForView(" + obj + "." + bf.fieldName + ")");
				} else if("approvalStage".equals(bf.fieldName)) {
					html.append("@services.ApprovalStage.getDescription(" + obj + "." + bf.fieldName + ")");
				} else {
					html.append("        <td>@" + obj + "." + bf.fieldName + "</td>" + NEW_LINE);
				}
			}
		}
		html.append("      </tr>" + NEW_LINE);
		html.append("    }" + NEW_LINE);
		html.append("  </table>" + NEW_LINE);
		html.append("}");

		return html.toString();
	}


	@Override
	public String htmlDetails() {
		String obj = NameProcessor.getInitials(sb.className);

		StringBuilder html = new StringBuilder();
		html.append("@(" + obj + ": " + sb.fullClassName() + ")" + DOUBLE_NEW_LINE);
		html.append("@import services.utils.JUtils" + NEW_LINE);
		html.append("@import services.utils.SUtils.formatRemarksForView" + NEW_LINE);
		html.append("@import " + sb.fullClassName() + DOUBLE_NEW_LINE);
		//html.append("@import helper._\r\n");
		//html.append("@import helper.twitterBootstrap._" + DOUBLE_NEW_LINE);
		//html.append("@implicitField = @{ FieldConstructor(bootstrap3.bootstrapFieldConstructorTemplate.f) }" + DOUBLE_NEW_LINE);
		html.append("@main(\"Details of " + sb.label + "\"){" + NEW_LINE);
		html.append("  <table class=\"table table-bordered\">" + NEW_LINE);
		for (BeanField bf : sb.beanFields) {
			if(bf.showInDetails && !bf.isId && !bf.isVersion) {
				html.append("    <tr><td>" + bf.label + "</td><td>");
				if(bf.htmlElementType == HtmlElementType.CHECK_BOX) {
					html.append("<input type=\"checkbox\" disabled @if(" + obj + "." + bf.fieldName + "){checked} />");
				} else if(bf.htmlElementType == HtmlElementType.DATE_PICKER_CALENDAR || bf.htmlElementType == HtmlElementType.DATE_PICKER_DROP_DOWN) {
					html.append("@JUtils.formatDate(" + obj + "." + bf.fieldName + ")");
				} else if("remarks".equals(bf.fieldName)) {
					html.append("@formatRemarksForView(" + obj + "." + bf.fieldName + ")");
				} else if("approvalStage".equals(bf.fieldName)) {
					html.append("@services.ApprovalStage.getDescription(" + obj + "." + bf.fieldName + ")");
				} else {
					html.append("@" + obj + "." + bf.fieldName);
				}

				html.append("</td></tr>" + NEW_LINE);
			}
		}
		html.append("  </table>" + NEW_LINE);

		html.append("  <br>" + DOUBLE_NEW_LINE);
		html.append("  <div class=\"col-md-6 col-md-offset-3\">" + NEW_LINE);
		html.append("    <a href=\"@" + sb.routesEditForm() + "(" + obj + "." + sb.idFieldName + ")\" class=\"btn btn-sm btn-block btn-primary active\" role=\"button\">Edit</a>" + NEW_LINE);
		html.append("  </div>" + NEW_LINE);
		html.append("  <br>" + DOUBLE_NEW_LINE);

		html.append("  @if(" + obj + ".isInCheckerQueue){" + NEW_LINE);
		html.append("    <hr class=\"hr\">" + NEW_LINE);
		html.append("    <form action=\"@" + sb.routesCheckerQueue() + "\" id=\"approval-form\" method=\"post\">" + NEW_LINE);
		html.append("    <input type=\"hidden\"  name=\"rowId[]\" id=\"rowId\" value=\"@" + obj + "." + sb.idFieldName + "\">" + NEW_LINE);
		html.append("    @views.html.template.checkerAction.render()" + NEW_LINE);
		html.append("  }" + DOUBLE_NEW_LINE);
		html.append("}");

		return html.toString();
	}

	@Override
	public String htmlCheckerQueue() {
		String obj = NameProcessor.getInitials(sb.className);
		String list = obj + "List";

		StringBuilder html = new StringBuilder();
		html.append("@(" + list + ": List[" + sb.fullClassName() + "])" + DOUBLE_NEW_LINE);
		html.append("@import services.utils.JUtils" + NEW_LINE);
		html.append("@import services.utils.SUtils.formatRemarksForView" + NEW_LINE);
		html.append("@import " + sb.fullClassName() + DOUBLE_NEW_LINE);
		html.append("@main(\"" + sb.label + " Checker Queue\"){" + NEW_LINE);
		html.append("  <form action=@" + sb.routesCheckerQueue() + " id=\"approval-form\" method=\"post\">" + NEW_LINE);
		html.append("  <table class=\"table table-striped table-bordered\">" + NEW_LINE);
		html.append("    <tr>" + NEW_LINE);
		html.append("      <th><input type=\"checkbox\" name=\"select-all\" id=\"select-all\"></th>" + NEW_LINE);
		html.append("      <th>#</th>" + NEW_LINE);
		html.append("      <th>" + sb.idFieldLabel + "</th>" + NEW_LINE);
		for (BeanField bf : sb.beanFields) {
			if(bf.showInList && !bf.isId && !bf.isVersion)
				html.append("      <th>" + bf.label + "</th>" + NEW_LINE);
		}
		html.append("      <th>Remarks</th>" + NEW_LINE);
		html.append("    </tr>" + NEW_LINE);

		html.append("    @for((" + obj + ", idx) <- " + list + ".zipWithIndex){" + NEW_LINE);
		html.append("      <tr>" + NEW_LINE);
		html.append("        <td><input type=\"checkbox\" name=\"rowId[]\" id=\"rowId\" value=\"@" + obj + "." + sb.idFieldName + "\"></td>" + NEW_LINE);
		html.append("        <td>@(idx+1)</td>" + NEW_LINE);
		html.append("        <td><a href=\"@" + sb.routesEditForm() + "(" + obj + "." + sb.idFieldName + ")\">@" + obj + "." + sb.idFieldName + "</a></td>" + NEW_LINE);
		for (BeanField bf : sb.beanFields) {
			if(bf.showInList && !bf.isId && !bf.isVersion) {
				if(bf.htmlElementType == HtmlElementType.CHECK_BOX) {
					html.append("        <td><input type=\"checkbox\" disabled @if(" + obj + "." + bf.fieldName + "){checked} /></td>" + NEW_LINE);
				} else if(bf.htmlElementType == HtmlElementType.DATE_PICKER_CALENDAR || bf.htmlElementType == HtmlElementType.DATE_PICKER_DROP_DOWN) {
					html.append("        <td>@JUtils.formatDate(" + obj + "." + bf.fieldName + ")</td>" + NEW_LINE);
				} else {
					html.append("        <td>@" + obj + "." + bf.fieldName + "</td>" + NEW_LINE);
				}
			}
		}
		html.append("        <td>@formatRemarksForView(" + obj + ".remarks)</td>" + NEW_LINE);
		html.append("      </tr>" + NEW_LINE);
		html.append("    }" + NEW_LINE);
		html.append("  </table>" + NEW_LINE);

		html.append("  <hr class=\"hr\">" + NEW_LINE);
		html.append("  @views.html.template.checkerAction.render()" + NEW_LINE);

		html.append("}");

		return html.toString();
	}

	@Override
	public String htmlMakerQueue() {
		String obj = NameProcessor.getInitials(sb.className);
		String list = obj + "List";

		StringBuilder html = new StringBuilder();
		html.append("@(" + list + ": List[" + sb.fullClassName() + "])" + DOUBLE_NEW_LINE);
		html.append("@import services.utils.JUtils" + NEW_LINE);
		html.append("@import services.utils.SUtils.formatRemarksForView" + NEW_LINE);
		html.append("@import " + sb.fullClassName() + DOUBLE_NEW_LINE);
		html.append("@main(\"" + sb.label + " Maker Queue\"){" + NEW_LINE);
		html.append("  <table class=\"table table-striped table-bordered\">" + NEW_LINE);
		html.append("    <tr>" + NEW_LINE);
		html.append("      <th>#</th>" + NEW_LINE);
		html.append("      <th>" + sb.idFieldLabel + "</th>" + NEW_LINE);
		for (BeanField bf : sb.beanFields) {
			if(bf.showInList && !bf.isId && !bf.isVersion)
				html.append("      <th>" + bf.label + "</th>" + NEW_LINE);
		}
		html.append("      <th>Remarks</th>" + NEW_LINE);
		html.append("    </tr>" + NEW_LINE);

		html.append("    @for((" + obj + ", idx) <- " + list + ".zipWithIndex){" + NEW_LINE);
		html.append("      <tr>" + NEW_LINE);
		html.append("        <td>@(idx+1)</td>" + NEW_LINE);
		html.append("        <td><a href=\"@" + sb.routesEditForm() + "(" + obj + "." + sb.idFieldName + ")\">@" + obj + "." + sb.idFieldName + "</a></td>" + NEW_LINE);
		for (BeanField bf : sb.beanFields) {
			if(bf.showInList && !bf.isId && !bf.isVersion) {
				if(bf.htmlElementType == HtmlElementType.CHECK_BOX) {
					html.append("        <td><input type=\"checkbox\" disabled @if(" + obj + "." + bf.fieldName + "){checked} /></td>" + NEW_LINE);
				} else if(bf.htmlElementType == HtmlElementType.DATE_PICKER_CALENDAR || bf.htmlElementType == HtmlElementType.DATE_PICKER_DROP_DOWN) {
					html.append("        <td>@JUtils.formatDate(" + obj + "." + bf.fieldName + ")</td>" + NEW_LINE);
				} else {
					html.append("        <td>@" + obj + "." + bf.fieldName + "</td>" + NEW_LINE);
				}
			}
		}
		html.append("        <td>@formatRemarksForView(" + obj + ".remarks)</td>" + NEW_LINE);
		html.append("      </tr>" + NEW_LINE);
		html.append("    }" + NEW_LINE);
		html.append("  </table>" + NEW_LINE);
		html.append("}");

		return html.toString();
	}

	@Override
	public String routeCreate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String routeEdit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String routeList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String routeDetails() {
		// TODO Auto-generated method stub
		return null;
	}

}
