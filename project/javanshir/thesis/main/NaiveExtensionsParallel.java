package javanshir.thesis.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import javanshir.thesis.main.Main;

public class NaiveExtensionsParallel {

	public static CopyOnWriteArrayList<ArrayList<String>> result = new CopyOnWriteArrayList<ArrayList<String>>();
	public static CopyOnWriteArrayList<ArrayList<String>> Q = new CopyOnWriteArrayList<ArrayList<String>>();
	// public static ArrayList<ArrayList<String>> result = new
	// ArrayList<ArrayList<String>>();

	// public static ArrayList<String> tempS = new ArrayList<String>();
	// public static ArrayList<String> S = new ArrayList<String>();
	// public static ArrayList<ArrayList<String>> Q = new
	// ArrayList<ArrayList<String>>();

	public static ArrayList<String> Amain = new ArrayList<String>();
	public static ArrayList<ArrayList<String>> Rmain = new ArrayList<ArrayList<String>>();

	private static final Object lock = new Object();
	public static ExecutorService executor;
	// public static PaExe paExe;
	private static AtomicInteger say = new AtomicInteger(0);

	// public static int say = 0;

	public NaiveExtensionsParallel() {
	}

	public NaiveExtensionsParallel(ArrayList<String> A,
			ArrayList<ArrayList<String>> R, ExecutorService exe) {
		Amain = new ArrayList<>(A);
		Rmain = new ArrayList<>(R);
		executor = exe;
		Naive(A, R, exe);
	}

	// @SuppressWarnings({ "unchecked", "rawtypes" })
	@SuppressWarnings({ "unchecked" })
	public void Naive(ArrayList<String> A, ArrayList<ArrayList<String>> R,
			ExecutorService exe) {

		// executor = exe;
		result.clear();
		// Amain = (ArrayList<String>) A.clone();
		// Rmain = (ArrayList<ArrayList<String>>) R.clone();
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
		Amain = (ArrayList<String>) A.clone();
		Rmain = (ArrayList<ArrayList<String>>) R.clone();

		ArrayList<String> muvR = new ArrayList<String>();
		ArrayList<String> r = new ArrayList<String>();
		r = findFirst(A, R, muvR);
		log2("r: " + r);
		Q.add(new ArrayList<>(r));
		result.add(new ArrayList<>(r));

		// CompareLists.sort(Q);
		log("Q: " + Q);
		checkQ();
	}

	public static synchronized void checkQ() {

		synchronized (lock) {
			while (Q.size() != 0) {
				log("--------------");

				ArrayList<String> tempS = new ArrayList<String>();

				tempS = new ArrayList<>(Q.get(0));
				log("Executed with: " + tempS);
				Q.remove(0);

				// logTemp(Q.size() + " :Q: " + Q);

				// result.add(tempS);
				PaExe paExe = new PaExe(new ArrayList<>(tempS),
						new ArrayList<>(Amain), new ArrayList<>(Rmain));
				executor.execute(paExe);
				say.incrementAndGet();
				log("Say: " + say);

			}
		}

		if (say.intValue() == 0) {
			executor.shutdown();
			// CompareLists.sort(result);
			log2("result: " + result);
			log2("result size: " + result.size());

			// TODO
			// /*
			Set<ArrayList<String>> s = new LinkedHashSet<>(result);
			log2("SSS size: " + s.size());
			result.clear();
			result.addAll(s);
			// */

			synchronized (Main.lock) {
				Main.lock.notify();
			}
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void findQs(ArrayList<String> S, ArrayList<String> A,
			ArrayList<ArrayList<String>> R) {
		ArrayList<String> forEach = new ArrayList<String>();
		// ArrayList<String> Sj = new ArrayList<String>();

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

		// ArrayList<String> oneToJ = new ArrayList<String>();
		// log("oneToJ: " + Amain + " --- just A, to check");

		CompareLists.sortList(A);

		// ArrayList<String> tempSj = new ArrayList<String>();
		// tempSj = (ArrayList<String>) Sj.clone();

		for (int j = forEach.size(); j > 0; j--) {
			ArrayList<String> Sj = new ArrayList<String>();
			ArrayList<String> tempSj = new ArrayList<String>();
			ArrayList<String> oneToJ = new ArrayList<String>();

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

			if (ifMIS(new ArrayList<>(toAdd), new ArrayList<>(newA), new ArrayList<>(R))) {
				log("true for: " + toAdd);
				boolean contains = false;
				// TODO adding should be synchronized
				// ArrayList<ArrayList<String>> tempResult = new
				// ArrayList<ArrayList<String>>();
				// tempResult = returnTempResult();
				if (ifMIS(new ArrayList<>(toAdd), new ArrayList<>(A), new ArrayList<>(R))) {

					// addResult(new ArrayList<>(toAdd));

					for (ArrayList<String> h : result) {
						if (h.containsAll(toAdd) && toAdd.containsAll(h)) {
							contains = true;
							log(toAdd + " exists");
						}
					}

					if (!contains) {
						log2(toAdd);
						result.add(new ArrayList<>(toAdd));
						Q.add(new ArrayList<>(toAdd));
					}

				} else {
					log(toAdd + " is not of:::");
					ArrayList<String> tempSmallR = new ArrayList<String>();

					ArrayList<String> temp = new ArrayList<String>();
					temp = find(new ArrayList<>(toAdd), A, R, tempSmallR);
					CompareLists.sortList(temp);

					// addResult(new ArrayList<>(temp));

					for (ArrayList<String> h : result) {
						if (h.containsAll(temp) && temp.containsAll(h)) {
							contains = true;
							log(temp + " exists");
						}
					}

					if (!contains) {
						log2(temp);
						result.add(new ArrayList<>(temp));
						Q.add(new ArrayList<>(temp));
					}

					log("Finalizing: " + temp);
					// r.clear();
				}

				// log("Q" + Q);
			} else {
				// if not MIS
				log("false for: " + toAdd);
			}
			// tempSj.clear();
			// Sj.clear();
			oneToJ.clear();
			log("------------------");
		} // main for with --

		// log("final Sj: " + Sj);
		forEach.clear();
		// Sj.clear();
		// oneToJ.clear();

		say.decrementAndGet();
		// logTemp("write:::" + Q.size());
		checkQ();
	}

	public synchronized void addResult(ArrayList<String> toAdd) {
		boolean contains = false;

		// ArrayList<ArrayList<String>> tempResult = new
		// ArrayList<ArrayList<String>>();
		// tempResult = returnTempResult();

		for (ArrayList<String> h : result) {
			if (h.containsAll(toAdd) && toAdd.containsAll(h)) {
				contains = true;
				log(toAdd + " exists");
			}
		}

		if (!contains) {
			log2(toAdd);
			result.add(new ArrayList<>(toAdd));
			Q.add(new ArrayList<>(toAdd));
		}
	}

	public synchronized ArrayList<ArrayList<String>> returnTempResult() {
		// TODO
		// return result;
		return new ArrayList<>(result);
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String> findFirst(ArrayList<String> A,
			ArrayList<ArrayList<String>> R, ArrayList<String> r) {
		ArrayList<String> newA = new ArrayList<String>();
		ArrayList<ArrayList<String>> newR = new ArrayList<ArrayList<String>>();
		ArrayList<String> newSmallR = new ArrayList<String>();

		newA = (ArrayList<String>) A.clone();
		newR = (ArrayList<ArrayList<String>>) R.clone();
		GenericReturnTriple temp = remove(A.get(0), newA, newR, r);
		newA = temp.returnA();
		newR = temp.returnR();
		newSmallR = temp.returnSmallR();

		CompareLists.sortList(newA);

		if (newA.size() > 1) {
			findFirst(newA, newR, newSmallR);
		} else if (newA.size() == 1) {
			newSmallR.add(newA.get(0));
		}

		return r;

	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String> find(ArrayList<String> arguments,
			ArrayList<String> A, ArrayList<ArrayList<String>> R,
			ArrayList<String> smallR) {
		ArrayList<String> newA = new ArrayList<String>();
		ArrayList<ArrayList<String>> newR = new ArrayList<ArrayList<String>>();
		ArrayList<String> newSmallR = new ArrayList<String>();

		newA = (ArrayList<String>) A.clone();
		newR = (ArrayList<ArrayList<String>>) R.clone();
		GenericReturnTriple temporary;
		for (String argument : arguments) {

			temporary = remove(argument, newA, newR, smallR);
			newA = temporary.returnA();
			newR = temporary.returnR();
			newSmallR = temporary.returnSmallR();
		}

		if (newA.size() > 1) {
			findFirst(newA, newR, newSmallR);
		} else if (newA.size() == 1) {
			smallR.add(newA.get(0));
		}

		return smallR;
	}

	public static GenericReturnTriple remove(String arg, ArrayList<String> A,
			ArrayList<ArrayList<String>> R, ArrayList<String> r) {
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

		return new GenericReturnTriple(A, R, r);
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
