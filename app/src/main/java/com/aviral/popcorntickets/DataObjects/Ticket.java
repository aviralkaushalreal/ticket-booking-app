package com.aviral.popcorntickets.DataObjects;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aviral.popcorntickets.R;

public class Ticket extends AppCompatActivity {
    TextView title,seats,date,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ticket);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        title = findViewById(R.id.confirm_title);
        seats=findViewById(R.id.confirm_seats);
        date=findViewById(R.id.confirm_date);
        time=findViewById(R.id.confirm_time);

        Intent intent=getIntent();
        title.setText(intent.getStringExtra("title"));
        date.setText("Date : "+intent.getStringExtra("date"));
        time.setText("Time : "+intent.getStringExtra("time"));
        String seatss="";
        for (String s : intent.getStringArrayListExtra("seats"))
        {
            seatss += " "+s;
        }
        seats.setText("Seats : "+seatss);
    }
}