package headliner;

import java.util.prefs.Preferences;

public class UserSessionManager {
    private static final Preferences prefs = Preferences.userNodeForPackage(UserSessionManager.class);
    private static String currentUser = null;
    private static boolean isLoggedIn = false;

    public static void loginUser(String username) {
        currentUser = username;
        isLoggedIn = true;
        prefs.put("currentUser", username);
        prefs.putBoolean("isLoggedIn", true);
        
        // Load user preferences
        DatabaseHelper db = new DatabaseHelper();
        String theme = db.getUserTheme(username);
        String profilePic = db.getUserProfilePic(username);
        prefs.put("theme", theme);
        prefs.put("profilePic", profilePic);
    }

    public static void logoutUser() {
        currentUser = null;
        isLoggedIn = false;
        prefs.remove("currentUser");
        prefs.remove("isLoggedIn");
        prefs.remove("googleLogin");
        prefs.remove("googleEmail");
        prefs.remove("googleName");
        prefs.remove("theme");
        prefs.remove("profilePic");
    }

    public static boolean isUserLoggedIn() {
        if (!isLoggedIn) {
            isLoggedIn = prefs.getBoolean("isLoggedIn", false);
            currentUser = prefs.get("currentUser", null);
        }
        return isLoggedIn;
    }

    public static String getCurrentUser() {
        if (currentUser == null) {
            currentUser = prefs.get("currentUser", null);
        }
        return currentUser;
    }

    public static String getCurrentUserEmail() {
        if (isUserLoggedIn()) {
            DatabaseHelper db = new DatabaseHelper();
            return db.getUserEmail(getCurrentUser());
        }
        return null;
    }

    public static void saveGoogleUser(String email, String name) {
        prefs.put("googleEmail", email);
        prefs.put("googleName", name);
        prefs.putBoolean("googleLogin", true);
        loginUser(name);
    }

    public static boolean isGoogleLogin() {
        return prefs.getBoolean("googleLogin", false);
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