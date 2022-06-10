package com.aviadl40.gdxutils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimationActor extends Image {
	private Animation<TextureRegionDrawable> animation = null;

	private float time = 0;
	private boolean paused = false;

	public AnimationActor(Animation<TextureRegionDrawable> animation) {
		setAnimation(animation, true);
	}

	public AnimationActor() {
		this(null);
	}

	public Animation<TextureRegionDrawable> getAnimation() {
		return animation;
	}

	public void setAnimation(final Animation<TextureRegionDrawable> a) {
		setAnimation(a, true);
	}

	public void setAnimation(final Animation<TextureRegionDrawable> a, final boolean restart) {
		Animation old = animation;
		animation = a;
		if (restart) restartAnimation();
		if (old != a) frameChanged();
		updateDrawable();
	}

	public float getTime() {
		return time;
	}

	public final int getAnimationFrameIndex() {
		return animation == null ? 0 : animation.getKeyFrameIndex(getTime());
	}

	public void setAnimationFrameIndex(final int frame) {
		setAnimationTime(animation == null ? 0 : frame * animation.getFrameDuration());
	}

	public float getAnimationDuration() {
		if (animation == null) return 0;
		return animation.getAnimationDuration();
	}

	public void setAnimationTime(final float time) {
		this.time = time;
	}

	public void setAnimationPlayMode(Animation.PlayMode playMode) {
		animation.setPlayMode(playMode);
	}

	public void restartAnimation() {
		time = 0;
	}

	public void pauseAnimation(boolean pause) {
		paused = pause;
	}

	public boolean isAnimationFinished() {
		return (animation != null) && animation.isAnimationFinished(time);
	}

	protected void frameChanged() {
	}

	private void updateDrawable() {
		setDrawable(GdxUtils.hasFrame(animation, time) ? animation.getKeyFrame(time) : null);
		layout();
	}

	@Override
	public float getPrefWidth() {
		return animation == null ? 0f : animation.getKeyFrames()[0].getMinWidth();
	}

	@Override
	public float getPrefHeight() {
		return animation == null ? 0f : animation.getKeyFrames()[0].getMinHeight();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		float temp = time;
		if (!paused)
			setAnimationTime(time + delta);
		if (GdxUtils.hasFrame(animation, time) && animation.getKeyFrameIndex(temp) != animation.getKeyFrameIndex(time))
			frameChanged();
		updateDrawable();
	}
}
