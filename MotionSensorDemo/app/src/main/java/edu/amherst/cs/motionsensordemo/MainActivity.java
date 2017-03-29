package edu.amherst.cs.motionsensordemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;


    int sitEvent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

//        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
//        //  or given type: TYPE_GYROSCOPE, TYPE_LINEAR_ACCELERATION, or TYPE_GRAVITY
//
//        for(int i=0; i<deviceSensors.size(); i++)
//        {
//            Log.d("MainActivity", deviceSensors.get(i).getStringType());
//        }

         mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        //default data delay (SENSOR_DELAY_NORMAL) is specified when the registerListener() method is invoked.
        // The data delay (or sampling rate) controls the interval at which sensor events are sent to your
        // application via the onSensorChanged() callback method. The default data delay is suitable for monitoring
        // typical screen orientation changes and uses a delay of 200,000 microseconds. You can specify other
        // data delays, such as SENSOR_DELAY_GAME (20,000 microsecond delay), SENSOR_DELAY_UI (60,000 microsecond delay),
        // or SENSOR_DELAY_FASTEST (0 microsecond delay) or directly in microseconds.
    }


    //A SensorEvent object contains information about the new sensor data,
    // including: the accuracy of the data, the sensor that generated the data,
    // the timestamp at which the data was generated, and the new data that the sensor recorded.
    public void onSensorChanged(SensorEvent event){

//        float x = event.values[0]; //Acceleration force along the x axis (including gravity).
//        float y = event.values[1]; //Acceleration force along the y axis (including gravity).
//        float z = event.values[2]; //Acceleration force along the z axis (including gravity).
//
//        float accuracy = event.accuracy;
//
//
//        Log.d("MainActivity", "x:" + x+", y:"+y+", z:"+z+", + acc:"+accuracy);



        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        float gravity[] = new float[3];
        float linear_acceleration[] = new float[3];
        final float alpha = 0.8f;

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        FileUtil.writeToFile(toTime()+","+linear_acceleration[0] +","+ linear_acceleration[1]+","+ linear_acceleration[2] + "," + sitEvent);

    }


    public void start (View v)
    {
        //FileUtil.writeToFile("start");
        sitEvent = 1;
    }


    public void stop (View v)
    {
        //FileUtil.writeToFile("stop");
        sitEvent = 0;
    }


    //SENSOR_STATUS_ACCURACY_LOW, SENSOR_STATUS_ACCURACY_MEDIUM,
    //SENSOR_STATUS_ACCURACY_HIGH, or SENSOR_STATUS_UNRELIABLE.
    public void onAccuracyChanged (Sensor sensor, int accuracy)
    {

    }

    //As a best practice you should always disable sensors you don't need,
    // especially when your activity is paused. Failing to do so can drain the
    // battery in just a few hours because some sensors have substantial power
    // requirements and can use up battery power quickly

    //onResume() register the accelerometer for listening the events
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
            //onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    private String toTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return sdf.format(new Date());
    }


}
