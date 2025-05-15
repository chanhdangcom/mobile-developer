package com.ngdat.mymusic.Service;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MockInterceptor implements Interceptor {
    private final Context context;
    private final boolean mockMode;

    public MockInterceptor(Context context, boolean mockMode) {
        this.context = context;
        this.mockMode = mockMode;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (mockMode) {
            try {
                String encodedPath = request.url().encodedPath();
                String mockFilePath = "mock" + encodedPath + ".json";

                String json = loadJSONFromAsset(mockFilePath);
                return new Response.Builder()
                        .request(request)
                        .protocol(Protocol.HTTP_1_1)
                        .code(200) // HTTP status code
                        .message("Mock response")
                        .body(ResponseBody.create(MediaType.parse("application/json"), json))
                        .build();
            } catch (IOException e) {
                return chain.proceed(request);
            }
        }
        return chain.proceed(request);
    }
    private String loadJSONFromAsset(String fileName) throws IOException {
        InputStream is = context.getAssets().open(fileName);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        return new String(buffer, "UTF-8");
    }
}