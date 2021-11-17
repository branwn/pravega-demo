package com.dellemc.pravega.chattingRoom;

import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.*;
import io.pravega.client.stream.impl.JavaSerializer;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class WriterDup {



    public static void main(String[] args) throws InterruptedException  {


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

        for(int i = 0; i < 50; i += 1){
            TimeUnit.MILLISECONDS.sleep(1000);
            System.out.println(i);
            writer.writeEvent(i);
            writer.flush();
        }
        System.out.println("writing finished");


    }
}
