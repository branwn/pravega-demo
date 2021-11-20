/*
This Reader class contains a reader group factory and a stream reader factory
*/


package com.dellemc.pravega.chattingRoom;

import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.stream.*;
import io.pravega.client.stream.impl.JavaSerializer;

import java.net.URI;


public class ReaderFactory {
    // this is a reader group factory
    public static ReaderGroupManager createReaderGroup(String url, String scope, String stream, String groupName) throws Exception {
        URI uri = new URI(url);
        ReaderGroupManager readerGroupManager = ReaderGroupManager.withScope(scope, uri);
        ReaderGroupConfig readerGroupConfig = ReaderGroupConfig.builder().stream(scope + "/" + stream).build();
        readerGroupManager.createReaderGroup(groupName, readerGroupConfig);
        return readerGroupManager;
    }

    // this is a stream reader factory
    public static EventStreamReader<String> createReader(String url, String scope, String readerId, String groupName) throws Exception {
        URI uri = new URI(url);
        ClientConfig build = ClientConfig.builder().controllerURI(uri).build();
        EventStreamClientFactory streamClientFactory = EventStreamClientFactory.withScope(scope, build);
        ReaderConfig build1 = ReaderConfig.builder().build();
        EventStreamReader<String> reader = streamClientFactory.createReader(readerId, groupName, new JavaSerializer<>(), build1);
        return reader;
    }



}
