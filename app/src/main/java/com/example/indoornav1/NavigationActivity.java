package com.example.indoornav1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NavigationActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    public TextView tv;
    public String query;
    DatabaseReference databaseReference;
    int bid; //ToDo: To be changed to String
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        query=getIntent().getStringExtra("payload");
        Log.e("SearchQuery", query+ " is null?");
        tv = findViewById(R.id.textView);
        Navigate navigate = new Navigate();
        //TextView tv = findViewById(R.id.textView);
        navigate.execute();

    }

    public class Navigate extends AsyncTask<String,String,String>
    {



        @Override
        protected String doInBackground(String... strings) {
            //REST Query
            callAPI(query);

            return "none";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NavigationActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            tv.setText(s+query);

        }
    }

    private void callAPI(String query) {
        int start,end;
        Log.e("Inside CallAPI",query);
        start=getNearestBeacon();


    }

    private int getNearestBeacon() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference=FirebaseDatabase.getInstance().getReference("indoornav1");
        //DataSnapshot dataSnapshot =

        return 1;
    }
}





//ToDo:set up firebase for Beacon IDs
//toDO:POST call and assign the url to image
//todo: Using that URL, load the image
//todo: Do this whenever the beacon value changes OR every 4s