package com.ckw.rxjavalearning.process;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;

/**
 * Created by ckw
 * on 2018/7/12.
 */
public interface TopSource {
    void subscribe(Bottom bottom);
}
