package com.example.fajar.verifikasiimb.rest;

import com.example.fajar.verifikasiimb.VerificationActivity;
import com.example.fajar.verifikasiimb.model.BangunanResponse;
import com.example.fajar.verifikasiimb.model.VerificationRespond;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Fajar on 4/3/2017.
 */

public interface ApiService {
    @GET("getBangunan.php")
    Call<BangunanResponse> Bangunanku();

//    @POST("postLaporan.php")
//    @FormUrlEncoded
//    Call<VerificationRespond> postVerifikasi(@Field("kondisi_bangunan") int kondisi_bangunan,
//                                             @Field("encoded_image") String encoded_image,
//                                             @Field("no_rumah") int no_rumah,
//                                             @Field("nama_jalan") String nama_jalan,
//                                             @Field("keterangan") String keterangan);
//    @FormUrlEncoded
    @Multipart
    @POST("postLaporan.php")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image,
                                 @Part("kondisi_bangunan")RequestBody  kondisi_bangunan,
                                 @Part("no_rumah")RequestBody  no_rumah,
                                 @Part("nama_jalan")RequestBody  nama_jalan,
                                 @Part("keterangan")RequestBody  keterangan);

}