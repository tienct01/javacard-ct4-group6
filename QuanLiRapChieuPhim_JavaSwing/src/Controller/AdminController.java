/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import javax.swing.*;
//import View.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Model.Admin;
import View.Login;
import View.FormMain;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pc
 */
public class AdminController {
      public static void login(String taiKhoan,String matKhau)
    {
       if (Admin.login(taiKhoan, matKhau)) {
            Login.dispose();
            FormMain.getInstance();
       } else {
       JOptionPane.showMessageDialog(null,"Dang nhap thất bại");}
    }
}
