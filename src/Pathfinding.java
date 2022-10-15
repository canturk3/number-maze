public class Pathfinding {
    private final Grid grid;
    private final Node[][] gridMap;

    private Node[] path;

    public Pathfinding(Grid grid){
        this.grid = grid;
        gridMap = grid.getGridMap();
    }

    public Node[] findPath(int startPosX,int startPosY,int targetPosX,int targetPosY) {
        Node startNode = gridMap[startPosX][startPosY];
        Node targetNode = gridMap[targetPosX][targetPosY];

        //Candidate nodes will contain neighbours of each current node.
        Node[] candidateNodes = new Node[gridMap.length * gridMap[0].length];

        //Discarded nodes will contain previous current nodes to not calculate them again.
        Node[] discardedNodes = new Node[gridMap.length * gridMap[0].length];

        addNode(candidateNodes,startNode);

        //Iterates over the grid until a path is found or all of the nodes are chosen as current node.
        while (nodeCount(candidateNodes) > 0)
        {
            Node currentNode = candidateNodes[0];
            for (int i = 1; i < candidateNodes.length; i++)
            {
                if(candidateNodes[i] != null){
                    //If there is a node in candidates that has less or the same f cost than current and it is closer to end node, it is chosen as current node.
                    if ((candidateNodes[i].fCost() < currentNode.fCost() || candidateNodes[i].fCost() == currentNode.fCost())
                            && candidateNodes[i].gethCost() < currentNode.gethCost()) {
                        currentNode = candidateNodes[i];
                        break;
                    }
                }
               else break;
            }

            //When a candidate is chosen as current node it is removed from candidates and added to discarded neighbours.
            removeNode(candidateNodes,currentNode);
            addNode(discardedNodes,currentNode);

            if (currentNode == targetNode) {
                createPath(startNode, targetNode);
                return path;
            }

            Node[] neighbours = grid.getNeighbours(currentNode);

            //Adds the walkable and not yet added neighbours of the current node to the candidate nodes
            //Also updates neighbour nodes when a faster path from the new current node is found.
            for(int i = 0; i < neighbours.length;i++){
                if(neighbours[i] != null){
                    Node neighbour = neighbours[i];

                    if (!neighbour.isWalkable() || containsNode(discardedNodes,neighbour))
                        continue;

                    //New movement cost of the neighbour is its distance to the start node plus distance from this node
                    int newMovementCostToNeighbour = currentNode.getgCost() + grid.getDistance(currentNode, neighbour);

                    //If neighbour is already in candidates but if it is closer to go from this current node it's fields are updated
                    if (newMovementCostToNeighbour < neighbour.getgCost() || !containsNode(candidateNodes,neighbour)) {
                        neighbour.setgCost(newMovementCostToNeighbour);
                        neighbour.sethCost(grid.getDistance(neighbour, targetNode));
                        neighbour.setParent(currentNode);

                        //Neighbour node is added to candidates if it is not already added.
                        if (!containsNode(candidateNodes,neighbour))
                            addNode(candidateNodes,neighbour);
                    }
                }
                else break;
            }
        }
        //Returns null when no possible path is found.
        return null;
    }

    //findPath method calls this when current node is the target node
    //Path is created by repeatedly taking the parent of the current node until start node is reached
    //(Parents were the nodes the path followed to reach the current node)
    private void createPath(Node startNode, Node endNode) {
        int pathLength = 0;
        Node currentNode = endNode;

        //Path length is counted to create the appropriate sized array.
        while (currentNode != startNode) {
            currentNode = currentNode.getParent();
            pathLength++;
        }
        path = new Node[pathLength];

        currentNode = endNode;
        while (currentNode != startNode) {
            addNode(path,currentNode);
            currentNode = currentNode.getParent();
        }

        //Path is reversed since it was created from end to start.
        reversePath(path);
    }

    public void reversePath(Node[] path)
    {
        Node temp;

        int start = 0;
        int end = path.length - 1;

        while (start < end)
        {
            temp = path[start];
            path[start] = path[end];
            path[end] = temp;

            start++;
            end--;
        }
    }

    //Finds the node in the array.
    private boolean containsNode(Node[] nodes, Node node){
        for(int i = 0; i < nodes.length;i++){
            if(nodes[i] == node){
                return true;
            }
        }
        return false;
    }

    //Adds the node to the array in the next empty spot.
    private void addNode(Node[] nodes, Node node){
        for(int i = 0; i < nodes.length;i++){
            if(nodes[i] == null){
                nodes[i] = node;
                break;
            }
        }
    }

    //Finds and removes a node then shifts the array.
    private void removeNode(Node[] nodes, Node node){
        for(int i = 0; i < nodes.length;i++){
            if(nodes[i] == node){
                nodes[i] = null;

                for(int j = i; j < nodes.length - 1;j++){
                    nodes[j] = nodes[j + 1];
                }
                nodes[nodes.length - 1] = null;
                break;
            }
        }
    }

    //Since arrays have static lengths to get the non empty element's size in the array this method is used
    private int nodeCount(Node[] nodes){
        int counter = 0;
        for(int i = 0; i < nodes.length;i++){
            if(nodes[i] == null){
                break;
            }
            counter++;
        }
        return counter;
    }
}
