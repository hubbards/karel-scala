# karel-scala

This project contains a deep embedding of a modified version of the
[Karel][karel] programming language in Scala. Karel is a language that is used
for controlling a simple robot. The concrete syntax for the (modified) language
is given by the following grammar. Note that comments for a syntactic category
are enclosed in parentheses and are not part of the grammar.

```
Num   ::= (any non-negative integer)

Macro ::= (any macro name)

Prog  ::= Defs define main as Stmt (a Karel program)

Defs  ::= ε | Def, Defs (sequence of definitions)

Stmts ::= ε | Stmt; Stmts (sequence of statements)

Card  ::= north | south | east | west (cardinal directions)

Dir   ::= front | back | right | left (relative directions)

Def   ::= define Macro as Stmt (macro definition)

Test  ::= not Test     (boolean negation)
       | facing Card ? (am I facing … ?)
       | clear Dir ?   (can I move … ?)
       | beeper?       (is there a beeper here?)
       | empty?        (is my bag empty?)

Stmt  ::= shutdown                   (end program)
       | move                        (move forward)
       | pick beeper                 (pick up a beeper)
       | put beeper                  (put down a beeper)
       | turn Dir                    (rotate in place)
       | call Macro                  (invoke a macro)
       | iterate Num times Stmt      (fixed repetition loop)
       | if Test then Stmt else Stmt (conditional branch)
       | while Test do Stmt          (conditional loop)
       | begin Stmts end             (statement block)
```

The abstract syntax for the language is defined in the module `karel.syntax`.
The denotational semantics for the language is defined in the module
`karel.semantics`. The state of a running program is defined in `karel.state`.

The application `KarelApp` contains several complete Karel programs. This
application also demonstrates how the denotational semantics is used as an
interpreter for the language.

[karel]: https://en.wikipedia.org/wiki/Karel_%28programming_language%29
