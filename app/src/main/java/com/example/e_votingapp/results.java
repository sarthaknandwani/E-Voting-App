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

public class results extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView candidatesList;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        firebaseFirestore = FirebaseFirestore.getInstance();
        candidatesList = findViewById(R.id.candidates_list_results);

        //Query
        Query query = firebaseFirestore.collection("Candidates");
        //Recycler Options
        FirestoreRecyclerOptions<candidateInfo> options = new FirestoreRecyclerOptions.Builder<candidateInfo>()
                .setQuery(query, candidateInfo.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<candidateInfo, com.example.e_votingapp.results.CandidatesViewHolder>(options) {

            @NonNull
            @Override
            public results.CandidatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.results_list_single_item, parent, false);
                return new results.CandidatesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull results.CandidatesViewHolder holder, int position, @NonNull candidateInfo model) {
                holder.list_id.setText(String.format("Candidate ID: %s", model.getId()));
                holder.list_name.setText(String.format("Candidate Name: %s", model.getName()));
                holder.list_gender.setText(String.format("Gender: %s", model.getGender()));
                holder.list_party.setText(String.format("Party Name: %s", model.getParty()));
//                holder.list_ward.setText(String.format("Ward: %s", model.getWard()));
                holder.list_votes.setText(String.format("Votes: %s", model.getVotes()));
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
        private TextView list_gender;
        private TextView list_party;
//        private TextView list_ward;
        private TextView list_votes;

        public CandidatesViewHolder(@NonNull View itemView) {
            super(itemView);

            list_id = itemView.findViewById(R.id.list_id_results);
            list_name = itemView.findViewById(R.id.list_name_results);
            list_gender = itemView.findViewById(R.id.list_gender_results);
            list_party = itemView.findViewById(R.id.list_party_results);
//            list_ward = itemView.findViewById(R.id.list_ward_results);
            list_votes = itemView.findViewById(R.id.list_votes_results);
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