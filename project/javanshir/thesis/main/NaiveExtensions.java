package javanshir.thesis.main;

import java.util.ArrayList;
import java.util.HashSet;

public class NaiveExtensions {

	public static ArrayList<String> r = new ArrayList<String>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList<ArrayList<String>> Naive(ArrayList<String> A,
			ArrayList<ArrayList<String>> R) {

		ArrayList<String> forEach = new ArrayList<String>();
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

		ArrayList<String> S = new ArrayList<String>();
		ArrayList<String> Sj = new ArrayList<String>();
		ArrayList<ArrayList<String>> Q = new ArrayList<ArrayList<String>>();

		CompareLists.sortList(A);
		log("A: " + A);
		log("R: " + R);

		
		// TODO remove if cf2
		// /*
		ArrayList<String> selfAttackers = new ArrayList<String>();
		for (int i = 0; i < R.size(); i++) {
			if (R.get(i).get(0).equals(R.get(i).get(1))) {
				selfAttackers.add(R.get(i).get(0));
			}
		}

		if (selfAttackers.size() > 0) {
			for (int i = 0; i < selfAttackers.size(); i++) {
				A.remove(selfAttackers.get(i));
			}

			for (int i = 0; i < R.size(); i++) {
				if (selfAttackers.contains(R.get(i).get(0))
						|| selfAttackers.contains(R.get(i).get(1))) {
					R.remove(i);
					i--;
				}
			}
			log("A: " + A);
			log("R: " + R);
		}
		// */
		// remove if cf2

		findFirst(A, R);
		log("r: " + r);
		
		Q.add(new ArrayList<>(r));
		result.add(new ArrayList<>(r));

		r.clear();

		CompareLists.sort(Q);
		log("Q: " + Q);

		while (Q.size() != 0) {
			log("--------------");

			for (int l = 0; l < Q.size(); l++) {
				CompareLists.sortList(Q.get(l));
			}
			
			log("Q: " + Q);
			S = Q.get(0);
			log("S: " + S);
			Q.remove(0);

			for (int i = 0; i < R.size(); i++) {
				if (S.contains((R.get(i)).get(0))
						&& R.get(i).get(1).compareTo(S.get(0)) > 0) {
					forEach.add(R.get(i).get(1));
				} else if (S.contains((R.get(i)).get(1))
						&& R.get(i).get(0).compareTo(S.get(0)) > 0) {
					forEach.add(R.get(i).get(0));
				}
			}

			HashSet hs = new HashSet();
			hs.addAll(forEach);
			forEach.clear();
			forEach.addAll(hs);
			CompareLists.sortList(forEach);
			log("for each: " + forEach);

			ArrayList<String> oneToJ = new ArrayList<String>();

			CompareLists.sortList(A);

			ArrayList<String> tempSj = new ArrayList<String>();

			for (int j = forEach.size(); j > 0; j--) {
				
				for (int i = 0; i < A.size(); i++) {
					oneToJ.add(A.get(i));
					if (forEach.size() != 0) {
						if (A.get(i).equals(forEach.get(j - 1))) {
							break;
						}
					}
				}
				log("oneToJ: " + oneToJ);

				for (String s : S) {
					if (oneToJ.contains(s))
						Sj.add(s);
				}
				log("Sj: " + Sj);

				tempSj = (ArrayList<String>) Sj.clone();
				for (int i = 0; i < R.size(); i++) {
					if (R.get(i).get(0).equals(forEach.get(j - 1))
							&& Sj.contains(R.get(i).get(1))) {
						tempSj.remove(R.get(i).get(1));
					} else if (R.get(i).get(1).equals(forEach.get(j - 1))
							&& Sj.contains(R.get(i).get(0))) {
						tempSj.remove(R.get(i).get(0));
					}
				}
				tempSj.add(forEach.get(j - 1));
				log("tempSj: " + tempSj);
				ArrayList<String> toAdd = new ArrayList<String>();
				toAdd = (ArrayList<String>) tempSj.clone();

				// finding argument set until the last argument in Sj
				ArrayList<String> newA = new ArrayList<String>();
				CompareLists.sortList(toAdd);
				for (int i = 0; i < A.size(); i++) {
					newA.add(A.get(i));
					if (A.get(i).equals(toAdd.get(toAdd.size() - 1))) {
						break;
					}
				}
				
				if (ifMIS(toAdd, newA, R)) {
					log("true");
					boolean contains = false;
					if (ifMIS(toAdd, A, R)) {
						for (ArrayList<String> h : result) {
							if (h.containsAll(toAdd) && toAdd.containsAll(h)) {
								contains = true;
								log(toAdd + " exists");
							}
						}
						if (!contains) {
							log2(toAdd);
							result.add(toAdd);
							Q.add(toAdd);
						}
					} else {
						find(toAdd, A, R);
						CompareLists.sortList(r);
						for (ArrayList<String> h : result) {
							if (h.containsAll(r) && r.containsAll(h)) {
								contains = true;
								log(r + " exists");
							}
						}
						if (!contains) {
							log2(r);
							result.add(new ArrayList<>(r));
							Q.add(new ArrayList<>(r));
						}
						log("Finalizing: " + r);
						r.clear();
					}

					log("Q" + Q);
				} else {
					// if not MIS
					log("false");
				}
				tempSj.clear();
				Sj.clear();
				oneToJ.clear();
				
			} // main for with --

			// log("final Sj: " + Sj);
			forEach.clear();
			Sj.clear();
			oneToJ.clear();

		}
		CompareLists.sort(result);
		log(result);
		log(result.size());

		return result;
	}

	@SuppressWarnings("unchecked")
	public static void findFirst(ArrayList<String> A,
			ArrayList<ArrayList<String>> R) {

		GenericReturn temp = remove(A.get(0), new ArrayList<>(A),
				new ArrayList<>(R));
		A = temp.returnA();
		R = temp.returnR();

		CompareLists.sortList(A);

		if (A.size() > 1) {
			findFirst(A, R);
		} else if (A.size() == 1) {
			r.add(A.get(0));
		}

	}

	@SuppressWarnings("unchecked")
	public static void find(ArrayList<String> arguments, ArrayList<String> A,
			ArrayList<ArrayList<String>> R) {

		GenericReturn temporary;
		for (String argument : arguments) {

			temporary = remove(argument, new ArrayList<>(A), new ArrayList<>(R));
			A = temporary.returnA();
			R = temporary.returnR();
		}

		if (A.size() > 1) {
			findFirst(A, R);
		} else if (A.size() == 1) {
			r.add(A.get(0));
		}
	}

	public static GenericReturn remove(String arg, ArrayList<String> A,
			ArrayList<ArrayList<String>> R) {
		ArrayList<String> temp = new ArrayList<String>();
		r.add(arg);
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

	@SuppressWarnings("unchecked")
	public static boolean ifMIS(ArrayList<String> t, ArrayList<String> A,
			ArrayList<ArrayList<String>> R) {

		GenericReturn temp;
		for (int i = 0; i < t.size(); i++) {
			temp = remove2(t.get(i), new ArrayList<>(A), new ArrayList<>(R));
			A = temp.returnA();
			R = temp.returnR();
		}

		if (A.size() == 0)
			return true;
		else
			return false;
	}

	public static GenericReturn remove2(String arg, ArrayList<String> A,
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

	private static void log(Object aObject) {
		//System.out.println(String.valueOf(aObject));
	}
	
	private static void log2(Object aObject) {
		//System.out.println(String.valueOf(aObject));
	}

}
