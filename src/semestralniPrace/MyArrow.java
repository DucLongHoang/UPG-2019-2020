package semestralniPrace;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import waterflowsim.Vector2D;

/**
 * 05.05.2020
 * @author Duc Long Hoang
 * Trida na vykresleni sipky
 */
public class MyArrow {
	double Ax, Ay, Ux, Uy;
	static final int K = 7;
	static final int L = 12;
	static final int ARR_LENGTH = 40;
	static final double RATIO = ARR_LENGTH / 40.0;
	
	/**
	 * Konstruktor pro sipku
	 * @param position zacatek sipky
	 * @param dirFlow smer toku
	 * @param name nazev sipky
	 * @param g2d graficky kontext
	 */
	public MyArrow(Point2D position, Vector2D<Double> dirFlow,
			String name, Graphics2D g2d) {
		this.Ax = position.getX();
		this.Ay = position.getY();
		this.Ux = -dirFlow.x;
		this.Uy = -dirFlow.y;
	}
	
	public void drawArrow(Graphics2D g2d) {
		// normalizace vektoru
		double length = Math.hypot(Ux, Uy);
		Ux /= length;
		Uy /= length;
		
		// delka sipky
		double Vx = Ux * ARR_LENGTH;
		double Vy = Uy * ARR_LENGTH;
		
		//konec sipky
		double Bx = Ax + Vx;
		double By = Ay + Vy;
		
		// vykresleni tela sipky
		g2d.draw(new Line2D.Double(Ax, Ay, Bx, By));
		
		// vykresleni hlavice sipky
		double Nx = Uy;
		double Ny = -Ux;
		double Cx = Bx - Ux * L * RATIO;
		double Cy = By - Uy * L * RATIO;
		double D1x = Cx - Nx * K * RATIO;
		double D1y = Cy - Ny * K * RATIO;
		double D2x = Cx + Nx * K * RATIO;
		double D2y = Cy + Ny * K * RATIO;
		g2d.draw(new Line2D.Double(D1x, D1y, Bx, By));
		g2d.draw(new Line2D.Double(D2x, D2y, Bx, By));
	}
	
}
