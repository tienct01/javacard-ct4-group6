/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Constants.AUTH_CONSTANTS;
import Constants.ISO7816;

import javax.swing.*;

import Controller.HostAppHelper;
import DB.DBConnection;
import Model.User;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.smartcardio.CardException;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.MaskFormatter;

/**
 * @author hieu
 */
public class ContainerCreateAccount {

    private static ContainerCreateAccount instance;
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
    private JLabel lbImage;
    private File imageFile;
    private JLabel avatar;
    private BufferedImage myPicture;

    JRadioButton rbMale;
    JRadioButton rbFemale;
    ButtonGroup bgGioiTinh;

    private JTextField tfFullname;
    private JTextField tfCMND;
    private JTextField tfAddress;
    private JTextField tfPhone;
    private JPasswordField tfEnterPin;
    private JButton btnImage;
    private JButton btnSave;

    private JComboBox day;
    private JComboBox month;
    private JComboBox year;
    private User user;

    private JButton selectUser;
    private JButton enterPin;

    public static ContainerCreateAccount getInstance() {

        instance = new ContainerCreateAccount();
        return instance;
    }

    public static ContainerCreateAccount getInstanceUser() {

        instance = new ContainerCreateAccount(1);
        return instance;
    }

    public static void dispose() {
        if (instance != null) {
            instance.fr.dispose();
            instance = null;
        }
    }

    public ContainerCreateAccount() {

//        this.mainPns.put("selectTypeAccount", ContainerCreateAccount.selectTypeAccount());
        selectTypeAccount();
    }

    public ContainerCreateAccount(int type) {

//        this.mainPns.put("selectTypeAccount", ContainerCreateAccount.selectTypeAccount());
        formCreateUser();
    }

    public void selectTypeAccount() {
        fr = new JFrame("Lựa chọn loại tài khoản");

        pn = new JPanel();
        fr.add(pn);
        pn.setBounds(2, 2, 402, 240);
        pn.setLayout(null);
        selectAdmin = new JButton();
//        pn.add(selectAdmin);
        selectAdmin.setBackground(new java.awt.Color(153, 102, 0));
        selectAdmin.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        selectAdmin.setForeground(new java.awt.Color(255, 255, 255));
        selectAdmin.setText("Tài khoản nhân viên");
        selectAdmin.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        selectAdmin.setBounds(0, 0, 200, 200);


        selectUser = new JButton();
        pn.add(selectUser);
        selectUser.setBackground(new java.awt.Color(153, 102, 0));
        selectUser.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        selectUser.setForeground(new java.awt.Color(255, 255, 255));
        selectUser.setText("Thẻ khách hàng");
        selectUser.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        selectUser.setBounds(0, 0, 402, 200);

        selectAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                formCreateAdmin();
            }
        });

        selectUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    if (HostAppHelper.getInstance().selectAuthenApplet()) try {
                        enterPincode();
                    } catch (IOException ex) {
                        Logger.getLogger(ContainerCreateAccount.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (CardException ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi kết nối");
                }

            }
        });

        fr.setSize(406, 244);
        fr.setLocationRelativeTo(null);
        fr.setResizable(false);
        fr.setVisible(true);
//        return selectTypeAccountPn;
    }

    public void enterPincode() throws IOException {
        if (User.currentUser != null) {
            ViewCard.getInstance();
            return;
        }
        if (fr != null)
            fr.dispose();
        fr = new JFrame("Nhập mã pin");

        pn = new JPanel();
        fr.add(pn);
        pn.setBounds(2, 2, 402, 200);
        pn.setLayout(null);
        tfEnterPin = new JPasswordField();
        pn.add(tfEnterPin);
        tfEnterPin.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N


        tfEnterPin.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tfEnterPin.setBounds(25, 30, 350, 40);
        enterPin = new JButton("Ok");
        pn.add(enterPin);

        enterPin.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N

        enterPin.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        enterPin.setBounds(153, 102, 100, 40);

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
                                    Logger.getLogger(ContainerCreateAccount.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(ContainerCreateAccount.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            fr.dispose();
                            if (!info) {
                                formCreateUser();
                            } else {
                                try {

                                    ViewCard.getInstance();
                                } catch (IOException ex) {
                                    Logger.getLogger(ContainerCreateAccount.class.getName()).log(Level.SEVERE, null, ex);
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
                } catch (CardException ex) {
                    JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi");
                }
            }
        });
        fr.setSize(406, 244);
        fr.setLocationRelativeTo(null);
        fr.setResizable(false);
        fr.setVisible(true);


//        return selectTypeAccountPn;
    }

    public void formCreateUser() {
        if (fr != null)
            fr.dispose();
        fr = new JFrame("Khởi tạo thẻ tích điểm");
        Font font = new Font("Serif", Font.PLAIN, 20);
        pn = new JPanel();
        fr.add(pn);
        pn.setBounds(354, 62, 540, 540);
        pn.setLayout(null);

        leftPn = new JPanel();
        fr.add(leftPn);
        leftPn.setBounds(2, 62, 350, 420);
        leftPn.setLayout(null);

        pnTitle = new JPanel();
        fr.add(pnTitle);
        pnTitle.setBounds(0, 0, 900, 40);
        pnTitle.setLayout(null);
        titleInitCard = new JLabel("KHỞI TẠO THẺ KHÁCH HÀNG", SwingConstants.CENTER);
        titleInitCard.setFont(new java.awt.Font("Times New Roman", 1, 28)); // NOI18N
        titleInitCard.setBounds(3, 0, 900, 40);
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

        lbImage = new JLabel("Ảnh :", SwingConstants.LEFT);
        lbImage.setBounds(20, 430, 100, 35);
        lbImage.setFont(font);
        pn.add(lbImage);

        tfFullname = new JTextField();
        tfFullname.setBounds(150, 10, 300, 35);
        tfFullname.setFont(font);
        pn.add(tfFullname);
        String[] array = new String[31];
        for (int i = 0; i < 31; i++) {
            array[i] = Integer.toString(i + 1);
        }

        day = new JComboBox(array);
        day.setBounds(150, 88, 60, 20);
        day.setFont(font);
        pn.add(day);
        array = new String[12];
        for (int i = 0; i < 12; i++) {
            array[i] = Integer.toString(i + 1);
        }
        JLabel s1 = new JLabel("/");
        s1.setFont(font);

        JLabel s2 = new JLabel("/");
        s2.setFont(font);
        s1.setBounds(230, 88, 10, 20);
        s2.setBounds(330, 88, 10, 20);
        pn.add(s1);
        pn.add(s2);
        month = new JComboBox(array);
        month.setBounds(250, 88, 60, 20);
        month.setFont(font);
        pn.add(month);

        array = new String[100];
        for (int i = 0; i < 100; i++) {
            array[i] = Integer.toString(1930 + i + 1);
        }

        year = new JComboBox(array);
        year.setBounds(350, 88, 90, 20);
        year.setFont(font);
        pn.add(year);

        bgGioiTinh = new ButtonGroup();
        rbMale = new JRadioButton("Nam");
        rbFemale = new JRadioButton("Nữ");
        bgGioiTinh.add(rbMale);
        bgGioiTinh.add(rbFemale);
        rbMale.setBounds(150, 150, 80, 25);
        rbMale.setFont(font);
        pn.add(rbMale);
        rbFemale.setBounds(240, 150, 80, 25);
        rbFemale.setFont(font);
        pn.add(rbFemale);

        tfCMND = new JTextField();
        tfCMND.setBounds(150, 220, 300, 35);
        tfCMND.setFont(font);
        pn.add(tfCMND);

        tfPhone = new JTextField();
        tfPhone.setBounds(150, 290, 300, 35);
        tfPhone.setFont(font);
        pn.add(tfPhone);

        tfAddress = new JTextField();
        tfAddress.setBounds(150, 360, 300, 35);
        tfAddress.setFont(font);
        pn.add(tfAddress);

        btnImage = new JButton("CHỌN ẢNH");
        btnImage.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnImage.setBounds(150, 430, 100, 35);
        pn.add(btnImage);

        btnSave = new JButton("LƯU");
        btnSave.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnSave.setBounds(350, 500, 100, 35);
        pn.add(btnSave);

        avatar = new JLabel();
        avatar.setBackground(new java.awt.Color(51, 204, 255));
        avatar.setFont(new java.awt.Font("Times New Roman", 2, 12)); // NOI18N
        avatar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        avatar.setHorizontalAlignment(JTextField.CENTER);
        avatar.setText("Chưa cập nhật");
        avatar.setBounds(25, 10, 300, 400);
        leftPn.add(avatar);
        btnImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImageClick(evt);
            }
        });

        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    try {
                        saveData(evt);
                    } catch (SQLException ex) {
                        Logger.getLogger(ContainerCreateAccount.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (CardException ex) {
                    Logger.getLogger(ContainerCreateAccount.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ContainerCreateAccount.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        fr.setSize(906, 664);
        fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        fr.setLocationRelativeTo(null);
        fr.setResizable(false);
        fr.setVisible(true);
    }

    private void asignAvatar() {
        try {
            myPicture = ImageIO.read(this.imageFile);
            avatar.setIcon(new ImageIcon(myPicture.getScaledInstance(350, 420, Image.SCALE_SMOOTH)));
        } catch (IOException ex) {
        }
    }

    private void btnImageClick(java.awt.event.ActionEvent evt) {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new JPEGImageFileFilter());
        jfc.showOpenDialog(fr);
        File file = jfc.getSelectedFile();

        if (file != null) {
            imageFile = file;
            asignAvatar();
        }
    }


    private void saveData(java.awt.event.ActionEvent evt) throws CardException, IOException, SQLException {
        if (!validate()) {
            JOptionPane.showMessageDialog(null, "Hãy nhập đẩy đủ thông tin!");
            return;
        }
        boolean gioiTinh;
        String birthDay = (String) day.getSelectedItem() + '/' + (String) month.getSelectedItem() + '/' + (String) year.getSelectedItem();
        int index = imageFile.getName().lastIndexOf(".");
        String typeFile = imageFile.getName().substring(index + 1);

        int id;
        try {
            Connection conn = DBConnection.getConnection();
            String query = "select Max(id) as maxId from users_key";
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            rs.next();
            id = rs.getInt("maxId") + 1;
        } catch (SQLException e) {
            id = 0;
        }

        User.currentUser = new User(
                id,
                tfFullname.getText().trim(),
                (String) day.getSelectedItem() + '/' + (String) month.getSelectedItem() + '/' + (String) year.getSelectedItem(),
                (rbFemale.isSelected()) ? false : true,
                tfCMND.getText().trim(),
                tfPhone.getText().trim(),
                tfAddress.getText().trim(),
                myPicture,
                typeFile
        );

//       HostAppHelper.getInstance().enterPin("123456". toCharArray());

        if (HostAppHelper.getInstance().createInfo()) {
            HostAppHelper.getInstance().getInfoMarket();
            fr.dispose();
            ChangePin.getInstance();
        } else {
            JOptionPane.showMessageDialog(null, "Khởi tạo thẻ thất bại!");
        }
    }

    private boolean validate() {
        if (tfFullname.getText().trim().isEmpty()) {
            return false;
        }
        int intDay = Integer.parseInt((String) day.getSelectedItem());
        int intMonth = Integer.parseInt((String) month.getSelectedItem());
        int intYear = Integer.parseInt((String) year.getSelectedItem());
        if (intDay <= 0 || intMonth <= 0 || intYear < 1930 || intDay > 31 || intMonth > 12 || intYear > 2030) {
            return false;
        }

        if (tfCMND.getText().trim().isEmpty()) {
            return false;
        }
        if (tfAddress.getText().trim().isEmpty()) {
            return false;
        }
        if (imageFile == null) {
            return false;
        }
        if ((!rbMale.isSelected()) && (!rbFemale.isSelected())) {
            return false;
        }
        return true;
    }

    public static void main(String args[]) {
        ContainerCreateAccount.getInstance();
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


