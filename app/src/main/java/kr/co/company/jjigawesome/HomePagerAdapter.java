package kr.co.company.jjigawesome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by sjy on 2018-07-23.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {
    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }
    private static int PAGE_NUMBER = 2;
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return UsedStampPage.newInstance();
            case 1:
                return ExtinctionCouponPage.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "스탬프 사용 내역";
            case 1:
                return "소멸 예정 쿠폰";
            default:
                return null;
        }
    }
}
