package edu.amherst.cs.downloaddemo;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

//Modified example from http://programmerguru.com/android-tutorial/android-asynctask-example/

public class MainActivity extends AppCompatActivity {
    // Button to download and play Music
    private Button btnPlayMusic;
    // Media Player Object
    private MediaPlayer mPlayer;
    // Progress Dialog Object
    private ProgressDialog progress;
    // Progress Dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    // Music resource URL
    private static String file_url = "http://www.sample-videos.com/audio/mp3/wave.mp3";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        // Show Download Music Button
        btnPlayMusic = (Button) findViewById(R.id.btnProgressBar);
        // Download Music Button click listener
        btnPlayMusic.setOnClickListener(new View.OnClickListener() {
            // When Download Music Button is clicked
            public void onClick(View v) {
                // Disable the button to avoid playing of song multiple times
                btnPlayMusic.setEnabled(false);
                // Downloaded Music File path in SD Card
                File file = new File(Environment.getExternalStorageDirectory().getPath()+"/wave.mp3");
                Log.d("onClick", file.getAbsolutePath());
                // Check if the Music file already exists
                if (file.exists()) {
                    Toast.makeText(getApplicationContext(), "File already exist under SD card, playing Music", Toast.LENGTH_LONG).show();
                    // Play Music
                    playMusic();
                    // If the Music File doesn't exist in SD card (Not yet downloaded)
                } else {

                    Toast.makeText(getApplicationContext(), "File doesn't exist under SD Card, downloading Mp3 from Internet", Toast.LENGTH_LONG).show();
                    // Trigger Async Task (onPreExecute method)
                    progress=new ProgressDialog(v.getContext());
                    progress.setMessage("Downloading Music");
                    progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progress.setIndeterminate(true);
                    progress.setProgress(0);
                    progress.show();
                    new DownloadMusicfromInternet().execute(file_url);
                }
            }
        });
    }

    // Async Task Class
    class DownloadMusicfromInternet extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // Get Music file length
                int lenghtOfFile = conection.getContentLength();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(),10*1024);
                // Output stream to write file in SD card
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/wave.mp3");
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress which triggers onProgressUpdate method
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // Write data to file
                    output.write(data, 0, count);
                }
                // Flush output
                output.flush();
                // Close streams
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        // While Downloading Music File
        protected void onProgressUpdate(String... progressTime) {
            // Set progress percentage
            progress.setProgress(Integer.parseInt(progressTime[0]));
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            // Dismiss the dialog after the Music file was downloaded
            progress.dismiss();
            Toast.makeText(getApplicationContext(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            playMusic();
        }
    }

    // Play Music
    protected void playMusic(){
        // Read Mp3 file present under SD card
        Uri myUri1 = Uri.parse("file://"+Environment.getExternalStorageDirectory().getPath()+"/wave.mp3");
        mPlayer  = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(getApplicationContext(), myUri1);
            mPlayer.prepare();
            // Start playing the Music file
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    // Once Music is completed playing, enable the button
                    btnPlayMusic.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Music completed playing",Toast.LENGTH_LONG).show();
                }
            });
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "URI cannot be accessed, permissed needed", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "Media Player is not in correct state", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "IO Error occured", Toast.LENGTH_LONG).show();
        }
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

}
