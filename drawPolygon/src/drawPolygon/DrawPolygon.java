package drawPolygon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class DrawPolygon {

    public static void main(String[] args) {
        new DrawPolygon();
    }

    public DrawPolygon() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               
                JFrame frame = new JFrame("Test");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new PolyPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }

        });
    }

    public static class PolyPane extends JPanel {

        private MouseHandler mouseHandler;
        private Path2D currentShape;
        private List<Path2D> lstPloys;
        private Point lastPoint;
        private Point currentPoint;

        public PolyPane() {
            lstPloys = new ArrayList<>();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(300, 300);
        }

        @Override
        public void addNotify() {
            super.addNotify();
            addMouseListener(getMouseHandler());
            addMouseMotionListener(getMouseHandler());
        }

        
        public MouseHandler getMouseHandler() {
            if (mouseHandler == null) {
                mouseHandler = new MouseHandler();
            }
            return mouseHandler;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            if (lastPoint != null) {
                g2d.setColor(Color.RED);
                g2d.fillOval(lastPoint.x - 2, lastPoint.y - 2, 4, 4);
            }
            if (currentShape != null) {
                g2d.setColor(Color.GREEN);
                g2d.draw(currentShape);
                if (lastPoint != null && currentPoint != null) {
                    System.out.println(lastPoint + " - " + currentPoint);
                    g2d.setColor(new Color(255, 0, 0, 64));
                    g2d.draw(new Line2D.Float(lastPoint, currentPoint));
                }
            }
            g2d.setColor(Color.BLACK);
            for (Shape shape : lstPloys) {
                g2d.draw(shape);
            }
            g2d.dispose();
        }

        public class MouseHandler extends MouseAdapter {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (e.getClickCount() == 1) {
                        Point p = e.getPoint();
                        lastPoint = p;
                        if (currentShape == null) {
                            currentShape = new Path2D.Float();
                            currentShape.moveTo(p.x, p.y);
                        } else {
                            currentShape.lineTo(p.x, p.y);
                        }
                        repaint();
                    } else if (e.getClickCount() == 2) {
                        currentShape.closePath();
                        lstPloys.add(currentShape);
                        currentShape = null;
                        lastPoint = null;
                        repaint();
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (currentShape != null) {
                    currentPoint = e.getPoint();
                    repaint();
                } else {
                    currentPoint = null;
                }
            }

        }

    }

}