package edu.amherst.cs.servicedemo;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PollService extends IntentService {

    final String TAG = "PollService";
    private static final int POLL_INTERVAL = 1000*60; //60 seconds

    public PollService() {
        super("PollService");
    }

    public static Intent newIntent (Context c)
    {
        return new Intent(c, PollService.class);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.i(TAG, "Received an intent: " + intent);
        }
    }


    public static void setServiceAlarm (Context context, boolean isOn)
    {
        Intent i = PollService.newIntent(context);

        //Context to which to send the intent, request code to distinguish this PendingIntent from others,
        //Intent object to send, and finally flags to tweak how PendingIntent is created.
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);


        //Can also be done with a Handler.sendMessageDelayed() or Handler.postDelayed() -- but Handler wont work if calling activity is not alive
        //Alarm_Service is a system service.
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if(isOn)
        {
            //Use time since last boot of device for interval calculations
            am.setInexactRepeating(am.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
        }
        else
        {
            am.cancel(pi);
            pi.cancel();
        }

    }

    public static boolean isServiceAlarmOn(Context c)
    {
        Intent i = PollService.newIntent(c);

        //The flag says that if the PendingIntent does not already exist, return null instead of creating it.
        PendingIntent pi = PendingIntent.getService(c, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi!=null; //if null, alarm was not set
    }

}
