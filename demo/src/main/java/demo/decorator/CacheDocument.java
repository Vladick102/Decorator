package demo.decorator;

import java.sql.*;


public class CacheDocument implements AbstractDecorator {
  private static final String dbPath = "jdbc:sqlite:ocr_cache.db";
  private SmartDocument wrappee;

  public CacheDocument(SmartDocument smartDocument) {
    this.wrappee = smartDocument;
    initializeDatabase();
  }

  private void initializeDatabase() {
    try (Connection conn = DriverManager.getConnection(dbPath)) {
      if (conn != null) {
        // Create the ocr_cache table if it does not exist
        String createTableSQL = "CREATE TABLE IF NOT EXISTS ocr_cache ("
            + "file_path TEXT PRIMARY KEY, " + "ocr_text TEXT)";
        try (Statement stmt = conn.createStatement()) {
          stmt.execute(createTableSQL);
          System.out.println("Database and table initialized.");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String parse() {
    String localPath = wrappee.getLocalPath();

    String cachedResult = getCachedResult(localPath);
    if (cachedResult != null) {
      System.out.println("Got cached result");
      return cachedResult;
    }

    System.out.println("Inserting new data");
    String ocrResult = wrappee.parse();
    insertUniqueResult(localPath, ocrResult);
    return ocrResult;
  }

  // Retrieve cached result from SQLite database
  private String getCachedResult(String filePath) {
    String query = "SELECT ocr_text FROM ocr_cache WHERE file_path = ?";
    try (Connection conn = DriverManager.getConnection(dbPath);
        PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setString(1, filePath);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getString("ocr_text");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null; // Return null if no cached result is found
  }

  // Insert unique OCR result in SQLite database
  private void insertUniqueResult(String filePath, String ocrText) {
    String query = "INSERT OR IGNORE INTO ocr_cache (file_path, ocr_text) VALUES (?, ?)";
    try (Connection conn = DriverManager.getConnection(dbPath);
        PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setString(1, filePath);
      stmt.setString(2, ocrText);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
