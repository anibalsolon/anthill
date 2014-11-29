package anthill.actions;

import static anthill.Ant.getAntArch;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import anthill.Ant;
import anthill.model.Location;
import anthill.model.WorldModel.LocationType;

public class optimizepath extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		Ant ant = getAntArch(ts.getUserAgArch());

		Literal path = (Literal) Literal.parseLiteral("path(_, _)");
		Iterator<Literal> pathbb = ts.getAg().getBB().getCandidateBeliefs(path, un);
		if (pathbb == null || !pathbb.hasNext())
			return false;

		Literal next = pathbb.next();
		int index = (int) ((NumberTermImpl) next.getTerm(0)).solve();

		Location[] paths = new Location[index + 1];
		Location start = Location.parseLiteral((Literal) next.getTerm(1));
		paths[index] = start;
		while (pathbb.hasNext()) {
			next = pathbb.next();
			index = (int) ((NumberTermImpl) next.getTerm(0)).solve();
			start = Location.parseLiteral((Literal) next.getTerm(1));
			paths[index] = start;
		}

		List<Location> pathList = new ArrayList<Location>(Arrays.asList(paths));

		for (int i = 0; i < pathList.size(); i++) {
			Location iloc = pathList.get(i);
			for (int j = pathList.size() - 1; j > i; j--) {
				if (iloc.equals(pathList.get(j))) {
					List<Location> pathListEnd = pathList.subList(j, pathList.size());
					pathList = pathList.subList(0, i);
					pathList.addAll(pathListEnd);
					break;
				}
			}
			if (i > 0) {
				Location at = ant.currentLevel.getAt(iloc);
				if (at.type == LocationType.HOLE_DOWN) {
					pathList = pathList.subList(i, pathList.size());
					break;
				}
			}
		}

		ListTermImpl finalpath = new ListTermImpl();
		for (int i = 0; i < pathList.size(); i++) {
			ListTerm lit = new ListTermImpl();
			Literal location = Literal.parseLiteral("path");
			lit.append(location);
			ListTerm terms = new ListTermImpl();
			terms.append(new NumberTermImpl(i));
			terms.append(pathList.get(i).asLiteral());
			lit.append(terms);
			Literal pathlit = Literal.newFromListOfTerms(lit);
			finalpath.append(pathlit);
		}

		return un.unifies(args[0], finalpath);
	}

}