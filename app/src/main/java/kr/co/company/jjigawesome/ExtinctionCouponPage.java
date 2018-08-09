package kr.co.company.jjigawesome;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExtinctionCouponPage extends android.support.v4.app.Fragment {


    public ExtinctionCouponPage() {
        // Required empty public constructor
    }

    public static ExtinctionCouponPage newInstance(){
        Bundle args = new Bundle();

        ExtinctionCouponPage fragment = new ExtinctionCouponPage();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_extinction_coupon_page, container, false);
    }

}
