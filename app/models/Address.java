package models;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import filter.template.FilterResponse;
import play.libs.Json;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import filter.FilterAddress;

/**
 * Created by saif-dream on 2/2/2016.
 */

@Entity
public class Address extends Model{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
    public Integer id;
    public String houseNo;
    public String roadNo;
    public String roadName;
    public String apartmentNo;
    public String flatNo;
    public String areaName;

    @OneToMany
    @JsonIgnore
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    public List<City> city;

    public static Model.Finder<Integer, Address> find = new Model.Finder<>(Integer.class, Address.class);
    public static List<Address> all() { return find.all(); }

    public static Address get(int id) { return find.byId(id); }

    public static FilterResponse filteredAddress(FilterAddress filterAddress) {
        ExpressionList<Address> el = find.where();

        el = filterAddress.getExprList(el);

        com.avaje.ebean.Query<Address> query = filterAddress.isAscending ? el.order().asc(filterAddress.sortColumnName) : el.order().desc(filterAddress.sortColumnName);
        PagedList<Address> pagedList = query.findPagedList((filterAddress.currentPage-1), filterAddress.rowsPerPage);
        pagedList.loadRowCount();

        FilterResponse fr = new FilterResponse();
        fr.currentPage = filterAddress.currentPage;
        fr.rowsPerPage = filterAddress.rowsPerPage;
        fr.totalPageCount = pagedList.getTotalPageCount();
        fr.totalRowsCount = pagedList.getTotalRowCount();
        fr.displayCounter = pagedList.getDisplayXtoYofZ(" to ", " from ");
        fr.sortColumnName = filterAddress.sortColumnName;
        fr.isAscending = filterAddress.isAscending;

        List<JsonNode> data = new ArrayList<>();
        for(Address a : pagedList.getList()){
            JsonNode jn = Json.toJson(a);
            System.out.println(jn);
            data.add(jn);
        }
        fr.data = data;

        return fr;
    }

}

