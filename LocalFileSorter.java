

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.*;




/**
 * This class is the core of this exercise. You have to write you code in here. Look at the docs for the two methods
 * for details on the exact task.
 */
public final class LocalFileSorter {
	
	public static void main(String[] args) throws IOException {
		sortFile("unsorted_1MB.txt","mine.txt",100000);
	}
	

    /**
     * This is the core sorting function for a local file. You should write a method that takes a file and sorts it
     * under a certain memory constraint. The `chunkSizeInBytes` determines how many bytes fit into memory.
     *
     * @param inputFileName Name of the input file to be sorted.
     * @param outputFileName Name of the file that should contain the sorted output.
     * @param chunkSizeInBytes Determines how many bytes fit into memory. Usually, this would be close to the size of
     *                         the actual memory, but for this exercise we can limit to less for simplicity.
     *
     * @throws IOException You do not have to deal with exceptions of file handling. If some of the file operations
     *                     fail, just let them escalate. In the tests, we will not require file error handling.
     */
    public static void sortFile(final String inputFileName, final String outputFileName,
            final long chunkSizeInBytes) throws IOException {
        // TODO: You code here
    	List<File> get_chunks=chunkFile(inputFileName,chunkSizeInBytes);
    	List<File> sorted_chunks=new ArrayList<>();
    	
    	
    	
    	List<String> lineList = new ArrayList<String>();
    	for(int i=0;i<get_chunks.size();i++) {
    		lineList.clear();
    	FileReader fileReader = new FileReader(get_chunks.get(i));
    	BufferedReader bufferedReader = new BufferedReader(fileReader);
    	String inputLine="";
    			while ((inputLine = bufferedReader.readLine()) != null) {
    		lineList.add(inputLine);
    		//System.out.println("the line list of file  is : "+lineList.get(0));
    	}
    			
    			
    	fileReader.close();
    	bufferedReader.close();

    	Collections.sort(lineList);
    	
    	
    	sorted_chunks.add(get_chunks.get(i));
    	
    	if(!lineList.isEmpty())
    	{
    		
    	//FileWriter fileWriter = new FileWriter(get_chunks.get(i));
    		
    		FileWriter fileWriter = new FileWriter(get_chunks.get(i));
    	BufferedWriter bw=new BufferedWriter(fileWriter);
    	for(String s:lineList)
    	{
    		bw.write(s);
    		bw.newLine();
    	}
    	bw.close();
    	
    	}
    	//if null check end here
    	}//for loop end here
    	
    
    	K_Merge(sorted_chunks,chunkSizeInBytes,outputFileName);
    	}
    

    

    /**
     * This method should split a given file into smaller file chunks with a certain memory limit. We need this method
     * to transfer file chunks between nodes and this can also be used for local sorting, depending on the
     * implementation strategy that you chose. This method should not sort any data, but simply split it into chunks.
     * We recommend looking at the `Files.createTempFile()` method to create a temporary file. There are many other
     * options in Java how to do this and we recommend using `/tmp` as the base directory for this if you create the
     * files manually.
     *
     * @param fileName Name of the file that should be chunked.
     * @param chunkSizeInBytes Determines how many bytes fit into memory. This is the maximum size that a chunk file
     *                         should have. If a new record does not fit into a chunk, create a new chunk. Do not split
     *                         records. Usually, this would be close to the size of the actual memory, but for this
     *                         exercise we can limit to less for simplicity.
     *
     * @return A list of the temporary chunk files.
     *
     * @throws IOException You do not have to deal with exceptions of file handling. If some of the file operations
     *                     fail, just let them escalate. In the tests, we will not require file error handling.
     */
    public static List<File> chunkFile(final String fileName, final long chunkSizeInBytes) throws IOException {
    	
		File fin=new File(fileName);
		FileReader is=new FileReader(fin);
		BufferedReader bis=new BufferedReader(is);
		int len=(int) fin.length();
		
		len=(int) (len/chunkSizeInBytes);//Number of files that will be formed
		
		String line="";
		List<File> file=new ArrayList<>();
		int total=0;//total length of read lines which are to be written on output files
		for(int i=0;i<len;i++)
		{
			String Chunkname=String.format("%d_%s", i,fin.getName());
			File out=new File(Chunkname);
			FileWriter fr=new FileWriter(out);
			BufferedWriter bw=new BufferedWriter(fr);
			if(i>0) {
				bw.write(line);
				bw.newLine();
			}
			while((line=bis.readLine())!=null && (total=total+line.length())<=chunkSizeInBytes)
			{
				bw.write(line);
				bw.newLine();
				
			}//while loop end
			
			file.add(out);
			bw.close();
			if(i!=len-1) {
				total=0;
			}
			
			
		}// for loop end
		
		
  bis.close();		
		return file;

}
    
    
    public static void K_Merge(List<File> lst,final long chunkSizeInBytes,String Outputfilename) throws IOException {
    	List<String> line=new ArrayList<>();
    	List<BufferedReader> pointer=new ArrayList<>();
    	String s="";
    	for(int i=0;i<lst.size();i++)
    	{
    		FileReader fr=new FileReader(lst.get(i));
    		BufferedReader p=new BufferedReader (fr);
    		pointer.add(p);
    	}
    	for(BufferedReader p:pointer) {
    		line.add(p.readLine());
    	}
    	
    	int check=0;
    	FileWriter fw=new FileWriter(Outputfilename);
    	BufferedWriter bw=new BufferedWriter(fw);
    	while(!(check==line.size()))
    	{
    		//line.set(0,null);
    		
    		
    		String smallest="";
    		int index=0;
    		for(String u:line) {
    			if(u!=null) {
    				smallest=u;
    				index=line.indexOf(u);
    			}
    		}
    		for(String p:line) {
    			if(p==null) {
    				check=check+1;
    			}
    			
    		}
    		if(check==10) {break;}
    		else {check=0;}
    		
    		
    		
    		
    	
    	for(String l:line) {
    		if(l!=null)
    		{
    		if(smallest.compareTo(l)>0) {
    			index=line.indexOf(l);
    			smallest=l;		
    		}
    		}
    		else{}// if not equal to null
    	}
    	
    	bw.write(smallest);//write smallest to output file
    	bw.newLine();
    	//line.remove(index);//remove that index
    	if((s=(pointer.get(index)).readLine())!=null)
    	{	
    	line.set(index,s);
    	}
    	else {
    		line.set(index,null);
    	}
    	//System.out.println(line);
    	}// line is empty meaning all is written to output file
    	bw.close();
    	for(int i=0;i<pointer.size();i++)
    	{
    		pointer.get(i).close();
    	}
    	
    	
    	
    }// k merge end here
    }//class end here
    


    	
    	
    

    

