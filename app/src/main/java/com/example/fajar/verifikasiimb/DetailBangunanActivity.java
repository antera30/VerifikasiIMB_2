package com.example.fajar.verifikasiimb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fajar.verifikasiimb.config.AppConfig;
import com.example.fajar.verifikasiimb.model.Bangunan;
import com.example.fajar.verifikasiimb.rest.ApiService;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailBangunanActivity extends AppCompatActivity {
    private Toolbar toolbar;
    Intent intent;
    int position, fid;
    Button btn_tdk_sesuai, btn_sesuai;
    ImageButton btn_single_maps_view;
    private List<Bangunan> mValues;
    String TAG = DetailBangunanActivity.class.getSimpleName();
    ApiService service;

    TextView tv_fid, tv_nib, tv_no_sk, tv_no_persil, tv_nama_site, tv_pemilik, tv_wilayah,
            tv_alamat, tv_ket_imb, tv_landuse, tv_nama_jalan, tv_gang, tv_nomor, tv_kelurahan, tv_kecamatan;

    int id_petugas, id_bangunan, id_ket_bangunan, imb;

    ImageView gambar_bangunan;
    private static final int MAX_WIDTH = 400;
    private static final int MAX_HEIGHT = 300;
    private Bitmap theBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bangunan);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Data Berdasarkan IMB");

        intent = getIntent();
        Bundle extras = intent.getExtras();
        position = extras.getInt("position");
        id_bangunan = extras.getInt("id");
        final int fid = extras.getInt("fid");
        int sk = extras.getInt("sk");
        int nib = extras.getInt("nib");
        imb = extras.getInt("imb");
        String ket_imb = extras.getString("ket_imb");
        int persil = extras.getInt("persil");
        String pemilik = extras.getString("pemilik");
        String wilayah = extras.getString("wilayah");
        String landuse = extras.getString("landuse");
        String kelurahan = extras.getString("kelurahan");
        String kecamatan = extras.getString("kecamatan");
        String namasite = extras.getString("namasite");
        String namajalan = extras.getString("namajalan");
        String gang = extras.getString("gang");
        String nomor = extras.getString("nomor");
        final double latitude = extras.getDouble("latitude");
        final double longitude = extras.getDouble("longitude");
        final String encoded_image = extras.getString("encoded_image");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        service = new Retrofit.Builder().baseUrl(AppConfig.BASE_API_URL).addConverterFactory(GsonConverterFactory.create())
                .client(client).build()
                .create(ApiService.class);


        Toast.makeText(this, "" + fid, Toast.LENGTH_LONG).show();

        btn_tdk_sesuai = (Button) findViewById(R.id.btn_bangunan_tdk_sesuai);
        btn_tdk_sesuai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailBangunanActivity.this, VerificationActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("task_id", 200);
                extras.putInt("bangunan_id", fid);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });


        btn_sesuai = (Button) findViewById(R.id.btn_bangunan_sesuai);
        btn_sesuai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_petugas = 1;
//                Toast.makeText(getApplicationContext(), ""+String.valueOf(id_petugas)+", "+String.valueOf(id_bangunan)+", "+String.valueOf(imb), Toast.LENGTH_SHORT).show();
                uploaddata(String.valueOf(id_petugas), String.valueOf(id_bangunan), String.valueOf(imb));
            }
        });

        //setup textView on detail bangunan activity
        tv_fid = (TextView) findViewById(R.id.bangunan_fid);
        tv_fid.setText(String.valueOf(fid));
        tv_nib = (TextView) findViewById(R.id.bangunan_nib);
        tv_nib.setText(String.valueOf(nib));
        tv_no_sk = (TextView) findViewById(R.id.bangunan_no_sk);
        tv_no_sk.setText(String.valueOf(sk));
        tv_no_persil = (TextView) findViewById(R.id.bangunan_no_persil);
        tv_no_persil.setText(String.valueOf(persil));
        tv_nama_site = (TextView) findViewById(R.id.bangunan_nama_site);
        tv_nama_site.setText(namasite);
        tv_pemilik = (TextView) findViewById(R.id.bangunan_pemilik);
        tv_pemilik.setText(pemilik);
        tv_wilayah = (TextView) findViewById(R.id.bangunan_wilayah);
        tv_wilayah.setText(wilayah);
//        tv_alamat = (TextView) findViewById(R.id.bangunan_alamat);
//        tv_alamat.setText(String.valueOf(alamat));
        tv_ket_imb = (TextView) findViewById(R.id.bangunan_ket_imb);
        tv_ket_imb.setText(ket_imb);
        tv_landuse = (TextView) findViewById(R.id.bangunan_landuse);
        tv_landuse.setText(landuse);
        tv_nama_jalan = (TextView) findViewById(R.id.bangunan_nama_jalan);
        tv_nama_jalan.setText(namajalan);
        tv_gang = (TextView) findViewById(R.id.bangunan_gang);
        tv_gang.setText(gang);
        tv_nomor = (TextView) findViewById(R.id.bangunan_nomor);
        tv_nomor.setText(nomor);
        tv_kelurahan = (TextView) findViewById(R.id.bangunan_kelurahan);
        tv_kelurahan.setText(kelurahan);
        tv_kecamatan = (TextView) findViewById(R.id.bangunan_kecamatan);
        tv_kecamatan.setText(kecamatan);
        gambar_bangunan = (ImageView) findViewById(R.id.imagerumah);

        if (encoded_image != null) {
            String encodedImage = "" + AppConfig.BASE_URL + encoded_image;
            Picasso.with(getApplicationContext())
                    .load(encodedImage)
                    .resize(MAX_WIDTH, MAX_HEIGHT)
                    .centerCrop()
                    .into(gambar_bangunan);
        }
        gambar_bangunan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //button that inflate to single maps view activity
        btn_single_maps_view = (ImageButton) findViewById(R.id.btn_single_maps_view);
        btn_single_maps_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SingleBangunanMapsView.class);
                Bundle extras = new Bundle();
                extras.putDouble("latitude", latitude);
                extras.putDouble("longitude", longitude);
                i.putExtras(extras);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(DetailBangunanActivity.this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
//                Intent intent = new Intent(DetailBangunanActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void uploaddata(String _id_petugas, String _id_bangunan, String _id_ket_imb) {
        RequestBody id_petugas = RequestBody.create(MediaType.parse("text/plain"), _id_petugas);
        RequestBody id_bangunan = RequestBody.create(MediaType.parse("text/plain"), _id_bangunan);
        RequestBody id_ket_bangunan = RequestBody.create(MediaType.parse("text/plain"), _id_ket_imb);
        Call<ResponseBody> call = service.postlaporansesuai(id_ket_bangunan, id_bangunan, id_petugas);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.response_success_upload, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DetailBangunanActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    DetailBangunanActivity.this.finish();
                } else {
                    response.message().toString();
//                    response.body().getClass();
                    Toast.makeText(getApplicationContext(), response.message().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.response_failure, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
