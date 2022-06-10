package com.aviadl40.gdxutils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;

public abstract class KonamiCodeListener extends GestureDetector.GestureAdapter {
	// Up Up Down Down Left Right A B Start

	private static final byte SEQUENCE_END = 9;

	private byte sequenceTracker = 0;

	protected abstract void onComplete();

	private void reset() {
		sequenceTracker = 0;
	}

	private void stepForward() {
		sequenceTracker++;
		if (sequenceTracker == SEQUENCE_END) {
			onComplete();
			reset();
		}
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		byte s = sequenceTracker;
		if (x < (float) Gdx.graphics.getWidth() / 2 || button == 0) { // A
			if (sequenceTracker == 6)
				stepForward();
		}
		if (x > (float) Gdx.graphics.getWidth() / 2 || button == 1) { // B
			if (sequenceTracker == 7)
				stepForward();
		}
		if (s == sequenceTracker)
			reset();
		return false;
	}

	@Override
	public boolean longPress(float x, float y) { // Start
		if (sequenceTracker == 8)
			stepForward();
		return super.longPress(x, y);
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		byte s = sequenceTracker;
		if (Math.abs(velocityX / velocityY) > 4) { // X
			if (velocityX < 0) // Left
				if (sequenceTracker == 4)
					stepForward();
			if (velocityX > 0) // Right
				if (sequenceTracker == 5)
					stepForward();
		}
		if (Math.abs(velocityY / velocityX) > 4) { // Y
			if (velocityY < 0) // Up
				if (sequenceTracker == 0 || sequenceTracker == 1)
					stepForward();
			if (velocityY > 0) // Down
				if (sequenceTracker == 2 || sequenceTracker == 3)
					stepForward();
		}
		if (s == sequenceTracker)
			reset();
		return false;
	}
}
