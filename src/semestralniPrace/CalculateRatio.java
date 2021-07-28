package semestralniPrace;

import waterflowsim.Simulator;

/**
 * 29.04.2020
 * @author Duc Long Hoang
 * Trida pro vypocet scalingu
 */
public class CalculateRatio {

	int sirka;
	int vyska;
	
	public CalculateRatio() {
		this.sirka = Simulator.getDimension().x;
		this.vyska = Simulator.getDimension().y;
	}
	
	/**
	 * Vypocet scalingu
	 * @param width, sirka okna
	 * @param height, vyska okna
	 * @return pomer
	 */
	public double getRatio(int width, int height) {
		double pomerX = (double) width / (this.sirka * Math.abs(Simulator.getDelta().x));
		double pomerY = (double) height / (this.vyska * Math.abs(Simulator.getDelta().y));
		return Math.min(pomerX, pomerY);
	}
}
