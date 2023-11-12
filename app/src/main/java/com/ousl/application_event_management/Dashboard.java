package com.ousl.application_event_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.ousl.application_event_management.databinding.ActivityDashboardBinding;

public class Dashboard extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDashboardBinding binding;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDashboard.toolbar);
        auth = FirebaseAuth.getInstance();

        binding.appBarDashboard.mainActionButton.setOnClickListener(new View.OnClickListener() {
            boolean shown = false;

            @Override
            public void onClick(View view) {

                if (!shown) {
                    Animation animation = AnimationUtils.loadAnimation(Dashboard.this, R.anim.rotate_open_anim);
                    binding.appBarDashboard.mainActionButton.startAnimation(animation);

//                    Animation animation2 = AnimationUtils.loadAnimation(dashboard.this, R.anim.from_bottom_anim);
//                    binding.appBarDashboard.publicEventAction.startAnimation(animation2);
//
//                    Animation animation3 = AnimationUtils.loadAnimation(dashboard.this, R.anim.from_bottom_anim);
//                    binding.appBarDashboard.privateEventAction.startAnimation(animation3);

                    binding.appBarDashboard.privateEventAction.setVisibility(View.VISIBLE);
                    binding.appBarDashboard.publicEventAction.setVisibility(View.VISIBLE);

                    shown = true;
                } else {
                    binding.appBarDashboard.privateEventAction.setVisibility(View.GONE);
                    binding.appBarDashboard.publicEventAction.setVisibility(View.GONE);

                    Animation animation = AnimationUtils.loadAnimation(Dashboard.this, R.anim.rotate_close_anim);
                    binding.appBarDashboard.mainActionButton.startAnimation(animation);

//                    Animation animation2 = AnimationUtils.loadAnimation(dashboard.this, R.anim.to_bottom_anim);
//                    binding.appBarDashboard.publicEventAction.startAnimation(animation2);
//
//                    Animation animation3 = AnimationUtils.loadAnimation(dashboard.this, R.anim.to_bottom_anim);
//                    binding.appBarDashboard.privateEventAction.startAnimation(animation3);

                    shown = false;
                }
            }
        });

        binding.appBarDashboard.publicEventAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, PublicEventEntry.class);
                startActivity(intent);
            }
        });

        binding.appBarDashboard.privateEventAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, PrivateEventEntry.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dasboard, R.id.nav_my_events, R.id.nav_friend_list)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
        navController.setGraph(R.navigation.mobile_navigation); // Replace with your navigation graph ID


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_drop_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_logout:
                auth.signOut();
                Intent intent = new Intent(Dashboard.this, LoginActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                break;

            case R.id.action_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}