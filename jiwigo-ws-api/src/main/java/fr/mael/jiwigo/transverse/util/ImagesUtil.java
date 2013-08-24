package fr.mael.jiwigo.transverse.util;


/*
 *  jiwigo-ws-api Piwigo webservice access Api
 *  Copyright (c) 2010-2011 Mael mael@le-guevel.com
 *                All Rights Reserved
 *
 *  This library is free software. It comes without any warranty, to
 *  the extent permitted by applicable law. You can redistribute it
 *  and/or modify it under the terms of the Do What The Fuck You Want
 *  To Public License, Version 2, as published by Sam Hocevar. See
 *  http://sam.zoy.org/wtfpl/COPYING for more details.
 */
/**

 * @author mael
 *
 */
public class ImagesUtil {

    //    private static final GraphicsConfiguration CONFIGURATION = GraphicsEnvironment.getLocalGraphicsEnvironment()
    //	    .getDefaultScreenDevice().getDefaultConfiguration();
    //
    //    //    public static void main(String[] args) throws IOException {
    //    //	scale("/home/mael/Bureau/test.png", "test4.jpg", 1999, 1999);
    //    //    }
    //
    //    /**
    //     * rotates an image
    //     * @param image the image to rotate
    //     * @param angle the angle of rotation
    //     * @return the rotated image
    //     */
    //    public static BufferedImage rotate(BufferedImage image, double angle) {
    //	GraphicsConfiguration gc = getDefaultConfiguration();
    //	double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
    //	int w = image.getWidth(), h = image.getHeight();
    //	int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h * cos + w * sin);
    //	int transparency = image.getColorModel().getTransparency();
    //	BufferedImage result = gc.createCompatibleImage(neww, newh, transparency);
    //	Graphics2D g = result.createGraphics();
    //	g.translate((neww - w) / 2, (newh - h) / 2);
    //	g.rotate(angle, w / 2, h / 2);
    //	g.drawRenderedImage(image, null);
    //	return result;
    //    }
    //
    //    /**
    //     * @return the graphics configuration
    //     */
    //    public static GraphicsConfiguration getDefaultConfiguration() {
    //	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    //	GraphicsDevice gd = ge.getDefaultScreenDevice();
    //	return gd.getDefaultConfiguration();
    //    }
    //
    //    /**
    //     * Writes an image in a file
    //     * @param file the file
    //     * @param bufferedImage the image
    //     * @throws IOException
    //     */
    //    public static void writeImageToPNG(File file, BufferedImage bufferedImage) throws IOException {
    //	ImageIO.write(bufferedImage, "png", file);
    //    }
    //
    //    /**
    //     * scales an image
    //     * @param filePath the path to the file of the image
    //     * @param tempName the name of the resulting file
    //     * @param width the width
    //     * @param height the height
    //     * @return true if successful
    //     * @throws IOException
    //     * @throws Exception
    //     */
    //    public static boolean scale(String filePath, String tempName, int width, int height) throws IOException {
    //	InputStream imageStream = new BufferedInputStream(new FileInputStream(filePath));
    //	Image image = (Image) ImageIO.read(imageStream);
    //
    //	double thumbRatio = (double) width / (double) height;
    //	int imageWidth = image.getWidth(null);
    //	int imageHeight = image.getHeight(null);
    //	if (imageWidth < width || imageHeight < height) {
    //	    return false;
    //	}
    //	double imageRatio = (double) imageWidth / (double) imageHeight;
    //	if (thumbRatio < imageRatio) {
    //	    height = (int) (width / imageRatio);
    //	} else {
    //	    width = (int) (height * imageRatio);
    //	}
    //	//	BufferedImage thumbImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    //	BufferedImage thumbImage = createCompatibleImage(ImageIO.read(new File(filePath)), width, height);
    //	Graphics2D graphics2D = thumbImage.createGraphics();
    //	graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    //	graphics2D.drawImage(image, 0, 0, width, height, null);
    //	graphics2D.dispose();
    //	//	ImageIO.write(thumbImage, "jpg", new FileOutputStream(System.getProperty("java.io.tmpdir") + "/" + tempName));
    //	saveImage(System.getProperty("java.io.tmpdir") + "/" + tempName, thumbImage);
    //	return true;
    //
    //    }
    //
    //    public static BufferedImage createCompatibleImage(BufferedImage image, int width, int height) {
    //	return CONFIGURATION.createCompatibleImage(width, height, image.getTransparency());
    //    }
    //
    //    /**
    //     * Save an image
    //     * @param fileName the name of the file
    //     * @param img the img
    //     * @throws FileNotFoundException
    //     * @throws IOException
    //     */
    //    private static void saveImage(String fileName, BufferedImage img) throws FileNotFoundException, IOException {
    //	Iterator iter = ImageIO.getImageWritersByFormatName("jpeg");
    //	ImageWriter writer = (ImageWriter) iter.next();
    //	ImageWriteParam iwp = writer.getDefaultWriteParam();
    //	iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    //	iwp.setCompressionQuality(1);
    //	File outputfile = new File(fileName);
    //	FileImageOutputStream output = new FileImageOutputStream(outputfile);
    //	writer.setOutput(output);
    //	IIOImage outimage = new IIOImage(img, null, null);
    //	writer.write(null, outimage, iwp);
    //	writer.dispose();
    //    }
    //
    //    // This method returns a buffered image with the contents of an image
    //    public static BufferedImage toBufferedImage(Image image) {
    //	if (image instanceof BufferedImage) {
    //	    return (BufferedImage) image;
    //	}
    //
    //	// This code ensures that all the pixels in the image are loaded
    //	image = new ImageIcon(image).getImage();
    //
    //	// Determine if the image has transparent pixels; for this method's
    //	// implementation, see Determining If an Image Has Transparent Pixels
    //	boolean hasAlpha = hasAlpha(image);
    //
    //	// Create a buffered image with a format that's compatible with the screen
    //	BufferedImage bimage = null;
    //	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    //	try {
    //	    // Determine the type of transparency of the new buffered image
    //	    int transparency = Transparency.OPAQUE;
    //	    if (hasAlpha) {
    //		transparency = Transparency.BITMASK;
    //	    }
    //
    //	    // Create the buffered image
    //	    GraphicsDevice gs = ge.getDefaultScreenDevice();
    //	    GraphicsConfiguration gc = gs.getDefaultConfiguration();
    //	    bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
    //	} catch (HeadlessException e) {
    //	    // The system does not have a screen
    //	}
    //
    //	if (bimage == null) {
    //	    // Create a buffered image using the default color model
    //	    int type = BufferedImage.TYPE_INT_RGB;
    //	    if (hasAlpha) {
    //		type = BufferedImage.TYPE_INT_ARGB;
    //	    }
    //	    bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
    //	}
    //
    //	// Copy image to buffered image
    //	Graphics g = bimage.createGraphics();
    //
    //	// Paint the image onto the buffered image
    //	g.drawImage(image, 0, 0, null);
    //	g.dispose();
    //
    //	return bimage;
    //    }
    //
    //    /**
    //     *  This method returns true if the specified image has transparent pixels
    //     * @param image the image to check
    //     * @return true or false
    //     */
    //    public static boolean hasAlpha(Image image) {
    //	// If buffered image, the color model is readily available
    //	if (image instanceof BufferedImage) {
    //	    BufferedImage bimage = (BufferedImage) image;
    //	    return bimage.getColorModel().hasAlpha();
    //	}
    //
    //	// Use a pixel grabber to retrieve the image's color model;
    //	// grabbing a single pixel is usually sufficient
    //	PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
    //	try {
    //	    pg.grabPixels();
    //	} catch (InterruptedException e) {
    //	}
    //
    //	// Get the image's color model
    //	ColorModel cm = pg.getColorModel();
    //	return cm.hasAlpha();
    //    }

}