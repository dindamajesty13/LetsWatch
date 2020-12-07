package com.majesty.letswatch.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majesty.letswatch.DetailActivity;
import com.majesty.letswatch.R;
import com.majesty.letswatch.adapter.MovieAdapter;
import com.majesty.letswatch.model.Movie;

import java.util.ArrayList;

public class HomeFragment extends Fragment{
    private HomeViewModel homeViewModel;
    private ProgressBar progressBar;
    LinearLayoutManager linearLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.lv_movie);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressBar = rootView.findViewById(R.id.progressBar);
        final MovieAdapter adapter = new MovieAdapter(getActivity());
        homeViewModel.setMovie();
        adapter.notifyDataSetChanged();
        showLoading(true);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie movie) {
                Intent detailActivity = new Intent(getActivity(), DetailActivity.class);
                detailActivity.putExtra("EXTRA_MOVIES", movie);
                startActivity(detailActivity);
            }
        });

        homeViewModel.getMovie().observe(getActivity(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> Movie) {
                if (Movie != null) {
                    adapter.setData(Movie);
                    showLoading(false);
                }
            }
        });

        RecyclerView recyclerView1 = rootView.findViewById(R.id.lv_popular);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView1.setLayoutManager(linearLayoutManager);
        progressBar = rootView.findViewById(R.id.progressBar);
        final MovieAdapter adapter1 = new MovieAdapter(getActivity());
        homeViewModel.setPopular();
        adapter1.notifyDataSetChanged();
        showLoading(true);
        recyclerView1.setAdapter(adapter1);

        adapter1.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie movie) {
                Intent detailActivity = new Intent(getActivity(), DetailActivity.class);
                detailActivity.putExtra("EXTRA_MOVIES", movie);
                startActivity(detailActivity);
            }
        });

        homeViewModel.getPopular().observe(getActivity(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> Movie) {
                if (Movie != null) {
                    adapter1.setData(Movie);
                    showLoading(false);
                }
            }
        });

        RecyclerView recyclerView2 = rootView.findViewById(R.id.lv_now_play);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView2.setLayoutManager(linearLayoutManager);
        progressBar = rootView.findViewById(R.id.progressBar);
        final MovieAdapter adapter2 = new MovieAdapter(getActivity());
        homeViewModel.setNowPlaying();
        adapter2.notifyDataSetChanged();
        showLoading(true);
        recyclerView2.setAdapter(adapter2);

        adapter2.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie movie) {
                Intent detailActivity = new Intent(getActivity(), DetailActivity.class);
                detailActivity.putExtra("EXTRA_MOVIES", movie);
                startActivity(detailActivity);
            }
        });

        homeViewModel.getNowPlaying().observe(getActivity(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> Movie) {
                if (Movie != null) {
                    adapter2.setData(Movie);
                    showLoading(false);
                }
            }
        });

        return rootView;

    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}