package uk.co.jawz;

import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.protocols.CCTouchDelegateProtocol;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import android.view.MotionEvent;

public class SneakyJoystick extends CCNode implements CCTouchDelegateProtocol {
		
	public static final float SJ_PI=3.14159265359f;
	public static final float SJ_PI_X_2=6.28318530718f;
	public static final float SJ_RAD2DEG=180.0f/SJ_PI;
	public static final float SJ_DEG2RAD=SJ_PI/180.0f;
	
	protected CGPoint stickPosition;
	protected float degrees;
	protected CGPoint velocity;
	protected boolean autoCenter;
	protected boolean isDPad;
	protected boolean hasDeadzone; //Turns Deadzone on/off for joystick, always true if ifDpad == true
	protected int numberOfDirections; //Used only when isDpad == true
	
	protected float joystickRadius;
	protected float thumbRadius;
	protected float deadRadius; //Size of deadzone in joystick (how far you must move before input starts). Automatically set if isDpad == true
	
	//Optimizations (keep Squared values of all radii for faster calculations) (updated internally when changing joy/thumb radii)
	protected float joystickRadiusSq;
	protected float thumbRadiusSq;
	protected float deadRadiusSq;
	
	/*
	 * property accessors
	 */
	
	// @property (nonatomic, readonly) CGPoint stickPosition;
	public CGPoint getStickPosition() {
		return stickPosition;
	}
	
	// @property (nonatomic, readonly) float degrees;
	public float getDegrees() {
		return degrees;
	}
	
	// @property (nonatomic, readonly) CGPoint velocity;
	public CGPoint getVelocity() {
		return velocity;
	}
	
	// @property (nonatomic, assign) BOOL autoCenter;
	public void setAutoCenter(boolean autoCenter) {
		this.autoCenter = autoCenter;
	}
	
	// @property (nonatomic, assign) BOOL isDPad;
	public void setIsDPad(boolean is) {
		isDPad = is;
		if (isDPad) {
			hasDeadzone = true;
			setDeadRadius(10.0f);
		}
	}
	
	// @property (nonatomic, assign) BOOL hasDeadzone;
	public void setHasDeadzone(boolean has) {
		hasDeadzone = has;
	}
	
	// @property (nonatomic, assign) NSUInteger numberOfDirections;
	public void setNumberOfDirections(int num) {
		numberOfDirections = num;
	}
	
	// @property (nonatomic, assign) float joystickRadius;
	public void setJoystickRadius(float r) {
		joystickRadius = r;
		joystickRadiusSq = r*r;
	}

	// @property (nonatomic, assign) float thumbRadius;
	public void setThumbRadius(float r) {
		thumbRadius = r;
		thumbRadiusSq = r*r;
	}

	// @property (nonatomic, assign) float deadRadius;
	public void setDeadRadius(float r) {
		deadRadius = r;
		deadRadiusSq = r*r;
	}
	
	
	// initWithRect
	public SneakyJoystick(CGRect rect) {
		stickPosition = CGPoint.zero();
		degrees = 0.0f;
		velocity = CGPoint.zero();
		autoCenter = true;
		isDPad = false;
		hasDeadzone = false;
		numberOfDirections = 4;
		
		setJoystickRadius(rect.size.width/2);
		setThumbRadius(32.0f);
		setDeadRadius(0.0f);

		position_ = rect.origin;
	}
	
	@Override
	public void onEnterTransitionDidFinish() {
		CCTouchDispatcher.sharedDispatcher().addTargetedDelegate(this, 1, true);
	}
	
	@Override
	public void onExit() {
		CCTouchDispatcher.sharedDispatcher().removeDelegate(this);
	}
	
	public void updateVelocity(CGPoint point) {
		// Calculate distance and angle from the center.
		float dx = point.x;
		float dy = point.y;
		float dSq = dx * dx + dy * dy;
		
		if(dSq <= deadRadiusSq){
			velocity = CGPoint.zero();
			degrees = 0.0f;
			stickPosition = point;
			return;
		}

		float angle = (float) Math.atan2(dy, dx); // in radians
		if(angle < 0){
			angle += SJ_PI_X_2;
		}
		float cosAngle;
		float sinAngle;
		
		if(isDPad){
			float anglePerSector = 360.0f / numberOfDirections * SJ_DEG2RAD;
			angle = Math.round(angle/anglePerSector) * anglePerSector;
		}
		
		cosAngle = (float) Math.cos(angle);
		sinAngle = (float) Math.sin(angle);
		
		// NOTE: Velocity goes from -1.0 to 1.0.
		if (dSq > joystickRadiusSq || isDPad) {
			dx = cosAngle * joystickRadius;
			dy = sinAngle * joystickRadius;
		}
		
		velocity = CGPoint.make(dx/joystickRadius, dy/joystickRadius);
		degrees = angle * SJ_RAD2DEG;
		
		// Update the thumb's position
		stickPosition = CGPoint.ccp(dx, dy);		
	}



	
	/*
	 * CCTouchDelegateProtocol implementation
	 */
	
	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.make(event.getX(), event.getY()));
		location = convertToNodeSpace(location);
		//Do a fast rect check before doing a circle hit check:
		if (location.x < -joystickRadius || location.x > joystickRadius  
		 || location.y < -joystickRadius || location.y > joystickRadius) {
			return false;
		} else {
			float dSq = location.x*location.x + location.y*location.y;
			if (joystickRadiusSq > dSq) {
				updateVelocity(location);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		CGPoint location = CCDirector.sharedDirector().convertToGL(CGPoint.make(event.getX(), event.getY()));
		location = convertToNodeSpace(location);
		updateVelocity(location);
		return true;
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		CGPoint location = CGPoint.zero();
		if (!autoCenter) {
			CGPoint loc = CCDirector.sharedDirector().convertToGL(CGPoint.make(event.getX(), event.getY()));
			location = convertToNodeSpace(loc);
		}
		updateVelocity(location);
		return true;
	}

	@Override
	public boolean ccTouchesCancelled(MotionEvent event) {
		return ccTouchesEnded(event);
	}
}
