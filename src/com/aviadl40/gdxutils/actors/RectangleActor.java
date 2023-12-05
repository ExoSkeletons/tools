package com.aviadl40.gdxutils.actors;

import com.aviadl40.gdxutils.ColoredRectangle.RectangleColors;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class RectangleActor extends Actor implements Disposable {
	private final ShapeRenderer sr = new ShapeRenderer();

	public RectangleColors colors = new RectangleColors();


	public RectangleActor(Color c0, Color c1, Color c2, Color c3) {
		colors.set(c0, c1, c2, c3);
	}

	public RectangleActor(Color color) {
		this(color, color, color, color);
	}

	public RectangleActor() {
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
		// multiply alphas
		final Color
				mc0 = colors.c0.cpy().mul(1, 1, 1, parentAlpha),
				mc1 = colors.c1.cpy().mul(1, 1, 1, parentAlpha),
				mc2 = colors.c2.cpy().mul(1, 1, 1, parentAlpha),
				mc3 = colors.c3.cpy().mul(1, 1, 1, parentAlpha);
		// draw
		sr.rect(getX(), getY(), getWidth(), getHeight(), mc0, mc1, mc2, mc3);
	}

	public void setColors(Color c0, Color c1, Color c2, Color c3) {
		colors.set(c0, c1, c2, c3);
		super.setColor(colors.c0);
	}

	public void setColors(RectangleColors colors) {
		setColors(colors.c0, colors.c1, colors.c2, colors.c3);
	}

	@Override
	public void setColor(Color color) {
		super.setColor(color);
		colors.set(color);
	}

	@Override
	public void setColor(float r, float g, float b, float a) {
		super.setColor(r, g, b, a);
		colors.set(r, g, b, a);
	}

	@Override
	public void dispose() {
		sr.dispose();
	}
}
