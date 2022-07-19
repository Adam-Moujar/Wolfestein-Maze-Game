import java.util.*;

public class Map {

    //mazeWidth and mazeHeight have to be odd, otherwise the algorithm doesn't work
    public int mazeWidth;    // width of maze  - initialised at constructor
    public int mazeHeight;   // height of maze - initialised at constructor
    public ArrayList<ArrayList<Integer>> map; // holds the actual map of the maze

    /**
    Map Constructor
    initialises the width, height and sets the whole map to be a wall
    then generates the maze
     */
    public Map() {
        mazeWidth = 11;
        mazeHeight = 11;
        map = new ArrayList<>();

        initMap();
        generateMaze(1, 1);
        set(mazeWidth - 2, mazeHeight - 2, 2);
    }

    /**
    Method to set all the steps in the map to be a wall
     */
    private void initMap() {
        for (int y = 0; y < mazeHeight; y++) {
            ArrayList<Integer> temp = new ArrayList<>();
            for (int x = 0; x < mazeWidth; x++) {
                temp.add(1);
            }
            map.add(temp);
        }
    }

    /**
     Generates the actual maze
     Stumbles through the maze randomly and empties every grid it visits
     */
    private void generateMaze(int x, int y) {
        set(x, y, 0);
        Integer[] dirs = new Integer[]{0, 1, 2, 3};
        shuffle(dirs);
        for (int i = 0; i < 4; ++i) {
            int dx = 0, dy = 0;
            switch (dirs[i]) {
                case 0 -> dy = -1;
                case 2 -> dy = 1;
                case 1 -> dx = 1;
                case 3 -> dx = -1;
            }
            int x2 = x + (dx << 1);
            int y2 = y + (dy << 1);
            if (inBounds(x2, y2)) {
                if (get(x2, y2) == 1) {
                    set(x2 - dx, y2 - dy, 0);
                    generateMaze(x2, y2);
                }
            }
        }
    }

    /**
     * Method for printing the map
     * Used for debugging purposes
     */
    private void printMap() {
        for (int y = 0; y < mazeHeight; y++) {
            for (int x = 0; x < mazeWidth; x++) {
                System.out.print(map.get(y).get(x));
            }
            System.out.println();
        }
    }

    /**
     * Changes a specific step of the map to the specified int
     * @param x     - X coordinate to change
     * @param y     - Y coordinate to change
     * @param toSet - What it should be changed to
     */
    private void set(int x, int y, int toSet) {
        ArrayList<Integer> temp = map.get(y);
        temp.set(x, toSet);
        map.set(y, temp);
    }

    /**
     * Returns a specific step of the map
     * @param x - X coordinate of step
     * @param y - Y coordinate of step
     * @return  - Returns the int value at that step
     */
    public int get(int x, int y) {
        return map.get(y).get(x);
    }

    /**
     * Randomly shuffles the given array
     * @param arr - Array to shuffle
     */
    private void shuffle(Integer[] arr) {
        List<Integer> list = Arrays.asList(arr);
        Collections.shuffle(list);
        list.toArray(arr);
    }

    /**
     * Returns if the given x and y are in the map width and height boundaries
     * @param x
     * @param y
     * @return - Returns false if the coordinates are outside the
     *           given width and height of the map, true otherwise
     */
    public boolean inBounds(int x, int y) {
        if (x <= 0 || x > mazeWidth - 2) {
            return false;
        }
        return y > 0 && y <= mazeHeight - 2;
    }
}