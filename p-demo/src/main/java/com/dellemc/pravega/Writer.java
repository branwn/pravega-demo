package com.dellemc.pravega;

import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.*;
import io.pravega.client.stream.impl.JavaSerializer;

import java.net.URI;

public class Writer {



    public static void main(String[] args)  {


        StreamConfiguration streamConfig = StreamConfiguration.builder()
                .scalingPolicy(ScalingPolicy.fixed(1))
                .build();
        URI controllerURI = URI.create("tcp://localhost:9090");
        try (StreamManager streamManager = StreamManager.create(controllerURI)) {
            streamManager.createScope("tutorial");
            streamManager.createStream("tutorial", "numbers", streamConfig);
        }
        ClientConfig clientConfig = ClientConfig.builder()
                .controllerURI(controllerURI).build();
        EventWriterConfig writerConfig = EventWriterConfig.builder().build();
        EventStreamClientFactory factory = EventStreamClientFactory
                .withScope("tutorial", clientConfig);
        EventStreamWriter<Integer> writer = factory
                .createEventWriter("numbers", new JavaSerializer<Integer>(), writerConfig);

        writer.writeEvent(1);
        writer.writeEvent(2);
        writer.writeEvent(3);
        writer.flush();




    }
}