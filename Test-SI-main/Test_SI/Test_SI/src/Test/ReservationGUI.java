package Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationGUI extends JFrame {
    private JTextField nomField, dateField, dureeField, salleField;
    private JButton ajouterBtn, modifierBtn, supprimerBtn;
    private JTable table;
    private DefaultTableModel tableModel;
    private FonctionnalitesReservation gestionReservations;
    private JComboBox<String> batimentComboBox;

    public ReservationGUI() {
        gestionReservations = new FonctionnalitesReservation();

        setTitle("Gestion des Réservations");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel labelNom = new JLabel("Nom Employé:");
        labelNom.setBounds(20, 20, 100, 25);
        add(labelNom);

        nomField = new JTextField();
        nomField.setBounds(120, 20, 200, 25);
        add(nomField);

        JLabel labelDate = new JLabel("Date (YYYY-MM-DD HH:MM):");
        labelDate.setBounds(20, 50, 200, 25);
        add(labelDate);

        dateField = new JTextField();
        dateField.setBounds(220, 50, 150, 25);
        add(dateField);

        JLabel labelDuree = new JLabel("Durée (heures):");
        labelDuree.setBounds(20, 80, 100, 25);
        add(labelDuree);

        dureeField = new JTextField();
        dureeField.setBounds(120, 80, 200, 25);
        add(dureeField);

        JLabel labelSalle = new JLabel("Salle:");
        labelSalle.setBounds(20, 110, 100, 25);
        add(labelSalle);

        salleField = new JTextField();
        salleField.setBounds(120, 110, 200, 25);
        add(salleField);

        JLabel labelBatiment = new JLabel("Bâtiment:");
        labelBatiment.setBounds(20, 140, 100, 25);
        add(labelBatiment);

        batimentComboBox = new JComboBox<>(new String[]{"1 - Batiment A", "2 - Batiment B", "3 - Batiment C"});
        batimentComboBox.setBounds(120, 140, 200, 25);
        add(batimentComboBox);

        ajouterBtn = new JButton("Ajouter");
        ajouterBtn.setBounds(20, 180, 100, 30);
        add(ajouterBtn);

        modifierBtn = new JButton("Modifier");
        modifierBtn.setBounds(130, 180, 100, 30);
        add(modifierBtn);

        supprimerBtn = new JButton("Supprimer");
        supprimerBtn.setBounds(240, 180, 100, 30);
        add(supprimerBtn);

        String[] columnNames = {"ID", "Nom Employé", "Date", "Durée", "Salle"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 230, 650, 200);
        add(scrollPane);

        ajouterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nom = nomField.getText().trim();
                    String dateText = dateField.getText().trim();
                    String dureeText = dureeField.getText().trim();
                    String salleText = salleField.getText().trim();
                    String batimentText = (String) batimentComboBox.getSelectedItem();

                    if (nom.isEmpty() || dateText.isEmpty() || dureeText.isEmpty() || salleText.isEmpty() || batimentText == null) {
                        throw new IllegalArgumentException("Tous les champs doivent être remplis.");
                    }

                    LocalDateTime date = LocalDateTime.parse(dateText.replace(" ", "T"));
                    double duree = Double.parseDouble(dureeText);
                    String[] batimentParts = batimentText.split(" - ");
                    Batiment batiment = new Batiment(batimentParts[1]);
                    Salle salle = new Salle(batiment, salleText);

                    if (duree <= 0) {
                        throw new IllegalArgumentException("La durée doit être supérieure à 0.");
                    }

                    Reservation reservation = new Reservation(nom, date, duree, salle);

                    if (gestionReservations.verifierConflitReservation(reservation)) {
                        throw new DejaReservéeException("La salle est deja reservee en ce moments.");
                    }

                    gestionReservations.addReservation(reservation);
                    rafraichirTable();
                    JOptionPane.showMessageDialog(null, "Réservation ajoutée !");
                } catch (DejaReservéeException ex) {
                    JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Erreur : Durée invalide.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage());
                }
            }
        });

        modifierBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1) {
                        throw new IllegalArgumentException("Veuillez sélectionner une réservation à modifier.");
                    }

                    String id = (String) tableModel.getValueAt(selectedRow, 0);
                    String nom = nomField.getText().trim();
                    String dateText = dateField.getText().trim();
                    String dureeText = dureeField.getText().trim();
                    String salleText = salleField.getText().trim();   //trim tegle3 white spaces
                    String batimentText = (String) batimentComboBox.getSelectedItem();

                    if (nom.isEmpty() || dateText.isEmpty() || dureeText.isEmpty() || salleText.isEmpty() || batimentText == null) {
                        throw new IllegalArgumentException("Tous les champs doivent être remplis.");
                    }

                    LocalDateTime date = LocalDateTime.parse(dateText.replace(" ", "T"));
                    double duree = Double.parseDouble(dureeText);
                    String[] batimentParts = batimentText.split(" - ");
                    Batiment batiment = new Batiment(batimentParts[0]);
                    Salle salle = new Salle(batiment, salleText);

                    if (duree <= 0) {
                        throw new IllegalArgumentException("La durée doit être supérieure à 0.");
                    }

                    gestionReservations.ModifierReservation(id, nom, date, duree, salle);
                    rafraichirTable();
                    JOptionPane.showMessageDialog(null, "Réservation modifiée !");
                } catch (ReservationNonTrouvéeException ex) {
                    JOptionPane.showMessageDialog(null, "Erreur : Réservation non trouvée.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Erreur : Durée invalide.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage());
                }
            }
        });

        supprimerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1) {
                        throw new IllegalArgumentException("Veuillez sélectionner une réservation à supprimer.");
                    }

                    String id = (String) tableModel.getValueAt(selectedRow, 0);
                    gestionReservations.removeReservation(id);
                    rafraichirTable();
                    JOptionPane.showMessageDialog(null, "Réservation supprimée !");
                } catch (ReservationNonTrouvéeException ex) {
                    JOptionPane.showMessageDialog(null, "Erreur : Réservation non trouvée.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erreur : " + ex.getMessage());
                }
            }
        });

        setVisible(true);
    }

    private void rafraichirTable() {
        tableModel.setRowCount(0);
        try {
            List<Reservation> reservations = gestionReservations.afficherReservations();
            for (Reservation r : reservations) {
                tableModel.addRow(new Object[]{
                        r.getIdResrvation(),
                        r.getNom_Employe(),
                        r.getDate().toString(),
                        r.getDurée(),
                        r.getSalle().getNom_Salle()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors du rafraîchissement de la table : " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new ReservationGUI();
    }
}
