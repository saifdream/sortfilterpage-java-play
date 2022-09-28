package filter.template;


public class FilterType {

    protected FilterType() {}

    public int id;

    public String label;

    public String fieldTextFormat;

    public String regexFormat;

    @Override
    public String toString() {
        return label;
    }


    public static FilterType get(Integer id) {
        if(id == null)
            return NO_FILTER;

        switch (id) {
            case ID_NO_FILTER:
                return NO_FILTER;
            case ID_EQUALS:
                return EQUALS;
            case ID_NOT_EQUALS:
                return NOT_EQUALS;
            case ID_BEGINS_WITH:
                return BEGINS_WITH;
            case ID_NOT_BEGINS_WITH:
                return NOT_BEGINS_WITH;
            case ID_ENDS_WITH:
                return ENDS_WITH;
            case ID_NOT_ENDS_WITH:
                return NOT_ENDS_WITH;
            case ID_CONTAINS:
                return CONTAINS;
            case ID_NOT_CONTAINS:
                return NOT_CONTAINS;
            case ID_LESS_THAN:
                return LESS_THAN;
            case ID_LESS_THAN_OR_EQUAL:
                return LESS_THAN_OR_EQUAL;
            case ID_GREATER_THAN:
                return GREATER_THAN;
            case ID_GREATER_THAN_OR_EQUAL:
                return GREATER_THAN_OR_EQUAL;
            case ID_BETWEEN:
                return BETWEEN;
            case ID_NOT_BETWEEN:
                return NOT_BETWEEN;
            case ID_IN:
                return IN;
            case ID_NOT_IN:
                return NOT_IN;
            case ID_IS_NULL:
                return IS_NULL;
            case ID_IS_NOT_NULL:
                return IS_NOT_NULL;
            case ID_SOUNDS_LIKE:
                return SOUNDS_LIKE;
            case ID_MONTH:
                return MONTH;
            default:
                return NO_FILTER;
        }
    }


    public static final int ID_NO_FILTER = 0;
    public static final int ID_EQUALS = 1;
    public static final int ID_NOT_EQUALS = 2;
    public static final int ID_BEGINS_WITH = 3;
    public static final int ID_NOT_BEGINS_WITH = 4;
    public static final int ID_ENDS_WITH = 5;
    public static final int ID_NOT_ENDS_WITH = 6;
    public static final int ID_CONTAINS = 7;
    public static final int ID_NOT_CONTAINS = 8;
    public static final int ID_LESS_THAN = 9;
    public static final int ID_LESS_THAN_OR_EQUAL = 10;
    public static final int ID_GREATER_THAN = 11;
    public static final int ID_GREATER_THAN_OR_EQUAL = 12;
    public static final int ID_BETWEEN = 13;
    public static final int ID_NOT_BETWEEN = 14;
    public static final int ID_IN = 15;
    public static final int ID_NOT_IN = 16;
    public static final int ID_IS_NULL = 17;
    public static final int ID_IS_NOT_NULL = 18;
    public static final int ID_SOUNDS_LIKE = 19;
    public static final int ID_MONTH = 20;


    public static final FilterType NO_FILTER = apply(ID_NO_FILTER, "- No Filter -", "", "");
    public static final FilterType EQUALS = apply(ID_EQUALS, "Equals", "{1}", "");
    public static final FilterType NOT_EQUALS = apply(ID_NOT_EQUALS, "Not Equals", "!={1}", "");
    public static final FilterType BEGINS_WITH = apply(ID_BEGINS_WITH, "Begins With", "%{1}", "");
    public static final FilterType NOT_BEGINS_WITH = apply(ID_NOT_BEGINS_WITH, "Not Begins With", "!%{1}", "");
    public static final FilterType ENDS_WITH = apply(ID_ENDS_WITH, "Ends With", "{1}%", "");
    public static final FilterType NOT_ENDS_WITH = apply(ID_NOT_ENDS_WITH, "Not Ends With", "!{1}%", "");
    public static final FilterType CONTAINS = apply(ID_CONTAINS, "Contains", "%{1}%", "");
    public static final FilterType NOT_CONTAINS = apply(ID_NOT_CONTAINS, "Not Contains", "!%{1}%", "");
    public static final FilterType LESS_THAN = apply(ID_LESS_THAN, "Less Than", "<", "");
    public static final FilterType LESS_THAN_OR_EQUAL = apply(ID_LESS_THAN_OR_EQUAL, "Less Than or Equal", "<=", "");
    public static final FilterType GREATER_THAN = apply(ID_GREATER_THAN, "Greater Than", ">", "");
    public static final FilterType GREATER_THAN_OR_EQUAL = apply(ID_GREATER_THAN_OR_EQUAL, "Greater Than or Equal", ">=", "");
    public static final FilterType BETWEEN = apply(ID_BETWEEN, "Between/Range", "{1}-{2}", "");
    public static final FilterType NOT_BETWEEN = apply(ID_NOT_BETWEEN, "Not Between", "!{1}-{2}", "");
    public static final FilterType IN = apply(ID_IN, "In/Multiple", "()", "");
    public static final FilterType NOT_IN = apply(ID_NOT_IN, "Not In", "!()", "");
    public static final FilterType IS_NULL = apply(ID_IS_NULL, "Is Null/Empty", "", "");
    public static final FilterType IS_NOT_NULL = apply(ID_IS_NOT_NULL, "Is Not Null", "", "");
    public static final FilterType SOUNDS_LIKE = apply(ID_SOUNDS_LIKE, "Sounds Like", "s({1})", "");
    public static final FilterType MONTH = apply(ID_MONTH, "Month", "m({1})", "");


    protected static final FilterType apply(int id, String label, String regexFormat, String fieldTextFormat) {
        FilterType f = new FilterType();
        f.id = id;
        f.label = label;
        f.regexFormat =regexFormat;
        f.fieldTextFormat = fieldTextFormat;

        return f;
    }

}
