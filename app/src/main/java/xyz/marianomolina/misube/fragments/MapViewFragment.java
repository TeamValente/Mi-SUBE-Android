package xyz.marianomolina.misube.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import xyz.marianomolina.misube.R;
import xyz.marianomolina.misube.utils.PermissionUtil;


/**
 * Created by Mariano Molina on 17/2/16.
 * Twitter: @xsincrueldadx
 */
public class MapViewFragment extends Fragment {
    // TAG
    private static final String LOG_TAG = MapViewFragment.class.getSimpleName();

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mLocationPermissionDenied = false;

    // propiedades
    private GoogleMap map;
    private SupportMapFragment supportMapFragment;
    private FloatingActionButton btn_find_my_location;

    public MapViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn_find_my_location = (FloatingActionButton) getActivity().findViewById(R.id.btn_find_my_location);

        FragmentManager fm = getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);

        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, supportMapFragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (map == null) {
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;

                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    /*if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }*/

                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        map.setMyLocationEnabled(true);
                    }

                    // Centramos el mapa en BUENOS AIRES
                    LatLng BUE = new LatLng(-34.6160275, -58.4333203);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(BUE, 13));

                    btn_find_my_location.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), "Click button", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void checkLocationPermissions() {
        if (PermissionUtil.hasSelfPermission(getActivity(), new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION })) {
            // ok
        } else {
            if (!mLocationPermissionDenied) {
                // SUPPORT ANDROID M RUNTIME PERMISSIONS
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION },
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // todo OK
            } else {
                Log.i("BaseActivity", "LOCATION permission was NOT granted.");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
