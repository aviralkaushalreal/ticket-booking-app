package com.aviral.popcorntickets.Booking;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.aviral.popcorntickets.R;
import com.aviral.popcorntickets.databinding.ActivityHomescreenBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Homescreen extends AppCompatActivity {

    private ActivityHomescreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        binding = ActivityHomescreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications,R.id.navigation_my_tickets)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_homescreen);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);





    }

}