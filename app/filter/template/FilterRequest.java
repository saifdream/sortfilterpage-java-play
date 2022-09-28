package filter.template;


import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import utils.StringUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class FilterRequest<T> {

    public int currentPage = 1;

    public int rowsPerPage = 10;

    public int totalPageCount;

    public int totalRowsCount;

    public String displayCounter;

    public String sortColumnName = "id";

    public boolean isAscending = true;

    public Map<String, Integer> filterTypeId;


    public abstract List<String> getFieldList();


    public abstract ExpressionList<T> getExprList(ExpressionList<T> el);


    public ExpressionList<T> genExprList(ExpressionList<T> el, String propertyName, boolean isReferencedObj, int filterTypeId, Date[] value) {
        if (value == null || value.length == 0)
            return genExprList(el, propertyName, isReferencedObj, filterTypeId, (Object[]) value);

        Date val = value[0];
        if(val == null)
            return genExprList(el, propertyName, isReferencedObj, filterTypeId, (Object[]) value);

        switch (filterTypeId) {
            case FilterType.ID_MONTH:
                el = el.eq(propertyName, val);
                break;
            default:
                el = genExprList(el, propertyName, isReferencedObj, filterTypeId, (Object[]) value);
                break;
        }

        return el;
    }

    public ExpressionList<T> genExprList(ExpressionList<T> el, String propertyName, boolean isReferencedObj, int filterTypeId, String[] value) {
        if(value==null || value.length==0)
            return genExprList(el, propertyName, isReferencedObj, filterTypeId, (Object[]) value);

        String val = StringUtil.tidyUp(value[0]);
        if(val == null)
            return genExprList(el, propertyName, isReferencedObj, filterTypeId, (Object[]) value);

        switch (filterTypeId){
            case FilterType.ID_EQUALS:
                el = el.ieq(propertyName, val);
                break;
            case FilterType.ID_BEGINS_WITH:
                el = el.istartsWith(propertyName, val);
                break;
            case FilterType.ID_ENDS_WITH:
                el = el.iendsWith(propertyName, val);
                break;
            case FilterType.ID_CONTAINS:
                el = el.icontains(propertyName, val);
                break;
            case FilterType.ID_NOT_EQUALS:
                el = el.not(Expr.ieq(propertyName, val));
                break;
            case FilterType.ID_NOT_BEGINS_WITH:
                el = el.not(Expr.istartsWith(propertyName, val));
                break;
            case FilterType.ID_NOT_ENDS_WITH:
                el = el.not(Expr.iendsWith(propertyName, val));
                break;
            case FilterType.ID_NOT_CONTAINS:
                el = el.not(Expr.icontains(propertyName, val));
                break;
            //case FilterType.ID_SOUNDS_LIKE:
            //    el = el.eq(Expr.icontains(propertyName, val));
            //    break;
            default:
                el = genExprList(el, propertyName, isReferencedObj, filterTypeId, (Object[]) value);
                break;
        }

        return el;
    }

    public ExpressionList<T> genExprList(ExpressionList<T> el, String propertyName, boolean isReferencedObj, int filterTypeId, Object[] value) {
        if(value==null || value.length==0) {
            switch (filterTypeId){
                case FilterType.ID_IS_NOT_NULL:
                    el = el.isNotNull(propertyName);
                    break;
                case FilterType.ID_IS_NULL:
                    el = el.isNull(propertyName);
                    break;
            }
        } else {
            Object val = value[0];
            int len = value.length;

            if(val == null)
                return el;

            switch (filterTypeId){
                case FilterType.ID_EQUALS:
                    el = el.eq(propertyName, val);
                    break;
                case FilterType.ID_BETWEEN:
                    if(len<2) { break; }
                    el = el.between(propertyName, val, value[1]);
                    break;
                case FilterType.ID_GREATER_THAN:
                    el = el.gt(propertyName, val);
                    break;
                case FilterType.ID_GREATER_THAN_OR_EQUAL:
                    el = el.ge(propertyName, val);
                    break;
                case FilterType.ID_IN:
                    el = el.in(propertyName, value);
                    break;
                case FilterType.ID_LESS_THAN:
                    el = el.lt(propertyName, val);
                    break;
                case FilterType.ID_LESS_THAN_OR_EQUAL:
                    el = el.le(propertyName, val);
                    break;
                case FilterType.ID_NOT_BETWEEN:
                    if(len<2) { break; }
                    el = el.not(Expr.between(propertyName, val, value[1]));
                    break;
                case FilterType.ID_NOT_EQUALS:
                    el = el.not(Expr.eq(propertyName, val));
                    break;
                case FilterType.ID_NOT_IN:
                    el = el.not(Expr.in(propertyName, value));
                    break;
            }
        }

        return el;
    }
}
