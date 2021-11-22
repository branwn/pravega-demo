package com.dellemc.pravega.chattingRoom;
import io.pravega.client.stream.EventStreamWriter;

import java.io.*;

public class FileUtil {
    protected static final String FILES_RECEIVED_FOLDER = "./files_received/";

    public static byte[] file_to_bytes(File file)  throws IOException {
        FileInputStream fl = new FileInputStream(file);
        byte[] arr = new byte[(int)file.length()];
        fl.read(arr);
        fl.close();
        return arr;
    }

    public static void bytes_to_file(byte[] bytes, File file)  {
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
        byte[] bytes = file_to_bytes(path);
        writer.writeEvent(bytes);

    }

    public static void readFile(String fileName, byte[] bytes) {
        try{
            File dest = new File(FILES_RECEIVED_FOLDER + "/" + fileName);
            if (!dest.exists()) {
                dest.createNewFile();
            }
            bytes_to_file(bytes, dest);
        }catch (Exception e) {
            System.out.println("[Debug] Fail to receive the file.");
        }
    }
}
