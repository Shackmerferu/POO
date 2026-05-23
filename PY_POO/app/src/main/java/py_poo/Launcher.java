package py_poo;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import py_poo.core.Constantes;
import py_poo.core.GameLoop;
import py_poo.engine.VideoJuego;
import py_poo.loderunner.JuegoLodeRunner;
import py_poo.pong.JuegoPong;
import py_poo.spaceinvaders.JuegoSpaceInvaders;
import py_poo.utils.CargadorRecursos;


public class Launcher extends JFrame {

    static final Color
        C_BG = new Color(0x16,0x18,0x1F), C_SURFACE = new Color(0x20,0x23,0x2C),
        C_CARD = new Color(0x2B,0x2E,0x3A), C_CARD_HOV = new Color(0x35,0x39,0x48),
        C_CARD_SEL = new Color(0x3A,0x32,0x12),
        C_BORDER = new Color(255,255,255,28), C_BORDER_GOLD = new Color(255,200,60,110),
        C_TEXT = Color.WHITE, C_TEXT2 = new Color(255,255,255,160),
        C_TEXT3 = new Color(255,255,255,90),
        C_GOLD = new Color(255,200,60), C_GOLD_BG = new Color(255,200,60,38),
        C_RED = new Color(240,100,100), C_RED_BG = new Color(240,100,100,38);

    static final Font
        F_TITLE = new Font("Dialog",Font.BOLD,17),
        F_SEC   = new Font("Dialog",Font.BOLD,15),
        F_BODY  = new Font("Dialog",Font.PLAIN,13),
        F_CARD  = new Font("Dialog",Font.BOLD,14),
        F_SMALL = new Font("Dialog",Font.PLAIN,12),
        F_ICON  = new Font("Segoe UI Emoji",Font.PLAIN,40),
        F_BTN   = new Font("Dialog",Font.BOLD,13);

    private static class RoundBtn extends JLabel {
        boolean hov;
        final Color bg, bgHov, brd;

        RoundBtn(String text, boolean gold) {
            super(text, CENTER);
            bg = gold ? C_GOLD_BG : new Color(255,255,255,18);
            bgHov = gold ? new Color(255,200,60,70) : new Color(255,255,255,35);
            brd = gold ? C_BORDER_GOLD : C_BORDER;
            setFont(F_BTN);
            setForeground(gold ? C_GOLD : C_TEXT2);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hov = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hov = false; repaint(); }
            });
        }

        @Override protected void paintComponent(Graphics g0) {
            Graphics2D g = aa(g0);
            paintRnd(g, this, hov ? bgHov : bg, brd, 0.8f, 7);
            g.dispose();
        }
    }

    private static class TabLbl extends JLabel {
        boolean on;

        TabLbl(String text, boolean active) {
            super(text, CENTER);
            on = active;
            setFont(F_SEC);
            setForeground(C_TEXT);
            setPreferredSize(new Dimension(120, 34));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        void setActive(boolean v) { on = v; repaint(); }

        @Override protected void paintComponent(Graphics g0) {
            Graphics2D g = aa(g0);
            paintRnd(g, this, new Color(255,255,255,on?45:12),
                    on ? new Color(255,255,255,55) : null, 0.8f, 8);
            g.dispose();
        }
    }

    static class GameEntry {
        String name, icon, coverPath;
        boolean fullscreen, sound = true, music = true;
        String skin = "original", speed = "media";
        int winPoints = 15;
        BufferedImage cover;

        GameEntry(String name, String icon, String coverPath) {
            this.name = name; this.icon = icon; this.coverPath = coverPath;
        }
        void resetConfig() {
            fullscreen = false; sound = true; music = true;
            skin = "original"; speed = "media"; winPoints = 15;
        }
    }

    private final CargadorRecursos recursos = new CargadorRecursos();
    private final List<GameEntry> games = new ArrayList<>();
    private String player;
    private int focused;
    private JPanel carouselPanel, tabGamesPanel, storePanel, detailBar, bodyPanel;
    private JScrollPane carouselScroll;
    private CardLayout bodyLayout;
    private JLabel sessionBtn;
    private TabLbl tabGames, tabStore;
    private JLabel detailNameLbl;
    private String activeTab = "games";

    public Launcher() {
        super("Game Launcher");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(920, 570);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
        games.addAll(Arrays.asList(
            new GameEntry("Pong", "🏓", "imagenes/Portada Pong.png"),
            new GameEntry("Space Invaders", "👾", null),
            new GameEntry("Lode Runner", "🏃", null)
        ));
        JPanel root = darkPanel(new BorderLayout());
        root.add(buildTopBar(), BorderLayout.NORTH);
        root.add(buildBody(), BorderLayout.CENTER);
        root.add(buildBottomBar(), BorderLayout.SOUTH);
        setContentPane(root);
        rebuildCarousel();
        SwingUtilities.invokeLater(this::scrollToFocused);
    }

    private JPanel buildTopBar() {
        JPanel bar = darkPanel(new BorderLayout());
        bar.setBorder(new EmptyBorder(12, 20, 8, 20));

        JPanel left = darkPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JLabel logo = new JLabel("R");
        logo.setFont(new Font("Dialog", Font.BOLD, 15));
        logo.setForeground(C_BG);
        logo.setOpaque(true);
        logo.setBackground(new Color(0xE0, 0xE0, 0xE0));
        logo.setBorder(new EmptyBorder(4, 8, 4, 8));

        JLabel back = styledLabel("\u2190", F_SEC, C_TEXT3);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addMouseListener(hover(back, C_TEXT3, C_TEXT, e -> clearFocus()));

        left.add(logo); left.add(back);
        left.add(styledLabel("Library", F_TITLE, C_TEXT));

        JPanel right = darkPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        sessionBtn = new JLabel("Iniciar Sesion") {
            @Override protected void paintComponent(Graphics g0) {
                Graphics2D g = aa(g0);
                boolean lg = player != null;
                paintRnd(g, this, lg ? C_GOLD_BG : new Color(255,255,255,18),
                        lg ? C_BORDER_GOLD : C_BORDER, 0.8f, 7);
                g.dispose();
            }
        };
        sessionBtn.setFont(F_BODY);
        sessionBtn.setForeground(C_TEXT2);
        sessionBtn.setPreferredSize(new Dimension(130, 30));
        sessionBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sessionBtn.setHorizontalAlignment(SwingConstants.CENTER);
        sessionBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { openSession(); }
        });

        JLabel gear = styledLabel("\u2699", new Font("Segoe UI Emoji", Font.PLAIN, 20), C_TEXT3);
        gear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gear.addMouseListener(hover(gear, C_TEXT3, C_TEXT, e -> openGlobalSettings()));

        right.add(sessionBtn); right.add(gear);
        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);

        JPanel wrapper = darkPanel(new BorderLayout());
        wrapper.add(bar, BorderLayout.CENTER);
        wrapper.add(hline(), BorderLayout.SOUTH);
        return wrapper;
    }

    private JPanel buildBody() {
        JPanel body = darkPanel(new BorderLayout());

        JPanel tabRow = darkPanel(new FlowLayout(FlowLayout.LEFT, 6, 10));
        tabRow.setBorder(new EmptyBorder(0, 16, 0, 0));
        tabGames = new TabLbl("My Games", true);
        tabStore = new TabLbl("Store", false);
        tabGames.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { switchTab("games"); }
        });
        tabStore.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { switchTab("store"); }
        });
        tabRow.add(tabGames); tabRow.add(tabStore);
        body.add(tabRow, BorderLayout.NORTH);

        bodyLayout = new CardLayout();
        bodyPanel = darkPanel(bodyLayout);

        tabGamesPanel = darkPanel(new BorderLayout());
        carouselPanel = darkPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        carouselScroll = new JScrollPane(carouselPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        carouselScroll.setOpaque(false);
        carouselScroll.getViewport().setOpaque(false);
        carouselScroll.setBorder(BorderFactory.createEmptyBorder());
        carouselScroll.getHorizontalScrollBar().setUnitIncrement(40);
        styleScrollBar(carouselScroll);
        carouselScroll.addMouseWheelListener(e -> {
            JScrollBar hsb = carouselScroll.getHorizontalScrollBar();
            hsb.setValue(hsb.getValue() + e.getUnitsToScroll() * 25);
        });
        tabGamesPanel.add(carouselScroll, BorderLayout.CENTER);
        detailBar = buildDetailBar();
        tabGamesPanel.add(detailBar, BorderLayout.SOUTH);

        storePanel = darkPanel(new BorderLayout());
        JLabel storeLbl = styledLabel("Store pr\u00F3ximamente disponible", F_BODY, C_TEXT3);
        storeLbl.setHorizontalAlignment(SwingConstants.CENTER);
        storePanel.add(storeLbl, BorderLayout.CENTER);

        bodyPanel.add(tabGamesPanel, "games");
        bodyPanel.add(storePanel, "store");
        body.add(bodyPanel, BorderLayout.CENTER);
        return body;
    }

    private void rebuildCarousel() {
        carouselPanel.removeAll();
        for (int i = 0; i < games.size(); i++)
            carouselPanel.add(buildCard(games.get(i), i == focused, i));
        carouselPanel.revalidate();
        carouselPanel.repaint();
        updateDetailBar();
    }

    private JPanel buildCard(GameEntry g, boolean sel, int idx) {
        int W = 140, H = 180, ARC = 14;
        boolean[] hov = {false};

        if (g.cover == null && g.coverPath != null) {
            g.cover = recursos.cargarImagen(g.coverPath);
        }

        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g0) {
                Graphics2D gr = aa(g0);
                Color bg = sel ? C_CARD_SEL : (hov[0] ? C_CARD_HOV : C_CARD);
                gr.setColor(bg);
                gr.fill(round(0, 0, W, H, ARC));
                if (sel) {
                    gr.setColor(C_GOLD); gr.setStroke(new BasicStroke(2f));
                } else {
                    gr.setColor(C_BORDER); gr.setStroke(new BasicStroke(0.8f));
                }
                gr.draw(round(1, 1, W-2, H-2, ARC));

                if (g.cover != null) {
                    int pad = 8;
                    int maxW = W - pad * 2;
                    int maxH = H - pad * 2;
                    double scale = Math.min((double) maxW / g.cover.getWidth(), (double) maxH / g.cover.getHeight());
                    int drawW = (int) (g.cover.getWidth() * scale);
                    int drawH = (int) (g.cover.getHeight() * scale);
                    int drawX = (W - drawW) / 2;
                    int drawY = (H - drawH) / 2;
                    gr.drawImage(g.cover, drawX, drawY, drawW, drawH, null);
                } else {
                    gr.setFont(F_ICON);
                    FontMetrics fm = gr.getFontMetrics();
                    gr.setColor(new Color(255,255,255, sel ? 230 : 170));
                    gr.drawString(g.icon, (W - fm.stringWidth(g.icon)) / 2, H/2 + fm.getAscent()/2 - 6);
                }
                gr.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(W, H); }
            @Override public Dimension getMinimumSize() { return getPreferredSize(); }
        };
        card.setOpaque(false);
        card.setLayout(null);

        JLabel nameLbl = styledLabel(g.name, F_CARD, sel ? C_GOLD : C_TEXT);
        nameLbl.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel wrapper = darkPanel(new BorderLayout(0, 8));
        wrapper.add(card, BorderLayout.CENTER);
        wrapper.add(nameLbl, BorderLayout.SOUTH);
        wrapper.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        wrapper.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hov[0] = true; card.repaint(); }
            @Override public void mouseExited(MouseEvent e) { hov[0] = false; card.repaint(); }
            @Override public void mouseClicked(MouseEvent e) {
                focused = idx; rebuildCarousel(); scrollToFocused();
            }
        });
        return wrapper;
    }

    private void scrollToFocused() {
        if (games.isEmpty()) return;
        SwingUtilities.invokeLater(() -> {
            JScrollBar hsb = carouselScroll.getHorizontalScrollBar();
            int total = hsb.getMaximum() - hsb.getMinimum();
            int visible = hsb.getVisibleAmount();
            double ratio = games.size() <= 1 ? 0.5 : (double) focused / (games.size() - 1);
            int target = (int) (ratio * (total - visible));
            int current = hsb.getValue();
            int rawStep = (target - current) / 6;
            int step = rawStep == 0 ? (target >= current ? 1 : -1) : rawStep;
            javax.swing.Timer t = new javax.swing.Timer(16, null);
            t.addActionListener(ae -> {
                int now = hsb.getValue();
                int dist = target - now;
                if (Math.abs(dist) <= Math.abs(step)) {
                    hsb.setValue(target); t.stop();
                } else {
                    hsb.setValue(now + step);
                }
            });
            t.start();
        });
    }

    private JPanel buildDetailBar() {
        JPanel bar = darkPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, C_BORDER));
        detailNameLbl = styledLabel("", F_SMALL, C_TEXT3);

        RoundBtn configBtn = buildActionBtn("\u2699  Config", false);
        configBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { openGameConfig(); }
        });

        RoundBtn launchBtn = buildActionBtn("\u25B6  Jugar", true);
        launchBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { launchGame(); }
        });

        bar.add(detailNameLbl);
        bar.add(Box.createHorizontalStrut(4));
        bar.add(configBtn);
        bar.add(launchBtn);
        bar.setVisible(!games.isEmpty());
        return bar;
    }

    private void updateDetailBar() {
        if (games.isEmpty()) {
            detailBar.setVisible(false);
            return;
        }
        detailNameLbl.setText(games.get(focused).name);
        detailBar.setVisible(true);
        detailBar.revalidate();
        detailBar.repaint();
    }

    private JPanel buildBottomBar() {
        JPanel bar = darkPanel(new BorderLayout());
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, C_BORDER),
                new EmptyBorder(8, 20, 12, 20)));

        RoundBtn addBtn = buildActionBtn("\u2295  Agregar un Juego", false);
        RoundBtn removeBtn = buildActionBtn("\u2296  Eliminar un Juego", true);
        removeBtn.setForeground(C_RED);

        addBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { openAddGame(); }
        });
        removeBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { removeSelected(); }
        });

        bar.add(addBtn, BorderLayout.WEST);
        bar.add(removeBtn, BorderLayout.EAST);
        return bar;
    }

    // ─── Diálogos ──────────────────────────────────────────
    private void openSession() {
        JDialog dlg = dialog("Sesión");
        JPanel p = darkPanel(new BorderLayout(0, 14));
        p.setBorder(new EmptyBorder(24, 28, 20, 28));
        p.add(styledLabel(player != null ? "Sesión activa" : "Iniciar Sesión",
                F_TITLE, C_TEXT), BorderLayout.NORTH);

        JPanel center = darkPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        if (player != null) {
            JLabel info = styledLabel("Jugador: " + player, F_BODY, C_GOLD);
            info.setAlignmentX(LEFT_ALIGNMENT);
            center.add(info);
        } else {
            JLabel hint = styledLabel("Ingresá tu nombre para guardar puntajes.", F_BODY, C_TEXT3);
            hint.setAlignmentX(LEFT_ALIGNMENT);
            center.add(hint);
            center.add(Box.createVerticalStrut(10));

            JTextField field = darkField();
            field.setAlignmentX(LEFT_ALIGNMENT);
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
            center.add(field);

            JPanel acts = darkPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            acts.add(buildDialogBtn("Cancelar", false, e -> dlg.dispose()));
            acts.add(buildDialogBtn("Ingresar", true, e -> {
                String n = field.getText().trim();
                if (n.isEmpty()) {
                    field.setBorder(BorderFactory.createLineBorder(C_RED)); return;
                }
                player = n;
                updateSessionLabel();
                dlg.dispose();
            }));
            field.addActionListener(e -> {
                String n = field.getText().trim();
                if (!n.isEmpty()) { player = n; updateSessionLabel(); dlg.dispose(); }
            });

            p.add(center, BorderLayout.CENTER);
            p.add(acts, BorderLayout.SOUTH);
            dlg.setContentPane(p);
            dlg.pack(); dlg.setMinimumSize(new Dimension(340, 0));
            dlg.setLocationRelativeTo(this);
            dlg.setVisible(true);
            return;
        }

        JPanel acts = darkPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        RoundBtn logoutBtn = buildDialogBtn("Cerrar sesión", false, null);
        logoutBtn.setForeground(C_RED);
        logoutBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                player = null; updateSessionLabel(); dlg.dispose();
            }
        });
        acts.add(logoutBtn);
        acts.add(buildDialogBtn("Cancelar", false, e -> dlg.dispose()));

        p.add(center, BorderLayout.CENTER);
        p.add(acts, BorderLayout.SOUTH);
        dlg.setContentPane(p);
        dlg.pack(); dlg.setMinimumSize(new Dimension(320, 0));
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void updateSessionLabel() {
        if (player != null) {
            sessionBtn.setText(player);
            sessionBtn.setForeground(C_GOLD);
        } else {
            sessionBtn.setText("Iniciar Sesion");
            sessionBtn.setForeground(C_TEXT2);
        }
        sessionBtn.repaint();
    }

    private void openAddGame() {
        JDialog dlg = dialog("Agregar Juego");
        JPanel p = darkPanel(new BorderLayout(0, 14));
        p.setBorder(new EmptyBorder(24, 28, 20, 28));
        p.add(styledLabel("Agregar Juego", F_TITLE, C_TEXT), BorderLayout.NORTH);

        JPanel center = darkPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel hint = styledLabel("Seleccioná un juego o escribí un nombre personalizado.",
                F_BODY, C_TEXT3);
        hint.setAlignmentX(LEFT_ALIGNMENT);
        center.add(hint);
        center.add(Box.createVerticalStrut(10));

        List<String> builtin = List.of("Pong", "Space Invaders", "Lode Runner");
        List<String> current = games.stream().map(g -> g.name).toList();
        List<String> avail = new ArrayList<>();
        avail.add("— Seleccionar —");
        for (String b : builtin) if (!current.contains(b)) avail.add(b);
        avail.add("Otro (nombre personalizado)");

        JComboBox<String> combo = darkCombo(avail.toArray(new String[0]));
        combo.setAlignmentX(LEFT_ALIGNMENT);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        center.add(combo);
        center.add(Box.createVerticalStrut(8));

        JTextField customField = darkField();
        customField.setAlignmentX(LEFT_ALIGNMENT);
        customField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        customField.setVisible(false);
        center.add(customField);

        combo.addActionListener(e -> {
            customField.setVisible("Otro (nombre personalizado)".equals(combo.getSelectedItem()));
            dlg.pack();
        });

        JPanel acts = darkPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acts.add(buildDialogBtn("Cancelar", false, e -> dlg.dispose()));
        acts.add(buildDialogBtn("Agregar", true, e -> {
            String sel = (String) combo.getSelectedItem();
            String name;
            if ("Otro (nombre personalizado)".equals(sel)) {
                name = customField.getText().trim();
            } else if (sel == null || sel.startsWith("—")) {
                return;
            } else {
                name = sel;
            }
            if (name.isEmpty()) return;
            if (games.stream().anyMatch(g -> g.name.equals(name))) {
                JOptionPane.showMessageDialog(dlg, "Ya está en la librería.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String icon = switch (name) {
                case "Pong" -> "🏓";
                case "Space Invaders" -> "👾";
                case "Lode Runner" -> "🏃";
                default -> "🎮";
            };
            String coverPath = switch (name) {
                case "Pong" -> "imagenes/Portada Pong.png";
                default -> null;
            };
            games.add(new GameEntry(name, icon, coverPath));
            focused = games.size() - 1;
            rebuildCarousel();
            scrollToFocused();
            dlg.dispose();
        }));

        p.add(center, BorderLayout.CENTER);
        p.add(acts, BorderLayout.SOUTH);
        dlg.setContentPane(p);
        dlg.pack(); dlg.setMinimumSize(new Dimension(360, 0));
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void removeSelected() {
        if (games.isEmpty()) return;
        GameEntry g = games.get(focused);

        JDialog dlg = dialog("Eliminar Juego");
        JPanel p = darkPanel(new BorderLayout(0, 14));
        p.setBorder(new EmptyBorder(24, 28, 20, 28));
        p.add(styledLabel("Eliminar Juego", F_TITLE, C_TEXT), BorderLayout.NORTH);

        JLabel msg = styledLabel("¿Eliminar \"" + g.name + "\" de tu librería?",
                F_BODY, C_TEXT2);
        p.add(msg, BorderLayout.CENTER);

        JPanel acts = darkPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acts.add(buildDialogBtn("Cancelar", false, e -> dlg.dispose()));
        RoundBtn delBtn = buildDialogBtn("Eliminar", false, null);
        delBtn.setForeground(C_RED);
        delBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                games.remove(focused);
                if (focused >= games.size() && focused > 0) focused--;
                rebuildCarousel();
                scrollToFocused();
                dlg.dispose();
            }
        });
        acts.add(delBtn);

        p.add(acts, BorderLayout.SOUTH);
        dlg.setContentPane(p);
        dlg.pack(); dlg.setMinimumSize(new Dimension(320, 0));
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void openGameConfig() {
        if (games.isEmpty()) return;
        GameEntry g = games.get(focused);

        JDialog dlg = dialog("Config — " + g.name);
        JPanel p = darkPanel(new BorderLayout(0, 16));
        p.setBorder(new EmptyBorder(24, 28, 20, 28));
        p.add(styledLabel("Config — " + g.name, F_TITLE, C_TEXT), BorderLayout.NORTH);

        JPanel form = darkPanel(new GridLayout(0, 2, 12, 12));
        JCheckBox cbFull  = darkCheck("", g.fullscreen);
        JCheckBox cbSound = darkCheck("", g.sound);
        JCheckBox cbMusic = darkCheck("", g.music);
        JComboBox<String> combSkin = darkCombo(
                new String[]{"original", "retro", "modern"});
        combSkin.setSelectedItem(g.skin);

        addFormRow(form, "Pantalla completa", cbFull);
        addFormRow(form, "Sonido", cbSound);
        addFormRow(form, "Música", cbMusic);
        addFormRow(form, "Skin", combSkin);

        JComboBox<String> combSpeed  = null;
        JComboBox<String> combPoints = null;

        if ("Space Invaders".equals(g.name)) {
            combSpeed = darkCombo(new String[]{"lenta", "media", "rapida"});
            combSpeed.setSelectedItem(g.speed);
            addFormRow(form, "Velocidad invasores", combSpeed);
        }
        if ("Pong".equals(g.name)) {
            combPoints = darkCombo(new String[]{"11", "15", "21"});
            combPoints.setSelectedItem(String.valueOf(g.winPoints));
            addFormRow(form, "Puntos para ganar", combPoints);
        }

        p.add(form, BorderLayout.CENTER);

        JComboBox<String> fSpeed  = combSpeed;
        JComboBox<String> fPoints = combPoints;

        JPanel acts = darkPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        RoundBtn resetBtn = buildDialogBtn("Reset", false, null);
        resetBtn.setForeground(C_RED);
        resetBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                g.resetConfig(); dlg.dispose();
                openGameConfig();
            }
        });
        acts.add(resetBtn);
        acts.add(buildDialogBtn("Cancelar", false, e -> dlg.dispose()));
        acts.add(buildDialogBtn("Guardar", true, e -> {
            g.fullscreen = cbFull.isSelected();
            g.sound      = cbSound.isSelected();
            g.music      = cbMusic.isSelected();
            g.skin       = (String) combSkin.getSelectedItem();
            if (fSpeed  != null) g.speed     = (String) fSpeed.getSelectedItem();
            if (fPoints != null) g.winPoints = Integer.parseInt((String) fPoints.getSelectedItem());
            dlg.dispose();
        }));

        p.add(acts, BorderLayout.SOUTH);
        dlg.setContentPane(p);
        dlg.pack(); dlg.setMinimumSize(new Dimension(360, 0));
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void openGlobalSettings() {
        JDialog dlg = dialog("Configuración Global");
        JPanel p = darkPanel(new BorderLayout(0, 16));
        p.setBorder(new EmptyBorder(24, 28, 20, 28));
        p.add(styledLabel("Configuración Global", F_TITLE, C_TEXT), BorderLayout.NORTH);

        JPanel form = darkPanel(new GridLayout(0, 2, 12, 12));
        addFormRow(form, "Pantalla completa", darkCheck("", false));
        addFormRow(form, "Sonido general",    darkCheck("", true));
        addFormRow(form, "Música de fondo",   darkCheck("", true));
        p.add(form, BorderLayout.CENTER);

        JPanel acts = darkPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acts.add(buildDialogBtn("Cancelar", false, e -> dlg.dispose()));
        acts.add(buildDialogBtn("Guardar",  true,  e -> dlg.dispose()));
        p.add(acts, BorderLayout.SOUTH);

        dlg.setContentPane(p);
        dlg.pack(); dlg.setMinimumSize(new Dimension(320, 0));
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void launchGame() {
        if (games.isEmpty()) return;
        if (player == null) {
            JOptionPane.showMessageDialog(this,
                    "Iniciá sesión antes de jugar.", "Sin sesión",
                    JOptionPane.WARNING_MESSAGE);
            openSession();
            return;
        }
        GameEntry g = games.get(focused);
        VideoJuego vj = crearJuego(g.name);
        if (vj == null) {
            JOptionPane.showMessageDialog(this,
                    "Juego no implementado: " + g.name, "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        vj.setNombreJugador(player);
        Launcher.this.setVisible(false);


        GameLoop gl = new GameLoop(g.name, Constantes.WIDTH, Constantes.HEIGHT);
        gl.setVideoJuego(vj);

        new Thread(() -> {
            try {
                
                gl.run(Constantes.FPS); 
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                SwingUtilities.invokeLater(() -> Launcher.this.setVisible(true));
            }
        }).start();
        
        
        /*
        GameLoop gl = new GameLoop(g.name, Constantes.WIDTH, Constantes.HEIGHT);
        gl.setVideoJuego(vj);

        new Thread(() -> {
            gl.run(Constantes.FPS);
            SwingUtilities.invokeLater(() -> Launcher.this.setVisible(true));
        }).start();*/
    }

    private VideoJuego crearJuego(String nombre) {
        return switch (nombre) {
            case "Pong" -> new JuegoPong();
            case "Space Invaders" -> new JuegoSpaceInvaders();
            case "Lode Runner" -> new JuegoLodeRunner();
            default -> null;
        };
    }

    // ─── Tabs ──────────────────────────────────────────────
    private void switchTab(String tab) {
        activeTab = tab;
        tabGames.setActive("games".equals(tab));
        tabStore.setActive("store".equals(tab));
        bodyLayout.show(bodyPanel, tab);
    }

    private void clearFocus() {
        focused = 0;
        rebuildCarousel();
        scrollToFocused();
    }

    // ─── Helpers ───────────────────────────────────────────
    private RoundBtn buildActionBtn(String text, boolean gold) {
        RoundBtn btn = new RoundBtn(text, gold);
        FontMetrics fm = getFontMetrics(F_BTN);
        btn.setPreferredSize(new Dimension(fm.stringWidth(text) + 28, 30));
        return btn;
    }

    private RoundBtn buildDialogBtn(String text, boolean gold, ActionListener al) {
        RoundBtn btn = buildActionBtn(text, gold);
        if (al != null) btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { al.actionPerformed(null); }
        });
        return btn;
    }

    private JPanel darkPanel(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setOpaque(true);
        p.setBackground(C_BG);
        return p;
    }

    private JPanel darkPanel() { return darkPanel(new FlowLayout()); }

    private JLabel styledLabel(String text, Font f, Color fg) {
        JLabel l = new JLabel(text);
        l.setFont(f); l.setForeground(fg); l.setOpaque(false);
        return l;
    }

    private JTextField darkField() {
        JTextField f = new JTextField();
        f.setBackground(C_CARD);
        f.setForeground(C_TEXT);
        f.setCaretColor(C_GOLD);
        f.setFont(F_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255,255,255,60)),
                new EmptyBorder(6, 10, 6, 10)));
        return f;
    }

    private JComboBox<String> darkCombo(String[] items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setBackground(C_CARD);
        c.setForeground(C_TEXT);
        c.setFont(F_BODY);
        return c;
    }

    private JCheckBox darkCheck(String label, boolean val) {
        JCheckBox cb = new JCheckBox(label, val);
        cb.setOpaque(false);
        cb.setForeground(C_TEXT);
        cb.setFont(F_BODY);
        return cb;
    }

    private void addFormRow(JPanel p, String label, JComponent comp) {
        p.add(styledLabel(label, F_BODY, C_TEXT2));
        p.add(comp);
    }

    private JDialog dialog(String title) {
        JDialog d = new JDialog(this, title, true);
        d.getContentPane().setBackground(C_SURFACE);
        return d;
    }

    private JSeparator hline() {
        JSeparator sep = new JSeparator();
        sep.setForeground(C_BORDER);
        sep.setBackground(C_BORDER);
        return sep;
    }

    private void styleScrollBar(JScrollPane sp) {
        JScrollBar hsb = sp.getHorizontalScrollBar();
        hsb.setPreferredSize(new Dimension(0, 8));
        hsb.setOpaque(true);
        hsb.setBackground(C_BG);
        hsb.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = new Color(255,255,255,40);
                thumbDarkShadowColor = new Color(255,255,255,60);
                thumbHighlightColor = new Color(255,255,255,60);
                thumbLightShadowColor = new Color(255,255,255,60);
                trackColor = C_BG;
            }
            @Override protected JButton createDecreaseButton(int o) { return zero(); }
            @Override protected JButton createIncreaseButton(int o) { return zero(); }
            private JButton zero() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
            @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = aa(g);
                g2.setColor(thumbColor);
                g2.fillRoundRect(r.x+2, r.y+2, r.width-4, r.height-4, 4, 4);
                g2.dispose();
            }
        });
    }

    private static Graphics2D aa(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return g2;
    }

    private static RoundRectangle2D.Float round(int x, int y, int w, int h, int arc) {
        return new RoundRectangle2D.Float(x, y, w, h, arc, arc);
    }

    private static void paintRnd(Graphics2D g, JComponent c, Color bg,
                                  Color brd, float stroke, int arc) {
        int w = c.getWidth(), h = c.getHeight();
        g.setColor(bg);
        g.fill(round(0, 0, w, h, arc));
        if (brd != null) {
            g.setColor(brd);
            g.setStroke(new BasicStroke(stroke));
            g.draw(round(1, 1, w-2, h-2, arc));
        }
        g.setFont(c.getFont());
        g.setColor(c.getForeground());
        FontMetrics fm = g.getFontMetrics();
        String t = ((JLabel) c).getText();
        g.drawString(t, (w - fm.stringWidth(t)) / 2,
                       (h - fm.getHeight()) / 2 + fm.getAscent());
    }

    private MouseAdapter hover(JLabel lbl, Color normal, Color hot,
                                Consumer<MouseEvent> onClick) {
        return new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { lbl.setForeground(hot); }
            @Override public void mouseExited(MouseEvent e) { lbl.setForeground(normal); }
            @Override public void mouseClicked(MouseEvent e) {
                if (onClick != null) onClick.accept(e);
            }
        };
    }
}
