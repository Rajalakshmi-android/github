package com.iamretailer.krishnasupermarket.Common;

import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Helper {



    public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        Log.i("adapter_count",myListAdapter.getCount()+"");
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            Log.i("sub_height",totalHeight+"");
        }
        Log.i("height of listItem1:", String.valueOf(totalHeight));
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * ((myListAdapter.getCount() > 1) ? (myListAdapter.getCount() - 1) : myListAdapter.getCount()));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem2:", String.valueOf(totalHeight));
    }

}