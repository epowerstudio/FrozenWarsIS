package Screens;

import java.util.Vector;


import Application.Assets;
import Application.GameSettings;
import Application.LaunchFrozenWars;
import Application.MatchManager;
import Server.SmartFoxServer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.ScreenUtils;

public class MultiplayerScreen implements Screen{


	private static MultiplayerScreen instance;

	public static MultiplayerScreen getInstance() {
		if (instance == null) instance = new MultiplayerScreen();
		return instance;
	}
	
	private class InvitedInfo{
		private String userName;
		private String status;
		public String getStatus() {
			return status;
		}
		public String getUserName() {
			return userName;
		}
		public InvitedInfo(String userName, String status) {
			this.userName = userName;
			this.status = status;
		}
	}	
	    /** The gui cam. */
	private OrthographicCamera guiCam;	
	
	/** Se utiliza para dibujar y optimizar las imagenes en el renderizado de la pantalla. */
	private SpriteBatch batcher;
	private GameSettings gSettings;
	private Vector3 touchPoint;
	private Game game;
	private SmartFoxServer sfsClient;
	private BitmapFont font;
	    
    private  BoundingBox externalPlayerTickClick;
    
    private  BoundingBox inviteButtonClick;
    private  BoundingBox playButtonClick;
    private  BoundingBox backButtonClick;
    private  BoundingBox exitTeamButtonClick;
    
    private  BoundingBox mapLeftArrowClick;
    private  BoundingBox mapRightArrowClick;
    private  BoundingBox modeLeftArrowClick;
    private  BoundingBox modeRightArrowClick;
	
    private BoundingBox scrollDownPlayersClick;
    private BoundingBox scrollUpPlayersClick;
    
    // Settings
    private boolean externalPlayers;
	private int gameMode;
	private String myName = "";
	private boolean empiezaPartida;

	private String gameAdmin;
	private int invitedScroll;
    
    private Vector<String> acceptedPlayers;
    private Vector<String> refusedPlayers;
    private Vector<String> waitingPlayers;
    
    private Vector<InvitedInfo> drawPlayers;
    
    public void setAcceptedPlayers(Vector<String> acceptedPlayers) {
		this.acceptedPlayers = acceptedPlayers;
		drawPlayers = new Vector<InvitedInfo>();
		for (int i = 0; i < acceptedPlayers.size(); i++) {
			String name = acceptedPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Accepted"));
		}
		for (int i = 0; i < waitingPlayers.size(); i++) {
			String name = waitingPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Waiting"));		
		}
		for (int i = 0; i < refusedPlayers.size(); i++) {
			String name = refusedPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Cancelled"));
		}
		
	}
    
    public void setRefusedPlayers(Vector<String> refusedPlayers) {
		this.refusedPlayers = refusedPlayers;
		drawPlayers = new Vector<InvitedInfo>();
		for (int i = 0; i < acceptedPlayers.size(); i++) {
			String name = acceptedPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Accepted"));
		}
		for (int i = 0; i < waitingPlayers.size(); i++) {
			String name = waitingPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Waiting"));		
		}
		for (int i = 0; i < refusedPlayers.size(); i++) {
			String name = refusedPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Cancelled"));
		}
	}
    
    public void setWaitingPlayers(Vector<String> waitingPlayers) {
		this.waitingPlayers = waitingPlayers;
		drawPlayers = new Vector<InvitedInfo>();
		for (int i = 0; i < acceptedPlayers.size(); i++) {
			String name = acceptedPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Accepted"));
		}
		for (int i = 0; i < waitingPlayers.size(); i++) {
			String name = waitingPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Waiting"));		
		}
		for (int i = 0; i < refusedPlayers.size(); i++) {
			String name = refusedPlayers.elementAt(i);
			if (font.getBounds(name).width > 195){
				int j = 5;
				while (font.getBounds(name.substring(0, j)).width < 168) j++;
				name = name.substring(0, j)+"...";
			}
			drawPlayers.add(new InvitedInfo(name, "Cancelled"));
		}
	}
    
    public void setExternalPlayers(boolean externalPlayers) {
		this.externalPlayers = externalPlayers;
	}

	public void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}
	
	public boolean amIAdmin(){
		return myName.equals(gameAdmin);
	}
	
	public void setEmpiezaPartida(boolean b) {
		empiezaPartida = b;
	}
	
	public void setMyName(String myName) {
		this.myName = myName;
		acceptedPlayers.add(myName);
		
		String name = myName;
		if (font.getBounds(name).width > 195){
			int j = 5;
			while (font.getBounds(name.substring(0, j)).width < 168) j++;
			name = name.substring(0, j)+"...";
		}
		drawPlayers.add(new InvitedInfo(name, "Accepted"));
	
		gameAdmin = myName;
	}
	
	public void setGameAdmin(String gameAdmin){
		this.gameAdmin = gameAdmin;
	}
    
    public MultiplayerScreen() {
		instance = this;
    	this.game = LaunchFrozenWars.getGame();

		guiCam = new OrthographicCamera(1024,630);
		guiCam.position.set(512,315,0);
		
	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();
	    font = new BitmapFont(Gdx.files.internal("data/first.fnt"), Gdx.files.internal("data/first.png"), false);
	    
	    acceptedPlayers = new Vector<String>();
	    refusedPlayers = new Vector<String>();
	    waitingPlayers = new Vector<String>();
	    drawPlayers = new Vector<InvitedInfo>();
	    
	    invitedScroll = 0;
	    
	    sfsClient = SmartFoxServer.getInstance();
	    
	    Gdx.input.getTextInput(new TextInputListener() {
	         public void input(String text) {
	        	 MultiplayerScreen.getInstance().setMyName(text);
	        	 sfsClient.conectaSala(text);
	         }
	
	         public void canceled() {
	        	 String user = "user".concat(Long.toString(Math.round(Math.random()*1000)));
	        	 MultiplayerScreen.getInstance().setMyName(user);
	        	 sfsClient.conectaSala(user);
	         }
	    }, "Enter user: ","");

	    
	    externalPlayerTickClick = new BoundingBox(new Vector3(120,370,0), new Vector3(170,410,0));
	    
	    inviteButtonClick = new BoundingBox(new Vector3(550,80,0), new Vector3(790,120,0));
	    playButtonClick = new BoundingBox(new Vector3(200,80,0), new Vector3(440,120,0));
	    backButtonClick = new BoundingBox(new Vector3(370,20,0), new Vector3(610,60,0));
	    exitTeamButtonClick = new BoundingBox(new Vector3(130,150,0), new Vector3(430,330,0));
	    
	    mapLeftArrowClick = new BoundingBox(new Vector3(50,200,0), new Vector3(100,290,0));
	    mapRightArrowClick = new BoundingBox(new Vector3(450,200,0), new Vector3(510,280,0));
	    modeLeftArrowClick = new BoundingBox(new Vector3(80,450,0), new Vector3(120,500,0));
	    modeRightArrowClick = new BoundingBox(new Vector3(425,450,0), new Vector3(460,500,0));
	    
	    scrollDownPlayersClick = new BoundingBox(new Vector3(900,190,0), new Vector3(950,225,0));
	    scrollUpPlayersClick = new BoundingBox(new Vector3(900,365,0), new Vector3(950,400,0));
	    
	    
	    empiezaPartida = false;
	    externalPlayers = false;
	    gameMode = 0;
	}

	
	@Override
	public void dispose() {
		batcher.dispose();
		Assets.music.dispose();
		System.exit(0);	
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	public void creaPartida(){
		MatchManager manager = new MatchManager(sfsClient);
		sfsClient.addManager(manager);
		GameScreen gameScreen = new GameScreen(game,manager);
		manager.setGameScreen(gameScreen);
		game.setScreen(gameScreen);
	}
	
	@Override
	public void render(float arg0) {
		
		//detectamos si se ha tocado la pantalla
		if (Gdx.input.justTouched()){
			guiCam.unproject(touchPoint.set(Gdx.input.getX(),Gdx.input.getY(),0));
      		//System.out.println(Integer.toString((int)touchPoint.x).concat(",").concat(Integer.toString((int)touchPoint.y)));
			//compruebo si he tocado play (se abre ventana de introduccion de usuario si no esta logeado)
			if (playButtonClick.contains(touchPoint) && amIAdmin()){
				sfsClient.insertInQueuesRequest(acceptedPlayers, !externalPlayers);
      		} else if (inviteButtonClick.contains(touchPoint) && amIAdmin()) {
      			InviteScreen inviteScreen = new InviteScreen();
      			game.setScreen(inviteScreen);
      		}else if (externalPlayerTickClick.contains(touchPoint) && amIAdmin()){
      			externalPlayers = !externalPlayers;
      			sfsClient.modExternalPlayersRequest(externalPlayers);
      		} else if (modeLeftArrowClick.contains(touchPoint) && amIAdmin()){
      			if (gameMode == 0) gameMode = 4;
      			else gameMode--;
      			sfsClient.modeChangeRequest(gameMode);
      		} else if (modeRightArrowClick.contains(touchPoint) && amIAdmin()){
      			gameMode = (gameMode + 1) % 5;
      			sfsClient.modeChangeRequest(gameMode);
      		} else if (scrollDownPlayersClick.contains(touchPoint)){
      			if (invitedScroll < (acceptedPlayers.size() + refusedPlayers.size() + waitingPlayers.size()) - 5) invitedScroll++;
      		} else if (scrollUpPlayersClick.contains(touchPoint)){
      			if (invitedScroll != 0) invitedScroll--;     	
			} else if (backButtonClick.contains(touchPoint)){
      			game.setScreen(InitialScreen.getInstance());
      		} else if (exitTeamButtonClick.contains(touchPoint)){
      			sfsClient.groupExitRequest(gameAdmin);
      		}
		}
		
		if (empiezaPartida) creaPartida();
		//crear solamente un batcher por pantalla y eliminarlo cuando no se use
	
      		GL10 gl = Gdx.graphics.getGL10(); //referencia a OpenGL 1.0
            gl.glClearColor(0,1,0,1);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
           
            guiCam.update();
            batcher.setProjectionMatrix(guiCam.combined);
             
            //Dibujando el Background
            batcher.disableBlending();
            //se elimina graficamente la transparencia ya que es un fondo
            batcher.begin();
            batcher.draw(Assets.backGrey,0,0,1024,630);
            batcher.end();

            //Dibujando elementos en pantalla activamos el Blending
            batcher.enableBlending();
            batcher.begin();
            batcher.draw(Assets.multiplayerGameTitle, 320, 540);
            drawMode();
            if (amIAdmin()) batcher.draw(Assets.modeLeftArrow, 85, 455);
            if (amIAdmin()) batcher.draw(Assets.modeRightArrow, 435, 455);   
            
            batcher.draw(Assets.externalPlayerButton, 130, 380);   
            if (externalPlayers) batcher.draw(Assets.externalPlayerTick, 130, 380);   
            batcher.draw(Assets.externalPlayerText, 200, 380);   
            
            batcher.draw(Assets.map, 130, 150);   
            if (amIAdmin()) batcher.draw(Assets.mapLeftArrow, 45, 200);   
            if (amIAdmin()) batcher.draw(Assets.mapRightArrow, 450, 200);   
            
            if (amIAdmin()) batcher.draw(Assets.playButton, 200, 80); 
            if (amIAdmin()) batcher.draw(Assets.inviteButton, 550, 80); 
            
            batcher.draw(Assets.backButton, 370, 20); 
           
            batcher.draw(Assets.pingu, 565, 112);
            
            drawInvited();
            	          
            batcher.end();

	}

	private void drawInvited() {
		batcher.draw(Assets.list, 630, 180); 
		batcher.draw(Assets.playersText, 720, 407); 
		
		for (int i = 0; i < Math.min(drawPlayers.size(), 5); i++){
			String name = drawPlayers.elementAt(i+invitedScroll).getUserName();
			
			font.draw(batcher, name, 700,(402-45*i));
			if (drawPlayers.elementAt(i+invitedScroll).getStatus().equals("Accepted")) 
				batcher.draw(Assets.statusTick, 650, (367-45*i));
			else if (drawPlayers.elementAt(i+invitedScroll).getStatus().equals("Waiting")) 
				batcher.draw(Assets.statusInterrogation, 650, (367-45*i));
			else if (drawPlayers.elementAt(i+invitedScroll).getStatus().equals("Cancelled"))
				batcher.draw(Assets.statusCancel, 650, (367-45*i));
		}
	}

	private void drawMode() {
		if (gameMode == 0) batcher.draw(Assets.normalRoyalMode, 100, 450);
		else if (gameMode == 1) batcher.draw(Assets.teamPlayMode, 100, 450);
		else if (gameMode == 2) batcher.draw(Assets._1vsAllMode, 100, 450);
		else if (gameMode == 3) batcher.draw(Assets.survivalMode, 100, 450);
		else if (gameMode == 4) batcher.draw(Assets.battleRoyalMode, 100, 450);
	}
	
	public void setDefault() {
	    acceptedPlayers.clear();
	    refusedPlayers.clear();
	    waitingPlayers.clear();
	    drawPlayers.clear();
	    
	    invitedScroll = 0;
	    externalPlayers = false;
	    gameMode = 0;
	    
		acceptedPlayers.add(myName);
		
		String name = myName;
		if (font.getBounds(name).width > 195){
			int j = 5;
			while (font.getBounds(name.substring(0, j)).width < 168) j++;
			name = name.substring(0, j)+"...";
		}
		drawPlayers.add(new InvitedInfo(name, "Accepted"));
	
		gameAdmin = myName;
	}
	

	
	
	@Override
	public void resize(int arg0, int arg1) {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void show() {
		
	}

}