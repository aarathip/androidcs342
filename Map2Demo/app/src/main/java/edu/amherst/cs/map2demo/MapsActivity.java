package edu.amherst.cs.map2demo;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final LatLng SMUD = new LatLng(42.369773, -72.516238);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMinZoomPreference(6.0f);
//        mMap.setMaxZoomPreference(20.0f);

//        // Add a marker in Seeley Mudd and move the camera
        LatLng smud = new LatLng(42.369773, -72.516238);
        mMap.addMarker(new MarkerOptions().position(smud).title("Seeley Mudd"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(42.371823, -72.516959)).title("Frost Library"));

//        //mMap.moveCamera(CameraUpdateFactory.newLatLng(smud));
//        //mMap.moveCamera(CameraUpdateFactory.zoomTo(20.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(smud, 18.0f));



        // Move the camera instantly to SMUD with a zoom of 15.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SMUD, 15));

// Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());

// Zoom out to zoom level 10, animating with a duration of 5 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 5000, null);

// Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(29.218103, -81.031723))      // Sets the center of the map to ??
                .zoom(10)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }
}
