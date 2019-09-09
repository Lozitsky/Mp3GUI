package gui;

import javax.swing.event.*;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import objects.MP3;
import objects.MP3Player;
import utils.FileUtils;
import utils.MP3PlayerFileFilter;
import utils.SkinUtils;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;
/*
 * Created by JFormDesigner on Fri Aug 30 21:38:55 EEST 2019
 */



/**
 * @author Kirilo
 */
public class MP3PlayerGUI extends JFrame implements BasicPlayerListener {
    private static final String MP3_FILE_DESCRIPTION = "mp3 files";
    private static final String MP3_FILE_EXTENSION = "mp3";
    private static final String PLAYLIST_FILE_EXTENSION = "pls";
    private static final String PLAYLIST_FILE_DESCRIPTION = "playlist files";
    private static final String INPUT_SONG_NAME = "input song name";
    private static Logger logger = Logger.getLogger(MP3PlayerGUI.class.getName());
    private DefaultListModel mp3ListModel = new DefaultListModel();
    private FileFilter mp3FileFilter = new MP3PlayerFileFilter(MP3_FILE_EXTENSION, MP3_FILE_DESCRIPTION);
    private FileFilter playlistFileFilter = new MP3PlayerFileFilter(PLAYLIST_FILE_EXTENSION, PLAYLIST_FILE_DESCRIPTION);
    private MP3Player player = new MP3Player(this);
    private long duration;
    private int bytesLen;
    private long secondsAmount;
    private boolean movingFromJump;
    private boolean moveAutomatic;
    private double posValue;

    public MP3PlayerGUI() {
        initComponents();
    }


    @Override
    public void opened(Object o, Map map) {
        duration = Math.round(((Long) map.get("duration")).longValue() / 1000000);
        bytesLen = Math.round(((Integer) map.get("mp3.length.bytes")).intValue());

        Object title = map.get("title");
        String stringTitle;
        String songName = title != null && (stringTitle = title.toString().trim()).length() > 0 ? stringTitle : FileUtils.getFileNameWithoutExtension(new File(o.toString()).getName());
        if (songName.length() > 30) {
            songName = songName.substring(0, 30) + "...";
        }
        labelSongName.setText(songName);
    }

    @Override
    public void progress(int bytesRead, long microseconds, byte[] bytes, Map map) {
        float progress = -1.0f;
        if ((bytesRead > 0) && (duration > 0)) {
            progress = (float) bytesRead / bytesLen;
        }
        secondsAmount = (long) (duration * progress);
        if (duration != 0) {
            if (!movingFromJump) {
                sliderProgress.setValue(Math.round(secondsAmount * 1000 / duration));
            }
        }
    }

    @Override
    public void stateUpdated(BasicPlayerEvent basicPlayerEvent) {
        int code = basicPlayerEvent.getCode();
        switch (code) {
            case BasicPlayerEvent.PLAYING:
                movingFromJump = false;
                break;
            case BasicPlayerEvent.SEEKING:
                movingFromJump = true;
                break;
            case BasicPlayerEvent.EOM:
                if (selectNextSong()) {
                    playFile();
                }
        }
    }

    @Override
    public void setController(BasicController basicController) {

    }

    private void menuSkin1ActionPerformed(ActionEvent e) {
        SkinUtils.changeSkin(this, UIManager.getSystemLookAndFeelClassName());
    }

    private void menuSkin2ActionPerformed(ActionEvent e) {
        SkinUtils.changeSkin(this, new NimbusLookAndFeel());
    }

    private void buttonAddSongActionPerformed(ActionEvent e) {
        FileUtils.addFileFilter(fileChooser, mp3FileFilter);
        int dialog = fileChooser.showOpenDialog(this);
        if (dialog == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            for (File selectedFile : selectedFiles) {
                MP3 mp3 = new MP3(selectedFile.getName(), selectedFile.getPath());
                mp3ListModel.addElement(mp3);
                // don't add if list contains this song
                /*if (!mp3ListModel.contains(mp3)) {
                    mp3ListModel.addElement(mp3);
                }*/
            }
            listPlayList.setModel(mp3ListModel);
        }
    }

    private void buttonDeleteSongActionPerformed(ActionEvent e) {
        int[] selectedIndices = listPlayList.getSelectedIndices();
        if (selectedIndices.length > 0) {
            List<MP3> mp3ListForRemove = new ArrayList<>();
            for (int selectedIndex : selectedIndices) {
                Object elementAt = mp3ListModel.getElementAt(selectedIndex);
                if (elementAt instanceof MP3) {
                    mp3ListForRemove.add((MP3) elementAt);
                }
            }
            mp3ListForRemove.forEach(mp3 -> mp3ListModel.removeElement(mp3));
        }
    }

    private void buttonSelectPrevActionPerformed(ActionEvent e) {
        selectPrevSong();
    }

    private boolean selectPrevSong() {
        int prevIndex = listPlayList.getSelectedIndex() - 1;
        logger.log(Level.SEVERE, "PrevINDEX: " + prevIndex);
        boolean isPrevIndex = prevIndex >= 0;
        if (isPrevIndex) {
            listPlayList.setSelectedIndex(prevIndex);
        }
        return isPrevIndex;
    }

    private void buttonSelectNextActionPerformed(ActionEvent e) {
        selectNextSong();
    }

    private boolean selectNextSong() {
        int nextIndex = listPlayList.getSelectedIndex() + 1;
        logger.log(Level.SEVERE, "NextINDEX: " + nextIndex);
        boolean isNextIndex = nextIndex < listPlayList.getModel().getSize();
        if (isNextIndex) {
            listPlayList.setSelectedIndex(nextIndex);
        }
        return isNextIndex;
    }

    private void buttonPlaySongActionPerformed(ActionEvent e) {
        playFile();
    }

    private void playFile() {
        int[] selectedIndices = listPlayList.getSelectedIndices();
        if (selectedIndices.length > 0) {
            Object mp3 = mp3ListModel.getElementAt(selectedIndices[0]);
            if (mp3 instanceof MP3) {
//                System.out.println(((MP3) mp3).getPath());
                player.play(((MP3) mp3).getPath());
                player.setVolume(sliderVolume.getValue(), sliderVolume.getMaximum());
            }
        }
    }

    private void menuSavePlaylistActionPerformed(ActionEvent e) {
        FileUtils.addFileFilter(fileChooser, playlistFileFilter);
        int resultSaveDialog = fileChooser.showSaveDialog(this);
        if (resultSaveDialog == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.exists()) {
                int resultConfirmDialog = JOptionPane.showConfirmDialog(this, "File exists", "Rewrite?", JOptionPane.YES_NO_CANCEL_OPTION);
                switch (resultConfirmDialog) {
                    case JOptionPane.NO_OPTION:
                        menuSavePlaylistActionPerformed(e);
                        return;
                    case JOptionPane.CANCEL_OPTION:
                        fileChooser.cancelSelection();
                        return;
                }
                fileChooser.approveSelection();
            }
            String fileExtension = FileUtils.getFileExtension(selectedFile);

            String fileNameForSave = fileExtension != null && fileExtension.equals(PLAYLIST_FILE_EXTENSION) ? selectedFile.getPath() : selectedFile.getPath() + "." + PLAYLIST_FILE_EXTENSION;
            FileUtils.serialize(mp3ListModel, fileNameForSave);
        }
    }

    private void menuOpenPlaylistActionPerformed(ActionEvent e) {
        FileUtils.addFileFilter(fileChooser, playlistFileFilter);
        int resultOpenDialog = fileChooser.showOpenDialog(this);
        if (resultOpenDialog == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            Object deSerialize = FileUtils.deSerialize(selectedFile.getPath());
            if (deSerialize instanceof DefaultListModel) {
                mp3ListModel = (DefaultListModel) deSerialize;
                listPlayList.setModel(mp3ListModel);
            }
        }
    }

    private void buttonSearchActionPerformed(ActionEvent e) {
        searchSong();
    }

    private void searchSong() {
        String searchText = textSearch.getText();
        if (searchText == null || searchText.trim().equals(EMPTY_STRING)) {
            return;
        }
        List<Integer> mp3IndexesFound = new ArrayList<>();
        for (int i = 0; i < mp3ListModel.size(); i++) {
            Object element = mp3ListModel.getElementAt(i);
            if (element instanceof MP3) {
                if (((MP3) element).getName().toLowerCase().contains(searchText.toLowerCase())) {
                    mp3IndexesFound.add(i);
                }
            }
        }
        if (mp3IndexesFound.size() == 0) {
            JOptionPane.showMessageDialog(this, "a searching in the string \'" + searchText + "\' failed");
            textSearch.requestFocus();
            textSearch.selectAll();
            return;
        }
//        int[] ints = mp3IndexesFound.stream().mapToInt(value -> value).toArray();
        int[] indexes = mp3IndexesFound.stream().mapToInt(Integer::intValue).toArray();
        listPlayList.setSelectedIndices(indexes);
    }

    private void textSearchFocusGained(FocusEvent e) {
        if (textSearch.getText().equals(INPUT_SONG_NAME)) {
            textSearch.setText(EMPTY_STRING);
        }
    }

    private void textSearchFocusLost(FocusEvent e) {
        if (textSearch.getText().trim().equals(EMPTY_STRING)) {
            textSearch.setText(INPUT_SONG_NAME);
        }
    }

    private void buttonStopSongActionPerformed(ActionEvent e) {
        player.stop();
    }

    private void buttonPauseSongActionPerformed(ActionEvent e) {
        player.pause();
    }

    private void sliderVolumeStateChanged(ChangeEvent e) {
        player.setVolume(sliderVolume.getValue(), sliderVolume.getMaximum());
        if (sliderVolume.getValue() == 0) {
            toggleButton.setSelected(true);
        } else {
            toggleButton.setSelected(false);
        }
    }

    private void listPlayListMouseClicked(MouseEvent e) {
        if (e.getModifiers() == InputEvent.BUTTON1_MASK && e.getClickCount() == 2) {
            playFile();
        }
    }

    private int currentVolumeValue;

    private void toggleButtonActionPerformed(ActionEvent e) {
        if (toggleButton.isSelected()) {
            currentVolumeValue = sliderVolume.getValue();
            sliderVolume.setValue(0);
        } else {
            sliderVolume.setValue(currentVolumeValue);
        }
    }

    private void buttonNextSongActionPerformed(ActionEvent e) {
        /*buttonSelectNextActionPerformed(e);
        buttonPlaySongActionPerformed(e);*/
        if (selectNextSong()) {
            playFile();
        }
    }

    private void buttonPrevSongActionPerformed(ActionEvent e) {
        /*buttonSelectPrevActionPerformed(e);
        buttonPlaySongActionPerformed(e);*/
        if (selectPrevSong()) {
            playFile();
        }
    }

    private void sliderProgressStateChanged(ChangeEvent e) {
        if (!sliderProgress.getValueIsAdjusting()) {
            if (moveAutomatic) {
                moveAutomatic = false;
                posValue = (float) sliderProgress.getValue() / 1000;
                processSeek(posValue);
            }
        } else {
                moveAutomatic = true;
                movingFromJump = true;
        }

    }

    private void processSeek(double bytes) {
        try {
            long skipBytes = Math.round(((Integer) bytesLen).intValue() * bytes);
            player.jump(skipBytes);
        } catch (Exception e) {
            e.printStackTrace();
            movingFromJump = false;
        }
    }

    private void textSearchKeyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER) {
            searchSong();
        }
    }

    private void popmenuAddSongActionPerformed(ActionEvent e) {
        buttonAddSongActionPerformed(e);
    }

    private void popmenuDeleteSongActionPerformed(ActionEvent e) {
        buttonDeleteSongActionPerformed(e);
    }

    private void popmenuOpenPlaylistActionPerformed(ActionEvent e) {
        menuOpenPlaylistActionPerformed(e);
    }

    private void popMenuClearPlaylistActionPerformed(ActionEvent e) {
        buttonDeleteSongActionPerformed(e);
    }

    private void popmenuPlayActionPerformed(ActionEvent e) {
        buttonPlaySongActionPerformed(e);
    }

    private void popmenuStopActionPerformed(ActionEvent e) {
        buttonStopSongActionPerformed(e);
    }

    private void popmenuPauseActionPerformed(ActionEvent e) {
        buttonPauseSongActionPerformed(e);
    }

    private void listPlayListKeyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER) {
            playFile();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        menuBar1 = new JMenuBar();
        menuFile = new JMenu();
        menuOpenPlaylist = new JMenuItem();
        menuSavePlaylist = new JMenuItem();
        menuExit = new JMenuItem();
        menuPrefs = new JMenu();
        menuChangeSkin = new JMenu();
        menuSkin1 = new JMenuItem();
        menuSkin2 = new JMenuItem();
        panelSearch = new JPanel();
        textSearch = new JTextField();
        buttonSearch = new JButton();
        panelMain = new JPanel();
        scrollPane1 = new JScrollPane();
        listPlayList = new JList();
        buttonAddSong = new JButton();
        buttonDeleteSong = new JButton();
        buttonSelectNext = new JButton();
        buttonSelectPrev = new JButton();
        separator1 = new JSeparator();
        toggleButton = new JToggleButton();
        sliderVolume = new JSlider();
        buttonPrevSong = new JButton();
        buttonPlaySong = new JButton();
        buttonPauseSong = new JButton();
        buttonStopSong = new JButton();
        buttonNextSong = new JButton();
        labelSongName = new JLabel();
        sliderProgress = new JSlider();
        fileChooser = new JFileChooser();
        popupPlaylist = new JPopupMenu();
        popmenuAddSong = new JMenuItem();
        popmenuDeleteSong = new JMenuItem();
        popmenuOpenPlaylist = new JMenuItem();
        popMenuClearPlaylist = new JMenuItem();
        popmenuPlay = new JMenuItem();
        popmenuStop = new JMenuItem();
        popmenuPause = new JMenuItem();

        //======== this ========
        setTitle("MP3 player");
        setBackground(new Color(240, 240, 240));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setMinimumSize(new Dimension(6, 28));
        Container contentPane = getContentPane();

        //======== menuBar1 ========
        {
            menuBar1.setPreferredSize(new Dimension(94, 22));
            menuBar1.setMaximumSize(new Dimension(94, 32));
            menuBar1.setMinimumSize(new Dimension(94, 22));

            //======== menuFile ========
            {
                menuFile.setText("File");
                menuFile.setPreferredSize(new Dimension(41, 21));

                //---- menuOpenPlaylist ----
                menuOpenPlaylist.setText("Open playlist");
                menuOpenPlaylist.setIcon(new ImageIcon(getClass().getResource("/images/open-icon.png")));
                menuOpenPlaylist.addActionListener(e -> menuOpenPlaylistActionPerformed(e));
                menuFile.add(menuOpenPlaylist);

                //---- menuSavePlaylist ----
                menuSavePlaylist.setText("Save playlist");
                menuSavePlaylist.setIcon(new ImageIcon(getClass().getResource("/images/save_16.png")));
                menuSavePlaylist.addActionListener(e -> menuSavePlaylistActionPerformed(e));
                menuFile.add(menuSavePlaylist);
                menuFile.addSeparator();

                //---- menuExit ----
                menuExit.setText("Exit");
                menuExit.setIcon(new ImageIcon(getClass().getResource("/images/exit.png")));
                menuFile.add(menuExit);
            }
            menuBar1.add(menuFile);

            //======== menuPrefs ========
            {
                menuPrefs.setText("Preferences");
                menuPrefs.setPreferredSize(new Dimension(78, 21));
                menuPrefs.setRolloverEnabled(true);

                //======== menuChangeSkin ========
                {
                    menuChangeSkin.setText("Appearance");
                    menuChangeSkin.setIcon(new ImageIcon(getClass().getResource("/images/gear_16.png")));

                    //---- menuSkin1 ----
                    menuSkin1.setText("Skin 1");
                    menuSkin1.addActionListener(e -> menuSkin1ActionPerformed(e));
                    menuChangeSkin.add(menuSkin1);

                    //---- menuSkin2 ----
                    menuSkin2.setText("Skin 2");
                    menuSkin2.addActionListener(e -> menuSkin2ActionPerformed(e));
                    menuChangeSkin.add(menuSkin2);
                }
                menuPrefs.add(menuChangeSkin);
            }
            menuBar1.add(menuPrefs);
        }
        setJMenuBar(menuBar1);

        //======== panelSearch ========
        {
            panelSearch.setBorder(new TitledBorder(""));
/*            panelSearch.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing.
            border. EmptyBorder( 0, 0, 0, 0) , "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e", javax. swing. border. TitledBorder. CENTER
            , javax. swing. border. TitledBorder. BOTTOM, new java .awt .Font ("Dialo\u0067" ,java .awt .Font
            .BOLD ,12 ), java. awt. Color. red) ,panelSearch. getBorder( )) ); panelSearch. addPropertyChangeListener (
            new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("borde\u0072"
            .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );*/

            //---- textSearch ----
            textSearch.setText("input name of song");
            textSearch.setFont(new Font("Tahoma", Font.ITALIC, 11));
            textSearch.setForeground(UIManager.getColor("Button.select"));
            textSearch.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    textSearchFocusGained(e);
                }
                @Override
                public void focusLost(FocusEvent e) {
                    textSearchFocusLost(e);
                }
            });
            textSearch.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    textSearchKeyPressed(e);
                }
            });

            //---- buttonSearch ----
            buttonSearch.setText("Find");
            buttonSearch.setIcon(new ImageIcon(getClass().getResource("/images/search_16.png")));
            buttonSearch.addActionListener(e -> buttonSearchActionPerformed(e));

            GroupLayout panelSearchLayout = new GroupLayout(panelSearch);
            panelSearch.setLayout(panelSearchLayout);
            panelSearchLayout.setHorizontalGroup(
                panelSearchLayout.createParallelGroup()
                    .addGroup(panelSearchLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(textSearch)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonSearch)
                        .addContainerGap())
            );
            panelSearchLayout.setVerticalGroup(
                panelSearchLayout.createParallelGroup()
                    .addGroup(panelSearchLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(panelSearchLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonSearch)
                            .addGroup(panelSearchLayout.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 1, Short.MAX_VALUE)
                                .addComponent(textSearch, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
            );
        }

        //======== panelMain ========
        {
            panelMain.setBorder(new TitledBorder(""));

            //======== scrollPane1 ========
            {

                //---- listPlayList ----
                listPlayList.setToolTipText("List of songs");
                listPlayList.setComponentPopupMenu(popupPlaylist);
                listPlayList.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        listPlayListMouseClicked(e);
                    }
                });
                listPlayList.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        listPlayListKeyPressed(e);
                    }
                });
                scrollPane1.setViewportView(listPlayList);
            }

            //---- buttonAddSong ----
            buttonAddSong.setIcon(new ImageIcon(getClass().getResource("/images/plus_16.png")));
            buttonAddSong.setHorizontalAlignment(SwingConstants.LEFT);
            buttonAddSong.setToolTipText("Add song");
            buttonAddSong.setHorizontalTextPosition(SwingConstants.LEFT);
            buttonAddSong.addActionListener(e -> buttonAddSongActionPerformed(e));

            //---- buttonDeleteSong ----
            buttonDeleteSong.setIcon(new ImageIcon(getClass().getResource("/images/remove_icon.png")));
            buttonDeleteSong.setHorizontalAlignment(SwingConstants.LEFT);
            buttonDeleteSong.setName("btnDeleteSong");
            buttonDeleteSong.setHorizontalTextPosition(SwingConstants.LEFT);
            buttonDeleteSong.setToolTipText("Delete song");
            buttonDeleteSong.addActionListener(e -> buttonDeleteSongActionPerformed(e));

            //---- buttonSelectNext ----
            buttonSelectNext.setIcon(new ImageIcon(getClass().getResource("/images/arrow-down-icon.png")));
            buttonSelectNext.setHorizontalAlignment(SwingConstants.LEFT);
            buttonSelectNext.setHorizontalTextPosition(SwingConstants.LEFT);
            buttonSelectNext.addActionListener(e -> buttonSelectNextActionPerformed(e));

            //---- buttonSelectPrev ----
            buttonSelectPrev.setIcon(new ImageIcon(getClass().getResource("/images/arrow-up-icon.png")));
            buttonSelectPrev.setHorizontalAlignment(SwingConstants.LEFT);
            buttonSelectPrev.setHorizontalTextPosition(SwingConstants.LEFT);
            buttonSelectPrev.addActionListener(e -> buttonSelectPrevActionPerformed(e));

            //---- separator1 ----
            separator1.setPreferredSize(new Dimension(3, 0));
            separator1.setOrientation(SwingConstants.VERTICAL);

            //---- toggleButton ----
            toggleButton.setSelectedIcon(new ImageIcon(getClass().getResource("/images/mute.png")));
            toggleButton.setIcon(new ImageIcon(getClass().getResource("/images/speaker.png")));
            toggleButton.setToolTipText("Off sound");
            toggleButton.addActionListener(e -> toggleButtonActionPerformed(e));

            //---- sliderVolume ----
            sliderVolume.setValue(100);
            sliderVolume.setMaximum(200);
            sliderVolume.setMinorTickSpacing(5);
            sliderVolume.setSnapToTicks(true);
            sliderVolume.setToolTipText("Change volume");
            sliderVolume.addChangeListener(e -> sliderVolumeStateChanged(e));

            //---- buttonPrevSong ----
            buttonPrevSong.setIcon(new ImageIcon(getClass().getResource("/images/prev-icon.png")));
            buttonPrevSong.addActionListener(e -> buttonPrevSongActionPerformed(e));

            //---- buttonPlaySong ----
            buttonPlaySong.setIcon(new ImageIcon(getClass().getResource("/images/Play.png")));
            buttonPlaySong.addActionListener(e -> buttonPlaySongActionPerformed(e));

            //---- buttonPauseSong ----
            buttonPauseSong.setIcon(new ImageIcon(getClass().getResource("/images/Pause-icon.png")));
            buttonPauseSong.addActionListener(e -> buttonPauseSongActionPerformed(e));

            //---- buttonStopSong ----
            buttonStopSong.setIcon(new ImageIcon(getClass().getResource("/images/stop-red-icon.png")));
            buttonStopSong.addActionListener(e -> buttonStopSongActionPerformed(e));

            //---- buttonNextSong ----
            buttonNextSong.setIcon(new ImageIcon(getClass().getResource("/images/next-icon.png")));
            buttonNextSong.addActionListener(e -> buttonNextSongActionPerformed(e));

            //---- labelSongName ----
            labelSongName.setText("...");
            labelSongName.setPreferredSize(new Dimension(303, 22));
            labelSongName.setMinimumSize(new Dimension(100, 22));
            labelSongName.setMaximumSize(new Dimension(303, 22));

            //---- sliderProgress ----
            sliderProgress.setValue(0);
            sliderProgress.setMaximum(1000);
            sliderProgress.setMinorTickSpacing(1);
            sliderProgress.setSnapToTicks(true);
            sliderProgress.setToolTipText("Change progress");
            sliderProgress.addChangeListener(e -> sliderProgressStateChanged(e));

            GroupLayout panelMainLayout = new GroupLayout(panelMain);
            panelMain.setLayout(panelMainLayout);
            panelMainLayout.setHorizontalGroup(
                panelMainLayout.createParallelGroup()
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addGroup(panelMainLayout.createParallelGroup()
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(buttonPrevSong, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonPlaySong, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonPauseSong, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonStopSong, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonNextSong, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelMainLayout.createParallelGroup()
                                    .addComponent(sliderProgress, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                                    .addGroup(panelMainLayout.createSequentialGroup()
                                        .addComponent(buttonAddSong, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buttonDeleteSong)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(separator1, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE)
                                        .addGap(27, 27, 27)
                                        .addComponent(buttonSelectNext, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(buttonSelectPrev, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelMainLayout.createSequentialGroup()
                                        .addComponent(toggleButton, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sliderVolume, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(labelSongName, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap())
            );
            panelMainLayout.setVerticalGroup(
                panelMainLayout.createParallelGroup()
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelMainLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(buttonAddSong, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonDeleteSong)
                            .addComponent(buttonSelectNext, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonSelectPrev, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(separator1, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelSongName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(sliderProgress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelMainLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(toggleButton)
                            .addComponent(sliderVolume, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelMainLayout.createParallelGroup()
                            .addComponent(buttonPrevSong, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonPlaySong, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonPauseSong, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonStopSong, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonNextSong, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22))
            );
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(panelSearch, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelMain, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(panelSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(panelMain, GroupLayout.PREFERRED_SIZE, 452, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(34, Short.MAX_VALUE))
        );
        setSize(345, 595);
        setLocationRelativeTo(null);

        //---- fileChooser ----
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setAcceptAllFileFilterUsed(false);

        //======== popupPlaylist ========
        {
            popupPlaylist.setPreferredSize(new Dimension(149, 155));
            popupPlaylist.setMaximumSize(new Dimension(1200, 240));
            popupPlaylist.setMinimumSize(new Dimension(3, 11));

            //---- popmenuAddSong ----
            popmenuAddSong.setText("Add song");
            popmenuAddSong.setIcon(new ImageIcon(getClass().getResource("/images/plus_16.png")));
            popmenuAddSong.setPreferredSize(new Dimension(147, 21));
            popmenuAddSong.addActionListener(e -> popmenuAddSongActionPerformed(e));
            popupPlaylist.add(popmenuAddSong);

            //---- popmenuDeleteSong ----
            popmenuDeleteSong.setText("Remove song");
            popmenuDeleteSong.setIcon(new ImageIcon(getClass().getResource("/images/remove_icon.png")));
            popmenuDeleteSong.setPreferredSize(new Dimension(147, 21));
            popmenuDeleteSong.addActionListener(e -> popmenuDeleteSongActionPerformed(e));
            popupPlaylist.add(popmenuDeleteSong);
            popupPlaylist.addSeparator();

            //---- popmenuOpenPlaylist ----
            popmenuOpenPlaylist.setText("Open playlist");
            popmenuOpenPlaylist.setIcon(new ImageIcon(getClass().getResource("/images/open-icon.png")));
            popmenuOpenPlaylist.setPreferredSize(new Dimension(147, 21));
            popmenuOpenPlaylist.addActionListener(e -> popmenuOpenPlaylistActionPerformed(e));
            popupPlaylist.add(popmenuOpenPlaylist);

            //---- popMenuClearPlaylist ----
            popMenuClearPlaylist.setText("Clean playlist");
            popMenuClearPlaylist.setIcon(new ImageIcon(getClass().getResource("/images/clear.png")));
            popMenuClearPlaylist.setPreferredSize(new Dimension(147, 21));
            popMenuClearPlaylist.addActionListener(e -> popMenuClearPlaylistActionPerformed(e));
            popupPlaylist.add(popMenuClearPlaylist);
            popupPlaylist.addSeparator();

            //---- popmenuPlay ----
            popmenuPlay.setText("Play");
            popmenuPlay.setIcon(new ImageIcon(getClass().getResource("/images/Play.png")));
            popmenuPlay.setPreferredSize(new Dimension(147, 21));
            popmenuPlay.addActionListener(e -> popmenuPlayActionPerformed(e));
            popupPlaylist.add(popmenuPlay);

            //---- popmenuStop ----
            popmenuStop.setText("Stop");
            popmenuStop.setIcon(new ImageIcon(getClass().getResource("/images/stop-red-icon.png")));
            popmenuStop.setPreferredSize(new Dimension(147, 21));
            popmenuStop.addActionListener(e -> popmenuStopActionPerformed(e));
            popupPlaylist.add(popmenuStop);

            //---- popmenuPause ----
            popmenuPause.setText("Pause");
            popmenuPause.setIcon(new ImageIcon(getClass().getResource("/images/Pause-icon.png")));
            popmenuPause.setPreferredSize(new Dimension(147, 21));
            popmenuPause.addActionListener(e -> popmenuPauseActionPerformed(e));
            popupPlaylist.add(popmenuPause);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JMenuBar menuBar1;
    private JMenu menuFile;
    private JMenuItem menuOpenPlaylist;
    private JMenuItem menuSavePlaylist;
    private JMenuItem menuExit;
    private JMenu menuPrefs;
    private JMenu menuChangeSkin;
    private JMenuItem menuSkin1;
    private JMenuItem menuSkin2;
    private JPanel panelSearch;
    private JTextField textSearch;
    private JButton buttonSearch;
    private JPanel panelMain;
    private JScrollPane scrollPane1;
    private JList listPlayList;
    private JButton buttonAddSong;
    private JButton buttonDeleteSong;
    private JButton buttonSelectNext;
    private JButton buttonSelectPrev;
    private JSeparator separator1;
    private JToggleButton toggleButton;
    private JSlider sliderVolume;
    private JButton buttonPrevSong;
    private JButton buttonPlaySong;
    private JButton buttonPauseSong;
    private JButton buttonStopSong;
    private JButton buttonNextSong;
    private JLabel labelSongName;
    private JSlider sliderProgress;
    private JFileChooser fileChooser;
    private JPopupMenu popupPlaylist;
    private JMenuItem popmenuAddSong;
    private JMenuItem popmenuDeleteSong;
    private JMenuItem popmenuOpenPlaylist;
    private JMenuItem popMenuClearPlaylist;
    private JMenuItem popmenuPlay;
    private JMenuItem popmenuStop;
    private JMenuItem popmenuPause;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
