package anthill.model.rl;

import java.util.ArrayList;
import java.util.List;

import net.davidrobles.rl.RLEnv;
import anthill.Ant;
import anthill.model.Location;
import anthill.model.WorldModel.LocationType;

public class RLAntEnv implements RLEnv<State, Action> {

	private Ant ant;

	public RLAntEnv(Ant ant) {
		this.ant = ant;
	}

	public State getCurrentState() {
		return new State(this.ant.position);
	}

	public List<Action> getPossibleActions(State state) {
		List<Location> neighbours = state.position.neighbours();
		List<Action> actions = new ArrayList<Action>();
		for (Location location : neighbours) {
			Action action = this.ant.position.actionTo(location);
			actions.add(action);
		}
		return actions;
	}

	public double performAction(Action Action) {
		return 0;
	}

	public void reset() {

	}

	public boolean isTerminal() {
		return this.ant.position.state == LocationType.FOOD;
	}

}
