/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Cleveroad
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */
package com.iamretailer.Common;



import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPagerWrapper extends ViewPager {
    private OnPageChangeListener listener;
    private PagerAdapter pagerAdapter;
    private ViewPager delegate;
     
    public ViewPagerWrapper(Context context) {
        super(context);
    }

    public ViewPagerWrapper(Context context, ViewPager viewPager) {
        super(context);
        this.delegate = viewPager;
        delegate.setOnPageChangeListener(new OnPageChangeListenerWrapper(context));
    }

    @Override
    public PagerAdapter getAdapter() {
        if (pagerAdapter == null) {
            pagerAdapter = new PagerAdapterWrapper(delegate.getAdapter());
        }

        return pagerAdapter;
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.listener = listener;
    }

    public void addOnPageChangeListener(OnPageChangeListener listener) {
        this.listener = listener;
    }

    private class OnPageChangeListenerWrapper implements OnPageChangeListener {
           Context cc;
        public OnPageChangeListenerWrapper(Context context) {
			// TODO Auto-generated constructor stub
        	cc=context;
		}

		@Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int penultimatePosition = getAdapter().getCount() - 3;
            listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            
        // Toast.makeText(cc, "Click"+position, Toast.LENGTH_SHORT).show();
             //  ((NewUserActivity)cc).SetClick(position);
         
        }

        @Override
        public void onPageSelected(int position) {
            listener.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            listener.onPageScrollStateChanged(state);
        }
    }

    /**
     * PagerAdapterWrapper that has overridden {@link PagerAdapter#getCount()} method, in order
     * to reduce numbers of pages by one.
     */
    class PagerAdapterWrapper extends PagerAdapter {
        PagerAdapter delegate;

        public PagerAdapterWrapper(PagerAdapter pagerAdapter) {
            this.delegate = pagerAdapter;
        }

        @Override
        public int getCount() {
            return delegate.getCount();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return delegate.isViewFromObject(view, object);
        }

		
    }
}
