package com.majesty.letswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.majesty.letswatch.model.SessionManager;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    ProgressDialog progressDialog;

    TextInputLayout email, password;
    CheckBox rememberMe;
    TextInputEditText emailEditText, passwordEditText;
    AppCompatButton letTheUserLogIn;
    TextView txtRegister;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        rememberMe = findViewById(R.id.remember_me);
        emailEditText = findViewById(R.id.login_email_editText);
        passwordEditText = findViewById(R.id.login_password_editText);
        letTheUserLogIn = findViewById(R.id.letTheUserLogIn);
        mAuth = FirebaseAuth.getInstance();
        txtRegister = findViewById(R.id.register);

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        SessionManager sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_REMEMMBERME);
        if (sessionManager.checkRememberMe()) {
            HashMap<String, String> rememberMeDetails = sessionManager.getRemeberMeDetailsFromSession();
            emailEditText.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPHONENUMBER));
            passwordEditText.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPASSWORD));
            rememberMe.setChecked(true);
        }else {
            rememberMe.setChecked(false);
        }
//
        progressDialog = new ProgressDialog(this);
//
        letTheUserLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Login");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                login();
            }
        });
    }

    private void login() {

        if (rememberMe.isChecked()) {
            SessionManager sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_REMEMMBERME);
            sessionManager.createRememberMeSession(emailEditText.getText().toString(), passwordEditText.getText().toString());
        }

//        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").equalTo(emailEditText.getText().toString());
//        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                //If Phone Number exists then get password
//                if (dataSnapshot.exists()) {
//                    String systemPassword = dataSnapshot.child(emailEditText.getText().toString()).child("password").getValue(String.class);
//                    //if password exists and matches with users password then get other fields from database
//                    if (systemPassword.equals(passwordEditText.getText().toString())) {
//                        password.setError(null);
//                        password.setErrorEnabled(false);
//
//                        //Get users data from firebase database
//                        String _fullname = dataSnapshot.child(emailEditText.getText().toString()).child("fullName").getValue(String.class);
//                        String _username = dataSnapshot.child(emailEditText.getText().toString()).child("username").getValue(String.class);
//                        String _email = dataSnapshot.child(emailEditText.getText().toString()).child("email").getValue(String.class);
//                        String _phoneNo = dataSnapshot.child(emailEditText.getText().toString()).child("phoneNo").getValue(String.class);
//                        String _password = dataSnapshot.child(emailEditText.getText().toString()).child("password").getValue(String.class);
//                        String _dateOfBirth = dataSnapshot.child(emailEditText.getText().toString()).child("date").getValue(String.class);
//                        String _gender = dataSnapshot.child(emailEditText.getText().toString()).child("gender").getValue(String.class);
//
//                        //Create a Session
//                        SessionManager sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_USERSESSION);
//                        sessionManager.createLoginSession(_fullname, _username, _email, _phoneNo, _password, _dateOfBirth, _gender);
//
////                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
////                        finish();
//                        progressDialog.dismiss();
//
//                    } else {
////                        progressbar.setVisibility(View.GONE);
//                        password.setError("Password does not match!");
//                    }
//                } else {
//                    progressDialog.dismiss();
//                    email.setError("No such user exist!");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                progressDialog.dismiss();
//                Toast.makeText(LoginActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

        mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(LoginActivity.this, "Email atau Password Salah!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


}