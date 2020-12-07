package com.majesty.letswatch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivityTvShow extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    String id, foto,  key;
    Button btn_watch, btn_view, btn_download;
    private static final String API_KEY = "f5322f42ae0ff4ea07cf1c8d05bca1d7";
    RatingBar ratingBar;
    boolean haveConnectedWifi;
    boolean haveConnectedMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv_show);

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

        final Movie movie = getIntent().getParcelableExtra("EXTRA_MOVIES");
        if (movie != null) {
            txtname.setText(Html.fromHtml("<font color='#ff0000'><b>Judul Movie: </b><br></font>" + movie.getName()));
            txtdesc.setText(movie.getDescription());
            txtrelease.setText(Html.fromHtml("<font color='#ff0000'><b>Tanggal Release: </b><br></font>"+ movie.getRelease()));
            ratingBar.setRating(Float.parseFloat(movie.getRuntime())/2);
            String imageUrl = movie.getPhoto();
            Picasso.get().load(imageUrl).fit().into(imageView);
            id = movie.getId();
        }else {
            id = getIntent().getStringExtra("id");
            txtname.setText(Html.fromHtml("<font color='#ff0000'><b>Judul Movie: </b><br></font>" + name));
            txtdesc.setText(description);
            txtrelease.setText(Html.fromHtml("<font color='#ff0000'><b>Tahun Release: </b><br></font>" + release));
            ratingBar.setRating(Float.parseFloat(rating)/2);
            Picasso.get().load(foto).fit().into(imageView);
        }


        databaseHelper = new DatabaseHelper(getApplicationContext());

        if (databaseHelper.DataFavoriteTvShow(id)) {
            loveView.setImageResource(R.drawable.ic_baseline_favorite_24);
        }else {
            loveView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        loveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (databaseHelper.DataFavoriteTvShow(id)) {
                    Toast.makeText(getApplicationContext(), "Favorite Deleted.", Toast.LENGTH_SHORT).show();
                    loveView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    databaseHelper.deleteDataFavTvShow(id);
                    finish();
                } else {
                    databaseHelper = new DatabaseHelper(getApplicationContext());
                    String name = txtname.getText().toString().trim();
                    String decsription = txtdesc.getText().toString().trim();
                    String photo = movie.getPhoto();
                    String release = txtrelease.getText().toString().trim();
                    String rating = movie.getRuntime();

                    long result = databaseHelper.insertFavTvShow(id, name, decsription, photo, release, rating);
                    if (result == -1) {
                        databaseHelper.deleteDataFavTvShow(id);
                        loveView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        Toast.makeText(getApplicationContext(), "Favorite Deleted.", Toast.LENGTH_SHORT).show();
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
                    String url = "https://api.themoviedb.org/3/tv/"+id+"/videos?api_key=" + API_KEY + "&language=en-US";
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
                                    Intent intent = new Intent(DetailActivityTvShow.this, WebViewActivity.class);
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
                    new AlertDialog.Builder(DetailActivityTvShow.this)
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
                    String url = "https://api.themoviedb.org/3/tv/"+id+"/videos?api_key=" + API_KEY + "&language=en-US";
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
                                    Intent intent = new Intent(DetailActivityTvShow.this, VideoActivity.class);
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
                    new AlertDialog.Builder(DetailActivityTvShow.this)
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
