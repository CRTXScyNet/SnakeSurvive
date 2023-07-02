package org.example.Painter;


import org.example.Enemy.Cell;

import java.awt.*;

public class Node {
    private Point point;


    boolean appleIsHere;

    private Cell cell;
    private Node up = null;
    private Node down = null;
    private Node left = null;
    private Node right = null;
    private Node[] nodes = new Node[4];

    private double distanse;

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    private int posX;
    private int posY;

    public Node(Point p, int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.point = p;
        cell = null;
        setNodes();
    }

    private void setNodes() {
        nodes[0] = up;
        nodes[1] = down;
        nodes[2] = left;
        nodes[3] = right;

    }


    public void setCell(Cell cell) {
        this.cell = cell;
    }


    public Node getUp() {
        return up;
    }

    public Node getDown() {
        return down;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public void setUp(Node up) {
        if (up != null) {
            this.up = up;
            if (up.getDown() == null) {
                up.setDown(this);
            }
        }
        setNodes();
    }

    public void setDown(Node down) {
        if (down != null) {
            this.down = down;
            if (down.getUp() == null) {
                down.setUp(this);
            }
        }
        setNodes();
    }

    public void setLeft(Node left) {
        if (left != null) {
            this.left = left;
            if (left.getRight() == null) {
                left.setRight(this);
            }
        }
        setNodes();
    }

    public void setRight(Node right) {
        if (right != null) {
            this.right = right;
            if (right.getLeft() == null) {
                right.setLeft(this);
            }
        }
        setNodes();
    }

    public boolean isAppleHere() {
        return appleIsHere;
    }

    public void setAppleIsHere(boolean appleIsHere) {
        this.appleIsHere = appleIsHere;
    }

    public Cell getCell() {
        return cell;
    }

    public Point getPos() {
        return this.point;
    }

    public Node[] getNodes() {
        return nodes;
    }
}
