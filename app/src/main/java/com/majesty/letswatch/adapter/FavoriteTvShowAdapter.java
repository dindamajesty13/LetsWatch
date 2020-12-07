package com.majesty.letswatch.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majesty.letswatch.DetailActivity;
import com.majesty.letswatch.DetailActivityTvShow;
import com.majesty.letswatch.R;
import com.majesty.letswatch.database.DatabaseContract;
import com.squareup.picasso.Picasso;

public class FavoriteTvShowAdapter extends RecyclerView.Adapter<FavoriteTvShowAdapter.ListViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public FavoriteTvShowAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }


    @NonNull
    @Override
    public FavoriteTvShowAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteTvShowAdapter.ListViewHolder(view);
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto, fav;
        TextView txtName, txtDescription, txtRelease, txtRuntime;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            txtName = itemView.findViewById(R.id.txt_judul);
            txtDescription = itemView.findViewById(R.id.txt_description);
            txtRelease = itemView.findViewById(R.id.txt_release);
//            txtRuntime = itemView.findViewById(R.id.txt_runtime);
            fav = itemView.findViewById(R.id.favorite);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteTvShowAdapter.ListViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String id_movie = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.FavoriteColumns.ID_MOVIE));
        String imageUrl = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.FavoriteColumns.PHOTO_MOVIE));
        String Name = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.FavoriteColumns.NAME_MOVIE));
        String Desc = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.FavoriteColumns.DESCRIPTION_MOVIE));
        String Release = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.FavoriteColumns.RELEASE_MOVIE));
        String Rating = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.FavoriteColumns.RATING_MOVIE));

//        holder.txtid_movie.setText(id_movie);
        holder.txtName.setText(Name);
        holder.txtDescription.setText(Desc);
        Picasso.get().load(imageUrl).fit().into(holder.imgPhoto);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailActivity = new Intent(mContext, DetailActivity.class);
                detailActivity.putExtra("id_movie", id_movie);
                detailActivity.putExtra("photo", imageUrl);
                detailActivity.putExtra("name", Name);
                detailActivity.putExtra("description", Desc);
                detailActivity.putExtra("release", Release);
                detailActivity.putExtra("rating", Rating);
                mContext.startActivity(detailActivity);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public FavoriteTvShowAdapter(Context context) {
        context = context;
    }

    private FavoriteTvShowAdapter.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(FavoriteTvShowAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(Cursor mcursor);
    }


}
