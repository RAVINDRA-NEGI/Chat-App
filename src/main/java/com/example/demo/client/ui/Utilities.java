package com.example.demo.client.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class Utilities {

    
    public static final Color PRIMARY_COLOR = new Color(30, 30, 47);         
    public static final Color SECONDARY_COLOR = new Color(46, 46, 62);     
    public static final Color ACCENT_COLOR = new Color(78, 159, 61);          
    public static final Color FIRST_COLOR = new Color(100, 149, 237);    
    public static final Color SECOND_COLOR = new Color(60, 63, 65);          
    public static final Color TEXT_COLOR = new Color(255, 255, 255);          
    public static final Color MUTED_TEXT_COLOR = new Color(170, 170, 170);    
    public static final Color ERROR_COLOR = new Color(255, 76, 76);           
    public static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
    public static final Color GREEN_COLOR= new Color(0, 200, 0);
    public static final Color GREY_COLOR= new Color(180, 180, 180);
    public static final Color SEMI_TRASNPARENT= new Color(0, 0, 0, 30);
   
    
 
    public static final Font HEADER_FONT = new Font("Inter", Font.BOLD, 18);
    public static final Font MESSAGE_FONT = new Font("Inter", Font.PLAIN, 18);
    public static final Font INPUT_FONT = new Font("Inter", Font.ROMAN_BASELINE, 18);
    public static final Font USERNAME_FONT = new Font("Inter", Font.BOLD, 16);

   
    public static Border addPadding(int top, int left, int bottom, int right) {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }  
    public static final double BUBBLE_WIDTH_RATIO = 0.6;
    public static final int MAX_BUBBLE_WIDTH = 480; 
    public static final int MIN_BUBBLE_WIDTH = 200; 

    
}
