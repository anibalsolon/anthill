package anthill;

import jacamo.infra.JaCaMoLauncher;
import jason.JasonException;

public class AnthillLauncher {

	public static void main(String[] args) throws JasonException {
		args = new String[] { "ants.jcm" };
		JaCaMoLauncher.main(args);
	}

}
