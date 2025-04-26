package com.ngdat.mymusic.Service;

public class APIService {
    // truyền url để tướng tác phía server
    private static final String url_base = "https://your-api-url.com/";

    // tướng tác tích hợp 2 thằng lại
    public static DataService getService() {
        return ConfigRetrofitClient.getClient(url_base).create(DataService.class);
    }
}
