package javanshir.thesis.main;

import java.util.ArrayList;

public final class GenericReturn
{

	public ArrayList<String> A = new ArrayList<String>();
	public ArrayList<ArrayList<String>> R = new ArrayList<ArrayList<String>>();

	@SuppressWarnings("static-access")
	public GenericReturn(ArrayList<String> a, ArrayList<ArrayList<String>> r)
	{
		this.A = a;
		this.R = r;
	}

	public ArrayList<String> returnA()
	{
		return A;
	}

	public ArrayList<ArrayList<String>> returnR()
	{
		return R;
	}

}
