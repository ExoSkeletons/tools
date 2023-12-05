package com.aviadl40.gdxutils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class ColoredRectangle extends Rectangle {
	public static class RectangleColors {
		public Color
				c0 = new Color(),
				c1 = new Color(),
				c2 = new Color(),
				c3 = new Color();

		public RectangleColors() {
			set(Color.WHITE);
		}

		RectangleColors(RectangleColors colors) {
			set(colors);
		}

		public RectangleColors(Color c0, Color c1, Color c2, Color c3) {
			set(c0, c1, c2, c3);
		}

		public void set(Color c0, Color c1, Color c2, Color c3) {
			this.c0.set(c0);
			this.c1.set(c1);
			this.c2.set(c2);
			this.c3.set(c3);
		}

		public void set(Color c) {
			set(c, c, c, c);
		}

		public void set(RectangleColors colors) {
			set(colors.c0, colors.c1, colors.c2, colors.c3);
		}

		public void set(float r, float g, float b, float a) {
			c0.set(r, g, b, a);
			set(c0);
		}
	}

	private RectangleColors colors = new RectangleColors();

	public ColoredRectangle(Color color) {
		super();
		colors.set(color);
	}

	public ColoredRectangle() {
		this(Color.WHITE);
	}

	public ColoredRectangle(float x, float y, float width, float height, Color color) {
		super(x, y, width, height);
		setColor(color);
	}

	public ColoredRectangle(float x, float y, float width, float height) {
		this(x, y, width, height, Color.WHITE);
	}

	public ColoredRectangle(Rectangle rect, Color color) {
		super(rect);
		setColor(color);
	}

	public ColoredRectangle(Rectangle rect) {
		this(rect, Color.WHITE);
	}

	public ColoredRectangle(ColoredRectangle rect) {
		super(rect);
		colors.set(rect.colors);
	}

	public RectangleColors getColors() {
		return colors;
	}

	public void setColors(RectangleColors colors) {
		this.colors.set(colors);
	}

	private void setColors(Color c0, Color c1, Color c2, Color c3) {
		colors.set(c0, c1, c2, c3);
	}

	public void setColor(Color color) {
		colors.set(color);
	}
}
