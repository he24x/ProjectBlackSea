package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    static private final long START_TIME_IN_MILLIS=60000;
    Button start,reset;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis=START_TIME_IN_MILLIS;
    ImageView profile;
    TextView name, bio,timer;
    CardView tips, counter, calendar, settings;
    int washes2 = 0;
    String user = "George";
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    public static final String SHARED_PREFERENCES="sharedPreferences";
    public static final String TEXT2="text2";
    public static final String TEXT3="text3";
    public static final String NUMBER="number2";
    public static final String PROFILE="profile";
    private String text2,text3;
    private int wash2;
    private long mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        int number = intent.getIntExtra("number", 0);
        washes2 = number;
        timer=findViewById(R.id.info);
        start=findViewById(R.id.start);
        reset=findViewById(R.id.reset);
        profile = findViewById(R.id.profilepic);
        name = findViewById(R.id.profiletext);
        bio = findViewById(R.id.statstext);
        tips = findViewById(R.id.useful_tips);
        counter = findViewById(R.id.counter);
        calendar = findViewById(R.id.calendar);
        settings = findViewById(R.id.settings);

        name.setText(user);
        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });
        counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity1();
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning){
                    pauseTimer();
                }else{
                    startTimer();
                }
            }
        });
        updateCountDownText();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        if (washes2 == 1) {
            bio.setText("Congratulation " + user + ", today you washed your hands once!");
        } else if (washes2 == 2) {
            bio.setText("Congratulation " + user + ", today you washed your hands twice!");
        } else if (washes2 == 0) {
            bio.setText("Please " + user + ", wash your hands to stay safe!");
        } else {
            bio.setText("Congratulation " + user + ", today you washed your hands " + washes2 + " times!");
        }
        if(washes2<1 && user!="user"){
            loadData2();
            updateView();
        }
    }
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure that you want to exit?");
        builder.setCancelable(false);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void openActivity() {
        Intent intent = new Intent(this, tips.class);
        startActivity(intent);
    }

    public void openActivity1() {
        Intent intent = new Intent(this, counter.class);
        startActivity(intent);
    }

    public void openActivity2() {
        Intent intent = new Intent(this, calendar.class);
        startActivity(intent);
    }

    public void openActivity3() {
        Intent intent = new Intent(this, settings.class);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    profile.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void saveData2(){
        SharedPreferences sharedPreferences2 = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putString(TEXT2,name.getText().toString());
        editor.putString(TEXT3,bio.getText().toString());
        editor.putInt(NUMBER,washes2);
        editor.apply();
        Toast.makeText(this,"Data saved", Toast.LENGTH_SHORT).show();
     }
    public void loadData2(){
        SharedPreferences sharedPreferences2 = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        text2=sharedPreferences2.getString(TEXT2,"Please insert a name!");
        text3=sharedPreferences2.getString(TEXT3,"You didn't was your hands!");
        wash2=sharedPreferences2.getInt(NUMBER,0);
        Toast.makeText(this,"Data loaded", Toast.LENGTH_SHORT).show();
    }
     public void updateView(){
        name.setText(text2);
        bio.setText(text3);
        washes2=wash2;
         Toast.makeText(this,"Data updated", Toast.LENGTH_SHORT).show();
     }
     private void startTimer(){
        mCountDownTimer=new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning=false;
                start.setText("Start");
                updateButtons();
            }
        }.start();
        mTimerRunning=true;
        updateButtons();
     }
     private void pauseTimer(){
        mCountDownTimer.cancel();
        mTimerRunning=false;
        start.setText("Start");
        reset.setVisibility(View.VISIBLE);
     }
     private void resetTimer(){
        mTimeLeftInMillis=START_TIME_IN_MILLIS;
        updateCountDownText();
        updateButtons();
     }
     private void updateCountDownText(){
        int minutes= (int)mTimeLeftInMillis/1000/60;
        int seconds= (int)mTimeLeftInMillis/1000%60;
        String timeLeftFormatted=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        timer.setText(timeLeftFormatted);
     }
     private void updateButtons(){
        if (mTimerRunning){
            reset.setVisibility(View.INVISIBLE);
            start.setText("Pause");
        }else{
            start.setText("Start");
            if(mTimeLeftInMillis<1000){
                start.setVisibility(View.INVISIBLE);
            }else{
                start.setVisibility(View.INVISIBLE);
            }
            if(mTimeLeftInMillis<START_TIME_IN_MILLIS){
                reset.setVisibility(View.VISIBLE);
            }else{
                reset.setVisibility(View.INVISIBLE);
            }
        }
     }
   /* @Override
    protected void onStop() {
        super.onStop();
        saveData2();
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft",mTimeLeftInMillis);
        editor.putBoolean("timerRunning",mTimerRunning);
        editor.putLong("endTime",mEndTime);
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
    }*/
}