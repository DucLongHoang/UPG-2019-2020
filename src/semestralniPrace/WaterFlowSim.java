package semestralniPrace;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import waterflowsim.Simulator;

/**
 * 04.05.2020
 * @author Duc Long Hoang
 * Hlavni okno vizualizace
 */
public class WaterFlowSim extends JFrame {

	private static final long serialVersionUID = 1L;



	/** boolean pro prekresleni, stop/resume funkce */
	private static boolean repaint = true;
	/** boolean zda je night mode ci neni */
	private static boolean isNight = false;

	public static void main(String[] args) throws IOException {
		
		int scenario = 0;
		if(args.length > 0)
			scenario = Integer.parseInt(args[0]);
		Simulator.runScenario(scenario);
		
		JFrame win = new JFrame();
		win.setTitle("UPG - Semestralni prace: A19B0054P");
		makeGui(win);
		win.pack();
		
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setLocationRelativeTo(null);
		win.setVisible(true);

		// casovac na prekreslovani okna
		Timer myTimer = new Timer();
		myTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if(repaint) {
					win.repaint();
				}
			}
		}, 0, 100);
	}
	
	/**
	 * Metoda nastavi tlacitko a zmeni ikonu
	 * @param button, tlacitko ktere se bude nastavovat
	 * @param fileName, umisteni souboru
	 * @throws IOException	vyjimka
	 */
	private static void setButton(JButton button, String fileName) throws IOException {
		Image img = ImageIO.read(new File(fileName));
		Image newIcon = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		button.setIcon(new ImageIcon(newIcon));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setContentAreaFilled(false);
		button.setFocusable(false);
	}

	/**
	 * Identicka metoda jen se da nastavit pomer vysky a sirky
	 * @param button	tlacitko
	 * @param fileName nazev souboru
	 * @param width	sirka
	 * @param size	velikost
	 * @throws IOException	vyjimka
	 */
	private static void setButton(JButton button, String fileName, int width, int size) throws IOException {
		Image img = ImageIO.read(new File(fileName));
		Image newIcon = img.getScaledInstance(width, size, Image.SCALE_SMOOTH);
		button.setIcon(new ImageIcon(newIcon));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setContentAreaFilled(false);
		button.setFocusable(false);
	}

	/**
	 * Metoda pro zmena ikony
	 * @param button, tlacitko na kterem se zmeni ikona
	 * @param fileName, umisteni souboru
	 * @throws IOException vyjimka
	 */
	public static void changeIcon(JButton button, String fileName) throws IOException {
		Image img = ImageIO.read(new File(fileName));
		Image newIcon = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		button.setIcon(new ImageIcon(newIcon));
	}

	/**
	 * Identicka metoda jen se da nastavit pomer sirky a vysky
	 * @param button	tlacitko co se zmeni
	 * @param fileName	nazev souboru
	 * @param width	sirka
	 * @param size	velikost
	 * @throws IOException	vyjimka
	 */
	public static void changeIcon(JButton button, String fileName, int width, int size) throws IOException {
		Image img = ImageIO.read(new File(fileName));
		Image newIcon = img.getScaledInstance(width, size, Image.SCALE_SMOOTH);
		button.setIcon(new ImageIcon(newIcon));
	}
	
	private static void makeGui(JFrame win) throws IOException {
		// buttony
		JButton bttnFaster = new JButton();
		JButton bttnSlower = new JButton();
		JButton bttnPausePlay = new JButton();
		JButton nightMode = new JButton();
		
		// nastaveni ikon
		setButton(bttnFaster, "img/faster.png");
		setButton(bttnSlower, "img/slower.png");
		setButton(bttnPausePlay, "img/pause.png");
		setButton(nightMode, MySimData.roosterDay[0], 80, 50);
		
		// pridani buttonu do panelu
		JPanel buttons = new JPanel();
		buttons.setBackground(MyColors.grey);
		buttons.add(bttnSlower);
		buttons.add(bttnPausePlay);
		buttons.add(bttnFaster);
		
		// vytvoreni dalsich panelu
		MyPanel panel = new MyPanel();
		Legend legend = new Legend();
		JPanel labelPanel = new JPanel();
			JLabel simSpeed = new JLabel();
			labelPanel.setBackground(MyColors.grey);
			labelPanel.add(nightMode);
			labelPanel.add(simSpeed);
			simSpeed.setText("Rychlost simulace: " + panel.myTime.getSpeed() + " %");
		
		// instance tridy MyColors pro zmenu barev
		MyColors night = new MyColors();
			
		// pridani panelu do okna
		win.setLayout(new BorderLayout());
		win.add(panel, BorderLayout.CENTER);
		win.add(buttons, BorderLayout.SOUTH);
		win.add(legend, BorderLayout.EAST);
		win.add(labelPanel, BorderLayout.NORTH);
		
		// udalosti k tlacitkum
		

		// nastaveni tlacitka Night Mode
		nightMode.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				night.nightMode();
				if(isNight) {
						try {if(nightMode.contains(e.getPoint())) changeIcon(nightMode, MySimData.roosterDay[1], 80, 50);
							else changeIcon(nightMode, MySimData.roosterDay[0], 80, 50);
							isNight = false;
						} catch (IOException e1) {e1.printStackTrace();}
				} else {
						try {if(nightMode.contains(e.getPoint())) changeIcon(nightMode, MySimData.roosterNight[1], 80, 50);
							else changeIcon(nightMode, MySimData.roosterNight[0], 80, 50);
							isNight = true;
						} catch (IOException e2) {e2.printStackTrace();}
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				try {if(!isNight) changeIcon(nightMode, MySimData.roosterDay[2], 80, 50);
					else changeIcon(nightMode, MySimData.roosterNight[2], 80, 50);
				} catch (IOException e1) {e1.printStackTrace();}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				try {if(!isNight) changeIcon(nightMode, MySimData.roosterDay[0], 80, 50);
					else changeIcon(nightMode, MySimData.roosterNight[0], 80, 50);
				} catch (IOException e1) {e1.printStackTrace();}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
					try {if(!isNight) changeIcon(nightMode, MySimData.roosterDay[1], 80, 50);
						else changeIcon(nightMode, MySimData.roosterNight[1], 80, 50);
					} catch (IOException e1) {e1.printStackTrace();}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		
		
		// Nastaveni tlacitka zpomalit
		bttnSlower.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				panel.myTime.slowDown();
				if(repaint) {
				simSpeed.setText("Rychlost simulace: " + panel.myTime.getSpeed() + " %");}
				try {
					if(bttnSlower.contains(e.getPoint())) changeIcon(bttnSlower, MySimData.slower[1]);
					else changeIcon(bttnSlower, MySimData.slower[0]);
				} catch (IOException e1) {e1.printStackTrace();}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					if(isNight) {
						changeIcon(bttnSlower, MySimData.slower[3]);
					} else changeIcon(bttnSlower, MySimData.slower[2]);
				} catch (IOException e1) {e1.printStackTrace();}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				try {changeIcon(bttnSlower, MySimData.slower[0]);
				} catch (IOException e1) {e1.printStackTrace();}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				try {changeIcon(bttnSlower, MySimData.slower[1]);
				} catch (IOException e1) {e1.printStackTrace();}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		
		// Nastaveni tlacitka zrychlit
		bttnFaster.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				panel.myTime.speedUp();
				if(repaint) {
				simSpeed.setText("Rychlost simulace: " + panel.myTime.getSpeed() + " %");}
				try {
					if(bttnFaster.contains(e.getPoint())) changeIcon(bttnFaster, MySimData.faster[1]);
					else changeIcon(bttnFaster, MySimData.faster[0]);
				} catch (IOException e1) {e1.printStackTrace();}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					if(isNight) {
						changeIcon(bttnFaster, MySimData.faster[3]);
					} else changeIcon(bttnFaster, MySimData.faster[2]);
				} catch (IOException e1) {e1.printStackTrace();}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				try {changeIcon(bttnFaster, MySimData.faster[0]);
				} catch (IOException e1) {e1.printStackTrace();}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				try {changeIcon(bttnFaster, MySimData.faster[1]);
				} catch (IOException e1) {e1.printStackTrace();}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		
		// Nastaveni tlacitka Play/Pause
		bttnPausePlay.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				stopResume(simSpeed, bttnPausePlay, panel);
				try {
					if(repaint) {
						if(bttnPausePlay.contains(e.getPoint())) changeIcon(bttnPausePlay, MySimData.pause[1]);
						else changeIcon(bttnPausePlay, MySimData.play[0]);
					} else {
						if(bttnPausePlay.contains(e.getPoint())) changeIcon(bttnPausePlay, MySimData.play[1]);
						else changeIcon(bttnPausePlay, MySimData.pause[0]);}
				} catch (IOException e1) {e1.printStackTrace();}}
			
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					if(repaint) {
						changeIcon(bttnPausePlay, MySimData.pause[2]);
					} else {changeIcon(bttnPausePlay, MySimData.play[2]);}
				} catch (IOException e1) {e1.printStackTrace();}}
			
			@Override
			public void mouseExited(MouseEvent e) {
					try {
					if(repaint) {
						changeIcon(bttnPausePlay, MySimData.pause[0]);
					} else {changeIcon(bttnPausePlay, MySimData.play[0]);}
					} catch (IOException e1) {e1.printStackTrace();}}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				try {
					if(repaint) {
						changeIcon(bttnPausePlay, MySimData.pause[1]);
					} else {changeIcon(bttnPausePlay, MySimData.play[1]);}
				} catch (IOException e1) {e1.printStackTrace();}}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
	}
	
	/**
	 * Metoda pauzne nebo pokracuje se simulaci/grafem, vypise rychlost simulace
	 * @param label, label kde se nachazi text s rychlosti simulace
	 * @param button, button, kterym se vola tato metoda
	 * @param panel, panel pro pristup ke zmene rychlosti grafu
	 */
	public static void stopResume(JLabel label, JButton button, MyPanel panel) {
		panel.stopResumeChart();
		if(repaint) {
			repaint = false;
			label.setText("Rychlost simulace: 0 %");
		} else {
			repaint = true;
			label.setText("Rychlost simulace: " + panel.myTime.getSpeed() + " %");
		}
	}
	
}
