package com.aviadl40.gdxutils.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class RectangleActor extends Actor implements Disposable {
	private final ShapeRenderer sr = new ShapeRenderer();

	private final Color
			c0 = new Color(),
			c1 = new Color(),
			c2 = new Color(),
			c3 = new Color();

	public RectangleActor(Color color) {
		setColor(color);
	}

	public RectangleActor(Color c0, Color c1, Color c2, Color c3) {
		setColors(c0, c1, c2, c3);
	}

	public RectangleActor() {
	}

	public Color getC0() {
		return c0;
	}

	public void setC0(Color c0) {
		this.c0.set(c0);
	}

	public Color getC1() {
		return c1;
	}

	public void setC1(Color c1) {
		this.c1.set(c1);
	}

	public Color getC2() {
		return c2;
	}

	public void setC2(Color c2) {
		this.c2.set(c2);
	}

	public Color getC3() {
		return c3;
	}

	public void setC3(Color c3) {
		this.c3.set(c3);
	}

	public void setColors(Color c0, Color c1, Color c2, Color c3) {
		setC0(c0);
		setC1(c1);
		setC2(c2);
		setC3(c3);
	}

	@Override
	@Deprecated
	public final void draw(Batch batch, float parentAlpha) {
		batch.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sr.setProjectionMatrix(batch.getProjectionMatrix());
		sr.setTransformMatrix(batch.getTransformMatrix());
		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.setColor(batch.getColor());
		draw(sr, parentAlpha);
		sr.end();
		batch.begin();
	}

	public void draw(ShapeRenderer sr, float parentAlpha) {
		sr.rect(
				getX(), getY(), getWidth(), getHeight(),
				getC0().cpy().mul(1, 1, 1, parentAlpha),
				getC1().cpy().mul(1, 1, 1, parentAlpha),
				getC2().cpy().mul(1, 1, 1, parentAlpha),
				getC3().cpy().mul(1, 1, 1, parentAlpha)
		);
	}

	@Override
	public void dispose() {
		sr.dispose();
	}

	@Override
	public final Color getColor() {
		return getC0();
	}

	@Override
	public void setColor(Color color) {
		setColors(color, color, color, color);
	}

	@Override
	public void setColor(float r, float g, float b, float a) {
		setColor(new Color(r, g, b, a));
	}
}
