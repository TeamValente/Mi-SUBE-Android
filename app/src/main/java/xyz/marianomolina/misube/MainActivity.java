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

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import xyz.marianomolina.misube.adapters.TabPageAdapter;

/**
 * Created by Mariano Molina on 17/2/16.
 * Twitter: @xsincrueldadx
 */
public class MainActivity extends AppCompatActivity {

    /*
    * TODO: Hay que implementar el permission dispacher para controlar los permisos de la app. Ahora estan dentro del MapViewFragment.java
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find button
        final FloatingActionButton btn_find_my_location = (FloatingActionButton) findViewById(R.id.btn_find_my_location);
        btn_find_my_location.hide();

        // config TabLayout
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.masterTab);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_saldo));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_mapa));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager mViewPager = (ViewPager) findViewById(R.id.masterViewPager);
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
                        break;
                    case 1:
                        btn_find_my_location.show();
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
