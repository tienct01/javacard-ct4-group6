package auth;
public interface INFO_CONSTANTS {
	public final static short COUNT_ID=0;
	public final static short COUNT_HOTEN=1;
	public final static short COUNT_NGAYSINH=2;
	public final static short COUNT_GIOITINH=3;
	public final static short COUNT_SDT=4;
	public final static short COUNT_CMND=5;
	public final static short COUNT_DIACHI=6;
	public final static short COUNT_AVATAR=7;
	
	public final static short GET_ID=0;
	public final static short GET_HOTEN=1;
	public final static short GET_NGAYSINH=2;
	public final static short GET_GIOITINH=3;
	public final static short GET_SDT=4;
	public final static short GET_CMND=5;
	public final static short GET_DIACHI=6;
	
	final static byte INS_INSERT = (byte)0x01;
	final static byte INS_THONGTIN = (byte)0x02;
	final static byte INS_CHANGE_INFO= (byte)0x03;
	final static byte INS_CHANGE_IMAGE = (byte)0x04;
	final static short KEY_AES_LENGTH = (short)16; //MD5
	final static byte PARAMETER_INTERFACE = 0x11;
}
