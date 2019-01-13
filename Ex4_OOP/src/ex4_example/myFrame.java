package ex4_example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import Geom.Point3D;
import Robot.*;
import Coords.*;

public class myFrame extends JFrame implements MouseListener, Runnable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage backroundImage;
	private BufferedImage pacmanImage;
	private BufferedImage fruitImage;
	private BufferedImage ghostImage;
	private BufferedImage playerImage;

	private Packman player;
	private Map map;
	private Play play = null;
	private ArrayList<String> board_data;
	private int clickX = -1;
	private int clickY = -1;
	private int button = 0;
	private boolean gameLoaded = false;
	private boolean playerLoaded = true;
	private boolean manualPlay = true;
	private double[] a = { 0, 0, 0 };

	private static MenuBar menubar;
	private Menu menu;
	private Menu menu2;
	private MenuItem itemSave;
	private MenuItem itemLoad;
	private MenuItem itemQuit;
	private MenuItem itemAPlay;
	private MenuItem itemMPlay;

	public myFrame() {
		myGUI();
		this.addMouseListener(this);
	}

	public void myGUI() {
		menubar = new MenuBar();
		String map_name = "C:\\\\Users\\\\lenovo\\\\workspace\\\\Ex4_OOP\\\\data\\\\Ariel1.png";

		double lat = 32.103813D;
		double lon = 35.207357D;
		double alt = 0D;
		double dx = 955.5D;
		double dy = 421.0D;
		LatLonAlt cen = new LatLonAlt(lat, lon, alt);
		map = new Map(cen, dx, dy, map_name);

		menu = new Menu("Game");

		menu2 = new Menu("Play");

		itemSave = new MenuItem("Save Game");
		itemLoad = new MenuItem("Load Game");
		itemQuit = new MenuItem("Quit");
		itemAPlay = new MenuItem("Auto Play");
		itemMPlay = new MenuItem("Manual Play");

//		itemSave.addActionListener(new myActionListener());
//		itemQuit.addActionListener(new myActionListener());

		itemLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter("CSV file", "csv"));
				chooser.showOpenDialog(null);
				File file = chooser.getSelectedFile();
				try {
					Game game = new Game(file.getPath());
					play = new Play(game);
					gameLoaded = true;
					Scanner input = new Scanner(file);
					input.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				repaint();
			}
		});

		myFrame temp = this;
		itemAPlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(temp);
				t.start();
			}
		});
		itemMPlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(temp);
				t.start();
				manualPlay = false;
			}
		});

		menu.add(itemSave);
		menu.add(itemLoad);
		menu.add(itemQuit);
		
		menu2.add(itemAPlay);
		menu2.add(itemMPlay);

		try {
			backroundImage = ImageIO.read(new File("C:\\Users\\lenovo\\workspace\\Ex4_OOP\\data\\Ariel1.PNG"));
			pacmanImage = ImageIO.read(new File("C:\\\\Users\\\\lenovo\\\\workspace\\\\Ex4_OOP\\\\data\\\\pacman.PNG"));
			fruitImage = ImageIO
					.read(new File("C:\\\\Users\\\\lenovo\\\\workspace\\\\Ex4_OOP\\\\data\\\\strawberry.PNG"));
			ghostImage = ImageIO.read(new File("C:\\\\Users\\\\lenovo\\\\workspace\\\\Ex4_OOP\\\\data\\\\ghost.PNG"));
			playerImage = ImageIO
					.read(new File("C:\\\\Users\\\\lenovo\\\\workspace\\\\Ex4_OOP\\\\data\\\\pac.gif_c200"));
		} catch (IOException E) {
			E.printStackTrace();
		}

		setMenuBar(menubar);
		menubar.add(menu);
		menubar.add(menu2);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setSize(1433, 642);
		setResizable(false); // change
		setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(backroundImage, 0, 0, this.getWidth(), this.getHeight(), this);

		if (clickX != -1 && clickY != -1) {
			if (button == 1) {
				playerLoaded = false;
				g.drawImage(playerImage, clickX, clickY, 30, 30, this);
				Point3D p = new Point3D(clickX, clickY);
				Point3D p2 = map.image2frame(p, this.getWidth(), this.getHeight());
				LatLonAlt p3 = map.frame2world(p2);				
				player = new Packman(p3, 2);
//				System.out.println(player.getLocation());
				button = -1;
				System.out.println();
				play.setInitLocation(player.getLocation().ix(), player.getLocation().iy());
			}
		}

		if (gameLoaded) {
			board_data = play.getBoard();
			Iterator<String> it = board_data.iterator();
			while (it.hasNext()) {
				String line = it.next();
				if (line.startsWith("P")) {
					Packman pacman = new Packman(line);
					LatLonAlt test = pacman.getLocation();
					Point3D test2 = map.world2frame(test);
					g.drawImage(pacmanImage, test2.ix(), test2.iy(), 30, 30, this);
				} else if (line.startsWith("G")) {
					Packman ghost = new Packman(line);
					LatLonAlt test = ghost.getLocation();
					Point3D test2 = map.world2frame(test);
					g.drawImage(ghostImage, test2.ix(), test2.iy(), 30, 30, this);
				} else if (line.startsWith("F")) {
					Fruit fruit = new Fruit(line);
					LatLonAlt test = fruit.getLocation();
					Point3D test2 = map.world2frame(test);
					g.drawImage(fruitImage, test2.ix(), test2.iy(), 30, 30, this);
				} else if (line.startsWith("B")) {
					GeoBox box = new GeoBox(line);
					LatLonAlt minTemp = box.getMin();
					LatLonAlt maxTemp = box.getMax();
					Point3D test1 = map.world2frame(minTemp);
					Point3D test2 = map.world2frame(maxTemp);

					LatLonAlt min = new LatLonAlt(test1.x(), test1.y(), test1.z());
					LatLonAlt max = new LatLonAlt(test2.x(), test2.y(), test2.z());

					GeoBox box2 = new GeoBox(min, max);

					int width = (int) Math.abs(box2.getMin().lat() - box2.getMax().lat());
					int height = (int) Math.abs(box2.getMax().lon() - box2.getMin().lon());

					int x2 = (int) (box2.getMin().lat());
					int y2 = (int) (box2.getMin().lon());
					g.drawRect(x2, y2, width, height);
					g.fillRect(x2, y2, width, height);
				} else if (line.startsWith("M") && !playerLoaded) { // will be false after nmouse click.
//					player = new Packman(line);
//					System.out.println(line);
//					System.out.println(player.getLocation());
					Point3D point = map.world2frame(player.getLocation());
//					System.out.println(point);
					g.drawImage(playerImage, point.ix() - 15, point.iy() - 65, 30, 30, this);
				}
			}
		}
	}

//	public void movePlayer() {
////		Point3D p = new Point3D(clickX,clickY);
////		Point3D p2 = map.image2frame(p, frame.getWidth(), frame.getHeight());
////		LatLonAlt p3 = map.frame2world(p2);
////		double[] a = player.getLocation().azimuth_elevation_dist(p3);
////		double angle= a[0];
////		play.rotate(angle);
////		frame.repaint();
//		new Thread() {
//			public void run() {
//				Point3D p = new Point3D(clickX, clickY);
//				Point3D p2 = map.image2frame(p, frame.getWidth(), frame.getHeight());
//				LatLonAlt p3 = map.frame2world(p2);
//				double[] a = player.getLocation().azimuth_elevation_dist(p3);
//				double angle = a[0];
//				play.rotate(angle);
//				frame.repaint();
//				repaint();
//				while (a[1] >= 1) {
//					System.out.println("distance is: " + a[1]);
//					System.out.println("in loop: " + player.getLocation());
//					a = player.getLocation().azimuth_elevation_dist(p3);
//					play.rotate(a[0]);
//					frame.repaint();
//					System.out.println("distance after moving: " + a[1]);
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					repaint();
//				}
//			}
//		}.run();
//	}

//	private class myActionListener implements ActionListener {
//
//		@Override
//		public void actionPerformed(ActionEvent arg0) {
//			if (arg0.getSource() == itemLoad) {
//				JFileChooser chooser = new JFileChooser();
//				chooser.setFileFilter(new FileNameExtensionFilter("CSV file", "csv"));
//				chooser.showOpenDialog(null);
//				File file = chooser.getSelectedFile();
//				try {
//					Play play = new Play(file.getPath());
//					gameLoaded = true;
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//				repaint();
//				play.setIDs(206008153, 206008154, 206008155);
//				gameLoaded = true;
//			}
//			if (arg0.getSource() == itemQuit)
//				System.exit(0);
//		}
//
//	}

//	private class myAdapter extends MouseAdapter {
//		public void mouseClicked(MouseEvent e) {
//			// super.mouseClicked(e);
//			System.out.println("mouse Clicked");
//			System.out.println("(" + e.getX() + "," + e.getY() + ")");
//			if (gameLoaded && e.getClickCount() == 1) {
//				Point3D point = new Point3D((double) e.getX(), (double) e.getY(), 0);
//				Point3D point2 = map.image2frame(point, frame.getWidth(), frame.getHeight());
//				LatLonAlt point3 = map.frame2world(point2);
//				player = new Packman(point3, 2);
//				play.setInitLocation(point3.ix(), point3.iy());
////				System.out.println("click get player :" + game.getPlayer());
//				revalidate();
//				repaint();
//				play.start();
////				gameStarted = play.isRuning();
//				gameLoaded = false;
//			}
//
//		}
//
//		public void mousePressed(MouseEvent e) {
//			if (play.isRuning()) {
//				clickX = e.getX();
//				clickY = e.getY();
//
//				System.out.println("player moved.");
//				System.out.println("located in: " + player.getLocation());
//			}
//		}
//
//	}

	public static void main(String[] a) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new myFrame();
			}
		});
	}

	@Override
	public void run() {
		if (!manualPlay) {
			play.setIDs(206008153,205946221,315310805);
			play.start();
			while (play.isRuning()) {
				System.out.println("run: "+a[0]);
				System.out.println("run: "+a[1]);
				System.out.println("run: "+a[2]);
				play.rotate(a[0]);
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					// TODO: handle exception
				}
				repaint();
			}
			System.out.println("**** Done Game (user stop) ****");
			String info = play.getStatistics();
			System.out.println(info);
			System.out.println();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (playerLoaded) {
			button = e.getButton();
			System.out.println(button);
			clickX = e.getX();
			clickY = e.getY();
//			Point3D p = new Point3D(clickX, clickY);
//			Point3D p2 = map.image2frame(p, this.getWidth(), this.getHeight());
//			LatLonAlt p3 = map.frame2world(p2);

//			player.setLocation(p3);
//			System.out.println(player.getLocation());
			repaint(clickX,clickY,30,30);
			
		} else {
			clickX = e.getX();
			clickY = e.getY();
			Point3D p = new Point3D(clickX, clickY);
			Point3D p2 = map.image2frame(p, this.getWidth(), this.getHeight());
			LatLonAlt p3 = map.frame2world(p2);
			System.out.println("p: "+p+",p2: "+p2+",p3: "+p3);
			a = player.getLocation().azimuth_elevation_dist(p3);
			System.out.println(player.getLocation());
			System.out.println("mouseclick: "+a[0]);
			System.out.println("mouseclick: "+a[1]);
			System.out.println("mouseclick: "+a[2]);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}