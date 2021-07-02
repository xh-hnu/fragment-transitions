package com.example.gridtopager.adapter;

import static com.example.gridtopager.adapter.ImageData.IMAGE_DRAWABLES;

import android.graphics.ImageFormat;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gridtopager.fragment.ImageFragment;
import com.example.gridtopager.fragment.ImagePagerFragment;

public class ImagePagerAdapter extends FragmentStatePagerAdapter {


    public ImagePagerAdapter(Fragment fragment) {
        super(fragment.getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(IMAGE_DRAWABLES[position]);
    }

    @Override
    public int getCount() {
        return IMAGE_DRAWABLES.length;
    }
}
