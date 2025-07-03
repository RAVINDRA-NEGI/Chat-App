package com.example.demo.client;

import java.awt.Color;

import javax.swing.border.EmptyBorder;

public class Utilitities {
	
	public static final Color TRANSPARENT_COLOR = new Color(0);
	public static final Color PRIMARY_COLOR = Color.decode("#2F2D2D");
	public static final Color SECOUNDARY_COLOR = Color.decode("#484444");
	public static final Color Text_Color = Color.WHITE;
	
	public static EmptyBorder addPadding(int top , int left , int bottom , int right) {
		return new EmptyBorder(top , left , bottom , right ); 
	}
}
