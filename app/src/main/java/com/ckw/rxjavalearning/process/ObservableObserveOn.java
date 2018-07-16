package com.ckw.rxjavalearning.process;

import io.reactivex.Scheduler;

/**
 * Created by ckw
 * on 2018/7/16.
 */
public class ObservableObserveOn extends Top{

    final Scheduler scheduler;
    final TopSource source;

    public ObservableObserveOn(Scheduler scheduler,TopSource source) {
        this.scheduler = scheduler;
        this.source = source;
    }

    @Override
    protected void subscribeActual(Bottom bottom) {
        Scheduler.Worker w = scheduler.createWorker();
        source.subscribe(new ObserveOnObserver(bottom,w));
    }

    static final class ObserveOnObserver implements Bottom,Runnable{

        final Bottom bottom;
        final Scheduler.Worker worker;

        public ObserveOnObserver(Bottom bottom, Scheduler.Worker worker) {
            this.bottom = bottom;
            this.worker = worker;
        }

        String data;

        @Override
        public void run() {
            bottom.onNext(data);
        }

        @Override
        public void onNext(String s) {
            data = s;
            worker.schedule(this);
        }
    }
}
