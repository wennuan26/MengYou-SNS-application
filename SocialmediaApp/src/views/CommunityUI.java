package views;

import Controller.CommunityController;
import Controller.CommunityPostContoller;
import Model.Comment;
import Model.Community;
import Model.Post;
import Model.User;
import util.LanguageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import static views.guiCons.*;

public class CommunityUI extends jFrame {
    private final CommunityController controller = new CommunityController();
    private final CommunityPostContoller postController = new CommunityPostContoller();
    private final User currentUser;
    private ArrayList<Community> communities;
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final HashMap<Integer, Community> communityMap = new HashMap<>();
    private JList<String> list;
    private jtextfield searchField;
    private JPanel mainPanel, rightPanel;

    public CommunityUI(User user) {
        this.currentUser = user;
        setTitle("Communities");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND);
        setContentPane(mainPanel);

        setupLeftPanel();
        setupRightPanel();

        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                refreshList(searchField.getText().trim());
            }
        });

        setVisible(true);
    }

    private void setupLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(BACKGROUND);
        leftPanel.setPreferredSize(new Dimension(400, 600));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(BACKGROUND);

        jlabel titleLabel = new jlabel("Communities:", 22, TEXT, Font.BOLD);
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(BACKGROUND);

        searchField = new jtextfield("Search communities...");
        searchField.setPreferredSize(new Dimension(200, 35));
        searchPanel.add(searchField);

        jbutton searchBtn = new jbutton("Search", 20, 14);
        jbutton resetBtn = new jbutton("Reset", 20, 14);
        searchBtn.setPreferredSize(new Dimension(80, 35));
        resetBtn.setPreferredSize(new Dimension(80, 35));

        searchPanel.add(searchBtn);
        searchPanel.add(resetBtn);

        topPanel.add(searchPanel);
        leftPanel.add(topPanel, BorderLayout.NORTH);

        list = new JList<>(model);
        list.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        list.setBackground(BACKGROUND);
        list.setForeground(SECONDARYHINT);
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createLineBorder(BUTTONS, 2));
        leftPanel.add(scroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBackground(BACKGROUND);

        jbutton createBtn = new jbutton("Create", 20, 14);
        jbutton backBtn = new jbutton(LanguageManager.t("back"), 20, 14);

        createBtn.setPreferredSize(new Dimension(100, 35));
        backBtn.setPreferredSize(new Dimension(100, 35));

        bottomPanel.add(createBtn);
        bottomPanel.add(backBtn);
        leftPanel.add(bottomPanel, BorderLayout.SOUTH);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        createBtn.addActionListener(e -> new CommunityCreationUI(currentUser));
        searchBtn.addActionListener(e -> refreshList(searchField.getText().trim()));
        resetBtn.addActionListener(e -> {
            searchField.setText("");
            refreshList(null);
        });

        backBtn.addActionListener(e -> {
            dispose();
            new HomeUI(currentUser);
        });

        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = list.getSelectedIndex();
                if (index >= 0 && index < communities.size()) {
                    Community selected = communities.get(index);
                    displayCommunity(selected);
                }
            }
        });

        refreshList(null);
    }

    private void setupRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBackground(BACKGROUND);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
    }

    private void refreshList(String keyword) {
        communities = controller.getAllCommunities();
        model.clear();
        communityMap.clear();

        for (Community c : communities) {
            boolean matches = keyword == null || keyword.isEmpty()
                    || c.getName().toLowerCase().contains(keyword.toLowerCase())
                    || c.getDescription().toLowerCase().contains(keyword.toLowerCase())
                    || c.getOwner().getFirstName().toLowerCase().contains(keyword.toLowerCase());
            if (matches) {
                model.addElement(c.getName() + " by " + c.getOwner().getFirstName());
                communityMap.put(model.getSize() - 1, c);
            }
        }
    }

    private void displayCommunity(Community community) {
        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout(10, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (!controller.isMember(currentUser.getId(), community.getId()) &&
                community.getOwner().getId() != currentUser.getId()) {

            JLabel info = new jlabel("You are not a member of this community.", 18, TEXT, Font.BOLD);
            jbutton joinBtn = new jbutton("Join", 20, 14);
            joinBtn.setPreferredSize(new Dimension(120, 40));

            JPanel center = new JPanel();
            center.setBackground(BACKGROUND);
            center.add(info);
            center.add(Box.createHorizontalStrut(10));
            center.add(joinBtn);

            joinBtn.addActionListener(e -> {
                if (controller.joinCommunity(currentUser.getId(), community.getId())) {
                    JOptionPane.showMessageDialog(null, "Joined successfully!");
                    displayCommunity(community);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to join.");
                }
            });

            rightPanel.add(center, BorderLayout.CENTER);
        } else {
            JPanel topInfo = new JPanel(new BorderLayout());
            topInfo.setBackground(BACKGROUND);

            // Left: Community Name and Description
            JPanel namePanel = new JPanel();
            namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
            namePanel.setBackground(BACKGROUND);
            namePanel.add(new jlabel(community.getName(), 20, TEXT, Font.BOLD));
            namePanel.add(new jlabel(community.getDescription(), 16, SECONDARYHINT, Font.PLAIN));

            // Right: Action Buttons (Settings / Leave)
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(BACKGROUND);

            // Determine role
            boolean isOwner = currentUser.getId() == community.getOwner().getId();
            boolean isAdmin = controller.isAdmin(currentUser.getId(), community.getId());
            boolean isMember = controller.isMember(currentUser.getId(), community.getId());

            if (isOwner || isAdmin) {
                jbutton settingsBtn = new jbutton("Settings", 18, 13);
                settingsBtn.setPreferredSize(new Dimension(100, 35));
                settingsBtn.addActionListener(e -> new CommunityAdminUI(currentUser, community.getId()));
                buttonPanel.add(settingsBtn);
            }

            if (isMember && !isOwner) {
                jbutton leaveBtn = new jbutton("Leave", 18, 13);
                leaveBtn.setPreferredSize(new Dimension(100, 35));
                leaveBtn.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Are you sure you want to leave this community?", "Leave Community", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (controller.leaveCommunity(currentUser.getId(), community.getId())) {
                            JOptionPane.showMessageDialog(this, "You left the community.");
                            refreshList(null);
                            rightPanel.removeAll();
                            rightPanel.repaint();
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to leave community.");
                        }
                    }
                });
                buttonPanel.add(leaveBtn);
            }

            topInfo.add(namePanel, BorderLayout.CENTER);
            topInfo.add(buttonPanel, BorderLayout.EAST);

            rightPanel.add(topInfo, BorderLayout.NORTH);
            
            JTextArea postArea = new JTextArea();
            postArea.setLineWrap(true);
            postArea.setWrapStyleWord(true);
            postArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            JScrollPane postScroll = new JScrollPane(postArea);
            postScroll.setPreferredSize(new Dimension(0, 100));

            JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
            inputPanel.setBackground(BACKGROUND);
            inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            inputPanel.add(postScroll, BorderLayout.CENTER);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
            btnPanel.setBackground(BACKGROUND);

            jbutton sendBtn = new jbutton("🡇 Post", 20, 16);
            sendBtn.setPreferredSize(new Dimension(140, 40));
            sendBtn.setBackground(new Color(0x28a745));
            sendBtn.setForeground(BACKGROUND);

            sendBtn.addActionListener(e -> {
                String content = postArea.getText().trim();
                if (content.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Cannot send empty post.");
                    return;
                }
                if (postController.sendPost(currentUser.getId(), community.getId(), content, null)) {
                    postArea.setText("");
                    displayCommunity(community);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to send post.");
                }
            });

            btnPanel.add(sendBtn);
            inputPanel.add(btnPanel, BorderLayout.SOUTH);

            JPanel messagesPanel = new JPanel();
            messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
            messagesPanel.setBackground(BACKGROUND);
            JScrollPane messageScroll = new JScrollPane(messagesPanel);
            messageScroll.setBorder(BorderFactory.createLineBorder(BUTTONS));

            ArrayList<Post> posts = postController.getPosts(community.getId());
            for (Post post : posts) {
                JPanel postPanel = new JPanel();
                postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
                postPanel.setBackground(BACKGROUND);
                postPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    BorderFactory.createLineBorder(SECONDARYHINT, 1)
                ));

                jlabel authorLabel = new jlabel(post.getAuthor().getFirstName() + ":", 16, TEXT, Font.BOLD);
                postPanel.add(authorLabel);
                postPanel.add(Box.createVerticalStrut(5));

                JTextArea contentArea = new JTextArea(post.getContent());
                contentArea.setLineWrap(true);
                contentArea.setWrapStyleWord(true);
                contentArea.setEditable(false);
                contentArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
                contentArea.setBackground(BACKGROUND);
                contentArea.setForeground(TEXT);
                postPanel.add(contentArea);

                // Like & Comment Row
                JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                actionPanel.setBackground(BACKGROUND);

                int likeCount = post.getLikes().size();
                boolean hasLiked = postController.hasUserLiked(currentUser.getId(), post.getId());

                jbutton likeBtn = new jbutton((hasLiked ? "♥" : "♡") + " " + likeCount, 16, 12);
                likeBtn.setPreferredSize(new Dimension(70, 30));
                likeBtn.addActionListener(e -> {
                    postController.toggleLike(currentUser.getId(), post.getId());
                    displayCommunity(community);  // Refresh
                });

                // Comment Field
                jtextfield commentField = new jtextfield("Write a comment...");
                commentField.setPreferredSize(new Dimension(200, 30));

                jbutton commentBtn = new jbutton("💬", 16, 12);
                commentBtn.setPreferredSize(new Dimension(50, 30));
                commentBtn.addActionListener(e -> {
                    String commentText = commentField.getText().trim();
                    if (!commentText.isEmpty()) {
                        postController.addComment(post.getId(), currentUser.getId(), commentText);
                        displayCommunity(community); // Refresh
                    }
                });

                actionPanel.add(likeBtn);
                actionPanel.add(commentField);
                actionPanel.add(commentBtn);

                postPanel.add(Box.createVerticalStrut(5));
                postPanel.add(actionPanel);

                // Display existing comments
                for (Comment c : post.getComments()) {
                    String cText = c.getAuthor().getFirstName() + ": " + c.getContent();
                    jlabel cLabel = new jlabel(cText, 14, SECONDARYHINT, Font.PLAIN);
                    cLabel.setBorder(BorderFactory.createEmptyBorder(2, 15, 2, 5));
                    postPanel.add(cLabel);
                }

                postPanel.add(Box.createVerticalStrut(10));
                messagesPanel.add(postPanel);
                messagesPanel.add(Box.createVerticalStrut(10));
            }


            rightPanel.add(messageScroll, BorderLayout.CENTER);
            rightPanel.add(inputPanel, BorderLayout.SOUTH);
        }

        rightPanel.revalidate();
        rightPanel.repaint();
    }
}
