package anthill.actions;

import static anthill.Ant.getAntArch;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;
import anthill.Ant;
import anthill.model.Location;

public class findnext extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		Ant ant = getAntArch(ts.getUserAgArch());
		Location next = ant.calcNextMove();
		return un.unifies(args[0], next.asLiteral());
	}

}