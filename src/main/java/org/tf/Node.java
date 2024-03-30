package org.tf;

public class Node {

    private int value;
    private Node left;
    private Node right;
    private Node prevRootOfHeap;
    private int sizeOfHeap = 1;
    public Node(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getPrevRootOfHeap() {
        return prevRootOfHeap;
    }

    public void setPrevRootOfHeap(Node prevRootOfHeap) {
        this.prevRootOfHeap = prevRootOfHeap;
    }

    public int getSizeOfHeap() {
        return sizeOfHeap;
    }

    public void setSizeOfHeap(int sizeOfHeap) {
        this.sizeOfHeap = sizeOfHeap;
    }
}
