package xyz.marianomolina.misube;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class ConfigActivity extends AppCompatActivity {
    // LOG_TAG
    private static final String LOG_TAG = ConfigActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setVersion
        TextView view_version = (TextView) findViewById(R.id.label_app_version);
        assert view_version != null;
        view_version.setText(setVersionNumber());


        Switch switch_remove_card = (Switch) findViewById(R.id.switch_remove_card);
        assert switch_remove_card != null;
        switch_remove_card.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(LOG_TAG, "Cambio");
            }
        });

        Button btn_about_us = (Button) findViewById(R.id.btn_about_us);
        assert btn_about_us != null;
        btn_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "BTN About us");
            }
        });

        Button btn_oss_libs = (Button) findViewById(R.id.btn_oss_libs);
        assert btn_oss_libs != null;
        btn_oss_libs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "BTN oss libs");
            }
        });

        Button btn_review = (Button) findViewById(R.id.btn_review);
        assert btn_review != null;
        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "BTN review");
            }
        });

    }

    /**
     * Set appVersionNumber.
     */
    private String setVersionNumber() {
        Context context = getApplicationContext();
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();

        String myVersionName = "not available";

        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "v" + myVersionName;
    }

}
