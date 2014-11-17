package anthill.model;

import java.awt.Point;

public class Level {

	public int level;
	public int width;
	public int height;
	public int offsetx;
	public int offsety;
	public Location[][] model;

	public Level(int level, int width, int height, int offsetx, int offsety) {
		this.level = level;
		this.width = width;
		this.height = height;
		this.offsetx = offsetx;
		this.offsety = offsety;
		this.model = new Location[width][height];
	}

	public Level(int level, int width, int height) {
		this.level = level;
		this.width = width;
		this.height = height;
		this.offsetx = 0;
		this.offsety = 0;
		this.model = new Location[width][height];
	}

	public Level(int level) {
		this.level = level;
	}

	public Point getAbsolutePoint(Point relative) {
		return new Point(relative.y + this.offsetx, relative.y + this.offsety);
	}

	public Location getLoc(Location location) {
		return this.model[location.x][location.y];
	}

	public Location getAt(Location location) {
		if (location.x - offsetx > this.model.length || location.y - offsety > this.model[location.x - offsetx].length) {
			return null;
		}
		return this.model[location.x - offsetx][location.y - offsety];
	}

}
