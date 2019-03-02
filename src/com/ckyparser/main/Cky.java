/* Cky class : Implementation of CKY parser */
package com.ckyparser.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.ckyparser.helper.BackwardPathMatrix;
import com.ckyparser.helper.ProbabilityMatrix;
import com.ckyparser.helper.Rules;

public class Cky {
	private List<Rules> rulesOfGrammar;
	private ProbabilityMatrix probMatrix;
	private BackwardPathMatrix backPointerMatrix;
	private String[] wordsOfSentence;

	
	/* Function : generateParseTree
	 * @out : Generated parse tree
	 * Purpose : Logic for building parse tree using cky algorithm
	 */
	
	public String generateParseTree() {		
		int colNo = 0;
		//Dynamic program
		for(String sentenceWord : getWordsOfSentence()) {
			sentenceWord = sentenceWord.toLowerCase();
			colNo++;
			//Set probability directly when terminal symbols is word
			List<String> wordList = Arrays.asList(sentenceWord);
			for(Rules rule : getRules(wordList)) {
				probMatrix.setSpecificCellProbability(colNo - 1, colNo, rule.getHead(), rule.getProbabilty());
				backPointerMatrix.setBackPointerSpecificCell(colNo - 1, colNo, rule.getHead(), null, null, null);			
				addSingleRules(colNo - 1, colNo);
			}

			// If two existing non-terminals form a production, dynamic program
			for(int i = colNo - 2; i >= -1; i--) {
				for(int j = i + 1; j <= colNo; j++) {
					for(String head1 : probMatrix.getHeadsForOneRowOrCol(i, j)) {
						for(String head2 : probMatrix.getHeadsForOneRowOrCol(j, colNo)) {
							List<String> headList = Arrays.asList(head1, head2);
							for(Rules r : getRules(headList)) { //Rules of form A->BC
								//Get the proability
								double probability = r.getProbabilty() * probMatrix.getSpecificCellProbability(i, j, head1) 
										* probMatrix.getSpecificCellProbability(j, colNo, head2);
								if(probability > probMatrix.getSpecificCellProbability(i, colNo, r.getHead())) {
									probMatrix.setSpecificCellProbability(i, colNo, r.getHead(), probability);
									backPointerMatrix.setBackPointerSpecificCell(i, colNo, r.getHead(), String.valueOf(j), head1, head2);
								}
							}
						}
					}
				}
				addSingleRules(i, colNo);			
			}
		}
		return getResultParseTree(0, getWordsOfSentence().length ,"s"); //S refers to the start symbol
	}

	
	/* Function : getResultParseTree
	 * @out : Generated parse tree
	 * Purpose : Logic for building the tree using back pointer matrix
	 */
	
	private  String getResultParseTree(int row, int column, String symbol) {
		List<String> pointersFromMatrix = backPointerMatrix.getBackPointerSpecificCell(row, column, symbol);
		String resultString = "";
		//Return the terminal symbol
		if(pointersFromMatrix == null || pointersFromMatrix.get(0) == null && pointersFromMatrix.get(1) == null && pointersFromMatrix.get(2) == null) {
			String[] words = getWordsOfSentence();
			resultString = "[" + symbol.toUpperCase() + " " + words[row] + "]";
			return resultString; 
		}
		else if(pointersFromMatrix.get(0) == null && pointersFromMatrix.get(2) == null) {
			//Recursively call getResultParseTree for all chain of singleRules
			String tempTree = getResultParseTree(row, column, pointersFromMatrix.get(1));
			resultString = "[" + symbol.toUpperCase() + " " + tempTree + "]"; 
			return resultString;
		}
		else {
			//Recursively call getResultParseTree for all non-terminals
			String tempTree1 = getResultParseTree(row, Integer.parseInt(pointersFromMatrix.get(0)), pointersFromMatrix.get(1));
			String tempTree2 = getResultParseTree(Integer.parseInt(pointersFromMatrix.get(0)), column, pointersFromMatrix.get(2));
			resultString = "[" + symbol.toUpperCase() + " " + tempTree1 + " " + tempTree2 + "]";
			return resultString;
		}
	}

	
	/* Function : addSingleRules
	 * @in : row, col
	 * Purpose : process rules
	 */
	
	private void addSingleRules(int row, int col) {
		//Maintain 2 lists, one for rules visited and one for heads
		List<Rules> rulesVisitedList = new LinkedList<>();
		Queue<String> headQueue = new LinkedList<>();
		headQueue.addAll(probMatrix.getHeadsForOneRowOrCol(row, col));
				
		while(!headQueue.isEmpty()) {
			String head = headQueue.poll();
			List<String> headList = Arrays.asList(head);
			for(Rules r : getRules(headList)) {
				if(!rulesVisitedList.contains(r)) {
					rulesVisitedList.add(r);
					headQueue.add(r.getHead());
				}				
				//Update probabilities
				Double probabilityofCurrentRule = r.getProbabilty() * probMatrix.getSpecificCellProbability(row, col, head);
				Double probabilityInMatrix = probMatrix.getSpecificCellProbability(row, col, r.getHead());
				if(probabilityofCurrentRule > probabilityInMatrix) {
					probMatrix.setSpecificCellProbability(row, col, r.getHead(), probabilityofCurrentRule);
					backPointerMatrix.setBackPointerSpecificCell(row, col, r.getHead(), null, head, null);
				}
			}
		}
	}

	
	/* Function : getRules
	 * @in : wordList
	 * @out : List containing rules for a particular word list
	 * Purpose : Return list of rules
	 */
	
	private List<Rules> getRules(List<String> wordList) {
		List<Rules> rulesResultList = new ArrayList<>();
		for(Rules r : getRulesOfGrammar()) {
			List<String> ruleSymbolsList = r.getSymbols();
			if(compareList(ruleSymbolsList, wordList)) {
				rulesResultList.add(r);
			}
		}
		return rulesResultList;
	}

	
	/* Function : compareList
	 * @in : wordList, ruleList
	 * @out : a boolean value if lists match
	 * Purpose : Compare List
	 */

	private boolean compareList(List<String>ruleList, List<String>wordList) {
		if(ruleList == null && wordList == null) {
			return true;
		}
		if(ruleList.size() == wordList.size()) {
			for(int i = 0; i < wordList.size(); i++) {
				if(!wordList.get(i).equals(ruleList.get(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	
	/* Function : getProbabilityMatrix
	 * @out : probability matrix
	 * Purpose : Return probability matrix
	 */
	
	public ProbabilityMatrix getProbabilityMatrix() {
		return probMatrix;
	}
	
	
	/* Function : getRulesOfGrammar
	 * @out : rulesOfGrammar
	 * Purpose : Return rules of grammar
	 */
	
	public List<Rules> getRulesOfGrammar() {
		return rulesOfGrammar;
	}

	
	/* Function : setRulesOfGrammar
	 * Purpose : Set rules of grammar
	 */
	
	public void setRulesOfGrammar(List<Rules> rulesOfGrammar) {
		this.rulesOfGrammar = rulesOfGrammar;
	}

	
	/* Function : getWordsOfSentence
	 * @out : wordsOfSentence
	 * Purpose : Get words of sentence
	 */
	
	public String[] getWordsOfSentence() {
		return wordsOfSentence;
	}

	
	/* Function : setWordsOfSentence
	 * Purpose : Set words of sentence
	 */	
	public void setWordsOfSentence(String[] wordsOfSentence) {
		this.wordsOfSentence = wordsOfSentence;
	}

	
	/* Function : init
	 * Purpose : Initialize matrix
	 */
	public void init() {
		this.probMatrix = new ProbabilityMatrix();
		this.backPointerMatrix = new BackwardPathMatrix();
	}	
}
