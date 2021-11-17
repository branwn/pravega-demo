package com.dellemc.pravega.chattingRoom;
import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.stream.EventStreamReader;
import io.pravega.client.stream.EventStreamWriter;

import static com.dellemc.pravega.chattingRoom.Reader.*;
import static com.dellemc.pravega.chattingRoom.Writer.*;

public class Chat {
    // createStream("tcp://127.0.0.1:9090","chattingRoom",peerName + "Inbox");
    EventStreamWriter<String> writer;
    EventStreamReader<String> reader;
    ReaderGroupManager readerGroupManager;
    String selfName;
    int selfNameHash;
    String inbox;
    int inboxHash;

    public void sendMsg(String msg) throws Exception {
        writeData(this.writer, selfName + ": " + msg);
    }

    public void recieveMsg() throws Exception {
        // read
        readData(this.reader);
    }

    public void close() {
        // close
        writer.close();
        reader.close();
        readerGroupManager.deleteReaderGroup(selfName);
        readerGroupManager.close();
    }

    public Chat(String selfName, int inboxHash) throws Exception {
        this.selfName = selfName;
        this.selfNameHash = selfName.hashCode();
        this.inboxHash = inboxHash;
        this.inbox = inboxHash + "";

        createStream("tcp://127.0.0.1:9090","chattingRoom",inbox);
        writer = getWriter("tcp://127.0.0.1:9090", "chattingRoom", inbox);
        readerGroupManager = createReaderGroup("tcp://127.0.0.1:9090", "chattingRoom", inbox, selfName);
        reader = createReader("tcp://127.0.0.1:9090", "chattingRoom", selfNameHash + "", selfName);
    }


}
