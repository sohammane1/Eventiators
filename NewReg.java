package com.example.helloworldapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;


public class NewReg extends AppCompatActivity {

    EditText REditName;
    EditText REditMail;
    EditText REditPass;
    EditText REditPhone;
    EditText REditAdd;
    EditText REditDist;
    Button RBtReg;
    Button OTP;
    Button Verify;
    EditText rotp;


    FirebaseAuth firebaseAuth;
    FirebaseAuth mAuth;
    String verificationCode = null;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reg);

        REditName = findViewById(R.id.ReditName);
        REditMail = findViewById(R.id.Rmail);
        REditPass = findViewById(R.id.ReditPass);
        REditPhone = findViewById(R.id.ReditTextPhone);
        REditAdd = findViewById(R.id.ReditAdd);
        REditDist = findViewById(R.id.ReditDist);
        RBtReg = findViewById(R.id.RbtReg);
        OTP = findViewById(R.id.sendOTP);
        Verify = findViewById(R.id.Rverify);
        rotp = findViewById(R.id.Rotp);

        Verify.setVisibility(View.INVISIBLE);
        rotp.setVisibility(View.INVISIBLE);










        OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=REditPhone.getText().toString();
                if(s.length()<10)
                {
                    REditPhone.setError("Enter Valid Phone Number");
                    REditPhone.requestFocus();
                }
                else
                {
                    Verify.setText("Verified");
                }
            }
        });




        RBtReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkName = REditName.getText().toString();
                String checkEmail = REditMail.getText().toString();
                String checkPass = REditPass.getText().toString();
                String checkPhone = REditPhone.getText().toString();
                String checkAdd = REditAdd.getText().toString();
                String checkDist = REditDist.getText().toString();

                firebaseAuth = FirebaseAuth.getInstance();

                if (checkName.isEmpty()) {
                    REditName.setError("Name Required");
                    REditName.requestFocus();
                } else if (checkPass.isEmpty()) {
                    REditPass.setError("Password Required");
                    REditPass.requestFocus();
                }

                if (checkEmail.isEmpty()) {
                    REditMail.setError("Email Required");
                    REditMail.requestFocus();
                } else if (!(checkPhone.length() ==10) && !Verify.getText().toString().equals("Verified")) {
                    REditPhone.setError("Phone No Required to be verified !!");
                    REditPhone.requestFocus();
                }

                if (checkAdd.isEmpty()) {
                    REditAdd.setError("Address Required");
                    REditAdd.requestFocus();
                } else if (checkDist.isEmpty() || checkDist.length()!=6) {
                    REditDist.setError("Enter Valid Pin-Code");
                    REditDist.requestFocus();
                }
                else if(!checkName.matches("^[a-zA-Z\\s]*$")){
                    REditName.setError("Only Alphabets Are Allowed");
                    REditName.requestFocus();
                }
                else if (!checkName.isEmpty() && !checkPass.isEmpty() && !checkEmail.isEmpty() && !checkPhone.isEmpty() && !checkAdd.isEmpty() && !checkDist.isEmpty()&& Verify.getText().toString().equals("Verified")) {

                    firebaseAuth.createUserWithEmailAndPassword(checkEmail, checkPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                firebaseAuth.signInWithEmailAndPassword(checkEmail, checkPass);
                                String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                User user = new User(checkName, checkEmail, checkPhone, checkAdd, checkDist,uid);

                                FirebaseDatabase.getInstance().getReference("Users").child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(NewReg.this, "User Registred ", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                Toast.makeText(NewReg.this, "Logged-In ", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(NewReg.this, nav_act_home.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(NewReg.this, "Error !!!" + task.getException(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                else{
                    Toast.makeText(NewReg.this, "Phone Number is yet to verify", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


}
