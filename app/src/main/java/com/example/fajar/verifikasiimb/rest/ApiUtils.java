package com.example.fajar.verifikasiimb.rest;

/**
 * Created by Fajar on 4/7/2017.
 */

public class ApiUtils {

    private ApiUtils() {}

    public static ApiInterface getAPIService() {

        return ApiClient.getClient().create(ApiInterface.class);
    }
}
