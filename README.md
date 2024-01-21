# SAT-solver

This repository contains the implementations for a sat solver using the CDCL procedure, generating 
a model if the problem is unsatisfiable or a proof if the problem is unsatisfiable. Since the problem
dimensions can be very big, the proof generation will be switched off when it goes over 500 lines becuase
it will be not very easy for a human making a check for all those lines.

## Input
The sat solver can handle both problems in cnf form or in propositional logic. The input must be
saved into a file with .cnf extension if it will contain a cnf form problem or with .txt extension if
the problem is in propositional logic. The file must be inserted inside the Input folder where you can
find already some test files. 

The sat solver will recognize if a problem is in cnf form, starting the resolution 
over it immediately, or if it is in propositional logic, making the translation into cnf form, saving 
the result into a cnf file in the input folder and then starting the resolution over it. The
translation from propositional logic to cnf form use the Tsenin Encoding rules, in order to get
an equisatisfiable cnf problem.

Inside the input folder there are already some files. The pigeonhole problem from the case with 1 hole and
2 pigeons till the case with 7 holes and 8 pigeons. Some files in propositional logic and some files from
https://www.cs.ubc.ca/~hoos/SATLIB/benchm.html website. In particular the files starting with *'uu'* are 
unsatisfiable problems, the ones starting with *'u'* contains satisfiable problems.

### Writing file in proposition logic
So if it is found a .txt file into the input folder then the solver will procede with te traduction into
ECNF. This transduction is then saved into a .cnf file inside the input folder, so is important in the execution 
with command *all* (see input prompt section) to delete previous generated .cnf files, otherwise the solver will
be executed twice, on the file .txt and on the correspondent .cnf file.

To write correctly a propositional formula into the .txt file is important to use the following syntax:
  - Variables can start with an uppercase or lowercase letter, but not with numbers which can be used from the second symbol. The variable 2Person is not valid, the variable P3rson2 is valid.
  - NOT: is the symbol for the negation
  - AND: is the symbol for the conjunction
  - OR: is the symbol for the disjunction
  - ->: is the symbol for the implication
  - <->: is the symbol that represent if and only if statement

All the rules of the propositional logic must be followed, like the priority between symbols and their meaning. 
To change priority inside the formula is possible to use *(* and *)* parenthesis. The formula can be written on more
lines. Some examples can be found already in the *Input* folder and are named as *testPropLogicFile*.

## Compiling and starting the sat solver 
Before starting the execution, you will have to compile the files. To make things easier it has been 
created some shell and batch script, depending on your OS, that you can use to compile and execute the sat solver.
In any case, in order to make things works is important that from the terminal you are into the SAT-solver
folder, not in any other folder. Then from here you can launch the scripts. 

### From Windows:
 
- .\compileWindows.bat -> the compiled file will be saved into the *outCompiled* folder
- .\execWindows.bat -> will execute the sat solver

### From Linux:
- chmod +x compileLinux.sh: for giving the permissions to the compiling script
- chmod +x execLinux.sh: for giving the permissions to the executing script
- ./compileLinux.sh: the compiled file will be saved into the *outCompiled* folder
- ./execLinux.sh: will start the execution of the sat solver

## INPUT PROMPT
The sat solver uses a stdin/stdout interface to make the user interact with it. All the commands
will be also desplayed at the starting of the software, anyway a short description is given also here:

- **help**: will output the commands with a short explanation about their usage
- **clear**: to clear the terminal
- **quit**: for turning off the sat solver
- **file *'fileName.txt/cnf'***: will start the resolution over the only indicated file. Important is that this
  file must be inside the *Input* folder and that only the file name must be given in input, not the all path
  because the sat solver will automatically search inside the Input folder.
- **all**: for making the execution over the all file inside the input folder. At the end it will be show how many
       satisfiable and unsatisfiable problems have been found. Then to have a short output about the result for
       each file, when the solver will ask you, insert _y_ and then press enter, otherwise press _n_. If you want
       to see the model or the proof computed from the sat solver, you have to open the Output folder
       in which are saved for each input file the correspondent output containing the model or the proof.

The output of the execution will be saved into a .txt file inside the Output folder.

## Output
The output of the execution over any file into the Input folder will be saved into a correspondent .txt
file inside the Output folder. This folder must not be deleted, despite the contained files. 

The output can be SAT with a model of literals and the correspondent truth value or UNSAT with a proof
that leads to the empty clause [], that means that has been reached a contradiction. The proof will 
not be desplayed as a tree because it would not be readable even after a few steps. The proof consists 
in a sequence of lines in which each one contains a proof step. The proof follows the binary resolution process,
so the parents of a proof steps will be searched between the problem clauses, or the child of the other resolutions
into the proof. This means that even if the tree form is not displayed, it is implicit. 