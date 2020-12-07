package com.majesty.letswatch;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Dialog dialog;
    Button mSaveButton;
    Button mCancelButton;
    RatingBar ratingBar;
    EditText edtKomentar;
    String komentar, rating;
    TextView txtkomentar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_favorite)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
            finish();
        }else if (item.getItemId() == R.id.logout){
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else if (item.getItemId() == R.id.rate_us){
            dialog = new Dialog(MainActivity.this);
            // Removing the features of Normal Dialogs
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.rate_dialog);
            dialog.setCancelable(true);

            dialog_action();
        }
        else if (item.getItemId() == R.id.your_rating){
            dialog = new Dialog(MainActivity.this);
            // Removing the features of Normal Dialogs
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.rating_dialog);
            dialog.setCancelable(true);

            dialog_action_rating();
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialog_action_rating() {
        ratingBar = (RatingBar) dialog.findViewById(R.id.rating_bar);
        txtkomentar = (TextView) dialog.findViewById(R.id.txt_komentar);
        readFile();
        dialog.show();
    }

    private void dialog_action() {
        mSaveButton = (Button) dialog.findViewById(R.id.save_button);
        mCancelButton = (Button) dialog.findViewById(R.id.cancel_button);
        ratingBar = (RatingBar) dialog.findViewById(R.id.rating_bar);
        edtKomentar = (EditText) dialog.findViewById(R.id.edtKomentar);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeFile();
//                readFile();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");
                dialog.dismiss();
                ratingBar.setRating(0f);
                edtKomentar.setText("");
            }
        });
        dialog.show();
    }

    public void writeFile() {
        komentar = edtKomentar.getText().toString();
        rating = String.valueOf(ratingBar.getRating());
        try {
            FileOutputStream fileOutputStream = openFileOutput("Rate Us.txt", MODE_PRIVATE);
            Properties props = new Properties();
            props.setProperty("komentar", komentar);
            props.setProperty("rating", rating);
            props.store(fileOutputStream, "This is a comment");
            fileOutputStream.close();

            Toast.makeText(getApplicationContext(), "Text Saved", Toast.LENGTH_SHORT).show();

            dialog.dismiss();
            ratingBar.setRating(0f);
            edtKomentar.setText("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile() {
        try {
            FileInputStream fileInputStream = openFileInput("Rate Us.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            Properties props = new Properties();
            props.load(fileInputStream); // load() is threadsafe
            fileInputStream.close();

            String komentar = props.getProperty("komentar");
            String rating = props.getProperty("rating");

            txtkomentar.setText(komentar);
            ratingBar.setRating(Float.parseFloat(rating));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
