package com.example.e_votingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class newCandidate extends AppCompatActivity {

    Button btnAllocateCandidate;
    EditText newCandidateName, newCandidatePhone, partyName, newCandidateID;
    DatePicker dateOfElection, dateOfBirth;
    Spinner gender;
    FirebaseFirestore fStore;
    String selectedItemText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_candidate);

//        ward = (Spinner) findViewById(R.id.spinner);
//// Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.ward_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//// Apply the adapter to the spinner
//        ward.setAdapter(adapter);

        gender = (Spinner) findViewById(R.id.spinner1);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        gender.setAdapter(adapter1);


        fStore = FirebaseFirestore.getInstance();
        newCandidateID = findViewById(R.id.newCandidateID);
        newCandidateName = findViewById(R.id.newCandidateName);
        newCandidatePhone = findViewById(R.id.newCandidatePhone);
        partyName = findViewById(R.id.partyName);
//        dateOfElection = findViewById(R.id.dateOfElection);
        dateOfBirth = findViewById(R.id.dateOfBirth);
        btnAllocateCandidate = findViewById(R.id.btnAllocateCandidate);


        DocumentReference documentReference = fStore.collection("Candidates").document();
        btnAllocateCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = newCandidateID.getText().toString();
                String name = newCandidateName.getText().toString();
                String phone = newCandidatePhone.getText().toString();
                String party = partyName.getText().toString();

                if(id.length()!=6){
                    newCandidateID.setError("Please Enter a Valid 6-digit id");
                    return;
                }

                if(TextUtils.isEmpty(name)){
                    newCandidateName.setError("Name of the Candidate is Required");
                    return;
                }
                if(phone.length()!=10){
                    newCandidatePhone.setError("A 10-digit Phone Number is Required");
                    return;
                }
                if(TextUtils.isEmpty(party)){
                    partyName.setError("Party Name is Required");
                    return;
                }
                if(!validateAge()){
                    Toast.makeText(newCandidate.this, "The candidate is not eligible, he/she must be 18+", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if(!validateDOE()){
//                    Toast.makeText(newCandidate.this, "Invalid Election Date", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                Map<String,Object> candidate = new HashMap<>();
                candidate.put("id", id);
                candidate.put("name", name);
                int month = dateOfBirth.getMonth()+1;
                candidate.put("dob", dateOfBirth.getDayOfMonth() + "/" + month + "/" + dateOfBirth.getYear());
//                candidate.put("ward", selectedItemText);
                phone = "+91 " + phone;
                candidate.put("phone", phone);
                candidate.put("party", party);
                //int month2 = dateOfElection.getMonth()+1;
//                candidate.put("doe", dateOfElection.getDayOfMonth() + "/" + month2 + "/" + dateOfElection.getYear());
                candidate.put("gender", selectedItemText2);
                candidate.put("votes", 0);









                DocumentReference documentReference = fStore.collection("Candidates").document(phone);
                String finalPhone = phone;
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            newCandidatePhone.setError("This mobile number is already registered");
//                            Toast.makeText(newVoter.this, "This mobile number is already registered", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            fStore.collection("Candidates").document(finalPhone).set(candidate);
                            Toast.makeText(newCandidate.this, "Candidate Created Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), adminHome.class));
                            finish();
                        }
                    }
                });






//                fStore.collection("Candidates").document().set(candidate, SetOptions.merge());
//                Toast.makeText(newCandidate.this, "Candidate Created Succesfully", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(), adminHome.class));
            }
        });

//        ward.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedItemText = (String) parent.getItemAtPosition(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText2 = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private boolean validateAge(){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int candidateYear = dateOfBirth.getYear();
        if(currentYear-candidateYear<18){
            return false;
        }
        else if (currentYear-candidateYear>18){
            return true;
        } else{
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
            int candidateMonth = dateOfBirth.getMonth();
            if(currentMonth<candidateMonth){
                return false;
            } else if(currentMonth>candidateMonth){
                return true;
            } else {
                int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int candidateDay = dateOfBirth.getDayOfMonth();
                if(currentDay<candidateDay){
                    return false;
                }
                else if(currentDay>=candidateDay){
                    return true;
                }
            }
        }
        return true;
    }

//    private boolean validateDOE(){
//        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
//        int electionYear = dateOfElection.getYear();
//        if(currentYear<electionYear){
//            return true;
//        } else if(currentYear>electionYear){
//            return false;
//        } else {
//            int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
//            int electionMonth = dateOfElection.getMonth();
//            if(currentMonth<electionMonth){
//                return true;
//            } else if(currentMonth>electionMonth){
//                return false;
//            } else {
//                int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
//                int electionDay = dateOfElection.getDayOfMonth();
//                if(currentDay<electionDay){
//                    return true;
//                } else if(currentDay>=electionDay){
//                    return false;
//                }
//            }
//        }
//        return true;
//    }

}
