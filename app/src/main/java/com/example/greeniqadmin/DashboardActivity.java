package com.example.greeniqadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DashboardActivity extends AppCompatActivity {

    private CardView coin, event, manage, certificate;
    private Intent coinIntent, eventIntent, manageIntent, certIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        coin = findViewById(R.id.cardView1);
        event = findViewById(R.id.cardView2);
        manage = findViewById(R.id.cardView3);
        certificate = findViewById(R.id.cardView4);

        coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coinIntent = new Intent(getApplicationContext(), CoinActivity.class);
                startActivity(coinIntent);
            }
        });

        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventIntent = new Intent(getApplicationContext(), EventActivity.class);
                startActivity(eventIntent);
            }
        });

        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageIntent = new Intent(getApplicationContext(), UsersActivity.class);
                startActivity(manageIntent);
            }
        });

        certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                certIntent = new Intent(getApplicationContext(), CertificateActivity.class);
                startActivity(certIntent);
            }
        });
    }
}