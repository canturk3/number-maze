import enigma.console.Console;
import enigma.console.TextAttributes;
import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;

import java.awt.*;

public class Menu {
    private static final enigma.console.Console cn = Enigma.getConsole("Number Maze", 80,27, 25);

    private Game game;

    private int mousepr;          // mouse pressed
    private int mousex, mousey;   // mouse text coords.

    private int previous_diff_choice, difficultyChoice, scoreMultiplier, optionChoice;
    private int addSpeed, pathfinderSpeed, randomMoveSpeed, playerSpeed, playerNumber, stunDuration;

    private boolean invincibility, invisibility, always9, zeroStun,rainbowMode;

    //Used by color customization
    private final Color[] colorSpectrum;
    private int staticColorIndex, randomColorIndex, pathFinderColorIndex, playerColorIndex, dotColorSevenIndex, dotColorEightIndex, dotColorNineIndex;

    //These temp values allow the player to change options and only apply it when apply button is pressed.
    private int tempAddSpeed, tempPathfSpeed, tempRandomSpeed, tempPlayerSpeed, tempPlayerNumber, tempStunDuration, tempScoreMultiplier;
    private int tempStaticColorIndex, tempRandomColorIndex, tempPathFinderColorIndex, tempPlayerColorIndex, tempDotColorSevenIndex, tempDotColorEightIndex, tempDotColorNineIndex;
    private boolean tempInvincibility, tempInvisibility, tempAlways9, tempZeroStun, tempRainbowMode;

    public Menu(){
        addSpeed = 5000;   pathfinderSpeed = 500; randomMoveSpeed = 500;
        playerSpeed = 500; playerNumber = 5;      stunDuration = 4000;

        invincibility = false;    invisibility = false;
        always9 = false;          zeroStun = false;
        rainbowMode = false;

        previous_diff_choice = 1; difficultyChoice = 1;
        scoreMultiplier = 1;      optionChoice = 0;

        colorSpectrum = new Color[10];

        colorSpectrum[0] = Color.white;    colorSpectrum[1] = Color.blue;
        colorSpectrum[2] = Color.cyan;     colorSpectrum[3] = Color.green;
        colorSpectrum[4] = Color.magenta;  colorSpectrum[5] = Color.orange;
        colorSpectrum[6] = Color.pink;     colorSpectrum[7] = Color.red;
        colorSpectrum[8] = Color.yellow;   colorSpectrum[9] = Color.gray;

        staticColorIndex = 3;      randomColorIndex = 8;
        pathFinderColorIndex = 7;  playerColorIndex = 2;
        dotColorSevenIndex = 6;    dotColorEightIndex = 5;
        dotColorNineIndex = 1;

        updateTempOptions();
    }

    //This procedure ends when play is pressed in the main menu.
    public void start(){
        //--------------
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
        //-------------

        while(true){
            int mainMenuChoice = mainMenuSelection();
            //START
            if(mainMenuChoice == 0){
                break;
            }
            //OPTIONS
            else if(mainMenuChoice == 1){
                changeOptions();
            }
            //HELP
            else if(mainMenuChoice == 2){
                readHelp();
            }
            //EXIT
            else{
                System.exit(0);
            }
        }
        clearScreen();
        game.start();
    }

    public static Console getCn() { return cn; }

    public void setGame(Game game) {
        this.game = game;
        updateGameOptions();
    }

    //0 = play, 1 = options, 2 = help, 3 = exit
    private int mainMenuSelection(){
        clearScreen();
        printMainMenu();
        while(true){
            if (mousepr == 1) {  // if mouse button pressed
                //Start Game
                if((mousex >= 35 && mousex <= 38) && (mousey == 7)){
                    mousepr = 0;
                    return 0;
                }
                //Options
                else if((mousex >= 34 && mousex <= 40) && (mousey == 10)) {
                    mousepr = 0;
                    return 1;
                }
                //Help
                else if((mousex >= 35 && mousex <= 48) && (mousey == 13)){
                    mousepr = 0;
                    return 2;
                }
                //Exit
                else if((mousex >= 35 && mousex <= 48) && (mousey == 16)){
                    mousepr = 0;
                    return 3;
                }
                mousepr = 0;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void changeOptions(){
        previous_diff_choice = difficultyChoice;
        optionChoice = 0;

        printOptions();
        //Temp options are used to store the information if apply is pressed it is updated for the game and in normal variables of menu.
        //The latter's purpose is to store the settings even after the game closes and returns back to the main menu.
        updateTempOptions();
        printSettingsInfo();

        while(true){
            if(mousepr == 1){
                //Based on the options choice different pages are shown
                //Difficulty
                if ((mousex >= 15 && mousex <= 25) && (mousey == 1)) {
                    optionChoice = 0;
                }
                //Customization
                else if ((mousex >= 32 && mousex <= 45) && (mousey == 1)) {
                    optionChoice = 1;
                }
                //Cheats
                else if ((mousex >= 51 && mousex <= 57) && (mousey == 1)) {
                    optionChoice = 2;
                }

                //Difficulty Tab
                if(optionChoice == 0){
                    printOptions();
                    //Easy
                    if((mousex >= 15 && mousex <= 18) && (mousey == 4)){
                        eraseSelectionSquares();
                        printSelectionSquare(4,15,4);

                        printToScreen("                                           ", 15, 7);
                        printToScreen("'Easy' preset is selected.", 15, 7);

                        changeTempDifficulties(7000,700,600,300,7,2000);
                        difficultyChoice = 0;
                    }
                    //Normal
                    else if((mousex >= 24 && mousex <= 29) && (mousey == 4)){
                        eraseSelectionSquares();
                        printSelectionSquare(6,24,4);

                        printToScreen("                                           ", 15, 7);
                        printToScreen("'Normal' preset is selected.", 15, 7);

                        changeTempDifficulties(5000,500,500,500,5,4000);
                        difficultyChoice = 1;
                    }
                    //Hard
                    else if((mousex >= 35 && mousex <= 39) && (mousey == 4)){
                        eraseSelectionSquares();
                        printSelectionSquare(4,35,4);

                        printToScreen("                                           ", 15, 7);
                        printToScreen("'Hard' preset is selected.", 15, 7);

                        changeTempDifficulties(2000,300,200,500,3,6000);
                        difficultyChoice = 2;
                    }
                    //Custom
                    //Both clicking on the Custom button and changing the values with arrows starts applying customizations
                    else if(((mousex >= 44 && mousex <= 50) && (mousey == 4)) || ((mousex >= 40 && mousex <= 41) && (mousey == 9))
                            || ((mousex >= 29 && mousex <= 30) && (mousey == 9)) || ((mousex >= 40 && mousex <= 41) && (mousey == 10))
                            || ((mousex >= 29 && mousex <= 30) && (mousey == 10)) || ((mousex >= 40 && mousex <= 41) && (mousey == 11))
                            || ((mousex >= 29 && mousex <= 30) && (mousey == 11)) || ((mousex >= 40 && mousex <= 41) && (mousey == 12))
                            || ((mousex >= 29 && mousex <= 30) && (mousey == 12)) || ((mousex >= 40 && mousex <= 41) && (mousey == 13))
                            || ((mousex >= 29 && mousex <= 30) && (mousey == 13)) || ((mousex >= 40 && mousex <= 41) && (mousey == 14))
                            || ((mousex >= 29 && mousex <= 30) && (mousey == 14))){

                        eraseSelectionSquares();
                        printSelectionSquare(6,44,4);

                        printToScreen("                                           ", 15, 7);
                        printToScreen("'Custom' preset is selected.", 15, 7);

                        changeTempDifficulty(mousex,mousey);
                        difficultyChoice = 3;
                    }
                }
                //Customization Tab
                else if(optionChoice == 1){
                    printOptions();
                    printSettingsInfo();

                    printSelectionSquare(13,32,1);

                    changeTempColor(mousex,mousey);
                }
                //Cheats Tab
                else{
                    printOptions();
                    printSettingsInfo();

                    printSelectionSquare(6,51,1);

                    changeTempCheat(mousex,mousey);
                }

                //Apply
                if((mousex >= 50 && mousex <= 55) && (mousey == 20)){
                    //Options are updated in menu too, to remember the options after the game ends
                    updateMenuOptions();
                    updateGameOptions();

                    mousepr = 0;
                    break;
                }
                //Cancel
                else if((mousex >= 25 && mousex <= 30) && (mousey == 20)){
                    difficultyChoice = previous_diff_choice;

                    mousepr = 0;
                    break;
                }
                printSettingsInfo();
                mousepr = 0;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void readHelp(){
        int page = 1;
        printGeneral();

        while(true){
            if(mousepr == 1){
                //Switches pages based on the arrow direction clicked.
                //Game Characters left
                if ((mousex >= 28 && mousex <= 29) && (mousey == 3) && page == 2) {
                    page--;
                    printGeneral();
                }
                //General Info right
                else if((mousex >= 48 && mousex <= 49) && (mousey == 3) && page == 1) {
                    page++;
                    printGameCharacters();
                }
                //Game Characters right
                else if ((mousex >= 47 && mousex <= 48) && (mousey == 3) && page == 2) {
                    page++;
                    printPlayingInfo();
                }
                //Playing Info left
                else if ((mousex >= 26 && mousex <= 27) && (mousey == 3) && page == 3) {
                    page--;
                    printGameCharacters();
                }
                //Playing Info right
                else if ((mousex >= 49 && mousex <= 50) && (mousey == 3) && page == 3) {
                    page++;
                    printScoring();
                }
                //Scoring left right
                else if ((mousex >= 32 && mousex <= 33) && (mousey == 3) && page == 4) {
                    page--;
                    printPlayingInfo();
                }
                //Go Back
                else if((mousex >= 60 && mousex <= 67) && (mousey == 2)){
                    mousepr = 0;
                    break;
                }
                mousepr = 0;
            }
            printPage(page);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGameOptions(){
        game.setAddNumberSpeed(addSpeed);           game.setPathfinderSpeed(pathfinderSpeed);
        game.setRandomMoverSpeed(randomMoveSpeed);  game.setPlayerSpeed(playerSpeed);
        game.setPlayerNumber(playerNumber);         game.setStunDuration(stunDuration);
        game.setScoreMultiplier(scoreMultiplier);   game.setInvincibility(invincibility);
        game.setZeroStun(zeroStun);                 game.setInvisiblity(invisibility);

        if (always9) {
            game.setPlayerNumber(9);
            game.setAlways9(always9);
        }

        //Sets the coloring rules for all of the objects that will be created by random number
        RandomNumber.setStatic_color(colorSpectrum[staticColorIndex]);
        RandomNumber.setRandom_move_color(colorSpectrum[randomColorIndex]);
        RandomNumber.setPathfinder_color(colorSpectrum[pathFinderColorIndex]);
        RandomNumber.setPlayer_color(colorSpectrum[playerColorIndex]);

        game.getMazeHandler().setDotColorSeven(colorSpectrum[dotColorSevenIndex]);
        game.getMazeHandler().setDotColorEight(colorSpectrum[dotColorEightIndex]);
        game.getMazeHandler().setDotColorNine(colorSpectrum[dotColorNineIndex]);

        game.getMazeHandler().setRainbowMode(rainbowMode);
    }

    private void updateTempOptions(){
        tempAddSpeed = addSpeed;            tempPathfSpeed = pathfinderSpeed;
        tempRandomSpeed = randomMoveSpeed;  tempPlayerSpeed = playerSpeed;
        tempPlayerNumber = playerNumber;    tempStunDuration = stunDuration;

        tempInvincibility = invincibility;  tempInvisibility = invisibility;
        tempAlways9 = always9;              tempZeroStun = zeroStun;
        tempRainbowMode = rainbowMode;

        tempStaticColorIndex = staticColorIndex;     tempRandomColorIndex = randomColorIndex;
        tempPlayerColorIndex = playerColorIndex;     tempPathFinderColorIndex = pathFinderColorIndex;
        tempDotColorSevenIndex = dotColorSevenIndex; tempDotColorEightIndex = dotColorEightIndex;
        tempDotColorNineIndex = dotColorNineIndex;

        tempScoreMultiplier = scoreMultiplier;
    }

    private void updateMenuOptions(){
        addSpeed = tempAddSpeed;            pathfinderSpeed = tempPathfSpeed;
        randomMoveSpeed = tempRandomSpeed;  playerSpeed = tempPlayerSpeed;
        playerNumber = tempPlayerNumber;    stunDuration = tempStunDuration;

        invincibility = tempInvincibility;  invisibility = tempInvisibility;
        always9 = tempAlways9;              zeroStun = tempZeroStun;
        rainbowMode = tempRainbowMode;

        staticColorIndex = tempStaticColorIndex;     randomColorIndex = tempRandomColorIndex;
        playerColorIndex = tempPlayerColorIndex;     pathFinderColorIndex = tempPathFinderColorIndex;
        dotColorSevenIndex = tempDotColorSevenIndex; dotColorEightIndex = tempDotColorEightIndex;
        dotColorNineIndex = tempDotColorNineIndex;

        scoreMultiplier = tempScoreMultiplier;
    }

    private void changeTempDifficulties(int addSpeed,int pathfinderSpeed,int randomMoveSpeed,int playerSpeed,int playerNumber,int stunDuration){
        tempAddSpeed = addSpeed;
        tempPathfSpeed = pathfinderSpeed;
        tempRandomSpeed = randomMoveSpeed;
        tempPlayerSpeed = playerSpeed;
        tempPlayerNumber = playerNumber;
        tempStunDuration = stunDuration;
    }

    private void changeTempDifficulty(int mousex,int mousey){
        if ((mousex >= 40 && mousex <= 41) && (mousey == 9)) {
            if (tempAddSpeed != 8000) {
                tempAddSpeed += 200;
            }
            else {
                tempAddSpeed = 1000;
            }
        }
        else if ((mousex >= 29 && mousex <= 30) && (mousey == 9)) {
            if (tempAddSpeed != 1000) {
                tempAddSpeed -= 200;
            }
            else {
                tempAddSpeed = 8000;
            }
        }
        else if ((mousex >= 40 && mousex <= 41) && (mousey == 10)) {
            if (tempPathfSpeed != 1000) {
                tempPathfSpeed += 100;
            }
            else {
                tempPathfSpeed = 100;
            }
        }
        else if ((mousex >= 29 && mousex <= 30) && (mousey == 10)) {
            if (tempPathfSpeed != 100) {
                tempPathfSpeed -= 100;
            }
            else {
                tempPathfSpeed = 1000;
            }
        }
        else if ((mousex >= 40 && mousex <= 41) && (mousey == 11)) {
            if (tempRandomSpeed != 1000) {
                tempRandomSpeed += 100;
            }
            else {
                tempRandomSpeed = 100;
            }
        }
        else if ((mousex >= 29 && mousex <= 30) && (mousey == 11)) {
            if (tempRandomSpeed != 100) {
                tempRandomSpeed -= 100;
            }
            else {
                tempRandomSpeed = 1000;
            }
        }
        else if ((mousex >= 40 && mousex <= 41) && (mousey == 12)) {
            if (tempPlayerSpeed != 1000) {
                tempPlayerSpeed += 100;
            }
            else {
                tempPlayerSpeed = 100;
            }
        }
        else if ((mousex >= 29 && mousex <= 30) && (mousey == 12)) {
            if (tempPlayerSpeed != 100) {
                tempPlayerSpeed -= 100;
            }
            else {
                tempPlayerSpeed = 1000;
            }
        }
        else if ((mousex >= 40 && mousex <= 41) && (mousey == 13)) {
            if (tempPlayerNumber != 9) {
                tempPlayerNumber++;
            }
            else {
                tempPlayerNumber = 1;
            }
        }
        else if ((mousex >= 29 && mousex <= 30) && (mousey == 13)) {
            if (tempPlayerNumber != 1) {
                tempPlayerNumber--;
            }
            else {
                tempPlayerNumber = 9;
            }
        }
        else if ((mousex >= 40 && mousex <= 41) && (mousey == 14)) {
            if (tempStunDuration != 8000) {
                tempStunDuration += 200;
            }
            else {
                tempStunDuration = 1000;
            }
        }
        else if ((mousex >= 29 && mousex <= 30) && (mousey == 14)) {
            if (tempStunDuration != 1000) {
                tempStunDuration -= 200;
            }
            else {
                tempStunDuration = 8000;
            }
        }
    }

    private void changeTempColor(int mousex,int mousey){
        if ((mousex >= 29 && mousex <= 30) && (mousey == 4)) {
            if (tempStaticColorIndex != 0) {
                tempStaticColorIndex--;
            }
            else {
                tempStaticColorIndex = 9;
            }
        }
        else if ((mousex >= 36 && mousex <= 37) && (mousey == 4)) {
            if (tempStaticColorIndex != 9) {
                tempStaticColorIndex++;
            }
            else {
                tempStaticColorIndex = 0;
            }
        }
        else if ((mousex >= 29 && mousex <= 30) && (mousey == 5)) {
            if (tempRandomColorIndex != 0) {
                tempRandomColorIndex--;
            }
            else {
                tempRandomColorIndex = 9;
            }
        }
        else if ((mousex >= 36 && mousex <= 37) && (mousey == 5)) {
            if (tempRandomColorIndex != 9) {
                tempRandomColorIndex++;
            }
            else {
                tempRandomColorIndex = 0;
            }
        }
        else if ((mousex >= 29 && mousex <= 30) && (mousey == 6)) {
            if (tempPathFinderColorIndex != 0) {
                tempPathFinderColorIndex--;
            }
            else {
                tempPathFinderColorIndex = 9;
            }
        }
        else if ((mousex >= 36 && mousex <= 37) && (mousey == 6)) {
            if (tempPathFinderColorIndex != 9) {
                tempPathFinderColorIndex++;
            }
            else {
                tempPathFinderColorIndex = 0;
            }
        }
        else if ((mousex >= 29 && mousex <= 30) && (mousey == 7)) {
            if (tempPlayerColorIndex != 0) {
                tempPlayerColorIndex--;
            }
            else {
                tempPlayerColorIndex = 9;
            }
        }
        else if ((mousex >= 36 && mousex <= 37) && (mousey == 7)) {
            if (tempPlayerColorIndex != 9) {
                tempPlayerColorIndex++;
            }
            else {
                tempPlayerColorIndex = 0;
            }
        }
        else if ((mousex >= 29 && mousex <= 30) && (mousey == 8)) {
            if (tempDotColorSevenIndex != 0) {
                tempDotColorSevenIndex--;
            }
            else {
                tempDotColorSevenIndex = 9;
            }
        }
        else if ((mousex >= 36 && mousex <= 37) && (mousey == 8)) {
            if (tempDotColorSevenIndex != 9) {
                tempDotColorSevenIndex++;
            }
            else {
                tempDotColorSevenIndex = 0;
            }
        }
        else if ((mousex >= 29 && mousex <= 30) && (mousey == 9)) {
            if (tempDotColorEightIndex != 0) {
                tempDotColorEightIndex--;
            }
            else {
                tempDotColorEightIndex = 9;
            }
        }
        else if ((mousex >= 36 && mousex <= 37) && (mousey == 9)) {
            if (tempDotColorEightIndex != 9) {
                tempDotColorEightIndex++;
            }
            else {
                tempDotColorEightIndex = 0;
            }
        }
        else if ((mousex >= 29 && mousex <= 30) && (mousey == 10)) {
            if (tempDotColorNineIndex != 0) {
                tempDotColorNineIndex--;
            }
            else {
                tempDotColorNineIndex = 9;
            }
        }
        else if ((mousex >= 36 && mousex <= 37) && (mousey == 10)) {
            if (tempDotColorNineIndex != 9) {
                tempDotColorNineIndex++;
            }
            else {
                tempDotColorNineIndex = 0;
            }
        }
        else if ((mousex >= 45 && mousex <= 68) && (mousey == 7)) {
            tempStaticColorIndex = 3; tempRandomColorIndex = 8;
            tempPathFinderColorIndex = 7; tempPlayerColorIndex = 2;
            tempDotColorSevenIndex = 6; tempDotColorEightIndex = 5;
            tempDotColorNineIndex = 1;

        }
        else if (((mousex >= 29 && mousex <= 30) || (mousex >= 36 && mousex <= 38)) && (mousey == 12)) {
            tempRainbowMode = !tempRainbowMode;
        }
    }

    private void changeTempCheat(int mousex, int mousey){
        if ((mousex >= 33 && mousex <= 34) || (mousex >= 40 && mousex <= 42)) {
            if (mousey == 8) {
                tempInvincibility = !tempInvincibility;
            }
            else if (mousey == 9) {
                tempInvisibility = !tempInvisibility;
            }
            else if (mousey == 10) {
                tempAlways9 = !tempAlways9;
            }
            else if (mousey == 11) {
                if (mousex <= 34) {
                    if (tempScoreMultiplier != 1) {
                        tempScoreMultiplier--;
                    }
                    else {
                        tempScoreMultiplier = 10;
                    }
                }
                else{
                    if (tempScoreMultiplier != 10) {
                        tempScoreMultiplier++;
                    }
                    else {
                        tempScoreMultiplier = 1;
                    }
                }

            }
            else if (mousey == 12) {
                tempZeroStun = !tempZeroStun;
            }
        }
    }

    private void printToScreen(String s, int x, int y) {
        for (int i = 0; i < s.length(); i++) {
            cn.getTextWindow().output(x + i, y, s.charAt(i));
        }
    }

    private void printMainMenu(){
        printToScreen("Number Maze",32,3);
        printToScreen("Play",35,7);
        printToScreen("Options",34,10);
        printToScreen("Help",35,13);
        printToScreen("Exit",35,16);

        printToScreen("Created By",32,19);
        printToScreen("Yigit Can Akcay",30,20);
        printToScreen("Emre Kocak",32,21);
        printToScreen("Can Turk Kucuk",30,22);
    }

    private void printOptions(){
        clearScreen();
        printToScreen("Difficulty",15,1);
        printToScreen("Customization", 32, 1);
        printToScreen("Cheats", 51, 1);

        if (optionChoice == 0) {
            printToScreen("Easy",15,4);
            printToScreen("Normal",24,4);
            printToScreen("Hard",35,4);
            printToScreen("Custom", 44, 4);

            printSelectionSquare(10,15,1);

            if(difficultyChoice == 0){
                printSelectionSquare(4,15,4);
            }
            else if(difficultyChoice == 1){
                printSelectionSquare(6,24,4);
            }
            else if(difficultyChoice == 2){
                printSelectionSquare(4,35,4);
            }
            else{
                printSelectionSquare(6,44,4);
            }

            printToScreen("To customise the settings, select 'Custom' or use the arrows.", 15, 16);

            if(difficultyChoice == 0){
                printToScreen("'Easy' preset is selected.", 15, 7);
            }
            else if(difficultyChoice == 1){
                printToScreen("'Normal' preset is selected.", 15, 7);
            }
            else if(difficultyChoice == 2){
                printToScreen("'Hard' preset is selected.", 15, 7);
            }
            else{
                printToScreen("'Custom' preset is selected.", 15, 7);
            }
        }

        printToScreen("Cancel",25,20);
        printToScreen("Apply",50,20);
    }

    private void printSettingsInfo() {
        if (optionChoice == 0) {
            for (int i = 9; i < 15; i++) {
                printToScreen("                                                  ", 15, i);
            }

            printToScreen("Adding Speed: <- " + tempAddSpeed + " ms ->", 15, 9);
            printToScreen("7/8/9 Speed : <- " + tempPathfSpeed + " ms ->", 15, 10);
            printToScreen("4/5/6 Speed : <- " + tempRandomSpeed + " ms ->", 15, 11);
            printToScreen("Player Speed: <- " + tempPlayerSpeed + " ms ->", 15, 12);
            printToScreen("Player No.  : <-   " + tempPlayerNumber + "    ->", 15, 13);
            printToScreen("Stun Time   : <- " + tempStunDuration + " ms ->", 15, 14);
        }
        else if (optionChoice == 1) {
            printToScreen("                                                 ", 15, 12);

            printToScreen("1/2/3 Color : <-     ->", 15, 4);
            printToScreen("4/5/6 Color : <-     ->", 15, 5);
            printToScreen("7/8/9 Color : <-     ->", 15, 6);
            printToScreen("Player Color: <-     ->", 15, 7);
            printToScreen("7 Dot Color : <-     ->", 15, 8);
            printToScreen("8 Dot Color : <-     ->", 15, 9);
            printToScreen("9 Dot Color : <-     ->", 15, 10);

            cn.getTextWindow().output(15, 12, 'R', new TextAttributes(Color.red));
            cn.getTextWindow().output(16, 12, 'a', new TextAttributes(Color.yellow));
            cn.getTextWindow().output(17, 12, 'i', new TextAttributes(Color.green));
            cn.getTextWindow().output(18, 12, 'n', new TextAttributes(Color.red));
            cn.getTextWindow().output(19, 12, 'b', new TextAttributes(Color.cyan));
            cn.getTextWindow().output(20, 12, 'o', new TextAttributes(Color.magenta));
            cn.getTextWindow().output(21, 12, 'w', new TextAttributes(Color.pink));
            cn.getTextWindow().output(23, 12, 'm', new TextAttributes(Color.green));
            cn.getTextWindow().output(24, 12, 'o', new TextAttributes(Color.red));
            cn.getTextWindow().output(25, 12, 'd', new TextAttributes(Color.cyan));
            cn.getTextWindow().output(26, 12, 'e', new TextAttributes(Color.yellow));

            if (!tempRainbowMode) {
                printToScreen(": <- Off ->", 27, 12);
            }
            else {
                printToScreen(": <-  On  ->", 27, 12);
            }

            printToScreen("Restore Default Colors", 45, 7);

            String str1 = "123";
            String str2 = "456";
            String str3 = "789";
            String str4 = "555";
            String str5 = "...";

            for (int i = 0; i < 3; i++) {
                cn.getTextWindow().output(32 + i, 4, str1.charAt(i), new TextAttributes(colorSpectrum[tempStaticColorIndex]));
                cn.getTextWindow().output(32 + i, 5, str2.charAt(i), new TextAttributes(colorSpectrum[tempRandomColorIndex]));
                cn.getTextWindow().output(32 + i, 6, str3.charAt(i), new TextAttributes(colorSpectrum[tempPathFinderColorIndex]));
                cn.getTextWindow().output(32 + i, 7, str4.charAt(i), new TextAttributes(colorSpectrum[tempPlayerColorIndex]));
                cn.getTextWindow().output(32 + i, 8, str5.charAt(i), new TextAttributes(colorSpectrum[tempDotColorSevenIndex]));
                cn.getTextWindow().output(32 + i, 9, str5.charAt(i), new TextAttributes(colorSpectrum[tempDotColorEightIndex]));
                cn.getTextWindow().output(32 + i, 10, str5.charAt(i), new TextAttributes(colorSpectrum[tempDotColorNineIndex]));

            }
        }
        else if (optionChoice == 2) {
            for (int i = 8; i < 13; i++) {
                printToScreen("                                              ", 15, i);
            }

            if (!tempInvincibility) { printToScreen("Invincibility   : <- Off ->", 15, 8); }
            else { printToScreen("Invincibility   : <-  On  ->", 15, 8); }

            if (!tempInvisibility) { printToScreen("Invisibility    : <- Off ->", 15, 9); }
            else { printToScreen("Invisibility    : <-  On  ->", 15, 9); }

            if (!tempAlways9) { printToScreen("Always 9        : <- Off ->", 15, 10); }
            else { printToScreen("Always 9        : <-  On  ->", 15, 10); }

            printToScreen("Score Multiplier: <- " + tempScoreMultiplier + "x ->", 15,11);

            if (!tempZeroStun) { printToScreen("Zero Stun       : <- Off ->", 15, 12); }
            else { printToScreen("Zero Stun       : <-  On  ->", 15, 12); }
        }
    }

    private void printSelectionSquare(int wordLength,int startPosX, int row){
        //Creates
        String closedUpDown = "+";
        for(int i = 0 ; i < wordLength; i++){
            closedUpDown += "-";
        }
        closedUpDown += "+";

        printToScreen(closedUpDown,startPosX - 1,row - 1);
        printToScreen("|",startPosX - 1,row);
        printToScreen("|",startPosX + wordLength,row);
        printToScreen(closedUpDown,startPosX - 1,row + 1);
    }

    private void eraseSelectionSquare(int wordLength,int startPosX, int row){
        String closedUpDown = " ";
        for(int i = 0 ; i < wordLength; i++){
            closedUpDown += " ";
        }
        closedUpDown += " ";

        printToScreen(closedUpDown,startPosX - 1,row - 1);
        printToScreen(" ",startPosX - 1,row);
        printToScreen(" ",startPosX + wordLength,row);
        printToScreen(closedUpDown,startPosX - 1,row + 1);
    }

    private void eraseSelectionSquares(){
        eraseSelectionSquare(4,15,4);
        eraseSelectionSquare(6,24,4);
        eraseSelectionSquare(4,35,4);
        eraseSelectionSquare(6,44,4);
    }

    private void printGeneral() {
        printBack();
        printToScreen("General Information ->", 28, 3);
        printToScreen("The game is played in a 23*55 game field including walls.", 10, 5);
        printToScreen("Game characters are numbers (1-9). Human player is represented", 10, 6);
        printToScreen("by a blue number. Computer controls other numbers. The aim", 10, 7);
        printToScreen("of the game is reaching the highest score by collecting from", 10, 8);
        printToScreen("Computer numbers (1-9) are inserted into the maze area an", 10, 10);
        printToScreen("input list. The input list (size of 10 numbers) is always", 10, 11);
        printToScreen("full of numbers, and shows the next item which will be inserted", 10, 12);
        printToScreen("into the maze. During the game, an item is inserted into the", 10, 13);
        printToScreen("maze every 5 seconds.", 10, 14);
        printToScreen("Human player have two backpacks: Left and right. These are ", 10, 16);
        printToScreen("used to put collected numbers. Each backpack size is 8.", 10, 17);
        printToScreen("When the human player's blue number meets a computer number", 10, 19);
        printToScreen("which is greater than itself, the game ends.", 10, 20);
        printToScreen("(Press 'S' key to end game)", 10, 22);
        printToScreen("(Press 'P' key to pause the game)", 10, 23);
        printToScreen("(Press 'R' key to restart the game)", 10, 24);
    }
    private void printGameCharacters() {
        printBack();
        printToScreen("<- Game Characters ->", 28, 3);
        printToScreen("Numbers: 1, 2, 3, 4, 5, 6, 7, 8, 9", 7, 5);
        printToScreen("Blue number: Human number", 7, 7);
        printToScreen(" - Cursor keys: To move human player number", 7, 8);
        printToScreen(" - Q: Transfer one item from right backpack to left backpack.", 7, 9);
        printToScreen(" - W: Transfer one item from left backpack to right backpack.", 7, 10);
        printToScreen("Other numbers: Computer numbers", 7, 12);
        printToScreen(" - Green numbers: 1, 2, 3. These numbers do not move(static numbers).", 7, 13);
        printToScreen(" - Yellow numbers: 4, 5, 6. These numbers move randomly.", 7, 14);
        printToScreen(" - Red numbers: 7, 8, 9. These numbers do path finding to", 7, 15);
        printToScreen(" - reach the blue number.", 7, 16);
        printToScreen(" - Red numbers' targeted path is marked in the game area.", 7, 17);
    }
    private void printPlayingInfo() {
        printBack();
        printToScreen("<- Playing Information ->", 26, 3);
        printToScreen("When the human player meets a computer number at the same square,", 3, 5);
        printToScreen(" - if computer number is greater than the human number, human player dies.", 3, 6);
        printToScreen(" - if computer number is less or equal than the human number, human player", 3, 7);
        printToScreen(" - collects the computer number. ", 3, 8);
        printToScreen("Collected numbers are put in the left backpack directly. Human player can", 3, 10);
        printToScreen("transfer the top item from a backpack to the other one, if there is a place.", 3, 11);
        printToScreen("When numbers at the two backpacks are the same, at the same backpack level,", 3, 13);
        printToScreen("they are matched. Matched numbers disappear from backpacks and turn into", 3, 14);
        printToScreen("score. Also each matching operation adds 1 to the human player's number.", 3, 15);
        printToScreen("If human player number exceeds 9, it turns into 1 again. Human player cannot", 3, 17);
        printToScreen("move when his/her number is 1. There are two options to move again;", 3, 18);
        printToScreen(" - Another matching operation makes human player number 2.", 3, 19);
        printToScreen(" - Otherwise, after 4 seconds, human number will be 2.", 3, 20);
        printToScreen("If the left backpack is full when the human player collects a new number,", 3, 22);
        printToScreen("the top item of the left backpack disappears, then new collected number", 3, 23);
        printToScreen("is inserted into the left backpack.", 3, 24);
    }
    private void printScoring() {
        printBack();
        printToScreen("<- Scoring", 32, 3);
        printToScreen("Score is calculated as follows when the human player", 10, 5);
        printToScreen("succeeds a matching operation.", 10, 6);
        printToScreen("Score  =  Matched_Number  *  Matched_Number_Score_Factor", 10, 8);
        printToScreen("Item        Generation probability     Score factor", 10, 10);
        printToScreen("1, 2, 3             75* %                    1", 10, 11);
        printToScreen("4, 5, 6             20* %                    5", 10, 12);
        printToScreen("7, 8, 9              5* %                   25", 10, 13);
        printToScreen("*(equal probabilities within themselves)", 10, 15);
    }

    private void printPage(int page) {
        printToScreen(page + "/4", 15, 3);
    }

    private void printBack() {
        clearScreen();
        printToScreen("Go Back", 60, 2);
    }

    private void clearScreen(){
        for(int i = 0; i < cn.getTextWindow().getColumns();i++){
            for(int j = 0; j < cn.getTextWindow().getRows();j++){
                cn.getTextWindow().output(i,j,' ');
            }
        }
    }
}
