package com.ousl.application_event_management.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.ousl.application_event_management.R;
import com.ousl.application_event_management.databinding.ActivitySearchViewBinding;
import com.ousl.application_event_management.views.adapters.PublicEventAdapter;
import com.ousl.application_event_management.views.adapters.SearchViewAdapter;

public class SearchViewActivity extends AppCompatActivity {

    ActivitySearchViewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SearchViewAdapter searchViewAdapter = new SearchViewAdapter(this);
        binding.searchView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.searchView.setAdapter(searchViewAdapter);

        binding.Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.w("listener", "onTextChanged: working");
                searchViewAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}