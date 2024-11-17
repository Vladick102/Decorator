package demo.decorator;

public interface AbstractDecorator {
  SmartDocument wrappee = null;

  String parse();
}
