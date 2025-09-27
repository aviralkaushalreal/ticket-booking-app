package com.aviral.popcorntickets.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aviral.popcorntickets.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatAdapter extends BaseAdapter {
    private final List<String> booked = new ArrayList<>();
    private final String title;
    private final String time;
    private final Context context;
    private final List<String> seats;
    private final Set<String> selectedSeats = new HashSet<>();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public SeatAdapter(Context context, List<String> seats, String title_, String time_) {
        this.context = context;
        this.seats = seats;
        this.title = title_;
        this.time = time_;

        bookedSeats(title_, time_);
    }

    public void updateTime( String s,String time_) {
        this.booked.clear();
        this.selectedSeats.clear();
        this.bookedSeats(s, time_);

    }


    public void bookedSeats(String title__, String time__) {
        firestore.collection("movies")
                .whereEqualTo("title", title__)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        ArrayList<String> seats_ = (ArrayList<String>) doc.get(time__);
                        if(seats_ != null) {
                            for (String s : seats_) {
                                booked.add(s);
                            }
                        }
                    }
                    notifyDataSetChanged();
                });
    }

    @Override
    public int getCount() {
        return seats.size();
    }

    @Override
    public Object getItem(int position) {
        return seats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.seat_item_layout, parent, false);
        }

        TextView seatText = convertView.findViewById(R.id.seatText);
        String seat = seats.get(position);
        seatText.setText(seat);

        if (booked.contains(seat)) {
            seatText.setBackgroundResource(R.drawable.seat_booked);
            seatText.setEnabled(false);
            seatText.setOnClickListener(null); // remove listener
        } else {
            // Available seat → toggle between selected and available
            seatText.setEnabled(true);

            if (selectedSeats.contains(seat)) {
                seatText.setBackgroundResource(R.drawable.seat_selected);
            } else {
                seatText.setBackgroundResource(R.drawable.seat_available);
            }

            seatText.setOnClickListener(v -> {
                if (selectedSeats.contains(seat)) {
                    selectedSeats.remove(seat);
                } else {
                    selectedSeats.add(seat);
                }
                notifyDataSetChanged(); // refresh UI
            });
        }

        return convertView;
    }

    public Set<String> getSelectedSeats() {
        return selectedSeats;
    }


}
