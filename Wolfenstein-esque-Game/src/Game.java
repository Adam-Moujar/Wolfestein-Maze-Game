import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

public class Game extends Canvas
{
    //region Window fields
    private final int width = 640;  // The width of the window
    private final int height = 480; // The height of the window
    private final JFrame frame;     // The frame that the canvas is added on to
    //endregion
    //region Game fields
    private final Map map;          // The map of the maze
    private final Player player;    // The player
    private final boolean running;  // Whether the game is running or not
    //endregion

    /**
     * Game Constructor
     * Inits the objects necessary
     * Sets the game to running
     * Runs the necessary functions for player input
     * Runs the gameloop
     */
    public Game()
    {
        // Initialising the necessary fields
        frame = new JFrame();
        map = new Map();
        player = new Player();
        //end

        running = true;         // Sets game to running

        initWindow();           // Creates the window
        setFocusable(true);     // Necessary to listen to player input
        requestFocus();         // ^^
        playerMove();           // Listens for player input
        gameloop();             // Runs the game
    }

    /**
     * Creates the window
     */
    public void initWindow()
    {
        setPreferredSize(new Dimension(width,height));
        String title = "Wolfestein-esque Game";
        frame.setTitle(title);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * Runs the main Loop
     * Renders every frame
     */
    public void gameloop()
    {
        while(running)
        {
            render();
        }
    }

    /**
     * Renders every frame
     * Uses the DDA algorithm
     * credit to : https://lodev.org/cgtutor/raycasting.html
     */
    public void render()
    {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null)
        {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.clearRect(0,0,width,height);
        for (int x = 0; x < width; x++)
        {
            double cameraX = 2 * x / ((double) width) - 1;
            double rayDirX = player.dirX + player.planeX * cameraX;
            double rayDirY = player.dirY + player.planeY * cameraX;

            int mapX = (int) (player.x);
            int mapY = (int) (player.y);


            double sideDistX, sideDistY;

            double deltaDistX = (rayDirX ==0) ? (Double.POSITIVE_INFINITY) : Math.abs(1/rayDirX);
            double deltaDistY = (rayDirY ==0) ? (Double.POSITIVE_INFINITY) : Math.abs(1/rayDirY);
            double perpWalldist;

            int stepX;
            int stepY;

            int hit = 0;
            int side = 0;

            if(rayDirX < 0)
            {
                stepX = -1;
                sideDistX = (player.x - mapX) * deltaDistX;
            }
            else
            {
                stepX = 1;
                sideDistX = (mapX + 1.0 - player.x) * deltaDistX;
            }

            if(rayDirY < 0)
            {
                stepY = -1;
                sideDistY = (player.y - mapY) * deltaDistY;
            }
            else
            {
                stepY = 1;
                sideDistY = (mapY + 1.0 - player.y) * deltaDistY;
            }

            while(hit==0)
            {
                if (sideDistX < sideDistY)
                {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                }
                else
                {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
                if( map.get(mapX,mapY) > 0)
                {
                    hit =1;
                }

            }

            if(side == 0)
            {
                perpWalldist = (sideDistX - deltaDistX);
            }
            else
            {
                perpWalldist = (sideDistY - deltaDistY);
            }

            int lineHeight = (int) (height / perpWalldist);

            int drawStart = -lineHeight / 2 + height /2;
            if(drawStart < 0)
            {
                drawStart = 0;
            }
            int drawEnd = lineHeight /2 + height /2;
            if(drawEnd >= height)
            {
                drawEnd = height - 1;
            }
            Color color = Color.RED;
            if(side == 1)
            {
                color = new Color(color.getRGB() /2);
            }
            if(map.get(mapX,mapY) == 2)
            {
                color = Color.GREEN;
            }

            g.setColor(color);
            g.drawLine(x,drawStart,x,drawEnd);
        }
        g.dispose();
        bs.show();
    }

    /**
     * Listens for player input
     * Checks if the movement is possible
     * Moves
     */
    public void playerMove()
    {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keycode = e.getKeyCode();
                if(keycode == KeyEvent.VK_W) {
                    int changeX = (int) (player.x + player.dirX * player.movespeed);
                    int changeY = (int) (player.y + player.dirY * player.movespeed);
                    if(map.inBounds(changeX,(int)player.y))
                    {
                        player.x += player.dirX * player.movespeed;
                    }
                    if(map.inBounds((int)player.x,changeY))
                    {
                        player.y += player.dirY * player.movespeed;
                    }
                }
                if(keycode == KeyEvent.VK_D)
                {
                    double oldDirX = player.dirX;
                    player.dirX = player.dirX * Math.cos(-player.rotspeed) - player.dirY * Math.sin(-player.rotspeed);
                    player.dirY = oldDirX * Math.sin(-player.rotspeed) + player.dirY * Math.cos(-player.rotspeed);
                    double oldPlaneX = player.planeX;
                    player.planeX = player.planeX * Math.cos(-player.rotspeed) - player.planeY * Math.sin(-player.rotspeed);
                    player.planeY = oldPlaneX * Math.sin(-player.rotspeed) + player.planeY * Math.cos(-player.rotspeed);
                }
                if(keycode == KeyEvent.VK_A)
                {
                    double oldDirX = player.dirX;
                    player.dirX = player.dirX * Math.cos(player.rotspeed) - player.dirY * Math.sin(player.rotspeed);
                    player.dirY = oldDirX * Math.sin(player.rotspeed) + player.dirY * Math.cos(player.rotspeed);
                    double oldPlaneX = player.planeX;
                    player.planeX = player.planeX * Math.cos(player.rotspeed) - player.planeY * Math.sin(player.rotspeed);
                    player.planeY = oldPlaneX * Math.sin(player.rotspeed) + player.planeY * Math.cos(player.rotspeed);
                }
            }
        });
    }
}
