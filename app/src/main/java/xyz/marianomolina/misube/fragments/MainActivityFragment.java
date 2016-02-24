package xyz.marianomolina.misube.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.marianomolina.misube.R;
import xyz.marianomolina.misube.adapters.TabPageAdapter;

/**
 * Created by Mariano Molina on 17/2/16.
 * Twitter: @xsincrueldadx
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Find button
        final FloatingActionButton btn_find_my_location = (FloatingActionButton) getActivity().findViewById(R.id.btn_find_my_location);
        btn_find_my_location.hide();

        // config TabLayout
        TabLayout mTabLayout = (TabLayout) getActivity().findViewById(R.id.masterTab);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_saldo));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_mapa));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager mViewPager = (ViewPager) getActivity().findViewById(R.id.masterViewPager);
        // set adapter to viewPager
        final TabPageAdapter mTabPageAdapter = new TabPageAdapter(getFragmentManager(), mTabLayout.getTabCount());
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
}
