package anthill.model.rl;


public class Action {

	public static enum Direction {
		UP, DOWN, LEFT, RIGHT;
	}

	public Direction direction;

	public Action(Direction direction) {
		this.direction = direction;
	}

}