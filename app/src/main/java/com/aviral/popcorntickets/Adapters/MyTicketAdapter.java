package com.aviral.popcorntickets.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aviral.popcorntickets.DataObjects.Movie;
import com.aviral.popcorntickets.DataObjects.MovieData;
import com.aviral.popcorntickets.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.List;

public class MyTicketAdapter extends RecyclerView.Adapter<MyTicketAdapter.MyTicketViewHolder> {

    private List<MovieData> movieList;
    private OnItemClickListener listener;

    public MyTicketAdapter(List<MovieData> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MyTicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ticket, parent, false);
        return new MyTicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTicketViewHolder holder, int position) {
        MovieData movie = movieList.get(position);
        holder.title.setText(movie.title);
        Glide.with(holder.itemView.getContext())
                .load(movie.img)
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.poster);
        holder.date.setText(movie.date);
        holder.time.setText(movie.time);

        ArrayList<String> seats = new ArrayList<>();
        seats=movie.seats;
        String seat="";
        for (String s : seats)
        {
            seat += s+",";
        }
        holder.seats.setText(seat);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    static class MyTicketViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title,date,time,seats;

        public MyTicketViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.home_movie_poster);
            title = itemView.findViewById(R.id.home_movie_title);
            date = itemView.findViewById(R.id.home_date);
            time = itemView.findViewById(R.id.home_time);
            seats = itemView.findViewById(R.id.home_seats);

        }
    }
}
