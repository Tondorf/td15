package org.deepserver.td15.sound;

import java.util.HashMap;

public enum SoundEffect {

	SHOT,
	KILL,
	FLY,
	ENGINE,
	MUSIC;

	HashMap<SoundEffect,Integer> lengths = new HashMap();

}
