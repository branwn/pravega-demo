package com.dellemc.pravega.chattingRoom;
import io.pravega.client.stream.EventStreamWriter;

import java.io.*;

public class FileUtil {
    private static final String FILES_RECEIVED_FOLDER = "./files_received/";

    // this function is used to encode a file to byte[] for sending using convenient
    private static byte[] fileToBytes(File file)  throws IOException {
        FileInputStream fl = new FileInputStream(file);
        byte[] arr = new byte[(int)file.length()];
        fl.read(arr);
        fl.close();
        return arr;
    }

    // this function is used to decode a file from a byte[]
    private static void bytesToFile(byte[] bytes, File file)  {
        try {
            OutputStream os = new FileOutputStream(file);
            os.write(bytes);
            os.close();
        }
        catch (Exception e) {
            System.out.println("[Debug] Fail to receive a file.");
            System.out.println("Exception: " + e);
        }
    }


    public static void sendFile(EventStreamWriter<byte[]> writer, String fileName) throws IOException {
        File path = new File(fileName);
        byte[] bytes = fileToBytes(path);
        writer.writeEvent(bytes);
    }

    public static void readFile(String fileName, byte[] bytes) {
        try{
            File dest = new File(FILES_RECEIVED_FOLDER + "/" + fileName);
            if (!dest.exists()) {
                dest.createNewFile();
            }
            bytesToFile(bytes, dest);
        }catch (Exception e) {
            System.out.println("[Debug] Fail to receive the file.");
        }
    }
}
