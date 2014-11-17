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

import java.util.Iterator;
import java.util.List;

import anthill.Ant;
import anthill.Ant.Heuristic;
import anthill.model.Location;
import anthill.model.WorldModel.State;

public class findclosest extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {

		Ant ant = getAntArch(ts.getUserAgArch());

		Literal location = (Literal) Literal.parseLiteral("location(_, _, _)");
		Iterator<Literal> relB = ts.getAg().getBB().getCandidateBeliefs(location, un);
		if (relB == null || !relB.hasNext()) {
			return false;
		}
		Literal literalFrom = (Literal) relB.next();
		int levelFrom = (int) ((NumberTermImpl) literalFrom.getTerm(0)).solve();
		int xFrom = (int) ((NumberTermImpl) literalFrom.getTerm(1)).solve();
		int yFrom = (int) ((NumberTermImpl) literalFrom.getTerm(2)).solve();

		Heuristic heuristic = null;
		if (args[0].isLiteral()) {
			Literal literalTo = (Literal) args[0];
			int levelTo = (int) ((NumberTermImpl) literalTo.getTerm(0)).solve();
			int xTo = (int) ((NumberTermImpl) literalTo.getTerm(1)).solve();
			int yTo = (int) ((NumberTermImpl) literalTo.getTerm(2)).solve();

			if (levelFrom != levelTo)
				throw new Exception();

			heuristic = heuristicForLocation(xTo, yTo);
		} else {
			final State state = State.valueOf(((StringTermImpl) args[0]).getString());
			heuristic = heuristicForState(state);
		}

		List<Location> shortest = ant.calcShortestPath(ant.currentLevel.model[xFrom][yFrom], heuristic);

		ListTermImpl path = new ListTermImpl();
		for (Location p : shortest) {
			path.append(p.asLiteral());
		}

		return un.unifies(args[1], path);
	}

	private Heuristic heuristicForState(final State state) {
		return new Heuristic() {
			public boolean isGoal(Location loc) {
				if (loc == null)
					return false;
				return loc.state == state;
			}
		};
	}

	private Heuristic heuristicForLocation(final int x, final int y) {
		return new Heuristic() {
			public boolean isGoal(Location loc) {
				return loc.x == x && loc.y == y;
			}
		};
	}

}