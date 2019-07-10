package assignment.mansa.com.iskconapp.ModelInfo;

import java.util.ArrayList;

/**
 * Created by sumanthanda on 23/08/16.
 */
public class MonthInfo {

   private ArrayList<DayInfo> dayInfos;
    private int month;
    private  int year;

    public MonthInfo(int month, int year, ArrayList<DayInfo> dayInfos){
        this.month = month;
        this.year =year;
        this.dayInfos = dayInfos;
    }

    public ArrayList<DayInfo> getDayInfos() {
        return dayInfos;
    }

    public void setDayInfos(ArrayList<DayInfo> dayInfos) {
        this.dayInfos = dayInfos;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
