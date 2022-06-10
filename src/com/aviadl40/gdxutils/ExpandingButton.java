package com.aviadl40.gdxutils;

import android.support.annotation.NonNull;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

public class ExpandingButton extends WidgetGroup {
	@NonNull
	private final Button root;

	private boolean hideRootOnExpand = false;
	private boolean horizontal = true, positive = true; // +/-
	private float sparsity = 0, calcSparsity = 0;

	private float progress = 0;
	private float animDuration = 0, collapseAfter = -1;
	@NonNull
	private Interpolation interpolation = Interpolation.smoother;

	private TemporalAction progressAction = new TemporalAction() {
		@Override
		protected void update(float percent) {
			progress = percent;
		}
	};
	private DelayAction delayAction = new DelayAction();
	private SequenceAction sequence = new SequenceAction();

	public ExpandingButton(@NonNull Button root) {
		this.root = root;
		root.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Actor listenerActor = event.getListenerActor();
				if (!(listenerActor instanceof Button)) return;
				Button root = (Button) listenerActor;

				// Listener fires after button is checked
				boolean expand = root.isChecked();

				root.removeAction(sequence);
				sequence.reset();

				progressAction.restart();
				progressAction.setDuration(animDuration);
				progressAction.setReverse(!expand);
				sequence.addAction(progressAction);

				if (expand && collapseAfter >= 0) {
					delayAction.setDuration(collapseAfter);
					delayAction.restart();
					sequence.addAction(delayAction);
					sequence.addAction(Actions.run(new Runnable() {
						@Override
						public void run() {
							if (delayAction.getDuration() >= 0) collapse();
						}
					}));
				}

				sequence.restart();
				addAction(sequence);
			}
		});
	}

	public boolean hidesRootOnExpand() {
		return hideRootOnExpand;
	}

	public void setHideRootOnExpand(boolean hideRoot) {
		this.hideRootOnExpand = hideRoot;
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}

	public boolean isPositive() {
		return positive;
	}

	public void setPositive(boolean positive) {
		this.positive = positive;
		invalidate();
	}

	public float getSparsity() {
		return sparsity;
	}

	public void setSparsity(float sparsity) {
		this.sparsity = sparsity;
		invalidate();
	}

	public float getDuration() {
		return animDuration;
	}

	public void setDuration(float duration) {
		this.animDuration = duration;
	}

	public float getCollapseAfter() {
		return collapseAfter;
	}

	public void setCollapseAfter(float collapseAfter) {
		this.collapseAfter = collapseAfter;
	}

	@NonNull
	public Interpolation getInterpolation() {
		return interpolation;
	}

	public void setInterpolation(@NonNull Interpolation interpolation) {
		this.interpolation = interpolation;
	}

	public float getProgress() {
		return progress;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	public void expand() {
		root.setChecked(true);
	}

	public void collapse() {
		root.setChecked(false);
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (progress == 0)
			return root.hit(x, y, touchable);
		if (progress == 1) {
			Actor hit = super.hit(x, y, touchable);
			if (hit != null) return hit;
			if (!hideRootOnExpand) return root.hit(x, y, touchable);
		}
		return null;
	}

	@Override
	public void act(float delta) {
		Array<Actor> children = getChildren();
		Actor c, prev = children.first();

		prev.setPosition(0, 0);

		float childX, childY, alignedX, alignedY;
		for (int i = 0; i < children.size; i++) {
			c = children.get(i);

			alignedX = root.getX() + (root.getWidth() * root.getScaleX() - c.getWidth() * c.getScaleX()) / 2;
			alignedY = root.getY() + (root.getHeight() * root.getScaleY() - c.getHeight() * c.getScaleY()) / 2;

			if (hideRootOnExpand && i == 0) {
				childX = alignedX;
				childY = alignedY;
			} else {
				if (horizontal) {
					childX = prev.getX() + interpolation.apply(
							0,
							(
									positive
											? prev.getWidth() * prev.getScaleX()
											: -c.getWidth() * c.getScaleX()
							) + (
									i > 0 ? calcSparsity : 0
							),
							progress
					);
					childY = alignedY;
				} else {
					childX = alignedX;
					childY = prev.getY() + interpolation.apply(
							0,
							(
									positive
											? prev.getHeight() * prev.getScaleY()
											: -c.getHeight() * c.getScaleY()
							) + (
									i > 0 ? calcSparsity : 0
							),
							progress
					);
				}
			}

			c.setPosition(childX, childY);

			/*/
			if (positive) {
				deltaX = horizontal ? sparsity + prev.getWidth() * prev.getScaleX() : 0;
				deltaY = horizontal ? 0 : sparsity + prev.getHeight() * prev.getScaleY();
			} else {
				deltaX = horizontal ? -sparsity - c.getWidth() * c.getScaleX() : 0;
				deltaY = horizontal ? 0 : sparsity - c.getHeight() * c.getScaleY();
			}

			c.setPosition(
					horizontal ? prev.getX() + interpolation.apply(0, deltaX, progress) : alignedX,
					!horizontal ? prev.getY() + interpolation.apply(0, deltaY, progress) : alignedY
			); //*/

			prev = c;
		}

		super.act(delta);
	}

	@Override
	public void layout() {
		calcSparsity = sparsity * (positive ? 1 : -1);
		super.layout();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		validate();
		if (isTransform()) applyTransform(batch, computeTransform());
		parentAlpha *= getColor().a;
		root.draw(
				batch,
				parentAlpha *
						interpolation.apply(1 - (progress * (hideRootOnExpand ? 1 : .5f)))
		);
		drawChildren(batch, parentAlpha);
		if (isTransform()) resetTransform(batch);
	}

	@Override
	protected void drawChildren(Batch batch, float parentAlpha) {
		super.drawChildren(batch, parentAlpha * interpolation.apply(progress));
	}

	@Override
	public void setWidth(float width) {
		root.setWidth(width);
	}

	@Override
	public void setHeight(float height) {
		root.setHeight(height);
	}

	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
		root.setSize(width, height);
	}

	@Override
	public void sizeBy(float size) {
		super.sizeBy(size);
		root.sizeBy(size);
	}

	@Deprecated
	public void sizeBy(float width, float height) {
		super.sizeBy(width, height);
		root.sizeBy(width, height);
	}

	@Override
	public float getPrefWidth() {
		if (progress > 0 && horizontal) {
			Actor last = getChild(getChildren().size - 1);
			if (positive)
				return last.getX() + last.getWidth() * last.getScaleX();
			return Math.abs(last.getX());
		}
		return getWidth();
	}

	@Override
	public float getPrefHeight() {
		if (progress > 0 && !horizontal) {
			Actor last = getChild(getChildren().size - 1);
			if (positive)
				return last.getY() + last.getHeight() * last.getScaleY();
			return Math.abs(last.getY());
		}
		return getHeight();
	}
}
