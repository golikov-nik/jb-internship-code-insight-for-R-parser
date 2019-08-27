# Grammar parser

Implement a parser for language with given grammar:


    <character>  ::= "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" | "J" 
    | "K" | "L" | "M" | "N" | "O" | "P" | "Q" | "R" | "S" | "T" | "U" | "V" 
    | "W" | "X" | "Y" | "Z" | "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" 
    | "i" | "j" | "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" | "s" | "t" 
    | "u" | "v" | "w" | "x" | "y" | "z" | "_"
    <digit>   ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
    <number> ::= <digit> | <digit> <number>
    <identifier> ::= <character> | <identifier> <character>
    <operation> ::= "+" | "-" | "*" | "/" | "%" | ">" | "<" | "="

    <constant-expression> ::= "-" <number> | <number>
    <binary-expression> ::= "(" <expression> <operation> <expression>  ")"
    <argument-list> ::= <expression> | <expression> "," <argument-list>
    <call-expression> ::= <identifier> "(" <argument-list> ")"
    <if-expression> ::= "[" <expression> "]?{" <expression> "}:{"<expression>"}"

    <expression> ::= <identifier>
                      | <constant-expression>
                      | <binary-expression>
                      | <if-expression>
                      | <call-expression>

    <parameter-list> ::= <identifier> | <identifier> "," <parameter-list>

    <function-definition> ::= <identifier>"(" <parameter_list> ")" "={" <expression> "}"

    <function-definition-list> : ""
                                 | <function-definition> <EOL>
                                 | <function-definition> <EOL> <function-definition-list>

    <program> ::= <function-definition-list> <expression>

`<EOL>` - newline symbol - `\n`, program doesn't contain any other whitespace symbols

Language semantics:
 
1. All variables are 32-bit integers
2. Overflows do not occur
3. All arithmetic operations are equivalent to corresponding ones from Java
4. Comparison operations return 1 if true, 0 otherwise
5. `<if-expression>` evaluates second expression if first one is not equal to zero, otherwise it evaluates third
6.  `<call-expression>` calls function with corresponding name
7.  Expressions are evaluated from left to right