import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FormMahasiswa extends JFrame implements ActionListener {

    // ===== Atomic Component =====
    private JTextField tfNim = new JTextField(15);
    private JTextField tfNama = new JTextField(15);
    private JRadioButton rbLaki = new JRadioButton("Laki-laki");
    private JRadioButton rbPerempuan = new JRadioButton("Perempuan");
    private ButtonGroup bgGender = new ButtonGroup(); // agar hanya satu radio yang aktif
    private JComboBox<String> cbProdi = new JComboBox<>(
            new String[]{"Teknik Informatika", "Sistem Informasi", "Teknik Elektro"});
    private JCheckBox chkCoding = new JCheckBox("Coding");
    private JCheckBox chkMusik = new JCheckBox("Musik");
    private JCheckBox chkOlahraga = new JCheckBox("Olahraga");
    private JButton btnSimpan = new JButton("Simpan");
    private JButton btnReset = new JButton("Reset");
    private JButton btnKeluar = new JButton("Keluar");
    private JTextArea taHasil = new JTextArea();
    private JLabel lblStatus = new JLabel("Silakan isi form.");

    // ===== Intermediate Container =====
    private JTabbedPane tab = new JTabbedPane();

    public FormMahasiswa() {
        // ===== Top-level Container =====
        setTitle("Form Biodata Mahasiswa");
        setSize(460, 400);
        setLocationRelativeTo(null);
        // DO_NOTHING_ON_CLOSE: penutupan jendela kita tangani sendiri (lihat WindowAdapter)
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Layout 1: BorderLayout untuk content pane frame
        getContentPane().setLayout(new BorderLayout(5, 5));

        // Tab "Form" dan tab "Hasil"
        taHasil.setEditable(false);
        tab.addTab("Form", buatPanelForm());
        tab.addTab("Hasil", new JScrollPane(taHasil));

        // Panel bawah: tombol (GridLayout) + label status
        JPanel panelTombol = new JPanel(new GridLayout(1, 3, 10, 0)); // Layout 3
        panelTombol.add(btnSimpan);
        panelTombol.add(btnReset);
        panelTombol.add(btnKeluar);

        JPanel panelBawah = new JPanel(new BorderLayout(0, 5));
        panelBawah.add(panelTombol, BorderLayout.CENTER);
        panelBawah.add(lblStatus, BorderLayout.SOUTH);

        getContentPane().add(tab, BorderLayout.CENTER);
        getContentPane().add(panelBawah, BorderLayout.SOUTH);

        pasangEvent();
    }

    // Membuat panel form dengan GridBagLayout (Layout 2)
    private JPanel buatPanelForm() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6); // jarak antar komponen
        c.anchor = GridBagConstraints.WEST; // rata kiri

        c.gridx = 0; c.gridy = 0; p.add(new JLabel("NIM"), c);
        c.gridx = 1;              p.add(tfNim, c);

        c.gridx = 0; c.gridy = 1; p.add(new JLabel("Nama"), c);
        c.gridx = 1;              p.add(tfNama, c);

        bgGender.add(rbLaki);
        bgGender.add(rbPerempuan);
        JPanel panelGender = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // Layout 4
        panelGender.add(rbLaki);
        panelGender.add(rbPerempuan);
        c.gridx = 0; c.gridy = 2; p.add(new JLabel("Jenis Kelamin"), c);
        c.gridx = 1;              p.add(panelGender, c);

        c.gridx = 0; c.gridy = 3; p.add(new JLabel("Program Studi"), c);
        c.gridx = 1;              p.add(cbProdi, c);

        JPanel panelHobi = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelHobi.add(chkCoding);
        panelHobi.add(chkMusik);
        panelHobi.add(chkOlahraga);
        c.gridx = 0; c.gridy = 4; p.add(new JLabel("Hobi"), c);
        c.gridx = 1;              p.add(panelHobi, c);

        return p;
    }

    // ===== Event Listener =====
    private void pasangEvent() {
        // 1) ActionListener (interface) - kelas ini yang menangani
        btnSimpan.addActionListener(this);
        btnReset.addActionListener(this);
        btnKeluar.addActionListener(this);

        // 2) KeyAdapter - NIM hanya boleh angka
        tfNim.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                if (!Character.isDigit(ch) && ch != KeyEvent.VK_BACK_SPACE) {
                    e.consume(); // batalkan karakter yang bukan angka
                    lblStatus.setText("NIM hanya boleh berisi angka!");
                }
            }
        });

        // 3) MouseAdapter - tampilkan petunjuk saat mouse di atas tombol Simpan
        btnSimpan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lblStatus.setText("Klik untuk menyimpan data.");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblStatus.setText("Silakan isi form.");
            }
        });

        // 4) WindowAdapter - konfirmasi saat tombol close (X) ditekan
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                konfirmasiKeluar();
            }
        });
    }

    // Override method dari interface ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        Object sumber = e.getSource();
        if (sumber == btnSimpan) {
            simpan();
        } else if (sumber == btnReset) {
            reset();
        } else if (sumber == btnKeluar) {
            konfirmasiKeluar();
        }
    }

    // ===== Method bantu =====
    private void simpan() {
        String nim = tfNim.getText().trim();
        String nama = tfNama.getText().trim();

        if (nim.isEmpty() || nama.isEmpty() || !(rbLaki.isSelected() || rbPerempuan.isSelected())) {
            JOptionPane.showMessageDialog(this, "NIM, Nama, dan Jenis Kelamin wajib diisi!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String gender = rbLaki.isSelected() ? "Laki-laki" : "Perempuan";

        String hobi = "";
        if (chkCoding.isSelected()) hobi += "Coding ";
        if (chkMusik.isSelected()) hobi += "Musik ";
        if (chkOlahraga.isSelected()) hobi += "Olahraga ";
        if (hobi.isEmpty()) hobi = "-";

        taHasil.append("NIM           : " + nim + "\n");
        taHasil.append("Nama          : " + nama + "\n");
        taHasil.append("Jenis Kelamin : " + gender + "\n");
        taHasil.append("Program Studi : " + cbProdi.getSelectedItem() + "\n");
        taHasil.append("Hobi          : " + hobi.trim() + "\n");
        taHasil.append("--------------------------------\n");

        tampilDialogSukses(nama);
        tab.setSelectedIndex(1); // pindah ke tab Hasil
        lblStatus.setText("Data " + nama + " tersimpan.");
    }

    // Top-level container kedua: JDialog buatan sendiri (modal)
    private void tampilDialogSukses(String nama) {
        final JDialog dlg = new JDialog(this, "Berhasil", true);
        dlg.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
        dlg.getContentPane().add(new JLabel("Data " + nama + " berhasil disimpan!"));
        JButton btnOk = new JButton("OK");
        btnOk.addActionListener(ev -> dlg.dispose()); // lambda = bentuk singkat ActionListener
        dlg.getContentPane().add(btnOk);
        dlg.setSize(300, 130);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void reset() {
        tfNim.setText("");
        tfNama.setText("");
        bgGender.clearSelection();
        cbProdi.setSelectedIndex(0);
        chkCoding.setSelected(false);
        chkMusik.setSelected(false);
        chkOlahraga.setSelected(false);
        tab.setSelectedIndex(0);
        lblStatus.setText("Form dikosongkan.");
    }

    private void konfirmasiKeluar() {
        int pilihan = JOptionPane.showConfirmDialog(this, "Yakin ingin keluar?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (pilihan == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormMahasiswa().setVisible(true));
    }
}
