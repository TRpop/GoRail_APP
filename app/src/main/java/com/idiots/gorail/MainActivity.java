package com.idiots.gorail;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BluetoothClient";

    private final int REQUEST_BLUETOOTH_ENABLE = 100;

    public static Bluetooth bluetooth;
    public static Fifo<String> q;

    private Button manualBtn, graphBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manualBtn = findViewById(R.id.main_manual_btn);
        graphBtn = findViewById(R.id.main_graph_btn);

        MainActivity.q = new Fifo<String>();
        bluetooth = null;

        manualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetooth != null && bluetooth.isConnected()) {
                    Intent intent = new Intent(getApplicationContext(), ControlActivity.class);
                    startActivity(intent);
                }
            }
        });

        graphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetooth != null && bluetooth.isConnected()){
                    Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bluetooth_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.pair_menu:
                //TODO 블루투스 페어링을 위한 코드 작성
                if(bluetooth != null) bluetooth.cancel();
                bluetooth = new Bluetooth(this, null) {
                    @Override
                    protected void onReceive(String s) {
                        if(s.contains("@#")){
                            MainActivity.q.clear();
                            MainActivity.q.append(s);
                            Intent intent = new Intent("BT_RECEIVE_CHANGED");
                            intent.putExtra("RECEIVING", true);
                            MainActivity.this.sendBroadcast(intent);

                            if(s.contains("&*")) {
                                Intent intent1 = new Intent("BT_RECEIVE_CHANGED");
                                intent1.putExtra("RECEIVING", false);
                                MainActivity.this.sendBroadcast(intent1);
                            }

                        }
                        else if(s.contains("&*")) {
                            MainActivity.q.append(s);
                            Intent intent = new Intent("BT_RECEIVE_CHANGED");
                            intent.putExtra("RECEIVING", false);
                            MainActivity.this.sendBroadcast(intent);
                        }
                        else{
                            MainActivity.q.append(s);
                        }
                    }

                    @Override
                    protected void onConnected(String deviceName) {
                        MainActivity.this.msg("connected to " + deviceName);
                    }
                };

                if (!bluetooth.isEnabled()) {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_BLUETOOTH_ENABLE);
                }
                else {
                    Log.d(TAG, "Initialisation successful.");

                    bluetooth.showPairedDevicesListDialog();
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_BLUETOOTH_ENABLE){
            if (resultCode == RESULT_OK){
                //BlueTooth is now Enabled
                bluetooth.showPairedDevicesListDialog();
            }
            if(resultCode == RESULT_CANCELED){
                bluetooth.showQuitDialog( "You need to enable bluetooth");
            }
        }
    }

    private void msg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
