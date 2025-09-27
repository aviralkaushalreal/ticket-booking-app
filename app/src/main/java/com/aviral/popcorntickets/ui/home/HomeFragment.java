package com.aviral.popcorntickets.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aviral.popcorntickets.DataObjects.Movie;
import com.aviral.popcorntickets.Adapters.MovieAdapter;
import com.aviral.popcorntickets.Booking.MovieScreen;
import com.aviral.popcorntickets.R;
import com.aviral.popcorntickets.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        RecyclerView rvMovies = root.findViewById(R.id.trending_recycler);
        /*rvMovies.setLayoutManager(
                new LinearLayoutManager(getContext(),2 LinearLayoutManager.HORIZONTAL, false));*/

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvMovies.setLayoutManager(gridLayoutManager);

        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("F1", "https://upload.wikimedia.org/wikipedia/en/3/38/F1_%282025_film%29.png",450));
        movies.add(new Movie("Gully Boy", "https://upload.wikimedia.org/wikipedia/en/0/07/Gully_Boy_poster.jpg",250));
        movies.add(new Movie("WAR", "https://upload.wikimedia.org/wikipedia/en/6/6f/War_official_poster.jpg",200));
        movies.add(new Movie("Fight Club", "https://s3.amazonaws.com/nightjarprod/content/uploads/sites/344/2024/08/21164326/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK-scaled.jpg",300));
        movies.add(new Movie("Avengers : Endgame", "https://upload.wikimedia.org/wikipedia/en/0/0d/Avengers_Endgame_poster.jpg",350));

        RecyclerView.Adapter adapter = new MovieAdapter(movies);
        rvMovies.setAdapter(adapter);

        ((MovieAdapter) adapter).setOnItemClickListener(movie -> {
            String clickedTitle = movie.getTitle();
            String imgUrl=movie.getUrl();
            Intent intent=new Intent(getContext(), MovieScreen.class);
            intent.putExtra("title",clickedTitle);
            intent.putExtra("url",imgUrl);
            intent.putExtra("cost",movie.getCost());
            startActivity(intent);

        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}