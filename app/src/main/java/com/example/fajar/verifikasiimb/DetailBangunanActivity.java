package com.example.fajar.verifikasiimb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fajar.verifikasiimb.model.Bangunan;
import com.example.fajar.verifikasiimb.model.BangunanResponse;
import com.example.fajar.verifikasiimb.model.SelectedBangunanList;

import java.util.ArrayList;
import java.util.List;

public class DetailBangunanActivity extends AppCompatActivity {
    private Toolbar toolbar;
    Intent intent;
    int position, fid;
    Button btn_verification;
    private List<Bangunan> mValues;

    TextView tv_fid, tv_nib, tv_no_sk, tv_no_persil, tv_nama_site, tv_pemilik, tv_wilayah,
    tv_alamat, tv_ket_imb, tv_landuse, tv_nama_jalan, tv_gang, tv_nomor, tv_kelurahan, tv_kecamatan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bangunan);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detail Bangunan");

        intent = getIntent();
        Bundle extras = intent.getExtras();
        position = extras.getInt("position");
        int fid = extras.getInt("fid");
        int sk = extras.getInt("sk");
        int nib = extras.getInt("nib");
        int imb = extras.getInt("imb");
        int persil = extras.getInt("persil");
        int pemilik = extras.getInt("pemilik");
        int wilayah = extras.getInt("wilayah");
        int alamat = extras.getInt("alamat");
        int landuse = extras.getInt("landuse");
        int kelurahan = extras.getInt("kelurahan");
        int kecamatan = extras.getInt("kecamatan");
        String namasite = extras.getString("namasite");
        String namajalan = extras.getString("namajalan");
        String gang = extras.getString("gang");
        String nomor = extras.getString("nomor");
        Double latitude = extras.getDouble("latitide");
        Double longitude = extras.getDouble("longitude");


        Toast.makeText(this, ""+fid,Toast.LENGTH_LONG).show();

        btn_verification = (Button) findViewById(R.id.btn_verification);
        btn_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailBangunanActivity.this, VerificationActivity.class);
                startActivity(intent);
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
        tv_pemilik.setText(String.valueOf(pemilik));
        tv_wilayah = (TextView) findViewById(R.id.bangunan_wilayah);
        tv_wilayah.setText(String.valueOf(wilayah));
        tv_alamat = (TextView) findViewById(R.id.bangunan_alamat);
        tv_alamat.setText(String.valueOf(alamat));
        tv_ket_imb = (TextView) findViewById(R.id.bangunan_ket_imb);
        tv_ket_imb.setText(String.valueOf(imb));
        tv_landuse = (TextView) findViewById(R.id.bangunan_landuse);
        tv_landuse.setText(String.valueOf(landuse));
        tv_nama_jalan = (TextView) findViewById(R.id.bangunan_nama_jalan);
        tv_nama_jalan.setText(namajalan);
        tv_gang = (TextView) findViewById(R.id.bangunan_gang);
        tv_gang.setText(gang);
        tv_nomor = (TextView) findViewById(R.id.bangunan_nomor);
        tv_nomor.setText(nomor);
        tv_kelurahan = (TextView) findViewById(R.id.bangunan_kelurahan);
        tv_kelurahan.setText(String.valueOf(kelurahan));
        tv_kecamatan = (TextView) findViewById(R.id.bangunan_kecamatan);
        tv_kecamatan.setText(String.valueOf(kecamatan));



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(DetailBangunanActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
