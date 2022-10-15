import enigma.console.TextAttributes;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class MazeHandler {
    private RandomNumber[][] maze;
    private Color dotColorSeven,dotColorEight,dotColorNine;
    private boolean rainbowMode;

    public MazeHandler(){
        dotColorSeven = Color.pink;
        dotColorEight = Color.ORANGE;
        dotColorNine = Color.blue;

        rainbowMode = false;
    }

    public void setDotColorSeven(Color dotColorSeven) { this.dotColorSeven = dotColorSeven; }
    public void setDotColorEight(Color dotColorEight) { this.dotColorEight = dotColorEight; }
    public void setDotColorNine(Color dotColorNine) { this.dotColorNine = dotColorNine; }
    public void setRainbowMode(boolean rainbowMode) { this.rainbowMode = rainbowMode; }

    //Reads the contents of a file into a String array.
    private String[] read_input_maze(String path_name){
        try{
            File input_txt = new File(path_name);
            Scanner count_scanner = new Scanner(input_txt);
            //Lines are counted first to create array in the appropriate size.
            int lineCount = 0;
            while(count_scanner.hasNextLine()){
                count_scanner.nextLine();
                lineCount++;
            }
            count_scanner.close();

            String[] input_maze = new String[lineCount];
            Scanner read_scanner = new Scanner(input_txt);

            for(int i = 0; i < lineCount;i++){
                input_maze[i] = read_scanner.nextLine();
            }
            read_scanner.close();

            return input_maze;
        }
        catch (FileNotFoundException e){
            System.out.println("ERROR: Maze was not created. File with the given path was not found.");
            return null;
        }
    }

    //Controls if the maze is closed and 23 by 55.
    private boolean control_input_maze(String[] input_maze){
        if(!(input_maze[0].length() == 55 && input_maze.length == 23)){
            System.out.println("ERROR: Maze was not created. Maze dimensions must be 23x55. ");
            return false;
        }
        else{
            for(int i = 0; i < input_maze.length;i++){
                //If all the lines are the same length and  it is closed to the left and right with '#'.
                if(input_maze[i].length() == input_maze[0].length() && input_maze[i].charAt(0) == '#' && input_maze[i].charAt(input_maze[0].length() - 1) == '#'){
                    //First and last line of the maze must contain all walls.
                    if(i == 0 || i == input_maze.length - 1){
                        for(int j = 0; j < input_maze[0].length();j++){
                            if(input_maze[i].charAt(j) != '#'){
                                System.out.println("ERROR: Maze was not created. Upper and lower parts of the maze must be closed with '#' character. ");
                                return false;
                            }
                        }
                    }
                    //All the in between lines, maze must only contain '#' as walls and empty spaces.
                    else{
                        for(int j = 0; j < input_maze[0].length();j++){
                            if(input_maze[i].charAt(j) != '#' && input_maze[i].charAt(j) != ' '){
                                System.out.println("ERROR: Maze was not created.It must not contain any characters other than, \"#\" as walls and empty spaces.");
                                return false;
                            }
                        }
                    }
                }
                else{
                    System.out.println("ERROR: Maze was not created. Outer walls of the maze must be on the same column and must be closed. ");
                    return false;
                }
            }
            return true;
        }
    }

    //Reads maze from a file, controls if the maze is valid and creates the maze array.
    public RandomNumber[][] initialize_maze(String path_name){
        //It controls the file and maze validation with methods above.
        String[] input_maze = read_input_maze(path_name);
        if(input_maze != null){
            if(control_input_maze(input_maze)){
                //Creates the maze by putting corresponding characters inside 2D RandomNumber array.
                maze = new RandomNumber[input_maze.length][input_maze[0].length()];
                for(int i = 0; i < input_maze.length;i++){
                    for(int j = 0; j < input_maze[i].length();j++){
                        maze[i][j] = new RandomNumber();
                        if(input_maze[i].charAt(j) == '#'){
                            maze[i][j].setWall();
                        }
                        else{
                            maze[i][j].setEmptySpace();
                        }
                    }
                }
                //If maze is created but there are no empty spaces it is not returned.
                if(!mazeIsFull()){
                    return maze;
                }
                else{
                    System.out.println("ERROR: Maze is full. There are no empty spaces for characters.");
                    return null;
                }
            }
            else return null;
        }
        else return null;
    }

    private boolean mazeIsFull(){
        for(int i = 1; i < maze.length - 1;i++){
            for(int j = 1; j < maze[0].length - 1;j++){
                if(maze[i][j].getNumber() == 0){
                    return false;
                }
            }
        }
        return true;
    }

    // This method is used for adding random numbers in certain amount to the maze.
    public void addToMaze(int howMany, CircularQueue inputList, int py, int px) {
        Random rnd = new Random();

        // These are used to store the random coordinates of the random number.
        int rand1;
        int rand2;

        // Loop will continue until all numbers are placed or maze is full.
        while (howMany > 0 && !mazeIsFull()) {

            // Random location is determined here.
            rand1 = rnd.nextInt(maze.length - 1) + 1;
            rand2 = rnd.nextInt(maze[0].length - 1) + 1;

            // If maze location is empty and it is at least 2 spaces away from the player, random number will be placed.
            if ((rand1 > py + 2 || rand1 < py - 2  || rand2 > px + 2 || rand2 < px - 2) && maze[rand1][rand2].getNumber() == 0) {
                maze[rand1][rand2] = ((RandomNumber) inputList.dequeue());
                howMany--;

                // After the placement, a new random number is generated and put at the end of the input list.
                // With this, inputList will never be empty as long as the game continues.
                RandomNumber r = new RandomNumber();
                r.generateNumber();
                inputList.enqueue(r);
            }
        }
    }

    //Adds the player number to the maze and returns the coordinates which will show that it is the player to the game.
    public int[] addPlayer(int number){
        Random rnd = new Random();
        int[] coord = new int[2];

        if(!mazeIsFull()){
            //Loops until an empty space is found by random coordinates for the player to be placed.
            while(true){
                coord[0] = rnd.nextInt(maze.length - 1) + 1;
                coord[1] = rnd.nextInt(maze[0].length - 1) + 1;
                if (maze[coord[0]][coord[1]].getNumber() == 0) {
                    maze[coord[0]][coord[1]].setPlayer(number);
                    return coord;
                }
            }
        }
        else{
            System.out.println("ERROR: Maze is full.");
            return null;
        }
    }

    public void print_maze(enigma.console.Console cn){
        //Clean maze
        for(int i = 0; i < maze.length;i++){
            for(int j = 0; j < maze[i].length;j++){
                if(maze[i][j].getNumber() != 0 || (maze[i][j].getNumber() == 0 && maze[i][j].getColor() != Color.BLACK)){
                    cn.getTextWindow().output(j,i,' ');
                }
            }
        }

        //Print maze
        for(int i = 0; i < maze.length;i++){
            for(int j = 0; j < maze[i].length;j++){
                TextAttributes colorAtt;
                if(!rainbowMode){
                    colorAtt = new TextAttributes(maze[i][j].getColor());
                }
                else{
                    colorAtt = new TextAttributes(generateRandomColor());
                }
                //If the place is empty.
                if(maze[i][j].getNumber() == 0){
                    //If there is a path
                    if(maze[i][j].getColor() != Color.BLACK){
                        cn.getTextWindow().output(j,i,'.',colorAtt);
                    }
                    else{
                        cn.getTextWindow().output(j,i,' ',colorAtt);
                    }
                }

                //If the place is wall.
                else if(maze[i][j].getNumber() == -1){
                    cn.getTextWindow().output(j,i,'#');
                }

                //If the place ıs a number.
                else{
                    //Converts integer to char by first taking it's string value then printing the 0 index.
                    String numStr = String.valueOf(maze[i][j].getNumber());
                    cn.getTextWindow().output(j,i,numStr.charAt(0),colorAtt);
                }
            }
        }
    }

    public Color generateRandomColor() {
        Color color;
        Random rnd = new Random();

        int colorIndex = rnd.nextInt(7) + 1;

        color = switch (colorIndex) {
            case (1) -> Color.cyan;
            case (2) -> Color.green;
            case (3) -> Color.magenta;
            case (4) -> Color.orange;
            case (5) -> Color.pink;
            case (6) -> Color.red;
            default -> Color.yellow;
        };

        return color;
    }

    //Moves all of the random pieces on board and returns if the game over condition is met or player is turned to 1 and should be stopped moving to the game.
    public boolean[] random_move_all(enigma.console.Console cn,int px, int py, Backpack backpack, boolean invincibility, boolean zeroStun, boolean always9){
        Random rand = new Random();
        boolean game_over = false;
        boolean blockMovement = false;

        //Maze is copied and pieces to move are taken from there and moved in the main maze
        //since the function moves the pieces on board it shouldn't control the same piece twice.
        RandomNumber[][] copy_maze = new RandomNumber[maze.length][maze[0].length];
        for(int i = 0; i < copy_maze.length;i++){
            for(int j = 0; j < copy_maze[0].length;j++){
                RandomNumber number = new RandomNumber(maze[i][j].getScoreFactor(),maze[i][j].getNumber(),maze[i][j].getPoints(),maze[i][j].getColor());

                copy_maze[i][j] = number;
            }
        }

        //Random pieces can move to empty spaces or the player piece.
        for(int i = 1; i < copy_maze.length - 1;i++){
            for(int j = 1; j < copy_maze[0].length - 1;j++){
                //If the number is 4-5-6 and it is not the player's piece
                if((copy_maze[i][j].getNumber() == 4 || copy_maze[i][j].getNumber() == 5 || copy_maze[i][j].getNumber() == 6) && !(i == py && j == px)){
                    //Random direction is chosen for the piece to move one step towards.
                    int direction = rand.nextInt(4);

                    RandomNumber moving_number = new RandomNumber(copy_maze[i][j].getScoreFactor(),copy_maze[i][j].getNumber(),copy_maze[i][j].getPoints(),copy_maze[i][j].getColor());
                    //If the moving number's new position corresponds to the player's piece's position.
                    if((direction == 0 && (i == py && j - 1 == px)) || (direction == 1 && (i == py && j + 1 == px)) ||
                            (direction == 2 && (i - 1 == py && j == px)) || (direction == 3 && (i + 1 == py && j == px) )){

                        if(moving_number.getNumber() > maze[py][px].getNumber() && !invincibility){
                            game_over = true;
                            break;
                        }
                        else{
                            backpack.addToFirst(moving_number);
                            backpack.newDisplay();

                            if(maze[py][px].getNumber() != 9){
                                maze[py][px].setNumber(maze[py][px].getNumber() + 1);
                            }
                            else if (maze[py][px].getNumber() == 9 && !always9){
                                maze[py][px].setNumber(1);

                                if (!zeroStun) {
                                    blockMovement = true;
                                }
                            }
                            //The old space in the array is emptied.
                            maze[i][j] = new RandomNumber();
                            maze[i][j].setEmptySpace();
                        }
                    }

                    //Moved Left
                    else if(direction == 0 && maze[i][j - 1].getNumber() == 0){
                        maze[i][j] = new RandomNumber();
                        maze[i][j].setEmptySpace();

                        maze[i][j - 1] = moving_number;
                    }
                    //Moved Right
                    else if(direction == 1 && maze[i][j + 1].getNumber() == 0){
                        maze[i][j] = new RandomNumber();
                        maze[i][j].setEmptySpace();
                        maze[i][j + 1] = moving_number;
                    }
                    //Moved Up
                    else if(direction == 2 && maze[i - 1][j].getNumber() == 0){
                        maze[i][j] = new RandomNumber();
                        maze[i][j].setEmptySpace();
                        maze[i - 1][j] = moving_number;
                    }
                    //Moved Down
                    else if(direction == 3 && maze[i + 1][j].getNumber() == 0){
                        maze[i][j] = new RandomNumber();
                        maze[i][j].setEmptySpace();
                        maze[i + 1][j] = moving_number;
                    }
                }
            }
            if(game_over){
                break;
            }
        }
        //All the moving functions in the game prints maze after their procedures since they all can be set to different intervals
        //one print maze wouldn't be accurate
        print_maze(cn);

        boolean[] gameoverAndBlock = new boolean[2];
        gameoverAndBlock[0] = blockMovement;
        gameoverAndBlock[1] = game_over;

        return gameoverAndBlock;
    }

    public boolean[] pathfinders(enigma.console.Console cn,int px, int py,Backpack backpack, boolean invincibility, boolean zeroStun, boolean always9, int targetX, int targetY){
        boolean game_over = false;
        boolean blockMovement = false;

        //Maps the maze with walkable and unwalkable flags and nodes that can connect to each other to form a path.
        Grid grid = new Grid(maze,py,px);

        //Finds the shortest path from current node to the target node using A* algorithm.
        Pathfinding pathfinding = new Pathfinding(grid);

        //Maze is copied and pieces to move are taken from there and moved in the main maze
        //since the function moves the pieces on board it shouldn't control the same piece twice.
        RandomNumber[][] copy_maze = new RandomNumber[maze.length][maze[0].length];
        for(int i = 0; i < copy_maze.length;i++){
            for(int j = 0; j < copy_maze[0].length;j++){
                RandomNumber number = new RandomNumber(maze[i][j].getScoreFactor(),maze[i][j].getNumber(),maze[i][j].getPoints(),maze[i][j].getColor());

                copy_maze[i][j] = number;
            }
        }
        cleanPaths();

        for(int i = 0; i < copy_maze.length;i++){
            for(int j = 0; j < copy_maze[0].length;j++){
                if((copy_maze[i][j].getNumber() == 7 || copy_maze[i][j].getNumber() == 8 || copy_maze[i][j].getNumber() == 9)  && !(i == py && j == px)){
                    //Path is created and piece is moved one step in the direction of the path.
                    //Rest of the path will print dots on the maze to represent the path that would be followed.
                    Node[] path = pathfinding.findPath(i,j,targetX,targetY);

                    if(path != null && path.length != 0 ){
                        RandomNumber moving_number = new RandomNumber(copy_maze[i][j].getScoreFactor(),copy_maze[i][j].getNumber(),copy_maze[i][j].getPoints(),copy_maze[i][j].getColor());
                        //Piece will move one step in the path.
                        int x = path[0].getGridX();
                        int y = path[0].getGridY();

                        //If the piece is not stepping on the player.
                        if(y != px || x != py){
                            //First place is emptied
                            maze[i][j] = new RandomNumber();
                            maze[i][j].setEmptySpace();

                            maze[x][y] = moving_number;
                        }
                        else{
                            if(moving_number.getNumber() > maze[x][y].getNumber() && !invincibility){
                                game_over = true;
                                break;
                            }
                            else{
                                backpack.addToFirst(moving_number);
                                backpack.newDisplay();

                                //Previous place is emptied
                                maze[i][j] = new RandomNumber();
                                maze[i][j].setEmptySpace();

                                if(maze[py][px].getNumber() != 9){
                                    maze[x][y].setNumber(maze[x][y].getNumber() + 1);
                                }
                                else if (maze[py][px].getNumber() == 9 && !always9){
                                    maze[x][y].setNumber(1);

                                    if (!zeroStun) {
                                        blockMovement = true;
                                    }
                                }
                            }
                        }
                        //Determines where the dots will be placed by changing the color field of spaces.
                        //Loops start from 1 instead of 0 since at 0 the piece will move
                        for(int k = 1; k < path.length;k++){
                            int gridX = path[k].getGridX();
                            int gridY = path[k].getGridY();

                            //The path will be printed until another path is on the way to avoid confusion
                            //The path that encountered other one can be stopped since they will follow the same path after their paths converge.
                            if(maze[gridX][gridY].getColor() != Color.BLACK){
                                break;
                            }
                            //All numbers are given different path colors.
                            else if(moving_number.getNumber() == 7){
                                maze[gridX][gridY].setColor(dotColorSeven);
                            }
                            else if(moving_number.getNumber() == 8){
                                maze[gridX][gridY].setColor(dotColorEight);
                            }
                            else{
                                maze[gridX][gridY].setColor(dotColorNine);
                            }
                        }
                    }
                }
            }
            if(game_over){
                break;
            }
        }
        //After determining the paths and movement of single cells, maze is printed again.
        //Since every movement interval is different and can change, all piece movements have their own prints.
        print_maze(cn);

        boolean[] gameoverAndBlock = new boolean[2];
        gameoverAndBlock[0] = blockMovement;
        gameoverAndBlock[1] = game_over;

        return gameoverAndBlock;
    }

    //Sets all the empty spaces that had contained paths to normal empty spaces.
    private void cleanPaths(){
        for(int i = 0; i < maze.length;i ++){
            for(int j = 0; j < maze[0].length;j++){
                if(maze[i][j].getNumber() == 0 && maze[i][j].getColor() != Color.BLACK){
                    maze[i][j].setColor(Color.BLACK);
                }
            }
        }
    }
}
