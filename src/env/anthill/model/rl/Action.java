package anthill.model.rl;

import anthill.model.Location;

public class Action {

	public static enum Direction {
		UP, DOWN, LEFT, RIGHT;
	}

	public Direction direction;
	public Location from;
	public Location to;

	public Action(Direction direction) {
		this.direction = direction;
	}

	public Action(Direction direction, Location from, Location to) {
		this.direction = direction;
		this.from = from;
		this.to = to;
	}
	
	public String toString(){
		return "Action: from " + from + " to " + to;
	}

}