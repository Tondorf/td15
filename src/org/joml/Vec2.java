/*
 * (C) Copyright 2015 Richard Greenlees

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.

 */
package org.joml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

/**
 *
 * @author RGreenlees
 * @author Kai Burjack
 */
public class Vec2 implements Serializable, Externalizable {

    public float x;
    public float y;

    public Vec2() {
    }

    public Vec2(float newX, float newY) {
        x = newX;
        y = newY;
    }

    public Vec2(Vec2 clone) {
        x = clone.x;
        y = clone.y;
    }

    /** Sets the X and Y attributes using the supplied float values */
    public void set(float newX, float newY) {
        x = newX;
        y = newY;
    }

    /** Sets this Vector2 to be a clone of v */
    public void set(Vec2 v) {
        x = v.x;
        y = v.y;
    }

    /** Stores the perpendicular of v in dest. Does not modify v */
    public static void perpendicular(Vec2 v, Vec2 dest) {
        dest.x = v.y;
        dest.y = v.x * -1;
    }

    /** Sets this Vector2 to be its perpendicular */
    public void perpendicular() {
        set(y, x * -1);
    }

    /** Subtracts b from a and stores the result in dest. Does not modify a or b */
    public static void sub(Vec2 a, Vec2 b, Vec2 dest) {
        dest.x = a.x - b.x;
        dest.y = a.y - b.y;
    }

    /** Subtracts v from this Vector2 */
    public void sub(Vec2 v) {
        x -= v.x;
        y -= v.y;
    }

    /** Returns the dot product of a and b */
    public static float dot(Vec2 a, Vec2 b) {
        return ((a.x * b.x) + (a.y * b.y));
    }

    /** Returns the dot product of this vector and v */
    public float dot(Vec2 v) {
        return ((x * v.x) + (y * v.y));
    }

    /** Returns the length of a */
    public static float length(Vec2 a) {
        return (float) Math.sqrt((a.x * a.x) + (a.y * a.y));
    }

    /** Returns the length of this Vector2 */
    public float length() {
        return (float) Math.sqrt((x * x) + (y * y));
    }

    /** Returns the distance between the start and end vectors */
    public static float distance(Vec2 start, Vec2 end) {
        return (float) Math.sqrt((end.x - start.x) * (end.x - start.x)
                + (end.y - start.y) * (end.y - start.y));
    }

    /** Returns the distance between this Vector and v */
    public float distance(Vec2 v) {
        return (float) Math.sqrt((v.x - x) * (v.x - x)
                + (v.y - y) * (v.y - y));
    }

    /** Stores a normalized copy of the supplied Vector2 in dest. Does not modify a */
    public static void normalize(Vec2 a, Vec2 dest) {
        float length = (float) Math.sqrt((a.x * a.x) + (a.y * a.y));
        dest.x = a.x / length;
        dest.y = a.y / length;
    }
    
    /** Normalizes this Vector2 */
    public void normalize() {
        float length = (float) Math.sqrt((x * x) + (y * y));
        x /= length;
        y /= length;
    }
    
    /** Adds v to this Vector2 */
    public void add(Vec2 v) {
        x += v.x;
        y += v.y;
    }
    
    /** Adds b to a and stores the results in dest */
    public static void add(Vec2 a, Vec2 b, Vec2 dest) {
        dest.x = a.x + b.x;
        dest.y = a.y + b.y;
    }

    /**
     * Set all components to zero.
     */
    public void zero() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeFloat(x);
        out.writeFloat(y);
    }

    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        x = in.readFloat();
        y = in.readFloat();
    }

    /**
     * Negate this vector.
     * 
     * @return this
     */
    public Vec2 negate() {
        x = -x;
        y = -y;
        return this;
    }

    public Vec2 mul(float f) {
    	x*=f;
    	y*=f;
    	return this;
    }
}
