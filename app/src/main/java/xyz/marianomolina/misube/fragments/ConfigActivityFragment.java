package xyz.marianomolina.misube.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.marianomolina.misube.R;
import xyz.marianomolina.misube.WebViewActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConfigActivityFragment extends Fragment {
    private static final String LOG_TAG = ConfigActivityFragment.class.getSimpleName();

    @Bind(R.id.label_app_version) TextView view_version;
    @Bind(R.id.switch_remove_card) Switch switch_remove_card;
    @Bind(R.id.btn_about_us) TextView btn_about_us;
    @Bind(R.id.btn_oss_libs) TextView btn_oss_libs;
    @Bind(R.id.btn_review) TextView btn_review;

    private Intent mIntent;
    private String EXTRA_WEBVIEW_URL;
    private String EXTRA_WEBVIEW_TAG;

    public ConfigActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // setWebView Intent
        mIntent = new Intent(getContext(), WebViewActivity.class);

        // setVersion
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

        switch_remove_card.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(LOG_TAG, "Cambio");
            }
        });

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

        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "BTN review");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * Set appVersionNumber.
     */
    private String setVersionNumber() {
        Context context = getContext();
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
