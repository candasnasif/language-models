import java.util.ArrayList;
import java.util.HashMap;

public class Perplexity {
	HashMap<String,Double> probabilityOfSentence = new HashMap<String,Double>();/*This arraylist keeps probability of each sentences' 
	 																			probability for task 2 */
	public HashMap<String,Double> PerplexityOfBigram(ArrayList<String> testSet, HashMap<String, Double> bigram, HashMap<String, Integer> words) {
		HashMap<String,Double> perplexity =new HashMap<String,Double>();
		Double result = Double.valueOf(0);
		Double probability = Double.valueOf(0);
		String word = "";
		/*testSet keeps all sentences of test data*/
		for (int i = 0; i < testSet.size(); i++) {
			result = Double.valueOf(0);
			ArrayList<String> wordsOfSentence = new ArrayList<String>();
			String parts[] = testSet.get(i).split("[^A-Za-z0-9\\!\\?\\.\\'\\,]");
			wordsOfSentence.add("<s>");/* I add <s> beginning of sentence*/
			for (int j = 0; j < parts.length; j++) {
				if(!parts[j].equals(""))
					wordsOfSentence.add(parts[j]);
			}
			wordsOfSentence.add("</s>");/* I add </s> end of sentence*/
			for (int j = 0; j < wordsOfSentence.size()-1; j++) {
				probability = Double.valueOf(0);
				/*I create group of two for get probability*/
				word = wordsOfSentence.get(j).toLowerCase()+"-"+wordsOfSentence.get(j+1).toLowerCase();
				probability = bigram.get(word);
				/*If there is not a probability for word I apply the smoothed laplace for this group of two*/
				if( probability == null) {
					if(words.get(wordsOfSentence.get(j).toLowerCase()) == null) {
						/*If there is not a first word in our unique word set then I divide one into unique word count for probability*/
						probability = Double.valueOf(1)/Double.valueOf(words.size());
						
					}
					else
					/*Otherwise I divide one into frequency of first word plus unique word count for probabilty */
					probability = Double.valueOf(1)/Double.valueOf(words.get(wordsOfSentence.get(j).toLowerCase()) + words.size());
				}
				/*I take the sum of probabilities of all group of two*/
				result = result + Math.log(probability);
				
			}
			/*I will apply the formula of perplexity with logaritma*/
			result = result * (Double.valueOf(-1)/Double.valueOf(wordsOfSentence.size()));
			result = Math.pow(2, result);
			perplexity.put(testSet.get(i), result);
			
		}
		
		return perplexity;
	}

	public HashMap<String,Double> PerplexityOfTrigram(ArrayList<String> testSet, HashMap<String, Double> trigram, HashMap<String, Double> bigram) {
		HashMap<String,Double> perplexity =new HashMap<String,Double>();
		
		Double result = Double.valueOf(0);
		Double probability = Double.valueOf(0);
		String word = "";
		for (int i = 0; i < testSet.size(); i++) {
			result = Double.valueOf(0);
			ArrayList<String> wordsOfSentence = new ArrayList<String>();
			String parts[] = testSet.get(i).split("[^A-Za-z0-9\\!\\?\\.\\'\\,]");
			wordsOfSentence.add("<s>");/* I add <s> beginning of sentence*/
			for (int j = 0; j < parts.length; j++) {
				if(!parts[j].equals(""))
					wordsOfSentence.add(parts[j]);
			}
			wordsOfSentence.add("</s>");/* I add </s> end of sentence*/
			for (int j = 0; j < wordsOfSentence.size()-2; j++) {
				probability = Double.valueOf(0);
				/*I create group of three for get probability*/
				word = wordsOfSentence.get(j).toLowerCase()+"-"+wordsOfSentence.get(j+1).toLowerCase()+"-"+wordsOfSentence.get(j+2).toLowerCase();
				probability = trigram.get(word);
				/*If there is not a probability for word I apply the smoothed laplace for this group of three*/
				if( probability == null) {
					/*If there is not a first group of two in our bigram model set then I divide one into bigram model size for probability*/
					if(bigram.get(wordsOfSentence.get(j).toLowerCase()+"-"+wordsOfSentence.get(j+1).toLowerCase()) == null){
						probability = Double.valueOf(1)/Double.valueOf((bigram.size()));
					}
					else
						/*Otherwise I divide one into frequency of first group of two plus bigram model size for probabilty */
					probability = Double.valueOf(1)/Double.valueOf(Double.valueOf(bigram.get(wordsOfSentence.get(j).toLowerCase()+"-"+wordsOfSentence.get(j+1).toLowerCase()))+Double.valueOf(bigram.size()));
				}
				
				result = result + Math.log(probability);
			}
			/*I put the probability in probabilityOfSentence for task2*/
			probabilityOfSentence.put(testSet.get(i), result);
			/*I will apply the formula of perplexity with logaritma*/
			result = result * (Double.valueOf(-1)/Double.valueOf(wordsOfSentence.size()));
			result = Math.pow(2, result);
			perplexity.put(testSet.get(i), result);
			
		}
		
		
		return perplexity;
	}


}
