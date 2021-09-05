package com.example.e_votingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class candidatesList extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView candidatesList;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidates_list);

        firebaseFirestore = FirebaseFirestore.getInstance();
        candidatesList = findViewById(R.id.candidates_list);

        //Query
        Query query = firebaseFirestore.collection("Candidates");
        //Recycler Options
        FirestoreRecyclerOptions<candidateInfo> options = new FirestoreRecyclerOptions.Builder<candidateInfo>()
                .setQuery(query, candidateInfo.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<candidateInfo, CandidatesViewHolder>(options) {

            @NonNull
            @Override
            public candidatesList.CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidates_list_item_single, parent, false);
                return new candidatesList.CandidatesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull candidatesList.CandidatesViewHolder holder, int position, @NonNull candidateInfo model) {
                holder.list_id.setText(String.format("Candidate ID: %s", model.getId()));
                holder.list_name.setText(String.format("Candidate Name: %s", model.getName()));
                holder.list_dob.setText(String.format("Date Of Birth: %s", model.getDob()));
//                holder.list_doe.setText(String.format("Date of Election: %s", model.getDoe()));
                holder.list_gender.setText(String.format("Gender: %s", model.getGender()));
                holder.list_party.setText(String.format("Party Name: %s", model.getParty()));
                holder.list_phone.setText(String.format("Phone No.:%s", model.getPhone()));
//                holder.list_ward.setText(String.format("Ward: %s", model.getWard()));
            }
        };

        candidatesList.setHasFixedSize(true);
        candidatesList.setLayoutManager(new LinearLayoutManager(this));
        candidatesList.setAdapter(adapter);

        //ViewHolder
    }

    private class CandidatesViewHolder extends RecyclerView.ViewHolder {

        private TextView list_id;
        private TextView list_name;
        private TextView list_dob;
//        private TextView list_doe;
        private TextView list_gender;
        private TextView list_party;
        private TextView list_phone;
//        private TextView list_ward;

        public CandidatesViewHolder(@NonNull View itemView) {
            super(itemView);

            list_id = itemView.findViewById(R.id.list_id);
            list_name = itemView.findViewById(R.id.list_name);
            list_dob = itemView.findViewById(R.id.list_dob);
//            list_doe = itemView.findViewById(R.id.list_doe);
            list_gender = itemView.findViewById(R.id.list_gender);
            list_party = itemView.findViewById(R.id.list_party);
            list_phone = itemView.findViewById(R.id.list_phone);
//            list_ward = itemView.findViewById(R.id.list_ward);
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
}