package com.idiots.gorail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapDropDown;
import com.beardedhen.androidbootstrap.BootstrapEditText;

public class ControlActivity extends AppCompatActivity {

    BootstrapEditText distanceEditText;
    BootstrapButton distanceBtn, stepSizeBtn, startBtn, stopBtn;
    BootstrapDropDown stepSizeDropDown;
    Spinner stepSizeSpinner;

    private int delta = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        distanceEditText = findViewById(R.id.control_distance_edit_text);
        distanceBtn = findViewById(R.id.control_distance_set_btn);
        stepSizeBtn = findViewById(R.id.control_step_size_set_btn);
        //stepSizeDropDown = findViewById(R.id.control_step_size_drop_down);
        stepSizeSpinner = findViewById(R.id.control_step_size_spinner);
        startBtn = findViewById(R.id.control_start_btn);
        stopBtn = findViewById(R.id.control_stop_btn);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.step_list,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stepSizeSpinner.setAdapter(adapter);

        stepSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                delta = position + 1;
                msg("Set to " + delta);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                delta = 0;
                msg("Set to " + delta);
            }
        });

        distanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = distanceEditText.getText().toString();
                if(!"".equals(temp)) {
                    MainActivity.bluetooth.sendMessage("@#di" + distanceEditText.getText().toString() + "b&*");
                    msg("Set to " + temp);
                }
            }
        });

        stepSizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delta != 0){
                    MainActivity.bluetooth.sendMessage("@#de"+delta+"b&*");
                    msg("Set to "+delta);
                }
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.bluetooth.sendMessage("@#sb&*");
                msg("Start");
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.bluetooth.sendMessage("@#pb&*");
                msg("Stop");
            }
        });

        /*
        stepSizeDropDown.setOnDropDownItemClickListener(new BootstrapDropDown.OnDropDownItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View v, int id) {
                msg(""+id);
            }
        });
        */
    }

    private void msg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
