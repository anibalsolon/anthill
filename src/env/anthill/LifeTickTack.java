package anthill;

import cartago.Artifact;
import cartago.INTERNAL_OPERATION;
import cartago.OPERATION;

public class LifeTickTack extends Artifact {

	boolean counting;
	final static long TICK_TIME = 1 * 1000;

	void init() {
		counting = false;
	}

	@OPERATION
	void startLife() {
		if (!counting) {
			counting = true;
			execInternalOp("count");
		} else {
			failed("already_counting");
		}
	}

	@OPERATION
	void stop() {
		counting = false;
	}

	@INTERNAL_OPERATION
	void count() {
		while (counting) {
			signal("deathIsNear");
			await_time(TICK_TIME);
		}
	}
}