package semestralniPrace;

import java.awt.Color;

/**
 * 03.05.2020
 * @author Duc Long Hoang
 * Trida kam si chodim pro barvicky
 */
public class MyColors {
	
	/** barvy sipek a nazvu zdroju */
	static Color arrowColor = Color.DARK_GRAY;
	static Color dayArrow = Color.DARK_GRAY;
	static Color nightArrow = Color.getHSBColor((float)(55.0/360), 0.7f, 0.8f);
	
	/** barva UI, legendy a "niceho" */
	static Color grey = Color.getHSBColor(0, 0, 0.77f);
	
	/** barvy vody */
	static Color waterColor = Color.getHSBColor((float)(170.0/360), 0.65f, 0.65f);
	static Color dayWater = Color.getHSBColor((float)(170.0/360), 0.65f, 0.65f);
	static Color nightWater = Color.getHSBColor((float)(335.0/360), 0.5f, 0.45f);
	
	/** teren rozdelen do peti barev + voda */
	static Color[] terrainColors = new Color[] {
			// zelena
			Color.getHSBColor((float)(100.0/360), 0.6f, 0.6f),
			Color.getHSBColor((float)(80.0/360), 0.4f, 0.6f),
			Color.getHSBColor((float)(50.0/360), 0.4f, 0.65f),
			Color.getHSBColor((float)(37.0/360), 0.6f, 0.65f),
			// hneda
			Color.getHSBColor((float)(25.0/360), 0.8f, 0.65f),
			
			// voda
			waterColor
	};
	
	/** stare barvicky terenu, mohou se hodit*/
	static Color[] dayTerrain = new Color[] {
			// zelena
			Color.getHSBColor((float)(100.0/360), 0.6f, 0.6f),
			Color.getHSBColor((float)(80.0/360), 0.4f, 0.6f),
			Color.getHSBColor((float)(50.0/360), 0.4f, 0.65f),
			Color.getHSBColor((float)(37.0/360), 0.6f, 0.65f),
			// hneda
			Color.getHSBColor((float)(25.0/360), 0.8f, 0.65f),
			
			// voda
			dayWater
	};
	
	static Color[] nightTerrain = new Color[] {
			// zelena
			Color.getHSBColor((float)(220.0/360), 0.2f, 0.3f),
			Color.getHSBColor((float)(220.0/360), 0.4f, 0.4f),
			Color.getHSBColor((float)(220.0/360), 0.6f, 0.45f),
			Color.getHSBColor((float)(165.0/360), 0.5f, 0.5f),
			// hneda
			Color.getHSBColor((float)(75.0/360), 0.5f, 0.5f),
			
			// voda
			nightWater
	};

	// change color modes
	public void nightMode() {
		if(waterColor.equals(dayWater)) {
			waterColor = nightWater;
			terrainColors = nightTerrain;
			arrowColor = nightArrow;
		} else {
			waterColor = dayWater;
			terrainColors = dayTerrain;
			arrowColor = dayArrow;
		}
		
	}
}
