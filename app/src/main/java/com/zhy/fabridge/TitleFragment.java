package com.zhy.fabridge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhy.fabridge.lib.Fabridge;

/**
 * Created by zhy on 16/4/12.
 */
public class TitleFragment extends Fragment
{
    public static final String BTN_ONE_CLICKED = "btn_one_clicked";
    public static final String BTN_TWO_CLICKED = "btn_two_clicked";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_title, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        Button btnOne = (Button) view.findViewById(R.id.id_btn_one);
        Button btnTwo = (Button) view.findViewById(R.id.id_btn_two);
        btnOne.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fabridge.call(getActivity(), BTN_ONE_CLICKED,v,"aaaa");
            }
        });
        btnTwo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fabridge.call(getActivity(), BTN_TWO_CLICKED);
            }
        });
    }
}
