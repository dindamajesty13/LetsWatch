package com.majesty.letswatch.ui.favorite.favoriteTv;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majesty.letswatch.DetailActivity;
import com.majesty.letswatch.R;
import com.majesty.letswatch.adapter.FavoriteAdapter;
import com.majesty.letswatch.database.DatabaseHelper;


public class FavoriteTvShowFragment extends Fragment {

    DatabaseHelper databaseHelper;
    FavoriteAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getActivity());
        final Cursor cursor = databaseHelper.getDataFavoriteTvShow();
        View rootView = inflater.inflate(R.layout.favorite_tv_show_fragment, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.lv_movie);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FavoriteAdapter(getActivity(), cursor);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}
