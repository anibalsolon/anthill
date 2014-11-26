package anthill.actions;

import jason.JasonException;
import jason.asSemantics.Circumstance;
import jason.asSemantics.CircumstanceListener;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.Event;
import jason.asSemantics.Intention;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.InternalActionLiteral;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.PlanBody;
import jason.asSyntax.PlanBody.BodyType;
import jason.asSyntax.PlanBodyImpl;
import jason.asSyntax.Term;
import jason.asSyntax.Trigger;
import jason.stdlib.suspend;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import anthill.Anthill;

public class wait extends DefaultInternalAction {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean canBeUsedInContext() {
		return false;
	}

	@Override
	public boolean suspendIntention() {
		return true;
	}

	@Override
	public int getMinArgs() {
		return 1;
	}

	@Override
	public int getMaxArgs() {
		return 1;
	}

	@Override
	public Object execute(final TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		checkArguments(args);

		long timeout = -1;
		Trigger te = null;
		LogicalFormula f = null;
		Term elapsedTime = null;

		NumberTerm time = (NumberTerm) args[0];
		timeout = (long) Math.ceil(time.solve());

		new WaitEvent(te, f, un, ts, timeout * Anthill.TIME_DILATATION, elapsedTime);

		return true;
	}

	class WaitEvent implements CircumstanceListener {
		private Trigger te;
		private LogicalFormula formula;
		private String sEvt;
		private Unifier un;
		private Intention si;
		private TransitionSystem ts;
		private Circumstance c;
		private boolean dropped = false;
		private Term elapsedTimeTerm;
		private long startTime;

		WaitEvent(Trigger te, LogicalFormula f, Unifier un, TransitionSystem ts, long timeout, Term elapsedTimeTerm) {
			this.te = te;
			this.formula = f;
			this.un = un;
			this.ts = ts;
			c = ts.getC();
			si = c.getSelectedIntention();
			this.elapsedTimeTerm = elapsedTimeTerm;

			c.addEventListener(this);

			if (te != null) {
				sEvt = te.toString();
			} else if (formula != null) {
				sEvt = formula.toString();
			} else {
				sEvt = "time" + (timeout);
			}
			sEvt = si.getId() + "/" + sEvt;
			c.addPendingIntention(sEvt, si);

			startTime = System.currentTimeMillis();

			if (timeout >= 0) {
				ts.getAg().getScheduler().schedule(new Runnable() {
					public void run() {
						resume(true);
					}
				}, timeout, TimeUnit.MILLISECONDS);
			}
		}

		void resume(final boolean stopByTimeout) {
			c.removeEventListener(this);
			ts.runAtBeginOfNextCycle(new Runnable() {
				public void run() {
					try {
						if (c.removePendingIntention(sEvt) == si && (si.isAtomic() || !c.hasIntention(si)) && !dropped) {
							if (stopByTimeout && te != null && elapsedTimeTerm == null) {
								if (si.isSuspended()) {
									PlanBody body = si.peek().getPlan().getBody();
									body.add(1, new PlanBodyImpl(BodyType.internalAction, new InternalActionLiteral(".fail")));
									c.addPendingIntention(suspend.SUSPENDED_INT + si.getId(), si);
								} else {
									ts.generateGoalDeletion(si, JasonException.createBasicErrorAnnots("wait_timeout", "timeout in .wait"));
								}
							} else {
								si.peek().removeCurrentStep();
								if (elapsedTimeTerm != null) {
									long elapsedTime = System.currentTimeMillis() - startTime;
									un.unifies(elapsedTimeTerm, new NumberTermImpl(elapsedTime));
								}
								if (si.isSuspended()) {
									c.addPendingIntention(suspend.SUSPENDED_INT + si.getId(), si);
								} else {
									c.resumeIntention(si);
								}
							}
						}
					} catch (Exception e) {
						ts.getLogger().log(Level.SEVERE, "Error at .wait thread", e);
					}
				}
			});
			ts.getUserAgArch().wake();
		}

		public void eventAdded(Event e) {
			if (dropped)
				return;
			if (te != null && un.unifies(te, e.getTrigger())) {
				resume(false);
			} else if (formula != null && ts.getAg().believes(formula, un)) {
				resume(false);
			}
		}

		public void intentionDropped(Intention i) {
			if (i.equals(si)) {
				dropped = true;
				resume(false);
			}
		}

		public void intentionAdded(Intention i) {
		}

		public void intentionResumed(Intention i) {
		}

		public void intentionSuspended(Intention i, String reason) {
		}

		public String toString() {
			return sEvt;
		}
	}
}