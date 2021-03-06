package anthill.model.rl;

public class QPair<S, A> {
	private S state;
	private A action;

	public QPair(S state, A action) {
		this.state = state;
		this.action = action;
	}

	public S getState() {
		return state;
	}

	public A getAction() {
		return action;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		QPair<S, A> qPair = (QPair<S, A>) o;

		if (action != null ? !action.equals(qPair.action) : qPair.action != null)
			return false;

		if (state != null ? !state.equals(qPair.state) : qPair.state != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = state != null ? state.hashCode() : 0;
		result = 31 * result + (action != null ? action.hashCode() : 0);
		return result;
	}
}
