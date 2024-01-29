package mini_python;

class Typing {

  static boolean debug = false;

  // use this method to signal typing errors
  static void error(Location loc, String msg) {
    throw new Error(loc + "\nerror: " + msg);
  }

  static TFile file(File f) {
    error(null, "TODO");
    return null;
  }

}
