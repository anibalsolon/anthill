package anthill.model;

import jason.JasonException;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTermImpl;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import anthill.model.WorldModel.State;
import anthill.util.Random;

public class Location extends Point {

	private static final long serialVersionUID = 1L;

	public Level level;
	public State state;
	public float pher = 0.0f;
	public Location link;
	public int visits = 0;

	public Location(int level, int x, int y) {
		this.level = new Level(level);
		super.setLocation(x, y);
	}

	public Location(Level level, int x, int y) {
		this.level = level;
		super.setLocation(x, y);
	}

	public boolean equals(Location obj) {
		if (this.level == null || obj.level == null)
			return false;
		else if (this.level.level != obj.level.level)
			return false;
		return this.x == obj.x && this.y == obj.y;
	}

	public boolean equals(Location obj, boolean compareLevel) {
		if (compareLevel) {
			if (this.level == null || obj.level == null)
				return false;
			else if (this.level.level != obj.level.level)
				return false;
		}
		return super.equals(obj);
	}

	public String toString() {
		return "(Level: " + (level == null ? "N" : level.level) + ", Loc: " + x + "," + y + ")";
	}

	public Literal asLiteral() throws JasonException {
		ListTerm lit = new ListTermImpl();
		Literal location = Literal.parseLiteral("location");
		lit.append(location);
		ListTerm terms = new ListTermImpl();
		terms.append(new NumberTermImpl((double) (this.level == null ? -1 : this.level.level)));
		terms.append(new NumberTermImpl((double) this.x));
		terms.append(new NumberTermImpl((double) this.y));
		lit.append(terms);
		return Literal.newFromListOfTerms(lit);
	}

	public static Location parseLiteral(Literal lit) throws JasonException {
		int levelFrom = (int) ((NumberTermImpl) lit.getTerm(0)).solve();
		int xFrom = (int) ((NumberTermImpl) lit.getTerm(1)).solve();
		int yFrom = (int) ((NumberTermImpl) lit.getTerm(2)).solve();
		return new Location(levelFrom, xFrom, yFrom);
	}

	public Location dumbLoc() {
		return new Location(this.level.level, this.x, this.y);
	}

	public Location relLoc() {
		return new Location(this.level.level, this.x - this.level.offsetx, this.y - this.level.offsety);
	}

	public List<Location> neighbours() {

		if (this.level == null)
			return null;

		Location[][] model = this.level.model;
		List<Location> neighs = new ArrayList<Location>();
		int[][] vars = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, };

		for (int i = 0; i < vars.length; i++) {
			try {
				int x = this.x + vars[i][0];
				int y = this.y + vars[i][1];
				if (model[x][y] != null)
					neighs.add(model[x][y]);
			} catch (ArrayIndexOutOfBoundsException e) {

			}
		}
		
		Collections.shuffle(neighs);
		return neighs;
	}

	public float chanceRate() {
		Random rand = new Random(System.currentTimeMillis());

		float pherWeight = 9f;
		float randWeight = 1f;
		float randValue = rand.nextFloat();
		randValue = .1f;
		// float dangerWeight = 0.7f;

		return (pher * pherWeight + randValue * randWeight) / (visits + 1);
	}

	public void increase(float factor) {
		pher += factor;
		if (pher > 1)
			pher = 1f;
	}

	public void evaporate(float factor) {
		pher -= factor;
		if (pher < 0)
			pher = 0f;
	}
}
