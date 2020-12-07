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

import com.majesty.letswatch.DetailActivityTvShow;
import com.majesty.letswatch.R;
import com.majesty.letswatch.database.DatabaseContract;
import com.squareup.picasso.Picasso;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ListViewHolder> {
    Context context;
    Cursor mCursor;

    public FavoriteAdapter(Context context, Cursor mCursor) {
        this.mCursor = mCursor;
        this.context = context;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto, fav;
        TextView txtName, txtDescription, txtRelease, txtRuntime, txtid;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
//            txtid = itemView.findViewById(R.id.id);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            txtName = itemView.findViewById(R.id.txt_judul);
            txtDescription = itemView.findViewById(R.id.txt_description);
            txtRelease = itemView.findViewById(R.id.txt_release);
//            txtRuntime = itemView.findViewById(R.id.txt_runtime);
            fav = itemView.findViewById(R.id.favorite);
        }
    }

    @NonNull
    @Override
    public FavoriteAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteAdapter.ListViewHolder holder, final int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.FavoriteColumns.ID));
        String imageUrl = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.FavoriteColumns.PHOTO));
        String Name = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.FavoriteColumns.NAME));
        String Desc = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.FavoriteColumns.DESCRIPTION));
        String Release = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.FavoriteColumns.RELEASE));
        String Rating = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.FavoriteColumns.RATING));

//        holder.txtid.setText(id);
        holder.txtName.setText(Name);
        holder.txtDescription.setText(Desc);
        Picasso.get().load(imageUrl).fit().into(holder.imgPhoto);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailActivity = new Intent(context, DetailActivityTvShow.class);
                detailActivity.putExtra("id", id);
                detailActivity.putExtra("photo", imageUrl);
                detailActivity.putExtra("name", Name);
                detailActivity.putExtra("description", Desc);
                detailActivity.putExtra("release", Release);
                detailActivity.putExtra("rating", Rating);
                context.startActivity(detailActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(Cursor cursor);
    }
}
