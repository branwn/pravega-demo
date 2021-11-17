package com.dellemc.pravega.chattingRoom;

import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.*;
import io.pravega.client.stream.impl.JavaSerializer;

import java.net.URI;

public class ReaderDup {




    public ReaderDup(String scopeName, String streamName) {

    }

    public static void main(String[] args) throws Exception {

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


        EventStreamClientFactory factory = EventStreamClientFactory
                .withScope("tutorial", clientConfig);



        ReaderGroupManager readerGroupManager = ReaderGroupManager
                .withScope("tutorial", clientConfig);



        ReaderGroupConfig readerGroupConfig = ReaderGroupConfig.builder()
                .stream("tutorial/numbers").build();

//        // clear reader group //////////////
//        readerGroupManager.deleteReaderGroup("numReader");

//        readerGroupManager.createReaderGroup("numReader", readerGroupConfig);


        EventStreamReader<Integer> reader = factory
                .createReader("myId2", "numReader",
                        new JavaSerializer<Integer>(), ReaderConfig.builder().build());

        Integer intEvent;
        for (int i = 0; i < 10; i++) {
            while ((intEvent = reader.readNextEvent(1000).getEvent()) != null) {
                System.out.println(intEvent);
            }
        }
        

        reader.close();

    }
}
