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
public class ExtinctionCouponPage extends android.support.v4.app.Fragment {
    View view;
    View v;
    Gson gson = new Gson();
    String url;
    String json;
    SharedPreferences mPrefs;
    Member member;
    PostString postString;
    List<Coupon> coupons = new ArrayList<>();


    private RecyclerView mRecyclerView;

    public ExtinctionCouponPage() {
        // Required empty public constructor
    }

    public static ExtinctionCouponPage newInstance() {
        Bundle args = new Bundle();

        ExtinctionCouponPage fragment = new ExtinctionCouponPage();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            v = inflater.inflate(R.layout.fragment_extinction_coupon_page, container, false);
            mPrefs = this.getActivity().getSharedPreferences("mPrefs", Context.MODE_PRIVATE);
            member = ((Member) SPtoObject.loadObject(mPrefs, "member", Member.class));
            mRecyclerView = (RecyclerView) v.findViewById(R.id.expire_recycler_view);
            postString = new PostString();
            postString.setToken(member.getToken());
            url = "http://18.218.187.138:3000/stamp/hold";
            json = gson.toJson(postString);
            new GetExpiredCouponTask().execute(url, json);
        return v;
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        ExpiredCouponAdapter adapter = new ExpiredCouponAdapter(coupons);
        mRecyclerView.setAdapter(adapter);
    }

    private class GetExpiredCouponTask extends PostTask {
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Response response;
            try {
                Log.d("expire response", s);
                coupons = Arrays.asList(gson.fromJson(s, Coupon[].class));
                if(coupons.size() == 0) {
                    TextView tv = (TextView) v.findViewById(R.id.textview_no_use);
                    tv.setText("소멸 예정 쿠폰이 없습니다.");
                    v.findViewById(R.id.expire_recycler_view).setVisibility(View.GONE);
                    v.findViewById(R.id.textview_no_use).setVisibility(View.VISIBLE);
                }
                else{
                    v.findViewById(R.id.expire_recycler_view).setVisibility(View.VISIBLE);
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

    private class ExpiredCouponAdapter extends RecyclerView.Adapter<ExpiredCouponAdapter.ViewHolder> {
        List<Coupon> coupons = new ArrayList<>();

        public ExpiredCouponAdapter(List<Coupon> coupons) {
            this.coupons = coupons;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_expire_coupon, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Coupon coupon = coupons.get(position);
            holder.expiredCouponName.setText(coupon.getStampname());
          /*  int type = coupon.getType();
            if(type == 0) {
                holder.expiredCouponName.setText("따릉이 1일 이용권");
            }
            else if(type == 1){
                holder.expiredCouponName.setText("경복궁 야간개장 무료관람권");
            }
            else if(type == 2){
                holder.expiredCouponName.setText("국립민속박물관 무료관람권");
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
            cal.add(Calendar.DATE, 90);

            holder.expiredDate.setText(dateFormat.format(cal.getTime()));
        }

        @Override
        public int getItemCount() {
            Log.d("쿠폰 사이즈", Integer.toString(coupons.size()));
            return coupons.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView expiredCouponName;
            TextView expiredDate;

            public ViewHolder(View itemView) {
                super(itemView);
                expiredCouponName = (TextView) itemView.findViewById(R.id.expire_coupon_name);
                expiredDate = (TextView) itemView.findViewById(R.id.expire_coupon_date);
            }
        }
    }
}
