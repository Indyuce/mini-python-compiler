# mini-python-compiler
A compiler for Python subset into assembly language

### Setup IntelliJ
- File > Project Structure > Librairies > Ajouter `lib/java-cup-11a-runtime.jar`
- Clic-droit sur `src` > Mark Directory As... > Source Root
- Clic-droit sur `test` > Mark Directory As... > Test Sources
- Utiliser Java 17 en JDK
- Créer une Run Configuration pour la classe Main, avec `--debug test.py` en arguments

### Setup Windows
- Télécharger MinGW32 Installer (minimalistic GNU for Windows) puis installer le compilateur
- Pour maker les tests, utiliser `mingw32-make -f .\Makefile_Windows`

### Tester du code assembleur généré
- Se connecter en SSH à un ordi de la salle info
- Compiler le code avec `gcc -g -no-pie <fichier.s> -o fichier`
- Lancer le code avec GDB en utilisant `gdb ./fichier`

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

<image style="width: 600px;" src="assets/x86-64-integer-registers-usage-conventions-l.jpg"></image>
<image style="width: 600px;" src="assets/1_4ipwUzIWd4eqUvcEmZ5tMQ.png"></image>

### Autres astuces

- Un bon compilateur C en ligne pour avoir de l'inspiration pour le code assembly : https://godbolt.org/

### Possibles améliorations

- Ou mettre l'addresse du TDA ????
- Mettre l'addresse des TD directement dans les valeurs au lieu de mettre un int
- Créer un système qui permet de vérifier formellement qu'il n'y a pas de problèmes d'allocation des registres (genre
  quand on utilise %r10 ça vérifie au compile time qu'il est pas écrasé plus tard et qu'on a pas fait exprès)
- Tuples python (unchangeable) ou classes
