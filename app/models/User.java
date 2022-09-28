package models;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import filter.FilterUser;
import filter.template.FilterResponse;
import play.libs.Json;
import utils.JUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="app_user")
public class User extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_seq")
    public Integer id;

    @Column(length=18)
    public String username;

    @Column(length=95)
    public String fullName;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    public Role role;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd", timezone="GMT")
    public Date dob;

    @Column(precision=16, scale=2)
    public BigDecimal salary;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    public City city;

    @Column(length=95)
    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    public Address address;

    public boolean isMarried;

    @Column(length=1)
    public Character gender;


    @SuppressWarnings("deprecation")
	private static Finder<Integer, User> find = new Finder<> (Integer.class, User.class);

    public static User get(int id) {
        return find.byId(id);
    }

    public static List<User> all() {
        return find.all();
    }


    //public static List<User> filteredList(FilterUser filter) {
    //public static FilterResponse<User> filteredList(FilterUser filter) {
    public static FilterResponse filteredList(FilterUser filter) {
        ExpressionList<User> el = find.fetch("city").where();
        //System.out.println(filter.dob[0]);

        /*
        if(filter.id != null)
            el = el.eq("id", filter.id);

        if(filter.username != null)
            el = JUtils.gelString(el, "username", filter.username);

        if(filter.fullName != null)
            el = JUtils.gelString(el, "fullName", filter.fullName);

        if(filter.dob!=null && filter.dob.from!=null)
            el = el.ge("dob", filter.dob.from);
        if(filter.dob!=null && filter.dob.to!=null)
            el = JUtils.gelToDate(el, "dob", filter.dob.to);

        if(filter.salary!=null && filter.salary.from!=null)
            el = el.ge("salary", filter.salary.from);
        if(filter.salary!=null && filter.salary.to!=null)
            el = el.le("salary", filter.salary.to);

        if(filter.address!=null)
            el = JUtils.gelString(el, "address", filter.address);

        if(filter.isMarried!=null)
            el = el.eq("isMarried", filter.isMarried);
        */

        el = filter.getExprList(el);
        //el.in("city.id", new Integer[] {1,2});

        com.avaje.ebean.Query<User> query = filter.isAscending ? el.order().asc(filter.sortColumnName) : el.order().desc(filter.sortColumnName);
        PagedList<User> pagedList = query.findPagedList((filter.currentPage-1), filter.rowsPerPage);
        pagedList.loadRowCount();

        filter.totalRowsCount = pagedList.getTotalRowCount();
        filter.totalPageCount = pagedList.getTotalPageCount();
        filter.displayCounter = pagedList.getDisplayXtoYofZ(" to ", " from ");

        /*
        filter.totalRowsCount= 10;
        filter.totalPageCount = 1;
        filter.displayCounter = "Abc";
        */

        //return el.findList();
        //return pagedList.getList();

        //FilterResponse<User> fr = new FilterResponse();
        FilterResponse fr = new FilterResponse();
        fr.currentPage = filter.currentPage;
        fr.rowsPerPage = filter.rowsPerPage;
        fr.totalPageCount = pagedList.getTotalPageCount();
        fr.totalRowsCount = pagedList.getTotalRowCount();
        fr.displayCounter = pagedList.getDisplayXtoYofZ(" to ", " from ");
        fr.sortColumnName = filter.sortColumnName;
        fr.isAscending = filter.isAscending;
        //fr.data = pagedList.getList();

        List<JsonNode> data = new ArrayList<>();
        for(User u : pagedList.getList()){
            Role r = u.role;
            City c = u.city;
            JsonNode jn = Json.toJson(u);
            if(c!=null && c.name!=null){
                ((ObjectNode)jn).put("city", c.name);
            }
            if(r!=null && r.name!=null){
                ((ObjectNode)jn).put("role", r.name);
            }
            //System.out.println(jn);
            data.add(jn);
        }
        fr.data = data;

        return fr;
    }

    public static List<User> filteredListTest() {
        ExpressionList<User> el = find.where();
        el.in("city.id", new Integer[] {1,2});
        com.avaje.ebean.Query<User> query = el.order().asc("id");
        PagedList<User> pagedList = query.findPagedList(0, 10);
        pagedList.loadRowCount();
        return pagedList.getList();
    }

}
