package triplei.mx.notification.Utilerias;


import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utilerias_Activity {

    public static String Get_DateTime() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getDefault());
        String time = dateFormatGmt.format(new Date());
        return time;
    }

    public static String Get_Date() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd");
        dateFormatGmt.setTimeZone(TimeZone.getDefault());
        String time = dateFormatGmt.format(new Date());
        return time;
    }

    public static String getIdUsuarioPreference(SharedPreferences sharedPreferences) // Envia Usuario
    {
        return sharedPreferences.getString("Id_Usuario", null);
    }

}
