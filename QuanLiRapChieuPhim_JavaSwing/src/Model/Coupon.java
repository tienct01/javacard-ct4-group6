/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 *
 * @author hieu
 */
public class Coupon {
    private String maKhuyenMai;
    private String moTa;
    private String donVi;
    private int giaTri;
    private int giaTriApDung;

    private int diem;

    public Coupon(String maKhuyenMai,  int giaTri, String donVi,int giaTriApDung,String moTa) {
        this.maKhuyenMai = maKhuyenMai;
        this.moTa = moTa;
        this.donVi = donVi;
        this.giaTri = giaTri;
        this.giaTriApDung = giaTriApDung;
    }

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public int getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(int giaTri) {
        this.giaTri = giaTri;
    }

    public int getGiaTriApDung() {
        return giaTriApDung;
    }

    public void setGiaTriApDung(int giaTriApDung) {
        this.giaTriApDung = giaTriApDung;
    }

    public int getDiem() {
        return diem;
    }

    public void setDiem(int diem) {
        this.diem = diem;
    }
}
