package filter.template;

public class FilterGroup {

    int id;

    FilterType[] filterTypes;


    public static FilterGroup get(int id) {
        switch (id) {
            case ID_NUMERIC_ID:
                return NUMERIC_ID;
            case ID_NUMBER:
                return NUMBER;
            case ID_DATE:
                return DATE;
            case ID_DATE_TIME:
                return DATE_TIME;
            case ID_NULLABLE_TEXT:
                return NULLABLE_TEXT;
            case ID_NOT_NULL_TEXT:
                return NOT_NULL_TEXT;
            default:
                return NULLABLE_TEXT;
        }
    }


    public static final int ID_NUMERIC_ID = 1;
    public static final int ID_NUMBER = 2;
    public static final int ID_DATE = 3;
    public static final int ID_DATE_TIME = 4;
    public static final int ID_NULLABLE_TEXT = 5;
    public static final int ID_NOT_NULL_TEXT = 6;
    public static final int ID_FREE_TEXT = 7;


    public static final FilterGroup NUMERIC_ID = numbericId();
    public static final FilterGroup NUMBER = number();
    public static final FilterGroup DATE = date();
    public static final FilterGroup DATE_TIME = dateTime();
    public static final FilterGroup NULLABLE_TEXT = nullableText();
    public static final FilterGroup NOT_NULL_TEXT = notNullText();
    public static final FilterGroup FREE_TEXT = freeText();


    private static final FilterGroup numbericId() {
        FilterGroup fg = new FilterGroup();
        fg.id = ID_NUMERIC_ID;
        fg.filterTypes =
            new FilterType[]{
                FilterType.EQUALS
                , FilterType.BETWEEN
                , FilterType.IN
                , FilterType.NOT_EQUALS
                , FilterType.NOT_BETWEEN
                , FilterType.NOT_IN
        };

        return fg;
    }

    private static final FilterGroup number() {
        FilterGroup fg = new FilterGroup();
        fg.id = ID_NUMBER;
        fg.filterTypes =
                new FilterType[]{
                        FilterType.EQUALS
                        , FilterType.BETWEEN
                        , FilterType.GREATER_THAN
                        , FilterType.GREATER_THAN_OR_EQUAL
                        , FilterType.IN
                        , FilterType.IS_NOT_NULL
                        , FilterType.IS_NULL
                        , FilterType.LESS_THAN
                        , FilterType.LESS_THAN_OR_EQUAL
                        , FilterType.NOT_EQUALS
                        , FilterType.NOT_BETWEEN
                        , FilterType.NOT_IN
                };

        return fg;
    }

    private static final FilterGroup date() {
        FilterGroup fg = new FilterGroup();
        fg.id = ID_DATE;
        fg.filterTypes =
                new FilterType[]{
                        FilterType.EQUALS
                        , FilterType.BETWEEN
                        , FilterType.GREATER_THAN_OR_EQUAL
                        , FilterType.IN
                        , FilterType.IS_NOT_NULL
                        , FilterType.IS_NULL
                        , FilterType.LESS_THAN
                        , FilterType.MONTH
                        , FilterType.NOT_EQUALS
                        , FilterType.NOT_BETWEEN
                        , FilterType.NOT_IN
                };

        return fg;
    }

    private static final FilterGroup dateTime() {
        FilterGroup fg = new FilterGroup();
        fg.id = ID_DATE_TIME;
        fg.filterTypes =
                new FilterType[]{
                        FilterType.EQUALS
                        , FilterType.BETWEEN
                        , FilterType.GREATER_THAN_OR_EQUAL
                        , FilterType.IS_NOT_NULL
                        , FilterType.IS_NULL
                        , FilterType.LESS_THAN
                        , FilterType.NOT_EQUALS
                        , FilterType.NOT_BETWEEN
                };

        return fg;
    }

    private static final FilterGroup notNullText() {
        FilterGroup fg = new FilterGroup();
        fg.id = ID_NOT_NULL_TEXT;
        fg.filterTypes =
                new FilterType[]{
                        FilterType.EQUALS
                        , FilterType.CONTAINS
                        , FilterType.BEGINS_WITH
                        , FilterType.ENDS_WITH
                        , FilterType.IN
                        , FilterType.SOUNDS_LIKE
                        , FilterType.NOT_CONTAINS
                        , FilterType.NOT_BEGINS_WITH
                        , FilterType.NOT_ENDS_WITH
                        , FilterType.NOT_EQUALS
                        , FilterType.NOT_IN
                };

        return fg;
    }

    private static final FilterGroup nullableText() {
        FilterGroup fg = new FilterGroup();
        fg.id = ID_NULLABLE_TEXT;
        fg.filterTypes =
                new FilterType[]{
                        FilterType.EQUALS
                        , FilterType.CONTAINS
                        , FilterType.BEGINS_WITH
                        , FilterType.ENDS_WITH
                        , FilterType.IN
                        , FilterType.SOUNDS_LIKE
                        , FilterType.IS_NULL
                        , FilterType.IS_NOT_NULL
                        , FilterType.NOT_CONTAINS
                        , FilterType.NOT_BEGINS_WITH
                        , FilterType.NOT_ENDS_WITH
                        , FilterType.NOT_EQUALS
                        , FilterType.NOT_IN
                };

        return fg;
    }

    private static final FilterGroup freeText() {
        FilterGroup fg = new FilterGroup();
        fg.id = ID_NULLABLE_TEXT;
        fg.filterTypes =
                new FilterType[]{
                        FilterType.SOUNDS_LIKE
                        , FilterType.CONTAINS
                        , FilterType.BEGINS_WITH
                        , FilterType.ENDS_WITH
                        , FilterType.IS_NULL
                        , FilterType.IS_NOT_NULL
                        , FilterType.NOT_CONTAINS
                        , FilterType.NOT_BEGINS_WITH
                        , FilterType.NOT_ENDS_WITH
                };

        return fg;
    }

}
