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
 * Contains the definition of a Vector comprising 4 floats and associated
 * transformations.
 * 
 * @author Richard Greenlees
 * @author Kai Burjack
 */
public class Vec4 implements Serializable, Externalizable {

    public float x;
    public float y;
    public float z;
    public float w = 1.0f;

    /**
     * Create a new {@link Vec4} of <code>(0, 0, 0, 1)</code>.
     */
    public Vec4() {
    }

    /**
     * Create a new {@link Vec4} with the same values as <code>v</code>.
     * 
     * @param v
     *            the {@link Vec4} to copy the values from
     */
    public Vec4(Vec4 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
    }

    /**
     * Create a new {@link Vec4} with the first three components from the
     * given <code>v</code> and the given <code>w</code>.
     * 
     * @param v
     *            the {@link Vec3}
     * @param w
     *            the w value
     */
    public Vec4(Vec3 v, float w) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = w;
    }

    /**
     * Create a new {@link Vec4} with the given component values.
     */
    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Set this {@link Vec4} to the values of the given <code>v</code>.
     * 
     * @param v
     *            the vector whose values will be copied into this
     * @return this
     */
    public Vec4 set(Vec4 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
        return this;
    }

    /**
     * Set the first three components of this to the components of
     * <code>v</code> and the last component to <code>w</code>.
     * 
     * @param v
     *            the {@link Vec3} to copy
     * @param w
     *            the w component
     * @return this
     */
    public Vec4 set(Vec3 v, float w) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = w;
        return this;
    }

    /**
     * Set the first three components of this to the components of
     * <code>v</code> and the last component to <code>w</code>.
     * 
     * @param x
     *            the x-component
     * @param y
     *            the y-component
     * @param z
     *            the z-component
     * @param w
     *            the w component
     * @return this
     */
    public Vec4 set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    /**
     * Subtract the supplied vector from this one.
     * 
     * @return this
     */
    public Vec4 sub(Vec4 v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        w -= v.w;
        return this;
    }

    /**
     * Decrement the components of this vector by the given values.
     * 
     * @return this
     */
    public Vec4 sub(float x, float y, float z, float w) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        this.w -= w;
        return this;
    }

    /**
     * Subtract v2 from v1 and store the result in dest.
     */
    public static void sub(Vec4 v1, Vec4 v2, Vec4 dest) {
        dest.x = v1.x - v2.x;
        dest.y = v1.y - v2.y;
        dest.z = v1.z - v2.z;
        dest.w = v1.w - v2.w;
    }

    /**
     * Add the supplied vector to this one.
     * 
     * @return this
     */
    public Vec4 add(Vec4 v) {
        x += v.x;
        y += v.y;
        z += v.z;
        w += v.w;
        return this;
    }

    /**
     * Increment the components of this vector by the given values.
     * 
     * @return this
     */
    public Vec4 add(float x, float y, float z, float w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
        return this;
    }

    /**
     * Add v2 to v1 and store the result in <code>dest</code>.
     */
    public static void add(Vec4 v1, Vec4 v2, Vec4 dest) {
        dest.x = v1.x + v2.x;
        dest.y = v1.y + v2.y;
        dest.z = v1.z + v2.z;
        dest.w = v1.w + v2.w;
    }

    /**
     * Multiply this Vector4 by another Vector4.
     * 
     * @return this
     */
    public Vec4 mul(Vec4 v) {
        x *= v.x;
        y *= v.y;
        z *= v.z;
        z *= v.w;
        return this;
    }

    /**
     * Multiply v1 by v2 component-wise and store the result into dest.
     */
    public static void mul(Vec4 v1, Vec4 v2, Vec4 dest) {
        dest.x = v1.x * v2.x;
        dest.y = v1.y * v2.y;
        dest.z = v1.z * v2.z;
        dest.w = v1.w * v2.w;
    }

    /**
     * Multiply this Vector4 by the given matrix mat and store the result in
     * <code>this</code>.
     * 
     * @param mat
     *            the matrix to multiply the vector with
     * @return this
     */
    public Vec4 mul(Matrix4 mat) {
        mul(this, mat, this);
        return this;
    }

    /**
     * Multiply this Vector4 by the given matrix mat and store the result in
     * <code>dest</code>.
     * 
     * @param mat
     *            the matrix to multiply the vector with
     * @param dest
     *            the destination vector to hold the result
     * @return this
     */
    public Vec4 mul(Matrix4 mat, Vec4 dest) {
        mul(this, mat, dest);
        return this;
    }

    /**
     * Multiply <code>v</code> by the given matrix <code>mat</code> and store
     * the result in <code>dest</code>.
     * 
     * @param v
     *            the vector to multiply the matrix with
     * @param mat
     *            the matrix
     * @param dest
     *            will hold the result
     */
    public static void mul(Vec4 v, Matrix4 mat, Vec4 dest) {
        if (v != dest) {
            dest.x = mat.m00 * v.x + mat.m10 * v.y + mat.m20 * v.z + mat.m30 * v.w;
            dest.y = mat.m01 * v.x + mat.m11 * v.y + mat.m21 * v.z + mat.m31 * v.w;
            dest.z = mat.m02 * v.x + mat.m12 * v.y + mat.m22 * v.z + mat.m32 * v.w;
            dest.w = mat.m03 * v.x + mat.m13 * v.y + mat.m23 * v.z + mat.m33 * v.w;
        } else {
            dest.set(mat.m00 * v.x + mat.m10 * v.y + mat.m20 * v.z + mat.m30 * v.w,
                     mat.m01 * v.x + mat.m11 * v.y + mat.m21 * v.z + mat.m31 * v.w,
                     mat.m02 * v.x + mat.m12 * v.y + mat.m22 * v.z + mat.m32 * v.w,
                     mat.m03 * v.x + mat.m13 * v.y + mat.m23 * v.z + mat.m33 * v.w);
        }
    }

    /**
     * Multiply all components of this {@link Vec4} by the given scalar
     * value.
     * 
     * @return this
     */
    public Vec4 mul(float scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        w *= scalar;
        return this;
    }

    /**
     * Multiply the components of this Vector4 by the given scalar values and store the result in <code>this</code>.
     * 
     * @return this
     */
    public Vec4 mul(float x, float y, float z, float w) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        this.w *= w;
        return this;
    }

    /**
     * Multiply the given Vector4 v by the scalar value, and store the result
     * in dest.
     */
    public static void mul(Vec4 v, float scalar, Vec4 dest) {
        dest.x = v.x * scalar;
        dest.y = v.y * scalar;
        dest.z = v.z * scalar;
        dest.w = v.w * scalar;
    }

    /**
     * Return the length squared of this vector.
     * 
     * @return the length squared
     */
    public float lengthSquared() {
        return x * x + y * y + z * z + w * w;
    }

    /**
     * Return the length of this vector.
     * 
     * @return the length
     */
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    /**
     * Normalizes this vector.
     * 
     * @return this
     */
    public Vec4 normalize() {
        float d = length();
        x /= d;
        y /= d;
        z /= d;
        w /= d;
        return this;
    }

    /**
     * Normalize the <code>original</code> vector and store the result in
     * <code>dest</code>.
     * 
     * @param original
     *            the vector to normalize
     * @param dest
     *            will hold the result
     */
    public static void normalize(Vec4 original, Vec4 dest) {
        float d = original.length();
        dest.set(original.x / d, original.y / d, original.z / d, original.w / d);
    }

    /**
     * Return the distance between <code>start</code> and <code>end</code>.
     * 
     * @param start
     *            the first vector
     * @param end
     *            the second vector
     * @return the euclidean distance
     */
    public static float distance(Vec4 start, Vec4 end) {
        return (float) Math.sqrt(
                  (end.x - start.x) * (end.x - start.x)
                + (end.y - start.y) * (end.y - start.y)
                + (end.z - start.z) * (end.z - start.z)
                + (end.w - start.w) * (end.w - start.w));
    }

    /**
     * Return the distance between <code>this</code> vector and <code>v</code>.
     * 
     * @return the euclidean distance
     */
    public float distance(Vec4 v) {
        return distance(this, v);
    }

    /**
     * Compute the dot product (inner product) of this vector and <code>v</code>
     * .
     * 
     * @param v
     *            the other vector
     * @return the dot product
     */
    public float dot(Vec4 v) {
        return (x * v.x) + (y * v.y) + (z * v.z) + (w * v.w);
    }

    /**
     * Return the dot product of the supplied <code>v1</code> and
     * <code>v2</code> vectors.
     * 
     * @return the dot product
     */
    public static float dot(Vec4 v1, Vec4 v2) {
        return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z) + (v1.w * v2.w);
    }

    /**
     * Set all components to zero.
     * 
     * @return this
     */
    public Vec4 zero() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.w = 0.0f;
        return this;
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeFloat(x);
        out.writeFloat(y);
        out.writeFloat(z);
        out.writeFloat(w);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        x = in.readFloat();
        y = in.readFloat();
        z = in.readFloat();
        w = in.readFloat();
    }

    /**
     * Set the components of this vector to be the component-wise minimum of
     * this and the other vector.
     *
     * @param v
     *            the other vector
     * @return this
     */
    public Vec4 min(Vec4 v) {
        this.x = Math.min(x, v.x);
        this.y = Math.min(y, v.y);
        this.z = Math.min(z, v.z);
        this.w = Math.min(w, v.w);
        return this;
    }

    /**
     * Set the components of this vector to be the component-wise maximum of
     * this and the other vector.
     *
     * @param v
     *            the other vector
     * @return this
     */
    public Vec4 max(Vec4 v) {
        this.x = Math.max(x, v.x);
        this.y = Math.max(y, v.y);
        this.z = Math.max(z, v.z);
        this.w = Math.min(w, v.w);
        return this;
    }

}