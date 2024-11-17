package demo;

import demo.decorator.CacheDocument;
import demo.decorator.SmartDocument;
import demo.decorator.TimedDocument;

public class Main {
  public static void main(String[] args) {
    SmartDocument sd = new SmartDocument("example.jpg");

    TimedDocument td = new TimedDocument(sd);
    System.out.println(td.parse());

    CacheDocument cd = new CacheDocument(sd);
    cd.parse();
    cd.parse();
  }
}
