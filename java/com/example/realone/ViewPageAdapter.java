package com.example.realone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPageAdapter extends FragmentPagerAdapter {

    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        DemoFragment demoFragment = new DemoFragment();
        i = i+1;

        Bundle bundle = new Bundle();
        bundle.putString("message","Fragment :"+i);
        demoFragment.setArguments(bundle);
        switch (i)
        {
            case 1:
                return PageOneFragment.newInstance();
            case 2:
                return PageTwoFragment.newInstance();
            case 3:
                return PageTreeFragment.newInstance();
            case 4:
                return PageFourFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        position = position+1;

        switch (position)
        {
            case 1:
                return "예상정보";
            case 2:
                return "실시간 정보";
            case 3:
                return "사용자 정보";
            case 4:
                return "기타 정보";
              default:
                  return "Fragment "+position;

        }

    }
}
