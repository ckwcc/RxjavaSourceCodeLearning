package com.ckw.rxjavalearning;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ckw
 * on 2018/7/25.
 */
public class RetrofitHelper {
    public static Retrofit instance(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    interface TestApi{
        @GET("group/{id}/users")
        Observable<List<User>> groupList(@Path("id") int groupId);

        @GET("users/{user}/repos")
        Observable<List<Repo>> listRepos(@Path("user") String user);
    }

    class User{
        String name;
    }

    class Repo{
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
