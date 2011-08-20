package uk.co.jawz;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.protocols.CCTouchDelegateProtocol;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import android.view.MotionEvent;

public class SneakyButton extends CCNode implements CCTouchDelegateProtocol {

	protected CGPoint center;
	
	protected float radius;
	protected float radiusSq;
	
	protected CGRect bounds;
	protected boolean active;
	protected boolean status;
	protected boolean value;
	protected boolean isHoldable;
	protected boolean isToggleable;
	protected float rateLimit;
	
	/*
	 * property accessors
	 */
	
	// @property (nonatomic, readonly) BOOL value;
	public boolean getValue() {
		return value;
	}
	
	// @property (nonatomic, readonly) BOOL active;
	public boolean getActive() {
		return active;
	}
	
	// @property (nonatomic, assign) BOOL status;
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	// @property (nonatomic, assign) BOOL isHoldable;
	public void setIsHoldable(boolean holdable) {
		isHoldable = holdable;
	}
	
	// @property (nonatomic, assign) BOOL isToggleable;
	public void setIsToggleable(boolean toggleable) {
		isToggleable = toggleable;
	}
	
	// @property (nonatomic, assign) float rateLimit;
	public void setRateLimit(float rateLimit) {
		this.rateLimit = rateLimit;
	}
	
	//Optimizations (keep Squared values of all radii for faster calculations) (updated internally when changing radii)
	// @property (nonatomic, assign) float radius;
	public void setRadius(float r) {
		radius = r;
		radiusSq = r*r;
	}
	
	// initWithRect
	public SneakyButton(CGRect rect) {
		bounds = CGRect.make(0, 0, rect.size.width, rect.size.height);
		center = CGPoint.make(rect.size.width/2, rect.size.height/2);
		status = true; //defaults to enabled
		active = false;
		value = false;
		isHoldable = false;
		isToggleable = false;
		radius = 32.0f;
		rateLimit = 1.0f/120.0f;
		
		position_ = rect.origin;
	}
	
	@SuppressWarnings("unused")
	private void limiter(float delta) {
		value = false;
		unschedule("limiter");
		active = false;
	}

	/*
	 * CCTouchDelegateProtocol implementation
	 */
	
	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		if (active) {
			return false;
		}
		
		CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.make(event.getX(), event.getY()));
		location = convertToNodeSpace(location);
		
		//Do a fast rect check before doing a circle hit check:
		if (location.x < -radius || location.x > radius || location.y < -radius || location.y > radius) {
			return false;
		} else {
			float dSq = location.x*location.x + location.y*location.y;
			if(radiusSq > dSq){
				active = true;
				if (!isHoldable && !isToggleable){
					value = true;
					schedule("limiter", rateLimit);
				}
				
				if (isHoldable) value = true;
				if (isToggleable) value = !value;
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		if (!active) return false;
		
		CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.make(event.getX(), event.getY()));
		location = convertToNodeSpace(location);
		
		//Do a fast rect check before doing a circle hit check:
		if (location.x < -radius || location.x > radius || location.y < -radius || location.y > radius) {
			return false;
		} else {
			float dSq = location.x*location.x + location.y*location.y;
			if (radiusSq > dSq) {
				if (isHoldable) value = true;
			} else {
				if (isHoldable) {
					value = false; 
					active = false;
				}
			}
		}
		
		return true;
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		if (!active) return false;
		if (isHoldable) value = false;
		if (isHoldable||isToggleable) active = false;
		
		return true;
	}

	@Override
	public boolean ccTouchesCancelled(MotionEvent event) {
		return ccTouchesEnded(event);
	}

}
