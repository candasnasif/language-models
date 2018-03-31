import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Random;

public class MailGenerate {
	
	/*This method generate sentence with unigram model and calculate probability of these sentences
	 * I can genrate smoothed unigram sentence with this method If "unigram" hashmap includes the smoothed unigram probabilities.*/
	public Double UnigramGenerate(HashMap<String, Double> unigram,  PrintWriter printWriter,
			String word, int init) {
		
		Double probability = Double.valueOf(0);/*This value keeps probability of sentence*/
		/*If Generated sentences' number of word less than 30 and next word is not equal the </s>
		 * we print the word into output file
		 */
		if (init < 30 && !word.equals("</s>")) {
			probability = Math.log(unigram.get(word));
			if (init != 0 && !word.equals("<s>")) {

				String output;
				if (init == 1)
					output = word.substring(0, 1).toUpperCase() + word.substring(1);
				else {
					output = word;
				}
				printWriter.print(output + " ");/*Print into file*/
			}
			/*If word contains punction then I must stop the generate*/
			if (word.matches("[\\.\\!\\?]+")) {
				
				return probability;
			}

			Double d = Math.random();/*Generate a double between 0 and 1*/
			Double result = Double.valueOf(0);
			/*
			 * I traverse the unigram map and probabilites of words in each step
			 * then If  random number "d" smaller then sum of probabilites
			 * It show us I am at probability of next word.
			 * My next word will be str at this point.
			 */
			for (String str : unigram.keySet()) {
				result = result + unigram.get(str);

				if (result >= d) {
					
					init++;
					probability += UnigramGenerate(unigram, printWriter, str, init);
					break;
				}
			}

				

			
		} 
		/*If we are exceed the 30 then put "." punction end of the word and add probabilites of it 
		 */
		else {
			probability = Math.log(unigram.get("."));
			printWriter.print(". ");
			
		}
		return probability;

	}
	/*This method generate sentence with bigram model and calculate probability of these sentences
	 * I can generate smoothed bigram sentence with this method If "bigram" hashmap includes the smoothed bigram probabilities.*/
	public Double BigramGenerate(HashMap<String, Double> bigram, PrintWriter printWriter, String word, int init) {
		Double probability = Double.valueOf(0);/*This value keeps probability of sentence*/
		if (init < 30) {
			
			if (init != 0) {
				if (word.equals("</s>")) {
					printWriter.print(".");

					return probability;
				} else {
					String output;
					if (init == 1)
						output = word.substring(0, 1).toUpperCase() + word.substring(1);
					else {
						output = word;
					}
					printWriter.print(output + " ");
					/*If word contains punction then I must stop the generate*/
					if (word.matches("[\\.\\!\\?]+")) {
						return probability;
					}
				}

			}

			Random random = new Random();
			Double d = random.nextDouble();
			Double x = Double.valueOf(0);
			for (String str : bigram.keySet()) {
				String token[] = str.split("-");
				if (token[0].equals(word)) {

					x += bigram.get(str);
				}
			}
			if (d > x) {
				d = x * d;
			}

			Double result = Double.valueOf(0);
			/*
			 * I traverse the bigram map and probabilites of words in each step
			 * then If  random number "d" smaller then sum of probabilites(result)
			 * It show us I am at probability of next word.
			 * My next word will be second word of group of two  at this point.
			 */
			for (String str : bigram.keySet()) {
				String token[] = str.split("-");
				if (token[0].equals(word)) {

					result = result + bigram.get(str);

					if (result >= d) {
						probability = Math.log(bigram.get(str));

						init++;
						probability += BigramGenerate(bigram, printWriter, token[1].toString(), init);
						break;
					}
				}
			}
			/*If we are exceed the 30 then put "." punction end of the word and add probabilites of it 
			 */
		} else {
			probability = Math.log(bigram.get(".-</s>"));
			printWriter.print(". ");

		}
		return probability;

	}
	/*This method generate sentence with trigram model and calculate probability of these sentences
	 * I can generate smoothed trigram sentence with this method If "trigram" hashmap includes the smoothed trigram probabilities.*/
	public Double TrigramGenerate(HashMap<String, Double> trigram, HashMap<String, Double> bigram,
			PrintWriter printWriter, String word, int init) {
		Double probability = Double.valueOf(0);
		if (init < 30) {
			String parts[] = word.split("-");
			String element ="";
			if(init == 0) {
				element = parts[0];
			}
			else
				element = parts[1];
			if (init != 0) {
				if (word.contains("</s>")) {
					printWriter.print(".");

					return probability;
				} else {
					String output="";
					String finalOut[] = word.split("-");
					if (init == 1)
						output = finalOut[1].substring(0, 1).toUpperCase() + finalOut[1].substring(1);
					else {
						output = finalOut[1];
					}
					
					
					printWriter.print(output+ " ");

					if (finalOut[1].matches("[\\.\\!\\?]+")) {
					
						return probability;
					}
				}

			}

			Random random = new Random();
			Double d = random.nextDouble();
			Double x = Double.valueOf(0);
			Double result = Double.valueOf(0);
			/*If I am at first step I have to generate the word with bigram
			 * I will use trigram model when I have two or more words
			 * */
			if (init < 1) {
				
				for (String str : bigram.keySet()) {
					String token[] = str.split("-");
					
					if (token[0].equals(element)) {

						x += bigram.get(str);
					}
				}
			} else {
				for (String str : trigram.keySet()) {
					String token[] = str.split("-");
					if (token[0].equals(parts[0]) && token[0].equals(parts[1])) {

						x += trigram.get(str);
					}
				}
			}
			if (d > x) {
				d = x * d;
			}
			if (init < 1) {
				for (String str : bigram.keySet()) {
					String token[] = str.split("-");
					if (token[0].equals(element)) {

						result = result + bigram.get(str);

						if (result >= d) {
							probability = Math.log(bigram.get(str));
							word = element+"-"+token[1];
							init++;
							probability += TrigramGenerate(trigram, bigram, printWriter,word, init);
							break;
						}
					}
				}
			} else {
				/*
				 * I traverse the trigram map and probabilites of words in each step
				 * then If  random number "d" smaller then sum of probabilites(result)
				 * It show us I am at probability of next word.
				 * My next word will be third word of group of two  at this point.
				 */
				for (String str : trigram.keySet()) {
					String token[] = str.split("-");
						if (token[0].equals(parts[0]) && token[1].equals(parts[1])){

						result = result + trigram.get(str);

						if (result >= d) {
							probability = Math.log(trigram.get(str));
							word = parts[1]+"-"+token[2];
							init++;
							probability += TrigramGenerate(trigram, bigram, printWriter, word, init);
							break;
						}
					}
				}
			}
		} else {
			/*If we are exceed the 30 then put "." punction end of the word 
			 */
			printWriter.print(". ");

		}
		return probability;

	}

}
