package com.bonree.common.util;

import io.netty.channel.EventLoopGroup;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class ResourceRelease {
    private static Logger log = LoggerFactory.getLogger(ResourceRelease.class);

    public static void release(PrintWriter out) {
        if (out != null) {
            out.close();
        }
    }

    public static void release(BufferedReader in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                log.error("BufferedReader resource release exception", e);
            }
        }
    }

    public static void release(Socket client) {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                log.error("Socket resource release exception", e);
            }
        }
    }


    public static void release(FileReader fileReader) {
        if (fileReader != null) {
            try {
                fileReader.close();
            } catch (IOException e) {
                log.error("FileReader resource release exception", e);
            }
        }
    }

    public static void release(FileWriter fileWriter) {
        if (fileWriter != null) {
            try {
                fileWriter.close();
            } catch (IOException e) {
                log.error("FileWriter resource release exception", e);
            }
        }
    }

    public static void release(EventLoopGroup group) {
        if (group != null) {
            group.shutdownGracefully();
        }
    }
    public static void release(ZooKeeper zk) {
        if (zk != null) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                log.error("ZooKeeper resource release exception", e);
            }
        }
    }

    public static void close(TelnetClient telnetClient) {
        try {
            if (telnetClient.isConnected()) {
                telnetClient.disconnect();
            }
        } catch (IOException e) {
            log.error("TelnetClient resource release exception", e);
        }
    }
}
