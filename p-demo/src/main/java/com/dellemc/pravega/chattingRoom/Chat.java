/*
This class would maintain a Writer to write to the inbox and a Reader to read the inbox,
with implementing AutoCloseable;
*/

package com.dellemc.pravega.chattingRoom;
import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.stream.EventRead;
import io.pravega.client.stream.EventStreamReader;
import io.pravega.client.stream.EventStreamWriter;

import java.util.concurrent.CompletableFuture;

import static com.dellemc.pravega.chattingRoom.ReaderFactory.*;
import static com.dellemc.pravega.chattingRoom.WriterFactory.*;

public class Chat implements AutoCloseable {
    protected static final String DEFAULT_SCOPE = "chattingRoom";
    protected static final String DEFAULT_CONTROLLER_URI = "tcp://127.0.0.1:9090";

    protected EventStreamWriter<byte[]> chatWriter;
    protected EventStreamReader<byte[]> chatReader;
    protected ReaderGroupManager chatReaderGroupManager;
    protected EventStreamWriter<byte[]> fileWriter;
    protected EventStreamReader<byte[]> fileReader;
    protected ReaderGroupManager fileReaderGroupManager;
    protected final String SELF_NAME;
    protected final int SELF_NAME_HASH;
    protected final String CHAT_STREAM_NAME;
    protected final String FILE_STREAM_NAME;


    // this function is used to read and print data from a specific stream reader
    public void recieveMsg() {
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

        System.out.println(CHAT_STREAM_NAME);
        System.out.println(FILE_STREAM_NAME);

        createStream(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, CHAT_STREAM_NAME);
        chatWriter = getWriter(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, CHAT_STREAM_NAME);

        chatReaderGroupManager = createReaderGroup(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, CHAT_STREAM_NAME, SELF_NAME);
        chatReader = createReader(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, SELF_NAME_HASH + "", SELF_NAME);


//        createStream(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, INBOXHASH);
//        chatWriter = getWriter(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, INBOXHASH);
//
//        chatReaderGroupManager = createReaderGroup(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, INBOXHASH, SELFNAME);
//        chatReader = createReader(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, SELFNAMEHASH + "", SELFNAME);


    }


}
