package anthill;

import cartago.Artifact;
import cartago.INTERNAL_OPERATION;
import cartago.OPERATION;
import cartago.ObsProperty;

public class LifeTickTack extends Artifact {

	boolean counting;
	final static long TICK_TIME = 1 * 1000;

	void init() {
		counting = false;
		defineObsProperty("tick", 0);
	}

	@OPERATION
	void startLife() {
		if (!counting) {
			counting = true;
			execInternalOp("count");
		}
	}

	@OPERATION
	void stop() {
		counting = false;
	}

	@INTERNAL_OPERATION
	void count() {
		while (counting) {
			ObsProperty prop = getObsProperty("tick");
			prop.updateValue(prop.intValue() + 1);
			await_time(TICK_TIME);
		}
	}
}