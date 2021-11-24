# pravega-demo
**This is a chatting room demo.**

In the p-demo direcotry, I have realized a Console-based Chatting Room with the following functions based on Pravega 0.9.1 and JRE 13:

- 1 to 1 chatting;
- Group chatting;
- Files transferring;



### User Instruction:

1. Starting with running 'Userlnterface.main()' which would launch a console UI;
2. Entering the Self Name, with Peer Name or Group Name following the instruction in console;
3. Typing any string with a enter click to send msg to peer / group;
4. Using keyword "upload@<file derectory>" to send a file to peer / group members;
5. Using keyword "exit" to exit the chatting room;



### UI Example:

**Bob view:**

```=========================
            Welcome to the chat room!
Please enter your name: Bob
1: one to one chatting.
2: group chat.
1
Please enter your peer name: Alice
ChatRoom -1170295328 (Hash Code) has been successfully created.
=========================
Let's start chatting!
Bob: Hi Alice
Bob: Could you please sent me the document?
Alice: Certainly, please wait a minute.
Receiving file: document.txt...Done.
Bob: Well recieved!, thank you Alice, and see you next time
Alice: Bye!
exit
```



**Alice view:**

```=========================
            Welcome to the chat room!
Please enter your name: Alice
1: one to one chatting.
2: group chat.
1
Please enter your peer name: Bob
ChatRoom -1170295328 (Hash Code) has been successfully created.
=========================
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
Let's start chatting!
Bob: Hi Alice
Bob: Could you please sent me the document?
Alice: Certainly, please wait a minute.
upload@./document.txt
Upload Finished!
Bob: Well recieved!, thank you Alice, and see you next time
Alice: Bye!
exit
```







