package com.example.indoornav1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        TextView tv=findViewById(R.id.textView3);

        String s = getIntent().getStringExtra("response");

        tv.setText(s);
    }
}
