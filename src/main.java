import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) throws IOException  {
		ReadFile file = new ReadFile(args[0]); 
		HashMap<String, Integer> words = new HashMap<String, Integer>(); /*Keep all unique words as a key and their frequencies*/
		HashMap<String, Double> unigram = new HashMap<String, Double>();/*Keep all unique unigram words as a key and their probabilities*/
		HashMap<String, Double> bigram = new HashMap<String, Double>();/*Keep all unique bigram words as a key and their probabilities*/
		HashMap<String, Double> trigram = new HashMap<String, Double>();/*Keep all unique trigram words as a key and their probabilities*/
		FileWriter fileWriter = new FileWriter(args[1]);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		MailGenerate generator = new MailGenerate();
		Unigram unigramModel = new Unigram();
		Bigram bigramModel = new Bigram();
		Trigram trigramModel = new Trigram();
		Perplexity perplexity = new Perplexity();
		Double unigramProbability;
		Double bigramProbability;
		Double trigramProbability;
		HashMap<String,Double> perplexityOfBigram = new HashMap<String,Double>();/*Keep each sentences'es perplexity in test data with bigram model*/
		HashMap<String,Double> perplexityOfTrigram = new HashMap<String,Double>();/*Keep each sentences'es perplexity in test data with trigram model*/
		/*file.LoadDataSet will take the all unique words and their frequencies.*/
		words = file.LoadDataSet();
		/*Take the unigram/bigram/trigram models with their probabilities*/
		unigram = unigramModel.UnigramModel(words, file.WordCount, file.trainSetSize);
		bigram = bigramModel.BigramModel(words, file.sentences);
		trigram = trigramModel.trigramModel(bigramModel.bigram, file.sentences, bigram.size());
		/*Calculate each sentences perplexity for task 2 (for bigram and trigram).*/
		perplexityOfBigram = perplexity.PerplexityOfBigram(file.testSentences, bigramModel.bigramLaplace, words);
		perplexityOfTrigram = perplexity.PerplexityOfTrigram(file.testSentences, trigramModel.trigramLaplace, bigramModel.bigramLaplace);
		printWriter.print("--------------------------------------\n"
						+ "|	TASK 3 : GENERATING SENTENCES	|\n"
						+ "--------------------------------------\n");
		printWriter.println("---------------------------------------------------< Generated Sentences With Unsmoothed Unigram >---------------------------------------------------");
		printWriter.println();
		/*I generate ten sentences for three models(smoothed and unsmoothed) then wrote the file with their probabilities*/
		for (int i = 0; i < 10; i++) {
			unigramProbability = generator.UnigramGenerate(unigram,  printWriter, "<s>", 0);
			printWriter.println(" |------>Probability of sentence: "+unigramProbability);
			printWriter.println();
		}
		printWriter.println();
		printWriter.println("---------------------------------------------------< Generated Sentences With Smoothed Unigram >---------------------------------------------------");
		printWriter.println();
		for (int i = 0; i < 10; i++) {
			unigramProbability = generator.UnigramGenerate(unigramModel.unigramLaplace,  printWriter,
					"<s>", 0);
			printWriter.println(" |------>Probability of sentence: "+unigramProbability);
			printWriter.println();
			
		}
		printWriter.println();
		printWriter.println("---------------------------------------------------< Generated Sentences With Unsmoothed Bigram >---------------------------------------------------");
		printWriter.println();
		for (int i = 0; i < 10; i++) {
			bigramProbability = generator.BigramGenerate(bigram, printWriter, "<s>", 0);
			printWriter.println(" |------>Probability of sentence: "+bigramProbability);
			printWriter.println();
		}
		printWriter.println();
		printWriter.println("---------------------------------------------------< Generated Sentences With Smoothed Bigram >---------------------------------------------------");
		printWriter.println();
		for (int i = 0; i < 10; i++) {
			bigramProbability = generator.BigramGenerate(bigramModel.bigramLaplace, printWriter, "<s>", 0);
			printWriter.println(" |------>Probability of sentence: "+bigramProbability);
			printWriter.println();
		}
		printWriter.println();
		printWriter.println("---------------------------------------------------< Generated Sentences With Unsmoothed Trigram >---------------------------------------------------");
		printWriter.println();
		for (int i = 0; i < 10; i++) {
			trigramProbability = generator.TrigramGenerate(trigram, bigram, printWriter, "<s>", 0);
			printWriter.println(" |------>Probability of sentence: "+trigramProbability);
			printWriter.println();
		}
		printWriter.println();
		printWriter.println("---------------------------------------------------< Generated Sentences With Smoothed Trigram >---------------------------------------------------");
		printWriter.println();
		for (int i = 0; i < 10; i++) {
			trigramProbability = generator.TrigramGenerate(trigramModel.trigramLaplace, bigramModel.bigramLaplace,
					printWriter, "<s>", 0);
			printWriter.println(" |------>Probability of sentence: "+trigramProbability);
			printWriter.println();
		}
		printWriter.println();
		printWriter.print("------------------------------------------\n"
						+ "|	TASK 4 : EVALUATION - PERPLEXITY	|\n"
						+ "------------------------------------------\n");
		printWriter.println("---------------------------------------------------< Perplexity Of Test Data Sentences With Bigram >---------------------------------------------------");
		printWriter.println();
		/* I wrote the file test data's perplexity for each sentences (for task 4)*/
		for (String string : perplexityOfBigram.keySet()) {
			printWriter.println(string+" |------>"+perplexityOfBigram.get(string));
			printWriter.println();
		}
		printWriter.println();
		printWriter.println("---------------------------------------------------< Perplexity Of Test Data Sentences With Trigram >---------------------------------------------------");
		printWriter.println();
		for (String string : perplexityOfTrigram.keySet()) {
			printWriter.println(string+" |------>"+perplexityOfTrigram.get(string));
			printWriter.println();
		}
		printWriter.println();
		printWriter.print("------------------------------------------------------\n"
						+ "|	TASK 2 : SMOOTHED TRIGRAM MODEL ON TEST DATA	|\n"
						+ "------------------------------------------------------\n");
		printWriter.println("---------------------------------------------------< Probabilities of Each Sentences With Smoothed Trigram Model >---------------------------------------------------");
		printWriter.println();
		/*Finally i used smoothed trigram model on test data and calculated probabilities of each sentences then wrote into result file*/
		for (String string : perplexity.probabilityOfSentence.keySet()) {
			printWriter.println(string+" |------>"+perplexity.probabilityOfSentence.get(string));
			printWriter.println();
		}
		printWriter.close();
		
	}

}
