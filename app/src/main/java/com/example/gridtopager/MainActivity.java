package com.example.gridtopager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.example.gridtopager.fragment.ExampleFragment;
import com.example.gridtopager.fragment.GridFragment;

public class MainActivity extends AppCompatActivity {

    public static int currentPosition;
    private static final String KEY_CURRENT_POSITION = "key_current_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, GridFragment.class, null)
                .setReorderingAllowed(true)
                .commit();
    }
}