package com.ckw.rxjavalearning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ckw.rxjavalearning.process.Bottom;
import com.ckw.rxjavalearning.process.Center;
import com.ckw.rxjavalearning.process.Top;
import com.ckw.rxjavalearning.process.TopOnSubscribe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        RxJavaUtils.createOperator();

        Top.create(new TopOnSubscribe() {
            @Override
            public void subscribe(Center center) {
                center.onNext("haha");
            }
        }).subscribe(new Bottom() {

            @Override
            public void onNext(String s) {
                Log.d("----", "onNext: 打印接收到的："+s);
            }
        });
    }

}
