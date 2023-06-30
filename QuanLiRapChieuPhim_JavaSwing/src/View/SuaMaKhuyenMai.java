/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.CouponController;
import Model.Coupon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * @author pc
 */
public class SuaMaKhuyenMai {
    public static SuaMaKhuyenMai getInstance(String maKhuyenMaiCu) {
        instance = new SuaMaKhuyenMai(maKhuyenMaiCu);
        return instance;
    }

    public static void dispose() {
        if (instance != null) {
            instance.fr.dispose();
            instance = null;
        }
    }

    public SuaMaKhuyenMai(String maKhuyenMaiCu) {
        try {
            this.coupon = CouponController.find(maKhuyenMaiCu);
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        this.mainPns.put("selectTypeAccount", ContainerCreateAccount.selectTypeAccount());
        show();
    }

    private static SuaMaKhuyenMai instance;

    Coupon coupon;

    JLabel lbMaHang = new JLabel("Mã khuyến mại");
    JTextField tfMaHang;
    JLabel lbTenHang = new JLabel("Giá trị");
    JTextField tfTenHang;
    JLabel lbDonGiaNhap = new JLabel("Giá trị áp dụng");
    JLabel lbDonGiaBan = new JLabel("Mô tả");
    JTextField tfDonGiaNhap;
    JTextField tfDonGiaBan;
    JLabel lbDonViTinh = new JLabel("Đơn vị");
    JTextField tfDonViTinh;

    JLabel lbDiem = new JLabel("Điểm");
    JTextField tfDiem;
    JFrame fr = new JFrame("THÊM MÃ KHUYẾN MẠI");
    JLabel lb = new JLabel("THÊM MÃ KHUYẾN MẠI");
    JButton bt = new JButton("Sửa");

    public void show() {

        tfMaHang = new JTextField(coupon.getMaKhuyenMai(), 15);
        tfTenHang = new JTextField(String.valueOf(coupon.getGiaTri()), 15);
        tfDonGiaNhap = new JTextField(String.valueOf(coupon.getGiaTriApDung()), 15);
        tfDonGiaBan = new JTextField(coupon.getMoTa(), 15);
        tfDonViTinh = new JTextField(coupon.getDonVi(), 15);
        tfDiem = new JTextField(String.valueOf(coupon.getDiem()), 15);

        fr.setLayout(null);
        fr.setSize(350, 450);
        lb.setBounds(85, 10, 200, 50);
        lbTenHang.setBounds(10, 110, 100, 30);
        lbMaHang.setBounds(10, 70, 100, 30);
        lbDonGiaNhap.setBounds(10, 190, 100, 30);
        lbDonGiaBan.setBounds(10, 230, 100, 30);
        lbDonViTinh.setBounds(10, 150, 70, 30);
        lbDiem.setBounds(10, 270, 70, 30);

        tfMaHang.setBounds(130, 70, 170, 30);
        tfTenHang.setBounds(130, 110, 170, 30);
        tfDonGiaNhap.setBounds(130, 190, 170, 30);
        tfDonGiaBan.setBounds(130, 230, 170, 30);
        tfDonViTinh.setBounds(130, 150, 170, 30);
        tfDiem.setBounds(130, 270, 170, 30);
        bt.setBounds(200, 340, 70, 30);
        fr.add(lb);
        fr.add(lbTenHang);
        fr.add(lbMaHang);
        fr.add(lbDonGiaNhap);
        fr.add(lbDonGiaBan);
        fr.add(lbDonViTinh);
        fr.add(lbDiem);
        fr.add(tfTenHang);
        fr.add(tfMaHang);
        fr.add(tfDonGiaNhap);
        fr.add(tfDonGiaBan);
        fr.add(tfDonViTinh);
        fr.add(tfDiem);
        fr.add(bt);
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String maHang = tfMaHang.getText();
                String tenHang = tfTenHang.getText();
                String donGiaNhap = tfDonGiaNhap.getText();
                String donGiaBan = tfDonGiaBan.getText();
                String donViTinh = tfDonViTinh.getText();
                String diem = tfDiem.getText();

                boolean inserted = CouponController.suaMaKhuyenMai(coupon.getMaKhuyenMai(), maHang, tenHang, donViTinh, donGiaNhap, donGiaBan, diem);
                if (inserted) {
                    JOptionPane.showMessageDialog(fr, "Sửa sản phẩm thành công");
                    DanhMucMaKhuyenMai.dispose();
                    DanhMucMaKhuyenMai.getInstance();
                    SuaMaKhuyenMai.dispose();

                    fr.dispose();
                } else {
                    JOptionPane.showMessageDialog(fr, "Sửa sản phẩm thất bại");
                    DanhMucMaKhuyenMai.dispose();
                    DanhMucMaKhuyenMai.getInstance();
                    SuaMaKhuyenMai.dispose();
                }
            }
        });
        fr.setLocationRelativeTo(null);
        fr.setResizable(false);
        fr.setVisible(true);
    }

}


