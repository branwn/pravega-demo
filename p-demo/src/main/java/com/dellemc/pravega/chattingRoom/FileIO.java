package com.dellemc.pravega.chattingRoom;

import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.stream.EventStreamReader;
import io.pravega.client.stream.EventStreamWriter;

import java.io.*;
import java.util.Arrays;

import static com.dellemc.pravega.chattingRoom.ReaderFactory.createReader;
import static com.dellemc.pravega.chattingRoom.ReaderFactory.createReaderGroup;
import static com.dellemc.pravega.chattingRoom.WriterFactory.createStream;
import static com.dellemc.pravega.chattingRoom.WriterFactory.getWriter;


public class FileIO {

    protected static final String DEFAULT_SCOPE = "chattingRoom";
    protected static final String DEFAULT_CONTROLLER_URI = "tcp://127.0.0.1:9090";


    public static void main(String[] args) throws Exception {


        String selfName = "fileInbox";
        int selfNameHash = selfName.hashCode();
        String streamName = "fileInbox";



        createStream(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, streamName);
        EventStreamWriter<String> writer = getWriter(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, streamName);
        ReaderGroupManager readerGroupManager = createReaderGroup(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, streamName, selfName);
        EventStreamReader<String> reader = createReader(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, selfNameHash + "", selfName);


//        Person person = new Person();
//        person.setAge(20);
//        person.setName("Joe");

        File f = new File("./text.txt");

        // read
        FileInputStream fileInputStream
                = new FileInputStream(f);
        ObjectInputStream objectInputStream
                = new ObjectInputStream(fileInputStream);
        File f2 = (File) objectInputStream.readObject();
        objectInputStream.close();


        File dest = new File("./text2.txt");
        f2.renameTo(dest);

        // write
        FileOutputStream fileOutputStream
                = new FileOutputStream(f2);
        ObjectOutputStream objectOutputStream
                = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(f2);
        objectOutputStream.flush();
        objectOutputStream.close();

    }
}

class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    static String country = "ITALY";
    private int age;
    private String name;
    transient int height;

    // getters and setters


    public static String getCountry() {
        return country;
    }

    public static void setCountry(String country) {
        Person.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}