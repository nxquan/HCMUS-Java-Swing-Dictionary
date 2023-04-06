package main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import Model.Word;
import Model.StatisticWord;
import org.xml.sax.SAXException;


public class MainScreen extends javax.swing.JFrame {
    private final File englishVietFile = new File("./src/main/java/data/Anh_Viet.xml");
    private final File vietEnglishFile = new File("./src/main/java/data/Viet_Anh.xml");
    public static File historyFile = new File("./src/main/java/data/searchHistory.txt");
    public static File favoriteFile = new File("./src/main/java/data/favoriteWords.txt");
    boolean isEnglish = true;
    private boolean isChangingEnglishDictionary = false;
    private boolean isChangingVietDictionary = false;

    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;

    private List<Word> dictionaryEnglishViet;
    private List<Word> dictionaryVietAnh;
    private List<Word> favoriteWordList;
    private List<StatisticWord> historyList;

    private DefaultListModel<String> listModel;
    private JPopupMenu popupMenu;
    private int row;

    public MainScreen() throws ClassNotFoundException {
        initComponents();

        init();

        loadDictionary(englishVietFile, dictionaryEnglishViet);
        loadDictionary(vietEnglishFile, dictionaryVietAnh);
        assignData(dictionaryEnglishViet);

        readHistory();
        readFavorite();
    }

    private void init() {
        //initialize com
        titleLabel.setIcon(new ImageIcon("./src/main/java/main/logo.png"));
        dictionaryEnglishViet = new ArrayList<>();
        dictionaryVietAnh = new ArrayList<>();
        favoriteWordList = new ArrayList<>();
        historyList = new ArrayList<>();
        popupMenu = new JPopupMenu();

        listModel = new DefaultListModel<>();
        wordList.setModel(listModel);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2 - 30;
        this.setLocation(x, y);
        this.setTitle("Main Screen");
        addPopupMenu();
    }

    private void assignData(List<Word> list) {
        listModel.removeAllElements();

        for (Word item : list) {
            listModel.addElement(item.getWord());
        }
    }

    private void addPopupMenu() {
        //Create Items
        JMenuItem delete = new JMenuItem("Delete");
        JMenuItem addFovarite = new JMenuItem("Add favorite list");

        //Adding popup
        popupMenu.add(delete);
        popupMenu.add(addFovarite);
        //Handle events
        delete.addActionListener((ActionEvent e) -> {
            int opt = JOptionPane.showConfirmDialog(this,
                    "Are you sure to delete this word?",
                    "Asking?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (opt == JOptionPane.YES_OPTION) {
                listModel.remove(row);
                if (isEnglish) {
                    dictionaryEnglishViet.remove(row);
                    isChangingEnglishDictionary = true;
                } else {
                    dictionaryVietAnh.remove(row);
                    isChangingVietDictionary = true;
                }
                setTitle("Main Screen*");
            }
        });

        addFovarite.addActionListener((ActionEvent e) -> {
            Word word;
            if (isEnglish) {
                word = dictionaryEnglishViet.get(row);
            } else {
                word = dictionaryVietAnh.get(row);
            }
            favoriteWordList.add(word);
            setTitle("Main Screen*");
        });

    }

    private void loadDictionary(File rawFile, List<Word> list) {
        try {
            dbFactory = DocumentBuilderFactory.newDefaultInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            try {
                Document doc = dBuilder.parse(rawFile);
                doc.getDocumentElement().normalize();
                NodeList nodeListStudent = doc.getElementsByTagName("record");
                for (int i = 0; i < nodeListStudent.getLength(); i++) {
                    Node currentNode = nodeListStudent.item(i);
                    if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element currentElement = (Element) currentNode;
                        list.add(new Word(currentElement.getElementsByTagName("word")
                                .item(0).getTextContent(),
                                currentElement.getElementsByTagName("meaning")
                                        .item(0).getTextContent()));
                    }
                }
            } catch (IOException | SAXException e) {
                System.out.println(e);
            }

        } catch (ParserConfigurationException e) {
            System.out.println(e);
        }
    }

    private void writeDictionary(File file, List<Word> list) {
        Document doc = dBuilder.newDocument();
        Element rootElement = doc.createElement("dictionary");

        for (Word item : list) {
            Element record = doc.createElement("record");

            Element word = doc.createElement("word");
            word.appendChild(doc.createTextNode(item.getWord()));
            record.appendChild(word);

            Element meaning = doc.createElement("meaning");
            meaning.appendChild(doc.createTextNode(item.getMeaning()));
            record.appendChild(meaning);

            rootElement.appendChild(record);
        }
        doc.appendChild(rootElement);

        // ghi nội dung vào file XML
        TransformerFactory transformerFactory
                = TransformerFactory.newInstance();

        try {
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            System.out.println(ex);
        }

    }

    private void readHistory() throws ClassNotFoundException {
        try {
            FileInputStream in = new FileInputStream(historyFile);
            ObjectInputStream ifs = new ObjectInputStream(in);
            while (true) {
                StatisticWord obj;
                try {
                    obj = (StatisticWord) ifs.readObject();
                    historyList.add(obj);
                } catch (EOFException ex) {
                    break;
                }
            }

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private void readFavorite() throws ClassNotFoundException {
        try {
            FileInputStream in = new FileInputStream(favoriteFile);
            ObjectInputStream ifs = new ObjectInputStream(in);
            while (true) {
                Word obj;
                try {
                    obj = (Word) ifs.readObject();
                    favoriteWordList.add(obj);
                } catch (EOFException ex) {
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public static <T> void writeList(List<T> list, File file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            ObjectOutputStream ofs = new ObjectOutputStream(out);
            for (T item : list) {
                ofs.writeObject(item);
            }
            ofs.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        typeDictionaryCombobox = new javax.swing.JComboBox<>();
        searchInput = new javax.swing.JTextField();
        wordListScrollPane = new javax.swing.JScrollPane();
        wordList = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        meaningTextPane = new javax.swing.JTextPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        wordInput = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        meaningInput = new javax.swing.JTextField();
        createBtn = new javax.swing.JButton();
        openForariteListBtn = new javax.swing.JButton();
        openStatisticBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(1000, 550));

        mainPanel.setBackground(new java.awt.Color(203, 253, 250));

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Dictionary System");

        jPanel2.setBackground(new java.awt.Color(203, 253, 250));
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(480, 30));

        typeDictionaryCombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Anh - Viet", "Viet - Anh" }));
        typeDictionaryCombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeDictionaryComboboxActionPerformed(evt);
            }
        });

        searchInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchInputKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(searchInput, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addComponent(typeDictionaryCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(typeDictionaryCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(searchInput)
                .addContainerGap())
        );

        wordListScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        wordList.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        wordList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
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
        wordListScrollPane.setViewportView(wordList);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Words:");
        jLabel2.setPreferredSize(new java.awt.Dimension(43, 30));

        meaningTextPane.setContentType("text/html"); // NOI18N
        meaningTextPane.setFocusable(false);
        jScrollPane4.setViewportView(meaningTextPane);

        jPanel3.setBackground(new java.awt.Color(203, 253, 250));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel3.setText("Create new words");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Words:");
        jLabel4.setPreferredSize(new java.awt.Dimension(43, 30));

        wordInput.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        wordInput.setToolTipText("Input keywords");
        wordInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wordInputActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Meanings:");
        jLabel5.setPreferredSize(new java.awt.Dimension(43, 30));

        meaningInput.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        meaningInput.setToolTipText("Input keywords");
        meaningInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                meaningInputActionPerformed(evt);
            }
        });

        createBtn.setBackground(new java.awt.Color(250, 201, 201));
        createBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        createBtn.setText("Create");
        createBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        createBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createBtnActionPerformed(evt);
            }
        });

        openForariteListBtn.setBackground(new java.awt.Color(250, 201, 201));
        openForariteListBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        openForariteListBtn.setText("My favorite words");
        openForariteListBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        openForariteListBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openForariteListBtnActionPerformed(evt);
            }
        });

        openStatisticBtn.setBackground(new java.awt.Color(250, 201, 201));
        openStatisticBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        openStatisticBtn.setText("Statistics");
        openStatisticBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        openStatisticBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openStatisticBtnActionPerformed(evt);
            }
        });

        saveBtn.setBackground(new java.awt.Color(250, 201, 201));
        saveBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        saveBtn.setText("Save*");
        saveBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(meaningInput)
                    .addComponent(wordInput)))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addContainerGap(330, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(createBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(openForariteListBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(openStatisticBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(wordInput, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(meaningInput, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(createBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(openForariteListBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openStatisticBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(wordListScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(wordListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void wordListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_wordListValueChanged
        if (!wordList.isSelectionEmpty() && !evt.getValueIsAdjusting()) {
            int selectedIndex = wordList.getSelectedIndex();
            if (isEnglish) {
                Word word = dictionaryEnglishViet.get(selectedIndex);
                meaningTextPane.setText(word.parse());
                row = selectedIndex;
                historyList.add(new StatisticWord(word, LocalDate.now()));
            } else {
                Word word = dictionaryVietAnh.get(selectedIndex);
                meaningTextPane.setText(word.getMeaning());
                historyList.add(new StatisticWord(word, LocalDate.now()));
            }
            row = selectedIndex;
            JScrollBar sb = wordListScrollPane.getVerticalScrollBar();
        }
    }//GEN-LAST:event_wordListValueChanged

    private void openStatisticBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openStatisticBtnActionPerformed
        // TODO add your handling code here:

        StatisticScreen statisticScreen = new StatisticScreen();
        statisticScreen.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - statisticScreen.getWidth()) / 2;
        int y = (screenSize.height - statisticScreen.getHeight()) / 2;

        statisticScreen.assignData(historyList);

        statisticScreen.setLocation(x, y);
        statisticScreen.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Handle the closing of this frame
                statisticScreen.dispose(); // Close the frame
            }
        });

        statisticScreen.show();
    }//GEN-LAST:event_openStatisticBtnActionPerformed

    private void wordListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_wordListMouseClicked
        // TODO add your handling code here:
        //Checking if right click is active
        if (SwingUtilities.isRightMouseButton(evt)) {
            if (!wordList.isSelectionEmpty()) {
                popupMenu.show(wordList, evt.getX(), evt.getY());
            }
        }

    }//GEN-LAST:event_wordListMouseClicked

    private void searchInputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchInputKeyTyped
        // TODO add your handling code here:
        String keyword = searchInput.getText();

        keyword += evt.getKeyChar();
        keyword = keyword.trim().toLowerCase();
        meaningTextPane.setText("");

        if (isEnglish) {
            for (int i = 0; i < listModel.size(); i++) {
                if (listModel.getElementAt(i).toLowerCase().startsWith(keyword)) {
                    JScrollBar sb = wordListScrollPane.getVerticalScrollBar();
                    sb.setValue(i * 24);
                    return;
                }
            }
        } else {
            for (int i = 0; i < listModel.size(); i++) {
                String curString = listModel.getElementAt(i);
                if (Helper.unicodeToASCII(curString).toLowerCase().startsWith(
                        Helper.unicodeToASCII(keyword).toLowerCase()
                )) {
                    JScrollBar sb = wordListScrollPane.getVerticalScrollBar();
                    sb.setValue(i * 24);
                    return;
                }
            }
        }
        wordList.clearSelection();
    }//GEN-LAST:event_searchInputKeyTyped

    private void typeDictionaryComboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeDictionaryComboboxActionPerformed
        // TODO add your handling code here:
        String language = (String) typeDictionaryCombobox.getSelectedItem();
        switch (language) {
            case "Anh - Viet" -> {
                isEnglish = true;
                assignData(dictionaryEnglishViet);
            }

            case "Viet - Anh" -> {
                isEnglish = false;
                assignData(dictionaryVietAnh);
            }
            default -> {
            }
        }
    }//GEN-LAST:event_typeDictionaryComboboxActionPerformed

    private void openForariteListBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openForariteListBtnActionPerformed
        // TODO add your handling code here:
        FavoriteWordScreen favoriteWordScreen = new FavoriteWordScreen();

        favoriteWordScreen.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - favoriteWordScreen.getWidth()) / 2;
        int y = (screenSize.height - favoriteWordScreen.getHeight()) / 2;
        favoriteWordScreen.setLocation(x, y);
        favoriteWordScreen.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                favoriteWordScreen.dispose();
            }
        });

        favoriteWordScreen.assignAndRenderData(favoriteWordList);
        favoriteWordScreen.show();
    }//GEN-LAST:event_openForariteListBtnActionPerformed

    private void createBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createBtnActionPerformed
        // TODO add your handling code here:
        String wordText = wordInput.getText();
        String meaningText = meaningInput.getText();

        if (wordText.length() > 0 && meaningText.length() > 0) {
            Word word = new Word(wordText, meaningText);

            if (isEnglish) {
                dictionaryEnglishViet.add(word);
                isChangingEnglishDictionary = true;
            } else {
                dictionaryVietAnh.add(word);
                isChangingVietDictionary = true;
            }
            listModel.addElement(word.getWord());
            JOptionPane.showMessageDialog(this, "Addding sucessfully!", "Info",
                    JOptionPane.INFORMATION_MESSAGE);

            wordInput.setText("");
            meaningInput.setText("");
            setTitle("Main Screen*");
        } else {
            JOptionPane.showMessageDialog(this, "Word or meaning are empty!", "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_createBtnActionPerformed

    private void meaningInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_meaningInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_meaningInputActionPerformed

    private void wordInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wordInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_wordInputActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        //Save dictionary
        if (isChangingEnglishDictionary) {
            writeDictionary(englishVietFile, dictionaryEnglishViet);
        }

        if (isChangingVietDictionary) {
            writeDictionary(vietEnglishFile, dictionaryVietAnh);
        }

        //Save favorite word list
        writeList(favoriteWordList, favoriteFile);
        //Save history
        writeList(historyList, historyFile);
        setTitle("Main Sreen");
    }//GEN-LAST:event_saveBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new MainScreen().setVisible(true);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton createBtn;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField meaningInput;
    private javax.swing.JTextPane meaningTextPane;
    private javax.swing.JButton openForariteListBtn;
    private javax.swing.JButton openStatisticBtn;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField searchInput;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JComboBox<String> typeDictionaryCombobox;
    private javax.swing.JTextField wordInput;
    private javax.swing.JList<String> wordList;
    private javax.swing.JScrollPane wordListScrollPane;
    // End of variables declaration//GEN-END:variables
}
