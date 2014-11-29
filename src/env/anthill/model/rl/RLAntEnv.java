//package anthill.model.rl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import net.davidrobles.rl.RLEnv;
//import anthill.Ant;
//import anthill.model.Location;
//import anthill.model.WorldModel.LocationType;
//
//public class RLAntEnv implements RLEnv<State, Action> {
//
//	public Ant ant;
//
//	public RLAntEnv(Ant ant) {
//		this.ant = ant;
//	}
//	
//	public void updateAnt(Ant ant){
//		this.ant = ant;
//	}
//
//	public State getCurrentState() {
//		return new State(this.ant.position);
//	}
//
//	public List<Action> getPossibleActions(State state) {
//		List<Action> actions = new ArrayList<Action>();
//		if (state.position.type != LocationType.FOOD) {
//			List<Location> neighbours = state.position.neighbours(ant.currentLevel);
//			for (Location location : neighbours) {
//				Action action = this.ant.position.actionTo(location);
//				actions.add(action);
//			}
//		}
//		return actions;
//	}
//
//	public double performAction(Action action) {
//		if(action.to.type == LocationType.FOOD)
//			return 100;
//		return action.to.pher * 10;
//	}
//
//	public void reset() {
//
//	}
//
//	public boolean isTerminal() {
//		return this.ant.position.type == LocationType.FOOD;
//	}
//
//}
