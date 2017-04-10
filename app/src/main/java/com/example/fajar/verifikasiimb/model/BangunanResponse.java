package com.example.fajar.verifikasiimb.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Fajar on 4/3/2017.
 */

public class BangunanResponse implements Serializable{
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

    public ArrayList<Bangunan> getBangunan(Context mContext) {
        sortBangunan(bangunan, mContext);
        return bangunan;
    }

    public void setBangunan(ArrayList<Bangunan> bangunan) {
        this.bangunan = bangunan;
    }

    public void sortBangunan(List<Bangunan> o, final Context mContext){
        Collections.sort(o, new Comparator<Bangunan>() {
            @Override
            public int compare(Bangunan lhs, Bangunan rhs) {
                lhs.setDistance(mContext);
                rhs.setDistance(mContext);
                return lhs.getDistance().compareTo(rhs.getDistance());

            }
        });
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
