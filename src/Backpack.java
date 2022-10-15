public class Backpack {
    enigma.console.Console cn;
    private final Stack firstBackpack;
    private final Stack secondBackpack;

    public Backpack(enigma.console.Console cn) {
        this.cn = cn;
        firstBackpack = new Stack(8);
        secondBackpack = new Stack(8);
    }

    //Adding number to first backpack
    public void addToFirst(RandomNumber object) {
        if (firstBackpack.isFull()) {
            firstBackpack.pop();
        }
        firstBackpack.push(object);
    }

    //Adding the second stack's top number to first backpack
    public void transferToFirst() {
        if (!secondBackpack.isEmpty()) {
            firstBackpack.push(secondBackpack.pop());
        }
    }

    //Adding the first stack's top number to second backpack
    public void addToSecond() {
        if (!firstBackpack.isEmpty()) {
            secondBackpack.push(firstBackpack.pop());
        }
    }

    //Checks if the same numbers are side by side
    public int comparison() {
        Stack temp1 = new Stack(8);
        Stack temp2 = new Stack(8);
        int score = -1;

        while (!firstBackpack.isEmpty()) {
            int number = firstBackpack.peek().getNumber();
            int size = firstBackpack.size();
            while (!secondBackpack.isEmpty()) {
                if (number == secondBackpack.peek().getNumber() && size == secondBackpack.size()) {
                    score = secondBackpack.peek().getPoints();
                    firstBackpack.pop();
                    secondBackpack.pop();
                    break;
                }
                else {
                    temp2.push(secondBackpack.pop());
                }
            }

            while (!temp2.isEmpty()) {
                secondBackpack.push(temp2.pop());
            }
            if(!firstBackpack.isEmpty()){
                temp1.push(firstBackpack.pop());
            }
        }

        while (!temp1.isEmpty()) {
            firstBackpack.push(temp1.pop());
        }

        return score;
    }

    public void newDisplay() {
        Stack temp1 = new Stack(8);
        Stack temp2 = new Stack(8);
        while (!firstBackpack.isEmpty()) {
            temp1.push(firstBackpack.pop());
        }
        while (!secondBackpack.isEmpty()) {
            temp2.push(secondBackpack.pop());
        }
        int realX = 60;
        int x = realX;
        int y = 6;
        char[] backpacks = { 'B', 'a', 'c', 'k', 'p', 'a', 'c', 'k', 's'};
        char[] left = { 'L', 'e', 'f', 't'};
        char[] right = { 'R', 'i', 'g', 'h', 't'};
        for (int i = 0; i < backpacks.length; i++) {
            cn.getTextWindow().output(x + 2 + i, y, backpacks[i]);
        }
        y++;

        for (int i = 0; i < 9; i++) {
            x = realX;
            if (i == 8) {
                for (int k = 0; k < 4; k++) {
                    if (k == 3) {
                        cn.getTextWindow().output(realX + 2, y + 2 + i, 'Q');
                        cn.getTextWindow().output(realX + 9, y + 2 + i, 'W');
                    }
                    else if (k == 2) {
                        for (int l = 0; l < left.length; l++) {
                            cn.getTextWindow().output(realX + l, y + 1 + i, left[l]);
                        }
                        for (int l = 0; l < right.length; l++) {
                            cn.getTextWindow().output(realX + 7 + l, y + 1 + i, right[l]);
                        }

                    }
                    else {
                        cn.getTextWindow().output(x, y + i, '+');
                        cn.getTextWindow().output(x + 1, y + i, '-');
                        cn.getTextWindow().output(x + 2, y + i, '-');
                        cn.getTextWindow().output(x + 3, y + i, '-');
                        cn.getTextWindow().output(x + 4, y + i, '+');
                        x += 7;
                    }
                }
            }
            else {
                //This is for clearing the old backpack pattern
                for (int k = 0; k < 2; k++) {
                    cn.getTextWindow().output(x, y + i, '|');
                    cn.getTextWindow().output(x + 2, y + 7 - i, ' ');
                    cn.getTextWindow().output(x + 4, y + i, '|');
                    x += 7;
                }

                x = realX;
                cn.getTextWindow().output(x, y + i, '|');
                cn.getTextWindow().output(x + 4, y + i, '|');
                cn.getTextWindow().output(x + 7, y + i, '|');
                cn.getTextWindow().output(x + 11, y + i, '|');

                if (!temp1.isEmpty()) {
                    String number = String.valueOf(temp1.peek().getNumber());
                    cn.getTextWindow().output(x + 2, y + 7 - i, number.charAt(0));
                    firstBackpack.push(temp1.pop());
                }

                if (!temp2.isEmpty()) {
                    String number = String.valueOf(temp2.peek().getNumber());
                    cn.getTextWindow().output(x + 9, y + 7 - i, number.charAt(0));
                    secondBackpack.push(temp2.pop());
                }

                cn.getTextWindow().output(x + 4, y + i, '|');
            }
        }
    }
    public Stack getFirstBackpack() {
        return firstBackpack;
    }

    public Stack getSecondBackpack() {
        return secondBackpack;
    }
}
