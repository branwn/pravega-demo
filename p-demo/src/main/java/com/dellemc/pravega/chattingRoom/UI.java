package com.dellemc.pravega.chattingRoom;


public class UI {
    public static void main(String[] args) throws Exception {
//        Chat my_chat2 = new Chat("Bob", "Alice");

        Chat my_chat = new Chat("Alice", "Bob");
        my_chat.recievedMsg();
        my_chat.close();
    }
}
