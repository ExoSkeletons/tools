package com.aviadl40.utils;

import android.support.annotation.Nullable;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class Utils {

	public static final Runnable DO_NOTHING = new Runnable() {
		@Override
		public void run() {

		}
	};

	public static float findX(float V, float T) { // X = VT
		return V * T;
	}

	public static float findV(float X, float T) { // V = X/T
		return X / T;
	}

	public static float findT(float X, float V) { // T = X/V
		return X / V;
	}

	public static boolean isFinite(float f) {
		return !(Float.isNaN(f) || Float.isInfinite(f));
	}

	public static float finite(float f, float defaultValue) {
		if (isFinite(f)) return f;
		return defaultValue;
	}

	public static int randomizeWithProbability(byte[] probArray) {
		int index = 0;
		byte sum = 0, ran;
		for (byte p : probArray)
			sum += p;
		ran = (byte) (MathUtils.random.nextInt(sum + 1) - 1);
		for (byte p : probArray) {
			if (ran < p)
				break;
			ran -= p;
			index++;
		}
		return index;
	}

	@SafeVarargs
	public static <T> ArrayList<T> toArrayList(T... args) {
		final ArrayList<T> res = new ArrayList<>();
		Collections.addAll(res, args);
		return res;
	}

	public static String toSingleLine(String s) {
		return s.replace('\t', ' ').replace('\n', ' ').replaceAll(" {2}", " ");
	}

	public static String plural(String s) {
		return s.endsWith("s")
				? s
				: (
				s.endsWith("y")
						? s.substring(0, s.length() - 1) + "ies"
						: s + "s"
		);
	}

	public static String amount(String name, int amount, boolean withAmount) {
		return "" + (withAmount ? amount + " " : "") + (Math.abs(amount) == 1 ? name : plural(name));
	}

	public static String capitaliseFirst(String s, @Nullable String regexSeparator) {
		StringBuilder res = new StringBuilder();
		if (regexSeparator != null)
			for (String word : s.split(regexSeparator))
				res.append(capitaliseFirst(word)).append(regexSeparator);
		else
			res.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1));
		return res.toString();
	}

	public static String capitaliseFirst(String s) {
		return capitaliseFirst(s, null);
	}

	public static String toStringShort(@Nullable Object o) {
		if (o == null)
			return "null";
		Class c = o.getClass();
		return (c.isAnonymousClass() ? c.getSuperclass().getSimpleName() : c.getSimpleName()) + "@" + Integer.toHexString(o.hashCode());
	}

	public static boolean getBit(int b, int index) {
		return ((b >> index) & 1) == 1;
	}

	public static float round(float f, int decimals) {
		if (decimals <= 0) return (int) f;
		float s = (float) Math.pow(10, decimals);
		return ((int) (f * s)) / s;
	}

	private Utils() {
	}
}