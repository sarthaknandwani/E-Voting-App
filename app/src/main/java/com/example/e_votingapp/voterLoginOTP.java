package com.example.e_votingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class voterLoginOTP extends AppCompatActivity {

    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    TextView state;
    EditText codeEnter, mPhone;
    Button nextBtn;
    CountryCodePicker codePicker;

    String verificationID;
    PhoneAuthProvider.ForceResendingToken token;
    Boolean verificationInProgress = false;

    String userID;
    String phoneNum;
    private static final String TAG = "voterLoginOTP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_login_o_t_p);

        mPhone = findViewById(R.id.mphone);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.mprogressBar);
        fStore = FirebaseFirestore.getInstance();

        codeEnter = findViewById(R.id.mcodeEnter);
        nextBtn = findViewById(R.id.mnextBtn);
        state = findViewById(R.id.mstate);
        codePicker = findViewById(R.id.ccp);

        phoneNum= "+"+codePicker.getSelectedCountryCode()+" "+mPhone.getText().toString();

//        userID = fAuth.getCurrentUser().getUid();
//        DocumentReference documentReference = fStore.collection("users").document(userID);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!verificationInProgress){
                    if(!mPhone.getText().toString().isEmpty() && mPhone.getText().toString().length()==10){
                        phoneNum= "+"+codePicker.getSelectedCountryCode()+" "+mPhone.getText().toString();
                        progressBar.setVisibility(View.VISIBLE);
                        state.setText("Sending OTP...");
                        state.setVisibility(View.VISIBLE);
                        Log.d(TAG, "onClick: Entering requestOTP");
                        requestOTP(phoneNum);
                        Log.d(TAG, "onClick: Exit requestOTP");
                    } else {
                        mPhone.setError("The number is not valid");
                    }
                } else{
                    String userOTP = codeEnter.getText().toString();
                    if(!userOTP.isEmpty() && userOTP.length()==6){
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,userOTP);
                        Log.d(TAG, "onClick: Entering verifyAuth");
                        verifyAuth(credential);
                        Log.d(TAG, "onClick: Exit verifyAuth");
//                        Map<String,Object> user = new HashMap<>();
//                        user.put("phone",phoneNum);
//                        userID = fAuth.getCurrentUser().getUid();
//                        DocumentReference documentReference = fStore.collection("users").document(userID);
//                        fStore.collection("users").document(userID).set(user, SetOptions.merge());
//                        documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if(task.isSuccessful()){
//                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                                    finish();
//                                } else {
//                                    Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
                    } else{
                        codeEnter.setError("OTP entered is not valid");
                    }
                }
            }
        });
    }

    private void verifyAuth(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    DocumentReference documentReference = fStore.collection("Voters").document(phoneNum);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                startActivity(new Intent(getApplicationContext(), voter_home.class).putExtra("message_key", phoneNum));
                                finish();
                            } else{
//                                Toast.makeText(voterLoginOTP.this, "New User", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getApplicationContext(), voter_home.class));
//                                Map<String,Object> user = new HashMap<>();
//                                user.put("phone",phoneNum);
//                                userID = fAuth.getCurrentUser().getUid();
//                                DocumentReference documentReference = fStore.collection("users").document(phoneNum);
//                                fStore.collection("users").document(phoneNum).set(user, SetOptions.merge());

//                                Toast.makeText(voterLoginOTP.this, "This phone number is not registered. Contact the admin, to get yourself registered.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), unregisteredVoter.class));
                                finish();
                            }
                        }
                    });
                    return;
                } else {
                    Toast.makeText(voterLoginOTP.this, "Authentication has Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void requestOTP(String phoneNum) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNum, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressBar.setVisibility(View.GONE);
                state.setVisibility(View.GONE);
                codeEnter.setVisibility(View.VISIBLE);
                verificationID = s;
                token = forceResendingToken;
                nextBtn.setText("VERIFY");
                verificationInProgress = true;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Toast.makeText(voterLoginOTP.this, "OTP Expired, Re-Request OTP", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onClick: Entering verifyAuth2");
//                verifyAuth(phoneAuthCredential);
                Toast.makeText(voterLoginOTP.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: Exit verifyAuth2");
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(voterLoginOTP.this, "Cannot Create Account" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}