package assignment.mansa.com.iskconapp.ModelInfo;

import android.util.Log;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sumanthanda on 23/08/16.
 */
public class DayInfo {

    private Date date;
    private String day;
    private String information;

    public DayInfo(JSONObject dayRawInfo){
        String dateString = dayRawInfo.optString("Date");
        setDate(dateString);
        day = dayRawInfo.optString("Day");
        information = dayRawInfo.optString("Information");
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDate(String dateString){
        date =  stringToDate(dateString);
    }

    public String getDateString(){
        return dateToString(date);
    }

    public String getDay() {
        return day;
    }

    public int getMonth(){
        if(date == null){
            return -1;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public int getyear(){
        if(date == null){
            return -1;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    private Date stringToDate(String dateString){

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        Date startDate;
        try {
            startDate = formatter.parse(dateString);
            return startDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    private String dateToString(Date date){
        if(date == null){
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        String dateString = formatter.format(date);
        return dateString;
    }

    public Boolean isValid(){
        if(date == null){
            return false;
        }
        return true;
    }



}
