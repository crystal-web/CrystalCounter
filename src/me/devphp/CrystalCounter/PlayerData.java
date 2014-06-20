package me.devphp.CrystalCounter;

import java.util.UUID;

import org.bukkit.entity.Player;

public class PlayerData {
	private UUID uid;
	private int blockbreak = 0;
	private int blockplaced = 0;
	private int kill = 0;
	private int perfectKill = 0;
	private int perfectKillActual = 0;
	private int death = 0;
	private int connectTime = 0;
	
	public PlayerData() {
		java.util.Date currentDate = new java.util.Date();
        long currenttimeInMillis = currentDate.getTime();
        this.connectTime = (int) (currenttimeInMillis/1000);
	}
	
	public int getConnectionTime() {
		java.util.Date currentDate = new java.util.Date();
        long currenttimeInMillis = currentDate.getTime();
        
        int currentTime = (int) (currenttimeInMillis/1000);
        return currentTime - this.connectTime;
	}
	
	public void setPlayer(Player player){
		this.uid = player.getUniqueId();
	}
	
	public void setBlockbreak(int block){
		this.blockbreak = block;
	}
	
	public int getBlockbreak(){
		return this.blockbreak;
	}
	
	public void blockbreak(){
		this.blockbreak = this.blockbreak+1;
	}
	
	
	public void setBlockplaced(int block) {
		this.blockplaced = block;
	}
	
	public int getBlockplaced(){
		return this.blockplaced;
	}
	
	public void blockplaced(){
		this.blockplaced = this.blockplaced+1;
	}
	
	
	public void setKill(int kill) {
		this.kill = kill;
	}
	
	public int getKill(){
		return this.kill;
	}
	
	public void kill(){
		this.kill = this.kill+1;
		perfectKill();
	}
	
	public void setPerfectKill(int kill) {
		this.perfectKillActual = kill;
	}
	
	public int getPerfectKill(){
		return this.perfectKillActual;
	}
	
	public void perfectKill(){
		this.perfectKill = this.perfectKill+1;
	}	

	public void setDeath(int death) {
		this.death = death;
	}
	
	public int getDeath(){
		return this.death;
	}
	
	public void death(){
		this.death = this.death+1;
		if (this.perfectKillActual < this.perfectKill) {
			this.perfectKillActual = this.perfectKill;
			this.perfectKill = 0;
		}
	}
	
	
	
}
