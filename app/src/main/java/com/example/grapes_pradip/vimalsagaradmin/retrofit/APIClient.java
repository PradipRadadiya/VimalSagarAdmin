package com.example.grapes_pradip.vimalsagaradmin.retrofit;

import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static final String BASE_URL = CommonURL.Main_url;
    private static Retrofit retrofit = null;
    private static Map<String, String> queryParams = new HashMap<>();


    public static Retrofit getClient() {
//        queryParams.put("api key","djkfbhsjdbfsdfmkdmfksdmfk");
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
//                    .client(get_HTTPClient(queryParams))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    private static OkHttpClient get_HTTPClient(final Map<String, String> headers)
    {
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers

                Request.Builder requestBuilder = original.newBuilder(); // <-- this is the important line

                for (Map.Entry<String, String> pairs : headers.entrySet()) {
                    if (pairs.getValue() != null) {
                        requestBuilder.header(pairs.getKey(), pairs.getValue());
                    }
                }

                requestBuilder.method(original.method(), original.body());
                Request request = requestBuilder.build();

                return chain.proceed(request);

            }
        });

        return httpClient.build();

    }


}
