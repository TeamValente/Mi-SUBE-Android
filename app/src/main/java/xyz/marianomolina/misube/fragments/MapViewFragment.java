package xyz.marianomolina.misube.fragments;


import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;
import permissions.dispatcher.RuntimePermissions;
import xyz.marianomolina.misube.R;


/**
 * Created by Mariano Molina on 17/2/16.
 * Twitter: @xsincrueldadx
 */
@RuntimePermissions
public class MapViewFragment extends Fragment {
    // TAG
    private static final String LOG_TAG = MapViewFragment.class.getSimpleName();
    // Permission Constants
    private static final int REQUEST_SHOWMAP = 0;
    private static final String[] PERMISSION_SHOWMAP = new String[] {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION" };
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
        // mostramos el mapa
        showMapWithCheck();
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE })
    public void showMap() {

        if (map == null) supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                map.setMyLocationEnabled(true);
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


    /*
    * Permisions
    * */
    private void showMapWithCheck() {
        if (PermissionUtils.hasSelfPermissions(getActivity(), PERMISSION_SHOWMAP)) {
            showMap();
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(getActivity(), PERMISSION_SHOWMAP)) {
                showMapRationale(new ShowMapPermissionRequest(getActivity()));
            } else {
                ActivityCompat.requestPermissions(getActivity(), PERMISSION_SHOWMAP, REQUEST_SHOWMAP);
            }
        }
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE })
    void onMapDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
        Toast.makeText(getContext(), R.string.location_permission_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE })
    void onMapNeverAskAgain() {
        Toast.makeText(getContext(), R.string.location_permission_denied, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE })
    void showMapRationale(PermissionRequest request) {
        showRationaleDialog(R.string.permission_rationale_location, request);
    }

    /*
    * Generate Rationale Dialog
    * */
    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(getContext())
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_SHOWMAP) {
            if (PermissionUtils.getTargetSdkVersion(getActivity()) < 23 && !PermissionUtils.hasSelfPermissions(getActivity(), PERMISSION_SHOWMAP)) {
                // metodo para cuando no hay acceso al mapa.
                onMapDenied();
            }
            if (PermissionUtils.verifyPermissions(grantResults)) {
                // mostramos el mapa
                showMap();
            } else {
                if (PermissionUtils.shouldShowRequestPermissionRationale(getActivity(), PERMISSION_SHOWMAP)) {
                    // mostramos el modal para no preguntar nunca mas.
                    onMapNeverAskAgain();
                } else {
                    // metodo para cuando no hay acceso al mapa.
                    onMapDenied();
                }
            }
        }
    }

    private final class ShowMapPermissionRequest implements PermissionRequest {
        private final FragmentActivity weakTarget;

        private ShowMapPermissionRequest(FragmentActivity weakTarget) {
            this.weakTarget = weakTarget;
        }

        @Override
        public void proceed() {
            FragmentActivity target = weakTarget;
            if (target == null) return;
            ActivityCompat.requestPermissions(target, PERMISSION_SHOWMAP, REQUEST_SHOWMAP);
        }

        @Override
        public void cancel() {
            FragmentActivity target = weakTarget;
            if (target == null) return;
            onMapDenied();
        }
    }
}
