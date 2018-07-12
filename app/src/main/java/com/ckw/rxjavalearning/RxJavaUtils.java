package com.ckw.rxjavalearning;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ckw
 * on 2018/7/12.
 */
public class RxJavaUtils {

    public static void createOperator(){
        /*
        * create方法需要有一个ObservableOnSubscribe对象作为source，在create方法中，通过new ObservableCreate<T>(source)
        * 创造一个observable对象（ObservableCreate对象继承自Observable）
        * 最后调用RxJavaPlugins.onAssembly(new ObservableCreate<T>(source));
        * onAssembly方法中，会返回两种，如果Function<? super Observable, ? extends Observable>不为空，
        * 返回的是? extends Observable类型的数据；如果为空，不做处理，返回Observable类型的source
        *
        * ObservableCreate中实现了Observable中的subscribeActual方法，它在Observable的subscribe方法中被调用
        * 在subscribeActual方法中，创造了发射器，（CreateEmitter类用于创造Emitter）比如这里的ObservableEmitter
        * observer.onSubscribe(parent);
        * source.subscribe(parent);
        * 两头分别订阅发射器
        * 具体的实现：
        * emitter.onNext("test");发送数据
        * 此时在ObservableEmitter中的onNext方法中，
        * 调用observer.onNext(t);
        * 核心思路就是：三个接口的调用和实现
        *Observable.create 传入ObservableOnSubscribe 接口的具体实现，
        * ObservableCreate中，该接口订阅发射器，使得在外部传入ObservableOnSubscribe的实现处获取到
        * 发射器对象，发送数据，
        * Observable.subscribe方法传入Observer接口的具体实现，同样也是在ObservableCreate中，这个Observer对象
        * 订阅发射器，在发射器的onNext具体实现方法中，调用Observer.onNext方法
        * 当外部的ObservableOnSubscribe的具体实现subscribe方法中，调用发射器对象发送数据时，
        * ObservableCreate中的发射器具体实现方法中拿到了数据，传给Observer对象，在外部Observer对象的具体实现方法
        * 中就可以获取到数据了
        * */


        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("test");
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });



    }
}
