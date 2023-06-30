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
import java.io.UnsupportedEncodingException;
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
 * @author hieu
 */
public class MoreInfoCard {

    private static MoreInfoCard instance;
    private JFrame fr;
    private JPanel pn;
    private JPanel pnTitle;
    private JPanel leftPn;
    private JButton selectAdmin;
    private JLabel titleInitCard;
    private JLabel lbFullname;
    private JLabel lbRank;
    private JLabel lbDiem;
    private JLabel lbTotalHistory;
    private JLabel lbTotal;
    private JLabel lbCoupon;

    private JLabel avatar;

    private JLabel tfFullname;
    private JLabel tfRank;

    private JLabel tfDiem;
    private JLabel tfTotalHistory;
    private JLabel tfTotal;
    private JComboBox tfCoupon;


    private JButton btnClose;


    private User user;


    public static MoreInfoCard getInstance() throws IOException, CardException {

        instance = new MoreInfoCard();

        return instance;
    }

    public static void dispose() {

        if (instance != null) {
            instance.fr.dispose();
            instance = null;
        }
    }

    public MoreInfoCard() throws IOException, CardException {
//        boolean moreInfo = false;  
//        try {
//           
//        moreInfo = HostAppHelper.getInstance().getInfoMarket();
//     } catch (IOException ex) {
//         Logger.getLogger(ContainerCreateAccount.class.getName()).log(Level.SEVERE, null, ex);
//     } catch (CardException ex) {
//           JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi");
//        }
//           
//         if (!moreInfo) {
//             ContainerCreateAccount.getInstanceUser();
//         } else {

        this.user = User.currentUser;
        init();
//         }
    }

    public void init() throws UnsupportedEncodingException {
        fr = new JFrame("Thông tin chi tiết");
        Font font = new Font("Serif", Font.PLAIN, 20);
        pn = new JPanel();
        fr.add(pn);
        pn.setBounds(354, 62, 540, 540);
        pn.setLayout(null);

        leftPn = new JPanel();
        fr.add(leftPn);
        leftPn.setBounds(2, 62, 350, 520);
        leftPn.setLayout(null);

        pnTitle = new JPanel();
        fr.add(pnTitle);
        pnTitle.setBounds(0, 0, 900, 40);
        pnTitle.setLayout(null);
        titleInitCard = new JLabel("THÔNG TIN CHI TIẾT", SwingConstants.CENTER);
        titleInitCard.setFont(new java.awt.Font("Times New Roman", 1, 28)); // NOI18N
        titleInitCard.setBounds(3, 0, 900, 40);
        pnTitle.add(titleInitCard);

        lbFullname = new JLabel("Họ tên :", SwingConstants.LEFT);
        lbFullname.setBounds(20, 10, 130, 35);
        lbFullname.setFont(font);
        pn.add(lbFullname);


        tfFullname = new JLabel(user.getFullname());
        tfFullname.setBounds(150, 10, 300, 35);
        tfFullname.setFont(font);
        pn.add(tfFullname);


        lbRank = new JLabel("Hạng", SwingConstants.LEFT);
        lbRank.setBounds(20, 60, 130, 35);
        lbRank.setFont(font);
        pn.add(lbRank);


        tfRank = new JLabel("Bạc");
        tfRank.setBounds(150, 60, 300, 35);
        tfRank.setFont(font);
        pn.add(tfRank);

        lbTotal = new JLabel("Số dư:", SwingConstants.LEFT);
        lbTotal.setBounds(20, 110, 130, 35);
        lbTotal.setFont(font);
        pn.add(lbTotal);


        tfTotal = new JLabel(user.getTotal() + "");
        tfTotal.setBounds(150, 110, 300, 35);
        tfTotal.setFont(font);
        pn.add(tfTotal);

        lbDiem = new JLabel("Điểm tích lũy", SwingConstants.LEFT);
        lbDiem.setBounds(20, 160, 130, 35);
        lbDiem.setFont(font);
        pn.add(lbDiem);


        tfDiem = new JLabel(user.getPoint() + "");
        tfDiem.setBounds(150, 160, 300, 35);
        tfDiem.setFont(font);
        pn.add(tfDiem);

        lbTotalHistory = new JLabel("Số tiền chi tiêu:", SwingConstants.LEFT);
        lbTotalHistory.setBounds(20, 210, 130, 35);
        lbTotalHistory.setFont(font);
        pn.add(lbTotalHistory);


        tfTotalHistory = new JLabel(user.getTotalHistory() + "");
        tfTotalHistory.setBounds(150, 210, 300, 35);
        tfTotalHistory.setFont(font);
        pn.add(tfTotalHistory);

        lbCoupon = new JLabel("Phiếu giảm giá:", SwingConstants.LEFT);
        lbCoupon.setBounds(20, 260, 130, 35);
        lbCoupon.setFont(font);
//        pn.add(lbCoupon);


        tfCoupon = new JComboBox(user.getPromotionCodes().toArray());
        tfCoupon.setBounds(150, 260, 300, 35);
        tfCoupon.setFont(font);
//        pn.add(tfCoupon);


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
        avatar.setBounds(25, 10, 300, 400);
        leftPn.add(avatar);
        int index = 0;

        avatar.setIcon(new ImageIcon(user.getBufferAvatar().getScaledInstance(350, 420, Image.SCALE_SMOOTH)));


        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fr.dispose();
            }
        });

        fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fr.setSize(906, 664);
        fr.setLocationRelativeTo(null);
        fr.setResizable(false);
        fr.setVisible(true);
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


