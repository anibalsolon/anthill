package anthill.actions;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Term;
import anthill.model.WorldModel.LocationType;

public class is_landmark extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) {
		StringTerm type = (StringTerm) args[0];
		LocationType locType = LocationType.valueOf(type.getString());
		return locType.isLandmark();
	}
}
