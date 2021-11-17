package com.dellemc.pravega.chattingRoom;

import static com.dellemc.pravega.chattingRoom.Writer.createStream;

public class UserRegister {

    public static void chatRoomRegister(String myName) throws Exception {
        createStream("tcp://127.0.0.1:9090","chattingRoom",myName + "Inbox");
        System.out.println("\t" + myName + " has been successfully registered.");
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            chatRoomRegister(args[i]);
        }
    }

}
