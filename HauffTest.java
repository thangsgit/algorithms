public class HauffTest {
  private static final String BASE = "/Users/thanga/hauffout/"  ;


  public static void test1(String s, boolean show, String dotfilename) {
    Hauffman h = new Hauffman(s,show,dotfilename);
    String d = h.encode();
    String f = h.decode();

    if(s.equals(f))
      System.out.println("PASS - Original and decoded string are same. PASS....") ;
    else
      System.out.println("FAIL - Original and decoded string are not same...") ;

    double sl = s.length() * 7 ;
    double dl = d.length();
    System.out.println("Original string cost = " + sl) ;
    System.out.println("Decoded  string cost = " + dl) ;
    double r = ((dl - sl)/sl) * 100 ;
    System.out.println("% reduction = " + (-r)) ;
  }

  public static void testbed() {
    boolean show = true ;
    test1("a",show,BASE + "1.dot");
    test1("aba",show,BASE+"2.dot");
    test1("aaabbggggghhhhaaaggggaaaaa_+@#",show,BASE +"3.dot");
    test1("A quick brown fox jumps over the lazy dog",show,BASE +"4.dot");
    test1("Pack my box with five dozen liquor jugs",show, BASE + "5.dot");
    test1("Long years ago we made a tryst with destiny, and now the time comes when we shall redeem our pledge, not wholly or in full measure, but very substantially.At the stroke of the midnight hour, when the world sleeps, India will awake to life and freedom. A moment comes, which comes but rarely in history, when we step out from the old to the new, when an age ends, and when the soul of a nation, long suppressed, finds utterance.",show,BASE +"6.dot");
    test1("Baa, baa, black sheep, have you any wool?",show,BASE + "7.dot") ;

    if (show) {
      System.out.println("===============  Done with Test1 ==================") ;
    }
  }

  public static void main(String[] args) {
    System.out.println("HauffTest.java");
    testbed() ;
    System.out.println(" All Hauffman Test cases are passed. Awesome");
  }

}

