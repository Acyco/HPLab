package cn.acyco.lab.util;

import java.util.Locale;

/**
 * @author Acyco
 * @create 2020-01-27 23:28
 */
public class Util {
    public static Util.EnumOS getOSType() {
        String s = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        if (s.contains("win"))
        {
            return Util.EnumOS.WINDOWS;
        }
        else if (s.contains("mac"))
        {
            return Util.EnumOS.OSX;
        }
        else if (s.contains("linux"))
        {
            return Util.EnumOS.LINUX;
        }
        else
        {
            return s.contains("unix") ? Util.EnumOS.LINUX : Util.EnumOS.UNKNOWN;
        }
    }
    public static enum EnumOS{
        LINUX,
        WINDOWS,
        OSX,
        UNKNOWN;
    }
}
