package com.ckw.rxjavalearning.process;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by ckw
 * on 2018/7/12.
 */
public interface Bottom {
    void onNext(String s);
}
