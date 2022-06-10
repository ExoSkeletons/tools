package com.aviadl40.gdxutils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class ColoredRectangle extends Rectangle {
	private Color color;

	public ColoredRectangle(Color color) {
		super();
		this.color = color;
	}

	public ColoredRectangle() {
		this(Color.WHITE);
	}

	public ColoredRectangle(float x, float y, float width, float height, Color color) {
		super(x, y, width, height);
		this.color = color;
	}

	public ColoredRectangle(float x, float y, float width, float height) {
		this(x, y, width, height, Color.WHITE);
	}

	public ColoredRectangle(Rectangle rect, Color color) {
		super(rect);
		this.color = color;
	}

	public ColoredRectangle(Rectangle rect) {
		this(rect, Color.WHITE);
		color = Color.WHITE;
	}

	public ColoredRectangle(ColoredRectangle rect) {
		super(rect);
		color = rect.color;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
