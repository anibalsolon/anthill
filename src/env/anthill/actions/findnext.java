package anthill.actions;

import static anthill.Ant.getAntArch;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import anthill.Ant;
import anthill.Ant.HeuristicMDP;
import anthill.model.Location;
import anthill.util.Random;

public class findnext extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		Ant ant = getAntArch(ts.getUserAgArch());

		Literal location = (Literal) Literal.parseLiteral("location(_, _, _)");
		Iterator<Literal> relB = ts.getAg().getBB().getCandidateBeliefs(location, un);
		if (relB == null || !relB.hasNext()) {
			return false;
		}

		Location curr = Location.parseLiteral(relB.next());

		Map<Integer, Location> visited = new HashMap<Integer, Location>();

		Literal pathFind = (Literal) Literal.parseLiteral("path(_, _)");
		Iterator<Literal> pathsIt = ts.getAg().getBB().getCandidateBeliefs(pathFind, un);
		if (pathsIt != null) {
			while (pathsIt.hasNext()) {
				Literal path = (Literal) pathsIt.next();
				int index = (int) ((NumberTermImpl) path.getTerm(0)).solve();
				Literal loc = (Literal) path.getTerm(1);
				Location vis = Location.parseLiteral((Literal) loc);
				visited.put(index, vis);
			}
		}

		List<Location> visitedList = new ArrayList<Location>();
		for (int i = 0; i < visited.size(); i++) {
			visitedList.add(i, visited.get(i));
		}

		HeuristicMDP heuristic = heuristicForNext(visitedList);
		Location next = ant.calcNextMove(ant.currentLevel.getLoc(curr), heuristic);

		return un.unifies(args[0], next.asLiteral());
	}

	private HeuristicMDP heuristicForNext(final List<Location> visited) {

		final LocationVisit visitCount = new LocationVisit();
		for (Location location : visited)
			visitCount.inc(location);

		return new HeuristicMDP() {
			public Location findNextStep(List<Location> neighbours) {
				Iterator<Location> it = neighbours.iterator();
				Location goodOne = it.next();
				goodOne.visits = visitCount.get(goodOne);

				while (it.hasNext()) {
					Location location = (Location) it.next();
					location.visits = visitCount.get(location);

					float rate = location.chanceRate();
					float goodRate = goodOne.chanceRate();

					if (rate == goodRate)
						goodOne = new Random().getItem(new Location[] { goodOne, location });

					if (rate > goodRate)
						goodOne = location;
				}
				return goodOne;
			}
		};
	}

	private class LocationVisit extends HashMap<Location, Integer> {

		private static final long serialVersionUID = 1L;

		public Integer get(Location key) {
			Integer curr = super.get(key);
			if (curr == null)
				return 0;
			return curr;
		}

		public void inc(Location key) {
			Integer curr = get(key);
			super.put(key, curr + 1);
			key.visits = curr + 1;
		}

	}

}