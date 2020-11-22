package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class settings extends AppCompatActivity {

        EditText edtTxt;
        Button btn,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        edtTxt=findViewById(R.id.editText);
        btn=findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtTxt.getText().toString().equals("")){
                    Toast.makeText(settings.this,"Please insert a username!", Toast.LENGTH_SHORT).show();
                }else {
                    String  username = edtTxt.getText().toString();
                    Intent sendUsername = new Intent(settings.this,MainActivity.class);
                    sendUsername.putExtra("keyname",username);
                    startActivity(sendUsername);
                }
            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}