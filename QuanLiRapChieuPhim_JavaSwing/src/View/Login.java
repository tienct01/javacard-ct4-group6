package View;

import controller.AdminController;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
/**
 *
 * @author pc
 */
public class Login {
   
    private static Login instance;

    public static Login getInstance() {
        if (instance == null) {
            instance = new Login();
        }
        return instance;
    }
    
    public static void dispose() {
       if (instance != null) {
            instance.frFormChinh.dispose();
            instance = null;
        } 
    }
    
     public Login() {
       
        initComponents();
    }
     
    private void initComponents() {
        
        
        frFormChinh=new JFrame("Đăng Nhập");
        pnDangNhap=new JPanel();
        lbTenDangNhap=new JLabel("Tên Đăng Nhập");
        lbMatKhau=new JLabel("Mật Khẩu");
        tfTenDangNhap=new JTextField(15);
        tfMatKhau=new JPasswordField(15);
        btDangNhap=new JButton("OK");
        font=new Font("Serif",Font.PLAIN,13);
        gbc=new GridBagConstraints();
        lbTenDangNhap.setFont(font);
        tfTenDangNhap.setFont(font);
        tfMatKhau.setFont(font);
        lbMatKhau.setFont(font);
        btDangNhap.setFont(font);
        
        pnDangNhap.setLayout(new GridBagLayout());
        
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=1;
        gbc.anchor = GridBagConstraints.WEST;
        pnDangNhap.add(lbTenDangNhap,gbc);
        
        gbc.gridx=1;
        gbc.gridy=0;
        gbc.gridwidth=2;
        pnDangNhap.add(tfTenDangNhap,gbc);
        
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.gridwidth=1;
        pnDangNhap.add(lbMatKhau,gbc);
        
        gbc.gridx=1;
        gbc.gridy=1;
        gbc.gridwidth=2;
        pnDangNhap.add(tfMatKhau,gbc);
        
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridwidth=1;
        gbc.gridx=1;
        gbc.gridy=2;
        pnDangNhap.add(new JLabel("  "),gbc);

        gbc.fill=GridBagConstraints.NONE;
        gbc.gridwidth=1;
        gbc.gridx=1;
        gbc.gridy=3;
        btDangNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                login(evt);
            }
        });
        pnDangNhap.add(btDangNhap,gbc);
        
        frFormChinh.setSize(300,180);
        frFormChinh.add(pnDangNhap);
        frFormChinh.setLocationRelativeTo(null);
        frFormChinh.setResizable(false);
        frFormChinh.setVisible(true);
        frFormChinh.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
    };
   
    private void login(ActionEvent e) {
        String TenDangNhap = tfTenDangNhap.getText();;
        String MatKhau = new String(tfMatKhau.getPassword());
        AdminController.login(TenDangNhap,MatKhau);

    }
    private JFrame frFormChinh;
    private JPanel pnDangNhap;
    private JLabel lbTenDangNhap;
    private JLabel lbMatKhau;

    private JTextField tfTenDangNhap;
    private JPasswordField tfMatKhau;
    private JButton btDangNhap;
    private Font font;
    private GridBagConstraints gbc;

}