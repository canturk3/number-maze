public class Grid {

    public Node[][] gridMap;

    private final RandomNumber[][] maze;
    //For the player's piece position
    private final int py, px;

    public Grid(RandomNumber[][] maze, int py , int px){
        this.maze = maze;
        this.py = py;
        this.px = px;

        CreateGridMap();
    }

    //Grid is created as the same dimensions as maze but in node elements.
    private void CreateGridMap()
    {
        gridMap = new Node[maze.length][maze[0].length];

        for (int i = 0; i < gridMap.length; i++)
        {
            for (int j = 0; j < gridMap[0].length; j++)
            {
                if(maze[i][j].getNumber() == 0 || maze[i][j] == maze[py][px]){
                    gridMap[i][j] = new Node(true, i, j);
                }
                else{
                    gridMap[i][j] = new Node(false, i, j);
                }
            }
        }
    }

    public Node[] getNeighbours(Node node)
    {
        Node[] neighbours = new Node[4];
        int neighbours_counter = 0;

        for (int x = -1; x <= 1; x++)
        {
            for (int y = -1; y <= 1; y++)
            {
                //Skips diagonal neighbours
                if (Math.abs(x) == Math.abs(y))
                    continue;

                int neighbourGridX = node.getGridX() + x;
                int neighbourGridY = node.getGridY() + y;

                //Controls if the dimensions are inside the grid.
                if (neighbourGridX >= 0 && neighbourGridX < gridMap.length && neighbourGridY >= 0 && neighbourGridY < gridMap[0].length) {
                    neighbours[neighbours_counter] = gridMap[neighbourGridX][neighbourGridY];
                    neighbours_counter++;
                }
            }
        }
        return neighbours;
    }

    //Horizontal and vertical distances are 10, diagonal distances are 14 between neighbours.
    public int getDistance(Node nodeA, Node nodeB) {
        int dstX = Math.abs(nodeA.getGridX() - nodeB.getGridX());
        int dstY = Math.abs(nodeA.getGridY() - nodeB.getGridY());

        if (dstX > dstY) {
            return 14 * dstY + 10 * (dstX - dstY);
        }
        return 14 * dstX + 10 * (dstY - dstX);
    }

    public Node[][] getGridMap(){
        return gridMap;
    }
}
