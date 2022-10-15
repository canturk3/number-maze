public class Main {
    public static void main(String[] args){
        boolean restart = false;

        Menu mainMenu = new Menu();
        Game mazeGame = new Game("maze.txt");

        if(mazeGame.getMaze() != null){
            while (true){
                mainMenu.setGame(mazeGame);

                if(!restart){
                    mainMenu.start();
                }
                else{
                    mazeGame.start();
                }

                restart = mazeGame.isRestart();

                mazeGame = new Game("maze.txt");
            }
        }
    }
}
