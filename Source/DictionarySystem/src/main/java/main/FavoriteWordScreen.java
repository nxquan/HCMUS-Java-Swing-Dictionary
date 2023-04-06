
package main;

import Model.Word;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;


public class FavoriteWordScreen extends javax.swing.JFrame {
    private List<Word> favoriteWordList;
    private DefaultListModel<String> listModel;
    private JPopupMenu popupMenu;
    private int row;

    public FavoriteWordScreen() {
        initComponents();
        init();
    }

    private void init() {
        listModel = new DefaultListModel<>();
        popupMenu = new JPopupMenu();

        wordList.setModel(listModel);
        wordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addPopupMenu();
    }

    public void assignAndRenderData(List<Word> list) {
        this.favoriteWordList = list;
        renderData(list);
    }

    private void renderData(List<Word> list) {
        listModel.removeAllElements();
        for (Word word : list) {
            listModel.addElement(word.getWord());
        }
    }

    private void addPopupMenu() {
        JMenuItem remove = new JMenuItem("remove");

        remove.addActionListener((ActionEvent e) -> {
            int opt = JOptionPane.showConfirmDialog(this,
                    "Are you sure to delete this word?",
                    "Asking?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (opt == JOptionPane.YES_OPTION) {
                listModel.remove(row);
                favoriteWordList.remove(row);
                setTitle("Screen*");
            }
        });

        popupMenu.add(remove);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        favoritePanel = new javax.swing.JPanel();
        titleLabel3 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        meaningTextPane = new javax.swing.JTextPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        wordList = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        sortingComboBox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        favoritePanel.setBackground(new java.awt.Color(203, 253, 250));

        titleLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        titleLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel3.setText("Fovarite word list");

        meaningTextPane.setEditable(false);
        meaningTextPane.setContentType("text/html"); // NOI18N
        meaningTextPane.setToolTipText("");
        meaningTextPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        meaningTextPane.setFocusable(false);
        meaningTextPane.setMaximumSize(new java.awt.Dimension(349, 348));
        jScrollPane5.setViewportView(meaningTextPane);

        wordList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        wordList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        wordList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                wordListMouseClicked(evt);
            }
        });
        wordList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                wordListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(wordList);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Words:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Meaning:");

        sortingComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A - Z", "Z - A" }));
        sortingComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortingComboBoxActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Sort by:");

        javax.swing.GroupLayout favoritePanelLayout = new javax.swing.GroupLayout(favoritePanel);
        favoritePanel.setLayout(favoritePanelLayout);
        favoritePanelLayout.setHorizontalGroup(
            favoritePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(titleLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(favoritePanelLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(favoritePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(favoritePanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sortingComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(favoritePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(favoritePanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))
                    .addGroup(favoritePanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addContainerGap())))
        );
        favoritePanelLayout.setVerticalGroup(
            favoritePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(favoritePanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(titleLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(favoritePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(favoritePanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(favoritePanelLayout.createSequentialGroup()
                        .addGroup(favoritePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sortingComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)))
                .addGroup(favoritePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                    .addComponent(jScrollPane5))
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 781, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(favoritePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 437, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(favoritePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void wordListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_wordListValueChanged
        if (!wordList.isSelectionEmpty()) {
            int selectedIndex = wordList.getSelectedIndex();
            Word selectedWord = favoriteWordList.get(selectedIndex);
            meaningTextPane.setText(selectedWord.parse());
            row = selectedIndex;
        }
    }//GEN-LAST:event_wordListValueChanged

    private void sortingComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortingComboBoxActionPerformed
        String option = (String) sortingComboBox.getSelectedItem();
        meaningTextPane.setText("");
        switch (option) {
            case "A - Z" ->
                Collections.sort(favoriteWordList, (Word o1, Word o2) -> o1.getWord().compareTo(o2.getWord()));
            case "Z - A" ->
                Collections.sort(favoriteWordList, (Word o1, Word o2) -> o2.getWord().compareTo(o1.getWord()));
            default -> {
            }
        }

        renderData(favoriteWordList);
    }//GEN-LAST:event_sortingComboBoxActionPerformed

    private void wordListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_wordListMouseClicked
        // TODO add your handling code here:
        if (!wordList.isSelectionEmpty()) {
            if (SwingUtilities.isRightMouseButton(evt)) {
                popupMenu.show(wordList, evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_wordListMouseClicked

    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new FavoriteWordScreen().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel favoritePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextPane meaningTextPane;
    private javax.swing.JComboBox<String> sortingComboBox;
    private javax.swing.JLabel titleLabel3;
    private javax.swing.JList<String> wordList;
    // End of variables declaration//GEN-END:variables
}
