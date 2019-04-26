
/*
 * Aidan Crump
 * Homework 2
 * 
 * Purpose: To use various classes and methods to create a fully functioning game of Asteroids
 * 
 * Original code by Dan Leyzberg and Art Simon
 */
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Asteroids extends Game {
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;

	private static final int SHIP_WIDTH = 40;
	private static final int SHIP_HEIGHT = 25;

	static int counter = 0;
	
	private static boolean collision = false;
	private static int collisionTime = 100;
	private static int lives = 5;
	private Color lastColor = Color.WHITE;
	
	public Star[] stars;


	private java.util.List<Asteroid> randomAsteroids = new ArrayList<Asteroid>();
		public ArrayList<Bullet> bulletRemovals = new ArrayList<Bullet>();
		public ArrayList<Asteroid> asteroidRemoval = new ArrayList<Asteroid>();


	private Ship ship;

	public Asteroids() {
		super("Asteroids!",SCREEN_WIDTH,SCREEN_HEIGHT);
		this.setFocusable(true);
		this.requestFocus();

		// ADDED BY ALEX:
		randomAsteroids = createRandomAsteroids(10,60,30);

		stars = createStars(200, 3);
		
		ship = createShip();
		this.addKeyListener(ship);

	}

	// ADDED BY ALEX: Create an array of random asteroids
	private ArrayList<Asteroid> createRandomAsteroids(int numberOfAsteroids, int maxAsteroidWidth,
			int minAsteroidWidth) {
		ArrayList<Asteroid> asteroids = new ArrayList<>(numberOfAsteroids);
		for(int i = 0; i < numberOfAsteroids; ++i) {
			// Create random asteroids by sampling points on a circle
			// Find the radius first.
			int radius = (int) (Math.random() * maxAsteroidWidth);
			if(radius < minAsteroidWidth) {
				radius += minAsteroidWidth;
			}
			// Find the circles angle
			double angle = (Math.random() * Math.PI * 1.0/2.0);
			if(angle < Math.PI * 1.0/5.0) {
				angle += Math.PI * 1.0/5.0;
			}
			// Sample and store points around that circle
			ArrayList<Point> asteroidSides = new ArrayList<Point>();
			double originalAngle = angle;
			while(angle < 2*Math.PI) {
				double x = Math.cos(angle) * radius;
				double y = Math.sin(angle) * radius;
				asteroidSides.add(new Point(x, y));
				angle += originalAngle;
			}
			// Set everything up to create the asteroid
			Point[] inSides = asteroidSides.toArray(new Point[asteroidSides.size()]);
			Point inPosition = new Point(Math.random() * SCREEN_WIDTH, Math.random() * SCREEN_HEIGHT);
			double inRotation = Math.random() * 360;
			asteroids.add(new Asteroid(inSides, inPosition, inRotation));
		}
		return asteroids;
	}

	// Create dimensions of Ship 
	private Ship createShip() {
		Point[] shipShape = {
				new Point(0, 0),
				new Point(SHIP_WIDTH/3.5, SHIP_HEIGHT/2),
				new Point(0, SHIP_HEIGHT),
				new Point(SHIP_WIDTH, SHIP_HEIGHT/2)
		};
		// Set ship at the middle of the screen
		Point startingPosition = new Point((width-SHIP_WIDTH)/2, (height-SHIP_HEIGHT)/2);
		int startingRotation = 0;
		return new Ship(shipShape, startingPosition, startingRotation);
	}
	
    // Create a certain number of stars with a given max radius
    public Star[] createStars(int numberOfStars, int maxRadius) {
        Star[] stars = new Star[numberOfStars];
        for(int i = 0; i < numberOfStars; ++i) {
            Point center = new Point(Math.random() * SCREEN_WIDTH, Math.random() * SCREEN_HEIGHT);
            int radius = (int) (Math.random() * maxRadius);
            if(radius < 1) {
                radius = 1;
            }
            stars[i] = new Star(center, radius);
        }
        return stars;
    }

	public void paint(Graphics brush) {
		// Creates game environment
		brush.setColor(Color.black);
		brush.fillRect(0,0,width,height);

		// Displays number of lives
		brush.setColor(Color.white);
		brush.drawString("Lives  " + lives, 10, 20);
		
		for (Asteroid asteroid : randomAsteroids) {
			asteroid.paint(brush,Color.white);
			asteroid.move();
			
			if(!collision) {
                collision = asteroid.collision(ship);
            }

		}

		/* If there is a collision: 
		 * Paint the ship a different color,
		 * track collision time,
		 * and decrement lives by 1
		 */
        if(collision) {
            ship.paint(brush, Color.red);
            collisionTime -= 1;
            if(collisionTime <= 0) {
            	lives -= 1;
                collision = false;
                collisionTime = 100;
            }
        } else {
            ship.paint(brush, Color.magenta);
        }
        
		ship.move();

		// Track bullets, and checks if bullets have hit an asteroid
        for(Bullet bullet : ship.getBullets()) {
            bullet.paint(brush, Color.red);
            bullet.move();
            for(Asteroid asteroid : randomAsteroids) {
            	if(asteroid.contains(bullet.getCenter())) {
            		asteroidRemoval.add(asteroid);
            		bulletRemovals.add(bullet);
            	}
            }
            // Check if bullet moved off screen
            if(bullet.outOfBounds()) {
                bulletRemovals.add(bullet);
            }
        }

        // Remove off screen bullets, or bullets that hit asteroids
        for(Bullet bullet : bulletRemovals) {
            ship.getBullets().remove(bullet);
        }
        bulletRemovals.clear();
        
        // Remove asteroids hit by bullets
        for(Asteroid asteroid : asteroidRemoval) {
        	randomAsteroids.remove(asteroid);
        }
        asteroidRemoval.clear();
		
        // Make stars twinkle
        if(lastColor.equals(Color.WHITE)) {
            lastColor = Color.YELLOW;
        }else if(lastColor.equals(Color.YELLOW)) {
            lastColor = Color.BLUE;
        }else if(lastColor.equals(Color.BLUE)) {
        	lastColor = Color.BLACK;
        }else if(lastColor.equals(Color.BLACK)) {
        	lastColor = Color.WHITE;
        }else{
        	lastColor = Color.WHITE;
        }
        
		// Create stars
        for(Star star : stars) {
            star.paint(brush, lastColor);
        }
        
        // Checks of player has won, or lost the game
        if(randomAsteroids.isEmpty() == true) {
        	brush.setColor(Color.black);
    		brush.fillRect(0,0,width,height);
    		brush.setColor(Color.white);
    		brush.drawString("YOU WON!!!",380,(SCREEN_HEIGHT/2));
    		on = false;
        }
        if(lives == 0) {
        	brush.setColor(Color.black);
    		brush.fillRect(0,0,width,height);
    		brush.setColor(Color.white);
    		brush.drawString("GAME OVER",380,(SCREEN_HEIGHT/2));
    		on = false;
        }
	}

	public static void main (String[] args) {
		Asteroids a = new Asteroids();
		
		a.repaint();
	}
 }