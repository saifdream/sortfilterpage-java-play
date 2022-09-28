package filter;

import com.avaje.ebean.ExpressionList;
import filter.template.FilterResponse;
import filter.template.FilterType;
import models.Address;
import filter.template.FilterRequest;

import java.util.Arrays;
import java.util.List;

/**
 * Created by saif-dream on 2/3/2016.
 */
public class FilterAddress extends FilterRequest<Address> {
    public String[] houseNo;
    public String[] roadNo;
    public String[] roadName;
    public String[] apartmentNo;
    public String[] flatNo;
    public String[] areaName;

    public FilterResponse filteredAddress(){return Address.filteredAddress(this);}

    @Override
    public List<String> getFieldList() {
        return Arrays.asList("houseNo","roadNo","roadName","apartmentNo","flatNo","areaName");
    }

    @Override
    public ExpressionList<Address> getExprList(ExpressionList<Address> el) {
        FilterType ftHouseNo = FilterType.get(filterTypeId.get("houseNo"));
        FilterType ftRoadNo = FilterType.get(filterTypeId.get("roadNo"));
        FilterType ftRoadName = FilterType.get(filterTypeId.get("roadName"));
        FilterType ftApartmentNo = FilterType.get(filterTypeId.get("apartmentNo"));
        FilterType ftFlatNo = FilterType.get(filterTypeId.get("flatNo"));
        FilterType ftAreaName = FilterType.get(filterTypeId.get("areaName"));

        el = genExprList(el,"houseNo",false,ftHouseNo.id,houseNo);
        el = genExprList(el,"roadNo",false,ftRoadNo.id,roadNo);
        el = genExprList(el,"roadName",false,ftRoadName.id,roadName);
        el = genExprList(el,"apartmentNo",false,ftApartmentNo.id,apartmentNo);
        el = genExprList(el,"flatNo",false,ftFlatNo.id,flatNo);
        el = genExprList(el,"areaName",false,ftAreaName.id,areaName);

        return el;
    }
}
