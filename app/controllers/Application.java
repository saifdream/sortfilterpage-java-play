package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import filter.FilterUser;
import filter.template.FilterRequest;
import filter.template.FilterResponse;
import models.User;
import models.City;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

import java.util.*;

public class Application extends Controller {

    static Form<User> form = Form.form(User.class);
    static Form<FilterUser> filterForm = Form.form(FilterUser.class);

    public static Result index() {
        return ok(index.render("Hello world", form));
    }


    /*public static Result listForm() {
        FilterUser ffm = new FilterUser();

        //return ok(list.render(FilterMe.all(), filterForm.fill(ffm)));
        //List<User> fmList = User.filteredList(ffm);
        //return ok(list.render(fmList, filterForm.fill(ffm)));
        return ok(list.render(null, filterForm.fill(ffm)));
    }

    public static Result list() {
        Form<FilterUser> fmForm = filterForm.bindFromRequest();
        //FilterUser ffm = fmForm.get().get();
        FilterUser ffm = fmForm.get();

        //List<User> fmList = User.filteredList(ffm);
        //return ok(list.render(fmList, filterForm.fill(ffm)));
        return ok(list.render(null, filterForm.fill(ffm)));
    }

    public static Result filterForm() {
        return ok(advanceFilter.render());
    }

    public static Result filter() {
        return ok(advanceFilter.render());
    }

    public static Result data() {
        //System.out.println(Json.toJson(User.all()));
        //return ok(Json.toJson(User.all()));
        //System.out.println(Json.toJson(User.filteredListTest()));
        //return ok(Json.toJson(User.filteredListTest()));
        //System.out.println(Json.toJson(User.filteredListTest()));
        return ok(Json.toJson(User.filteredListTest()));
    }*/


    public static Result testForm() {
        return ok(testMainTemplate.render());
    }

    public static Result test() {
        return ok(testMainTemplate.render());
    }

    public static Result city() {
        return ok(cityTemplate.render());
    }

    public static Result role() {
        return ok(roleTemplate.render());
    }

    public static Result address() {
        return ok(addressTemplate.render());
    }

    /*public static Result editForm() {
        return ok(editForm.render());
    }

    public static Result edit() {
        return ok(editForm.render());
    }*/
}
