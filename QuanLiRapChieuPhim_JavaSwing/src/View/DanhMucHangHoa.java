/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Constants.ConstAdmin;
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
public class DanhMucHangHoa {

    public static DanhMucHangHoa getInstance() {
        instance = new DanhMucHangHoa();
        return instance;
    }

    public static void dispose() {
        if (instance != null) {
            instance.frDanhMucHangHoa.dispose();
            instance = null;
        }
    }

    public DanhMucHangHoa() {

//        this.mainPns.put("selectTypeAccount", ContainerCreateAccount.selectTypeAccount());
        showDanhMucHangHoa();
    }

    private static DanhMucHangHoa instance;
    Font font = new Font("Serif", Font.PLAIN, 13);
    JButton btThem = new JButton("Thêm");
    JButton btXoa = new JButton("Xóa");
    JButton btSua = new JButton("Sửa");
    JButton btThemHang = new JButton("Thêm hàng");
    JTextField btTimKiem = new JTextField();
    JFrame frDanhMucHangHoa = new JFrame("DANH MỤC PHIM");
    JLabel lb1 = new JLabel("DANH MỤC PHIM");
    String x = "";

    public void showDanhMucHangHoa() {
        JTable tb = new JTable(HangHoaController.getTableHangHoa(""));
        JScrollPane sp = new JScrollPane(tb);
        frDanhMucHangHoa.setSize(1000, 550);
        frDanhMucHangHoa.setLayout(null);
        lb1.setBounds(170, 10, 450, 40);
        lb1.setFont(new Font("Serif", Font.PLAIN, 30));
        btThem.setFont(font);
        btSua.setFont(font);
        btThemHang.setFont(font);
        btXoa.setFont(font);
        btTimKiem.setFont(font);
        tb.setFont(font);
        sp.setBounds(25, 55, 691 + 252, 400);
        tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tb.getColumnModel().getColumn(0).setPreferredWidth(70);
        tb.getColumnModel().getColumn(1).setPreferredWidth(200);
        tb.getColumnModel().getColumn(2).setPreferredWidth(80);
        tb.getColumnModel().getColumn(3).setPreferredWidth(100);
        tb.getColumnModel().getColumn(4).setPreferredWidth(100);
        tb.getColumnModel().getColumn(5).setPreferredWidth(130);
        tb.getColumnModel().getColumn(6).setPreferredWidth(130);
        tb.getColumnModel().getColumn(7).setPreferredWidth(130);

        btTimKiem.setBounds(25, 460, 105, 30);
        btThemHang.setBounds(545, 460, 100, 25);
        btXoa.setBounds(675, 460, 70, 25);
        btSua.setBounds(775, 460, 70, 25);
        btThem.setBounds(875, 460, 70, 25);
        frDanhMucHangHoa.add(sp);
        frDanhMucHangHoa.add(lb1);
        frDanhMucHangHoa.add(btThem);
        frDanhMucHangHoa.add(btSua);
//        frDanhMucHangHoa.add(btThemHang);
        frDanhMucHangHoa.add(btXoa);
        frDanhMucHangHoa.add(btTimKiem);
        btThem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Admin.currentAdmin.getRole() == ConstAdmin.role.get("giamDoc") || Admin.currentAdmin.getRole() == ConstAdmin.role.get("quanKho")) {
                    // fr.setVisible(false)

                    ThemHang.getInstance();
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
                        int idProduct = Integer.parseInt((String) tb.getValueAt(x, 0));
                        try {
                            SuaHang.getInstance(idProduct);
                        } catch (SQLException ex) {
                            Logger.getLogger(DanhMucHangHoa.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else JOptionPane.showMessageDialog(frDanhMucHangHoa, "Chọn 1 hàng để sửa");
                } else
                    JOptionPane.showMessageDialog(frDanhMucHangHoa, "Bạn không có quyền sửa");
            }

        });
        btThemHang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Admin.currentAdmin.getRole() == ConstAdmin.role.get("giamDoc") || Admin.currentAdmin.getRole() == ConstAdmin.role.get("quanKho")) {
                    // fr.setVisible(false)
                    int x = tb.getSelectedRow();
                    if (x != -1) {
                        int idProduct = Integer.parseInt((String) tb.getValueAt(x, 0));
                        try {
                            NhapHang.getInstance(idProduct);
                        } catch (SQLException ex) {
                            Logger.getLogger(DanhMucHangHoa.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else JOptionPane.showMessageDialog(frDanhMucHangHoa, "Chọn 1 hàng để thêm");
                } else
                    JOptionPane.showMessageDialog(frDanhMucHangHoa, "Bạn không có quyền thêm");
            }

        });
        btXoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Admin.currentAdmin.getRole() == ConstAdmin.role.get("giamDoc") || Admin.currentAdmin.getRole() == ConstAdmin.role.get("quanKho")) {
                    // fr.setVisible(false)
                    int x = tb.getSelectedRow();
                    if (x != -1) {
                        int idProduct = Integer.parseInt((String) tb.getValueAt(x, 0));
                        //                            SuaHang.getInstance(idProduct);
                        HangHoaController.xoaHang(idProduct);
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
                tb.getColumnModel().getColumn(0).setPreferredWidth(70);
                tb.getColumnModel().getColumn(1).setPreferredWidth(200);
                tb.getColumnModel().getColumn(2).setPreferredWidth(69);
                tb.getColumnModel().getColumn(3).setPreferredWidth(100);
                tb.getColumnModel().getColumn(4).setPreferredWidth(100);
                tb.getColumnModel().getColumn(5).setPreferredWidth(100);
                tb.getColumnModel().getColumn(6).setPreferredWidth(100);
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
