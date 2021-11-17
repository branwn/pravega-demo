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
        ReadMsgThread readMsg = new ReadMsgThread(myChat);
        readMsg.start();
        System.out.print("You(" + selfName + "): ");
        for (String input = ""; !(input.equals("exit")); input = s.nextLine()) {
//            myChat.recieveMsg();
            System.out.println("You(" + selfName + "): " + input);
            myChat.sendMsg(input);
        }

        myChat.close();
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



class ReadMsgThread extends Thread {
    private Thread t;
    private Chat myChat;

    ReadMsgThread(Chat myChat) {
        this.myChat = myChat;
    }

    public void run() {
//        System.out.println("Running " +  threadName );
        try {
            for(int i = 100; i > 0; i--) {
                myChat.recieveMsg();
//                System.out.println("Thread: " + threadName + ", " + i);
                Thread.sleep(1800);
            }
        }catch (InterruptedException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start () {
        if (t == null) {
            t = new Thread (this);
            t.start ();
        }
    }
}