package com.javamaze.maze.gui;

import javax.swing.*;

import com.javamaze.maze.Maze;
import com.javamaze.maze.Player;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//Group 5: Written by Dillon and Eric

public class MazeGUI extends JFrame implements KeyListener {

    private Maze maze;
    private Player player;
    private JPanel mazePanel;

    public MazeGUI(Maze maze) {
        this.maze = maze;
        this.player = new Player(maze);
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Maze Navigation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);

        mazePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMaze(g);
            }
        };
        mazePanel.addKeyListener(this);
        mazePanel.setFocusable(true);
        mazePanel.requestFocusInWindow(); // Ensure the panel has focus

        add(mazePanel);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void drawMaze(Graphics g) {
        char[][] grid = maze.getGrid();
        int cellSize = Math.min(getWidth(), getHeight()) / Math.max(grid.length, grid[0].length);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int x = j * cellSize;
                int y = i * cellSize;
                if (grid[i][j] == '#') {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, cellSize, cellSize);
                } else if (grid[i][j] == 'S') {
                    g.setColor(Color.GREEN);
                    g.fillRect(x, y, cellSize, cellSize);
                } else if (grid[i][j] == 'E') {
                    g.setColor(Color.RED);
                    g.fillRect(x, y, cellSize, cellSize);
                }
                Maze.Point playerPosition = player.getPosition();
                if (playerPosition.getRow() == i && playerPosition.getCol() == j) {
                    g.setColor(Color.BLUE);
                    g.fillRect(x, y, cellSize, cellSize);
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char direction = ' ';
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: direction = 'U'; break;
            case KeyEvent.VK_DOWN: direction = 'D'; break;
            case KeyEvent.VK_LEFT: direction = 'L'; break;
            case KeyEvent.VK_RIGHT: direction = 'R'; break;
        }
        if (player.move(direction)) {
            mazePanel.repaint();
        }
        if (player.hasReachedEnd()) {
            JOptionPane.showMessageDialog(this, "Congrats! What a thrill that was, huh?");
            maze.regenerateMaze();
            player.reset();
            mazePanel.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Maze maze = new Maze(20, 20); // You can adjust the size as needed
            new MazeGUI(maze);
        });
    }
}