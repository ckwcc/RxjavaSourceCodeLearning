package com.ckw.rxjavalearning;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ckw
 * on 2018/7/19.
 */
public class FlowableUtils {
    //BUFFER 同observable那种一样
    public static void errorOperator(){
        //在此策略下，如果放入Flowable的异步缓存池中的数据超限了，则会抛出MissingBackpressureException异常。
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i <= 129; i++) {
                    emitter.onNext(i);
                    Log.d("----", "subscribe: 发射的数据："+i);
                    Log.d("----", "subscribe: 要求的："+emitter.requested());
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("----", "onNext: 接收的数据："+integer);
                        //能接收到一个 0
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d("----", "onError: "+t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("----", "onComplete: 完成");
                    }
                });

    }

    public static void dropOperator(){
        //缓存池中数据的清理，并不是Subscriber接收一条，便清理一条，而是每累积到95条清理一次。
        // 也就是Subscriber接收到第96条数据时，缓存池才开始清理数据，之后Flowable发射的数据才得以放入
        //查看日志可以发现，Subscriber接收到第96条数据后，Flowable发射第288条数据。
        // 而第128到288之间的数据，正好处于缓存池存满的状态，而被丢弃，
        //所以Subscriber在接收完第128条数据之后，接收到的是第288条数据，而不是第129条
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 500; i++) {
                    emitter.onNext(i);
                    Thread.sleep(100);
                    Log.d("----", "subscribe: 发射数据："+i);
                }
                emitter.onComplete();
            }
        },BackpressureStrategy.DROP)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("----", "onNext: 接收到的数据："+integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d("----", "onError: 错误");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("----", "onComplete: 完成");
                    }
                });
    }

    public static void latestOperator(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 500; i++) {
                    emitter.onNext(i);
                    Thread.sleep(100);
                    Log.d("----", "subscribe: 发射数据："+i);
                }
                emitter.onComplete();
            }
        },BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("----", "onNext: 接收到的数据："+integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d("----", "onError: 错误");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("----", "onComplete: 完成");
                    }
                });

        /*
        * 接收到的数据：377
07-20 14:06:29.712 28497-28520/com.ckw.rxjavalearning D/----: onNext: 接收到的数据：378
07-20 14:06:30.012 28497-28520/com.ckw.rxjavalearning D/----: onNext: 接收到的数据：379
07-20 14:06:30.313 28497-28520/com.ckw.rxjavalearning D/----: onNext: 接收到的数据：380
07-20 14:06:30.613 28497-28520/com.ckw.rxjavalearning D/----: onNext: 接收到的数据：381
07-20 14:06:30.913 28497-28520/com.ckw.rxjavalearning D/----: onNext: 接收到的数据：382
07-20 14:06:31.213 28497-28520/com.ckw.rxjavalearning D/----: onNext: 接收到的数据：499
        * */
    }


}
