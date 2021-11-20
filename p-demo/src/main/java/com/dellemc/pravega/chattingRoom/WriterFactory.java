package com.dellemc.pravega.chattingRoom;

import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.EventStreamWriter;
import io.pravega.client.stream.EventWriterConfig;
import io.pravega.client.stream.StreamConfiguration;
import io.pravega.client.stream.impl.JavaSerializer;
/*
This Writer class contains a writer factory and a stream creator
*/

import java.net.URI;
import java.util.concurrent.CompletableFuture;

public class WriterFactory {

    // the writer factory
    public static EventStreamWriter<byte[]> getWriter(String url, String scope, String stream) throws Exception {
        URI uri = new URI(url);
        ClientConfig build = ClientConfig.builder().controllerURI(uri).build();
        EventStreamClientFactory streamClientFactory = EventStreamClientFactory.withScope(scope, build);
        EventWriterConfig writerConfig = EventWriterConfig.builder().build();
        return streamClientFactory.createEventWriter(stream, new JavaSerializer<>(), writerConfig);
    }

    // the stream creator
    public static void createStream(String url, String scope, String stream) throws Exception{
        URI uri = new URI(url);
        StreamManager streamManager = StreamManager.create(uri);
        streamManager.createScope(scope);
        if (!streamManager.checkStreamExists(scope, stream)){
            StreamConfiguration build = StreamConfiguration.builder().build();
            streamManager.createStream(scope, stream, build);
        }
    }

}
