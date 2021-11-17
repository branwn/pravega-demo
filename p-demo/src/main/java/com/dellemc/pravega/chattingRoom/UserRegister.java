package com.dellemc.pravega.chattingRoom;

import static com.dellemc.pravega.chattingRoom.Writer.createStream;

public class UserRegister {

    public static void chatRoomRegister(String stream) throws Exception {
        createStream("tcp://127.0.0.1:9090","chattingRoom",stream);
        System.out.println("\t" + stream + "(Room Hash Code) has been successfully create.");
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            chatRoomRegister(args[i]);
        }
    }

}
