package javanshir.thesis.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lines1_6 {
	public static HashMap<String, List<String>> attackers = new HashMap<String, List<String>>();

	@SuppressWarnings("unchecked")
	public static void labeling(ArrayList<String> A,
			ArrayList<ArrayList<String>> R, ArrayList<String> Lin,
			ArrayList<String> Lout, ArrayList<String> Lout_delta,
			ArrayList<String> Lundecided) {

		// creating @attackers map, which stores attackers for each argument
		for (int i = 0; i < A.size(); i++) {
			attackers.put(A.get(i), new ArrayList<String>());
			for (int j = 0; j < R.size(); j++) {
				if (R.get(j).get(1).equals(A.get(i))) {
					attackers.get(A.get(i)).add(R.get(j).get(0));
				}
			}
		}
		log("attackers: ");
		log(attackers);

		// adding all arguments into @Lundecided
		// Lundecided.addAll(A);

		// doing lines 1-6
		ArrayList<String> X = new ArrayList<String>();
		ArrayList<String> Y = new ArrayList<String>();

		// !(X.isEmpty() && Y.isEmpty())
		boolean cont;
		do {
			cont = false;
			for (int i = 0; i < Lundecided.size(); i++) {
				log("argument: " + Lundecided.get(i));
				// log("====================\n============");
				// log("is reachable? " + Util.isReachable("c", "b", R));
				// adding arguments into X, line 1
				if (attackers.get(Lundecided.get(i)).isEmpty()) {
					X.add(Lundecided.get(i));
					log("first: " + Lundecided.get(i));
				} else if (isAllAttackersOutDelta(
						attackers.get(Lundecided.get(i)), Lout_delta)) {
					X.add(Lundecided.get(i));
					log("second: " + Lundecided.get(i));
				}

				// adding arguments into Y, line 2
				for (int j = 0; j < attackers.get(Lundecided.get(i)).size(); j++) {
					if (Lin.contains(attackers.get(Lundecided.get(i)).get(j))) {
						// log("yoxlama: " + Lundecided.get(i));
						ArrayList<ArrayList<String>> tempR = new ArrayList<ArrayList<String>>();
						tempR = (ArrayList<ArrayList<String>>) R.clone();

						ArrayList<String> newA = new ArrayList<String>();
						ArrayList<ArrayList<String>> newR = new ArrayList<ArrayList<String>>();
						newA = (ArrayList<String>) A.clone();
						newR = (ArrayList<ArrayList<String>>) R.clone();

						for (int k = 0; k < Lout_delta.size(); k++) {
							GenericReturn temp = Util.remove2(
									Lout_delta.get(k), newA, newR);
							newA = temp.returnA();
							newR = temp.returnR();
						}
						// log("new R is reachable");
						log(newR);
						// log("is reachable? " + Util.isReachable("c", "b",
						// newR));

						if (!Util.isReachable(Lundecided.get(i),
								attackers.get(Lundecided.get(i)).get(j), newR)) {
							Y.add(Lundecided.get(i));
						} else {
							log(attackers.get(Lundecided.get(i)).get(j)
									+ Lundecided.get(i));
						}

					}
				}
			}

			// line 5
			//System.out.println("X: " + X);
			//System.out.println("Y: " + Y);
			for (int i = 0; i < X.size(); i++) {
				Lin.add(X.get(i));
				Lundecided.remove(X.get(i));
				cont = true;
			}
			for (int i = 0; i < Y.size(); i++) {
				Lout_delta.add(Y.get(i));
				Lundecided.remove(Y.get(i));
				cont = true;
			}
			// line 6
			ArrayList<String> temp = new ArrayList<String>();
			X = (ArrayList<String>) temp.clone();
			Y = (ArrayList<String>) temp.clone();
			
		} while (cont);
	}

	public static boolean isAllAttackersOutDelta(List<String> atc,
			ArrayList<String> Lout_delta) {
		for (int i = 0; i < atc.size(); i++) {
			if (Lout_delta.contains(atc.get(i))) {
			} else
				return false;
		}
		return true;
	}

	public static void log(Object object) {
		// System.out.println(object);
	}

}
