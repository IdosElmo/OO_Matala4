package ex4_example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import GIS.*;
import Geom.Point3D;
import Robot.*;
import Coords.*;

public class myFrame extends JPanel {

	private Packman player;

	private ArrayList<Path> Paths;
	private ArrayList<Packman> pacmans;
	private ArrayList<Fruit> fruits;
	private ArrayList<Packman> ghosts;
	private ArrayList<GeoBox> boxes;
	private Game game;
//
	private BufferedImage backroundImage;
	private BufferedImage pacmanImage;
	private BufferedImage fruitImage;
	private BufferedImage ghostImage;
	private BufferedImage playerImage;

	private Map map;
	private Play play;
	private ArrayList<String> board_data = new ArrayList<String>();

	private boolean gameLoaded = false;
	private boolean gameStarted = false;
	private boolean buttonPacman = false;
	private boolean buttonFruit = false;
	private JFrame frame;
	private static JMenuBar menubar;
	private JMenu menu;
	private JMenu menu2;
	private JMenu menu3;
	private JMenuItem itemPacman;
	private JMenuItem itemFruit;
	private JMenuItem itemSave;
	private JMenuItem itemLoad;
	private JMenuItem itemQuit;
	private JMenuItem itemAPlay;
	private JMenuItem itemMPlay;
	private JMenuItem itemClear;

	public myFrame() {
		myGUI();
	}

	public void myGUI() {
		frame = new JFrame("GIS Pacman");
		menubar = new JMenuBar();
		Paths = new ArrayList<>();
		pacmans = new ArrayList<>();
		fruits = new ArrayList<>();
		ghosts = new ArrayList<>();
		boxes = new ArrayList<>();
		game = new Game();
		play = new Play(game);

		String map_name = "C:\\\\Users\\\\lenovo\\\\workspace\\\\Ex4_OOP\\\\data\\\\Ariel1.png";

		double lat = 32.103813D;
		double lon = 35.207357D;
		double alt = 0D;
		double dx = 955.5D;
		double dy = 421.0D;
		LatLonAlt cen = new LatLonAlt(lat, lon, alt);
		map = new Map(cen, dx, dy, map_name);
		// map = new Map();
//		play.setIDs(1,2,3);

//		player = new Packman(new LatLonAlt(1, 0, 0), 1);

		menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_A);

		menu2 = new JMenu("Operations");
		menu2.setMnemonic(KeyEvent.VK_O);

		menu3 = new JMenu("Play");
		menu3.setMnemonic(KeyEvent.VK_P);

		itemPacman = new JMenuItem("Add Packman");
		itemFruit = new JMenuItem("Add Fruit");
		itemSave = new JMenuItem("Save Game");
		itemLoad = new JMenuItem("Load Game");
		itemQuit = new JMenuItem("Quit");
		itemQuit.setMnemonic(KeyEvent.VK_Q);
		itemAPlay = new JMenuItem("Auto Play");
		itemMPlay = new JMenuItem("Manual Play");
		itemClear = new JMenuItem("Clear");

		itemPacman.addActionListener(new myActionListener());
		itemFruit.addActionListener(new myActionListener());
		itemSave.addActionListener(new myActionListener());
		itemLoad.addActionListener(new myActionListener());
		itemQuit.addActionListener(new myActionListener());
		itemAPlay.addActionListener(new myActionListener());
		itemClear.addActionListener(new myActionListener());

		menu.add(itemSave);
		menu.add(itemLoad);
		menu.add(itemQuit);
		menu2.add(itemPacman);
		menu2.add(itemFruit);
		menu2.add(itemClear);
		menu3.add(itemAPlay);
		menu3.add(itemMPlay);

		try {
			backroundImage = ImageIO.read(new File("C:\\Users\\lenovo\\workspace\\Ex4_OOP\\data\\Ariel1.PNG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			pacmanImage = ImageIO.read(new File("C:\\\\Users\\\\lenovo\\\\workspace\\\\Ex4_OOP\\\\data\\\\pacman.PNG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fruitImage = ImageIO.read(new File("C:\\\\Users\\\\lenovo\\\\workspace\\\\Ex4_OOP\\\\data\\\\strawberry.PNG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			ghostImage = ImageIO.read(new File("C:\\\\Users\\\\lenovo\\\\workspace\\\\Ex4_OOP\\\\data\\\\ghost.PNG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			playerImage = ImageIO.read(new File("C:\\\\Users\\\\lenovo\\\\workspace\\\\Ex4_OOP\\\\data\\\\pac.gif_c200"));
		} catch (IOException E) {
			E.printStackTrace();
		}

		frame.setJMenuBar(menubar);

		menubar.add(menu);
		menubar.add(menu2);
		menubar.add(menu3);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.addMouseListener(new myAdapter());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// +
		// setPreferredSize(size);
		frame.getContentPane().add(this);
		// addMouseListener(new myASize(size);
		frame.pack();
		frame.setSize(1433, 642);
		frame.setResizable(false); // change
		frame.setVisible(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(backroundImage, 0, 0, frame.getWidth(), frame.getHeight(), frame);
		Graphics2D graphic = (Graphics2D) g;

//		board_data = play.getBoard();
//		for (int i = 0; i < board_data.size(); i++) {
//			if (board_data.get(i).startsWith("M")) {
//				player = new Packman(board_data.get(i));
//				g.drawImage(playerImage, (int) player.getLocation().x(), (int) player.getLocation().y(), 30, 30, frame);
//			}
//			if (board_data.get(i).startsWith("P")) {
//				Packman pac = new Packman(board_data.get(i));
//				g.drawImage(pacmanImage, (int) pac.getLocation().x(), (int) pac.getLocation().y(), 30, 30, frame);
//			}
//			if (board_data.get(i).startsWith("G")) {
//				Packman ghost = new Packman(board_data.get(i));
//				g.drawImage(ghostImage, (int) ghost.getLocation().x(), (int) ghost.getLocation().y(), 30, 30, frame);;
//			}
//			if (board_data.get(i).startsWith("F")) {
//				Fruit fruit = new Fruit(board_data.get(i));
//				g.drawImage(fruitImage, (int) fruit.getLocation().x(), (int) fruit.getLocation().y(), 30, 30, frame);
//			}
//		}
//		for (GeoBox box : boxes) {
//		int width = (int) Math.abs(box.getMin().lat() - box.getMax().lat());
//		int height = (int) Math.abs(box.getMax().lon() - box.getMin().lon());
//		
//		int x2 = (int) (box.getMin().lat());
//		int y2 = (int) (box.getMin().lon());
//		g.drawRect(x2, y2, width, height);
//		g.fillRect(x2, y2, width, height);
//	}
		
		for (Packman pac : pacmans) {
			LatLonAlt test = pac.getLocation();
			Point3D test2 = map.world2frame(test);
			g.drawImage(pacmanImage, test2.ix(), test2.iy(), 30, 30, frame);

		}

		for (Packman ghost : ghosts) {
			LatLonAlt test = ghost.getLocation();
			Point3D test2 = map.world2frame(test);
			g.drawImage(ghostImage, test2.ix(),test2.iy(), 30, 30, frame);
		}

		for (Fruit fruit : fruits) {
			LatLonAlt test = fruit.getLocation();
			Point3D test2 = map.world2frame(test);
			g.drawImage(fruitImage, test2.ix(),  test2.iy(), 30, 30, frame);
		}

		for (GeoBox box : boxes) {
			
			LatLonAlt minTemp = box.getMin();
			LatLonAlt maxTemp = box.getMax();
			Point3D test1 = map.world2frame(minTemp);
			Point3D test2 = map.world2frame(maxTemp);
			
			LatLonAlt min = new LatLonAlt(test1.x(),test1.y(),test1.z());
			LatLonAlt max = new LatLonAlt(test2.x(),test2.y(),test2.z());
			
			GeoBox box2 = new GeoBox(min,max);
			
			int width = (int) Math.abs(box2.getMin().lat() - box2.getMax().lat());
			int height = (int) Math.abs(box2.getMax().lon() - box2.getMin().lon());
			
			int x2 = (int) (box2.getMin().lat());
			int y2 = (int) (box2.getMin().lon());
			g.drawRect(x2, y2, width, height);
			g.fillRect(x2, y2, width, height);
		}

		if (gameStarted) { //will be false after nmouse click.
			Packman p = new Packman(game.getPlayer());
			Point3D point = map.world2frame(p.getLocation());
			g.drawImage(playerImage, point.ix() - 15, point.iy() - 65, 30, 30, frame);
		}
	}

	
	public boolean isPacman() {
		return buttonPacman;
	}

	public boolean isFruit() {
		return buttonFruit;
	}

	private class myActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == itemPacman) {
				System.out.println("pacman clicked");
				buttonPacman = true;
			}
			if (arg0.getSource() == itemFruit) {
				System.out.println("fruit clicked");
				// fIndex = Integer.parseInt(JOptionPane.showInputDialog("Enter
				// Number of Fruits:"));
				buttonFruit = true;
			}
			if (arg0.getSource() == itemSave) {
				String pathname = JOptionPane.showInputDialog("Enter save location:");
				game = new Game(pathname);
			}
			if (arg0.getSource() == itemLoad) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter("CSV file", "csv"));
				chooser.showOpenDialog(null);
				File file = chooser.getSelectedFile();
				game = new Game(file.getPath());
				for (int i = 0; i < game.getGame().size(); i++) {
					if (game.getGame().get(i).startsWith("M")) {
						player = new Packman(game.getGame().get(i));
						game.setPlayer(player);
					}
					if (game.getGame().get(i).startsWith("P")) {
						Packman p = new Packman(game.getGame().get(i));
//						LatLonAlt test = p.getLocation();
//						Point3D test2 = map.world2frame(test);
//						p.setLocation(new LatLonAlt(test2.x(), test2.y(), test2.z()));
						pacmans.add(p);
					}
					if (game.getGame().get(i).startsWith("G")) {
						Packman ghost = new Packman(game.getGame().get(i));
						ghosts.add(ghost);
					}
					if (game.getGame().get(i).startsWith("F")) {
						Fruit fruit = new Fruit(game.getGame().get(i));
//						LatLonAlt test = fruit.getLocation();
//						Point3D test2 = map.world2frame(test);
//						Fruit fruit2 = new Fruit(new LatLonAlt(test2.x(), test2.y(),test.z() ));
						fruits.add(fruit);
					}
					if (game.getGame().get(i).startsWith("B")) {
						GeoBox box = new GeoBox(game.getGame().get(i));
//				
//						LatLonAlt minTemp = box.getMin();
//						LatLonAlt maxTemp = box.getMax();
//						Point3D test1 = map.world2frame(minTemp);
//						Point3D test2 = map.world2frame(maxTemp);
//						
//						LatLonAlt min = new LatLonAlt(test1.x(),test1.y(),test1.z());
//						LatLonAlt max = new LatLonAlt(test2.x(),test2.y(),test2.z());
//						
//						GeoBox box2 = new GeoBox(min,max);
						boxes.add(box);
					}
				}
				repaint();
				play = new Play(game);
				
				ArrayList<String> board_data = play.getBoard();
				for(int i=0;i<board_data.size();i++) {
					System.out.println(board_data.get(i));
				}
				
				play.setIDs(206008153, 206008154, 206008155);
				gameLoaded = true;
			}
			if (arg0.getSource() == itemClear) {
				clearScreen();
			}
			if (arg0.getSource() == itemQuit)
				System.exit(0);
		}



		public void clearScreen() {
			game.getGame().clear();
			pacmans.clear();
			fruits.clear();
			ghosts.clear();
			boxes.clear();
			revalidate();
			repaint();
		}
	}
	
	private class myAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			// super.mouseClicked(e);
			System.out.println("mouse Clicked");
			System.out.println("(" + e.getX() + "," + e.getY() + ")");
//			play.setInitLocation(e.getX(), e.getY());
			if (gameLoaded && e.getClickCount() == 1) {
				Point3D point = new Point3D((double) e.getX(), (double) e.getY(), 0);
				Point3D point2 = map.image2frame(point, frame.getWidth(), frame.getHeight());
				LatLonAlt point3 = map.frame2world(point2);
				player = new Packman(point3, 2);
				game.setPlayer(player);
				play.setInitLocation(point3.ix(), point3.iy());
//				System.out.println("click get player :" + game.getPlayer());
				revalidate();
				repaint();
				play.start();
				gameStarted = play.isRuning();
				gameLoaded = false;
			}
			if (gameStarted) {
				int x = e.getX();
				int y = e.getY();
				
				Point3D p = new Point3D(x,y);
				Point3D p2 = map.image2frame(p, frame.getWidth(), frame.getHeight());
				LatLonAlt p3 = map.frame2world(p2);
				double[] a = player.getLocation().azimuth_elevation_dist(p3);
				double angle= a[0];
				play.rotate(angle);
				repaint();
				
				while(a[1] >= 1) {
					System.out.println("distance is: " + a[1]);
					System.out.println("in loop: " + player.getLocation());
					a = player.getLocation().azimuth_elevation_dist(p3);
					play.rotate(a[0]);
//					repaint();
					System.out.println("distance after moving: " + a[1]);
					revalidate();
					repaint();
					try {
						Thread.sleep(200);
						repaint();
						frame.repaint();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				System.out.println("player moved.");
				System.out.println("located in: " + player.getLocation());
				
//				double angle = Math.atan(Math.abs(y2 - y) / Math.abs(x2-x));
//				System.out.println(game.getPlayer().getLocation());
//				play.rotate(angle);
//				System.out.println(game.getPlayer().getLocation());
//				repaint();
			}
		}
	}

	public static void main(String[] a) {
		new myFrame();

	}
}