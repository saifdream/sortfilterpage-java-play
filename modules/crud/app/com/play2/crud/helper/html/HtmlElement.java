package com.play2.crud.helper.html;

import java.lang.reflect.Field;

import javax.persistence.ManyToOne;

import com.play2.crud.enumeration.HtmlElementType;
import com.play2.crud.helper.Utils;

public class HtmlElement {

	//TODO implement
	//public static HtmlElementType getHtmlElementTypeByClass(Class<?> fieldClass) {
	public static HtmlElementType getHtmlElementTypeByField(Field field) { 
		Class<?> fieldClass = field.getType();
		if(java.lang.String.class.equals(fieldClass))
			return HtmlElementType.TEXT_BOX;
		
		if(Utils.isDate(fieldClass))
			return HtmlElementType.DATE_PICKER_CALENDAR;
		
		if(java.lang.Boolean.class.equals(fieldClass) || boolean.class.equals(fieldClass))
			return HtmlElementType.CHECK_BOX;

		if(isNumber(fieldClass))
			return HtmlElementType.NUMBER;

		ManyToOne mtm = field.getAnnotation(ManyToOne.class);
		if(mtm != null)
			return HtmlElementType.DROP_DOWN;
		
		return HtmlElementType.TEXT_BOX;
	}


    private static final boolean isNumber(Class<?> fieldClass) {
        if(java.lang.Integer.class.equals(fieldClass) || int.class.equals(fieldClass)
                || java.lang.Float.class.equals(fieldClass) || float.class.equals(fieldClass)
                || java.lang.Double.class.equals(fieldClass) || double.class.equals(fieldClass)
                || java.lang.Long.class.equals(fieldClass) || long.class.equals(fieldClass))
            return true;

        return false;
    }
}
