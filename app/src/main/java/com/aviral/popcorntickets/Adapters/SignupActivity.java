package com.aviral.popcorntickets.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aviral.popcorntickets.Booking.Homescreen;
import com.aviral.popcorntickets.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    TextView reg,signup;

    EditText email,name,pass;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        reg=findViewById(R.id.btnRegisterlog);
        signup=findViewById(R.id.btnRegister);
        email=findViewById(R.id.signupemail);
        name=findViewById(R.id.signupemail);
        pass=findViewById(R.id.signuppassword);


        signup.setOnClickListener(v->{
            String email_ = email.getText().toString();
            String pass_ = pass.getText().toString();
            String name_ = name.getText().toString();


            signupUser(name_,email_,pass_);
        });




        reg.setOnClickListener(v->{
            Intent intent=new Intent(this, Homescreen.class);
            startActivity(intent);
            finish();
        });
    }

    private void signupUser(String name, String email, String pass) {
        auth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(success ->{
            firestore.collection("users").document(auth.getCurrentUser().getUid()).update("username",name);
            Intent intent=new Intent(this,Homescreen.class);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e->{
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }
}