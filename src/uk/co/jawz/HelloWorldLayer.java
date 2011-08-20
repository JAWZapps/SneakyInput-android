package uk.co.jawz;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.ccColor4B;

public class HelloWorldLayer extends CCLayer {
	
	private SneakyJoystick leftJoystick;
	private SneakyButton rightButton;
	
	public static HelloWorldLayer node() {
		return new HelloWorldLayer();
	}
	
	protected HelloWorldLayer() {
		setIsTouchEnabled(true);
		
		SneakyJoystickSkinnedBase leftJoy = new SneakyJoystickSkinnedBase();
		leftJoy.setPosition(64, 64);
		leftJoy.setJoystick(new SneakyJoystick(CGRect.make(0, 0, 128, 128)));
		leftJoy.setBackgroundSprite(ColoredCircleSprite.circleWithColorAndRadius(ccColor4B.ccc4(255, 0, 0, 128), 64));
		leftJoy.setThumbSprite(ColoredCircleSprite.circleWithColorAndRadius(ccColor4B.ccc4(0, 0, 255, 200), 32));
		leftJoystick = leftJoy.getJoystick();
		addChild(leftJoy);

		SneakyButtonSkinnedBase rightBut = new SneakyButtonSkinnedBase();
		rightBut.setPosition(448,32);
		rightBut.setButton(new SneakyButton(CGRect.make(0, 0, 64, 64)));
		rightBut.setDefaultSprite(ColoredCircleSprite.circleWithColorAndRadius(ccColor4B.ccc4(255, 255, 255, 128), 32));
		rightBut.setActivatedSprite(ColoredCircleSprite.circleWithColorAndRadius(ccColor4B.ccc4(255, 255, 255, 255), 32));
		rightBut.setPressSprite(ColoredCircleSprite.circleWithColorAndRadius(ccColor4B.ccc4(255, 0, 0, 255), 32));
		rightButton = rightBut.getButton();
		rightButton.setIsToggleable(true);
		addChild(rightBut);
		
		CCDirector.sharedDirector().setAnimationInterval(1.0f/60.0f);
	}
}
