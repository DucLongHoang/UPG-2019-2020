package semestralniPrace;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import waterflowsim.Simulator;

/**
 * 30.04.2020
 * @author Duc Long Hoang
 * Panel kde si zvlast vykreslim legendu
 */
public class Legend extends JPanel{

	/** velikost kolecek */
	private int ovalSize = 20;
	/** rozmezi mezi textem a kolecky */
	private int gap = 15;
	/** pole s popisky legendy */
	private static String[] text = getHeightDiff();
	
	public Legend() {
		this.setPreferredSize(new Dimension(Simulator.getDimension().x / 3, 
				Simulator.getDimension().y));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g2d);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// platno
		g2d.setColor(MyColors.grey);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		// posun protoze z neznameho duvodu saha panel az na horni okraj okna
		g2d.translate(0, 30);
		drawLegend(g2d);
	}

	/**
	 * Vykresleni cele legendy
	 * @param g2d, graficky kontext
	 */
	private void drawLegend(Graphics2D g2d) {
		drawTitle(g2d);
		drawLabels(g2d);
	}

	/**
	 * Vykresleni barevnych kolecek a popisku k nim
	 * @param g2d, graficky kontext
	 */
	private void drawLabels(Graphics2D g2d) {
		for(int i = 0; i < 6; i++) {
			g2d.setColor(Color.black);
			g2d.setFont(MySimData.font(10));
			g2d.drawString(text[i], (int) (2.5 * gap), gap * (2 + 2 * i));
			g2d.setColor(MyColors.terrainColors[i]);
			g2d.fillOval(gap, gap * (1 + 2 * i), ovalSize, ovalSize);
			g2d.setColor(Color.BLACK);
			g2d.drawOval(gap, gap * (1 + 2 * i), ovalSize, ovalSize);
		}
	}
	
	/**
	 * Vykresleni titulku "Legenda"
	 * @param g2d, graficky kontext
	 */
	private void drawTitle(Graphics2D g2d) {
		g2d.setColor(Color.black);
		g2d.setFont(MySimData.font(20));
		
		String s = "Legenda";
		FontMetrics f = g2d.getFontMetrics(g2d.getFont());
		int x = (int) (Simulator.getDimension().x / 6.0);
		g2d.drawString(s, x - (f.stringWidth(s) / 2), 0);
	}

	/**
	 * Vytvori pole popisku k legende
	 */
	private static String[] getHeightDiff() {
		String[] array = new String[6];
		
		// vypocet min a max terenu
		MySimData data = new MySimData();
		double min = data.getMinTerrain();
		double max = data.getMaxTerrain();
		double dif = max - min;
		
		for(int i = 0; i < array.length; i++) {
			array[i] = String.format("  %.1f m - %.1f m",
					(dif * 0.2 * i + min),
					(dif * 0.2 * (i + 1) + min));
		}
		array[5] = "  Voda";
		return array;
	}
	
}
