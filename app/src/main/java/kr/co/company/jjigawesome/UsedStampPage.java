package kr.co.company.jjigawesome;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsedStampPage extends android.support.v4.app.Fragment {
    int newUiOptions;
    View v;
    View view;
    Gson gson = new Gson();
    String url;
    String json;
    SharedPreferences mPrefs;
    Member member;
    PostString postString;
    List<Coupon> coupons = new ArrayList<>();


    private RecyclerView mRecyclerView;

    public UsedStampPage() {
        // Required empty public constructor
    }

    public static UsedStampPage newInstance() {
        Bundle args = new Bundle();
        UsedStampPage fragment = new UsedStampPage();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_used_stamp_page, container, false);
        mPrefs = this.getActivity().getSharedPreferences("mPrefs", Context.MODE_PRIVATE);
        member = ((Member) SPtoObject.loadObject(mPrefs, "member", Member.class));
        mRecyclerView = (RecyclerView) v.findViewById(R.id.used_recycler_view);
        postString = new PostString();
        postString.setToken(member.getToken());
        url = "http://18.218.187.138:3000/stamp/used";
        json = gson.toJson(postString);
        new GetUsedCouponTask().execute(url, json);
        Log.d("리스폰", Integer.toString(coupons.size()));
        return v;
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        UsedCouponAdapter adapter = new UsedCouponAdapter(coupons);
        mRecyclerView.setAdapter(adapter);
    }

    private class GetUsedCouponTask extends PostTask {
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Response response;
            try {
                Log.d("response", s);
                coupons = Arrays.asList(gson.fromJson(s, Coupon[].class));
                if(coupons.size() == 0) {
                    v.findViewById(R.id.used_recycler_view).setVisibility(View.GONE);
                    v.findViewById(R.id.textview_no_use).setVisibility(View.VISIBLE);
                }
                else{
                    v.findViewById(R.id.used_recycler_view).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.textview_no_use).setVisibility(View.GONE);
                }
                setRecyclerView();
            } catch (NullPointerException e) {
                Toast.makeText(getActivity(), "오류! 서버로부터 응답 받지 못함", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "오류!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }


    private class UsedCouponAdapter extends RecyclerView.Adapter<UsedCouponAdapter.ViewHolder> {
        List<Coupon> coupons = new ArrayList<>();

        public UsedCouponAdapter(List<Coupon> coupons) {
            this.coupons = coupons;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_used_coupon, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Coupon coupon = coupons.get(position);
            holder.usedCouponName.setText(coupon.getStampname());
            int stampNum = coupon.getRemarks() * -1;
            holder.usedStampCount.setText("스탬프 " + stampNum + "개");
            /*if(type == 0) {
                holder.usedCouponName.setText("1111");
                holder.usedStampCount.setText("스탬프 5개");
            }
            else if(type == 1){
                holder.usedCouponName.setText("경복궁 야간개장 무료관람권");
                holder.usedStampCount.setText("스탬프 20개");
            }
            else if(type == 2){
                holder.usedCouponName.setText("국립민속박물관 무료관람권");
                holder.usedStampCount.setText("스탬프 30개");
            }*/
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = dateFormat.parse(coupon.getCreateDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            holder.usedDate.setText(dateFormat.format(cal.getTime()));
        }

        @Override
        public int getItemCount() {
            Log.d("사용내역 사이즈", Integer.toString(coupons.size()));
            return coupons.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView usedCouponName;
            TextView usedStampCount;
            TextView usedDate;

            public ViewHolder(View itemView) {
                super(itemView);
                usedCouponName = (TextView) itemView.findViewById(R.id.used_coupon_name);
                usedStampCount = (TextView) itemView.findViewById(R.id.used_stamp_count);
                usedDate = (TextView) itemView.findViewById(R.id.used_coupon_date);
            }
        }
    }
}