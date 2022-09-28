package models;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import filter.FilterRole;
import filter.FilterUser;
import filter.template.FilterResponse;
import play.libs.Json;

import javax.persistence.*;
import java.util.*;

/**
 * Created by saif-dream on 2/2/2016.
 */

@Entity
public class Role extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    public Integer id;
    public String name;
    public String description;
    public Integer type;

    @OneToMany
    @JsonIgnore
    public List<User> users;

    public static Finder<Integer, Role> find = new Finder<>(Integer.class, Role.class);
    public static List<Role> all() { return find.all(); }

    public static Role get(int id) { return find.byId(id); }

    private static Map<Integer, String> sortByComparator(Map<Integer, String> unsortMap) {
        // Convert Map to List
        List<Map.Entry<Integer, String>> list = new LinkedList<Map.Entry<Integer, String>>(unsortMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, (o1, o2) -> (o1.getValue()).compareTo(o2.getValue()));

        // Convert sorted map back to a Map
        Map<Integer, String> sortedMap = new LinkedHashMap<Integer, String>();
        for (Iterator<Map.Entry<Integer, String>> it = list.iterator(); it.hasNext();) {
            Map.Entry<Integer, String> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static HashMap<Integer,String> roles = new HashMap<Integer,String>();

    public static Map<Integer, String> getRoleData() {
        for(Role role : Role.find.all()){
            roles.put(role.id, role.name);
        }
        Map<Integer, String> sortedRole = sortByComparator(roles);
        //printMap(sortedCities);
        return sortedRole;
    }

    public static FilterResponse filteredRole(FilterRole filterRole) {
        ExpressionList<Role> el = find.where();

        el = filterRole.getExprList(el);

        com.avaje.ebean.Query<Role> query = filterRole.isAscending ? el.order().asc(filterRole.sortColumnName) : el.order().desc(filterRole.sortColumnName);
        PagedList<Role> pagedList = query.findPagedList((filterRole.currentPage-1), filterRole.rowsPerPage);
        pagedList.loadRowCount();

        FilterResponse fr = new FilterResponse();
        fr.currentPage = filterRole.currentPage;
        fr.rowsPerPage = filterRole.rowsPerPage;
        fr.totalPageCount = pagedList.getTotalPageCount();
        fr.totalRowsCount = pagedList.getTotalRowCount();
        fr.displayCounter = pagedList.getDisplayXtoYofZ(" to ", " from ");
        fr.sortColumnName = filterRole.sortColumnName;
        fr.isAscending = filterRole.isAscending;

        List<JsonNode> data = new ArrayList<>();
        for(Role r : pagedList.getList()){
            JsonNode jn = Json.toJson(r);
            System.out.println(jn);
            data.add(jn);
        }
        fr.data = data;

        return fr;
    }
}
