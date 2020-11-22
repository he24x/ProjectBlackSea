package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class tips extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        textView=findViewById(R.id.information);
        textView.setText("  Coronavirus disease 2019 (COVID-19) is a contagious respiratory and vascular disease caused by severe acute respiratory syndrome coronavirus 2 (SARS-CoV-2). \n"+
                "\n"+"      Signs and symptoms \n"+" \n"+"  Symptoms of COVID-19 are variable, but usually include fever and a cough. People with the same infection may have different symptoms, and their symptoms may change over time. For example, one person may have a high fever, a cough, and fatigue, and another person may have a low fever at the start of the disease and develop difficulty breathing a week later. \n"
        +"\n"+"      Transmission\n"+"\n"+"  COVID-19 spreads from person to person mainly through the respiratory route after an infected person coughs, sneezes, sings, talks or breathes. A new infection occurs when virus-containing particles exhaled by an infected person, either respiratory droplets or aerosols, get into the mouth, nose, or eyes of other people who are in close contact with the infected person.\n"
                +"\n"+"      Prevention \n"+"\n"+ " A COVID-19 vaccine is not expected until 2021 at the earliest. The US National Institutes of Health guidelines do not recommend any medication for prevention of COVID-19, before or after exposure to the SARS-CoV-2 virus, outside the setting of a clinical trial.\n"+ "     Recommendations: \n"+
                "           -Personal protective equipment\n"+"           -Face masks\n"+"           -Social distancing\n"+"           -Hand-washing and hygiene\n"+"\n"+"\n"+"     Source: Wikipedia\n"+"\n"+"\n");
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}