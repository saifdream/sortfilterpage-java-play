package filter;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import filter.template.FilterRequest;
import filter.template.FilterResponse;
import filter.template.FilterType;
import models.User;
import play.data.format.Formats;
import utils.StringUtil;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
//import filter.template.RangeBigDecimal;
//import filter.template.RangeDate;


public class FilterUser extends FilterRequest<User> {

    public String[] username;

    public String[] fullName;

    public Integer[] role;

    @Formats.DateTime(pattern = "yyyy/MM/dd")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd", timezone="GMT")
    //@JsonSerialize(using=JsonDateSerializer.class)
    public Date[] dob;

    public BigDecimal[] salary;

    public Integer[] city;

    public String[] address;

    public Boolean[] isMarried;

    public Character[] gender;

    /*
    public List<User> filteredList() {
        return User.filteredList(this);
    }
    */
    //public FilterResponse<User> filteredList() {
    public FilterResponse filteredList() {
        return User.filteredList(this);
    }

    @Override
    public ExpressionList<User> getExprList(ExpressionList<User> el) {

        FilterType ftUsername = FilterType.get(filterTypeId.get("username"));
        FilterType ftFullName = FilterType.get(filterTypeId.get("fullName"));
        FilterType ftRole = FilterType.get(filterTypeId.get("role"));
        FilterType ftDob = FilterType.get(filterTypeId.get("dob"));
        FilterType ftSalary = FilterType.get(filterTypeId.get("salary"));
        FilterType ftCity = FilterType.get(filterTypeId.get("city"));
        FilterType ftAddress = FilterType.get(filterTypeId.get("address"));
        FilterType ftIsMarried = FilterType.get(filterTypeId.get("isMarried"));
        FilterType ftGender = FilterType.get(filterTypeId.get("gender"));

        /*if(dob.length != 0){
            System.out.println(dob[0]);
        }*/

        el = genExprList(el, "username", false, ftUsername.id, username);
        el = genExprList(el, "fullName", false, ftFullName.id, fullName);
        el = genExprList(el, "role.id", false, ftRole.id, role);
        el = genExprList(el, "dob", false, ftDob.id, dob);
        el = genExprList(el, "salary", false, ftSalary.id, salary);
        el = genExprList(el, "city.id", false, ftCity.id, city);
        el = genExprList(el, "address", false, ftAddress.id, address);
        el = genExprList(el, "isMarried", false, ftIsMarried.id, isMarried);
        el = genExprList(el, "gender", false, ftGender.id, gender);

        /*
        if(this.username != null)
            this.username = this.username.trim().toLowerCase();

        if(this.fullName != null)
            this.fullName = this.fullName.trim();
                    //.toLowerCase();

        if(this.address != null)
            this.address = this.address.trim();
        */

        return el;
    }

    @Override
    public List<String> getFieldList() {
        return Arrays.asList("username", "fullName", "role", "dob", "salary", "city", "address", "isMarried", "gender");
    }
}
