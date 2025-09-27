package com.aviral.popcorntickets.Auth;

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
import com.aviral.popcorntickets.Adapters.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    TextView reg,login;
    EditText email,password;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(auth.getCurrentUser() != null)
        {
            Intent intent=new Intent(this, Homescreen.class);
            startActivity(intent);
            finish();
        }

        reg=findViewById(R.id.btnLoginReg);
        login=findViewById(R.id.btnLogin);
        email=findViewById(R.id.loginemail);
        password=findViewById(R.id.loginpassword);

        login.setOnClickListener(v -> {
            String email_ = email.getText().toString();
            String pass = password.getText().toString();

            loginuser(email_,pass);
        });




        reg.setOnClickListener(v->{
            Intent intent=new Intent(this, SignupActivity.class);
            startActivity(intent);
            finish();
        });


    }

    private void loginuser(String email, String pass) {


        if(email.equals("") || pass.equals(""))
        {
            Toast.makeText(this, "Please enter details", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(success->{
            Intent intent=new Intent(this, Homescreen.class);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e ->{
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });


    }
}