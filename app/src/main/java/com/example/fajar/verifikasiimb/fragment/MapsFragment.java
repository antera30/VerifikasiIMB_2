package com.example.fajar.verifikasiimb.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.fajar.verifikasiimb.R;
import com.example.fajar.verifikasiimb.VerificationActivity;
import com.example.fajar.verifikasiimb.model.Bangunan;
import com.example.fajar.verifikasiimb.model.BangunanResponse;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment {


    MapView mMapView;
    private GoogleMap mMap;
    View rootView;
    private LocationManager locationManager;
    ImageButton btn_verification;
    public static List<Bangunan> listBangunan = new ArrayList<>();
    public boolean cameraFirstMove = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        btn_verification = (ImageButton) rootView.findViewById(R.id.insert_verification);
        btn_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VerificationActivity.class);
                startActivity(intent);
                //Toast.makeText(getActivity().getApplicationContext(), listBangunan.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setIndoorEnabled(false);
                mMap.setBuildingsEnabled(false);
                mMap.setTrafficEnabled(false);

                List<Bangunan> bangunanList = getListBangunan();
                if (bangunanList.size() != 0) {
                    for (int i = 0; i < bangunanList.size(); i++) {
                        LatLng loc = new LatLng(bangunanList.get(i).getLatitude(), bangunanList.get(i).getLongitude());
                        int pemilik = bangunanList.get(i).getIdPemilik();
                        int ket_imb = bangunanList.get(i).getIdKetImb();
                        addMarker(loc, mMap, "Id Pemilik : " + pemilik + " status imb : " +ket_imb, ket_imb);
                    }
                }

                googleMap.setMyLocationEnabled(true);
                if (mMapView != null &&
                        mMapView.findViewById(Integer.parseInt("1")) != null) {
                    // Get the button view
                    View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                    // and next place it, on bottom right (as Google Maps app)
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(120, 120); // size of button in dp
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                    params.setMargins(0, 0, 20, 0);
                    locationButton.setLayoutParams(params);
                }

                mMap.setOnMyLocationChangeListener(myLocationChangeListener);

            }
        });
        return rootView;
    }

    public void addMarker(LatLng latLng, GoogleMap googleMap, String title, int status) {
        if (status == 1) {
            googleMap.addMarker(new MarkerOptions().position(latLng).title(title)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        } else if (status == 2) {
            googleMap.addMarker(new MarkerOptions().position(latLng).title(title)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        } else if (status == 3) {
            googleMap.addMarker(new MarkerOptions().position(latLng).title(title)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }

    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            if (mMap != null && cameraFirstMove == false) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16.0f));
                cameraFirstMove = true;
            } else {
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public MapsFragment() {
        // Required empty public constructor
    }

    public List<Bangunan> getListBangunan() {
        return listBangunan;
    }

    public void setListBangunan(List<Bangunan> bangunan) {
        listBangunan = bangunan;
    }
}
