package com.project.furnishyourhome;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.getbase.floatingactionbutton.FloatingActionButton;


public class AddRoomActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        ImageView ivRoom = (ImageView) findViewById(R.id.iv_room);

        Spinner spinner = (Spinner) findViewById(R.id.sp_rooms);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spiner_item, getResources().getStringArray(R.array.rooms));
        spinnerAdapter.setDropDownViewResource(R.layout.spiner_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //shared preferences
                //change ivRoom
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //default
            }
        });

        EditText etWidth = (EditText) findViewById(R.id.et_width);
       /* etWidth.addTextChangedListener(new TextWatcher() {
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
                Log.d("WIDTH: ", w + "");
                //Toast.makeText(getActivity().getApplicationContext(), w+"", Toast.LENGTH_LONG).show();
            }
        });*/
        EditText etHeight = (EditText) findViewById(R.id.et_height);
        EditText etDepth = (EditText) findViewById(R.id.et_depth);


        FloatingActionButton mFab = (FloatingActionButton)findViewById(R.id.fab);
        mFab.setColorNormal(getResources().getColor(R.color.ColorPrimary));
        mFab.setColorPressed(getResources().getColor(R.color.ColorPrimaryDark));
        mFab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_add));
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle entries
            }
        });
    }
}
