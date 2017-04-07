package com.example.fajar.verifikasiimb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fajar on 4/7/2017.
 */

public class VerificationRespond {

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("id")
    @Expose
    private int id;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "VerificationRespond{" +
                "error=" + error +
                ", id=" + id +
                '}';
    }
}