package views;

import java.util.HashMap;

public class lang {
    public static String currentLang = "EN"; // default to English
    private static final HashMap<String, String> EN = new HashMap<>();
    private static final HashMap<String, String> ZH = new HashMap<>();

    static {
        EN.put("name", "Welcome to MengYou");
        EN.put("first_name", "First Name");
        EN.put("last_name", "Last Name");
        EN.put("email", "Email");
        EN.put("password", "Password");
        EN.put("confirm_password", "Confirm Password");
        EN.put("create_account", "Create Account");
        EN.put("already_have", "Already have an account? Login");
        EN.put("lang_switch", "中文");

        ZH.put("name", "欢迎使用 MengYou梦友");
        ZH.put("first_name", "名");
        ZH.put("last_name", "姓");
        ZH.put("email", "电子邮箱");
        ZH.put("password", "密码");
        ZH.put("confirm_password", "确认密码");
        ZH.put("create_account", "创建账户");
        ZH.put("already_have", "已有账户？登录");
        ZH.put("lang_switch", "English");
    }

    public static String get(String key) {
        return currentLang.equals("EN") ? EN.get(key) : ZH.get(key);
    }

    public static void toggleLanguage() {
        currentLang = currentLang.equals("EN") ? "ZH" : "EN";
    }
}
