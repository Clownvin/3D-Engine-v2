package com.engine.environment.map;

import java.awt.Color;
import java.util.ArrayList;

import com.engine.math.Face;
import com.engine.math.Point3D;
import com.engine.util.ColorPalette;

public final class MapGenerator {
	
	private static Face[] pointDataToFaces(float[][] pointData, float scale) {
		int width = pointData.length;
		int height = pointData[0].length;
		ArrayList<Face> faceList = new ArrayList<Face>(width*height*2);
		int idx = 0;
		for (int x = 1; x < width; x++) {
			for (int y = 1; y < height; y++) {
				faceList.add(new Face(new Point3D[] {new Point3D(((x-(width/2))-1) * scale, ((y-(height/2))-1) * scale, pointData[x-1][y-1]), new Point3D((x-(width/2)) * scale, ((y-(height/2))-1) * scale, pointData[x][y-1]), new Point3D((x-(width/2)) * scale, (y-(height/2)) * scale, pointData[x][y])}, Color.WHITE));
				faceList.add(new Face(new Point3D[] {new Point3D((x-(width/2)) * scale, (y-(height/2)) * scale, pointData[x][y]), new Point3D(((x-(width/2))-1) * scale, (y-(height/2)) * scale, pointData[x-1][y]), new Point3D(((x-(width/2))-1) * scale, ((y-(height/2))-1) * scale, pointData[x-1][y-1])}, Color.WHITE));
			}
		}
		return faceList.toArray(new Face[faceList.size()]);
	}
	
	private static float[][] diffuseHeight(float[][] pointData) {
		float avgHeight = 0;
		int width = pointData.length;
		int height = pointData[0].length;
		for (int x = 2; x < width - 2; x++) {
			for (int y = 2; y < height - 2; y++) {
				int t = 0;
				float tHeight = 0.0f;
				for (int x2 = x - 2; x2 < x + 2; x2++) {
					for (int y2 = y - 2; y2 < y + 2; y2++) {
						tHeight += pointData[x2][y2];
						t++;
					}
				}
				pointData[x][y] = (0.5f * pointData[x][y]) + (0.5f * (tHeight / (float) t));
			}
		}
		for (int x = width - 3; x > 3; x--) {
			for (int y = height - 3; y > 3; y--) {
				int t = 0;
				float tHeight = 0.0f;
				for (int x2 = x - 2; x2 < x + 2; x2++) {
					for (int y2 = y - 2; y2 < y + 2; y2++) {
						tHeight += pointData[x2][y2];
						t++;
					}
				}
				pointData[x][y] = (0.5f * pointData[x][y]) + (0.5f * (tHeight / (float) t));
				avgHeight += pointData[x][y];
			}
		}
		avgHeight /= width * height;
		System.out.println("Avg height: "+(avgHeight));
		return pointData;
	}
	
	//Frequency 0.0 - 1.0
	private static float[][] generateHills(float[][] pointData, float frequency, int minHeight, int maxHeight, float scale) {
		int width = pointData.length;
		int height = pointData[0].length;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (x <= 1 || y <= 1 || x >= width - 2 || y >= height -2)
					continue;
				pointData[x][y] = (float) (Math.random() * Integer.MAX_VALUE < frequency * Integer.MAX_VALUE ? ((Math.random() * (double)(maxHeight - minHeight)) + minHeight) / scale : pointData[x][y]);
			}
		}
		return pointData;
	}
	
	public static Face[] generateBaseMap(int width, int height, float scale) {
		float[][] pointData = new float[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				pointData[x][y] = 0.0f;
			}
		}
		float lastPercent = 1.0f;
		float lastHeight = 10;
		for (int i = 0; i < 50; i++) {
			lastPercent *= 0.80f;
			lastHeight = 10 * ( 1.0f/(lastPercent));
			pointData = generateHills(pointData, lastPercent, (int) lastHeight, (int)lastHeight * 2, scale);
		}
		for (int i = 0; i < 12; i++) {
		pointData = diffuseHeight(pointData);
		}
		return pointDataToFaces(pointData, scale);
	}
}
