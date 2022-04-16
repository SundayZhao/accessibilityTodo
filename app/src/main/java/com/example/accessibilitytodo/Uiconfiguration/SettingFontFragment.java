package com.example.accessibilitytodo.Uiconfiguration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.accessibilitytodo.R;
import com.xw.repo.BubbleSeekBar;

public class SettingFontFragment extends Fragment {
    private BubbleSeekBar seekBar;
    private  TextView textview_FrontSize;
    private  TextView textview_FrontColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_setting_front, null);
        initView(thisView);
        return thisView;
    }

    protected void initView(View view) {
        seekBar = (BubbleSeekBar) view.findViewById(R.id.seekBar_frontsize);
        textview_FrontSize=(TextView) view.findViewById(R.id.textview_selectfront);
        textview_FrontColor =(TextView) view.findViewById(R.id.textview_selectfrontcolor);
    }


}
