package com.aviadl40.gdxutils.actors;

import android.support.annotation.NonNull;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
	private float spacing = 0, calcSpacing = 0;

	private float progress = 0;
	private float animDuration = 0;
	@NonNull
	private Interpolation interpolation = Interpolation.smoother;

	private float collapseAfter = -1;

	private TemporalAction progressAction = new TemporalAction() {
		@Override
		protected void update(float percent) {
			progress = percent;
		}
	};
	private SequenceAction sequence = new SequenceAction();

	/**
	 * @param root Root Button to be used for expanding and collapsing the widget.
	 */
	public ExpandingButton(@NonNull Button root) {
		this.root = root;
		root.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// Listener fires after button is checked
				final boolean expand = ExpandingButton.this.root.isChecked();

				sequence.reset();

				sequence.addAction(restartProgressAction(expand));

				if (expand && collapseAfter >= 0) {
					sequence.addAction(Actions.delay(collapseAfter));
					sequence.addAction(Actions.run(new Runnable() {
						@Override
						public void run() {
							Button root = ExpandingButton.this.root;
							root.setProgrammaticChangeEvents(false);
							root.setChecked(false);
							root.setProgrammaticChangeEvents(true);
							sequence.addAction(restartProgressAction(false));
						}
					}));
				}

				sequence.restart();
				addAction(sequence);
			}
		});
	}

	private TemporalAction restartProgressAction(boolean expand) {
		progressAction.restart();
		progressAction.setDuration(animDuration);
		progressAction.setReverse(!expand);
		return progressAction;
	}

	public boolean hidesRootOnExpand() {
		return hideRootOnExpand;
	}

	/**
	 * Sets whether or not to hide the root Button when the children are shown.
	 */
	public void setHideRootOnExpand(boolean hideRoot) {
		this.hideRootOnExpand = hideRoot;
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	/**
	 * Sets the axis to expand in.
	 *
	 * @param horizontal expands horizontally if true, vertically if false
	 */
	public void setAxis(boolean horizontal) {
		this.horizontal = horizontal;
	}

	public boolean isPositive() {
		return positive;
	}

	/**
	 * Sets whether to expand in the positive direction or the negative.
	 */
	public void setPositive(boolean positive) {
		this.positive = positive;
		invalidate();
	}

	public float getSpacing() {
		return spacing;
	}

	/**
	 * Sets the spacing between children
	 */
	public void setSpacing(float spacing) {
		this.spacing = spacing;
		invalidate();
	}

	public float getDuration() {
		return animDuration;
	}

	/**
	 * Sets the expand/collapse animation duration.
	 */
	public void setDuration(float duration) {
		this.animDuration = duration;
	}

	@NonNull
	public Interpolation getInterpolation() {
		return interpolation;
	}

	/**
	 * Sets the interpolation to use when animating.
	 *
	 * @see Interpolation
	 */
	public void setInterpolation(@NonNull Interpolation interpolation) {
		this.interpolation = interpolation;
	}

	/**
	 * @return State of the Widget between 0-1.
	 * 0 = Fully collapsed
	 * 1 = Fully expanded
	 */
	public float getProgress() {
		return progress;
	}

	public void setProgress(float progress) {
		this.progress = MathUtils.clamp(progress, 0f, 1f);
	}

	public float getCollapseAfter() {
		return collapseAfter;
	}

	/**
	 * Sets how long after expanding should the widget automatically collapse back.
	 * Set to -1 to remain expanded (Default).
	 */
	public void setCollapseAfter(float collapseAfter) {
		this.collapseAfter = collapseAfter;
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
									i > 0 ? calcSpacing : 0
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
									i > 0 ? calcSpacing : 0
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
		calcSpacing = spacing * (positive ? 1 : -1);
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
