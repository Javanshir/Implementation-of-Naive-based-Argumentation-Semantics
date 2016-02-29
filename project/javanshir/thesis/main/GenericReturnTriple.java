package javanshir.thesis.main;

import java.util.ArrayList;

public class GenericReturnTriple {
	
	public ArrayList<String> A = new ArrayList<String>();
	public ArrayList<ArrayList<String>> R = new ArrayList<ArrayList<String>>();
	public ArrayList<String> r = new ArrayList<String>();

	@SuppressWarnings("static-access")
	public GenericReturnTriple(ArrayList<String> A, ArrayList<ArrayList<String>> R, ArrayList<String> r)
	{
		this.A = A;
		this.R = R;
		this.r = r;
	}

	public ArrayList<String> returnA()
	{
		return A;
	}

	public ArrayList<ArrayList<String>> returnR()
	{
		return R;
	}
	
	public ArrayList<String> returnSmallR()
	{
		return r;
	}

}
