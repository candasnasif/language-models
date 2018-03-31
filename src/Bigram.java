import java.util.ArrayList;
import java.util.HashMap;


public class Bigram {
	
	HashMap<String, Integer> bigram = new HashMap<String, Integer>();/*keep bigram words with their frequencies*/
	HashMap<String, Double> bigramLaplace = new HashMap<String, Double>();/*keep smoothed bigram words with their frequencies*/
	HashMap<String, Double> BigramModel(HashMap<String, Integer> words, ArrayList<String> sentences) {
		
		/*I traverse into sentences and calculate their frequencies 
		 * then call the CalculateProbability method for calculate their probabilities*/
		for (int i = 0; i < sentences.size(); i++) {
			
			String items[] = sentences.get(i).split("[^A-Za-z0-9\\!\\?\\.\\'\\,]");
			ArrayList<String> bigramSentence = new ArrayList<String>();
			for (int j = 0; j < items.length; j++) {
				if (!(items[j].equals(""))) {
					bigramSentence.add(items[j]);
				}
			}
			/*I create the bigram map following line
			 * I add <s> beginning of word If we are at the beginning of sentence. 
			 * I create bigrams for model in loop
			 * If I am at the end of sentence then i add </s> into last bigram elements' second word
			 * I take all words in the lowercase.Because I do not want to miss any word.*/
			Integer x = bigram.get("<s>-" + bigramSentence.get(0).toLowerCase());
			x = (x == null) ? 1 : ++x;
			bigram.put("<s>-" + bigramSentence.get(0).toLowerCase(), x);
			for (int j = 0; j < bigramSentence.size(); j++) {

				 if (j == bigramSentence.size() - 1 ) {
					Integer b = bigram.get(bigramSentence.get(j).toLowerCase() + "-</s>");
					b = (b == null) ? 1 : ++b;
					bigram.put(bigramSentence.get(j).toLowerCase() + "-</s>", b);
				} else {
					Integer c = bigram
							.get(bigramSentence.get(j).toLowerCase() + "-" + bigramSentence.get(j + 1).toLowerCase());
					c = (c == null) ? 1 : ++c;
					bigram.put(bigramSentence.get(j).toLowerCase() + "-" + bigramSentence.get(j + 1).toLowerCase(), c);
				}

			}

			
		}

		return CalculateProbability(words, bigram);
	}
	/*Calculate probabilities of bigram words group*/
	HashMap<String, Double> CalculateProbability(HashMap<String, Integer> words,
			HashMap<String, Integer> bigram) {
		HashMap<String, Double> bigramModel = new HashMap<String, Double>();
		for (String str : bigram.keySet()) {
			String token[] = str.split("-");/*I split the word from "-".Token will keep first and second word of bigram model argument*/
			/*I divide to frequencies of bigram word group into frequencies of first word when I calculated to probabilites of bigram group*/
			bigramModel.put(str.toLowerCase(), Double.valueOf(bigram.get(str))/Double.valueOf(words.get(token[0])));
			/*I divide to frequencies of bigram word group plus one into frequencies of first word plus unique word count
			 * When I calculated to probabilities of smoothed bigram
			 */
			bigramLaplace.put(str.toLowerCase(),(Double.valueOf(bigram.get(str)) + 1) / (Double.valueOf(words.get(token[0])) + words.size()));

		}
		return bigramModel;
	}
}
