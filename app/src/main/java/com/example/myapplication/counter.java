package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class counter extends AppCompatActivity {

    TextView number,information;
    Button plus,minus;
    int washes=0;

    public static final String SHARED_PREFS="sharedPrefs";
    public static final String TEXT="text";
    public static final String TEXT1="text1";
    public static final String NUMBER="number";
    private String text,text1;
    private int wash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        number=findViewById(R.id.number);
        plus=findViewById(R.id.plus);
        minus=findViewById(R.id.minus);
        information=findViewById(R.id.washingnumber);
        information.setText("You have washed your hands "+washes+" times today!");
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                washes++;
                number.setText(Integer.toString(washes));
                if (washes==1) {
                    information.setText("You have washed your hands once today!");
                }else if (washes==2) {
                    information.setText("You have washed your hands twice today!");
                }else if(washes==0){
                    information.setText("You didn't wash your hands!");
                }else{
                    information.setText("You have washed your hands " + washes + " times today!");
                }
                saveData();
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                washes--;
                number.setText(Integer.toString(washes));
                if (washes==1) {
                    information.setText("You have washed your hands once today!");
                }else if (washes==2) {
                    information.setText("You have washed your hands twice today!");
                }else if(washes==0){
                    information.setText("You didn't wash your hands!");
                }else{
                    information.setText("You have washed your hands " + washes + " times today!");
                }
                saveData();
            }
        });
        loadData();
        updateViews();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        Intent intent = new Intent (counter.this,MainActivity.class);
        intent.putExtra("number",washes);
        startActivityForResult(intent,1);
        saveData();
    }
    public void  saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT,number.getText().toString());
        editor.putString(TEXT1,information.getText().toString());
        editor.putInt(NUMBER,washes);
        editor.apply();
    }
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        text=sharedPreferences.getString(TEXT,"0");
        text1=sharedPreferences.getString(TEXT1,"You didn't wash your hands!");
        wash=sharedPreferences.getInt(NUMBER,0);
    }

    public void updateViews(){
        number.setText(text);
        information.setText(text1);
        washes=wash;
    }
}