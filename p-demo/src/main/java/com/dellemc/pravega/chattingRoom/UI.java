package com.dellemc.pravega.chattingRoom;
import java.util.Scanner;

import static com.dellemc.pravega.chattingRoom.UserRegister.chatRoomRegister;


public class UI {
    static String LINE      = "=========================";
    static String HALFLINE  = "============";
    static String HALFSPACE = "            ";


    private static void intro() throws Exception {
        String selfName = "";
        int inboxHashCode = 0;
        System.out.println(LINE);
        System.out.println(HALFSPACE + "Welcome to the chat room!");

        Scanner s = new Scanner(System.in);  // Create a Scanner object
        System.out.print("Please enter you name: ");
        selfName = s.nextLine();  // Read user input

        for (;;) {
            System.out.println("1: one to one chatting.\n2: group chat.");
            String choice = s.nextLine();
            if (choice.equals("1")) {
                System.out.print("Please enter peer name: ");
                String input = s.nextLine();
                inboxHashCode = selfName.hashCode() + input.hashCode();
                break;
            } else if (choice.equals("2")) {
                System.out.print("Please enter the group name: ");
                String input = s.nextLine();
                inboxHashCode = input.hashCode();
                break;
            }
        }
        mainLoop(selfName, inboxHashCode);
    }

    private static void mainLoop(String selfName, int inboxHashCode) throws Exception {
        // user register
        chatRoomRegister(inboxHashCode + "");
//        chatRoomRegister(peerNameHash);
        System.out.println("Room " + inboxHashCode + " (Hash Code) has been successfully created.");
        System.out.println(LINE);
        Chat myChat = new Chat(selfName, inboxHashCode);
        ReadMsgThread readMsg = new ReadMsgThread(myChat);
        readMsg.start();
        Scanner s = new Scanner(System.in);
        for (String input = ""; !(input.equals("exit")); input = s.nextLine()) {
            if (!input.equals("")) {
                myChat.sendMsg(input);
            }
        }
        readMsg.interrupt();
        myChat.close();
    }

    public static void main(String[] args) {
        try {
            intro();
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