package com.play2.crud.annotation;

import java.lang.Class;
import java.lang.Object;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

//import com.play2.crud.enumeration.DatePickerType;
import com.play2.crud.enumeration.HtmlElementType;
//import com.play2.crud.enumeration.SortType;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Scaffold {

	boolean showInCreate() default true;
	boolean showInEdit() default true;
	boolean editable() default true;
	boolean showInList() default true;
	boolean showInDetails() default true;
	
	float sortOrder() default 0;  // ** negative means from last position
	//SortType sortType() default SortType.FROM_FIRST;
	
	//DatePickerType datePickerType() default DatePickerType.NONE;
	
	HtmlElementType htmlElementType() default HtmlElementType.NONE;

	String label() default "";

	//Class<?> manyToManyBridge() default Object.class;
}