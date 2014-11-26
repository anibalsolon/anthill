package anthill.actions;

import static anthill.Ant.getAntArch;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.StringTermImpl;
import jason.asSyntax.Term;
import anthill.Ant;
import anthill.model.Level;
import anthill.model.Location;
import anthill.model.WorldModel.InfoType;
import anthill.model.WorldModel.LocationType;

public class knowledge extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		Ant ant = getAntArch(ts.getUserAgArch());
		if (args.length == 3) {
			int level = (int) ((NumberTermImpl) args[0]).solve();
			int width = (int) ((NumberTermImpl) args[1]).solve();
			int height = (int) ((NumberTermImpl) args[2]).solve();
			ant.currentLevel = new Level(level, width, height);
		}
		if (args.length == 5) {
			int x = (int) ((NumberTermImpl) args[1]).solve();
			int y = (int) ((NumberTermImpl) args[2]).solve();
			InfoType type = InfoType.valueOf(((StringTermImpl) args[3]).getString());
			if (ant.currentLevel.model[x][y] == null)
				ant.currentLevel.model[x][y] = new Location(ant.currentLevel, x, y);
			switch (type) {
			case PHER:
				double pher = ((NumberTermImpl) args[4]).solve();
				ant.currentLevel.model[x][y].pher = (float) pher;
				break;
			case STATE:
				LocationType state = LocationType.valueOf(((StringTermImpl) args[4]).getString());
				ant.currentLevel.model[x][y].state = state;
				break;
			default:
				break;
			}
		}
		return true;
	}
}
