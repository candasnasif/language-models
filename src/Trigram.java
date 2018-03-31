import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Trigram {
	
	HashMap<String, Double> trigramLaplace = new HashMap<String, Double>();/*Keep probabilites of smoothed trigram*/
	HashMap<String, Double> trigramModel(HashMap<String, Integer> bigram, ArrayList<String> sentences, int vocablarySize) throws IOException {
	
		HashMap<String, Integer> trigram = new HashMap<String, Integer>();
		/*
		 * I traverse into train datas' sentences and create groups of three and calculate their frequencies.
		 * I put "-" between words.Because I will split from "-" when I use these words.
		 * I call CalculateProbability method for calculate probabilities of groups of three.
		 */
		for (int i = 0; i < sentences.size(); i++) {

			String items[] = sentences.get(i).split("[^A-Za-z0-9\\!\\?\\.\\'\\,]");
			ArrayList<String> trigramSentence = new ArrayList<String>();
			for (int j = 0; j < items.length; j++) {
				if (!(items[j].equals(""))) {
					trigramSentence.add(items[j]);
				}
			}
			if(trigramSentence.size()>=2) {
			
			Integer x = trigram.get("<s>-" + trigramSentence.get(0).toLowerCase() + "-"+trigramSentence.get(1).toLowerCase());
			x = (x == null) ? 1 : ++x;
		
			trigram.put("<s>-" + trigramSentence.get(0).toLowerCase() +"-"+ trigramSentence.get(1).toLowerCase(), x);
			}
			for (int j = 0; j < trigramSentence.size()-1; j++) {
				 

				 if (j == trigramSentence.size() - 2 ) {
					Integer b = trigram.get(trigramSentence.get(j).toLowerCase()+"-"+trigramSentence.get(j+1).toLowerCase() + "-</s>");
					b = (b == null) ? 1 : ++b;
					trigram.put(trigramSentence.get(j).toLowerCase()+"-"+trigramSentence.get(j+1).toLowerCase() + "-</s>", b);
					
				} else {
					Integer c = trigram
							.get(trigramSentence.get(j).toLowerCase() + "-" + trigramSentence.get(j + 1).toLowerCase()+ "-" + trigramSentence.get(j + 2).toLowerCase());
					c = (c == null) ? 1 : ++c;
					trigram.put(trigramSentence.get(j).toLowerCase() + "-" + trigramSentence.get(j + 1).toLowerCase()+ "-" + trigramSentence.get(j + 2).toLowerCase(), c);
				}

			}

			
		}

		return CalculateProbability(bigram, trigram, vocablarySize);
	}
	/*I calculate probability of groups of three and return them into map*/
	HashMap<String, Double> CalculateProbability(HashMap<String, Integer> bigram,
			HashMap<String, Integer> trigram, int vocablarySize) throws IOException {
		HashMap<String, Double> trigramModel = new HashMap<String, Double>();
	    String bi = "";
		for (String str : trigram.keySet()) {
			String token[] = str.split("[-\r\n]+");
				bi =token[0].toLowerCase()+"-"+token[1].toLowerCase();
			/*I divide frequency of group of three into frequency of group of two at the begining*/
			trigramModel.put(str, Double.valueOf(trigram.get(str))/Double.valueOf(bigram.get(bi)));
			/*I divide frequency of group of three plus one into frequency of group of two at the begining 
			 * plus unique groups of two count for smoothed trigram model*/
			trigramLaplace.put(str, (Double.valueOf(trigram.get(str))+1)/(Double.valueOf(bigram.get(bi))+Double.valueOf(vocablarySize)));
			
		}
		return trigramModel;
	}
}

