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
    String selfName, peerName, groupName;

    public void sendMsg(String msg) throws Exception {
        writeData(this.writer, selfName + ": " + msg);
    }

    public void recieveMsg() throws Exception {
        // read
        readData(reader, "");
    }

    public void close() {
        // close
        writer.close();
        reader.close();
        readerGroupManager.deleteReaderGroup(groupName);
        readerGroupManager.close();
    }

    public Chat(String selfName, String peerName) throws Exception {

        this.selfName = selfName;
        this.peerName = peerName;
        this.groupName = selfName;
        createStream("tcp://127.0.0.1:9090","chattingRoom",selfName + "Inbox");
        writer = getWriter("tcp://127.0.0.1:9090", "chattingRoom", peerName + "Inbox");
        readerGroupManager = createReaderGroup("tcp://127.0.0.1:9090", "chattingRoom", selfName + "Inbox", selfName);
        reader = createReader("tcp://127.0.0.1:9090", "chattingRoom", selfName + "ID", selfName);
    }


}
