import java.util.HashMap;

public class Unigram {
	HashMap<String,Double> unigramLaplace = new HashMap<String,Double>();
	HashMap<String,Double> UnigramModel(HashMap<String,Integer> words,int totalWordCount,int sentenceCount){
		
		HashMap<String,Double> unigram = new HashMap<String,Double>();
		
		for (String keys : words.keySet()) {/*traverse all unique words for calculate probailities*/
			
			/*unigram keep probabilites of words.I divide current word frequency into total unique words count*/ 
			unigram.put(keys.toLowerCase(), Double.valueOf(words.get(keys.toLowerCase()))/(Double.valueOf(totalWordCount)+Double.valueOf(2*sentenceCount)));
			/*unigramLaplace map keep smoothed probabilites.I add one to current words' frequency and divide it into two times unique words count */
			unigramLaplace.put(keys.toLowerCase(), (Double.valueOf(words.get(keys.toLowerCase()))+1)/(Double.valueOf(totalWordCount)+Double.valueOf(2*sentenceCount)+words.size()));
			
		}
		
		return unigram;
	}

}
