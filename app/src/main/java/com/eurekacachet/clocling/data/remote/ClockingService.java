package com.eurekacachet.clocling.data.remote;


import com.eurekacachet.clocling.data.local.PreferencesHelper;
import com.eurekacachet.clocling.data.model.ActionResponse;
import com.eurekacachet.clocling.data.model.AuthResponse;
import com.eurekacachet.clocling.data.model.Beneficiary;
import com.eurekacachet.clocling.data.model.Clock;
import com.eurekacachet.clocling.data.model.Fingerprint;
import com.eurekacachet.clocling.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface ClockingService {

    @FormUrlEncoded
    @POST("login")
    Observable<AuthResponse> login(@FieldMap Map<String, String> credentials);

    @GET("fingerprints")
    Observable<List<Fingerprint>> getFingerprints();

    @FormUrlEncoded
    @POST("clocks")
    Observable<Beneficiary> clock(@Query("event") String event,
                                  @FieldMap Map<String, String> payload);

    @FormUrlEncoded
    @POST("clocks")
    Observable<ActionResponse> pushClocks(@FieldMap Map<String, List<Clock>> payload, @Query("type") String type);

    class Creator{

        public static ClockingService newClockingService(final PreferencesHelper preferencesHelper){

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();

            com.squareup.okhttp.Interceptor mInterceptor = new com.squareup.okhttp.Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization: Bearer ", preferencesHelper.apiToken())
                            .build();
                    return chain.proceed(request);
                }
            };

            com.squareup.okhttp.OkHttpClient client = new com.squareup.okhttp.OkHttpClient();
            client.interceptors().add(mInterceptor);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            return retrofit.create(ClockingService.class);
        }
    }
}
