package org.eclipse.wb.swt;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import controle.Pixel;
import controle.RGB;
import controle.TipoVizinhanca;

public class Util {
	
	public static void definirRGB(Pixel pixel, BufferedImage img) {
		
		int pixels[] = new int[4];
		WritableRaster raster = img.getRaster();
		raster.getPixel(pixel.x, pixel.y, pixels);
		pixel.r = pixels[0];
		pixel.g = pixels[1];
		pixel.b = pixels[2];
		
	}

	public static List<Pixel> vizinhos(Pixel pixel, TipoVizinhanca tipo) {
		
		List<Pixel> vizinhos = new ArrayList<>();
		
		if(tipo.equals(TipoVizinhanca.QUADRADO)) {
			vizinhos.add(pixel.pixelCima());
			vizinhos.add(pixel.pixelCimaEsquerda());
			vizinhos.add(pixel.pixelCimaDireita());
			vizinhos.add(pixel.pixelDireita());
			vizinhos.add(pixel.pixelEsquerda());
			vizinhos.add(pixel.pixelBaixo());
			vizinhos.add(pixel.pixelBaixoEsquerda());
			vizinhos.add(pixel.pixelBaixoDireita());
		} else if(tipo.equals(TipoVizinhanca.CRUZ)) {
			vizinhos.add(pixel.pixelCima());
			vizinhos.add(pixel.pixelDireita());
			vizinhos.add(pixel.pixelEsquerda());
			vizinhos.add(pixel.pixelBaixo());
		} else if(tipo.equals(TipoVizinhanca.XIS)) {
			vizinhos.add(pixel.pixelCimaEsquerda());
			vizinhos.add(pixel.pixelCimaDireita());
			vizinhos.add(pixel.pixelBaixoEsquerda());
			vizinhos.add(pixel.pixelBaixoDireita());
		}
		
		return vizinhos;
		
	}
	
	public static double mediana(List<Pixel> pixels, RGB rgb) {
		
		Collections.sort(pixels, new Comparator<Pixel>() {
	        @Override
	        public int compare(Pixel px1, Pixel px2)
	        {
	            return  px2.getCor(rgb) - px1.getCor(rgb);
	        }
	    });
		
		double mediana;
		int meio = pixels.size() / 2;
		if (pixels.size() % 2 == 0)
		    mediana = (pixels.get(meio - 1).getCor(rgb) + pixels.get(meio - 1).getCor(rgb)) / 2;
		else
		    mediana = pixels.get(meio).getCor(rgb);
		
		return mediana;
		
	}
	
	public static double media(List<Pixel> pixels, RGB rgb) {
		
		double total = 0;
		for(Pixel px : pixels) {
			total += px.getCor(rgb);
		}
		
		return total / pixels.size();		
		
	}
	
}
