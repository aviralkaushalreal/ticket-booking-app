package com.aviral.popcorntickets.Booking;

import static android.view.View.INVISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aviral.popcorntickets.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MovieScreen extends AppCompatActivity {

    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView title_ = findViewById(R.id.movie_title);
        ImageView img = findViewById(R.id.poster);
        TextView about = findViewById(R.id.movie_about);
        CardView progress = findViewById(R.id.progress_data);
        TextView book = findViewById(R.id.book_ticket);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String url = intent.getStringExtra("url");
        int cost=intent.getIntExtra("cost",0);

        book.setOnClickListener(v -> {
            Intent intent_ = new Intent(this,BookSeat.class);
            intent_.putExtra("title",title);
            intent_.putExtra("img",url);
            intent_.putExtra("cost",cost);
            startActivity(intent_);
        });


        getAbout(title,about,progress);

        title_.setText(title);
        Glide.with(this)
                .load(url)
                .into(img);
        TextView cost_=findViewById(R.id.movie_cost);
        cost_.setText("₹"+cost);

    }
    String about="";

    private String getAbout(String title, TextView aboutTextView, CardView pb) {

        firestore.collection("movies").whereEqualTo("title",title).get().addOnSuccessListener( querySnapshot -> {
            for (QueryDocumentSnapshot doc:querySnapshot)
            {
                about = doc.getString("about");
                if (about != null) {
                    aboutTextView.setText(about);
                    pb.setVisibility(INVISIBLE);
                }
            }
        });

        return about;
    }
}