# Implementation-of-Naive-based-Argumentation-Semantics
The project was done for my Master's thesis work at the University of Dresden.
The "test files" folder contains all the test instances we used during the evaluation process throughout the thesis.
The AFs used to compare the <i>stage</i> implementations in Section 3.3.1 and 3.3.2 are also given (with the names AF3arg - AF9arg, where the numbers correspond to the number of arguments in AF).

<b>File name and it's meaning:</b>

    MV-25-50-0-010.dl

The first number corresponds to the number of arguments inside the AF (here 25). 
Second (50) defines the group, 25 for first, 50 for the second and 75 for the third group of AFs.
If the third number is <i>0</i>, then all the arguments are connected to form SCCs. 
In case of 1, it ensures that the cardinality of the biggest SCCs is smaller than the number of arguments inside the AF.
The last number is just an identifier for the file.

<b>How to (command line):</b>

Open the "main" folder in command line and run:
  
    javac *.java


This will create .class files on the same folder. Then, open the "project" folder in command line and run one of the following commands:
  
    a) java javanshir.thesis.main.Main labelling_name file show_results
    b) java javanshir.thesis.main.Main labelling_name file parallel_serial show_results
    c) java javanshir.thesis.main.Main labelling_name file parallel_serial number_of_processors show_results
  
where

    labelling_name = naive/cf2/stage/stage2
    file = full file path + file name + extension
    parallel_serial = parallel/serial
    show_results = labellings/extensions/hide
    number_of_processors = integer value
  
  
  
  The defaults are the following:
  
    for a) serial
    for b) number_of_processors = 4
  
