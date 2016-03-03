package xyz.marianomolina.misube.fragments;


import android.Manifest;
import android.content.DialogInterface;
import android.location.Location;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.marianomolina.misube.R;
import xyz.marianomolina.misube.model.PuntoCarga;
import xyz.marianomolina.misube.services.DondeCargoAPI;


/**
 * Created by Mariano Molina on 17/2/16.
 * Twitter: @xsincrueldadx
 */
@RuntimePermissions
public class MapViewFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // TAG
    private static final String LOG_TAG = MapViewFragment.class.getSimpleName();

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 167;

    // Permission Constants
    private static final int REQUEST_SHOWMAP = 0;
    private static final String[] PERMISSION_SHOWMAP = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
    // propiedades
    private GoogleMap map;
    private SupportMapFragment supportMapFragment;
    private FloatingActionButton btn_find_my_location;

    // location
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private Location mLocation;
    private LatLng USER_LOC;

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

        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (checkGooglePlayServices()) {

            btn_find_my_location = (FloatingActionButton) getActivity().findViewById(R.id.btn_find_my_location);

            FragmentManager fm = getChildFragmentManager();
            supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);

            if (supportMapFragment == null) {
                supportMapFragment = SupportMapFragment.newInstance();
                fm.beginTransaction().replace(R.id.map_container, supportMapFragment).commit();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // mostramos el mapa
        showMapWithCheck();
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void showMap() {

        if (map == null) supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                map = googleMap;

                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                // Ignorar el error que esta marcando, los permisos ya estan implementados
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.setMyLocationEnabled(true);

                if (mLocation != null) {
                    USER_LOC = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(USER_LOC, 13));
                } else {
                    // Centramos el mapa en BUENOS AIRES
                    USER_LOC = new LatLng(-34.6160275, -58.4333203);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(USER_LOC, 12));
                }

                btn_find_my_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            getLocation(mLocation);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d(LOG_TAG, "Error al ejecutar el DondeCargoService");
                        }
                    }
                });
            }
        });
    }

    /*
    * Google Play API
    * */
    private boolean checkGooglePlayServices() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int result = api.isGooglePlayServicesAvailable(getContext());
        if (result != ConnectionResult.SUCCESS) {
            if (api.isUserResolvableError(result)) {
                api.getErrorDialog(getActivity(), result, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    /*
    * Location
    * */
    protected void getLocation(Location location) throws IOException {
        /*
        // GET MyLocation
        LocationManager service = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, true);

        // Ignorar el error que esta marcando, los permisos ya estan implementados
        Location location = service.getLastKnownLocation(provider);
        */
        if (location != null) {
            Log.d(LOG_TAG, "El GPS esta encendido");
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 13));

            dondeCargoService();
        } else {
            Log.d(LOG_TAG, "Error de conexion");
        }
    }

    @Override
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(10000);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "GoogleApiClient connection has failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOG_TAG, "onLocationChanged" + location.toString());
        mLocation = location;
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


    /*
    * REST adapter
    * */
    public void dondeCargoService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://dondecargolasube.com.ar/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DondeCargoAPI service = retrofit.create(DondeCargoAPI.class);

        Call<List<PuntoCarga>> call = service.loadPuntosCarga("1390472","-34.61201","-58.44356");
        call.enqueue(new Callback<List<PuntoCarga>>() {
            @Override
            public void onResponse(Call<List<PuntoCarga>> call, Response<List<PuntoCarga>> response) {
                Log.d(LOG_TAG, "Response message: " + response.body());
            }

            @Override
            public void onFailure(Call<List<PuntoCarga>> call, Throwable t) {
                Log.d(LOG_TAG, "RetrofitError: " + t.getLocalizedMessage());
            }
        });

    }
}
