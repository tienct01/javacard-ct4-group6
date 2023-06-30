/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Constants.APPLET_CONSTANTS;
import Constants.AUTH_CONSTANTS;
import Constants.INFO_CONSTANTS;
import Constants.MARKET_CONSTANTS;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Constants.ISO7816;
import DB.DBConnection;
import Model.Invoice;
import Utils.ConvertData;
import Model.User;

import static Utils.RandomUtil.randomData;

import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.swing.JOptionPane;

/**
 * @author Admin
 */
public class HostAppHelper {


    private Card card;
    private TerminalFactory factory;
    public CardChannel channel;
    private CardTerminal terminal;
    private List<CardTerminal> terminals;
    private short currentSelect;
    private PublicKey pubKey;// khai
    private static HostAppHelper instance;

    private HostAppHelper() {
    }

    public static HostAppHelper getInstance() {
        if (instance == null) {
            instance = new HostAppHelper();
        }
        return instance;
    }

    public boolean connectReader() throws CardException {

        if (channel == null) {

            try {
                factory = TerminalFactory.getDefault();
                terminals = factory.terminals().list();
                terminal = terminals.get(0);
                card = terminal.connect("T=1");

                ATR atr = card.getATR();
                byte[] baAtr = atr.getBytes();

                channel = card.getBasicChannel();

                return channel != null;
            } catch (CardException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, "Lỗi kết nối");
                return false;
            }
        }
        return false;
    }

    public int getTotalFailure() throws CardException {
        try {
            if (selectInfoApplet()) {
                ResponseAPDU response = sendAPDU(
                        APPLET_CONSTANTS.CLA, INFO_CONSTANTS.INS_CHECKERROR, 1,
                        2, null);
                String check = Integer.toHexString(response.getSW());
                if (check.equals(ISO7816.SW_NO_ERROR)) {
                    byte[] data = response.getData();
                    return data[0];
                }
            }
        } catch (CardException ex) {
            Logger.getLogger(HostAppHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return -1;
    }

    public boolean incrementFailure() throws CardException {
        try {
            if (selectInfoApplet()) {
                ResponseAPDU response = sendAPDU(
                        APPLET_CONSTANTS.CLA, INFO_CONSTANTS.INS_ERROR, 1,
                        2, null);
                String check = Integer.toHexString(response.getSW());
                return check.equals(ISO7816.SW_NO_ERROR);
            }
        } catch (CardException ex) {
            Logger.getLogger(HostAppHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return false;
    }

    public boolean unblockInfo() throws CardException {
        try {
            if (selectInfoApplet()) {
                ResponseAPDU response = sendAPDU(
                        APPLET_CONSTANTS.CLA, INFO_CONSTANTS.INS_UNBLOCK, 1,
                        2, null);
                String check = Integer.toHexString(response.getSW());
                return check.equals(ISO7816.SW_NO_ERROR);
            }
        } catch (CardException ex) {
            Logger.getLogger(HostAppHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return false;
    }

    public boolean blockInfo() throws CardException {
        try {
            if (selectInfoApplet()) {
                ResponseAPDU response = sendAPDU(
                        APPLET_CONSTANTS.CLA, INFO_CONSTANTS.INS_BLOCK, 1,
                        2, null);
                String check = Integer.toHexString(response.getSW());
                return check.equals(ISO7816.SW_NO_ERROR);
            }
        } catch (CardException ex) {
            Logger.getLogger(HostAppHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return false;
    }

    public int isBlockInfo() throws CardException {
        try {
            if (selectInfoApplet()) {
                ResponseAPDU response = sendAPDU(
                        APPLET_CONSTANTS.CLA, INFO_CONSTANTS.INS_CHECKBLOCKE, 1,
                        2, null);
                String check = Integer.toHexString(response.getSW());
                if (check.equals(ISO7816.SW_NO_ERROR)) {
                    byte[] data = response.getData();
                    return data[0];
                }
            }
        } catch (CardException ex) {
            Logger.getLogger(HostAppHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return -1;
    }

    public boolean getInfomation() throws CardException, IOException, SQLException {
        try {
            if (selectInfoApplet()) {
                byte[] dataNgaysinh = {};
                byte[] dataId = {};
                byte[] dataHoten = {};
                byte[] dataDiachi = {};
                byte[] dataCMND = {};
                byte[] dataSDT = {};
                byte[] dataAvatar = {};
                byte[] data = {};
                int flag = 0;
                int count = 0;
                byte dataGioitinh = (byte) 48;
                ResponseAPDU response = sendAPDU(
                        APPLET_CONSTANTS.CLA, INFO_CONSTANTS.INS_THONGTIN, 1,
                        2, null);
                String check = Integer.toHexString(response.getSW());
                if (check.equals(ISO7816.SW_NO_ERROR)) {
                    data = response.getData();
                    if (data.length < 2) return false;
//                   if (data
                    for (int i = 0; i < data.length; i++) {
                        if (data[i] == (byte) 0x01) {
                            switch (flag) {
                                case INFO_CONSTANTS.COUNT_ID:
                                    dataId = new byte[count];
                                    System.arraycopy(data, i - count, dataId, 0, count);
                                    break;
                                case INFO_CONSTANTS.COUNT_HOTEN:
                                    dataHoten = new byte[count];
                                    System.arraycopy(data, i - count, dataHoten, 0, count);
                                    break;
                                case INFO_CONSTANTS.COUNT_NGAYSINH:
                                    dataNgaysinh = new byte[count];
                                    System.arraycopy(data, i - count, dataNgaysinh, 0, count);
                                    break;
                                case INFO_CONSTANTS.COUNT_GIOITINH:
                                    dataGioitinh = (data[i - 1] == (byte) 0x49) ? (byte) 0x49 : (byte) 0x48;
                                    break;

                                case INFO_CONSTANTS.COUNT_SDT:
                                    dataSDT = new byte[count];
                                    System.arraycopy(data, i - count, dataSDT, 0, count);
                                    break;
                                case INFO_CONSTANTS.COUNT_CMND:
                                    dataCMND = new byte[count];
                                    System.arraycopy(data, i - count, dataCMND, 0, count);
                                    break;
                                case INFO_CONSTANTS.COUNT_DIACHI:
                                    dataDiachi = new byte[count];
                                    System.arraycopy(data, i - count, dataDiachi, 0, count);
                                    break;
                                default:
                                    break;
                            }
                            if (flag < INFO_CONSTANTS.COUNT_AVATAR) {
                                count = 0;
                                flag++;
                                continue;
                            }

                        }
                        count++;
                    }
                    dataAvatar = new byte[count];
                    System.arraycopy(data, data.length - count, dataAvatar, 0, count);

                    ByteArrayInputStream bis = new ByteArrayInputStream(dataAvatar);
                    BufferedImage fileAvatar;
                    try {
                        fileAvatar = ImageIO.read(bis);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    User.currentUser = new User(dataId, dataHoten, dataNgaysinh, dataGioitinh, dataDiachi, dataCMND, dataSDT, fileAvatar);
                    User.currentUser.getInfoDatabase();
                    HostAppHelper.getInstance().getInfoMarket();
                    return true;
                }
            }
        } catch (CardException ex) {
            Logger.getLogger(HostAppHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return false;
    }

    public boolean updateMarket(Invoice invoice) throws CardException {
        try {
            if (selectMarketApplet()) {
                ArrayList<String> maKhuyenMaiDonHang = invoice.getMaKhuyenmaiDonHang();
                int soLuongMa = maKhuyenMaiDonHang.size();
                byte[] data = new byte[29 + 8 * soLuongMa];// 1 mã giảm giá, về sau sẽ nâng// da nang

                byte[] total = String.valueOf(invoice.getIntoMoney()).getBytes();
                byte[] usePoint = String.valueOf(invoice.getDiem()).getBytes();
                byte[] accumulatePoint = String.valueOf((int) (invoice.getTotal() / 1000)).getBytes();

                System.arraycopy(total, 0, data, 12 - total.length, total.length);
                System.arraycopy(usePoint, 0, data, 28 - usePoint.length, usePoint.length);

                System.arraycopy(accumulatePoint, 0, data, 20 - accumulatePoint.length, accumulatePoint.length);

                for (int i = 0; i < soLuongMa; i++) {
                    byte[] ma = maKhuyenMaiDonHang.get(i).getBytes();
                    System.arraycopy(ma, 0, data, 29 + 8 * i, 8);
                }


                for (int i = 0; i < 28; i++) {
                    if (data[i] >= (byte) 48) {
                        data[i] = (byte) (data[i] - 48);
                    }
                }
                data[28] = (byte) invoice.getIndexMaGiamGia();
                ResponseAPDU response = sendAPDU(
                        APPLET_CONSTANTS.CLA, MARKET_CONSTANTS.INS_THANHTOAN, 1,
                        2, data);
                String check = Integer.toHexString(response.getSW());
                if (check.equals(ISO7816.SW_NO_ERROR)) {


                    return true;
                }
            }
        } catch (CardException ex) {
            Logger.getLogger(HostAppHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return false;
    }

    public boolean getInfoMarket() throws CardException, IOException {
        try {
            if (selectMarketApplet()) {
                byte[] totalHistory = new byte[12];
                byte[] total = new byte[12];
                byte[] point = new byte[8];
                byte[] promotionCodes = new byte[224];


                ResponseAPDU response = sendAPDU(
                        APPLET_CONSTANTS.CLA, MARKET_CONSTANTS.INS_THONGTIN, 1,
                        2, null);
                String check = Integer.toHexString(response.getSW());
                if (check.equals(ISO7816.SW_NO_ERROR)) {
                    byte[] data = response.getData();
//                   if (data
                    System.arraycopy(data, 0, total, 0, 12);
                    System.arraycopy(data, 12, totalHistory, 0, 12);
                    System.arraycopy(data, 24, point, 0, 8);
                    System.arraycopy(data, 32, promotionCodes, 0, 224);
                    User.currentUser.setTotal(total);
                    User.currentUser.setTotalHistory(totalHistory);

                    User.currentUser.setPoint(point);
                    User.currentUser.setPromotionCodes(promotionCodes);


//                User.currentUser = new User(dataHoten, dataNgaysinh, dataGioitinh, dataDiachi, dataCMND, dataSDT, fileAvatar);

                    return true;
                }
            }
        } catch (CardException ex) {
            Logger.getLogger(HostAppHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return false;
    }
    // type = jpg


    public boolean createInfo() throws CardException, IOException, SQLException {
        if (selectInfoApplet()) {
            ResponseAPDU response = sendAPDU(
                    APPLET_CONSTANTS.CLA, INFO_CONSTANTS.INS_INSERT, 1,
                    2, User.currentUser.encodeInfo());
            String check = Integer.toHexString(response.getSW());

            byte[] data = response.getData();
            if (data.length > 0)// Khởi taoj thẻ
            {

                ArrayList<String> pubKeyString = pubkeyRsa();
                User.currentUser.setPublicKey(pubKeyString);
                User.currentUser.saveDatabase();

            }
            return check.equals(ISO7816.SW_NO_ERROR);
        }
        return false;
    }

    public boolean updateInfo() throws CardException, IOException, SQLException {
        if (selectInfoApplet()) {
            ResponseAPDU response = sendAPDU(
                    APPLET_CONSTANTS.CLA, INFO_CONSTANTS.INS_CHANGE_INFO, 1,
                    2, User.currentUser.encodeInfoNotImage());
            String check = Integer.toHexString(response.getSW());

            byte[] data = response.getData();
            if (data.length > 0)// Khởi taoj thẻ
            {

                ArrayList<String> pubKeyString = pubkeyRsa();
                User.currentUser.setPublicKey(pubKeyString);
                User.currentUser.saveDatabase();

            }
            return check.equals(ISO7816.SW_NO_ERROR);
        }
        return false;
    }

    public boolean updateImage() throws CardException, IOException, SQLException {
        if (selectInfoApplet()) {
            ResponseAPDU response = sendAPDU(
                    APPLET_CONSTANTS.CLA, INFO_CONSTANTS.INS_CHANGE_IMAGE, 1,
                    2, User.currentUser.encodeImage());
            String check = Integer.toHexString(response.getSW());

            byte[] data = response.getData();
            if (data.length > 0)// Khởi taoj thẻ
            {

                ArrayList<String> pubKeyString = pubkeyRsa();
                User.currentUser.setPublicKey(pubKeyString);
                User.currentUser.saveDatabase();

            }
            return check.equals(ISO7816.SW_NO_ERROR);
        }
        return false;
    }

    public boolean NapTien(String money) throws CardException, IOException, SQLException {
        if (selectMarketApplet()) {
            byte[] byteMoney = new byte[12];

            for (int i = 12 - money.length(); i < 12; i++) {
                byteMoney[i] = (byte) (money.getBytes()[i - 12 + money.length()] - 48);
            }
            ResponseAPDU response = sendAPDU(
                    APPLET_CONSTANTS.CLA, MARKET_CONSTANTS.INS_NAPTIEN, 1,
                    2, byteMoney);
            String check = Integer.toHexString(response.getSW());

            return check.equals(ISO7816.SW_NO_ERROR);
        }
        return false;
    }

    public boolean selectInfoApplet() throws CardException {
        if (channel == null) {
            connectReader();
            currentSelect = APPLET_CONSTANTS.SELECT_NULL;
        }
        try {
            if (currentSelect == APPLET_CONSTANTS.SELECT_INFO) return true;
            ResponseAPDU response = selectApplet(APPLET_CONSTANTS.AID_INFO_APPLET);
            String check = Integer.toHexString(response.getSW());

            if (check.equals(ISO7816.SW_NO_ERROR)) {
                byte[] baCardUid = response.getData();
                currentSelect = APPLET_CONSTANTS.SELECT_INFO;
                return true;

            }
        } catch (CardException ex) {
            Logger.getLogger(HostAppHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return false;
    }

    public boolean selectMarketApplet() throws CardException {
        if (channel == null) {
            connectReader();
            currentSelect = APPLET_CONSTANTS.SELECT_NULL;
        }
        try {
            if (currentSelect == APPLET_CONSTANTS.SELECT_MARKET) return true;

            ResponseAPDU response = selectApplet(APPLET_CONSTANTS.AID_MARKET_APPLET);
            String check = Integer.toHexString(response.getSW());

            if (check.equals(ISO7816.SW_NO_ERROR)) {
                byte[] baCardUid = response.getData();
                currentSelect = APPLET_CONSTANTS.SELECT_MARKET;

                return true;

            }
        } catch (CardException ex) {
            Logger.getLogger(HostAppHelper.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return false;
    }

    public boolean selectAuthenApplet() throws CardException {
        if (channel == null) {
            connectReader();
            currentSelect = APPLET_CONSTANTS.SELECT_NULL;
        }

        try {
            if (currentSelect == APPLET_CONSTANTS.SELECT_AUTH) return true;
            ResponseAPDU response = selectApplet(APPLET_CONSTANTS.AID_AUTH_APPLET);
            String check = Integer.toHexString(response.getSW());

            switch (check) {
                case ISO7816.SW_NO_ERROR:
                    byte[] baCardUid = response.getData();


                    currentSelect = APPLET_CONSTANTS.SELECT_AUTH;
                    return true;
                default:
                    JOptionPane.showMessageDialog(null, "Lỗi kết nối");
                    return false;
            }
        } catch (CardException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi kết nối");
            return false;
        }
    }

    // select
    private ResponseAPDU selectApplet(byte[] aid) throws CardException {
        ResponseAPDU response = channel.transmit(new CommandAPDU(
                0x00, (byte) 0xA4,
                0x04, 0x00,
                aid)
        );
        return response;
    }

    public String enterPin(char[] pinText) throws CardException {
        if (selectAuthenApplet()) {
            ResponseAPDU response = sendAPDU(
                    APPLET_CONSTANTS.CLA, APPLET_CONSTANTS.VERIFY, AUTH_CONSTANTS.AUTH,
                    (byte) 2, ConvertData.charArrayToByteArray(pinText));
            return Integer.toHexString(response.getSW());
        }
        return null;

    }

    public String updatePin(char[] pin) throws CardException {
        if (selectAuthenApplet()) {
            ResponseAPDU response = sendAPDU(
                    APPLET_CONSTANTS.CLA, APPLET_CONSTANTS.UPDATE, AUTH_CONSTANTS.AUTH,
                    (byte) 2, ConvertData.charArrayToByteArray(pin));
            return Integer.toHexString(response.getSW());
        }
        return null;

    }

    public boolean unlockCard() throws CardException {
        if (selectAuthenApplet()) {
            ResponseAPDU response = sendAPDU(
                    APPLET_CONSTANTS.CLA, APPLET_CONSTANTS.UNBLOCK, AUTH_CONSTANTS.AUTH,
                    (byte) 2, null);
            String check = Integer.toHexString(response.getSW());
            return check.equals(ISO7816.SW_NO_ERROR);
        }
        return false;
    }

    public boolean closeConnection() {
        try {
            card.disconnect(false);
            channel = null;
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public ArrayList<String> pubkeyRsa() throws CardException {
        ArrayList<String> result = new ArrayList<String>();
        if (selectAuthenApplet()) {
            byte[] moduBytes;
            byte[] expoBytes;
            pubKey = null;
            BigInteger exponent = null;
            BigInteger modulus = null;
            try {
                // lấy modu từ applet
                ResponseAPDU getmodu = sendAPDU(APPLET_CONSTANTS.CLA, APPLET_CONSTANTS.GET_MOL, 0X01, 0X01, null);
                moduBytes = getmodu.getData();
                modulus = new BigInteger(1, moduBytes);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                // lấy expo từ applet
                ResponseAPDU getex = sendAPDU(APPLET_CONSTANTS.CLA, APPLET_CONSTANTS.GET_EXPO, 0X01, 0X01, null);
                expoBytes = getex.getData();
                exponent = new BigInteger(1, expoBytes);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                // sinh khoá pubRSA
                RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
                result.add(modulus.toString());
                result.add(exponent.toString());

                KeyFactory factory = KeyFactory.getInstance("RSA");
                pubKey = factory.generatePublic(spec);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        return null;
    }

    public boolean VerifyRsa() throws SQLException, CardException {

        //đọc file pubRSA
        if (selectAuthenApplet()) {
            Connection conn = DBConnection.getConnection();

            try {
//            fis = new FileInputStream("publicKey.rsa");
//            byte[] b = new byte[fis.available()];
//            fis.read(b);
//            fis.close();
                String publicKeyModule = "";
                String publicKeyExponent = "";
                User.currentUser.getInfoDatabase();
                publicKeyModule = User.currentUser.getPublicKeyModulus();
                publicKeyExponent = User.currentUser.getPublicKeyExponent();

//           System.out.println(publicKeyString);
//           System.out.println("..............................................................................");
//           for(int i = 0; i< publicBytes.length; i++) {
//                System.out.println(publicBytes[i]);
//            }
//            System.out.println("..............................................................................");
//             System.out.println(publicBytes.length);
//              System.out.println("..............................................................................");
                RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger(publicKeyModule), new BigInteger(publicKeyExponent));
                KeyFactory factory = KeyFactory.getInstance("RSA");
                System.out.println(7);
                pubKey = factory.generatePublic(spec);

            } catch (NoSuchAlgorithmException ex) {
                System.out.println(1);
            } catch (InvalidKeySpecException ex) {
                System.out.println(ex);
            }
//       
            byte[] data_random = randomData(20);
            boolean isCorrect = false;
            try {
//            truyền thằng random byte[] xuống và lấy chữ ký lên

                ResponseAPDU sendverify = sendAPDU(APPLET_CONSTANTS.CLA, APPLET_CONSTANTS.GET_RSA, 0X01, 0X01, data_random);
                byte[] signal = sendverify.getData();
                System.out.println(bytesToHex(signal));
                Signature publicSignature = Signature.getInstance("SHA1withRSA");
                // pubRSA lấy từ file, có thể lấy thẳng thằng pubRSA ở trên cũng đc
                publicSignature.initVerify(pubKey);

                // đầu vào để xác thực, cái thằng byte[] đầu mà sinh ngẫu nhiên
                publicSignature.update(data_random);

                // verify truyền thằng chữ ký vào
                isCorrect = publicSignature.verify(signal);
                System.out.println("dd: " + isCorrect);
            } catch (Exception e) {
                e.printStackTrace();
            }



            return isCorrect;
        } else return false;

    }

    public static String bytesToHex(byte[] bytes) {
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public ResponseAPDU sendAPDU(
            int cla, int ins, int p1, int p2, byte[] data
    ) throws CardException {
        if (channel == null) {
            throw new CardException(ISO7816.SW_UNKNOWN);
        }
        return channel.transmit(new CommandAPDU(
                cla, ins, p1, p2, data));
    }
}
