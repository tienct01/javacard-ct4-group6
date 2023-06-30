/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DB.DBConnection;

import javax.swing.*;
//import View.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Admin;
import Model.Coupon;
import Model.Movie;
import View.Login;
import View.FormMain;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * @author pc
 */
public class CouponController {
    public static void xoaMa(String maHang) {
        Connection conn = DBConnection.getConnection();


        try {
            Statement st = conn.createStatement();


            //   int rs = st.executeUpdate("insert into hang values('" + maNV + "','" + hoTen +"','"+ngaySinh+"','"+gioiTinh+"',"+luong+","+0+",'"+diaChi+"','"+chucVu+"','"+soDienThoai+"')");
            int rs = st.executeUpdate("delete from promotion_code where id='" + maHang + "'");

        } catch (SQLException throwables) {
        }

    }

    public static boolean themMa(String maKhuyenMai, String giaTri, String donVi, String giaTriApDung, String moTa, String diem) {
        Connection conn = DBConnection.getConnection();

        if (maKhuyenMai.equals("") || giaTri.equals("") | donVi.equals("") || giaTriApDung.equals("")) {
            return false;
        }
        try {
            Statement st = conn.createStatement();


            //   int rs = st.executeUpdate("insert into hang values('" + maNV + "','" + hoTen +"','"+ngaySinh+"','"+gioiTinh+"',"+luong+","+0+",'"+diaChi+"','"+chucVu+"','"+soDienThoai+"')");
            int rs = st.executeUpdate("INSERT INTO `promotion_code` (`id`, `point`, `type`, `value`, `price`, `description`)"
                    + "VALUES ('" + maKhuyenMai + "', '" + diem + "', '" + donVi + "', '" + giaTri + "', '" + giaTriApDung + "', '" + moTa + "');");
            return true;
        } catch (SQLException throwables) {
            System.out.print("loi");
        }
        return false;
    }

    public static ArrayList<String> getIdKhuyenMai() {
        Connection conn = DBConnection.getConnection();
        ArrayList<String> ids = new ArrayList<>();
        ids.add("--Chọn mã--");
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select id from promotion_code");
            while (rs.next()) {
                ids.add(rs.getString("id"));
                System.out.println(rs.getString("id"));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public static Coupon find(String id) throws SQLException {
        Connection conn = DBConnection.getConnection();

        try {
            Statement st = conn.createStatement();

            System.out.println(id);

            ResultSet rs = st.executeQuery("select * from promotion_code where id = '" + id + "'");

            if (rs.next()) {
    //            int rs = st.executeUpdate("INSERT INTO `promotion_code` (`id`, `point`, `type`, `value`, `price`, `description`)"
                System.out.println("o kia");
                Coupon x = new Coupon(rs.getString("id"), rs.getInt("value"), rs.getString("type"), rs.getInt("price"),
                        rs.getString("description")
                );
                x.setDiem(rs.getInt("point"));
                conn.close();
                return x;
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean suaMaKhuyenMai(String maKhuyenMaiCu, String maKhuyenMaiMoi, String giaTri, String donVi, String giaTriApDung, String moTa, String diem) {
        Connection conn = DBConnection.getConnection();
        try {

//            int rs = st.executeUpdate("INSERT INTO `promotion_code` (`id`, `point`, `type`, `value`, `price`, `description`)"

            Statement st = conn.createStatement();
            String sql = "update " + "promotion_code"+ " set ";
            sql += "point=" + diem;
            sql += ",price=" + giaTriApDung;

            sql += ",value=" + giaTri;

            if (!maKhuyenMaiMoi.equals("")) {
                sql += ",id ='" + maKhuyenMaiMoi + "'";
            }
            if (!donVi.equals("")) {
                sql += ",type ='" + donVi + "'";
            }
            if (!moTa.equals("")) {
                sql += ",description ='" + moTa + "'";
            }
            sql += " where id='" + maKhuyenMaiCu + "'";
            int rs = st.executeUpdate(sql);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static DefaultTableModel getList(String x) {
        Connection conn = DBConnection.getConnection();
        Vector data = new Vector();
        Vector column = new Vector();
        column.add("ID");
        column.add("Giá trị khuyến mại");
        column.add("Đơn vị");
        column.add("Giá trị áp dụng");
        column.add("Mô tả");
        column.add("Đểm");

        try {

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from promotion_code where id like '" + x + "%'");
            while (rs.next()) {
                Vector record = new Vector();
                record.add(rs.getString("id"));
                record.add(rs.getString("value"));
                record.add(rs.getString("type"));
                record.add(rs.getString("price"));
                record.add(rs.getString("description"));
                record.add(rs.getInt("point"));
                data.add(record);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, column);
        return tableModel;
    }
}
