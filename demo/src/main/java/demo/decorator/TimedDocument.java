package demo.decorator;

public class TimedDocument implements AbstractDecorator {
  SmartDocument wrappee = null;

  public TimedDocument(SmartDocument wrappee) {
    this.wrappee = wrappee;
  }

  @Override
  public String parse() {
    long start = System.nanoTime();
    String ret_value = wrappee.parse();
    long end_time = System.nanoTime();

    return ret_value + "\nExecution time is: " + ((double) (end_time - start)) / 1000000000d;
  }
}
