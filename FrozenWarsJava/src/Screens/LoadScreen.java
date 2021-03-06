package Screens;

import Application.Assets;
import Application.Desktop;
import Application.LaunchFrozenWars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LoadScreen implements Screen{
	private SpriteBatch batcher; 
	private Game game;
	private OrthographicCamera guiCam;
	private long t0, t1;
	private boolean showed;
	 
	public LoadScreen(){
		this.game = LaunchFrozenWars.getGame();
		this.showed = false;
		guiCam = new OrthographicCamera(1024,630);
		guiCam.position.set(512,315,0);
		batcher = new SpriteBatch(); 
		t0 =  System.currentTimeMillis(); //Hora de  creacion de la pantalla	
	}	
	
	@Override
	public void dispose() {
		batcher.dispose();
		Assets.music.dispose();
		System.exit(0);			
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float arg0) {
		
		t1 = System.currentTimeMillis(); //hora actual
		//comprobamos si han pasado 5 seg o se ha tocado la pantalla
		//en cuyo caso se abre la ventana de inicio
        if ((((t1 - t0) >= (5000)) || Gdx.input.justTouched()) && !showed) { 
        	showed = true;
            game.setScreen(InitialScreen.getInstance());
    	return;

        }
 
        GL10 gl = Gdx.graphics.getGL10(); //referencia a OpenGL 1.0
        gl.glClearColor(0,0,0,0);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
        guiCam.update();
        batcher.setProjectionMatrix(guiCam.combined);
         
        //Dibujando el Background
        batcher.disableBlending();
        //se elimina graficamente la transparencia ya que es un fondo
        batcher.begin();
        //Conchi linea
        batcher.draw(Assets.backConf,0,0);

        batcher.end();
        
        batcher.enableBlending();
          batcher.begin();
        batcher.draw(Assets.fwlogo,50,50);
        batcher.end();
        
     /*   batcher.enableBlending();
        //se elimina graficamente la transparencia ya que es un fondo
         switch (){ //Se mira el tiempo restante para cerrar la ventana para mostrarlo por pantalla
         if ((int)((t1-t0)/1000))
         	case 1:
         		batcher.begin();
         		batcher.draw(Assets.cuatroSec,50,170);
         		batcher.end();
         		break;
         	case 2:
         		batcher.begin();
         		batcher.draw(Assets.tresSec,50,170);
         		batcher.end();
         		break;
         	case 3:
         		batcher.begin();
         		batcher.draw(Assets.dosSec,50,170);
         		batcher.end();
         		break;
         	case 4:
         		batcher.begin();
         		batcher.draw(Assets.unSec,50,170);
         		batcher.end();
         		break;
          }*/
	}

	@Override
	public void resize(int arg0, int arg1) {
		if (arg0!=1024 || arg1!=630) Desktop.resetScreenSize();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub	
	}

}
