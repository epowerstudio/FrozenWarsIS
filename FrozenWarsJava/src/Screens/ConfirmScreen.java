package Screens;

import java.util.Vector;

import Application.Assets;
import Application.Desktop;
import Application.LaunchFrozenWars;

import Server.SmartFoxServer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.ScreenUtils;

public class ConfirmScreen implements Screen{
	private static ConfirmScreen instance;
	private OrthographicCamera guiCam;
	private SpriteBatch batcher; 
	private Vector3 touchPoint;
	private BoundingBox yesClick;
	private BoundingBox noClick;
	private Game game;
	private BitmapFont font;
	private TextureRegion background;
	private Vector<String> screenModeV;
	private Vector<String> userV;
	private Screen ancestor; 
	private String screenMode;
	private String user;
	private int width;
	private int height;
	private int posW;
	private int posH;
	
	public static ConfirmScreen getInstance() {
		if (instance == null) instance = new ConfirmScreen();
		return instance;
	}

	public ConfirmScreen() {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		posW = (width-Assets.window.getRegionWidth())/2;
		posH = (height-Assets.window.getRegionHeight())/2;
		instance = this;
		this.game = LaunchFrozenWars.getGame();
		screenModeV = new Vector<String>();
		userV = new Vector<String>();
		guiCam = new OrthographicCamera(width, height);
		guiCam.position.set(width/2, height/2,0);
		
		font = new BitmapFont(Gdx.files.internal("data/first.fnt"), Gdx.files.internal("data/first.png"), false);;

	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();
	    yesClick = new BoundingBox(new Vector3(posW+20,posH+45,0), new Vector3(posW+155,posH+100,0));
	    noClick = new BoundingBox(new Vector3(posW+190,posH+45,0), new Vector3(posW+320,posH+100,0));   
	    Gdx.input.setOnscreenKeyboardVisible(false);
	}
	
	public void setNewConfirmScreen(String mode, String usr){
		if (!(mode.equals("InviteGame") && MultiplayerScreen.getInstance().isInQueue())){
			screenModeV.add(mode);
			userV.add(usr);
		}
	}
	
	public void createConfirmIfNeeded(){
		if (!screenModeV.isEmpty()){
			background = ScreenUtils.getFrameBufferTexture();
			ancestor = game.getScreen();
			screenMode = screenModeV.remove(0);
			user = userV.remove(0);
			game.setScreen(this);
		}
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

	@Override
	public void render(float arg0) { 
      //detectamos si se ha tocado la pantalla
      if (Gdx.input.justTouched()){
      		guiCam.unproject(touchPoint.set(Gdx.input.getX(),Gdx.input.getY(),0));
      		System.out.println(Integer.toString((int)touchPoint.x).concat(",").concat(Integer.toString((int)touchPoint.y)));

      		//compruebo si he tocado yes 
      		if (yesClick.contains(touchPoint)){
      			if (screenMode.equals("Exit")) this.dispose();
      			else if (screenMode.equals("InviteGame")) {
      				SmartFoxServer.getInstance().acceptRequest(user);
      				MultiplayerScreen.getInstance().setGameAdmin(user);
      				game.setScreen(MultiplayerScreen.getInstance());
      			} else if (screenMode.equals("InvitedDisconnected")){
      				game.setScreen(new InviteScreen());
      			} else if (screenMode.equals("BeFriends?")){
      				SmartFoxServer.getInstance().sendBeFriends("yes", user);
      				FriendsListScreen.getInstance().updateFriends();
      				game.setScreen(ancestor);
      			} else if (screenMode.equals("Unfriend")) {
      				SmartFoxServer.getInstance().sendBeFriends("no", user);
      				FriendsListScreen.getInstance().updateFriends();
      				game.setScreen(ancestor);
      			}
      			
      		} else if(noClick.contains(touchPoint)){ //compruebo si he tocado no
      			if (screenMode.equals("Exit") || screenMode.equals("InvitedDisconnected") || screenMode.equals("Unfriend")) game.setScreen(ancestor);
      			else if (screenMode.equals("InviteGame")) {
      				SmartFoxServer.getInstance().refuseRequest(user);
      				game.setScreen(ancestor);
      			} else if (screenMode.equals("BeFriends?")){
      				SmartFoxServer.getInstance().sendBeFriends("no", user);
      				game.setScreen(ancestor);
      			}
      		}
      }
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
            batcher.draw(background,0,0);
            batcher.end();

          //Dibujando elementos en pantalla activamos el Blending
            batcher.enableBlending();
            batcher.begin();    
            
            batcher.draw(Assets.window, posW, posH);
            if (screenMode.equals("Exit")) batcher.draw(Assets.exitText, posW, posH);
            else if (screenMode.equals("InviteGame")) {
            	String message = user.concat(" has invited you to join his game.");
            	font.drawWrapped(batcher, message, posW+15, posH+225, 330);
            	font.draw(batcher, "Do you want to join him?", posW+15, posH+155);
            } else if (screenMode.equals("InvitedDisconnected")){
            	String message = user.concat(" has disconnected. Do you want to invite anyone else?");
            	font.drawWrapped(batcher, message, posW+15, posH+225, 330);
            } else if (screenMode.equals("BeFriends?")){
            	String message = user.concat(" wants to be your friend.");
            	font.drawWrapped(batcher, message, posW+15, posH+225, 330);
            	font.draw(batcher, "Do you want to be friends?", posW+15, posH+145);
            } else if (screenMode.equals("Unfriend")){
            	String message = "Do you want to remove "+user+" from your friend list?";
            	font.drawWrapped(batcher, message, posW+15, posH+225, 330);
            }
            batcher.end();	
	}

	@Override
	public void resize(int arg0, int arg1) {
		if (arg0!=1024 || arg1!=630) Desktop.resetScreenSize();

	}

	@Override
	public void resume() {
	}

	@Override
	public void show() {
	}
}
