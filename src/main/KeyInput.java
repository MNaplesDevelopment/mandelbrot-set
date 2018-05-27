package main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
    private MandelbrotSet tower;

    public KeyInput(MandelbrotSet tower) {
        this.tower = tower;
    }

    public void keyPressed(KeyEvent e) {
        tower.keyPressed(e);
    }
}
