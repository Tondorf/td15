package org.deepserver.td15.monster;

import org.apache.log4j.Logger;
import org.deepserver.td15.World;
import org.deepserver.td15.sound.AudioManager;
import org.joml.Vec2;

public class MonsterRock extends MonsterSprite {

	private static Logger log = Logger.getLogger(MonsterRock.class);

	protected final float typicalRadius=0.1f;

	public MonsterRock(World world, Vec2 v) {
		super(world, v);
	}

	@Override
	public String getTextureName() {
		return "rock.png";
	}

	@Override
	public void destroy() {
		log.debug("Player hit a rock. Rock doesn't care.");
	}

}
