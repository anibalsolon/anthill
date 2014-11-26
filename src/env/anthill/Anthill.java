package anthill;

import java.util.HashMap;
import java.util.Map;

import anthill.actions.my_type;
import anthill.model.Level;
import anthill.model.Location;
import anthill.model.WorldModel;
import anthill.model.WorldModel.InfoType;
import anthill.model.WorldModel.LocationType;
import anthill.view.AnthillView;
import cartago.AgentId;
import cartago.Artifact;
import cartago.INTERNAL_OPERATION;
import cartago.OPERATION;

public class Anthill extends Artifact {

	public AnthillView view;
	public WorldModel model;
	public Map<String, Integer> anthillCount;

	boolean counting = false;
	
	public final static long TIME_DILATATION = 100; // TODO verify english
	public final static long EVAPORATION_TIME = 5 * TIME_DILATATION;

	public void init() {
		try {
			model = new WorldModel(this);
			view = new AnthillView(this);
			view.repaint();

			anthillCount = new HashMap<String, Integer>();

			if (!counting) {
				counting = true;
				execInternalOp("evaporate");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@INTERNAL_OPERATION
	void evaporate() {
		Location[][] groundModel = model.levels.get(0).model;
		while (counting) {
			for (int i = 0; i < groundModel.length; i++) {
				for (int j = 0; j < groundModel[i].length; j++) {
					groundModel[i][j].evaporate(0.1f);
				}
			}
			await_time(EVAPORATION_TIME);
		}
	}

	@OPERATION
	public void initAnt() {
		AgentId ant = getOpUserId();

		String type = my_type.getType(ant.getAgentName());
		Integer count = anthillCount.get(type);
		if (count == null)
			anthillCount.put(type, 1);
		else
			anthillCount.put(type, count + 1);

		Location random = model.getRandomSafePlace();
		model.setAntPosition(ant.getAgentName(), random);
		location();
		level();
	}

	@OPERATION
	public void die() {
		AgentId ant = getOpUserId();
		String type = my_type.getType(ant.getAgentName());
		Integer count = anthillCount.get(type);
		anthillCount.put(type, count - 1);
	}

	@OPERATION
	public void pheromone(double factor) {
		AgentId ant = getOpUserId();
		Location current = model.getAntPosition(ant.getAgentName());
		if (current.level.level == 0) {
			current.increase((float) factor);
			signal("lvlknow", current.level.level, current.x, current.y, InfoType.PHER.toString(), current.pher);
		}

		if (view != null && view.level() == 0)
			view.repaint();

	}

	@OPERATION
	public void move(int x, int y) {
		AgentId ant = getOpUserId();
		Location current = model.getAntPosition(ant.getAgentName());
		Location newPos = model.getLocationAt(current.level.level, current.x + x, current.y + y);
		model.setAntPosition(ant.getAgentName(), newPos);
	}

	@OPERATION
	public void up() {
		AgentId ant = getOpUserId();
		Location current = model.getAntPosition(ant.getAgentName());
		if (current.state == LocationType.HOLE_UP) {
			Location up = model.getLink(current);
			model.setAntPosition(ant.getAgentName(), up);
			location();
			level();
		}
	}

	@OPERATION
	public void down() {
		AgentId ant = getOpUserId();
		Location current = model.getAntPosition(ant.getAgentName());
		if (current.state == LocationType.HOLE_DOWN) {
			Location down = model.getLink(current);
			model.setAntPosition(ant.getAgentName(), down);
			location();
			level();
		}
	}

	public void level() {
		AgentId ant = getOpUserId();
		Location loc = model.getAntPosition(ant.getAgentName());
		Level level = loc.level;
		Location[][] model = level.model;
		signal(ant, "lvlknow", false);
		signal(ant, "lvlknow", level.level, level.width, level.height);

		for (int x = 0; x < model.length; x++) {
			for (int y = 0; y < model[x].length; y++) {
				if (model[x][y] == null)
					continue;
				signal(ant, "lvlknow", level.level, x, y, InfoType.STATE.toString(), model[x][y].state.toString());
				signal(ant, "lvlknow", level.level, x, y, InfoType.PHER.toString(), model[x][y].pher);
			}
		}

		signal(ant, "lvlknow", true);
	}

	public void location() {
		AgentId ant = getOpUserId();
		Location loc = model.getAntPosition(ant.getAgentName()).relLoc();
		signal(ant, "location", loc.level.level, loc.x, loc.y);
	}

}