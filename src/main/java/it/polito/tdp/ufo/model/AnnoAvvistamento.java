package it.polito.tdp.ufo.model;

public class AnnoAvvistamento {

	private int anno;
	private int avvistamenti;
	public AnnoAvvistamento(int anno, int avvistamenti) {
		super();
		this.anno = anno;
		this.avvistamenti = avvistamenti;
	}
	public int getAnno() {
		return anno;
	}
	public void setAnno(int anno) {
		this.anno = anno;
	}
	public int getAvvistamenti() {
		return avvistamenti;
	}
	public void setAvvistamenti(int avvistamenti) {
		this.avvistamenti = avvistamenti;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + anno;
		result = prime * result + avvistamenti;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnnoAvvistamento other = (AnnoAvvistamento) obj;
		if (anno != other.anno)
			return false;
		if (avvistamenti != other.avvistamenti)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return anno + "--" + avvistamenti;
	}
	
}
