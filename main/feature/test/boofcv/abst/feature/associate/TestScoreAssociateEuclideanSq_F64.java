/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
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

package boofcv.abst.feature.associate;

import boofcv.struct.feature.TupleDesc_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestScoreAssociateEuclideanSq_F64 {
	@Test
	public void compareToExpected() {
		ScoreAssociateEuclideanSq_F64 score = new ScoreAssociateEuclideanSq_F64();

		TupleDesc_F64 a = new TupleDesc_F64(5);
		TupleDesc_F64 b = new TupleDesc_F64(5);

		a.value=new double[]{1,2,3,4,5};
		b.value=new double[]{2,-1,7,-8,10};

		assertEquals(195,score.score(a,b),1e-4);
	}

	@Test
	public void check() {
		ScoreAssociateEuclideanSq_F64 score = new ScoreAssociateEuclideanSq_F64();
		assertTrue(score.isZeroMinimum());
	}
}