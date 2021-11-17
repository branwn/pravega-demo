package com.dellemc.pravega.chattingRoom;
import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.stream.EventStreamReader;
import io.pravega.client.stream.EventStreamWriter;

import static com.dellemc.pravega.chattingRoom.Reader.*;
import static com.dellemc.pravega.chattingRoom.Writer.*;

public class Chat {
    //        createStream("tcp://127.0.0.1:9090","chattingRoom",peerName + "Inbox");
    EventStreamWriter<String> writer;
    EventStreamReader<String> reader;
    ReaderGroupManager readerGroupManager;
    String selfName, peerName;

    public void sendMsg(String msg) throws Exception {
        System.out.println("[Debug] Writing to " + this.peerName + "'s Inbox ...");
        writeData(this.writer,"Hello from " + this.selfName);
        System.out.println("[Debug] Writing Finished");
    }

    public void recievedMsg() throws Exception {
        // read
        System.out.println("[Debug] Reading " + this.selfName + "'s Inbox ...");
        readData(reader);
        System.out.println("[Debug] Reading Finished");
    }

    public void close() {
        // close
        writer.close();
        reader.close();
        readerGroupManager.close();
    }

    public Chat(String selfName, String peerName) throws Exception {
        this.selfName = selfName;
        this.peerName = peerName;
        createStream("tcp://127.0.0.1:9090","chattingRoom",selfName + "Inbox");
        writer = getWriter("tcp://127.0.0.1:9090", "chattingRoom", peerName + "Inbox");
        readerGroupManager = createReaderGroup("tcp://127.0.0.1:9090", "chattingRoom", selfName + "Inbox", "self");
        reader = createReader("tcp://127.0.0.1:9090", "chattingRoom", "myId", "self");
    }


}
