package javanshir.thesis.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompareLists
{
	public static ArrayList<String> r = new ArrayList<String>();
	public static ArrayList<String> rn = new ArrayList<String>();
	public static ArrayList<ArrayList<String>> Q = new ArrayList<ArrayList<String>>();
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		rn.add("a");
		rn.add("d");
		r = (ArrayList<String>) rn.clone();
		Q.add(r);
		rn.clear();
		
		rn.add("c");
		rn.add("f");
		r = (ArrayList<String>) rn.clone();
		Q.add(r);
		rn.clear();
		
		rn.add("e");
		rn.add("b");
		r = (ArrayList<String>) rn.clone();
		Q.add(r);
		rn.clear();
		
		rn.add("a");
		rn.add("c");
		r = (ArrayList<String>) rn.clone();
		Q.add(r);
		rn.clear();
		
		System.out.println(Q);
		
		sort(Q);
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<ArrayList<String>> sort(ArrayList<ArrayList<String>> q){
		ArrayList<ArrayList<String>> finalQ = new ArrayList<ArrayList<String>>();
		ArrayList<String> tempQ = new ArrayList<String>();
		ArrayList<String> tempInerList = new ArrayList<String>();
		ArrayList<String> tempInerList2 = new ArrayList<String>();
		
		for(ArrayList<String> h : q){
			sortList(h);
		}
		
		for(ArrayList<String> al : q){
			String temp = "";
			for(int i=0;i<al.size();i++){
				temp+=al.get(i);
				temp+=".";
			}
			tempQ.add(temp);
		}
		
		sortList(tempQ);
		//log("tempQ from compare lists: " + tempQ + "=== and size of it: " + tempQ.size());
		String[] ar = null;
		for(int i=0;i<tempQ.size();i++){
			ar = tempQ.get(i).split(".");
			//log(ar.length);
			tempInerList.clear();
			for(int j=1; j<ar.length; j++){
				tempInerList.add(ar[j]);
			}
			tempInerList2 = (ArrayList<String>) tempInerList.clone();
			finalQ.add(tempInerList2);
		}
		
		return finalQ;
	}
	
	public static synchronized void sortList(List<String> aItems)
	{
		Collections.sort(aItems, String.CASE_INSENSITIVE_ORDER);
		//Collections.sort(aItems);
	}
	
	public static void log(Object aObject)
	{
		System.out.println(String.valueOf(aObject));
	}

}



