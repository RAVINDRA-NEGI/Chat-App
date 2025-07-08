package com.example.demo.client.component;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class RoundedPanel extends JPanel {
    private final int cornerRadius;

    public RoundedPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false);  // So we can draw our own background
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
			return;
		}

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        
       
       
        GradientPaint gradient = new GradientPaint(
            0, 0, getBackground().brighter(),
            0, height, getBackground().darker()
        );
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, width - 4, height - 4, cornerRadius, cornerRadius);

       
        g2.setColor(getBackground().darker().darker());
        g2.drawRoundRect(0, 0, width - 4, height - 4, cornerRadius, cornerRadius);

        g2.dispose();

        super.paintComponent(g); 
    }

    
}
