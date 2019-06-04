package com.king.desk.gdata;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import com.king.desk.gdata.model.Preference;
import com.king.desk.gdata.model.ViewUtil;
import com.king.desk.gdata.model.bean.TableItem;
import com.king.desk.gdata.view.OnConfirmCancelListener;
import com.king.desk.gdata.view.StoppableFrame;
import com.king.desk.gdata.viewmodel.DataTableViewModel;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.RecordDetail;
import com.king.service.gdb.bean.RecordType1v1;
import com.king.service.gdb.bean.RecordType3w;

import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class DetailActivity extends BaseActivity {

	private StoppableFrame frame;
	private JComboBox comboBox;
	private JTextArea textAreaStory;
	private JTextArea textAreaScene;
	private JTextArea textAreaRim;
	private JTextArea textAreaBjob;
	private JTextArea textAreaForeplay;
	private JTextArea textAreaRhythm;
	private JTextArea textAreaCshow;
	private JTextArea textAreaFk1;
	private JTextArea textAreaFk2;
	private JTextArea textAreaFk3;
	private JTextArea textAreaFk4;
	private JTextArea textAreaFk5;
	private JTextArea textAreaFk6;
	private JTextArea textAreaFk7;
	private JTextArea textAreaFk8;
	private JLabel lableFk6;
	private JLabel lableFk7;
	private JLabel lableFk8;
	private JLabel lableRecordName;

	private TableItem mTableItem;
	private DataTableViewModel viewModel;
	
	private String[] types = new String[] {
			"1v1", "3w", "Multi", "Together"
	};

	private OnEditorListener onEditorListener;
	
	public interface OnEditorListener {
		void onEditorChanged(TableItem item);
	}

	public void setOnEditorListener(OnEditorListener onEditorListener) {
		this.onEditorListener = onEditorListener;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DetailActivity window = new DetailActivity();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DetailActivity() {
		initialize();
	}

	/**
	 * Create the application.
	 */
	public DetailActivity(TableItem tableItem) {
		mTableItem = tableItem;
		initialize();
	}
	
	public void setViewModel(DataTableViewModel viewModel) {
		this.viewModel = viewModel;
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new StoppableFrame();
		frame.setBounds(Preference.getDetailFrame());
		// EXIT_ON_CLOSE关闭全部窗体，DISPOSE_ON_CLOSE关闭当前窗体
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent arg0) {}
			
			@Override
			public void componentResized(ComponentEvent arg0) {
				Preference.setDetailFrame(frame.getBounds());
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) {
				Preference.setDetailFrame(frame.getBounds());
			}
			
			@Override
			public void componentHidden(ComponentEvent arg0) {}
		});
		frame.setVisible(true);
		frame.setOnCloseListener(new StoppableFrame.OnCloseListener() {
			
			@Override
			public void warningClose() {
				showConfirmCancel("退出将放弃修改，是否继续？", new OnConfirmCancelListener() {
					
					@Override
					public void onConfirm() {
						frame.dispose();
					}
					
					@Override
					public void onCancel() {
						
					}
				});
			}
			
			@Override
			public boolean stopClose() {
				return true;
			}
			
			@Override
			public void onWindowClosing() {
				if (onEditorListener != null) {
					onEditorListener.onEditorChanged(mTableItem);
				}
			}
		});
		
		lableRecordName = new JLabel();
		lableRecordName.setBounds(36, 10, 563, 50);
		Font normal = new Font("Times New Roma", Font.PLAIN, 12);
		lableRecordName.setFont(normal);
		frame.getContentPane().add(lableRecordName);
		
		comboBox = new JComboBox(types);
		comboBox.setBounds(36, 70, 89, 21);
		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {
					updateType();
				}
			}
		});
		frame.getContentPane().add(comboBox);
		
		JLabel lblStory = new JLabel("Story(20)");
		lblStory.setBounds(36, 112, 65, 21);
		frame.getContentPane().add(lblStory);
		textAreaStory = new JTextArea();
		textAreaStory.setBounds(104, 111, 37, 21);
		frame.getContentPane().add(textAreaStory);
		
		JLabel label_1 = new JLabel("Scene(20)");
		label_1.setBounds(171, 112, 58, 21);
		frame.getContentPane().add(label_1);
		textAreaScene = new JTextArea();
		textAreaScene.setBounds(236, 111, 37, 21);
		frame.getContentPane().add(textAreaScene);
		
		JLabel label_2 = new JLabel("Rim(20)");
		label_2.setBounds(305, 112, 58, 21);
		frame.getContentPane().add(label_2);
		textAreaRim = new JTextArea();
		textAreaRim.setBounds(382, 111, 37, 21);
		frame.getContentPane().add(textAreaRim);
		
		JLabel label_3 = new JLabel("Bjob(50)");
		label_3.setBounds(462, 112, 55, 21);
		frame.getContentPane().add(label_3);
		textAreaBjob = new JTextArea();
		textAreaBjob.setBounds(523, 111, 37, 21);
		frame.getContentPane().add(textAreaBjob);
		
		JLabel label_4 = new JLabel("ForePlay(20)");
		label_4.setBounds(305, 148, 72, 21);
		frame.getContentPane().add(label_4);
		textAreaForeplay = new JTextArea();
		textAreaForeplay.setBounds(382, 147, 37, 21);
		frame.getContentPane().add(textAreaForeplay);
		
		JLabel label_5 = new JLabel("Rhythm(20)");
		label_5.setBounds(36, 148, 65, 21);
		frame.getContentPane().add(label_5);
		textAreaRhythm = new JTextArea();
		textAreaRhythm.setBounds(104, 147, 37, 21);
		frame.getContentPane().add(textAreaRhythm);
		
		JLabel label_6 = new JLabel("CShow(20)");
		label_6.setBounds(171, 148, 65, 21);
		frame.getContentPane().add(label_6);
		textAreaCshow = new JTextArea();
		textAreaCshow.setBounds(236, 147, 37, 21);
		frame.getContentPane().add(textAreaCshow);
		
		JLabel lableFk1 = new JLabel("坐面");
		lableFk1.setBounds(36, 205, 65, 21);
		frame.getContentPane().add(lableFk1);
		textAreaFk1 = new JTextArea();
		textAreaFk1.setBounds(104, 204, 37, 21);
		frame.getContentPane().add(textAreaFk1);

		JLabel lableFk2 = new JLabel("坐背");
		lableFk2.setBounds(171, 205, 37, 21);
		frame.getContentPane().add(lableFk2);
		textAreaFk2 = new JTextArea();
		textAreaFk2.setBounds(236, 204, 37, 21);
		frame.getContentPane().add(textAreaFk2);

		JLabel lableFk3 = new JLabel("立面");
		lableFk3.setBounds(305, 205, 37, 21);
		frame.getContentPane().add(lableFk3);
		textAreaFk3 = new JTextArea();
		textAreaFk3.setBounds(382, 204, 37, 21);
		frame.getContentPane().add(textAreaFk3);

		JLabel lableFk4 = new JLabel("立背");
		lableFk4.setBounds(462, 205, 37, 21);
		frame.getContentPane().add(lableFk4);
		textAreaFk4 = new JTextArea();
		textAreaFk4.setBounds(523, 204, 37, 21);
		frame.getContentPane().add(textAreaFk4);
		
		JLabel lableFk5 = new JLabel("侧");
		lableFk5.setBounds(36, 243, 65, 21);
		frame.getContentPane().add(lableFk5);
		textAreaFk5 = new JTextArea();
		textAreaFk5.setBounds(104, 243, 37, 21);
		frame.getContentPane().add(textAreaFk5);
		
		lableFk6 = new JLabel("特殊");
		lableFk6.setBounds(171, 243, 65, 21);
		frame.getContentPane().add(lableFk6);
		textAreaFk6 = new JTextArea();
		textAreaFk6.setBounds(236, 243, 37, 21);
		frame.getContentPane().add(textAreaFk6);

		lableFk7 = new JLabel("Sequence");
		lableFk7.setBounds(305, 243, 65, 21);
		frame.getContentPane().add(lableFk7);
		textAreaFk7 = new JTextArea();
		textAreaFk7.setBounds(382, 243, 37, 21);
		frame.getContentPane().add(textAreaFk7);

		lableFk8 = new JLabel("特殊");
		lableFk8.setBounds(462, 243, 65, 21);
		frame.getContentPane().add(lableFk8);
		textAreaFk8 = new JTextArea();
		textAreaFk8.setBounds(523, 242, 37, 21);
		frame.getContentPane().add(textAreaFk8);
		
		JButton btnCancel = new JButton("取消");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		btnCancel.setBounds(36, 299, 237, 23);
		frame.getContentPane().add(btnCancel);
		
		JButton btnOk = new JButton("保存");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveDetails();
			}
		});
		btnOk.setBounds(360, 299, 237, 23);
		frame.getContentPane().add(btnOk);
		
		updateType();
		
		initData();
	}

	private void initData() {
		// new Record
		if (mTableItem == null || mTableItem.getDetail() == null) {
			return;
		}
		ViewUtil.setLabelText(lableRecordName, mTableItem.getBean().getName());
		comboBox.setSelectedIndex(mTableItem.getBean().getType() - 1);
		updateType();
		RecordDetail detail = mTableItem.getDetail();
		textAreaBjob.setText(String.valueOf(detail.getScoreBjob()));
		textAreaRhythm.setText(String.valueOf(detail.getScoreRhythm()));
		textAreaRim.setText(String.valueOf(detail.getScoreRim()));
		textAreaStory.setText(String.valueOf(detail.getScoreStory()));
		textAreaCshow.setText(String.valueOf(detail.getScoreCshow()));
		textAreaForeplay.setText(String.valueOf(detail.getScoreForePlay()));
		textAreaScene.setText(String.valueOf(detail.getScoreScene()));
		textAreaFk1.setText(String.valueOf(detail.getScoreFkType1()));
		textAreaFk2.setText(String.valueOf(detail.getScoreFkType2()));
		textAreaFk3.setText(String.valueOf(detail.getScoreFkType3()));
		textAreaFk4.setText(String.valueOf(detail.getScoreFkType4()));
		textAreaFk5.setText(String.valueOf(detail.getScoreFkType5()));
		textAreaFk6.setText(String.valueOf(detail.getScoreFkType6()));
		if (detail instanceof RecordType3w) {
			RecordType3w detail3w = (RecordType3w) detail;
			textAreaFk7.setText(String.valueOf(detail3w.getScoreFkType7()));
			textAreaFk8.setText(String.valueOf(detail3w.getScoreFkType8()));
		}
	}
	
	private void updateType() {
		int type = comboBox.getSelectedIndex();
		if (type == 0) {
			lableFk6.setText("特殊");
			lableFk7.setVisible(false);
			lableFk8.setVisible(false);
			textAreaFk7.setVisible(false);
			textAreaFk8.setVisible(false);
		}
		else {
			lableFk6.setText("Double");
			lableFk7.setVisible(true);
			lableFk8.setVisible(true);
			textAreaFk7.setVisible(true);
			textAreaFk8.setVisible(true);
		}
		
	}

	protected void saveDetails() {
		if (mTableItem == null) {
			frame.dispose();
			return;
		}
		
		int type = comboBox.getSelectedIndex() + 1;
		// new Record
		if (mTableItem.getDetail() == null) {
			if (type == GDBProperites.VALUE_RECORD_TYPE_1V1) {
				mTableItem.setDetail(new RecordType1v1());
			}
			else {
				mTableItem.setDetail(new RecordType3w());
			}
		}
		mTableItem.getBean().setType(type);

		RecordDetail detail = mTableItem.getDetail();
		if (isNotEmpty(textAreaBjob.getText())) {
			try {
				detail.setScoreBjob(Integer.parseInt(textAreaBjob.getText()));
			} catch (Exception e) {showConfirm("Bjob输入有误");return;}
		}
		if (isNotEmpty(textAreaCshow.getText())) {
			try {
				detail.setScoreCshow(Integer.parseInt(textAreaCshow.getText()));
			} catch (Exception e) {showConfirm("CShow输入有误");return;}
		}
		if (isNotEmpty(textAreaStory.getText())) {
			try {
				detail.setScoreStory(Integer.parseInt(textAreaStory.getText()));
			} catch (Exception e) {showConfirm("Story输入有误");return;}
		}
		if (isNotEmpty(textAreaForeplay.getText())) {
			try {
				detail.setScoreForePlay(Integer.parseInt(textAreaForeplay.getText()));
			} catch (Exception e) {showConfirm("Foreplay输入有误");return;}
		}
		if (isNotEmpty(textAreaRhythm.getText())) {
			try {
				detail.setScoreRhythm(Integer.parseInt(textAreaRhythm.getText()));
			} catch (Exception e) {showConfirm("Rhythm输入有误");return;}
		}
		if (isNotEmpty(textAreaRim.getText())) {
			try {
				detail.setScoreRim(Integer.parseInt(textAreaRim.getText()));
			} catch (Exception e) {showConfirm("Rim输入有误");return;}
		}
		if (isNotEmpty(textAreaScene.getText())) {
			try {
				detail.setScoreScene(Integer.parseInt(textAreaScene.getText()));
			} catch (Exception e) {showConfirm("Scene输入有误");return;}
		}
		if (isNotEmpty(textAreaFk1.getText())) {
			try {
				detail.setScoreFkType1(Integer.parseInt(textAreaFk1.getText()));
			} catch (Exception e) {showConfirm("Fk1输入有误");return;}
		}
		if (isNotEmpty(textAreaFk2.getText())) {
			try {
				detail.setScoreFkType2(Integer.parseInt(textAreaFk2.getText()));
			} catch (Exception e) {showConfirm("Fk2输入有误");return;}
		}
		if (isNotEmpty(textAreaFk3.getText())) {
			try {
				detail.setScoreFkType3(Integer.parseInt(textAreaFk3.getText()));
			} catch (Exception e) {showConfirm("Fk3输入有误");return;}
		}
		if (isNotEmpty(textAreaFk4.getText())) {
			try {
				detail.setScoreFkType4(Integer.parseInt(textAreaFk4.getText()));
			} catch (Exception e) {showConfirm("Fk4输入有误");return;}
		}
		if (isNotEmpty(textAreaFk5.getText())) {
			try {
				detail.setScoreFkType5(Integer.parseInt(textAreaFk5.getText()));
			} catch (Exception e) {showConfirm("Fk5输入有误");return;}
		}
		if (isNotEmpty(textAreaFk6.getText())) {
			try {
				detail.setScoreFkType6(Integer.parseInt(textAreaFk6.getText()));
			} catch (Exception e) {showConfirm("Fk6输入有误");return;}
		}
	
		if (detail instanceof RecordType3w) {
			RecordType3w detail3w = (RecordType3w) detail;
			if (isNotEmpty(textAreaFk7.getText())) {
				try {
					detail3w.setScoreFkType7(Integer.parseInt(textAreaFk7.getText()));
				} catch (Exception e) {showConfirm("Fk7输入有误");return;}
			}
			if (isNotEmpty(textAreaFk8.getText())) {
				try {
					detail3w.setScoreFkType8(Integer.parseInt(textAreaFk8.getText()));
				} catch (Exception e) {showConfirm("Fk8输入有误");return;}
			}
		}

		mTableItem.setDetailChanged(true);
		if (viewModel != null) {
			viewModel.setFileChanged(true);
		}
		frame.dispose();
	}

	private boolean isNotEmpty(String text) {
		return text != null && text.trim().length() > 0;
	}
}
