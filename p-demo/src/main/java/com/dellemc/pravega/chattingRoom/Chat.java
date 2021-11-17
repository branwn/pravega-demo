package com.dellemc.pravega.chattingRoom;
import io.pravega.client.stream.EventStreamReader;
import io.pravega.client.stream.EventStreamWriter;

import static com.dellemc.pravega.chattingRoom.Reader.*;
import static com.dellemc.pravega.chattingRoom.Writer.*;

public class Chat {


    public Chat(String selfName, String peerName) throws Exception {
//        createStream("tcp://127.0.0.1:9090","chattingRoom",peerName + "Inbox");
        EventStreamWriter<String> writer = getWriter("tcp://127.0.0.1:9090", "chattingRoom", peerName + "Inbox");

        System.out.print("[Debug] Writing to " + peerName + "'s Inbox ...");

        for (int i = 0; i < 5; i++) {
            writeData(writer,i+"");
        }

        System.out.println("[Debug] Writing Finished");


        // read
        createReaderGroup("tcp://127.0.0.1:9090", "chattingRoom", selfName + "Inbox", "self");
        EventStreamReader<String> reader = createReader("tcp://127.0.0.1:9090", "chattingRoom", "myId", "self");
        System.out.print("[Debug] Reading " + selfName + "'s Inbox ...");
        readData(reader);
        System.out.println("[Debug] Reading Finished");


        // close
        writer.close();
        reader.close();

    }


}
