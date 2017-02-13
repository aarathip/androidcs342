package edu.amherst.cs.servicedemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start PollService
        Intent i = new Intent (this, PollService.class);
        i.setAction("START_POLL_SERVICE");
        startService(i);

        // or say
//        Intent i = PollService.newIntent(this);
//        startService(i);

        // Setting alarm to start Service repeatedly
        // PollService.setServiceAlarm(this, true);

       // boolean shouldStartAlarm = !PollService.isServiceAlarmOn(this);
        // PollService.setServiceAlarm(this, shouldStartAlarm);
        //Perhaps a toggle button is better. How do you create one?
    }

    //onClick method for the button
    public void onClick(View v)
    {


    }




}
