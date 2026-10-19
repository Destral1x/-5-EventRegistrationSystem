/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package eventregistrationsystem;

import eventregistrationsystem.dao.DatabaseUtil;
import java.awt.Color;
import java.awt.Font;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableCellRenderer;
import eventregistrationsystem.dao.LietotajuDAO;
import eventregistrationsystem.dao.PasakumuDAO;
import eventregistrationsystem.dao.RegistracijaDAO;
import eventregistrationsystem.model.Administrators;
import eventregistrationsystem.model.Dalibnieks;
import eventregistrationsystem.model.Lietotajs;
import eventregistrationsystem.model.Pasakums;
import eventregistrationsystem.model.Registracija;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author artjomsdoktorovs, glebsvasiljievs
 */
public class EventRegistrationSystemGUI extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EventRegistrationSystemGUI.class.getName());
    private final LietotajuDAO lietotajuDAO = new LietotajuDAO();
    private final PasakumuDAO pasakumuDAO = new PasakumuDAO();
    private final RegistracijaDAO registracijaDAO = new RegistracijaDAO();

    private Lietotajs currentUser;
    private List<Pasakums> pasakumi = new ArrayList<>();
    private List<Registracija> dalibnieki = new ArrayList<>();
    private boolean atjaunina = false;

    private static final DateTimeFormatter DATUMA_FORMATS
            = DateTimeFormatter.ofPattern("dd.MM.uuuu").withResolverStyle(ResolverStyle.STRICT);
    
    /**
     * Creates new form EventRegistrationSystemGUI
     */
        public EventRegistrationSystemGUI() {
        initComponents();
        noformet();
        pievienotKlausitajus();
    }

    // dizains, ko nevar uzlikt Design logā
    private void noformet() {
        Color zila = new Color(59, 99, 224);
        Color peleka = new Color(160, 170, 185);

        // pogas
        noformetPogu(btnRegistretiesDaliba, zila);
        noformetPogu(btnDalibnieki, zila);
        noformetPogu(btnLogin, zila);
        noformetPogu(btnLoginRegister, zila);
        noformetPogu(btnRegister, zila);
        noformetPogu(btnRegisterCancel, peleka);
        noformetPogu(btnIzrakstities, peleka);
        noformetPogu(btnIziet, peleka);
        noformetPogu(btnPievienotPasakumu, zila);
        noformetPogu(btnEventSave, zila);
        noformetPogu(btnEventCancel, peleka);
        noformetPogu(btnApprove, new Color(56, 130, 64));
        noformetPogu(btnReject, new Color(160, 40, 35));
        noformetPogu(btnParticipantsClose, peleka);
        btnDalibnieki.setBackground(Color.WHITE);
        btnLoginRegister.setBackground(Color.WHITE);
        btnPievienotPasakumu.setBackground(Color.WHITE);

        spnEventMax.setModel(new SpinnerNumberModel(10, 1, 1000, 1));

        dlgLogin.getRootPane().setDefaultButton(btnLogin); // lai var ielogoties ar Enter

        // tabula
        tblPasakumi.setRowHeight(28);
        tblPasakumi.setGridColor(new Color(196, 212, 236));
        tblPasakumi.getTableHeader().setBackground(new Color(220, 232, 252));
        tblPasakumi.getTableHeader().setForeground(new Color(30, 58, 138));
        tblPasakumi.getTableHeader().setFont(tblPasakumi.getFont().deriveFont(Font.BOLD));
        scrPasakumi.getViewport().setBackground(Color.WHITE);

        // brīvās vietas pa vidu, krāsa pēc vietu skaita
        tblPasakumi.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable t, Object v,
                    boolean izvelets, boolean fokuss, int rinda, int kol) {
                super.getTableCellRendererComponent(t, v, izvelets, fokuss, rinda, kol);
                setHorizontalAlignment(JLabel.CENTER);
                setFont(getFont().deriveFont(Font.BOLD));
                int vietas = v instanceof Integer ? (Integer) v : 0;
                if (!izvelets) {
                    setForeground(vietas <= 0 ? new Color(178, 34, 34)
                            : vietas <= 10 ? new Color(178, 106, 30) : new Color(46, 125, 50));
                }
                return this;
            }
        });

        tblParticipants.setRowHeight(28);
        tblParticipants.setGridColor(new Color(196, 212, 236));
        tblParticipants.getTableHeader().setBackground(new Color(220, 232, 252));
        tblParticipants.getTableHeader().setForeground(new Color(30, 58, 138));
        tblParticipants.getTableHeader().setFont(tblParticipants.getFont().deriveFont(Font.BOLD));
        scrParticipants.getViewport().setBackground(Color.WHITE);

        // statuss pa vidu, krāsa pēc statusa
        tblParticipants.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable t, Object v,
                    boolean izvelets, boolean fokuss, int rinda, int kol) {
                super.getTableCellRendererComponent(t, v, izvelets, fokuss, rinda, kol);
                setHorizontalAlignment(JLabel.CENTER);
                setFont(getFont().deriveFont(Font.BOLD));
                if (!izvelets) {
                    setForeground("Apstiprināts".equals(v) ? new Color(46, 125, 50)
                            : "Noraidīts".equals(v) ? new Color(178, 34, 34) : new Color(178, 106, 30));
                }
                return this;
            }
        });
    }

    private void noformetPogu(JButton poga, Color apmale) {
        poga.setOpaque(true);
        poga.setBorder(BorderFactory.createLineBorder(apmale, 2));
    }

    // galvenais logs ekrāna vidū
    private void atvertGalvenoLogu() {
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dlgLogin = new javax.swing.JDialog();
        pnlLoginHeader = new javax.swing.JPanel();
        lblLoginHeader = new javax.swing.JLabel();
        pnlLoginBody = new javax.swing.JPanel();
        lblLoginHint = new javax.swing.JLabel();
        lblLoginUsername = new javax.swing.JLabel();
        txtLoginUsername = new javax.swing.JTextField();
        lblLoginPassword = new javax.swing.JLabel();
        pwdLoginPassword = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        btnLoginRegister = new javax.swing.JButton();
        dlgRegister = new javax.swing.JDialog();
        pnlRegisterHeader = new javax.swing.JPanel();
        lblRegisterHeader = new javax.swing.JLabel();
        pnlRegisterBody = new javax.swing.JPanel();
        lblRegisterHint = new javax.swing.JLabel();
        lblRegisterName = new javax.swing.JLabel();
        txtRegisterName = new javax.swing.JTextField();
        lblRegisterSurname = new javax.swing.JLabel();
        txtRegisterSurname = new javax.swing.JTextField();
        lblRegisterUsername = new javax.swing.JLabel();
        txtRegisterUsername = new javax.swing.JTextField();
        lblRegisterPhone = new javax.swing.JLabel();
        txtRegisterPhone = new javax.swing.JTextField();
        lblRegisterPassword = new javax.swing.JLabel();
        pwdRegisterPassword = new javax.swing.JPasswordField();
        lblRegisterPassword2 = new javax.swing.JLabel();
        pwdRegisterPassword2 = new javax.swing.JPasswordField();
        sepRegister = new javax.swing.JSeparator();
        btnRegister = new javax.swing.JButton();
        btnRegisterCancel = new javax.swing.JButton();
        dlgAddEvent = new javax.swing.JDialog();
        pnlEventHeader = new javax.swing.JPanel();
        lblEventHeader = new javax.swing.JLabel();
        pnlEventBody = new javax.swing.JPanel();
        lblEventHint = new javax.swing.JLabel();
        lblEventName = new javax.swing.JLabel();
        txtEventName = new javax.swing.JTextField();
        lblEventDescription = new javax.swing.JLabel();
        scrEventDescription = new javax.swing.JScrollPane();
        txtEventDescription = new javax.swing.JTextArea();
        lblEventDate = new javax.swing.JLabel();
        txtEventDate = new javax.swing.JTextField();
        lblEventTime = new javax.swing.JLabel();
        txtEventTime = new javax.swing.JTextField();
        lblEventPlace = new javax.swing.JLabel();
        txtEventPlace = new javax.swing.JTextField();
        lblEventMax = new javax.swing.JLabel();
        spnEventMax = new javax.swing.JSpinner();
        lblEventNote = new javax.swing.JLabel();
        sepEvent = new javax.swing.JSeparator();
        btnEventSave = new javax.swing.JButton();
        btnEventCancel = new javax.swing.JButton();
        dlgParticipants = new javax.swing.JDialog();
        pnlParticipantsHeader = new javax.swing.JPanel();
        lblParticipantsHeader = new javax.swing.JLabel();
        pnlParticipantsBody = new javax.swing.JPanel();
        lblParticipantsEvent = new javax.swing.JLabel();
        cmbParticipantsEvent = new javax.swing.JComboBox<>();
        scrParticipants = new javax.swing.JScrollPane();
        tblParticipants = new javax.swing.JTable();
        btnApprove = new javax.swing.JButton();
        btnReject = new javax.swing.JButton();
        btnParticipantsClose = new javax.swing.JButton();
        pnlHeader = new javax.swing.JPanel();
        lblVirsraksts = new javax.swing.JLabel();
        pnlBody = new javax.swing.JPanel();
        scrPasakumi = new javax.swing.JScrollPane();
        tblPasakumi = new javax.swing.JTable();
        btnRegistretiesDaliba = new javax.swing.JButton();
        btnDalibnieki = new javax.swing.JButton();
        btnPievienotPasakumu = new javax.swing.JButton();
        btnIzrakstities = new javax.swing.JButton();
        btnIziet = new javax.swing.JButton();

        dlgLogin.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        dlgLogin.setTitle("Pasākumu sistēma – Ielogošanās");
        dlgLogin.setResizable(false);
        dlgLogin.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                dlgLoginWindowClosing(evt);
            }
        });

        pnlLoginHeader.setBackground(new java.awt.Color(59, 99, 224));

        lblLoginHeader.setFont(lblLoginHeader.getFont().deriveFont(lblLoginHeader.getFont().getStyle() | java.awt.Font.BOLD, lblLoginHeader.getFont().getSize()+3));
        lblLoginHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblLoginHeader.setText("Ielogošanās");

        javax.swing.GroupLayout pnlLoginHeaderLayout = new javax.swing.GroupLayout(pnlLoginHeader);
        pnlLoginHeader.setLayout(pnlLoginHeaderLayout);
        pnlLoginHeaderLayout.setHorizontalGroup(
            pnlLoginHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginHeaderLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblLoginHeader)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlLoginHeaderLayout.setVerticalGroup(
            pnlLoginHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginHeaderLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblLoginHeader)
                .addGap(12, 12, 12))
        );

        pnlLoginBody.setBackground(new java.awt.Color(248, 250, 252));

        lblLoginHint.setForeground(new java.awt.Color(100, 116, 139));
        lblLoginHint.setText("Lūdzu, ievadiet savus piekļuves datus:");

        lblLoginUsername.setText("Lietotājvārds:");

        lblLoginPassword.setText("Parole:");

        btnLogin.setBackground(new java.awt.Color(59, 99, 224));
        btnLogin.setFont(btnLogin.getFont().deriveFont(btnLogin.getFont().getStyle() | java.awt.Font.BOLD));
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("Ielogoties");
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnLoginRegister.setForeground(new java.awt.Color(59, 99, 224));
        btnLoginRegister.setText("Reģistrēties");
        btnLoginRegister.setFocusPainted(false);
        btnLoginRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginRegisterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlLoginBodyLayout = new javax.swing.GroupLayout(pnlLoginBody);
        pnlLoginBody.setLayout(pnlLoginBodyLayout);
        pnlLoginBodyLayout.setHorizontalGroup(
            pnlLoginBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginBodyLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(pnlLoginBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLoginHint)
                    .addGroup(pnlLoginBodyLayout.createSequentialGroup()
                        .addGroup(pnlLoginBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblLoginUsername)
                            .addComponent(lblLoginPassword))
                        .addGap(24, 24, 24)
                        .addGroup(pnlLoginBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtLoginUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                            .addComponent(pwdLoginPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLoginBodyLayout.createSequentialGroup()
                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnLoginRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, 22))
        );
        pnlLoginBodyLayout.setVerticalGroup(
            pnlLoginBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginBodyLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblLoginHint)
                .addGap(16, 16, 16)
                .addGroup(pnlLoginBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLoginUsername)
                    .addComponent(txtLoginUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(pnlLoginBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLoginPassword)
                    .addComponent(pwdLoginPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(pnlLoginBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLoginRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout dlgLoginLayout = new javax.swing.GroupLayout(dlgLogin.getContentPane());
        dlgLogin.getContentPane().setLayout(dlgLoginLayout);
        dlgLoginLayout.setHorizontalGroup(
            dlgLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlLoginHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlLoginBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgLoginLayout.setVerticalGroup(
            dlgLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgLoginLayout.createSequentialGroup()
                .addComponent(pnlLoginHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlLoginBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dlgRegister.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        dlgRegister.setTitle("Pasākumu sistēma – Reģistrācija");
        dlgRegister.setResizable(false);
        dlgRegister.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                dlgRegisterWindowClosing(evt);
            }
        });

        pnlRegisterHeader.setBackground(new java.awt.Color(59, 99, 224));

        lblRegisterHeader.setFont(lblRegisterHeader.getFont().deriveFont(lblRegisterHeader.getFont().getStyle() | java.awt.Font.BOLD, lblRegisterHeader.getFont().getSize()+3));
        lblRegisterHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblRegisterHeader.setText("Reģistrācija");

        javax.swing.GroupLayout pnlRegisterHeaderLayout = new javax.swing.GroupLayout(pnlRegisterHeader);
        pnlRegisterHeader.setLayout(pnlRegisterHeaderLayout);
        pnlRegisterHeaderLayout.setHorizontalGroup(
            pnlRegisterHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegisterHeaderLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblRegisterHeader)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlRegisterHeaderLayout.setVerticalGroup(
            pnlRegisterHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegisterHeaderLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblRegisterHeader)
                .addGap(12, 12, 12))
        );

        pnlRegisterBody.setBackground(new java.awt.Color(248, 250, 252));

        lblRegisterHint.setForeground(new java.awt.Color(100, 116, 139));
        lblRegisterHint.setText("Aizpildiet visus laukus, lai izveidotu kontu");

        lblRegisterName.setText("Vārds:");

        lblRegisterSurname.setText("Uzvārds:");

        lblRegisterUsername.setText("E-pasts / Lietotājvārds:");

        lblRegisterPhone.setText("Tālrunis:");

        lblRegisterPassword.setText("Parole:");

        lblRegisterPassword2.setText("Parole (atkārtoti):");

        btnRegister.setBackground(new java.awt.Color(59, 99, 224));
        btnRegister.setFont(btnRegister.getFont().deriveFont(btnRegister.getFont().getStyle() | java.awt.Font.BOLD));
        btnRegister.setForeground(new java.awt.Color(255, 255, 255));
        btnRegister.setText("Reģistrēties");
        btnRegister.setFocusPainted(false);
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        btnRegisterCancel.setBackground(new java.awt.Color(242, 245, 250));
        btnRegisterCancel.setForeground(new java.awt.Color(51, 65, 81));
        btnRegisterCancel.setText("Atcelt");
        btnRegisterCancel.setFocusPainted(false);
        btnRegisterCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlRegisterBodyLayout = new javax.swing.GroupLayout(pnlRegisterBody);
        pnlRegisterBody.setLayout(pnlRegisterBodyLayout);
        pnlRegisterBodyLayout.setHorizontalGroup(
            pnlRegisterBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegisterBodyLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(pnlRegisterBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRegisterHint)
                    .addGroup(pnlRegisterBodyLayout.createSequentialGroup()
                        .addGroup(pnlRegisterBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRegisterName)
                            .addComponent(lblRegisterSurname)
                            .addComponent(lblRegisterUsername)
                            .addComponent(lblRegisterPhone)
                            .addComponent(lblRegisterPassword)
                            .addComponent(lblRegisterPassword2))
                        .addGap(24, 24, 24)
                        .addGroup(pnlRegisterBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtRegisterName, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                            .addComponent(txtRegisterSurname, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                            .addComponent(txtRegisterUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                            .addComponent(txtRegisterPhone, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                            .addComponent(pwdRegisterPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                            .addComponent(pwdRegisterPassword2, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)))
                    .addComponent(sepRegister)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRegisterBodyLayout.createSequentialGroup()
                        .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnRegisterCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, 22))
        );
        pnlRegisterBodyLayout.setVerticalGroup(
            pnlRegisterBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRegisterBodyLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblRegisterHint)
                .addGap(16, 16, 16)
                .addGroup(pnlRegisterBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRegisterName)
                    .addComponent(txtRegisterName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(pnlRegisterBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRegisterSurname)
                    .addComponent(txtRegisterSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(pnlRegisterBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRegisterUsername)
                    .addComponent(txtRegisterUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(pnlRegisterBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRegisterPhone)
                    .addComponent(txtRegisterPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(pnlRegisterBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRegisterPassword)
                    .addComponent(pwdRegisterPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(pnlRegisterBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRegisterPassword2)
                    .addComponent(pwdRegisterPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(sepRegister, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(pnlRegisterBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegisterCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout dlgRegisterLayout = new javax.swing.GroupLayout(dlgRegister.getContentPane());
        dlgRegister.getContentPane().setLayout(dlgRegisterLayout);
        dlgRegisterLayout.setHorizontalGroup(
            dlgRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlRegisterHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlRegisterBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgRegisterLayout.setVerticalGroup(
            dlgRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgRegisterLayout.createSequentialGroup()
                .addComponent(pnlRegisterHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlRegisterBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dlgAddEvent.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        dlgAddEvent.setTitle("Pasākumu sistēma – Pasākuma izveide");
        dlgAddEvent.setResizable(false);
        dlgAddEvent.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                dlgAddEventWindowClosing(evt);
            }
        });

        pnlEventHeader.setBackground(new java.awt.Color(59, 99, 224));

        lblEventHeader.setFont(lblEventHeader.getFont().deriveFont(lblEventHeader.getFont().getStyle() | java.awt.Font.BOLD, lblEventHeader.getFont().getSize()+3));
        lblEventHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblEventHeader.setText("Pasākuma izveide (administrators)");

        javax.swing.GroupLayout pnlEventHeaderLayout = new javax.swing.GroupLayout(pnlEventHeader);
        pnlEventHeader.setLayout(pnlEventHeaderLayout);
        pnlEventHeaderLayout.setHorizontalGroup(
            pnlEventHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEventHeaderLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblEventHeader)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlEventHeaderLayout.setVerticalGroup(
            pnlEventHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEventHeaderLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblEventHeader)
                .addGap(12, 12, 12))
        );

        pnlEventBody.setBackground(new java.awt.Color(248, 250, 252));

        lblEventHint.setForeground(new java.awt.Color(100, 116, 139));
        lblEventHint.setText("Aizpildiet pasākuma datus");

        lblEventName.setText("Nosaukums:");

        lblEventDescription.setText("Apraksts:");

        txtEventDescription.setColumns(20);
        txtEventDescription.setLineWrap(true);
        txtEventDescription.setRows(3);
        txtEventDescription.setWrapStyleWord(true);
        scrEventDescription.setViewportView(txtEventDescription);

        lblEventDate.setText("Norises datums:");

        lblEventTime.setText("Norises laiks:");

        lblEventPlace.setText("Vieta:");

        lblEventMax.setText("Maks. dalībnieku skaits:");

        lblEventNote.setForeground(new java.awt.Color(178, 106, 30));
        lblEventNote.setText("* datums dd.mm.gggg (nedrīkst būt pagātnē), laiks hh:mm");

        btnEventSave.setBackground(new java.awt.Color(59, 99, 224));
        btnEventSave.setFont(btnEventSave.getFont().deriveFont(btnEventSave.getFont().getStyle() | java.awt.Font.BOLD));
        btnEventSave.setForeground(new java.awt.Color(255, 255, 255));
        btnEventSave.setText("Saglabāt");
        btnEventSave.setFocusPainted(false);
        btnEventSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEventSaveActionPerformed(evt);
            }
        });

        btnEventCancel.setBackground(new java.awt.Color(242, 245, 250));
        btnEventCancel.setForeground(new java.awt.Color(51, 65, 81));
        btnEventCancel.setText("Atcelt");
        btnEventCancel.setFocusPainted(false);
        btnEventCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEventCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlEventBodyLayout = new javax.swing.GroupLayout(pnlEventBody);
        pnlEventBody.setLayout(pnlEventBodyLayout);
        pnlEventBodyLayout.setHorizontalGroup(
            pnlEventBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEventBodyLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(pnlEventBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEventHint)
                    .addGroup(pnlEventBodyLayout.createSequentialGroup()
                        .addGroup(pnlEventBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEventName)
                            .addComponent(lblEventDescription)
                            .addComponent(lblEventDate)
                            .addComponent(lblEventTime)
                            .addComponent(lblEventPlace)
                            .addComponent(lblEventMax))
                        .addGap(24, 24, 24)
                        .addGroup(pnlEventBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEventName, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addComponent(scrEventDescription, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addComponent(txtEventDate, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEventTime, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEventPlace, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addComponent(spnEventMax, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblEventNote)))
                    .addComponent(sepEvent)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEventBodyLayout.createSequentialGroup()
                        .addComponent(btnEventSave, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnEventCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, 22))
        );
        pnlEventBodyLayout.setVerticalGroup(
            pnlEventBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEventBodyLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblEventHint)
                .addGap(16, 16, 16)
                .addGroup(pnlEventBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEventName)
                    .addComponent(txtEventName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(pnlEventBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEventDescription)
                    .addComponent(scrEventDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(pnlEventBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEventDate)
                    .addComponent(txtEventDate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(pnlEventBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEventTime)
                    .addComponent(txtEventTime, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(pnlEventBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEventPlace)
                    .addComponent(txtEventPlace, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(pnlEventBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEventMax)
                    .addComponent(spnEventMax, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(lblEventNote)
                .addGap(16, 16, 16)
                .addComponent(sepEvent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(pnlEventBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEventSave, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEventCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout dlgAddEventLayout = new javax.swing.GroupLayout(dlgAddEvent.getContentPane());
        dlgAddEvent.getContentPane().setLayout(dlgAddEventLayout);
        dlgAddEventLayout.setHorizontalGroup(
            dlgAddEventLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlEventHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlEventBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgAddEventLayout.setVerticalGroup(
            dlgAddEventLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAddEventLayout.createSequentialGroup()
                .addComponent(pnlEventHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(pnlEventBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dlgParticipants.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        dlgParticipants.setTitle("Pasākumu sistēma – Dalībnieku pārvaldība");
        dlgParticipants.setResizable(false);
        dlgParticipants.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                dlgParticipantsWindowClosing(evt);
            }
        });

        pnlParticipantsHeader.setBackground(new java.awt.Color(59, 99, 224));

        lblParticipantsHeader.setFont(lblParticipantsHeader.getFont().deriveFont(lblParticipantsHeader.getFont().getStyle() | java.awt.Font.BOLD, lblParticipantsHeader.getFont().getSize()+3));
        lblParticipantsHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblParticipantsHeader.setText("Dalībnieku pārvaldība (administrators)");

        javax.swing.GroupLayout pnlParticipantsHeaderLayout = new javax.swing.GroupLayout(pnlParticipantsHeader);
        pnlParticipantsHeader.setLayout(pnlParticipantsHeaderLayout);
        pnlParticipantsHeaderLayout.setHorizontalGroup(
            pnlParticipantsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlParticipantsHeaderLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblParticipantsHeader)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlParticipantsHeaderLayout.setVerticalGroup(
            pnlParticipantsHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlParticipantsHeaderLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblParticipantsHeader)
                .addGap(12, 12, 12))
        );

        pnlParticipantsBody.setBackground(new java.awt.Color(248, 250, 252));

        lblParticipantsEvent.setText("Pasākums:");

        tblParticipants.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Vārds", "Uzvārds", "E-pasts", "Tālrunis", "Statuss"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrParticipants.setViewportView(tblParticipants);

        btnApprove.setBackground(new java.awt.Color(76, 163, 84));
        btnApprove.setFont(btnApprove.getFont().deriveFont(btnApprove.getFont().getStyle() | java.awt.Font.BOLD));
        btnApprove.setForeground(new java.awt.Color(255, 255, 255));
        btnApprove.setText("Apstiprināt");
        btnApprove.setFocusPainted(false);

        btnReject.setBackground(new java.awt.Color(201, 58, 50));
        btnReject.setFont(btnReject.getFont().deriveFont(btnReject.getFont().getStyle() | java.awt.Font.BOLD));
        btnReject.setForeground(new java.awt.Color(255, 255, 255));
        btnReject.setText("Noraidīt");
        btnReject.setFocusPainted(false);

        btnParticipantsClose.setBackground(new java.awt.Color(242, 245, 250));
        btnParticipantsClose.setForeground(new java.awt.Color(51, 65, 81));
        btnParticipantsClose.setText("Aizvērt");
        btnParticipantsClose.setFocusPainted(false);
        btnParticipantsClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnParticipantsCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlParticipantsBodyLayout = new javax.swing.GroupLayout(pnlParticipantsBody);
        pnlParticipantsBody.setLayout(pnlParticipantsBodyLayout);
        pnlParticipantsBodyLayout.setHorizontalGroup(
            pnlParticipantsBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlParticipantsBodyLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(pnlParticipantsBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlParticipantsBodyLayout.createSequentialGroup()
                        .addComponent(lblParticipantsEvent)
                        .addGap(24, 24, 24)
                        .addComponent(cmbParticipantsEvent, 0, 320, Short.MAX_VALUE))
                    .addComponent(scrParticipants, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                    .addGroup(pnlParticipantsBodyLayout.createSequentialGroup()
                        .addComponent(btnApprove, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnReject, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnParticipantsClose, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(22, 22, 22))
        );
        pnlParticipantsBodyLayout.setVerticalGroup(
            pnlParticipantsBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlParticipantsBodyLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnlParticipantsBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblParticipantsEvent)
                    .addComponent(cmbParticipantsEvent, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(scrParticipants, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addGap(22, 22, 22)
                .addGroup(pnlParticipantsBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnApprove, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReject, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnParticipantsClose, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout dlgParticipantsLayout = new javax.swing.GroupLayout(dlgParticipants.getContentPane());
        dlgParticipants.getContentPane().setLayout(dlgParticipantsLayout);
        dlgParticipantsLayout.setHorizontalGroup(
            dlgParticipantsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlParticipantsHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlParticipantsBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dlgParticipantsLayout.setVerticalGroup(
            dlgParticipantsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgParticipantsLayout.createSequentialGroup()
                .addComponent(pnlParticipantsHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(pnlParticipantsBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Pasākumu sistēma – Pasākumu saraksts");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

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

        btnDalibnieki.setFont(btnDalibnieki.getFont().deriveFont(btnDalibnieki.getFont().getSize()+1f));
        btnDalibnieki.setForeground(new java.awt.Color(59, 99, 224));
        btnDalibnieki.setText("Dalībnieki");
        btnDalibnieki.setFocusPainted(false);
        btnDalibnieki.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDalibniekiActionPerformed(evt);
            }
        });

        btnPievienotPasakumu.setForeground(new java.awt.Color(59, 99, 224));
        btnPievienotPasakumu.setText("Pievienot pasākumu");
        btnPievienotPasakumu.setFocusPainted(false);
        btnPievienotPasakumu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPievienotPasakumuActionPerformed(evt);
            }
        });

        btnIzrakstities.setBackground(new java.awt.Color(242, 245, 250));
        btnIzrakstities.setForeground(new java.awt.Color(51, 65, 81));
        btnIzrakstities.setText("Izrakstīties");
        btnIzrakstities.setFocusPainted(false);
        btnIzrakstities.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIzrakstitiesActionPerformed(evt);
            }
        });

        btnIziet.setBackground(new java.awt.Color(242, 245, 250));
        btnIziet.setForeground(new java.awt.Color(51, 65, 81));
        btnIziet.setText("Iziet");
        btnIziet.setFocusPainted(false);
        btnIziet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIzietActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBodyLayout = new javax.swing.GroupLayout(pnlBody);
        pnlBody.setLayout(pnlBodyLayout);
        pnlBodyLayout.setHorizontalGroup(
            pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBodyLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(pnlBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrPasakumi)
                    .addGroup(pnlBodyLayout.createSequentialGroup()
                        .addComponent(btnRegistretiesDaliba, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnDalibnieki, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnPievienotPasakumu, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnIzrakstities, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btnIziet, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(btnPievienotPasakumu, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIzrakstities, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIziet, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGap(0, 0, 0)
                .addComponent(pnlBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        iziet(this);
    }//GEN-LAST:event_formWindowClosing

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
     String lietotajvards = txtLoginUsername.getText().trim();
        String parole = new String(pwdLoginPassword.getPassword());

        if (lietotajvards.isEmpty() || parole.isEmpty()) {
            kluda(dlgLogin, "Ievadiet lietotājvārdu un paroli!");
            return;
        }
        Lietotajs l = lietotajuDAO.atrastLietotaju(lietotajvards);
        if (l == null || !l.parbauditParoli(parole)) {
            kluda(dlgLogin, "Nepareizs lietotājvārds vai parole!");
            return;
        }

        currentUser = l;
        pwdLoginPassword.setText("");
        uzstaditTiesibas();
        ieladetPasakumus();
        info(dlgLogin, "Ieeja ir veiksmīgi pabeigta!");
        dlgLogin.setVisible(false);
        atvertGalvenoLogu();
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnLoginRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginRegisterActionPerformed
        dlgLogin.setVisible(false);
        paraditLogu(dlgRegister);
    }//GEN-LAST:event_btnLoginRegisterActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
            String vards = txtRegisterName.getText().trim();
        String uzvards = txtRegisterSurname.getText().trim();
        String lietotajvards = txtRegisterUsername.getText().trim();
        String talrunis = txtRegisterPhone.getText().trim();
        String parole = new String(pwdRegisterPassword.getPassword());
        String parole2 = new String(pwdRegisterPassword2.getPassword());

        if (vards.isEmpty() || uzvards.isEmpty() || lietotajvards.isEmpty() || parole.isEmpty()) {
            kluda(dlgRegister, "Aizpildiet visus obligātos laukus!");
            return;
        }
        if (!lietotajvards.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            kluda(dlgRegister, "Ievadiet derīgu e-pasta adresi!");
            return;
        }
        if (parole.length() < 6) {
            kluda(dlgRegister, "Parolei jābūt vismaz 6 simbolus garai!");
            return;
        }
        if (!parole.equals(parole2)) {
            kluda(dlgRegister, "Paroles nesakrīt!");
            return;
        }
        if (vards.length() > 50 || uzvards.length() > 50 || lietotajvards.length() > 50 || talrunis.length() > 20) {
            kluda(dlgRegister, "Kāds no laukiem ir pārāk garš!");
            return;
        }
        if (lietotajuDAO.atrastLietotaju(lietotajvards) != null) {
            kluda(dlgRegister, "Šāds lietotājvārds jau eksistē!");
            return;
        }

        if (lietotajuDAO.registretLietotaju(vards, uzvards, lietotajvards,
                talrunis.isEmpty() ? null : talrunis, parole)) {
            info(dlgRegister, "Reģistrēšana ir veiksmīgi pabeigta!");
            txtRegisterName.setText("");
            txtRegisterSurname.setText("");
            txtRegisterUsername.setText("");
            txtRegisterPhone.setText("");
            pwdRegisterPassword.setText("");
            pwdRegisterPassword2.setText("");
            txtLoginUsername.setText(lietotajvards);
            dlgRegister.setVisible(false);
            paraditLogu(dlgLogin);
        } else {
            kluda(dlgRegister, "Neizdevās saglabāt datus datu bāzē.");
        }
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnRegisterCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterCancelActionPerformed
        dlgRegister.setVisible(false);
        paraditLogu(dlgLogin);
    }//GEN-LAST:event_btnRegisterCancelActionPerformed

    private void dlgLoginWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_dlgLoginWindowClosing
        iziet(dlgLogin);
    }//GEN-LAST:event_dlgLoginWindowClosing

    private void dlgRegisterWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_dlgRegisterWindowClosing
        dlgRegister.setVisible(false);
        paraditLogu(dlgLogin);
    }//GEN-LAST:event_dlgRegisterWindowClosing

    private void btnIzrakstitiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIzrakstitiesActionPerformed
        int atbilde = JOptionPane.showConfirmDialog(this, "Vai tiešām vēlaties izrakstīties?",
                "Izrakstīties", JOptionPane.YES_NO_OPTION);
        if (atbilde == JOptionPane.YES_OPTION) {
            currentUser = null;
            setVisible(false);
            txtLoginUsername.setText("");
            pwdLoginPassword.setText("");
            paraditLogu(dlgLogin);
        }
    }//GEN-LAST:event_btnIzrakstitiesActionPerformed

    private void btnIzietActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIzietActionPerformed
        iziet(this);
    }//GEN-LAST:event_btnIzietActionPerformed

    private void btnPievienotPasakumuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPievienotPasakumuActionPerformed
        setVisible(false);
        paraditLogu(dlgAddEvent);
    }//GEN-LAST:event_btnPievienotPasakumuActionPerformed

    private void btnEventSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEventSaveActionPerformed
            if (!(currentUser instanceof Administrators)) {
            kluda(dlgAddEvent, "Tikai administrators var izveidot pasākumus!");
            return;
        }

        String nosaukums = txtEventName.getText().trim();
        String apraksts = txtEventDescription.getText().trim();
        String datumsTeksts = txtEventDate.getText().trim();
        String laiksTeksts = txtEventTime.getText().trim();
        String vieta = txtEventPlace.getText().trim();
        int maks = (Integer) spnEventMax.getValue();

        if (nosaukums.isEmpty() || datumsTeksts.isEmpty() || laiksTeksts.isEmpty() || vieta.isEmpty()) {
            kluda(dlgAddEvent, "Aizpildiet visus obligātos laukus!");
            return;
        }
        if (nosaukums.length() > 100 || vieta.length() > 100 || apraksts.length() > 500) {
            kluda(dlgAddEvent, "Kāds no laukiem ir pārāk garš!");
            return;
        }

        LocalDate datums;
        try {
            datums = LocalDate.parse(datumsTeksts, DATUMA_FORMATS);
        } catch (DateTimeParseException ex) {
            kluda(dlgAddEvent, "Datums jāievada formātā dd.mm.gggg!");
            return;
        }
        if (datums.isBefore(LocalDate.now())) {
            kluda(dlgAddEvent, "Datums nedrīkst būt pagātnē!");
            return;
        }

        String laiks;
        try {
            laiks = LocalTime.parse(laiksTeksts, DateTimeFormatter.ofPattern("H:mm"))
                    .format(DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException ex) {
            kluda(dlgAddEvent, "Laiks jāievada formātā hh:mm!");
            return;
        }
        if (maks <= 0) {
            kluda(dlgAddEvent, "Dalībnieku skaitam jābūt lielākam par 0!");
            return;
        }

        Pasakums p = ((Administrators) currentUser).izveidotPasakumu(nosaukums,
                apraksts.isEmpty() ? null : apraksts, java.sql.Date.valueOf(datums), laiks, vieta, maks);

        if (pasakumuDAO.saglabatPasakumu(p)) {
            info(dlgAddEvent, "Pasākums veiksmīgi izveidots!");
            txtEventName.setText("");
            txtEventDescription.setText("");
            txtEventDate.setText("");
            txtEventTime.setText("");
            txtEventPlace.setText("");
            spnEventMax.setValue(10);
            ieladetPasakumus();
            dlgAddEvent.setVisible(false);
            atvertGalvenoLogu();
        } else {
            kluda(dlgAddEvent, "Neizdevās saglabāt pasākumu datu bāzē.");
        }
    }//GEN-LAST:event_btnEventSaveActionPerformed

    private void btnEventCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEventCancelActionPerformed
        dlgAddEvent.setVisible(false);
        atvertGalvenoLogu();
    }//GEN-LAST:event_btnEventCancelActionPerformed

    private void dlgAddEventWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_dlgAddEventWindowClosing
        dlgAddEvent.setVisible(false);
        atvertGalvenoLogu();
    }//GEN-LAST:event_dlgAddEventWindowClosing

    private void btnDalibniekiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDalibniekiActionPerformed
        ieladetPasakumus();
        ieladetDalibniekus();
        setVisible(false);
        paraditLogu(dlgParticipants);
    }//GEN-LAST:event_btnDalibniekiActionPerformed

    private void btnParticipantsCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnParticipantsCloseActionPerformed
        dlgParticipants.setVisible(false);
        atvertGalvenoLogu();
    }//GEN-LAST:event_btnParticipantsCloseActionPerformed

    private void dlgParticipantsWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_dlgParticipantsWindowClosing
        dlgParticipants.setVisible(false);
        atvertGalvenoLogu();
    }//GEN-LAST:event_dlgParticipantsWindowClosing

    // atver logu ekrāna vidū
    private void paraditLogu(javax.swing.JDialog logs) {
        logs.pack();
        logs.setLocationRelativeTo(null);
        logs.setVisible(true);
    }

    // pirms aizvēršanas pajautā
    private void iziet(java.awt.Component logs) {
        int atbilde = JOptionPane.showConfirmDialog(logs, "Vai tiešām vēlaties aizvērt programmu?",
                "Iziet", JOptionPane.YES_NO_OPTION);
        if (atbilde == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

        // ---------- datu bāzes savienojums ar GUI ----------

    private void pievienotKlausitajus() {
        btnRegistretiesDaliba.addActionListener(e -> pieteiktiesPasakumam());
        btnApprove.addActionListener(e -> apstradatPieteikumu(true));
        btnReject.addActionListener(e -> apstradatPieteikumu(false));
        cmbParticipantsEvent.addActionListener(e -> {
            if (!atjaunina) {
                ieladetDalibniekus();
            }
        });
    }

    private void kluda(java.awt.Component vecaks, String teksts) {
        JOptionPane.showMessageDialog(vecaks, teksts, "Kļūda", JOptionPane.ERROR_MESSAGE);
    }

    private void info(java.awt.Component vecaks, String teksts) {
        JOptionPane.showMessageDialog(vecaks, teksts, "Informācija", JOptionPane.INFORMATION_MESSAGE);
    }

    private String formatDatums(Pasakums p) {
        return p.getDatums().toLocalDate().format(DATUMA_FORMATS) + " " + p.getLaiks();
    }

    // administrators redz pārvaldību, dalībnieks - pieteikšanos
    private void uzstaditTiesibas() {
        boolean admin = currentUser instanceof Administrators;
        btnPievienotPasakumu.setVisible(admin);
        btnDalibnieki.setVisible(admin);
        btnRegistretiesDaliba.setVisible(!admin);
        lblVirsraksts.setText("Pasākumu saraksts – " + currentUser.getVards() + " " + currentUser.getUzvards());
    }

    // pasākumu tabula + izvēlne dalībnieku logā
    private void ieladetPasakumus() {
        int izvelesId = -1;
        int ind = cmbParticipantsEvent.getSelectedIndex();
        if (ind >= 0 && ind < pasakumi.size()) {
            izvelesId = pasakumi.get(ind).getId();
        }

        pasakumi = pasakumuDAO.iegutVisusPasakumus();
        DefaultTableModel tm = (DefaultTableModel) tblPasakumi.getModel();
        tm.setRowCount(0);

        String[] nosaukumi = new String[pasakumi.size()];
        int jaunaisInd = pasakumi.isEmpty() ? -1 : 0;
        for (int i = 0; i < pasakumi.size(); i++) {
            Pasakums p = pasakumi.get(i);
            tm.addRow(new Object[]{p.getNosaukums(), formatDatums(p), p.getVieta(), p.getBrivoVietuSkaitu()});
            nosaukumi[i] = p.getNosaukums() + " (" + formatDatums(p) + ")";
            if (p.getId() == izvelesId) {
                jaunaisInd = i;
            }
        }

        atjaunina = true;
        cmbParticipantsEvent.setModel(new DefaultComboBoxModel<>(nosaukumi));
        if (jaunaisInd >= 0) {
            cmbParticipantsEvent.setSelectedIndex(jaunaisInd);
        }
        atjaunina = false;
    }

    // dalībnieku tabula izvēlētajam pasākumam
    private void ieladetDalibniekus() {
        DefaultTableModel tm = (DefaultTableModel) tblParticipants.getModel();
        tm.setRowCount(0);

        int ind = cmbParticipantsEvent.getSelectedIndex();
        if (ind < 0 || ind >= pasakumi.size()) {
            dalibnieki = new ArrayList<>();
            return;
        }
        dalibnieki = registracijaDAO.iegutPasakumaDalibniekus(pasakumi.get(ind));
        for (Registracija r : dalibnieki) {
            Dalibnieks d = r.getDalibnieks();
            tm.addRow(new Object[]{d.getVards(), d.getUzvards(), d.getLietotajvardu(),
                d.getTalrunis() == null ? "" : d.getTalrunis(), r.getStatussTeksts()});
        }
    }

    // poga "Reģistrēties dalībai"
    private void pieteiktiesPasakumam() {
        if (!(currentUser instanceof Dalibnieks)) {
            info(this, "Administrators nevar pieteikties dalībai pasākumā.");
            return;
        }
        int rinda = tblPasakumi.getSelectedRow();
        if (rinda < 0 || rinda >= pasakumi.size()) {
            kluda(this, "Izvēlieties pasākumu no saraksta!");
            return;
        }
        Pasakums p = pasakumi.get(tblPasakumi.convertRowIndexToModel(rinda));

        if (p.getBrivoVietuSkaitu() <= 0) {
            kluda(this, "Vietu skaits ir pilns!");
            return;
        }
        if (registracijaDAO.jauPieteicies(p.getId(), currentUser.getId())) {
            kluda(this, "Jūs jau esat pieteicies šim pasākumam!");
            return;
        }

        Registracija r = ((Dalibnieks) currentUser).registretiesPasakumam(p);
        if (registracijaDAO.saglabatRegistraciju(r)) {
            info(this, "Pieteikums nosūtīts, gaidiet apstiprinājumu!");
        } else {
            kluda(this, "Neizdevās saglabāt pieteikumu. Mēģiniet vēlreiz.");
        }
        ieladetPasakumus();
    }

    // pogas "Apstiprināt" / "Noraidīt"
    private void apstradatPieteikumu(boolean apstiprinat) {
        int rinda = tblParticipants.getSelectedRow();
        if (rinda < 0 || rinda >= dalibnieki.size()) {
            kluda(dlgParticipants, "Izvēlieties pieteikumu no saraksta!");
            return;
        }
        Registracija r = dalibnieki.get(tblParticipants.convertRowIndexToModel(rinda));

        if (apstiprinat) {
            int rez = registracijaDAO.apstiprinatArKontroli(r.getId());
            if (rez == -1) {
                kluda(dlgParticipants, "Vietu skaits ir pilns!");
                return;
            } else if (rez == 0) {
                kluda(dlgParticipants, "Neizdevās atjaunināt statusu.");
                return;
            }
        } else {
            int atbilde = JOptionPane.showConfirmDialog(dlgParticipants,
                    "Vai tiešām vēlaties noraidīt šo pieteikumu?",
                    "Noraidīt", JOptionPane.YES_NO_OPTION);
            if (atbilde != JOptionPane.YES_OPTION) {
                return;
            }
            if (!registracijaDAO.mainitStatusu(r.getId(), Registracija.NORAIDITS)) {
                kluda(dlgParticipants, "Neizdevās atjaunināt statusu.");
                return;
            }
        }
        info(dlgParticipants, "Statuss atjaunināts!");
        ieladetPasakumus();
        ieladetDalibniekus();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // Metal, citādi nestrādā pogu krāsas
        try {
            javax.swing.UIManager.put("swing.boldMetal", Boolean.FALSE);
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
            // Yes/No latviski
            javax.swing.UIManager.put("OptionPane.yesButtonText", "Jā");
            javax.swing.UIManager.put("OptionPane.noButtonText", "Nē");
            javax.swing.UIManager.put("OptionPane.okButtonText", "Labi");
            javax.swing.UIManager.put("OptionPane.cancelButtonText", "Atcelt");
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        try {
            DatabaseUtil.init();
        } catch (SQLException e) {
            System.err.println("Neizdevās pieslēgties datu bāzei: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "Neizdevās pieslēgties datu bāzei.\nPārbaudiet, vai Java DB serveris ir palaists.",
                    "Datu bāzes kļūda", JOptionPane.ERROR_MESSAGE);
        }

        // sākumā atveras login logs
        java.awt.EventQueue.invokeLater(() -> {
            EventRegistrationSystemGUI gui = new EventRegistrationSystemGUI();
            gui.paraditLogu(gui.dlgLogin);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApprove;
    private javax.swing.JButton btnDalibnieki;
    private javax.swing.JButton btnEventCancel;
    private javax.swing.JButton btnEventSave;
    private javax.swing.JButton btnIziet;
    private javax.swing.JButton btnIzrakstities;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnLoginRegister;
    private javax.swing.JButton btnParticipantsClose;
    private javax.swing.JButton btnPievienotPasakumu;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnRegisterCancel;
    private javax.swing.JButton btnRegistretiesDaliba;
    private javax.swing.JButton btnReject;
    private javax.swing.JComboBox<String> cmbParticipantsEvent;
    private javax.swing.JDialog dlgAddEvent;
    private javax.swing.JDialog dlgLogin;
    private javax.swing.JDialog dlgParticipants;
    private javax.swing.JDialog dlgRegister;
    private javax.swing.JLabel lblEventDate;
    private javax.swing.JLabel lblEventDescription;
    private javax.swing.JLabel lblEventHeader;
    private javax.swing.JLabel lblEventHint;
    private javax.swing.JLabel lblEventMax;
    private javax.swing.JLabel lblEventName;
    private javax.swing.JLabel lblEventNote;
    private javax.swing.JLabel lblEventPlace;
    private javax.swing.JLabel lblEventTime;
    private javax.swing.JLabel lblLoginHeader;
    private javax.swing.JLabel lblLoginHint;
    private javax.swing.JLabel lblLoginPassword;
    private javax.swing.JLabel lblLoginUsername;
    private javax.swing.JLabel lblParticipantsEvent;
    private javax.swing.JLabel lblParticipantsHeader;
    private javax.swing.JLabel lblRegisterHeader;
    private javax.swing.JLabel lblRegisterHint;
    private javax.swing.JLabel lblRegisterName;
    private javax.swing.JLabel lblRegisterPassword;
    private javax.swing.JLabel lblRegisterPassword2;
    private javax.swing.JLabel lblRegisterPhone;
    private javax.swing.JLabel lblRegisterSurname;
    private javax.swing.JLabel lblRegisterUsername;
    private javax.swing.JLabel lblVirsraksts;
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlEventBody;
    private javax.swing.JPanel pnlEventHeader;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlLoginBody;
    private javax.swing.JPanel pnlLoginHeader;
    private javax.swing.JPanel pnlParticipantsBody;
    private javax.swing.JPanel pnlParticipantsHeader;
    private javax.swing.JPanel pnlRegisterBody;
    private javax.swing.JPanel pnlRegisterHeader;
    private javax.swing.JPasswordField pwdLoginPassword;
    private javax.swing.JPasswordField pwdRegisterPassword;
    private javax.swing.JPasswordField pwdRegisterPassword2;
    private javax.swing.JScrollPane scrEventDescription;
    private javax.swing.JScrollPane scrParticipants;
    private javax.swing.JScrollPane scrPasakumi;
    private javax.swing.JSeparator sepEvent;
    private javax.swing.JSeparator sepRegister;
    private javax.swing.JSpinner spnEventMax;
    private javax.swing.JTable tblParticipants;
    private javax.swing.JTable tblPasakumi;
    private javax.swing.JTextField txtEventDate;
    private javax.swing.JTextArea txtEventDescription;
    private javax.swing.JTextField txtEventName;
    private javax.swing.JTextField txtEventPlace;
    private javax.swing.JTextField txtEventTime;
    private javax.swing.JTextField txtLoginUsername;
    private javax.swing.JTextField txtRegisterName;
    private javax.swing.JTextField txtRegisterPhone;
    private javax.swing.JTextField txtRegisterSurname;
    private javax.swing.JTextField txtRegisterUsername;
    // End of variables declaration//GEN-END:variables
}
