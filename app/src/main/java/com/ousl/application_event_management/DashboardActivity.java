package com.ousl.application_event_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ousl.application_event_management.adapters.FragmentsAdapter;
import com.ousl.application_event_management.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        floatingActionButtonFunction();

        binding.publicEventAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PublicEventEntry.class);
                startActivity(intent);
            }
        });

        binding.privateEventAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PrivateEventEntry.class);
                startActivity(intent);
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

//                    Animation animation2 = AnimationUtils.loadAnimation(dashboard.this, R.anim.from_bottom_anim);
//                    binding.appBarDashboard.publicEventAction.startAnimation(animation2);
//
//                    Animation animation3 = AnimationUtils.loadAnimation(dashboard.this, R.anim.from_bottom_anim);
//                    binding.appBarDashboard.privateEventAction.startAnimation(animation3);

                    binding.privateEventAction.setVisibility(View.VISIBLE);
                    binding.publicEventAction.setVisibility(View.VISIBLE);

                    shown = true;
                } else {
                    binding.privateEventAction.setVisibility(View.GONE);
                    binding.publicEventAction.setVisibility(View.GONE);

                    Animation animation = AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.rotate_close_anim);
                    binding.mainActionButton.startAnimation(animation);

//                    Animation animation2 = AnimationUtils.loadAnimation(dashboard.this, R.anim.to_bottom_anim);
//                    binding.appBarDashboard.publicEventAction.startAnimation(animation2);
//
//                    Animation animation3 = AnimationUtils.loadAnimation(dashboard.this, R.anim.to_bottom_anim);
//                    binding.appBarDashboard.privateEventAction.startAnimation(animation3);

                    shown = false;
                }
            }
        });
    }
}