package com.example.demo.client.ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

public class Utilities {

    // 🎨 Colors
    public static final Color PRIMARY_COLOR = new Color(30, 30, 47);          // #1E1E2F
    public static final Color SECOUNDARY_COLOR = new Color(46, 46, 62);       // #2E2E3E
    public static final Color ACCENT_COLOR = new Color(78, 159, 61);          // #4E9F3D
    public static final Color FIRST_COLOR = new Color(100, 149, 237);    // #FFCC00
    public static final Color SECOND_COLOR = new Color(60, 63, 65);          // #FFA500
    public static final Color TEXT_COLOR = new Color(255, 255, 255);          // #FFFFFF
    public static final Color MUTED_TEXT_COLOR = new Color(170, 170, 170);    // #AAAAAA
    public static final Color ERROR_COLOR = new Color(255, 76, 76);           // #FF4C4C
    public static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
    public static final Color GREEN_COLOR= new Color(0, 200, 0);
    
    // 🖋️ Fonts
    public static final Font HEADER_FONT = new Font("Inter", Font.BOLD, 18);
    public static final Font MESSAGE_FONT = new Font("Inter", Font.PLAIN, 18);
    public static final Font INPUT_FONT = new Font("Inter", Font.ROMAN_BASELINE, 18);
    public static final Font USERNAME_FONT = new Font("Inter", Font.BOLD, 16);

    // 🧱 Padding utility
    public static Border addPadding(int top, int left, int bottom, int right) {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }
}
