public class Node {
    private boolean walkable;

    //It's position in the 2D grid
    private int gridX;
    private int gridY;

    //Distance to start
    private int gCost;
    //Distance to end
    private int hCost;

    //Stores the node,the path came from
    private Node parent;

    public Node(boolean walkable, int gridX, int gridY) {
        this.walkable = walkable;
        this.gridX = gridX;
        this.gridY = gridY;
    }

    public int fCost() {
        return gCost + hCost;
    }

    public boolean isWalkable(){ return walkable; }
    public int getGridX() { return gridX; }
    public int getGridY() { return gridY; }
    public int getgCost() { return gCost; }
    public int gethCost() { return hCost; }
    public Node getParent() { return parent; }

    public void setWalkable(boolean walkable) { this.walkable = walkable; }
    public void setGridX(int gridX) { this.gridX = gridX; }
    public void setGridY(int gridY) { this.gridY = gridY; }
    public void setgCost(int gCost) { this.gCost = gCost; }
    public void sethCost(int hCost) { this.hCost = hCost; }
    public void setParent(Node parent) { this.parent = parent; }
}
