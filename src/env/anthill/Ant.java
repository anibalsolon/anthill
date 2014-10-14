package anthill;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jacamo.infra.JaCaMoAgArch;
import jason.RevisionFailedException;
import jason.asSyntax.Literal;

public class Ant extends JaCaMoAgArch {

	final static int EPOCH_TIME = 1;

	private long lastCheck = this.curr();
	private int currEpoch = 0;

	public long curr() {
		return new Date().getTime() / 1000;
	}

//	@Override
//	public void checkMail() {
//
//		super.checkMail();
//
//		long thisCheck = curr();
//		if (thisCheck - lastCheck >= EPOCH_TIME) {
//			currEpoch++;
//			addBelief(Literal.parseLiteral("epoch(" + currEpoch + ")"));
//			System.out.println("epoch(" + currEpoch + ")");
//		}
//		lastCheck = thisCheck;
//
//	}
	
	@Override
	public List<Literal> perceive() {
		return Arrays.asList(Literal.parseLiteral("epoch(" + currEpoch + ")"));
	}

	public void addBelief(Literal belief) {
		getTS().getAg().buf(Arrays.asList(belief));
	}

}
