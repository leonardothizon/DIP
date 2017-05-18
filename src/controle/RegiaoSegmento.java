package controle;

public class RegiaoSegmento {
	
	public int inicio, fim;
	public int[] pixelsCor;
	
	public RegiaoSegmento(int inicio, int fim, int[] pixelsCor) {
		this.inicio = inicio;
		this.fim = fim;
		this.pixelsCor = pixelsCor;
	}
	
	public boolean pertence(int valor) {
		if(valor >= inicio && valor <= fim)
			return true;
					
		return false;
	}
	
}
