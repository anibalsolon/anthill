package anthill.actions;

import static anthill.Ant.getAntArch;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.StringTermImpl;
import jason.asSyntax.Term;

import java.util.List;

import anthill.Ant;
import anthill.Ant.StopCondition;
import anthill.model.Location;
import anthill.model.WorldModel.LocationType;

public class findclosest extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {

		Ant ant = getAntArch(ts.getUserAgArch());		
		Location current = ant.position;

		StopCondition heuristic = null;
		if (args[0].isLiteral()) {
			Literal literalTo = (Literal) args[0];
			int levelTo = (int) ((NumberTermImpl) literalTo.getTerm(0)).solve();
			int xTo = (int) ((NumberTermImpl) literalTo.getTerm(1)).solve();
			int yTo = (int) ((NumberTermImpl) literalTo.getTerm(2)).solve();

			if (current.level.level != levelTo)
				throw new Exception();

			heuristic = heuristicForLocation(xTo, yTo);
		} else {
			final LocationType state = LocationType.valueOf(((StringTermImpl) args[0]).getString());
			heuristic = heuristicForState(state);
		}

		List<Location> shortest = ant.calcShortestPath(ant.currentLevel.model[current.x][current.y], heuristic);

		ListTermImpl path = new ListTermImpl();
		for (Location p : shortest) {
			path.append(p.asLiteral());
		}

		return un.unifies(args[1], path);
	}

	private StopCondition heuristicForState(final LocationType state) {
		return new StopCondition() {
			public boolean isGoal(Location loc) {
				if (loc == null)
					return false;
				return loc.type == state;
			}
		};
	}

	private StopCondition heuristicForLocation(final int x, final int y) {
		return new StopCondition() {
			public boolean isGoal(Location loc) {
				return loc.x == x && loc.y == y;
			}
		};
	}

}