/*
This class is the Console based User Interface;
Which would run a new thread to keep refreshing the inbox information.
follow the instruction to generate a 1 to 1 chatting or a group chat.
type "exit" to exit the chatting room.
*/

package com.dellemc.pravega.chattingRoom;
import java.util.Scanner;



public class UserInterface {
    static final String LINE = "=========================";
    static final String SPACE = "            ";
    static final int refreshLatency = 1000;
    static final String FILE_TAG = "upload@";

    // this function is used to generate a chatting room and save the username
    private static void initializor() throws Exception {
        String selfName = "";
        int inboxHashCode = 0;
        System.out.println(LINE);
        System.out.println(SPACE + "Welcome to the chat room!");

        Scanner s = new Scanner(System.in);  // Create a Scanner object
        System.out.print("Please enter you name: ");
        selfName = s.nextLine();  // Read user input

        for (;;) {
            System.out.println("1: one to one chatting.\n2: group chat.");
            String choice = s.nextLine();
            if (choice.equals("1")) {
                System.out.print("Please enter peer name: ");
                String input = s.nextLine();
                inboxHashCode = selfName.hashCode() * input.hashCode();
                break;
            } else if (choice.equals("2")) {
                System.out.print("Please enter the group name: ");
                String input = s.nextLine();
                inboxHashCode = input.hashCode() * input.hashCode();
                break;
            }
        }
        chattingMainLoop(selfName, inboxHashCode);
    }



    // this is the main chattingLoop, which will run a reading thread to keep reading the inbox
    private static void chattingMainLoop(String selfName, int chatInboxHashCode) throws Exception {
        System.out.println("ChatRoom " + chatInboxHashCode + " (Hash Code) has been successfully created.");
        System.out.println(LINE);
        try (ChatRoomClient myChatRoomClient = new ChatRoomClient(selfName, chatInboxHashCode)) {
            // [Debug]
            System.out.println("Let's start chatting!");
            ReadingThread readMsg = new ReadingThread(myChatRoomClient, refreshLatency);
            readMsg.start();
            Scanner s = new Scanner(System.in);
            for (String input = ""; !(input.equals("exit")); input = s.nextLine()) {
                myChatRoomClient.sendData(input);
            }
            readMsg.interrupt();
        }

    }

    public static void main(String[] args) {
        try {
            initializor();
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.printf("[Debug] Fail to open a chatting room");
        }
    }
}


class ReadingThread extends Thread {
    private Thread t;
    private ChatRoomClient myChatRoomClient;
    private int refreshLatency = 1000;

    ReadingThread(ChatRoomClient myChatRoomClient) {
        this.myChatRoomClient = myChatRoomClient;
    }

    ReadingThread(ChatRoomClient myChatRoomClient, int refreshLatency) {
        this.myChatRoomClient = myChatRoomClient;
        this.refreshLatency = refreshLatency;
    }

    public void run() {
        try {
            while (! isInterrupted()){
                myChatRoomClient.receiveData();
                Thread.sleep(this.refreshLatency);
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