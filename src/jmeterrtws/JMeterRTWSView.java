/*
 * JMeterRTWSView.java
 */
package jmeterrtws;

import CodeBreak.VisitingClassMethod.MethodVisitor;



import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.*;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.io.FileInputStream;
import javax.swing.Timer;
import javax.swing.JDialog;
import javax.swing.filechooser.FileNameExtensionFilter;
import BuildingDifferenceWSDL.Construction;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;



import com.qarks.util.Cancellable;
import com.qarks.util.CancellableImpl;
import com.qarks.util.files.diff.DirContentStatus;
import com.qarks.util.files.diff.DirDiffResult;
import com.qarks.util.files.diff.DirContentStatus.Status;
import com.qarks.util.files.diff.core.FolderComparator;
import com.qarks.util.files.diff.ui.DiffProvider;
import com.qarks.util.files.diff.ui.FileDiffDialog;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import javax.swing.DefaultListModel;

/**
 * The application's main frame.
 */
public class JMeterRTWSView extends FrameView {

    private SAXTreeBuilder saxTree2 = null;
    private SAXTreeBuilder saxTree3 = null;
    private File WSDL1;
    private File WSDL2;
    File WSDL3;
    File WSDL4, WSDL5;
    File testSuiteFile1 = null, testSuiteFile2 = null, testSuiteFile3 = null, testSuiteFile4 = null, testSuiteFile5 = null;
    Object X;
    String diffOp[] = new String[100];
    String reduceOp[] = new String[100];
    String combinedUniqueOp[] = new String[100];
    String inputList2[] = new String[100];
    int j = 0, x;
    Object Y, Z;
    String inputList3[] = new String[100];
    String inputList4[] = new String[100];
    String unitOp[] = new String[100];
    String buildTestCase[] = new String[100];
    int I = 0, J = 0;
    String TC[][] = new String[100][100];
    String TestCase = "";

    public JMeterRTWSView(SingleFrameApplication app) {
        super(app);

        initComponents();

        listModel = new DefaultListModel();
        modifiedFiles = new JList(listModel);
        leftRoot = new DefaultMutableTreeNode();
        leftTreeModel = new DefaultTreeModel(leftRoot);
        leftTree = new JTree(leftTreeModel);
        leftTree.setRootVisible(false);
        leftTree.setShowsRootHandles(true);
        leftScroll = new JScrollPane(leftTree);
        //leftTree.setCellRenderer(renderer);
        rightRoot = new DefaultMutableTreeNode();
        rightTreeModel = new DefaultTreeModel(rightRoot);
        rightTree = new JTree(rightTreeModel);
        rightTree.setRootVisible(false);
        rightTree.setShowsRootHandles(true);
        rightScroll = new JScrollPane(rightTree);
        //rightTree.setCellRenderer(renderer);
        compareFolders = new JButton("Changed Operations ");
        compareFiles = new JButton("Compare Individual Operations changes");
        compareFiles.setEnabled(false);
        leftBorder = new TitledBorder("Old Operations");
        rightBorder = new TitledBorder("New Operations");

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
        toolbar.setBorder(new EmptyBorder(2, 2, 2, 2));
        toolbar.add(compareFolders);
        toolbar.add(compareFiles);
        //JPanel mainPanel = new JPanel(new BorderLayout(5,5));
        JPanel leftPanel = new JPanel(new BorderLayout(2, 2));
        leftPanel.setBorder(leftBorder);
        leftPanel.add(leftScroll, BorderLayout.CENTER);
        JPanel rightPanel = new JPanel(new BorderLayout(3, 3));
        rightPanel.setBorder(rightBorder);
        rightPanel.add(rightScroll, BorderLayout.CENTER);
        jPanel4.setBorder(new EmptyBorder(0, 5, 5, 5));
        JPanel treesPanel = new JPanel(new GridLayout(1, 1, 2, 2));
        treesPanel.add(leftPanel, 0);
        treesPanel.add(rightPanel, 1);
        //JPanel listPanel = new JPanel(new BorderLayout(1, 1));
        //listPanel.add(new JScrollPane(modifiedFiles), BorderLayout.CENTER);
        //listPanel.setBorder(new TitledBorder("Modified Operations"));


        jPanel4.setLayout(new GridLayout(2, 1, 2, 2));
        jPanel4.add(treesPanel, BorderLayout.NORTH);
        //jPanel4.add(listPanel, BorderLayout.SOUTH);
        //mainPanel.setLayout(new BorderLayout(5, 5));
        jPanel4.add(toolbar, BorderLayout.NORTH);
        //mainPanel.add(toolbar, BorderLayout.CENTER);

        compareFolders.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                compareFolders();
            }

            private void compareFolders() {
                Window window = SwingUtilities.getWindowAncestor(mainPanel);
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setCurrentDirectory(new File(currentDir));
                fileChooser.setDialogTitle("Select first folder");

                if (fileChooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
                    File leftFolder = fileChooser.getSelectedFile();
                    currentDir = leftFolder.getParent();
                    fileChooser.setDialogTitle("Select second folder");
                    if (fileChooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
                        File rightFolder = fileChooser.getSelectedFile();
                        compareFolders(leftFolder, rightFolder);
                    }
                }
            }

            private void compareFolders(File leftFolder, File rightFolder) {
                if (comparator != null) {
                    comparator.cancel();
                }
                comparator = new FolderComparatorMonitor(leftFolder, rightFolder);
                comparator.start();
                mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                compareFolders.setEnabled(false);
            }
        });
        compareFiles.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                compareFiles();
            }

            private void compareFiles() {
                TreePath leftPath = leftTree.getSelectionModel().getSelectionPath();
                if (leftPath != null) {
                    DefaultMutableTreeNode leftNode = (DefaultMutableTreeNode) leftPath.getLastPathComponent();
                    if (leftNode != null) {
                        DirContentStatus leftStatus = (DirContentStatus) leftNode.getUserObject();
                        compareFiles(leftStatus);
                    }
                } else {
                    TreePath rightPath = rightTree.getSelectionModel().getSelectionPath();
                    if (rightPath != null) {
                        DefaultMutableTreeNode rightNode = (DefaultMutableTreeNode) rightPath.getLastPathComponent();
                        if (rightNode != null) {
                            DirContentStatus rightStatus = (DirContentStatus) rightNode.getUserObject();
                            compareFiles(rightStatus);
                        }
                    } else if (modifiedFiles.getSelectedValue() != null) {
                        DirContentStatus content = (DirContentStatus) modifiedFiles.getSelectedValue();
                        compareFiles(content);
                    }
                }
            }

            private void compareFiles(DirContentStatus content) {
                if (diffProvider != null) {
                    diffProvider.compareFiles(mainPanel, content.getFile(), content.getOtherFile());
                } else {
                    Window window = SwingUtilities.getWindowAncestor(mainPanel);
                    FileDiffDialog.showDiffDialog(window, content.getFile(), content.getOtherFile());
                }
            }
        });
        leftTree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent arg0) {
                if (!selection) {
                    selection = true;
                    rightTree.getSelectionModel().setSelectionPath(null);
                    modifiedFiles.getSelectionModel().clearSelection();
                    checkSelection();
                    selection = false;
                }
            }
        });
        modifiedFiles.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!selection) {
                    selection = true;
                    rightTree.getSelectionModel().setSelectionPath(null);
                    leftTree.getSelectionModel().setSelectionPath(null);
                    checkSelection();
                    selection = false;
                }
            }
        });

        rightTree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent arg0) {
                if (!selection) {
                    selection = true;
                    leftTree.getSelectionModel().setSelectionPath(null);
                    modifiedFiles.getSelectionModel().clearSelection();
                    checkSelection();
                    selection = false;
                }
            }
        });

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = JMeterRTWSApp.getApplication().getMainFrame();
            aboutBox = new JMeterRTWSAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        JMeterRTWSApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        mainPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jTextField7 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        scrollPane1 = new java.awt.ScrollPane();
        scrollPane2 = new java.awt.ScrollPane();
        scrollPane3 = new java.awt.ScrollPane();
        jButton18 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        scrollPane5 = new java.awt.ScrollPane();
        scrollPane6 = new java.awt.ScrollPane();
        jButton26 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        mainPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton4 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jTextField4 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jTextField5 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jTextField9 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        scrollPane4 = new java.awt.ScrollPane();
        jLabel17 = new javax.swing.JLabel();
        scrollPane7 = new java.awt.ScrollPane();
        scrollPane8 = new java.awt.ScrollPane();
        jButton27 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jButton16 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jButton17 = new javax.swing.JButton();
        scrollPane9 = new java.awt.ScrollPane();
        jLabel19 = new javax.swing.JLabel();
        scrollPane10 = new java.awt.ScrollPane();
        scrollPane11 = new java.awt.ScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jButton28 = new javax.swing.JButton();
        scrollPane14 = new java.awt.ScrollPane();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTextField18 = new javax.swing.JTextField();
        jButton29 = new javax.swing.JButton();
        jTextField19 = new javax.swing.JTextField();
        jButton30 = new javax.swing.JButton();
        jTextField20 = new javax.swing.JTextField();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jTextField21 = new javax.swing.JTextField();
        mainPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jButton19 = new javax.swing.JButton();
        jTextField16 = new javax.swing.JTextField();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList4 = new javax.swing.JList();
        scrollPane12 = new java.awt.ScrollPane();
        scrollPane13 = new java.awt.ScrollPane();
        jTextField17 = new javax.swing.JTextField();
        jButton22 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList5 = new javax.swing.JList();
        jScrollPane6 = new javax.swing.JScrollPane();
        jList6 = new javax.swing.JList();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jButton25 = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        mainPanel1.setName("mainPanel1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(jmeterrtws.JMeterRTWSApp.class).getContext().getResourceMap(JMeterRTWSView.class);
        jTextField1.setText(resourceMap.getString("jTextField1.text")); // NOI18N
        jTextField1.setName("jTextField1"); // NOI18N

        jTextField2.setText(resourceMap.getString("jTextField2.text")); // NOI18N
        jTextField2.setName("jTextField2"); // NOI18N

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTextField3.setText(resourceMap.getString("jTextField3.text")); // NOI18N
        jTextField3.setName("jTextField3"); // NOI18N

        jTextField6.setText(resourceMap.getString("jTextField6.text")); // NOI18N
        jTextField6.setName("jTextField6"); // NOI18N

        jButton8.setText(resourceMap.getString("jButton8.text")); // NOI18N
        jButton8.setName("jButton8"); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jTextField7.setText(resourceMap.getString("jTextField7.text")); // NOI18N
        jTextField7.setName("jTextField7"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        scrollPane1.setName("scrollPane1"); // NOI18N

        scrollPane2.setName("scrollPane2"); // NOI18N

        scrollPane3.setName("scrollPane3"); // NOI18N

        jButton18.setText(resourceMap.getString("jButton18.text")); // NOI18N
        jButton18.setName("jButton18"); // NOI18N
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        scrollPane5.setName("scrollPane5"); // NOI18N

        scrollPane6.setName("scrollPane6"); // NOI18N

        jButton26.setText(resourceMap.getString("jButton26.text")); // NOI18N
        jButton26.setName("jButton26"); // NOI18N
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanel1Layout = new javax.swing.GroupLayout(mainPanel1);
        mainPanel1.setLayout(mainPanel1Layout);
        mainPanel1Layout.setHorizontalGroup(
            mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel1Layout.createSequentialGroup()
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addComponent(jButton8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField2)
                            .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanel1Layout.createSequentialGroup()
                                .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3))
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(mainPanel1Layout.createSequentialGroup()
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(scrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(scrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(scrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(scrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(scrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        mainPanel1Layout.setVerticalGroup(
            mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton18)
                    .addComponent(jButton26)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel14)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(scrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                    .addComponent(scrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                    .addComponent(scrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                    .addComponent(scrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                    .addComponent(scrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(mainPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(mainPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        mainPanel2.setName("mainPanel2"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jList1.setName("jList1"); // NOI18N
        jScrollPane1.setViewportView(jList1);

        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList2.setName("jList2"); // NOI18N
        jScrollPane2.setViewportView(jList2);

        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setName("jButton5"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText(resourceMap.getString("jButton6.text")); // NOI18N
        jButton6.setName("jButton6"); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jTextField4.setText(resourceMap.getString("jTextField4.text")); // NOI18N
        jTextField4.setName("jTextField4"); // NOI18N

        jButton7.setText(resourceMap.getString("jButton7.text")); // NOI18N
        jButton7.setName("jButton7"); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jTextField5.setText(resourceMap.getString("jTextField5.text")); // NOI18N
        jTextField5.setName("jTextField5"); // NOI18N

        jTextField8.setText(resourceMap.getString("jTextField8.text")); // NOI18N
        jTextField8.setName("jTextField8"); // NOI18N

        jButton10.setText(resourceMap.getString("jButton10.text")); // NOI18N
        jButton10.setName("jButton10"); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jTextField9.setText(resourceMap.getString("jTextField9.text")); // NOI18N
        jTextField9.setName("jTextField9"); // NOI18N

        jButton11.setText(resourceMap.getString("jButton11.text")); // NOI18N
        jButton11.setName("jButton11"); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        scrollPane4.setName("scrollPane4"); // NOI18N

        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        scrollPane7.setName("scrollPane7"); // NOI18N

        scrollPane8.setName("scrollPane8"); // NOI18N

        jButton27.setText(resourceMap.getString("jButton27.text")); // NOI18N
        jButton27.setName("jButton27"); // NOI18N
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanel2Layout = new javax.swing.GroupLayout(mainPanel2);
        mainPanel2.setLayout(mainPanel2Layout);
        mainPanel2Layout.setHorizontalGroup(
            mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel2Layout.createSequentialGroup()
                        .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanel2Layout.createSequentialGroup()
                                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                                    .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)))
                            .addGroup(mainPanel2Layout.createSequentialGroup()
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(78, 78, 78)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                            .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                        .addGap(33, 33, 33)))
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField9, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton5)
                            .addComponent(jButton11)))
                    .addGroup(mainPanel2Layout.createSequentialGroup()
                        .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(mainPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(153, 153, 153))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(scrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton27))
                            .addComponent(scrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(scrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(70, 70, 70))
        );
        mainPanel2Layout.setVerticalGroup(
            mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel2Layout.createSequentialGroup()
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel2Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton6)
                            .addComponent(jButton4)))
                    .addGroup(mainPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton7)
                                .addComponent(jButton5)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, 0)
                        .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton10)
                            .addComponent(jButton11)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel17)
                            .addComponent(jButton27)
                            .addComponent(jLabel5))))
                .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(mainPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1))
                    .addGroup(mainPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jScrollPane2))
                    .addGroup(mainPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(scrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(scrollPane8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(scrollPane7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE))))
                .addContainerGap(87, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(mainPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(mainPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N

        jTextField10.setName("jTextField10"); // NOI18N

        jTextField11.setName("jTextField11"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jButton12.setText(resourceMap.getString("jButton12.text")); // NOI18N
        jButton12.setName("jButton12"); // NOI18N
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText(resourceMap.getString("jButton13.text")); // NOI18N
        jButton13.setName("jButton13"); // NOI18N
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        jTextField12.setName("jTextField12"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jTextField13.setText(resourceMap.getString("jTextField13.text")); // NOI18N
        jTextField13.setName("jTextField13"); // NOI18N

        jButton14.setText(resourceMap.getString("jButton14.text")); // NOI18N
        jButton14.setName("jButton14"); // NOI18N
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setText(resourceMap.getString("jButton15.text")); // NOI18N
        jButton15.setName("jButton15"); // NOI18N
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        jTextField14.setText(resourceMap.getString("jTextField14.text")); // NOI18N
        jTextField14.setName("jTextField14"); // NOI18N

        jButton16.setText(resourceMap.getString("jButton16.text")); // NOI18N
        jButton16.setName("jButton16"); // NOI18N
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        jTextField15.setText(resourceMap.getString("jTextField15.text")); // NOI18N
        jTextField15.setName("jTextField15"); // NOI18N

        jButton17.setText(resourceMap.getString("jButton17.text")); // NOI18N
        jButton17.setName("jButton17"); // NOI18N
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        scrollPane9.setName("scrollPane9"); // NOI18N

        jLabel19.setText(resourceMap.getString("jLabel19.text")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N

        scrollPane10.setName("scrollPane10"); // NOI18N

        scrollPane11.setName("scrollPane11"); // NOI18N

        jPanel4.setFocusable(false);
        jPanel4.setName("jPanel4"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 503, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 144, Short.MAX_VALUE)
        );

        jButton28.setText(resourceMap.getString("jButton28.text")); // NOI18N
        jButton28.setName("jButton28"); // NOI18N
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        scrollPane14.setName("scrollPane14"); // NOI18N

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        jLabel26.setText(resourceMap.getString("jLabel26.text")); // NOI18N
        jLabel26.setName("jLabel26"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8)
                                            .addComponent(jLabel10))
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextField14, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                                            .addComponent(jTextField12, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                                            .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel16)
                                .addGap(87, 87, 87)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel9))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField15, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                                    .addComponent(jTextField13)
                                    .addComponent(jTextField11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton13)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton28)
                                .addGap(60, 60, 60)
                                .addComponent(jLabel26)
                                .addGap(95, 95, 95)
                                .addComponent(jLabel18))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(scrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)))
                .addContainerGap(74, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel10)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(15, 15, 15)
                        .addComponent(jLabel11)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jButton28))
                        .addComponent(jLabel16))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel26)
                        .addComponent(jLabel18)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(scrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(scrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(scrollPane11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(scrollPane10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)))
                .addGap(42, 42, 42))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        jPanel6.setName("jPanel6"); // NOI18N

        jScrollPane7.setName("jScrollPane7"); // NOI18N

        jScrollPane8.setName("jScrollPane8"); // NOI18N

        jScrollPane9.setName("jScrollPane9"); // NOI18N

        jTextField18.setText(resourceMap.getString("jTextField18.text")); // NOI18N
        jTextField18.setName("jTextField18"); // NOI18N

        jButton29.setText(resourceMap.getString("jButton29.text")); // NOI18N
        jButton29.setName("jButton29"); // NOI18N
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jTextField19.setText(resourceMap.getString("jTextField19.text")); // NOI18N
        jTextField19.setName("jTextField19"); // NOI18N

        jButton30.setText(resourceMap.getString("jButton30.text")); // NOI18N
        jButton30.setName("jButton30"); // NOI18N
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        jTextField20.setText(resourceMap.getString("jTextField20.text")); // NOI18N
        jTextField20.setName("jTextField20"); // NOI18N

        jButton31.setText(resourceMap.getString("jButton31.text")); // NOI18N
        jButton31.setName("jButton31"); // NOI18N
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        jButton32.setText(resourceMap.getString("jButton32.text")); // NOI18N
        jButton32.setName("jButton32"); // NOI18N
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        jTextField21.setText(resourceMap.getString("jTextField21.text")); // NOI18N
        jTextField21.setName("jTextField21"); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField20, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jButton29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jButton31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton32)
                            .addComponent(jButton30))))
                .addContainerGap(568, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton29)
                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton30))
                .addGap(2, 2, 2)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton31)
                    .addComponent(jButton32)
                    .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(168, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel6.TabConstraints.tabTitle"), jPanel6); // NOI18N

        mainPanel3.setName("mainPanel3"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jList3.setName("jList3"); // NOI18N
        jScrollPane3.setViewportView(jList3);

        jButton19.setText(resourceMap.getString("jButton19.text")); // NOI18N
        jButton19.setName("jButton19"); // NOI18N
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jTextField16.setText(resourceMap.getString("jTextField16.text")); // NOI18N
        jTextField16.setName("jTextField16"); // NOI18N

        jButton20.setText(resourceMap.getString("jButton20.text")); // NOI18N
        jButton20.setName("jButton20"); // NOI18N
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton21.setText(resourceMap.getString("jButton21.text")); // NOI18N
        jButton21.setName("jButton21"); // NOI18N
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        jList4.setName("jList4"); // NOI18N
        jList4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList4MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jList4);

        scrollPane12.setName("scrollPane12"); // NOI18N

        scrollPane13.setName("scrollPane13"); // NOI18N

        jTextField17.setText(resourceMap.getString("jTextField17.text")); // NOI18N
        jTextField17.setName("jTextField17"); // NOI18N

        jButton22.setText(resourceMap.getString("jButton22.text")); // NOI18N
        jButton22.setName("jButton22"); // NOI18N
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N

        jLabel21.setText(resourceMap.getString("jLabel21.text")); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N

        jLabel22.setText(resourceMap.getString("jLabel22.text")); // NOI18N
        jLabel22.setName("jLabel22"); // NOI18N

        jLabel23.setText(resourceMap.getString("jLabel23.text")); // NOI18N
        jLabel23.setName("jLabel23"); // NOI18N

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        jList5.setName("jList5"); // NOI18N
        jScrollPane5.setViewportView(jList5);

        jScrollPane6.setName("jScrollPane6"); // NOI18N

        jList6.setName("jList6"); // NOI18N
        jScrollPane6.setViewportView(jList6);

        jButton23.setText(resourceMap.getString("jButton23.text")); // NOI18N
        jButton23.setName("jButton23"); // NOI18N
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton24.setText(resourceMap.getString("jButton24.text")); // NOI18N
        jButton24.setName("jButton24"); // NOI18N
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jLabel24.setText(resourceMap.getString("jLabel24.text")); // NOI18N
        jLabel24.setName("jLabel24"); // NOI18N

        jLabel25.setText(resourceMap.getString("jLabel25.text")); // NOI18N
        jLabel25.setName("jLabel25"); // NOI18N

        jButton25.setText(resourceMap.getString("jButton25.text")); // NOI18N
        jButton25.setName("jButton25"); // NOI18N
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanel3Layout = new javax.swing.GroupLayout(mainPanel3);
        mainPanel3.setLayout(mainPanel3Layout);
        mainPanel3Layout.setHorizontalGroup(
            mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton22))
                    .addGroup(mainPanel3Layout.createSequentialGroup()
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton20)
                                    .addComponent(jLabel20))
                                .addGap(93, 93, 93)
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(jButton21))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton23)
                                    .addComponent(jLabel25))
                                .addGap(85, 85, 85)
                                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(mainPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel22)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton25))
                                    .addComponent(jButton24)))
                            .addGroup(mainPanel3Layout.createSequentialGroup()
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(10, 10, 10)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(218, 218, 218))
        );
        mainPanel3Layout.setVerticalGroup(
            mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton19)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton20)
                    .addComponent(jButton21)
                    .addComponent(jButton23)
                    .addComponent(jButton24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel20)
                    .addComponent(jLabel24)
                    .addComponent(jLabel25)
                    .addComponent(jLabel22)
                    .addComponent(jButton25)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(scrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("mainPanel3.TabConstraints.tabTitle"), mainPanel3); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1168, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(jmeterrtws.JMeterRTWSApp.class).getContext().getActionMap(JMeterRTWSView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 1178, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1008, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        //File file = null;
        JFileChooser fileopen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
        fileopen.addChoosableFileFilter(filter);
        int ret = fileopen.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            WSDL1 = fileopen.getSelectedFile();
            jTextField1.setText(WSDL1.getPath());
        }

        SAXTreeBuilder saxTree1 = null;

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(WSDL1);
        saxTree1 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(saxTree1);
            saxParser.parse(new InputSource(new FileInputStream(WSDL1)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }

        JTree tree = new JTree(saxTree1.getTree());
        scrollPane1.add(tree);                       // TODO add your handling code here:
}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        //File file = null;
        JFileChooser fileopen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
        fileopen.addChoosableFileFilter(filter);

        int ret = fileopen.showDialog(null, "Open file");

        if (ret == JFileChooser.APPROVE_OPTION) {
            WSDL2 = fileopen.getSelectedFile();
            jTextField2.setText(WSDL2.getPath());
        }

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(WSDL2);

        saxTree2 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(saxTree2);
            saxParser.parse(new InputSource(new FileInputStream(WSDL2)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }
        JTree tree = new JTree(saxTree2.getTree());
        scrollPane2.add(tree);
}//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Scanner fileToRead;
        boolean y;
        try {
            File file = null;
            JFileChooser fileopen = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
            fileopen.addChoosableFileFilter(filter);
            int ret = fileopen.showDialog(null, "Save file");
            if (ret == JFileChooser.APPROVE_OPTION) {
                file = fileopen.getSelectedFile();
                jTextField3.setText("file:///" + file.getPath());
            }
            Construction differenceWSDL = new Construction();
            //readingpatch111112.Construction differWSDL = new readingpatch111112.Construction();
            String WSDL1name = jTextField1.getText();
            System.out.println(" XXXXXX " + WSDL1name);
            String WSDL2name = jTextField2.getText();
            System.out.println(" XXXXXX " + WSDL2name);
            String diffWSDL = jTextField3.toString();
            String contains = null;

            StringBuffer stringBufferOfData = new StringBuffer();

            try {
                fileToRead = new Scanner(WSDL2); //point the scanner method to a file
                //check if there is a next line and it is not null and then read it in
                for (String line; fileToRead.hasNextLine() && (line = fileToRead.nextLine()) != null;) {
                    stringBufferOfData.append(line).append("\r\n");
                }
                y = stringBufferOfData.toString().contains("wsdl:definition");
                //if (y) {
                diffOp = differenceWSDL.ConstructDIfferenceWSDL(WSDL1, WSDL2, file);

                //} else {
                //  differWSDL.differenceConstruct(WSDL1, WSDL2, file);
                //}

                fileToRead.close();//this is used to release the scanner from file
            } catch (FileNotFoundException ex) {
            }



            // TODO add your handling code here:

            DefaultMutableTreeNode top = new DefaultMutableTreeNode(file);

            saxTree3 = new SAXTreeBuilder(top);

            try {
                SAXParser saxParser = new SAXParser();
                saxParser.setContentHandler(saxTree3);
                saxParser.parse(new InputSource(new FileInputStream(file)));
            } catch (Exception ex) {
                top.add(new DefaultMutableTreeNode(ex.getMessage()));
            }
            JTree tree = new JTree(saxTree3.getTree());
            scrollPane3.add(tree);
        } catch (Exception ex) {
            Logger.getLogger(JMeterRTWSView.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_jButton3ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        JFileChooser fileopen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
        fileopen.addChoosableFileFilter(filter);
        int ret = fileopen.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            testSuiteFile1 = fileopen.getSelectedFile();
            jTextField6.setText(testSuiteFile1.getPath() + File.separator + testSuiteFile1.getName());
        }
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(testSuiteFile1);
        SAXTreeBuilder RWsaxTree3 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(RWsaxTree3);
            saxParser.parse(new InputSource(new FileInputStream(testSuiteFile1)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }
        JTree tree = new JTree(RWsaxTree3.getTree());
        scrollPane5.add(tree);
}//GEN-LAST:event_jButton8ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        File ReduceTS = null;
        JFileChooser fileopen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
        fileopen.addChoosableFileFilter(filter);
        int ret = fileopen.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            ReduceTS = fileopen.getSelectedFile();
            jTextField7.setText(ReduceTS.getPath() + File.separator + ReduceTS.getName());
        }
        String s[] = diffOp;//{"Index", "editFile"};
        ParseJMeterTS.Construction TC = new ParseJMeterTS.Construction();
        try {
            TC.ConstructReduceTC(testSuiteFile1, s, ReduceTS);
        } catch (Exception ex) {
            Logger.getLogger(JMeterRTWSView.class.getName()).log(Level.SEVERE, null, ex);
        }

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(ReduceTS);
        SAXTreeBuilder RWsaxTree3 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(RWsaxTree3);
            saxParser.parse(new InputSource(new FileInputStream(ReduceTS)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }
        JTree tree = new JTree(RWsaxTree3.getTree());
        scrollPane6.add(tree);
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        testSuiteFile4 = testSuiteFile1;
        jTextField16.setText(testSuiteFile4.getPath() + File.separator + testSuiteFile4.getName());
        I = 0;
        J = 0;

        TestCase = "";
        final String inputList[] = new String[100];
        for (int i = 0; i < 100; i++) {
            inputList[i] = null;
            inputList3[i] = null;
            inputList4[i] = null;
            buildTestCase[i] = null;
        }
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                TC[i][j] = null;
            }
        }

        jList4.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });

        jList5.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });

        jList6.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(testSuiteFile4);
        SAXTreeBuilder RWsaxTree3 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(RWsaxTree3);
            saxParser.parse(new InputSource(new FileInputStream(testSuiteFile4)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }
        JTree tree = new JTree(RWsaxTree3.getTree());
        scrollPane12.add(tree);
        ParseJMeterTS.Construction obj = new ParseJMeterTS.Construction();

        //String [] output = null;
        try {
            TC = obj.testCases(testSuiteFile4);
        } catch (Exception ex) {
            Logger.getLogger(JMeterRTWSView.class.getName()).log(Level.SEVERE, null, ex);
        }
        final String[] output;
        output = new String[100];
        int j = 0;

        for (; TC[j][0] != null; j++) {
            output[j] = TC[j][0];
            //System.reduceOp.println(TC[j]);
        }

        jList3.setModel(new javax.swing.AbstractListModel() {

            String[] strings = output;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });

        inputList3 = diffOp;
        jList4.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList3;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
}//GEN-LAST:event_jButton26ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int x = jList1.getAnchorSelectionIndex();
        X = jList1.getModel().getElementAt(x);

        System.out.print(X.toString());
        inputList2[j] = X.toString();
        j++;

        jList2.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList2;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
}//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            File reduceFile = null;
            JFileChooser fileopen = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
            fileopen.addChoosableFileFilter(filter);
            int ret = fileopen.showDialog(null, "Save file");
            if (ret == JFileChooser.APPROVE_OPTION) {
                reduceFile = fileopen.getSelectedFile();
                jTextField5.setText(reduceFile.getPath());
            }

            for (int i = 0; inputList2[i] != null; i++) {
                reduceOp[i] = inputList2[i];
            }/*List list = jList2.getSelectedValuesList();
            Iterator it = list.iterator();
            int j = 0;
            for (; it.hasNext(); it.next()) {
            Object element = it.next();
            reduceOp[j] = element.toString();
            j++;
            System.reduceOp.println(" element ");
            System.reduceOp.print(reduceOp[j] + "reduceOp[j] ");
            }*/

            for (int z = 0; reduceOp[z] != null; z++) {
                //String startmessage = "<wsdl:message name=\"" + s[i] + "Response\">";
                System.out.println("\n out value" + reduceOp[z]);
            }

            BuildingReducedWSDL.Construction ReduceWSDL = new BuildingReducedWSDL.Construction();

            String reduceWSDL = jTextField5.getText();
            System.out.println(" YYYYYYYY " + reduceWSDL);
            ReduceWSDL.ConstructReduceWSDL(WSDL3, reduceOp, reduceFile); // TODO add your handling code here:

            DefaultMutableTreeNode top = new DefaultMutableTreeNode(reduceFile);
            SAXTreeBuilder RWsaxTree3 = new SAXTreeBuilder(top);

            try {
                SAXParser saxParser = new SAXParser();
                saxParser.setContentHandler(RWsaxTree3);
                saxParser.parse(new InputSource(new FileInputStream(reduceFile)));
            } catch (Exception ex) {
                top.add(new DefaultMutableTreeNode(ex.getMessage()));
            }
            JTree tree = new JTree(RWsaxTree3.getTree());
            scrollPane4.add(tree);
        } catch (IOException ex) {
            Logger.getLogger(JMeterRTWSView.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        int x = jList2.getAnchorSelectionIndex();
        System.out.println(" X  " + X.toString() + " x " + x);
        Object X = jList2.getModel().getElementAt(x);

        System.out.print(X.toString());
        for (int i = 0; inputList2[i] != null; i++) {
            if (inputList2[i] == X.toString()) {
                inputList2[i] = inputList2[i + 1];
                j--;
                System.out.println("if inside " + inputList2[i] + inputList2[i + 1]);
                while (inputList2[i] != null) {
                    inputList2[i] = inputList2[i + 1];
                    i++;
                    ;
                }
                inputList2[i] = null;
                ;
            }
            System.out.println("for loop " + inputList2[i]);

        }
        jList2.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList2;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
}//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        JFileChooser fileopen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
        fileopen.addChoosableFileFilter(filter);
        int ret = fileopen.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            WSDL3 = fileopen.getSelectedFile();
            jTextField4.setText(WSDL3.getPath() + File.separator + WSDL3.getName());
        }
        final String inputList[] = new String[100];
        final String[] output;
        output = new String[100];
        for (int i = 0; i < 100; i++) {
            inputList[i] = null;
            output[i] = null;
        }
        jList2.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });

        BuildingReducedWSDL.Search searchObject = new BuildingReducedWSDL.Search();
        //String[] reduceOp = null;
        String out[];
        out = new String[100];
        //String [] output = null;
        try {
            out = searchObject.search(WSDL3.getPath());
        } catch (IOException ex) {
            Logger.getLogger(JMeterRTWSView.class.getName()).log(Level.SEVERE, null, ex);
        }

        int j = 0;
        for (; out.length >= j && out[j] != null; j++) {
            output[j] = out[j];
            System.out.println(out[j]);
        }

        jList1.setModel(new javax.swing.AbstractListModel() {

            String[] strings = output;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });

        SAXTreeBuilder saxTree1 = null;

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(WSDL3);
        saxTree1 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(saxTree1);
            saxParser.parse(new InputSource(new FileInputStream(WSDL3)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }

        JTree tree = new JTree(saxTree1.getTree());
        scrollPane4.add(tree);
        // TODO add your handling code here:
}//GEN-LAST:event_jButton7ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        JFileChooser fileopen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
        fileopen.addChoosableFileFilter(filter);
        int ret = fileopen.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            testSuiteFile2 = fileopen.getSelectedFile();
            jTextField8.setText(testSuiteFile2.getPath() + File.separator + testSuiteFile2.getName());
        }
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(testSuiteFile2);
        SAXTreeBuilder RWsaxTree3 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(RWsaxTree3);
            saxParser.parse(new InputSource(new FileInputStream(testSuiteFile2)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }
        JTree tree = new JTree(RWsaxTree3.getTree());
        scrollPane7.add(tree);
}//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        File ReduceTS = null;
        JFileChooser fileopen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
        fileopen.addChoosableFileFilter(filter);
        int ret = fileopen.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            ReduceTS = fileopen.getSelectedFile();
            jTextField9.setText(ReduceTS.getPath() + File.separator + ReduceTS.getName());
        }
        String s[];
        s = new String[100];
        for (int i = 0; inputList2[i] != null; i++) {
            s[i] = inputList2[i];
        }
        ParseJMeterTS.Construction TC = new ParseJMeterTS.Construction();
        try {
            TC.ConstructReduceTC(testSuiteFile2, s, ReduceTS);
        } catch (Exception ex) {
            Logger.getLogger(JMeterRTWSView.class.getName()).log(Level.SEVERE, null, ex);
        }

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(ReduceTS);
        SAXTreeBuilder RWsaxTree3 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(RWsaxTree3);
            saxParser.parse(new InputSource(new FileInputStream(ReduceTS)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }
        JTree tree = new JTree(RWsaxTree3.getTree());
        scrollPane8.add(tree);
}//GEN-LAST:event_jButton11ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        testSuiteFile4 = testSuiteFile2;
        jTextField16.setText(testSuiteFile4.getPath() + File.separator + testSuiteFile4.getName());
        I = 0;
        J = 0;

        TestCase = "";
        final String inputList[] = new String[100];
        for (int i = 0; i < 100; i++) {
            inputList[i] = null;
            inputList3[i] = null;
            inputList4[i] = null;
            buildTestCase[i] = null;
        }
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                TC[i][j] = null;
            }
        }

        jList4.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });

        jList5.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });

        jList6.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(testSuiteFile4);
        SAXTreeBuilder RWsaxTree3 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(RWsaxTree3);
            saxParser.parse(new InputSource(new FileInputStream(testSuiteFile4)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }
        JTree tree = new JTree(RWsaxTree3.getTree());
        scrollPane12.add(tree);
        ParseJMeterTS.Construction obj = new ParseJMeterTS.Construction();

        //String [] output = null;
        try {
            TC = obj.testCases(testSuiteFile4);
        } catch (Exception ex) {
            Logger.getLogger(JMeterRTWSView.class.getName()).log(Level.SEVERE, null, ex);
        }
        final String[] output;
        output = new String[100];
        int j = 0;

        for (; TC[j][0] != null; j++) {
            output[j] = TC[j][0];
            //System.reduceOp.println(TC[j]);
        }

        jList3.setModel(new javax.swing.AbstractListModel() {

            String[] strings = output;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });

        inputList3 = inputList2;
        jList4.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList3;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        try {
            File OldFile = null;
            JFileChooser fileopen = new JFileChooser();
            int ret = fileopen.showDialog(null, "Open file");
            if (ret == JFileChooser.APPROVE_OPTION) {
                OldFile = fileopen.getSelectedFile();
                jTextField10.setText(OldFile.getPath());
            }
            CompilationUnit cu;
            cu = JavaParser.parse(OldFile);
            new MethodVisitor().visit(cu, "Old");

        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        try {
            File NewFile = null;
            JFileChooser fileopen = new JFileChooser();
            int ret = fileopen.showDialog(null, "Save file");
            if (ret == JFileChooser.APPROVE_OPTION) {
                NewFile = fileopen.getSelectedFile();
                jTextField11.setText(NewFile.getPath());
            }
            CompilationUnit cu;
            cu = JavaParser.parse(NewFile);
            new MethodVisitor().visit(cu, "New");

        } catch (Exception e) {
        }
}//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed

        //File file = null;
        JFileChooser fileopen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
        fileopen.addChoosableFileFilter(filter);
        int ret = fileopen.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            WSDL4 = fileopen.getSelectedFile();
            jTextField12.setText(WSDL4.getPath());
        }

        SAXTreeBuilder saxTree1 = null;

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(WSDL4);
        saxTree1 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(saxTree1);
            saxParser.parse(new InputSource(new FileInputStream(WSDL4)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }

        JTree tree = new JTree(saxTree1.getTree());
        scrollPane9.add(tree);
}//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        try {
            File reduceFile = null;
            JFileChooser fileopen = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
            fileopen.addChoosableFileFilter(filter);
            int ret = fileopen.showDialog(null, "Save file");
            if (ret == JFileChooser.APPROVE_OPTION) {
                reduceFile = fileopen.getSelectedFile();
                jTextField13.setText(reduceFile.getPath());
            }
            /*List list = jList2.getSelectedValuesList();
            Iterator it = list.iterator();
            int j = 0;
            for (; it.hasNext(); it.next()) {
            Object element = it.next();
            reduceOp[j] = element.toString();
            j++;
            System.reduceOp.println(" element ");
            System.reduceOp.print(reduceOp[j] + "reduceOp[j] ");
            }*/
            String[] inputList5 = new String[100];
            Object A;
            for (int x = 0; x < modifiedFiles.getModel().getSize(); x++) {       //= jList1.getAnchorSelectionIndex();
                String UnitOperation;
                A = modifiedFiles.getModel().getElementAt(x);
                System.out.print("A " + A.toString());

                int start = A.toString().lastIndexOf("\\");
                int end = A.toString().indexOf(".");
                UnitOperation = A.toString().substring(start + 1, end);

                inputList5[x] = UnitOperation;
                unitOp[x] = UnitOperation;
            }
            BuildingReducedWSDL.Construction UnitWSDL = new BuildingReducedWSDL.Construction();

            String reduceWSDL = jTextField5.getText();
            System.out.println(" YYYYYYYY " + reduceWSDL);
            UnitWSDL.ConstructReduceWSDL(WSDL4, unitOp, reduceFile); // TODO add your handling code here:

            DefaultMutableTreeNode top = new DefaultMutableTreeNode(reduceFile);
            SAXTreeBuilder RWsaxTree3 = new SAXTreeBuilder(top);

            try {
                SAXParser saxParser = new SAXParser();
                saxParser.setContentHandler(RWsaxTree3);
                saxParser.parse(new InputSource(new FileInputStream(reduceFile)));
            } catch (Exception ex) {
                top.add(new DefaultMutableTreeNode(ex.getMessage()));
            }
            JTree tree = new JTree(RWsaxTree3.getTree());
            scrollPane14.add(tree);
        } catch (IOException ex) {
            Logger.getLogger(JMeterRTWSView.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        JFileChooser fileopen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
        fileopen.addChoosableFileFilter(filter);
        int ret = fileopen.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            testSuiteFile3 = fileopen.getSelectedFile();
            jTextField14.setText(testSuiteFile3.getPath() + File.separator);
        }
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(testSuiteFile3);
        SAXTreeBuilder RWsaxTree3 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(RWsaxTree3);
            saxParser.parse(new InputSource(new FileInputStream(testSuiteFile3)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }
        JTree tree = new JTree(RWsaxTree3.getTree());
        scrollPane10.add(tree);
}//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        //modifiedFiles.getModel().getElementAt(x);
        String[] inputList5 = new String[100];
        Object A;

        File UnitTS = null;
        JFileChooser fileopen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
        fileopen.addChoosableFileFilter(filter);
        int ret = fileopen.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            UnitTS = fileopen.getSelectedFile();
            jTextField15.setText(UnitTS.getPath() + File.separator);
        }
        for (int x = 0; x < modifiedFiles.getModel().getSize(); x++) {       //= jList1.getAnchorSelectionIndex();
            String UnitOperation;
            A = modifiedFiles.getModel().getElementAt(x);
            System.out.print("A " + A.toString());

            int start = A.toString().lastIndexOf("\\");
            int end = A.toString().indexOf(".");
            UnitOperation = A.toString().substring(start + 1, end);

            inputList5[x] = UnitOperation;
            unitOp[x] = UnitOperation;
        }
        //String s[] = {"India"};
        ParseJMeterTS.Construction TC = new ParseJMeterTS.Construction();
        try {
            TC.ConstructReduceTC(testSuiteFile3, inputList5, UnitTS);
        } catch (Exception ex) {
            Logger.getLogger(JMeterRTWSView.class.getName()).log(Level.SEVERE, null, ex);
        }

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(UnitTS);
        SAXTreeBuilder RWsaxTree3 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(RWsaxTree3);
            saxParser.parse(new InputSource(new FileInputStream(UnitTS)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }
        JTree tree = new JTree(RWsaxTree3.getTree());
        scrollPane11.add(tree);        // TODO add your handling code here:
}//GEN-LAST:event_jButton17ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        testSuiteFile4 = testSuiteFile3;
        jTextField16.setText(testSuiteFile4.getPath() + File.separator + testSuiteFile4.getName());
        I = 0;
        J = 0;

        TestCase = "";
        final String inputList[] = new String[100];
        for (int i = 0; i < 100; i++) {
            inputList[i] = null;
            inputList3[i] = null;
            inputList4[i] = null;
            buildTestCase[i] = null;
        }
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                TC[i][j] = null;
            }
        }

        jList4.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });

        jList5.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });

        jList6.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(testSuiteFile4);
        SAXTreeBuilder RWsaxTree3 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(RWsaxTree3);
            saxParser.parse(new InputSource(new FileInputStream(testSuiteFile4)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }
        JTree tree = new JTree(RWsaxTree3.getTree());
        scrollPane12.add(tree);
        ParseJMeterTS.Construction obj = new ParseJMeterTS.Construction();

        //String [] output = null;
        try {
            TC = obj.testCases(testSuiteFile4);
        } catch (Exception ex) {
            Logger.getLogger(JMeterRTWSView.class.getName()).log(Level.SEVERE, null, ex);
        }
        final String[] output;
        output = new String[100];
        int j = 0;

        for (; TC[j][0] != null; j++) {
            output[j] = TC[j][0];
            //System.reduceOp.println(TC[j]);
        }

        jList3.setModel(new javax.swing.AbstractListModel() {

            String[] strings = output;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });

        inputList3 = unitOp;
        jList4.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList3;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
}//GEN-LAST:event_jButton28ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        //File testSuiteFile4;
        I = 0;
        J = 0;

        TestCase = "";
        final String inputList[] = new String[100];
        for (int i = 0; i < 100; i++) {
            inputList[i] = null;
            inputList3[i] = null;
            inputList4[i] = null;
            buildTestCase[i] = null;
        }
        for (int i = 0; i < 100; i++) {
            for (int j = 0; i < 100; i++) {
                TC[i][j] = null;
            }
        }

        jList4.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        jList5.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });

        jList6.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });

        JFileChooser fileopen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
        fileopen.addChoosableFileFilter(filter);
        int ret = fileopen.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            testSuiteFile4 = fileopen.getSelectedFile();
            jTextField16.setText(testSuiteFile4.getPath() + File.separator + testSuiteFile4.getName());
        }
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(testSuiteFile4);
        SAXTreeBuilder RWsaxTree3 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(RWsaxTree3);
            saxParser.parse(new InputSource(new FileInputStream(testSuiteFile4)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }
        JTree tree = new JTree(RWsaxTree3.getTree());
        scrollPane12.add(tree);
        ParseJMeterTS.Construction obj = new ParseJMeterTS.Construction();

        //String [] output = null;
        try {
            TC = obj.testCases(testSuiteFile4);
        } catch (Exception ex) {
            Logger.getLogger(JMeterRTWSView.class.getName()).log(Level.SEVERE, null, ex);
        }
        final String[] output;
        output = new String[100];
        int j = 0;

        for (; TC[j][0] != null; j++) {
            output[j] = TC[j][0];
            //System.reduceOp.println(TC[j]);
        }

        jList3.setModel(new javax.swing.AbstractListModel() {

            String[] strings = output;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
}//GEN-LAST:event_jButton19ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        int x = jList3.getAnchorSelectionIndex();
        Y = jList3.getModel().getElementAt(x);

        System.out.print(Y.toString());
        inputList3[I] = Y.toString();
        I++;

        jList4.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList3;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
}//GEN-LAST:event_jButton20ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        int x = jList4.getAnchorSelectionIndex();
        System.out.println(" X  " + Y.toString() + " x " + x);
        Object X = jList4.getModel().getElementAt(x);

        System.out.print(X.toString());
        for (int i = 0; inputList3[i] != null; i++) {
            if (inputList3[i] == X.toString()) {
                inputList3[i] = inputList3[i + 1];
                I--;
                System.out.println("if inside " + inputList3[i] + inputList3[i + 1]);
                while (inputList3[i] != null) {
                    inputList3[i] = inputList3[i + 1];
                    i++;
                }
                inputList3[i] = null;

            }
            System.out.println("for loop " + inputList3[i]);

        }
        jList4.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList3;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
}//GEN-LAST:event_jButton21ActionPerformed

    private void jList4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList4MouseClicked
        int x = jList4.getAnchorSelectionIndex();
        Object Y = jList4.getModel().getElementAt(x);
        int i = 0;
        final String s[] = new String[100];
        buildTestCase[0] = Y.toString();
        for (; TC[i][0] != null; i++) {
            if (Y.toString() == TC[i][0] && TC[i][0] != null) {
                for (int j = 1; TC[i][j] != null; j++) {
                    s[j - 1] = TC[i][j];
                }
            }
        }

        final String inputList[] = new String[100];
        for (int j = 0; j < 100; j++) {
            inputList[j] = null;
            inputList4[j] = null;
            buildTestCase[j] = null;
        }
        J = 0;
        jList6.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        jList5.setModel(new javax.swing.AbstractListModel() {

            String[] strings = s;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
}//GEN-LAST:event_jList4MouseClicked

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        File ReduceTS = null;
        JFileChooser fileopen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
        fileopen.addChoosableFileFilter(filter);
        int ret = fileopen.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            ReduceTS = fileopen.getSelectedFile();
            jTextField4.setText(ReduceTS.getPath() + File.separator + ReduceTS.getName());
        }
        String s[];
        s = new String[100];
        for (int i = 0; inputList3[i] != null; i++) {
            s[i] = inputList3[i];
        }
        ParseJMeterTS.Construction TC = new ParseJMeterTS.Construction();
        try {
            TC.ConstructReduceTC(testSuiteFile4, TestCase, ReduceTS);
        } catch (Exception ex) {
            Logger.getLogger(JMeterRTWSView.class.getName()).log(Level.SEVERE, null, ex);
        }

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(ReduceTS);
        SAXTreeBuilder RWsaxTree3 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(RWsaxTree3);
            saxParser.parse(new InputSource(new FileInputStream(ReduceTS)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }
        JTree tree = new JTree(RWsaxTree3.getTree());
        scrollPane13.add(tree);
}//GEN-LAST:event_jButton22ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        int x = jList5.getAnchorSelectionIndex();
        Z = jList5.getModel().getElementAt(x);

        System.out.print(Z.toString());
        inputList4[J] = Z.toString();
        J++;

        jList6.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList4;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });        // TODO add your handling code here:
}//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        int x = jList6.getAnchorSelectionIndex();
        System.out.println(" X  " + Z.toString() + " x " + x);
        Object X = jList6.getModel().getElementAt(x);

        System.out.print(X.toString());
        for (int i = 0; inputList4[i] != null; i++) {
            if (inputList4[i] == X.toString()) {
                inputList4[i] = inputList4[i + 1];
                J--;
                System.out.println("if inside " + inputList4[i] + inputList4[i + 1]);
                while (inputList4[i] != null) {
                    inputList4[i] = inputList4[i + 1];
                    i++;

                }
                inputList4[i] = null;

            }
            System.out.println("for loop " + inputList4[i]);

        }
        jList6.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList4;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
}//GEN-LAST:event_jButton24ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        int x = jList4.getAnchorSelectionIndex();
        Z = jList4.getModel().getElementAt(x);
        buildTestCase[0] = Z.toString();

        for (int i = 0; inputList4[i] != null; i++) {
            buildTestCase[i + 1] = inputList4[i];
        }

        jList6.setModel(new javax.swing.AbstractListModel() {

            String[] strings = inputList4;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {

                return strings[i];
            }
        });
        try {
            ParseJMeterTS.Construction obj = new ParseJMeterTS.Construction();
            String z = obj.buildTestCase(testSuiteFile4, buildTestCase);
            TestCase = TestCase.concat(z);
        } catch (Exception e) {
        }//(testSuiteFile4, buildTestCase);
}//GEN-LAST:event_jButton25ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        //File file = null;
        JFileChooser fileopen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
        fileopen.addChoosableFileFilter(filter);
        int ret = fileopen.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            WSDL5 = fileopen.getSelectedFile();
            jTextField18.setText(WSDL5.getPath());
        }

        SAXTreeBuilder saxTree1 = null;

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(WSDL5);
        saxTree1 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(saxTree1);
            saxParser.parse(new InputSource(new FileInputStream(WSDL5)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }

        JTree tree = new JTree(saxTree1.getTree());
        scrollPane7.add(tree);                       // TODO add your handling code here:
}//GEN-LAST:event_jButton29ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        int combineOpLength = diffOp.length + reduceOp.length + unitOp.length;
        int k = 0;
        String[] uniqueOp = new String[100];
        for (int x = 0; x < combineOpLength; x++) {       //= jList1.getAnchorSelectionIndex();
            for (int y = 0; y < diffOp.length; y++) {
                combinedUniqueOp[x] = diffOp[y];
            }
            for (int y = 0; y < diffOp.length; y++) {
                combinedUniqueOp[x] = reduceOp[y];
            }
            for (int y = 0; y < diffOp.length; y++) {
                combinedUniqueOp[x] = unitOp[y];
            }
            for (int i = 0; i < combinedUniqueOp.length; i++) {
                boolean isDistinct = false;
                for (int j = 0; j < i; j++) {
                    if (combinedUniqueOp[i] == combinedUniqueOp[j]) {
                        isDistinct = true;
                        break;
                    }
                }
                if (!isDistinct) {
                    System.out.print(combinedUniqueOp[i] + " ");
                    uniqueOp[k] = combinedUniqueOp[i];
                }
            }
        }

        try {
            File combinedFile = null;
            JFileChooser fileopen = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
            fileopen.addChoosableFileFilter(filter);
            int ret = fileopen.showDialog(null, "Save file");
            if (ret == JFileChooser.APPROVE_OPTION) {
                combinedFile = fileopen.getSelectedFile();
                jTextField19.setText("file:///" + combinedFile.getPath());
            }

            for (int z = 0; uniqueOp[z] != null; z++) {
                //String startmessage = "<wsdl:message name=\"" + s[i] + "Response\">";
                System.out.println("\n out value" + uniqueOp[z]);
            }

            BuildingReducedWSDL.Construction ReduceWSDL = new BuildingReducedWSDL.Construction();

            ReduceWSDL.ConstructReduceWSDL(WSDL5, uniqueOp, combinedFile); // TODO add your handling code here:

            DefaultMutableTreeNode top = new DefaultMutableTreeNode(combinedFile);
            SAXTreeBuilder CUsaxTree3 = new SAXTreeBuilder(top);

            try {
                SAXParser saxParser = new SAXParser();
                saxParser.setContentHandler(CUsaxTree3);
                saxParser.parse(new InputSource(new FileInputStream(combinedFile)));
            } catch (Exception ex) {
                top.add(new DefaultMutableTreeNode(ex.getMessage()));
            }
            JTree tree = new JTree(CUsaxTree3.getTree());
            scrollPane8.add(tree);
        } catch (IOException ex) {
            Logger.getLogger(JMeterRTWSView.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_jButton30ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        JFileChooser fileopen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
        fileopen.addChoosableFileFilter(filter);
        int ret = fileopen.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            testSuiteFile5 = fileopen.getSelectedFile();
            jTextField18.setText(testSuiteFile5.getPath() + File.separator + testSuiteFile5.getName());
        }
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(testSuiteFile5);
        SAXTreeBuilder RWsaxTree3 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(RWsaxTree3);
            saxParser.parse(new InputSource(new FileInputStream(testSuiteFile5)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }
        JTree tree = new JTree(RWsaxTree3.getTree());
        scrollPane5.add(tree);
}//GEN-LAST:event_jButton31ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        File ReduceTS = null;
        JFileChooser fileopen = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
        fileopen.addChoosableFileFilter(filter);
        int ret = fileopen.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            ReduceTS = fileopen.getSelectedFile();
            jTextField19.setText(ReduceTS.getPath() + File.separator + ReduceTS.getName());
        }
        String s[] = combinedUniqueOp;//{"Index", "editFile"};
        ParseJMeterTS.Construction TC = new ParseJMeterTS.Construction();
        try {
            TC.ConstructReduceTC(testSuiteFile5, s, ReduceTS);
        } catch (Exception ex) {
            Logger.getLogger(JMeterRTWSView.class.getName()).log(Level.SEVERE, null, ex);
        }

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(ReduceTS);
        SAXTreeBuilder RWsaxTree3 = new SAXTreeBuilder(top);

        try {
            SAXParser saxParser = new SAXParser();
            saxParser.setContentHandler(RWsaxTree3);
            saxParser.parse(new InputSource(new FileInputStream(ReduceTS)));
        } catch (Exception ex) {
            top.add(new DefaultMutableTreeNode(ex.getMessage()));
        }
        JTree tree = new JTree(RWsaxTree3.getTree());
        scrollPane8.add(tree);
}//GEN-LAST:event_jButton32ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JList jList4;
    private javax.swing.JList jList5;
    private javax.swing.JList jList6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel mainPanel1;
    private javax.swing.JPanel mainPanel2;
    private javax.swing.JPanel mainPanel3;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private java.awt.ScrollPane scrollPane1;
    private java.awt.ScrollPane scrollPane10;
    private java.awt.ScrollPane scrollPane11;
    private java.awt.ScrollPane scrollPane12;
    private java.awt.ScrollPane scrollPane13;
    private java.awt.ScrollPane scrollPane14;
    private java.awt.ScrollPane scrollPane2;
    private java.awt.ScrollPane scrollPane3;
    private java.awt.ScrollPane scrollPane4;
    private java.awt.ScrollPane scrollPane5;
    private java.awt.ScrollPane scrollPane6;
    private java.awt.ScrollPane scrollPane7;
    private java.awt.ScrollPane scrollPane8;
    private java.awt.ScrollPane scrollPane9;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private DefaultMutableTreeNode leftRoot, rightRoot;
    private JTree leftTree, rightTree;
    private String currentDir = "";
    private boolean selection = false;
    private JList modifiedFiles;
    private JButton compareFolders, compareFiles;
    private DefaultListModel listModel;
    private DefaultTreeModel leftTreeModel, rightTreeModel;
    private JScrollPane leftScroll, rightScroll;
    private DiffProvider diffProvider;
    private TitledBorder leftBorder, rightBorder;
    private FolderComparatorMonitor comparator;

    private class FolderTreeRenderer extends DefaultTreeCellRenderer {

        private Icon dirIcon = UIManager.getIcon("FileView.directoryIcon");
        private Icon fileIcon = UIManager.getIcon("FileView.fileIcon");

        public FolderTreeRenderer() {
            setOpaque(true);
        }
    }

    private class FolderComparatorMonitor extends Thread {

        private DirDiffResult result = null;
        private Cancellable cancellable;
        private File leftFolder, rightFolder;

        public FolderComparatorMonitor(File leftFolder, File rightFolder) {
            cancellable = new CancellableImpl();
            this.leftFolder = leftFolder;
            this.rightFolder = rightFolder;
        }

        public void cancel() {
            cancellable.cancel();
        }

        public void run() {
            try {
                result = FolderComparator.compareFolders(leftFolder, rightFolder, cancellable);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (!cancellable.isCancelled()) {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            onFinish(result);
                        }
                    });
                }
            }
        }
    }

    private void onFinish(DirDiffResult result) {
        mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        compareFolders.setEnabled(true);
        if (result == null) {
            // an error occured, display it
        } else {
            // build the trees
            leftBorder.setTitle(result.getLeftContent().getFile().getName());
            rightBorder.setTitle(result.getRightContent().getFile().getName());
            listModel.clear();
            leftRoot.removeAllChildren();
            leftTreeModel.nodeStructureChanged(leftRoot);
            rightRoot.removeAllChildren();
            rightTreeModel.nodeStructureChanged(rightRoot);

            buildModifiedFiles(result.getLeftContent());
            addNode(leftRoot, leftTreeModel, result.getLeftContent());
            addNode(rightRoot, rightTreeModel, result.getRightContent());
            leftTree.expandPath(new TreePath(new Object[]{leftRoot, leftRoot.getChildAt(0)}));
            rightTree.expandPath(new TreePath(new Object[]{rightRoot, rightRoot.getChildAt(0)}));

            mainPanel.repaint();
        }
    }

    private void buildModifiedFiles(DirContentStatus content) {
        if (content.getStatus() == Status.MODIFIED && !content.getFile().isDirectory()) {

            listModel.addElement(content);//.getFile().getName());
        }
        for (DirContentStatus child : content.getChildren()) {
            buildModifiedFiles(child);
        }
    }

    private void addNode(DefaultMutableTreeNode node, DefaultTreeModel treeModel, DirContentStatus dirContent) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dirContent);//.getFile().getName());
        node.add(newNode);
        newNode.setParent(node);
        treeModel.nodesWereInserted(node, new int[]{node.getChildCount() - 1});

        dirContent.sort();
        List<DirContentStatus> children = dirContent.getChildren();
        for (DirContentStatus child : children) {
            addNode(newNode, treeModel, child);
        }
    }

    private void checkSelection() {
        TreePath leftPath = leftTree.getSelectionModel().getSelectionPath();
        if (leftPath != null) {
            DefaultMutableTreeNode leftNode = (DefaultMutableTreeNode) leftPath.getLastPathComponent();
            if (leftNode != null) {
                DirContentStatus status = (DirContentStatus) leftNode.getUserObject();
                compareFiles.setEnabled(status.getStatus() == Status.MODIFIED);
            } else {
                compareFiles.setEnabled(false);
            }
        } else {
            TreePath rightPath = rightTree.getSelectionModel().getSelectionPath();
            if (rightPath != null) {
                DefaultMutableTreeNode rightNode = (DefaultMutableTreeNode) rightPath.getLastPathComponent();
                if (rightNode != null) {
                    DirContentStatus status = (DirContentStatus) rightNode.getUserObject();
                    compareFiles.setEnabled(status.getStatus() == Status.MODIFIED);
                } else {
                    compareFiles.setEnabled(false);
                }
            } else if (modifiedFiles.getSelectedValue() != null) {
                DirContentStatus content = (DirContentStatus) modifiedFiles.getSelectedValue();
                compareFiles.setEnabled(content != null);
            } else {
                compareFiles.setEnabled(false);
            }
        }
    }
}
