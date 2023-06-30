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
public class NapTien {
    
    private static NapTien instance;
    private JFrame fr;
    private JPanel pn;
    private JLabel lbTitle;
    private JButton btnNapTien;
    private JTextField tfNapTien;
     private JLabel lbNapTien;
      private JTextField tfPin;
     private JLabel lbPin;
    
    
   
    private User user;


     public static NapTien getInstance() throws IOException, CardException, SQLException {
        
            instance = new NapTien();
       
        return instance;
    }
    
    public static void dispose() {
        
       if (instance != null) {
            instance.fr.dispose();
            instance = null;
        } 
    }
     public void init() {
         fr=new JFrame("Nạp tiền");
       
        pn = new JPanel();
        fr.add(pn);
        pn.setBounds(2, 2, 402, 600);
        pn.setLayout(null);
        
        lbTitle = new JLabel("Nạp tiền", SwingConstants.CENTER);
        lbTitle.setBounds(01, 10, 400, 40);
        lbTitle.setFont(new java.awt.Font("Times New Roman", 1, 28));
        pn.add(lbTitle);
        
        lbNapTien = new JLabel("Số tiền cần nạp:", SwingConstants.LEFT);
        lbNapTien.setBounds(20, 80, 120, 40);
        lbNapTien.setFont(new java.awt.Font("Times New Roman", 1, 16));
        pn.add(lbNapTien);
        tfNapTien = new JTextField();
        tfNapTien.setBounds(130, 80, 230, 40);
        tfNapTien.setFont(new java.awt.Font("Times New Roman", 1, 16));
        pn.add(tfNapTien);
        
        
        
        
        
         btnNapTien = new JButton("Nạp");
        btnNapTien.setBounds(260, 260, 100, 35);
        btnNapTien.setFont(new java.awt.Font("Times New Roman", 1, 16));
        pn.add(btnNapTien);
       
        NapTien thisChangePin = this;
        btnNapTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    //
                    boolean verify = false;
                    try {
                        verify = HostAppHelper.getInstance().VerifyRsa();
                        if (verify) {
                            try {
                               boolean result = HostAppHelper.getInstance().NapTien(tfNapTien.getText());
                               if (!result) {
                                   JOptionPane.showMessageDialog(null, "Nạp tiền thất bại");
                               } else {
                                   User.currentUser.napTien(Integer.valueOf(tfNapTien.getText()));
                                    fr.dispose();
                                    MoreInfoCard.getInstance();
                               }
                            } catch (IOException ex) {
                                Logger.getLogger(NapTien.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                         JOptionPane.showMessageDialog(null, "Xác thực thất bại");
                        }
                    } catch (CardException ex) {
                        Logger.getLogger(NapTien.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(NapTien.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        fr.setSize(406,390);
        fr.setLocationRelativeTo(null);
        fr.setResizable(false);
        fr.setVisible(true);
        
       
//        return selectTypeAccountPn;
    }
 
    public NapTien() throws IOException, CardException, SQLException {
      init();
    }
  
     
      
    public static void main(String args[]) throws IOException, CardException, SQLException {
        NapTien.getInstance();
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


