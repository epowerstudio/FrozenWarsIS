package com.a51integrated.sfs2x;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import com.smartfoxserver.v2.api.CreateRoomSettings;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.SFSRoomRemoveMode;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class MyExt extends SFSExtension {
	
	private int nRooms;			// To set a different name for the room
	private Queue<User> queue1; // queues for the start of games
	private Queue<User[]> queue2;
	private Queue<User[]> queue3;
	private HashMap<String,User> users; // users connected
	private HashMap<String,InvitationRoom> gamesInCreation; // String: leader name, games in creation.

	@Override
	public void init() {				// here we initialize the queues,the hash of the users connected, and set handslers
		queue1 = new LinkedList<User>();
		queue2 = new LinkedList<User[]>();
		queue3 = new LinkedList<User[]>();
		users=new HashMap<String,User>();
		gamesInCreation=new HashMap<String,InvitationRoom>();
		this.addRequestHandler("conectarse", Connect.class); // handler fired when a user connects
		this.addRequestHandler("db",DataBase.class); // handler fired when we want to know information of the database
		this.addEventHandler(SFSEventType.USER_JOIN_ZONE, ZoneJoinEventHandler.class); // handler fired when checked the pword
		this.addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class); // handler fired when a user want to connect
		this.addEventHandler(SFSEventType.USER_DISCONNECT, Disconnect.class); // handler fired after a user disconnects
		this.addRequestHandler("meter1", Put1Handler.class); //handlers to introduce players into de queues
		this.addRequestHandler("meter2", Put2Handler.class);
		this.addRequestHandler("meter3", Put3Handler.class);
		this.addRequestHandler("lanzarPartida", ThrowGameHandler.class);
		this.addRequestHandler("sacardecola", ExtractUsersHandler.class); //handlers to introduce players into de queues
		this.addRequestHandler("putHarpoon", PutHarpoon.class); // handler to put an harpoon in the game.
		this.addRequestHandler("GetConnectedFriends", GetConnectedFriends.class); // handler fired when a user wants to see the connected friends
		this.addRequestHandler("asignImprovements", AsignImprovementsHandler.class);
		this.addRequestHandler("GetTime", GetTime.class);//Gets the servers time.
		this.addRequestHandler("Invite", Invite.class); // handler fired when a user invite someone.
		this.addRequestHandler("Accept", Accept.class); // handler fired when a user accepts an invitation.
		this.addRequestHandler("Refuse", Refuse.class); // handler fired when a user refuses an invitation.
		this.addRequestHandler("ModeChange", ModeChange.class); // handler fired when a user changes the mode of his game.
		this.addRequestHandler("ModExternalPlayers", ModExternalPlayers.class); // handler fired when a user changes the "external players" option.
		this.addRequestHandler("ExitGroup", ExitGroup.class); // handler fired when a user want to exit the current team.
		this.addRequestHandler("GameMessage", GameMessage.class);
		nRooms=0;
	}

	public HashMap<String, InvitationRoom> getGamesInCreation() {
		return gamesInCreation;
	}

	public void setGamesInCreation(HashMap<String, InvitationRoom> gamesInCreation) {
		this.gamesInCreation = gamesInCreation;
	}

	public Queue<User> getQueue1() {
		return queue1;
	}

	public void setQueue1(Queue<User> queue1) {
		this.queue1 = queue1;
	}

	public Queue<User[]> getQueue2() {
		return queue2;
	}

	public void setQueue2(Queue<User[]> queue2) {
		this.queue2 = queue2;
	}
	
	public Queue<User[]> getQueue3() {
		return queue3;
	}

	public void setQueue3(Queue<User[]> queue3) {
		this.queue3 = queue3;
	}
	
	public HashMap<String,User> getUsers(){
		return this.users;
	}

	public void setUsers(HashMap<String,User> users) {
		this.users = users;
	}
	
	public Room creatGame(User a, User b,User c,User d){
		ISFSObject rtn = new SFSObject();
		ISFSObject rtna = new SFSObject();
		ISFSObject rtnb = new SFSObject();
		ISFSObject rtnc = new SFSObject();
		ISFSObject rtnd = new SFSObject();
		try {
			CreateRoomSettings settings= new CreateRoomSettings();
			settings.setMaxUsers(4);
			settings.setGame(true);
			settings.setName("sala"+ nRooms);
			nRooms++;
			 settings.setDynamic(true);
			 settings.setAutoRemoveMode(SFSRoomRemoveMode.WHEN_EMPTY);
			//Room room = this.getApi().createRoom(this.getParentZone(), settings, a,true,a.getLastJoinedRoom());
			Room room = this.getApi().createRoom(this.getParentZone(), settings, a);
			
			try{	
				a.getLastJoinedRoom().removeUser(a);
				room.addUser(a);
				b.getLastJoinedRoom().removeUser(b);
				room.addUser(b);
				c.getLastJoinedRoom().removeUser(c);
				room.addUser(c);
				d.getLastJoinedRoom().removeUser(d);
				room.addUser(d);
				 Thread.currentThread().sleep(5000);
			}catch(Exception e){};
			try{
				//rtn.putUtfString("res", "Partida en marcha con los siguientes jugadores: "+a.getName()+", "+b.getName()+", "+c.getName()+", "+d.getName()+", ");
				rtna.putInt("id", a.getPlayerId(room));
				this.send("startGame", rtna, a);
				rtnb.putInt("id", b.getPlayerId(room));
				this.send("startGame", rtnb, b);         //Sends the ids of each player at the new room.
				rtnc.putInt("id", c.getPlayerId(room));
				this.send("startGame", rtnc, c);
				rtnd.putInt("id", d.getPlayerId(room));
				this.send("startGame", rtnd, d);
			}catch(Exception e){};
			return room;
		} catch (SFSCreateRoomException e) {
			e.printStackTrace();
			rtn.putUtfString("ras", "No se ha podido crear la sala");
			this.send("meter1", rtn, a);
			return null;
		}	

	}
}
