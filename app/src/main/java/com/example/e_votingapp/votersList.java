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

public class votersList extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView votersList;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voters_list);

        firebaseFirestore = FirebaseFirestore.getInstance();
        votersList = findViewById(R.id.voters_list);

        //Query
        Query query = firebaseFirestore.collection("Voters");
        //Recycler Options
        FirestoreRecyclerOptions<voterInfo> options = new FirestoreRecyclerOptions.Builder<voterInfo>()
                .setQuery(query, voterInfo.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<voterInfo, VotersViewHolder>(options){

            @NonNull
            @Override
            public votersList.VotersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voters_list_item_single, parent, false);
                return new votersList.VotersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull votersList.VotersViewHolder holder, int position, @NonNull voterInfo model) {
                holder.list_id.setText(String.format("Voter ID: %s", model.getId()));
                holder.list_name.setText(String.format("Voter Name: %s", model.getName()));
                holder.list_dob.setText(String.format("Date Of Birth: %s", model.getDob()));
                holder.list_gender.setText(String.format("Gender: %s", model.getGender()));
                holder.list_phone.setText(String.format("Phone No.:%s", model.getPhone()));
//                holder.list_ward.setText(String.format("Ward: %s", model.getWard()));
            }
        };

        votersList.setHasFixedSize(true);
        votersList.setLayoutManager(new LinearLayoutManager(this));
        votersList.setAdapter(adapter);
    }

    private class VotersViewHolder extends RecyclerView.ViewHolder {

        private TextView list_id;
        private TextView list_name;
        private TextView list_dob;
        private TextView list_gender;
        private TextView list_phone;
//        private TextView list_ward;

        public VotersViewHolder(@NonNull View itemView) {
            super(itemView);

            list_id = itemView.findViewById(R.id.list_id1);
            list_name = itemView.findViewById(R.id.list_name1);
            list_dob = itemView.findViewById(R.id.list_dob1);
            list_gender = itemView.findViewById(R.id.list_gender1);
            list_phone = itemView.findViewById(R.id.list_phone1);
//            list_ward = itemView.findViewById(R.id.list_ward1);
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