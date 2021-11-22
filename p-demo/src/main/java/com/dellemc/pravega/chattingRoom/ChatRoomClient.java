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
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static com.dellemc.pravega.chattingRoom.ReaderFactory.*;
import static com.dellemc.pravega.chattingRoom.WriterFactory.*;

public class ChatRoomClient implements AutoCloseable {
    private static final String DEFAULT_SCOPE = "chattingRoom";
    private static final String DEFAULT_CONTROLLER_URI = "tcp://127.0.0.1:9090";
    private static final String FILE_TAG = "upload@";

    private final EventStreamWriter<byte[]> chatWriter;
    private final EventStreamReader<byte[]> chatReader;
    private final ReaderGroupManager chatReaderGroupManager;
    private final String SELF_NAME;
    private final int SELF_NAME_HASH;
    private final String CHAT_STREAM_NAME;
    private final String FILE_STREAM_NAME;

    private boolean sent_file_recently = false;



    // this function is used to read and print msg or receive a file from a specific stream reader
    protected void receiveData() {
        while (true) {
            EventRead<byte[]> event = chatReader.readNextEvent(200);
            if (event.getEvent() == null) { break; }

            String dataReceived = new String(event.getEvent());
            if (!dataReceived.startsWith(FILE_TAG)) {
                // print msg
                System.out.println(new String(event.getEvent()));
                continue;
            }
            if (sent_file_recently) {
                sent_file_recently = false;
                EventRead<byte[]> fileEvent = chatReader.readNextEvent(200);
                continue;
            }

            // receive a file
            dataReceived = dataReceived.substring(FILE_TAG.length());
            String[] directoryName = dataReceived.split("/");
            String fileName = directoryName[directoryName.length - 1];
            System.out.print("Receiving file: " + fileName + "...");
            EventRead<byte[]> fileEvent = chatReader.readNextEvent(200);
            byte[] bytes = fileEvent.getEvent();
            FileUtil.readFile(fileName, bytes);
            System.out.println("Done.");

        }
    }

    // this function is used to write data or a file to a specific stream reader
    protected void sendData(String message) throws Exception {
        if (message.startsWith(FILE_TAG)){
            chatWriter.writeEvent(message.getBytes());
            try {
                FileUtil.sendFile(chatWriter, message.substring(FILE_TAG.length()));
            } catch (Exception e) {
                System.out.println("[Debug] Fail to send the file.");
                return;
            }
            sent_file_recently = true;
            chatWriter.flush();
            System.out.println("Upload Finished!");
            return;
        }
        if (!message.equals("")) {
            chatWriter.writeEvent((SELF_NAME + ": " + message).getBytes());
            chatWriter.flush();
            return;
        }
    }

    // this function is used to close the writer, reader, and group_manager
    public void close() {
        // close
        chatWriter.close();
        chatReader.close();
        chatReaderGroupManager.deleteReaderGroup(SELF_NAME);
        chatReaderGroupManager.close();
    }

    protected ChatRoomClient(String selfName, int inboxHash) throws Exception {
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
