package com.steven.idlost.adapters;

import com.steven.idlost.CollectedIdFragament;
import com.steven.idlost.WaiIdFragament;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class IdPagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    private String id;
    public IdPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs=numOfTabs;
        this.id=id;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
               // UserRequestsFragment frReq=new UserRequestsFragment();
                //frReq.setId(id);
                return new WaiIdFragament();
            case 1:
//                UserDonationFragment frDon=new UserDonationFragment();
//                frDon.setId(id);
                return new CollectedIdFragament();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
