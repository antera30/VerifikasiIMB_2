package com.example.fajar.verifikasiimb.rest;

/**
 * Created by Fajar on 4/7/2017.
 */

public class ApiUtils {

    private ApiUtils() {}

    public static ApiService getAPIService() {

        return ApiClient.getClient().create(ApiService.class);
    }
}
