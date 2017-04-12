package com.example.fajar.verifikasiimb;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SingleBangunanMapsView extends AppCompatActivity {

    public SingleBangunanMapsView() {

    }

    private GoogleMap mMap;
    private MapView mapView;

    private LatLng pos_bangunan;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Lokasi Bangunan");

        Intent i = getIntent();
        if (i != null) {
            Bundle extras = i.getExtras();
            double latitude = extras.getDouble("latitude");
            double longitude = extras.getDouble("longitude");
            pos_bangunan = new LatLng(latitude, longitude);
            Toast.makeText(SingleBangunanMapsView.this, "latitude : "+latitude+", longitude: "+longitude, Toast.LENGTH_SHORT).show();
        }

        mapView = (MapView) findViewById(R.id.maps);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);

                // Add a marker in Sydney and move the camera
                if (pos_bangunan != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(pos_bangunan).zoom(20).build();
                    //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    mMap.addMarker(new MarkerOptions().position(pos_bangunan).title("this is the location of your building")
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)));
                }

                if (mapView != null &&
                        mapView.findViewById(Integer.parseInt("1")) != null) {
                    // Get the button view
                    View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                    // and next place it, on bottom right (as Google Maps app)
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(120, 120); // size of button in dp
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                    params.setMargins(0, 0, 20, 0);
                    locationButton.setLayoutParams(params);
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
