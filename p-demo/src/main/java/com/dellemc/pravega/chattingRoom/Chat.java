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

    protected EventStreamWriter<byte[]> writer;
    protected EventStreamReader<byte[]> reader;
    protected ReaderGroupManager readerGroupManager;
    protected final String SELFNAME;
    protected final int SELFNAMEHASH;
    protected final String STREAMNAME;


    // this function is used to read and print data from a specific stream reader
    public void recieveMsg() {
        while (true) {
            EventRead<byte[]> event = reader.readNextEvent(500);
            if (event.getEvent() == null) { break; }
            System.out.println(new String(event.getEvent()));
        }
    }

    // this function is used to write data to a specific stream reader
    public void sendMsg(String message) throws Exception {
        CompletableFuture<Void> future = writer.writeEvent((SELFNAME + ": " + message).getBytes());
        future.get();
    }

    // this function is used to close the writer, reader, and group_manager
    public void close() {
        // close
        writer.close();
        reader.close();
        readerGroupManager.deleteReaderGroup(SELFNAME);
        readerGroupManager.close();
    }

    public Chat(String selfName, String inboxStream) throws Exception {
        this.SELFNAME = selfName;
        this.SELFNAMEHASH = selfName.hashCode();
        this.STREAMNAME = inboxStream;

        createStream(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, STREAMNAME);
        writer = getWriter(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, STREAMNAME);

        readerGroupManager = createReaderGroup(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, STREAMNAME, SELFNAME);
        reader = createReader(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, SELFNAMEHASH + "", SELFNAME);
    }


}
