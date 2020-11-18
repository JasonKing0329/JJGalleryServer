package com.king.desk.gdata;

import com.king.desk.gdata.model.Preference;
import com.king.desk.gdata.viewmodel.DataTableViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FolderEditor extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textSrcPath;
    private JTextField textSrcFrom;
    private JTextField textSrcTo;
    private JTextField textTarget;
    private JButton btnRemove;
    private JLabel labelProgress;
    private DataTableViewModel dataTableViewModel;

    private OnFolderChangedListener onFolderChangedListener;

    public void setDataTableViewModel(DataTableViewModel dataTableViewModel) {
        this.dataTableViewModel = dataTableViewModel;
    }

    public FolderEditor() {
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
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeImages();
            }
        });
    }

    public void setOnFolderChangedListener(OnFolderChangedListener onFolderChangedListener) {
        this.onFolderChangedListener = onFolderChangedListener;
    }

    private void onOK() {
        // add your code here
        onFolderChangedListener.onSetChange(textSrcPath.getText(), textSrcFrom.getText()
                , textSrcTo.getText(), textTarget.getText());
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        FolderEditor dialog = new FolderEditor();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void updateSizeAndLocation() {
        Rectangle bounds = getBounds();
        Point locations = getLocation();
        Rectangle frame = new Rectangle();
        frame.setBounds(locations.x, locations.y, bounds.width, bounds.height);
        Preference.setFolderEditorFrame(frame);
    }

    public void showDialog() {
        setTitle("批量修改目录");
        Rectangle rectangle = Preference.getFolderEditorFrame();
        setPreferredSize(new Dimension(rectangle.width, rectangle.height));
        setLocation(rectangle.x, rectangle.y);
        pack();
        setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private void removeImages() {
        dataTableViewModel.removeUselessImages();
    }

    public void updateProgress(String value) {
        labelProgress.setText(value);
    }

    public interface OnFolderChangedListener {
        void onSetChange(String src, String from, String to, String target);
    }
}
