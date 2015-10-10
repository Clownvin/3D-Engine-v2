package com.engine.util;

import java.awt.Color;

public enum ColorPalette {
	BLACK(Color.BLACK), GRAY_1(new Color(255 / 10, 255 / 10, 255 / 10)), SEAFOAM(
			mixColors(Color.GREEN, Color.CYAN)), GRAY_2(
					new Color((255 / 10) * 2, (255 / 10) * 2, (255 / 10) * 2)), GRAY_3(
							new Color((255 / 10) * 3, (255 / 10) * 3, (255 / 10) * 3)), GRAY_4(new Color(
									(255 / 10) * 4, (255 / 10)
											* 4,
							(255 / 10) * 4)), GRAY_5(new Color((255 / 10) * 5, (255 / 10) * 5, (255 / 10) * 5)), GRAY_6(
									new Color((255 / 10) * 6, (255 / 10) * 6, (255 / 10) * 6)), GRAY_7(
											new Color((255 / 10) * 7, (255 / 10) * 7, (255 / 10) * 7)), GRAY_8(
													new Color((255 / 10) * 8, (255 / 10) * 8, (255 / 10) * 8)), GRAY_9(
															new Color((255 / 10) * 9,
																	(255 / 10)
																			* 9,
															(255 / 10) * 9)), WHITE(Color.WHITE), RED(Color.RED), GREEN(
																	Color.GREEN), BLUE(Color.BLUE), CYAN(
																			Color.CYAN), MAGENTA(Color.MAGENTA), ORANGE(
																					Color.ORANGE), YELLOW(
																							Color.YELLOW), PURPLE(
																									mixColors(Color.RED,
																											Color.BLUE));
	public static Color darken(Color color, double percent) {
		if (percent > 1) {
			percent = 1;
		}
		double r = color.getRed() / 100.0D;
		double g = color.getGreen() / 100.0D;
		double b = color.getBlue() / 100.0D;
		double d = (1.0D - percent) * 100;
		return new Color((int) (r * d), (int) (g * d), (int) (b * d));
	}

	public static Color getOpposite(Color color) {
		int r = 255 - color.getRed();
		int g = 255 - color.getGreen();
		int b = 255 - color.getBlue();
		return new Color(r, g, b);
	}

	public static Color lightFilter(Color original, Color light) {
		float rPercent = (float) original.getRed() / (float) 0xFF;
		float gPercent = (float) original.getGreen() / (float) 0xFF;
		float bPercent = (float) original.getBlue() / (float) 0xFF;
		return new Color((int) (rPercent * light.getRed()), (int) (gPercent * light.getGreen()),
				(int) (bPercent * light.getBlue()));

	}

	public static Color mixByPercent(Color[] colors, float[] percents) {
		float totalPercent = 0.0f;
		for (int i = 0; i < percents.length; i++) {
			if (percents[i] > 1.0f) {
				percents[i] = 1.0f;
			}
			totalPercent += (1.0f - percents[i]);
		}
		int r = 0;
		int g = 0;
		int b = 0;
		for (int i = 0; i < colors.length; i++) {
			r += (int) (colors[i].getRed() * ((1.0f - percents[i]) / totalPercent));
			g += (int) (colors[i].getGreen() * ((1.0f - percents[i]) / totalPercent));
			b += (int) (colors[i].getBlue() * ((1.0f - percents[i]) / totalPercent));
		}
		if (r > 255) {
			r = 255;
		}
		if (g > 255) {
			g = 255;
		}
		if (b > 255) {
			b = 255;
		}
		if (r < 0) {
			r = 0;
		}
		if (g < 0) {
			g = 0;
		}
		if (b < 0) {
			b = 0;
		}
		return new Color(r, g, b);
	}

	public static Color mixColors(Color color1, Color color2) {
		int r = 0, g = 0, b = 0;
		r = color1.getRed();
		g = color1.getGreen();
		b = color1.getBlue();
		r += color2.getRed();
		g += color2.getGreen();
		b += color2.getBlue();
		return new Color(r / 2, g / 2, b / 2);
	}

	public static Color setAlpha(Color color, int alpha) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		return new Color(r, g, b, alpha);
	}

	private final Color color;

	private ColorPalette(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}
