package com.example.e_votingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class adminHome extends AppCompatActivity {

    Button btnNewCandidate, btnViewCandidate, btnNewVoter, btnViewVoter, btnResults, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        btnNewCandidate = findViewById(R.id.btnNewCandidate);
        btnViewCandidate = findViewById(R.id.btnViewCandidate);
        btnNewVoter = findViewById(R.id.btnNewVoter);
        btnViewVoter = findViewById(R.id.btnViewVoter);
        btnResults = findViewById(R.id.btnResults);
        btnLogout = findViewById(R.id.btnLogout);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        btnNewCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), newCandidate.class));
            }
        });

        btnViewCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), candidatesList.class));
            }
        });

        btnNewVoter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), newVoter.class));
            }
        });

        btnViewVoter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), votersList.class));
            }
        });

        btnResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), results.class));
            }
        });
    }
}