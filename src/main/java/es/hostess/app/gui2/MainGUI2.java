package es.hostess.app.gui2;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jidesoft.swing.CheckBoxList;
import es.hostess.app.entities.HostsLocal;
import es.hostess.app.entities.HostsSource;
import es.hostess.app.model.HostsLocalModel;
import es.hostess.app.model.HostsSourceModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainGUI2 extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(MainGUI2.class.getName());
    private JPanel mainPanel;
    private JPanel pnlHeader;
    private JPanel pnlHeaderIcons;
    private JPanel pnlHeaderIconsUpdate;
    private JLabel lblUpdateIcon;
    private JPanel pnlDashboard;
    private JTabbedPane tabPanel;
    private JPanel tabPanelHome;
    private JPanel tabPanelSources;
    private JButton btnAddSource;
    private JPanel tabPanelLocal;
    private JPanel tabPanelExclusions;
    private JTextArea txtAreaLocal;
    private JButton btnLocalSave;
    private JButton btnAddExclusion;
    private JScrollPane scrPaneSource;
    private CheckBoxList cblSources;
    private CheckBoxList cblExclusions;
    private JTextPane txtSourceDescription;
    private JTextPane txtExclusionsDescription;
    private JTextPane txtLocalDescription;
    private JButton btnRemoveSource;
    private JButton btnRemoveExclusion;
    private JLabel lblHomeImg;
    private HostsLocal hostLocal;
    private HostsLocalModel hostsLocalModel = new HostsLocalModel();
    private HostsSourceModel hostsSourceModel = new HostsSourceModel();


    private static final int HOVER_IN = 0xB3B5B9;
    private static final int HOVER_OUT = 0xDFE1E5;

    /**
     * Method to change the color of the panel when hovering over it.
     *
     * @param hover The panel being hovered over.
     * @param color The color to change to.
     */
    public void changeColor(JPanel hover, Color color) {
        hover.setBackground(color);
    }

    /**
     * Method to decide the path to the hosts file based on OS.
     * <b>Deprecated</b> as we had to shift the method of writting to hosts file with the use of .bat
     *
     * @return Path of the hosts file.
     */
    public Path findHostsPath() {
        Path hostsFilePath;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            hostsFilePath = Paths.get("C:\\Windows\\System32\\drivers\\etc\\hosts");
        } else {
            hostsFilePath = Paths.get("/etc/hosts");
        }
        return hostsFilePath;
    }

    /**
     * Method to show am informational dialog based on the code provided.
     *
     * @param code The variable which will determine the message that will be provided.
     */
    public void showInformationalDialogue(int code) {
        String message = "";
        if (code == 1) {
            message = "This entry already exists in the list!";
        } else if (code == 2) {
            message = "The URL appears to be invalid!\nPlease check and try again!";
        } else if (code == 3) {
            message = "Hostess will now merge to your hosts file!\nClose this dialogue and wait patiently!";
        } else if (code == 4) {
            message = "Successfully written to hosts file!";
        } else if (code == 5) {
            message = "Loaded initial configurations!";
        }
        String title = "Information";
        int messageType = JOptionPane.INFORMATION_MESSAGE;
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }

    /**
     * Method to download the hostsFile from the URL provided.
     *
     * @param url The URL of from where the list will be downloaded.
     * @return The contents of the list downloaded.
     */
    public String getHostsFile(String url) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
                if (!line.trim().isEmpty()) {
                    response.append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }


    public MainGUI2() {
        setContentPane(mainPanel);
        setTitle("Hostess");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        // Add favicon
        URL iconURL = getClass().getResource("/icons/icons8-stewardess-96.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());

        setVisible(true);

        // Models for CheckBoxList
        DefaultListModel<String> lmdlSources = new DefaultListModel<>();
        cblSources.setModel(lmdlSources);
        DefaultListModel<String> lmdlExclusions = new DefaultListModel<>();
        cblExclusions.setModel(lmdlExclusions);

        //Source retrieval
        List<HostsSource> hostsSourceList = hostsSourceModel.findAll();
        for (HostsSource hostsSource : hostsSourceList) {
            String sourceURL = hostsSource.getSourceURL();
            lmdlSources.addElement(sourceURL);
        }

        //Local retrieval
        hostsLocalModel = new HostsLocalModel();
        hostLocal = hostsLocalModel.getLocal();
        if (hostLocal != null) {
            String localHosts = hostLocal.getLocalHosts();
            txtAreaLocal.setText(localHosts);
        } else {
            hostLocal = new HostsLocal();
        }

        //Exclusion retrieval
        List<HostsLocal> hostsExclusionList = hostsLocalModel.getExclusion();
        for (HostsLocal hostsExclusion : hostsExclusionList) {
            String exclusionURL = hostsExclusion.getLocalHosts();
            lmdlExclusions.addElement(exclusionURL);
        }

        // Loaded message
        showInformationalDialogue(5);

        pnlHeaderIconsUpdate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    showInformationalDialogue(3);
                    compile(lmdlSources, lmdlExclusions);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    JOptionPane.getRootFrame().dispose();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                changeColor(pnlHeaderIconsUpdate, new Color(HOVER_IN));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                changeColor(pnlHeaderIconsUpdate, new Color(HOVER_OUT));
            }
        });
        btnAddSource.addActionListener(e -> {
            String sourceURL = JOptionPane.showInputDialog(null, "Please enter a valid URL: ").toLowerCase().trim();
            LocalDate date = LocalDate.now();
            String hostsFile = "";
            try {
                hostsFile = getHostsFile(sourceURL);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            HostsSource hostsSource = new HostsSource(sourceURL, date, hostsFile);

            //Adding to SourcesList
            if (lmdlSources.contains(sourceURL)) {
                showInformationalDialogue(1);
            } else if (hostsFile.equals("")) {
                showInformationalDialogue(2);
            } else {
                lmdlSources.addElement(sourceURL);
                hostsSourceModel.saveEntry(hostsSource, hostsSource.getId());
            }
        });
        btnAddExclusion.addActionListener(e -> {
            String localURL = JOptionPane.showInputDialog(null, "Please enter a valid URL: ").toLowerCase().trim();
            LocalDate date = LocalDate.now();
            boolean isExclusion = true;
            HostsLocal hostsLocal = new HostsLocal(localURL, isExclusion, date);

            //Adding to ExclusionsList
            if (lmdlExclusions.contains(localURL)) {
                showInformationalDialogue(1);
            } else {
                lmdlExclusions.addElement(localURL);
                hostsLocalModel.saveExclusion(hostsLocal);
            }
        });

        btnLocalSave.addActionListener(e -> {
            String text = txtAreaLocal.getText();
            hostLocal.setLocalHosts(text);
            hostsLocalModel.saveLocal(hostLocal);
        });
        btnRemoveSource.addActionListener(e -> removeSource(lmdlSources));
        btnRemoveExclusion.addActionListener(e -> removeExclusion(lmdlExclusions));
    }

    /**
     * Method to remove the checked sources from the CheckBoxList and the DB.
     *
     * @param lmdlSources The model that the CheckBoxList uses to store the information.
     */
    public void removeSource(DefaultListModel<String> lmdlSources) {
        List<HostsSource> hostsSourceList = hostsSourceModel.findAll();
        int[] checkedSources = cblSources.getCheckBoxListSelectedIndices();

        for (HostsSource hostsSource : hostsSourceList) {
            for (int check : checkedSources) {
                if (lmdlSources.getElementAt(check).equals(hostsSource.getSourceURL())) {
                    hostsSourceModel.deleteById(hostsSource, hostsSource.getId());
                }
            }
        }
        for (int check : checkedSources) {
            lmdlSources.remove(check);
        }
    }

    /**
     * Method to remove the checked exclusion from the CheckBoxList and the DB.
     *
     * @param lmdlExclusions The model that the CheckBoxList uses to store the information.
     */
    public void removeExclusion(DefaultListModel<String> lmdlExclusions) {
        List<HostsLocal> hostsLocalList = hostsLocalModel.getExclusion();
        int[] checkedExclusions = cblExclusions.getCheckBoxListSelectedIndices();

        for (HostsLocal hostsLocal : hostsLocalList) {
            for (int check : checkedExclusions) {
                if (lmdlExclusions.getElementAt(check).equals(hostsLocal.getLocalHosts())) {
                    hostsLocalModel.deleteById(hostsLocal, hostsLocal.getId());
                }
            }
        }
        for (int check : checkedExclusions) {
            lmdlExclusions.remove(check);
        }
    }

    /**
     * Method to iterate through the CheckBoxList and update the entries that have been checked.
     *
     * @param lmdlSources The model that the CheckBoxList uses to store the information.
     * @return The stringBuilder with the hosts compilation to be modified by the next method.
     * @throws IOException
     */
    public StringBuilder addSources(DefaultListModel<String> lmdlSources) throws IOException {
        StringBuilder compiledHostsFile = new StringBuilder();
        List<HostsSource> hostsSourceList = hostsSourceModel.findAll();
        String hostsFile = "";
        int[] checkedSources = cblSources.getCheckBoxListSelectedIndices();
        for (HostsSource hostsSource : hostsSourceList) {
            for (int check : checkedSources) {
                if (lmdlSources.getElementAt(check).equals(hostsSource.getSourceURL())) {
                    //Update DB entry
                    hostsFile = getHostsFile(hostsSource.getSourceURL());
                    hostsSource.setHostsFile(hostsFile);
                    LocalDate date = LocalDate.now();
                    hostsSource.setSourceDate(date);
                    hostsSourceModel.saveEntry(hostsSource, hostsSource.getId());
                }
            }
            compiledHostsFile.append(hostsFile);
        }
        return compiledHostsFile;
    }

    /**
     * Method to iterate through the exclusions and remove those instances from the hosts compilation.
     *
     * @param lmdlExclusions    The model that the CheckBoxList uses to store the information.
     * @param compiledHostsFile The StringBuilder with the compilation hosts from which to eliminate entries.
     * @return The compilation from which the entries have been eliminated so that it can be written to the hosts file on the machine.
     */
    public StringBuilder removeExclusions(DefaultListModel<String> lmdlExclusions, StringBuilder compiledHostsFile) {
        List<HostsLocal> hostsLocalList = hostsLocalModel.getExclusion();
        String toRemove0 = "";
        String toRemove127 = "";
        for (int i = 0; i < lmdlExclusions.getSize(); i++) {
            for (HostsLocal hostsLocal : hostsLocalList) {
                toRemove0 = "0.0.0.0 " + hostsLocal.getLocalHosts();
                toRemove127 = "127.0.0.1 " + hostsLocal.getLocalHosts();
                for (int j = compiledHostsFile.indexOf(toRemove0); j != -1; j = compiledHostsFile.indexOf(toRemove0, j + toRemove0.length())) {
                    compiledHostsFile.delete(j, j + toRemove0.length());
                }
                for (int j = compiledHostsFile.indexOf(toRemove127); j != -1; j = compiledHostsFile.indexOf(toRemove127, j + toRemove127.length())) {
                    compiledHostsFile.delete(j, j + toRemove127.length());
                }
            }
        }
        return compiledHostsFile;
    }

    /**
     * Method to compile the information from the tabs, write to the hosts file and update DB, if necessarry.
     *
     * @param URLs from the Sources CheckBoxList.
     * @param URLs from the Exclusions CheckBoxList.
     */
    public void compile(DefaultListModel<String> lmdlSources, DefaultListModel<String> lmdlExclusions) throws IOException {
        //TODO show dialog when no sources have been selected.

        StringBuilder compiledHostsFile = new StringBuilder();

        //Add Local first for ease of reviewing.
        compiledHostsFile.append(txtAreaLocal.getText() + "\n");

        //Add sources
        compiledHostsFile.append(addSources(lmdlSources));

        //Remove Exclusions
        compiledHostsFile = removeExclusions(lmdlExclusions, compiledHostsFile);

        // Add compiled hosts to DB for backups
        //TODO As of now there's no way to retrieve them so I'm commenting this part to not saturate the DB with useless data.
//        HostsCompiledModel hostsCompiledModel = new HostsCompiledModel();
//        HostsCompiled hostsCompiled = new HostsCompiled();
//        hostsCompiled.setCompiledHosts(String.valueOf(compiledHostsFile));
//        LocalDate date = LocalDate.now();
//        hostsCompiled.setLastUpdate(date);
//        hostsCompiledModel.saveEntry(hostsCompiled, hostsCompiled.getId());

        // Clean temp folder of remnants to avoid breaking the program
        File[] files = new File(System.getProperty("java.io.tmpdir")).listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().startsWith("hosts")) {
                file.delete();
            }
        }

        // Create temp file
        Path tempFile = Files.createTempFile("hosts", "");

        // write to hosts in temp folder
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(tempFile), true))) {
            // Iterate through each line in the StringBuilder
            for (String line : compiledHostsFile.toString().split("\n")) {
                if (!line.isEmpty()) {
                    // Append a newline character if it doesn't end with one
                    line += "\n";
                }
                // Write each line to the BufferedWriter object
                writer.write(line);
            }
        } catch (IOException e) {
            LOGGER.severe("Error writing to the hosts file: " + e.getMessage());

        }

        // Specify the path to the .bat file
        InputStream batFilePath = MainGUI2.class.getClassLoader().getResourceAsStream("hostess.bat");

        // Move .bat to temp folder
        String appDataPath = System.getenv("APPDATA");
        String destinationPath = appDataPath + "\\hostess";
        String batPath = destinationPath + "\\hostess.bat";

        if (Files.notExists(Paths.get(batPath))) {
            Files.copy(batFilePath, Paths.get(batPath), StandardCopyOption.REPLACE_EXISTING);
        }

        // Execute the .bat file using the Runtime class
        Process process = Runtime.getRuntime().exec(batPath);
        // Wait for the process to finish
        try {
            int exitCode = process.waitFor();
            LOGGER.log(Level.INFO, () -> ("Process exit code: " + exitCode));
            showInformationalDialogue(4);

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MainGUI2();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(-13882324));
        mainPanel.setPreferredSize(new Dimension(500, 300));
        pnlHeader = new JPanel();
        pnlHeader.setLayout(new BorderLayout(0, 0));
        pnlHeader.setBackground(new Color(-2104859));
        pnlHeader.setForeground(new Color(-2104859));
        pnlHeader.setPreferredSize(new Dimension(500, 50));
        mainPanel.add(pnlHeader, BorderLayout.NORTH);
        pnlHeader.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        pnlHeaderIcons = new JPanel();
        pnlHeaderIcons.setLayout(new GridBagLayout());
        pnlHeaderIcons.setBackground(new Color(-13882324));
        pnlHeaderIcons.setPreferredSize(new Dimension(150, 150));
        pnlHeader.add(pnlHeaderIcons, BorderLayout.EAST);
        pnlHeaderIconsUpdate = new JPanel();
        pnlHeaderIconsUpdate.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        pnlHeaderIconsUpdate.setBackground(new Color(-2104859));
        pnlHeaderIconsUpdate.setToolTipText("Press to merge all tabs and write to hosts file!");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        pnlHeaderIcons.add(pnlHeaderIconsUpdate, gbc);
        pnlHeaderIconsUpdate.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        lblUpdateIcon = new JLabel();
        lblUpdateIcon.setIcon(new ImageIcon(getClass().getResource("/icons/icons8-actualizar-24.png")));
        lblUpdateIcon.setText("");
        pnlHeaderIconsUpdate.add(lblUpdateIcon, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setForeground(new Color(-13882324));
        label1.setText("Update");
        pnlHeaderIconsUpdate.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pnlDashboard = new JPanel();
        pnlDashboard.setLayout(new CardLayout(0, 0));
        pnlDashboard.setBackground(new Color(-2104859));
        pnlDashboard.setDoubleBuffered(true);
        pnlDashboard.setEnabled(true);
        pnlDashboard.setPreferredSize(new Dimension(425, 200));
        pnlDashboard.setVisible(true);
        mainPanel.add(pnlDashboard, BorderLayout.CENTER);
        tabPanel = new JTabbedPane();
        tabPanel.setTabPlacement(2);
        tabPanel.setToolTipText("");
        pnlDashboard.add(tabPanel, "Card1");
        tabPanelHome = new JPanel();
        tabPanelHome.setLayout(new GridBagLayout());
        tabPanel.addTab("Home", null, tabPanelHome, "No place like 127.0.0.1");
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tabPanelHome.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        tabPanelHome.add(spacer2, gbc);
        lblHomeImg = new JLabel();
        lblHomeImg.setAutoscrolls(false);
        lblHomeImg.setIcon(new ImageIcon(getClass().getResource("/img/home.png")));
        lblHomeImg.setMaximumSize(new Dimension(500, 300));
        lblHomeImg.setMinimumSize(new Dimension(500, 300));
        lblHomeImg.setOpaque(true);
        lblHomeImg.setPreferredSize(new Dimension(400, 250));
        lblHomeImg.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        tabPanelHome.add(lblHomeImg, gbc);
        tabPanelSources = new JPanel();
        tabPanelSources.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabPanel.addTab("Sources", null, tabPanelSources, "Tab to add Source URLs do be downloaded.");
        scrPaneSource = new JScrollPane();
        tabPanelSources.add(scrPaneSource, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(200, -1), null, null, 0, false));
        cblSources = new CheckBoxList();
        cblSources.setClickInCheckBoxOnly(false);
        cblSources.setFixedCellHeight(-1);
        cblSources.setFixedCellWidth(500);
        scrPaneSource.setViewportView(cblSources);
        btnAddSource = new JButton();
        btnAddSource.setText("+ Add Source");
        tabPanelSources.add(btnAddSource, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        tabPanelSources.add(spacer3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        tabPanelSources.add(spacer4, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txtSourceDescription = new JTextPane();
        txtSourceDescription.setEditable(false);
        txtSourceDescription.setText("Add source URLs to be downloaded and merged like:\nhttps://winhelp2002.mvps.org/hosts.txt");
        tabPanelSources.add(txtSourceDescription, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        btnRemoveSource = new JButton();
        btnRemoveSource.setText("- Remove Source");
        tabPanelSources.add(btnRemoveSource, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tabPanelLocal = new JPanel();
        tabPanelLocal.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabPanel.addTab("Local", null, tabPanelLocal, "Add current hosts file and/or anything else you want to block.");
        final JScrollPane scrollPane1 = new JScrollPane();
        tabPanelLocal.add(scrollPane1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txtAreaLocal = new JTextArea();
        txtAreaLocal.setColumns(40);
        txtAreaLocal.setPreferredSize(new Dimension(320, 306));
        txtAreaLocal.setRows(15);
        scrollPane1.setViewportView(txtAreaLocal);
        btnLocalSave = new JButton();
        btnLocalSave.setText("Save");
        tabPanelLocal.add(btnLocalSave, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        tabPanelLocal.add(spacer5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        tabPanelLocal.add(spacer6, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txtLocalDescription = new JTextPane();
        txtLocalDescription.setText("Use the standard hosts formats:\n0.0.0.0 somethingToBlock.com\n127.0.0.1 somethingElse.es");
        tabPanelLocal.add(txtLocalDescription, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        tabPanelExclusions = new JPanel();
        tabPanelExclusions.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabPanel.addTab("Exclusions", null, tabPanelExclusions, "URLs to remove from the downloaded lists.");
        final JScrollPane scrollPane2 = new JScrollPane();
        tabPanelExclusions.add(scrollPane2, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(200, -1), null, null, 0, false));
        cblExclusions = new CheckBoxList();
        cblExclusions.setClickInCheckBoxOnly(false);
        cblExclusions.setFixedCellWidth(500);
        scrollPane2.setViewportView(cblExclusions);
        btnAddExclusion = new JButton();
        btnAddExclusion.setText("+ Add Exclusion");
        tabPanelExclusions.add(btnAddExclusion, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        tabPanelExclusions.add(spacer7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        tabPanelExclusions.add(spacer8, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txtExclusionsDescription = new JTextPane();
        txtExclusionsDescription.setEditable(false);
        txtExclusionsDescription.setText("Just add URL to exclude:\ngoodsite.com\nALL will be excluded regardless of checks!");
        tabPanelExclusions.add(txtExclusionsDescription, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        btnRemoveExclusion = new JButton();
        btnRemoveExclusion.setText("- Remove Exclusion");
        tabPanelExclusions.add(btnRemoveExclusion, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}

