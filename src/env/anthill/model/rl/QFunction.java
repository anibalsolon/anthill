package anthill.model.rl;

import java.util.HashMap;
import java.util.Map;

public class QFunction<S, A> {
	private Map<QPair<S, A>, Double> actionValues = new HashMap<QPair<S, A>, Double>();

	public double getValue(S state, A action) {
		return getValue(new QPair<S, A>(state, action));
	}

	public double getValue(QPair<S, A> qPair) {
		return actionValues.containsKey(qPair) ? actionValues.get(qPair) : 0;
	}

	public void setValue(QPair<S, A> qPair, double value) {
		actionValues.put(qPair, value);
	}

	public void setValue(S state, A action, double value) {
		setValue(new QPair<S, A>(state, action), value);
	}
}
