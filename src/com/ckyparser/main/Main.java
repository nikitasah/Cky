/* Main class */
package com.ckyparser.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ckyparser.helper.Rules;

/* Custom Exception Handler in case numbers of arguments is less than 2 */
class InsufficientArgsException extends Exception {
	private static final long serialVersionUID = 1L;
	public InsufficientArgsException(String s) 
	{ 
		// Call constructor of parent Exception 
		super(s); 
	} 
}


public class Main {

	public static void main(String[] args) {
		final long startTime = System.currentTimeMillis();
		try {
			if(args.length < 2) {
				throw new InsufficientArgsException("Insufficient arguments");
			}
			else {
				final String[] wordsOfSentence = args[1].split(" ");
				List<Rules> rules = null;
				rules = parseGrammar(args[0]);	

				// Initialize CKY parser with rules and words of sentence.
				Cky cky = new Cky();
				cky.init();
				cky.setRulesOfGrammar(rules);
				cky.setWordsOfSentence(wordsOfSentence);
				
				String resultTree = cky.generateParseTree();
				
				//Printing the result
				double resultProbability = cky.getProbabilityMatrix().getSpecificCellProbability(0, wordsOfSentence.length, "s");
				if(resultProbability == 0.0) {
					System.out.println("The sentence is not recognized by the grammar");
				}
				else {
					System.out.println("Parse tree for Sentence : " + args[1]);
					System.out.println(resultTree);
					System.out.println("Sentence probability is " + resultProbability);
				}
				System.out.println("The total execution time " + (System.currentTimeMillis() - startTime));
			}
		}catch(InsufficientArgsException e) {
			e.printStackTrace();
		}
	}

	/* Function : parseGrammer 
	 * @in : fileName - Grammar file
	 * @out : List containing grammar rules
	 * Purpose : Parses the grammar file
	 */
	private static List<Rules> parseGrammar(String fileName) {
		List<Rules> rulesList = new ArrayList<>();
		Pattern pattern = Pattern.compile("(.*\\[.+\\])");
		try {
			Scanner scanner = new Scanner(new File(fileName));
			while(scanner.hasNext()) {
				String lineInFile = scanner.nextLine();
				Matcher matcher = pattern.matcher(lineInFile);
				if(matcher.find()) {
					String match = matcher.group().trim();
					String text = match.replaceAll("[\\[^\\]']", "");
					text = text.replace("->", "").replaceAll("\\s+", " ");
					String[] splitString = text.split(" ");
					Rules rule = new Rules();
					rule.setHead(splitString[0].toLowerCase());
					rule.setProbabilty(Double.parseDouble(splitString[splitString.length - 1]));
					List<String> symbols = new ArrayList<>();
					for(int i = 0; i < splitString.length - 2; i++) {
						symbols.add(splitString[i+1].toLowerCase());
					}
					rule.setSymbols(symbols);
					rulesList.add(rule);
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rulesList;
	}
}
