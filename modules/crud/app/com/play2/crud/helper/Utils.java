package com.play2.crud.helper;


public class Utils {
	public static boolean isDate(Class<?> clazz) {
		if(java.util.Date.class.equals(clazz)
				|| java.sql.Date.class.equals(clazz)) {
			
			return true;
		}
		
		return false;
	}
	
	
	public static String scalaType(String javaType) {
		if(javaType==null || javaType.isEmpty())
			return "";
		
		if(javaType.equals(Constants.ID_TYPE_INTEGER))
			return "Int";
		
		if(javaType.equals(Constants.ID_TYPE_LONG))
			return "Long";
		
		return "";
	}


    public static String dateFormatJavaToJs(String javaFormat) {
        if(javaFormat==null)
            return null;

        return javaFormat.toUpperCase();
    }

    public static String getDummyClassName(String className) {
        return "Dummy" + className;
    }
}
