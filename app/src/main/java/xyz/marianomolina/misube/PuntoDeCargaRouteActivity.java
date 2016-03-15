package xyz.marianomolina.misube;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class PuntoDeCargaRouteActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionCallback {
    // TAG
    private static final String LOG_TAG = PuntoDeCargaRouteActivity.class.getSimpleName();
    private GoogleMap mMap;
    private LatLng userLatLng;
    private LatLng puntoLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punto_de_carga_route);
        initToolbar();

        Bundle bundle = getIntent().getParcelableExtra("bundle");

        if (bundle != null) {
            userLatLng = bundle.getParcelable("USER_LAT_LNG");
            puntoLatLng = bundle.getParcelable("PUNTO_LAT_LNG");
            Log.d(LOG_TAG, "User Lat:" + userLatLng + ", detino: " + puntoLatLng);

            requestDirection();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));
    }

    public void requestDirection() {
        GoogleDirection.withServerKey(getResources().getString(R.string.google_direction_key))
                .from(userLatLng)
                .to(puntoLatLng)
                .transportMode(TransportMode.WALKING)
                .unit(Unit.METRIC)
                .execute(this);
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            mMap.addMarker(new MarkerOptions().position(userLatLng));
            mMap.addMarker(new MarkerOptions().position(puntoLatLng));

            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, ContextCompat.getColor(this, R.color.accent)));
        } else {
            Log.d(LOG_TAG, "ERROR al consultar con la API" + rawBody);
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Log.d(LOG_TAG, "ERROR al consultar con la API");
        Toast.makeText(PuntoDeCargaRouteActivity.this, "ERROR al consultar con la API", Toast.LENGTH_SHORT).show();
    }
}
