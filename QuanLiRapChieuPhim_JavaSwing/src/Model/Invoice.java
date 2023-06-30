/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.UnsupportedEncodingException;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

import java.util.ArrayList;

/**
 * @author hieu
 */
public class Invoice {
    private ArrayList<Movie> products;
    private int total;
    private int indexMaGiamGia = 0x60;

    public int getIndexMaGiamGia() {
        return indexMaGiamGia;
    }

    public void setIndexMaGiamGia(int indexMaGiamGia) {
        this.indexMaGiamGia = indexMaGiamGia;
    }

    public int getIntoMoney() {
        int result = total;

        if (coupon != null) {
            String donVi = coupon.getDonVi();
            if (donVi.equals("%")) {
                int giamGia = (int) result * coupon.getGiaTri() / 100;
                result -= giamGia;
            } else {
                result -= coupon.getGiaTri();

            }

        }
        return result;
    }


    //    private
    private Admin admin;
    private User user;
    private Coupon coupon;

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    private int diem;
    private int diemObligatory = 0;

    public int getDiem() {
        return diem;
    }

    public void addDiem(int diem) {
        this.diem += diem;
        this.diemObligatory += diem;
    }

    public int getDiemObligatory() {
        return diemObligatory;
    }

    public void setDiem(int diem) {
        this.diem = max(diem, diemObligatory);
    }


    public Invoice(User user) {
        this.user = user;
        this.admin = Admin.currentAdmin;
        products = new ArrayList<Movie>();
        total = 0;
    }

    public ArrayList<Movie> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Movie> products) {
        this.products = products;
    }


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void addTotal(int i, int quantity) {
//        this.total -= products.get(i).getTotal();
//
//        products.get(i).incCurrentQuantity(quantity);
//        this.total += products.get(i).getTotal();
    }

    public void addTotal(int total) {
        this.total += total;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<String> getMaKhuyenmaiDonHang() {
        ArrayList<String> maKhuyenMai = new ArrayList<String>();
//        int leng = products.size();
//        for (int i = 0; i < leng; i++) {
//            Product xy = products.get(i);
//            if (xy.getMaKhuyenMai().equals("")) continue;
//            for (int j = 0; j < xy.getCurrentQuantity(); j++)
//                maKhuyenMai.add(xy.getMaKhuyenMai());
//
//        }
        return maKhuyenMai;
    }
}
