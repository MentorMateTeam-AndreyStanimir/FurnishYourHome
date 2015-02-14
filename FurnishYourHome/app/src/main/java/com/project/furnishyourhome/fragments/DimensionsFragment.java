package com.project.furnishyourhome.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.furnishyourhome.AddRoomActivity;
import com.project.furnishyourhome.MainActivity;
import com.project.furnishyourhome.R;

/**
 * Created by Andrey on 11.2.2015 г..
 */
public class DimensionsFragment extends Fragment {

    String[] rooms = {"living room", "bedroom", "kitchen"};


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dimensions,container,false);

        ImageView ivRoom = (ImageView) rootView.findViewById(R.id.iv_room);

        Spinner spinner = (Spinner) rootView.findViewById(R.id.sp_rooms);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spiner_item, rooms);
        spinnerAdapter.setDropDownViewResource(R.layout.spiner_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //shared preferences
                //ivRoom
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               //default
            }
        });

        EditText etWidth = (EditText) rootView.findViewById(R.id.et_width);
        etWidth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //shared preferences
                double w;
                if(s.toString().isEmpty()) {
                    w = 0;
                } else {
                    w = Double.parseDouble(s.toString());
                }
                Log.d("WIDTH: ", w+"");
                //Toast.makeText(getActivity().getApplicationContext(), w+"", Toast.LENGTH_LONG).show();
            }
        });
        EditText etHeight = (EditText) rootView.findViewById(R.id.et_height);
        EditText etDepth = (EditText) rootView.findViewById(R.id.et_depth);

        Button btn = (Button) rootView.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddRoomActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}