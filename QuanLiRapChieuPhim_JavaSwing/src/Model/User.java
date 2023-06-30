package Model;

import DB.DBConnection;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.security.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * @author hieu
 */
public class User extends Person {

    private int point;
    private String publicKeyModulus;
    private String publicKeyExponent;

    public String getPublicKeyModulus() {
        return publicKeyModulus;
    }

    public String getPublicKeyExponent() {
        return publicKeyExponent;
    }


    public void setPublicKey(ArrayList<String> publicKey) {
        this.publicKeyModulus = publicKey.get(0);
        this.publicKeyExponent = publicKey.get(1);

    }

    private byte[] promotionCodes = new byte[224];
    private int total;
    private int totalHistory;

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setPoint(byte[] point) {
        this.point = 0;
        int exponential = 1;
        for (int i = point.length - 1; i >= 0; i--) {
            this.point += exponential * point[i];
            exponential *= 10;
        }


    }

    public ArrayList<String> getPromotionCodes() throws UnsupportedEncodingException {
        ArrayList<String> list = new ArrayList<String>();
        list.add("--Chọn mã--");
        byte[] temp = new byte[8];

        for (int i = 0; i < 224; i += 8) {
            if (promotionCodes[i] != 0x00) {
                System.arraycopy(promotionCodes, i, temp, 0, 8);
                list.add(new String(temp, "UTF-8") + "" + (i / 8));
            }
        }
        return list;
    }

    public void setPromotionCodes(byte[] promotionCodes) {
        this.promotionCodes = promotionCodes;
    }

    public void removePromotionCodes(int index) throws UnsupportedEncodingException {
        System.arraycopy(new byte[8], 0, promotionCodes, index * 8, 8);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void napTien(int money) {
        this.total += money;
    }

    public void setTotal(byte[] total) {
        this.total = 0;
        int exponential = 1;
        for (int i = total.length - 1; i >= 0; i--) {
            this.total += exponential * total[i];
            exponential *= 10;
        }
    }

    public int getTotalHistory() {
        return totalHistory;
    }

    public void setTotalHistory(byte[] totalHistory) {
        this.totalHistory = 0;
        int exponential = 1;
        for (int i = totalHistory.length - 1; i >= 0; i--) {
            this.totalHistory += exponential * totalHistory[i];
            exponential *= 10;
        }
    }

    public void setTotalHistory(int totalHistory) {
        this.totalHistory = totalHistory;
    }

    private BufferedImage bufferAvatar;
    private String typeAvatar;
    public static User currentUser;

    public User(int id, String fullname, String birthday, boolean gender, String CMND, String phone, String address, BufferedImage bufferAvatar, String typeAvatar) {
        this.id = id;
        this.birthday = birthday;
        this.fullname = fullname;
        this.gender = gender;
        this.phone = phone;
        this.CMND = CMND;
        this.address = address;
        this.bufferAvatar = bufferAvatar;
        this.typeAvatar = typeAvatar;
    }

    public static User login(String username, String password) {
        return null;
    }

    ;

    public boolean changePassword(String newPassword) {

        return false;
    }

    ;

    public User(byte[] bufId, byte[] bufHoten, byte[] ngaySinh, byte gioiTinh, byte[] bufDiachi, byte[] bufCMND, byte[] bufSDT, BufferedImage bufAvatar) {


        this.fullname = new String(bufHoten);
        this.id = Integer.parseInt(new String(bufId));
        ;


        this.birthday = new String(ngaySinh);
        this.address = new String(bufDiachi);
        this.phone = new String(bufSDT);
        this.gender = (gioiTinh == (byte) 0x49);
        this.CMND = new String(bufCMND);
        this.bufferAvatar = bufAvatar;


    }

    public String getTypeAvatar() {
        return typeAvatar;
    }

    public void setTypeAvatar(String typeAvatar) {
        this.typeAvatar = typeAvatar;
    }

    public BufferedImage getBufferAvatar() {
        return bufferAvatar;
    }


    public boolean checkPassword(String newPassword) {

        return false;
    }

    ;

    public void getInfoDatabase() throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "Select * from users_key where id = ?";
        PreparedStatement st = conn.prepareStatement(query);
        System.out.println(id);
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            publicKeyModulus = rs.getString("public_key_modulus");
            publicKeyExponent = rs.getString("public_key_exponent");
        }


    }

    public void setBufferAvatar(BufferedImage bufferAvatar) {
        this.bufferAvatar = bufferAvatar;
    }

    public void saveDatabase() throws SQLException {

        Connection conn = DBConnection.getConnection();
        String query = "INSERT INTO users_key VALUES (?,?, ?);";
        PreparedStatement st = conn.prepareStatement(query);
        st.setInt(1, id);
        st.setString(2, publicKeyModulus);
        st.setString(3, publicKeyExponent);
        st.executeUpdate();
    }

    public void updateUserMarket(Invoice invoice) {

        //them Ma khuye mai
//        ArrayList<String> list = invoice.getMaKhuyenmaiDonHang();
//        int soluong = list.size();
//        for (int j = 0; j < soluong; j++) {
//            byte[] temp = list.get(j).getBytes();
//            for (int i = 0; i < 224; i += 8) {
//                System.out.println(promotionCodes[i]);
//                if (promotionCodes[i] == 0) {
//                    System.arraycopy(temp, 0, promotionCodes, i, 8);
//                    break;
//                }
//            }
//        }
//        // Xoas ma khuyen mai
//        int index = invoice.getIndexMaGiamGia();
//        if (index < 28) {
//            for (int i = 8 * index; i < 8 * index + 8; i++) {
//                promotionCodes[i] = 0;
//            }
//
//        }
        // Tang tien
        totalHistory += invoice.getIntoMoney();
        total -= invoice.getIntoMoney();
        point += (int) invoice.getTotal() / 1000;
        point -= invoice.getDiem();
    }

    public byte[] encodeInfo() throws IOException {
        int lenId = String.valueOf(id).length();
        int lenFullname = fullname.length();
        int lenBirthday = birthday.length();

        int lenPhone = phone.length();
        int lenCMND = CMND.length();
        int lenAddress = address.length();


        ByteArrayOutputStream bos = new ByteArrayOutputStream();


        ImageIO.write(bufferAvatar, typeAvatar, bos);

        byte[] napanh = bos.toByteArray();

        byte[] result = new byte[lenId + lenFullname + lenBirthday + lenPhone + lenCMND + lenAddress + napanh.length + 8];
        System.arraycopy(String.valueOf(id).getBytes(), 0, result, 0, lenId);

        result[lenId] = (byte) 1;

        System.arraycopy(this.fullname.getBytes(), 0, result, lenId + 1, lenFullname);

        result[lenId + 1 + lenFullname] = (byte) 1;

        System.arraycopy(this.birthday.getBytes(), 0, result, lenFullname + 1 + lenId + 1, lenBirthday);
        result[lenId + 1 + lenFullname + lenBirthday + 1] = (byte) 0x01;
        result[lenId + 1 + lenFullname + lenBirthday + 2] = this.gender ? (byte) 0x49 : (byte) 0x48;

        result[lenId + 1 + lenFullname + lenBirthday + 3] = (byte) 0x01;

        System.arraycopy(this.phone.getBytes(), 0, result, lenFullname + lenBirthday + 4 + lenId + 1, lenPhone);

        result[lenId + 1 + lenFullname + lenBirthday + lenPhone + 4] = (byte) 0x01;
        System.arraycopy(this.CMND.getBytes(), 0, result, lenFullname + lenBirthday + lenPhone + 5 + lenId + 1, lenCMND);
        result[lenId + 1 + lenFullname + lenBirthday + lenPhone + lenCMND + 5] = (byte) 0x01;
        System.arraycopy(this.address.getBytes(), 0, result, lenFullname + lenBirthday + lenPhone + lenCMND + 6 + lenId + 1, lenAddress);
        result[lenId + 1 + lenFullname + lenBirthday + lenPhone + lenCMND + lenAddress + 6] = (byte) 0x01;
        System.arraycopy(napanh, 0, result, lenFullname + lenBirthday + lenPhone + lenCMND + lenAddress + 7 + lenId + 1, napanh.length);
        return result;
    }

    public byte[] encodeInfoNotImage() throws IOException {
        int lenId = String.valueOf(id).length();
        int lenFullname = fullname.length();
        int lenBirthday = birthday.length();

        int lenPhone = phone.length();
        int lenCMND = CMND.length();
        int lenAddress = address.length();


        byte[] result = new byte[lenId + lenFullname + lenBirthday + lenPhone + lenCMND + lenAddress + 7];
        System.arraycopy(String.valueOf(id).getBytes(), 0, result, 0, lenId);

        result[lenId] = (byte) 1;

        System.arraycopy(this.fullname.getBytes(), 0, result, lenId + 1, lenFullname);

        result[lenId + 1 + lenFullname] = (byte) 1;

        System.arraycopy(this.birthday.getBytes(), 0, result, lenFullname + 1 + lenId + 1, lenBirthday);
        result[lenId + 1 + lenFullname + lenBirthday + 1] = (byte) 0x01;
        result[lenId + 1 + lenFullname + lenBirthday + 2] = this.gender ? (byte) 0x49 : (byte) 0x48;

        result[lenId + 1 + lenFullname + lenBirthday + 3] = (byte) 0x01;

        System.arraycopy(this.phone.getBytes(), 0, result, lenFullname + lenBirthday + 4 + lenId + 1, lenPhone);

        result[lenId + 1 + lenFullname + lenBirthday + lenPhone + 4] = (byte) 0x01;
        System.arraycopy(this.CMND.getBytes(), 0, result, lenFullname + lenBirthday + lenPhone + 5 + lenId + 1, lenCMND);
        result[lenId + 1 + lenFullname + lenBirthday + lenPhone + lenCMND + 5] = (byte) 0x01;
        System.arraycopy(this.address.getBytes(), 0, result, lenFullname + lenBirthday + lenPhone + lenCMND + 6 + lenId + 1, lenAddress);
        return result;
    }

    public byte[] encodeImage() throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();


        ImageIO.write(bufferAvatar, typeAvatar, bos);

        byte[] napanh = bos.toByteArray();


        return napanh;
    }
}

