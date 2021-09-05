package com.example.e_votingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class voter_home extends AppCompatActivity {
    private static final String TAG = "voter_home";
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button toVote;
        firebaseFirestore = FirebaseFirestore.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_home);

        toVote=findViewById(R.id.btnToVote);
        Intent intent123 = getIntent();
        String str = intent123.getStringExtra("message_key");
        Log.d(TAG, "onCreate: str = " + str);


        toVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Voters").whereEqualTo("phone", str)
                        .whereEqualTo("voted", 0).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        Intent intent = new Intent(getApplicationContext(), candidate_info_to_choose.class);
                                        intent.putExtra("message_key", str);
                                        startActivity(intent);
                                        finish();
                                        return;
                                    }
                                    Intent intent = new Intent(getApplicationContext(), already_voted.class);
                                    startActivity(intent);
                                    finish();

                                } else {

                                }
                            }
                        });
            }
        });


    }
}