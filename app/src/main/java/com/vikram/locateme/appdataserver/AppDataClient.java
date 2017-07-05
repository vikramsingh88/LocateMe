package com.vikram.locateme.appdataserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public final class AppDataClient {

    private static final long CONNECTION_TIMEOUT = 30;
    private static final String STATUS_MESSAGE = "success";

    public interface OnDataReceived<T> {
        void onDataSuccess(T object);

        void onDataFailure(String error);
    }


    private final IAppDataAPIs mClient;

    private static class SingletonHolder {
        private static final AppDataClient INSTANCE = new AppDataClient();
    }

    private AppDataClient() {

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(IAppDataAPIs.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header(IAppDataAPIs.CONTENT_TYPE, IAppDataAPIs.JSON_TYPE);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        httpClient.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        httpClient.readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        httpClient.writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);

        Retrofit retrofit = builder.client(httpClient.build()).build();
        mClient = retrofit.create(IAppDataAPIs.class);
    }

    public static IAppDataAPIs getClient() {
        return SingletonHolder.INSTANCE.mClient;
    }


    public static AppDataClient getService() {
        return SingletonHolder.INSTANCE;
    }

    public <T extends RetroResponse> void onResponse(retrofit2.Response<T> response,
                                                     OnDataReceived callBack,
                                                     String cacheKey) {
        String errMessage = "Unknown error";
        T result = null;
        if (response.isSuccessful()) {
            if (response.body() != null) {
                result = response.body();
            } else {
                errMessage = "Parser error";
            }
        } else {
            try {
                errMessage = response.errorBody().string();
            } catch (IOException e) {
                e.printStackTrace();
                errMessage = e.getMessage();
            }
        }

        if (result == null) {
            callBack.onDataFailure(errMessage);
        } else {
            if (result.getStatusMessage().equals(STATUS_MESSAGE)) {
                callBack.onDataSuccess(result);
            } else {
                callBack.onDataFailure(result.getMessage());
            }
        }

    }
}
