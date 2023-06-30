/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;
import Constants.AUTH_CONSTANTS;
import Constants.ConstAdmin;
import Constants.ISO7816;
import javax.swing.*;
import Controller.HostAppHelper;
import Model.Admin;
import Model.User;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.smartcardio.CardException;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author hieu
 */
public class ViewCard {
    
    private static ViewCard instance;
    private JFrame fr;
    private JPanel pn;
    private JPanel pnTitle;
    private JPanel leftPn;
    private JButton selectAdmin;
    private JLabel titleInitCard;
    private JLabel lbFullname;
    private JLabel lbBirthDay;
    private JLabel lbCMND;
    private JLabel lbAddress;
    private JLabel lbPhone;
    private JLabel lbGender;
   
   
     private JPasswordField tfEnterPin;
       private JButton enterPin;
    private JLabel avatar;
    
    JRadioButton rbMale;
    JRadioButton rbFemale; 
    ButtonGroup bgGioiTinh;
    
    private JLabel tfFullname;
    private JLabel tfCMND;
    private JLabel tfAddress;
    private JLabel tfPhone;
    
     private JLabel tfBirthday;
    private JLabel tfGender;
    
    private JButton btnClose;
    private JButton btnChangePin;
    private JButton btnMoreInfo;

    private JButton btnChangeInfo;
    private JButton btnThanhToan;
    private JButton btnDangXuat;
     private JButton btnNapTien;
    
    private JComboBox day;
    private JComboBox month;
    private JComboBox year;
    private User user;
    
    private JButton selectUser;
   
     public static ViewCard getInstance() throws IOException {
        
            instance = new ViewCard();
       
        return instance;
    }
      public static ViewCard getInstance(boolean isCreate) throws IOException, CardException, SQLException {
        
            instance = new ViewCard(true);
       
        return instance;
    }
    public static void dispose() {
        
       if (instance != null) {
            instance.fr.dispose();
            instance = null;
        } 
    }
     public void enterPincode() {
          ViewCard thisViewCard = this;
         if (User.currentUser != null) {
            thisViewCard.user = User.currentUser;
            init();
            return;
         }
         fr=new JFrame("Nhập mã pin");
       
        pn = new JPanel();
        fr.add(pn);
        pn.setBounds(2, 2, 402, 200);
        pn.setLayout(null);
        tfEnterPin =new JPasswordField();
        pn.add(tfEnterPin);
        tfEnterPin.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        
       
        tfEnterPin.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tfEnterPin.setBounds(25, 30, 350,40);
        enterPin =new JButton("Ok");
        pn.add(enterPin);
       
        enterPin.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
      
        enterPin.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        enterPin.setBounds(153, 102, 100,40);
       
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
                    Logger.getLogger(ViewCard.class.getName()).log(Level.SEVERE, null, ex);
                }
                } catch (IOException ex) {
                    Logger.getLogger(ContainerCreateAccount.class.getName()).log(Level.SEVERE, null, ex);
                }
                    fr.dispose();
                    if (!info) {
                        ContainerCreateAccount.getInstanceUser();
                    } else {
                        
                        thisViewCard.user = User.currentUser;
                        init();
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
        fr.setSize(406,244);
        fr.setLocationRelativeTo(null);
        fr.setResizable(false);
        fr.setVisible(true);
        
       
//        return selectTypeAccountPn;
    }
    
    public ViewCard() throws IOException {
        try {
            if (HostAppHelper.getInstance().selectAuthenApplet()) {
                enterPincode();
            }
        } catch (CardException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi kết nối");
        }
        
    }
    public ViewCard(boolean isCreate) throws IOException, CardException, SQLException {
        boolean info = false;  
        try {
        info = HostAppHelper.getInstance().getInfomation();
     } catch (IOException ex) {
         Logger.getLogger(ContainerCreateAccount.class.getName()).log(Level.SEVERE, null, ex);
     } catch (CardException ex) {
           JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi");
        }
           
         if (!info) {
             ContainerCreateAccount.getInstanceUser();
         } else {
            
             this.user = User.currentUser;
             init();
         }
    }
  
    public void init() {
        fr=new JFrame("Thông tin thẻ");
        Font font=new Font("Serif",Font.PLAIN,20);
        pn = new JPanel();
        fr.add(pn);
        pn.setBounds(554, 62, 540, 540);
        pn.setLayout(null);
        
        leftPn = new JPanel();
        fr.add(leftPn);
        leftPn.setBounds(2, 62, 550, 520);
        leftPn.setLayout(null);
        
        pnTitle = new JPanel();
        fr.add(pnTitle);
        pnTitle.setBounds(0, 0, 1100, 40);
        pnTitle.setLayout(null);
        titleInitCard = new JLabel("THÔNG TIN THẺ", SwingConstants.CENTER);
        titleInitCard.setFont(new java.awt.Font("Times New Roman", 1, 28)); // NOI18N
        titleInitCard.setBounds(3, 0, 1100, 40);
        pnTitle.add(titleInitCard);
        
        lbFullname = new JLabel("Họ tên :", SwingConstants.LEFT);
        lbFullname.setBounds(20, 10, 100, 35);
        lbFullname.setFont(font);
        pn.add(lbFullname);
        
        lbBirthDay = new JLabel("Ngày Sinh :", SwingConstants.LEFT);
        lbBirthDay.setBounds(20, 80, 100, 35);
        lbBirthDay.setFont(font);
        pn.add(lbBirthDay);
        
        lbGender = new JLabel("Giới tính :", SwingConstants.LEFT);
        lbGender.setBounds(20, 150, 100, 35);
        lbGender.setFont(font);
        pn.add(lbGender);
        
        lbPhone = new JLabel("SĐT :", SwingConstants.LEFT);
        lbPhone.setBounds(20, 220, 100, 35);
        lbPhone.setFont(font);
        pn.add(lbPhone);
        
        lbCMND = new JLabel("CMND :", SwingConstants.LEFT);
        lbCMND.setBounds(20, 290, 100, 35);
        lbCMND.setFont(font);
        pn.add(lbCMND);   
        
        lbAddress = new JLabel("Địa chỉ :", SwingConstants.LEFT);
        lbAddress.setBounds(20, 360, 100, 35);
        lbAddress.setFont(font);
        pn.add(lbAddress); 
     
        tfFullname = new JLabel(user.getFullname());
        tfFullname.setBounds(150, 10, 300, 35);
        tfFullname.setFont(font);
        pn.add(tfFullname);
       
        tfBirthday = new JLabel(user.getBirthday());
        tfBirthday.setBounds(150, 80, 300, 35);
        tfBirthday.setFont(font);
        pn.add(tfBirthday);
       
        tfGender = new JLabel(user.isGender() ? "Nam" : "Nữ");
        tfGender.setBounds(150, 150, 300, 35);
        tfGender.setFont(font);
        pn.add(tfGender);
       
        tfCMND = new JLabel(user.getCMND());
        tfCMND.setBounds(150, 220, 300, 35);    
        tfCMND.setFont(font);
        pn.add(tfCMND);
        
        tfPhone = new JLabel(user.getPhone());
        tfPhone.setBounds(150, 290, 300, 35);    
        tfPhone.setFont(font);
        pn.add(tfPhone);   
        
        tfAddress = new JLabel(user.getAddress());
        tfAddress.setBounds(150, 360, 300, 35);
        tfAddress.setFont(font);
        pn.add(tfAddress);  
        
       
      
        
        btnClose = new JButton("ĐÓNG");
        btnClose.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnClose.setBounds(350, 500, 100, 35);
        pn.add(btnClose);
        
        avatar = new JLabel();
        avatar.setBackground(new java.awt.Color(51, 204, 255));
        avatar.setFont(new java.awt.Font("Times New Roman", 2, 12)); // NOI18N
        avatar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        avatar.setHorizontalAlignment(JLabel.CENTER);
        avatar.setText("Chưa cập nhật");
        avatar.setBounds(225, 10, 300, 400);
        int index = 0;
        btnMoreInfo = new JButton("Thông tin chi tiết");
        btnMoreInfo.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        btnMoreInfo.setBounds(22, 10 + 80 * index, 160, 60);
        leftPn.add(btnMoreInfo);
        index++;
        
        
        if (Admin.currentAdmin.getRole() == ConstAdmin.role.get("giamDoc") || Admin.currentAdmin.getRole() == ConstAdmin.role.get("nhanSu")) {
            btnChangeInfo = new JButton("Thay đổi thông tin");
            btnChangeInfo.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
            btnChangeInfo.setBounds(22, 10 + 80 * index, 160, 60);
            leftPn.add(btnChangeInfo);
            leftPn.add(avatar);
            index ++;
            
            btnChangePin = new JButton("Thay đổi mã pin");
            btnChangePin.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
            btnChangePin.setBounds(22, 10 + 80 * index, 160, 60);
            leftPn.add(btnChangePin);
            index ++;
        }
        if (Admin.currentAdmin.getRole() == ConstAdmin.role.get("giamDoc") || Admin.currentAdmin.getRole() == ConstAdmin.role.get("nhanSu")) {
            btnNapTien = new JButton("Nạp tiền");
            btnNapTien.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
            btnNapTien.setBounds(22, 10 + 80 * index, 160, 60);
            leftPn.add(btnNapTien);
            index ++;
        }
        if (Admin.currentAdmin.getRole() == ConstAdmin.role.get("giamDoc") || Admin.currentAdmin.getRole() == ConstAdmin.role.get("banHang")) {
            btnThanhToan = new JButton("Thanh Toán");
            btnThanhToan.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
            btnThanhToan.setBounds(22, 10 + 80 * index, 160, 60);
            leftPn.add(btnThanhToan);
            index ++;
        }
        
            btnDangXuat = new JButton("Đăng xuất");
            btnDangXuat.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
            btnDangXuat.setBounds(22, 10 + 80 * index, 160, 60);
//            leftPn.add(btnDangXuat);
            index ++;
        
        avatar.setIcon(new ImageIcon(user.getBufferAvatar().getScaledInstance(350, 420,Image.SCALE_SMOOTH)));
         
        
 
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               fr.dispose();
            }
        });

        btnDangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                User.currentUser = null;
                fr.dispose();
            }
        });

        btnMoreInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    MoreInfoCard.getInstance();
                } catch (IOException ex) {
                    Logger.getLogger(ViewCard.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CardException ex) {
                    Logger.getLogger(ViewCard.class.getName()).log(Level.SEVERE, null, ex);
                }
               fr.dispose();
            }
        });
        btnChangeInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    UpdateInfo.getInstance();
                } catch (IOException ex) {
                    Logger.getLogger(ViewCard.class.getName()).log(Level.SEVERE, null, ex);
                }
               fr.dispose();
            }
        });
         btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    CreateInvoice.getInstance();
                } catch (IOException ex) {
                    Logger.getLogger(ViewCard.class.getName()).log(Level.SEVERE, null, ex);
                }
               fr.dispose();
            }
        });
        
        btnChangePin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    ChangePin.getInstance();
                } catch (IOException ex) {
                    Logger.getLogger(ViewCard.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CardException ex) {
                    Logger.getLogger(ViewCard.class.getName()).log(Level.SEVERE, null, ex);
                }
               fr.dispose();
            }
        });
        
        btnNapTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    NapTien.getInstance();
                } catch (IOException ex) {
                    Logger.getLogger(ViewCard.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CardException ex) {
                    Logger.getLogger(ViewCard.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ViewCard.class.getName()).log(Level.SEVERE, null, ex);
                }
               fr.dispose();
            }
        });
        
        fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fr.setSize(1106,664);
        fr.setLocationRelativeTo(null);
        fr.setResizable(false);
        fr.setVisible(true);
    }
  
     
      
    public static void main(String args[]) throws IOException {
        ViewCard.getInstance();
    }
    
    public class JPEGImageFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.getName().toLowerCase().endsWith(".jpeg")) {
                return true;
            }
            if (f.getName().toLowerCase().endsWith(".png")) {
                return true;
            }
            if (f.getName().toLowerCase().endsWith(".jpg")) {
                return true;
            }
            return f.isDirectory();
        }

        @Override
        public String getDescription() {
            return "JPEG files";
        }

    }
}


