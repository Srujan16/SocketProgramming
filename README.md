# SocketProgramming
Chat application implementation in java using sockets.

# Functionalities 
1) OneToOne chat.
2) Group chat.
3) Creating a new group.
4) Adding to an existing group chat.
5) Leaving a group chat.

## Server
Server is a thread that keeps listening on a port, waiting for incoming client connections.
A new thread is created for each newly connected client, that keeps listening for incoming messages from clients.
**SocketTimeout of 3000** is set for each socket inputstream read operation.

## Data Exchange 
**JSON Format** is used for data exchange between client and server. Used ObjectMapper to **"convert JavaObjects to JSON and back"** (serialization and deserialization).
> # Message Formats
 >**OneToOne**  : {"type":"OneToOne", "message":"Message Data", "to":"UserName"}
 
 >**Action**    : {"type":"Action", "group":"groupName", "action":"Join/Leave"}
 
 >**BroadCast** : {"type":"BroadCast", "group":"groupName","message":"Messaged to be published in group"}

## Client
On the client side, two threads are created for each user. One thread keeps listening for the input from user and the other thread keeps listening 
for the messages from server.
