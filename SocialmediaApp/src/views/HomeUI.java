package views;

import Controller.PostController;
import Model.Comment;
import Model.Post;
import Model.User;
import Controller.UserController;
import util.LanguageManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import static views.guiCons.*;

public class HomeUI extends JFrame {
    private User currentUser;
    private PostController postController;
    private JPanel feedPanel;
    private ArrayList<Post> allPosts;
    private JTextField searchField;

    public HomeUI(User user) {
        this.currentUser = user;
        this.postController = new PostController();

        setTitle("Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setResizable(true);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND);
        setContentPane(mainPanel);

        mainPanel.add(createSidebar(), BorderLayout.WEST);
        mainPanel.add(createTopBar(), BorderLayout.NORTH);

        feedPanel = new JPanel();
        feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS));
        feedPanel.setBackground(TEXT);
        JScrollPane feedScroll = new JScrollPane(feedPanel);
        feedScroll.setBorder(BorderFactory.createLineBorder(BACKGROUND, 2));
        mainPanel.add(feedScroll, BorderLayout.CENTER);

        allPosts = postController.fetchAllPosts();
        updateFeed(allPosts);

        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BACKGROUND);
        sidebar.setPreferredSize(new Dimension(220, getHeight()));

        JLabel title = new JLabel("MengYou");
        title.setFont(new Font("微软雅黑", Font.BOLD, 22));
        title.setForeground(TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebar.add(title);

        ImageIcon marketIcon = new ImageIcon("/Users/cheizhao/Documents/LNBTI/SocialmediaApp/src/util/icons8-shop-48.png");
        ImageIcon communityIcon = new ImageIcon("/Users/cheizhao/Documents/LNBTI/SocialmediaApp/src/util/icons8-community-32.png");
        ImageIcon profileIcon = new ImageIcon("/Users/cheizhao/Documents/LNBTI/SocialmediaApp/src/util/icons8-profile-32.png");
        ImageIcon logoutIcon = new ImageIcon("/Users/cheizhao/Documents/LNBTI/SocialmediaApp/src/util/icons8-logout-30.png");

        jbutton marketButton = new jbutton(LanguageManager.t("marketplace"), 20, 14, marketIcon);
        jbutton communityButton = new jbutton(LanguageManager.t("communities"), 20, 14, communityIcon);
        jbutton profileButton = new jbutton(LanguageManager.t("profile"), 20, 14, profileIcon);
        jbutton logoutButton = new jbutton(LanguageManager.t("logout"), 20, 14, logoutIcon);

        Dimension btnSize = new Dimension(180, 40);
        for (jbutton btn : new jbutton[]{marketButton, communityButton, profileButton, logoutButton}) {
            btn.setMaximumSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(10));
        }

        marketButton.addActionListener(e -> {
            dispose();
            new MarketplaceUI(currentUser);
        });

        communityButton.addActionListener(e -> {
            dispose();
            new CommunityUI(currentUser);
        });

        profileButton.addActionListener(e -> {
        dispose();
        UserController userController = new UserController(); // ✅ create controller instance
        new ProfileUI(currentUser, userController);           // ✅ pass both User and Controller
        });


        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginUI();
            }
        });

        return sidebar;
    }
    private JPanel createTopBar() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel postArea = new JPanel();
        postArea.setLayout(new BoxLayout(postArea, BoxLayout.Y_AXIS));
        postArea.setBackground(BACKGROUND);

        // Welcome Row
        JPanel welcomeRow = new JPanel();
        welcomeRow.setLayout(new BoxLayout(welcomeRow, BoxLayout.X_AXIS));
        welcomeRow.setBackground(BACKGROUND);

        ImageIcon homeIcon = new ImageIcon("/Users/cheizhao/Documents/LNBTI/SocialmediaApp/src/util/icons8-home-page-48-2.png");
        JLabel iconLabel = new JLabel(homeIcon);
        JLabel welcomeLabel = new JLabel(LanguageManager.t("welcome") + ", " + currentUser.getFirstName());
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        welcomeLabel.setForeground(TEXT);
        welcomeRow.add(iconLabel);
        welcomeRow.add(Box.createHorizontalStrut(10));
        welcomeRow.add(welcomeLabel);

        postArea.add(welcomeRow);
        postArea.add(Box.createVerticalStrut(10));

        // Post field + Button in same row
        JPanel postRow = new JPanel(new BorderLayout(10, 0));
        postRow.setBackground(BACKGROUND);

        JTextArea postField = new JTextArea(3, 50);
        postField.setLineWrap(true);
        postField.setWrapStyleWord(true);
        postField.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        postField.setBackground(SECONDARYHINT);
        postField.setForeground(BACKGROUND);
        JScrollPane postScroll = new JScrollPane(postField);
        postScroll.setBorder(BorderFactory.createLineBorder(BACKGROUND, 2));

        jbutton postButton = new jbutton(LanguageManager.t("post"), 20, 14);
        postButton.setPreferredSize(new Dimension(70, 55));
        postButton.setBackground(new Color(0x28a745));
        postButton.setForeground(BACKGROUND);

        postRow.add(postScroll, BorderLayout.CENTER);
        postRow.add(postButton, BorderLayout.EAST);

        postArea.add(postRow);
        topPanel.add(postArea, BorderLayout.CENTER);

        // Search Row to the right
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(BACKGROUND);

        searchField = new JTextField(25);
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        searchField.setPreferredSize(new Dimension(250, 35));
        searchField.setForeground(BACKGROUND);
        searchField.setBackground(SECONDARYHINT);

        jbutton searchBtn = new jbutton(LanguageManager.t("search"), 18, 12);
        searchBtn.setPreferredSize(new Dimension(120, 35));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        topPanel.add(searchPanel, BorderLayout.EAST);

        // Add post button logic
        postButton.addActionListener(e -> {
            String content = postField.getText().trim();
            if (content.isBlank()) {
                JOptionPane.showMessageDialog(this, "Post content cannot be empty.");
                return;
            }
            postButton.setEnabled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() {
                    return postController.createPost(currentUser.getId(), content);
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            postField.setText("");
                            allPosts = postController.fetchAllPosts();
                            updateFeed(allPosts);
                        } else {
                            JOptionPane.showMessageDialog(HomeUI.this, "Post failed.");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        postButton.setEnabled(true);
                        setCursor(Cursor.getDefaultCursor());
                    }
                }
            };
            worker.execute();
        });

        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().toLowerCase().trim();
            ArrayList<Post> filtered = new ArrayList<>();
            for (Post p : allPosts) {
                if (p.getContent().toLowerCase().contains(keyword) ||
                    p.getAuthor().getFirstName().toLowerCase().contains(keyword)) {
                    filtered.add(p);
                }
            }
            updateFeed(filtered);
        });

        return topPanel;
        }
    private void updateFeed(ArrayList<Post> posts) {
        feedPanel.removeAll();
        for (Post post : posts) {
            JPanel postPanel = new JPanel();
            postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
            postPanel.setBackground(BACKGROUND); // ✅ Use BG1
            postPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BUTTONS));

            JPanel header = new JPanel(new BorderLayout(10, 10));
            header.setBackground(BACKGROUND); // ✅


            JLabel avatarLabel = new JLabel();
            String avatarPath = post.getAuthor().getAvatarPath();
            if (avatarPath != null && !avatarPath.isBlank() && new File(avatarPath).exists()) {
                avatarLabel.setIcon(getCircularImageIcon(avatarPath, 50));
            } else {
                avatarLabel.setIcon(getCircularImageIcon("/Users/cheizhao/Documents/LNBTI/SocialmediaApp/src/util/account.png", 50));
            }
            avatarLabel.setPreferredSize(new Dimension(60, 60));

            JPanel leftInfo = new JPanel();
            leftInfo.setLayout(new BoxLayout(leftInfo, BoxLayout.Y_AXIS));
            leftInfo.setBackground(BACKGROUND); // ✅


            JLabel nameLabel = new JLabel(post.getAuthor().getFirstName());
            nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
            nameLabel.setForeground(TEXT); // ✅


            JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            buttonsRow.setBackground(BACKGROUND); // ✅

            ArrayList<User> likes = postController.fetchLikesForPost(post.getId());
            boolean likedByCurrentUser = likes.stream().anyMatch(u -> u.getId() == currentUser.getId());

            jbutton likeButton = new jbutton(likedByCurrentUser ? "♥ Like" : "♡ Like", 18, 14);
            likeButton.setForeground(likedByCurrentUser ? Color.RED : BACKGROUND);
            likeButton.setPreferredSize(new Dimension(100, 30));

            JLabel likeCount = new JLabel(likes.size() + " likes");
            likeCount.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            likeCount.setForeground(TEXT);

            jbutton commentToggle = new jbutton("💬 Comments", 18, 14);
            commentToggle.setPreferredSize(new Dimension(120, 30));

            likeButton.addActionListener(e -> {
                postController.toggleLike(post.getId(), currentUser.getId());
                updateFeed(postController.fetchAllPosts());
            });

            buttonsRow.add(likeButton);
            buttonsRow.add(likeCount);
            buttonsRow.add(commentToggle);

            leftInfo.add(nameLabel);
            leftInfo.add(buttonsRow);

            header.add(avatarLabel, BorderLayout.WEST);
            header.add(leftInfo, BorderLayout.CENTER);
            postPanel.add(header);

            JTextArea postText = new JTextArea(post.getContent());
            postText.setLineWrap(true);
            postText.setWrapStyleWord(true);
            postText.setEditable(false);
            postText.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            postText.setBackground(BACKGROUND); // ✅
            postText.setForeground(TEXT); // ✅

            postText.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            postPanel.add(postText);

            JPanel commentSection = new JPanel();
            commentSection.setLayout(new BoxLayout(commentSection, BoxLayout.Y_AXIS));
            commentSection.setBackground(new Color(0xf0f0f0));
            commentSection.setVisible(false);
            commentSection.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));

            ArrayList<Comment> comments = postController.fetchCommentsForPost(post.getId());
            for (Comment comment : comments) {
                JLabel commentLabel = new JLabel(comment.getAuthor().getFirstName() + ": " + comment.getContent());
                commentLabel.setForeground(TEXT);
                commentLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
                commentSection.add(commentLabel);
                commentSection.setBackground(BACKGROUND); // ✅
                commentSection.add(Box.createVerticalStrut(3));
            }

            JPanel commentInputPanel = new JPanel(new BorderLayout(5, 5));
            commentInputPanel.setBackground(BACKGROUND); // ✅

            JTextField commentInput = new JTextField();
            commentInput.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            commentInputPanel.setBackground(BACKGROUND); // ✅

            jbutton commentPostButton = new jbutton("➤", 16, 12);
            commentPostButton.setPreferredSize(new Dimension(50, 30));

            commentPostButton.addActionListener(e -> {
                String content = commentInput.getText().trim();
                if (!content.isEmpty()) {
                    postController.addComment(post.getId(), currentUser.getId(), content);
                    commentInput.setText("");
                    updateFeed(postController.fetchAllPosts());
                }
            });

            commentInputPanel.add(commentInput, BorderLayout.CENTER);
            commentInputPanel.add(commentPostButton, BorderLayout.EAST);
            commentSection.add(Box.createVerticalStrut(5));
            commentSection.add(commentInputPanel);

            commentToggle.addActionListener(e -> {
                commentSection.setVisible(!commentSection.isVisible());
                postPanel.revalidate();
            });

            postPanel.add(commentSection);
            feedPanel.add(postPanel);
        }
        feedPanel.revalidate();
        feedPanel.repaint();
    }

    private ImageIcon getCircularImageIcon(String path, int diameter) {
        try {
            File file = new File(path);
            if (!file.exists()) return new ImageIcon();
            BufferedImage original = ImageIO.read(file);
            if (original == null) return new ImageIcon();
            Image scaled = original.getScaledInstance(diameter, diameter, Image.SCALE_SMOOTH);
            BufferedImage circleBuffer = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circleBuffer.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, diameter, diameter));
            g2.drawImage(scaled, 0, 0, null);
            g2.dispose();
            return new ImageIcon(circleBuffer);
        } catch (Exception e) {
            e.printStackTrace();
            return new ImageIcon();
        }
    }
}
