package headliner;

import java.util.prefs.Preferences;

public class UserSessionManager {
    private static final Preferences prefs = Preferences.userNodeForPackage(UserSessionManager.class);
    private static String currentUser = null;
    private static int currentUserId = -1; // ADDED THIS
    private static boolean isLoggedIn = false;

    public static void loginUser(String username) {
        currentUser = username;
        isLoggedIn = true;
        prefs.put("currentUser", username);
        prefs.putBoolean("isLoggedIn", true);
        
        // Load user preferences and ID
        DatabaseHelper db = new DatabaseHelper();
        currentUserId = db.getUserId(username); // ADDED THIS
        String theme = db.getUserTheme(username);
        String profilePic = db.getUserProfilePic(username);
        prefs.put("theme", theme);
        prefs.put("profilePic", profilePic);
    }

    public static void logoutUser() {
        currentUser = null;
        currentUserId = -1; // ADDED THIS
        isLoggedIn = false;
        prefs.remove("currentUser");
        prefs.remove("isLoggedIn");
        prefs.remove("theme");
        prefs.remove("profilePic");
    }

    public static boolean isUserLoggedIn() {
        if (!isLoggedIn) {
            isLoggedIn = prefs.getBoolean("isLoggedIn", false);
            if (isLoggedIn) {
                currentUser = prefs.get("currentUser", null);
                DatabaseHelper db = new DatabaseHelper();
                currentUserId = db.getUserId(currentUser); // ADDED THIS
            }
        }
        return isLoggedIn;
    }

    public static String getCurrentUser() {
        if (currentUser == null) {
            currentUser = prefs.get("currentUser", null);
        }
        return currentUser;
    }

    // ADDED THIS METHOD
    public static int getCurrentUserId() {
        if (currentUserId == -1 && isUserLoggedIn()) {
            DatabaseHelper db = new DatabaseHelper();
            currentUserId = db.getUserId(getCurrentUser());
        }
        return currentUserId;
    }

    public static String getCurrentUserEmail() {
        if (isUserLoggedIn()) {
            DatabaseHelper db = new DatabaseHelper();
            return db.getUserEmail(getCurrentUser());
        }
        return null;
    }

    public static String getTheme() {
        return prefs.get("theme", "light");
    }

    public static void setTheme(String theme) {
        prefs.put("theme", theme);
        if (isUserLoggedIn()) {
            DatabaseHelper db = new DatabaseHelper();
            db.updateUserProfile(getCurrentUser(), getProfilePic(), theme);
        }
    }

    public static String getProfilePic() {
        return prefs.get("profilePic", "default");
    }

    public static void setProfilePic(String profilePic) {
        prefs.put("profilePic", profilePic);
        if (isUserLoggedIn()) {
            DatabaseHelper db = new DatabaseHelper();
            db.updateUserProfile(getCurrentUser(), profilePic, getTheme());
        }
    }
}