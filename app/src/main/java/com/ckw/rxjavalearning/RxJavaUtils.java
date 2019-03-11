package com.ckw.rxjavalearning;

import android.annotation.SuppressLint;
import android.util.Log;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ckw
 * on 2018/7/12.
 */
public class RxJavaUtils {


    /*
    * 创建操作
    * */

    public static void createOperator(){
        /*
        * 数据传输(不包括线程的切换)：
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


        /*
        * 线程切换：
        *  observeOn -> new ObservableSubscribeOn中的SubscribeOnObserver
        *
        * */


        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("数据");
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Log.d("----", "onNext: "+s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    @SuppressLint("CheckResult")
    public static void fromOperator(){
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("数据："+i);
        }

        Observable.fromArray(list)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<ArrayList<String>>() {
                    @Override
                    public void accept(ArrayList<String> strings) throws Exception {
                        strings.add("haha");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<String>>() {
                    @Override
                    public void accept(ArrayList<String> strings) throws Exception {
                        strings.add("嘻嘻");
                        Log.d("----", "accept: "+strings);
                        //accept: [数据：0, 数据：1, 数据：2, 数据：3, 数据：4, haha, 嘻嘻]这里的结果就有"haha"
                    }
                });


    }

    public static void justOperator(){
        flatMapOperator();
    }

    public static void timerOperator(){
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //创建一个在给定的延时之后发射数据项为0的Observable<Long>
                        Log.d("----", "accept: 打印出的值："+aLong);
                        //accept: 打印出的值：0
                    }
                });
    }

    public static void intervalOperator(){
        //创建一个按照给定的时间间隔发射从0开始的整数序列的
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d("----", "accept: 接收到的值："+aLong);
                    }
                });
    }

    public static void rangeOperator(){
        //创建一个发射指定范围的整数序列的Observable<Integer>
        Observable.range(2,4)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d("----", "accept: 接收到的数据："+integer);

                    }
                });
    }

    public static void deferOperator(){
        //只有当订阅者订阅才创建Observable，为每个订阅创建一个新的Observable
        Observable.defer(new Callable<ObservableSource<String>>() {
            @Override
            public ObservableSource<String> call() throws Exception {
                return Observable.just("哈哈");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d("----", "accept: 接收到的数据："+s);
            }
        });
    }


    /*
    * 合并操作
    * */
    public static void concatOperator(){
        //按顺序连接多个Observables
        Observable<Integer> observable1 = Observable.just(1,2,3);
        Observable<Integer> observable2 = Observable.just(4,5,6);

        Observable.concat(observable1,observable2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d("----", "accept: 接收的数据："+integer);
                        //一次输出1,2,3,4,5,6
                    }
                });

    }

    public static void mergeOperator(){
        //将多个Observable合并为一个。不同于concat，merge不是按照添加顺序连接，而是按照时间线来连接。
        // 其中mergeDelayError将异常延迟到其它没有错误的Observable发送完毕后才发射。
        // 而merge则是一遇到异常将停止发射数据，发送onError通知
        Observable<Integer> just = Observable.just(1, 2, 3, 4);
        Observable<Integer> just1 = Observable.just(6, 7, 8);
        Observable.merge(just1,just)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d("----", "accept: 接收的数据："+integer);
                    }
                });


    }

    public static void zipOperator(){
        //使用一个函数组合多个Observable发射的数据集合，然后再发射这个结果。
        // 如果多个Observable发射的数据量不一样，则以最少的Observable为标准进行压合。
        // 内部通过ObservableZip进行压合
        Observable<Integer> just = Observable.just(1, 2, 3, 4);
        Observable<Integer> just1 = Observable.just(6, 7, 8);
        Observable.zip(just, just1, new BiFunction<Integer, Integer, String>() {
            @Override
            public String apply(Integer integer, Integer integer2) throws Exception {
                return integer + "：" + integer2;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d("----", "accept: 接收到的数据："+s);
            }
        });
    }

    public static void combineLatestOperator(){
        //当两个Observables中的任何一个发射了一个数据时，
        // 通过一个指定的函数组合每个Observable发射的最新数据（一共两个数据），然后发射这个函数的结果。
        // 类似于zip，但是，不同的是zip只有在每个Observable都发射了数据才工作，
        // 而combineLatest任何一个发射了数据都可以工作，每次与另一个Observable最近的数据压合
        Observable<Integer> just = Observable.just(1, 2, 3, 4);
        Observable<Integer> just1 = Observable.just(6, 7, 8);

        Observable.combineLatest(just, just1, new BiFunction<Integer, Integer, String>() {
            @Override
            public String apply(Integer integer, Integer integer2) throws Exception {
                return integer+";"+integer2;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d("----", "accept: 接收的数据："+s);
                //输出 4;6  4;7  4;8
            }
        });

    }


    /*
    * 过滤操作
    * */
    public static void filterOperator(){
        //过滤数据
        Observable.just(1,2,3,4,5)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer>=3;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("----", "accept: 接收的数据："+integer);
            }
        });
    }

    public static void ofTypeOperator(){
        //过滤指定类型的数据，与filter类似
        Observable.just(1,"haha",2,3)
                .ofType(Integer.class)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d("----", "accept: 接收的数据："+integer);
                        //接收到1,2,3
                    }
                });
    }

    public static void takeOperator(){
        Observable.just(1,2,3,4,5)
                .take(3)//指定前几项
                .take(100,TimeUnit.MILLISECONDS)//也可以指定某一段时间内发送的数据
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d("----", "accept: 接收到的数据："+integer);
                    }
                });

        //只发射最后的N项数据或者一定时间内的数据
        Observable.just(1,2,3,4,5)
                .takeLast(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d("----", "accept: 接收到的数据："+integer);
                    }
                });

    }

    public static void elementAtOperator(){
        Observable.just(1,2,3,4,5)
                .elementAt(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d("----", "accept: 接收的数据："+integer);
                        //输出3
                    }
                });
    }

    public static void distinctOperator(){
        //去除重复数据
        Observable.just(1,2,2,3,3)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d("----", "accept: 接收的数据："+integer);
                        //输出1,2,3
                    }
                });
    }


    /*
    * 条件/布尔操作
    * */
    public static void allOperator(){
        // 判断所有的数据项是否满足某个条件,返回false或者true
        //跟takeWhile操作符有点类似的判断
        Observable.just(1,2,3,4,5,6)
                .all(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 3;
                    }
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.d("----", "accept: " + aBoolean);
            }
        });
    }

    public static void containsOperator(){
        //断在发射的所有数据项中是否包含指定的数据
        Observable.just(1,2,3,4,5)
                .contains(3)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d("----", "accept: 接收的数据："+aBoolean);
                    }
                });
    }

    public static void takeWhileOperator(){
        //当所有的数据满足条件时，onNext才会接收到数据，只要有某一项数据不符合条件，就只会调用onComplete方法
        Observable.just(1,2,3,4,5)
                .takeWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer >= 1;
                    }
                }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.d("----", "onNext: "+integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d("----", "onComplete: ");
            }
        });
    }


    /*
    * 聚合操作
    * */
    public static void reduceOperator(){
        //对整个序列每个项做同样的规则的累加操作
        Observable.just(1,2,3,4,5)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer * integer2;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d("----", "accept: 接收的数据："+integer);
                //输出 120
            }
        });
    }

    public static void collectOperator(){
        // 使用collect收集数据到一个可变的数据结构。
        Observable.just(1,3,5,7)
                .collect(new Callable<List<Integer>>() {
                    @Override
                    public List<Integer> call() throws Exception {
                        return new ArrayList<>();
                    }
                }, new BiConsumer<List<Integer>, Integer>() {
                    @Override
                    public void accept(List<Integer> integers, Integer integer) throws Exception {
                        integers.add(integer);
                    }
                }).subscribe(new BiConsumer<List<Integer>, Throwable>() {
            @Override
            public void accept(List<Integer> integers, Throwable throwable) throws Exception {
                for (int i = 0; i < integers.size(); i++) {
                    Log.d("----", "accept: 接收的数据："+integers.get(i));
                }
            }
        });
    }

    /*
    * 转换操作
    * */
    public static void toListOperator(){
        //将数据转为list，与collect操作符类似
        Observable.just(1,2,3,4)
                .toList()
                .subscribe(new BiConsumer<List<Integer>, Throwable>() {
                    @Override
                    public void accept(List<Integer> integers, Throwable throwable) throws Exception {
                        Log.d("----", "accept: 接收的数据："+integers.size());
                    }
                });
    }

    public static void toSortedListOperator(){
        //默认是升序，收集原始Observable发射的所有数据到一个有序列表，然后返回这个列表
        Observable.just(2,4,7,1,3)
                .toSortedList(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer integer, Integer t1) {
//                        return integer - t1;//>0 升序 ，<0 降序
                        return t1 - integer;//>0 升序 ，<0 降序
                    }
                })
                .subscribe(new BiConsumer<List<Integer>, Throwable>() {
                    @Override
                    public void accept(List<Integer> integers, Throwable throwable) throws Exception {
                        for (int i = 0; i < integers.size(); i++) {
                            Log.d("----", "accept接收的数据: "+integers.get(i));
                        }
                    }
                });
    }

    public static void mapOperator(){
        //对Observable发射的每一项数据都应用一个函数来变换。
        Observable.just(1,2,3,4)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "haha"+integer;
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d("----", "accept: 接收到的数据："+s);
            }
        });
    }

    public static void flatMapOperator(){
        //将Observable发射的数据变换为Observables集合，然后将这些Observable发射的数据平坦化的放进一个单独的Observable
        Observable.just(1,2,3,4,5)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(final Integer integer) throws Exception {

                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                emitter.onNext("数据"+integer);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        //这里做数据的处理并不会对下面的操作有影响,fromArray中的例子就有影响
                        s = s + "哈哈";
                        Log.d("----", "accept: doonnext中的数据："+s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        //可能是由于数据源的不同导致的吧
                        Log.d("----", "accept: 最后接收到的数据："+s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    public static void bufferOperator(){
        //它定期从Observable收集数据到一个集合，然后把这些数据集合打包发射，而不是一次发射一个
        Observable.just(1,2,3,4,5)
                .buffer(3)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        for (int i = 0; i <integers.size(); i++) {
                            Log.d("----", "accept: 接收的数据："+integers.get(i));
                        }
                    }
                });
    }



}
