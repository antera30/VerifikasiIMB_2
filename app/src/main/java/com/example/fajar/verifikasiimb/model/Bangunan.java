package com.example.fajar.verifikasiimb.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.android.volley.toolbox.StringRequest;
import com.example.fajar.verifikasiimb.function.DistanceMeasure;
import com.example.fajar.verifikasiimb.function.GPSTracker;
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
    @SerializedName("gambar_bangunan")
    @Expose
    private String gambarBangunan;
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
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("no_hp")
    @Expose
    private String noHp;
    @SerializedName("no_ktp")
    @Expose
    private Object noKtp;
    @SerializedName("nama_wilayah")
    @Expose
    private String namaWilayah;
    @SerializedName("id_imb")
    @Expose
    private Integer idImb;
    @SerializedName("ket_imb")
    @Expose
    private String ketImb;
    @SerializedName("tipe_landuse")
    @Expose
    private String tipeLanduse;
    @SerializedName("nama_kecamatan")
    @Expose
    private String namaKecamatan;
    @SerializedName("kelurahan")
    @Expose
    private String kelurahan;


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

    public String getGambarBangunan() {
        return gambarBangunan;
    }

    public void setGambarBangunan(String gambarBangunan) {
        this.gambarBangunan = gambarBangunan;
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

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public Object getNoKtp() {
        return noKtp;
    }

    public void setNoKtp(Object noKtp) {
        this.noKtp = noKtp;
    }

    public String getNamaWilayah() {
        return namaWilayah;
    }

    public void setNamaWilayah(String namaWilayah) {
        this.namaWilayah = namaWilayah;
    }

    public Integer getIdImb() {
        return idImb;
    }

    public void setIdImb(Integer idImb) {
        this.idImb = idImb;
    }

    public String getKetImb() {
        return ketImb;
    }

    public void setKetImb(String ketImb) {
        this.ketImb = ketImb;
    }

    public String getTipeLanduse() {
        return tipeLanduse;
    }

    public void setTipeLanduse(String tipeLanduse) {
        this.tipeLanduse = tipeLanduse;
    }

    public String getNamaKecamatan() {
        return namaKecamatan;
    }

    public void setNamaKecamatan(String namaKecamatan) {
        this.namaKecamatan = namaKecamatan;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }

    private Double distance;

    public void setDistance(Context mContext){
        GPSTracker gpsTracker = new GPSTracker(mContext);
        this.distance = DistanceMeasure.getDistance(getLatitude(), getLongitude(), gpsTracker.getLatitude(),gpsTracker.getLongitude());
    }

    public Double getDistance(){
        return distance;
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
                ", gambarBangunan='" + gambarBangunan + '\'' +
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