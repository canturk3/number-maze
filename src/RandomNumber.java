import java.awt.*;
import java.util.Random;

public class RandomNumber {

    // Attributes.
    private int scoreFactor;
    private int number;
    private int points;
    private Color color;

    //Static colors that are set in the menu for all numbers.
    private static Color static_color,random_move_color,pathfinder_color,player_color;

    // Constructor.
    public RandomNumber() {

    }

    public RandomNumber(int scoreFactor, int number, int points, Color color) {
        this.scoreFactor = scoreFactor;
        this.number = number;
        this.points = points;
        this.color = color;
    }

    public static void setStatic_color(Color static_color) {
        RandomNumber.static_color = static_color;
    }

    public static void setRandom_move_color(Color random_move_color) {
        RandomNumber.random_move_color = random_move_color;
    }

    public static void setPathfinder_color(Color pathfinder_color) {
        RandomNumber.pathfinder_color = pathfinder_color;
    }

    public static void setPlayer_color(Color player_color) {
        RandomNumber.player_color = player_color;
    }

    // Method for randomly generating a number.
    public void generateNumber() {
        Random rnd = new Random();
        int rand;	// Used to store the random number.
        int x;		// Used to store the number generated.
        int factor; // Used to store the score factor of the number generated.
        Color color;   // Used to store the color of the number generated.

        rand = rnd.nextInt(20) + 1; // A random number is generated between 1-20.

        // If the random number is between 1-15, a new random number is generated between 0-2.
        // This corresponds to the 75% probability of the number being 1, 2 or 3.
        if (rand <= 15) {
            rand = rnd.nextInt(3); // Equal probability of having a 1, 2 or 3.

            if (rand == 0) {
                x = 1;
            }
            else if (rand == 1) {
                x = 2;
            }
            else {
                x = 3;
            }
        }
        // If the random number is 20, a new random number is generated between 0-2.
        // This corresponds to the 5% probability of the number being 7, 8 or 9.
        else if (rand == 20) {
            rand = rnd.nextInt(3); // Equal probability of having a 7, 8 or 9.

            if (rand == 0) {
                x = 7;
            }
            else if (rand == 1) {
                x = 8;
            }
            else {
                x = 9;
            }
        }
        // If the random number is between 16-19, a new random number is generated between 0-2.
        // This corresponds to the 20% probability of the number being 4, 5 or 6.
        else {
            rand = rnd.nextInt(3); // Equal probability of having a 4, 5 or 6.

            if (rand == 0) {
                x = 4;
            }
            else if (rand == 1) {
                x = 5;
            }
            else {
                x = 6;
            }
        }

        // Determining the score factor and the color based on the number generated.
        if (x == 1 || x == 2 || x == 3) {
            factor = 1;
            color = static_color;
        }
        else if (x == 4 || x == 5 || x == 6) {
            factor = 5;
            color = random_move_color;
        }
        else {
            factor = 25;
            color = pathfinder_color;
        }

        // Setting the number, scoreFactor, points and the color of the RandomNumber object.
        setNumber(x);
        setScoreFactor(factor);
        setPoints(x, factor);
        setColor(color);
    }

    // Getters
    public int getScoreFactor() {return scoreFactor;}
    public int getNumber() {return number;}
    public int getPoints() {return points;}
    public Color getColor() {return color;}

    // Setters
    public void setNumber(int number) {this.number = number;}
    public void setScoreFactor(int factor) {scoreFactor = factor;}
    public void setPoints(int number, int factor) {points = number * factor;}
    public void setColor(Color color) {this.color = color;}

    // This method sets the empty parts of the maze.
    public void setEmptySpace(){
        setNumber(0);
        setScoreFactor(0);
        setPoints(0, 0);
        setColor(Color.BLACK);
    }

    // This method sets the walls of the maze.
    public void setWall(){
        setNumber(-1);
        setScoreFactor(-1);
        setPoints(-1, -1);
        setColor(Color.LIGHT_GRAY);
    }

    // This method sets the player's number and other parts.
    public void setPlayer(int number){
        setNumber(number);
        setScoreFactor(0);
        setPoints(0, 0);
        setColor(player_color);
    }

    // Method for printing the random number without using getNumber().
    public String toString(){
        return String.valueOf(number);
    }
}
