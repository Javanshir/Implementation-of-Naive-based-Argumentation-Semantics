package javanshir.thesis.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class StageExtensions {

	public static ArrayList<String> r = new ArrayList<String>();
	public static ArrayList<String> A = new ArrayList<String>();
	public static ArrayList<ArrayList<String>> R = new ArrayList<ArrayList<String>>();
	static int say = 0;

	public static ArrayList<ArrayList<String>> findStage(ArrayList<String> A,
			ArrayList<ArrayList<String>> R, ArrayList<ArrayList<String>> naive) {
		
		ArrayList<ArrayList<String>> stage = new ArrayList<ArrayList<String>>();
		
		log("A: " + A);
		log("R: " + R);
		boolean satisfies[] = new boolean[naive.size()];
		for (int i = 0; i < satisfies.length; i++) {
			satisfies[i] = true;
		}

		// filling all arguments to reverseRange
		List<ArrayList<String>> reverseRange = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < naive.size(); i++) {
			reverseRange.add(new ArrayList<>(A));
		}
		
		log("reverseRange: " + reverseRange);

		// removing naive set arguments from reverseRange
		for (int i = 0; i < naive.size(); i++) {
			for (int j = 0; j < naive.get(i).size(); j++) {
				reverseRange.get(i).remove(naive.get(i).get(j));
			}
		}
		
		log("reverseRange: " + reverseRange);

		for (int i = 0; i < naive.size(); i++) {
			for (int j = 0; j < naive.get(i).size(); j++) {

				for (int k = 0; k < R.size(); k++) {
					if (R.get(k).get(0).equals(naive.get(i).get(j))) {
						reverseRange.get(i).remove(R.get(k).get(1));
					}
				}

			}
		}

		log("reverseRange 2nd: " + reverseRange);

		for (int i = 0; i < reverseRange.size(); i++) {
			if (satisfies[i]) {

				say++;
				for (int j = 0; j < reverseRange.size(); j++) {
					if (satisfies[j]) {

						
						// TODO exchange and test
						if (reverseRange.get(j)
								.containsAll(reverseRange.get(i))
								&& reverseRange.get(j).size() > reverseRange
										.get(i).size()) {
							satisfies[j] = false;
						}

					}
				}

			}
		}
		
		
		for(int i=0; i<reverseRange.size(); i++){
			if(satisfies[i]){
				//log2(reverseRange.get(i));
				stage.add(naive.get(i));
			}
		}
		
		
		log("============");
		log(stage);
		log(stage.size());
		
		log2("say: " + say);
		return stage;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList<ArrayList<String>> Naive(ArrayList<String> A,
			ArrayList<ArrayList<String>> R) {

		HashMap<Integer, String> mapForEach;
		ArrayList<String> forEach = new ArrayList<String>();
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

		ArrayList<String> S = new ArrayList<String>();
		ArrayList<String> Sj = new ArrayList<String>();
		ArrayList<ArrayList<String>> Q = new ArrayList<ArrayList<String>>();

		CompareLists.sortList(A);
		log("A: " + A);
		log("R: " + R);
		
		// remove if cf2
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
			// Q = CompareLists.sort(Q);
			log("Q: " + Q);
			mapForEach = new HashMap<Integer, String>();
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
			log("oneToJ: " + A + " --- just A, to check");

			CompareLists.sortList(A);

			ArrayList<String> tempSj = new ArrayList<String>();
			// tempSj = (ArrayList<String>) Sj.clone();

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
				// TODO
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
							result.add(toAdd);
							Q.add(toAdd);
						}
					} else {
						find(toAdd, A, R);
						ArrayList<String> temp = new ArrayList<String>();
						temp = (ArrayList<String>) r.clone();
						CompareLists.sortList(temp);
						for (ArrayList<String> h : result) {
							if (h.containsAll(temp) && temp.containsAll(h)) {
								contains = true;
								log(temp + " exists");
							}
						}
						if (!contains) {
							result.add(temp);
							Q.add(temp);
						}
						log("Finalizing: " + temp);
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
		ArrayList<String> newA = new ArrayList<String>();
		ArrayList<ArrayList<String>> newR = new ArrayList<ArrayList<String>>();

		newA = (ArrayList<String>) A.clone();
		newR = (ArrayList<ArrayList<String>>) R.clone();
		GenericReturn temp = remove(A.get(0), newA, newR);
		newA = temp.returnA();
		newR = temp.returnR();

		CompareLists.sortList(newA);

		if (newA.size() > 1) {
			findFirst(newA, newR);
		} else if (newA.size() == 1) {
			r.add(newA.get(0));
		}

	}

	@SuppressWarnings("unchecked")
	public static void find(ArrayList<String> arguments, ArrayList<String> A,
			ArrayList<ArrayList<String>> R) {
		ArrayList<String> newA = new ArrayList<String>();
		ArrayList<ArrayList<String>> newR = new ArrayList<ArrayList<String>>();

		newA = (ArrayList<String>) A.clone();
		newR = (ArrayList<ArrayList<String>>) R.clone();
		GenericReturn temporary;
		for (String argument : arguments) {

			temporary = remove(argument, newA, newR);
			newA = temporary.returnA();
			newR = temporary.returnR();
		}

		if (newA.size() > 1) {
			findFirst(newA, newR);
		} else if (newA.size() == 1) {
			r.add(newA.get(0));
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
		ArrayList<String> newA = new ArrayList<String>();
		ArrayList<ArrayList<String>> newR = new ArrayList<ArrayList<String>>();

		newA = (ArrayList<String>) A.clone();
		newR = (ArrayList<ArrayList<String>>) R.clone();

		GenericReturn temp;
		for (int i = 0; i < t.size(); i++) {
			temp = remove2(t.get(i), newA, newR);
			newA = temp.returnA();
			newR = temp.returnR();
		}

		if (newA.size() == 0)
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
