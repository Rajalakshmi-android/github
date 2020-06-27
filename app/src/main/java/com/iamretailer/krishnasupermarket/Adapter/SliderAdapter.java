package com.iamretailer.krishnasupermarket.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.iamretailer.krishnasupermarket.FullView;
import com.iamretailer.krishnasupermarket.ProductFullView;


import com.iamretailer.krishnasupermarket.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SliderAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> imgurl;

    public SliderAdapter(Context context, ArrayList<String> imgurl) {
        this.context = context;
        this.imgurl = imgurl;
    }

    @Override
    public int getCount() {
        return imgurl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        Log.d("img_sizeV", imgurl.get(position));
        Picasso.with(view.getContext()).load(imgurl.get(position)).placeholder(R.mipmap.place_holder).noFade().into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent(view.getContext(), FullView.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("url", ((ProductFullView) context).zoom_url);
                data.putExtras(bundle);
                view.getContext().startActivity(data);
            }
        });


        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
