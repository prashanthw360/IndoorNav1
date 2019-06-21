package com.example.indoornav1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NavigationActivity extends AppCompatActivity implements BeaconConsumer {
    ProgressDialog progressDialog;
    public TextView tv;
    public ImageView imageView;
    public String start;
    public String end;
    public String string2;
    DatabaseReference databaseReference;
    DataSnapshot dataSnapshot;
    private NetworkImageView mNetworkImageView;
    private ImageLoader mImageLoader;
    String bid; //ToDo: To be changed to String
    String imageURL;
    String navStatus;
    private BeaconManager beaconManager;
    Navigate navigate;



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
        string2="2";
        ImageView imageView=findViewById(R.id.mapImage);
        end=getIntent().getStringExtra("payload");
        Log.e("SearchQuery", end+ " is null?");
        tv = findViewById(R.id.textView);
        navigate = new Navigate();
        navigate.execute("bid1",end);


    }

    public class Navigate extends AsyncTask<String,String,String>
    {



        @Override
        protected String doInBackground(String... strings) {
            String cs,ps=null;
            try {
                    //while(!string2.equals(strings[1]) ) {
                        Log.e("NavigationActivity","UUID "+string2);
                        tv.setText(string2);
                        cs=string2;  //---(2)
                        //ToDo: Uncomment the below statement later
                        if(cs.equals(ps)) {
                            if(navStatus==null)
                            {
                                Log.e("NavigationActivity", "NavStatus null");
                            }
                        }
                        else {
                            navStatus = callAPI("bid1", "bid3");
                            Log.e("Order", "After callAPI.Data is " + navStatus);
                            ps = cs;
                            publishProgress(navStatus);
                        }

                 //   }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //ToDo: If imageURL is null then server is down probably
            Log.e("Data ","Data is"+imageURL);
            return "none";
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NavigationActivity.this);
            imageView = findViewById(R.id.mapImage);


        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            publishImage(values[0]);

        }

        private void publishImage(String value) {

            Toast.makeText(getApplicationContext(), "onProgressUpdate",Toast.LENGTH_SHORT).show();
            mNetworkImageView = (NetworkImageView) findViewById(R.id.mapImage);
            mImageLoader = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getImageLoader();
            mImageLoader.get(value, ImageLoader.getImageListener(mNetworkImageView, R.mipmap.ic_launcher_round, android.R.drawable.ic_menu_report_image));
            Log.e("Image Published", value);
            mNetworkImageView.setImageUrl(value, mImageLoader);
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("Data ","Data is"+s);

        }


    }



    private String callAPI(String start,String end) throws IOException, JSONException {
        Log.e("Inside CallAPI",start);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("src",start);
            jsonObject.put("dest",end);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        URL url=new URL("https://suhas-api.herokuapp.com/map");
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("Content-Length", String.valueOf(jsonObject.toString().getBytes().length));
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Content-Language", "en-US");
        //Send Request
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(jsonObject.toString().getBytes());
        outputStream.flush();
        outputStream.close();

        //Get Response
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();


        String imageLink= response.toString();
        Log.e("In callAPI", imageLink);
        imageLink = new JSONObject(imageLink).getString("response");
        Log.e("Order","Step-1");
        return imageLink;


    }


    private String getNearestBeacon() {



        return "1";
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(),"OnStart STarted",Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(),"OnResume STarted",Toast.LENGTH_LONG).show();
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
                        System.out.println("Count : " + count);
                        Integer rssi = Integer.valueOf(b.getRssi());
                        arr.add(rssi);

                        Integer maxRssi = Collections.max(arr);
                        for(Beacon b1 : beacons){

                            if(b1.getRssi() == maxRssi)
                            {
                                string2 = b1.getId1().toString();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Unbinds an Android Activity or Service to the BeaconService to avoid leak.
        Log.e("On Destroyed Called","Here");
        beaconManager.unbind(this);
        finish();

    }


}




//toDo: Center the image while its loading
//Todo: Handle all the errors in the app. Handle the communication erros by reading the error variable in JSON response
//todo: Do this whenever the beacon value changes OR every 4s