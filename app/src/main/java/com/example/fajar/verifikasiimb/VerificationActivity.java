package com.example.fajar.verifikasiimb;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.example.fajar.verifikasiimb.function.ImageLoadingUtils;
import com.example.fajar.verifikasiimb.rest.ApiService;
import com.example.fajar.verifikasiimb.rest.ApiUtils;
import com.example.fajar.verifikasiimb.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VerificationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    public ImageView iv_photo_1, iv_photo_2, iv_photo_3;

    private static int TASK_ID_FROM_BANGUNAN = 200;

    // LogCat tag
    private static final String TAG = VerificationActivity.class.getSimpleName();

    public static final int MEDIA_TYPE_IMAGE = 1;
    private Button btn_upload;
    public int status;
    private EditText et_nomor_rumah, et_nama_jalan, et_keterangan;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Uri fileUri;
    ApiService service;
    ImageLoadingUtils utils;
    public static String realPath, sresultnameimage;
    public Context mContext;
    public static String ImageCompressedPath;
    ArrayList<String> capturedImagePath = new ArrayList<String>();
    public double latitude_bang, longitude_bang;
    public int task_id, bangunan_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = new ImageLoadingUtils(this);
        setContentView(R.layout.activity_verification);
        mContext = getApplicationContext();

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        task_id = extras.getInt("task_id");
        if (task_id == TASK_ID_FROM_BANGUNAN) {
            bangunan_id = extras.getInt("bangunan_id");
        } else {
            latitude_bang = extras.getDouble("latitude");
            longitude_bang = extras.getDouble("longitude");
        }

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Verifikasi Bangunan");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        service = new Retrofit.Builder().baseUrl(AppConfig.BASE_API_URL)
                .client(client).build()
                .create(ApiService.class);

        iv_photo_1 = (ImageView) findViewById(R.id.photo_first);
        iv_photo_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        iv_photo_2 = (ImageView) findViewById(R.id.photo_second);
        iv_photo_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        iv_photo_3 = (ImageView) findViewById(R.id.photo_third);
        iv_photo_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        et_nomor_rumah = (EditText) findViewById(R.id.et_nomor_rumah_ver);
        et_nama_jalan = (EditText) findViewById(R.id.et_nama_jalan_ver);
        et_keterangan = (EditText) findViewById(R.id.et_keterangan_ver);

        btn_upload = (Button) findViewById(R.id.btn_upload_verifikasi);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nomor_rumah = et_nomor_rumah.getText().toString().trim();
                final String nama_jalan = et_nama_jalan.getText().toString().trim();
                final String keterangan = et_keterangan.getText().toString().trim();

                if (!TextUtils.isEmpty(nama_jalan) && !TextUtils.isEmpty(keterangan)
                        && !TextUtils.isEmpty(String.valueOf(nomor_rumah))) {
//                    Toast.makeText(mContext, "position : " + String.valueOf(status) + " nomor : " + nomor_rumah +
//                            " nama jalan : " + nama_jalan + " keterangan : " + keterangan, Toast.LENGTH_LONG).show();
                    uploadFile(String.valueOf(status), nomor_rumah, nama_jalan, keterangan);
                }

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
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            sresultnameimage = Utils.getDateTime().toString();
            //Log.d("FILE URI", "Real Path: "+fileUri.toString());

            realPath = getRealPathFromUri(fileUri);


            new ImageCompressionAsyncTask().execute(realPath);


        } else if (resultCode == RESULT_CANCELED) {

            // user cancelled Image capture
            Toast.makeText(getApplicationContext(),
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();

        } else {
            // failed to capture image
            Toast.makeText(getApplicationContext(),
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        status = position;
        //Toast.makeText(getApplicationContext(), "position : " + status, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public ProgressDialog progressDialog;

    private void uploadFile(String kondisi_bangunan, String no_rumah, String nama_jalan, String keterangan) {
        // String filePath = getRealPathFromUri(fileUri);

        progressDialog = new ProgressDialog(VerificationActivity.this);
        progressDialog.setMessage("uploading data ...");
        progressDialog.setTitle("Upload data to Server");
        progressDialog.setCancelable(true);
        progressDialog.show();

        RequestBody konbang = RequestBody.create(MediaType.parse("text/plain"), kondisi_bangunan);
        RequestBody norum = RequestBody.create(MediaType.parse("text/plain"), no_rumah);
        RequestBody namjal = RequestBody.create(MediaType.parse("text/plain"), nama_jalan);
        RequestBody keter = RequestBody.create(MediaType.parse("text/plain"), keterangan);


        if (capturedImagePath.size() == 1 && task_id == TASK_ID_FROM_BANGUNAN) {
            RequestBody id_bangunan = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(bangunan_id));
            File file = new File(capturedImagePath.get(0));
            if (file.exists()) {
                // create RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                // executes the request
                Call<ResponseBody> call = service.postImage(body, konbang, norum, namjal, keter, id_bangunan);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call,
                                           Response<ResponseBody> response) {
                        progressDialog.dismiss();
                        if (response.code() == 200) {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            Toast.makeText(getApplicationContext(), "Success Upload Data", Toast.LENGTH_LONG).show();
                            finish();
                            Log.i(TAG, "success" + response.message().toString());
                            Log.i(TAG, "success" + response.body().toString());
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to upload, please check your connection", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                        progressDialog.dismiss();
                    }
                });
            }
        } else if (capturedImagePath.size() == 2 && task_id == TASK_ID_FROM_BANGUNAN) {
            RequestBody id_bangunan = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(bangunan_id));
            File file_1 = new File(capturedImagePath.get(0));
            File file_2 = new File(capturedImagePath.get(1));
            if (file_1.exists() && file_2.exists()) {
                // create RequestBody instance from file
                RequestBody requestFile_1 = RequestBody.create(MediaType.parse("multipart/form-data"), file_1);
                MultipartBody.Part body_1 =
                        MultipartBody.Part.createFormData("image", file_1.getName(), requestFile_1);

                RequestBody requestFile_2 = RequestBody.create(MediaType.parse("multipart/form-data"), file_2);
                MultipartBody.Part body_2 =
                        MultipartBody.Part.createFormData("image_2", file_2.getName(), requestFile_2);

                // executes the request
                Call<ResponseBody> call = service.postImage(body_1, body_2, konbang, norum, namjal, keter, id_bangunan);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call,
                                           Response<ResponseBody> response) {
                        progressDialog.dismiss();
                        Log.i(TAG, "success" + response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                        progressDialog.dismiss();
                    }
                });
            }
        } else if (capturedImagePath.size() == 3 && task_id == TASK_ID_FROM_BANGUNAN) {
            RequestBody id_bangunan = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(bangunan_id));
            File file_1 = new File(capturedImagePath.get(0));
            File file_2 = new File(capturedImagePath.get(1));
            File file_3 = new File(capturedImagePath.get(2));
            if (file_1.exists() && file_2.exists() && file_3.exists()) {
                // create RequestBody instance from file
                RequestBody requestFile_1 = RequestBody.create(MediaType.parse("multipart/form-data"), file_1);
                MultipartBody.Part body_1 =
                        MultipartBody.Part.createFormData("image", file_1.getName(), requestFile_1);

                RequestBody requestFile_2 = RequestBody.create(MediaType.parse("multipart/form-data"), file_2);
                MultipartBody.Part body_2 =
                        MultipartBody.Part.createFormData("image_2", file_2.getName(), requestFile_2);

                RequestBody requestFile_3 = RequestBody.create(MediaType.parse("multipart/form-data"), file_3);
                MultipartBody.Part body_3 =
                        MultipartBody.Part.createFormData("image_3", file_3.getName(), requestFile_3);

                // executes the request
                Call<ResponseBody> call = service.postImage(body_1, body_2, body_3, konbang, norum, namjal, keter, id_bangunan);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call,
                                           Response<ResponseBody> response) {
                        progressDialog.dismiss();
                        Log.i(TAG, "success" + response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                        progressDialog.dismiss();
                    }
                });
            }
        } else if (capturedImagePath.size() == 1 && task_id != TASK_ID_FROM_BANGUNAN) {
            RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude_bang));
            RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude_bang));
            File file = new File(capturedImagePath.get(0));
            if (file.exists()) {
                // create RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                // executes the request
                Call<ResponseBody> call = service.postImage(body, konbang, norum, namjal, keter, latitude, longitude);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call,
                                           Response<ResponseBody> response) {
                        progressDialog.dismiss();
                        if (response.code() == 200) {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            Toast.makeText(getApplicationContext(), "Success Upload Data", Toast.LENGTH_LONG).show();
                            finish();
                            Log.i(TAG, "success" + response.message().toString());
                            Log.i(TAG, "success" + response.body().toString());
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to upload, please check your connection", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                        progressDialog.dismiss();
                    }
                });
            }
        } else if (capturedImagePath.size() == 2 && task_id != TASK_ID_FROM_BANGUNAN) {
            RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude_bang));
            RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude_bang));
            File file_1 = new File(capturedImagePath.get(0));
            File file_2 = new File(capturedImagePath.get(1));
            if (file_1.exists() && file_2.exists()) {
                // create RequestBody instance from file
                RequestBody requestFile_1 = RequestBody.create(MediaType.parse("multipart/form-data"), file_1);
                MultipartBody.Part body_1 =
                        MultipartBody.Part.createFormData("image", file_1.getName(), requestFile_1);

                RequestBody requestFile_2 = RequestBody.create(MediaType.parse("multipart/form-data"), file_2);
                MultipartBody.Part body_2 =
                        MultipartBody.Part.createFormData("image_2", file_2.getName(), requestFile_2);

                // executes the request
                Call<ResponseBody> call = service.postImage(body_1, body_2, konbang, norum, namjal, keter, latitude, longitude);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call,
                                           Response<ResponseBody> response) {
                        progressDialog.dismiss();
                        Log.i(TAG, "success" + response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                        progressDialog.dismiss();
                    }
                });
            }
        } else if (capturedImagePath.size() == 3 && task_id != TASK_ID_FROM_BANGUNAN) {
            RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude_bang));
            RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude_bang));
            File file_1 = new File(capturedImagePath.get(0));
            File file_2 = new File(capturedImagePath.get(1));
            File file_3 = new File(capturedImagePath.get(2));
            if (file_1.exists() && file_2.exists() && file_3.exists()) {
                // create RequestBody instance from file
                RequestBody requestFile_1 = RequestBody.create(MediaType.parse("multipart/form-data"), file_1);
                MultipartBody.Part body_1 =
                        MultipartBody.Part.createFormData("image", file_1.getName(), requestFile_1);

                RequestBody requestFile_2 = RequestBody.create(MediaType.parse("multipart/form-data"), file_2);
                MultipartBody.Part body_2 =
                        MultipartBody.Part.createFormData("image_2", file_2.getName(), requestFile_2);

                RequestBody requestFile_3 = RequestBody.create(MediaType.parse("multipart/form-data"), file_3);
                MultipartBody.Part body_3 =
                        MultipartBody.Part.createFormData("image_3", file_3.getName(), requestFile_3);

                // executes the request
                Call<ResponseBody> call = service.postImage(body_1, body_2, body_3, konbang, norum, namjal, keter, latitude, longitude);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call,
                                           Response<ResponseBody> response) {
                        progressDialog.dismiss();
                        Log.i(TAG, "success" + response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                        progressDialog.dismiss();
                    }
                });
            }
        }


    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

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
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public String getRealPathFromUri(final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(mContext, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(mContext, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(mContext, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(mContext, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

//    ProgressDialog progressDialogcompres;

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
//            progressDialogcompres = new ProgressDialog(VerificationActivity.this);
//            progressDialogcompres.setMessage("Compress Foto...");
//            progressDialogcompres.setCancelable(false);
//            progressDialogcompres.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = compressImage(params[0]);
            Log.i("FIle Path", filePath);
            return filePath;
        }

        public String compressImage(String imageUri) {

            String filePath = realPath;
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
            float maxHeight = 800.0f;
            float maxWidth = 600.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            // scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            Paint paint = new Paint();
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

            Paint tPaint = new Paint();
            tPaint.setTextSize(20);
            tPaint.setColor(Color.YELLOW);
            tPaint.setStyle(Paint.Style.FILL);
            Typeface tf = Typeface.create("RobotBold.ttf", Typeface.BOLD);
            paint.setTypeface(tf);
            paint.setTextAlign(Paint.Align.LEFT);
            float height = tPaint.measureText("yY");
            canvas.drawText(Utils.getSettingTime(VerificationActivity.this).toString(), 20f, height + 15f, tPaint);

            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out = null;

            String filename = Utils.getFilename(sresultnameimage, "Images");

            try {
                out = new FileOutputStream(filename);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return filename;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            progressDialogcompres.dismiss();
            capturedImagePath.add(result);
            ImageCompressedPath = result;
            if (capturedImagePath.size() == 1) {
                Picasso.with(getApplicationContext()).load("file://" + capturedImagePath.get(0)).into(iv_photo_1);
            } else if (capturedImagePath.size() == 2) {
                Picasso.with(getApplicationContext()).load("file://" + capturedImagePath.get(1)).into(iv_photo_2);
            } else if (capturedImagePath.size() == 3) {
                Picasso.with(getApplicationContext()).load("file://" + capturedImagePath.get(2)).into(iv_photo_3);
            }

        }

    }

}
