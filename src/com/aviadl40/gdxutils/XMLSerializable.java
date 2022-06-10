package com.aviadl40.gdxutils;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

import java.io.IOException;
import java.io.Serializable;

public interface XMLSerializable extends Serializable {
	void read(XmlReader.Element e, long saveFormat);

	void write(XmlWriter w) throws IOException;
}
