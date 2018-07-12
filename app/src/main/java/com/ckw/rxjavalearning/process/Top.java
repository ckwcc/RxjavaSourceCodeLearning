package com.ckw.rxjavalearning.process;


import io.reactivex.Observer;

/**
 * Created by ckw
 * on 2018/7/12.
 * 对应Observable
 */
public abstract class Top implements TopSource{

    public static Top create(TopOnSubscribe topOnSubscribe){
        return new TopCreate(topOnSubscribe);
    }

    @Override
    public void subscribe(Bottom bottom) {
        subscribeActual(bottom);
    }

    protected abstract void subscribeActual(Bottom bottom);
}
