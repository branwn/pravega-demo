package com.dellemc.pravega.chattingRoom;

import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.stream.*;
import io.pravega.client.stream.impl.JavaSerializer;

import java.net.URI;

public class Reader {
    public static void createReaderGroup(String url, String scope, String stream, String groupName) throws Exception {
        URI uri = new URI(url);
        ReaderGroupManager readerGroupManager = ReaderGroupManager.withScope(scope, uri);

        // clear reader group //////////////
        readerGroupManager.deleteReaderGroup(groupName);

//        readerGroupManager.createReaderGroup("numReader", readerGroupConfig);

        ReaderGroupConfig build = ReaderGroupConfig.builder().stream(scope + "/" + stream).build();
        readerGroupManager.createReaderGroup(groupName, build);
//        readerGroupManager.close();
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
        while (true) {
            EventRead<String> event = reader.readNextEvent(1000);
            if (event.getEvent() == null) {
//                System.out.println("[Debug] No more event");
                break;
            }
            System.out.println(event.getEvent());
        }
    }

    public static void main(String[] args) throws Exception {

        createReaderGroup("tcp://127.0.0.1:9090", "dell", "demo", "dellemc");
        EventStreamReader<String> reader = createReader("tcp://127.0.0.1:9090", "dell", "demo", "dellemc");
        readData(reader);
        reader.close();
    }
}
