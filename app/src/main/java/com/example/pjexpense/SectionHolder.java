package com.example.pjexpense;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SectionHolder extends RecyclerView.ViewHolder {

    public TextView textView;

    public SectionHolder(View v) {
        super(v);
        textView = (TextView)v.findViewById(R.id.textView_section);
    }
}

