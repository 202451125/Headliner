package headliner;

public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        
        DatabaseHelper db = new DatabaseHelper();
        
        // Test registration
        boolean result = db.registerUser("testuser", "testpass");
        System.out.println("Registration test: " + (result ? "SUCCESS" : "FAILED"));
        
        // Test validation
        boolean valid = db.validateUser("testuser", "testpass");
        System.out.println("Validation test: " + (valid ? "SUCCESS" : "FAILED"));
        
        // Test wrong password
        boolean invalid = db.validateUser("testuser", "wrongpass");
        System.out.println("Wrong password test: " + (!invalid ? "SUCCESS" : "FAILED"));
    }
}