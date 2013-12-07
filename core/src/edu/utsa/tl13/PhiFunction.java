package edu.utsa.tl13;

import java.util.ArrayList;

public class PhiFunction {
	String orig_var;
	VarWithNumbers destination;
	ArrayList<VarWithNumbers> sources;
	
	public PhiFunction( String var, VarWithNumbers dest ) {
		orig_var = var;
		sources = new ArrayList<VarWithNumbers>();
		destination = dest;
		
	}
	
	public String getOrig_var() {
		return orig_var;
	}
	public void setOrig_var(String orig_var) {
		this.orig_var = orig_var;
	}
	public VarWithNumbers getDestination() {
		return destination;
	}
	public void setDestination(VarWithNumbers destination) {
		this.destination = destination;
	}
	public ArrayList<VarWithNumbers> getSources() {
		return sources;
	}
	public void setSources(ArrayList<VarWithNumbers> sources) {
		this.sources = sources;
	}

	public void addSource( VarWithNumbers var ) {
		sources.add(var);
	}
	
	public String printText() {
		String text = new String("phi ");
		text += this.getDestination().getName() + "." 
		+ this.getDestination().getNumber()
		+ " <-- ";
		for( VarWithNumbers s : this.sources ) {
			text += s.getName() + "." + s.getNumber() + " ";
		}
		return text;
	}
	
}
