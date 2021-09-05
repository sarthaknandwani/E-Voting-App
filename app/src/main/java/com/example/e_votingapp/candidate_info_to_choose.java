package com.example.e_votingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class candidate_info_to_choose extends AppCompatActivity {
    private static final String TAG = "candidate_info_to_choos";

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView candidatesList;
    private FirestoreRecyclerAdapter adapter;
    private RadioGroup rg;
    int count=0;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_info_to_choose);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();
        candidatesList = findViewById(R.id.candidatesList);
        rg = findViewById(R.id.candidate_radio_group);
        btnSubmit = findViewById(R.id.btnSubmitVote);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedID = rg.getCheckedRadioButtonId();
                if(checkedID==-1){
                    Toast.makeText(candidate_info_to_choose.this, "Please select a candidate of your choice", Toast.LENGTH_SHORT).show();
                }
                else {
                    String s = findViewById(checkedID).getContentDescription().toString();
                    Log.d(TAG, "onClick: s = " + s);
                    String s1 = s;
                    s1 = s1.substring(15);
                    Log.d(TAG, "onClick: s1 = " + s1);
                    String s2 = s;
                    s2 = s2.substring(0, 14);
                    Log.d(TAG, "onClick: s2 = " + s2);

                    String finalS = s1;
                    String finalS1 = s2;

                    firebaseFirestore.collection("Candidates")
                            .whereEqualTo("phone", finalS1)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                            int x = Integer.parseInt(finalS);


                                            Map<String, Object> data = new HashMap<>();
                                            data.put("votes", x+1);

                                            firebaseFirestore.collection("Candidates").document(finalS1)
                                                    .set(data, SetOptions.merge());

                                            Intent intent123 = getIntent();
                                            String str = intent123.getStringExtra("message_key");
                                            Log.d(TAG, "onCreate: str = " + str);


                                            Map<String, Object> data1 = new HashMap<>();
                                            data1.put("voted", 1);

                                            firebaseFirestore.collection("Voters").document(str)
                                                    .set(data1, SetOptions.merge());

                                            Intent intent = new Intent(getApplicationContext(), thanks_for_voting.class);
                                            startActivity(intent);
                                            finish();


                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                    Toast.makeText(candidate_info_to_choose.this, "Thanks for Voting for ID = " + checkedID, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Query
        Query query = firebaseFirestore.collection("Candidates");
        //Recycler Options
        FirestoreRecyclerOptions<candidateInfo> options = new FirestoreRecyclerOptions.Builder<candidateInfo>()
                .setQuery(query, candidateInfo.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<candidateInfo, CandidatesViewHolder2>(options) {

            @NonNull
            @Override
            public CandidatesViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidate_list_item_single_to_choose, parent, false);
                return new CandidatesViewHolder2(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CandidatesViewHolder2 holder, int position, @NonNull candidateInfo model) {
//                holder.list_name.setText(String.format("Candidate Name: %s", model.getName()));
//                holder.list_gender.setText(String.format("Gender: %s", model.getGender()));
//                holder.list_party.setText(String.format("Party Name: %s", model.getParty()));
//                holder.list_ward.setText(String.format("Ward: %s", model.getWard()));

                holder.radioButton.setText("NAME:  "+ model.getName() + "\nGENDER:  " + model.getGender() + "\nPARTY:  " + model.getParty());
                holder.radioButton.setContentDescription(model.getPhone()+ "/" + model.getVotes());
                addRadioButton(holder.radioButton, rg);
            }
        };

        candidatesList.setHasFixedSize(true);
        candidatesList.setLayoutManager(new LinearLayoutManager(this));
        candidatesList.setAdapter(adapter);


        //ViewHolder
    }
    private class CandidatesViewHolder2 extends RecyclerView.ViewHolder {

//        private TextView list_name;
//        private TextView list_gender;
//        private TextView list_party;
//        private TextView list_ward;
        private final RadioButton radioButton;

        public CandidatesViewHolder2(@NonNull View itemView) {
            super(itemView);

//            list_name = itemView.findViewById(R.id.list_name_to_choose);
//            list_gender = itemView.findViewById(R.id.list_gender_to_choose);
//            list_party = itemView.findViewById(R.id.list_party_to_choose);
//            list_ward = itemView.findViewById(R.id.list_ward_to_choose);
            radioButton = itemView.findViewById(R.id.radioButton2);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void addRadioButton(RadioButton rb, RadioGroup radgr) {
//        final RadioButton[] rb = new RadioButton[5];
         //create the RadioGroup
        radgr.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
            rb.setId(100 + count);
        Log.d(TAG, "addRadioButton: id= " + rb.getId());
            count++;
        ((ViewGroup)rb.getParent()).removeView(rb);
            radgr.addView(rb);
    }
}