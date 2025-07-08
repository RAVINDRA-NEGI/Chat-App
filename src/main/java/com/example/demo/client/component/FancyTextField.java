package com.example.demo.client.component;

import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JTextField;

import com.example.demo.client.ui.Utilities;

public class FancyTextField extends JTextField {
    private final int radius = 20;

    public FancyTextField(int columns) {
        super(columns);
        setOpaque(false);
        setMargin(new Insets(10, 15, 10, 15)); 
        setForeground(Utilities.TEXT_COLOR);
        setFont(new Font("Inter", Font.PLAIN, 18));
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

        
        g2.setColor(Utilities.SEMI_TRASNPARENT);
        g2.fillRoundRect(2, 4, width - 4, height - 4, radius, radius);

       
        GradientPaint gp = new GradientPaint(
            0, 0, Utilities.SECONDARY_COLOR,
            0, height, Utilities.SECONDARY_COLOR.darker()
        );
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, width - 2, height - 2, radius, radius);

        g2.dispose();
        super.paintComponent(g); 
    }

    @Override
    protected void paintBorder(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
			return;
		}

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Utilities.TEXT_COLOR.darker());
        g2.drawRoundRect(0, 0, width - 2, height - 2, radius, radius);
        g2.dispose();
    }

    @Override
    public Insets getInsets() {
        return new Insets(10, 15, 10, 15); 
    }
}
