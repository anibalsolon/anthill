package anthill.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import anthill.Anthill;

public class WorldModel {

	Random rand = new Random(667L);

	public enum InfoType {
		STATE, PHER;
	}

	public enum LocationType {
		GROUND, HOLE_UP, HOLE_DOWN, FOOD;
	}

	public Anthill anthill;

	public int width;
	public int height;

	public Map<String, Location> ants;
	public List<Level> levels;

	public WorldModel(Anthill anthill) throws Exception {

		ants = new HashMap<String, Location>();

		this.anthill = anthill;

		String leveldef;
		leveldef = "/levels/anthill-1.lvl";
//		 leveldef = "/levels/anthill-sample-1.lvl";
//		 leveldef = "/levels/anthill-sample-2.lvl";

		File resource = new File(getClass().getResource(leveldef).toURI());
		BufferedReader br = new BufferedReader(new FileReader(resource));

		int levelsnum = Integer.parseInt(br.readLine());
		levels = new ArrayList<Level>(levelsnum);

		for (int i = 0; i < levelsnum; i++) {
			int width = Integer.parseInt(br.readLine());
			int height = Integer.parseInt(br.readLine());
			int offsetx = Integer.parseInt(br.readLine());
			int offsety = Integer.parseInt(br.readLine());

			if (i == 0) {
				this.width = width;
				this.height = height;
			} else {
				if (this.width < width || this.height < height)
					throw new Exception();
			}

			Level level = new Level(i, width, height, offsetx, offsety);
			levels.add(i, level);

			for (int y = 0; y < height; y++) {

				String line = br.readLine();

				for (int x = 0; x < line.length(); x++) {

					level.model[x][y] = new Location(level, x + offsetx, y + offsety);

					switch (line.charAt(x)) {
					case '█':
						level.model[x][y].state = LocationType.GROUND;
						break;
					case '░':
						level.model[x][y].state = LocationType.FOOD;
						break;
					case '↑':
						level.model[x][y].state = LocationType.HOLE_UP;
						break;
					case '↓':
						level.model[x][y].state = LocationType.HOLE_DOWN;
						break;
					default:
						level.model[x][y] = null;
						break;
					}
				}
			}
		}

		cleanInvalidStairs();

		br.close();
	}

	public void cleanInvalidStairs() {
		for (int i = 0; i < levels.size(); i++) {
			Level level = levels.get(i);
			for (int x = 0; x < level.width; x++) {
				for (int y = 0; y < level.height; y++) {
					if (level.model[x][y] == null)
						continue;
					if (level.model[x][y].state != LocationType.HOLE_DOWN && level.model[x][y].state != LocationType.HOLE_UP)
						continue;
					level.model[x][y].link = getLink(level.model[x][y]);
					if (level.model[x][y].link == null) {
						System.out.println("There is a disconnected stair at " + level.model[x][y] + " .");
						level.model[x][y].state = LocationType.GROUND;
					}
				}
			}
		}
	}

	public Location getLink(Location loc) {
		if (loc == null || loc.level == null)
			return null;
		int leveli = loc.level.level;
		if (loc.state == LocationType.HOLE_UP) {
			if (leveli == 0)
				return null;
			Location locto = levels.get(leveli - 1).getAt(loc);
			if (locto != null && locto.state == LocationType.HOLE_DOWN)
				return locto;
		}
		if (loc.state == LocationType.HOLE_DOWN) {
			if (leveli == levels.size() - 1)
				return null;
			Location locto = levels.get(leveli + 1).getAt(loc);
			if (locto != null && locto.state == LocationType.HOLE_UP)
				return locto;

		}
		return null;
	}

	public Location getAntPosition(String agentName) {
		return ants.get(agentName);
	}

	public void setAntPosition(String ant, Location loc) {
		ants.put(ant, loc);
		if (anthill.view != null && anthill.view.level() == loc.level.level)
			anthill.view.repaint();
	}

	public int[] getSafeLevels() {
		int[] levels = new int[this.levels.size() - 1];
		for (int i = 0; i < levels.length; i++) {
			levels[i] = i + 1;
		}
		return levels;
	}

	public List<Location> getSafePlaces() {
		int[] levels = getSafeLevels();
		int level = levels[rand.nextInt(levels.length)];
		Location[][] model = this.levels.get(level).model;
		List<Location> points = new ArrayList<Location>();
		for (int x = 0; x < model.length; x++) {
			for (int y = 0; y < model[x].length; y++) {
				if (model[x][y] == null)
					continue;
				switch (model[x][y].state) {
				case GROUND:
					points.add(model[x][y]);
					break;
				default:
					break;
				}
			}
		}
		return points;
	}

	public List<Location> getPlaces(Level level, LocationType state) {
		Location[][] model = level.model;
		List<Location> points = new ArrayList<Location>();
		for (int x = 0; x < model.length; x++) {
			for (int y = 0; y < model[x].length; y++) {
				if (model[x][y] == null)
					continue;
				if (model[x][y].state == state)
					points.add(model[x][y]);
			}
		}
		return points;
	}

	public Location getRandomPlace(Level level, LocationType state) {
		List<Location> places = getPlaces(level, state);
		int point = rand.nextInt(places.size());
		return places.get(point);
	}

	public Location getRandomSafePlace() {
		List<Location> safePlaces = getSafePlaces();
		int point = rand.nextInt(safePlaces.size());
		return safePlaces.get(point);
	}

	public Location getLocationAt(int lvl, int x, int y) {
		Level level = levels.get(lvl);
		return level.model[x - level.offsetx][y - level.offsety];
	}

}
