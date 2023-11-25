package com.ousl.application_event_management.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ousl.application_event_management.databinding.FragmentMyEventsBinding;

public class MyEventsFragment extends Fragment {

    private FragmentMyEventsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //
        return root;
    }

    public MyEventsFragment() {
        // Required empty public constructor
    }

}