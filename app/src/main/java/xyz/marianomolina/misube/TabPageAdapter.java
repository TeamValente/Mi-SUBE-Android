package xyz.marianomolina.misube;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


/**
 * Created by Mariano Molina on 17/2/16.
 * Twitter: @xsincrueldadx
 */
public class TabPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public TabPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SaldoViewFragment();
            case 1:
                return new MapViewFragment();
            case 2:
                return new ConfigViewFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
