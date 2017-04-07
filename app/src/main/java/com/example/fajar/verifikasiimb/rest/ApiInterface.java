package com.example.fajar.verifikasiimb.rest;

import com.example.fajar.verifikasiimb.model.BangunanResponse;
import com.example.fajar.verifikasiimb.model.VerificationRespond;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Fajar on 4/3/2017.
 */

public interface ApiInterface {
    @GET("getBangunan.php")
    Call<BangunanResponse> Bangunanku();

    @POST("postLaporan.php")
    @FormUrlEncoded
    Call<VerificationRespond> postVerifikasi(@Field("kondisi_bangunan") int kondisi_bangunan,
                                             @Field("encoded_image") String encoded_image,
                                             @Field("no_rumah") int no_rumah,
                                             @Field("nama_jalan") String nama_jalan,
                                             @Field("keterangan") String keterangan);
}