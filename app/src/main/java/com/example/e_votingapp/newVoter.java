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

public class newVoter extends AppCompatActivity {

    Button btnAllocateVoter;
    EditText newVoterName, newVoterPhone, newVoterID;
    DatePicker dateOfBirth1;
    Spinner ward, gender;
    FirebaseFirestore fStore;
    String selectedItemText, selectedItemText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_voter);
//        ward = (Spinner) findViewById(R.id.spinner2);
//// Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.ward_array, android.R.layout.simple_spinner_item);
//// Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//// Apply the adapter to the spinner
//        ward.setAdapter(adapter);

        gender = (Spinner) findViewById(R.id.spinner3);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        gender.setAdapter(adapter1);


        fStore = FirebaseFirestore.getInstance();
        newVoterID = findViewById(R.id.newVoterID);
        newVoterName = findViewById(R.id.newVoterName);
        newVoterPhone = findViewById(R.id.newVoterPhone);
        dateOfBirth1 = findViewById(R.id.dateOfBirth1);
        btnAllocateVoter = findViewById(R.id.btnAllocateVoter);


        DocumentReference documentReference = fStore.collection("Voters").document();
        btnAllocateVoter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = newVoterID.getText().toString();
                String name = newVoterName.getText().toString();
                String phone = newVoterPhone.getText().toString();

                if(id.length()!=6){
                    newVoterID.setError("Please Enter a Valid 6-digit id");
                    return;
                }

                if(TextUtils.isEmpty(name)){
                    newVoterName.setError("Name of the Voter is Required");
                    return;
                }
                if(phone.length()!=10){
                    newVoterPhone.setError("A 10-digit Phone Number is Required");
                    return;
                }

                String abcd = "+91 " + phone;
//                if(!uniqueNumber(abcd)){
//                    Toast.makeText(newVoter.this, "The phone number is already registered", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if(!validateAge()){
                    Toast.makeText(newVoter.this, "The voter is not eligible, he/she must be 18+", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String,Object> voter = new HashMap<>();
                voter.put("id", id);
                voter.put("name", name);
                int month = dateOfBirth1.getMonth()+1;
                voter.put("dob", dateOfBirth1.getDayOfMonth() + "/" + month + "/" + dateOfBirth1.getYear());
//                voter.put("ward", selectedItemText);
                phone = "+91 " + phone;
                voter.put("phone", phone);
                voter.put("gender", selectedItemText2);
                voter.put("voted", 0);












                DocumentReference documentReference = fStore.collection("Voters").document(phone);
                String finalPhone = phone;
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            newVoterPhone.setError("This mobile number is already registered");
//                            Toast.makeText(newVoter.this, "This mobile number is already registered", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            fStore.collection("Voters").document(finalPhone).set(voter);
                            Toast.makeText(newVoter.this, "Voter Created Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), adminHome.class));
                            finish();
                        }
                    }
                });







//                fStore.collection("Voters").document(phone).set(voter)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(newVoter.this, "Voter Created Succesfully", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getApplicationContext(), adminHome.class));
//                            }
//                        })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(newVoter.this, "This mobile number is already registered", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    });
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
        int candidateYear = dateOfBirth1.getYear();
        if(currentYear-candidateYear<18){
            return false;
        }
        else if (currentYear-candidateYear>18){
            return true;
        } else{
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
            int candidateMonth = dateOfBirth1.getMonth();
            if(currentMonth<candidateMonth){
                return false;
            } else if(currentMonth>candidateMonth){
                return true;
            } else {
                int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int candidateDay = dateOfBirth1.getDayOfMonth();
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

//    private boolean uniqueNumber(final String phoneN) {
//        final boolean[] isExisting = {false};
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference cref=db.collection("Voters");
//        Query q1=cref.whereEqualTo("phone", phoneN);
//        q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for (DocumentSnapshot ds : queryDocumentSnapshots) {
//                    String phoneNu;
//                    phoneNu = ds.getString("phone");
//                    if (phoneN.equals(phoneNu)) {
//                        isExisting[0] = true;
//                    }
//                }
//            }
//        });
//        return !isExisting[0];
//    }

//    private boolean uniqueNumber(final String phoneN) {
//        boolean[] isExisting = {false};
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference cref=db.collection("Voters");
//        Query q1=cref.whereEqualTo("phone", phoneN);
//        q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
////                for (DocumentSnapshot ds : queryDocumentSnapshots) {
////                    String phoneNu;
////                    phoneNu = ds.getString("phone");
////                    if (phoneN.equals(phoneNu)) {
//                        isExisting[0] = true;
////                    }
////                }
//            }
//        });
//        return !isExisting[0];
//    }
}