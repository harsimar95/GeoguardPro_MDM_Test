package assignment.mansa.com.iskconapp.ProcessorInfo;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import assignment.mansa.com.iskconapp.ModelInfo.DayInfo;
import assignment.mansa.com.iskconapp.ModelInfo.MonthInfo;
import assignment.mansa.com.iskconapp.R;

/**
 * Created by sumanthanda on 23/08/16.
 */
public class CalendarProcessor {

    private ArrayList<DayInfo> dayInfos;

    public CalendarProcessor(Activity activity){
        dayInfos = getDayInfos(activity);
    }

    private JSONArray readCalendarRaw(Activity activity){
        Log.d("day", "read calendar raw");
        InputStream stream = activity.getResources().openRawResource(R.raw.calendar);
        JSONProcessor processor = new JSONProcessor();
        String jsonString = processor.loadJSON(stream);

//        Log.d("day", jsonString);
        try{
            JSONArray jsonArray = new JSONArray(jsonString);
            return jsonArray;
        }
        catch(JSONException exception) {
            Log.d("day", "exception"+exception.toString());
            return new JSONArray();
        }
    }


    public void readCalendar(){
        Log.d("day", "");
        Log.d("day", "read calendar");
        ArrayList<MonthInfo> infos = getInfos();
        for (MonthInfo monthInfo : infos){
            Log.d("rest", "year==> "+String.valueOf(monthInfo.getYear()));
            Log.d("rest", "month ==> "+String.valueOf(monthInfo.getMonth()));
        }

    }




    private ArrayList<MonthInfo> getInfos(){
        ArrayList<MonthInfo> infos =  new ArrayList<MonthInfo>();
        for (int year=2016; year<=2033; year++){
            ArrayList<MonthInfo> yearInfos = getInfosForYear(year);
            infos.addAll(yearInfos);
        }

        return infos;
    }

    private ArrayList<MonthInfo> getInfosForYear(int year){
        ArrayList<MonthInfo> monthInfos =  new ArrayList<MonthInfo>();
        for (int month=0; month<=11; month++){
            MonthInfo monthInfo = getMonthInfo(month, year);
            monthInfos.add(monthInfo);
        }
        return monthInfos;
    }


    private MonthInfo getMonthInfo(int month, int year){
        ArrayList<DayInfo> monthDayInfos =  new ArrayList<DayInfo>();
        for (DayInfo dayInfo : dayInfos){
            if(dayInfo.getMonth() == month && dayInfo.getyear() == year){
                monthDayInfos.add(dayInfo);
//                dayInfos.remove(dayInfo);
            }
        }

        MonthInfo monthInfo = new MonthInfo(month, year, monthDayInfos);
        return monthInfo;
    }

    private ArrayList<DayInfo> getDayInfos(Activity activity){
        JSONArray rawInfos = readCalendarRaw(activity);
        ArrayList<DayInfo> dayInfos = new ArrayList<DayInfo>();
        Log.d("read", "rawInfos.length  =>"+rawInfos.length());

        for (int i = 0; i< rawInfos.length(); i++){
//        for (int i = 0; i< 2; i++){
            JSONObject dayRawInfo = rawInfos.optJSONObject(i);
            DayInfo dayInfo = new DayInfo(dayRawInfo);
            if(!dayInfo.isValid()){
                   continue;
            }
            dayInfos.add(dayInfo);
        }
        return dayInfos;
    }
}
