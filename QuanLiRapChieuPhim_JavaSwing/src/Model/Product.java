package Model;
import DB.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.security.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hieu
 */
public class Product {

    private String tenHang;
    private String donViTinh;
    private int maHang;
    private int donGiaNhap;
    private int donGiaBan;
    private int soLuong;
    private int currentQuantity = 0;
    private String maKhuyenMai = "";
    private int diem;
     private int giamGia;
    private String donViGiamGia;
    public static Product currentAdmin;

    public Product(int maHang, String tenHang, String donViTinh, int soLuong, int donGiaNhap, int donGiaBan, String maKhuyenMai, int diem, int giamGia, String donViGiamGia) {
        this.tenHang = tenHang;
        this.donViTinh = donViTinh;
        this.maHang = maHang;
        this.donGiaNhap = donGiaNhap;
        this.donGiaBan = donGiaBan;
        this.soLuong = soLuong;
        this.maKhuyenMai = maKhuyenMai;
        this.diem = diem;
        this.giamGia = giamGia;
        this.donViGiamGia = donViGiamGia;
    }

    public Product(String tenHang, String donViTinh, int donGiaNhap, int donGiaBan) {
        this.tenHang = tenHang;
        this.donViTinh = donViTinh;
        this.donGiaNhap = donGiaNhap;
        this.donGiaBan = donGiaBan;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public int getMaHang() {
        return maHang;
    }

    public void setMaHang(int maHang) {
        this.maHang = maHang;
    }

    public int getDonGiaNhap() {
        return donGiaNhap;
    }

    public void setDonGiaNhap(int donGiaNhap) {
        this.donGiaNhap = donGiaNhap;
    }

    public int getDonGiaBan() {
        return donGiaBan;
    }
    
    public void setDonGiaBan(int donGiaBan) {
        this.donGiaBan = donGiaBan;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
     public void addSoLuong(int soLuong) {
        this.soLuong += soLuong;
    }
    
    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public int incCurrentQuantity(int soLuong) {
        this.currentQuantity += soLuong;
        if (this.currentQuantity < 0) currentQuantity = 0; 
         if (this.currentQuantity > this.soLuong) currentQuantity = this.soLuong; 
        return currentQuantity;
    }

    public String getMaKhuyenMai() {
        return (maKhuyenMai == null) ? "" : maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public int getDiem() {
        return diem;
    }

    public void setDiem(int diem) {
        this.diem = diem;
    }

    public int getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(int giamGia) {
        this.giamGia = giamGia;
    }

    public String getDonViGiamGia() {
        return donViGiamGia;
    }

    public void setDonViGiamGia(String donViGiamGia) {
        this.donViGiamGia = donViGiamGia;
    }

    public static Product getCurrentAdmin() {
        return currentAdmin;
    }

    public static void setCurrentAdmin(Product currentAdmin) {
        Product.currentAdmin = currentAdmin;
    }
    public int getTotal() {
        int total = donGiaBan * currentQuantity;
        int khuyenmai = 0;
        if (donViGiamGia.equals("%")) {
            khuyenmai = (int) total * giamGia / 100;
        } else {
            khuyenmai = giamGia * currentQuantity;
        }
        return total - khuyenmai;
    }
    public Vector getVectorDetail() {
        Vector record = new Vector();
         record.add(getMaHang());
        record.add(getTenHang());
        record.add(getCurrentQuantity());
        record.add(getDonGiaBan());
        record.add(getDonViTinh());
         record.add(getDiem());
        record.add(getMaKhuyenMai());
        if ( getDonViGiamGia().equals("%")) {
            record.add(getGiamGia() + " " + getDonViGiamGia());
        } else {
            record.add(getGiamGia() * soLuong + " " + getDonViGiamGia());
        }
        record.add(getTotal());
        return record;
    }

   
}

