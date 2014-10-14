package anthill.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import anthill.Anthill;

public class WorldModel {

	public WorldModel(Anthill anthill) {
		try {
			File resource = new File(getClass().getResource("/levels/anthill-1.lvl").toURI());

			state = new State[levels][width][height];
			int level = 0, i = 0;

			BufferedReader br = new BufferedReader(new FileReader(resource));
			String line;
			while ((line = br.readLine()) != null) {

				for (int j = 0; j < line.length(); j++) {
					switch (line.charAt(j)) {
					case 'G':
						state[level][i][j] = State.GROUND;
						break;
					case 'U':
						state[level][i][j] = State.HOLE_UP;
						break;
					case 'D':
						state[level][i][j] = State.HOLE_DOWN;
						break;
					default:
						state[level][i][j] = State.EMPTY;
						break;
					}
				}

				i++;
				if (line.length() == 0) {
					i = 0;
					level++;
				}

			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public enum State {
		EMPTY, GROUND, HOLE_UP, HOLE_DOWN;
	}

	public int levels = 5;
	public int ground = 0;
	public int width = 11;
	public int height = 10;

	public State[][][] state;

}
