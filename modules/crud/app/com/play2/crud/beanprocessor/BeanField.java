package com.play2.crud.beanprocessor;

import static com.play2.crud.helper.Constants.*;

import com.play2.crud.enumeration.HtmlElementType;
import com.play2.crud.helper.NameProcessor;

import java.lang.String;


public class BeanField implements Comparable<BeanField>{

	public boolean isId;
	public boolean isVersion;
    public String manyToManyBridgeClass;
	public String typePackageName;
	public String typeClassName;
	public String fieldName;
	public String label;
	public HtmlElementType htmlElementType;
	public boolean isPrimitive;
    public String dateFormat;
	//public boolean isDropDown;
	public float sortOrder = 0;
	public boolean showInCreate = true;
	public boolean showInEdit = true;
	public boolean editable = true;
	public boolean showInList = true;
	public boolean showInDetails = true;
	public boolean retainInDummy = true;
	public boolean isString = true;

	//public boolean hasDatePicker;

	public String getFieldHtml(String formName) {
		switch (htmlElementType) {
		case NONE:
			return intuitiveHtml(formName);
		case TEXT_BOX:
			return textBoxHtml(formName);
		case TEXT_AREA:
			return textAreaHtml(formName);
		case DROP_DOWN:
			return dropDownHtml(formName);
		case CHECK_BOX:
			return checkBoxHtml(formName);
		case DATE_PICKER_CALENDAR:
			return datePickerCalendarHtml(formName);
		case DATE_PICKER_DROP_DOWN:
			return datePickerDropDownHtml(formName);
		case PASSWORD:
			return passwordDownHtml(formName);
		case NUMBER:
			return numberHtml(formName);
		default:
			return textBoxHtml(formName);
		}
	}

	public String getFieldHtmlAsComment(String formName, String space) {
		return "@formComment(" + formName + "(\"" + fieldName + "\"), '_label->\"" + label + "\") " + NEW_LINE
				+ space + "@hidden(\"" + fieldName + "\", " + formName + "(\"" + fieldName + "\").value)";
	}

	// ** HTML for various input types **//
	private String textBoxHtml(String formName) {
		//return "@inputText(" + formName + "(\"" + fieldName + "\"), args='_label->\"" + label + "\", 'class -> \"form-control\")";
		//@b3.text( fooForm("bar"), '_label -> "Bar", 'placeholder -> "Bar placeholder..." )
		return "@text(" + formName + "(\"" + fieldName + "\"), '_label->\"" + label + "\")";
	}

	private String passwordDownHtml(String formName) {
		//return "@inputText(" + formName + "(\"" + fieldName + "\"), args='_label->\"" + label + "\")";
		return "@password(" + formName + "(\"" + fieldName + "\"), '_label->\"" + label + "\", 'class -> \"form-control\")";
	}

	private String textAreaHtml(String formName) {
		return "@textarea(" + formName + "(\"" + fieldName + "\"), '_label->\"" + label + "\")";
	}

	private String dropDownHtml(String formName) {
		return "@select("+ formName + "(\"" + fieldName + ".id\"), helper.options(" + typePackageName + "." +typeClassName + ".activeList()), '_label -> \"" + label + "\", '_default -> \"-- Choose an option --\")";
	}

	private String checkBoxHtml(String formName) {
		return "@checkbox(" + formName + "(\"" + fieldName + "\"), '_label->\"" + label + "\")";
	}

	private String datePickerCalendarHtml(String formName) {
		//return "DatePickerCalendar(" + "\"" + fieldName + "\"), args='_label->\"" + label + "\")";
		return "@datepicker(" + formName + "(\"" + fieldName + "\"), '_label->\"" + label + "\")";
	}

	//TODO implement
	private String datePickerDropDownHtml(String formName) {
		return "DatePickerDropDown(" + "\"" + fieldName + "\"), '_label->\"" + label + "\")";
	}

    private String numberHtml(String formName) {
        return "@number(" + formName + "(\"" + fieldName + "\"), '_label->\"" + label + "\")";
    }

	//TODO implement intuitive html element
	private String intuitiveHtml(String formName) {
		if(isPrimitive && ("boolean".equals(typeClassName) || "Boolean".equals(typeClassName)))
			return checkBoxHtml(formName);

		return textBoxHtml(formName);
	}

	@Override
	public int compareTo(BeanField o) {
		if(sortOrder>-1 && o.sortOrder<=-1)
			return -1;

		if(o.sortOrder>-1 && sortOrder<=-1)
			return 1;

		if(sortOrder != o.sortOrder)
			return (sortOrder>o.sortOrder) ? 1 : -1;

        return fieldName.compareTo(o.fieldName);
	}

}
