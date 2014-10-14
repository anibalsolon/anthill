package anthill;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import anthill.model.WorldModel;
import anthill.model.WorldModel.State;
import cartago.tools.GUIArtifact;

public class Anthill extends GUIArtifact {

	public AnthillView view;
	public WorldModel model;

	@Override
	public void init() {
		model = new WorldModel(this);
		view = new AnthillView(this);
	}

	class AnthillView extends JFrame {

		private static final long serialVersionUID = 1L;

		public Anthill anthill;
		private Drawer drawPanel;

		public AnthillView(Anthill anthill) {

			this.anthill = anthill;

			setTitle("AntHill");
			setSize(900, 600);
			setLocation(0,0);
			setVisible(true);

			drawPanel = new Drawer(this);
			setContentPane(drawPanel);
			this.repaint();

		}

	}

	class Drawer extends JPanel implements MouseWheelListener {

		private static final long serialVersionUID = 1L;

		private final AnthillView view;
		private final Map<String, BufferedImage> images;
		private final Random randomGenerator;		
		private final Dimension tilesize, tilehalfsize;
		
		private int currentLevel = 0;
		private float zoom = 1;

		public Drawer(AnthillView view) {

			this.view = view;
			
			addMouseWheelListener(this);

			randomGenerator = new Random(666L);
			
			tilesize = new Dimension(80, 30);
			tilehalfsize = new Dimension(40, 15);

			String[] imagepaths = new String[]{
				"ground-1.png",
				"ground-2.png",
				"hole_up-1.png",
				"hole_down-1.png",
				"border-nw.png",
				"border-ne.png",
				"border-sw.png",
				"border-se.png",
			};
			
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
		
		public BufferedImage drawLevel(int level){
			
			int startX = tilehalfsize.width * model.width;
			int startY = -2;

			int width = (model.width + model.height) * tilehalfsize.width;
			int height = (model.width + model.height) * tilehalfsize.height;

			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D big = bi.createGraphics();

			for (int i = 0; i < model.width; i++) {
				for (int j = 0; j < model.height; j++) {

					int x = startX - ((i - j) * (tilehalfsize.width)) - tilehalfsize.width - 2;
					int y = startY + ((i + j) * (tilehalfsize.height));

					State st = model.state[level][i][j];

					int nextInt;
					String[] grounds;

					switch (st) {
					case EMPTY:
						break;
					case GROUND:
						grounds = new String[] { "ground-1", "ground-2" };
						nextInt = randomGenerator.nextInt(grounds.length);
						big.drawImage(images.get(grounds[nextInt]), x, y, tilesize.width + 4, tilesize.height + 2, this);
						break;
					case HOLE_DOWN:
						grounds = new String[] { "hole_down-1" };
						nextInt = randomGenerator.nextInt(grounds.length);
						big.drawImage(images.get(grounds[nextInt]), x, y, tilesize.width + 4, tilesize.height + 2, this);
						break;
					case HOLE_UP:
						grounds = new String[] { "hole_up-1" };
						nextInt = randomGenerator.nextInt(grounds.length);
						big.drawImage(images.get(grounds[nextInt]), x, y, tilesize.width + 4, tilesize.height + 2, this);
						break;
					default:
						break;
					}
					
					switch (st) {
					case EMPTY:
						break;
					case HOLE_DOWN:
						if (j == model.height - 1 || model.state[level][i][j + 1] == State.EMPTY)
							big.drawImage(images.get("border-se"), x, y, tilesize.width + 4, tilesize.height + 2, this);
						if (i == model.width - 1 || model.state[level][i + 1][j] == State.EMPTY)
							big.drawImage(images.get("border-sw"), x, y, tilesize.width + 4, tilesize.height + 2, this);
						break;
					case GROUND:
					case HOLE_UP:
						if (i == 0 || model.state[level][i - 1][j] == State.EMPTY)
							big.drawImage(images.get("border-ne"), x, y, tilesize.width + 4, tilesize.height + 2, this);
						if (j == 0 || model.state[level][i][j - 1] == State.EMPTY)
							big.drawImage(images.get("border-nw"), x, y, tilesize.width + 4, tilesize.height + 2, this);
						if (j == model.height - 1 || model.state[level][i][j + 1] == State.EMPTY)
							big.drawImage(images.get("border-se"), x, y, tilesize.width + 4, tilesize.height + 2, this);
						if (i == model.width - 1 || model.state[level][i + 1][j] == State.EMPTY)
							big.drawImage(images.get("border-sw"), x, y, tilesize.width + 4, tilesize.height + 2, this);
						break;
					default:
						break;
					}
				}
			}
			
			return bi;
			
		}
		
		private float levelRate(int level){
			return 1.0f - ( level / 10.0f );
		}
		
		private Dimension levelSize(int level){
			int width = (model.width + model.height) * tilehalfsize.width;
			int height = (model.width + model.height) * tilehalfsize.height;
			float rate = levelRate(level);
			return new Dimension( (int) (width * rate),  (int) (height * rate));
		}
		
		private Point levelPosition(int level){
			Dimension levelsize = levelSize(level);
			Dimension zerosize;
			if(level == 0)
				zerosize = levelsize;
			else
				zerosize = levelSize(0);
			return new Point(zerosize.width / 2 - levelsize.width / 2, 80 * level);
		}
		
		private Dimension anthillSize(float zoom){
			int lastLevel = model.levels - currentLevel - 1;
			Point levelpos = levelPosition(lastLevel);
			Dimension zerosize = levelSize(0);
			Dimension levelsize = levelSize(lastLevel);
			return new Dimension( (int) (zerosize.width * zoom), (int) (( levelpos.y + levelsize.height ) * zoom) );
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
			Graphics2D big = abi.createGraphics();
			
			for (int level = model.levels - 1; level >= currentLevel; level--) {
				
				BufferedImage bi = drawLevel(level);
				
				int relativeLevel = level - currentLevel;

				Dimension levelsize = levelSize(relativeLevel);
				Point levelpos = levelPosition(relativeLevel);
				
				if(relativeLevel > 0){
					float rate = levelRate(relativeLevel) * 0.5f;					
			        WritableRaster wr = bi.getRaster();
			        int[] pixel = new int[4];
			        for(int i = 0; i < wr.getWidth(); i++){
			            for(int j = 0; j < wr.getHeight(); j++){
			                wr.getPixel(i, j, pixel);
			                pixel[0] = (int) (pixel[0] * rate);
			                pixel[1] = (int) (pixel[1] * rate);
			                pixel[2] = (int) (pixel[2] * rate);
			                wr.setPixel(i, j, pixel);
			            }
			        }
				}
				
				big.drawImage(bi, levelpos.x, levelpos.y, levelsize.width, levelsize.height, this);

			}

			Point anthillPos = anthillPosition(zoom);
			Dimension anthillSize = anthillSize(zoom);
			g.drawImage(abi, anthillPos.x, anthillPos.y, anthillSize.width, anthillSize.height, this);
			g.dispose();

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
				if (currentLevel >= model.levels)
					currentLevel = model.levels - 1;
				if (oldlevel != currentLevel)
					this.repaint();
			}
		}

	}
}

