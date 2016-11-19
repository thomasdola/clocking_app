package com.eurekacachet.clocling.data.remote;


import com.eurekacachet.clocling.data.local.PreferencesHelper;
import com.eurekacachet.clocling.data.model.AuthResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ClockingService {

    String ENDPOINT = "http://localhost/api/";

    @FormUrlEncoded
    @POST("login")
    Observable<AuthResponse> login(@FieldMap Map<String, String> credentials);

    class Creator{

        public static ClockingService newClockingService(final PreferencesHelper preferencesHelper){

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();

            Interceptor mInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization: Bearer ", preferencesHelper.apiToken())
                            .build();
                    return chain.proceed(request);
                }
            };

            OkHttpClient client = new OkHttpClient();
            client.interceptors().add(mInterceptor);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ClockingService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            return retrofit.create(ClockingService.class);
        }
    }
}
