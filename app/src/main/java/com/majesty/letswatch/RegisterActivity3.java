package com.majesty.letswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.majesty.letswatch.model.SessionManager;
import com.majesty.letswatch.model.UserHelperClass;

public class RegisterActivity3 extends AppCompatActivity {
    ScrollView scrollView;
    TextInputLayout phoneNumber;
    CountryCodePicker countryCodePicker;
    RelativeLayout progressbar;
    Button signup_next_button;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference DatabaseReference;
// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register3);

        firebaseAuth = FirebaseAuth.getInstance();

        scrollView = findViewById(R.id.signup_3rd_screen_scroll_view);
        countryCodePicker = findViewById(R.id.country_code_picker);
        phoneNumber = findViewById(R.id.signup_phone_number);
        progressbar = findViewById(R.id.signup_progress_bar);
        signup_next_button = findViewById(R.id.signup_next_button);

        DatabaseReference = FirebaseDatabase.getInstance().getReference("Users");

        signup_next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithPhoneAuthCredential();
            }
        });
    }

    private void signInWithPhoneAuthCredential() {

        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            callVerifyOTPScreen();
                        }
                    }
                });
    }

    public void callVerifyOTPScreen() {

        //Validate fields
        if (!validatePhoneNumber()) {
            return;
        }//Validation succeeded and now move to next screen to verify phone number and save data
        progressbar.setVisibility(View.VISIBLE);


        //Get all values passed from previous screens using Intent
        final String _fullName = getIntent().getStringExtra("fullName");
        final String _email = getIntent().getStringExtra("email");
        final String _username = getIntent().getStringExtra("username");
        final String _password = getIntent().getStringExtra("password");
        final String _date = getIntent().getStringExtra("date");
        final String _gender = getIntent().getStringExtra("gender");

        //Get values from fields
        String _getUserEnteredPhoneNumber = phoneNumber.getEditText().getText().toString().trim(); //Get Phone Number
        if (_getUserEnteredPhoneNumber.charAt(0) == '0') {
            _getUserEnteredPhoneNumber = _getUserEnteredPhoneNumber.substring(1);
        } //remove 0 at the start if entered by the user
        final String _phoneNo = "+" + countryCodePicker.getFullNumberWithPlus() + _getUserEnteredPhoneNumber;
        Toast.makeText(RegisterActivity3.this, _phoneNo, Toast.LENGTH_SHORT).show();


        //Create helperclass reference and store data using firebase
        UserHelperClass addNewUser = new UserHelperClass(_fullName, _username, _email, _phoneNo, _password, _date, _gender);
        DatabaseReference.child(_phoneNo).setValue(addNewUser);

        //Create a Session
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        sessionManager.createLoginSession(_fullName, _username, _email, _phoneNo, _password, _date, _gender);

        startActivity(new Intent(RegisterActivity3.this, MainActivity.class));
        finish();

    }


    /*
    Show
    Internet
    Connection Dialog
     */
    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please connect to the internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    /*
    Validation function
     */
    private boolean validatePhoneNumber() {
        String val = phoneNumber.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            phoneNumber.setError("Enter valid phone number");
            return false;
        } else if (!val.matches(checkspaces)) {
            phoneNumber.setError("No White spaces are allowed!");
            return false;
        } else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }

    }
}