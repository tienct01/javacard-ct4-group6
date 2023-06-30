/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.CouponController;
import controller.HangHoaController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author pc
 */
public class ThemMaKhuyenMai {
    public static ThemMaKhuyenMai getInstance() {
        instance = new ThemMaKhuyenMai();
        return instance;
    }

    public static void dispose() {
        if (instance != null) {
            instance.fr.dispose();
            instance = null;
        }
    }

    public ThemMaKhuyenMai() {

//        this.mainPns.put("selectTypeAccount", ContainerCreateAccount.selectTypeAccount());
        show();
    }

    private static ThemMaKhuyenMai instance;

    JLabel lbMaHang = new JLabel("Mã khuyến mại");
    JTextField tfMaHang = new JTextField(15);
    JLabel lbTenHang = new JLabel("Giá trị");
    JTextField tfTenHang = new JTextField(15);
    JLabel lbDonGiaNhap = new JLabel("Giá trị áp dụng");
    JLabel lbDonGiaBan = new JLabel("Mô tả");
    JTextField tfDonGiaNhap = new JTextField(15);
    JTextField tfDonGiaBan = new JTextField(15);
    JLabel lbDonViTinh = new JLabel("Đơn vị");
    JTextField tfDonViTinh = new JTextField(15);

    JLabel lbDiem = new JLabel("Điểm");
    JTextField tfDiem = new JTextField(15);
    JFrame fr = new JFrame("THÊM MÃ KHUYẾN MẠI");
    JLabel lb = new JLabel("THÊM MÃ KHUYẾN MẠI");
    JButton bt = new JButton("Thêm");

    public void show() {
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

                boolean inserted = CouponController.themMa(maHang, tenHang, donViTinh, donGiaNhap, donGiaBan, diem);
                if (inserted) {
                    JOptionPane.showMessageDialog(fr, "Thêm sản phẩm thành công");
                    DanhMucMaKhuyenMai.dispose();
                    DanhMucMaKhuyenMai.getInstance();
                    ThemMaKhuyenMai.dispose();

                    fr.dispose();
                } else {
                    JOptionPane.showMessageDialog(fr, "Thêm sản phẩm thất bại");
                    DanhMucMaKhuyenMai.dispose();
                    DanhMucMaKhuyenMai.getInstance();
                    ThemMaKhuyenMai.dispose();
                }
            }
        });
        fr.setLocationRelativeTo(null);
        fr.setResizable(false);
        fr.setVisible(true);
    }

}


