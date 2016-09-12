package net.Dankrushen.SansTrack;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import javax.swing.JPanel;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Robot;
import java.awt.Toolkit;

public class SansTrack implements NativeMouseInputListener, WindowListener, NativeKeyListener {

	private JFrame frmSansTracker;
	private JLabel lblEaten;
	private JLabel lblDelay;
	private JLabel menu;
	private JPanel panel_1;
	private JLabel label;
	private JLabel atc;

	private HashMap<Integer, String> hmap = new HashMap<Integer, String>();
	private ArrayList<String> keys = new ArrayList<String>();
	private ArrayList<Integer> keys2 = new ArrayList<Integer>();
	private int[][] s = new int[10][5];
	private boolean[] b = new boolean[4];

	private String rand = "Random";
	private String both = "Bones from both sides";
	private String blueshort = "Blue bone short bone, reversed";
	private String jump = "Jumpy platforms";
	private String moving = "Moving bones and platforms";
	private String back = "Back and forth platform";
	private String laser = "Laser platforms";
	private String topbottom = "Top and bottom bones";
	private String spare = "Spare";
	private String randmix = "Randomized Mix";
	private String floaty = "Floaty lasers";
	private String upfloor = "Bones up from floor";
	private String updown = "Up and down bones";

	private Robot rb;

	private int x = 0;
	private int y = 0;

	private int food = 0;
	private int delay = 0;
	private int attack = 0;

	private boolean me = false;
	private int setSpots = 0;
	private int[] spots = new int[] {705, 763, 876, 756, 1077, 757, 1176, 757};
	
	private int fontSize = 35;
	private int DebugMode = 1;
	/* DebugMode options
	 * 0: Disabled
	 * 1: Shows keys pressed
	 * 2: Shows colour checking info
	 * 3: Shows position fixing info
	 * 4: Shows spot setting info
	 */

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} catch (Exception e) {}
				try {
					SansTrack window = new SansTrack();
					window.frmSansTracker.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SansTrack() {
		GlobalScreen.setEventDispatcher(new SwingDispatchService());
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSansTracker = new JFrame();
		frmSansTracker.setTitle("Sans Tracker");
		frmSansTracker.setResizable(false);
		frmSansTracker.addWindowListener(this);
		frmSansTracker.setBounds(100, 100, 650, 446);
		frmSansTracker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSansTracker.setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		frmSansTracker.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		Font f = new Font("Tahoma", Font.PLAIN, fontSize);

		lblEaten = new JLabel("Food Eaten: 0");
		lblEaten.setFont(f);
		lblEaten.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblEaten, BorderLayout.NORTH);

		lblDelay = new JLabel("Delay: 0");
		lblDelay.setFont(f);
		lblDelay.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblDelay, BorderLayout.SOUTH);

		menu = new JLabel("");
		menu.setHorizontalAlignment(SwingConstants.CENTER);
		frmSansTracker.getContentPane().add(menu, BorderLayout.CENTER);
		menu.setIcon(new ImageIcon(SansTrack.class.getResource("/Menu/0.png")));

		panel_1 = new JPanel();
		frmSansTracker.getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		label = new JLabel("Next Attack:");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.PLAIN, 35));
		panel_1.add(label, BorderLayout.NORTH);

		atc = new JLabel(" ");
		atc.setHorizontalAlignment(SwingConstants.CENTER);
		atc.setFont(new Font("Tahoma", Font.PLAIN, 35));
		panel_1.add(atc, BorderLayout.SOUTH);

		try {
			rb = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		resetImages();

		hmap.put(0, both);
		hmap.put(1, blueshort);
		hmap.put(2, both);
		hmap.put(3, jump);
		hmap.put(4, jump);
		hmap.put(5, moving);
		hmap.put(6, back);
		hmap.put(7, laser);
		hmap.put(8, back);
		hmap.put(9, both);
		hmap.put(10, topbottom);
		hmap.put(11, both);
		hmap.put(12, laser);
		hmap.put(13, rand);
		hmap.put(14, spare);
		hmap.put(15, randmix);
		hmap.put(16, floaty);
		hmap.put(17, randmix);
		hmap.put(18, upfloor);
		hmap.put(19, upfloor);
		hmap.put(20, floaty);
		hmap.put(21, updown);
		hmap.put(22, randmix);
		hmap.put(23, upfloor);

		atc.setText(hmap.get(attack));

		keys2.add(NativeKeyEvent.VC_UP);
		keys2.add(NativeKeyEvent.VC_DOWN);
		keys2.add(NativeKeyEvent.VC_LEFT);
		keys2.add(NativeKeyEvent.VC_RIGHT);
		keys2.add(NativeKeyEvent.VC_Z);
		keys2.add(NativeKeyEvent.VC_X);
	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent arg0) {
		if(setSpots > 0) {
			if(setSpots == 1) {
				setSpots++;
				spots = new int[8];
				spots[0] = arg0.getX();
				spots[1] = arg0.getY();
				if(DebugMode == 4) System.out.println("Set spot 1!");
			} else if(setSpots == 2) {
				setSpots++;
				spots = new int[8];
				spots[2] = arg0.getX();
				spots[3] = arg0.getY();
				if(DebugMode == 4) System.out.println("Set spot 2!");
			} else if(setSpots == 3) {
				setSpots++;
				spots = new int[8];
				spots[4] = arg0.getX();
				spots[5] = arg0.getY();
				if(DebugMode == 4) System.out.println("Set spot 3!");
			} else if(setSpots == 4) {
				spots = new int[8];
				spots[6] = arg0.getX();
				spots[7] = arg0.getY();
				setSpots = 0;
				if(DebugMode == 4) System.out.println("Set spot 4!");
				if(DebugMode == 4 || DebugMode == 1) System.out.println("Finished spot setting!");
			}
		}
	}

	public int getKeyCode(char c) {
		final char c2 = String.valueOf(c).toUpperCase().charAt(0);
		if(Character.isLetter(c)) {
			return (int)c2;
		}
		else if(Character.isDigit(c)) {
			return 48+(int)c2;
		}
		return 0;
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent arg0) { /* Unimplemented */ }

	@Override
	public void nativeMouseReleased(NativeMouseEvent arg0) { /* Unimplemented */ }

	@Override
	public void nativeMouseDragged(NativeMouseEvent arg0) { /* Unimplemented */ }

	@Override
	public void nativeMouseMoved(NativeMouseEvent arg0) { /* Unimplemented */ }

	@Override
	public void windowClosed(WindowEvent arg0) {
		//Clean up the native hook.
		try {
			GlobalScreen.unregisterNativeHook();
		} catch (NativeHookException e) {
			e.printStackTrace();
		}
		System.runFinalization();
		System.exit(0);
	}

	@Override
	public void windowClosing(WindowEvent e) { /* Unimplemented */ }

	@Override
	public void windowIconified(WindowEvent e) { /* Unimplemented */ }

	@Override
	public void windowDeiconified(WindowEvent e) { /* Unimplemented */ }

	@Override
	public void windowActivated(WindowEvent e) { /* Unimplemented */ }

	@Override
	public void windowDeactivated(WindowEvent e) { /* Unimplemented */ }

	@Override
	public void windowOpened(WindowEvent arg0) {
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			ex.printStackTrace();

			System.exit(1);
		}

		// Clear previous logging configurations.
		LogManager.getLogManager().reset();

		// Get the logger for "org.jnativehook" and set the level to off.
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);

		GlobalScreen.addNativeMouseListener(this);
		GlobalScreen.addNativeMouseMotionListener(this);
		GlobalScreen.addNativeKeyListener(this);
	}

	public void itemLoop(boolean isLeft) {
		if(isLeft) {
			x++;
			while(s[x][y] != -1) x++;
			if(s[6][3] != -1) x = x-2;
			else x--;
		} else {
			x--;
			while(s[x][y] != -1) x--;
			x++;
		}
	}

	public void foodRemove() {
		/*
		//First Page
		s[4][3] = 8;  s[5][3] = 9;
		s[4][2] = 10; s[5][2] = 11;

		//Second Page
		s[6][3] = 12; s[7][3] = 13;
		s[6][2] = 14; s[7][2] = 15;
		 */

		if(s[5][3] == -1) {
			s[4][3] = -1; s[5][3] = -1;
			s[4][2] = -1; s[5][2] = -1;

			s[6][3] = -1; s[7][3] = -1;
			s[6][2] = -1; s[7][2] = -1;
		} else if(s[4][2] == -1) {
			s[4][3] = 27; s[5][3] = -1;
			s[4][2] = -1; s[5][2] = -1;

			s[6][3] = -1; s[7][3] = -1;
			s[6][2] = -1; s[7][2] = -1;
		} else if(s[5][2] == -1) {
			s[4][3] = 25; s[5][3] = 26;
			s[4][2] = -1; s[5][2] = -1;

			s[6][3] = -1; s[7][3] = -1;
			s[6][2] = -1; s[7][2] = -1;
		} else if(s[6][3] == -1) {
			s[4][3] = 22; s[5][3] = 23;
			s[4][2] = 24; s[5][2] = -1;

			s[6][3] = -1; s[7][3] = -1;
			s[6][2] = -1; s[7][2] = -1;
		} else if(s[7][3] == -1) {
			s[4][3] = 8;  s[5][3] = 9;
			s[4][2] = 10; s[5][2] = 11;

			s[6][3] = -1; s[7][3] = -1;
			s[6][2] = -1; s[7][2] = -1;
		} else if(s[6][2] == -1) {
			s[4][3] = 8;  s[5][3] = 9;
			s[4][2] = 10; s[5][2] = 11;

			s[6][3] = 21; s[7][3] = -1;
			s[6][2] = -1; s[7][2] = -1;
		} else if(s[7][2] == -1) {
			s[4][3] = 8;  s[5][3] = 9;
			s[4][2] = 10; s[5][2] = 11;

			s[6][3] = 19; s[7][3] = 20;
			s[6][2] = -1; s[7][2] = -1;
		} else {
			s[4][3] = 8;  s[5][3] = 9;
			s[4][2] = 10; s[5][2] = 11;

			s[6][3] = 16; s[7][3] = 17;
			s[6][2] = 18; s[7][2] = -1;
		}

		food++;
		delay++;

		lblEaten.setText("Food Eaten: " + food);
		lblDelay.setText("Delay: " + delay);
	}

	public void resetImages() {
		for(int a = 0; a < 10; a++) {
			for(int b = 0; b < 5; b++) {
				s[a][b] = -1;
			}
		}

		s[0][0] = 0;
		s[2][0] = 1;
		s[4][0] = 2;
		s[9][0] = 3;
		s[0][2] = 4;
		s[2][2] = 5;
		s[4][2] = 10;
		s[5][2] = 11;
		s[6][2] = 14;
		s[7][2] = 15;
		s[9][2] = 7;
		s[4][3] = 8;
		s[5][3] = 9;
		s[6][3] = 12;
		s[7][3] = 13;
		s[2][4] = 6;
	}

	public boolean pixlCheck() {
		setCord();

		for(int i = 0; i < 4; i++) {
			if(b[i]) {
				if(DebugMode == 2) System.out.println("Color Match at Button " + i);
				me = true;
				return true;
			} else if(DebugMode == 2) System.out.println("Colour Not Found at Button " + i);
		}

		return false;
	}

	public boolean inMenu(int arg0) {
		if(keys2.contains(arg0)) {
			if(!me) {
				if(DebugMode == 2) System.out.println("Checking Color...");

				if(pixlCheck()) return true;

			} else return true;
		}
		return false;
	}

	public void setCord() {
		Color px = new Color(255,255,0);
		Color px1 = new Color(255,255,64);
		b = new boolean[]{rb.getPixelColor(spots[0], spots[1]).equals(px1), rb.getPixelColor(spots[2], spots[3]).equals(px), rb.getPixelColor(spots[4], spots[5]).equals(px), rb.getPixelColor(spots[6], spots[7]).equals(px)};
	}

	public void fixPos() {
		setCord();

		for(int i = 0; i < 4; i++) {
			if(b[i]) {
				if(DebugMode == 3) System.out.println("Color Match!");
				if(DebugMode == 3) System.out.println("i = " + i);
				if(i == 0) {
					if(x != 0) {
						x = 0;
						y = 0;
					}
				} else if(i == 1) {
					if(x != 2) {
						x = 2;
						y = 0;
					}
				} else if(i == 2) {
					if(!((x < 8 && x > 3) && (y < 4 && y > 1)) || (x == 4 && y == 0)) {
						x = 4;
						y = 0;
					}
				} else if(i == 3) {
					if(x != 9) {
						x = 9;
						y = 0;
					}
				}
				me = true;
			} else if(DebugMode == 3) System.out.println("Colour Not Found");
		}

		b = null;
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		//390, 1021 | 741, 1023 | 1101, 1021 | 1437, 1021

		if(inMenu(arg0.getKeyCode())) {
			if(arg0.getKeyCode() == NativeKeyEvent.VC_UP && !keys.contains("Up")) {
				keys.add("Up");
				if(DebugMode == 1) System.out.println("Up");
				if((x < 8 && x > 3) && (y < 4 && y > 1)) {
					y++;
					if(s[x][y] == -1) y = y-2;
					if(s[x][y] == -1) y = y+1;
				}
			} else if(arg0.getKeyCode() == NativeKeyEvent.VC_DOWN && !keys.contains("Down")) {
				keys.add("Down");
				if(DebugMode == 1) System.out.println("Down");
				if((x < 8 && x > 3) && (y < 4 && y > 1)) {
					y--;
					if(s[x][y] == -1) y = y+2;
					if(s[x][y] == -1) y = y-1;
				}
			} else if(arg0.getKeyCode() == NativeKeyEvent.VC_LEFT && !keys.contains("Left")) {
				keys.add("Left");
				if(DebugMode == 1) System.out.println("Left");
				if(y == 0) {
					if(x == 9) x = 4;
					else if(x == 0) x = 9;
					else x = x-2;
				} else if((x < 8 && x > 3) && (y < 4 && y > 1)) {
					x--;
					if(s[x][y] == -1){
						if(y == 3) itemLoop(true);
						else x++;
					}
				}
			} else if(arg0.getKeyCode() == NativeKeyEvent.VC_RIGHT && !keys.contains("Right")) {
				keys.add("Right");
				if(DebugMode == 1) System.out.println("Right");
				if(y == 0) {
					if(x == 4) x = 9;
					else if(x == 9) x = 0;
					else x = x+2;
				} else if((x < 8 && x > 3) && (y < 4 && y > 1)) {
					x++;
					if(s[x][y] == -1) itemLoop(false);
				}
			}

			try {
				Thread.sleep(3);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			fixPos();

			if(arg0.getKeyCode() == NativeKeyEvent.VC_Z && !keys.contains("Z")) {
				keys.add("Z");
				if(DebugMode == 1) System.out.println("Z");
				if(x == 4 && y == 0 && s[4][3] != -1) {
					y = y+3;
				} else if ((x == 0 && y == 0) || (x == 2 && y == 0) || (x == 9 && y == 0) || (x == 2 && y == 2)) {
					y = y+2;
				} else if(x == 0 && y == 2) {
					me = false;
					if(!(hmap.get(attack).equalsIgnoreCase(rand) && delay > 0)) attack++;
					else delay--;
					x = 0;
					y = 0;
				} else if(x == 2 && y == 4) {
					me = false;
					delay++;
					if(!(hmap.get(attack).equalsIgnoreCase(rand) && delay > 0) && !hmap.get(attack).equalsIgnoreCase(spare)) attack++;
					x = 2;
					y = 0;
				} else if((x < 8 && x > 3) && (y < 4 && y > 1)) {
					me = false;
					foodRemove();
					if(!(hmap.get(attack).equalsIgnoreCase(rand) && delay > 0) && !hmap.get(attack).equalsIgnoreCase(spare)) attack++;
					x = 4;
					y = 0;
				} else if(x == 9 && y == 2) {
					me = false;
					delay++;
					if(!(hmap.get(attack).equalsIgnoreCase(rand) && delay > 0)) attack++;
					x = 9;
					y = 0;
				}
			} else if(arg0.getKeyCode() == NativeKeyEvent.VC_X && !keys.contains("X")) {
				keys.add("X");
				if(DebugMode == 1) System.out.println("X");
				if((x < 8 && x > 3) && (y < 4 && y > 1)) {
					x = 4;
					y = 0;
				} else if ((x == 0 && y == 2) || (x == 2 && y == 2) || (x == 9 && y == 2) || (x == 2 && y == 4)) {
					y = y-2;
				}
			}
			menu.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(SansTrack.class.getResource("/Menu/" + s[x][y] + ".png"))));
		} else if(arg0.getKeyCode() == NativeKeyEvent.VC_M && !keys.contains("M")) {
			keys.add("M");
			if(DebugMode == 1) System.out.println("M");
			x = 0;
			y = 0;
			resetImages();
			food = 0;
			delay = 0;
			attack = 0;
			me = false;
			atc.setText(hmap.get(attack));
			menu.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(SansTrack.class.getResource("/Menu/" + s[x][y] + ".png"))));
		} else if(arg0.getKeyCode() == NativeKeyEvent.VC_N && !keys.contains("N")) {
			keys.add("N");
			if(DebugMode == 1) System.out.println("N");
			setSpots = 1;
			if(DebugMode == 4 || DebugMode == 1) System.out.println("Started spot setting!");
		} else if(!atc.getText().equalsIgnoreCase(hmap.get(attack))) {
			new Thread() {
				public void run() {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					atc.setText(hmap.get(attack));
				}
			}.start();
		}

		lblEaten.setText("Food Eaten: " + food);
		lblDelay.setText("Delay: " + delay);
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		if(arg0.getKeyCode() == NativeKeyEvent.VC_UP && keys.contains("Up")) {
			keys.remove("Up");
		} else if(arg0.getKeyCode() == NativeKeyEvent.VC_DOWN && keys.contains("Down")) {
			keys.remove("Down");
		} else if(arg0.getKeyCode() == NativeKeyEvent.VC_LEFT && keys.contains("Left")) {
			keys.remove("Left");
		} else if(arg0.getKeyCode() == NativeKeyEvent.VC_RIGHT && keys.contains("Right")) {
			keys.remove("Right");
		} else if(arg0.getKeyCode() == NativeKeyEvent.VC_Z && keys.contains("Z")) {
			keys.remove("Z");
		} else if(arg0.getKeyCode() == NativeKeyEvent.VC_X && keys.contains("X")) {
			keys.remove("X");
		} else if(arg0.getKeyCode() == NativeKeyEvent.VC_M && keys.contains("M")) {
			keys.remove("M");
		} else if(arg0.getKeyCode() == NativeKeyEvent.VC_N && keys.contains("N")) {
			keys.remove("N");
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) { /* Unimplemented */ }
}
