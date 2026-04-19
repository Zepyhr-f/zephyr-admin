package com.zephyr.ops.pojo.vo;

import lombok.Data;
import java.util.List;

@Data
public class ServerVO {
    private Cpu cpu = new Cpu();
    private Mem mem = new Mem();
    private Jvm jvm = new Jvm();
    private Sys sys = new Sys();
    private List<SysFile> sysFiles;

    @Data
    public static class Cpu {
        private int cpuNum;
        private double total;
        private double sys;
        private double used;
        private double wait;
        private double free;
    }

    @Data
    public static class Mem {
        private double total;
        private double used;
        private double free;
        private double usage;
    }

    @Data
    public static class Jvm {
        private double total;
        private double max;
        private double free;
        private double usage;
        private String version;
        private String home;
        private String name;
        private String startTime;
        private String runTime;
    }

    @Data
    public static class Sys {
        private String computerName;
        private String computerIp;
        private String userDir;
        private String osName;
        private String osArch;
    }

    @Data
    public static class SysFile {
        private String dirName;
        private String sysTypeName;
        private String typeName;
        private String total;
        private String free;
        private String used;
        private double usage;
    }
}
