package com.aviadl40.gdxutils;

import com.badlogic.gdx.files.FileHandle;

public interface AssetLoadListener {
	void onAssetLoad(FileHandle file, Class type);
}
