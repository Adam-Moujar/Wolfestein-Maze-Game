public class Player
{
    public double x,y; // Holds the position of the player
    public double dirX, dirY; // Holds the direction the player is facing
    public double planeX, planeY; // Holds the direction the camera plane is in

    public double movespeed; // How fast the player moves
    public double rotspeed; // How fast the camera rotates

    /**
     * Player Constructor
     * The fields are initialized to somewhat arbitrary values
     * You can play around them if you want and see what happens
     * However the camera plane (planeX and planeY) have to be parallel to dirX and dirY
     * Otherwise weird things happen with the graphics
     */
    public Player()
    {
        x = 1;
        y = 1;

        dirX = -1;
        dirY = 0;

        planeX = 0;
        planeY = 0.66;

        movespeed = 1;
        rotspeed = 0.4;
    }
}
