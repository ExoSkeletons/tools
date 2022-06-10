package com.aviadl40.gdxutils;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.utils.Align;

public class LabelNode extends Tree.Node<LabelNode, String, Label> {
	public LabelNode(String text, Label.LabelStyle style) {
		super(new Label(text, style));
		getActor().setAlignment(Align.topLeft);
	}
}
