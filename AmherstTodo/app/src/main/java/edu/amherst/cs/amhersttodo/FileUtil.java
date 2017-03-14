package edu.amherst.cs.amhersttodo;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by aprasad on 1/29/17.
 */

public class FileUtil {


    public static ArrayList<TodoItem> readFromFile(Context ctx)
    {
        ArrayList<TodoItem> tItems = new ArrayList<>();

//        InputStream inputStream = ctx.getResources().openRawResource(R.raw.val);
//        InputStreamReader inputreader = new InputStreamReader(inputStream);
//        BufferedReader buffreader = new BufferedReader(inputreader);
//        String line;
//
//
//        try {
//            while (( line = buffreader.readLine()) != null) {
//
////                TodoItem m = new TodoItem();
////                Log.d("FileUtil", line);
////                String[] parts = line.split(",");
////                m.setType(parts[0]);
////                m.setSection(parts[1]);
////                m.setItems(parts[2]);
//                //menuItems.add(m);
////                text.append(line);
////                text.append('\n');
//            }
//        } catch (IOException e) {
//            return null;
//        }

        return tItems;
    }

}
