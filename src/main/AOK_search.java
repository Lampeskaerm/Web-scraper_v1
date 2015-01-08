package main;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class AOK_search {
	int pageHeader = 1;
	Document doc;
	String url = "http://politiken.dk/ibyen/restauranter/?side=1&sortering=senest-anmeldt";
	public Queue<String> queue = new LinkedList<String>();
	OutputStreamWriter writer;
	
	public static void main (String[] args) throws IOException{
		AOK_search search = new AOK_search();
		search.storeLinks();
	}
	
	public void storeLinks() throws IOException{
		FileOutputStream fileStream = new FileOutputStream("restaurants.txt");
		writer = new OutputStreamWriter(fileStream, "UTF-8");
		doc = Jsoup.connect(url).timeout(0).get();
		
		Elements titleElements = doc.select("h2");
		for(int i = 0; i < titleElements.size(); i++){
			Element elem = titleElements.get(i);
			if(elem.hasClass("entitytitle")){
				Element childElem = elem.child(0);
				queue.add(childElem.attr("abs:href"));
			}
		}
		goToNextPage();
		writer.close();
		fileStream.close();
	}
	
	public void goToNextPage() throws IOException{
		if(pageHeader < 68){
			int replacement = pageHeader+1;
			url = url.replace("" + pageHeader, "" + replacement);
			pageHeader++;
			storeLinks();
		} else {
			getPoll();
		}
	}
	
	public void getPoll() throws IOException{
		if(queue.size() > 0){
			url = queue.poll();
			getInfo();
		}
	}
	
	public void getInfo() throws IOException{
		doc = Jsoup.connect(url).timeout(0).get();
		
		Element elem = doc.select("div.entitypage").first(); //finding title container
		elem = elem.select("h1").first(); //getting title
		String title = elem.html();
		elem = doc.select("address").first(); //getting address
		String[] array = elem.html().split("<br>");//getting needed part of address
		Elements elems = doc.select("div.infobox"); //getting all elements that might contain website
		String website = "NULL";
		
		for(int i = 0; i < elems.size(); i++){
			Element urlElement = elems.get(i);
			if(urlElement.select("header").html().equals("E-mail og hjemmeside")){
				Elements href = urlElement.select("a[href]");
				for(int j = 0; j < href.size(); j++){
					Element link = href.get(j);
					if(!link.attr("abs:href").startsWith("mailto:")){
						website = link.attr("abs:href");
					}
				}
			}
		}
		String string = title + " ¤ " + array[0] + " ¤ " + array[1] + " ¤ " + website;
		writeToFile(string);
	}
	
	public void writeToFile(String string) throws IOException{
		writer.write(string + "\n");
		getPoll();
	}
}
