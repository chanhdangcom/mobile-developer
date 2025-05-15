package com.ngdat.mymusic.Service;

public class APIService {
    private static final String url_base = "https://your-api-url.com/";

    public static DataService getService() {
        return ConfigRetrofitClient.getClient(url_base).create(DataService.class);
    }
}
