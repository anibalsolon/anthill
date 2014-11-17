package anthill.actions;

import static anthill.Ant.getAntArch;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.InternalAction;
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
import anthill.Ant.HeuristicMDP;
import anthill.model.Location;
import anthill.model.WorldModel.State;

public class move extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	private static InternalAction singleton = null;

	public static InternalAction create() {
		if (singleton == null)
			singleton = new move();
		return singleton;
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public int getMaxArgs() {
		return 2;
	}

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		checkArguments(args);

		Ant ant = getAntArch(ts.getUserAgArch());

		Literal location = (Literal) Literal.parseLiteral("location(_, _, _)");
		Iterator<Literal> relB = ts.getAg().getBB().getCandidateBeliefs(location, un);
		if (relB == null || !relB.hasNext()) {
			return false;
		}
		Literal literal = (Literal) relB.next();
		int x = (int) ((NumberTermImpl) literal.getTerm(1)).solve();
		int y = (int) ((NumberTermImpl) literal.getTerm(2)).solve();

		final State state = State.valueOf(((StringTermImpl) args[0]).getString());

		System.out.println("FROM: " + ant.currentLevel.model[x][y].toString());

//		List<Location> shortest = ant.calcShortestPath(ant.currentLevel.model[x][y], new HeuristicMDP() {
//			public boolean isGoal(Location loc) {
//				// if (loc.state == state)
//				System.out.println("TO: " + loc.toString() + ", " + loc.state);
//				return loc.state == state;
//			}
//		});

		ListTermImpl path = new ListTermImpl();
//		for (Location p : shortest) {
//			path.append(p.asLiteral());
//		}

		return un.unifies(args[1], path);
	}
}
