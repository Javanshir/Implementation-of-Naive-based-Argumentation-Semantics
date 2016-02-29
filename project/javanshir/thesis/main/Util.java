package javanshir.thesis.main;

import java.util.ArrayList;
import java.util.HashSet;

public class Util {

	public static boolean isReachable(String start, String end,
			ArrayList<ArrayList<String>> R) {
		ArrayList<String> visited = new ArrayList<String>();
		for (int i = 0; i < R.size(); i++) {

			if (!visited.contains(start))
				visited.add(start);

			if (R.get(i).get(0).equals(start)
					&& !visited.contains(R.get(i).get(1))) {
				String newStart = R.get(i).get(1);
				if (newStart.equals(end)) {
					// log("There is a path");
					return true;
				} else {
					visited.add(newStart);
					if (isReachable(newStart, end, visited, R)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isReachable(String start, String end,
			ArrayList<String> visited, ArrayList<ArrayList<String>> R) {
		for (int i = 0; i < R.size(); i++) {
			if (!visited.contains(start))
				visited.add(start);

			if (R.get(i).get(0).equals(start)
					&& !visited.contains(R.get(i).get(1))) {
				String newStart = R.get(i).get(1);
				if (newStart.equals(end)) {
					// log("There is a path");
					return true;
				} else {
					visited.add(newStart);
					if (isReachable(newStart, end, visited, R))
						return true;

				}
			}
		}
		return false;
	}

	public static GenericReturn remove(String arg, ArrayList<String> A,
			ArrayList<ArrayList<String>> R) {
		ArrayList<String> temp = new ArrayList<String>();
		A.remove(arg);

		for (int i = 0; i < R.size(); i++) {
			if ((R.get(i)).get(0).equals(arg)) {
				temp.add((R.get(i)).get(1));
				A.remove((R.get(i)).get(1));
				R.remove(i);
				i--;
			} else if ((R.get(i)).get(1).equals(arg)) {
				temp.add((R.get(i)).get(0));
				A.remove((R.get(i)).get(0));
				R.remove(i);
				i--;
			}
		}

		for (int i = 0; i < R.size(); i++) {
			if (temp.contains((R.get(i)).get(0))) {
				R.remove(i);
				i--;
			} else if (temp.contains((R.get(i)).get(1))) {
				R.remove(i);
				i--;
			}
		}

		return new GenericReturn(A, R);
	}

	public static GenericReturn remove2(String arg, ArrayList<String> A,
			ArrayList<ArrayList<String>> R) {
		ArrayList<String> temp = new ArrayList<String>();
		A.remove(arg);

		for (int i = 0; i < R.size(); i++) {
			if ((R.get(i)).get(0).equals(arg)) {
				R.remove(i);
				i--;
			} else if ((R.get(i)).get(1).equals(arg)) {
				R.remove(i);
				i--;
			}
		}

		for (int i = 0; i < R.size(); i++) {
			if (temp.contains((R.get(i)).get(0))) {
				R.remove(i);
				i--;
			} else if (temp.contains((R.get(i)).get(1))) {
				R.remove(i);
				i--;
			}
		}

		return new GenericReturn(A, R);
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String> findB(ArrayList<ArrayList<String>> R,
			ArrayList<String> lin, ArrayList<String> lundecided) {

		ArrayList<String> B = new ArrayList<String>();
		ArrayList<String> tempLundecided = new ArrayList<String>();
		tempLundecided = (ArrayList<String>) lundecided.clone();

		log("temp undecided: " + tempLundecided);
		for (int i = 0; i < tempLundecided.size(); i++) {
			boolean isB = true;
			// log(i + tempLundecided.get(i));
			for (int j = 0; j < R.size(); j++) {

				if ((R.get(j)).get(0).equals(tempLundecided.get(i))) {
					if (lin.contains(R.get(j).get(1))
							|| R.get(j).get(0).equals(R.get(j).get(1))) {
						isB = false;
						break;
					}
				} else if ((R.get(j)).get(1).equals(tempLundecided.get(i))) {
					if (lin.contains(R.get(j).get(0))) {
						isB = false;
						break;
					}
				}
			}

			if (isB) {
				B.add(tempLundecided.get(i));
				tempLundecided.remove(i);
				i--;
			}
		}

		return B;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String> findC(ArrayList<String> A,
			ArrayList<ArrayList<String>> R, ArrayList<String> Lout_delta,
			ArrayList<String> B) {
		ArrayList<String> C = new ArrayList<String>();

		ArrayList<String> newA = new ArrayList<String>();
		ArrayList<ArrayList<String>> newR = new ArrayList<ArrayList<String>>();
		newA = (ArrayList<String>) A.clone();
		newR = (ArrayList<ArrayList<String>>) R.clone();

		for (int k = 0; k < Lout_delta.size(); k++) {
			GenericReturn temp = Util.remove2(Lout_delta.get(k), newA, newR);
			newA = temp.returnA();
			newR = temp.returnR();
		}
		// newR is our new R without L_out_delta edges

		for (int i = 0; i < B.size(); i++) {
			boolean isC = true;
			for (int j = 0; j < B.size(); j++) {
				if (Util.isReachable(B.get(j), B.get(i), newR)) {
					if (!Util.isReachable(B.get(i), B.get(j), newR)) {
						isC = false;
						break;
					}
				}
			}
			if (isC) {
				C.add(B.get(i));
			}
		}

		return C;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String> findD(ArrayList<String> A,
			ArrayList<ArrayList<String>> R, ArrayList<String> Lout_delta,
			ArrayList<String> Lundecided, ArrayList<String> C) {

		ArrayList<String> D = new ArrayList<String>();
		ArrayList<String> newA = new ArrayList<String>();
		ArrayList<ArrayList<String>> newR = new ArrayList<ArrayList<String>>();
		newA = (ArrayList<String>) A.clone();
		newR = (ArrayList<ArrayList<String>>) R.clone();

		for (int k = 0; k < Lout_delta.size(); k++) {
			GenericReturn temp = Util.remove2(Lout_delta.get(k), newA, newR);
			newA = temp.returnA();
			newR = temp.returnR();
		}
		// newR is our new R without L_out_delta edges

		// adding all arguments from C
		for (int i = 0; i < C.size(); i++) {
			D.add(C.get(i));
		}

		// adding self attacking arguments
		for (int i = 0; i < Lundecided.size(); i++) {

			for (int j = 0; j < C.size(); j++) {

				if (isReachable(Lundecided.get(i), C.get(j), newR)
						&& isReachable(C.get(j), Lundecided.get(i), newR)) {
					log("argument to add to D: " + Lundecided.get(i));
					D.add(Lundecided.get(i));
					break;
				}

			}

		}

		HashSet hs = new HashSet();
		hs.addAll(D);
		D.clear();
		D.addAll(hs);

		return D;
	}

	public static void log(Object object) {
		// System.out.println(object);
	}

}
