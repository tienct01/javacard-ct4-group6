package Model;
import java.util.Vector;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hieu
 */
public class Movie {

    //
    private int id;
    private String name;
    private String description;

    private int ageLimit;
    private int price;
    private int quality;
    private String startTime;
    private String endTime;

    private int currentQuantity = 0;

    public static Movie currentAdmin;

    public Movie(int maHang, String tenHang, String description, int soLuong, String startTime, String endTime, int price, int age) {
        this.name = tenHang;
        this.description = description;
        this.id = maHang;
        this.startTime = startTime;
        this.endTime = endTime;
        this.quality = soLuong;
        this.price = price;
        this.ageLimit = age;
    }

//    public Movie(String tenHang, String donViTinh, int donGiaNhap, int donGiaBan) {
//        this.name = tenHang;
//        this.donViTinh = donViTinh;
//        this.donGiaNhap = donGiaNhap;
//        this.donGiaBan = donGiaBan;
//    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public int incCurrentQuantity(int soLuong) {
        this.currentQuantity += soLuong;
        if (this.currentQuantity < 0) currentQuantity = 0;
        if (this.currentQuantity > this.quality) currentQuantity = this.quality;
        return currentQuantity;
    }

//    public int getTotal(int price) {
//        int total = price * currentQuantity;
//        int khuyenmai = 0;
//        if (donViGiamGia.equals("%")) {
//            khuyenmai = (int) total * giamGia / 100;
//        } else {
//            khuyenmai = giamGia * currentQuantity;
//        }
//        return total - khuyenmai;
//    }


    public Vector getVectorDetail() {
        Vector record = new Vector();
        record.add(getName());
        record.add(getCurrentQuantity());
        record.add(getPrice());
        record.add(getCurrentQuantity()*getPrice());

        return record;
    }

   
}

