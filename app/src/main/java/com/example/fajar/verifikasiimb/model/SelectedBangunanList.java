package com.example.fajar.verifikasiimb.model;

/**
 * Created by Fajar on 4/6/2017.
 */

public class SelectedBangunanList {

    public int fid, nib, nomor_sk, nomor_persil, pemilik, wilayah, alamat, keterangan_imb, land_use, kelurahan, kecamatan;
    public String nama_site, nama_jalan, gang, nomor;
    public double latitude, longitude;

    public SelectedBangunanList(int fid, int nib, int nomor_sk, int nomor_persil, int pemilik, int wilayah, int alamat, int keterangan_imb, int land_use, int kelurahan, int kecamatan, String nama_site, String nama_jalan, String gang, String nomor, double latitude, double longitude) {
        this.fid = fid;
        this.nib = nib;
        this.nomor_sk = nomor_sk;
        this.nomor_persil = nomor_persil;
        this.pemilik = pemilik;
        this.wilayah = wilayah;
        this.alamat = alamat;
        this.keterangan_imb = keterangan_imb;
        this.land_use = land_use;
        this.kelurahan = kelurahan;
        this.kecamatan = kecamatan;
        this.nama_site = nama_site;
        this.nama_jalan = nama_jalan;
        this.gang = gang;
        this.nomor = nomor;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public SelectedBangunanList(){

    }

    public int getFid() {
        return fid;
    }

    public int getNib() {
        return nib;
    }

    public int getNomor_sk() {
        return nomor_sk;
    }

    public int getNomor_persil() {
        return nomor_persil;
    }

    public int getPemilik() {
        return pemilik;
    }

    public int getWilayah() {
        return wilayah;
    }

    public int getAlamat() {
        return alamat;
    }

    public int getKeterangan_imb() {
        return keterangan_imb;
    }

    public int getLand_use() {
        return land_use;
    }

    public int getKelurahan() {
        return kelurahan;
    }

    public int getKecamatan() {
        return kecamatan;
    }

    public String getNama_site() {
        return nama_site;
    }

    public String getNama_jalan() {
        return nama_jalan;
    }

    public String getGang() {
        return gang;
    }

    public String getNomor() {
        return nomor;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
