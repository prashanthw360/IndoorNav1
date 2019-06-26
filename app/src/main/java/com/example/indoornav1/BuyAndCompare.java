package com.example.indoornav1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class BuyAndCompare extends AppCompatActivity {
    EditText editText;
    Spinner spinner;
    Button button;
    String SearchQuery;
    String SearchCat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_and_compare);
        spinner = findViewById(R.id.CBCat);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        button = findViewById(R.id.cbbutton);
        editText=findViewById(R.id.CBSearch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPayload();



            }
        });

    }

    private void sendPayload() {
        SearchQuery=editText.getText().toString();
        SearchCat=spinner.getSelectedItem().toString();
        if(SearchCat.equals("Select from here")){
            Toast.makeText(getApplicationContext(), "Error! Select the category", Toast.LENGTH_SHORT).show();

        }else{
            Intent intent = new Intent(BuyAndCompare.this,CBResults.class);
            startActivity(intent);
        }
    }


}
