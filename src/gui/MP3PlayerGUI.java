package gui;

import utils.SkinUtils;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
/*
 * Created by JFormDesigner on Fri Aug 30 21:38:55 EEST 2019
 */



/**
 * @author Kirilo
 */
public class MP3PlayerGUI extends JFrame {
    public MP3PlayerGUI() {
        initComponents();
    }

    private void menuSkin1ActionPerformed(ActionEvent e) {
        SkinUtils.changeSkin(this, UIManager.getSystemLookAndFeelClassName());
    }

    private void menuSkin2ActionPerformed(ActionEvent e) {
        SkinUtils.changeSkin(this, new NimbusLookAndFeel());
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
        listPlayList = new JList<>();
        buttonAddSong = new JButton();
        buttonDeleteSong = new JButton();
        buttonSelectPrev = new JButton();
        buttonSelectNext = new JButton();
        separator1 = new JSeparator();
        toggleButton1 = new JToggleButton();
        sliderVolume = new JSlider();
        buttonPrevSong = new JButton();
        buttonPlaySong = new JButton();
        buttonPauseSong = new JButton();
        buttonStopSong = new JButton();
        buttonNextSong = new JButton();

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
                menuFile.add(menuOpenPlaylist);

                //---- menuSavePlaylist ----
                menuSavePlaylist.setText("Save playlist");
                menuSavePlaylist.setIcon(new ImageIcon(getClass().getResource("/images/save_16.png")));
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
/*            panelSearch.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(
            0,0,0,0), "JF\u006frmDes\u0069gner \u0045valua\u0074ion",javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder
            .BOTTOM,new java.awt.Font("D\u0069alog",java.awt.Font.BOLD,12),java.awt.Color.
            red),panelSearch. getBorder()));panelSearch. addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void propertyChange(java.
            beans.PropertyChangeEvent e){if("\u0062order".equals(e.getPropertyName()))throw new RuntimeException();}});*/

            //---- textSearch ----
            textSearch.setText("input name of song");
            textSearch.setFont(new Font("Tahoma", Font.ITALIC, 11));
            textSearch.setForeground(UIManager.getColor("Button.select"));

            //---- buttonSearch ----
            buttonSearch.setText("Find");
            buttonSearch.setIcon(new ImageIcon(getClass().getResource("/images/search_16.png")));

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
                listPlayList.setModel(new AbstractListModel<String>() {
                    String[] values = {
                        "song 1",
                        "song 2",
                        "song 3",
                        "song 4",
                        "song 5",
                        "song 6",
                        "song 7",
                        "song 8",
                        "song 9",
                        "song 10"
                    };
                    @Override
                    public int getSize() { return values.length; }
                    @Override
                    public String getElementAt(int i) { return values[i]; }
                });
                listPlayList.setToolTipText("List of songs");
                scrollPane1.setViewportView(listPlayList);
            }

            //---- buttonAddSong ----
            buttonAddSong.setIcon(new ImageIcon(getClass().getResource("/images/plus_16.png")));
            buttonAddSong.setHorizontalAlignment(SwingConstants.LEFT);
            buttonAddSong.setToolTipText("Add song");

            //---- buttonDeleteSong ----
            buttonDeleteSong.setIcon(new ImageIcon(getClass().getResource("/images/remove_icon.png")));
            buttonDeleteSong.setHorizontalAlignment(SwingConstants.LEFT);
            buttonDeleteSong.setName("btnDeleteSong");
            buttonDeleteSong.setHorizontalTextPosition(SwingConstants.LEFT);
            buttonDeleteSong.setToolTipText("Delete song");

            //---- buttonSelectPrev ----
            buttonSelectPrev.setIcon(new ImageIcon(getClass().getResource("/images/arrow-down-icon.png")));
            buttonSelectPrev.setHorizontalAlignment(SwingConstants.LEFT);

            //---- buttonSelectNext ----
            buttonSelectNext.setIcon(new ImageIcon(getClass().getResource("/images/arrow-up-icon.png")));
            buttonSelectNext.setHorizontalAlignment(SwingConstants.LEFT);
            buttonSelectNext.setHorizontalTextPosition(SwingConstants.LEFT);
            buttonSelectNext.setHideActionText(true);

            //---- separator1 ----
            separator1.setPreferredSize(new Dimension(3, 0));
            separator1.setOrientation(SwingConstants.VERTICAL);

            //---- toggleButton1 ----
            toggleButton1.setSelectedIcon(new ImageIcon(getClass().getResource("/images/mute.png")));
            toggleButton1.setIcon(new ImageIcon(getClass().getResource("/images/speaker.png")));
            toggleButton1.setToolTipText("Off sound");

            //---- sliderVolume ----
            sliderVolume.setValue(0);

            //---- buttonPrevSong ----
            buttonPrevSong.setIcon(new ImageIcon(getClass().getResource("/images/prev-icon.png")));

            //---- buttonPlaySong ----
            buttonPlaySong.setIcon(new ImageIcon(getClass().getResource("/images/Play.png")));

            //---- buttonPauseSong ----
            buttonPauseSong.setIcon(new ImageIcon(getClass().getResource("/images/Pause-icon.png")));

            //---- buttonStopSong ----
            buttonStopSong.setIcon(new ImageIcon(getClass().getResource("/images/stop-red-icon.png")));

            //---- buttonNextSong ----
            buttonNextSong.setIcon(new ImageIcon(getClass().getResource("/images/next-icon.png")));

            GroupLayout panelMainLayout = new GroupLayout(panelMain);
            panelMain.setLayout(panelMainLayout);
            panelMainLayout.setHorizontalGroup(
                panelMainLayout.createParallelGroup()
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addGroup(panelMainLayout.createParallelGroup()
                            .addGroup(panelMainLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelMainLayout.createParallelGroup()
                                    .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                                    .addGroup(panelMainLayout.createSequentialGroup()
                                        .addComponent(buttonAddSong, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buttonDeleteSong)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                                        .addComponent(separator1, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE)
                                        .addGap(27, 27, 27)
                                        .addComponent(buttonSelectPrev, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(buttonSelectNext, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelMainLayout.createSequentialGroup()
                                        .addComponent(toggleButton1, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sliderVolume, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                                .addGap(0, 60, Short.MAX_VALUE)))
                        .addContainerGap())
            );
            panelMainLayout.setVerticalGroup(
                panelMainLayout.createParallelGroup()
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelMainLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(buttonAddSong, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonDeleteSong)
                            .addComponent(buttonSelectPrev, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonSelectNext, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(separator1, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 287, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(panelMainLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(toggleButton1)
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
                    .addComponent(panelMain, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(36, Short.MAX_VALUE))
        );
        setSize(345, 595);
        setLocationRelativeTo(null);
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
    private JList<String> listPlayList;
    private JButton buttonAddSong;
    private JButton buttonDeleteSong;
    private JButton buttonSelectPrev;
    private JButton buttonSelectNext;
    private JSeparator separator1;
    private JToggleButton toggleButton1;
    private JSlider sliderVolume;
    private JButton buttonPrevSong;
    private JButton buttonPlaySong;
    private JButton buttonPauseSong;
    private JButton buttonStopSong;
    private JButton buttonNextSong;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
