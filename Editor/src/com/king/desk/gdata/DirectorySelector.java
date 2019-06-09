package com.king.desk.gdata;

import com.king.desk.gdata.adapter.TextAdapter;
import com.king.desk.gdata.model.Preference;
import com.king.desk.gdata.view.MouseClickListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DirectorySelector extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel lableName;
    private JList listText;
    private JLabel lableMiddleTitle;
    private JTextField textMiddle;
    private JLabel lableResultTitle;
    private JLabel labelResult;
    private JCheckBox cbMulti;

    private StringBuffer resultBuffer;
    private List<String> dirList;

    private OnResultSetListener onResultSetListener;

    public void setOnResultSetListener(OnResultSetListener onResultSetListener) {
        this.onResultSetListener = onResultSetListener;
    }

    public DirectorySelector() {
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateSizeAndLocation();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                updateSizeAndLocation();
            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        textMiddle.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                onResultChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                onResultChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        cbMulti.addItemListener(arg0 -> {
            onResultChanged();
        });

        listText.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listText.addMouseListener(new MouseClickListener() {

            @Override
            protected void onDoubleClick(MouseEvent event) {
            }

            @Override
            public void onClickRightMouse(MouseEvent event) {
            }

            @Override
            public void onClickLeftMouse(MouseEvent event) {
                int index = listText.getSelectedIndex();
                System.out.println("index " + index);
                onResultChanged();
            }
        });
        dirList = getTextList();
        TextAdapter adapter = new TextAdapter();
        adapter.setList(dirList);
        listText.setModel(adapter);
    }

    private List<String> getTextList() {
        List<String> list = new ArrayList<>();
        list.add("H:/root/G/Actors");
        list.add("H:/root/G/Scenes");
        list.add("H:/root/G/3");
        list.add("H:/root/G/Long");
        list.add("H:/root/G/multi");
        list.add("D:/king/game/other/scene");
        list.add("D:/king/game/other/star");
        list.add("E:/TDDOWNLOAD/Other");
        list.add("F:/myparadise/latestShow/other/star");
        list.add("F:/myparadise/latestShow/other/three-way");
        list.add("F:/myparadise/latestShow/other/together");
        list.add("F:/myparadise/latestShow/other/multi-way");
        return list;
    }

    private void onResultChanged() {
        resultBuffer = new StringBuffer();
        int index = listText.getSelectedIndex();
        if (index >= 0 && index < dirList.size()) {
            resultBuffer.append(dirList.get(index));
        }
        if (textMiddle.getText() != null && textMiddle.getText().trim().length() > 0) {
            resultBuffer.append("/").append(textMiddle.getText());
        }
        if (cbMulti.isSelected()) {
            resultBuffer.append("/3orMulti-way");
        }
        labelResult.setText(resultBuffer.toString());
    }

    private void onOK() {
        // add your code here
        if (onResultSetListener != null) {
            onResultSetListener.onResultText(labelResult.getText());
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Preference.create();
        DirectorySelector dialog = new DirectorySelector();
        dialog.showDialog();
    }

    public void showDialog() {
        setTitle("Set Directory");
        Rectangle rectangle = Preference.getDeprecatedSelectorFrame();
        setPreferredSize(new Dimension(rectangle.width, rectangle.height));
        setLocation(rectangle.x, rectangle.y);
        pack();
        setVisible(true);
    }

    private void updateSizeAndLocation() {
        Rectangle bounds = getBounds();
        Point locations = getLocation();
        Rectangle frame = new Rectangle();
        frame.setBounds(locations.x, locations.y, bounds.width, bounds.height);
        Preference.setDeprecatedSelectorFrame(frame);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public interface OnResultSetListener {
        void onResultText(String text);
    }
}
