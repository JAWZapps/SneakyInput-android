package uk.co.jawz;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGSize;

public class SneakyJoystickSkinnedBase extends CCSprite {
	protected CCSprite backgroundSprite;
	protected CCSprite thumbSprite;
	protected SneakyJoystick joystick;
	
	public SneakyJoystickSkinnedBase() {
		setBackgroundSprite(null);
		setThumbSprite(null);
		setJoystick(null);
		
		schedule("updatePositions");
	}

	public void updatePositions(float delta) {
		if (joystick != null && thumbSprite != null) {
			thumbSprite.setPosition(joystick.getStickPosition());
		}
	}
	
	public void setContentSize(CGSize s) {
		contentSize_ = s;
		backgroundSprite.setContentSize(s);
		joystick.setJoystickRadius(s.width/2);
	}
	
	// 	@property (nonatomic, retain) CCSprite *backgroundSprite;
	public CCSprite getBackgroundSprite() {
		return backgroundSprite;
	}

	public void setBackgroundSprite(CCSprite aSprite) {
		if (backgroundSprite != null) {
			if (backgroundSprite.getParent() != null) {
				backgroundSprite.getParent().removeChild(backgroundSprite, true);
			}
		}
		
		backgroundSprite = aSprite;
		if (aSprite != null) {
			addChild(backgroundSprite, 0);
			setContentSize(backgroundSprite.getContentSize());
		}
	}

	// 	@property (nonatomic, retain) CCSprite *thumbSprite;
	public CCSprite getThumbSprite() {
		return thumbSprite;
	}

	public void setThumbSprite(CCSprite aSprite) {
		if (thumbSprite != null) {
			if (thumbSprite.getParent() != null) {
				thumbSprite.getParent().removeChild(thumbSprite, true);
			}
		}
		
		thumbSprite = aSprite;
		if (aSprite != null) {
			addChild(thumbSprite, 1);
			joystick.setThumbRadius(thumbSprite.getContentSize().width/2);
		}
	}

	// 	@property (nonatomic, retain) SneakyJoystick *joystick;
	public SneakyJoystick getJoystick() {
		return joystick;
	}

	public void setJoystick(SneakyJoystick aJoystick) {
		if (joystick != null) {
			if (joystick.getParent() != null) {
				joystick.getParent().removeChild(joystick, true);
			}
		}
		
		joystick = aJoystick;
		if (aJoystick != null) {
			addChild(joystick, 2);
			if (thumbSprite != null) {
				joystick.setThumbRadius(thumbSprite.getContentSize().width/2);
			} else {
				joystick.setThumbRadius(0);
			}
			
			if (backgroundSprite != null) {
				joystick.setJoystickRadius(backgroundSprite.getContentSize().width/2);
			}
		}
	}	
}
