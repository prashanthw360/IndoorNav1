package com.example.indoornav1;

import android.content.Intent;
import  android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CBResults extends AppCompatActivity {
    TextView textView;
    Spinner spinner;
    Toolbar toolbar;
    String search;
    String category;

    private List<Store> storeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private StoreAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbresults);
        search = getIntent().getStringExtra("search");
        category=getIntent().getStringExtra("category");
        Log.e("CBResults ", search+ " "+category);



        spinner = findViewById(R.id.sort);
        toolbar = findViewById(R.id.cbtoolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //ToDo: Position the spinner to the right
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CBResults.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.cbmenu));
        spinner.setAdapter(arrayAdapter);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                callAPI(new callBack() {
                    @Override
                    public void onReceive(final JSONObject result) throws JSONException {
                        //Callback has been done
                        Log.e("CBResults", result.toString());
                        prepareStoreData(result.getJSONObject("response").getJSONArray("price_wise"));
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(CBResults.this,
                                        spinner.getSelectedItem().toString(),
                                        Toast.LENGTH_SHORT).show();
                                if(parent.getItemAtPosition(position).equals("Sort by Price")) {
                                    try {
                                        prepareStoreData(result.getJSONObject("response").getJSONArray("price_wise"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                if(parent.getItemAtPosition(position).equals("Sort by Ratings")){
                                    try {
                                        prepareStoreData(result.getJSONObject("response").getJSONArray("price_wise"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {


                            }
                        });

                    }
                }, search, category);
                handler.postDelayed(this,5000);
                Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
            }

        };


        handler.postDelayed(runnable,5000);

        sAdapter = new StoreAdapter(storeList);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(sAdapter);

        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Store store = storeList.get(position);
                Toast.makeText(getApplicationContext(), store.getSid() + " is selected!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CBResults.this, NavigationActivity.class);
                intent.putExtra("payload", store.getSid());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //prepareStoreData();

    }

    private void callAPI(final callBack cb, String search, String category) {

        RequestQueue requestQueue = Volley.newRequestQueue(CBResults.this);
        Log.e("S&C is ", search+ " "+category);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://suhas-api.herokuapp.com/search?item="+search+"&category="+category, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            cb.onReceive(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);



    }

    /**
     * Prepares sample data to provide data set to adapter
     */
    private void prepareStoreData(JSONArray jsonArray) throws JSONException {
        Log.e("CB Results ",jsonArray.toString());
        Store store;
        if(!storeList.isEmpty()){
            storeList.clear();
        }
        JSONArray items= new JSONArray();
        for(int i=0;i<jsonArray.length();i++){
            items=jsonArray.getJSONArray(i);
            store=new Store(items.getString(0), items.getString(1), items.getString(2), items.getString(3));
            storeList.add(store);
        }

        //Store store = new Store("s1", "50", "5", "1kg");
        //storeList.add(store);

        sAdapter.notifyDataSetChanged();

    }

    interface callBack{
        public void onReceive(JSONObject result) throws JSONException;
    }

}
