package com.dark.pro.jimsradioadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class FragmentProgram extends Fragment implements View.OnClickListener {
    Button btnset_add_prog,btnset_edit_prog,btnset_remove_prog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_program, container, false);

        btnset_add_prog=(Button)v.findViewById(R.id.btn_set_add_program);
        btnset_add_prog.setOnClickListener(this);
        btnset_edit_prog=(Button)v.findViewById(R.id.btn_set_edit_program);
        btnset_edit_prog.setOnClickListener(this);
        btnset_remove_prog=(Button)v.findViewById(R.id.btn_set_remove_program);
        btnset_remove_prog.setOnClickListener(this);


        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_set_add_program:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_main, new FragmentAddProgram()).commit();
                break;
            case R.id.btn_set_edit_program:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_main, new FragmentEditProgram()).commit();
                break;
            case R.id.btn_set_remove_program:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_main, new FragmentRemoveProgram()).commit();
                break;
        }
    }
}
