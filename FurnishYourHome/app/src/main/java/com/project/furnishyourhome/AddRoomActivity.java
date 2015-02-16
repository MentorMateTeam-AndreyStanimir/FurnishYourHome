package com.project.furnishyourhome;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;


public class AddRoomActivity extends ActionBarActivity {

    private static final String TAG = AddRoomActivity.class.getSimpleName();

    private EditText etWidth, etHeight, etDepth;
    private ImageView ivRoom;
    private String room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        ivRoom = (ImageView) findViewById(R.id.iv_room);

        Spinner spinner = (Spinner) findViewById(R.id.sp_rooms);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spiner_item, getResources().getStringArray(R.array.spinerItems));
        spinnerAdapter.setDropDownViewResource(R.layout.spiner_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                room = parent.getItemAtPosition(position).toString();
                Log.d(TAG, room);
                //change ivRoom
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               //room = "";
            }
        });

        etWidth = (EditText) findViewById(R.id.et_width);
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
        etHeight = (EditText) findViewById(R.id.et_height);
        etDepth = (EditText) findViewById(R.id.et_depth);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setColorNormal(getResources().getColor(R.color.ColorPrimary));
        fab.setColorPressed(getResources().getColor(R.color.ColorPrimaryDark));
        fab.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        fab.setIconDrawable(getResources().getDrawable(android.R.drawable.ic_menu_add));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClick();
            }
        });
    }

    private void handleClick() {

        float w, h, d;
        String key = room+"_";

        if(room.contains("-")) {
            Toast.makeText(this, "Please choose room", Toast.LENGTH_LONG).show();
            return;
        }

        if(etWidth.getText().toString().isEmpty() && etHeight.getText().toString().isEmpty() && etDepth.getText().toString().isEmpty() ) {
            etWidth.setError("Please enter width");
            etHeight.setError("Please enter height");
            etDepth.setError("Please enter depth");
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if(etWidth.getText().toString().isEmpty()) {
                etWidth.setError("Please enter width");
                return;
            } else {
                w = Float.parseFloat(etWidth.getText().toString());
            }

            if(etHeight.getText().toString().isEmpty()) {
                etHeight.setError("Please enter height");
                return;
            } else {
                h = Float.parseFloat(etHeight.getText().toString());
            }

            if(etDepth.getText().toString().isEmpty()) {
                etDepth.setError("Please enter depth");
                return;
            } else {
                d = Float.parseFloat(etDepth.getText().toString());
            }
        }

        //SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(key+"width", w);
        Log.d(TAG, key+"width "+w);
        editor.putFloat(key+"height", h);
        Log.d(TAG, key+"height "+h);
        editor.putFloat(key+"depth", d);
        Log.d(TAG, key+"depth "+d);

        editor.apply();

        String info = "You have created new "+room;
        Toast.makeText(this, info, Toast.LENGTH_LONG).show();

        finish();
    }
}
