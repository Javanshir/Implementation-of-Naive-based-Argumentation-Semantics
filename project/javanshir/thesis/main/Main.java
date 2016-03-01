package javanshir.thesis.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	public static ArrayList<String> A = new ArrayList<String>();
	public static ArrayList<ArrayList<String>> R = new ArrayList<ArrayList<String>>();

	public static int count = 0;
	public static long startTime;
	public static boolean test = false;
	public static boolean parallel = false;
	public static int numberOfProcessors = 4;

	public static int secim = 3;
	public static Object lock = new Object();
	public static boolean show_results = true;
	public static boolean error = false;
	public static boolean labellings = true;

	public static void chooseLabelling(String str) {
		switch (str.toLowerCase()) {
		case "naive":
			secim = 0;
			break;
		case "cf2":
			secim = 1;
			break;
		case "stage":
			secim = 2;
			break;
		case "stage2":
			secim = 3;
			break;
		default:
			secim = 0;
			break;
		}
	}

	public static void showError(){
        log2("Wrong input. Possible variations:");
        log2("1. labelling_name file show_results");
        log2("2. labelling_name file parallel show_results");
        log2("3. labelling_name file parallel number_of_processors show_results");
        
        log2("");
        log2("labelling_name: naive/stage/cf2/stage2");
        log2("file: full_file_path+file_name");
        log2("parallel: serial/parallel");
        log2("number_of_processors: int_value");
        log2("show_results: labellings/extensions/hide");
        log2("Defaults: serial/4/show");
    }

	@SuppressWarnings("unchecked")
	public static void main(String args[]) {
		// long startTime = System.nanoTime();

		System.out.println("START");
		startTime = System.nanoTime();
		// TODO reading from a file
		//String folderName = "/Users/javanshir/Dropbox/thesis/test files/";
		//String fileName = "gmainp";
		//String fileExtension = "";
		// String path = folderName + fileName + fileExtension;

		// TESTING
		String path = null;
		//path = folderName + fileName + fileExtension;
		switch (args.length) {
		case 0:
			break;
		case 3:
			chooseLabelling(args[0]);
			path = args[1];
			if (args[2].equals("hide")) {
				show_results = false;
			} else if (args[2].equals("extensions")) {
				labellings = false;
			}
			parallel = false;
			break;
		case 4:
			chooseLabelling(args[0]);
			path = args[1];
			if (args[2].equals("parallel")) {
				parallel = true;
				numberOfProcessors = 4;
			}
			if (args[3].equals("hide")) {
				show_results = false;
			} else if (args[3].equals("extensions")) {
				labellings = false;
			}
			break;
		case 5:
			chooseLabelling(args[0]);
			path = args[1];
			if (args[2].equals("parallel")) {
				parallel = true;
			}
			numberOfProcessors = Integer.parseInt(args[3]);
			if (args[4].equals("hide")) {
				show_results = false;
			} else if (args[4].equals("extensions")) {
				labellings = false;
			}
			break;
		default:
			showError();
			System.exit(1);
			// System.out.println("Show error, ask again");
			break;
		}

		// TESTING

		Scanner input = null;
		try {
			input = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (input.hasNext()) {
			String s = input.next();
			if (s.equals("exit"))
				break;

			if (s.startsWith("arg")) {
				String arg = s.substring(4, s.lastIndexOf(")"));
				A.add(arg);
			} else if (s.startsWith("att")) {
				ArrayList<String> temp = new ArrayList<String>();
				String att1 = s.substring(4, s.lastIndexOf(","));
				String att2 = s.substring(s.lastIndexOf(",") + 1,
						s.lastIndexOf(")"));
				temp.add(att1);
				temp.add(att2);
				R.add(temp);
			}
		}

		ArrayList<String> Lin = new ArrayList<String>();
		ArrayList<String> Lout = new ArrayList<String>();
		ArrayList<String> Lout_delta = new ArrayList<String>();
		ArrayList<String> Lundecided = new ArrayList<String>();
		ArrayList<ArrayList<String>> naive = new ArrayList<ArrayList<String>>();

		Lundecided = (ArrayList<String>) A.clone();

		switch (secim) {
		case 0:
			findNaive(A, R);
			break;
		case 1:
			if (labellings) {
				System.out.println("cf2 labellings:");
			} else {
				System.out.println("cf2 extensions:");
			}
			cf2(A, R, Lin, Lout, Lout_delta, Lundecided, naive);
			break;
		case 2:
			findStage(A, R);
			break;
		case 3:
            if (labellings) {
                System.out.println("cf2 labellings:");
            } else {
                System.out.println("cf2 extensions:");
            }
			stage2(A, R, Lin, Lout, Lout_delta, Lundecided, naive);
			break;
		default:
			cf2(A, R, Lin, Lout, Lout_delta, Lundecided, naive);
			break;
		}

		log2("\nNumber of solutions: " + count);
		long endTime = System.nanoTime();
		double computationTime = (double) ((endTime - startTime) / 1000000000.0);
		System.out.println(computationTime + " seconds");

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void findNaive(ArrayList<String> A,
			ArrayList<ArrayList<String>> R) {
		ArrayList<ArrayList<String>> naive = new ArrayList<ArrayList<String>>();
		if (!parallel) {
			naive = NaiveExtensions.Naive(A, R);
		} else {
			// ExecutorService executor = Executors.newCachedThreadPool();
			ExecutorService executor = Executors
					.newFixedThreadPool(numberOfProcessors);
			// NaiveExtensionsParallel.Naive(C, newR, executor);

			NaiveExtensionsParallel nvp = new NaiveExtensionsParallel(A, R,
					executor);
			try {
				synchronized (lock) {
					Main.lock.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			naive = new ArrayList<>(NaiveExtensionsParallel.result);
		}

		if (show_results) {
			if (labellings) {
				log2("Naive labellings: ");

				ArrayList<ArrayList<String>> naive_out = new ArrayList<ArrayList<String>>();
				List<ArrayList<String>> naive_undecided = new ArrayList<ArrayList<String>>();

				for (int i = 0; i < naive.size(); i++) {

					ArrayList<String> temp = new ArrayList<String>();
					for (int j = 0; j < naive.get(i).size(); j++) {

						for (int k = 0; k < R.size(); k++) {
							if (R.get(k).get(0).equals(naive.get(i).get(j))) {
								temp.add(R.get(k).get(1));
							}
						}
					}

					naive_out.add(new ArrayList<>(temp));
				}

				for (int i = 0; i < naive_out.size(); i++) {
					HashSet hs = new HashSet();
					hs.addAll(naive_out.get(i));
					naive_out.get(i).clear();
					naive_out.get(i).addAll(hs);
				}

				for (int i = 0; i < naive.size(); i++) {
					naive_undecided.add(new ArrayList<>(A));
				}

				for (int i = 0; i < naive.size(); i++) {
					for (int j = 0; j < naive.get(i).size(); j++) {
						naive_undecided.get(i).remove(naive.get(i).get(j));
					}
				}

				for (int i = 0; i < naive_out.size(); i++) {
					for (int j = 0; j < naive_out.get(i).size(); j++) {
						naive_undecided.get(i).remove(naive_out.get(i).get(j));
					}
				}

				for (int i = 0; i < naive.size(); i++) {
					log2("{ " + naive.get(i) + ", " + naive_out.get(i) + ", "
							+ naive_undecided.get(i) + " };");
				}

			} else {
				log2("Naive extensions: ");
				for (int i = 0; i < naive.size(); i++) {
					log2(naive.get(i));
				}
			}
		}
		count = naive.size();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void findStage(ArrayList<String> A,
			ArrayList<ArrayList<String>> R) {
		ArrayList<String> newA = new ArrayList<String>();
		ArrayList<ArrayList<String>> newR = new ArrayList<ArrayList<String>>();

		newA = (ArrayList<String>) A.clone();
		newR = (ArrayList<ArrayList<String>>) R.clone();

		ArrayList<ArrayList<String>> naive = new ArrayList<ArrayList<String>>();

		if (!parallel) {
			naive = NaiveExtensions.Naive(newA, newR);
		} else {
			ExecutorService executor = Executors
					.newFixedThreadPool(numberOfProcessors);

			NaiveExtensionsParallel nvp = new NaiveExtensionsParallel(newA,
					newR, executor);
			try {
				synchronized (lock) {
					Main.lock.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			naive = new ArrayList<>(NaiveExtensionsParallel.result);
		}

		ArrayList<ArrayList<String>> stage = new ArrayList<ArrayList<String>>();
		stage = StageExtensions.findStage(A, R, naive);

		// log2("================");
		

		if (show_results) {
			if (labellings) {
				log2("Stage labellings: ");
				
				ArrayList<ArrayList<String>> stage_out = new ArrayList<ArrayList<String>>();
				List<ArrayList<String>> stage_undecided = new ArrayList<ArrayList<String>>();

				for (int i = 0; i < stage.size(); i++) {

					ArrayList<String> temp = new ArrayList<String>();
					for (int j = 0; j < stage.get(i).size(); j++) {

						for (int k = 0; k < R.size(); k++) {
							if (R.get(k).get(0).equals(stage.get(i).get(j))) {
								temp.add(R.get(k).get(1));
							}
						}
					}

					stage_out.add(new ArrayList<>(temp));
				}

				for (int i = 0; i < stage_out.size(); i++) {
					HashSet hs = new HashSet();
					hs.addAll(stage_out.get(i));
					stage_out.get(i).clear();
					stage_out.get(i).addAll(hs);
				}

				for (int i = 0; i < stage.size(); i++) {
					stage_undecided.add(new ArrayList<>(A));
				}

				for (int i = 0; i < stage.size(); i++) {
					for (int j = 0; j < stage.get(i).size(); j++) {
						stage_undecided.get(i).remove(stage.get(i).get(j));
					}
				}

				for (int i = 0; i < stage_out.size(); i++) {
					for (int j = 0; j < stage_out.get(i).size(); j++) {
						stage_undecided.get(i).remove(stage_out.get(i).get(j));
					}
				}

				for (int i = 0; i < stage.size(); i++) {
					log2("{ " + stage.get(i) + ", " + stage_out.get(i) + ", "
							+ stage_undecided.get(i) + " };");
				}
			} else {
				log2("Stage extensions: ");
				for (int i = 0; i < stage.size(); i++) {
					log2(stage.get(i));
				}
			}
		}
		count = stage.size();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void cf2(ArrayList<String> A, ArrayList<ArrayList<String>> R,
			ArrayList<String> Lin, ArrayList<String> Lout,
			ArrayList<String> Lout_delta, ArrayList<String> Lundecided,
			ArrayList<ArrayList<String>> naive) {
		ArrayList<ArrayList<String>> newNaive = new ArrayList<ArrayList<String>>();
		// lines 1-6
		Lines1_6.labeling(A, R, Lin, Lout, Lout_delta, Lundecided);
		log("after lines 1-6");

		// removing duplicates
		HashSet hs = new HashSet();
		hs.addAll(Lout_delta);
		Lout_delta.clear();
		Lout_delta.addAll(hs);
		CompareLists.sortList(Lout_delta);

		log("L in: " + Lin);
		log("L out: " + Lout);
		log("L out delta: " + Lout_delta);
		log("L undecided: " + Lundecided);
		log("ending lines 1-6");

		// line 7 - finding B
		ArrayList<String> B = new ArrayList<String>();
		B = Util.findB(R, Lin, Lundecided);
		log("B: " + B);
		// end of line 7

		// line 8-9, finding C
		ArrayList<String> C = new ArrayList<String>();
		if (B.size() != 0) {
			if (B.size() == 1) {
				C.add(B.get(0));
			} else {
				// getting new R: R-L_out_delta edges

				C = Util.findC(A, R, Lout_delta, B);
			}
			// log2("C: " + C);
			// end of line 8-9

			// line 11
			ArrayList<String> newA = new ArrayList<String>();
			ArrayList<ArrayList<String>> newR = new ArrayList<ArrayList<String>>();
			newA = (ArrayList<String>) A.clone();
			newR = (ArrayList<ArrayList<String>>) R.clone();

			ArrayList<String> tempA = new ArrayList<String>();
			tempA = (ArrayList<String>) A.clone();
			for (int k = 0; k < C.size(); k++) {
				tempA.remove(C.get(k));
			}

			// log("newA: " + A);
			// log("newR: " + R);
			for (int k = 0; k < tempA.size(); k++) {
				GenericReturn temp = Util.remove2(tempA.get(k), newA, newR);
				newA = temp.returnA();
				newR = temp.returnR();
			}
			log("tempA: " + tempA);
			log("newA: " + newA);
			log("newR: " + newR);

			if (C.size() == 1) {
				naive.removeAll(naive);
				naive.clear();
				naive.add(C);
			} else {
				if (test) {
					long endTime = System.nanoTime();
					double computationTime = (double) ((endTime - startTime) / 1000000000.0);
					System.out.println(computationTime + " seconds");
				}

				naive.clear();
				if (!parallel) {
					naive = NaiveExtensions.Naive(C, newR);
				} else {
					ExecutorService executor = Executors
							.newFixedThreadPool(numberOfProcessors);

					NaiveExtensionsParallel nvp = new NaiveExtensionsParallel(
							C, newR, executor);
					try {
						synchronized (lock) {
							Main.lock.wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					naive = new ArrayList<>(NaiveExtensionsParallel.result);
				}

				if (test) {
					test = false;
					long endTime = System.nanoTime();
					double computationTime = (double) ((endTime - startTime) / 1000000000.0);
					System.out.println(computationTime
							+ " seconds :::: naive size: " + naive.size());

				}
			}

			// log2("naive extensions: " + naive);

			// for all naive do {
			ArrayList<String> newLin = new ArrayList<String>();
			ArrayList<String> newLout = new ArrayList<String>();
			ArrayList<String> newLout_delta = new ArrayList<String>();
			ArrayList<String> newLundecided = new ArrayList<String>();

			for (int i = 0; i < naive.size(); i++) {
				log("Size of naive: " + naive.size());
				log("naive extensions second: " + naive);
				newLin = (ArrayList<String>) Lin.clone();
				newLout = (ArrayList<String>) Lout.clone();
				newLout_delta = (ArrayList<String>) Lout_delta.clone();
				newLundecided = (ArrayList<String>) Lundecided.clone();
				newNaive = (ArrayList<ArrayList<String>>) naive.clone();

				// line 12

				for (int j = 0; j < naive.get(i).size(); j++) {
					log("Size of naive.get(" + i + "): " + naive.get(i).size());
					log("Used naive in for: " + naive.get(i));
					if (!newLin.contains(naive.get(i).get(j))) {
						newLin.add(naive.get(i).get(j));
						newLundecided.remove(naive.get(i).get(j));
					}

					for (int k = 0; k < newR.size(); k++) {
						if (newR.get(k).get(0).equals(naive.get(i).get(j))) {
							if (!newLout.contains(newR.get(k).get(1))) {
								newLout.add(newR.get(k).get(1));
								newLundecided.remove(newR.get(k).get(1));
							}
						}
					}

				}

				// calling recursively cf2
				cf2(A, R, newLin, newLout, newLout_delta, newLundecided,
						newNaive);
			}

		} else {

			// log2("=======================");
			// log2(count + "::::::L in: " + Lin);
			// log2("L out: " + Lout);
			// log2("L out delta: " + Lout_delta);
			// log2("L undecided: " + Lundecided);
			// log2("=======================");
			if (show_results) {
				if (labellings) {
					log2("{ " + Lin + ", " + Lout + ", " + Lout_delta + ", "
							+ Lundecided + " };");
				} else {
					log2(Lin);
				}
			}
			count++;
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void stage2(ArrayList<String> A,
			ArrayList<ArrayList<String>> R, ArrayList<String> Lin,
			ArrayList<String> Lout, ArrayList<String> Lout_delta,
			ArrayList<String> Lundecided, ArrayList<ArrayList<String>> stage) {

		ArrayList<ArrayList<String>> newNaive = new ArrayList<ArrayList<String>>();
		// lines 1-6
		Lines1_6.labeling(A, R, Lin, Lout, Lout_delta, Lundecided);
		log("after lines 1-6");

		// removing duplicates
		HashSet hs = new HashSet();
		hs.addAll(Lout_delta);
		Lout_delta.clear();
		Lout_delta.addAll(hs);
		CompareLists.sortList(Lout_delta);

		log("L in: " + Lin);
		log("L out: " + Lout);
		log("L out delta: " + Lout_delta);
		log("L undecided: " + Lundecided);
		log("ending lines 1-6");

		// line 7 - finding B
		ArrayList<String> B = new ArrayList<String>();
		B = Util.findB(R, Lin, Lundecided);
		log("B: " + B);
		// end of line 7

		// line 8-9, finding C
		ArrayList<String> C = new ArrayList<String>();
		if (B.size() != 0) {
			if (B.size() == 1) {
				C.add(B.get(0));
			} else {
				// getting new R: R-L_out_delta edges

				C = Util.findC(A, R, Lout_delta, B);
			}
			log("C: " + C);
			// end of line 8-9

			// line 10 - finding D
			ArrayList<String> D = new ArrayList<String>();
			D = Util.findD(A, R, Lout_delta, Lundecided, C);
			log("D: " + D);

			// line 12
			ArrayList<String> newA = new ArrayList<String>();
			ArrayList<ArrayList<String>> newR = new ArrayList<ArrayList<String>>();
			newA = (ArrayList<String>) A.clone();
			newR = (ArrayList<ArrayList<String>>) R.clone();

			ArrayList<String> tempA = new ArrayList<String>();
			tempA = (ArrayList<String>) A.clone();
			for (int k = 0; k < D.size(); k++) {
				tempA.remove(D.get(k));
			}

			log("newA: " + newA);
			log("newR: " + newR);
			for (int k = 0; k < tempA.size(); k++) {
				GenericReturn temp = Util.remove2(tempA.get(k), newA, newR);
				newA = temp.returnA();
				newR = temp.returnR();
			}
			log("tempA: " + tempA);
			log("newA: " + newA);
			log("newR: " + newR);

			ArrayList<ArrayList<String>> naive = new ArrayList<ArrayList<String>>();
			if (D.size() == 1) {
				naive.removeAll(naive);
				naive.clear();
				naive.add(D);
			} else {
				if (test) {
					long endTime = System.nanoTime();
					double computationTime = (double) ((endTime - startTime) / 1000000000.0);
					System.out.println(computationTime + " seconds");
				}

				naive.clear();

				if (!parallel) {
					naive = NaiveExtensions.Naive(D, new ArrayList<>(newR));
				} else {
					ExecutorService executor = Executors
							.newFixedThreadPool(numberOfProcessors);

					NaiveExtensionsParallel nvp = new NaiveExtensionsParallel(
							D, new ArrayList<>(newR), executor);
					try {
						synchronized (lock) {
							Main.lock.wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					naive = new ArrayList<>(NaiveExtensionsParallel.result);
				}

				if (test) {
					test = false;
					long endTime = System.nanoTime();
					double computationTime = (double) ((endTime - startTime) / 1000000000.0);
					System.out.println(computationTime
							+ " seconds :::: naive size: " + naive.size());

				}
			}
			log("naive extensions: " + naive);
			stage = StageExtensions.findStage(newA, newR, naive);
			log("stage extensions: " + stage);

			// for all naive do {
			ArrayList<String> newLin = new ArrayList<String>();
			ArrayList<String> newLout = new ArrayList<String>();
			ArrayList<String> newLout_delta = new ArrayList<String>();
			ArrayList<String> newLundecided = new ArrayList<String>();

			for (int i = 0; i < stage.size(); i++) {
				log("Size of stage: " + stage.size());
				log("stage extensions second: " + stage);
				newLin = (ArrayList<String>) Lin.clone();
				newLout = (ArrayList<String>) Lout.clone();
				newLout_delta = (ArrayList<String>) Lout_delta.clone();
				newLundecided = (ArrayList<String>) Lundecided.clone();
				newNaive = (ArrayList<ArrayList<String>>) stage.clone();

				// line 12

				for (int j = 0; j < stage.get(i).size(); j++) {
					log("Size of stage.get(" + i + "): " + stage.get(i).size());
					log("Used stage in for: " + stage.get(i));
					if (!newLin.contains(stage.get(i).get(j))) {
						newLin.add(stage.get(i).get(j));
						newLundecided.remove(stage.get(i).get(j));
					}

					for (int k = 0; k < newR.size(); k++) {
						if (newR.get(k).get(0).equals(stage.get(i).get(j))) {
							if (!newLout.contains(newR.get(k).get(1))) {
								newLout.add(newR.get(k).get(1));
								newLundecided.remove(newR.get(k).get(1));
							}
						}
					}

				}

				// calling recursively cf2
				log("------------------");
				log("L in: " + newLin);
				log("L out: " + newLout);
				log("L out delta: " + newLout_delta);
				log("L undecided: " + newLundecided);
				stage2(A, R, newLin, newLout, newLout_delta, newLundecided,
						newNaive);
			}

		} else {

			// log2("=======================");
			// log2("L in: " + Lin);
			// log2("L out: " + Lout);
			// log2("L out delta: " + Lout_delta);
			// log2("L undecided: " + Lundecided);
			// log2("=======================");
			if (show_results) {
				if(labellings){
					log2("{ " + Lin + ", " + Lout + ", " + Lout_delta + ", "
							+ Lundecided + " };");
				}else{
					log2(Lin);
				}
			}
			count++;
		}
	}

	public static void log(Object object) {
		// System.out.println(object);
	}

	public static void log2(Object object) {
		System.out.println(object);
	}

}
