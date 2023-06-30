package Model;
import DB.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.security.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hieu
 */
public class Admin  extends Person{

    private String username;
    private int wage;
    private short role;
    public static Admin currentAdmin;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWage() {
        return wage;
    }

    public void setWage(int wage) {
        this.wage = wage;
    }

    public short getRole() {
        return role;
    }

    public void setRole(short role) {
        this.role = role;
    }
   
    public Admin(int id, String fullname, String username, short role, boolean gender, String phone, String address, int wage) {
        this.id = id;
        this.fullname = fullname;
        this.username = username;
        this.role = role;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.wage = wage;
    }
     public static boolean login(String username, String password) {
        Connection conn = DBConnection.getConnection();
        try {
           
            byte[] bytesOfMessage = password.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theMD5digest = md.digest(bytesOfMessage);
            String query = "select * from users where password=? and email=?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, password);
            st.setString(2, username);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Admin.currentAdmin = new Admin(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    (short)rs.getInt("role"),
                    (boolean)rs.getObject("gender"),
                    rs.getString("phone"),
                    "adsf",
                    100
                );
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
        }
        return false;
    };
    public boolean changePassword(String newPassword) {
        Connection conn = DBConnection.getConnection();
        try {
            byte[] bytesOfMessage = newPassword.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theMD5digest = md.digest(bytesOfMessage);
            String query = "update admin set password=? where id=?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, theMD5digest.toString());
            st.setInt(2, this.id);
             int rs = st.executeUpdate();
             if (rs > 0) {
                 return true;
             }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return false;
    };
    
     public boolean checkPassword(String password) {
        Connection conn = DBConnection.getConnection();
        try {
            byte[] bytesOfMessage = password.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theMD5digest = md.digest(bytesOfMessage);
            String query = "select * from admin where password=? and id=?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, theMD5digest.toString());
            st.setInt(2, this.id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            
        }
        return false;
    };
}

