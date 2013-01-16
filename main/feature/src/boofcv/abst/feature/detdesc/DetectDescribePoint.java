/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.abst.feature.detdesc;

import boofcv.abst.feature.detect.interest.InterestPointDetector;
import boofcv.struct.feature.TupleDesc;
import boofcv.struct.image.ImageSingleBand;

/**
 * Interface for detecting and describing point features. By detecting and describing at the same time some algorithms
 * can be significantly speed up by avoiding recomputing preprocessing steps twice.
 *
 * @author Peter Abeles
 */
public interface DetectDescribePoint<T extends ImageSingleBand, TD extends TupleDesc>
		extends InterestPointDetector<T>
{
	/**
	 * Creates new description instance which can be processed by this class
	 *
	 * @return New descriptor
	 */
	public TD createDescription();

	/**
	 * Returns the feature descriptor at the specified index.
	 *
	 * @param index Which feature
	 * @return Feature descriptor
	 */
	public TD getDescription(int index);

	/**
	 * The type of region descriptor generated
	 *
	 * @return Returns the descriptor type.
	 */
	public Class<TD> getDescriptionType();

	/**
	 * Number of elements in the descriptor tuple.
	 *
	 * @return Length of descriptor.
	 */
	public int getDescriptionLength();
}