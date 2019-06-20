package com.maxwell.mahalirmattum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class ProgrammsFragment extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_content_programms, container, false);

        GridView gridview = (GridView) view.findViewById(R.id.grid_programms);
        gridview.setAdapter(new ImageAdapter(getActivity()));

        return view;
    }
}
