package com.example.demo.client;

import javax.swing.*;

import com.example.demo.util.Utilities;

import java.awt.*;

public class FancyTextField extends JTextField {
    private int radius = 20;

    public FancyTextField(int columns) {
        super(columns);
        setOpaque(false); // So we can paint the background ourselves
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Padding
        setForeground(Utilities.TEXT_COLOR);
        setFont(new Font("Inter", Font.ROMAN_BASELINE, 18));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Antialiasing for smooth corners and shadows
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw shadow
        g2.setColor(new Color(0, 0, 0, 30)); // light transparent shadow
        g2.fillRoundRect(2, 4, width - 4, height - 4, radius, radius);

        // Gradient background
        GradientPaint gp = new GradientPaint(0, 0, Utilities.SECOUNDARY_COLOR,
                                             0, height, Utilities.SECOUNDARY_COLOR.darker());
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, width - 2, height - 2, radius, radius);

        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Utilities.TEXT_COLOR.darker());
        g2.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2, radius, radius);

        g2.dispose();
    }
}
