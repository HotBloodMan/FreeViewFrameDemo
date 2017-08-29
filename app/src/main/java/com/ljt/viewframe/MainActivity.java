package com.ljt.viewframe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ljt.viewframe.view.DiscrollView;

public class MainActivity extends AppCompatActivity {

    protected DiscrollView mDiscrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDiscrollView= (DiscrollView) findViewById(R.id.layout);
    }
}
