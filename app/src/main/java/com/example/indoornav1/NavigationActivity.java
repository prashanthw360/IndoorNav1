package com.example.indoornav1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
import java.util.HashMap;
import java.util.Map;

public class NavigationActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    public TextView tv;
    public ImageView imageView;
    public String start;
    public String end;
    DatabaseReference databaseReference;
    DataSnapshot dataSnapshot;
    private NetworkImageView mNetworkImageView;
    private ImageLoader mImageLoader;
    String bid; //ToDo: To be changed to String
    String imageURL;
    String navStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        //start=getNearestBeacon();
        start="bid1";

        end=getIntent().getStringExtra("payload");
        Log.e("SearchQuery", end+ " is null?");
        tv = findViewById(R.id.textView);
        //ImageView imageView=findViewById(R.id.mapImage);
        //navStatus=new HashMap<>();
        Navigate navigate = new Navigate();
        navigate.execute(start,end);


    }


    public class Navigate extends AsyncTask<String,String,String>
    {



        @Override
        protected String doInBackground(String... strings) {
            //REST Query
            int i=0;
            try {

                do{
                    if(strings[0]==strings[1]) {//exit the loop
                    }
                    navStatus=callAPI(strings[0],strings[1]);
                    Log.e("Order","After callAPI.Data is "+navStatus);
                    //For Testing Purpose
                    if(i==1){ navStatus="https://res.infoq.com/presentations/rest-api-testing-postman/en/slides/sl1-1542245696017.jpg";}
                    if(i==2){navStatus="https://octoperf.com/img/blog/jmeter-rest-api-testing/jmeter-rest-api-testing.jpg";}
                    publishProgress(navStatus);
                    i++;
               }while(i<3);

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
            progressDialog.setMessage("Maps Loading");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "onProgressUpdate",Toast.LENGTH_SHORT).show();
            mNetworkImageView = (NetworkImageView) findViewById(R.id.mapImage);
            mImageLoader = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getImageLoader();
            mImageLoader.get(values[0], ImageLoader.getImageListener(mNetworkImageView, R.mipmap.ic_launcher_round, android.R.drawable.ic_menu_report_image));
            Log.e("Image Published", values[0]);
            mNetworkImageView.setImageUrl(values[0], mImageLoader);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            tv.setText(s);
            Log.e("Data ","Data is"+s);
//                mNetworkImageView = (NetworkImageView) findViewById(R.id.mapImage);
//                mImageLoader = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getImageLoader();
//                mImageLoader.get(s, ImageLoader.getImageListener(mNetworkImageView, R.mipmap.ic_launcher_round, android.R.drawable.ic_menu_report_image));
//                mNetworkImageView.setImageUrl(s, mImageLoader);

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
        Log.e("Order","Step-2");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(),"OnResume STarted",Toast.LENGTH_LONG).show();
        Log.e("Order","Step-2");
    }
}





//ToDo:set up firebase for Beacon IDs
//toDO:POST call and assign the url to image
//todo: Using that URL, load the image
//todo: Do this whenever the beacon value changes OR every 4s