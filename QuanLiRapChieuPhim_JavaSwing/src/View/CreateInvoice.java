/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Constants.AUTH_CONSTANTS;
import Constants.ConstAdmin;
import Constants.ISO7816;
import Controller.CouponController;
import Controller.HostAppHelper;
import Controller.InvoiceController;
import Model.*;
import controller.HangHoaController;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.CardException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author pc
 */
public class CreateInvoice {

    public static CreateInvoice getInstance() throws UnsupportedEncodingException {
        instance = new CreateInvoice();
        return instance;
    }

    public static void dispose() {
        if (instance != null) {
            instance.fr.dispose();
            instance = null;
        }
    }

    public CreateInvoice() throws UnsupportedEncodingException {

//        this.mainPns.put("selectTypeAccount", ContainerCreateAccount.selectTypeAccount());
        try {
            if (HostAppHelper.getInstance().selectAuthenApplet()) try {
                enterPincode();
            } catch (IOException ex) {
                Logger.getLogger(ContainerCreateAccount.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (CardException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi kết nối");
        }
    }

    private JPasswordField tfEnterPin;
    private Invoice invoice;
    private JButton enterPin;
    Vector invoiceDetail = new Vector();
    Vector column = new Vector();
    private JPanel pn;
    private User user;
    private JTable tb, tbInvoice;
    private JLabel lbMaKhachHang;
    private JLabel lbTenKhachHang;
    private JLabel lbSoDu;
    private JLabel lbDiem;
    private JLabel lbListMaGiamGia;
    private JLabel lbMotaMaGiamGia;
    private JComboBox cbListMaGiamGia;
    private JLabel tfMaKhachHang;
    private JLabel tfTenKhachHang;
    private JLabel tfSoDu;
    private JLabel tfDiem;
    private JLabel lbTongCong;
    private JLabel tfTongCong;

    private JLabel lbThanhTien;
    private JLabel lbUseMaGiamGia;
    private JLabel tfUseMaGiamGia;
    private JLabel tfThanhTien;
    private JLabel lbUseDiem;
    private JLabel lbUseDiemValue;
    private JTextField tfUseDiem;
    private JButton btThem;
    private JButton btThanhToan;
    private JTextField tfSearch;

    public void enterPincode() throws UnsupportedEncodingException {
        CreateInvoice thisCreateInvoice = this;
        if (User.currentUser != null) {
            thisCreateInvoice.user = User.currentUser;
            invoice = new Invoice(thisCreateInvoice.user);
            showCreateInvoice();
            return;
        }
        fr1 = new JFrame("Nhập mã pin");

        pn = new JPanel();
        fr1.add(pn);
        pn.setBounds(2, 2, 402, 200);
        pn.setLayout(null);
        tfEnterPin = new JPasswordField();
        pn.add(tfEnterPin);
        tfEnterPin.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N


        tfEnterPin.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tfEnterPin.setBounds(25, 30, 350, 40);
        enterPin = new JButton("Ok");
        pn.add(enterPin);

        enterPin.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N

        enterPin.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        enterPin.setBounds(153, 102, 100, 40);

        enterPin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //
                char[] pinText = tfEnterPin.getPassword();
                try {
                    String check = HostAppHelper.getInstance().enterPin(pinText);

                    switch (check) {
                        case ISO7816.SW_NO_ERROR:
//                    // verify success
                            boolean info = false;
                            try {
                                try {
                                    info = HostAppHelper.getInstance().getInfomation();
                                } catch (SQLException ex) {
                                    Logger.getLogger(CreateInvoice.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(ContainerCreateAccount.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (!info) {
                                fr1.dispose();
                                ContainerCreateAccount.getInstanceUser();
                            } else {
                                fr1.dispose();
                                thisCreateInvoice.user = User.currentUser;
                                invoice = new Invoice(thisCreateInvoice.user);
                                try {
                                    showCreateInvoice();
                                } catch (UnsupportedEncodingException ex) {
                                    Logger.getLogger(CreateInvoice.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            break;

                        case AUTH_CONSTANTS.SW_WRONG_PIN_LEN:
                            JOptionPane.showMessageDialog(null, "Vui lòng nhập đúng 6 ký tự mã PIN");
                            break;
                        case AUTH_CONSTANTS.SW_OVER_TRY_TIMES:
                            JOptionPane.showMessageDialog(null, "Qúa số lần thử. Tài khoản đã bị khóa !");
                            break;
                        case AUTH_CONSTANTS.SW_VERIFICATION_FAILED:
                            JOptionPane.showMessageDialog(null, "Sai mã PIN");
                            break;
                    }
                } catch (CardException ex) {
                    JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi");
                }
            }
        });
        fr1.setSize(406, 244);
        fr1.setLocationRelativeTo(null);
        fr1.setResizable(false);
        fr1.setVisible(true);


//        return selectTypeAccountPn;
    }

    private static CreateInvoice instance;
    Font font = new Font("Serif", Font.PLAIN, 13);
    JFrame fr = new JFrame("Lập hóa đơn");
    JFrame fr1 = new JFrame("Nhập mã pin");
    JLabel lb1 = new JLabel("LẬP HÓA ĐƠN", SwingConstants.CENTER);
    JPanel pnInvoiceInfo = new JPanel();
    JPanel pnCardInfo = new JPanel();
    JLabel lbNgay, ipNgay;

    public void showCreateInvoice() throws UnsupportedEncodingException {

        lb1.setBounds(0, 10, 1000, 40);
        lb1.setFont(new Font("Serif", Font.PLAIN, 30));
        pnInvoiceInfo.setBounds(0, 40, 1000, 400);
        pnInvoiceInfo.setLayout(null);
        pnCardInfo.setLayout(null);
        pnCardInfo.setBounds(500, 340, 500, 300);

        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-YYYY");
        String dateString = formatter.format(date);
        ipNgay = new JLabel(dateString);
        lbNgay = new JLabel("Ngày: ");
        lbNgay.setBounds(10, 10, 60, 30);
        ipNgay.setBounds(50, 10, 120, 30);


        // Table
        tb = new JTable(HangHoaController.getTableHangHoaOptimize(""));
        JScrollPane sp = new JScrollPane(tb);
        sp.setBounds(10, 70, 980, 200);
        tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tb.getColumnModel().getColumn(0).setPreferredWidth(70);
        tb.getColumnModel().getColumn(1).setPreferredWidth(150);
        tb.getColumnModel().getColumn(2).setPreferredWidth(193);
        tb.getColumnModel().getColumn(3).setPreferredWidth(100);
        tb.getColumnModel().getColumn(4).setPreferredWidth(130);
        tb.getColumnModel().getColumn(5).setPreferredWidth(130);
        tb.getColumnModel().getColumn(6).setPreferredWidth(130);
        tb.getColumnModel().getColumn(6).setPreferredWidth(130);
        tfSearch = new JTextField("Tìm kiếm");
        tfSearch.setForeground(Color.GRAY);
        tfSearch.setBounds(10, 285, 365, 30);
        btThem = new JButton("Thêm");
        btThem.setBounds(420, 420, 80, 30);
        //
        tbInvoice = new JTable();

        column.add("Tên Phim");
        column.add("Số lượng");
        column.add("Đơn Giá");
        column.add("Thành Tiền");

        tbInvoice.setModel(new DefaultTableModel(invoiceDetail, column));

        tbInvoice.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbInvoice.getColumnModel().getColumn(0).setPreferredWidth(150);
        tbInvoice.getColumnModel().getColumn(1).setPreferredWidth(80);
        tbInvoice.getColumnModel().getColumn(2).setPreferredWidth(80);
        tbInvoice.getColumnModel().getColumn(3).setPreferredWidth(80);


        JScrollPane sp1 = new JScrollPane(tbInvoice);
        sp1.setBounds(10, 340, 395, 230);
        fr.add(sp1);

        pnInvoiceInfo.add(lbNgay);
        pnInvoiceInfo.add(ipNgay);
        fr.add(btThem);
//        pnInvoiceInfo.add(tfSearch);
        pnInvoiceInfo.add(sp);

        lbMaKhachHang = new JLabel("Mã khách hàng :");
        lbTenKhachHang = new JLabel("Tên khách hàng :");
        lbSoDu = new JLabel("Số dư :");
        lbDiem = new JLabel("Điểm :");
        lbListMaGiamGia = new JLabel("Chọn mã giảm giá :");
        lbMotaMaGiamGia = new JLabel("");
        lbMaKhachHang.setBounds(30, 50, 110, 30);
        lbMotaMaGiamGia.setBounds(30, 220, 220, 100);
        lbTenKhachHang.setBounds(30, 90, 110, 30);
        lbSoDu.setBounds(30, 130, 110, 30);
        lbDiem.setBounds(30, 170, 110, 30);
        lbListMaGiamGia.setBounds(30, 210, 110, 30);


        tfMaKhachHang = new JLabel(String.valueOf(invoice.getUser().getId()));
        tfTenKhachHang = new JLabel(invoice.getUser().getFullname());
        tfSoDu = new JLabel(String.valueOf(invoice.getUser().getTotal()));
        tfSoDu = new JLabel(String.valueOf(User.currentUser.getTotal()));
        tfDiem = new JLabel(String.valueOf(invoice.getUser().getPoint()));
        cbListMaGiamGia = new JComboBox(CouponController.getIdKhuyenMai().toArray());


        tfMaKhachHang.setBounds(140, 50, 110, 30);
        tfTenKhachHang.setBounds(140, 90, 110, 30);
        tfSoDu.setBounds(140, 130, 110, 30);
        tfDiem.setBounds(140, 170, 110, 30);
        cbListMaGiamGia.setBounds(140, 210, 110, 30);

        pnCardInfo.add(lbMaKhachHang);
        pnCardInfo.add(cbListMaGiamGia);
        pnCardInfo.add(lbListMaGiamGia);
        pnCardInfo.add(lbMotaMaGiamGia);
        pnCardInfo.add(lbTenKhachHang);
        pnCardInfo.add(lbSoDu);
        pnCardInfo.add(lbDiem);
        pnCardInfo.add(tfMaKhachHang);
        pnCardInfo.add(tfTenKhachHang);
        pnCardInfo.add(tfSoDu);
        pnCardInfo.add(tfDiem);

//        lbTongCong = new JLabel("Tổng cộng :");
//        lbTongCong.setBounds(750, 580, 110, 30);
//        fr.add(lbTongCong);
//
//        tfTongCong = new JLabel("0");
//        tfTongCong.setBounds(860, 580, 110, 30);
//        fr.add(tfTongCong);

        lbUseDiem = new JLabel("Tổng cộng :");
        lbUseDiem.setBounds(750, 590, 110, 30);
        fr.add(lbUseDiem);

        lbUseDiemValue = new JLabel("0");
        lbUseDiemValue.setBounds(860, 590, 80, 30);
        fr.add(lbUseDiemValue);

        lbUseMaGiamGia = new JLabel("Giảm:");
        lbUseMaGiamGia.setBounds(750, 620, 110, 30);
        fr.add(lbUseMaGiamGia);
        tfUseMaGiamGia = new JLabel("0");
        tfUseMaGiamGia.setBounds(860, 620, 120, 30);
        fr.add(tfUseMaGiamGia);

        lbThanhTien = new JLabel("Thành tiền:");
        lbThanhTien.setBounds(750, 650, 110, 30);
        fr.add(lbThanhTien);
        tfThanhTien = new JLabel("0");
        tfThanhTien.setBounds(860, 650, 80, 30);
        fr.add(tfThanhTien);
        btThanhToan = new JButton("Thanh toán");
        btThanhToan.setBounds(860, 690, 120, 30);
        fr.add(btThanhToan);


        fr.setSize(1020, 800);
        fr.setLayout(null);
        fr.add(lb1);
        fr.add(pnInvoiceInfo);
        fr.add(pnCardInfo);

        fr.setLocationRelativeTo(null);
        fr.setResizable(false);
        fr.setVisible(true);

        tfSearch.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (tfSearch.getText().equals("Tìm kiếm")) {
                    tfSearch.setText("");
                    tfSearch.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (tfSearch.getText().isEmpty()) {
                    tfSearch.setForeground(Color.GRAY);
                    tfSearch.setText("Tìm kiếm");
                }
            }
        });

        tfSearch.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent event) {

            }

            @Override
            public void keyReleased(KeyEvent event) {
                String o = tfSearch.getText();


                tb.setModel(HangHoaController.getTableHangHoaOptimize(o));
                tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                tb.getColumnModel().getColumn(0).setPreferredWidth(30);
                tb.getColumnModel().getColumn(1).setPreferredWidth(180);
                tb.getColumnModel().getColumn(2).setPreferredWidth(80);
                tb.getColumnModel().getColumn(2).setPreferredWidth(90);

            }

            @Override
            public void keyPressed(KeyEvent event) {

            }
        });

        cbListMaGiamGia.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getItem().toString().equals("--Chọn mã--")) {
                    tfUseMaGiamGia.setText("0");
                    lbMotaMaGiamGia.setText("");
                    invoice.setCoupon(null);
                    invoice.setDiem(0);
                    invoice.setIndexMaGiamGia(0x60);

                    return;
                }

                String value = e.getItem().toString();
                System.out.println(value);
                try {
                    invoice.setCoupon(CouponController.find(value));
                    if (invoice.getCoupon() != null) {
                        tfUseMaGiamGia.setText(String.valueOf(invoice.getCoupon().getGiaTri())+invoice.getCoupon().getDonVi());
                        invoice.setDiem(invoice.getCoupon().getDiem());
                        lbMotaMaGiamGia.setText("<html>Mô tả: " + invoice.getCoupon().getMoTa() + "</html>");
                        tfThanhTien.setText(String.valueOf(invoice.getIntoMoney()));
                    } else {
                        JOptionPane.showMessageDialog(fr, "Mã giảm giá không tồn tại");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(CreateInvoice.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        btThem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = tb.getSelectedRow();
                int idProduct = Integer.parseInt((String) tb.getValueAt(x, 0));
                Movie m = null;
                try {
                    m = HangHoaController.find(idProduct);
                } catch (SQLException ex) {
                    Logger.getLogger(CreateInvoice.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (m == null) JOptionPane.showMessageDialog(fr, "Sản phẩm không tồn tại");
                else {
//                    invoice.addDiem(m.getDiem());
//                    tfUseDiem.setText(Integer.toString(invoice.getDiem()));
                    ArrayList<Movie> movies = invoice.getProducts();
                    int leng = movies.size();
                    boolean isExist = false;
                    for (int i = 0; i < leng; i++) {
                        Movie xy = movies.get(i);
                        if (xy.getCurrentQuantity() == 0) return;
                        if (xy.getId() == m.getId()) {
                            xy.incCurrentQuantity(1);
                            invoiceDetail.set(i, xy.getVectorDetail());
                            isExist = true;
                        }
                    }
                    if (!isExist) {
                        movies.add(m);
                        m.incCurrentQuantity(1);
                        if (m.getCurrentQuantity() == 0) return;

                        //            k=k+1;
                        invoiceDetail.add(m.getVectorDetail());
                    }
                    invoice.addTotal(m.getPrice());
                    //            tb.setModel(new DefaultTableModel(data, column));
                    tbInvoice.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    tbInvoice.getColumnModel().getColumn(0).setPreferredWidth(150);
                    tbInvoice.getColumnModel().getColumn(1).setPreferredWidth(80);
                    tbInvoice.getColumnModel().getColumn(2).setPreferredWidth(80);
                    tbInvoice.getColumnModel().getColumn(3).setPreferredWidth(80);

                    System.out.println("test" + invoice.getCoupon());
                    lbUseDiemValue.setText(String.valueOf(invoice.getTotal()));
                    tfThanhTien.setText(String.valueOf(invoice.getIntoMoney()));
                }
            }
        });

        btThanhToan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean verify = false;
                try {
                    try {
                        verify = HostAppHelper.getInstance().VerifyRsa();
                    } catch (SQLException ex) {
                        Logger.getLogger(CreateInvoice.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (!verify) {
                        JOptionPane.showMessageDialog(null, "Xác thực thất bại");
                        return;
                    }
                    boolean isSuccess = InvoiceController.thanhToan(invoice);
                    if (isSuccess) {
                        fr.dispose();
                    }
                } catch (CardException ex) {
                    Logger.getLogger(CreateInvoice.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }


}
