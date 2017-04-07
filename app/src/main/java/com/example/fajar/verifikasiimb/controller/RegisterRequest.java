package com.example.fajar.verifikasiimb.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fajar.verifikasiimb.config.AppConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fajar on 3/10/2017.
 */

public class RegisterRequest {
    private String URL_POST = AppConfig.URL_REGISTER;
    private Context mContext;
    private String name, email, password;
    private ProgressDialog progressDialog;
    public RegisterRequest(String name, String email, String password, Context context, ProgressDialog progressDialog){
        this.mContext = context;
        this.name = name;
        this.email = email;
        this.password = password;
        this.progressDialog = progressDialog;
    }

    public void ExecuteData(){
        progressDialog.setTitle("Registering...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                //make user
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext,error+"",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                return  params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

}
