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
 * Contains the definition of a Vector comprising 3 floats and associated
 * transformations.
 *
 * @author Richard Greenlees
 * @author Kai Burjack
 */
public class Vec3 implements Serializable, Externalizable {

	public float x;
	public float y;
	public float z;

	public Vec3() {
	}

	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3(Vec3 clone) {
		this.x = clone.x;
		this.y = clone.y;
		this.z = clone.z;
	}

	public Vec3(Vec2 clone) {
		this.x = clone.x;
		this.y = clone.y;
		this.z = 0.0f;
	}

	/**
	 * Set the x, y and z attributes to match the supplied vector.
	 *
	 * @param v
	 * @return this
	 */
	public Vec3 set(Vec3 v) {
		x = v.x;
		y = v.y;
		z = v.z;
		return this;
	}

	/**
	 * Set the x, y and z attributes to the supplied float values.
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return this
	 */
	public Vec3 set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	/**
	 * Subtract the supplied vector from this one and store the result in
	 * <code>this</code>.
	 *
	 * @param v
	 * @return this
	 */
	public Vec3 sub(Vec3 v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return this;
	}

	/**
	 * Decrement the components of this vector by the given values.
	 *
	 * @return this
	 */
	public Vec3 sub(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	/**
	 * Subtract v2 from v1 and store the result in <code>dest</code>.
	 *
	 * @param v1
	 *            the left operand
	 * @param v2
	 *            the right operand
	 * @param dest
	 *            will hold the result
	 */
	public static void sub(Vec3 v1, Vec3 v2, Vec3 dest) {
		dest.x = v1.x - v2.x;
		dest.y = v1.y - v2.y;
		dest.z = v1.z - v2.z;
	}

	/**
	 * Add the supplied vector to this one and store the result in
	 * <code>this</code>.
	 *
	 * @param v
	 *            the vector to add
	 * @return this
	 */
	public Vec3 add(Vec3 v) {
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}

	/**
	 * Increment the components of this vector by the given values.
	 *
	 * @return this
	 */
	public Vec3 add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	/**
	 * Add v2 to v1 and store the result in <code>dest</code>.
	 *
	 * @param v1
	 *            the first vector
	 * @param v2
	 *            the second vector
	 * @param dest
	 *            will hold the result
	 */
	public static void add(Vec3 v1, Vec3 v2, Vec3 dest) {
		dest.x = v1.x + v2.x;
		dest.y = v1.y + v2.y;
		dest.z = v1.z + v2.z;
	}

	/**
	 * Multiply this Vector3 by the given vector component-wise and store the
	 * result in <code>this</code>.
	 *
	 * @param v
	 *            the other vector
	 * @return this
	 */
	public Vec3 mul(Vec3 v) {
		x *= v.x;
		y *= v.y;
		z *= v.z;
		return this;
	}

	/**
	 * Multiply v1 by v2 component-wise and store the result in dest.
	 *
	 * @param v1
	 *            the first vector
	 * @param v2
	 *            the second vector
	 * @param dest
	 *            will hold the result
	 */
	public static void mul(Vec3 v1, Vec3 v2, Vec3 dest) {
		dest.x = v1.x * v2.x;
		dest.y = v1.y * v2.y;
		dest.z = v1.z * v2.z;
	}

	/**
	 * Multiply this Vector3 by the given matrix <code>mat</code> and store the
	 * result in <code>this</code>.
	 *
	 * @param mat
	 *            the matrix to multiply this vector by
	 * @return this
	 */
	public Vec3 mul(Matrix4 mat) {
		mul(this, mat, this);
		return this;
	}

	/**
	 * Multiply this Vector3 by the given matrix <code>mat</code> and store the
	 * result in <code>dest</code>.
	 *
	 * @param mat
	 *            the matrix to multiply this vector by
	 * @param dest
	 *            will hold the result
	 * @return this
	 */
	public Vec3 mul(Matrix4 mat, Vec3 dest) {
		mul(this, mat, dest);
		return this;
	}

	/**
	 * Multiply Vector3 v by the given matrix <code>mat</code> and store the
	 * result in dest.
	 *
	 * @param v
	 *            the vector to multiply
	 * @param mat
	 *            the matrix
	 * @param dest
	 *            will hold the result
	 */
	public static void mul(Vec3 v, Matrix4 mat, Vec3 dest) {
		if (v != dest) {
			dest.x = mat.m00 * v.x + mat.m10 * v.y + mat.m20 * v.z;
			dest.y = mat.m01 * v.x + mat.m11 * v.y + mat.m21 * v.z;
			dest.z = mat.m02 * v.x + mat.m12 * v.y + mat.m22 * v.z;
		} else {
			dest.set(mat.m00 * v.x + mat.m10 * v.y + mat.m20 * v.z, mat.m01 * v.x + mat.m11 * v.y
					+ mat.m21 * v.z, mat.m02 * v.x + mat.m12 * v.y + mat.m22 * v.z);
		}
	}

	/**
	 * Multiply this Vector3 by the given matrix and store the result in
	 * <code>this</code>.
	 *
	 * @param mat
	 *            the matrix
	 * @return this
	 */
	public Vec3 mul(Matrix3 mat) {
		mul(this, mat, this);
		return this;
	}

	/**
	 * Multiply this Vector3 by the given matrix and store the result in
	 * <code>dest</code>.
	 *
	 * @param mat
	 *            the matrix
	 * @param dest
	 *            will hold the result
	 * @return this
	 */
	public Vec3 mul(Matrix3 mat, Vec3 dest) {
		mul(this, mat, dest);
		return this;
	}

	/**
	 * Multiply Vector3 v by the given matrix and store the result in
	 * <code>dest</code>.
	 *
	 * @param v
	 *            the vector to multiply
	 * @param mat
	 *            the matrix
	 * @param dest
	 *            will hold the result
	 */
	public static void mul(Vec3 v, Matrix3 mat, Vec3 dest) {
		if (v != dest) {
			dest.x = mat.m00 * v.x + mat.m10 * v.y + mat.m20 * v.z;
			dest.y = mat.m01 * v.x + mat.m11 * v.y + mat.m21 * v.z;
			dest.z = mat.m02 * v.x + mat.m12 * v.y + mat.m22 * v.z;
		} else {
			dest.set(mat.m00 * v.x + mat.m10 * v.y + mat.m20 * v.z, mat.m01 * v.x + mat.m11 * v.y
					+ mat.m21 * v.z, mat.m02 * v.x + mat.m12 * v.y + mat.m22 * v.z);
		}
	}

	/**
	 * Multiply this Vector3 by the given scalar value and store the result in
	 * <code>this</code>.
	 *
	 * @param scalar
	 *            the scalar factor
	 * @return this
	 */
	public Vec3 mul(float scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
		return this;
	}

	/**
	 * Multiply the components of this Vector3 by the given scalar values and
	 * store the result in <code>this</code>.
	 *
	 * @return this
	 */
	public Vec3 mul(float x, float y, float z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return this;
	}

	/**
	 * Multiply the given Vector3 <code>v</code> by the scalar value and store
	 * the result in <code>dest</code>.
	 *
	 * @param v
	 *            the vector to scale
	 * @param scalar
	 *            the scalar factor
	 * @param dest
	 *            will hold the result
	 */
	public static void mul(Vec3 v, float scalar, Vec3 dest) {
		dest.x = v.x * scalar;
		dest.y = v.y * scalar;
		dest.z = v.z * scalar;
	}

	/**
	 * Return the length squared of this vector.
	 */
	public float lengthSquared() {
		return x * x + y * y + z * z;
	}

	/**
	 * Return the length of this vector.
	 */
	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	/**
	 * Normalize this vector.
	 *
	 * @return this
	 */
	public Vec3 normalize() {
		float d = length();
		x /= d;
		y /= d;
		z /= d;
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
	public static void normalize(Vec3 original, Vec3 dest) {
		float d = original.length();
		dest.set(original.x / d, original.y / d, original.z / d);
	}

	/**
	 * Set this vector to be the cross product of itself and <code>v</code>.
	 *
	 * @return this
	 */
	public Vec3 cross(Vec3 v) {
		return set(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
	}

	/**
	 * Set this vector to be the cross product of <code>v1</code> and
	 * <code>v2</code>.
	 *
	 * @return this
	 */
	public Vec3 cross(Vec3 v1, Vec3 v2) {
		return set(v1.y * v2.z - v1.z * v2.y, v1.z * v2.x - v1.x * v2.z, v1.x * v2.y - v1.y * v2.x);
	}

	/**
	 * Calculate the cross product of <code>v1</code> and <code>v2</code> and
	 * store the result in <code>dest</code>.
	 */
	public static void cross(Vec3 v1, Vec3 v2, Vec3 dest) {
		dest.set(v1.y * v2.z - v1.z * v2.y, v1.z * v2.x - v1.x * v2.z, v1.x * v2.y - v1.y * v2.x);
	}

	/**
	 * Return the distance between <code>start</code> and <code>end</code>.
	 */
	public static float distance(Vec3 start, Vec3 end) {
		return (float) Math.sqrt((end.x - start.x) * (end.x - start.x) + (end.y - start.y)
				* (end.y - start.y) + (end.z - start.z) * (end.z - start.z));
	}

	/**
	 * Return the distance between this Vector and <code>v</code>.
	 */
	public float distance(Vec3 v) {
		return (float) Math.sqrt((v.x - this.x) * (v.x - this.x) + (v.y - this.y) * (v.y - this.y)
				+ (v.z - this.z) * (v.z - this.z));
	}

	/**
	 * Return the dot product of this vector and the supplied vector.
	 */
	public float dot(Vec3 v) {
		return (x * v.x) + (y * v.y) + (z * v.z);
	}

	/**
	 * Return the dot product of the supplied v1 and v2 vectors.
	 */
	public static float dot(Vec3 v1, Vec3 v2) {
		return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
	}

	/**
	 * Set the components of this vector to be the component-wise minimum of
	 * this and the other vector.
	 *
	 * @param v
	 *            the other vector
	 * @return this
	 */
	public Vec3 min(Vec3 v) {
		this.x = Math.min(x, v.x);
		this.y = Math.min(y, v.y);
		this.z = Math.min(z, v.z);
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
	public Vec3 max(Vec3 v) {
		this.x = Math.max(x, v.x);
		this.y = Math.max(y, v.y);
		this.z = Math.max(z, v.z);
		return this;
	}

	/**
	 * Set all components to zero.
	 *
	 * @return this
	 */
	public Vec3 zero() {
		this.x = 0.0f;
		this.y = 0.0f;
		this.z = 0.0f;
		return this;
	}

	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeFloat(x);
		out.writeFloat(y);
		out.writeFloat(z);
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		x = in.readFloat();
		y = in.readFloat();
		z = in.readFloat();
	}

	/**
	 * Negate this vector.
	 *
	 * @return this
	 */
	public Vec3 negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}
}
