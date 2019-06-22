package com.example.indoornav1;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static javax.xml.transform.OutputKeys.MEDIA_TYPE;

public class NavigationActivity extends AppCompatActivity implements BeaconConsumer {
    ProgressDialog progressDialog;
    public TextView tv;

    public String start;
    public String end;
    public String string2;
    public String string2old;
    String bid; //ToDo: To be changed to String
    String imageURL;
    String navStatus;
    private BeaconManager beaconManager;
    RequestQueue queue;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        beaconManager = BeaconManager.getInstanceForApplication(this);

        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        //Binding MainActivity to the BeaconService.
        beaconManager.bind(this);
        string2="No Nearby Beacon Found";
        end=getIntent().getStringExtra("payload");
        Toast.makeText(getApplicationContext(), end, Toast.LENGTH_SHORT).show();
        Log.e("EndBID", "End BID "+end);
        tv = findViewById(R.id.textView);
        ;
        queue= Volley.newRequestQueue(this);

    }


    private void callAPI(final VolleyCallback volleyCallback, String start, String end) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        if(start.equals("No Nearby Beacon Found")){
            volleyCallback.onSuccess("https://en.wikipedia.org/wiki/Blue_rose#/media/File:Blue_rose-artificially_coloured.jpg");
        }else {

            jsonObject.put("src", start);
            jsonObject.put("dest", end);
            Log.e("Start and End is ", start + " " + end);
            String url = "https://suhas-api.herokuapp.com/map";
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response is ", response.toString());
                            try {
                                volleyCallback.onSuccess(response.getString("response"));
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

    public interface VolleyCallback{
        void onSuccess(String result);
    }






    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(),"OnStart Started",Toast.LENGTH_LONG).show();

        try {
            callAPI(new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    tv.setText(string2);
                    imageView=findViewById(R.id.mapImage);
                    Log.e("Image Published", result);
                    Glide.with(getApplicationContext())
                            .load(result)
                            .placeholder(R.drawable.common_google_signin_btn_icon_dark_normal_background)
                            .error(R.drawable.common_google_signin_btn_icon_dark)
                            .into(imageView);

                }
            }, string2,end);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
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

                            if(b1.getRssi() == maxRssi)
                            {
                                string2 = b1.getId1().toString();
                                tv.setText(string2);
                                if(!string2.equals(string2old)){
                                    try {
                                        callAPI(new VolleyCallback() {
                                            @Override
                                            public void onSuccess(String result) {
                                                tv.setText(string2);
                                                imageView=findViewById(R.id.mapImage);
                                                Log.e("Image Published", result);
                                                Toast.makeText(getApplicationContext(), "Beacon Change "+string2, Toast.LENGTH_LONG).show();
                                                Glide.with(getApplicationContext())
                                                        .load(result)
                                                        .placeholder(R.drawable.common_google_signin_btn_icon_dark_normal_background)
                                                        .error(R.drawable.common_google_signin_btn_icon_dark)
                                                        .into(imageView);

                                            }
                                        }, string2, end);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    string2old=string2;
                                }
                                Log.e("NavigationActivity","UUID "+string2);
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

//    private void fetchImage(String string2) {
//        if(navigate.getStatus()==AsyncTask.Status.RUNNING) {
//            Log.e("New Beacon Detected ", string2);
//            navigate.execute(string2,end);
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("On Destroyed Called","Here");
        beaconManager.unbind(this);

    }


}




//toDo: Center the image while its loading
//Todo: Handle all the errors in the app. Handle the communication erros by reading the error variable in JSON response
//todo: Do this whenever the beacon value changes OR every 4s