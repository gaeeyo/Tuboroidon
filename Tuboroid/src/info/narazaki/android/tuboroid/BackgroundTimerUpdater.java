package info.narazaki.android.tuboroid;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BackgroundTimerUpdater {
    private static final String TAG = "BackgroundTimerUpdater";
    
    public static final String ACTION = "info.narazaki.android.tuboroid.service.TuboroidService.UPDATE_TIMER";
    
    public static void updateTimer(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean enabled = pref.getBoolean("favorites_update_service", false);
        int interval = Integer.parseInt(pref.getString("favorites_update_interval", "30"));
        
        AlarmManager alarm_manager = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));
        
        Intent intent = new Intent(BackgroundCheckUpdate.ACTION);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        alarm_manager.cancel(pending);
        if (!enabled) return;
        
        Calendar cal = Calendar.getInstance();
        int min = cal.get(Calendar.MINUTE);
        min = min % interval;
        cal.add(Calendar.MINUTE, interval - min);
        cal.add(Calendar.SECOND, -cal.get(Calendar.SECOND));
        
        alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), interval * 60 * 1000, pending);
    }
}
