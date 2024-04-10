//package csc;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FishPanel extends JPanel {

	private ArrayList<Fish> freeFishes;
	private ArrayList<Fish> caughtFishes;
	Fisherman player = new Fisherman(300, 300, Color.BLACK);
	private static Timer countdownTimer;
	private Timer updateTimerLabelTimer;
	private JLabel timerLabel;
	private Clip catchSoundClip;
	Graphics2D g2d;
	
	public void movePlayer(int deltaX, int deltaY) {
		player.setX(player.getX() + deltaX);
		player.setY(player.getY() + deltaY);
		repaint();
	}
	
	public void catchFish() {
		Iterator<Fish> iterator = freeFishes.iterator();
		while (iterator.hasNext()) {
			Fish fish = iterator.next();
			if (isBoatOnFish(player.getX(), player.getY(), fish)) {
				caughtFishes.add(fish);
				iterator.remove();  // Use iterator to safely remove the clicked fish
				playCatchSound();
				repaint();
				continue;
			}
		}
		
	}
	

private void playCatchSound() {
    catchSoundClip.setFramePosition(0);
    catchSoundClip.start();
}
	
	
	class MyKeyEventDispatcher implements KeyEventDispatcher {
		
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				handleKeyPress(e.getKeyCode());
			}
			return false;
		}

		private void handleKeyPress(int keyCode) {
			switch (keyCode) {
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				movePlayer(-40, 0);
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				movePlayer(40, 0);
				break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				movePlayer(0, -40);
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				movePlayer(0, 40);
				break;
			case KeyEvent.VK_SPACE:
				catchFish();
				break;
			}
		}
	}	

	public FishPanel(ArrayList<Fish> freeFishes, ArrayList<Fish> caughtFishes) {
		this.freeFishes = freeFishes;
		this.caughtFishes = caughtFishes;

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new MyKeyEventDispatcher());
		setLayout(null);
		setFocusable(true);
		requestFocusInWindow();
		try {
		    AudioInputStream catchSoundInputStream = AudioSystem.getAudioInputStream(
		        new File(getClass().getResource("/New Recording 8.wav").toURI()));
		    catchSoundClip = AudioSystem.getClip();
		    catchSoundClip.open(catchSoundInputStream);

		    // Move this line after catchSoundClip initialization
		    Runtime.getRuntime().addShutdownHook(new Thread(() -> catchSoundClip.close()));
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException | URISyntaxException e) {
		    e.printStackTrace();
		}

		// Use Timer for animation
		Timer timer = new Timer(20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateFish();
				repaint();
			}
		});

		int countdownTime = 30 * 1000; // Convert seconds to milliseconds
		

		countdownTimer = new Timer(countdownTime, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				timer.stop(); // Stop the animation timer when the countdown is done
				displayScore();
			}
		});
		
		countdownTimer.setRepeats(false); // Execute only once
        // Start the countdown timer
        countdownTimer.start();

        // Start the animation timer
        timer.start();
	}

	public int getScore(ArrayList<Fish> caughtFishes) {
		this.caughtFishes = caughtFishes;
		int score = 0;

		for (int i = 0; i<caughtFishes.size(); i++) {
			score += caughtFishes.get(i).getPointValue();
		}
		return score;
	}

	public String getNames(ArrayList<Fish> caughtFishes) {
		this.caughtFishes = caughtFishes;
		String names = "";

		for (int i = 0; i<caughtFishes.size(); i++) {
			names = names + caughtFishes.get(i).getName() + ", ";
		}
		return names;
	}
	private void displayScore() {
	    String message = "<html>This game may be fin-ished, but you're the greatest catch of them all <3<br>"
                + "Your Score: " + getScore(caughtFishes) + "/600" + "<br>"
                + "You caught: " + getNames(caughtFishes) + "</html>";
	  
	    JLabel label = new JLabel(message);
	    label.setPreferredSize(new Dimension(500, 250)); // Set your desired size

	    JOptionPane.showMessageDialog(this, label);
	}
	//CHANGE TO PLAYER
	private boolean isBoatOnFish(int boatX, int boatY, Fish fish) {
		int fishX = fish.getX();
		int fishY = fish.getY();
		return boatX >= fishX - fish.getSize() && boatX <= fishX + fish.getSize() &&
				boatY >= fishY - fish.getSize() && boatY <= fishY + fish.getSize();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		// Draw visible fishes
		for (Fish fish : freeFishes) {
			int fishX = fish.getX();
			int fishY = fish.getY();

			 Ellipse2D.Double body = new Ellipse2D.Double(fishX, fishY, fish.getSize(), fish.getSize());
	            g2d.setColor(fish.getColor());
	            g2d.fill(body);
	            g2d.setColor(Color.BLACK);
	            g2d.draw(body);

	            // Draw the fish tail (triangle)
	            int[] xPoints = {fishX + fish.getSize() / 2, fishX + fish.getSize() / 2 - 10, fishX + fish.getSize() / 2 + 10};
	            int[] yPoints = {fishY + fish.getSize(), fishY + fish.getSize() + 10, fishY + fish.getSize() + 10};
	            Polygon tail = new Polygon(xPoints, yPoints, 3);
	            g2d.setColor(fish.getColor());
	            g2d.fill(tail);
	            g2d.setColor(Color.BLACK);
	            g2d.draw(tail);
		}
		
			//draw boat
		int playerX = player.getX();
		int playerY = player.getY();

		GeneralPath canoe = new GeneralPath();

		canoe.moveTo(playerX, playerY + 50); // Start at the bottom-left corner
		canoe.lineTo(playerX + 10, playerY);  // Draw left edge of canoe
		canoe.lineTo(playerX + 20, playerY);  // Draw left top edge of canoe
		canoe.lineTo(playerX + 30, playerY + 50); // Draw right top edge of canoe
		canoe.closePath(); // Close the shape

		g2d.setColor(player.getColor());
		g2d.fill(canoe);
		g2d.draw(canoe);

	
	}

	// Update fish position and check for collisions with the screen boundaries
	private void updateFish() {
		for (Fish fish : freeFishes) {
			int fishX = fish.getX();
			int fishY = fish.getY();
			int fishSpeedX = fish.getSpeedX();
			int fishSpeedY = fish.getSpeedY();

			fishX += fishSpeedX;
			fishY += fishSpeedY;

			// Check for collisions with the horizontal boundaries
			if (fishX < 0 || fishX + fish.getSize() > getWidth()) {
				fishSpeedX = -fishSpeedX; // Reverse direction
			}

			// Check for collisions with the vertical boundaries
			if (fishY < 0 || fishY + fish.getSize() > getHeight()) {
				fishSpeedY = -fishSpeedY; // Reverse direction
			}

			fish.setX(fishX);
			fish.setY(fishY);
			fish.setSpeedX(fishSpeedX);
			fish.setSpeedY(fishSpeedY);
		}
	}

	public static void main(String[] args) {
		// Create an ArrayList to hold visible fishes
		ArrayList<Fish> visibleFishes = new ArrayList<>();
		visibleFishes.add(new Fish(75, "Bella Thorne", Color.PINK, 2, 10));
		visibleFishes.add(new Fish (60, "Tom hanks", Color.RED, 3, 20));
		visibleFishes.add(new Fish(45, "Camilla Cabello", Color.ORANGE, 4, 30));
		visibleFishes.add(new Fish(30, "Paris Hitlon", Color.MAGENTA, 5, 40));
		visibleFishes.add(new Fish(15, "Emma Roberts", Color.GREEN, 6, 50));
		visibleFishes.add(new Fish(75, "Carbi B", Color.PINK, 2, 10));
		visibleFishes.add(new Fish (60, "T.I.", Color.RED, 3, 20));
		visibleFishes.add(new Fish(45, "Chris Brown", Color.ORANGE, 4, 30));
		visibleFishes.add(new Fish(30, "O.J Simpson", Color.MAGENTA, 5, 40));
		visibleFishes.add(new Fish(15, "Adam Purtee", Color.GREEN, 6, 50));
		visibleFishes.add(new Fish(75, "Brad Pitt", Color.PINK, 2, 10));
		visibleFishes.add(new Fish (60, "Tarantino", Color.RED, 3, 20));
		visibleFishes.add(new Fish(45, "Christian Walker", Color.ORANGE, 4, 30));
		visibleFishes.add(new Fish(30, "Pete Davidson", Color.MAGENTA, 5, 40));
		visibleFishes.add(new Fish(15, "Bobby Kardashian", Color.GREEN, 6, 50));
		visibleFishes.add(new Fish(75, "James Charles", Color.PINK, 2, 10));
		visibleFishes.add(new Fish (60, "Kanye", Color.RED, 3, 20));
		visibleFishes.add(new Fish(45, "Charlie Sheen", Color.ORANGE, 4, 30));
		visibleFishes.add(new Fish(30, "Jefree Starr", Color.MAGENTA, 5, 40));
		visibleFishes.add(new Fish(15, "Shane Dawson", Color.GREEN, 6, 50));

		// Create an ArrayList to hold removed fishes
		ArrayList<Fish> removedFishes = new ArrayList<>();

		// Create a JFrame
		JFrame frame = new JFrame("Course Project");
		frame.setSize(1000, 1400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create a FishPanel and add it to the JFrame
		FishPanel fishPanel = new FishPanel(visibleFishes, removedFishes);
		frame.add(fishPanel);
		fishPanel.setBackground(Color.BLUE);

		 // Make the JFrame visible
        frame.setVisible(true);
        

	}
}