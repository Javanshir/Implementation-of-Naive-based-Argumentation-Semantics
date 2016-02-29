package javanshir.thesis.main;

import java.util.ArrayList;

public class PaExe implements Runnable{
	
	NaiveExtensionsParallel naiveExtensions = new NaiveExtensionsParallel();
	NaiveExtensionsParallel nvp = new NaiveExtensionsParallel();
	
	public ArrayList<String> A = new ArrayList<String>();
	public ArrayList<ArrayList<String>> R = new ArrayList<ArrayList<String>>();
	
	ArrayList<String> S = new ArrayList<String>();
	
	public PaExe(ArrayList<String> S, ArrayList<String> A, ArrayList<ArrayList<String>> R){
		this.S = S;
		this.A = A;
		this.R = R;
	}

	@Override
	public void run() {
		naiveExtensions.findQs(new ArrayList<>(S), new ArrayList<>(A), new ArrayList<>(R));
		//nvp.findQs(Ss);
	}
	
	public void getVar(){
		
	}

}
