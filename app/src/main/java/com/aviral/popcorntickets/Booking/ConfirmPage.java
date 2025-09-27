package com.aviral.popcorntickets.Booking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aviral.popcorntickets.DataObjects.MovieData;
import com.aviral.popcorntickets.DataObjects.Ticket;
import com.aviral.popcorntickets.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import com.razorpay.PaymentResultListener;
import com.razorpay.Checkout;



public class ConfirmPage extends AppCompatActivity implements PaymentResultListener  {
    TextView title, date, time, seats, confirm,cost;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    int total_cost=0;
    FirebaseAuth auth = FirebaseAuth.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Checkout.preload(getApplicationContext());

        title = findViewById(R.id.confirm_title);
        seats = findViewById(R.id.confirm_seats);
        date = findViewById(R.id.confirm_date);
        time = findViewById(R.id.confirm_time);
        confirm = findViewById(R.id.btnConfirm);


        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        date.setText("Date : " + intent.getStringExtra("date"));
        time.setText("Time : " + intent.getStringExtra("time"));
        String seatss = "";
        for (String s : intent.getStringArrayListExtra("seats")) {
            seatss += " " + s;
        }
        seats.setText("Seats : " + seatss);
        title.setText(intent.getStringExtra("title"));

        cost=findViewById(R.id.cost);

        total_cost = (intent.getIntExtra("cost",0))*(intent.getStringArrayListExtra("seats").size());
        cost.setText("₹"+total_cost);
        confirm.setOnClickListener(v -> {
            startPayment();
        });


    }

    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_PS8Ayb7WVNmhds"); // test key

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Movie Tickets");
            options.put("description", "Ticket Booking for Movie");
            options.put("currency", "INR");
            options.put("amount", total_cost*100); // amount in paise (₹1 = 100 paise)

            JSONObject prefill = new JSONObject();
            prefill.put("email", "example@mail.com");
            prefill.put("contact", "9999999999");
            options.put("prefill", prefill);

            checkout.open(this, options);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Intent intent = getIntent();
        Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        ArrayList<String> seatsToBook = intent.getStringArrayListExtra("seats");


        firestore.collection("movies").whereEqualTo("title", intent.getStringExtra("title")).get().addOnSuccessListener(querySnapshot -> {
            for (QueryDocumentSnapshot doc : querySnapshot) {
                String key = intent.getStringExtra("date") + "-" + intent.getStringExtra("time");
                doc.getReference().update(key, com.google.firebase.firestore.FieldValue.arrayUnion(seatsToBook.toArray())).addOnSuccessListener(success -> {
                    Toast.makeText(ConfirmPage.this, "Tickets Booked Successfully", Toast.LENGTH_SHORT).show();
                });

            }
        });

        MovieData movieData = new MovieData();
        movieData.setData(intent.getStringExtra("title"), intent.getStringExtra("date"), intent.getStringExtra("time"), intent.getStringArrayListExtra("seats"), getIntent().getStringExtra("img"));
        firestore.collection("users").document(auth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    long num = 0;

                    if (documentSnapshot.exists()) {
                        Object numObj = documentSnapshot.get("num");
                        if (numObj != null) {
                            if (numObj instanceof Long) num = (Long) numObj;
                            else if (numObj instanceof Integer) num = ((Integer) numObj).longValue();
                        }
                        num++;
                        firestore.collection("users").document(auth.getCurrentUser().getUid())
                                .update("num", com.google.firebase.firestore.FieldValue.increment(1),
                                        "ticket" + num, movieData);
                    } else {
                        // New user → create document
                        HashMap<String, Object> userData = new HashMap<>();
                        userData.put("num", 1);
                        userData.put("ticket1", movieData);

                        firestore.collection("users").document(auth.getCurrentUser().getUid())
                                .set(userData);
                    }

                    // Go to ticket page
                    Intent intent1 = new Intent(ConfirmPage.this, Ticket.class);
                    intent1.putExtra("title", movieData.title);
                    intent1.putExtra("date", movieData.date);
                    intent1.putExtra("time", movieData.time);
                    intent1.putExtra("seats", movieData.seats);
                    intent1.putExtra("img", getIntent().getStringExtra("img"));
                    startActivity(intent1);
                    finish();
                });

    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment failed: " + response, Toast.LENGTH_SHORT).show();
    }



}