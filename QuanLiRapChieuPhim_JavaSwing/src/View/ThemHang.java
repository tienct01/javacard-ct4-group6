/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Movie;
import controller.HangHoaController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author pc
 */
public class ThemHang {
    public static ThemHang getInstance() {
        instance = new ThemHang();
        return instance;
    }

    public static void dispose() {
        if (instance != null) {
            instance.fr.dispose();
            instance = null;
        }
    }

    public ThemHang() {

//        this.mainPns.put("selectTypeAccount", ContainerCreateAccount.selectTypeAccount());
        show();
    }

    private static ThemHang instance;
    private JLabel lbTenHang;
    private JTextField tfTenHang;
    private JLabel lbEndTime;
    private JLabel lbAgeLimit;
    private JTextField tfEndTime;
    private JTextField tfAgeLimit;
    private JLabel tfMaHang;
    private JLabel lbMaHang;
    private JLabel lbDonViTinh;
    private JTextField tfDonViTinh;
    private JLabel lbDescription;
    private JTextField tfDescription;
    private JLabel lbPrice;
    private JTextField tfPrice;
    private JLabel lbQuality;
    private JTextField tfQuality;
    private JLabel lbStartTime;
    private JTextField tfStartTime;
    JFrame fr = new JFrame("Thêm SẢN PHẨM");
    JLabel lb = new JLabel("Thêm SẢN PHẨM");
    JButton bt = new JButton("Thêm");

    public void show() {
        lbTenHang = new JLabel("Tên sản phẩm");
        tfTenHang = new JTextField( 15);
        lbDescription = new JLabel("Tóm tắt");
        tfDescription = new JTextField( 15);
        lbPrice = new JLabel("Giá");
        tfPrice = new JTextField(15);
        lbQuality = new JLabel("Số lượng");
        tfQuality = new JTextField(15);
        lbStartTime = new JLabel("Thời gian bắt đầu");
        tfStartTime = new JTextField( 15);
        lbEndTime = new JLabel("Thời gian kết thúc");
        lbAgeLimit = new JLabel("Tuổi giới hạn");
        tfEndTime = new JTextField(15);
        tfAgeLimit = new JTextField( 15);

        fr.setLayout(null);
        fr.setSize(350, 520);
//        lbMaHang.setBounds(10, 70, 100, 30);
//        tfMaHang.setBounds(130, 70, 170, 30);
        lb.setBounds(100, 10, 200, 50);
        lbTenHang.setBounds(10, 110, 100, 30);
        lbDescription.setBounds(10, 190, 100, 30);
        lbAgeLimit.setBounds(10, 230, 100, 30);
        tfTenHang.setBounds(130, 110, 170, 30);
        tfDescription.setBounds(130, 190, 170, 30);
        tfAgeLimit.setBounds(130, 230, 170, 30);
//        lbDonViTinh.setBounds(10, 150, 100, 30);
//        tfDonViTinh.setBounds(130, 150, 170, 30);
        lbPrice.setBounds(10, 270, 100, 30);
        tfPrice.setBounds(130, 270, 170, 30);
        lbStartTime.setBounds(10, 310, 70, 30);
        tfStartTime.setBounds(130, 310, 170, 30);
        lbEndTime.setBounds(10, 350, 100, 30);
        tfEndTime.setBounds(130, 350, 170, 30);
        lbQuality.setBounds(10, 390, 100, 30);
        tfQuality.setBounds(150, 390, 170, 30);
        bt.setBounds(200, 440, 70, 30);
        fr.add(lb);
//        fr.add(lbMaHang);
//        fr.add(tfMaHang);
        fr.add(lbTenHang);
        fr.add(lbEndTime);
        fr.add(lbAgeLimit);
//        fr.add(lbDonViTinh);
        fr.add(tfTenHang);
        fr.add(tfEndTime);
        fr.add(tfAgeLimit);
//        fr.add(tfDonViTinh);
        fr.add(bt);
        fr.add(lbDescription);
        fr.add(tfDescription);
        fr.add(lbPrice);
        fr.add(tfPrice);
        fr.add(lbQuality);
        fr.add(tfQuality);
        fr.add(lbStartTime);
        fr.add(tfStartTime);
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = tfTenHang.getText();
                String description = tfDescription.getText();
                String ageLimit = tfAgeLimit.getText();
                String price = tfPrice.getText();
                String startTime = tfStartTime.getText();
                String endTime = tfEndTime.getText();
                String quality = tfQuality.getText();

                Movie movie = new Movie(1, name, description, Integer.parseInt(quality), startTime, endTime, Integer.parseInt(price), Integer.parseInt(ageLimit));

                boolean inserted = HangHoaController.themHang(movie);
                if (inserted) {
                    JOptionPane.showMessageDialog(fr, "Thêm sản phẩm thành công");
                    DanhMucHangHoa.dispose();
                    DanhMucHangHoa.getInstance();
                    ThemHang.dispose();

                    fr.dispose();
                } else {
                    JOptionPane.showMessageDialog(fr, "Thêm sản phẩm thất bại");
                    DanhMucHangHoa.dispose();
                    DanhMucHangHoa.getInstance();
                    ThemHang.dispose();
                }
            }
        });
        fr.setLocationRelativeTo(null);
        fr.setResizable(false);
        fr.setVisible(true);
    }

}


