package anthill.view;

import javax.swing.JFrame;

import anthill.Anthill;

public class AnthillView extends JFrame {

	private static final long serialVersionUID = 1L;

	public Anthill anthill;
	private Drawer drawPanel;

	public AnthillView(Anthill anthill) {

		this.anthill = anthill;

		setTitle("AntHill");
		setSize(500, 500);
		setLocation(0, 0);
		setVisible(true);

		drawPanel = new Drawer(anthill.model);
		setContentPane(drawPanel);

		addMouseWheelListener(drawPanel);
		addKeyListener(drawPanel);

	}

	public int level() {
		return drawPanel.currentLevel;
	}

}