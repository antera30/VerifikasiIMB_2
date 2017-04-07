package com.example.fajar.verifikasiimb.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fajar on 1/2/2017.
 */

public class Bangunan{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("fid")
    @Expose
    private Integer fid;
    @SerializedName("nib")
    @Expose
    private Integer nib;
    @SerializedName("no_sk")
    @Expose
    private Integer noSk;
    @SerializedName("no_persil")
    @Expose
    private Integer noPersil;
    @SerializedName("nama_site")
    @Expose
    private String namaSite;
    @SerializedName("id_pemilik")
    @Expose
    private Integer idPemilik;
    @SerializedName("id_wilayah")
    @Expose
    private Integer idWilayah;
    @SerializedName("id_alamat")
    @Expose
    private Integer idAlamat;
    @SerializedName("id_ket_imb")
    @Expose
    private Integer idKetImb;
    @SerializedName("id_landuse")
    @Expose
    private Integer idLanduse;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("nama_jalan")
    @Expose
    private String namaJalan;
    @SerializedName("gang")
    @Expose
    private String gang;
    @SerializedName("nomor")
    @Expose
    private String nomor;
    @SerializedName("id_kelurahan")
    @Expose
    private Integer idKelurahan;
    @SerializedName("id_kecamatan")
    @Expose
    private Integer idKecamatan;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Integer getNib() {
        return nib;
    }

    public void setNib(Integer nib) {
        this.nib = nib;
    }

    public Integer getNoSk() {
        return noSk;
    }

    public void setNoSk(Integer noSk) {
        this.noSk = noSk;
    }

    public Integer getNoPersil() {
        return noPersil;
    }

    public void setNoPersil(Integer noPersil) {
        this.noPersil = noPersil;
    }

    public String getNamaSite() {
        return namaSite;
    }

    public void setNamaSite(String namaSite) {
        this.namaSite = namaSite;
    }

    public Integer getIdPemilik() {
        return idPemilik;
    }

    public void setIdPemilik(Integer idPemilik) {
        this.idPemilik = idPemilik;
    }

    public Integer getIdWilayah() {
        return idWilayah;
    }

    public void setIdWilayah(Integer idWilayah) {
        this.idWilayah = idWilayah;
    }

    public Integer getIdAlamat() {
        return idAlamat;
    }

    public void setIdAlamat(Integer idAlamat) {
        this.idAlamat = idAlamat;
    }

    public Integer getIdKetImb() {
        return idKetImb;
    }

    public void setIdKetImb(Integer idKetImb) {
        this.idKetImb = idKetImb;
    }

    public Integer getIdLanduse() {
        return idLanduse;
    }

    public void setIdLanduse(Integer idLanduse) {
        this.idLanduse = idLanduse;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNamaJalan() {
        return namaJalan;
    }

    public void setNamaJalan(String namaJalan) {
        this.namaJalan = namaJalan;
    }

    public String getGang() {
        return gang;
    }

    public void setGang(String gang) {
        this.gang = gang;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public Integer getIdKelurahan() {
        return idKelurahan;
    }

    public void setIdKelurahan(Integer idKelurahan) {
        this.idKelurahan = idKelurahan;
    }

    public Integer getIdKecamatan() {
        return idKecamatan;
    }

    public void setIdKecamatan(Integer idKecamatan) {
        this.idKecamatan = idKecamatan;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Bangunan{" +
                "id=" + id +
                ", fid=" + fid +
                ", nib=" + nib +
                ", noSk=" + noSk +
                ", noPersil=" + noPersil +
                ", namaSite='" + namaSite + '\'' +
                ", idPemilik=" + idPemilik +
                ", idWilayah=" + idWilayah +
                ", idAlamat=" + idAlamat +
                ", idKetImb=" + idKetImb +
                ", idLanduse=" + idLanduse +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", namaJalan='" + namaJalan + '\'' +
                ", gang='" + gang + '\'' +
                ", nomor='" + nomor + '\'' +
                ", idKelurahan=" + idKelurahan +
                ", idKecamatan=" + idKecamatan +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}