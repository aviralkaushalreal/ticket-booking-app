package com.aviral.popcorntickets.Booking;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aviral.popcorntickets.Adapters.SeatAdapter;
import com.aviral.popcorntickets.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class BookSeat extends AppCompatActivity {
    String selected_date;
    String selected_time;
    String title;
    GridView gridView;
    SeatAdapter adapter;


    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_seat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        title = getIntent().getStringExtra("title");
        int cost=getIntent().getIntExtra("cost",0);

        gridView = findViewById(R.id.gridViewSeats);

        TextView date1 = findViewById(R.id.date1);
        TextView date2 = findViewById(R.id.date2);
        TextView date3 = findViewById(R.id.date3);
        TextView time1 = findViewById(R.id.time1);
        TextView time2 = findViewById(R.id.time2);
        TextView time3 = findViewById(R.id.time3);


        date1.setOnClickListener(v -> {
            resetDate(date1);
        });
        date2.setOnClickListener(v -> {
            resetDate(date2);
        });
        date3.setOnClickListener(v -> {
            resetDate(date3);
        });
        time1.setOnClickListener(v -> {
            resetTime(time1);
        });
        time2.setOnClickListener(v -> {
            resetTime(time2);
        });
        time3.setOnClickListener(v -> {
            resetTime(time3);
        });


        List<String> seats = new ArrayList<>();
        for (char row = 'A'; row <= 'D'; row++) {
            for (int num = 1; num <= 5; num++) {
                seats.add(row + String.valueOf(num));
            }
        }


        selected_time = "11AM";
        selected_date = getTodayDate();
        date1.setText(getTodayDate());
        date2.setText(getTomorrow());
        date3.setText(getDayAfterTomorrow());
        String key = getTodayDate() + "-" + selected_time;

        adapter = new SeatAdapter(this, seats, title, key);
        gridView.setAdapter(adapter);

        TextView bookBtn = findViewById(R.id.bookBtn);

        bookBtn.setOnClickListener(v -> {
            Set<String> selected = adapter.getSelectedSeats();
            ArrayList<String> selectedList = new ArrayList<>(selected);
            Intent intent=new Intent(this,ConfirmPage.class);
            intent.putStringArrayListExtra("seats", selectedList);
            intent.putExtra("title",title);
            intent.putExtra("date",selected_date);
            intent.putExtra("time",selected_time);
            intent.putExtra("cost",cost);
            intent.putExtra("img",getIntent().getStringExtra("img"));
            startActivity(intent);
            finish();

        });

    }

    String formatDate(Calendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("d-M-yy", Locale.getDefault());
        return sdf.format(cal.getTime());
    }


    String getTomorrow() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        return formatDate(cal);
    }

    String getDayAfterTomorrow() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 2);
        return formatDate(cal);
    }


    private void resetDate(TextView date1) {
        TextView date = findViewById(R.id.date1);
        TextView date2 = findViewById(R.id.date2);
        TextView date3 = findViewById(R.id.date3);
        selected_date = date1.getText().toString();
        String s = getIntent().getStringExtra("title");
        adapter.updateTime(s, selected_date + "-" + selected_time);

        if (date1 == date) {

            date.setBackgroundResource(R.drawable.btn_selected);
            date2.setBackgroundResource(R.drawable.btn_back);
            date3.setBackgroundResource(R.drawable.btn_back);
        } else if (date1 == date2) {
            date2.setBackgroundResource(R.drawable.btn_selected);
            date.setBackgroundResource(R.drawable.btn_back);
            date3.setBackgroundResource(R.drawable.btn_back);
        } else {
            date3.setBackgroundResource(R.drawable.btn_selected);
            date.setBackgroundResource(R.drawable.btn_back);
            date2.setBackgroundResource(R.drawable.btn_back);
        }
    }

    String getTodayDate() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("d-M-yy", Locale.getDefault());
        return sdf.format(now);
    }


    private void resetTime(TextView time) {

        TextView time1 = findViewById(R.id.time1);
        TextView time2 = findViewById(R.id.time2);
        TextView time3 = findViewById(R.id.time3);

        selected_time = time.getText().toString();
        String key = selected_date + "-" + selected_time;
        String s = getIntent().getStringExtra("title");
        adapter.updateTime(s, key);

        time1.setBackgroundResource(R.drawable.btn_back);
        time2.setBackgroundResource(R.drawable.btn_back);
        time3.setBackgroundResource(R.drawable.btn_back);

        time.setBackgroundResource(R.drawable.btn_selected);

    }


}