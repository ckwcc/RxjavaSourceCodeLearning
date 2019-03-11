package com.ckw.rxjavalearning.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by ckw
 * on 2018/7/25.
 */
public class MyRetrofit {

    @SuppressWarnings("unchecked")
    public static <T> T create(final Class<T> service){
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                boolean post = method.isAnnotationPresent(POST.class);
                if(post){
                    POST annotation = method.getAnnotation(POST.class);
                    return annotation.value();
                }

                boolean get = method.isAnnotationPresent(GET.class);
                if(get){
                    GET annotation = method.getAnnotation(GET.class);
                    return annotation.value();
                }

                return "ckwcc";
            }
        });
    }
}
