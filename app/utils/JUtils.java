package utils;

import com.avaje.ebean.ExpressionList;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


public class JUtils {

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }


    public static ExpressionList gelString(ExpressionList el, String property, String value) {
        if(value==null || value.trim().isEmpty())
            return el;

        String val = value.trim();
        if(val.trim().startsWith("%") || val.endsWith("%")) {
            el = el.like(property, val);
        } else {
            el = el.eq(property, val);
        }

        return el;
    }

    public static ExpressionList gelToDate(ExpressionList el, String property, Date value) {
        if(value==null)
            return el;

        LocalDate date = value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);
        el = el.le(property, date);

        return el;
    }
}
