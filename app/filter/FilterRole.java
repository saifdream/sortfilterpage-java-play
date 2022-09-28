package filter;

import com.avaje.ebean.ExpressionList;
import filter.template.FilterRequest;
import filter.template.FilterResponse;
import filter.template.FilterType;
import models.Role;

import java.util.Arrays;
import java.util.List;

/**
 * Created by saif-dream on 2/3/2016.
 */
public class FilterRole extends FilterRequest<Role> {
    public String[] name;
    public String[] description;
    public String[] type;

    public FilterResponse filteredRole() {return Role.filteredRole(this);}

    @Override
    public List<String> getFieldList() {
        return Arrays.asList("name","description","type");
    }

    @Override
    public ExpressionList<Role> getExprList(ExpressionList<Role> el) {
        FilterType ftName = FilterType.get(filterTypeId.get("name"));
        FilterType ftDescription = FilterType.get(filterTypeId.get("description"));
        FilterType ftType = FilterType.get(filterTypeId.get("type"));

        el = genExprList(el, "name", false, ftName.id, name);
        el = genExprList(el, "description", false, ftDescription.id, description);
        el = genExprList(el, "type", false, ftType.id, type);

        return el;
    }
}
