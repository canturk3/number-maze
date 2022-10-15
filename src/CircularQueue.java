public class CircularQueue {

    // Attributes.
    private int rear, front;
    private Object[] elements;

    // Constructor.
    public CircularQueue(int capacity){
        elements = new Object[capacity];
        rear = -1;
        front = 0;
    }

    // Method for adding an object to the queue.
    public void enqueue(Object data) {
        if (!IsFull()) {
            rear = (rear + 1) % elements.length;
            elements[rear] = data;
        }
    }

    // Method for removing the front object from the queue.
    public Object dequeue() {
        if (IsEmpty()) {
            return null;
        }
        else {
            Object retData = elements[front];
            elements[front] = null;
            front = (front + 1) % elements.length;
            return retData;
        }
    }

    // Method for getting the front object.
    public Object peek() {
        if (IsEmpty()) {
            return null;
        }
        else {
            return elements[front];
        }
    }

    // Method which checks if the queue is empty.
    public boolean IsFull() {
        return (front == (rear + 1) % elements.length && elements[front] != null && elements[rear] != null);
    }

    // Method which checks if the queue is full.
    public boolean IsEmpty() {
        return elements[front] == null;
    }

    // Method which returns the queue size.
    public int size() {
        if (elements[front] == null) {
            return 0;
        }
        else {
            if (rear >= front) {
                return rear - front + 1;
            }
            else {
                return elements.length - (front - rear) + 1;
            }
        }
    }
}
