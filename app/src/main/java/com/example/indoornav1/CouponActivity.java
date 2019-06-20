package com.example.indoornav1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class CouponActivity extends AppCompatActivity {

    public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.ViewHolder> {


        private List<Coupons> couponsList;
        int rand;
        String color[] = {"#e57373", "#F06292", "#BA68C8", "#7986CB", "#4FC3F7", "#81C784", "#FFF176", "#FF8A65", "#A1887F", "#AFB42B"};

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.coupon_list, viewGroup, false);
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
            rand = new Random().nextInt(10);
            viewHolder.cdesc.setBackgroundColor(Color.parseColor(color[rand]));
            viewHolder.ctl.setBackgroundColor(Color.parseColor(color[(rand + 3) % 10]));


        }

        @Override
        public int getItemCount() {
            return couponsList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView ctl;
            public TextView cdesc;
            Typeface descfont;

            public ViewHolder(View view) {
                super(view);
                ctl = view.findViewById(R.id.title);
                cdesc = view.findViewById(R.id.description);
                descfont = Typeface.createFromAsset(getAssets(), "GrandHotel-Regular.otf");
                cdesc.setTypeface(descfont);
            }
        }

    }

    private RecyclerView recyclerView;
    ProgressDialog progressDialog;
    private CouponsAdapter couponsAdapter;
    FloatingActionButton floatingActionButton;
    TextView textView;
    String ad;
    String ctl;
    String cdesc;
    String bid;
    CallREST callREST;
    String backgroundResponse;
    private List<Coupons> couponList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Coupons and Premiums");
        setContentView(R.layout.activity_coupon);
        recyclerView = findViewById(R.id.coupon);
        textView = findViewById(R.id.ad);
        couponsAdapter = new CouponsAdapter(couponList);
        floatingActionButton = findViewById(R.id.fab);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager); //ToDo: GridLayoutManager and Item Animator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(couponsAdapter);
        bid = "Get the bid here";
        //ToDO: when you get the bid, check if the bid is present in arrayList or not, if present ,reorder it so that latest bid comes first

        callREST = new CallREST();
        bid = "5";
        callREST.execute(bid);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callREST = new CallREST();
                Snackbar.make(v, "Reloading Goodies", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                callREST.execute(bid);
            }
        });


    }

    public class CallREST extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {
            bid = strings[0];
            Log.e("CouponActivity", "DIB " + bid);
            if(isCancelled())
                return "Cancelled";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("bid", bid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            URL url = null;
            try {
                url = new URL("https://suhas-api.herokuapp.com/marketing");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                connection.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", String.valueOf(jsonObject.toString().getBytes().length));
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("Content-Language", "en-US");
            //Send Request
            OutputStream outputStream = null;
            try {
                outputStream = connection.getOutputStream();
                outputStream.write(jsonObject.toString().getBytes());
                outputStream.flush();
                outputStream.close();
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();

                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                Log.e("CouponActivity", "DIB2 " + response.toString());
//                publishProgress(response.toString());
                backgroundResponse = response.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return backgroundResponse;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CouponActivity.this);
            progressDialog.setMessage("Goodies on the way!");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //couponList = new ArrayList<>();
            progressDialog.dismiss();
            Log.e("CouponsActivity", "onPostExecute " + s);
            try {
                JSONObject jsonObject = new JSONObject(s).getJSONObject("response");
                Log.e("CouponsActivity", jsonObject.toString());
                ad = jsonObject.getString("ads");
                ctl = jsonObject.getString("cname");
                cdesc = jsonObject.getString("coupons");
                Coupons coupons = new Coupons(ctl, cdesc);
                if (!couponList.contains(coupons))
                    couponList.add(coupons);
                textView.setText(ad);

                couponsAdapter.notifyDataSetChanged();
                cancel(true);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            callREST.cancel(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}

