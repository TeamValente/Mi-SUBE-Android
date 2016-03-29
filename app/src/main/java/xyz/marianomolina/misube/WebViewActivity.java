package xyz.marianomolina.misube;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        // obtenemos el id desde el otro activity
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        String EXTRA_WEBVIEW_URL = extras.getString("EXTRA_WEBVIEW_URL");
        String EXTRA_WEBVIEW_TAG = extras.getString("EXTRA_WEBVIEW_TAG");
        assert EXTRA_WEBVIEW_TAG != null;

        initToolbar(EXTRA_WEBVIEW_TAG);


        WebView mWebView = (WebView) findViewById(R.id.webViewsComponent);
        assert mWebView != null;
        mWebView.loadUrl(EXTRA_WEBVIEW_URL);
    }

    private void initToolbar(String titleTag) {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            switch (titleTag) {
                case "about":
                    getSupportActionBar().setTitle(R.string.title_webview_about_us);
                    break;
                case "oss":
                    getSupportActionBar().setTitle(R.string.title_webview_oss_libs);
                    break;
                case "changelog":
                    getSupportActionBar().setTitle(R.string.title_webview_changelog);
                    break;
            }
        }

        assert toolbar != null;
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

}
