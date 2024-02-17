# mini-python-compiler
A compiler for Python subset into assembly language

## Setup local (IntelliJ)
- File > Project Structure > Librairies > Ajouter `lib/java-cup-11a-runtime.jar`
- Clic-droit sur `src` > Mark Directory As... > Source Root
- Clic-droit sur `test` > Mark Directory As... > Test Sources
- Utiliser Java 17 en JDK
- Créer une Run Configuration pour la classe Main, avec `--debug test.py` en arguments

### Setup Windows
- Télécharger MinGW32 Installer (minimalistic GNU for Windows) puis installer le compilateur
- Pour maker les tests, utiliser `mingw32-make -f .\Makefile_Windows`
