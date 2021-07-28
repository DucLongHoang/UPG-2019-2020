package semestralniPrace;

import java.awt.Font;

import waterflowsim.Cell;
import waterflowsim.Simulator;

/**
 * 04.05.2020
 * @author Duc Long Hoang
 * Trida pro ziskavani dat a vypocet extremu terenu
 */
public class MySimData {

	/**
	 * Nadefinovani nazvu pro vstup k obrazkum pres pole
	 */
	static String [] pause = new String[] {"img/pause.png", "img/pause_hover.png", "img/pause_press.png"};
	static String [] play = new String[] {"img/play.png", "img/play_hover.png", "img/play_press.png"};
	static String [] faster = new String[] {"img/faster.png", "img/faster_hover.png", "img/faster_press.png", "img/faster_pressNM.png"};
	static String [] slower = new String[] {"img/slower.png", "img/slower_hover.png", "img/slower_press.png", "img/slower_pressNM.png"};
	static String [] roosterDay = new String[] {"img/rooster_day.png", "img/rooster_day_hover.png", "img/rooster_day_press.png"};
	static String[] roosterNight = new String[] {"img/rooster_night.png", "img/rooster_night_hover.png", "img/rooster_night_press.png"};
	
	double terrainLvMax = Double.MIN_VALUE;
	double terrainLvMin = Double.MAX_VALUE;
	Cell [] data;
	
	public MySimData() {
		this.data = Simulator.getData();
	}
	
	/**
	 * Metoda vraci nejvyssi bod terenu
	 * @return nejvyssi bod terenu
	 */
	public double getMaxTerrain() {
		double terrain = 0;
		for(int i = 0; i < data.length; i++) {
			terrain = data[i].getTerrainLevel();
			if(terrain > this.terrainLvMax) this.terrainLvMax = terrain;
		}
		return this.terrainLvMax;
	}
	
	/**
	 * Metoda vraci nejnizsi bod terenu
	 * @return nejnizsi bod terenu
	 */
	public double getMinTerrain() {
		double terrain = 0;
		for(int i = 0; i < data.length; i++) {
			terrain = data[i].getTerrainLevel();
			if(terrain < this.terrainLvMin) this.terrainLvMin = terrain;
		}
		return this.terrainLvMin;
	}
	
	/**
	 * Metoda vraci font v zadane velikosti
	 * @param fontSize, velikost fontu
	 * @return font s velikosti v parametru
	 */
	public static Font font(int fontSize) {
		return new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
	}
}
