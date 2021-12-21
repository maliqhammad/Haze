package com.hammad.iphoneringtones.ui.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;

import com.hammad.iphoneringtones.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Categories {
    public static ArrayList<String> getFeatures(Context context) {
        return new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.features_name)));
    }

    public static List<String> getColors(Context context) {
        Log.d("TAG", "getColors: " + Arrays.asList(context.getResources().getStringArray(R.array.colors_array)).size());
        return Arrays.asList(context.getResources().getStringArray(R.array.colors_array));
    }

    public static TypedArray getFeatureImages(Context context) {
        return context.getResources().obtainTypedArray(R.array.features_images_array);
    }
}
