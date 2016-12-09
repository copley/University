Describes what the various files do
-----------------------------------

ChatClient: Just creates a new instance of the main window and then does nothing
else

ChatClientGUI: The main part of the program, handles what to do with diffrent messages

ChannelWindow: Contains the information for each channel you have joined, like all the messages
and users on that channel

ConnectWindow: Asks the user for information about the IRC Server they wish to join

ServerConnection: Handles all the comunication with the server, sending and reciving

IRCMessageParser: Parses a message into its components and returns it as an IRCMesage

IRCMessage: Class that holds all the information for an IRC message that has been sent
to the client

IRCMessageType: Contains the two diffrent IRC Message types, a code or a command

Replies: Contains all the codes and definitions for a reply from the server