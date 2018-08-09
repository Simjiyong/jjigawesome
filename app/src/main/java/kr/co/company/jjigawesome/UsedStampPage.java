package kr.co.company.jjigawesome;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsedStampPage extends android.support.v4.app.Fragment {


    public UsedStampPage() {
        // Required empty public constructor
    }

    public static UsedStampPage newInstance(){
        Bundle args = new Bundle();
        UsedStampPage fragment = new UsedStampPage();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_used_stamp_page, container, false);
    }

}
