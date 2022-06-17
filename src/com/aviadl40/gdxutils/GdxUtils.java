package com.aviadl40.gdxutils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aviadl40.gdxutils.listeners.AssetLoadListener;
import com.aviadl40.utils.Extension;
import com.aviadl40.utils.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.XmlReader;

public final class GdxUtils {
	public static boolean hasFrame(Animation<TextureRegionDrawable> animation, float time) {
		return animation != null && !(animation.getPlayMode() == Animation.PlayMode.NORMAL && time < 0);
	}

	/**
	 * Creates a TextureRegionDrawable animation from a Texture,
	 * by splitting the Texture into TextureRegions of the given width and height.
	 *
	 * @param tx            Texture to split
	 * @param splitW        Width of final TextureRegion, used for splitting
	 * @param splitH        Height of final TextureRegion, used for splitting
	 * @param frameCount    Number of frames to use, <= 0 to use all
	 * @param frameDuration Animation frame duration
	 * @param pm            Animation PlayMode
	 * @return Result animation
	 * @throws IllegalArgumentException If failed to split using the given width and height.
	 * @see Texture
	 * @see TextureRegion
	 * @see Animation
	 * @see Animation.PlayMode
	 */
	@NonNull
	public static Animation<TextureRegionDrawable> buildAnimation(@NonNull Texture tx, int splitW, int splitH, int frameCount, float frameDuration, Animation.PlayMode pm) {
		TextureRegionDrawable[] frames;
		TextureRegion[][] tr;
		Animation<TextureRegionDrawable> animation;

		tx.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

		// Split texture into regions
		tr = TextureRegion.split(tx, splitW, splitH);
		// Place the regions into a 1D array, starting from the top left, going across.
		frames = new TextureRegionDrawable[frameCount <= 0 ? ((tx.getWidth() / splitW) * (tx.getHeight() / splitH)) : frameCount];
		for (int i = 0, index = 0; i < tx.getHeight() / splitH && index < frames.length; i++)
			for (int j = 0; j < tx.getWidth() / splitW && index < frames.length; j++, index++) {
				frames[index] = new TextureRegionDrawable(tr[i][j]);
			}

		if (frames.length == 0)
			throw new IllegalArgumentException("could not build " + splitW + "x" + splitH + " frames out of " + tx.getWidth() + "x" + tx.getHeight() + " sheet.");

		if (tx.getWidth() % splitW > 0 || tx.getHeight() % splitH > 0)
			System.err.println("warning: " + tx.getWidth() + "x" + tx.getHeight() + " sheet does not fully split into [" + splitW + "x" + splitH + "] frames.");

		animation = new Animation<>(frameDuration, frames);
		animation.setPlayMode(pm);
		return animation;

	}

	/**
	 * Creates a TextureRegionDrawable animation from an asset png file,
	 * by splitting the Texture into TextureRegions based on the given width and height.
	 *
	 * @param assetManager  Asset manager to get asset from
	 * @param assetPath     Path to the asset file
	 * @param splitW        Width of final TextureRegion, used for splitting
	 * @param splitH        Height of final TextureRegion, used for splitting
	 * @param frameCount    Number of frames to use, <= 0 to use all
	 * @param frameDuration Animation frame duration
	 * @param pm            Animation PlayMode
	 * @return Result animation, null if asset does not exist.
	 * @see AssetManager
	 * @see TextureRegion
	 * @see Animation
	 * @see Animation.PlayMode
	 */
	@Nullable
	public static Animation<TextureRegionDrawable> buildAnimation(@NonNull AssetManager assetManager, @NonNull String assetPath, int splitW, int splitH, int frameCount, float frameDuration, @NonNull Animation.PlayMode pm) {
		FileHandle file = Gdx.files.internal(assetPath + Extension.png);

		System.out.println(assetPath + " (" + ((file.exists()) ? "FOUND" : "MISSING") + ") " + splitW + "x" + splitH + " " + pm.name());

		if (!assetManager.isLoaded(file.path(), Texture.class))
			return null;

		return buildAnimation(assetManager.get(file.path(), Texture.class), splitW, splitH, frameCount, frameDuration, pm);
	}

	/**
	 * Load all files and sub-files recursively from a given directory into the given AssetManager.
	 *
	 * @param assetManager      Asset manager to use for loading.
	 * @param dir               Target directory.
	 * @param extension         {@link Extension} to use for file filtering, leave null for all files.
	 * @param type              Class type of asset to load.
	 * @param assetLoadListener {@link AssetLoadListener} to fire for each file loaded
	 */
	public static <T> void loadAssetFilesRecursively(@NonNull AssetManager assetManager, @NonNull FileHandle dir, @Nullable Extension extension, @NonNull Class<T> type, @Nullable AssetLoadListener assetLoadListener) {
		for (FileHandle file : dir.list())
			if (file.isDirectory())
				loadAssetFilesRecursively(assetManager, file, extension, type, assetLoadListener);
			else if (extension == null || file.extension().equals(extension.name())) {
				String path = file.path();
				if (assetLoadListener != null) assetLoadListener.onAssetLoad(file, type);
				if (!assetManager.isLoaded(path, type)) assetManager.load(path, type);
			}
	}

	public static void centerX(Actor actor, float XMin, float XMax) {
		actor.setX(XMin + ((XMax - XMin) - actor.getWidth() * actor.getScaleX()) / 2);
	}

	public static void centerY(Actor actor, float YMin, float YMax) {
		actor.setY(YMin + ((YMax - YMin) - actor.getHeight() * actor.getScaleY()) / 2);
	}

	public static void centerXY(Actor actor, float XMin, float XMax, float YMin, float YMax) {
		centerX(actor, XMin, XMax);
		centerY(actor, YMin, YMax);
	}

	public static void centerX(Actor actor) {
		centerX(actor, 0, Gdx.graphics.getWidth());
	}

	public static void centerY(Actor actor) {
		centerY(actor, 0, Gdx.graphics.getHeight());
	}

	public static void centerXY(Actor actor) {
		centerXY(actor, 0, Gdx.graphics.getWidth(), 0, Gdx.graphics.getHeight());
	}

	public static boolean isOnScreen(Actor a) {

			/*

              H  |    H    |  H
            -----+---------+-----
              H  |    V    |  H
            -----+---------+-----
              H  |    H    |  H

            */

		float width = Gdx.graphics.getWidth(), height = Gdx.graphics.getHeight();

		float x = a.getX();
		float xm = x + a.getWidth() * a.getScaleX();

		float y = a.getY();
		float ym = y + a.getHeight() * a.getScaleY();

		return ((x >= 0 && x <= width) && (xm >= 0 && xm <= width))
				&& ((y >= 0 && y <= height) && (ym >= 0 && ym <= height));
	}

	public static void setAlignmentAll(SelectBox selectBox, int align) {
		selectBox.setAlignment(align);
		selectBox.getList().setAlignment(align);
	}

	public static void performClick(Actor actor) {
		Array<EventListener> listeners = actor.getListeners();
		for (int i = 0; i < listeners.size; i++)
			if (listeners.get(i) instanceof ClickListener)
				((ClickListener) listeners.get(i)).clicked(null, 0, 0);
	}

	public static float getFiniteFloatAttribute(XmlReader.Element e, String name, float defaultValue) {
		return Utils.finite(e.getFloatAttribute(name, Float.NaN), defaultValue);
	}

	public static void paste(TextField textField, String text) {
		int i = textField.getCursorPosition();
		textField.cut();
		textField.setText(textField.getText().substring(0, textField.getCursorPosition()) + text + textField.getText().substring(textField.getCursorPosition()));
		textField.setCursorPosition(i + text.length());
	}

	@SafeVarargs
	public static <V, A extends Actor, N extends Tree.Node<N, V, A>, T extends Tree<N, V>> T addAll(T t, N... nodes) {
		for (N node : nodes)
			t.add(node);
		return t;
	}

	@SafeVarargs
	public static <V, A extends Actor, N extends Tree.Node<N, V, A>, T extends Tree<N, V>> N addAll(N n, N... nodes) {
		for (N node : nodes)
			n.add(node);
		return n;
	}

	public static void resetCamera(Camera camera) {
		camera.direction.set(0, 0, -1);
		camera.up.set(0, 1, 0);
		camera.update();
	}

	public static String hash(FileHandle fileHandle) {
		return ""; // TODO: implement
	}

	public static <T> int countValue(Array<T> array, T comparedValue, boolean identity) {
		int i = array.size - 1;
		T[] items = array.items;
		int count = 0;

		if (!identity && comparedValue != null) {
			while (i >= 0) {
				if (comparedValue.equals(items[i]))
					count++;
				i--;
			}
		} else {
			while (i >= 0) {
				if (items[i] == comparedValue)
					count++;
				i--;
			}
		}

		return count;
	}

	public static <T> T getLast(Array<T> array) {
		return array.get(array.size - 1);
	}

	public static Array<Class> toClassArray(Array<?> array) {
		Array<Class> res = new Array<>();
		for (Object o : array)
			res.add(o.getClass());
		return res;
	}

	public static void setCheckboxImageSize(CheckBox checkBox, float size) {
		checkBox.getImageCell().size(size).fill();
		checkBox.getImage().setScaling(Scaling.fit);
	}

	private GdxUtils() {
	}
}
