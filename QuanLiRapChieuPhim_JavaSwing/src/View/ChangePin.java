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
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
public class ChangePin {
    
    private static ChangePin instance;
    private JFrame fr;
    private JPanel pn;
    private JLabel lbTitle;
    private JButton enterPin;
    private JPasswordField tfOldPass;
    private JPasswordField tfNewPass2;
    private JPasswordField tfNewPass;
    private JLabel lbNewPass;
     private JLabel lbOldPass;
      private JLabel lbNewPass2;
    
    
   
    private User user;


     public static ChangePin getInstance() throws IOException, CardException {
        
            instance = new ChangePin();
       
        return instance;
    }
    
    public static void dispose() {
        
       if (instance != null) {
            instance.fr.dispose();
            instance = null;
        } 
    }
     public void init() {
         fr=new JFrame("Đổi mã pin");
       
        pn = new JPanel();
        fr.add(pn);
        pn.setBounds(2, 2, 402, 600);
        pn.setLayout(null);
        
        lbTitle = new JLabel("ĐỔI MÃ PIN", SwingConstants.CENTER);
        lbTitle.setBounds(01, 10, 400, 40);
        lbTitle.setFont(new java.awt.Font("Times New Roman", 1, 28));
        pn.add(lbTitle);
        
        lbOldPass = new JLabel("Mã pin cũ:", SwingConstants.LEFT);
        lbOldPass.setBounds(20, 80, 120, 40);
        lbOldPass.setFont(new java.awt.Font("Times New Roman", 1, 16));
        pn.add(lbOldPass);
        tfOldPass = new JPasswordField();
        tfOldPass.setBounds(130, 80, 230, 40);
        tfOldPass.setFont(new java.awt.Font("Times New Roman", 1, 16));
        pn.add(tfOldPass);
        
        
        lbNewPass = new JLabel("Mã pin mới:", SwingConstants.LEFT);
        lbNewPass.setBounds(20, 150, 120, 40);
        lbNewPass.setFont(new java.awt.Font("Times New Roman", 1, 16));
        pn.add(lbNewPass);
        tfNewPass = new JPasswordField();
        tfNewPass.setBounds(130, 150, 230, 40);
        tfNewPass.setFont(new java.awt.Font("Times New Roman", 1, 16));
        pn.add(tfNewPass);
        
        lbNewPass2 = new JLabel("Nhập lại mã pin:", SwingConstants.LEFT);
        lbNewPass2.setBounds(20, 220, 120, 40);
        lbNewPass2.setFont(new java.awt.Font("Times New Roman", 1, 16));
        pn.add(lbNewPass2);
        tfNewPass2 = new JPasswordField();
        tfNewPass2.setBounds(130, 220, 230, 40);
        tfNewPass2.setFont(new java.awt.Font("Times New Roman", 1, 16));
        pn.add(tfNewPass2);
        
         enterPin = new JButton("Lưu");
        enterPin.setBounds(260, 300, 100, 35);
        enterPin.setFont(new java.awt.Font("Times New Roman", 1, 16));
        pn.add(enterPin);
       
        ChangePin thisChangePin = this;
        enterPin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                    //               
            char[] pinOld = tfOldPass.getPassword();
            char[] pinNew = tfNewPass.getPassword();
            char[] pinNew2 = tfNewPass2.getPassword();
            if (pinNew.length != 6) {
                JOptionPane.showMessageDialog(null, "Mã pin phải có 6 kí tự");
                return;
            }
            if (!Arrays.equals(pinNew, pinNew2)) {
                JOptionPane.showMessageDialog(null, "Mã pin mới không khớp");
                return;
            }
            char[] pin = new char[pinOld.length + pinNew.length];
            System.arraycopy(pinOld, 0, pin, 0, pinOld.length);
            System.arraycopy(pinNew, 0, pin, pinOld.length, pinNew.length);
            try {
            if (HostAppHelper.getInstance().selectAuthenApplet()) {
                String check = HostAppHelper.getInstance().updatePin(pin);
           

            switch (check) {
                case ISO7816.SW_NO_ERROR:
                {
                    try {
                        //                    // verify success
                        ViewCard.getInstance();
                        fr.dispose();
                    } catch (IOException ex) {
                        Logger.getLogger(ChangePin.class.getName()).log(Level.SEVERE, null, ex);
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
        }
            
        } catch (CardException ex) {
           JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi");
        }
            }
        });
        fr.setSize(406,390);
        fr.setLocationRelativeTo(null);
        fr.setResizable(false);
        fr.setVisible(true);
        
       
//        return selectTypeAccountPn;
    }
 
    public ChangePin() throws IOException, CardException {
        boolean moreInfo = false;  
        try {
           
        moreInfo = HostAppHelper.getInstance().getInfoMarket();
     } catch (IOException ex) {
         Logger.getLogger(ContainerCreateAccount.class.getName()).log(Level.SEVERE, null, ex);
     } catch (CardException ex) {
           JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi");
        }
           
         if (!moreInfo) {
             ContainerCreateAccount.getInstanceUser();
         } else {
           
             this.user = User.currentUser;
             init();
         }
    }
  
     
      
    public static void main(String args[]) throws IOException, CardException {
        MoreInfoCard.getInstance();
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


