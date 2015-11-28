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
import java.nio.FloatBuffer;

/**
 * Contains the definition of a 3x3 Matrix of floats, and associated functions to transform
 * it. The matrix is column-major to match OpenGL's interpretation, and it looks like this:
 * <p>
 *      m00  m10  m20</br>
 *      m01  m11  m21</br>
 *      m02  m12  m22</br>
 * 
 * @author Richard Greenlees
 * @author Kai Burjack
 */
public class Matrix2 implements Serializable, Externalizable {

    public float m00;
    public float m01;
    public float m10;
    public float m11;
    
    public Matrix2() {
        super();
        identity();
    }

    public Matrix2(Matrix2 mat) {
        this.m00 = mat.m00;
        this.m01 = mat.m01;
        this.m10 = mat.m10;
        this.m11 = mat.m11;
    }
    
    public Matrix2(float m00, float m01, float m02, float m10, float m11,
                    float m12, float m20, float m21, float m22) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
    }

    /** Set the values in this matrix to the ones in m1 */
    public Matrix2 set(Matrix2 m1) {
        m00 = m1.m00;
        m01 = m1.m01;
        m10 = m1.m10;
        m11 = m1.m11;
        return this;
    }

    /**
     * Multiplies this matrix by the supplied matrix. This matrix will be the left-sided one.
     * 
     * @param right
     * @return this
     */
    public Matrix2 mul(Matrix2 right) {
        mul(this, right, this);
        return this;
    }

    /** Multiplies the left matrix by the right, and stores the results in dest. Does not modify the left or right matrices */
    public static void mul(Matrix2 left, Matrix2 right, Matrix2 dest) {
        if (left != dest && right != dest) {
            dest.m00 = left.m00 * right.m00 + left.m10 * right.m01 ;
            dest.m01 = left.m01 * right.m00 + left.m11 * right.m01 ;
            dest.m10 = left.m00 * right.m10 + left.m10 * right.m11 ;
            dest.m11 = left.m01 * right.m10 + left.m11 * right.m11 ;
        } else {
            dest.set( left.m00 * right.m00 + left.m10 * right.m01 ,
                      left.m01 * right.m00 + left.m11 * right.m01 ,
                      left.m00 * right.m10 + left.m10 * right.m11 ,
                      left.m01 * right.m10 + left.m11 * right.m11 );
        }
    }
    
    /** Sets the values within this matrix to the supplied float values. The result looks like this:
     * <p>
     * m00, m10, m20</br>
     * m01, m11, m21</br>
     * m02, m12, m22</br>
     */
    public Matrix2 set(float m00, float m01, float m10, float m11) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
        return this;
    }

    /** Sets the values in this matrix based on the supplied float array. The result looks like this:
     * <p>
     * 0, 3, 6</br>
     * 1, 4, 7</br>
     * 2, 5, 8</br>
     * 
     * Only uses the first 9 values, all others are ignored
     * 
     * @return this
     */
    public Matrix2 set(float m[]) {
        m00 = m[0];
        m01 = m[1];
        m10 = m[3];
        m11 = m[4];
        return this;
    }

    /** Returns the determinant of this Matrix */
    public float determinant() {
        return   m00 * m11
               - m10 * m01;
    }

    /**
     * Invert this matrix.
     *
     * @return this
     */
    public Matrix2 invert() {
        float s = determinant();
        
        if (s == 0.0f) {
            return this;
        }
        s = 1.0f / s;

        return set(        		
        		s * m11,
        		s * -m10,
        		s * -m01,
        		s * m00);
    }
  
    
    /**
     * Transpose this matrix.
     *
     * @return this
     */
    public Matrix2 transpose() {
        transpose(this, this);
        return this;
    }
    
    /**
     * Transpose the supplied original matrix and store the results in dest.
     */
    public static void transpose(Matrix2 original, Matrix2 dest) {
        if (original != dest) {
            dest.m00 = original.m00;
            dest.m01 = original.m10;
            dest.m10 = original.m01;
            dest.m11 = original.m11;
        } else {
            dest.set(original.m00, original.m10,
                     original.m01, original.m11);
        }
    }
    
    /** Multiply the supplied Matrix by the supplied scalar value and store the results in dest. Does not modify the source */
    public static void mul(Matrix2 source, float scalar, Matrix2 dest) {
        dest.m00 = source.m00 * scalar;
        dest.m01 = source.m01 * scalar;
        dest.m10 = source.m10 * scalar;
        dest.m11 = source.m11 * scalar;
    }
    
    public String toString() {
        return "Matrix3 { " + this.m00 + ", " + this.m10 + ",\n"
                + "           " + this.m01 + ", " + this.m11 +  "}\n";

    }
     
    /**
     * Get the current values of <code>this</code> matrix and store them into
     * <code>dest</code>.
     * <p>
     * This is the reverse method of {@link #set(Matrix2)} and allows to obtain
     * intermediate calculation results when chaining multiple transformations.
     * 
     * @param dest
     *          the destination matrix
     * @return this
     */
    public Matrix2 get(Matrix2 dest) {
        dest.set(this);
        return this;
    }

    /** Stores this matrix in the supplied FloatBuffer */
    public Matrix2 get(FloatBuffer buffer) {
        buffer.put(this.m00);
        buffer.put(this.m01);
        buffer.put(this.m10);
        buffer.put(this.m11);
        return this;
    }

    /** Read and set the matrix values from the supplied FloatBuffer */
    public Matrix2 set(FloatBuffer buffer) {
        this.m00 = buffer.get();
        this.m01 = buffer.get();
        this.m10 = buffer.get();
        this.m11 = buffer.get();
        return this;
    }

    /** Sets all the values within this matrix to 0 */
    public Matrix2 zero() {
        this.m00 = 0.0f;
        this.m01 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 0.0f;
        return this;
    }
    
    /** Sets this matrix to the identity */
    public Matrix2 identity() {
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        return this;
    }

    /**
     * Apply scaling to this matrix by scaling the unit axes by the given x,
     * y and z factors.
     * <p>
     * If <code>M</code> is <code>this</code> matrix and <code>S</code> the scaling matrix,
     * then the new matrix will be <code>M * S</code>. So when transforming a
     * vector <code>v</code> with the new matrix by using <code>M * S * v</code>
     * , the scaling will be applied first!
     * 
     * @param x
     *            the factor of the x component
     * @param y
     *            the factor of the y component
     * @param z
     *            the factor of the z component
     * @return this
     */
    public Matrix2 scale(float x, float y, float z) {
        // scale matrix elements:
        // m00 = x, m11 = y, m22 = z
        // all others = 0
        m00 = m00 * x;
        m01 = m01 * x;
        m10 = m10 * y;
        m11 = m11 * y;
        return this;
    }

    /**
     * Apply scaling to this matrix by uniformly scaling all unit axes by the given <code>xyz</code> factor.
     * <p>
     * If <code>M</code> is <code>this</code> matrix and <code>S</code> the scaling matrix,
     * then the new matrix will be <code>M * S</code>. So when transforming a
     * vector <code>v</code> with the new matrix by using <code>M * S * v</code>
     * , the scaling will be applied first!
     * 
     * @see #scale(float, float, float)
     * 
     * @param xyz
     *            the factor for all components
     * @return this
     */
    public Matrix2 scale(float xyz) {
        return scale(xyz, xyz, xyz);
    }

    /**
     * Set the given matrix <code>dest</code> to be a simple scale matrix.
     * 
     * @param scale
     * 			the scale applied to each dimension
     */
    public static void scaling(Vec2 scale, Matrix2 dest) {
    	dest.identity();
        dest.m00 = scale.x;
        dest.m11 = scale.y;
    }
    
    /**
     * Set this matrix to be a simple scale matrix.
     * 
     * @param x
     * 			the scale in x
     * @param y
     * 			the scale in y
     * @param z
     * 			the scale in z
     * @return this
     */
    public Matrix2 scaling(float x, float y,  Matrix2 dest) {
    	dest.identity();
    	dest.m00 = x;
    	dest.m11 = y;
    	return this;
    }

    /**
     * Set this matrix to a rotation matrix which rotates the given radians about the given axis.
     * 
     * From <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Rotation_matrix_from_axis_and_angle">Wikipedia</a>
     * 
     * @return this
     */
    public Matrix2 rotation(float angle) {
    	float x=0;
    	float y=0;
    	float z=1;
    	float cos = (float) Math.cos(angle);
    	float sin = (float) Math.sin(angle);
    	float C = 1.0f - cos;
    	m00 = cos + x * x * C;
    	m10 = x * y * C - z * sin;
    	m01 = y * x * C + z * sin;
    	m11 = cos + y * y * C;
    	return this;
    }
    
    public Vec2 getAhead() {
    	return new Vec2(m01,m11);
    }


    /**
     * Set the destination matrix to a rotation matrix which rotates the given radians about a given axis.
     * 
     * From <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Rotation_matrix_from_axis_and_angle">Wikipedia</a>
     */
    public static void rotation(float angle, Matrix2 dest) {
    	float cos = (float) Math.cos(angle);
    	float sin = (float) Math.sin(angle);
        float C = 1.0f - cos;
    	float x=0;
    	float y=0;
    	float z=1;
    	dest.m00 = cos + x * x * C;
    	dest.m10 = x * y * C - z * sin;
    	dest.m01 = y * x * C + z * sin;
    	dest.m11 = cos + y * y * C;
    }
    

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeFloat(m00);
        out.writeFloat(m01);
        out.writeFloat(m10);
        out.writeFloat(m11);
    }

    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        m00 = in.readFloat();
        m01 = in.readFloat();
        m10 = in.readFloat();
        m11 = in.readFloat();
    }
}
