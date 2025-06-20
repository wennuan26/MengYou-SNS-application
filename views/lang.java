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
        EN.put("alert_title", "Alert");
        EN.put("alert_empty_fields", "Please fill in all fields.");
        EN.put("alert_password_mismatch", "Passwords do not match.");
        EN.put("account_created_successfully", "Account created successfully!");
        EN.put("account_creation_failed", "Account creation failed. Please try again.");
        EN.put("password_too_short", "Password must be at least 6 characters long.");
        EN.put("password_empty", "Password cannot be empty.");
        EN.put("email_empty", "Email cannot be empty.");

        ZH.put("name", "欢迎使用 MengYou梦友");
        ZH.put("first_name", "名");
        ZH.put("last_name", "姓");
        ZH.put("email", "电子邮箱");
        ZH.put("password", "密码");
        ZH.put("confirm_password", "确认密码");
        ZH.put("create_account", "创建账户");
        ZH.put("already_have", "已有账户？登录");
        ZH.put("lang_switch", "English");
        ZH.put("alert_title", "警告");
        ZH.put("alert_empty_fields", "请填写所有字段。");
        ZH.put("alert_password_mismatch", "密码不匹配。");
        ZH.put("account_created_successfully", "账户创建成功！");
        ZH.put("account_creation_failed", "账户创建失败，请重试。");
        ZH.put("password_too_short", "密码必须至少6个字符长。");
        ZH.put("password_empty", "密码不能为空。");
        ZH.put("email_empty", "电子邮箱不能为空。");
        

    }

    public static String get(String key) {
        return currentLang.equals("EN") ? EN.get(key) : ZH.get(key);
    }

    public static void toggleLanguage() {
        currentLang = currentLang.equals("EN") ? "ZH" : "EN";
    }
}
