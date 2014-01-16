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

package boofcv.alg.segmentation;

import boofcv.alg.weights.WeightDistanceUniform_F32;
import boofcv.alg.weights.WeightDistance_F32;
import boofcv.alg.weights.WeightPixelUniform_F32;
import boofcv.alg.weights.WeightPixel_F32;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.factory.segmentation.FactorySegmentationAlg;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSInt32;
import org.ddogleg.struct.GrowQueue_F32;
import org.ddogleg.struct.GrowQueue_I32;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author Peter Abeles
 */
public class VisualizeSegmentMeanShiftApp {

	public static void main(String[] args) {
//		BufferedImage image = UtilImageIO.loadImage("../data/evaluation/sunflowers.png");
//		BufferedImage image = UtilImageIO.loadImage("../data/evaluation/shapes01.png");
//		BufferedImage image = UtilImageIO.loadImage("../data/applet/trees_rotate_01.jpg");
//		BufferedImage image = UtilImageIO.loadImage("../data/applet/segment/mountain_pines_people.jpg");
		BufferedImage image = UtilImageIO.loadImage("/home/pja/Desktop/segmentation/example-orig.jpg");

		ImageFloat32 gray = ConvertBufferedImage.convertFrom(image,(ImageFloat32)null);

		BufferedImage outColor = new BufferedImage(gray.width,gray.height,BufferedImage.TYPE_INT_RGB);
		BufferedImage outPeak = new BufferedImage(gray.width,gray.height,BufferedImage.TYPE_INT_RGB);

//		BlurImageOps.gaussian(gray,gray,0.5,-1,null);

		WeightPixel_F32 weightSpacial = new WeightPixelUniform_F32();
		WeightDistance_F32 weightGray = new WeightDistanceUniform_F32(4.5f);

		SegmentMeanShiftGray<ImageFloat32> alg =
				FactorySegmentationAlg.meanShiftGray(30,0.1f,weightSpacial,weightGray,ImageFloat32.class);

		alg.setRadius(6);
		long time0 = System.currentTimeMillis();
		alg.process(gray);
		long time1 = System.currentTimeMillis();

		GrowQueue_F32 peakValue = alg.getPeakValue();
		ImageSInt32 peakToIndex = alg.getPeakToIndex();
		GrowQueue_I32 peakCounts = alg.getPeakMemberCount();

		MergeRegionMeanShiftGray merge = new MergeRegionMeanShiftGray(1);
		merge.merge(peakToIndex,peakCounts,peakValue);

		long time2 = System.currentTimeMillis();

		Random rand = new Random(234);

		int colors[] = new int[ peakValue.size ];
		for( int i = 0; i < colors.length; i++ ) {
			colors[i] = rand.nextInt();
		}

		for( int i = 0; i < peakCounts.size; i++ ) {
			if( peakCounts.get(i) < 20 ) {
				colors[i] = 0;
				peakValue.set(i, -1);
			}
		}

		for( int y = 0; y < gray.height; y++ ) {
			for( int x = 0; x < gray.width; x++ ) {
				int index = peakToIndex.unsafe_get(x,y);
				int gv = (int)peakValue.get(index);

				int grayRGB = gv == -1 ? 0xFF0000 : gv << 16 | gv << 8 | gv;

				outColor.setRGB(x, y, colors[index]);
				outPeak.setRGB(x,y,grayRGB);
			}
		}

		System.out.println("Time MS "+(time1-time0)+"  Time Merge "+(time2-time1));

		ShowImages.showWindow(outColor,"Regions");
		ShowImages.showWindow(outPeak,"Color of Peak");
	}
}