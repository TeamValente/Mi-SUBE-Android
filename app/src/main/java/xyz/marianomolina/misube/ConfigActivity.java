package xyz.marianomolina.misube;

import android.content.Context;
import android.content.Intent;
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
    private Intent mIntent;
    private String EXTRA_WEBVIEW_URL;
    private String EXTRA_WEBVIEW_TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setWebView Intent
        mIntent = new Intent(ConfigActivity.this, WebViewActivity.class);

        // setVersion
        TextView view_version = (TextView) findViewById(R.id.label_app_version);
        assert view_version != null;
        view_version.setText(setVersionNumber());
        view_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EXTRA_WEBVIEW_URL = "http://misube.com/app/changelog-android.html";
                EXTRA_WEBVIEW_TAG = "changelog";
                mIntent.putExtra("EXTRA_WEBVIEW_URL", EXTRA_WEBVIEW_URL);
                mIntent.putExtra("EXTRA_WEBVIEW_TAG", EXTRA_WEBVIEW_TAG);
                startActivity(mIntent);
            }
        });

        Switch switch_remove_card = (Switch) findViewById(R.id.switch_remove_card);
        assert switch_remove_card != null;
        switch_remove_card.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(LOG_TAG, "Cambio");
            }
        });

        TextView btn_about_us = (TextView) findViewById(R.id.btn_about_us);
        assert btn_about_us != null;
        btn_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EXTRA_WEBVIEW_URL = "http://misube.com/app/about.html";
                EXTRA_WEBVIEW_TAG = "about";
                mIntent.putExtra("EXTRA_WEBVIEW_URL", EXTRA_WEBVIEW_URL);
                mIntent.putExtra("EXTRA_WEBVIEW_TAG", EXTRA_WEBVIEW_TAG);
                startActivity(mIntent);
            }
        });

        TextView btn_oss_libs = (TextView) findViewById(R.id.btn_oss_libs);
        assert btn_oss_libs != null;
        btn_oss_libs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EXTRA_WEBVIEW_URL = "http://misube.com/app/libs-android.html";
                EXTRA_WEBVIEW_TAG = "oss";
                mIntent.putExtra("EXTRA_WEBVIEW_URL", EXTRA_WEBVIEW_URL);
                mIntent.putExtra("EXTRA_WEBVIEW_TAG", EXTRA_WEBVIEW_TAG);
                startActivity(mIntent);
            }
        });

        TextView btn_review = (TextView) findViewById(R.id.btn_review);
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
