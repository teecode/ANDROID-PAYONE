package android_serialport_api;

import java.io.File;
import android.text.format.Time;

/**
 * Created by chams on 4/2/2018.
 */

public class SerialProperties {
    public final int ENABLE_BUTTON = 2;
    public final int SHOW_VERSION = 3;
    public final int UPDATE_FW = 4;
    public final int SHOW_PROGRESS = 5;
    public final int DISABLE_BUTTON = 6;
    public final int HIDE_PROGRESS=7;
    public final int REFRESH_PROGRESS=8;
    public final int SHOW_FONT_UPTAE_INFO=9;
    public final int SHOW_PRINTER_INFO_WHEN_INIT=10;
    public final byte  HDX_ST_NO_PAPER1 = (byte)(1<<0);     // 1 缺纸
    //public final byte  HDX_ST_BUF_FULL  = (byte)(1<<1);     // 1 缓冲满
    //public final byte  HDX_ST_CUT_ERR   = (byte)(1<<2);     // 1 打印机切刀错误
    public final byte  HDX_ST_HOT       = (byte)(1<<4);     // 1 打印机太热
    public final byte  HDX_ST_WORK      = (byte)(1<<5);     // 1 打印机在工作状态

    public boolean stop = false;
    public static int BinFileNum = 0;
    public static boolean ver_start_falg = false;
    public boolean Status_Start_Falg = false;
    public byte [] Status_Buffer=new byte[300];
    public int Status_Buffer_Index = 0;
    public static int update_ver_event = 0;
    public static boolean update_ver_event_err = false;
    public static StringBuilder strVer=new StringBuilder("922");
    public static StringBuilder oldVer=new StringBuilder("922");
    public static File BinFile;
    public static final String TAG = "GoldenChance";
    public static   String Error_State = "";
    public Time time = new Time();
    public int TimeSecond;

    public int iProgress   = 0;
    public String Printer_Info =new String();

    public static boolean flow_start_falg = false;
    public byte [] flow_buffer=new byte[300];


    public  static int get_ver_count = 0;
}
