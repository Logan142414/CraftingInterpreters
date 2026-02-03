//java <script name> <argument>       - 0 arguments, runs REPL  - 1 argument, runs file   - 2+ arguments, error
//Ex) java com.craftinginterpreters.lox.Lox hello.lox

// Error handling methods at the bottom, after line 60. 
//If all is well, user doesn't see anything the the developer wrote. But errors are when user and backend interact.
package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
  static boolean hadError = false;

  // / Command-line entry + mode selection (main)
  public static void main(String[] args) throws IOException {
    if (args.length > 1) {
      System.out.println("Usage: jlox [script]");
      System.exit(64);
    } else if (args.length == 1) {
      runFile(args[0]);
    } else {
      runPrompt();
    }
  }

  // Option 1: Read the entire file into memory and hand the source text to run(...)
  private static void runFile(String path) throws IOException {
    byte[] bytes = Files.readAllBytes(Paths.get(path));
    run(new String(bytes, Charset.defaultCharset()));

  //   // Indicate an error in the exit code.
     if (hadError) System.exit(65);
     }

  // Option 2: Read from standard input, line by line
  private static void runPrompt() throws IOException {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);

  // Control+Z (Windows) or Control+D (Unix) to exit REPL
    for (;;) {
      System.out.print("> ");
      String line = reader.readLine();
      if (line == null) break;
      run(line);
      // hadError = false;
    }
  }

  //Now, what do you print out when given file or expression
  private static void run(String source) {
    Scanner scanner = new Scanner(source);
    List<Token> tokens = scanner.scanTokens();

    // The parsing part (to be added later)
    // Parser parser = new Parser(tokens);
    // Expr expression = parser.parse();

    // For now, just print the tokens.
    for (Token token : tokens) {
      System.out.println(token);

    // Stop if there was a syntax error. Rather than executing code with errors.
    if (hadError) return;

    System.out.println(new AstPrinter().print(expression));
  }

///////////////////////////////////////////////////////////////////////////

    // Now handle errors
    // report is what will flag the errors and print them out
    // error line method for errors not associated with a specific token (scanner)
    // error token method for errors associated with a specific token (parser)
  static void error(int line, String message) {
    report(line, "", message);
  }

  private static void report(int line, String where, String message) {
    System.err.println("[line " + line + "] Error" + where + ": " + message);
    hadError = true;
  }

  static void error(Token token, String message) {
    if (token.type == TokenType.EOF) {
      report(token.line, " at end", message);
    } else {
      report(token.line, " at '" + token.lexeme + "'", message);
    }
  }

}