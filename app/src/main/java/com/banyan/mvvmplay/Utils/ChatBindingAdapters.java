package com.banyan.mvvmplay.Utils;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.banyan.mvvmplay.R;
import com.banyan.mvvmplay.databinding.ChatViewBinding;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;


/**
 * CallLogView, used ListView to show recent call logs, Follows MVVM pattern
 */
public class ChatBindingAdapters {

    @BindingAdapter("bindNetworkSrc")
    public static void bindNetworkSrc(final ImageView imageView, final String src) {
        Glide.with(imageView.getContext()).load(src).into(imageView);
    }
}