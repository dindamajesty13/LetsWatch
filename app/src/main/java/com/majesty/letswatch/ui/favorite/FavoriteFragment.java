package com.majesty.letswatch.ui.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.majesty.letswatch.R;
import com.majesty.letswatch.adapter.SectionsPagerAdapter;
import com.majesty.letswatch.ui.favorite.favoriteMovie.FavoriteMovieFragment;
import com.majesty.letswatch.ui.favorite.favoriteTv.FavoriteTvShowFragment;


public class FavoriteFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorite_fragment, container, false);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        sectionsPagerAdapter.AddFragment(new FavoriteMovieFragment(), "Movie");
        sectionsPagerAdapter.AddFragment(new FavoriteTvShowFragment(), "Tv Show");

        ViewPager viewPager = rootView.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = rootView.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        return rootView;
    }

}
