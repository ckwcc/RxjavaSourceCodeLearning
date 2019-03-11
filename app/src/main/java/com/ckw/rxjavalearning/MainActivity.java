package com.ckw.rxjavalearning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ckw.rxjavalearning.process.Bottom;
import com.ckw.rxjavalearning.process.Center;
import com.ckw.rxjavalearning.process.Top;
import com.ckw.rxjavalearning.process.TopOnSubscribe;
import com.ckw.rxjavalearning.proxy.MyApi;
import com.ckw.rxjavalearning.proxy.MyRetrofit;

import java.io.IOException;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MyApi myApi = MyRetrofit.create(MyApi.class);
//        String s = myApi.getValue("123");
//        Log.d("----", "onCreate: 打印出："+s);
//        RetrofitHelper.TestApi api = RetrofitHelper.instance().create(RetrofitHelper.TestApi.class);
//        Observable<List<RetrofitHelper.Repo>> ckwcc = api.listRepos("ckwcc");
//        ckwcc
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<RetrofitHelper.Repo>>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.d("----", "onSubscribe: ");
//            }
//
//            @Override
//            public void onNext(List<RetrofitHelper.Repo> repos) {
//                Log.d("----", "onNext: ");
//                for (int i = 0; i < repos.size(); i++) {
//                    Log.d("----", "onNext: "+repos.get(i).name);
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d("----", "onError: "+e.toString());
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d("----", "onComplete: ");
//            }
//        });
        
        //创建操作
//        RxJavaUtils.createOperator();
//        RxJavaUtils.fromOperator();
//        RxJavaUtils.justOperator();
//        RxJavaUtils.timerOperator();
//        RxJavaUtils.intervalOperator();
//        RxJavaUtils.rangeOperator();
//        RxJavaUtils.deferOperator();

        //合并操作
//        RxJavaUtils.concatOperator();
//        RxJavaUtils.mergeOperator();
//        RxJavaUtils.zipOperator();
//        RxJavaUtils.combineLatestOperator();

        //过滤操作
        RxJavaUtils.filterOperator();
//        RxJavaUtils.ofTypeOperator();
//        RxJavaUtils.takeOperator();

//        RxJavaUtils.elementAtOperator();
//        RxJavaUtils.distinctOperator();

        //条件/布尔操作
//        RxJavaUtils.allOperator();
//        RxJavaUtils.containsOperator();
//        RxJavaUtils.takeWhileOperator();

        //聚合操作
//        RxJavaUtils.reduceOperator();
//        RxJavaUtils.collectOperator();

        //转换操作
//        RxJavaUtils.toListOperator();
//        RxJavaUtils.toSortedListOperator();
//        RxJavaUtils.mapOperator();
//        RxJavaUtils.flatMapOperator();
//        RxJavaUtils.bufferOperator();

        //Flowable
//        FlowableUtils.errorOperator();
//        FlowableUtils.dropOperator();
//        FlowableUtils.latestOperator();
//        Top.create(new TopOnSubscribe() {
//            @Override
//            public void subscribe(Center center) {
//
//                Log.d("----", "subscribe: 发送端线程:"+Thread.currentThread().getName());
//                center.onNext("haha");
//            }
//        })
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Bottom() {
//
//            @Override
//            public void onNext(String s) {
//                Log.d("----", "onNext: 打印接收到的："+s+";接收到的线程:"+Thread.currentThread().getName());
//            }
//        });
    }

}
