package uk.co.jawz;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGSize;

public class SneakyButtonSkinnedBase extends CCSprite {
	protected CCSprite defaultSprite;
	protected CCSprite activatedSprite;
	protected CCSprite disabledSprite;
	protected CCSprite pressSprite;
	
	protected SneakyButton button;
	
	/*
	 * property accessors
	 */
	
	// @property (nonatomic, retain) CCSprite *defaultSprite;
	public CCSprite getDefaultSprite() {
		return defaultSprite;
	}
	
	public void setDefaultSprite(CCSprite aSprite) {
		if (defaultSprite != null) {
			if (defaultSprite.getParent() != null) {
				defaultSprite.getParent().removeChild(defaultSprite, true);
			}
		}
		defaultSprite = aSprite;
		if (aSprite != null) {
			addChild(defaultSprite, 0);
			setContentSize(defaultSprite.getContentSize());
		}
	}

	// @property (nonatomic, retain) CCSprite *activatedSprite;
	public CCSprite getActivatedSprite() {
		return activatedSprite;
	}
	
	public void setActivatedSprite(CCSprite aSprite) {
		if (activatedSprite != null) {
			if (activatedSprite.getParent() != null) {
				activatedSprite.getParent().removeChild(activatedSprite, true);
			}
		}
		activatedSprite = aSprite;
		if (aSprite != null) {
			addChild(activatedSprite, 1);
			setContentSize(activatedSprite.getContentSize());
		}
	}

	// @property (nonatomic, retain) CCSprite *disabledSprite;
	public CCSprite getDisabledSprite() {
		return disabledSprite;
	}
	
	public void setDisabledSprite(CCSprite aSprite) {
		if (disabledSprite != null) {
			if (disabledSprite.getParent() != null) {
				disabledSprite.getParent().removeChild(disabledSprite, true);
			}
		}
		disabledSprite = aSprite;
		if (aSprite != null) {
			addChild(disabledSprite, 2);
			setContentSize(disabledSprite.getContentSize());
		}
	}

	// @property (nonatomic, retain) CCSprite *pressSprite;
	public CCSprite getPressSprite() {
		return pressSprite;
	}
	
	public void setPressSprite(CCSprite aSprite) {
		if (pressSprite != null) {
			if (pressSprite.getParent() != null) {
				pressSprite.getParent().removeChild(pressSprite, true);
			}
		}
		pressSprite = aSprite;
		if (aSprite != null) {
			addChild(pressSprite, 3);
			setContentSize(pressSprite.getContentSize());
		}
	}

	// @property (nonatomic, retain) SneakyButton *button;
	public SneakyButton getButton() {
		return button;
	}
	
	public void setButton(SneakyButton aButton) {
		if (button != null) {
			if (button.getParent() != null) {
				button.getParent().removeChild(button, true);
			}
		}
		button = aButton;
		if (aButton != null) {
			addChild(button, 4);
			if (defaultSprite != null) {
				button.setRadius(defaultSprite.getContentSize().width/2);
			}
		}
	}
	
	/*
	 * implementation
	 */
	
	public SneakyButtonSkinnedBase() {
		super();
		setDefaultSprite(null);
		setActivatedSprite(null);
		setDisabledSprite(null);
		setPressSprite(null);
		setButton(null);
		
		schedule("watchSelf");
	}
	
	public void watchSelf(float delta) {
		if (!button.getStatus()){
			if (disabledSprite != null) {
				disabledSprite.setVisible(true);
			} else {
				disabledSprite.setVisible(false);
			}
		} else {
			if (!button.getActive()) {
				pressSprite.setVisible(false);
				if (!button.getValue()){
					activatedSprite.setVisible(false);
					if (defaultSprite != null) {
						defaultSprite.setVisible(true);
					}
				} else {
					activatedSprite.setVisible(true);
				}
			} else {
				defaultSprite.setVisible(false);
				if (pressSprite != null) {
					pressSprite.setVisible(true);
				}
			}
		}
	}
	
	public void setContentSize(CGSize s) {
		contentSize_ = s;
		defaultSprite.setContentSize(s);
		button.setRadius(s.width/2);
	}
}
