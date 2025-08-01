/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.HashMap;

public class LanguageManager {
    public static String current = "EN"; // default language

    private static final HashMap<String, String[]> dictionary = new HashMap<>() {{
        put("login", new String[]{"Login", "登录"});
        put("register", new String[]{"SIGN UP", "注册"});
        put("email", new String[]{"Email", "邮箱"});
        put("password", new String[]{"Password", "密码"});
        put("first_name", new String[]{"First Name", "名"});
        put("last_name", new String[]{"Last Name", "姓"});
        put("post", new String[]{"Post", "发布"});
        put("welcome", new String[]{"Welcome", "欢迎"});
        put("addItem", new String[]{"Add Item", "添加商品"});
        put("marketplace", new String[]{"Marketplace", "市场"});
        put("name", new String[]{"Name", "名称"});
        put("description", new String[]{"Description", "描述"});
        put("price", new String[]{"Price", "价格"});
        put("communities", new String[]{"Communities", "社区"});
        put("create", new String[]{"Create", "创建"});
        put("language", new String[]{"Language", "语言"});
        put("switch", new String[]{"中文", "英文"});
    }};

    public static String t(String key) {
        int idx = current.equals("EN") ? 0 : 1;
        return dictionary.getOrDefault(key, new String[]{key, key})[idx];
    }

    public static void toggleLanguage() {
        current = current.equals("EN") ? "ZH" : "EN";
    }
}
