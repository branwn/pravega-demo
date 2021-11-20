/*
This class would maintain a Writer to write to the inbox and a Reader to read the inbox,
*/

package com.dellemc.pravega.chattingRoom;
import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.stream.EventRead;
import io.pravega.client.stream.EventStreamReader;
import io.pravega.client.stream.EventStreamWriter;

import java.util.concurrent.CompletableFuture;

import static com.dellemc.pravega.chattingRoom.ReaderFactory.*;
import static com.dellemc.pravega.chattingRoom.WriterFactory.*;

public class Chat {
    EventStreamWriter<String> writer;
    EventStreamReader<String> reader;
    ReaderGroupManager readerGroupManager;
    String selfName;
    int selfNameHash;
    String inboxStream;


    // this function is used to read and print data from a specific stream reader
    public void recieveMsg() {
        while (true) {
            EventRead<String> event = reader.readNextEvent(500);
            if (event.getEvent() == null) { break; }
            System.out.println(event.getEvent());
        }
    }

    // this function is used to write data to a specific stream reader
    public void sendMsg(String message) throws Exception {
        CompletableFuture<Void> future = writer.writeEvent(selfName + ": " + message);
        future.get();
    }

    // this function is used to close the writer, reader, and group_manager
    public void close() {
        // close
        writer.close();
        reader.close();
        readerGroupManager.deleteReaderGroup(selfName);
        readerGroupManager.close();
    }

    public Chat(String selfName, String inboxStream) throws Exception {
        this.selfName = selfName;
        this.selfNameHash = selfName.hashCode();
        this.inboxStream = inboxStream;

        createStream("tcp://127.0.0.1:9090","chattingRoom", this.inboxStream);
        writer = getWriter("tcp://127.0.0.1:9090", "chattingRoom", this.inboxStream);
        readerGroupManager = createReaderGroup("tcp://127.0.0.1:9090", "chattingRoom", this.inboxStream, selfName);
        reader = createReader("tcp://127.0.0.1:9090", "chattingRoom", selfNameHash + "", selfName);
    }


}
