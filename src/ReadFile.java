import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadFile {
	String FileName;
	int WordCount = 0;/*Total word count of train data set*/

	int totalSentenceCount = 0;/*Total sentence count of all file*/
	int trainSetSize = 0;/*Total sentence count of train data set*/
	ArrayList<String> sentences = new ArrayList<String>();/*Keep each sentences in the train data set*/
	ArrayList<String> testSentences = new ArrayList<String>();/*Keep each sentences in the train test set*/
	ArrayList<String> wordsTest = new ArrayList<String>();/*Keep all words in test set*/

	public ReadFile(String fileName) {
		super();
		FileName = fileName;
	}
	/*This method read the file and separate two parts.
	 * First part is the train data and second part is the test data.
	 * We calculate frequencies each word in data set then call the CalculateFrequencies method
	 * for unigram model.CalculateFrequencies method return all words with their frequencies.
	 */
	HashMap<String, Integer> LoadDataSet() throws IOException {
		ArrayList<String> wordsTrain = new ArrayList<String>();/*Keep all words in train data set*/
		FileInputStream fstream = new FileInputStream(this.FileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		

		String strLine;

		/* Read File Line By Line
		 * Calculate total sentence count in file
		 */
		while ((strLine = br.readLine()) != null) {
			
			totalSentenceCount++;

		}
		br.close();
		/* We take %60 of file for train data set*/
		trainSetSize = (totalSentenceCount * 6) / 10;
		FileInputStream fstream1 = new FileInputStream(this.FileName);
		BufferedReader br1 = new BufferedReader(new InputStreamReader(fstream1));	
		int j = 0;
		while ((strLine = br1.readLine()) != null) {
			/*I split the word if there is not alphanumeric, ?,!,', \, ,. */
			String parts[] = strLine.split("[^A-Za-z0-9\\!\\?\\.\\'\\,]");
			if (j < trainSetSize) {
				sentences.add(strLine);
				
				for (int i = 0; i < parts.length; i++) {
					if (!parts[i].equals("")) {
						wordsTrain.add(parts[i].toLowerCase());

					}

				}
			} else {
				testSentences.add(strLine);
				for (int i = 0; i < parts.length; i++) {
					if (!parts[i].equals("")) {
						wordsTest.add(parts[i].toLowerCase());
						
					}

				}
			}

			j++;
		}
		br1.close();
		WordCount = wordsTrain.size();
		return CalculateFrequencies(wordsTrain);
	}
	/*This method calculate the frequencies of words and return them with a map*/
	HashMap<String, Integer> CalculateFrequencies(ArrayList<String> wordsTrain) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (String w : wordsTrain) {/*traverse in words of train data*/
			Integer n = map.get(w.toLowerCase());/* get the frequency of word*/
			n = (n == null) ? 1 : ++n; /* If n equals the null then assign 1 its frequency otherwise add 1 to its frequency*/
			map.put(w, n);/*put into map new frequency and word*/
		}
		map.put("<s>", trainSetSize);/*add <s> number of sentences for train data*/
		map.put("</s>", trainSetSize);/*add </s> number of sentences for train data*/
		return map;
	}

}
