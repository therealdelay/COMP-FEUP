# PROJECT TITLE: Yal2jvm Compiler

## Members

|Name               | Number    |
| ------------- |:-------------:|
|Danny Soares       | 201505509 |
|João Pinheiro      | 201104913 |
|Leonardo Teixeira  | 201502848 |
|Vitor Magalhães    | 201503447 |

### SUMMARY 
The tool is a compiler for the Yal Programming Language.
By receiving a .yal file, the tool parses the file, outputting any lexical, syntactic or semantic error.
Afterwards, the tool generates the necessary code to compile it in the Jasmin Assembler, which outputs a .class file.
 
### BUILD AND COMPILE OUR TOOL

!Important Note: It's necessary to have a folder in the project root with io.class and jasmin.jar files inside.

To build the tool and have it set up to use, run the script build.sh by using the command "sh build.sh". This will generate all the class files needed to use the tool and a .jar file.



All generated files go to bin/ directory inside yal package.

### COMPILE .yal FILES

All generated files (.j and .class) go to bin/generatedFiles directory.

#### CREATE .j FILES

To create the jasmin files we have 2 ways:
 - directly with .jar file:
    - java -jar bin/yal2jvm.jar <path_to_.yal_file> 
    (e.g. java -jar bin/yal2jvm.jar yalExamples/array1.yal).
 - with a script using .jar file
    - sh yal2jasmin.sh <path_to_.yal_file>
    (e.g. sh yal2jasmin_jar.sh yalExamples/array1.yal).

#### CREATE .class FILES

To generate the .class files from jasmin files:
- with a script using jasmin.jar file
    - sh jasmin2class.sh <path to .j file>
    (e.g. sh jasmin2class.sh bin/generatedFiles/array1.j).

#### ALL TOGETHER

To compile .yal files into .j files and then into .class files all together, we provided a script which usage is:
- sh compile_yal.sh <file_name>
    - (e.g. sh compile_yal.sh array1)
    <file_name>.yal must be inside yalExamples/ directory

If you have any doubt, you can run it without arguments, which will indicate how to run the tool.
 
### DEALING WITH SYNTACTIC ERRORS: 
The system parses a .yal file and checks for syntactic errors, using a DCG that represents the syntaxe of the yal language. If an error is found, the system shows a message with the error and uses a counter to check if there are 10 or more errors. Having 10 or more errors on the .yal file, the system shows the messages for the 10 first errors and exits. To avoid having errors caused by other previous errors, when a syntactic error is found, we ignore tokens until a token related to the part of the code that had errors is found. (e.g. if a “while” expression has an error, we ignore every token until a “{“ shows up and deal with the rest of the code normally)

### SEMANTIC ANALYSIS: 
The semantic analysis is performed using the AST generated by the syntactic analysis and a symbol table. In this analysis, our tool makes sure that there are no semantic errors on the code, either invalid assigns, conflicting types of variables, invalid function calls, etc.
The main rules we check are:
- assigning something to a variable, both the lhs and the rhs need to have the same type, either INT or ARRAY_INT;
- in a comparison with relational operators, both the lhs and the rhs need to have the same type, either INT or ARRAY_INT;
- it is not possible to compare arrays;
- all variables must be initialized before being used;
- in function calls, the parameters must be passed according to the function arguments and the variable getting the return must have the same type as the return variable of the function.
 
### INTERMEDIATE REPRESENTATIONS (IRs): 
We didn’t use any intermediate representation.

### CODE GENERATION: 
To generate code, we write into a file as we analyse the flow of the program, depending on the node. We start with the root of the AST(the module), followed by the global declarations and then each individual function. At the end, we write the clinit method.
For each function, we analyse the nodes, which are the possible statements(ifs, assigns, loops and function calls). a function can have.	When analysing each node, we use the previously generated Symbol Table to aid us with information like variable types and return types, as well as some information about local variable usage and the maximum stack size.
 
### OVERVIEW: 
Our tool uses the DCG given by the teachers to parse .yal files and check their syntaxe. 
To do the semantic analysis, we used a breadth first search through the AST generated in the syntactic analysis, to get all functions and declarations. After that we used a depth first search to analyze the code inside every function. By doing so, we ensured that when analyzing function calls, the symbol table already knew every function in that module.
In the code generation, the logic is similar to the semantic analysis, we parse the file for functions and declarations and, after that, we parse each function and generate its code.
 
### TESTSUITE AND TEST INFRASTRUCTURE: 
To test all the tool’s functionalities, we used all the files given by the teachers in the folder “yalExamples” of the repository and we developed some other examples that are also in that folder. For the delivery we created a folder named “testsuite” with 5 of this files, that test our tool. To test each of the files, we wrote a script, so to try one file run the script that has the name of the file.
 
### TASK DISTRIBUTION: 
Syntactic analysis: Danny, João, Leonardo and Vitor
Semantic analysis: Danny, João, Leonardo and Vitor
Code generation: Danny, João, Leonardo and Vitor
Everyone worked well and did their part.
 
## PROS: 
To remove ambiguity from the DCG we avoided using “lookahead”, performing some changes to the non-literals that removed that ambiguity.
The error messages shown are very self explanatory, describing the errors and where they happened.
The tool accepts functions with the same name, but different type/number of arguments.

## CONS: 
Due to lack of time, we didn’t implement optimizations or register allocation, which would make the project better.
