package com.ckw.rxjavalearning.process;


import io.reactivex.Scheduler;

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

    public final Top subscribeOn(Scheduler scheduler){
        return new TopBottomOn(scheduler,this);
    }

    public final Top observeOn(Scheduler scheduler){
        return new ObservableObserveOn(scheduler,this);
    }
}
