import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DVFileWriter {

	public FileWriter fstream;
	public BufferedWriter out;

	public DVFileWriter() {
		try {
			fstream = new FileWriter("dv_out_unicode3.txt");
			out = new BufferedWriter(fstream);
		}
		catch (Exception e) {
			
		}
	}
	
	public void writeLine(String pid, String term, int tf, long df, double tfidf) {
		try {
			out.write(pid + "\t" + term + "\t" + tf + "\t" + df + "\t" + tfidf + "\n");
		}
		catch(Exception e) {
			
		}
	}
	
	
	public void close() throws IOException {
		out.close();
		fstream.close();
	}
}
