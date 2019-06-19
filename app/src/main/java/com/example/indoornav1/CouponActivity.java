package com.example.indoornav1;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CouponActivity extends AppCompatActivity {

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.ViewHolder>{


    private List<Coupons> couponsList;
    int rand;
    String color[] = {"#e57373", "#F06292", "#BA68C8","#7986CB","#4FC3F7", "#81C784","#FFF176","#FF8A65","#A1887F","#AFB42B"};
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.coupon_list,viewGroup,false);
            return new ViewHolder(itemview);
    }

    public CouponsAdapter(List<Coupons> couponsList) {
        this.couponsList = couponsList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Coupons coupons = couponsList.get(i);
        viewHolder.ctl.setText(coupons.getCtitle());
        viewHolder.cdesc.setText(coupons.getCdesc());
        rand= new Random().nextInt(10);
        viewHolder.cdesc.setBackgroundColor(Color.parseColor(color[rand]));
        viewHolder.ctl.setBackgroundColor(Color.parseColor(color[(rand+3)%10]));


    }

    @Override
    public int getItemCount() {
        return couponsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView ctl;
        public TextView cdesc;
        Typeface descfont;

        public ViewHolder(View view){
            super(view);
            ctl=view.findViewById(R.id.title);
            cdesc=view.findViewById(R.id.description);
            descfont=Typeface.createFromAsset(getAssets(),"GrandHotel-Regular.otf");
            cdesc.setTypeface(descfont);
        }
    }

}

    private RecyclerView recyclerView;
    private CouponsAdapter couponsAdapter;
    String ad;
    String ctl;
    String cdesc;
    String bid;
    private List<Coupons> couponList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Coupons and Premiums");
        setContentView(R.layout.activity_coupon);
        recyclerView=findViewById(R.id.coupon);

        couponsAdapter=new CouponsAdapter(couponList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mLayoutManager); //ToDo: GridLayoutManager and Item Animator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(couponsAdapter);
        bid="Get the bid here";
        //ToDO: when you get the bid, check if the bid is present in arrayList or not, if present ,reorder it so that latest bid comes first
        try {
            prepareData();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void prepareData() throws JSONException {

        couponList=new ArrayList<>();

//        Coupons coupons = new Coupons("C1","CDESC");
//        couponList.add(coupons);
//
//        coupons=new Coupons("C2","C2DESC");
//        couponList.add(coupons);
//
//        coupons=new Coupons("C3","C3DESC");
//        couponList.add(coupons);
//
//        coupons=new Coupons("C4","C5DESC");
//        couponList.add(coupons);
//        coupons=new Coupons("C6","C26ESC");
//        couponList.add(coupons);
//
//        coupons=new Coupons("C3","C2D3SC");
//        couponList.add(coupons);
//
//        coupons=new Coupons("C231","CdaDESC");
//        couponList.add(coupons);


        JSONObject jsonObject=callAPI();
        ad=jsonObject.getString("ad");
        ctl=jsonObject.getString("ca");
        cdesc=jsonObject.getString("da");

        Coupons coupons = new Coupons(ctl,cdesc);
        couponList.add(coupons);

        couponsAdapter.notifyDataSetChanged();
    }

    JSONObject jsonObject;
    private JSONObject callAPI() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://suhas-api.herokuapp.com/map",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonObject=new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("In CouponActivity CallAPI", error.toString());

                    }
                })
        {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("bid", bid);
                        return params;
                    }
        };

        return jsonObject;

    }

}
