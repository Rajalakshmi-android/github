package com.iamretailer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.iamretailer.FullView;
import com.iamretailer.ProductFullView;


import com.iamretailer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SliderAdapter extends PagerAdapter {
    private final Context context;
    private final ArrayList<String> imgurl;

    public SliderAdapter(Context context, ArrayList<String> imgurl) {
        this.context = context;
        this.imgurl = imgurl;
    }

    @Override
    public int getCount() {
        return imgurl.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        if (inflater != null) {
            view = inflater.inflate(R.layout.item_slider, null, false);
        }
        ImageView imageView = null;
        if (view != null) {
            imageView = view.findViewById(R.id.image);
        }
        if (imgurl.get(position)!=null && imgurl.get(position).length() > 0)
            Picasso.with(context).load(imgurl.get(position)).placeholder(R.mipmap.place_holder).noFade().into(imageView);
        else
            Picasso.with(context).load(R.mipmap.place_holder).placeholder(R.mipmap.place_holder).noFade().into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent(view.getContext(), FullView.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("url", ((ProductFullView) context).zoom_url);
                bundle.putInt("image", position);
                data.putExtras(bundle);
                view.getContext().startActivity(data);
            }
        });


        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
