package edu.amherst.cs.locationdemo;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends AppCompatActivity {

    private GoogleApiClient aClient;
    private static String TAG = "MainActivity";
    TextView latlong, locationVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyLocationPermissions(this);

        int error = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (error != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(error, getParent(), 0, new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            errorDialog.show();

        }

        latlong = (TextView) findViewById(R.id.longlat);
        locationVal = (TextView) findViewById(R.id.locationVal);


        aClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

                    public void onConnected(Bundle bundle) {
                        Log.d(TAG, "aClient connected");
                    }

                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();

    }

    protected void onStart() {
        super.onStart();
        aClient.connect();
    }

    protected void onStop() {
        super.onStop();
        aClient.disconnect();
    }


    public void findMe(View v) {

        Log.d(TAG, "Where am I button clicked?");
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);

        LocationServices.FusedLocationApi
                .requestLocationUpdates(aClient, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.d(TAG, "Got a location: " + location);
                        Log.d(TAG, "Location obtained from: " + location.getProvider());
                        latlong.setText("I'm at: " + location.getLatitude() + "," + location.getLongitude());

                        locationVal.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        locationVal.setText("And for you human, that means I'm at " + useGeoCoder(location.getLatitude(), location.getLongitude()));
                    }

                });
    }

    private static final int REQUEST_LOCATION = 1;
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    //persmission method.
    public static void verifyLocationPermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_LOCATION,
                    REQUEST_LOCATION
            );
        }
    }


    public String useGeoCoder(double latitude, double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;

        String fullAddress = "";

        geocoder = new Geocoder(this, Locale.getDefault());

        try {

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            fullAddress = address + ", " + city + ", " + state;

        }

        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        return fullAddress;
    }

}





