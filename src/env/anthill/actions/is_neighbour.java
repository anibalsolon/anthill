package anthill.actions;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

public class is_neighbour extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) {

		Literal literal = (Literal) args[0];
		int level = (int) ((NumberTermImpl) literal.getTerm(0)).solve();
		int x = (int) ((NumberTermImpl) literal.getTerm(1)).solve();
		int y = (int) ((NumberTermImpl) literal.getTerm(2)).solve();

		Literal literal2 = (Literal) args[1];
		int level2 = (int) ((NumberTermImpl) literal2.getTerm(0)).solve();
		int x2 = (int) ((NumberTermImpl) literal2.getTerm(1)).solve();
		int y2 = (int) ((NumberTermImpl) literal2.getTerm(2)).solve();

		if (level != level2)
			return false;

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (x + i == x2 && y + j == y2) {
					return true;
				}
			}
		}

		return false;

	}
}
