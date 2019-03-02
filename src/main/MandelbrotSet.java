package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JComponent;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class MandelbrotSet extends JComponent  {
	private static final long serialVersionUID = 1L;
    private static final int WIDTH = 900;
    private static final int HEIGHT = 900;

    private int iterations = 50;
    private float saturation = 0.65f;
    private double scale = 1d;
    private float hueOffset = 1f;
    private double horizontalOffset = 0.0d;
    private double verticalOffset = 0.0d;
    private float horOffInc = 0.1f;
    private float vertOffInc = 0.1f;
    private double viewportMinX = -2.5d;
    private double viewportMinY = -2.5d;
    private int breakPoint = 4;

    private double cxMin;
    private double cxMax;
    private double cyMin;
    private double cyMax;

    private boolean showGUI = true;

    private JFrame frame;

    private BufferedImage buffer;

    public static void main(String args[])  {
        new MandelbrotSet();
    }

    public MandelbrotSet()  {
        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        renderSet();

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));

        frame = new JFrame("Mandelbrot Set");
        frame.getContentPane().add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        this.setFocusable(true);
        this.requestFocus();
        addKeyListener(new KeyInput(this));
    }

    public void renderSet() {
        double w = (double)WIDTH;
        double h = (double)HEIGHT;
        double hO = horizontalOffset;
        double vO = verticalOffset;
        double s = scale;
        double vmx = viewportMinX;
        double vmy = viewportMinY;
        int color;

        cxMin = ((vmx + ((0 / w) * 5))/s)+hO;
        cxMax = ((vmx + ((w / w) * 5))/s)+hO;
        cyMin = ((vmy + ((0 / h) * 5))/s)+vO;
        cyMax = ((vmy + ((h / h) * 5))/s)+vO;

        for(int x = 0; x < WIDTH; x++)  {
            for(int y = 0; y < HEIGHT; y++) { // A magic line of code that took way too long to figure out
                color = calculatePoint((((vmx + ((x / w) * 5))/s)+hO), (((vmy + ((y / h) * 5))/s)+vO));
                buffer.setRGB((int)x, (int)y, color);
            }
        }
    }

    public int calculatePoint(double x, double y) {
        double cx, cy;
        cx = x;
        cy = y;
        int i = 0;

        for(; i < iterations; i++) {
            double nx = x*x - y*y + cx;
            double ny = 2 * x * y + cy;

            x = nx;
            y = ny; 

            if(x*x + y*y > breakPoint)   {
                break;
            }
        }
        
        if(i == iterations) {
            return 0x00000000;
        }
        else    {
            return Color.HSBtoRGB((float)i / iterations + (float)hueOffset, saturation, 1);
        }
    }

    @Override
    public void paint(Graphics g)    {
        g.drawImage(buffer, 0, 0, null);
        if(showGUI) {
            g.setColor(new Color(0.2f, 0.2f, 0.2f, 0.3f));
            g.fillRect(30, 10, 450, 120);
            g.setColor(new Color(255, 255, 255));
            String out1 = "cxMin: " + cxMin + "  cxMax: " + cxMax + "  cyMin: " + cyMin + "i  cyMax: " + cyMax+"i";
            String out2 = "Horizontal offset increment: " + horOffInc + "  Vertical offset Increment: " + vertOffInc;
            String out3 = "Iterations: " + iterations + "  Accuracy: " + breakPoint;
            String out4 = "Color: " + hueOffset + "  Saturation: " + saturation;
            double zoomLvl = 1 / scale;
            String out5 = "Zoom level: " + zoomLvl;
            g.drawString(out1, 40, 40);
            g.drawString(out2, 40, 60);
            g.drawString(out3, 40, 80);
            g.drawString(out4, 40, 100);
            g.drawString(out5, 40, 120);
        }
    }

    public void keyPressed(KeyEvent e)  {
        int k = e.getKeyCode();
        switch(k)   {
            /**
             * Movement
             */
            case KeyEvent.VK_RIGHT:
            horizontalOffset += horOffInc;
            repaint();
            renderSet();
            break;
            case KeyEvent.VK_LEFT:
            horizontalOffset -= horOffInc;
            repaint();
            renderSet();
            break;
            case KeyEvent.VK_UP:
            verticalOffset -= vertOffInc;
            repaint();
            renderSet();
            break;
            case KeyEvent.VK_DOWN:
            verticalOffset += vertOffInc;
            repaint();
            renderSet();
            break;
            /**
             * Zoom
             */
            case KeyEvent.VK_PERIOD:
            scale *= 2;
            horOffInc /= 2f;
            vertOffInc /= 2f;
            repaint();
            renderSet();
            break;
            case KeyEvent.VK_COMMA:
            scale /= 2;
            horOffInc *= 2;
            vertOffInc *= 2;
            repaint();
            renderSet();
            break;
            /**
             * Hue Offset
             */
            case KeyEvent.VK_K:
            hueOffset -= 0.01f;
            repaint();
            renderSet();
            break;
            case KeyEvent.VK_L:
            hueOffset += 0.01f;
            repaint();
            renderSet();
            break;
            /**
             * Horizontal Offset
             */
            case KeyEvent.VK_F:
            horOffInc /= 10.0f;
            repaint();
            break;
            case KeyEvent.VK_R:
            horOffInc *= 10.0f;
            repaint();
            break;
            /**
             * Vertical Offset
             */
            case KeyEvent.VK_G:
            vertOffInc /= 10f;
            repaint();
            break;
            case KeyEvent.VK_T:
            vertOffInc *= 10f;
            repaint();
            break;
            /**
             * Iterations
             */
            case KeyEvent.VK_J:
            iterations -= 25;
            repaint();
            renderSet();
            break;
            case KeyEvent.VK_U:
            iterations += 25;
            repaint();
            renderSet();
            break;
            /**
             * Saturation
             */
            case KeyEvent.VK_I:
            saturation -= 0.05f;
            if(saturation <= 0){ saturation = 0.1f; }
            repaint();
            renderSet();
            break;
            case KeyEvent.VK_O:
            saturation += 0.05f;
            if(saturation  >= 1){ saturation = 1f; }
            repaint();
            renderSet();
            break;
            /**
             * Accuracy
             */
            case KeyEvent.VK_Y:
            breakPoint *= 2;
            repaint();
            renderSet();
            break;
            case KeyEvent.VK_H:
            breakPoint /= 2;
            if(breakPoint <= 2) { breakPoint = 2; }
            renderSet();
            repaint();
            break;
            /**
             * Utilities
             */
            case KeyEvent.VK_P:
            save();
            break;
            case KeyEvent.VK_C:
            reset();
            repaint();
            renderSet();
            break;
            case KeyEvent.VK_Z:
            showGUI = !showGUI;
            repaint();
            break;
        }
    }

    public void reset() {
        iterations = 50;
        saturation = .65f;
        scale = 1;
        hueOffset = 1f;
        horizontalOffset = 0.0f;
        verticalOffset = 0.0f;
        horOffInc = 0.1f;
        vertOffInc = 0.1f;
        viewportMinX = -2.5f;
        viewportMinY = -2.5f;
        breakPoint = 16;
    }

    public void save()  {
        try{
            File out = new File("pic.png");
            ImageIO.write(buffer, "png", out);
        }
        catch(IOException e)    {
            e.printStackTrace();
        }
    }
}
