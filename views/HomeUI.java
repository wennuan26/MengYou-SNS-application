package views;

import Controller.PostController;
import Model.Post;
import Model.User;
import util.LanguageManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


@SuppressWarnings("serial")
public class HomeUI extends jFrame {
    private User currentUser;
    private PostController postController;
    private JTextArea feedArea;
    private ArrayList<Post> allPosts;
    private jtextfield searchField;

    public HomeUI(User user) {
        this.currentUser = user;
        this.postController = new PostController();
        setLayout(null);

        // 🏷 Welcome Label
        jlabel welcomeLabel = new jlabel(LanguageManager.t("welcome") + ", " + user.getFirstName(), 18, guiCons.White, Font.BOLD);
        welcomeLabel.setBounds(30, 15, 400, 30);
        add(welcomeLabel);

        // 📝 Post Field
        JTextArea postField = new JTextArea();
        postField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        postField.setForeground(guiCons.Hint1);
        postField.setLineWrap(true);
        JScrollPane postScroll = new JScrollPane(postField);
        postScroll.setBounds(30, 60, 400, 60);
        postScroll.setBorder(BorderFactory.createLineBorder(guiCons.lightpink, 2));
        add(postScroll);

        // 📤 Post Button
        jbutton postButton = new jbutton(LanguageManager.t("post"), 25, 14);
        postButton.setBounds(450, 70, 100, 40);
        add(postButton);

        // 🔍 Search Bar
        searchField = new jtextfield("Search...");
        searchField.setBounds(30, 140, 400, 40);
        add(searchField);

        jbutton searchButton = new jbutton(LanguageManager.t("search"), 25, 14);
        searchButton.setBounds(450, 140, 100, 40);
        add(searchButton);

        // 📄 Feed Display
        feedArea = new JTextArea();
        feedArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        feedArea.setForeground(guiCons.Hint1);
        feedArea.setEditable(false);
        feedArea.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(feedArea);
        scrollPane.setBounds(30, 200, 520, 360);
        scrollPane.setBorder(BorderFactory.createLineBorder(guiCons.lightpink, 2));
        add(scrollPane);

        // 🛒 Marketplace Button
        jbutton marketButton = new jbutton(LanguageManager.t("marketplace"), 25, 14);
        marketButton.setBounds(100, 580, 150, 40);
        add(marketButton);

        // 👥 Communities Button
        jbutton communityButton = new jbutton(LanguageManager.t("communities"), 25, 14);
        communityButton.setBounds(350, 580, 150, 40);
        add(communityButton);

        // 📬 Button Actions
        postButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                String content = postField.getText().trim();
                if (!content.isBlank()) {
                    postController.createPost(user.getId(), content);
                    postField.setText("");
                    allPosts = postController.fetchAllPosts(); // refresh
                    updateFeed(allPosts);
                }
            }
        });

        searchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                String keyword = searchField.getText().toLowerCase().trim();
                ArrayList<Post> filtered = new ArrayList<>();
                for (Post p : allPosts) {
                    if (p.getContent().toLowerCase().contains(keyword) ||
                        p.getAuthor().getFirstName().toLowerCase().contains(keyword)) {
                        filtered.add(p);
                    }
                }
                updateFeed(filtered);
            }
        });

        marketButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new MarketplaceUI(currentUser);
            }
        });

        communityButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new CommunityUI(currentUser);
            }
        });

        // ⏬ Load all posts initially
        allPosts = postController.fetchAllPosts();
        updateFeed(allPosts);
        
        setSize(650, 750);
        setLocationRelativeTo(null);  // to center the window on screen
        setVisible(true);
        
    }
    

    private void updateFeed(ArrayList<Post> posts) {
        StringBuilder sb = new StringBuilder();
        for (Post post : posts) {
            sb.append(post.getAuthor().getFirstName())
              .append(" says:\n")
              .append(post.getContent())
              .append("\n---\n");
        }
        feedArea.setText(sb.toString());
    }
}
