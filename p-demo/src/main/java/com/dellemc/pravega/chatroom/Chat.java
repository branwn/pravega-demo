package com.dellemc.pravega.chatroom;
import io.pravega.client.stream.EventStreamReader;
import io.pravega.client.stream.EventStreamWriter;

import static com.dellemc.pravega.chatroom.Reader.*;
import static com.dellemc.pravega.chatroom.Writer.*;

public class Chat {


    public Chat(String selfName, String peerName) throws Exception {
        createStream("tcp://127.0.0.1:9090","dell","demo");
        EventStreamWriter<String> writer = getWriter("tcp://127.0.0.1:9090", "dell", "demo");
        writeData(writer,"EDG niubi");
        System.out.println("write data successfully");



        // read
        createReaderGroup("tcp://127.0.0.1:9090", "dell", "demo", "dellemc");
        EventStreamReader<String> reader = createReader("tcp://127.0.0.1:9090", "dell", "demo", "dellemc");
        readData(reader);


        // close
        writer.close();
        reader.close();

        // close factories

    }


}
