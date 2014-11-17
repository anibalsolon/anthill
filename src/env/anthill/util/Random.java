package anthill.util;

import java.util.List;

public class Random extends java.util.Random {

	private static final long serialVersionUID = 1L;

	public Random() {
	}

	public Random(long currentTimeMillis) {
		super(currentTimeMillis);
	}

	public <T> T getItem(List<T> objects) {
		int i = nextInt(objects.size());
		return objects.get(i);
	}

	public <T> T getItem(T[] objects) {
		int i = nextInt(objects.length);
		return objects[i];
	}

}
