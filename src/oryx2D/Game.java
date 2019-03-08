package oryx2D;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import oryx2D.entity.mob.Player;
import oryx2D.graphics.Screen;
import oryx2D.input.Keyboard;
import oryx2D.input.Mouse;
import oryx2D.level.Level;
import oryx2D.level.TileCoordinate;
import rotmg.WebMain;
import rotmg.account.core.WebAccount;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
    private static final String TITLE = "Oryx2D";

    private static final double frameRate = 60;

    private static final int scale = 6; // (50 / 8 = 6.25)

    private static final float UI_SCALING = 1.25F;

    private static final int width = (int) ((800 / scale) * UI_SCALING);
    private static final int height = (int) ((600 / scale) * UI_SCALING);

    // FPS

    private static boolean showFPS = true;
    private int currentFPS = 0;

    //

    private boolean running = false;
    private Thread thread;

    //

    private Keyboard key;

    private Level level = new Level(500, 500);

    private Player player;

    private Screen screen;

    public Game() {
        Dimension size = new Dimension(width * scale, height * scale);
        this.setPreferredSize(size);

        this.screen = new Screen(width, height);
        this.frame = new JFrame();
        this.key = new Keyboard();
        TileCoordinate playerSpawn = new TileCoordinate(113, 159);
        this.player = new Player(playerSpawn.x(), playerSpawn.y(), this.key);
        this.player.init(this.level);

        this.addKeyListener(this.key);

        try {
            BufferedImage image = ImageIO.read(Game.class.getResource("/oryx2D/cursor/cursor.png"));
            this.frame.setCursor(this.frame.getToolkit().createCustomCursor(
                    image, new Point(image.getHeight() / 2, image.getWidth() / 2),
                    "null"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Mouse mouse = new Mouse();
        this.addMouseListener(mouse);
        this.addMouseMotionListener(mouse);
    }

    public static int getWindowWidth() {
        return width * scale;
    }

    public static int getWindowHeight() {
        return height * scale;
    }

    public static void launch(WebAccount account) {
        WebAccount.setInstance(account);

        WebMain webMain = new WebMain();
        //webMain.hook(this);

        Game game = new Game();
        game.frame.add(game);
        game.frame.setTitle(Game.TITLE);
        game.frame.setResizable(true);
        game.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.frame.setLocationRelativeTo(null);
        game.frame.pack();
        game.frame.setVisible(true);
        game.start();
    }

    public synchronized void start() {
        if (this.running) {
            return;
        }

        this.running = true;
        this.thread = new Thread(this, "Render thread");
        this.thread.start();
    }

    public synchronized void stop() {
        this.running = false;
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / frameRate;
        double delta = 0;
        int updates = 0;

        this.requestFocus();

        while (this.running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) { //Only happens 60 times a second
                this.update();
                this.render();

                updates++;
                delta--;
            }

            if ((System.currentTimeMillis() - timer) > 1000) {
                timer += 1000;
                this.currentFPS = updates;
                updates = 0;
            }
        }
        this.stop();
    }

    public void update() {
        //System.out.println("Squares : " + AbstractMap.squares.length + ", BO : " + AbstractMap.boDict.size() + ", GO : " + AbstractMap.goDict.size());
        this.key.update();
        this.player.update();
    }

    public void render() {

        if (!this.isFocusOwner()) {
            return;
        }

        BufferStrategy bs = this.getBufferStrategy();

        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        this.screen.clear();

        int xscroll = this.player.x - (this.screen.width / 2);
        int yscroll = this.player.y - (this.screen.height / 2);

        this.screen.setOffset(xscroll, yscroll);

        this.level.render(this.screen);
        this.player.render(this.screen);

        Graphics g = bs.getDrawGraphics();
        this.screen.drawPixels(g, this.getWidth(), this.getHeight());
        this.drawDebug(g);
        g.dispose(); //Release the memory

        bs.show(); //Show the buffer

    }

    private void drawDebug(Graphics g) {
        if (showFPS) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Verdana", Font.PLAIN, 20));

            if (showFPS) {
                g.drawString("frameRate : " + this.currentFPS, 80, 40);
            }

			/*g.fillRect(Mouse.getX() - 10, Mouse.getY() - 10, 10, 10);
				if (Mouse.getButton() != -1)
					g.drawString("Button: " + Mouse.getButton(), 80, 80);*/

        }
    }
}
