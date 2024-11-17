package demo.decorator;

import lombok.Getter;
import lombok.Setter;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Getter
@Setter
public class SmartDocument implements Document {
  private String localPath;

  public String parse() {
    Tesseract tesseract = new Tesseract();
    tesseract.setDatapath("tesseract_dp");
    tesseract.setLanguage("ukr");

    try {
      System.out.println("debug");
      return tesseract.doOCR(new java.io.File(localPath));
    } catch (TesseractException e) {
      e.printStackTrace();
      return "";
    }
  }
}
