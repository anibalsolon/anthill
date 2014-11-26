package anthill.actions;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.InternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.Term;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class my_type extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	private static InternalAction singleton = null;

	public static InternalAction create() {
		if (singleton == null)
			singleton = new my_type();
		return singleton;
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public int getMaxArgs() {
		return 1;
	}

	public static String getType(String agentName) {
		final Pattern p = Pattern.compile("([a-z]+).*");
		final Matcher m = p.matcher(agentName);
		if (m.matches()) {
			return m.group(1);
		}
		return null;
	}

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		checkArguments(args);
		String name = ts.getUserAgArch().getAgName();
		String type = getType(name);
		return un.unifies(args[0], new Atom(type));
	}
}
