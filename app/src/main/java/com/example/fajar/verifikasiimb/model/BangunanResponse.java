package com.example.fajar.verifikasiimb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fajar on 4/3/2017.
 */

public class BangunanResponse implements Serializable {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("bangunan")
    @Expose
    public ArrayList<Bangunan> bangunan = new ArrayList<>();

    public BangunanResponse(){

    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public ArrayList<Bangunan> getBangunan() {
        return bangunan;
    }

    public void setBangunan(ArrayList<Bangunan> bangunan) {
        this.bangunan = bangunan;
    }


}
