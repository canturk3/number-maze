public class Stack {
    private int top;
    private RandomNumber[] elements;
    public Stack(int capacity) {
        elements = new RandomNumber[capacity];
        top = -1;
    }
    void push(RandomNumber data) {
        if (isFull()) {
            System.out.println("Stack overflow");
        }
        else {
            top++;
            elements[top] = data;
        }
    }
    RandomNumber pop() {
        if (isEmpty()) {
            System.out.println("Stack is empty");
            return null;
        }
        else {
            RandomNumber temp = elements[top];
            top--;
            return temp;
        }
    }
    RandomNumber peek() {
        if (isEmpty()) {
            System.out.println("Stack is empty");
            return null;
        }
        else {
            return elements[top];
        }
    }
    boolean isEmpty() {
        return top == -1;
    }
    boolean isFull() {
        return top + 1 == elements.length;
    }
    int size() {
        return top + 1;
    }
}

