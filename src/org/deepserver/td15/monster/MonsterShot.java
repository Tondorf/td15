package org.deepserver.td15.monster;

import org.deepserver.td15.InputStatus;
import org.deepserver.td15.World;
import org.joml.Vec2;

public class MonsterShot extends MonsterSprite {
	protected final float ownSpeed=0.02f;
	public float speed;
	

	public MonsterShot(World world, Vec2 position,float speed) {
		super(world,position);
		this.speed=speed+ownSpeed;
	}
	
	@Override
	public String getTextureName() {
		return "ship1.png";
	}
	
	@Override
	public void action(double delta, InputStatus is) {
		super.action(delta, is);

		Vec2 temp=new Vec2(orientation.getAhead());
		temp.mul(speed);
		
		position.add(temp);
	}

}
