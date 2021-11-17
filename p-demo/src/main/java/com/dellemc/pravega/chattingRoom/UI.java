package com.dellemc.pravega.chattingRoom;
import java.util.Scanner;

import static com.dellemc.pravega.chattingRoom.UserRegister.chatRoomRegister;


public class UI {
    static String LINE      = "=========================";
    static String HALFLINE  = "============";
    static String HALFSPACE = "            ";


    private static void mainLoop() throws Exception {
        System.out.println(LINE);
        System.out.println(HALFSPACE + "Welcome to the chat room!");

        Scanner s = new Scanner(System.in);  // Create a Scanner object
        System.out.print("Please enter you  name: ");
        String selfName = s.nextLine();  // Read user input
        System.out.print("Please enter peer name: ");
        String peerName = s.nextLine();  // Read user input

        // user register
        chatRoomRegister(selfName);
        chatRoomRegister(peerName);

        Chat myChat = new Chat(selfName, peerName);
        ReadMsgThread readMsg = new ReadMsgThread(myChat);
        readMsg.start();
        for (String input = ""; !(input.equals("exit")); ) {
            input = s.nextLine();
            System.out.println(selfName + "(You): " + input);
            myChat.sendMsg(input);
        }
        readMsg.interrupt();
        myChat.close();
    }

    public static void main(String[] args) {
        try {
            mainLoop();
        }catch (Exception e){
            System.out.printf("[Debug] Fail to open a chatting room");
        }
    }
}



class ReadMsgThread extends Thread {
    private Thread t;
    private Chat myChat;
    private int sleepTime = 1000;

    ReadMsgThread(Chat myChat) {
        this.myChat = myChat;
    }

    ReadMsgThread(Chat myChat, int sleepTime) {
        this.myChat = myChat;
        this.sleepTime = sleepTime;
    }

    public void run() {
        try {
            while (! isInterrupted()){
                myChat.recieveMsg();
                Thread.sleep(this.sleepTime);
            }
        } catch (InterruptedException e) {

        } catch (IllegalStateException e){

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