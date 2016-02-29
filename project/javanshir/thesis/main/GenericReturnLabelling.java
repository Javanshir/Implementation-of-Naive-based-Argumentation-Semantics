package javanshir.thesis.main;

import java.util.ArrayList;

public class GenericReturnLabelling {

	private ArrayList<String> Lin = new ArrayList<String>();
	private ArrayList<String> Lout = new ArrayList<String>();
	private ArrayList<String> Lundecided = new ArrayList<String>();

	@SuppressWarnings("static-access")
	public GenericReturnLabelling(ArrayList<String> Lin,
			ArrayList<String> Lout, ArrayList<String> Lundec) {
		this.Lin = Lin;
		this.Lout = Lout;
		this.Lundecided = Lundec;
	}

	public ArrayList<String> returnLin() {
		return Lin;
	}

	public ArrayList<String> returnLout() {
		return Lout;
	}

	public ArrayList<String> returnLundec() {
		return Lundecided;
	}

}
