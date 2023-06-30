/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Constants.ConstAdmin;
import Controller.CouponController;
import Model.Admin;
import controller.HangHoaController;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * @author pc
 */
public class DanhMucMaKhuyenMai {

    public static DanhMucMaKhuyenMai getInstance() {
        instance = new DanhMucMaKhuyenMai();
        return instance;
    }

    public static void dispose() {
        if (instance != null) {
            instance.frDanhMucHangHoa.dispose();
            instance = null;
        }
    }

    public DanhMucMaKhuyenMai() {

//        this.mainPns.put("selectTypeAccount", ContainerCreateAccount.selectTypeAccount());
        showDanhMucHangHoa();
    }

    private static DanhMucMaKhuyenMai instance;
    Font font = new Font("Serif", Font.PLAIN, 13);
    JButton btThem = new JButton("Thêm");
    JButton btXoa = new JButton("Xóa");
    JButton btSua = new JButton("Sửa");
    JTextField btTimKiem = new JTextField();
    JFrame frDanhMucHangHoa = new JFrame("DANH MỤC COUPON");
    JLabel lb1 = new JLabel("DANH MỤC COUPON");
    String x = "";

    public void showDanhMucHangHoa() {
        JTable tb = new JTable(CouponController.getList(""));
        JScrollPane sp = new JScrollPane(tb);
        frDanhMucHangHoa.setSize(1000, 550);
        frDanhMucHangHoa.setLayout(null);
        lb1.setBounds(170, 10, 600, 40);
        lb1.setFont(new Font("Serif", Font.PLAIN, 30));
        btThem.setFont(font);
        btSua.setFont(font);
        btXoa.setFont(font);
        btTimKiem.setFont(font);
        tb.setFont(font);
        sp.setBounds(25, 55, 691 + 252, 400);
        tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tb.getColumnModel().getColumn(0).setPreferredWidth(80);
        tb.getColumnModel().getColumn(1).setPreferredWidth(200);
        tb.getColumnModel().getColumn(2).setPreferredWidth(69);
        tb.getColumnModel().getColumn(3).setPreferredWidth(155);
        tb.getColumnModel().getColumn(4).setPreferredWidth(336);
        tb.getColumnModel().getColumn(5).setPreferredWidth(100);

        btTimKiem.setBounds(25, 460, 105, 30);
        btXoa.setBounds(675, 460, 70, 25);
        btSua.setBounds(775, 460, 70, 25);
        btThem.setBounds(875, 460, 70, 25);
        frDanhMucHangHoa.add(sp);
        frDanhMucHangHoa.add(lb1);
        frDanhMucHangHoa.add(btThem);
        frDanhMucHangHoa.add(btSua);
        frDanhMucHangHoa.add(btXoa);
        frDanhMucHangHoa.add(btTimKiem);
        btThem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Admin.currentAdmin.getRole() == ConstAdmin.role.get("giamDoc") || Admin.currentAdmin.getRole() == ConstAdmin.role.get("quanKho")) {
                    // fr.setVisible(false)

                    ThemMaKhuyenMai.getInstance();
                } else
                    JOptionPane.showMessageDialog(frDanhMucHangHoa, "Bạn không có quyền thêm");
            }
        });
        btSua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Admin.currentAdmin.getRole() == ConstAdmin.role.get("giamDoc") || Admin.currentAdmin.getRole() == ConstAdmin.role.get("quanKho")) {
                    // fr.setVisible(false)
                    int x = tb.getSelectedRow();
                    if (x != -1) {
                        String idProduct = (String) tb.getValueAt(x, 0);
                        SuaMaKhuyenMai.getInstance(idProduct);
                    } else JOptionPane.showMessageDialog(frDanhMucHangHoa, "Chọn 1 hàng để sửa");
                } else
                    JOptionPane.showMessageDialog(frDanhMucHangHoa, "Bạn không có quyền sửa");
            }

        });

        btXoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Admin.currentAdmin.getRole() == ConstAdmin.role.get("giamDoc") || Admin.currentAdmin.getRole() == ConstAdmin.role.get("quanKho")) {
                    // fr.setVisible(false)
                    int x = tb.getSelectedRow();
                    if (x != -1) {
                        String idProduct =(String) tb.getValueAt(x, 0);
                        CouponController.xoaMa(idProduct);
                        DanhMucHangHoa.dispose();
                        DanhMucHangHoa.getInstance();
                    } else JOptionPane.showMessageDialog(frDanhMucHangHoa, "Chọn 1 hàng để xoas");
                } else
                    JOptionPane.showMessageDialog(frDanhMucHangHoa, "Bạn không có quyền xoas");
            }

        });
        btTimKiem.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent event) {

            }

            @Override
            public void keyReleased(KeyEvent event) {
                String o = btTimKiem.getText();


                tb.setModel(HangHoaController.getTableHangHoa(o));
                tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                tb.getColumnModel().getColumn(0).setPreferredWidth(80);
                tb.getColumnModel().getColumn(1).setPreferredWidth(200);
                tb.getColumnModel().getColumn(2).setPreferredWidth(69);
                tb.getColumnModel().getColumn(3).setPreferredWidth(155);
                tb.getColumnModel().getColumn(4).setPreferredWidth(430);

            }

            @Override
            public void keyPressed(KeyEvent event) {

            }
        });
        frDanhMucHangHoa.setLocationRelativeTo(null);
        frDanhMucHangHoa.setResizable(false);
        frDanhMucHangHoa.setVisible(true);
    }


}
