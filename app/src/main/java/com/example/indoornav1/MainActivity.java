package com.example.indoornav1;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.BaseBundle;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




class NavItem {
    String mTitle;
    String mSubtitle;
    int mIcon;

    public NavItem(String title, String subtitle, int icon) {
        mTitle = title;
        mSubtitle = subtitle;
        mIcon = icon;
    }
}

class DrawerListAdapter extends BaseAdapter{
    Context mContext;
    ArrayList<NavItem> mNavItems;
    public DrawerListAdapter(Context context, ArrayList<NavItem> navItems){
        mContext=context;
        mNavItems=navItems;

    }
    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_item,null);

        }else{
            view=convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.title);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon);

        TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
        titleView.setText( mNavItems.get(position).mTitle );
        subtitleView.setText( mNavItems.get(position).mSubtitle );
        iconView.setImageResource(mNavItems.get(position).mIcon);

        return view;
    }
}

public class MainActivity extends AppCompatActivity {


    public static ProgressDialog progressDialog;

        ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
          RelativeLayout mDrawerPane;
    ListView mDrawerList;
    private DrawerLayout mDrawerLayout;

    private EditText searchBox;
    Button goButton;
    String searchQuery;
    ImageView imageView;
    private NetworkImageView mNetworkImageView;
    private ImageLoader mImageLoader;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    //For HTTPRequest
    String retVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission();
        BluetoothAdapter b = BluetoothAdapter.getDefaultAdapter();
        b.enable();

        setContentView(R.layout.activity_main);
        //NavigationBar
        mNavItems.add(new NavItem("Navigate", "Default Page", R.drawable.ic_launcher_foreground));
        mNavItems.add(new NavItem("Compare and Buy", "Some Thing1", R.drawable.ic_launcher_foreground));
        mNavItems.add(new NavItem("Coupons and Premiums", "Something2", R.drawable.ic_launcher_foreground));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectItemFromDrawer(position);

            }
        });

        getSupportActionBar().setTitle("The Troshos");










    }
    String bid;
    private void startNavigation(String searchQuery) throws JSONException {

        final Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
        Log.e("MainActivity","Search Query is "+searchQuery);
        FirebaseDatabase.getInstance().getReference().child(searchQuery).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("MainActivity","Inside onDataChange");
                bid=dataSnapshot.getValue(String.class);

                intent.putExtra("payload",bid);
                Log.e("In onDataChange"," Bid is "+bid);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                progressDialog.dismiss();
                Log.e("Firebase", bid);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected void onStart() {

        super.onStart();
        searchBox=findViewById(R.id.editText);
        goButton=findViewById(R.id.button);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    while(searchQuery == null)
                        searchQuery=searchBox.getText().toString();
                    startNavigation(searchQuery);
                    progressDialog.show();
                    searchQuery=null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private  void selectItemFromDrawer(int position) {

        if(position ==0){
            Toast.makeText(MainActivity.this, "In Here",Toast.LENGTH_SHORT).show();
        }
       else if (position == 1) {
              Intent intent = new Intent(MainActivity.this, BuyAndCompare.class);
              startActivity(intent);
            //Toast.makeText(MainActivity.this, "Yet to come",Toast.LENGTH_SHORT).show();

        } else if (position == 2) {
           Intent intent = new Intent(MainActivity.this, CouponActivity.class);
           startActivity(intent);

        }

        mDrawerLayout.closeDrawer(mDrawerPane);

    }

    // Checking Permission whether ACCESS_COARSE_LOCATION permssion is granted or not
    public void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Location Permission");
                builder.setPositiveButton("OK", null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }
    }

    //Code for prompting coarse location
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {

                // If Permission is Granted than its ok
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                }

                // If not Granted then alert the user by the message
                else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            checkPermission();
                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
            ((ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE))
                    .clearApplicationUserData();
            Log.e("Data Destroyed", "onDestroy");
            BluetoothAdapter b = BluetoothAdapter.getDefaultAdapter();
            b.disable();
            finish();
        }
    }
}


//TODO: Add Hamburger Icon, recolor the toolbar and add icons
//CodeTheory: Add More Functionality to Navigation Bar