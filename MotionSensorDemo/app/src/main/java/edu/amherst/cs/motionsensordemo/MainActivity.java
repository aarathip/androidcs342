package edu.amherst.cs.motionsensordemo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        float x = event.values[0]; //Acceleration force along the x axis (including gravity).
        float y = event.values[1]; //Acceleration force along the y axis (including gravity).
        float z = event.values[2]; //Acceleration force along the z axis (including gravity).

        float accuracy = event.accuracy;


        Log.d("MainActivity", "x:" + x+", y:"+y+", z:"+z+", + acc:"+accuracy);
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


}
