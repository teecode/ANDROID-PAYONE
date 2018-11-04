package android_serialport_api;

/**
 * Created by chams on 4/7/2018.
 */

public class CharacterSequence {
    public final int[] ClearCommand = new int[]{ 0x0a};
    public final int[] LineFeed = new int[]{ 0x1b, 0x4a, 0x30};
    public final int[] DoubleHeight = new int[]{ 0x1D, 0x21, 0x01};
    public final int[] DoubleHeightCancel = new int[]{ 0x1D, 0x21, 0x00};
    public final int[] DoubleWeight = new int[]{ 0x1D, 0x21, 0x10};
    public final int[] DoubleWeightCancel = new int[]{ 0x1D, 0x21, 0x00};



}
