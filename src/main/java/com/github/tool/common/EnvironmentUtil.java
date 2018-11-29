package com.github.tool.common;

/**
 * <p>运行环境工具包</p>
 *
 * @author PengCheng
 * @date 2018/11/1
 */
public class EnvironmentUtil {

    /**
     * jdk版本
     */
    private static final String jdkVersion;

    /**
     * 操作系统名
     */
    private static final String osName;

    /**
     * 操作系统版本
     */
    private static final String osVersion;

    //系统运行时加载环境配置
    static {
        jdkVersion = System.getProperty("java.version");
        osName = System.getProperty("os.name");
        osVersion = System.getProperty("os.version");
    }

    /**
     * 获取当前运行环境jdk版本
     * @return  {String}
     */
    public static String getJdkVersion(){
        return jdkVersion;
    }

    /**
     * 获取当前操作系统
     * @return  {OsType}
     */
    public static OsType getOsType(){
        for (OsType osType : OsType.values()){
            if (osVersion.equalsIgnoreCase("linux")){
                return osType;
            }
        }
        return null;
    }

    /**
     * 获取当前操作系统版本
     * @return
     */
    public static String getOsVersion(){
        return osVersion;
    }

}
