/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Movie;
import Model.Product;
import controller.HangHoaController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * @author pc
 */
public class SuaHang {
    public static SuaHang getInstance(int idProduct) throws SQLException {
        instance = new SuaHang(idProduct);
        return instance;
    }

    public static void dispose() {
        if (instance != null) {
            instance.fr.dispose();
            instance = null;
        }
    }

    public SuaHang(int idProduct) throws SQLException {
        this.product = HangHoaController.find(idProduct);
//        this.mainPns.put("selectTypeAccount", ContainerCreateAccount.selectTypeAccount());
        show();
    }

    private static SuaHang instance;
    private Movie product;
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
    JFrame fr = new JFrame("SỬA SẢN PHẨM");
    JLabel lb = new JLabel("Sửa SẢN PHẨM");
    JButton bt = new JButton("Sửa");

    public void show() {
        lbTenHang = new JLabel("Tên sản phẩm");
        tfTenHang = new JTextField(product.getName(), 15);
        lbDescription = new JLabel("Tóm tắt");
        tfDescription = new JTextField(product.getDescription(), 15);
        lbPrice = new JLabel("Giá");
        tfPrice = new JTextField(String.valueOf(product.getPrice()), 15);
        lbQuality = new JLabel("Số lượng");
        tfQuality = new JTextField(String.valueOf(product.getQuality()), 15);
        lbStartTime = new JLabel("Thời gian bắt đầu");
        tfStartTime = new JTextField(product.getStartTime(), 15);
        lbEndTime = new JLabel("Thời gian kết thúc");
        lbAgeLimit = new JLabel("Tuổi giới hạn");
        tfEndTime = new JTextField(String.valueOf(product.getEndTime()), 15);
        tfAgeLimit = new JTextField(String.valueOf(product.getAgeLimit()), 15);

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
               product.setPrice( Integer.parseInt(tfPrice.getText()));
                product.setDescription(tfDescription.getText());
                product.setEndTime( tfEndTime.getText());
                product.setAgeLimit( Integer.parseInt(tfAgeLimit.getText()));
                product.setName(tfTenHang.getText());
                product.setStartTime(tfStartTime.getText());
                product.setQuality( Integer.parseInt(tfQuality.getText()));

                boolean inserted = HangHoaController.suaHang(product);
                if(inserted ){
                    JOptionPane.showMessageDialog(fr,"Sửa sản phẩm thành công");
                    DanhMucHangHoa.dispose();
                    DanhMucHangHoa.getInstance();
                    SuaHang.dispose();

                    fr.dispose();
                }else{
                    JOptionPane.showMessageDialog(fr,"Sửa sản phẩm thất bại");
                    DanhMucHangHoa.getInstance();
                    SuaHang.dispose();
                }
            }
        });
        fr.setLocationRelativeTo(null);
        fr.setResizable(false);
        fr.setVisible(true);
    }

}


