package com.dellemc.pravega.chattingRoom;

import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.EventStreamWriter;
import io.pravega.client.stream.EventWriterConfig;
import io.pravega.client.stream.StreamConfiguration;
import io.pravega.client.stream.impl.JavaSerializer;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

public class Writer {

    public static void writeData(EventStreamWriter<String> writer, String message) throws Exception {
        CompletableFuture<Void> future = writer.writeEvent(message);
        future.get();
    }

    public static EventStreamWriter<String> getWriter(String url, String scope, String stream) throws Exception {
        URI uri = new URI(url);
        ClientConfig build = ClientConfig.builder().controllerURI(uri).build();
        EventStreamClientFactory streamClientFactory = EventStreamClientFactory.withScope(scope, build);
        EventWriterConfig writerConfig = EventWriterConfig.builder().build();
        return streamClientFactory.createEventWriter(stream, new JavaSerializer<String>(), writerConfig);
    }


    public static void createStream(String url, String scope, String stream)throws  Exception{
        URI uri = new URI(url);
        StreamManager streamManager = StreamManager.create(uri);
        streamManager.createScope(scope);

        StreamConfiguration build = StreamConfiguration.builder().build();
        streamManager.createStream(scope, stream, build);
    }

    public static void main(String[] args) throws Exception {
        createStream("tcp://127.0.0.1:9090","dell","demo");
        EventStreamWriter<String> writer = getWriter("tcp://127.0.0.1:9090", "dell", "demo");
        writeData(writer,"EDG niubi");
        System.out.println("write data successfully");
        writer.close();
    }
}
