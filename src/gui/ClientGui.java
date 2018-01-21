package gui;
import db.ArrayDbDatabaseNetwork;
import db.Database;
import db.DbNetwork;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGui {
    GuiCommand guiCommand = new GuiCommand();
    Database database = new Database();
    ArrayDbDatabaseNetwork arrayDbDatabaseNetwork = new ArrayDbDatabaseNetwork();

    public JPanel panelMain;
    public JTextField textFieldIpAddress;
    public JTextField textFieldHostname;
    public JTextField textFieldVlan;
    public JTextField textFieldBeschreibung;
    public JTextField textFieldPortSwitch;
    public JTextField textFieldMacAddress;
    public JButton buttonEintragEinfuegen;
    public JButton buttonEintragSpeichern;
    public JButton buttonEintragLoeschen;
    public JButton buttonEintragAbrufen;
    public JButton buttonAktualisierenListe;
    public JLabel labelIpAddress;
    public JLabel labelHostname;
    public JLabel labelVlan;
    public JLabel labelBeschreibung;
    public JLabel labelPortSwitch;
    public JLabel labelMacAddress;
    private JScrollPane scrollPaneTable;

    JTable t = new JTable(database.dbTableData()){
        public boolean isCellEditable(int x, int y){
            return false;
        }
    };

    public ClientGui(){

        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPaneTable.getViewport().add(t);
        //createTable();

        //Listeners
        buttonEintragEinfuegen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiCommand.addDataSet(textFieldIpAddress.getText(),textFieldHostname.getText(),textFieldVlan.getText(),textFieldPortSwitch.getText(),textFieldMacAddress.getText());
                JOptionPane.showMessageDialog(null, "Eintrag erfolgreich dazugefügt!");
                clearTextfields();
                createTable();
            }
        });
        buttonEintragAbrufen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int iSelectedRow = t.getSelectedRow();
                if (iSelectedRow < 0){
                    JOptionPane.showMessageDialog(null, "Bitte Zeile wählen!");
                } else{
                    DbNetwork dbData = database.dbReadEntry(iSelectedRow);
                    textFieldIpAddress.setText(dbData.getIpAddress());
                    textFieldHostname.setText(dbData.getHostname());
                    textFieldVlan.setText(Integer.toString(dbData.getVlan()));
                    textFieldPortSwitch.setText(Integer.toString(dbData.getPortSwitch()));
                    textFieldMacAddress.setText(dbData.getMacAddress());
                }
            }
        });
        buttonEintragSpeichern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int iSelectedRow = t.getSelectedRow();
                guiCommand.updateDataSet(iSelectedRow,textFieldIpAddress.getText(),textFieldHostname.getText(),textFieldVlan.getText(),textFieldPortSwitch.getText(),textFieldMacAddress.getText());
                clearTextfields();
                createTable();
                JOptionPane.showMessageDialog(null, "Eintrag erfolgreich geändert!");
            }
        });
        buttonEintragLoeschen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int iSelectedRow = t.getSelectedRow();
                database.dbDeleteIndex(iSelectedRow);
                database.dbUpdatePk();
                createTable();
                JOptionPane.showMessageDialog(null, "Eintrag erfolgreich gelöscht!");
            }
        });
    }
    public void createTable(){
        /*JTable t = new JTable(database.dbTableData()){
            public boolean isCellEditable(int x, int y){
                return false;
            }
        };*/
        t.setModel(database.dbTableData());
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPaneTable.getViewport().add(t);
    }
    private void clearTextfields(){
        textFieldIpAddress.setText("");
        textFieldHostname.setText("");
        textFieldVlan.setText("");
        textFieldPortSwitch.setText("");
        textFieldMacAddress.setText("");
    }
}
