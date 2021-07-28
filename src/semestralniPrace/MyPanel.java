package semestralniPrace;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import waterflowsim.Simulator;
import waterflowsim.Vector2D;
import waterflowsim.WaterSourceUpdater;

/**
 * 05.05.2020
 * @author Duc Long Hoang
 * Platno vizualizace
 */
public class MyPanel extends JPanel{
	private static final long serialVersionUID = 1L;


	private static Storage<double[][]> waterLvlData = new Storage<>();
	private static Storage<Double> timeData = new Storage<>();
	MyTime myTime;
	Map map;
	ColorizedMap clrMap;

	/** parametr nextStepu */
	private final double step = 0.02;
	
	/** boolean pro stop/resume vykresleni grafu */
	private static boolean repaintChart = true;
	
	/** Zastavi/pokracuje vykreslovani grafu */
	public void stopResumeChart() {
		repaintChart = !repaintChart;
	}

	/** vyberovy obdelnik */
	private Rectangle rect;
	/** souradnice kliknuti mysi */
	private static int mouseX, mouseY;
	/** souradnice release mysi */
	private static int releaseX, releaseY;
	
	/** kalkulator scalingu */
	private static CalculateRatio ratio = new CalculateRatio();
	/** trida pro ziskavani dat */
	private static MySimData sim = new MySimData();


	
	/** startovaci cas simulace */
	static double startTime = 0;

	// Ulozeni hodnoty casu
	public void storeTimeData() {
		double time = System.currentTimeMillis();
		timeData.store((time - startTime) / 1000.0);
	}

	// Ulozeni hladiny vody ve vsech bodech simulace
	public void storeWaterData(){
		int sirka = ratio.sirka;
		int vyska = ratio.vyska;
		int index = 0;
		double[][] cellData = new double[sirka][vyska];
		for(int i = 0; i < vyska; i++) {
			for(int j = 0; j < sirka; j++) {
				cellData[j][i] = map.waterPoint(index);
				index++;
			}
		}
		waterLvlData.store(cellData);
	}
	
	/**
	 * Metoda spusti scenar, ktery se vykresli podle parametru predaneho za scanneru
	 * @param sc scenar, ktery se spusti
	 */
	public void chooseScenario(Scanner sc) {
		int scenario = Integer.parseInt(sc.nextLine());
		Simulator.runScenario(scenario);
	}
	
	public MyPanel() {
		this.myTime = new MyTime(step);
		//Scanner sc = new Scanner(System.in);
		//chooseScenario(sc);
		this.setPreferredSize(new Dimension(Simulator.getDimension().x, Simulator.getDimension().y));
		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {
	            rect.setBounds(Math.min(mouseX, e.getX()), Math.min(mouseY, e.getY()),
						Math.abs(e.getX() - mouseX), Math.abs(e.getY() - mouseY));
	            repaint();
				
			}
		});
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				rect = null;
				repaint();
				
				final int sirka = ratio.sirka;
				final int vyska = ratio.vyska;
				double pomer = ratio.getRatio(getWidth(), getHeight());
				
				// podminka na release mimo panel
				if(e.getX() > (sirka * pomer) || e.getY() > (vyska * pomer)) return;
				
				releaseX = (int) (e.getX() / pomer);
				releaseY = (int) (e.getY() / pomer);
				
				if(releaseX < 0 || releaseY < 0) return;
				
				// podminka aby mouseClicked != mouseReleased
				if(mouseX != releaseX || mouseY != releaseY) {
					JFrame chartFrame = new JFrame();
					chartFrame.setTitle("Prumerna vyska vodni hladiny oblasti v case");
					makeChartGui(chartFrame);
					ChartPanel chartPanel = new ChartPanel(makeMeanChart());
					chartPanel.setPreferredSize(new Dimension(600, 480));
					chartFrame.add(chartPanel);
					chartFrame.pack();
					
					chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					chartFrame.setLocationRelativeTo(null);
					chartFrame.setVisible(true);
					
					Timer chartTimer = new Timer();
					chartTimer.scheduleAtFixedRate(new TimerTask() {
						
						@Override
						public void run() {
							if(repaintChart) {
								chartPanel.setChart(makeMeanChart());
								chartFrame.repaint();
							}
						}
					}, 0, 100);
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				final int sirka = ratio.sirka;
				final int vyska = ratio.vyska;
				double pomer = ratio.getRatio(getWidth(), getHeight());
				
				// podminka na kliknuti mimo panel
				if(e.getX() > (sirka * pomer) || e.getY() > (vyska * pomer)) return;
				
				mouseX = (int) (e.getX() / pomer);
				mouseY = (int) (e.getY() / pomer);
				rect = new Rectangle(mouseX, mouseY);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				final int sirka = ratio.sirka;
				final int vyska = ratio.vyska;
				double pomer = ratio.getRatio(getWidth(), getHeight());
				
				// podminka na kliknuti mimo panel
				if(e.getX() > (sirka * pomer) || e.getY() > (vyska * pomer)) return;
				
				mouseX = (int) (e.getX() / pomer);
				mouseY = (int) (e.getY() / pomer);
				//int index = (mouseX * mouseY);
				
				//System.out.println("x: " + mouseX + " y: " + mouseY);
				//System.out.println("index: " + index);
				
				JFrame chartFrame = new JFrame();
				chartFrame.setTitle("Vyska vodni hladiny v case");
				makeChartGui(chartFrame);
				ChartPanel chartPanel = new ChartPanel(makeChart());
				chartPanel.setPreferredSize(new Dimension(600, 480));
				chartFrame.add(chartPanel);
				chartFrame.pack();
				
				chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				chartFrame.setLocationRelativeTo(null);
				chartFrame.setVisible(true);
				
				
				Timer chartTimer = new Timer();
				chartTimer.scheduleAtFixedRate(new TimerTask() {
					
					@Override
					public void run() {
						if(repaintChart) {
							chartPanel.setChart(makeChart());
							chartFrame.repaint();
						}
					}
				}, 0, 100);
			}});
			
	}
	
	/**
	 * Udela v okne GUI
	 * @param chartFrame, okno do ktereho se udela GUI
	 */
	public void makeChartGui(JFrame chartFrame) {
		JButton bttnExit = new JButton("Exit");
		JPanel buttons = new JPanel();
		buttons.add(bttnExit);
		chartFrame.setLayout(new BorderLayout());
		chartFrame.add(buttons, BorderLayout.SOUTH);
		
		bttnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chartFrame.dispose();
			}});
		}
	
	/**
	 * Metoda udela graf vysky vody v danem bode zavisle na case
	 * @return graf
	 */
	public static JFreeChart makeChart() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		List<Double> waterHeight = new ArrayList<>();
		for(int i = 0; i < waterLvlData.size(); i++) {
			waterHeight.add(waterLvlData.get(i)[mouseX][mouseY]);
		}
		
		for(int i = 0; i < waterHeight.size(); i++) {
			dataset.addValue(waterHeight.get(i), "Water Level", (timeData.get(i)));
		}
		
		JFreeChart chart = ChartFactory.createLineChart(
				"Vyska vodni hladiny v case",
				"Cas / s",
				"Vodni hladina / m", dataset);
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.gray);
		
		return chart;
	}

	public static JFreeChart makeChart(List<Double> waterLvls){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List<Double> meanHeight = new ArrayList<>();

		for(int i = 0; i < meanHeight.size(); i++) {
			dataset.addValue(meanHeight.get(i), "Vyska vodni hladiny", (timeData.get(i)));
		}

		JFreeChart chart = ChartFactory.createLineChart(
				"Vyska vodni hladiny oblasti v case",
				"Cas / s",
				"Vyska vodni hladiny / m", dataset);
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.gray);
		return chart;
	}

	/**
	 * Metoda udela graf prumernych vysek vody v oblasti na case
	 * @return graf
	 */
	public static JFreeChart makeMeanChart() {
		int x1 = mouseX;
		int y1 = mouseY;
		int x2 = releaseX;
		int y2 = releaseY;
		
		//bereme mouseX a mouseY jako pocatek souradnicoveho systemu
		
		// release v 1. kvadrantu
		if(releaseX > mouseX && releaseY < mouseY) {
			y1 = releaseY; y2 = mouseY;
		} else {
			// release v 2. kvadrantu
			if(releaseX < mouseX && releaseY < mouseY) {
				x1 = releaseX; y1 = releaseY;
				x2 = mouseX; y2 = mouseY;
			} else {
				// release ve 3. kvadrantu
				if(releaseX < mouseX && releaseY > mouseY) {
				x1 = releaseX; x2 = mouseX;
				}
			}
		}

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List<Double> meanHeight = new ArrayList<>();
		
		for(int i = 0; i < waterLvlData.size(); i++) {
			int count = 0;
			double avgData = 0;
			// prochazeni od leveho horniho rohu do praveho spodniho roku obdelnika
			for(int j = y1; j < y2; j++) {
				for(int k = x1; k < x2; k++) {
					// podminka pro release mysi mimo panel
					if(k < 0 || j < 0) break;
					else {
						double data = waterLvlData.get(i)[k][j];
						if(data > 0.0) {
							count++;			// pocet hodnot
							avgData += data;	// pricteni hodnoty
						}
					}
				}
			}
			avgData /= count;				// zprumerovani hodnot
			meanHeight.add(avgData);		// pridani hodnoty do kolekce
		}
		
		for(int i = 0; i < meanHeight.size(); i++) {
			dataset.addValue(meanHeight.get(i), "Vyska vodni hladiny", (timeData.get(i)));
		}

		JFreeChart chart = ChartFactory.createLineChart(
				"Vyska vodni hladiny oblasti v case",
				"Cas / s",
				"Vyska vodni hladiny / m", dataset);
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.gray);
		return chart;
	}

	@Override
	public void paintComponent(Graphics g) {
		Simulator.nextStep(step);
		this.map = new Map();
		this.clrMap = new ColorizedMap(map);
		clrMap.colorizeMap();

		// nastaveni startovaciho casu se zacatkem simulace
		if(startTime == 0) {
			startTime = System.currentTimeMillis();
		}
		// data se ukladaji pouze kdyz simulace bezi
		if(repaintChart) {
			storeWaterData();
			storeTimeData();
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(MyColors.grey);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		drawWaterFlowState(g2d);
		
		if (rect!=null){
			g2d.setStroke(new BasicStroke(2));
			g2d.setColor(Color.black);
            g2d.draw(rect);
        }
	}

	/**
	 * Cele vykresleni jak terenu tak vody
	 * @param g2d, graficky kontext
	 */
	void drawWaterFlowState(Graphics2D g2d) {
		double pomer = ratio.getRatio(this.getWidth(), this.getHeight());
		// prvni scale pro zachovani pomeru okna s rozmery simulace
		g2d.scale(pomer, pomer);
		
		double deltaX = Math.abs(Simulator.getDelta().x);
		double deltaY = Math.abs(Simulator.getDelta().y);
		// druhy scale je pro zachovani pomeru delta
		g2d.scale(deltaX, deltaY);
		
		drawLand(g2d);
	}
	
	/**
	 * Vykredleni cele krajiny
	 * @param g2d, graficky kontext
	 */
	void drawLand(Graphics2D g2d) {
		double terrainLvMin = map.lowestPoint();
		double terrainLvMax = map.highestPoint();
		
		// prochazeni vsech bunek, pokud isDry() == false tak se bunka vybarvi ricni modri
		int index = 0;
		double terrainLv;
		g2d.setStroke(new BasicStroke(2));
		for (int i = 0; i < Simulator.getDimension().y; i++) {
			for (int j = 0; j < Simulator.getDimension().x; j++) {
				// teren
				terrainLv = map.currPoint(index);
				g2d.setColor(
						colorPalette(terrainLv, terrainLvMin, terrainLvMax)
						//clrMap.getColor(index)
				);
				g2d.draw(new Rectangle.Double(j, i, 1, 1));
				// voda
				if(!sim.data[index].isDry()) {
					g2d.setColor(MyColors.waterColor);
					g2d.draw(new Rectangle.Double(j, i, 1, 1));
				}
				index++;
			}
		}
		drawWaterSources(g2d);
	}

	/**
	 * Vykresleni vsech sipek a nazvu
	 * @param g2d, graficky kontext
	 */
	void drawWaterSources(Graphics2D g2d) {
		int sirka = Simulator.getDimension().x;
		
		// ziskavani zdroju
		WaterSourceUpdater[] zdroje = Simulator.getWaterSources();
		for (WaterSourceUpdater zdroj : zdroje) {
			int index = zdroj.getIndex();
			// ulozeni souradnic x a y zdroje, vypocet z hodnoty indexu
			Point2D position = new Point2D.Double((index % sirka), (index / sirka));
			drawWaterFlowLabel(position, Simulator.getGradient(index), zdroj.getName(), g2d);
		}
	}

	/**
	 * Metoda pro posun sipek jsou-li v rozich
	 * V kazdem rohu se vytvori ctverec a pokud ten obsahuje pocatek sipky
	 * tak se pocatek sipky posune blize ke stredu
	 * @param position pozice pocatku sipky
	 * @return novou pozici pocatku pokud je v rohu, jinak vraci puvodni pozici
	 */
	public Point2D testCorners(Point2D position) {
		int sirka = Simulator.getDimension().x;
		int vyska = Simulator.getDimension().y;
		int size = 5;
		
		Rectangle2D.Double r1 = new Rectangle2D.Double(0, 0, size, size);
		Rectangle2D.Double r2 = new Rectangle2D.Double(sirka - size, 0, size, size);
		Rectangle2D.Double r3 = new Rectangle2D.Double(0, vyska - size, size, size);
		Rectangle2D.Double r4 = new Rectangle2D.Double(sirka - size, vyska - size, size, size);
		
		double posX = position.getX();
		double posY = position.getY();
		int shift = 75;
		
		if(r1.contains(position)) return new Point2D.Double(posX + shift, posY + shift);
		if(r2.contains(position)) return new Point2D.Double(posX - shift, posY + shift);
		if(r3.contains(position)) return new Point2D.Double(posX + shift, posY - shift);
		if(r4.contains(position)) return new Point2D.Double(posX - shift, posY - shift);
		return position;
	}
	
	/**
	 * Metoda vykresli sipky se smerem toku a nazvy vodnich zdroju
	 * @param position, misto, kde se nachazi vodni zdroj
	 * @param dirFlow, smer toku v danem miste, aka smer sipky
	 * @param name, nazev vodniho zdroje
	 * @param g2d, graficky kontext
	 */
	void drawWaterFlowLabel(Point2D position, Vector2D<Double> dirFlow, String name, Graphics2D g2d) {
		// vzhled sipky a nazvu
		g2d.setColor(MyColors.arrowColor);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setFont(MySimData.font(15));
		g2d.setStroke(new BasicStroke(3));
		
		// test zda jsou sipky v rozich
		position = testCorners(position);
		
		// vykresleni sipek
		MyArrow arrow = new MyArrow(position, dirFlow, name, g2d);
		arrow.drawArrow(g2d);
		
		// vykresleni nazvu
		AffineTransform transform = g2d.getTransform();
		
		/* pro vykresleni nazvu se pouzije vodni zdroj jako pocatek
		 * a nakresna se otoci tak aby normalizovany vektor dirFlow
		 * byl smerovym vektorem (1,0)
		 */
		g2d.translate(position.getX(), position.getY());
		
		// normalizace vektoru pro dalsi vypocet
		double ux = -dirFlow.x;
		double uy = -dirFlow.y;
		double norm = Math.hypot(ux, uy);
		ux /= norm;
		uy /= norm;
		
		/* vypocet uhlu pro nakloneni nazvu reky podel sipky,
		 * uhel je pocitan jako cosinus uhlu sviraneho
		 * mezi normalizovanym vektorem dirFlow a vektorem (1,0)
		 */
		double angle = Math.acos(ux / Math.hypot(ux, uy));
		g2d.rotate(-angle);
		
		// posun o delku retezce, urcite lepsi nez posun o pevne danou hodnotu
		FontMetrics fm = g2d.getFontMetrics(getFont());
		
		// dodatecne podminky natoceni nazvu pro scenar s indexem = 2
		if(dirFlow.y < 0) g2d.rotate(Math.toRadians(180) + 2 * angle);
		if(dirFlow.x > 0) g2d.translate(-fm.stringWidth(name), 0); 
		if(dirFlow.x * dirFlow.y > 0) g2d.rotate(Math.toRadians(180));
		// nova podminka pro sopku a diskoteku
		if(dirFlow.x > 0 && dirFlow.y > 0) g2d.translate(-2 * fm.stringWidth(name), 0);
			
		// posun o -10, aby nazev nebyl uplne prilepen k sipce
		g2d.drawString(name, 0, -10);
		g2d.setTransform(transform);
	}

	/**
	 * Metoda vraci barvu podle diskretni skaly. Teren je rozdelen do peti casti
	 * @param terrainLv, uroven terenu na daenm indexu
	 * @param terrainLvMin, minimalni vyska terenu
	 * @param terrainLvMax, maximalni vyska terenu
	 * @return barvu podle toho do jakeho vyskoveho patra patri vyska v indexu
	 */

	public Color colorPalette(double terrainLv, double terrainLvMin, double terrainLvMax) {
		double terrainDiff = terrainLvMax - terrainLvMin;
		
		if(terrainLv < ((terrainDiff / 5.0 ) + terrainLvMin)) {
			return MyColors.terrainColors[0];}
		if(terrainLv < ((terrainDiff / (5.0 / 2)) + terrainLvMin)) {
			return MyColors.terrainColors[1];}
		if(terrainLv < ((terrainDiff / (5.0 / 3)) + terrainLvMin)) {
			return MyColors.terrainColors[2];}
		if(terrainLv < ((terrainDiff / (5.0 / 4)) + terrainLvMin)) {
			return MyColors.terrainColors[3];}
		if(((terrainDiff / (5.0 / 4)) + terrainLvMin) <= terrainLv) {
			return MyColors.terrainColors[4];}
		return Color.black;
	}

}
