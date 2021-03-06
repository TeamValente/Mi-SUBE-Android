package xyz.marianomolina.misube;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import xyz.marianomolina.misube.adapters.TabPageAdapter;

/**
 * Created by Mariano Molina on 17/2/16.
 * Twitter: @xsincrueldadx
 */
public class MainActivity extends AppCompatActivity {
    // Bind Views
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.btn_find_my_location) FloatingActionButton btn_find_my_location;
    @Bind(R.id.btn_open_filter) FloatingActionButton btn_open_filter;
    @Bind(R.id.btn_close_detail) FloatingActionButton btn_close_detail;
    @Bind(R.id.detail_view) LinearLayout detail_view;
    @Bind(R.id.filter_view) LinearLayout filter_view;
    @Bind(R.id.masterTab) TabLayout mTabLayout;
    @Bind(R.id.masterViewPager) ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        detail_view.setVisibility(View.INVISIBLE);
        filter_view.setVisibility(View.INVISIBLE);

        btn_find_my_location.hide();
        btn_open_filter.hide();
        btn_close_detail.hide();

        // config TabLayout
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_saldo));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_mapa));

        // tabs sin label
        //mTabLayout.addTab(mTabLayout.newTab());
        //mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // set adapter to viewPager
        final TabPageAdapter mTabPageAdapter = new TabPageAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mTabPageAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        btn_find_my_location.hide();
                        btn_open_filter.hide();
                        btn_close_detail.hide();
                        break;
                    case 1:
                        btn_find_my_location.show();
                        btn_open_filter.show();
                        btn_close_detail.hide();
                        break;
                }
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        /*
        * Set Icons for tabs
        * */
        TabLayout.Tab tabMiCuenta = mTabLayout.getTabAt(0);
        assert tabMiCuenta != null;
        tabMiCuenta.setIcon(R.drawable.selector_credit_card);

        TabLayout.Tab tabMap = mTabLayout.getTabAt(1);
        assert tabMap != null;
        tabMap.setIcon(R.drawable.selector_map);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intentConfig = new Intent(this, ConfigActivity.class);
            startActivity(intentConfig);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
