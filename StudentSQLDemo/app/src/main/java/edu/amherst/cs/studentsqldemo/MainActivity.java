package edu.amherst.cs.studentsqldemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.ParseException;

public class MainActivity extends AppCompatActivity {
    MyDataSource mds;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mds = new MyDataSource(this);
    }

    public void saveToDb (View v)
    {
        //Get name, major and year
        EditText editText = (EditText) findViewById(R.id.name);
        String name = editText.getText().toString();

        editText = (EditText) findViewById(R.id.major);
        String major = editText.getText().toString();

        editText = (EditText) findViewById(R.id.year);
        int year = -1;

        try {
            year = Integer.parseInt(editText.getText().toString());
        }
        catch(NumberFormatException nfe)
        {
            nfe.printStackTrace();
        }

        //Open database connection
        mds.open();

        //Add student record
        StudentDemo s = mds.addStudent(name, major, year);

        //Dont forget to close the database connection
        mds.close();

        if(s!=null)
            Log.d(TAG, "Added the student " + name + " to the database.");

    }

}
