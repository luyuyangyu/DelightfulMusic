package com.sdbi.delightfulmusic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //欢迎界面两秒后杀死
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(4000);
                    Welcome.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
