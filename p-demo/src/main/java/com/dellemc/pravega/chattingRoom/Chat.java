/*
This class would maintain a Writer to write to the inbox and a Reader to read the inbox,
with implementing AutoCloseable;
*/

package com.dellemc.pravega.chattingRoom;
import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.stream.EventRead;
import io.pravega.client.stream.EventStreamReader;
import io.pravega.client.stream.EventStreamWriter;

import java.io.*;
import java.util.concurrent.CompletableFuture;

import static com.dellemc.pravega.chattingRoom.ReaderFactory.*;
import static com.dellemc.pravega.chattingRoom.WriterFactory.*;

public class Chat implements AutoCloseable {
    protected static final String DEFAULT_SCOPE = "chattingRoom";
    protected static final String DEFAULT_CONTROLLER_URI = "tcp://127.0.0.1:9090";
    protected static final String FILE_TAG = "upload@";

    protected EventStreamWriter<byte[]> chatWriter;
    protected EventStreamReader<byte[]> chatReader;
    protected ReaderGroupManager chatReaderGroupManager;
    protected final String SELF_NAME;
    protected final int SELF_NAME_HASH;
    protected final String CHAT_STREAM_NAME;
    protected final String FILE_STREAM_NAME;



    public static byte[] file_to_bytes(File file)  throws IOException {

        // Creating an object of FileInputStream to
        // read from a file
        FileInputStream fl = new FileInputStream(file);

        // Now creating byte array of same length as file
        byte[] arr = new byte[(int)file.length()];

        // Reading file content to byte array
        // using standard read() method
        fl.read(arr);

        // lastly closing an instance of file input stream
        // to avoid memory leakage
        fl.close();

        // Returning above byte array
        return arr;
    }

    static void bytes_to_file(byte[] bytes, File file)  {
        try {

            // Initialize a pointer
            // in file using OutputStream
            OutputStream
                    os
                    = new FileOutputStream(file);

            // Starts writing the bytes in it
            os.write(bytes);
            System.out.println("Successfully"
                    + " byte inserted");

            // Close the file
            os.close();
        }

        catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }


    public void sendFile() {
        try{
            File path = new File("./from.txt");
            byte[] bytes = file_to_bytes(path);
            chatWriter.writeEvent(bytes);
        }catch (Exception e) {
            System.out.println("[Debug] Fail to send the file.");
        }

    }

    public void readFile() {
        try{
            File dest = new File("./dest.txt");
            if (!dest.exists()) {
                dest.createNewFile();
            }
            EventRead<byte[]> event = chatReader.readNextEvent(100);
            if (event.getEvent() != null) {
                byte[] bytes = event.getEvent();
                bytes_to_file(bytes, dest);
            }
        }catch (Exception e) {
            System.out.println("[Debug] Fail to receive the file.");
        }
    }

    // this function is used to read and print data from a specific stream reader
    public void receiveMsg() {
        while (true) {
            EventRead<byte[]> event = chatReader.readNextEvent(500);
            if (event.getEvent() == null) { break; }
            System.out.println(new String(event.getEvent()));
        }
    }

    // this function is used to write data to a specific stream reader
    public void sendMsg(String message) throws Exception {
        CompletableFuture<Void> future = chatWriter.writeEvent((SELF_NAME + ": " + message).getBytes());
        future.get();
    }

    // this function is used to close the writer, reader, and group_manager
    public void close() {
        // close
        chatWriter.close();
        chatReader.close();
        chatReaderGroupManager.deleteReaderGroup(SELF_NAME);
        chatReaderGroupManager.close();
    }

    public Chat(String selfName, int inboxHash) throws Exception {
        this.SELF_NAME = selfName;
        this.SELF_NAME_HASH = selfName.hashCode();
        this.CHAT_STREAM_NAME = inboxHash + "";
        this.FILE_STREAM_NAME = inboxHash + 1 + "";

        // create chat stream
        createStream(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, CHAT_STREAM_NAME);
        chatWriter = getWriter(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, CHAT_STREAM_NAME);
        chatReaderGroupManager = createReaderGroup(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, CHAT_STREAM_NAME, SELF_NAME);
        chatReader = createReader(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, SELF_NAME_HASH + "", SELF_NAME);

    }


}
