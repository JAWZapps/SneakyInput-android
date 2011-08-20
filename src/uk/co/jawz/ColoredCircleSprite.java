package uk.co.jawz;

import static javax.microedition.khronos.opengles.GL10.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.cocos2d.config.ccConfig;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.protocols.CCBlendProtocol;
import org.cocos2d.protocols.CCRGBAProtocol;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccBlendFunc;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

public class ColoredCircleSprite extends CCSprite implements CCRGBAProtocol, CCBlendProtocol {

	protected ccColor3B color;
	protected float radius;
	protected int opacity;

	protected int numberOfSegments;
	
	protected ccBlendFunc blendFunc;
	
	protected FloatBuffer circleVertices;
	
    private static FloatBuffer tmpFloatBuf;
	
	public static ColoredCircleSprite circleWithColorAndRadius(ccColor4B color, float radius) {
		ColoredCircleSprite ccs = new ColoredCircleSprite();
		ccs.initWithColorAndRadius(color, radius);
		return ccs;
	}

	//FIXME
	private void initWithColorAndRadius(ccColor4B color, float radius) {
		this.color.r = color.r;
		this.color.g = color.g;
		this.color.b = color.b;
		this.opacity = color.a;
		setRadius(radius);
	}
	
	protected ColoredCircleSprite() {
		radius = 10.0f;
		numberOfSegments = 36;
		
		blendFunc = new ccBlendFunc(ccConfig.CC_BLEND_SRC, ccConfig.CC_BLEND_DST);
		
		color = ccColor3B.ccc3(0, 0, 0);
		opacity = 255;
		
		circleVertices = getVertices(2*numberOfSegments); 
		circleVertices.position(0);

		setRadius(radius);
	}

	public boolean containsPoint(CGPoint point) {
		float dSq = point.x * point.x + point.y * point.y;
		float rSq = radius * radius;
		return (dSq <= rSq);
	}
	
	public void setRadius(float size) {
		radius = size;
		float theta_inc = (float) ((float)2.0f * Math.PI / numberOfSegments);
		float theta = 0.0f;
		
		for (int i=0; i < numberOfSegments; i++) {
			float j = (float) (radius * CCDirector.sharedDirector().getContentScaleFactor() * Math.cos(theta) + position_.x);
			float k = (float) (radius * CCDirector.sharedDirector().getContentScaleFactor() * Math.sin(theta) + position_.y);

			circleVertices.put(j);
			circleVertices.put(k);
			
			theta += theta_inc;
		}
		
		circleVertices.position(0);
		
		updateContentSize();
	}
	
	public void setContentSize(CGSize size) {
		radius = size.width/2;
	}
	
	public void updateContentSize() {
		setContentSize(CGSize.make(radius*2, radius*2));
	}

	@Override
	public void draw(GL10 gl) {
		// Default GL states: GL_TEXTURE_2D, GL_VERTEX_ARRAY, GL_COLOR_ARRAY, GL_TEXTURE_COORD_ARRAY
		// Needed states: GL_VERTEX_ARRAY
		// Unneeded states: GL_COLOR_ARRAY, GL_TEXTURE_2D, GL_TEXTURE_COORD_ARRAY
		gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL_COLOR_ARRAY);
		gl.glDisable(GL_TEXTURE_2D);
		
		gl.glVertexPointer(2, GL_FLOAT, 0, circleVertices);
		gl.glColor4f(color.r/255.0f, color.g/255.0f, color.b/255.0f, opacity/255.0f);

		boolean newBlend = false;
		if (blendFunc.src != ccConfig.CC_BLEND_SRC || blendFunc.dst != ccConfig.CC_BLEND_DST) {
			newBlend = true;
			gl.glBlendFunc(blendFunc.src, blendFunc.dst);
		} else if (opacity == 255) {
			newBlend = true;
			gl.glBlendFunc(GL_ONE, GL_ZERO);
		} else {
			newBlend = true;
			gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		}
		
		gl.glDrawArrays(GL_TRIANGLE_FAN, 0, numberOfSegments);
		
		if (newBlend) {
			gl.glBlendFunc(ccConfig.CC_BLEND_SRC, ccConfig.CC_BLEND_DST);
		}
		
		// restore default GL state
		gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL_COLOR_ARRAY);
		gl.glEnable(GL_TEXTURE_2D);
	}
	
	/*
	 *  CCRGBAProtocol implementation
	 */

	@Override
	public void setColor(ccColor3B color) {
		this.color = color;
	}

	@Override
	public ccColor3B getColor() {
		return color;
	}

	@Override
	public int getOpacity() {
		return opacity;
	}

	@Override
	public void setOpacity(int opacity) {
		this.opacity = opacity;
		updateColor();
	}

//	@Override
//	public void setOpacityModifyRGB(boolean b) {
//		// TODO Auto-generated method stub
//		
//	}

//	@Override
//	public boolean doesOpacityModifyRGB() {
//		// TODO Auto-generated method stub
//		return false;
//	}

	/*
	 * CCBlendProtocol implementation
	 */

	@Override
	public void setBlendFunc(ccBlendFunc blendFunc) {
		this.blendFunc = blendFunc;
	}

	@Override
	public ccBlendFunc getBlendFunc() {
		return blendFunc;
	}
	
	// shamelessly nicked from CCDrawingPrimitives; sadly they made it private
    private static FloatBuffer getVertices(int size) {
        if (tmpFloatBuf == null || tmpFloatBuf.capacity() < size) {
        	ByteBuffer vbb = ByteBuffer.allocateDirect(4 * size);
        	vbb.order(ByteOrder.nativeOrder());
        	tmpFloatBuf = vbb.asFloatBuffer();
        }
        tmpFloatBuf.rewind();
        
        return tmpFloatBuf;
    }
}
