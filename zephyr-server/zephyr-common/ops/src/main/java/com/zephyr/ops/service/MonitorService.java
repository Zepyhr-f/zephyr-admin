package com.zephyr.ops.service;

import com.zephyr.ops.pojo.vo.ServerVO;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
public class MonitorService {

    private static final int OSHI_WAIT_SECOND = 1000;

    public ServerVO getServerInfo() {
        ServerVO vo = new ServerVO();
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        
        setCpuInfo(vo, hal.getProcessor());
        setMemInfo(vo, hal.getMemory());
        setSysInfo(vo);
        setJvmInfo(vo);
        setSysFiles(vo, si.getOperatingSystem());
        
        return vo;
    }

    private void setCpuInfo(ServerVO vo, CentralProcessor processor) {
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long total = user + sys + idle;
        
        ServerVO.Cpu cpu = vo.getCpu();
        cpu.setCpuNum(processor.getLogicalProcessorCount());
        cpu.setTotal(total);
        cpu.setSys(Double.parseDouble(String.format("%.2f", 100.0 * sys / total)));
        cpu.setUsed(Double.parseDouble(String.format("%.2f", 100.0 * user / total)));
        cpu.setFree(Double.parseDouble(String.format("%.2f", 100.0 * idle / total)));
    }

    private void setMemInfo(ServerVO vo, GlobalMemory memory) {
        ServerVO.Mem mem = vo.getMem();
        long total = memory.getTotal();
        long available = memory.getAvailable();
        mem.setTotal(formatGb(total));
        mem.setUsed(formatGb(total - available));
        mem.setFree(formatGb(available));
        mem.setUsage(Double.parseDouble(String.format("%.2f", 100.0 * (total - available) / total)));
    }

    private void setSysInfo(ServerVO vo) {
        ServerVO.Sys sys = vo.getSys();
        Properties props = System.getProperties();
        sys.setComputerName(getHostName());
        sys.setComputerIp(getHostIp());
        sys.setOsName(props.getProperty("os.name"));
        sys.setOsArch(props.getProperty("os.arch"));
        sys.setUserDir(props.getProperty("user.dir"));
    }

    private void setJvmInfo(ServerVO vo) {
        ServerVO.Jvm jvm = vo.getJvm();
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        double total = formatMb(Runtime.getRuntime().totalMemory());
        double max = formatMb(Runtime.getRuntime().maxMemory());
        double free = formatMb(Runtime.getRuntime().freeMemory());
        
        jvm.setTotal(total);
        jvm.setMax(max);
        jvm.setFree(free);
        jvm.setUsage(Double.parseDouble(String.format("%.2f", 100.0 * (total - free) / total)));
        jvm.setVersion(System.getProperty("java.version"));
        jvm.setHome(System.getProperty("java.home"));
        jvm.setName(runtime.getName());
        jvm.setStartTime(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(runtime.getStartTime())));
        jvm.setRunTime(getDuration(runtime.getUptime()));
    }

    private void setSysFiles(ServerVO vo, OperatingSystem os) {
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsList = fileSystem.getFileStores();
        List<ServerVO.SysFile> sysFiles = new ArrayList<>();
        for (OSFileStore fs : fsList) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            ServerVO.SysFile sysFile = new ServerVO.SysFile();
            sysFile.setDirName(fs.getMount());
            sysFile.setSysTypeName(fs.getType());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(formatGbStr(total));
            sysFile.setFree(formatGbStr(free));
            sysFile.setUsed(formatGbStr(used));
            sysFile.setUsage(total == 0 ? 0 : Double.parseDouble(String.format("%.2f", 100.0 * used / total)));
            sysFiles.add(sysFile);
        }
        vo.setSysFiles(sysFiles);
    }

    private double formatGb(long bytes) { return Double.parseDouble(String.format("%.2f", bytes / 1024.0 / 1024.0 / 1024.0)); }
    private String formatGbStr(long bytes) { return String.format("%.2f GB", bytes / 1024.0 / 1024.0 / 1024.0); }
    private double formatMb(long bytes) { return Double.parseDouble(String.format("%.2f", bytes / 1024.0 / 1024.0)); }

    private String getHostName() {
        try { return InetAddress.getLocalHost().getHostName(); }
        catch (UnknownHostException e) { return "未知"; }
    }

    private String getHostIp() {
        try { return InetAddress.getLocalHost().getHostAddress(); }
        catch (UnknownHostException e) { return "127.0.0.1"; }
    }

    private String getDuration(long uptime) {
        long days = uptime / (1000 * 60 * 60 * 24);
        long hours = (uptime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (uptime % (1000 * 60 * 60)) / (1000 * 60);
        return days + "天" + hours + "小时" + minutes + "分钟";
    }
}
