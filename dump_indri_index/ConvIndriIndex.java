import lemurproject.indri.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

public class ConvIndriIndex {

	static String DELIM = "___DELIM___";

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		QueryEnvironment env = new QueryEnvironment();
		
		long N;
		int[] docs = new int[1];
		long df;
		int tf;

		DVFileWriter dvw = new DVFileWriter();
		// Build document vector hash: key -> TF
		HashMap<String, Integer> ds = new HashMap<String, Integer>();
		
		try {
			env.addIndex("/Users/codex/prj/Digging/index_unicode");

			N = env.documentCount();
			
			for(int j = 1; j <= N; j++) {
				docs[0] = j;
				System.out.print("\r" + j + "/" + N + " (" + j * 100 / N + "%)");
				System.out.flush();
				
				// Call Indri for document vector information
				// Equivalent to dumpindex
				DocumentVector[] dv = env.documentVectors(docs);

				String[] docnos = env.documentMetadata(docs, "docno");
				String docno = docnos[0];
				
				ds.clear();
				
				for (int i : dv[0].positions) {
					String stem = dv[0].stems[i];
					
					// ignore OOV
					if(stem.compareTo("[OOV]") == 0) {
						continue;
					}
					
					String key = docno + DELIM + stem;
					if(ds.containsKey(key)) {
						ds.put(key, ds.get(key) + 1);
					}
					else {
						ds.put(key, 1);
					}
				}
				
				// store the dv to db
				for(String key: ds.keySet()) {
					String pid = key.split(DELIM)[0];
					String term = key.split(DELIM)[1];
					df = env.documentCount(term);

					// maybe a bug? indri attempts to return stop word stats...
					if(df == 0) {
						df = 1;
					}

					tf = ds.get(key);

					dvw.writeLine(pid, term, tf, df, tf * Math.log(N/df));
				}

			}
		} catch (Exception e) {
			System.err.println("Indri error");
			System.err.println(e.toString());
		}
		
		dvw.close();
		
	}

}
