package com.aviral.popcorntickets.Booking;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aviral.popcorntickets.Adapters.MyTicketAdapter;
import com.aviral.popcorntickets.DataObjects.MovieData;
import com.aviral.popcorntickets.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyTicketsFragment extends Fragment {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private MyTicketAdapter adapter;

    public MyTicketsFragment() {
        super(R.layout.fragment_my_tickets);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<MovieData> movies = new ArrayList<>();
        RecyclerView rvMovies = view.findViewById(R.id.trending_recycler);
        adapter = new MyTicketAdapter(movies);
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvMovies.setAdapter(adapter);

        firestore.collection("users").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Map<String, Object> allFields = documentSnapshot.getData(); // All fields in the document

                if (allFields != null) {
                    for (Map.Entry<String, Object> entry : allFields.entrySet()) {
                        String fieldName = entry.getKey();     // e.g. "ticket1", "ticket2"
                        Object fieldValue = entry.getValue();  // The data stored in that field

                        if(!fieldName.equals("num")) {
                            if (fieldValue instanceof Map) {
                                Map<String, Object> ticketData = (Map<String, Object>) fieldValue;
                                String title = (String) ticketData.get("title");
                                String img = (String) ticketData.get("img");
                                MovieData movie = new MovieData();
                                movie.setData(title, (String) ticketData.get("date"), (String) ticketData.get("time"), (ArrayList<String>)ticketData.get("seats"), img);
                                movies.add(movie);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        });
    }
}
