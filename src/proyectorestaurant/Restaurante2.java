/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package proyectorestaurant;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Statement;
import java.util.function.Consumer;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Element;
import java.io.File;
import java.io.FileOutputStream;
public class Restaurante2 extends javax.swing.JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContraseña;
    private JLabel lblMensaje;
    private final String url = "jdbc:postgresql://localhost:5432/restaurante_bd"; // Cambia por tu base de datos
    private final String usuario = "postgres"; // Cambia por tu usuario
    private final String contraseña1 = "12345"; // Cambia por tu contraseña
    private Connection conexion;
    private JTextField txtBuscar = new JTextField(20);
    private DefaultTableModel modelProductos = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Nombre", "Precio", "Acción"});
    private DefaultTableModel modelCarrito = new DefaultTableModel(new Object[][]{}, new String[]{"FFF", "Nombre", "Cantidad", "Total"});
    private JTable tablaProductos = new JTable(modelProductos);  
    private JTable tablaCarrito = new JTable(modelCarrito);
    private double total = 0.0;
     public Restaurante2() {
        // Configuración de la ventana principal
        setTitle("Sistema Restaurante");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Configuración del panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(34, 40, 49)); // Color de fondo oscuro
        setContentPane(panel);

        // Título de la ventana
        JLabel lblTitulo = new JLabel("Inicio de Sesión");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(30));  // Espaciado

        // Panel para campos de usuario y contraseña
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(2, 2, 10, 20)); // Ajuste del espaciado en la cuadrícula
        loginPanel.setBackground(new Color(34, 40, 49)); // Fondo del panel de login
        loginPanel.setMaximumSize(new Dimension(400, 120));

        // Etiquetas y campos de texto
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsuario.setForeground(Color.WHITE);

        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblContraseña.setForeground(Color.WHITE);

        txtUsuario = new JTextField(20);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setPreferredSize(new Dimension(200, 30));
        txtUsuario.setBackground(new Color(54, 57, 63));
        txtUsuario.setForeground(Color.WHITE);
        txtUsuario.setCaretColor(Color.WHITE);
        txtUsuario.setBorder(BorderFactory.createLineBorder(new Color(72, 84, 96), 1));

        txtContraseña = new JPasswordField(20);
        txtContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContraseña.setPreferredSize(new Dimension(200, 30));
        txtContraseña.setBackground(new Color(54, 57, 63));
        txtContraseña.setForeground(Color.WHITE);
        txtContraseña.setCaretColor(Color.WHITE);
        txtContraseña.setBorder(BorderFactory.createLineBorder(new Color(72, 84, 96), 1));

        // Añadir componentes al panel de login
        loginPanel.add(lblUsuario);
        loginPanel.add(txtUsuario);
        loginPanel.add(lblContraseña);
        loginPanel.add(txtContraseña);

        panel.add(loginPanel);

        // Espaciado adicional entre los campos y el botón
        panel.add(Box.createVerticalStrut(20)); // Espaciado de 20 pixeles

        // Botón de inicio de sesión
        JButton btnLogin = new JButton("Ingresar");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(0, 123, 255)); // Azul brillante
        btnLogin.setBorder(BorderFactory.createEmptyBorder());
        btnLogin.setPreferredSize(new Dimension(100, 40));
        btnLogin.setFocusPainted(false);

        panel.add(btnLogin);
        panel.add(Box.createVerticalStrut(20)); // Espaciado entre el botón y el mensaje

        // Etiqueta de mensaje (para mostrar errores o éxito)
        lblMensaje = new JLabel("");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMensaje.setForeground(Color.RED);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblMensaje);

        // Evento para el botón de login
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUsuario();
            }
        });
    }


    private void loginUsuario() {
        String nombreUsuario = txtUsuario.getText();
        String contraseña = new String(txtContraseña.getPassword());

        try {
            // Conectar a la base de datos
            Connection conexion = DriverManager.getConnection(url, usuario, contraseña1);
            String sql = "SELECT tipo FROM usuario WHERE nombre_usuario = ? AND contraseña = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, contraseña);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String tipoUsuario = rs.getString("tipo");
                if ("admin".equals(tipoUsuario)) {
                    // Si es administrador, abre la ventana de administración
                    abrirVentanaAdmin();
                } 
                else if ("cajero".equals(tipoUsuario)) {
                    // Si es administrador, abre la ventana de administración
                    abrirVentanaCajero();
                }
                else {
                    lblMensaje.setText("Bienvenido, " + nombreUsuario);
                    lblMensaje.setForeground(new Color(40, 167, 69)); // Verde de éxito
                }
            } else {
                lblMensaje.setText("Usuario o contraseña incorrectos.");
                lblMensaje.setForeground(Color.RED);
            }

            rs.close();
            stmt.close();
            conexion.close();
        } catch (SQLException ex) {
            lblMensaje.setText("Error al conectar con la base de datos.");
            lblMensaje.setForeground(Color.RED);
            ex.printStackTrace();
        }
    }

   
  private void abrirVentanaAdmin() {
        // Cierra la ventana de login
        dispose();

        // Crea una nueva ventana de administración para el administrador
        JFrame ventanaAdmin = new JFrame("Ventana de Administración");
        ventanaAdmin.setSize(800, 600);
        ventanaAdmin.setLocationRelativeTo(null);

        // Panel principal de la ventana de administración
        JPanel panelAdmin = new JPanel();
        panelAdmin.setLayout(new BorderLayout());
        panelAdmin.setBackground(new Color(248, 249, 250)); // Fondo claro
        ventanaAdmin.setContentPane(panelAdmin);

        // Barra lateral con botones
        JPanel barraLateral = new JPanel();
        barraLateral.setLayout(new BoxLayout(barraLateral, BoxLayout.Y_AXIS));
        barraLateral.setBackground(new Color(23, 162, 184)); // Azul verdoso

        // Botones de opciones
        JButton btnGestionarUsuarios = new JButton("Gestionar Usuarios");
        JButton btnGestionarProductos = new JButton("Gestionar Productos");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");

        // Personalización de botones
        configurarBoton(btnGestionarUsuarios);
        configurarBoton(btnGestionarProductos);
        configurarBoton(btnCerrarSesion);

        // Añadir botones a la barra lateral
        barraLateral.add(btnGestionarUsuarios);
        barraLateral.add(btnGestionarProductos);
        barraLateral.add(btnCerrarSesion);

        // Panel central para mostrar información (por ejemplo, una tabla)
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(new Color(248, 249, 250)); // Fondo claro

        // Añadir barra lateral y panel central a la ventana de administración
        ventanaAdmin.add(barraLateral, BorderLayout.WEST);
        ventanaAdmin.add(panelCentral, BorderLayout.CENTER);

        // Acción de botón "Gestionar Usuarios"
        btnGestionarUsuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarTablaUsuarios(panelCentral);
            }
        });

        // Acción de botón "Gestionar Productos"
        btnGestionarProductos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarTablaProductos(panelCentral);
            }
        });

        // Acción de botón "Cerrar Sesión"
        btnCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cerrar sesión y regresar al login
                ventanaAdmin.dispose();
                new Restaurante2().setVisible(true);
            }
        });

        // Mostrar la ventana de administración
        ventanaAdmin.setVisible(true);
    }
  private void mostrarTablaUsuarios(JPanel panelCentral) {
        // Conexión a la base de datos y obtención de los usuarios
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id_usuario");
        model.addColumn("Nombre");
        model.addColumn("Apellido");
        model.addColumn("Correo");
        model.addColumn("Tipo");
        model.addColumn("Contraseña");
        model.addColumn("Nombre de Usuario");

        try {
            Connection conexion = DriverManager.getConnection(url, usuario, contraseña1);
            String sql = "SELECT * FROM usuario";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("correo"),
                    rs.getString("tipo"),
                    rs.getString("contraseña"),
                    rs.getString("nombre_usuario")
                });
            }

            rs.close();
            stmt.close();
            conexion.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JTable tablaUsuarios = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        panelCentral.removeAll();
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        panelCentral.revalidate();
        panelCentral.repaint();

        // Agregar los botones de acción para Usuarios
        JPanel panelAcciones = new JPanel();
        panelAcciones.setLayout(new FlowLayout());
        
        JButton btnAgregarUsuario = new JButton("Agregar Usuario");
        JButton btnActualizarUsuario = new JButton("Actualizar Usuario");
        JButton btnEliminarUsuario = new JButton("Eliminar Usuario");
        
        panelAcciones.add(btnAgregarUsuario);
        panelAcciones.add(btnActualizarUsuario);
        panelAcciones.add(btnEliminarUsuario);
        
        panelCentral.add(panelAcciones, BorderLayout.SOUTH);

        // Evento para agregar un usuario
        btnAgregarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarUsuario();
            }
        });

        // Evento para actualizar un usuario
        btnActualizarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarUsuario(tablaUsuarios);
            }
        });

        // Evento para eliminar un usuario
        btnEliminarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarUsuario(tablaUsuarios);
            }
        });
    }

 private void agregarUsuario() {
    String nombre = JOptionPane.showInputDialog(this, "Nombre:");
    String apellido = JOptionPane.showInputDialog(this, "Apellido:");
    String correo = JOptionPane.showInputDialog(this, "Correo:");
    String tipo = JOptionPane.showInputDialog(this, "Tipo:");
    String contraseña = JOptionPane.showInputDialog(this, "Contraseña:");
    String nombreUsuario = JOptionPane.showInputDialog(this, "Nombre de Usuario:");

    // Validaciones
    if (nombre == null || nombre.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
        return;
    }

    if (apellido == null || apellido.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El apellido no puede estar vacío.");
        return;
    }

    if (correo == null || correo.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El correo no puede estar vacío.");
        return;
    }

    if (tipo == null || tipo.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El tipo no puede estar vacío.");
        return;
    }

    if (contraseña == null || contraseña.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "La contraseña no puede estar vacía.");
        return;
    }

    if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El nombre de usuario no puede estar vacío.");
        return;
    }

    // Insertar usuario en la base de datos
    try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña1)) {
        String sql = "INSERT INTO usuario (nombre, apellido, correo, tipo, contraseña, nombre_usuario) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conexion.prepareStatement(sql);
        stmt.setString(1, nombre);
        stmt.setString(2, apellido);
        stmt.setString(3, correo);
        stmt.setString(4, tipo);
        stmt.setString(5, contraseña);
        stmt.setString(6, nombreUsuario);
        stmt.executeUpdate();
        JOptionPane.showMessageDialog(this, "Usuario agregado exitosamente");
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al agregar el usuario.");
    }
}

private void actualizarUsuario(JTable table) {
    int row = table.getSelectedRow();
    if (row != -1) {
        int id = (int) table.getValueAt(row, 0);  // Obtener ID del usuario seleccionado
        String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre:");
        String nuevoApellido = JOptionPane.showInputDialog(this, "Nuevo apellido:");
        String nuevoCorreo = JOptionPane.showInputDialog(this, "Nuevo correo:");
        String nuevoTipo = JOptionPane.showInputDialog(this, "Nuevo tipo:");
        String nuevaContraseña = JOptionPane.showInputDialog(this, "Nueva contraseña:");
        String nuevoNombreUsuario = JOptionPane.showInputDialog(this, "Nuevo nombre de usuario:");

        try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña1)) {
            String sql = "UPDATE usuario SET nombre = ?, apellido = ?, correo = ?, tipo = ?, contraseña = ?, nombre_usuario = ? WHERE id_usuario = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, nuevoNombre);
            stmt.setString(2, nuevoApellido);
            stmt.setString(3, nuevoCorreo);
            stmt.setString(4, nuevoTipo);
            stmt.setString(5, nuevaContraseña);
            stmt.setString(6, nuevoNombreUsuario);
            stmt.setInt(7, id);  // Asegúrate de usar el ID del usuario en la condición WHERE
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Usuario actualizado exitosamente");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar el usuario.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Por favor selecciona un usuario.");
    }
}

private void eliminarUsuario(JTable table) {
    int row = table.getSelectedRow();
    if (row != -1) {
        int id = (int) table.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres eliminar este usuario?");
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña1)) {
                String sql = "DELETE FROM usuario WHERE id_usuario = ?";
                PreparedStatement stmt = conexion.prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al eliminar el usuario.");
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Por favor selecciona un usuario.");
    }
}

private void abrirVentanaCajero() {
    dispose(); // Cierra la ventana actual
    JFrame ventanaCajero = new JFrame("Ventana Cajero");
    ventanaCajero.setSize(1000, 600);
    ventanaCajero.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    ventanaCajero.setLocationRelativeTo(null);
    ventanaCajero.getContentPane().setBackground(new Color(255, 204, 102));

    JPanel panel = new JPanel(new BorderLayout());
    ventanaCajero.add(panel);

    // Panel de botones (con GridLayout vertical)
    JPanel botonesPanel = new JPanel(new GridLayout(4, 1, 10, 10)); // 4 filas, 1 columna
    botonesPanel.setBackground(new Color(255, 204, 102)); // Puedes darle el mismo color de fondo a los botones si lo deseas

    JButton btnMostrarMenu = new JButton("Mostrar Menú");
    btnMostrarMenu.setFont(new Font("Arial", Font.BOLD, 18));
    btnMostrarMenu.setBackground(new Color(255, 153, 51));
    botonesPanel.add(btnMostrarMenu);

    JButton btnGenerarOrden = new JButton("Generar Orden");
    btnGenerarOrden.setFont(new Font("Arial", Font.BOLD, 18));
    btnGenerarOrden.setBackground(new Color(255, 153, 51));
    botonesPanel.add(btnGenerarOrden);

    JButton btnHistorialOrdenes = new JButton("Ver Historial de Órdenes");
    btnHistorialOrdenes.setFont(new Font("Arial", Font.BOLD, 18));
    btnHistorialOrdenes.setBackground(new Color(255, 153, 51));
    botonesPanel.add(btnHistorialOrdenes);
    
    JButton btnTicket = new JButton("Ticket");
    btnTicket.setFont(new Font("Arial", Font.BOLD, 18));
    btnTicket.setBackground(new Color(255, 153, 51));
    botonesPanel.add(btnTicket);

    panel.add(botonesPanel, BorderLayout.WEST); // Los botones se añaden en el panel lateral

    // Panel central
    JPanel panelCentral = new JPanel(new BorderLayout());
    panel.add(panelCentral, BorderLayout.CENTER);

    // Modelo para la tabla de productos
    DefaultTableModel modelProductos = new DefaultTableModel(
        new Object[][]{}, 
        new String[]{"ID", "Nombre", "Precio", "Acción"}
    );

    // Campo de búsqueda
    JTextField campoBusqueda = new JTextField();
    campoBusqueda.setFont(new Font("Arial", Font.PLAIN, 16));
    campoBusqueda.setBackground(new Color(255, 255, 255));
    campoBusqueda.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            // Llamar a cargar productos con el término de búsqueda
            cargarProductos(modelProductos, campoBusqueda.getText());
        }
    });

    JPanel panelBusqueda = new JPanel(new BorderLayout());
    panelBusqueda.add(new JLabel("Buscar Producto:"), BorderLayout.WEST);
    panelBusqueda.add(campoBusqueda, BorderLayout.CENTER);
    panelCentral.add(panelBusqueda, BorderLayout.NORTH);

    // Tabla de productos
    JTable tablaProductos = new JTable(modelProductos);
    tablaProductos.setRowHeight(30);
    JScrollPane scrollProductos = new JScrollPane(tablaProductos);
    panelCentral.add(scrollProductos, BorderLayout.CENTER);

    // Tabla del carrito
    DefaultTableModel modelCarrito = new DefaultTableModel(
        new Object[][]{}, 
        new String[]{"ID", "Nombre", "Cantidad", "Subtotal", "Acción"}
    );
    JTable tablaCarrito = new JTable(modelCarrito);
    tablaCarrito.setRowHeight(30);
    JScrollPane scrollCarrito = new JScrollPane(tablaCarrito);

    JPanel panelCarrito = new JPanel(new BorderLayout());
    panelCarrito.add(new JLabel("Carrito:"), BorderLayout.NORTH);
    panelCarrito.add(scrollCarrito, BorderLayout.CENTER);
    panelCentral.add(panelCarrito, BorderLayout.EAST);

    // Eventos para botones
    btnMostrarMenu.addActionListener(e -> cargarProductos(modelProductos, ""));
    btnGenerarOrden.addActionListener(e -> generarOrden(modelCarrito));
    btnHistorialOrdenes.addActionListener(e -> verHistorialOrdenes());
    btnTicket.addActionListener(e -> abrirVentanaTicket());

    agregarBotonAgregar(modelProductos, modelCarrito, tablaProductos);
    agregarBotonEliminar(modelCarrito, tablaCarrito);

    ventanaCajero.setVisible(true);
}

private void cargarProductos(DefaultTableModel model, String busqueda) {
    model.setRowCount(0); // Limpia la tabla
    try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña1)) {
        String sql = "SELECT * FROM producto WHERE nombre LIKE ?";
        PreparedStatement stmt = conexion.prepareStatement(sql);
        stmt.setString(1, "%" + busqueda + "%"); // Filtra los productos por nombre usando LIKE
        ResultSet rs = stmt.executeQuery();

        // Verificación de que la consulta está devolviendo datos
        if (!rs.isBeforeFirst()) {
            System.out.println("No se encontraron productos que coincidan con la búsqueda.");
        }

        while (rs.next()) {
            int id = rs.getInt("id_producto");
            String nombre = rs.getString("nombre");
            double precio = rs.getDouble("precio");
            model.addRow(new Object[]{id, nombre, precio, "Agregar"});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// Método para mostrar la tabla de productos
private void mostrarTablaProductos(JPanel panel) {
    // Crear el modelo de la tabla
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("Id_producto");
    model.addColumn("Nombre");
    model.addColumn("Precio");

    // Cargar los productos desde la base de datos
    try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña1)) {
        String sql = "SELECT * FROM producto";
        PreparedStatement stmt = conexion.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id_producto"),
                rs.getString("nombre"),
                rs.getDouble("precio")
            });
        }

        rs.close();
        stmt.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    // Crear la tabla de productos
    JTable tablaProductos = new JTable(model);
    JScrollPane scrollPane = new JScrollPane(tablaProductos);

    // Panel donde se mostrará la tabla de productos
    JPanel panelTabla = (JPanel) panel.getComponent(1);  // Obtener el panel central para agregar la tabla
    panelTabla.removeAll();  // Limpiar el contenido anterior
    panelTabla.add(scrollPane, BorderLayout.CENTER);  // Agregar la tabla al panel
    panelTabla.revalidate();  // Revalidar el panel
    panelTabla.repaint();     // Repaint para asegurar que la tabla se vea
}
// Modificar el evento para agregar productos al carrito
private void agregarBotonAgregar(DefaultTableModel modelProductos, DefaultTableModel modelCarrito, JTable tablaProductos) {
    tablaProductos.getColumn("Acción").setCellRenderer(new ButtonRenderer("Agregar"));
    tablaProductos.getColumn("Acción").setCellEditor(new ButtonEditor(new JCheckBox(), tablaProductos, modelProductos, modelCarrito, "Agregar"));
}

private void agregarBotonEliminar(DefaultTableModel modelCarrito, JTable tablaCarrito) {
    tablaCarrito.getColumn("Acción").setCellRenderer(new ButtonRenderer("Eliminar"));
    tablaCarrito.getColumn("Acción").setCellEditor(new ButtonEditor(new JCheckBox(), tablaCarrito, modelCarrito, "Eliminar"));
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer(String text) {
        setText(text);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Acción" : value.toString());
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String actionType; // Puede ser "Agregar" o "Eliminar"
    private JTable table; // Referencia a la tabla
    private boolean isPushed;
    private DefaultTableModel modelProductos;
    private DefaultTableModel modelCarrito;

    // Constructor para agregar (dos modelos)
    public ButtonEditor(JCheckBox checkBox, JTable table, DefaultTableModel modelProductos, DefaultTableModel modelCarrito, String actionType) {
        super(checkBox);
        this.table = table;
        this.modelProductos = modelProductos;
        this.modelCarrito = modelCarrito;
        this.actionType = actionType;

        initializeButton();
    }

    // Constructor para eliminar (un modelo)
    public ButtonEditor(JCheckBox checkBox, JTable table, DefaultTableModel modelCarrito, String actionType) {
        super(checkBox);
        this.table = table;
        this.modelCarrito = modelCarrito;
        this.actionType = actionType;

        initializeButton();
    }

    private void initializeButton() {
        button = new JButton();
        button.setOpaque(true);

        button.addActionListener(e -> {
            fireEditingStopped();
            if ("Agregar".equals(actionType)) {
                agregarProducto();
            } else if ("Eliminar".equals(actionType)) {
                eliminarProducto();
            }
        });
    }

    private void agregarProducto() {
        int selectedRow = table.getSelectedRow(); // Ahora usamos la tabla directamente
        if (selectedRow >= 0) {
            int idProducto = (int) modelProductos.getValueAt(selectedRow, 0);
            String nombreProducto = (String) modelProductos.getValueAt(selectedRow, 1);
            double precioProducto = (double) modelProductos.getValueAt(selectedRow, 2);

            String cantidadStr = JOptionPane.showInputDialog(
                    button,
                    "Ingrese la cantidad de \"" + nombreProducto + "\":",
                    "Cantidad",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (cantidadStr != null) {
                try {
                    int cantidad = Integer.parseInt(cantidadStr.trim());
                    if (cantidad > 0) {
                        double subtotal = cantidad * precioProducto;
                        modelCarrito.addRow(new Object[]{idProducto, nombreProducto, cantidad, subtotal, "Eliminar"});
                        JOptionPane.showMessageDialog(null, "Producto agregado correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(button, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(button, "Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void eliminarProducto() {
        int selectedRow = table.getSelectedRow(); // Ahora usamos la tabla directamente
        if (selectedRow >= 0) {
            int idProducto = (int) modelCarrito.getValueAt(selectedRow, 0);
            int cantidadActual = (int) modelCarrito.getValueAt(selectedRow, 2);

            String cantidadStr = JOptionPane.showInputDialog(
                    button,
                    "¿Cuántas piezas desea eliminar? (Disponibles: " + cantidadActual + "):",
                    "Eliminar",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (cantidadStr != null) {
                try {
                    int cantidadEliminar = Integer.parseInt(cantidadStr.trim());
                    if (cantidadEliminar > 0 && cantidadEliminar <= cantidadActual) {
                        if (cantidadEliminar < cantidadActual) {
                            int nuevaCantidad = cantidadActual - cantidadEliminar;
                            modelCarrito.setValueAt(nuevaCantidad, selectedRow, 2);

                            double precioUnitario = (double) modelCarrito.getValueAt(selectedRow, 3) / cantidadActual;
                            double nuevoSubtotal = nuevaCantidad * precioUnitario;
                            modelCarrito.setValueAt(nuevoSubtotal, selectedRow, 3);
                        } else {
                            modelCarrito.removeRow(selectedRow);
                        }

                        eliminarProductoCarritoBD(idProducto, cantidadEliminar, cantidadActual);
                        JOptionPane.showMessageDialog(null, "Producto eliminado correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Cantidad no válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(button, "Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText(actionType);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        isPushed = false;
        return button.getText();
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}

// Método para actualizar la tabla `carrito` en la base de datos
private void agregarProductoCarritoBD(int idProducto, int cantidad) {
    try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña1)) {
        // Verificar si el producto ya está en el carrito
        String consulta = "SELECT cantidad FROM carrito WHERE id_producto = ?";
        PreparedStatement stmtConsulta = conexion.prepareStatement(consulta);
        stmtConsulta.setInt(1, idProducto);
        ResultSet rs = stmtConsulta.executeQuery();

        if (rs.next()) {
            // Producto existe: actualizar cantidad
            int cantidadActual = rs.getInt("cantidad");
            String update = "UPDATE carrito SET cantidad = ? WHERE id_producto = ?";
            PreparedStatement stmtUpdate = conexion.prepareStatement(update);
            stmtUpdate.setInt(1, cantidadActual + cantidad);
            stmtUpdate.setInt(2, idProducto);
            stmtUpdate.executeUpdate();
        } else {
            // Producto no existe: insertar nuevo
            String insert = "INSERT INTO carrito (id_producto, cantidad) VALUES (?, ?)";
            PreparedStatement stmtInsert = conexion.prepareStatement(insert);
            stmtInsert.setInt(1, idProducto);
            stmtInsert.setInt(2, cantidad);
            stmtInsert.executeUpdate();
        }

        rs.close();
        stmtConsulta.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
private void cargarCarrito(DefaultTableModel modelCarrito) {
    modelCarrito.setRowCount(0); // Limpia la tabla
    try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña1)) {
        String sql = "SELECT c.id_producto, p.nombre, c.cantidad, p.precio " +
                     "FROM carrito c JOIN producto p ON c.id_producto = p.id_producto";
        PreparedStatement stmt = conexion.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id_producto");
            String nombre = rs.getString("nombre");
            int cantidad = rs.getInt("cantidad");
            double precio = rs.getDouble("precio");
            double subtotal = cantidad * precio;
            modelCarrito.addRow(new Object[]{id, nombre, cantidad, subtotal, "Eliminar"});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void eliminarProductoCarritoBD(int idProducto, int cantidadEliminar, int cantidadActual) {
    try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña1)) {
        if (cantidadEliminar < cantidadActual) {
            // Si se elimina parcialmente, actualizar la cantidad restante
            String sql = "UPDATE carrito SET cantidad = ? WHERE id_producto = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, cantidadActual - cantidadEliminar);
            stmt.setInt(2, idProducto);
            stmt.executeUpdate();
            stmt.close();
        } else {
            // Si se elimina completamente, eliminar el registro
            String sql = "DELETE FROM carrito WHERE id_producto = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, idProducto);
            stmt.executeUpdate();
            stmt.close();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al eliminar producto del carrito: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


private double obtenerPrecioProducto(int idProducto) {
    try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña1)) {
        String sql = "SELECT precio FROM producto WHERE id_producto = ?";
        PreparedStatement stmt = conexion.prepareStatement(sql);
        stmt.setInt(1, idProducto);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getDouble("precio");
        }
        rs.close();
        stmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0.0;
}
private void actualizarCantidadCarritoBD(int idProducto, int cantidad) {
    try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña1)) {
        String sql = "UPDATE carrito SET cantidad = ? WHERE id_producto = ?";
        PreparedStatement stmt = conexion.prepareStatement(sql);
        stmt.setInt(1, cantidad);
        stmt.setInt(2, idProducto);
        stmt.executeUpdate();
        stmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
private void generarOrden(DefaultTableModel modelCarrito) {
    // Reseteamos el total antes de generar la orden
    total = 0;  // Reiniciar total
    
    // Verificar si hay productos en el carrito
    if (modelCarrito.getRowCount() == 0) {
        JOptionPane.showMessageDialog(null, "El carrito está vacío. No se puede generar la orden.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Calcular el total de la orden
    for (int i = 0; i < modelCarrito.getRowCount(); i++) {
        try {
            // Obtener el subtotal (precio * cantidad) de la columna 3 (según tu estructura del carrito)
            double subtotal = (double) modelCarrito.getValueAt(i, 3);
            total += subtotal;  // Acumulamos el total
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en los valores del carrito: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    // Crear la ventana para la orden
    JFrame ventanaOrden = new JFrame("Detalle de la Orden");
    ventanaOrden.setSize(800, 600);
    ventanaOrden.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    ventanaOrden.setLocationRelativeTo(null);

    JPanel panelOrden = new JPanel(new BorderLayout());
    ventanaOrden.add(panelOrden);

    // Tabla para mostrar los productos de la orden
    DefaultTableModel modelOrden = new DefaultTableModel(
        new Object[][] {},
        new String[]{"ID", "Nombre", "Cantidad", "Subtotal"}
    );

    for (int i = 0; i < modelCarrito.getRowCount(); i++) {
        int id = (int) modelCarrito.getValueAt(i, 0);
        String nombre = (String) modelCarrito.getValueAt(i, 1);
        int cantidad = (int) modelCarrito.getValueAt(i, 2);
        double subtotal = (double) modelCarrito.getValueAt(i, 3);
        modelOrden.addRow(new Object[]{id, nombre, cantidad, subtotal});
    }

    JTable tablaOrden = new JTable(modelOrden);
    tablaOrden.setRowHeight(30);
    JScrollPane scrollOrden = new JScrollPane(tablaOrden);
    panelOrden.add(scrollOrden, BorderLayout.CENTER);

    // Mostrar el total
    JPanel panelTotal = new JPanel();
    JLabel lblTotal = new JLabel("Total de la compra: $ " + total);
    lblTotal.setFont(new Font("Arial", Font.BOLD, 20));
    panelTotal.add(lblTotal);
    panelOrden.add(panelTotal, BorderLayout.SOUTH);

    // Botón para finalizar la compra
    JButton btnFinalizarCompra = new JButton("Finalizar Compra");
    btnFinalizarCompra.setFont(new Font("Arial", Font.BOLD, 18));
    btnFinalizarCompra.setBackground(new Color(255, 153, 51));
    btnFinalizarCompra.addActionListener(e -> {
        finalizarCompra(modelCarrito, total);
        ventanaOrden.dispose();
    });
    panelOrden.add(btnFinalizarCompra, BorderLayout.NORTH);

    ventanaOrden.setVisible(true);
}

private void finalizarCompra(DefaultTableModel modelCarrito, double total) {
    // Insertar la orden en la base de datos
    try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña1)) {
        // Insertar la orden
        String sqlInsertOrden = "INSERT INTO orden (fecha, total) VALUES (NOW(), ?)";
        PreparedStatement stmtOrden = conexion.prepareStatement(sqlInsertOrden, Statement.RETURN_GENERATED_KEYS);
        stmtOrden.setDouble(1, total);
        stmtOrden.executeUpdate();

        // Obtener el ID de la orden generada
        ResultSet rsOrden = stmtOrden.getGeneratedKeys();
        int idOrden = 0;
        if (rsOrden.next()) {
            idOrden = rsOrden.getInt(1);  // Obtener el ID de la orden
        }

        // Insertar los detalles de la orden
        for (int i = 0; i < modelCarrito.getRowCount(); i++) {
            int idProducto = (int) modelCarrito.getValueAt(i, 0);
            int cantidad = (int) modelCarrito.getValueAt(i, 2);
            double subtotal = (double) modelCarrito.getValueAt(i, 3);

            String sqlInsertDetalle = "INSERT INTO detalle_orden (id_orden, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtDetalle = conexion.prepareStatement(sqlInsertDetalle);
            stmtDetalle.setInt(1, idOrden);
            stmtDetalle.setInt(2, idProducto);
            stmtDetalle.setInt(3, cantidad);
            stmtDetalle.setDouble(4, subtotal);
            stmtDetalle.executeUpdate();
        }

        // Limpiar el carrito y el total
        modelCarrito.setRowCount(0);
        total = 0;  // Reiniciar el total
        JOptionPane.showMessageDialog(null, "La compra ha sido finalizada exitosamente.", "Compra Finalizada", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al finalizar la compra: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void verHistorialOrdenes() {
    // Crear una nueva ventana para el historial de órdenes
    JFrame ventanaHistorial = new JFrame("Historial de Órdenes");
    ventanaHistorial.setSize(800, 600);
    ventanaHistorial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    ventanaHistorial.setLocationRelativeTo(null);

    JPanel panelHistorial = new JPanel(new BorderLayout());
    ventanaHistorial.add(panelHistorial);

    // Modelo para la tabla de órdenes
    DefaultTableModel modelOrdenes = new DefaultTableModel(
        new Object[][]{},
        new String[]{"ID Orden", "Fecha", "Total"}
    );

    // Cargar las órdenes desde la base de datos
    try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña1)) {
        String sql = "SELECT id_orden, fecha, total FROM orden ORDER BY fecha DESC";
        PreparedStatement stmt = conexion.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int idOrden = rs.getInt("id_orden");
            String fecha = rs.getString("fecha");
            double total = rs.getDouble("total");
            modelOrdenes.addRow(new Object[]{idOrden, fecha, total});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Tabla para mostrar las órdenes
    JTable tablaOrdenes = new JTable(modelOrdenes);
    tablaOrdenes.setRowHeight(30);
    JScrollPane scrollOrdenes = new JScrollPane(tablaOrdenes);
    panelHistorial.add(scrollOrdenes, BorderLayout.CENTER);

    // Agregar un listener de clic para mostrar los detalles de la orden seleccionada
    tablaOrdenes.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = tablaOrdenes.getSelectedRow();
            if (row >= 0) {
                int idOrden = (int) modelOrdenes.getValueAt(row, 0);
                verDetallesOrden(idOrden); // Mostrar los detalles de la orden
            }
        }
    });

    ventanaHistorial.setVisible(true);
}
private void verDetallesOrden(int idOrden) {
    // Crear una nueva ventana para los detalles de la orden
    JFrame ventanaDetalles = new JFrame("Detalles de la Orden");
    ventanaDetalles.setSize(800, 600);
    ventanaDetalles.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    ventanaDetalles.setLocationRelativeTo(null);

    JPanel panelDetalles = new JPanel(new BorderLayout());
    ventanaDetalles.add(panelDetalles);

    // Modelo para la tabla de detalles de la orden
    DefaultTableModel modelDetalles = new DefaultTableModel(
        new Object[][]{},
        new String[]{"ID Producto", "Nombre", "Cantidad", "Subtotal"}
    );

    // Cargar los detalles de la orden desde la base de datos
    try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña1)) {
        String sql = "SELECT p.id_producto, p.nombre, d.cantidad, d.subtotal " +
                     "FROM detalle_orden d " +
                     "JOIN producto p ON d.id_producto = p.id_producto " +
                     "WHERE d.id_orden = ?";
        PreparedStatement stmt = conexion.prepareStatement(sql);
        stmt.setInt(1, idOrden);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int idProducto = rs.getInt("id_producto");
            String nombre = rs.getString("nombre");
            int cantidad = rs.getInt("cantidad");
            double subtotal = rs.getDouble("subtotal");
            modelDetalles.addRow(new Object[]{idProducto, nombre, cantidad, subtotal});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Tabla para mostrar los detalles de la orden
    JTable tablaDetalles = new JTable(modelDetalles);
    tablaDetalles.setRowHeight(30);
    JScrollPane scrollDetalles = new JScrollPane(tablaDetalles);
    panelDetalles.add(scrollDetalles, BorderLayout.CENTER);

    ventanaDetalles.setVisible(true);
}
private void abrirVentanaTicket() {
    // Crear la ventana para seleccionar la orden
    JFrame ventanaOrdenes = new JFrame("Seleccionar Orden");
    ventanaOrdenes.setSize(600, 400);
    ventanaOrdenes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    ventanaOrdenes.setLocationRelativeTo(null);

    JPanel panelOrdenes = new JPanel(new BorderLayout());
    ventanaOrdenes.add(panelOrdenes);

    // Tabla para mostrar las órdenes disponibles
    DefaultTableModel modelOrdenes = new DefaultTableModel(new Object[][]{}, new String[]{"ID de Orden", "Fecha", "Total"});
    JTable tablaOrdenes = new JTable(modelOrdenes);
    tablaOrdenes.setRowHeight(30);

    // Cargar las órdenes en la tabla
    cargarOrdenes(modelOrdenes);

    JScrollPane scrollOrdenes = new JScrollPane(tablaOrdenes);
    panelOrdenes.add(scrollOrdenes, BorderLayout.CENTER);

    // Botón para elegir la orden
    JButton btnSeleccionarOrden = new JButton("Seleccionar Orden");
    btnSeleccionarOrden.setFont(new Font("Arial", Font.BOLD, 18));
    btnSeleccionarOrden.setBackground(new Color(255, 153, 51));
    btnSeleccionarOrden.addActionListener(e -> {
        int row = tablaOrdenes.getSelectedRow();
        if (row != -1) {
            // Obtener el ID de la orden seleccionada
            int idOrden = (int) tablaOrdenes.getValueAt(row, 0);
            ventanaOrdenes.dispose(); // Cerrar la ventana de selección de orden
            abrirVentanaCorreo(idOrden); // Abrir ventana para ingresar el correo y enviar el ticket
        } else {
            JOptionPane.showMessageDialog(ventanaOrdenes, "Por favor seleccione una orden.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
    panelOrdenes.add(btnSeleccionarOrden, BorderLayout.SOUTH);

    ventanaOrdenes.setVisible(true);
}

private void abrirVentanaCorreo(int idOrden) {
    // Crear la ventana para ingresar el correo
    JFrame ventanaCorreo = new JFrame("Enviar Ticket por Correo");
    ventanaCorreo.setSize(400, 200);
    ventanaCorreo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    ventanaCorreo.setLocationRelativeTo(null);

    JPanel panelCorreo = new JPanel(new BorderLayout());
    ventanaCorreo.add(panelCorreo);

    // Campo de texto para ingresar el correo
    JTextField correoField = new JTextField();
    correoField.setFont(new Font("Arial", Font.PLAIN, 16));
    panelCorreo.add(new JLabel("Ingrese su correo electrónico:"), BorderLayout.NORTH);
    panelCorreo.add(correoField, BorderLayout.CENTER);

    // Botón para enviar el ticket
    JButton btnEnviarTicket = new JButton("Enviar Ticket");
    btnEnviarTicket.setFont(new Font("Arial", Font.BOLD, 18));
    btnEnviarTicket.setBackground(new Color(255, 153, 51));
    btnEnviarTicket.addActionListener(e -> {
        String correo = correoField.getText();
        if (correo != null && !correo.isEmpty()) {
            // Enviar el ticket por correo
            enviarTicketPorCorreo(correo, idOrden);
            ventanaCorreo.dispose(); // Cerrar la ventana de correo después de enviar
        } else {
            JOptionPane.showMessageDialog(ventanaCorreo, "Por favor ingrese un correo válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
    panelCorreo.add(btnEnviarTicket, BorderLayout.SOUTH);

    ventanaCorreo.setVisible(true);
}

private void cargarOrdenes(DefaultTableModel modelOrdenes) {
    // Aquí cargamos las órdenes desde la base de datos
    try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña1)) {
        String sql = "SELECT id_orden, fecha, total FROM orden ORDER BY fecha DESC";
        PreparedStatement stmt = conexion.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            modelOrdenes.addRow(new Object[]{
                rs.getInt("id_orden"),
                rs.getDate("fecha"),
                rs.getDouble("total")
            });
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void enviarTicketPorCorreo(String correo, int idOrden) {
    String host = "smtp.gmail.com";
    String from = "usuario@gmail.com"; // Tu correo de envío
    String pass = "iwix rftu ftrg kztp"; // La contraseña de tu correo
    String to = correo;

    // Generar el PDF
    String pdfPath = generarTicketPDF(idOrden);
    if (pdfPath == null) {
        JOptionPane.showMessageDialog(null, "Error al generar el ticket en PDF.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Configuración de la sesión de correo
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(from, pass);
        }
    });

    try {
        // Crear el mensaje con el archivo adjunto
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("Ticket de Compra - Orden #" + idOrden);
        message.setText("Gracias por tu compra. Aquí está tu ticket de la orden #" + idOrden);

        // Agregar el archivo adjunto
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText("Adjunto encontrarás tu ticket de compra.");

        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile(new File(pdfPath)); // Adjuntar el PDF

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        multipart.addBodyPart(attachmentPart);

        message.setContent(multipart);

        // Enviar el correo
        Transport.send(message);

        JOptionPane.showMessageDialog(null, "Ticket enviado a " + correo);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al enviar el correo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private String generarTicketPDF(int idOrden) {
    String pdfPath = "ticket_orden_" + idOrden + ".pdf";

    try {
        // Conexión a la base de datos para obtener los detalles de la orden
        try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña1)) {
            // Consulta para obtener los detalles de la orden
            String sql = "SELECT p.nombre AS producto, detalle_orden.cantidad, p.precio " +
                         "FROM detalle_orden " +
                         "JOIN producto p ON detalle_orden.id_producto = p.id_producto " +
                         "WHERE detalle_orden.id_orden = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, idOrden); // Establecer el valor del parámetro id_orden
            ResultSet rs = stmt.executeQuery();

            // Crear el PDF
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();

            // Agregar el logo 
            try {
                Image logo = Image.getInstance("C:\\Users\\Morena\\Documents\\NetBeansProjects\\ProyectoRestaurant\\src\\ImagenesRes\\logoR.png"); // Cambia la ruta
                logo.scaleToFit(150, 150); // Escala la imagen
                logo.setAlignment(Element.ALIGN_CENTER); // Centra la imagen
                document.add(logo); // Agrega la imagen al documento
                document.add(new Paragraph("\n")); // Salto de línea después del logo
            } catch (Exception e) {
                e.printStackTrace();
                // Si hay un error con la imagen, simplemente no la agrega
            }

            // Agregar un encabezado
            Paragraph encabezado = new Paragraph("Ticket de Compra - Orden #" + idOrden,
                    new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD));
            encabezado.setAlignment(Element.ALIGN_CENTER);
            document.add(encabezado);
            document.add(new Paragraph("\n"));

            // Agregar detalles de la empresa (opcional)
            Paragraph detallesEmpresa = new Paragraph("Restaurante Comida mexa\nDirección: Calle Riveras #34\nTel: 555-1234",
                    new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.ITALIC));
            detallesEmpresa.setAlignment(Element.ALIGN_CENTER);
            document.add(detallesEmpresa);
            document.add(new Paragraph("\n"));

            // Crear la tabla para los productos
            PdfPTable table = new PdfPTable(4); // 4 columnas: Producto, Cantidad, Precio, Subtotal
            table.addCell("Producto");
            table.addCell("Cantidad");
            table.addCell("Precio");
            table.addCell("Subtotal");

            double totalCompra = 0;

            // Llenar la tabla con los datos del ResultSet
            while (rs.next()) {
                String producto = rs.getString("producto");
                int cantidad = rs.getInt("cantidad");
                double precio = rs.getDouble("precio");
                double subtotal = cantidad * precio;

                table.addCell(producto); // Nombre del producto
                table.addCell(String.valueOf(cantidad)); // Cantidad
                table.addCell(String.format("$%.2f", precio)); // Precio
                table.addCell(String.format("$%.2f", subtotal)); // Subtotal

                totalCompra += subtotal; // Acumular el total de la compra
            }

            document.add(table);

            // Agregar un salto de línea
            document.add(new Paragraph("\n"));

            // Mostrar el total de la compra
            Paragraph total = new Paragraph("Total: " + String.format("$%.2f", totalCompra),
                    new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD));
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            // Agregar un mensaje de agradecimiento
            document.add(new Paragraph("\nGracias por tu compra.", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12)));
            document.add(new Paragraph("\n"));

            // Cerrar el documento PDF
            document.close();
        }
    } catch (Exception e) {
        e.printStackTrace();
        return null; // Retornar null si ocurre algún error
    }

    return pdfPath; // Retornar la ruta del archivo PDF generado
}

private void configurarBoton(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(23, 162, 184));
        boton.setPreferredSize(new Dimension(200, 50));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
    }
public void conectar() {
        try {
            // Cargar el controlador JDBC para PostgreSQL
            Class.forName("org.postgresql.Driver");

            // Establecer la conexión
            conexion = DriverManager.getConnection(url, usuario, contraseña1);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el controlador JDBC: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
public void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
  


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   public static void main(String[] args) {
        Restaurante2 app = new Restaurante2();
        app.conectar();  // Conectar primero
         app.setVisible(true);  // Abrir ventana después de conectar
    }
};


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

