package com.example.fajar.verifikasiimb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fajar.verifikasiimb.config.AppConfig;
import com.example.fajar.verifikasiimb.model.VerificationRespond;
import com.example.fajar.verifikasiimb.rest.ApiInterface;
import com.example.fajar.verifikasiimb.rest.ApiUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    public ImageView iv_photo_1, iv_photo_2, iv_photo_3;

    // LogCat tag
    private static final String TAG = VerificationActivity.class.getSimpleName();

    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri_1, fileUri_2, fileUri_3;
    private String encodedImage_1 = "", encodedImage_2 = "", encodedImage_3 = "";
    private int BUTTON_ID;
    private ProgressDialog progress;
    private Button btn_upload;
    public int status;
    private EditText et_nomor_rumah, et_nama_jalan, et_keterangan;
    private ApiInterface mApiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Verifikasi Bangunan");

        iv_photo_1 = (ImageView) findViewById(R.id.photo_first);
        iv_photo_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BUTTON_ID = 1;
                captureImage(1);
            }
        });

        iv_photo_2 = (ImageView) findViewById(R.id.photo_second);
        iv_photo_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BUTTON_ID = 2;
                captureImage(2);
            }
        });


        iv_photo_3 = (ImageView) findViewById(R.id.photo_third);
        iv_photo_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BUTTON_ID = 3;
                captureImage(3);
            }
        });

        mApiInterface = ApiUtils.getAPIService();

        progress = new ProgressDialog(this);
        progress.setMessage("Please wait");
        progress.setTitle("Uploading Data");

        et_nomor_rumah = (EditText) findViewById(R.id.et_nomor_rumah_ver);
        et_nama_jalan = (EditText) findViewById(R.id.et_nama_jalan_ver);
        et_keterangan = (EditText) findViewById(R.id.et_keterangan_ver);

        btn_upload = (Button) findViewById(R.id.btn_upload_verifikasi);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();

                final int nomor_rumah = Integer.parseInt(et_nomor_rumah.getText().toString().trim());
                final String nama_jalan = et_nama_jalan.getText().toString().trim();
                final String keterangan = et_keterangan.getText().toString().trim();
                if (!TextUtils.isEmpty(nama_jalan) && !TextUtils.isEmpty(keterangan)
                        && !TextUtils.isEmpty(String.valueOf(nomor_rumah))) {
                    postVerifikasi(encodedImage_1, encodedImage_2, encodedImage_3, status,
                            nomor_rumah, nama_jalan, keterangan);
                }


//                new Thread() {
//                    public void run() {
//                        try {
//                            // sleep the thread, whatever time you want.
//                            sleep(1000);
//                        } catch (Exception e) {
//                        }
//                    }
//                }.start();

            }

        });

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.status_bangunan_spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> status = new ArrayList<String>();
        status.add("Belum berdiri");
        status.add("Sudah Berdiri");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


    }

    private void postVerifikasi(String encodedImage_1, String encodedImage_2, String encodedImage_3, int status, int nomor_rumah, String nama_jalan, String keterangan) {
        mApiInterface.postVerifikasi(status, encodedImage_1, nomor_rumah, nama_jalan, keterangan).enqueue(new Callback<VerificationRespond>() {
            @Override
            public void onResponse(Call<VerificationRespond> call, Response<VerificationRespond> response) {
                if (response.isSuccessful()) {
                    showResponse(response.body().toString());
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Uploading data verification Success : " +response.body().toString(), Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(i);
//                    VerificationActivity.this.finish();
                    Log.i(TAG, "post submitted to API." + response.body().toString());

                }
            }

            @Override
            public void onFailure(Call<VerificationRespond> call, Throwable t) {
                //Log.e(TAG, "Unable to submit post to API." + t.getMessage().toString());
            }
        });

    }

    private void showResponse(String s) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Launching camera app to capture image
     */
    private void captureImage(int idCamera) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (idCamera == 1) {
            startActivityForResult(intent, idCamera);
            // start the image capture Intent
        } else if (idCamera == 2) {
            startActivityForResult(intent, idCamera);

        } else if (idCamera == 3) {
            startActivityForResult(intent, idCamera);
        } else {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap photo = (Bitmap) data.getExtras().get("data");

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && photo != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            //Log.i("ID", String.valueOf(BUTTON_ID));
            fileUri_1 = getOutputMediaFileUri(photo);
            encodedImage_1 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            iv_photo_1.setImageURI(fileUri_1);
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK && photo != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            //Log.i("ID", String.valueOf(BUTTON_ID));
            fileUri_2 = getOutputMediaFileUri(photo);
            encodedImage_2 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            iv_photo_2.setImageURI(fileUri_2);
        } else if (requestCode == 3 && resultCode == Activity.RESULT_OK && photo != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            //.i("ID", String.valueOf(BUTTON_ID));
            fileUri_3 = getOutputMediaFileUri(photo);
            encodedImage_3 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            iv_photo_3.setImageURI(fileUri_3);
        } else {
            Log.i("result", "the bitmat is null");
        }
    }

    public Uri getOutputMediaFileUri(Bitmap bitmap) {
        return Uri.fromFile(getOutputMediaFile(bitmap));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(Bitmap bitmapImage) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                AppConfig.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + AppConfig.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mediaFile);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mediaFile;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        status = position;
        Toast.makeText(getApplicationContext(), "position : " + status, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
