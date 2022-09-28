package filter;

import com.avaje.ebean.ExpressionList;
import filter.template.FilterRequest;
import filter.template.FilterResponse;
import filter.template.FilterType;
import models.City;

import java.util.Arrays;
import java.util.List;

/**
 * Created by saif-dream on 1/15/2016.
 */
public class FilterCity extends FilterRequest<City> {

    public String[] name;

    public String[] shortName;

    public FilterResponse filteredCity() {
        return City.filteredCity(this);
    }

    @Override
    public List<String> getFieldList() {
        return Arrays.asList("name", "shortName");
    }

    @Override
    public ExpressionList<City> getExprList(ExpressionList<City> el) {
        FilterType ftName = FilterType.get(filterTypeId.get("name"));
        FilterType ftShortName = FilterType.get(filterTypeId.get("shortName"));

        el = genExprList(el, "name", false, ftName.id, name);
        el = genExprList(el, "shortName", false, ftShortName.id, shortName);

        return el;
    }
}
