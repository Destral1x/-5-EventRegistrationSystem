/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package eventregistrationsystem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Galvenais logs — pasākumu saraksts (2.6. att.).
 *
 * @author artjomsdoktorovs
 */
public class EventRegistrationSystemGUI extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EventRegistrationSystemGUI.class.getName());

    // Krāsas no saskarnes skicēm
    private static final Color ZILA = new Color(59, 99, 224);
    private static final Color ZILA_TUMSA = new Color(47, 85, 212);
    private static final Color GALVENES_FONS = new Color(220, 232, 252);
    private static final Color GALVENES_TEKSTS = new Color(30, 58, 138);
    private static final Color RINDA_OTRA = new Color(242, 246, 252);
    private static final Color REZGIS = new Color(196, 212, 236);
    private static final Color TEKSTS = new Color(20, 24, 33);
    private static final Color ZALA = new Color(46, 125, 50);
    private static final Color ORANZA = new Color(178, 106, 30);
    private static final Color SARKANA = new Color(183, 28, 28);

    /**
     * Creates new form EventRegistrationSystemGUI
     */
    public EventRegistrationSystemGUI() {
        initComponents();
        noformetDizainu();
        ieliktParaugaDatus();
    }

    /** Noformējums, ko nevar iestatīt GUI Builder logā (apmales, tabulas galvene, krāsas šūnās). */
    private void noformetDizainu() {
        pnlHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, ZILA_TUMSA));

        noformetPogu(btnRegistretiesDaliba, ZILA_TUMSA);
        noformetPogu(btnDalibnieki, ZILA);

        tblPasakumi.setFont(tblPasakumi.getFont().deriveFont(tblPasakumi.getFont().getSize() + 1f));
        tblPasakumi.setRowHeight(28);
        tblPasakumi.setShowGrid(true);
        tblPasakumi.setGridColor(REZGIS);
        tblPasakumi.setSelectionBackground(GALVENES_FONS);
        tblPasakumi.setSelectionForeground(TEKSTS);

        scrPasakumi.setBorder(BorderFactory.createLineBorder(REZGIS));
        scrPasakumi.getViewport().setBackground(pnlBody.getBackground());

        // Tabulas galvene
        JTableHeader galvene = tblPasakumi.getTableHeader();
        galvene.setReorderingAllowed(false);
        galvene.setPreferredSize(new Dimension(galvene.getPreferredSize().width, 32));
        galvene.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                                                           boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, v, false, false, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBackground(GALVENES_FONS);
                setForeground(GALVENES_TEKSTS);
                setFont(t.getFont().deriveFont(Font.BOLD));
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, REZGIS));
                return this;
            }
        });

        // Parastās šūnas — pārmaiņus balta / gaiši zila rinda
        DefaultTableCellRenderer rindas = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                                                           boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, false, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                if (!sel) {
                    setBackground(row % 2 == 0 ? Color.WHITE : RINDA_OTRA);
                    setForeground(TEKSTS);
                }
                return this;
            }
        };
        tblPasakumi.setDefaultRenderer(Object.class, rindas);

        // Brīvās vietas — centrā, treknrakstā, krāsa pēc skaita
        DefaultTableCellRenderer vietas = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                                                           boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, false, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(t.getFont().deriveFont(Font.BOLD));
                if (!sel) {
                    setBackground(row % 2 == 0 ? Color.WHITE : RINDA_OTRA);
                }
                int skaits = (v instanceof Integer) ? (Integer) v : 0;
                setForeground(skaits <= 0 ? SARKANA : skaits < 10 ? ORANZA : ZALA);
                return this;
            }
        };
        tblPasakumi.getColumnModel().getColumn(3).setCellRenderer(vietas);

        int[] platumi = {228, 115, 158, 111};
        for (int i = 0; i < platumi.length; i++) {
            tblPasakumi.getColumnModel().getColumn(i).setPreferredWidth(platumi[i]);
        }
    }

    private void noformetPogu(JButton poga, Color apmale) {
        poga.setOpaque(true);
        poga.setContentAreaFilled(true);
        poga.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        poga.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(apmale, 2, true),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)));
    }

    /** PAGAIDU dati izskata pārbaudei — vēlāk tiks aizstāti ar datiem no datu bāzes. */
    private void ieliktParaugaDatus() {
        DefaultTableModel modelis = (DefaultTableModel) tblPasakumi.getModel();
        modelis.addRow(new Object[]{"IT konference 2026", "12.10.2026", "Rīga, LU", 24});
        modelis.addRow(new Object[]{"Java seminārs", "20.10.2026", "Daugavpils, DU", 8});
        modelis.addRow(new Object[]{"Datu bāzu praktikums", "03.11.2026", "Tiešsaiste", 0});
        modelis.addRow(new Object[]{"Studentu hakatons", "15.11.2026", "Liepāja", 31});
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblVirsraksts = new javax.swing.JLabel();
        pnlBody = new javax.swing.JPanel();
        scrPasakumi = new javax.swing.JScrollPane();
        tblPasakumi = new javax.swing.JTable();
        btnRegistretiesDaliba = new javax.swing.JButton();
        btnDalibnieki = new javax.swing.JButton();
        lblAdminPiezime = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pasākumu reģistrācijas sistēma");

        pnlHeader.setBackground(new java.awt.Color(59, 99, 224));

        lblVirsraksts.setFont(lblVirsraksts.getFont().deriveFont(lblVirsraksts.getFont().getStyle() | java.awt.Font.BOLD, lblVirsraksts.getFont().getSize()+5));
        lblVirsraksts.setForeground(new java.awt.Color(255, 255, 255));
        lblVirsraksts.setText("Pasākumu saraksts");

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblVirsraksts)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblVirsraksts)
                .addGap(12, 12, 12))
        );

        pnlBody.setBackground(new java.awt.Color(248, 250, 252));

        tblPasakumi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nosaukums", "Datums", "Vieta", "Brīvās vietas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrPasakumi.setViewportView(tblPasakumi);

        btnRegistretiesDaliba.setBackground(new java.awt.Color(59, 99, 224));
        btnRegistretiesDaliba.setFont(btnRegistretiesDaliba.getFont().deriveFont(btnRegistretiesDaliba.getFont().getStyle() | java.awt.Font.BOLD, btnRegistretiesDaliba.getFont().getSize()+1));
        btnRegistretiesDaliba.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistretiesDaliba.setText("Reģistrēties dalībai");
        btnRegistretiesDaliba.setFocusPainted(false);

        btnDalibnieki.setBackground(new java.awt.Color(255, 255, 255));
        btnDalibnieki.setFont(btnDalibnieki.getFont().deriveFont(btnDalibnieki.getFont().getSize()+1f));
        btnDalibnieki.setForeground(new java.awt.Color(59, 99, 224));
        btnDalibnieki.setText("Dalībnieki");
        btnDalibnieki.setFocusPainted(false);

        lblAdminPiezime.setForeground(new java.awt.Color(100, 116, 139));
        lblAdminPiezime.setText("← redzama tikai administratoram");

        javax.swing.GroupLayout pnlBodyLayout = new javax.swing.GroupLayout(pnlBody);
        pnlBody.setLayout(pnlBodyLayout);
        pnlBodyLayout.setHorizontalGroup(
            pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBodyLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrPasakumi, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                    .addGroup(pnlBodyLayout.createSequentialGroup()
                        .addComponent(btnRegistretiesDaliba, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnDalibnieki, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(lblAdminPiezime)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(22, 22, 22))
        );
        pnlBodyLayout.setVerticalGroup(
            pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBodyLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(scrPasakumi, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                .addGap(22, 22, 22)
                .addGroup(pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnRegistretiesDaliba, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDalibnieki, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAdminPiezime))
                .addGap(60, 60, 60))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(pnlBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Metal izskats — tikai tajā strādā pogu un galvenes krāsas (Nimbus un macOS tās ignorē) */
        try {
            javax.swing.UIManager.put("swing.boldMetal", Boolean.FALSE);
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new EventRegistrationSystemGUI().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDalibnieki;
    private javax.swing.JButton btnRegistretiesDaliba;
    private javax.swing.JLabel lblAdminPiezime;
    private javax.swing.JLabel lblVirsraksts;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JScrollPane scrPasakumi;
    private javax.swing.JTable tblPasakumi;
    // End of variables declaration//GEN-END:variables
}
