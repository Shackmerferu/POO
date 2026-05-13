package py_poo;

import py_poo.core.Videojuego;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LauncherFrame extends JFrame {

    private final List<Videojuego> games = new ArrayList<>();

    private JPanel NorthPanel;
    private JPanel centerPanel;
    private JPanel SouthPanel;

    public LauncherFrame() {

        initializeWindow();

        initializeComponents();

        refreshLauncher();

    }

    private void initializeWindow() {

        setTitle("Retro Launcher");

        setSize(1600, 900);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

    }

    private void initializeComponents() {

        createNorthPanel();

        createGamesPanel();

        createSouthPanel();

        add(centerPanel, BorderLayout.CENTER);

        add(NorthPanel, BorderLayout.NORTH);

        add(SouthPanel, BorderLayout.SOUTH);

    }

    private void createSouthPanel() {

        SouthPanel = new JPanel();

        SouthPanel.setPreferredSize(new Dimension(1600, 81));

    }

    private void createNorthPanel(){

        NorthPanel = new JPanel();

        NorthPanel.setPreferredSize(new Dimension(1600, 143));

        NorthPanel.setLayout(new BorderLayout());

    }

    private void createGamesPanel() {

        centerPanel = new JPanel();

        centerPanel.setLayout(new BorderLayout());

    }

    private void refreshLauncher() {

        createLibrary(games);

        createGameGrid(games);

    }

    private void createLibrary(List<Videojuego> games) {

        JPanel libraryContainer = new JPanel();

        libraryContainer.setLayout(
                new BoxLayout(
                        libraryContainer,
                        BoxLayout.Y_AXIS));

        // for (Videojuego game : games) {

        // JButton gameButton = new JButton(game.getName());

        // gameButton.setMaximumSize(
        // new Dimension(Integer.MAX_VALUE, 60)
        // );

        // gameButton.addActionListener(e -> {
        // showGameDetails(game);
        // });

        // libraryContainer.add(gameButton);

        // }

        JScrollPane scrollPane = new JScrollPane(libraryContainer);

        JLabel title = new JLabel("BIBLIOTECA");

        title.setHorizontalAlignment(SwingConstants.CENTER);

        title.setFont(new Font("Arial", Font.BOLD, 22));

        revalidate();

        repaint();

    }

    private void createGameGrid(List<Videojuego> games) {

        JPanel gridPanel = new JPanel();

        gridPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20));

        gridPanel.setLayout(
                new GridLayout(
                        0,
                        4,
                        20,
                        20));

        for (Videojuego game : games) {

            JPanel card = createGameCard(game);

            gridPanel.add(card);

        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);

        centerPanel.removeAll();

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        revalidate();

        repaint();

    }

    private JPanel createGameCard(Videojuego game) {

        JPanel card = new JPanel();

        card.setLayout(new BorderLayout());

        card.setPreferredSize(new Dimension(250, 300));

        card.setBorder(
                BorderFactory.createLineBorder(Color.GRAY));

        /*
         * RUTA PORTADA JUEGO:
         * reemplazar por ImageIcon
         */

        JLabel imageLabel = new JLabel("PORTADA");

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        imageLabel.setPreferredSize(
                new Dimension(250, 200));

        // JLabel title = new JLabel(game.getName());

        // title.setHorizontalAlignment(SwingConstants.CENTER);

        // title.setFont(
        // new Font("Arial", Font.BOLD, 18)
        // );

        JButton playButton = new JButton("JUGAR");

        // playButton.addActionListener(e -> {
        // game.start();
        // });

        JButton detailsButton = new JButton("DETALLES");

        detailsButton.addActionListener(e -> {
            showGameDetails(game);
        });

        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new GridLayout(1, 2));

        buttonPanel.add(playButton);

        buttonPanel.add(detailsButton);

        card.add(imageLabel, BorderLayout.NORTH);

        // card.add(title, BorderLayout.CENTER);

        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;

    }

    private void showGameDetails(Videojuego game) {

        JPanel container = new JPanel();

        container.setLayout(new BorderLayout(20, 20));

        /*
         * RUTA IMAGEN DETALLE:
         * colocar ImageIcon aquí
         */

        JLabel imageLabel = new JLabel("IMAGEN DEL JUEGO");

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        imageLabel.setPreferredSize(
                new Dimension(500, 300));

        // JLabel title = new JLabel(game.getName());

        // title.setFont(
        // new Font("Arial", Font.BOLD, 40)
        // );

        JTextArea description = new JTextArea();

        // description.setText(game.getDescription());

        description.setEditable(false);

        description.setLineWrap(true);

        description.setWrapStyleWord(true);

        JButton playButton = new JButton("JUGAR");

        playButton.setPreferredSize(
                new Dimension(200, 60));

        // playButton.addActionListener(e -> {
        // game.start();
        // });

        JButton backButton = new JButton("VOLVER");

        backButton.addActionListener(e -> {

            add(centerPanel, BorderLayout.CENTER);

            revalidate();

            repaint();

        });

        JPanel topPanel = new JPanel(
                new BorderLayout());

        topPanel.add(backButton, BorderLayout.WEST);

        JPanel contentPanel = new JPanel();

        contentPanel.setLayout(
                new BorderLayout(20, 20));

        // contentPanel.add(title, BorderLayout.NORTH);

        contentPanel.add(description, BorderLayout.CENTER);

        contentPanel.add(playButton, BorderLayout.SOUTH);

        container.add(imageLabel, BorderLayout.NORTH);

        container.add(contentPanel, BorderLayout.CENTER);



        remove(centerPanel);


        revalidate();

        repaint();

    }
}