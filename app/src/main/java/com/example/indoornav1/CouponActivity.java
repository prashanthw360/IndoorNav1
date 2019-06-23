package com.example.indoornav1;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.ArmaRssiFilter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CouponActivity extends AppCompatActivity implements BeaconConsumer {

    public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.ViewHolder>{


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
    String backgroundResponse;
    private List<Coupons> couponList = new ArrayList<>();
    String oldbid;
    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oldbid="none";
        beaconManager = BeaconManager.getInstanceForApplication(this);

        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        //Binding MainActivity to the BeaconService.
        beaconManager.bind(this);

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
        bid = "No Nearby Beacon Found";
        //ToDO: when you get the bid, check if the bid is present in arrayList or not, if present ,reorder it so that latest bid comes first

        Log.e("Details ", "Start Here");




//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callREST = new CallREST();
//                Snackbar.make(v, "Reloading Goodies", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                callREST.execute(bid);
//            }
//        });





    }

    @Override
    public void onBeaconServiceConnect() {

        //Constructing a new Region object to be used for Ranging or Monitoring
        final Region region = new Region("myBeaons", null, null, null);

        //Specifies a class that should be called each time the BeaconService sees or stops seeing a Region of beacons.
        beaconManager.addMonitorNotifier(new MonitorNotifier() {

            /*
                This override method is runned when some beacon will come under the range of device.
            */
            @Override
            public void didEnterRegion(Region region) {
                System.out.println("ENTER ------------------->");
                try {

                    //Tells the BeaconService to start looking for beacons that match the passed Region object
                    // , and providing updates on the estimated mDistance every seconds while beacons in the Region
                    // are visible.
                    beaconManager.startRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            /*
                 This override method is runned when beacon that comes in the range of device
                 ,now been exited from the range of device.
             */
            @Override
            public void didExitRegion(Region region) {
                System.out.println("EXIT----------------------->");
                try {

                    //Tells the BeaconService to stop looking for beacons
                    // that match the passed Region object and providing mDistance
                    // information for them.
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }


            /*
                 This override method will Determine the state for the device , whether device is in range
               of beacon or not , if yes then i = 1 and if no then i = 0
            */
            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                System.out.println("I have just switched from seeing/not seeing beacons: " + state);
            }
        });

        final String TAG="onBeacon";
        //Specifies a class that should be called each time the BeaconService gets ranging data,
        // which is nominally once per second when beacons are detected.
        beaconManager.addRangeNotifier(new RangeNotifier() {

            /*
               This Override method tells us all the collections of beacons and their details that
               are detected within the range by device
             */
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Log.e(TAG, "inside beacon1");
                // Checking if the Beacon inside the collection (ex. list) is there or not

                // if Beacon is detected then size of collection is > 0
                if (beacons.size() > 0) {

                    final ArrayList<ArrayList<String>> arrayList = new ArrayList<ArrayList<String>>();
                    BeaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);
                    // Iterating through all Beacons from Collection of Beacons
                    ArrayList<Integer> arr = new ArrayList<Integer>();
                    int count = 0;
                    for (Beacon b : beacons) {
                        //RSSI
                        count += 1;
                        Log.e("Count : " ," "+count);
                        Integer rssi = Integer.valueOf(b.getRssi());
                        arr.add(rssi);

                        Integer maxRssi = Collections.max(arr);
                        for(Beacon b1 : beacons){

                            if(b1.getRssi() == maxRssi) {
                                bid = b1.getId1().toString();
                                if (!oldbid.equals(bid)) {
                                    try {

                                        callAPI(new couponCallBack() {
                                            @Override
                                            public void onSuccess(String ads, String cname, String result) {
                                                Log.e("Details are ", ads + cname + result);
                                                Coupons coupons = new Coupons(cname, result);
                                                if (!couponList.contains(coupons)) {
                                                    couponList.add(coupons);
                                                    Toast.makeText(getApplicationContext(), "No new Coupon added", Toast.LENGTH_SHORT).show();
                                                }
                                                textView.setText(ads);
                                                couponsAdapter.notifyDataSetChanged();
                                            }
                                        }, bid);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    oldbid=bid;

                                }
                            }
                        }
                    }
                }
            }
        });
        try {

            //Tells the BeaconService to start looking for beacons that match the passed Region object.
            beaconManager.startMonitoringBeaconsInRegion(region);
        } catch (RemoteException e) {
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("On Destroyed Called","Here");
        beaconManager.unbind(this);

    }





    public interface couponCallBack{
        void onSuccess(String ads, String cname, String result);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Details ", "Onstart");
        try {
            callAPI(new couponCallBack(){
                @Override
                public void onSuccess(String ads, String cname, String result) {
                    Log.e("Details are ",ads+cname+result);
                    Coupons coupons = new Coupons(cname, result);
                    if (!couponList.contains(coupons)) {
                        couponList.add(coupons);
                        Toast.makeText(getApplicationContext(), "No new Coupon added", Toast.LENGTH_SHORT).show();
                    }
                    textView.setText(ads);
                    couponsAdapter.notifyDataSetChanged();
                }
            }, bid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void callAPI(final couponCallBack couponCallBack, String bid) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.e("Details ", "inCallAPI");
        if(bid.equals("No Nearby Beacon Found")){

            //volleyCallback.onSuccess("https://xbosoft.com/wp-content/uploads/2017/09/API-Testing-Services_1-400x304.jpg");
        }else {

            jsonObject.put("bid", bid);
            Log.e("Bid is ",bid);
            String url = "https://suhas-api.herokuapp.com/marketing";
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response is ", response.toString());
                            try {

                                couponCallBack.onSuccess(response.getJSONObject("response").getString("ads")
                                ,response.getJSONObject("response").getString("cname"),
                                        response.getJSONObject("response").getString("coupons"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error is ", error.toString());
                }
            });

            queue.add(postRequest);
        }


    }
}

