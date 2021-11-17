package com.dellemc.pravega.chattingRoom;


import java.util.Scanner;

public class UI {
    static String LINE      = "=========================";
    static String HALFLINE  = "============";
    static String HALFSPACE = "            ";


    private static void mainLoop() throws Exception {
        System.out.println(LINE);
        System.out.println(HALFSPACE + "Welcome to the chat room!");

        Scanner s = new Scanner(System.in);  // Create a Scanner object
        System.out.print("Please enter you name: ");
        String selfName = s.nextLine();  // Read user input
        System.out.print("Please enter peer name: ");
        String peerName = s.nextLine();  // Read user input

        Chat myChat = new Chat(selfName, peerName);
        for (String input = ""; !(input.equals("exit")); input = s.nextLine()) {
            myChat.sendMsg(input);
            myChat.recievedMsg();
            System.out.print( selfName + ": ");
        }
    }



    public static void main(String[] args) {
        try {
            mainLoop();
        }catch (Exception e){
            System.out.printf("[Debug] Fail to open a chatting room");
        }


//        Chat my_chat2 = new Chat("Bob", "Alice");
//        my_chat2.sendMsg("Hello from Bob!");

//        Chat my_chat = new Chat("Alice", "Bob");
//        my_chat.recievedMsg();
//        my_chat.close();

    }
}
