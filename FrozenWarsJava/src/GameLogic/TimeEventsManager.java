package GameLogic;

import java.util.HashMap;

import GameLogic.TypeGame;
import GameLogic.TypeEvent;

import com.badlogic.gdx.utils.Timer;


/**
 * The main objective of this class is helping class match by controlling
 * game events such as counting where an harpoon must be sunken, the duration
 * of the sunken time, or other time events.
 */

public class TimeEventsManager{
	
	private final float sunkenTime = 0.5f;
	private final float invisibleTime = 10;
	private final float sinkPenguinTime = 1;
	private final float endGameBattleRoyalTime = 180;
	private final float endGameSurvivalTime = 90;
	private final float respawnTime = 0.5f;
	
	private Match match;
	private TimeEventsTask timeMatch;
	private Timer timerMatch;
	private HashMap<Player,TimeEventsTask> invisibleTimerPlayers;
	private HashMap<Player,Timer> invisiblePlayers;
	private HashMap<Harpoon,Timer> activeHarpoonTimer;
	private boolean timeGameRunning;
	
	public TimeEventsManager(Match match){
		this.match = match;
		this.invisiblePlayers = new HashMap<Player,Timer>();
		this.activeHarpoonTimer = new HashMap<Harpoon,Timer>();
		this.invisibleTimerPlayers = new HashMap<Player,TimeEventsTask>();
		this.timeGameRunning = false;
	}
	
	
	/**
	 * <p>
	 * This method <b>stops</b> the harpoon timers. He gets the harpoons
	 * from the activeHarpoonTimer list, stop the timer and remove
	 * it from the list. 
	 * 
	 * @param harpoon - The harpoon which wants to stop.
	 */
	
	public void stopHarpoonTimer(Harpoon harpoon) {
		Timer timer = activeHarpoonTimer.get(harpoon);
		timer.stop();
		activeHarpoonTimer.remove(harpoon);
	}
	
	public void freezeWaterEvent(Harpoon sunkenHarpoon) {
		TimeEventsTask timerTask = new TimeEventsTask(this,sunkenHarpoon,TypeEvent.freezeWater);
		Timer timer = new Timer();
		timer.scheduleTask(timerTask,sunkenTime);
		timer.start();
	}
	
	public void sinkPenguinEvent(Player player) {
		TimeEventsTask timerTask = new TimeEventsTask(this,player,TypeEvent.sinkPenguin);
		Timer timer = new Timer();
		timer.scheduleTask(timerTask,sinkPenguinTime);
		timer.start();
	}
	
	public void invisibleEvent(Player player){
		Timer timer = new Timer();
		invisiblePlayers.put(player,timer);
		TimeEventsTask timerTask = new TimeEventsTask(this,player,TypeEvent.invisible,invisibleTime);
		invisibleTimerPlayers.put(player,timerTask);
		timer.scheduleTask(timerTask,invisibleTime);
		timer.start();
	}
	
	public void removeInvisibleTimer(Player player) {
		Timer timer = invisiblePlayers.get(player);
		timer.stop();
		invisiblePlayers.remove(player);
		invisibleTimerPlayers.remove(player);
	}
	
	public void sinkHarpoonEvent(Harpoon harpoon,long time){
		TimeEventsTask timerTask = new TimeEventsTask(this,harpoon,TypeEvent.sinkHarpoon);
		long currentTime = System.currentTimeMillis();
		float waitTime = (time - currentTime)/1000;
		Timer timer = new Timer();
		activeHarpoonTimer.put(harpoon,timer);
		timer.scheduleTask(timerTask,waitTime);
		timer.start();
	}
	
	public void respawnTimeEvent(Player p){
		TimeEventsTask timerTask = new TimeEventsTask(this,p,TypeEvent.respawn);
		Timer timer = new Timer();
		timer.scheduleTask(timerTask,respawnTime);
		timer.start();
	}
	
	public void endGameEvent(TypeGame type){
		float time = 0; 
		timerMatch = new Timer();
		if (type.equals(TypeGame.BattleRoyale)) time = endGameBattleRoyalTime;
		else if (type.equals(TypeGame.Survival)) time = endGameSurvivalTime;
		timeMatch = new TimeEventsTask(this,null,TypeEvent.endGame,time);
		timerMatch.scheduleTask(timeMatch,time);
		timerMatch.start();
		timeGameRunning = true;
	}
	
	public void actionPerformed(TypeEvent type, Object taskObject, TimeEventsTask timeEventsTask) {
		if (type.equals(TypeEvent.sinkHarpoon)){
			Harpoon harpoon = (Harpoon)taskObject;
			Timer timer = activeHarpoonTimer.get(harpoon);
			activeHarpoonTimer.remove(timer);
			match.sendSinkHarpoon(harpoon);
		}
		else if (type.equals(TypeEvent.freezeWater)){
			Harpoon harpoon = (Harpoon)taskObject;
			match.freezeWater(harpoon);
		}
		else if (type.equals(TypeEvent.sinkPenguin)){
			Player player = (Player)taskObject;
			match.sinkPenguinFinish(player);
		}
		else if (type.equals(TypeEvent.invisible)){
			Player player = (Player)taskObject;
			match.endInvisible(player);
		}
		else if(type.equals(TypeEvent.endGame)){
			match.gameTimeOff();
		}
		else if(type.equals(TypeEvent.respawn)){
			Player player = (Player)taskObject;
			match.sinkPenguinFinish(player);
		}
	}

	public float getSunkenTime() {
		return sunkenTime;
	}

	public float getSinkPenguinTime() {
		return sinkPenguinTime;
	}

	public long getTimeMatch(){
		return timeMatch.getEndTime();
	}

	public long getTimeInvisible(Player player) {
		TimeEventsTask timer = invisibleTimerPlayers.get(player);
		long time = 0;
		try{
			time = timer.getEndTime();
		}catch(Exception e){}
		return time;
	}


	public void stopGameTimer() {
		timerMatch.stop();
		timeGameRunning = false;
	}


	public boolean isGameTimeRunning() {
		return timeGameRunning;
	}

}
