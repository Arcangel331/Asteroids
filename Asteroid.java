/*
 * Aidan Crump
 * Homework 2
 * 
 * Purpose: To make an Asteroid object that extends the Polygon class
 */
import java.awt.Color;
import java.awt.Graphics;

public class Asteroid extends Polygon {

	public Asteroid(Point[] inShape, Point inPosition, double inRotation) {
		super(inShape, inPosition, inRotation);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void paint(Graphics brush, Color color) {
		Point[] pts = getPoints();
		int[] xpts = new int[pts.length];
		int[] ypts = new int[pts.length];
		int npts = pts.length;
		
		for (int i = 0; i < npts; i++) {
			xpts[i] = (int)pts[i].x;
			ypts[i] = (int)pts[i].y;
		}
		
		brush.setColor(color);
		brush.drawPolygon(xpts, ypts, npts);

	}

@Override
	 public void move() {
        position.x += Math.cos(Math.toRadians(rotation));
        position.y += Math.sin(Math.toRadians(rotation));
        if(position.x > Asteroids.SCREEN_WIDTH) {
            position.x -= Asteroids.SCREEN_WIDTH;
        } else if(position.x < 0) {
            position.x += Asteroids.SCREEN_WIDTH;
        }
        if(position.y > Asteroids.SCREEN_HEIGHT) {
            position.y -= Asteroids.SCREEN_HEIGHT;
        } else if(position.y < 0) {
            position.y += Asteroids.SCREEN_HEIGHT;
        }
    }
}

