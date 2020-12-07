package com.majesty.letswatch;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.app.ActivityCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.majesty.letswatch.database.DatabaseHelper;
import com.majesty.letswatch.model.Movie;
import com.majesty.letswatch.ui.favorite.FavoriteFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    DatabaseHelper databaseHelper;
    String id_movie, foto, key;
    Button btn_watch, btn_view, btn_download;
    private static final String API_KEY = "f5322f42ae0ff4ea07cf1c8d05bca1d7";
    RatingBar ratingBar;
    boolean haveConnectedWifi;
    boolean haveConnectedMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        foto = getIntent().getStringExtra("photo");
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        String release = getIntent().getStringExtra("release");
        String rating = getIntent().getStringExtra("rating");

        final ImageView loveView = findViewById(R.id.favorite);
        final TextView txtname = findViewById(R.id.txt_judul);
        final TextView txtdesc = findViewById(R.id.txt_deskripsi);
        final ImageView imageView = findViewById(R.id.img_photo);
        final TextView txtrelease = findViewById(R.id.txt_release);
//        final TextView txtruntime = findViewById(R.id.txt_runtime);
        ratingBar = findViewById(R.id.rating_bar);
        btn_watch = findViewById(R.id.btn_watch);
        btn_view = findViewById(R.id.btn_view);
        btn_download = findViewById(R.id.btn_download);
        verifyStoragePermissions(this);

        final Movie movie = getIntent().getParcelableExtra("EXTRA_MOVIES");
        if (movie != null) {
            txtname.setText(Html.fromHtml("<font color='#ff0000'><b>Judul Movie: </b><br></font>" + movie.getName()));
            txtdesc.setText(movie.getDescription());
            txtrelease.setText(Html.fromHtml("<font color='#ff0000'><b>Tanggal Release: </b><br></font>"+ movie.getRelease()));
            ratingBar.setRating(Float.parseFloat(movie.getRuntime())/2);
            String imageUrl = movie.getPhoto();
            Picasso.get().load(imageUrl).fit().into(imageView);
            id_movie = movie.getId();
        }else {
            id_movie = getIntent().getStringExtra("id_movie");
            txtname.setText(Html.fromHtml("<font color='#ff0000'><b>Judul Movie: </b><br></font>" + name));
            txtdesc.setText(description);
            txtrelease.setText(Html.fromHtml("<font color='#ff0000'><b>Tahun Release: </b><br></font>" + release));
            ratingBar.setRating(Float.parseFloat(rating)/2);
            Picasso.get().load(foto).fit().into(imageView);
        }

        databaseHelper = new DatabaseHelper(getApplicationContext());

        if (databaseHelper.DataFavoriteMovie(id_movie)){
            loveView.setImageResource(R.drawable.ic_baseline_favorite_24);
        }else {
            loveView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        loveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (databaseHelper.DataFavoriteMovie(id_movie)){
                    Toast.makeText(getApplicationContext(), "Favorite Deleted.", Toast.LENGTH_SHORT).show();
                    loveView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    databaseHelper.deleteDataFavMovie(id_movie);
                    finish();
                }else {
                    databaseHelper = new DatabaseHelper(getApplicationContext());
                    String name_movie = txtname.getText().toString().trim();
                    String decsription_movie = txtdesc.getText().toString().trim();
                    String photo_movie = movie.getPhoto();
                    String release_movie = txtrelease.getText().toString().trim();
                    String rating_movie = movie.getRuntime();

                    long result = databaseHelper.insertFavMovie(id_movie, name_movie, decsription_movie, photo_movie, release_movie, rating_movie);
                    if (result == -1) {
                        Toast.makeText(getApplicationContext(), "Favorite Deleted.", Toast.LENGTH_SHORT).show();
                        loveView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        databaseHelper.deleteDataFavMovie(id_movie);
                    } else {
                        Toast.makeText(getApplicationContext(), "Favorite Added.", Toast.LENGTH_SHORT).show();
                        loveView.setImageResource(R.drawable.ic_baseline_favorite_24);
                    }
                }


            }
        });

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileOutputStream fileOutputStream=null;
                File file=getdisc();
                if (!file.exists() && !file.mkdirs())
                {
                    Toast.makeText(getApplicationContext(),"sorry can not make dir",Toast.LENGTH_LONG).show();
                    return;
                }
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyymmsshhmmss");
                String date=simpleDateFormat.format(new Date());
                String name="poster"+date+".jpeg";
                String file_name=file.getAbsolutePath()+"/"+name; File new_file=new File(file_name);
                try {
                    fileOutputStream =new FileOutputStream(new_file);
                    Bitmap bitmap=viewToBitmap(imageView,imageView.getWidth(),imageView.getHeight());
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                    Toast.makeText(getApplicationContext(),"Sucses, foto disimpan pada folder Poster", Toast.LENGTH_LONG).show();
                    btn_download.setText("Download Berhasil");
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                catch
                (FileNotFoundException e) {

                } catch (IOException e) {

                } refreshGallary(file);
            } private void refreshGallary(File file)
            { Intent i=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                i.setData(Uri.fromFile(file)); sendBroadcast(i);
            }
            private File getdisc(){
                File file= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                return new File(file,"Poster");
            } });

        btn_watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                haveNetworkConnection();
                if (haveConnectedWifi || haveConnectedMobile){
                    AsyncHttpClient client = new AsyncHttpClient();
                    String url = "https://api.themoviedb.org/3/movie/"+id_movie+"/videos?api_key=" + API_KEY + "&language=en-US";
                    client.get(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                            try {
                                String result = new String(responseBody);
                                JSONObject responseObject = new JSONObject(result);
                                JSONArray list = responseObject.getJSONArray("results");
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject listJson = list.getJSONObject(i);
                                    key = listJson.getString("key");
                                    Intent intent = new Intent(DetailActivity.this, WebViewActivity.class);
                                    intent.putExtra("key", key);
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
                                Log.d("Exception", e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                            Log.d("onFailure", error.getMessage());
                        }
                    });
                }else {
                    new AlertDialog.Builder(DetailActivity.this)
                            .setMessage("Internet tidak Aktif")
                            .setPositiveButton("Aktifkan", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }


//
            }
        });

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                haveNetworkConnection();
                if (haveConnectedWifi || haveConnectedMobile){
                    AsyncHttpClient client = new AsyncHttpClient();
                    String url = "https://api.themoviedb.org/3/movie/"+id_movie+"/videos?api_key=" + API_KEY + "&language=en-US";
                    client.get(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                            try {
                                String result = new String(responseBody);
                                JSONObject responseObject = new JSONObject(result);
                                JSONArray list = responseObject.getJSONArray("results");
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject listJson = list.getJSONObject(i);
                                    key = listJson.getString("key");
                                    Intent intent = new Intent(DetailActivity.this, VideoActivity.class);
                                    intent.putExtra("key", key);
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
                                Log.d("Exception", e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                            Log.d("onFailure", error.getMessage());
                        }
                    });
                }else {
                    new AlertDialog.Builder(DetailActivity.this)
                            .setMessage("Internet tidak Aktif")
                            .setPositiveButton("Aktifkan", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }

            }
        });


    }

    private static Bitmap viewToBitmap(View view, int widh, int hight)
    {
        Bitmap bitmap=Bitmap.createBitmap(widh,hight, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap); view.draw(canvas);
        return bitmap;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(DetailActivity.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity the activity from which permissions are checked
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private boolean haveNetworkConnection() {
        haveConnectedWifi = false;
        haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI")){
                if (ni.isConnected()){
                    haveConnectedWifi = true;
                }
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")){
                if (ni.isConnected()){
                    haveConnectedMobile = true;
                }
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
