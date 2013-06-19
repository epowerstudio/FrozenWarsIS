package Screens;
import GameLogic.Direction;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class PenguinAnimation {
	private Animation walkAnimationD; 
	private Animation walkAnimationL; 
	private Animation walkAnimationR; 
	private Animation walkAnimationU; 
    private Texture penguin;              
    private TextureRegion[] walkFramesD;
    private TextureRegion[] walkFramesL;
    private TextureRegion[] walkFramesR;
    private TextureRegion[] walkFramesU;
    private TextureRegion currentFrame;
    
	public PenguinAnimation(FileHandle dir,Direction lookAt) {
		penguin= new Texture(dir);
		TextureRegion[][] tmp = TextureRegion.split(penguin, penguin.getWidth() / 4, penguin.getHeight() / 4);
		walkFramesD = new TextureRegion[4];
        walkFramesL = new TextureRegion[4];
        walkFramesR = new TextureRegion[4];
        walkFramesU = new TextureRegion[4];
        int index = 0;
        int i=0;
        for (int j = 0; j < 4; j++) {
        	walkFramesD[index] = tmp[i][j];
            walkFramesL[index]= tmp[i+1][j];
            walkFramesR[index]= tmp[i+2][j];
            walkFramesU[index]= tmp[i+3][j];
            index++;
                }
       walkAnimationD = new Animation(0.25f, walkFramesD);
       walkAnimationL = new Animation(0.25f, walkFramesL);
       walkAnimationR = new Animation(0.25f, walkFramesR);
       walkAnimationU = new Animation(0.25f, walkFramesU);
       currentFrame = getwalkAnimation(lookAt).getKeyFrame(0,true);
	}

	private Animation getwalkAnimation(Direction lookAt) {
		Animation animation = null;
		if (Direction.left.equals(lookAt)){
			animation = walkAnimationL;
		} else if (Direction.right.equals(lookAt)){
			animation = walkAnimationR;
		} else if (Direction.up.equals(lookAt)){
			animation = walkAnimationU;
		} else if (Direction.down.equals(lookAt)){
			animation = walkAnimationD;
		}
		return animation;
	}


	public Animation getwalkAnimationR() {
		return walkAnimationR;
	}
	
	public Animation getwalkAnimationL() {
		return walkAnimationL;
	}
	
	public Animation getwalkAnimationD() {
		return walkAnimationD;
	}
	
	public Animation getwalkAnimationU() {
		return walkAnimationU;
	}

	public TextureRegion getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(TextureRegion currentFrame) {
		this.currentFrame = currentFrame;
	}

	public void setCurrentFrame(Direction lookAt) {
		this.currentFrame = getwalkAnimation(lookAt).getKeyFrame(0,true);
	}
}
