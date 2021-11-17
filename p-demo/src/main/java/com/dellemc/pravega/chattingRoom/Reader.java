package com.dellemc.pravega.chattingRoom;

import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.stream.*;
import io.pravega.client.stream.impl.JavaSerializer;

import java.net.URI;

public class Reader {
    public static ReaderGroupManager createReaderGroup(String url, String scope, String stream, String groupName) throws Exception {
        URI uri = new URI(url);
        ReaderGroupManager readerGroupManager = ReaderGroupManager.withScope(scope, uri);
        // clear reader group //////////////
//        try {
//            readerGroupManager.deleteReaderGroup(groupName);
//        }catch (Exception e){
//
//        }
        ReaderGroupConfig readerGroupConfig = ReaderGroupConfig.builder().stream(scope + "/" + stream).build();
        readerGroupManager.createReaderGroup(groupName, readerGroupConfig);
//        readerGroupManager.close();
        return readerGroupManager;
    }

    public static EventStreamReader<String> createReader(String url, String scope, String readerId, String groupName) throws Exception {
        URI uri = new URI(url);
        ClientConfig build = ClientConfig.builder().controllerURI(uri).build();
        EventStreamClientFactory streamClientFactory = EventStreamClientFactory.withScope(scope, build);
        ReaderConfig build1 = ReaderConfig.builder().build();
        EventStreamReader<String> reader = streamClientFactory.createReader(readerId, groupName, new JavaSerializer<String>(), build1);
        return reader;
    }

    public static void readData(EventStreamReader<String> reader) {
        readData(reader, "");
    }

    public static void readData(EventStreamReader<String> reader, String prefix) {
        while (true) {
            EventRead<String> event = reader.readNextEvent(500);
            if (event.getEvent() == null) { break; }
            System.out.println(prefix + event.getEvent());
        }
    }

}
