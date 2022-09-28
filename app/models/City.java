package models;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import filter.FilterCity;
import filter.template.FilterResponse;
import play.libs.Json;

import javax.persistence.*;
import java.util.*;


@Entity
public class City extends Model {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="city_seq")
    public Integer id;

    public String name;

    public String shortName;

    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    public Address address;

    @OneToMany
    @JsonIgnore
    public List<User> users;

    public static Finder<Integer, City> find = new Finder<> (Integer.class, City.class);

    public static City get(int id) {
        return find.byId(id);
    }

    public static List<City> all() {
        return find.all();
    }

    private static Map<Integer, String> sortByComparator(Map<Integer, String> unsortMap) {
        // Convert Map to List
        List<Map.Entry<Integer, String>> list = new LinkedList<Map.Entry<Integer, String>>(unsortMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<Integer, String>>() {
            public int compare(Map.Entry<Integer, String> o1, Map.Entry<Integer, String> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // Convert sorted map back to a Map
        Map<Integer, String> sortedMap = new LinkedHashMap<Integer, String>();
        for (Iterator<Map.Entry<Integer, String>> it = list.iterator(); it.hasNext();) {
            Map.Entry<Integer, String> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    /*public static void printMap(Map<Integer,String> map) {
        for (Map.Entry<Integer,String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }*/

    public static HashMap<Integer,String> cities = new HashMap<Integer,String>();
    public static Map<Integer, String> getCityData() {
        for(City city : City.find.all()){
            cities.put(city.id, city.name);
        }
        Map<Integer, String> sortedCities = sortByComparator(cities);
        //printMap(sortedCities);
        return sortedCities;
    }

    @Override
    public String toString() {
        return shortName;
    }

    public static FilterResponse filteredCity(FilterCity filterCity) {
        ExpressionList<City> el = find.where();

        el = filterCity.getExprList(el);

        com.avaje.ebean.Query<City> query = filterCity.isAscending ? el.order().asc(filterCity.sortColumnName) : el.order().desc(filterCity.sortColumnName);
        PagedList<City> pagedList = query.findPagedList((filterCity.currentPage-1), filterCity.rowsPerPage);
        pagedList.loadRowCount();

        FilterResponse fr = new FilterResponse();
        fr.currentPage = filterCity.currentPage;
        fr.rowsPerPage = filterCity.rowsPerPage;
        fr.totalPageCount = pagedList.getTotalPageCount();
        fr.totalRowsCount = pagedList.getTotalRowCount();
        fr.displayCounter = pagedList.getDisplayXtoYofZ(" to ", " from ");
        fr.sortColumnName = filterCity.sortColumnName;
        fr.isAscending = filterCity.isAscending;

        List<JsonNode> data = new ArrayList<>();
        for(City c : pagedList.getList()){
            JsonNode jn = Json.toJson(c);
            System.out.println(jn);
            data.add(jn);
        }
        fr.data = data;

        return fr;
    }
}
