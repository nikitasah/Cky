/* BackwardPathMatrix class : Class to get and set back pointers */
package com.ckyparser.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackwardPathMatrix {
	private final Map<Integer, Map<Integer, Map<String, List<String>>>> backPathMatrixMap; 

	public BackwardPathMatrix() {
		backPathMatrixMap = new HashMap<>();
	}

	/* Function : getBackPointerSpecificCell
	 * @in : row, col, symbol
	 * @out : a list of symbols
	 * Purpose : Get the symbols for a specific cell 
	 */
	
	public List<String> getBackPointerSpecificCell(int row, int col, String symbol){
		return backPathMatrixMap.getOrDefault(row, new HashMap<>()).getOrDefault(col, new HashMap<>()).get(symbol);
	}

	/* Function : setBackPointerSpecificCell
	 * @in : row, col, symbol, midPointer, gead1, head2
	 * Purpose : Set the pointers for a specific cell for back tracking 
	 */
	public void setBackPointerSpecificCell(int row, int col, String symbol, String midPointer, String head1, String head2) {
		List<String> pointer = new ArrayList<>();
		pointer.add(0, midPointer);;
		pointer.add(1, head1);
		pointer.add(2, head2);

		if(!backPathMatrixMap.containsKey(row)) {
			backPathMatrixMap.put(row, new HashMap<>());
		}

		if(!backPathMatrixMap.get(row).containsKey(col)) {
			backPathMatrixMap.get(row).put(col, new HashMap<>());
		}	
		backPathMatrixMap.get(row).get(col).put(symbol, pointer);
	}
}
