package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.Locale;

import static com.example.myapplication.App.CHANNEL_1_ID;

public class MainActivity extends AppCompatActivity {

    private int notif1=0;
    private NotificationManagerCompat notificationManager;
    static private final long START_TIME_IN_MILLIS=3000;
    Button start,reset;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    ImageView profile;
    TextView name, bio,timer;
    CardView tips, counter, calendar, settings;
    int washes2 = 0,answer=0;
    String user = "George";
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    public static final String SHARED_PREFERENCES="sharedPreferences";
    public static final String TEXT2="text2";
    public static final String TEXT3="text3";
    public static final String NUMBER2="number2";
    public static final String NUMBER3="number3";
    public static final String NUME="nume";
    private String text2,text3;
    private int wash2,counting;
    private String numele;
    private long mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationManager = NotificationManagerCompat.from(this);
        Intent intent = getIntent();
        washes2= intent.getIntExtra("number", 0);
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
                loadData2();
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
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        stats();
        if(washes2+notif1<1 && user!="user"){
            loadData2();
            updateView2();
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
        saveData2();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void openActivity1() {
        Intent intent = new Intent(this, counter.class);
        startActivity(intent);
        saveData2();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void openActivity2() {
        Intent intent = new Intent(this, calendar.class);
        startActivity(intent);
        saveData2();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void openActivity3() {
        Intent intent = new Intent(this, settings.class);
        startActivity(intent);
        saveData2();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        startTimer();
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

     private void startTimer(){
        mEndTime=System.currentTimeMillis()+mTimeLeftInMillis;

        mCountDownTimer=new CountDownTimer(mTimeLeftInMillis,100) {
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
                notif1++;
                sendOnChannel1();
                stats();
                saveData2();
                resetTimer();
                startTimer();
            }
        }.start();
        mTimerRunning=true;
        updateButtons();
     }
    public void saveData2(){
        SharedPreferences sharedPreferences2 = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        editor2.putString(TEXT2,name.getText().toString());
        editor2.putString(TEXT3,bio.getText().toString());
        editor2.putInt(NUMBER2,washes2);
        editor2.putInt(NUMBER3,notif1);
        editor2.putString(NUME,user);
        editor2.apply();
        Toast.makeText(this,"Data saved", Toast.LENGTH_SHORT).show();
    }
    public void loadData2(){
        SharedPreferences sharedPreferences2 = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        text2=sharedPreferences2.getString(TEXT2,"Please insert a name!");
        text3=sharedPreferences2.getString(TEXT3,"You didn't was your hands!");
        wash2=sharedPreferences2.getInt(NUMBER2,0);
        counting=sharedPreferences2.getInt(NUMBER3,notif1);
        numele=sharedPreferences2.getString(NUME,"no name selected");
        Toast.makeText(this,"Data loaded", Toast.LENGTH_SHORT).show();
    }
    public void updateView2(){
        name.setText(text2);
        bio.setText(text3);
        washes2=wash2;
        notif1=counting;
        user=numele;
        Toast.makeText(this,"Data updated", Toast.LENGTH_SHORT).show();
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
            if(mTimeLeftInMillis<100){
                start.setVisibility(View.INVISIBLE);
            }else{
                start.setVisibility(View.VISIBLE);
            }
            if(mTimeLeftInMillis<START_TIME_IN_MILLIS){
                reset.setVisibility(View.VISIBLE);
            }else{
                reset.setVisibility(View.INVISIBLE);
            }
        }
     }
    @Override
    protected void onStop() {
        super.onStop();
        saveData2();
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft",mTimeLeftInMillis);
        editor.putBoolean("timerRunning",mTimerRunning);
        editor.putLong("endTime",mEndTime);
        editor.apply();
        stats();
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mTimeLeftInMillis=prefs.getLong("millisLeft",START_TIME_IN_MILLIS);
        mTimerRunning=prefs.getBoolean("timerRunning",false);
        updateCountDownText();
        updateButtons();
        if(mTimerRunning){
            mEndTime=prefs.getLong("endTime",0);
            mTimeLeftInMillis=mEndTime-System.currentTimeMillis();
            if(mTimeLeftInMillis<0){
                mTimeLeftInMillis=0;
                mTimerRunning=false;
                updateCountDownText();
                updateButtons();
            }else{
                startTimer();
            }
        }
    }
    public void sendOnChannel1(){
        String title = "It's the time to wash your hands!";
        String message = "One hour passed, "+user+" please try to wash your hands!";

        Intent activityOpener = new Intent(this, chronometer.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,activityOpener,0);

        Intent broadCast = new Intent(this, NotificationManager.class);
        broadCast.putExtra("toastMessage",message);

        PendingIntent activityIntent = PendingIntent.getBroadcast(this,0, broadCast, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .addAction(R.mipmap.ic_launcher,"Go to chronometer!",activityIntent)
                .build();
        notificationManager.notify(1,notification);

    }

    public void sendOnChannel2(View v){
        String title = "Title!";
        String message = "Message!";
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1,notification);
    }
    public void stats(){
        if (washes2+notif1 == 1) {
            bio.setText("Congratulation " + user + ", today you washed your hands once!");
        } else if (washes2+notif1 == 2) {
            bio.setText("Congratulation " + user + ", today you washed your hands twice!");
        } else if (washes2+notif1 <= 0) {
            bio.setText("Please " + user + ", wash your hands to stay safe!");
        } else{
            bio.setText("Congratulation " + user + ", today you washed your hands " + (washes2+notif1) + " times!");
        }

    }
}