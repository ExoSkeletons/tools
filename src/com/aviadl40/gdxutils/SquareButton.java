package com.aviadl40.gdxutils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

@SuppressWarnings({"SuspiciousNameCombination", "deprecation"})
public class SquareButton extends Button {
	public SquareButton(Skin skin) {
		super(skin);
	}

	public SquareButton(Skin skin, String styleName) {
		super(skin, styleName);
	}

	public SquareButton(Actor child, Skin skin, String styleName) {
		super(child, skin, styleName);
	}

	public SquareButton(Actor child, ButtonStyle style) {
		super(child, style);
	}

	public SquareButton(ButtonStyle style) {
		super(style);
	}

	public SquareButton() {
		super();
	}

	public SquareButton(Drawable up) {
		super(up);
	}

	public SquareButton(Drawable up, Drawable down) {
		super(up, down);
	}

	public SquareButton(Drawable up, Drawable down, Drawable checked) {
		super(up, down, checked);
	}

	public SquareButton(Actor child, Skin skin) {
		super(child, skin);
	}

	public float getSize() {
		return getWidth();
	}

	public void setSize(float size) {
		setSize(size, size);
	}

	public float getScale() {
		return getScaleX();
	}

	@Override
	@Deprecated
	public void setWidth(float width) {
		setSize(width, width);
	}

	@Override
	@Deprecated
	public void setHeight(float height) {
		setSize(height, height);
	}

	@Override
	@Deprecated
	public void setSize(float width, float height) {
		super.setSize(width, width);
	}

	@Override
	@Deprecated
	public void sizeBy(float width, float height) {
		super.sizeBy(width, width);
	}

	@Override
	public void setScaleX(float scaleX) {
		setScale(scaleX);
	}

	@Override
	@Deprecated
	public void setScaleY(float scaleY) {
		setScale(scaleY);
	}

	@Override
	@Deprecated
	public void scaleBy(float scaleX, float scaleY) {
		scaleBy(scaleX);
	}
}
