package com.example.mierul.myapplication21;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Hexa-Amierul.Japri on 28/9/2017.
 */

public class DateUtil {

    private final static String TIMESTAMP_FIREBASE_FORMAT = "yyMMddHHmmss";

    public static String timestamp(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FIREBASE_FORMAT, Locale.getDefault());
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    public static long getTimestampInMS(){
        return Calendar.getInstance().getTimeInMillis();
    }

    public static long getTimestampFromMS(long ms){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);
        return calendar.getTimeInMillis();
    }

    public static String getStringTimestampFromMS(long ms){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);
        int mSecond = calendar.get(Calendar.SECOND);

        return mYear+""+mMonth+""+mDay+""+mHour+""+mMinute+""+mSecond;
    }
}
