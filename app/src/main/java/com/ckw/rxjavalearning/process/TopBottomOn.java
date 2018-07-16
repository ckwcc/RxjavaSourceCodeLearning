package com.ckw.rxjavalearning.process;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

/**
 * Created by ckw
 * on 2018/7/16.
 */
public final class TopBottomOn extends Top{

    final Scheduler scheduler;
    TopSource source;
    public TopBottomOn(Scheduler scheduler, TopSource source) {
        this.scheduler = scheduler;
        this.source = source;
    }

    @Override
    protected void subscribeActual(Bottom bottom) {
        final SubscribeOnObserver parent = new SubscribeOnObserver(bottom);
        parent.setNewTask(scheduler.scheduleDirect(new SubscribeTask(parent)));
    }

    static final class SubscribeOnObserver extends AtomicReference<Disposable> implements Bottom,Center{

        final Bottom bottom;
        final AtomicReference<Disposable> s;

        SubscribeOnObserver(Bottom bottom){
            this.bottom = bottom;
            this.s = new AtomicReference<Disposable>();
        }


        @Override
        public void onNext(String s) {
            bottom.onNext(s);
        }

        void setNewTask(Disposable disposable){

        }
    }

    final class SubscribeTask implements Runnable{

        private final SubscribeOnObserver parent;

        public SubscribeTask(SubscribeOnObserver parent) {
            this.parent = parent;
        }

        @Override
        public void run() {
            source.subscribe(parent);
        }
    }
}
