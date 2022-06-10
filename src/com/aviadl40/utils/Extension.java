package com.aviadl40.utils;

public enum Extension {
	png,
	mp3,
	adv,
	bin,
	xml;

	@Override
	public String toString() {
		return "." + name();
	}
}
