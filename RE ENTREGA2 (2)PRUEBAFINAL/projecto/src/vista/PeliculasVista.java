package vista;

import javax.swing.*;
import java.awt.*;
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
        topBar.setBackground(new Color(245, 245, 245));
        topBar.setPreferredSize(new Dimension(0, 80));
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        // Panel izquierdo: Datos del usuario
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userInfoPanel.setBackground(new Color(245, 245, 245));

        JLabel welcomeLabel = new JLabel("Bienvenido/a, " + usuario.getUsername());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userInfoPanel.add(welcomeLabel);

        JLabel emailLabel = new JLabel(" (" + usuario.getCorreo() + ")");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        emailLabel.setForeground(Color.GRAY);
        userInfoPanel.add(emailLabel);

        // Panel central: Buscador
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(new Color(245, 245, 245));

        searchField = new JTextField(25);
        searchField.setPreferredSize(new Dimension(300, 35));
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        searchButton = new JButton("🔍 Buscar");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setBackground(new Color(0, 122, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);

        searchPanel.add(new JLabel("Buscar pelicula: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Panel derecho: Boton de cerrar sesion
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(new Color(245, 245, 245));

        logoutButton = new JButton("Cerrar Sesion");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);

        logoutPanel.add(logoutButton);

        topBar.add(userInfoPanel, BorderLayout.WEST);
        topBar.add(searchPanel, BorderLayout.CENTER);
        topBar.add(logoutPanel, BorderLayout.EAST);

        return topBar;
    }

    public void mostrarPeliculas() {
        contentPanel.removeAll();

        JPanel peliculasPanel = new JPanel();
        peliculasPanel.setLayout(new BoxLayout(peliculasPanel, BoxLayout.Y_AXIS));
        peliculasPanel.setBackground(Color.WHITE);
        peliculasPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String tituloSeccion = esPrimerLogin ? "🌟 Top 10 Peliculas Mejor Rankeadas - ¡Calificalas!"
                : "🎬 Peliculas Recomendadas Para Ti";

        JLabel tituloLabel = new JLabel(tituloSeccion);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 20));
        tituloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        peliculasPanel.add(tituloLabel);
        peliculasPanel.add(Box.createVerticalStrut(20));

        for (Pelicula pelicula : peliculasActuales) {
            peliculasPanel.add(crearPanelPelicula(pelicula));
            peliculasPanel.add(Box.createVerticalStrut(15));
        }

        JScrollPane scrollPane = new JScrollPane(peliculasPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel crearPanelPelicula(Pelicula pelicula) {
        JPanel peliculaPanel = new JPanel(new BorderLayout(10, 10));
        peliculaPanel.setBackground(new Color(250, 250, 250));
        peliculaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        peliculaPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(250, 250, 250));

        JLabel tituloLabel = new JLabel(pelicula.getMetadatos().getTitulo());
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 16));
        tituloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel directorLabel = new JLabel("Director: " + pelicula.getMetadatos().getDirector());
        directorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        directorLabel.setForeground(Color.GRAY);
        directorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel generoLabel = new JLabel("Genero: " + String.join(", ", pelicula.getGeneros()));
        generoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        generoLabel.setForeground(Color.GRAY);
        generoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel anioLabel = new JLabel("Anio: " + pelicula.getAnio());
        anioLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        anioLabel.setForeground(Color.GRAY);
        anioLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel ratingLabel = new JLabel("⭐ Rating: " + String.format("%.1f", pelicula.getRatingPromedio()) + "/10");
        ratingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        ratingLabel.setForeground(new Color(255, 165, 0));
        ratingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(tituloLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(directorLabel);
        infoPanel.add(generoLabel);
        infoPanel.add(anioLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(ratingLabel);

        JPanel ratingPanel = crearPanelCalificacion(pelicula);

        peliculaPanel.add(infoPanel, BorderLayout.CENTER);
        peliculaPanel.add(ratingPanel, BorderLayout.EAST);

        // Guardar referencia a la pelicula en el panel para uso posterior
        peliculaPanel.putClientProperty("pelicula", pelicula);

        return peliculaPanel;
    }

    public JPanel crearPanelCalificacion(Pelicula pelicula) {
        JPanel ratingPanel = new JPanel();
        ratingPanel.setLayout(new BoxLayout(ratingPanel, BoxLayout.Y_AXIS));
        ratingPanel.setBackground(new Color(250, 250, 250));

        JLabel instruccion = new JLabel("Tu calificacion:");
        instruccion.setFont(new Font("Arial", Font.PLAIN, 12));
        instruccion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel starPanel = new JPanel(new FlowLayout());
        starPanel.setBackground(new Color(250, 250, 250));

        JComboBox<Integer> ratingCombo = new JComboBox<>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
        ratingCombo.setSelectedIndex(7); // Default 8/10

        JButton calificarButton = new JButton("Calificar");
        calificarButton.setFont(new Font("Arial", Font.BOLD, 12));
        calificarButton.setBackground(new Color(40, 167, 69));
        calificarButton.setForeground(Color.WHITE);
        calificarButton.setOpaque(true);
        calificarButton.setBorderPainted(false);
        calificarButton.setFocusPainted(false);

        // Guardar referencias para el controlador
        calificarButton.putClientProperty("pelicula", pelicula);
        calificarButton.putClientProperty("ratingCombo", ratingCombo);

        starPanel.add(ratingCombo);
        starPanel.add(calificarButton);

        ratingPanel.add(instruccion);
        ratingPanel.add(Box.createVerticalStrut(5));
        ratingPanel.add(starPanel);

        return ratingPanel;
    }

    private void establecerPropiedadesVentana() {
        setTitle("Plataforma de Streaming - " + usuario.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
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
