import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

public class Game {
    private final RandomNumber[][] maze;   //Walls are attained -1 and empty spaces 0 in the maze array.
    private final MazeHandler mazeHandler;
    private final CircularQueue inputList;
    private final Backpack backpack;
    private final enigma.console.Console cn;

    Timer timer;
    TimerTask movePlayer,addAndDisplay,displayTime,moveRandom,movePathfinders,invisibleTargetCreate;

    private int time, score;
    private int px,py; //Player positions
    private int pathfindTargetX,pathfindTargetY;

    private int addNumberSpeed,pathfinderSpeed,randomMoverSpeed,playerSpeed,playerNumber,stunDuration,scoreMultiplier;

    private boolean game_over,restart,canMove,always9,invincibility,zeroStun,invisibility;

    private int mousepr;          // mouse pressed
    private int mousex, mousey;   // mouse text coords.

    private int arrowKeyIsPr; //To control if key is pressed
    private int actionKeyIsPr; //To control if key is pressed

    private int pressArrKey, pressActKey;

    public Game(String maze_pathname){
        cn = Menu.getCn();

        inputList = new CircularQueue(10);
        backpack = new Backpack(cn);
        mazeHandler = new MazeHandler();
        timer = new Timer();
        maze = mazeHandler.initialize_maze(maze_pathname);

        addNumberSpeed = 5000;    pathfinderSpeed = 500;
        randomMoverSpeed = 500;   playerSpeed = 500;

        playerNumber = 5;  stunDuration = 4000;
        time = 0;     score = 0;    scoreMultiplier = 1;

        canMove = true;        restart = false;
        game_over = false;     always9 = false;
        invincibility = false; zeroStun = false;
        invisibility = false;
    }

    public void setAddNumberSpeed(int addNumberSpeed) {
        this.addNumberSpeed = addNumberSpeed;
    }
    public void setInvisiblity(boolean value) {
        invisibility = value;
    }
    public void setPathfinderSpeed(int pathfinderSpeed) {
        this.pathfinderSpeed = pathfinderSpeed;
    }
    public void setStunDuration(int stunDuration) {
        this.stunDuration = stunDuration;
    }
    public void setZeroStun(boolean value) {
        this.zeroStun = value;
    }
    public void setRandomMoverSpeed(int randomMoverSpeed) {
        this.randomMoverSpeed = randomMoverSpeed;
    }
    public void setPlayerSpeed(int playerSpeed) {
        this.playerSpeed = playerSpeed;
    }
    public void setScoreMultiplier(int scoreMultiplier) {
        this.scoreMultiplier = scoreMultiplier;
    }
    public void setPlayerNumber(int playerNumber) { this.playerNumber = playerNumber; }
    public void setAlways9(boolean value) {
        always9 = value;
    }
    public void setInvincibility(boolean value) {
        this.invincibility = value;
    }

    public RandomNumber[][] getMaze(){return maze;}
    public MazeHandler getMazeHandler() {
        return mazeHandler;
    }
    public boolean isRestart() {
        return restart;
    }

    public void increaseScore(int Score) {
        score += Score * scoreMultiplier;
    }

    // Method for increasing the player number when matching occurs in backpacks.
    private void increasePlayerNumber() {
        if (maze[py][px].getNumber() != 9) {
            maze[py][px].setNumber(maze[py][px].getNumber() + 1);
        }
        else if (maze[py][px].getNumber() == 9 && !always9) {
            maze[py][px].setNumber(1);

            if (!zeroStun) {
                blockMovement();
            }
        }
    }

    private void createInputList(){
        for (int i = 0; i < 10; i++) {
            RandomNumber r = new RandomNumber();
            r.generateNumber();
            inputList.enqueue(r);
        }
    }

    // This method is used for displaying the score.
    private void displayScore() {
        printToScreenAfterMaze(Integer.toString(score), 44, 21);
    }

    // This method is used for displaying first 10 elements of the input list.
    private void displayQueue() {
        int size = inputList.size();
        int count = 0;

        // This loop traverses the inputlist, prints the first 10 element and
        // brings the inputList back to its original order.
        for (int i = 0; i < size; i++) {
            if (count < 10) {
                String output = ((RandomNumber) inputList.peek()).getNumber() + " ";
                for(int j = 0;j < output.length();j++){
                    cn.getTextWindow().output(maze.length + 37 + i + j, 2, output.charAt(j));
                }
                count++;
            }
            // inputList is shuffled even if 10 elements are already displayed.
            // This is done in order to not disrupt the order of the elements inside the inputList.
            inputList.enqueue(inputList.dequeue());
        }
    }

    // This method is used to print static informations that are on the right side of the maze.
    private void printToScreenAfterMaze(String output, int x, int y) {
        for (int i = 0; i < output.length(); i++) {
            cn.getTextWindow().output(maze.length + x + i, y, output.charAt(i));
        }
    }

    private void printStaticInputScreen(){
        // Static informations that are on the right side of the screen.
        printToScreenAfterMaze("Input", 37, 0);
        printToScreenAfterMaze("<<<<<<<<<<", 37, 1);
        printToScreenAfterMaze("<<<<<<<<<<", 37, 3);
        printToScreenAfterMaze("Score:", 37, 21);
        printToScreenAfterMaze("Time:", 37, 22);
    }

    private void clearBackpack(){
        for(int i = 0; i < 10;i++){
            for(int j = 0; j < 4;j++){
                cn.getTextWindow().output(60 + i,j,' ');
            }
        }
    }

    private void gameOver(){
        //If R key isn't pressed and game ended waits for user to click either restart or main menu
        //If R key is pressed program returns to the loop in Main.
        if(!restart){
            TextMouseListener tmlis = new TextMouseListener() {
                public void mouseClicked(TextMouseEvent arg0) {
                }

                public void mousePressed(TextMouseEvent arg0) {
                    if (mousepr == 0) {
                        mousepr = 1;
                        mousex = arg0.getX();
                        mousey = arg0.getY();
                    }
                }

                public void mouseReleased(TextMouseEvent arg0) {
                }
            };
            cn.getTextWindow().addTextMouseListener(tmlis);

            clearBackpack();
            printToScreenAfterMaze("Game Over",39,1);
            printToScreenAfterMaze("Restart",40,3);
            printToScreenAfterMaze("Main Menu",39,4);

            while(true) {
                if (mousepr == 1) {  // if mouse button pressed
                    //Restarts the game without returning to main menu
                    if((mousex >= 61 && mousex <= 67) && (mousey == 3)){
                        mousepr = 0;
                        restart = true;
                        printToScreenAfterMaze("         ",39,1);
                        printToScreenAfterMaze("       ",40,3);
                        printToScreenAfterMaze("         ",39,4);
                        break;
                    }
                    //Returns to main menu
                    else if((mousex >= 60 && mousex <= 68) && (mousey == 4)){
                        mousepr = 0;
                        break;
                    }
                    mousepr = 0;     // last action
                }
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            printToScreenAfterMaze("         ",39,1);
            printToScreenAfterMaze("       ",40,3);
            printToScreenAfterMaze("         ",39,4);
        }
    }

    // This method blocks the movement of the player for determined seconds when its number becomes 1 from 9.
    private void blockMovement() {
        Timer playerMovement = new Timer();
        canMove = false; // Moving is blocked.

        // Task that allows the player to move again.
        TimerTask playerMove = new TimerTask() {
            public void run() {
                canMove = true;
                playerMovement.cancel(); // Timer is cancelled when player is free to move again.
            }
        };

        // Task runs after determined seconds to allow the player to move again. (Default is 4 sec.)
        playerMovement.scheduleAtFixedRate(playerMove, stunDuration, stunDuration);
    }

    private void playerMovement(){
        if(arrowKeyIsPr==1) {
            RandomNumber pressed_number = null;

            if (canMove || maze[py][px].getNumber() != 1) {
                //If arrow keys are pressed and there is no wall in the direction of movement.
                if(pressArrKey==KeyEvent.VK_LEFT && maze[py][px - 1].getNumber() != -1){
                    pressed_number = maze[py][px - 1];
                }

                else if(pressArrKey==KeyEvent.VK_RIGHT && maze[py][px + 1].getNumber() != -1){
                    pressed_number = maze[py][px + 1];
                }

                else if(pressArrKey==KeyEvent.VK_UP && maze[py - 1][px].getNumber() != -1){
                    pressed_number = maze[py - 1][px];
                }

                else if (pressArrKey==KeyEvent.VK_DOWN && maze[py + 1][px].getNumber() != -1){
                    pressed_number = maze[py + 1][px];
                }
            }

            if(pressed_number != null){
                if(pressed_number.getNumber() > maze[py][px].getNumber() && !invincibility){
                    game_over = true;
                }
                else{
                    RandomNumber player_number = new RandomNumber(maze[py][px].getScoreFactor(),maze[py][px].getNumber(),maze[py][px].getPoints(),maze[py][px].getColor());

                    //If it was not an empty space, increase player's number
                    if(pressed_number.getNumber() != 0){

                        backpack.addToFirst(pressed_number);
                        backpack.newDisplay();

                        if(player_number.getNumber() != 9){
                            player_number.setNumber(player_number.getNumber() + 1);
                        }
                        else if (player_number.getNumber() == 9 && !always9){
                            player_number.setNumber(1);

                            if (!zeroStun) {
                                blockMovement();
                            }
                        }
                    }
                    maze[py][px] = new RandomNumber();
                    maze[py][px].setEmptySpace();


                    if(pressArrKey==KeyEvent.VK_LEFT) {
                        px--;
                    }
                    else if(pressArrKey==KeyEvent.VK_RIGHT ){
                        px++;
                    }
                    else if(pressArrKey==KeyEvent.VK_UP ){
                        py--;
                    }
                    else{
                        py++;
                    }

                    maze[py][px] = player_number;
                }
            }
            arrowKeyIsPr=0;    // last action

            int score = backpack.comparison();

            if(score != -1){
                if(maze[py][px].getNumber() != -1 && !canMove){
                    canMove = true;
                }
                increaseScore(score);

                if (!always9) {
                    increasePlayerNumber();
                }
            }
            backpack.newDisplay();
        }

        if(backpack.getFirstBackpack().isFull() && backpack.getSecondBackpack().isFull()){
            game_over = true;
        }

        mazeHandler.print_maze(cn);

        displayScore();
    }

    private void createAndScheduleTasks(Timer timer){
        addAndDisplay = new TimerTask() {
            public void run() {
                mazeHandler.addToMaze(1,inputList,py,px);
                displayQueue();
            }
        };

        // Task for displaying how much time has passed since the game started.
        displayTime = new TimerTask() {
            public void run() {
                time++;
                printToScreenAfterMaze(Integer.toString(time), 43, 22);
            }
        };

        moveRandom = new TimerTask() {
            public void run() {
                boolean[] gameOverAndBlockMov = mazeHandler.random_move_all(cn,px,py,backpack,invincibility,zeroStun,always9);
                game_over = gameOverAndBlockMov[1];
                boolean blockMovement = gameOverAndBlockMov[0];
                if(blockMovement){
                    blockMovement();
                }
            }
        };

        movePlayer = new TimerTask() {
            public void run() {
                playerMovement();
            }
        };

        timer.scheduleAtFixedRate(addAndDisplay, addNumberSpeed, addNumberSpeed); // Adds a random number to the maze
        // every determined seconds. (Default is 5 sec.)
        timer.scheduleAtFixedRate(displayTime, 1000, 1000);  // Displays time every 1 sec.

        timer.scheduleAtFixedRate(movePlayer,0,playerSpeed); // Moves player every determined seconds. (Default
        // is 0.5 sec.)
        timer.scheduleAtFixedRate(moveRandom,0,randomMoverSpeed); // Moves numbers in random directions every
        // determined seconds. (Default is 0.5 sec.)

        //If invisibility is open, a target is created every interval for the pathfinder to go to.
        if(invisibility){
            invisibleTargetCreate = new TimerTask() {
                public void run() {
                    int[] invisibleTargetCoord = mazeHandler.addPlayer(0);
                    pathfindTargetX = invisibleTargetCoord[0];
                    pathfindTargetY = invisibleTargetCoord[1];
                }
            };
            timer.scheduleAtFixedRate(invisibleTargetCreate,0,4000); // Moves numbers in random directions every determined seconds. (Default is 0.5 sec.)
        }

        movePathfinders = new TimerTask() {
            public void run() {
                if(!invisibility){
                    pathfindTargetX = py;
                    pathfindTargetY = px;
                }
                boolean[] gameOverAndBlockMov = mazeHandler.pathfinders(cn,px,py,backpack,invincibility,zeroStun,always9,pathfindTargetX,pathfindTargetY);
                game_over = gameOverAndBlockMov[1];
                boolean blockMovement = gameOverAndBlockMov[0];
                if(blockMovement){
                    blockMovement();
                }
            }
        };
        timer.scheduleAtFixedRate(movePathfinders,0,pathfinderSpeed); // Moves 7/8/9 every determined seconds. (Default is 0.5 sec.)
    }

    public void start(){
        if(maze != null){
            //Since moving player and doing actions such as backpack operations work at different delay intervals,listeners are separated
            KeyListener movePlayerListener = new KeyListener() {
                public void keyTyped(KeyEvent e) {
                }

                public void keyPressed(KeyEvent e) {
                    if (arrowKeyIsPr == 0) {
                        arrowKeyIsPr = 1;
                        pressArrKey = e.getKeyCode();
                    }
                }

                public void keyReleased(KeyEvent e) {
                }
            };
            cn.getTextWindow().addKeyListener(movePlayerListener);

            KeyListener keyActionsListener = new KeyListener() {
                public void keyTyped(KeyEvent e) {
                }

                public void keyPressed(KeyEvent e) {
                    if (actionKeyIsPr == 0) {
                        actionKeyIsPr = 1;
                        pressActKey = e.getKeyCode();
                    }
                }

                public void keyReleased(KeyEvent e) {
                }
            };
            cn.getTextWindow().addKeyListener(keyActionsListener);

            //Player's number is the number which is under the cursor at the start of the game
            int[] coord = mazeHandler.addPlayer(playerNumber);
            if(coord != null){
                py = coord[0];
                px = coord[1];

                pathfindTargetX = py;
                pathfindTargetY = px;
            }

            //Add random numbers
            createInputList();
            mazeHandler.addToMaze(25,inputList,py,px);

            mazeHandler.pathfinders(cn,px,py,backpack,invincibility,zeroStun,always9,pathfindTargetX,pathfindTargetY);

            backpack.newDisplay();
            printStaticInputScreen();

            displayScore(); // Displaying score as 0 at the beginning of the game.
            displayQueue(); // Displaying queue at the beginning of the game.

            gameLoop();
        }
    }

    private void gameLoop(){
        createAndScheduleTasks(timer);

        boolean paused = false;
        while(true){
            if(actionKeyIsPr == 1){
                //Ends the game
                if(pressActKey == KeyEvent.VK_S){
                    game_over = true;
                }
                //Ends and starts a new game
                else if(pressActKey == KeyEvent.VK_R){
                    game_over = true;
                    restart = true;
                }
                //Backpack operations
                else if(pressActKey==KeyEvent.VK_Q) {
                    backpack.transferToFirst();
                }
                else if(pressActKey==KeyEvent.VK_W) {
                    backpack.addToSecond();
                }
                //Pauses and resumes the game
                else if(pressActKey==KeyEvent.VK_P){
                    //PAUSE
                    if(!paused){
                        printToScreenAfterMaze("PAUSED", 63 - maze.length,5);

                        timer.cancel();
                        timer.purge();
                        paused = true;
                    }
                    //RESUME
                    else{
                        printToScreenAfterMaze("      ", 63 - maze.length,5);

                        //To resume cancelled tasks are created and scheduled again.
                        timer = new Timer();
                        createAndScheduleTasks(timer);
                        paused = false;
                    }
                }
                actionKeyIsPr = 0;
            }
            if(game_over){
                timer.cancel();
                timer.purge();
                gameOver();
                break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}