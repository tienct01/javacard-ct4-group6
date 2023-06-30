/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DB.DBConnection;
import Model.Movie;
import Model.Product;

import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 * @author pc
 */
public class HangHoaController {

    private static String test = "movies JOIN price " +
            "on movies.id = price.movies_id " +
            "INNER JOIN schedules " +
            "on schedules.id = price.schedules_id";

    public static Movie find(int id) throws SQLException {
        Connection conn = DBConnection.getConnection();

        Statement st = conn.createStatement();
//        ResultSet rs = st.executeQuery("select * from product where maHang = '" + id + "'");
        ResultSet rs = st.executeQuery("SELECT * from " + test + " where movies.id = '" + id + "'");

        if (rs.next()) {

            Movie x = new Movie(rs.getInt("id"), rs.getString("name"),
                    rs.getString("description"), rs.getInt("quality"),
                    rs.getString("start_time"), rs.getString("end_time"),
                    rs.getInt("price"), rs.getInt("age_limit")
            );
            conn.close();
            return x;
        }
        conn.close();
        return null;
    }

    public static boolean themHang(Movie movie) {
        Connection conn = DBConnection.getConnection();

        if (movie.getName().equals("")) {
            return false;
        }

        try {

            String sqlSchedules = "INSERT INTO " + "schedules"  + " VALUES(NULL,?,?)";
            PreparedStatement stSchedules = conn.prepareStatement(sqlSchedules);
            stSchedules.setString(1, movie.getStartTime());
            stSchedules.setString(2, movie.getEndTime());

            stSchedules.addBatch();
            stSchedules.executeBatch();

            String sqlMovies = "INSERT INTO " + "movies"  + " VALUES(NULL,?,NULL,?,?,?)";
            PreparedStatement stMovies = conn.prepareStatement(sqlMovies);
            stMovies.setString(1, movie.getName());
            stMovies.setString(2, movie.getDescription());
            stMovies.setInt(3,  movie.getAgeLimit());
            stMovies.setInt(4, movie.getQuality());

            stMovies.addBatch();
            stMovies.executeBatch();

            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();
            ResultSet rsMovies = st1.executeQuery("SELECT * FROM movies ORDER BY ID DESC LIMIT 1");
            ResultSet rsSchedules = st2.executeQuery("SELECT * FROM schedules ORDER BY ID DESC LIMIT 1");

            if (rsMovies.next() && rsSchedules.next()) {
                int movieID = rsMovies.getInt("id");
                int scheduleId  = rsSchedules.getInt("id");

                String sqlPrice = "INSERT INTO " + "price"  + " VALUES(NULL,?,?,?)";
                PreparedStatement stPrice = conn.prepareStatement(sqlPrice);
                stPrice.setInt(1, movieID);
                stPrice.setInt(2, scheduleId);
                stPrice.setInt(3, movie.getPrice());

                stPrice.addBatch();
                stPrice.executeBatch();

                return true;
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        try {
//
//            String sqlSchedules = "INSERT INTO " + "schedules"  + " VALUES(NULL,?,?)";
//            String sqlMovie = "INSERT INTO " + "movies"  + " VALUES(NULL,?,?, NULL,?,?)";
//            String sqlPrice = "INSERT INTO " + "price"  + " VALUES(NULL,?,?, NULL,?,?)";
//            PreparedStatement st = conn.prepareStatement(sqlSchedules);
//            st.setString(1, "2023-10-23 22:41:40");
//            st.setString(2, "2023-10-23 23:41:40");
//            st.addBatch();
//            st.executeBatch();
//            return true;
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
        return false;
    }

    public static boolean suaHang(Movie movie) {
        Connection conn = DBConnection.getConnection();
        try {

            Statement st = conn.createStatement();
            String sql = "update " + test+ " set ";
            sql += "age_limit=" + movie.getAgeLimit();
            sql += ",price=" + movie.getPrice();

            sql += ",quality=" + movie.getQuality();

            if (!movie.getName().equals("")) {
                sql += ",name ='" + movie.getName() + "'";
            }
            if (!movie.getDescription().equals("")) {
                sql += ",description ='" + movie.getDescription() + "'";
            }
            if (!movie.getStartTime().equals("")) {
                sql += ",start_time ='" + movie.getStartTime() + "'";
            }
            if (!movie.getEndTime().equals("")) {
                sql += ",end_time ='" + movie.getEndTime() + "'";
            }


            sql += " where movies.id='" + movie.getId() + "'";
            int rs = st.executeUpdate(sql);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static void xoaHang(int id) {
        Connection conn = DBConnection.getConnection();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * from price where movies_id='" + id + "'");
            if (rs.next()) {
                int schedulesId = rs.getInt("schedules_id");
                int priceId  = rs.getInt("id");

                st.executeUpdate("delete from schedules" + " where id='" + schedulesId + "'");
                st.executeUpdate("delete from price" + " where id='" + priceId + "'");
                st.executeUpdate("delete from movies" + " where id='" + id + "'");
                System.out.println(schedulesId);
            }



            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static DefaultTableModel getTableHangHoa(String x) {
        Connection conn = DBConnection.getConnection();
        Vector data = new Vector();
        Vector column = new Vector();
        column.add("Ma Phim");
        column.add("Ten phim");
        column.add("Tóm tắt");
        column.add("Tuổi giới hạn");
        column.add("Giá");
        column.add("Giờ chiếu");
        column.add("Giờ kết thúc");
        column.add("Số lượng");
        try {

            Statement st = conn.createStatement();
//            ResultSet rs = st.executeQuery("select * from product where maHang like '" + x + "%'");
            ResultSet rs = st.executeQuery("SELECT * from " + test);
            while (rs.next()) {
                Vector record = new Vector();
                record.add(rs.getString("id"));
                record.add(rs.getString("name"));
                record.add(rs.getString("description"));
                record.add(rs.getString("age_limit"));
                record.add(rs.getString("price"));
                record.add(rs.getString("start_time"));
                record.add(rs.getString("end_time"));

                record.add(rs.getString("quality"));
//
//                record.add(rs.getString("giamGia") + " " + rs.getString("donviGiamGia"));


                data.add(record);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, column);
        return tableModel;
    }

    public static DefaultTableModel getTableHangHoaOptimize(String x) {
        Connection conn = DBConnection.getConnection();
        Vector data = new Vector();
        Vector column = new Vector();
        column.add("Ma Phim");
        column.add("Ten phim");
        column.add("Tóm tắt");
        column.add("Tuổi giới hạn");
        column.add("Giá");
        column.add("Giờ chiếu");
        column.add("Giờ kết thúc");
        column.add("Số lượng");
        try {

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from " + test+ " where movies.id like '" + x + "%' or movies.name like '" + x + "%'");
            while (rs.next()) {
                Vector record = new Vector();
                record.add(rs.getString("id"));
                record.add(rs.getString("name"));
                record.add(rs.getString("description"));
                record.add(rs.getString("age_limit"));
                record.add(rs.getString("price"));
                record.add(rs.getString("start_time"));
                record.add(rs.getString("end_time"));

                record.add(rs.getString("quality"));
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
