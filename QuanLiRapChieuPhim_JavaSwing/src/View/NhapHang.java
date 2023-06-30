/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Product;
import controller.HangHoaController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * @author pc
 */
public class NhapHang {
    public static NhapHang getInstance(int idProduct) throws SQLException {
        instance = new NhapHang(idProduct);
        return instance;
    }

    public static void dispose() {
        if (instance != null) {
            instance.fr.dispose();
            instance = null;
        }
    }

    public NhapHang(int idProduct) throws SQLException {
//       this.product = HangHoaController.find(idProduct);
//        this.mainPns.put("selectTypeAccount", ContainerCreateAccount.selectTypeAccount());
        show();
    }

    private static NhapHang instance;
    private Product product;
    private JLabel lbTenHang;
    private JLabel tfTenHang;

    private JLabel tfMaHang;
    private JLabel lbMaHang;
    private JLabel lbDonViTinh;
    private JTextField tfDonViTinh;

    JFrame fr = new JFrame("SỬA SẢN PHẨM");
    JLabel lb = new JLabel("Sửa SẢN PHẨM");
    JButton bt = new JButton("Sửa");

    public void show() {
        lbTenHang = new JLabel("Tên sản phẩm");
        tfTenHang = new JLabel(product.getTenHang());

        tfMaHang = new JLabel(String.valueOf(product.getMaHang()));
        lbMaHang = new JLabel("Mã sản phẩm");
        lbDonViTinh = new JLabel("Số lượng thêm: ");
        tfDonViTinh = new JTextField("0", 15);

        fr.setLayout(null);
        fr.setSize(350, 520);
        lbMaHang.setBounds(10, 70, 100, 30);
        tfMaHang.setBounds(130, 70, 170, 30);
        lb.setBounds(100, 10, 200, 50);
        lbTenHang.setBounds(10, 110, 100, 30);

        tfTenHang.setBounds(130, 110, 170, 30);

        lbDonViTinh.setBounds(10, 150, 100, 30);
        tfDonViTinh.setBounds(130, 150, 170, 30);

        bt.setBounds(200, 440, 70, 30);
        fr.add(lb);
        fr.add(lbMaHang);
        fr.add(tfMaHang);
        fr.add(lbTenHang);

        fr.add(lbDonViTinh);
        fr.add(tfTenHang);
        fr.add(tfDonViTinh);
        fr.add(bt);
        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                product.addSoLuong(Integer.valueOf(tfDonViTinh.getText()));

//                boolean inserted = HangHoaController.suaHang(product);
//                if (inserted) {
//                    JOptionPane.showMessageDialog(fr, "Sửa sản phẩm thành công");
//                    DanhMucHangHoa.dispose();
//                    DanhMucHangHoa.getInstance();
//                    NhapHang.dispose();
//
//                    fr.dispose();
//                } else {
//                    JOptionPane.showMessageDialog(fr, "Sửa sản phẩm thất bại");
//                    DanhMucHangHoa.getInstance();
//                    NhapHang.dispose();
//                }
            }
        });
        fr.setLocationRelativeTo(null);
        fr.setResizable(false);
        fr.setVisible(true);
    }

}


