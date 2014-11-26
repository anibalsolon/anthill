package anthill.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import anthill.model.Level;
import anthill.model.Location;
import anthill.model.WorldModel;
import anthill.model.WorldModel.LocationType;

public class Drawer extends JPanel implements MouseWheelListener, KeyListener {

	private static final long serialVersionUID = 1L;

	private final WorldModel model;
	private final Map<String, BufferedImage> images;
	private final Random rand;

	private final Dimension tilesize, tilehalfsize;
	private final Rectangle levelsize;

	public int currentLevel = 0;
	public float zoom = 1;

	public Drawer(WorldModel model) {

		this.model = model;

		rand = new Random(666L);

		tilesize = new Dimension(80, 30);
		tilehalfsize = new Dimension(40, 15);

		levelsize = new Rectangle(tilehalfsize.width * model.width, -2, (model.width + model.height) * tilehalfsize.width, (model.width + model.height) * tilehalfsize.height);

		String[] imagepaths = new String[] { "ground-1.png", "ground-2.png", "hole_up-1.png", "hole_down-1.png", "food-1.png", "border-nw.png", "border-ne.png", "border-sw.png", "border-se.png", };

		images = new HashMap<String, BufferedImage>();
		for (String imagepath : imagepaths) {
			try {
				URL resource = getClass().getResource("/tiles/" + imagepath);
				String tilename = imagepath.substring(0, imagepath.indexOf('.'));
				images.put(tilename, ImageIO.read(resource));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private Point pointForIndex(int i, int j) {
		return pointForIndex(i, j, false);
	}

	private Point pointForIndex(int i, int j, boolean center) {
		int x = levelsize.x - ((i - j) * (tilehalfsize.width)) - tilehalfsize.width - 2 + (center ? tilehalfsize.width : 0);
		int y = levelsize.y + ((i + j) * (tilehalfsize.height)) + +(center ? tilehalfsize.height : 0);
		return new Point(x, y);
	}

	public void drawLevel(Graphics2D big, int leveli) {

		Level level = model.levels.get(leveli);

		for (int i = 0; i < level.width; i++) {
			for (int j = 0; j < level.height; j++) {

				Location loc = level.model[i][j];
				if (loc == null)
					continue;

				int x = i + level.offsetx;
				int y = j + level.offsety;

				Point p = pointForIndex(x, y);
				LocationType st = loc.state;

				int nextInt;
				String[] grounds;

				switch (st) {
				case FOOD:
				case GROUND:
					big.drawImage(images.get("ground-1"), p.x, p.y, tilesize.width + 4, tilesize.height + 2, this);

					BufferedImage pherBi = new BufferedImage(tilesize.width, tilesize.height, BufferedImage.TYPE_INT_ARGB);
					Graphics g = pherBi.createGraphics();
					g.drawImage(images.get("ground-1"), 0, 0, this);
					WritableRaster wr = pherBi.getRaster();

					int rate = (int) (loc.pher * 255);
					paintColor(wr, new Color(255, 0, 0, rate));

					big.drawImage(pherBi, p.x, p.y, tilesize.width + 4, tilesize.height + 2, this);

					String pher = String.format("%.2g%n", loc.pher);
//					big.drawString(pher + "", p.x + tilehalfsize.width, p.y + tilehalfsize.height);

					break;
				case HOLE_DOWN:
					big.drawImage(images.get("hole_down-1"), p.x, p.y, tilesize.width + 4, tilesize.height + 2, this);
					break;
				case HOLE_UP:
					big.drawImage(images.get("hole_up-1"), p.x, p.y, tilesize.width + 4, tilesize.height + 2, this);
					break;
				default:
					break;
				}
				
				if(st == LocationType.FOOD){
					big.drawImage(images.get("food-1"), p.x, p.y, tilesize.width + 4, tilesize.height + 2, this);
				}

				switch (st) {
				case HOLE_DOWN:
					if (j == level.height - 1 || level.model[i][j + 1] == null)
						big.drawImage(images.get("border-se"), p.x, p.y, tilesize.width + 4, tilesize.height + 2, this);
					if (i == level.width - 1 || level.model[i + 1][j] == null)
						big.drawImage(images.get("border-sw"), p.x, p.y, tilesize.width + 4, tilesize.height + 2, this);
					break;
				case GROUND:
				case HOLE_UP:
				case FOOD:
					if (i == 0 || level.model[i - 1][j] == null)
						big.drawImage(images.get("border-ne"), p.x, p.y, tilesize.width + 4, tilesize.height + 2, this);
					if (j == 0 || level.model[i][j - 1] == null)
						big.drawImage(images.get("border-nw"), p.x, p.y, tilesize.width + 4, tilesize.height + 2, this);
					if (j == level.height - 1 || level.model[i][j + 1] == null)
						big.drawImage(images.get("border-se"), p.x, p.y, tilesize.width + 4, tilesize.height + 2, this);
					if (i == level.width - 1 || level.model[i + 1][j] == null)
						big.drawImage(images.get("border-sw"), p.x, p.y, tilesize.width + 4, tilesize.height + 2, this);
					break;
				default:
					break;
				}
			}
		}

	}

	private void paintColor(WritableRaster wr, Color c) {
		int[] pixel = new int[4];
		for (int pi = 0; pi < wr.getWidth(); pi++) {
			for (int pj = 0; pj < wr.getHeight(); pj++) {
				wr.getPixel(pi, pj, pixel);
				if (pixel[3] == 255) {
					pixel[0] = c.getRed();
					pixel[1] = c.getGreen();
					pixel[2] = c.getBlue();
					pixel[3] = c.getAlpha();
					wr.setPixel(pi, pj, pixel);
				}
			}
		}
	}

	private void drawAnts(Graphics2D big, int level) {
		Set<String> ants = model.ants.keySet();
		for (String ant : ants) {
			Location location = model.ants.get(ant);
			if (location.level.level == level) {
				Point p = pointForIndex(location.x, location.y, true);
				big.drawOval(p.x - 5, p.y - 5, 10, 10);
			}
		}
	}

	private float levelRate(int level) {
		return 1.0f - (level / 10.0f);
	}

	private Dimension levelSize(int level, float rate) {
		int width = (model.width + model.height) * tilehalfsize.width;
		int height = (model.width + model.height) * tilehalfsize.height;
		return new Dimension((int) (width * rate), (int) (height * rate));
	}

	private Dimension levelSize(int level) {
		return levelSize(level, 1.0f);
	}

	private Point levelPosition(int level) {
		Dimension levelsize = levelSize(level, levelRate(level));
		Dimension zerosize;
		if (level == 0)
			zerosize = levelsize;
		else
			zerosize = levelSize(0);
		return new Point(zerosize.width / 2 - levelsize.width / 2, 80 * level);
	}

	private Dimension anthillSize(float zoom) {
		int lastLevel = model.levels.size() - currentLevel - 1;
		Point levelpos = levelPosition(lastLevel);
		Dimension zerosize = levelSize(0);
		Dimension levelsize = levelSize(lastLevel, levelRate(lastLevel));
		return new Dimension((int) (zerosize.width * zoom), (int) ((levelpos.y + levelsize.height) * zoom));
	}

	private Point anthillPosition(float zoom) {
		Dimension size = anthillSize(zoom);
		return new Point(getWidth() / 2 - size.width / 2, getHeight() / 2 - size.height / 2);
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Dimension size = getSize();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, size.width, size.height);

		Dimension anthillRealSize = anthillSize(1f);
		BufferedImage abi = new BufferedImage(anthillRealSize.width, anthillRealSize.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D bigfull = abi.createGraphics();

		for (int level = model.levels.size() - 1; level >= currentLevel; level--) {

			BufferedImage bi = new BufferedImage(levelsize.width, levelsize.height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D big = bi.createGraphics();
			drawLevel(big, level);

			int relativeLevel = level - currentLevel;

			Dimension levelsize = levelSize(relativeLevel, levelRate(relativeLevel));
			Point levelpos = levelPosition(relativeLevel);

			if (relativeLevel > 0) {
				WritableRaster wr = bi.getRaster();
				changeLevelOpacity(wr, relativeLevel);
			} else {
				drawAnts(big, level);
			}

			bigfull.drawImage(bi, levelpos.x, levelpos.y, levelsize.width, levelsize.height, this);

		}

		Point anthillPos = anthillPosition(zoom);
		Dimension anthillSize = anthillSize(zoom);
		g.drawImage(abi, anthillPos.x, anthillPos.y, anthillSize.width, anthillSize.height, this);
		g.dispose();

	}

	private void changeLevelOpacity(WritableRaster wr, int relativeLevel) {
		float rate = levelRate(relativeLevel) * 0.5f;
		int[] pixel = new int[4];
		for (int i = 0; i < wr.getWidth(); i++) {
			for (int j = 0; j < wr.getHeight(); j++) {
				wr.getPixel(i, j, pixel);
				pixel[0] = (int) (pixel[0] * rate);
				pixel[1] = (int) (pixel[1] * rate);
				pixel[2] = (int) (pixel[2] * rate);
				wr.setPixel(i, j, pixel);
			}
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		int notches = e.getWheelRotation();
		if (e.isControlDown()) {
			zoom += -0.06 * notches;
			this.repaint();
		} else {
			int oldlevel = currentLevel;
			currentLevel += notches;
			if (currentLevel < 0)
				currentLevel = 0;
			if (currentLevel >= model.levels.size())
				currentLevel = model.levels.size() - 1;
			if (oldlevel != currentLevel)
				this.repaint();
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {

		int notches = e.getKeyCode();
		int diff = 0;

		switch (notches) {
		case KeyEvent.VK_UP:
			diff = -1;
			break;
		case KeyEvent.VK_DOWN:
			diff = 1;
			break;
		}

		if (diff != 0) {
			int oldlevel = currentLevel;
			currentLevel += diff;
			if (currentLevel < 0)
				currentLevel = 0;
			if (currentLevel >= model.levels.size())
				currentLevel = model.levels.size() - 1;
			if (oldlevel != currentLevel)
				this.repaint();
		}

	}

}