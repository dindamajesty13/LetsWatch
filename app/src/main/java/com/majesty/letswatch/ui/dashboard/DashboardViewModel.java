package com.majesty.letswatch.ui.dashboard;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.majesty.letswatch.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class DashboardViewModel extends ViewModel {
    private static final String API_KEY = "f5322f42ae0ff4ea07cf1c8d05bca1d7";
    private MutableLiveData<ArrayList<Movie>> listMovie = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Movie>> listPopular = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Movie>> listNowPlaying = new MutableLiveData<>();

    void setMovie() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();
        if(Locale.getDefault().getDisplayLanguage().equals("English")){
            String url = "https://api.themoviedb.org/3/tv/airing_today?api_key="+API_KEY+"&language=en-US";
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String result = new String(responseBody);
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray list = responseObject.getJSONArray("results");
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject listJson = list.getJSONObject(i);
                            Movie movie = new Movie();
                            movie.setId(listJson.getString("id"));
                            movie.setName(listJson.getString("name"));
                            movie.setDescription(listJson.getString("overview"));
                            movie.setRuntime(listJson.getString("vote_average"));
                            movie.setRelease(listJson.getString("first_air_date"));
                            String filename = listJson.getString("poster_path");
                            final String image_url = "https://image.tmdb.org/t/p/w342" + filename;
                            String imageUrl = image_url;
                            movie.setPhoto(imageUrl);
                            listItems.add(movie);
                        }
                        listMovie.postValue(listItems);
                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure", error.getMessage());
                }
            });
        }
        if(Locale.getDefault().getDisplayLanguage().equals("Indonesia")){
            String url = "https://api.themoviedb.org/3/tv/airing_today?api_key="+API_KEY+"&language=id-ID";
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String result = new String(responseBody);
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray list = responseObject.getJSONArray("results");
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject listJson = list.getJSONObject(i);
                            Movie movie = new Movie();
                            movie.setId(listJson.getString("id"));
                            movie.setName(listJson.getString("name"));
                            movie.setDescription(listJson.getString("overview"));
                            movie.setRuntime(listJson.getString("vote_average"));
                            movie.setRelease(listJson.getString("first_air_date"));
                            String filename = listJson.getString("poster_path");
                            final String image_url = "https://image.tmdb.org/t/p/w342" + filename;
                            String imageUrl = image_url;
                            movie.setPhoto(imageUrl);
                            listItems.add(movie);
                        }
                        listMovie.postValue(listItems);
                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure", error.getMessage());
                }
            });
        }
    }

    LiveData<ArrayList<Movie>> getMovie() {
        return listMovie;
    }

    void setNowPlaying() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();
        if (Locale.getDefault().getDisplayLanguage().equals("English")) {
            String url = "https://api.themoviedb.org/3/tv/popular?api_key=" + API_KEY + "&language=en-US";
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    try {
                        String result = new String(responseBody);
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray list = responseObject.getJSONArray("results");
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject listJson = list.getJSONObject(i);
                            Movie movie = new Movie();
                            movie.setId(listJson.getString("id"));
                            movie.setName(listJson.getString("name"));
                            movie.setDescription(listJson.getString("overview"));
                            movie.setRuntime(listJson.getString("vote_average"));
                            movie.setRelease(listJson.getString("first_air_date"));
                            String filename = listJson.getString("poster_path");
                            final String image_url = "https://image.tmdb.org/t/p/w342" + filename;
                            String imageUrl = image_url;
                            movie.setPhoto(imageUrl);
                            listItems.add(movie);
                        }
                        listNowPlaying.postValue(listItems);
                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure", error.getMessage());
                }
            });
        }
        if (Locale.getDefault().getDisplayLanguage().equals("Indonesia")) {
            String url = "https://api.themoviedb.org/3/tv/popular?api_key=" + API_KEY + "&language=id-ID";
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    try {
                        String result = new String(responseBody);
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray list = responseObject.getJSONArray("results");
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject listJson = list.getJSONObject(i);
                            Movie movie = new Movie();
                            movie.setId(listJson.getString("id"));
                            movie.setName(listJson.getString("name"));
                            movie.setDescription(listJson.getString("overview"));
                            movie.setRuntime(listJson.getString("vote_average"));
                            movie.setRelease(listJson.getString("first_air_date"));
                            String filename = listJson.getString("poster_path");
                            final String image_url = "https://image.tmdb.org/t/p/w342" + filename;
                            String imageUrl = image_url;
                            movie.setPhoto(imageUrl);
                            listItems.add(movie);
                        }
                        listNowPlaying.postValue(listItems);
                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure", error.getMessage());
                }

            });
        }
    }

    LiveData<ArrayList<Movie>> getNowPlaying() {
        return listNowPlaying;
    }

    void setPopular() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();
        if (Locale.getDefault().getDisplayLanguage().equals("English")) {
            String url = "https://api.themoviedb.org/3/tv/on_the_air?api_key=" + API_KEY + "&language=en-US";
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    try {
                        String result = new String(responseBody);
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray list = responseObject.getJSONArray("results");
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject listJson = list.getJSONObject(i);
                            Movie movie = new Movie();
                            movie.setId(listJson.getString("id"));
                            movie.setName(listJson.getString("name"));
                            movie.setDescription(listJson.getString("overview"));
                            movie.setRuntime(listJson.getString("vote_average"));
                            movie.setRelease(listJson.getString("first_air_date"));
                            String filename = listJson.getString("poster_path");
                            final String image_url = "https://image.tmdb.org/t/p/w342" + filename;
                            String imageUrl = image_url;
                            movie.setPhoto(imageUrl);
                            listItems.add(movie);
                        }
                        listPopular.postValue(listItems);
                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure", error.getMessage());
                }
            });
        }
        if (Locale.getDefault().getDisplayLanguage().equals("Indonesia")) {
            String url = "https://api.themoviedb.org/3/tv/on_the_air?api_key=" + API_KEY + "&language=id-ID";
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    try {
                        String result = new String(responseBody);
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray list = responseObject.getJSONArray("results");
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject listJson = list.getJSONObject(i);
                            Movie movie = new Movie();
                            movie.setId(listJson.getString("id"));
                            movie.setName(listJson.getString("name"));
                            movie.setDescription(listJson.getString("overview"));
                            movie.setRuntime(listJson.getString("vote_average"));
                            movie.setRelease(listJson.getString("first_air_date"));
                            String filename = listJson.getString("poster_path");
                            final String image_url = "https://image.tmdb.org/t/p/w342" + filename;
                            String imageUrl = image_url;
                            movie.setPhoto(imageUrl);
                            listItems.add(movie);
                        }
                        listPopular.postValue(listItems);
                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure", error.getMessage());
                }

            });
        }
    }

    LiveData<ArrayList<Movie>> getPopular() {
        return listPopular;
    }
}
