package com.zhy.fabridge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;
import com.zhy.fabridge.lib.Fabridge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhy on 16/4/12.
 */
public class ListFragment extends Fragment
{
    private ListView mListView;
    private CommonAdapter<String> mAdapter;
    private List<String> mDatas = new ArrayList<String>(Arrays.asList("a easy way for communication between activity and fragment".split(" ")));

    public static final String EVENT_LIST_ITEM_CLICK = "event_list_item_click";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState)
    {
        mListView = (ListView) view.findViewById(R.id.id_listview);
        mListView.setAdapter(mAdapter = new CommonAdapter<String>(getActivity(), R.layout.item_string, mDatas)
        {
            @Override
            public void convert(ViewHolder viewHolder, String s)
            {
                viewHolder.setText(R.id.id_title_tv, s);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //针对某个事件的参数，预先要设计好（类似于接口回调方法的参数），因为Fragment是复用的
                Fabridge.call(getActivity(), EVENT_LIST_ITEM_CLICK, view, position, mAdapter.getItem(position));
            }
        });
    }


    public void addTitle(String title)
    {
        mDatas.add(title);
        mAdapter.notifyDataSetChanged();
    }

}
