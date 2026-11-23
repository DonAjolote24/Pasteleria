package vista;

import dao.UsuarioDAO;
import dao.ProveedorDAO;
import dao.CargoDAO;
import dao.ClienteDAO;

import modelo.Usuario;
import Estilo.Estilo;
import component.FormularioDatosPersonales;
import component.FormularioContacto;
import component.FormularioDomicilio;
import component.FormularioProveedor;
import component.FormularioCargo;
import component.FormularioCliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PanelUsuarios extends JPanel {

    private FormularioDatosPersonales panelPersonales;
    private FormularioContacto panelContacto;
    private FormularioDomicilio panelDomicilio;
    
    private FormularioProveedor formProveedor;
    private FormularioCargo formCargo;
    private FormularioCliente formCliente;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtBuscar;
    private JComboBox<String> cmbFiltroRol; 

    private UsuarioDAO dao;
    private ProveedorDAO daoProveedor;
    private CargoDAO daoCargo;
    private ClienteDAO daoCliente;

    private int idUsuarioSeleccionado = -1;
    private int pasoActual = 0; 
    private String rolSeleccionado = "";
    private JPanel panelFormularios;
    private CardLayout cardFormularios;
    private JButton btnAtras;
    private JButton btnSiguienteGuardar;
    private JLabel lblPaso;

    private List<Object[]> datosCrudos = new ArrayList<>();
    private final String PLACEHOLDER = " 🔍 Buscar por nombre, apellidos, RFC o CURP...";

    private boolean modoEdicion = false;
    private int idEdicion = -1;

    public PanelUsuarios() {
        dao = new UsuarioDAO();
        daoProveedor = new ProveedorDAO();
        daoCargo = new CargoDAO();
        daoCliente = new ClienteDAO();

        setLayout(new BorderLayout());
        setBackground(Estilo.VAINILLA);

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(Estilo.VAINILLA);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ==========================================
        // FORMULARIOS (Pasos 1-4)
        // ==========================================
        cardFormularios = new CardLayout();
        panelFormularios = new JPanel(cardFormularios);
        panelFormularios.setOpaque(false);
        panelFormularios.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFormularios.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));

        panelPersonales = new FormularioDatosPersonales();
        panelContacto   = new FormularioContacto();
        panelDomicilio  = new FormularioDomicilio();
        formProveedor   = new FormularioProveedor();
        formCargo       = new FormularioCargo();
        formCliente     = new FormularioCliente();

        panelFormularios.add(panelPersonales, "PERSONALES");
        panelFormularios.add(panelContacto,   "CONTACTO");
        panelFormularios.add(panelDomicilio,  "DOMICILIO");
        panelFormularios.add(formProveedor,   "PROVEEDOR");
        panelFormularios.add(formCargo,       "CARGO");
        panelFormularios.add(formCliente,     "CLIENTE");

        panelContenido.add(panelFormularios);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 10)));

        // ==========================================
        // INDICADOR Y BOTONES DE NAVEGACIÓN
        // ==========================================
        lblPaso = new JLabel();
        lblPaso.setFont(Estilo.F_SECCION);
        lblPaso.setForeground(Estilo.CHOCOLATE);
        lblPaso.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelContenido.add(lblPaso);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBotones.setOpaque(false);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnAtras = crearBotonFuerte("← Atrás", new Color(120, 120, 120));
        btnAtras.addActionListener(e -> irAtras());

        btnSiguienteGuardar = crearBotonFuerte("Siguiente →", Estilo.CHOCOLATE);
        btnSiguienteGuardar.addActionListener(e -> irSiguienteOGuardar());

        JButton btnLimpiar = crearBotonFuerte("Limpiar Campos", new Color(150, 120, 90));
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panelBotones.add(btnAtras);
        panelBotones.add(btnSiguienteGuardar);
        panelBotones.add(btnLimpiar);

        panelContenido.add(panelBotones);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 30)));

        // ==========================================
        // TABLA Y FILTROS
        // ==========================================
        JPanel panelTablaCompleto = new JPanel(new BorderLayout(0, 10));
        panelTablaCompleto.setOpaque(false);
        panelTablaCompleto.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTablaCompleto.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Estilo.BORDE_SUAVE, 1),
                "Directorio de Personas", 0, 0, Estilo.F_SECCION, Estilo.CHOCOLATE));

        JPanel panelSuperiorTabla = new JPanel(new BorderLayout(10, 0));
        panelSuperiorTabla.setOpaque(false);

        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBuscar.setOpaque(false);
        
        JLabel lblFiltro = new JLabel("Filtrar por Rol:");
        lblFiltro.setFont(Estilo.F_TEXTO);
        lblFiltro.setForeground(Estilo.CHOCOLATE);

        cmbFiltroRol = new JComboBox<>(new String[]{"Todos", "Proveedor", "Cliente", "Empleado", "Administrador"});
        cmbFiltroRol.setFont(Estilo.F_TEXTO);
        cmbFiltroRol.addActionListener(e -> actualizarTablaSegunRol());

        txtBuscar = Estilo.campo(25);
        txtBuscar.setForeground(Color.GRAY);
        txtBuscar.setText(PLACEHOLDER);
        txtBuscar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtBuscar.getText().equals(PLACEHOLDER)) {
                    txtBuscar.setText("");
                    txtBuscar.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtBuscar.getText().isEmpty()) {
                    txtBuscar.setForeground(Color.GRAY);
                    txtBuscar.setText(PLACEHOLDER);
                }
            }
        });
        
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { buscar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { buscar(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { buscar(); }
        });

        panelBuscar.add(lblFiltro);
        panelBuscar.add(cmbFiltroRol);
        panelBuscar.add(txtBuscar);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelAcciones.setOpaque(false);

        JButton btnEditar = crearBotonFuerte("Editar Seleccionado", Estilo.CARAMELO);
        btnEditar.addActionListener(e -> prepararEdicion());

        JButton btnEliminar = crearBotonFuerte("Eliminar Seleccionado", new Color(180, 50, 50));
        btnEliminar.addActionListener(e -> eliminarUsuario());

        panelAcciones.add(btnEditar);
        panelAcciones.add(btnEliminar);

        panelSuperiorTabla.add(panelBuscar, BorderLayout.CENTER);
        panelSuperiorTabla.add(panelAcciones, BorderLayout.EAST);

        modeloTabla = new DefaultTableModel();
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(Estilo.F_SECCION);
        tabla.getTableHeader().setBackground(Estilo.FRESA_CLARO);
        tabla.getTableHeader().setForeground(Estilo.CHOCOLATE);

        sorter = new TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(sorter);

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    int filaModelo = tabla.convertRowIndexToModel(fila);
                    idUsuarioSeleccionado = (int) modeloTabla.getValueAt(filaModelo, 0);
                }
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createLineBorder(Estilo.BORDE_SUAVE, 1));
        scrollTabla.setPreferredSize(new Dimension(0, 250));

        panelTablaCompleto.add(panelSuperiorTabla, BorderLayout.NORTH);
        panelTablaCompleto.add(scrollTabla, BorderLayout.CENTER);
        panelContenido.add(panelTablaCompleto);

        JScrollPane scrollPrincipal = new JScrollPane(panelContenido);
        scrollPrincipal.setBorder(null);
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(16);
        scrollPrincipal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPrincipal.getViewport().setBackground(Estilo.VAINILLA);

        add(scrollPrincipal, BorderLayout.CENTER);

        actualizarPaso();
        actualizarTablaSegunRol();
    }

    private JButton crearBotonFuerte(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(colorFondo);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 40));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(colorFondo.brighter()); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(colorFondo); }
        });
        return btn;
    }

    // ==========================================
    // LÓGICA DE EDICIÓN
    // ==========================================
    private void prepararEdicion() {
        if (idUsuarioSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una persona de la tabla primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modoEdicion = true;
        idEdicion = idUsuarioSeleccionado;

        Object[] datos = dao.obtenerUsuarioPorId(idEdicion);
        if (datos != null) {
            // Si tus formularios tienen el método llamado "setDatos", se usa aquí
            panelPersonales.setDatos((String) datos[0], (String) datos[1], (String) datos[2]);
            panelContacto.setDatos((String) datos[4], (String) datos[5], (String) datos[3], (String) datos[6]);
            panelDomicilio.setDatos((String) datos[7], (String) datos[8], (String) datos[9], (String) datos[10], (String) datos[11], (String) datos[12]);

            rolSeleccionado = (String) datos[6];

            if (rolSeleccionado.equalsIgnoreCase("Proveedor")) {
                Object[] datProv = daoProveedor.obtenerPorIdUsuario(idEdicion);
                if (datProv != null) {
                    formProveedor.setDatos((String) datProv[0], (String) datProv[1], (String) datProv[2]);
                }
            } else if (rolSeleccionado.equalsIgnoreCase("Administrador") || rolSeleccionado.equalsIgnoreCase("Empleado")) {
                Object[] datCargo = daoCargo.obtenerPorIdUsuario(idEdicion);
                if (datCargo != null) {
                    formCargo.setDatos((String) datCargo[0], (String) datCargo[1], (String) datCargo[2], (String) datCargo[3], (String) datCargo[4], (String) datCargo[5]);
                }
            } else {
                Object[] datCli = daoCliente.obtenerPorIdUsuario(idEdicion);
                if (datCli != null) {
                    formCliente.setDatos((String) datCli[0], (String) datCli[1]);
                }
            }

            pasoActual = 0;
            actualizarPaso();
            JOptionPane.showMessageDialog(this, "Datos cargados. Puedes avanzar y modificar los campos.", "Modo Edición", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==========================================
    // TABLA DINÁMICA
    // ==========================================
    private void actualizarTablaSegunRol() {
        String filtroRol = cmbFiltroRol.getSelectedItem().toString();
        datosCrudos = dao.listarConDetalles(filtroRol);
        
        String[] columnasVisibles;
        int[] indicesColumnas;

        if (filtroRol.equals("Proveedor")) {
            columnasVisibles = new String[]{"ID", "Nombre", "A. Paterno", "A. Materno", "Correo", "Teléfono", "Rol", "Empresa", "Horario", "T. Entrega"};
            indicesColumnas = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        } else if (filtroRol.equals("Cliente")) {
            columnasVisibles = new String[]{"ID", "Nombre", "A. Paterno", "A. Materno", "Correo", "Teléfono", "Rol", "N° Interno", "Referencia"};
            indicesColumnas = new int[]{0, 1, 2, 3, 4, 5, 6, 15, 16};
        } else if (filtroRol.equals("Empleado") || filtroRol.equals("Administrador")) {
            columnasVisibles = new String[]{"ID", "Nombre", "A. Paterno", "A. Materno", "Correo", "Teléfono", "Rol", "CUP", "RFC", "Tipo Cargo", "Salario"};
            indicesColumnas = new int[]{0, 1, 2, 3, 4, 5, 6, 10, 11, 13, 14};
        } else {
            columnasVisibles = new String[]{"ID", "Nombre", "A. Paterno", "A. Materno", "Correo", "Teléfono", "Rol"};
            indicesColumnas = new int[]{0, 1, 2, 3, 4, 5, 6};
        }

        modeloTabla = new DefaultTableModel(null, columnasVisibles) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        for (Object[] filaCruda : datosCrudos) {
            Object[] filaVisible = new Object[indicesColumnas.length];
            for (int i = 0; i < indicesColumnas.length; i++) {
                int indiceReal = indicesColumnas[i];
                if (indiceReal < filaCruda.length) {
                    filaVisible[i] = filaCruda[indiceReal];
                } else {
                    filaVisible[i] = "";
                }
            }
            modeloTabla.addRow(filaVisible);
        }

        tabla.setModel(modeloTabla);
        sorter = new TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(sorter);
        buscar();
    }

    private void buscar() {
        String texto = txtBuscar.getText();
        if (texto.equals(PLACEHOLDER) || texto.trim().isEmpty()) {
            if (sorter != null) sorter.setRowFilter(null);
        } else {
            if (sorter != null) sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto.trim()));
        }
    }

    // ==========================================
    // FLUJO DE PASOS
    // ==========================================
    private void irAtras() {
        if (pasoActual > 0 && pasoActual < 3) { 
            pasoActual--;
            actualizarPaso();
        }
    }

    private void irSiguienteOGuardar() {
        if (pasoActual < 3) {
            if (!validarPasoActual()) return;
            if (pasoActual == 1) {
                rolSeleccionado = panelContacto.getEstado();
            }
            pasoActual++;
            actualizarPaso();
        } else {
            if (!validarPasoExtra()) return;
            guardarTodo();
        }
    }

    private void actualizarPaso() {
        switch (pasoActual) {
            case 0 -> {
                cardFormularios.show(panelFormularios, "PERSONALES");
                lblPaso.setText("Paso 1 de 4: Datos Personales");
                btnAtras.setEnabled(false);
                btnSiguienteGuardar.setText("Siguiente →");
            }
            case 1 -> {
                cardFormularios.show(panelFormularios, "CONTACTO");
                lblPaso.setText("Paso 2 de 4: Contacto y Rol");
                btnAtras.setEnabled(true);
                btnSiguienteGuardar.setText("Siguiente →");
            }
            case 2 -> {
                cardFormularios.show(panelFormularios, "DOMICILIO");
                lblPaso.setText("Paso 3 de 4: Domicilio");
                btnAtras.setEnabled(true);
                btnSiguienteGuardar.setText("Siguiente →");
            }
            case 3 -> {
                if (rolSeleccionado.equalsIgnoreCase("Proveedor")) {
                    cardFormularios.show(panelFormularios, "PROVEEDOR");
                    lblPaso.setText("Paso 4 de 4: Datos del Proveedor");
                } else if (rolSeleccionado.equalsIgnoreCase("Administrador") || rolSeleccionado.equalsIgnoreCase("Empleado")) {
                    cardFormularios.show(panelFormularios, "CARGO");
                    lblPaso.setText("Paso 4 de 4: Datos Laborales");
                } else {
                    cardFormularios.show(panelFormularios, "CLIENTE");
                    lblPaso.setText("Paso 4 de 4: Datos del Cliente");
                }
                btnAtras.setEnabled(false); 
                btnSiguienteGuardar.setText(modoEdicion ? "Actualizar Persona" : "Guardar Persona");
            }
        }
    }

    private boolean validarPasoActual() {
        List<String> faltantes = new ArrayList<>();
        switch (pasoActual) {
            case 0 -> {
                if (vacio(panelPersonales.getNombre()))     faltantes.add("Nombre");
                if (vacio(panelPersonales.getApPaterno()))  faltantes.add("Apellido Paterno");
                if (vacio(panelPersonales.getApMaterno()))  faltantes.add("Apellido Materno");
            }
            case 1 -> {
                if (vacio(panelContacto.getCorreo()))   faltantes.add("Correo");
                if (vacio(panelContacto.getTelefono())) faltantes.add("Teléfono");
                if (vacio(panelContacto.getCelular()))  faltantes.add("Celular");
                if (vacio(panelContacto.getEstado()))   faltantes.add("Rol");
            }
            case 2 -> {
                if (vacio(panelDomicilio.getCodPostal())) faltantes.add("Código Postal");
                if (vacio(panelDomicilio.getCalle()))     faltantes.add("Calle");
                if (vacio(panelDomicilio.getNumero()))    faltantes.add("Número");
                if (vacio(panelDomicilio.getColonia()))   faltantes.add("Colonia");
                if (vacio(panelDomicilio.getLocalidad())) faltantes.add("Localidad");
                if (vacio(panelDomicilio.getMunicipio())) faltantes.add("Municipio");
            }
        }
        if (!faltantes.isEmpty()) {
            mostrarFaltantes(faltantes);
            return false;
        }
        return true;
    }

    private boolean validarPasoExtra() {
        List<String> faltantes = new ArrayList<>();
        if (rolSeleccionado.equalsIgnoreCase("Proveedor")) {
            if (vacio(formProveedor.getEmpresa())) faltantes.add("Empresa");
            if (vacio(formProveedor.getHorario())) faltantes.add("Horario");
        } else if (rolSeleccionado.equalsIgnoreCase("Administrador") || rolSeleccionado.equalsIgnoreCase("Empleado")) {
            if (vacio(formCargo.getCup())) faltantes.add("CUP");
            if (vacio(formCargo.getRfc())) faltantes.add("RFC");
            if (vacio(formCargo.getContrasena())) faltantes.add("Contraseña");
            if (vacio(formCargo.getSalario())) faltantes.add("Salario");
        } else {
            if (vacio(formCliente.getNumeroInterno())) faltantes.add("Número Interno");
        }
        if (!faltantes.isEmpty()) {
            mostrarFaltantes(faltantes);
            return false;
        }
        return true;
    }

    private boolean vacio(String s) {
        return s == null || s.trim().isEmpty();
    }

    private void mostrarFaltantes(List<String> faltantes) {
        StringBuilder sb = new StringBuilder("Debes completar los siguientes campos:\n\n");
        for (String f : faltantes) sb.append("  • ").append(f).append("\n");
        JOptionPane.showMessageDialog(this, sb.toString(), "Campos incompletos", JOptionPane.WARNING_MESSAGE);
    }

    private void guardarTodo() {
        if (modoEdicion) {
            actualizarTodo();
            return;
        }

        Usuario u = new Usuario();
        u.setNombre(panelPersonales.getNombre().trim());
        u.setApPaterno(panelPersonales.getApPaterno().trim());
        u.setApMaterno(panelPersonales.getApMaterno().trim());
        u.setTelefono(panelContacto.getTelefono().trim());
        u.setCelular(panelContacto.getCelular().trim());
        u.setCorreo(panelContacto.getCorreo().trim());
        u.setEstado(rolSeleccionado.trim());
        u.setCodPostal(panelDomicilio.getCodPostal().trim());
        u.setCalle(panelDomicilio.getCalle().trim());
        u.setNumeroDomicilio(panelDomicilio.getNumero().trim());
        u.setColonia(panelDomicilio.getColonia().trim());
        u.setLocalidad(panelDomicilio.getLocalidad().trim());
        u.setMunicipio(panelDomicilio.getMunicipio().trim());

        int idGenerado = dao.insertarYObtenerID(u);
        if (idGenerado == -1) {
            JOptionPane.showMessageDialog(this, "Error al registrar el usuario base.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean exitoExtra = false;
        if (rolSeleccionado.equalsIgnoreCase("Proveedor")) {
            exitoExtra = daoProveedor.insertar(formProveedor.getEmpresa(), formProveedor.getHorario(), formProveedor.getTipoEntrega(), idGenerado);
        } else if (rolSeleccionado.equalsIgnoreCase("Administrador") || rolSeleccionado.equalsIgnoreCase("Empleado")) {
            try {
                double salario = Double.parseDouble(formCargo.getSalario());
                exitoExtra = daoCargo.insertarCompleto(formCargo.getCup(), formCargo.getRfc(), idGenerado, formCargo.getContrasena(), formCargo.getTipoCargoSeleccionado(), formCargo.getDescripcion(), salario);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El salario debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
                return; 
            }
        } else {
            exitoExtra = daoCliente.insertar(formCliente.getNumeroInterno(), formCliente.getReferencia(), idGenerado);
        }

        if (exitoExtra) {
            JOptionPane.showMessageDialog(this, "¡Persona registrada con éxito!");
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario guardado, pero hubo un error con sus datos extra.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            limpiarFormulario();
        }
    }

    private void actualizarTodo() {
        Usuario u = new Usuario();
        u.setIdUsuario(idEdicion);
        u.setNombre(panelPersonales.getNombre().trim());
        u.setApPaterno(panelPersonales.getApPaterno().trim());
        u.setApMaterno(panelPersonales.getApMaterno().trim());
        u.setTelefono(panelContacto.getTelefono().trim());
        u.setCelular(panelContacto.getCelular().trim());
        u.setCorreo(panelContacto.getCorreo().trim());
        u.setEstado(rolSeleccionado.trim());
        u.setCodPostal(panelDomicilio.getCodPostal().trim());
        u.setCalle(panelDomicilio.getCalle().trim());
        u.setNumeroDomicilio(panelDomicilio.getNumero().trim());
        u.setColonia(panelDomicilio.getColonia().trim());
        u.setLocalidad(panelDomicilio.getLocalidad().trim());
        u.setMunicipio(panelDomicilio.getMunicipio().trim());

        if (!dao.actualizar(u)) {
            JOptionPane.showMessageDialog(this, "Error al actualizar los datos personales.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean exitoExtra = false;
        if (rolSeleccionado.equalsIgnoreCase("Proveedor")) {
            exitoExtra = daoProveedor.actualizarPorIdUsuario(idEdicion, formProveedor.getEmpresa(), formProveedor.getHorario(), formProveedor.getTipoEntrega());
        } else if (rolSeleccionado.equalsIgnoreCase("Administrador") || rolSeleccionado.equalsIgnoreCase("Empleado")) {
            try {
                double salario = Double.parseDouble(formCargo.getSalario());
                exitoExtra = daoCargo.actualizarCompleto(idEdicion, formCargo.getCup(), formCargo.getRfc(), formCargo.getContrasena(), formCargo.getTipoCargoSeleccionado(), formCargo.getDescripcion(), salario);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El salario debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
                return; 
            }
        } else {
            exitoExtra = daoCliente.actualizarPorIdUsuario(idEdicion, formCliente.getNumeroInterno(), formCliente.getReferencia());
        }

        if (exitoExtra) {
            JOptionPane.showMessageDialog(this, "¡Persona actualizada con éxito!");
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario base actualizado, pero hubo un error con sus datos extra.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            limpiarFormulario();
        }
    }

    private void eliminarUsuario() {
        if (idUsuarioSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una persona de la tabla primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar a esta persona?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.eliminar(idUsuarioSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Registro eliminado con éxito.");
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarFormulario() {
        panelPersonales.limpiar();
        panelContacto.limpiar();
        panelDomicilio.limpiar();
        formProveedor.limpiar();
        formCargo.limpiar();
        formCliente.limpiar();
        
        idUsuarioSeleccionado = -1;
        idEdicion = -1;
        modoEdicion = false;
        rolSeleccionado = "";
        tabla.clearSelection();
        
        txtBuscar.setForeground(Color.GRAY);
        txtBuscar.setText(PLACEHOLDER);
        if (sorter != null) sorter.setRowFilter(null);

        cmbFiltroRol.setSelectedIndex(0);
        pasoActual = 0;
        actualizarPaso();
        
        actualizarTablaSegunRol();
    }
}