package com.dellemc.pravega.chatroom;
import io.pravega.client.stream.EventStreamReader;
import io.pravega.client.stream.EventStreamWriter;

import static com.dellemc.pravega.chatroom.Reader.*;
import static com.dellemc.pravega.chatroom.Writer.*;

public class Chat {


    public Chat(String selfName, String peerName) throws Exception {
        createStream("tcp://127.0.0.1:9090","dell","demo");
        EventStreamWriter<String> writer = getWriter("tcp://127.0.0.1:9090", "dell", "demo");

        System.out.println("[Debug] Writing...");

        for (int i = 0; i < 5; i++) {
            writeData(writer,i+"");
        }

        System.out.println("[Debug] Writing Finished");



        // read
        createReaderGroup("tcp://127.0.0.1:9090", "dell", "demo", "dellemc");
        EventStreamReader<String> reader = createReader("tcp://127.0.0.1:9090", "dell", "demo", "dellemc");
        System.out.println("[Debug] Reading...");
        readData(reader);
        System.out.println("[Debug] Reading Finished");

        // close
        writer.close();
        reader.close();


    }


}
