package com.majesty.letswatch.ui.favorite.favoriteMovie;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majesty.letswatch.DetailActivityTvShow;
import com.majesty.letswatch.R;
import com.majesty.letswatch.adapter.FavoriteTvShowAdapter;
import com.majesty.letswatch.database.DatabaseHelper;

public class FavoriteMovieFragment extends Fragment {

    DatabaseHelper databaseHelper;
    LinearLayoutManager linearLayoutManager;
    FavoriteTvShowAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getActivity());
        final Cursor cursor = databaseHelper.getDataFavoriteMovie();
        View rootView = inflater.inflate(R.layout.favorite_tv_show_fragment, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.lv_movie);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new FavoriteTvShowAdapter(getActivity(), cursor);
        recyclerView.setAdapter(adapter);

        return rootView;

    }
}
