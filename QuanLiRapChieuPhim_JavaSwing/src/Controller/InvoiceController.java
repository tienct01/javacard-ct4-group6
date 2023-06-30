/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DB.DBConnection;
import Model.Invoice;
import Model.Product;
import Model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.smartcardio.CardException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * @author pc
 */
public class InvoiceController {
    public static boolean thanhToan(Invoice invoice) throws CardException {
        Connection conn = DBConnection.getConnection();


        try {
            Statement st = conn.createStatement();
            if (invoice.getDiem() > invoice.getUser().getPoint()) {
                JOptionPane.showMessageDialog(null, "Số điểm không khả dụng");
                return false;
            }

            if (invoice.getIntoMoney() > invoice.getUser().getTotal()) {
                JOptionPane.showMessageDialog(null, "Số dư không khả dụng");
                return false;
            }

            String maKhuyenMai = null;
            if (invoice.getCoupon() != null) {
                maKhuyenMai = invoice.getCoupon().getMaKhuyenMai();
                    if (invoice.getTotal() < invoice.getCoupon().getGiaTriApDung()) {
                        JOptionPane.showMessageDialog(null, "Đơn hàng phải lớn hơn " + invoice.getCoupon().getGiaTriApDung() + " VNĐ để có thể áp dụng mã giảm giá");
                        return false;
                    }
            }

//            int rs;
//            rs = st.executeUpdate("insert into donhang values(" + null + ",'" + invoice.getAdmin().getId() + "','" + invoice.getUser().getId() + "','" + invoice.getIntoMoney() + "','" + maKhuyenMai + "'," + invoice.getDiem() + ")");
//            ResultSet rs1 = st.executeQuery("select max(id) as id from donhang");
//            rs1.next();
//            int invoiceId = rs1.getInt("id");
//            for (int i = 0; i < invoice.getProducts().size(); i++) {
////                Product product = invoice.getProducts().get(i);
////                 rs = st.executeUpdate("insert into chitietdonhang values(" + null + ",'" + invoiceId +"','"+product.getMaHang()+"','"+product.getCurrentQuantity()+"',"+product.getDonGiaBan() + ")");
////                 rs = st.executeUpdate("update product set soLuong = " + (product.getSoLuong() - product.getCurrentQuantity()) + " where maHang ='" + product.getMaHang() + "'");
//            }
            boolean isUpdateCard = HostAppHelper.getInstance().updateMarket(invoice);
            if (!isUpdateCard) {
                JOptionPane.showMessageDialog(null, "Update thẻ thất bại");
                return false;
            }
            User.currentUser.updateUserMarket(invoice);
            JOptionPane.showMessageDialog(null, "Thanh toán thành công");
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Thanh toán thất bại");
            e.printStackTrace();
            return false;
        }
    }

    public static DefaultTableModel getTableHoaDon(String x) {
        Connection conn = DBConnection.getConnection();
        Vector data = new Vector();
        Vector column = new Vector();
        column.add("Mã HD");
        column.add("Mã NV");
        column.add("Mã KH");
        column.add("Ngày lập");
        column.add("Tổng HD");

        try {

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from hoadon where maHD like '" + x + "%'");
            while (rs.next()) {
                Vector record = new Vector();
                record.add(rs.getString("maHD"));
                record.add(rs.getString("maNV"));
                record.add(rs.getString("maKH"));
                record.add(rs.getString("ngaylapHD"));
                record.add(rs.getString("tongtien"));

                data.add(record);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, column);
        return tableModel;
    }


    public static void themCTHD(String maHD, String maHang, int soLuong, int donGia, int giamGia) {
        Connection conn = DBConnection.getConnection();


        try {
            Statement st = conn.createStatement();


            //   int rs = st.executeUpdate("insert into hang values('" + maNV + "','" + hoTen +"','"+ngaySinh+"','"+gioiTinh+"',"+luong+","+0+",'"+diaChi+"','"+chucVu+"','"+soDienThoai+"')");
            int rs = st.executeUpdate("insert into chitietHD  values('" + maHD + "','" + maHang + "'," + soLuong + "," + donGia + "," + giamGia + ")");

            ResultSet rs1 = st.executeQuery("select tonquay from hang where mahang = '" + maHang + "'");

            rs1.next();
            int x = rs1.getInt("tonquay");
            x = x - soLuong;

            rs = st.executeUpdate("update hang set tonquay='" + x + "'where mahang='" + maHang + "'");
        } catch (SQLException throwables) {
        }

    }

    public static String MaHoaDon() {
        Connection conn = DBConnection.getConnection();

        try {
            Statement st = conn.createStatement();
            //   int rs = st.executeUpdate("insert into nhanvien values('" + maNV + "','" + hoTen +"','"+ngaySinh+"','"+gioiTinh+"',"+luong+","+0+",'"+diaChi+"','"+chucVu+"','"+soDienThoai+"')");
            ResultSet rs = st.executeQuery("select*from hoadon order by mahd desc");

            rs.next();

            if (rs.getString("mahd").length() == 0) return "HD00000001";
            int x = Integer.parseInt((rs.getString("mahd")).substring(2));
            x++;
            String y = String.valueOf(x);

            for (int i = 8 - y.length(); i > 0; i--) {
                y = "0" + y;
            }

            return "HD" + y;


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "HD00000001";
    }
}
