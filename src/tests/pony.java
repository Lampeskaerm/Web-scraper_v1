package tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import main.AOK_search;

import org.junit.Before;
import org.junit.Test;
//import main.AOK_search;


public class pony {
	public AOK_search search;
	@Before
	public void init(){
		 search = new AOK_search();
	}
	
	@Test
	public void testStoreLinks() throws IOException{
		search.storeLinks();
		//assertEquals(search.queue.size(), 15);
		System.out.println(search.queue.size());
	}
}
