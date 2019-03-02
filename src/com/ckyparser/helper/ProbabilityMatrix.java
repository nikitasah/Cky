/* ProbabilityMatrix class : Class to get and set probability for speicfic row or col */
package com.ckyparser.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProbabilityMatrix {
	private Map<Integer, Map<Integer, Map<String, Double>>> probabilityMatrixMap;
	
	public ProbabilityMatrix() {
		probabilityMatrixMap = new HashMap<>();
	}
	

	/* Function : getSpecificCellProbability
	 * @in : row, col, symbol
	 * @out : a double value
	 * Purpose : Get the probability for a specific cell 
	 */
	
	public double getSpecificCellProbability(int row, int col, String symbol) {
		return probabilityMatrixMap.getOrDefault(row, new HashMap<>()).getOrDefault(col, new HashMap<>()).getOrDefault(symbol, 0.0);
	}
	
	/* Function : setSpecificCellProbability
	 * @in : row, col, symbol, probability
	 * Purpose : Set the probability for a specific cell 
	 */
	
	public void setSpecificCellProbability(int row, int col, String symbol, double probability) {
		if(!probabilityMatrixMap.containsKey(row)) {
			probabilityMatrixMap.put(row, new HashMap<>());
		}
		if(!probabilityMatrixMap.get(row).containsKey(col)) {
			probabilityMatrixMap.get(row).put(col, new HashMap<>());
		}
		probabilityMatrixMap.get(row).get(col).put(symbol, probability);
	}
	
	/* Function : getHeadsForOneRowOrCol
	 * @in : row, col
	 * out : Set of heads
	 * Purpose : Get the heads for a particular row or col 
	 */
	public Set<String> getHeadsForOneRowOrCol(int row, int col) {
		return probabilityMatrixMap.getOrDefault(row, new HashMap<>()).getOrDefault(col, new HashMap<>()).keySet();
	}
}
