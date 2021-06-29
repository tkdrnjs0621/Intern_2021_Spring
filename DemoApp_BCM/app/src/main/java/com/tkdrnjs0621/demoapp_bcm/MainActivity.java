package com.tkdrnjs0621.demoapp_bcm;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ClickManagerCallback{

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
        Log.i("ee","eeee");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        BluetoothClickManager bcm = new BluetoothClickManager(this, BluetoothAdapter.getDefaultAdapter(), Looper.getMainLooper(),size.x,size.y);
        bcm.addView(R.id.btn0);
        bcm.addView(R.id.btn1);
        bcm.addView(R.id.btn2);
        bcm.addView(R.id.btn3);
        bcm.addView(R.id.btn4);
        bcm.addView(R.id.btn5);
        bcm.addView(R.id.btn6);
        bcm.addView(R.id.btn7);
        bcm.addView(R.id.btn8);
        bcm.addView(R.id.btn9);
        bcm.addView(R.id.btn10);
        bcm.addView(R.id.btn11);
        bcm.addView(R.id.btn12);
        bcm.addView(R.id.btn13);
        bcm.addView(R.id.btn14);


        tv = (TextView) findViewById(R.id.textView);

        View.OnClickListener cl = new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                try{
                    Button btn = (Button) view;
                    tv.setText("Clicked Button : "+btn.getText().toString());
                }
                catch (Exception e)
                {

                }
            }
        };


        ((Button) findViewById(R.id.btn0)).setOnClickListener(cl);
        ((Button) findViewById(R.id.btn1)).setOnClickListener(cl);
        ((Button) findViewById(R.id.btn2)).setOnClickListener(cl);
        ((Button) findViewById(R.id.btn3)).setOnClickListener(cl);
        ((Button) findViewById(R.id.btn4)).setOnClickListener(cl);
        ((Button) findViewById(R.id.btn5)).setOnClickListener(cl);
        ((Button) findViewById(R.id.btn6)).setOnClickListener(cl);
        ((Button) findViewById(R.id.btn7)).setOnClickListener(cl);
        ((Button) findViewById(R.id.btn8)).setOnClickListener(cl);
        ((Button) findViewById(R.id.btn9)).setOnClickListener(cl);
        ((Button) findViewById(R.id.btn10)).setOnClickListener(cl);
        ((Button) findViewById(R.id.btn11)).setOnClickListener(cl);
        ((Button) findViewById(R.id.btn12)).setOnClickListener(cl);
        ((Button) findViewById(R.id.btn13)).setOnClickListener(cl);
        ((Button) findViewById(R.id.btn14)).setOnClickListener(cl);
    }

    @Override
    public void performAction(int action, int id) {
        switch (action)
        {
            case 0: //Hover
            {

                break;
            }
            case 1: //Click
            {
                View v = findViewById(id);
                Log.i("MainActivity","clicked : "+id);
                v.performClick();
                break;
            }
            default:
            {

            }//should not fall here

        }
    }

    public void performAction(int action, int x, int y)
    {
        ImageView iv = (ImageView) findViewById(R.id.imageView);
        switch (action)
        {
            case 0: //Hover
            case 1: //Click
            {
                iv.setX(x);
                iv.setY(y);
                break;
            }
            default:
            {

            }//should not fall here

        }
    }
    @Override
    public ViewDataArgs getViewData(int id) {
        View v = findViewById(id);
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        int x_tl=location[0];
        int y_tl=location[1];
        int w = v.getWidth();
        int h = v.getHeight();
        return new ViewDataArgs(x_tl,y_tl,w,h);
    }
}