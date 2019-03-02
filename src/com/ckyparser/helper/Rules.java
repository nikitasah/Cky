/* Rules class : A pojo class for getting and setting attributes of rules */

package com.ckyparser.helper;

import java.util.List;

public class Rules {
	private String head;
	private double probabilty;
	private List<String> symbols;
	
	@Override
	public String toString() {
		return "Rule is" + " " + getHead() + " " + getProbabilty() + " ";
	}
	
	public String getHead() {
		return head;
	}
	
	public void setHead(String head) {
		this.head = head;
	}
	
	public double getProbabilty() {
		return probabilty;
	}
	
	public void setProbabilty(double probabilty) {
		this.probabilty = probabilty;
	}
	
	public List<String> getSymbols() {
		return symbols;
	}
	
	public void setSymbols(List<String> symbols) {
		this.symbols = symbols;
	}
	
}
