package edu.amherst.cs.motionsensordemo;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by aprasad on 1/29/17.
 */

public class FileUtil {


    public static void writeToFile(String str)
    {
        File file;
//        FileOutputStream outputStream;
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            file = new File(Environment.getExternalStorageDirectory(), "motion.txt");
            if(!file.exists()) {
                file.createNewFile();

            }

//            outputStream = new FileOutputStream(file);
//            outputStream.write(str.getBytes());

            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);

            bw.write(str+"\n");
//            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }


    }

}
