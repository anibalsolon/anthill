package anthill.actions;

import static anthill.Ant.getAntArch;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import anthill.Ant;
import anthill.model.Location;

public class position extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		Ant ant = getAntArch(ts.getUserAgArch());
		Location loc = Location.parseLiteral((Literal) args[0]);
		ant.position = loc;
		return true;
	}

}