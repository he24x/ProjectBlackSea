package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class calendar extends AppCompatActivity {

    private int currentYear=0;
    private int currentMonth=0;
    private int currenDay=0;

    private int index=0;
    private List<String> calendarString;
    private int[] days;
    private int[] months;
    private int[] years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        final CalendarView calendarView = findViewById(R.id.calendar);
        calendarString= new ArrayList<>();
        final int numberofDays= 2000;
        days = new int[numberofDays];
        months = new int[numberofDays];
        years = new int[numberofDays];
        readInfo();
        final EditText textInput = findViewById(R.id.status);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                currentYear=year;
                currentMonth=month;
                currenDay=dayOfMonth;
                for(int h=0;h<index;h++){
                    if(years[h]==currentYear){
                        for(int i=0;i<index;i++){
                            if(days[i]==currenDay){
                                for(int j=0;j<index;j++){
                                    if(months[j]==currentMonth && days[j]==currenDay && years[j]==currentYear){
                                        textInput.setText(calendarString.get(j));
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
                textInput.setText("");
            }
        });
        final Button btn = findViewById(R.id.savebtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                days[index]=currenDay;
                months[index]=currentMonth;
                years[index]=currentYear;
                calendarString.add(index, textInput.getText().toString());
                textInput.setText("");
                index++;
            }
        });

    }

    private void readInfo() {
        File file = new File(this.getFilesDir(), "saved");
        File daysFile = new File(this.getFilesDir(), "days");
        File monthsFile = new File(this.getFilesDir(), "months");
        File yearsFile = new File(this.getFilesDir(), "years");

        if(!file.exists()){
            return;
        }
        try {
            FileInputStream is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            FileInputStream isDays = new FileInputStream(daysFile);
            BufferedReader readerDays = new BufferedReader(new InputStreamReader(isDays));

            FileInputStream isMonths = new FileInputStream(monthsFile);
            BufferedReader readerMonths = new BufferedReader(new InputStreamReader(isMonths));

            FileInputStream isYears = new FileInputStream(yearsFile);
            BufferedReader readerYears = new BufferedReader(new InputStreamReader(isYears));

            int i=0;
            String line = reader.readLine();

            while(line!=null){
                calendarString.add(line);
                line = reader.readLine();
                days[i]= readerDays.read();
                months[i]= readerMonths.read();
                years[i]= readerYears.read();
                i++;
            }

            index=i;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        saveInfo();
    }

    private void saveInfo() {
        File file = new File(this.getFilesDir(), "saved");
        File daysFile = new File(this.getFilesDir(), "days");
        File monthsFile = new File(this.getFilesDir(), "months");
        File yearsFile = new File(this.getFilesDir(), "years");

        try{
            FileOutputStream fOut = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fOut));

            FileOutputStream fOutDays = new FileOutputStream(daysFile);
            BufferedWriter bwDays = new BufferedWriter(new OutputStreamWriter(fOutDays));

            FileOutputStream fOutMonths = new FileOutputStream(monthsFile);
            BufferedWriter bwMonths = new BufferedWriter(new OutputStreamWriter(fOutMonths));

            FileOutputStream fOutYears = new FileOutputStream(yearsFile);
            BufferedWriter bwYears = new BufferedWriter(new OutputStreamWriter(fOutYears));

            for(int i=0; i<index; i++){
                bw.write(calendarString.get(i));
                bw.newLine();
                bwDays.write(days[i]);
                bwMonths.write(months[i]);
                bwYears.write(years[i]);
            }

            bw.close();
            fOut.close();
            bwDays.close();
            fOutDays.close();
            bwMonths.close();
            fOutMonths.close();
            bwYears.close();
            fOutYears.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}