package edu.amherst.cs.jsonxmldemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.text.ParseException;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    TextView myTxt;
    EditText sname, sGradYear, cName1, cNum1, cName2, cNum2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTxt = (TextView) findViewById(R.id.jsonTxt);

        sname = (EditText) findViewById(R.id.sname);
        sGradYear = (EditText) findViewById(R.id.gradYear);
        cName1 = (EditText) findViewById(R.id.cname1);
        cName2 = (EditText) findViewById(R.id.cname2);
        cNum1 = (EditText) findViewById(R.id.cnum1);
        cNum2 = (EditText) findViewById(R.id.cnum2);

        String studentInfo = "";
        List<Student> allStudents = new ArrayList<>();
        try {
            allStudents = readJsonStream();

            for(Student s: allStudents)
            {
                studentInfo+= "Name:" + s.name + "\nGradYear: " + s.gradYear + "\nCourses:\n";
                for(Course c: s.courses)
                {
                    studentInfo+= "\t" + c.name + "(CS"+ c.num + ")\n";
                }

                studentInfo+= "\n\n\n";
            }

            myTxt.setText(studentInfo);

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void addStudent(View v) {

        String name = sname.getText().toString();
        int gradYear = Integer.parseInt(sGradYear.getText().toString());

        List<Course> courses = new ArrayList<>();
        courses.add(new Course(cName1.getText().toString(),Integer.parseInt(cNum1.getText().toString())));
        courses.add(new Course(cName2.getText().toString(),Integer.parseInt(cNum2.getText().toString())));

        List<Student> students = new ArrayList<>();
        students.add(new Student(name, gradYear, courses));

        try{
        writeJsonStream(System.out, students);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }



        public void writeJsonStream(OutputStream out, List<Student> students) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writeStudentsArray(writer, students);
        writer.close();
    }

        public void writeStudentsArray(JsonWriter writer, List<Student> students) throws IOException {
        writer.beginArray();
        for (Student s : students) {
            writeStudent(writer, s);
        }
        writer.endArray();
    }

        public void writeStudent(JsonWriter writer, Student s) throws IOException {
        writer.beginObject();
        writer.name("name").value(s.name);
        writer.name("gradYear").value(s.gradYear);
        if (s.courses != null) {
            writer.name("courses");
            writeCoursesArray(writer, s.courses);
        } else {
            writer.name("courses").nullValue();
        }
        writer.endObject();
    }

    public void writeCourse(JsonWriter writer, Course c) throws IOException {
        writer.beginObject();
        writer.name("name").value(c.name);
        writer.name("num").value(c.num);
        writer.endObject();
    }

    public void writeCoursesArray(JsonWriter writer, List<Course> courses) throws IOException {
        writer.beginArray();
        for (Course c : courses) {
            writeCourse(writer, c);
        }
        writer.endArray();
    }


    public List<Student> readJsonStream() throws IOException {
        InputStream is = getResources().openRawResource(R.raw.student);

        JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(is, "UTF-8")));
        try {
            return readStudentsArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<Student> readStudentsArray(JsonReader reader) throws IOException {
        List<Student> students = new ArrayList<Student>();

        reader.beginArray();
        while (reader.hasNext()) {
            students.add(readStudent(reader));
        }
        reader.endArray();
        return students;
    }

    public List<Course> readCoursesArray(JsonReader reader) throws IOException {
        List<Course> courses = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {

            courses.add(readCourse(reader));
        }
        reader.endArray();
        return courses;
    }

    public Student readStudent(JsonReader reader) throws IOException {
        String label = "";
        String name="";
        int gradYear=-1;
        List<Course> courses = null;

        reader.beginObject();
        while (reader.hasNext()) {
            label = reader.nextName();
            if (label.equals("name")) {
                name = reader.nextString();
            } else if (label.equals("gradYear")) {
                gradYear = reader.nextInt();
            } else if (label.equals("courses")) { // && reader.peek() != JsonToken.NULL) {
                courses = readCoursesArray(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Student(name, gradYear, courses);
    }

    public Course readCourse(JsonReader reader) throws IOException {
        String label = "";
        String name="";
        int num=-1;

        reader.beginObject();
        while (reader.hasNext()) {
            label = reader.nextName();
            if (label.equals("name")) {
                name = reader.nextString();
            } else if (label.equals("num")) {
                num = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Course(name, num);
    }
//
//
//    public User readUser(JsonReader reader) throws IOException {
//        String username = null;
//        int followersCount = -1;
//
//        reader.beginObject();
//        while (reader.hasNext()) {
//            String name = reader.nextName();
//            if (name.equals("name")) {
//                username = reader.nextString();
//            } else if (name.equals("followers_count")) {
//                followersCount = reader.nextInt();
//            } else {
//                reader.skipValue();
//            }
//        }
//        reader.endObject();
//        return new User(username, followersCount);
//    }


//    public void parse()
//    {
//        JSONObject student;
//        JSONArray courses = new JSONArray();
//        String name= "", major="";
//        int gradYear = -1;
//        try{
//            student=(new JSONObject(JSON_STRING)).getJSONObject("student");
////            courses =  new JSONArray(JSON_STRING2); //.getJSONArray("courses");
////
////            for (int i = 0; i < courses.length(); i++) {
////                JSONObject jsonobject = courses.getJSONObject(i).getJSONObject("course");
////                 String cname = jsonobject.getString("name");
////                    int cnum = jsonobject.getInt("num");
////            }
//
//           // courses= (new JSONObject(JSON_STRING2)).getJSONArray("courses");
//            name=student.getString("name");
//            major=student.getString("major");
//            gradYear= student.getInt("gradYear");
//        }
//        catch(JSONException ex)
//        {
//            ex.printStackTrace();
//        }
//        String str="Student Name:"+name+"\n"+" Grad Year:"+gradYear + "\n" + " Major:" + major
//                + "\n" + "Courses: " + courses.length();
//        myTxt.setText(str);
//
//    }

}
