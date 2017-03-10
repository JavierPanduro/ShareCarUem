package com.aja.proyectointegrado;

/**
 * Created by Javier on 12/01/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PublicarViaje tab1 = new PublicarViaje();
                return tab1;
            case 1:
                MisViajes tab2 = new MisViajes();
                return tab2;
            case 2:
                BuscarViaje tab3 = new BuscarViaje();
                return tab3;
            case 3:
                MisReservas tab4 = new MisReservas();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}