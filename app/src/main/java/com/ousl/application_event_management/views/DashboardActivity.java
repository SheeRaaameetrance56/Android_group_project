package com.ousl.application_event_management.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.ousl.application_event_management.R;
import com.ousl.application_event_management.views.adapters.FragmentsAdapter;
import com.ousl.application_event_management.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbarDashboard;
        setSupportActionBar(toolbar);

        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        floatingActionButtonFunction();

        binding.publicEventAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PublicEventEntry.class);
                startActivity(intent);
                finish();
            }
        });

        binding.privateEventAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PrivateEventEntry.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public void floatingActionButtonFunction(){
        binding.mainActionButton.setOnClickListener(new View.OnClickListener() {
            boolean shown = false;

            @Override
            public void onClick(View view) {

                if (!shown) {
                    Animation animation = AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.rotate_open_anim);
                    binding.mainActionButton.startAnimation(animation);

                    binding.privateEventAction.setVisibility(View.VISIBLE);
                    binding.publicEventAction.setVisibility(View.VISIBLE);

                    shown = true;
                } else {
                    binding.privateEventAction.setVisibility(View.GONE);
                    binding.publicEventAction.setVisibility(View.GONE);

                    Animation animation = AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.rotate_close_anim);
                    binding.mainActionButton.startAnimation(animation);

                    shown = false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.toolbar_menu_profile){
            Intent intent = new Intent(DashboardActivity.this, ProfileViewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }
        if(id == R.id.dropdown_menu_log_out){
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        if(id == R.id.dropdown_menu_about){
            startActivity(new Intent(DashboardActivity.this, AboutUsActivity.class));
        }
        return true;
    }
}