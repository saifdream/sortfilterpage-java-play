package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import filter.FilterAddress;
import filter.FilterCity;
import filter.FilterRole;
import filter.FilterUser;
import filter.template.FilterRequest;
import models.Address;
import models.City;
import models.Role;
import models.User;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class Test extends Controller {

    public static Result jsonFilter() {
        JsonNode json = request().body().asJson();

        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
            FilterUser fu = Json.fromJson(json, FilterUser.class);
            if(fu.dob.length != 0){
                System.out.println(fu.dob[0]);
                System.out.println(new Date());
            }
            //return ok(u.toString());
            System.out.println("Request For Data: "+json);
            return ok(Json.toJson(User.filteredList(fu)));
        }
    }

    public static Result jsonCityFilter() {
        JsonNode json = request().body().asJson();

        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            FilterCity c = Json.fromJson(json, FilterCity.class);
            //System.out.println(c);
            return ok(Json.toJson(City.filteredCity(c)));
        }
    }

    public static Result jsonRoleFilter(){
        JsonNode json = request().body().asJson();

        if(json == null){
            return badRequest("Expect Json data");
        } else {
            FilterRole r = Json.fromJson(json, FilterRole.class);
            return ok(Json.toJson(Role.filteredRole(r)));
        }
    }

    public static Result jsonAddressFilter(){
        JsonNode json = request().body().asJson();

        if(json == null){
            return badRequest("Expect Json data");
        } else {
            FilterAddress a = Json.fromJson(json, FilterAddress.class);
            return ok(Json.toJson(Address.filteredAddress(a)));
        }
    }

    public static Result testEbean() {
        //User u = User.get(2);
        //return ok("Size: " + User.all().size());
        return ok("Size: " + User.filteredListTest().size());
    }


}
