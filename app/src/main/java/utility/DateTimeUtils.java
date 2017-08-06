package utility;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SAI on 8/6/2017.
 */

public class DateTimeUtils {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";

    public static String getDate(Date date) {
        return convertDateToString(date, DATE_FORMAT);
    }

    public static String getTime(Date date) {
        return convertDateToString(date, TIME_FORMAT);
    }

    public static Date convertStringToDate(String strDate, String strFormate) {
        if (!strDate.equals("null") || !strDate.equals("")) {
            DateFormat df = new SimpleDateFormat(strFormate);
            try {
                return df.parse(strDate);
            } catch (ParseException e) {
                Logger.writeToCrashlytics(e);
                return new Date(0);
            }
        } else {
            return new Date(0);
        }
    }

    public static String convertDateToString(Date dt, String strFormate) {
        if (dt != null) {
            try {
                SimpleDateFormat df = new SimpleDateFormat(strFormate);
                return df.format(dt.getTime());
            } catch (Exception e) {
                Logger.writeToCrashlytics(e);
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    public static String convertDateToAbbrevString(String strDateTime) {
        try {
            long now = System.currentTimeMillis();
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT + " " + TIME_FORMAT);
            Date convertedDate = dateFormat.parse(strDateTime);

            CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(
                    convertedDate.getTime(),
                    now,
                    0L, DateUtils.FORMAT_ABBREV_ALL);
            return relativeTime.toString();
        } catch (Exception e) {
            Logger.writeToCrashlytics(e);
            e.printStackTrace();
        }
        return "";
    }

}
