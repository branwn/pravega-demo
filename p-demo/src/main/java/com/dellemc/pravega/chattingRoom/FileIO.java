package com.dellemc.pravega.chattingRoom;

import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.stream.EventRead;
import io.pravega.client.stream.EventStreamReader;
import io.pravega.client.stream.EventStreamWriter;

import java.io.*;

import static com.dellemc.pravega.chattingRoom.ReaderFactory.createReader;
import static com.dellemc.pravega.chattingRoom.ReaderFactory.createReaderGroup;
import static com.dellemc.pravega.chattingRoom.WriterFactory.createStream;
import static com.dellemc.pravega.chattingRoom.WriterFactory.getWriter;



public class FileIO {

    protected static final String DEFAULT_SCOPE = "chattingRoom";
    protected static final String DEFAULT_CONTROLLER_URI = "tcp://127.0.0.1:9090";

    protected EventStreamWriter<byte[]> writer;
    protected ReaderGroupManager readerGroupManager;
    protected EventStreamReader<byte[]> reader;
    protected final String SELFNAME;
    protected final int SELFNAMEHASH;
    protected final String SELFNAME_READ;
    protected final int SELFNAME_READHASH;
    protected final String STREAMNAME;



    public static byte[] file_to_bytes(File file)  throws IOException {

        // Creating an object of FileInputStream to
        // read from a file
        FileInputStream fl = new FileInputStream(file);

        // Now creating byte array of same length as file
        byte[] arr = new byte[(int)file.length()];

        // Reading file content to byte array
        // using standard read() method
        fl.read(arr);

        // lastly closing an instance of file input stream
        // to avoid memory leakage
        fl.close();

        // Returning above byte array
        return arr;
    }

    static void bytes_to_file(byte[] bytes, File file)  {
        try {

            // Initialize a pointer
            // in file using OutputStream
            OutputStream
                    os
                    = new FileOutputStream(file);

            // Starts writing the bytes in it
            os.write(bytes);
            System.out.println("Successfully"
                    + " byte inserted");

            // Close the file
            os.close();
        }

        catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    // this function is used to close the writer, reader, and group_manager
    public void close() {
        // close
        writer.close();
        reader.close();
        readerGroupManager.deleteReaderGroup(SELFNAME);
        readerGroupManager.close();
    }

    public FileIO() {

        this.SELFNAME = "filetrans";
        this.SELFNAMEHASH = SELFNAME.hashCode();
        this.SELFNAME_READ = SELFNAME + "read";
        this.SELFNAME_READHASH = this.SELFNAME_READ.hashCode();
        this.STREAMNAME = "fileInbox6";

        try {
            createStream(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, STREAMNAME);
            this.writer = getWriter(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, STREAMNAME);


            this.readerGroupManager = createReaderGroup(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, STREAMNAME, SELFNAME_READ);
            this.reader = createReader(DEFAULT_CONTROLLER_URI, DEFAULT_SCOPE, SELFNAME_READHASH + "", SELFNAME_READ);


            // write
            File path = new File("./text.txt");
            byte[] array = file_to_bytes(path);
            writer.writeEvent(array);
            //
            //Files.readAllBytes() method
            //byte[] array = Files.readAllBytes(path.toPath());


            // read and save
            File dest = new File("./temp/text.txt");
            if (!dest.exists()) {
                dest.createNewFile();
            }
            EventRead<byte[]> event = reader.readNextEvent(500);
            if (event.getEvent() != null) {
                byte[] bytes = event.getEvent();
                bytes_to_file(bytes, dest);
            }
        } catch (Exception e){
            // close

            close();
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
        }



    }

    public static void main(String[] args) throws Exception {

        FileIO myFIO = new FileIO();

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