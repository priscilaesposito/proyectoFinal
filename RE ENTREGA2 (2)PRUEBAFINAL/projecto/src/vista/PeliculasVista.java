package vista;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.List;
import model.Usuario;
import model.Pelicula;

/**
 * Vista de Peliculas - Responsable unicamente de mostrar la lista de peliculas.
 * No contiene logica de negocio.
 */
public class PeliculasVista extends JFrame {

    private Usuario usuario;
    private boolean esPrimerLogin;
    private List<Pelicula> peliculasActuales;

    private JPanel mainPanel;
    private JPanel contentPanel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton logoutButton;

    public PeliculasVista(Usuario usuario, List<Pelicula> peliculas, boolean esPrimerLogin) {
        this.usuario = usuario;
        this.peliculasActuales = peliculas;
        this.esPrimerLogin = esPrimerLogin;

        inicializarComponentes();
        mostrarPeliculas();
        establecerPropiedadesVentana();
    }

    private void inicializarComponentes() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel topBar = crearBarraSuperior();
        mainPanel.add(topBar, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel crearBarraSuperior() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setPreferredSize(new Dimension(0, 120));
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 40, 20, 40)));

        // Panel superior con usuario y botón cerrar sesión
        JPanel topRowPanel = new JPanel(new BorderLayout());
        topRowPanel.setBackground(Color.WHITE);

        // Usuario a la izquierda
        JLabel userLabel = new JLabel(usuario.getUsername());
        userLabel.setFont(new Font("Arial", Font.BOLD, 15));

        // Botón cerrar sesión a la derecha
        logoutButton = new JButton("Cerrar Sesión");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 13));
        logoutButton.setBackground(new Color(0, 122, 255));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setPreferredSize(new Dimension(140, 35));

        topRowPanel.add(userLabel, BorderLayout.WEST);
        topRowPanel.add(logoutButton, BorderLayout.EAST);

        // Panel inferior con título y buscador
        JPanel bottomRowPanel = new JPanel(new BorderLayout(30, 0));
        bottomRowPanel.setBackground(Color.WHITE);
        bottomRowPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Título a la izquierda
        JLabel titleLabel = new JLabel("Bienvenido a la Plataforma de Streaming");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        // Buscador a la derecha
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField(25);
        searchField.setPreferredSize(new Dimension(300, 38));
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        searchButton = new JButton("🔍");
        searchButton.setFont(new Font("Arial", Font.BOLD, 18));
        searchButton.setBackground(new Color(0, 122, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setPreferredSize(new Dimension(50, 38));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        bottomRowPanel.add(titleLabel, BorderLayout.WEST);
        bottomRowPanel.add(searchPanel, BorderLayout.EAST);

        // Panel principal con ambas filas
        JPanel mainTopPanel = new JPanel();
        mainTopPanel.setLayout(new BoxLayout(mainTopPanel, BoxLayout.Y_AXIS));
        mainTopPanel.setBackground(Color.WHITE);

        topRowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomRowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainTopPanel.add(topRowPanel);
        mainTopPanel.add(Box.createVerticalStrut(12));
        mainTopPanel.add(bottomRowPanel);

        topBar.add(mainTopPanel, BorderLayout.CENTER);

        return topBar;
    }

    public void mostrarPeliculas() {
        contentPanel.removeAll();

        JPanel peliculasPanel = new JPanel(new BorderLayout());
        peliculasPanel.setBackground(Color.WHITE);
        peliculasPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Mensaje de bienvenida
        String mensajeSeccion = "Seguro viste alguna de estas películas, haznos saber que te pareció dejando una reseña";
        JLabel mensajeLabel = new JLabel(mensajeSeccion);
        mensajeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mensajeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        peliculasPanel.add(mensajeLabel, BorderLayout.NORTH);

        // Panel para la tabla
        JPanel tablaPanel = new JPanel();
        tablaPanel.setLayout(new BoxLayout(tablaPanel, BoxLayout.Y_AXIS));
        tablaPanel.setBackground(Color.WHITE);

        // Crear encabezado
        JPanel headerPanel = crearEncabezadoTabla();
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tablaPanel.add(headerPanel);

        // Crear filas de películas
        for (Pelicula pelicula : peliculasActuales) {
            JPanel filaPanel = crearFilaPelicula(pelicula);
            filaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            tablaPanel.add(filaPanel);
        }

        JScrollPane scrollPane = new JScrollPane(tablaPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        peliculasPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(peliculasPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel crearEncabezadoTabla() {
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridy = 0;

        // Poster
        gbc.gridx = 0;
        gbc.weightx = 0.08;
        JLabel posterLabel = new JLabel("Poster");
        posterLabel.setFont(new Font("Arial", Font.BOLD, 13));
        headerPanel.add(posterLabel, gbc);

        // Título
        gbc.gridx = 1;
        gbc.weightx = 0.15;
        JLabel tituloLabel = new JLabel("Título");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 13));
        headerPanel.add(tituloLabel, gbc);

        // Género
        gbc.gridx = 2;
        gbc.weightx = 0.15;
        JLabel generoLabel = new JLabel("Genero");
        generoLabel.setFont(new Font("Arial", Font.BOLD, 13));
        headerPanel.add(generoLabel, gbc);

        // Resumen
        gbc.gridx = 3;
        gbc.weightx = 0.42;
        JLabel resumenLabel = new JLabel("Resumen");
        resumenLabel.setFont(new Font("Arial", Font.BOLD, 13));
        headerPanel.add(resumenLabel, gbc);

        // Botón
        gbc.gridx = 4;
        gbc.weightx = 0.2;
        JLabel accionLabel = new JLabel("");
        headerPanel.add(accionLabel, gbc);

        return headerPanel;
    }

    private JPanel crearFilaPelicula(Pelicula pelicula) {
        JPanel filaPanel = new JPanel(new GridBagLayout());
        filaPanel.setBackground(Color.WHITE);
        filaPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        filaPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridy = 0;

        // Poster
        gbc.gridx = 0;
        gbc.weightx = 0.08;
        JLabel posterLabel = new JLabel();
        posterLabel.setPreferredSize(new Dimension(60, 80));
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        posterLabel.setVerticalAlignment(SwingConstants.CENTER);
        posterLabel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        String posterUrl = pelicula.getPoster();
        if (posterUrl != null && !posterUrl.isEmpty() && !posterUrl.equals("N/A")) {
            try {
                ImageIcon icon = new ImageIcon(new URI(posterUrl).toURL());
                Image img = icon.getImage().getScaledInstance(60, 80, Image.SCALE_SMOOTH);
                posterLabel.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                posterLabel.setText("X");
                posterLabel.setFont(new Font("Arial", Font.BOLD, 20));
                posterLabel.setForeground(Color.LIGHT_GRAY);
            }
        } else {
            posterLabel.setText("X");
            posterLabel.setFont(new Font("Arial", Font.BOLD, 20));
            posterLabel.setForeground(Color.LIGHT_GRAY);
        }
        filaPanel.add(posterLabel, gbc);

        // Título
        gbc.gridx = 1;
        gbc.weightx = 0.15;
        JLabel tituloLabel = new JLabel(pelicula.getMetadatos().getTitulo());
        tituloLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        filaPanel.add(tituloLabel, gbc);

        // Género
        gbc.gridx = 2;
        gbc.weightx = 0.15;
        String genero = pelicula.getGeneros().isEmpty() ? "" : pelicula.getGeneros().get(0);
        JLabel generoLabel = new JLabel(genero);
        generoLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        filaPanel.add(generoLabel, gbc);

        // Resumen
        gbc.gridx = 3;
        gbc.weightx = 0.42;
        JLabel resumenLabel = new JLabel("Texto del resumen");
        resumenLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        filaPanel.add(resumenLabel, gbc);

        // Botón Calificar
        gbc.gridx = 4;
        gbc.weightx = 0.2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton calificarButton = new JButton("Calificar");
        calificarButton.setFont(new Font("Arial", Font.BOLD, 12));
        calificarButton.setBackground(new Color(0, 122, 255));
        calificarButton.setForeground(Color.WHITE);
        calificarButton.setOpaque(true);
        calificarButton.setBorderPainted(false);
        calificarButton.setFocusPainted(false);
        calificarButton.setPreferredSize(new Dimension(100, 30));
        calificarButton.putClientProperty("pelicula", pelicula);
        filaPanel.add(calificarButton, gbc);

        return filaPanel;
    }

    private void establecerPropiedadesVentana() {
        setTitle("Plataforma de Streaming - " + usuario.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1100, 700));
    }

    // Getters para el Controlador
    public Usuario getUsuario() {
        return usuario;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    public String getSearchTerm() {
        return searchField.getText().trim();
    }
}
