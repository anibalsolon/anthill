package anthill;

import jacamo.infra.JaCaMoAgArch;
import jason.architecture.AgArch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anthill.model.Level;
import anthill.model.Location;
import c4jason.CAgentArch;

public class Ant extends JaCaMoAgArch {

	public Level currentLevel;
	public Location position;

//	public RLPolicy<State, Action> policy;
//	public RLAntEnv zeroEnv;
//	public QLearning<State, Action> learn;

	@Override
	public void init() throws Exception {
		super.init();
	}

	public CAgentArch getCartagoArch() {
		return super.getCartagoArch();
	}

	public static Ant getAntArch(AgArch agarch) {
		AgArch arch = agarch.getFirstAgArch();
		while (arch != null) {
			if (arch instanceof Ant) {
				return (Ant) arch;
			}
			arch = arch.getNextAgArch();
		}
		return null;
	}

	// public interface HeuristicMDP {
	// public Location findNextStep(List<Location> neighbours);
	// }


	public Location calcNextMove() {
//		Action act = policy.getAction(zeroEnv, new QFunction() {
//
//			public double getValue(QPair qPair) {
//				return this.getValue(qPair.getState(), qPair.getAction());
//			}
//
//			public double getValue(State state, Action action) {
//				if (action.to.type == LocationType.FOOD)
//					return 100;
//				return action.to.pher * 10;
//			}
//			
//		});		
//		return act.to;
		return null;
	}

	public interface StopCondition {
		public boolean isGoal(Location loc);
	}
	
	public List<Location> calcShortestPath(Location loc, StopCondition cond) {

		Map<Location, Location> path = new HashMap<Location, Location>();
		Map<Location, Integer> distance = new HashMap<Location, Integer>();
		List<Location> closedList = new ArrayList<Location>();
		List<Location> openList = new ArrayList<Location>();
		List<Location> finalPath = new ArrayList<Location>();

		openList.add(loc);
		distance.put(loc, 0);

		while (openList.size() != 0) {

			Location current = openList.get(0);

			if (cond.isGoal(current)) {
				while (current != loc) {
					finalPath.add(current);
					current = path.get(current);
				}
				Collections.reverse(finalPath);
				break;
			}

			openList.remove(current);
			closedList.add(current);

			for (Location neighbor : current.neighbours()) {

				boolean neighborIsBetter;

				if (closedList.contains(neighbor))
					continue;

				int neighborDistanceFromStart = distance.get(current) + 1;

				if (!openList.contains(neighbor)) {
					openList.add(neighbor);
					neighborIsBetter = true;
				} else if (neighborDistanceFromStart < distance.get(current)) {
					neighborIsBetter = true;
				} else {
					neighborIsBetter = false;
				}

				if (neighborIsBetter) {
					path.put(neighbor, current);
					distance.put(neighbor, neighborDistanceFromStart);
				}

			}

		}

		return finalPath;
	}

}
