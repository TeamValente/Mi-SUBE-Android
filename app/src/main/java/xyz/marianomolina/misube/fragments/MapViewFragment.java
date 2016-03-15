package xyz.marianomolina.misube.fragments;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.viewanimator.ViewAnimator;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import xyz.marianomolina.misube.PuntoDeCargaRouteActivity;
import xyz.marianomolina.misube.R;
import xyz.marianomolina.misube.helper.DetailHelper;
import xyz.marianomolina.misube.model.Filtro;
import xyz.marianomolina.misube.model.MiUbicacion;
import xyz.marianomolina.misube.model.PuntoCarga;
import xyz.marianomolina.misube.services.DondeCargoService;


/**
 * Created by Mariano Molina on 17/2/16.
 * Twitter: @xsincrueldadx
 */
@RuntimePermissions
public class MapViewFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    // TAG
    private static final String LOG_TAG = MapViewFragment.class.getSimpleName();
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 167;
    // Permission Constants
    private static final int REQUEST_SHOWMAP = 0;
    private static final String[] PERMISSION_SHOWMAP = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
    // view outlets
    private GoogleMap map;
    private SupportMapFragment supportMapFragment;
    private FloatingActionButton btn_find_my_location;
    private FloatingActionButton btn_close_detail;
    private FloatingActionButton btn_close_filter;
    private FloatingActionButton btn_open_filter;
    private LinearLayout detail_view;
    private LinearLayout filter_view;
    private Switch option_hours;
    private Switch option_chargecost;
    private Switch option_noseller;
    private Switch option_novisiblehours;

    // location
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private Location mLocation;
    private LatLng USER_LOC;
    // model
    private DondeCargoService mService;
    private MiUbicacion mUbicacion;
    private Filtro mFiltro;
    private Map<Marker, PuntoCarga> markerPuntoCargaMap = new HashMap<>();

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

            mService = new DondeCargoService();

            FragmentManager fm = getChildFragmentManager();
            supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);

            // find elements
            btn_find_my_location = (FloatingActionButton) getActivity().findViewById(R.id.btn_find_my_location);
            btn_open_filter = (FloatingActionButton) getActivity().findViewById(R.id.btn_open_filter);
            btn_open_filter.hide();

            btn_close_detail = (FloatingActionButton) getActivity().findViewById(R.id.btn_close_detail);
            btn_close_detail.hide();

            btn_close_filter = (FloatingActionButton) getActivity().findViewById(R.id.btn_close_filter_view);
            btn_close_filter.hide();

            detail_view = (LinearLayout) getActivity().findViewById(R.id.detail_view);
            filter_view = (LinearLayout) getActivity().findViewById(R.id.filter_view);

            // filterSwitch
            option_hours = (Switch) getActivity().findViewById(R.id.switch_option_hours);
            option_chargecost = (Switch) getActivity().findViewById(R.id.switch_option_chargecost);
            option_noseller = (Switch) getActivity().findViewById(R.id.switch_option_noseller);
            option_novisiblehours = (Switch) getActivity().findViewById(R.id.switch_option_novisiblehours);

            //Aplico Filtros antes de dibujar
            mFiltro = new Filtro(false,false,false,false);

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
                map.getUiSettings().setMapToolbarEnabled(false);
                map.setMyLocationEnabled(true);

                if (mLocation != null) {
                    try {
                        getLocation(mLocation);

                        USER_LOC = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(USER_LOC, 15   ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Centramos el mapa en BUENOS AIRES
                    USER_LOC = new LatLng(-34.6160275, -58.4333203);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(USER_LOC, 15));
                }

                btn_find_my_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            startLocationUpdate();
                            getLocation(mLocation);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d(LOG_TAG, "Error al ejecutar el DondeCargoService");
                        }
                    }
                });

                btn_open_filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFilterView();
                    }
                });

                /*
                * Switch listeners
                * */
                option_hours.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            mFiltro.isOcultarCerrados();
                            try {
                                getLocation(mLocation);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                option_chargecost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            mFiltro.isOcultarCobraCarga();
                            try {
                                getLocation(mLocation);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                option_noseller.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            mFiltro.isOcultarNoVendeSUBE();
                            try {
                                getLocation(mLocation);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                option_novisiblehours.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            mFiltro.isOcutarHorarioSinIndicar();
                            try {
                                getLocation(mLocation);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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

    @Override
    public void onPause() {
        stopLocationUpdate();
        super.onPause();
    }

    /*
    * Location
    * */
    protected void getLocation(Location location) throws IOException {
        if (location != null) {
            Log.d(LOG_TAG, "El GPS esta encendido");

            btn_open_filter.show();

            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

            mUbicacion = new MiUbicacion();
            mUbicacion.setLongitude(userLocation.longitude);
            mUbicacion.setLatitud(userLocation.latitude);

            getPuntosDeCarga(userLocation.latitude, userLocation.longitude);

        } else {
            Log.d(LOG_TAG, "Error de conexion");
        }
    }

    @Override
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdate();
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
        try {
            getLocation(mLocation);
            stopLocationUpdate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void startLocationUpdate() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(10000);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    protected void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
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
    public void getPuntosDeCarga(double latitude, double longitude) throws IOException {
        Call<List<PuntoCarga>> call = mService.obtenerPuntosCargaPOST(latitude, longitude);
        call.enqueue(new Callback<List<PuntoCarga>>() {
            @Override
            public void onResponse(Call<List<PuntoCarga>> call, Response<List<PuntoCarga>> response) {
                parseGeoData(map, response.body());
            }

            @Override
            public void onFailure(Call<List<PuntoCarga>> call, Throwable t) {
                Log.d(LOG_TAG, "RetrofitError: " + t.getLocalizedMessage());
            }
        });
    }

    private void parseGeoData(final GoogleMap map, List<PuntoCarga> items) {
        MarkerOptions markerOptions = new MarkerOptions();
        Marker mMarker = null;
        mService.setMiFiltro(mFiltro);
        mService.aplicarFiltro(items);

        // limpiamos los puntos antedes de volver a llamarlos
        map.clear();
        markerPuntoCargaMap.clear();

        for (int i = 0; i < items.size(); i++) {
            markerOptions.position(new LatLng(items.get(i).getLatitude(), items.get(i).getLongitude()));
            //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place));
            // Agregamos los marker al mapa.
            mMarker = map.addMarker(markerOptions);
            // Guardamos los datos del marker en un hashMap
            markerPuntoCargaMap.put(mMarker, items.get(i));
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {

                PuntoCarga puntoCarga = markerPuntoCargaMap.get(marker);
                //Uso el Helper para llenar los campos
                DetailHelper detalleHelper = new DetailHelper(puntoCarga);

                // findDetail Elements
                TextView label_direction = (TextView) getActivity().findViewById(R.id.label_direction_detail);
                TextView label_distance = (TextView) getActivity().findViewById(R.id.label_distance_detail);
                TextView label_hours = (TextView) getActivity().findViewById(R.id.label_hours_detail);
                TextView label_seller = (TextView) getActivity().findViewById(R.id.label_seller_detail);
                TextView label_charge_cost = (TextView) getActivity().findViewById(R.id.label_charge_detail);
                TextView label_type = (TextView) getActivity().findViewById(R.id.label_type_detail);
                Button btn_view_route = (Button) getActivity().findViewById(R.id.btn_open_route_view);

                // setLabelValues
                label_direction.setText(detalleHelper.getDireccion());
                label_distance.setText(detalleHelper.getDistancia(mUbicacion));
                label_hours.setText(detalleHelper.getHorario());
                label_charge_cost.setText(detalleHelper.getCobraCarga());
                label_seller.setText(detalleHelper.getVendeSube());
                label_type.setText(detalleHelper.getTipoPunto());

                showDetail();

                btn_view_route.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentMapRoute = new Intent(getContext(), PuntoDeCargaRouteActivity.class);

                        LatLng userLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                        LatLng puntoLatLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);

                        Bundle args = new Bundle();
                        args.putParcelable("USER_LAT_LNG", userLatLng);
                        args.putParcelable("PUNTO_LAT_LNG", puntoLatLng);

                        intentMapRoute.putExtra("bundle", args);

                        getActivity().startActivity(intentMapRoute);
                    }
                });

                return false;
            }
        });


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (detail_view.getVisibility() == View.VISIBLE) {
                    hideDetail();
                } else if (filter_view.getVisibility() == View.VISIBLE) {
                    hideFilterView();
                }
            }
        });

        btn_close_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDetail();
            }
        });

        btn_close_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFilterView();
            }
        });
    }

    private void hideDetail() {
        detail_view.setVisibility(View.INVISIBLE);
        btn_find_my_location.show();
        btn_open_filter.show();
        map.getUiSettings().setScrollGesturesEnabled(true);
        btn_close_detail.hide();
    }

    private void showDetail() {
        map.getUiSettings().setScrollGesturesEnabled(false);
        btn_close_detail.show();
        btn_open_filter.hide();
        btn_find_my_location.hide();
        detail_view.setVisibility(View.VISIBLE);
        ViewAnimator.animate(detail_view)
                .translationY(857, 0)
                .alpha(0,1)
                .descelerate()
                .duration(500)
                .start();
    }

    private void showFilterView() {
        map.getUiSettings().setScrollGesturesEnabled(false);
        btn_close_filter.show();
        btn_open_filter.hide();
        btn_find_my_location.hide();
        filter_view.setVisibility(View.VISIBLE);
        ViewAnimator.animate(filter_view)
                .translationY(852, 0)
                .alpha(0,1)
                .descelerate()
                .duration(500)
                .start();
    }

    private void hideFilterView() {
        btn_close_filter.hide();
        btn_find_my_location.show();
        btn_open_filter.show();
        filter_view.setVisibility(View.INVISIBLE);
        map.getUiSettings().setScrollGesturesEnabled(true);
    }

}
