### Compilation of built-in functions (unary operators, binary operators)

| method  | %rdi          | %rsi           |
|---------|---------------|----------------|
| ! (not) | Object caller |                |
| - (neg) | Object caller |                |
| + (add) | Object caller | First argument |
| - (sub) | Caller object | First argument |
| * (mul) | Caller object | First argument |
| / (div) | Caller object | First argument |
| % (mod) | Caller object | First argument |

### Allocating memory

Pas de call à `v.malloc(int)` sauf au début du programme ! Il faut utiliser `v.newValue(Type, int)`, qui en plus
d'allouer la mémoire et de setup le type identifier de notre nouvelle valeur, il empile tous les registres caller-saved.
Attention aux registres %rdi et %rsi qui sont souvent utilisés et qui sont bien caller-saved !!

### Registers

Convention : %r15 est callee-saved donc ne sera pas modifié par les syscalls par exemple. Ainsi, il est interdit
d'utiliser ce registre car il contient l'addresse du type descriptor array.

<image src="assets/x86-64-integer-registers-usage-conventions-l.jpg"></image>

### Possibles améliorations

- Ou mettre l'addresse du TDA ????
- Mettre l'addresse des TD directement dans les valeurs au lieu de mettre un int
- Créer un système qui permet de vérifier formellement qu'il n'y a pas de problèmes d'allocation des registres (genre
  quand on utilise %r10 ça vérifie au compile time qu'il est pas écrasé plus tard et qu'on a pas fait exprès)