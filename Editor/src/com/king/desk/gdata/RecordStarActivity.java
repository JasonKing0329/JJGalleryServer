package com.king.desk.gdata;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JRadioButton;

import com.king.desk.gdata.adapter.RecordStarAdapter;
import com.king.desk.gdata.model.Preference;
import com.king.desk.gdata.model.bean.TableItem;
import com.king.desk.gdata.view.MouseClickListener;
import com.king.desk.gdata.view.OnConfirmCancelListener;
import com.king.desk.gdata.view.OnDataChangedListener;
import com.king.desk.gdata.view.StoppableFrame;
import com.king.desk.gdata.viewmodel.DataTableViewModel;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.RecordStar;
import com.king.service.gdb.bean.Star;

public class RecordStarActivity extends BaseActivity {

	private StoppableFrame frame;
	private JTable table;
	private JRadioButton rdbtnTop;
	private JRadioButton rdbtnBottom;
	private JRadioButton rdbtnMix;
	
	private TableItem mTableItem;
	private RecordStarAdapter adapter;
	private DataTableViewModel viewModel;
	
	private OnEditorListener onEditorListener;
	
	private int mContextMenuRow;
	
	public interface OnEditorListener {
		void onEditorChanged(TableItem item);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RecordStarActivity window = new RecordStarActivity();
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
	public RecordStarActivity() {
		initialize();
	}

	/**
	 * Create the application.
	 */
	public RecordStarActivity(TableItem tableItem) {
		mTableItem = tableItem;
		initialize();
	}
	
	public void setOnEditorListener(OnEditorListener onEditorListener) {
		this.onEditorListener = onEditorListener;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new StoppableFrame();
		// EXIT_ON_CLOSE关闭全部窗体，DISPOSE_ON_CLOSE关闭当前窗体
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(Preference.getRecordStarFrame());
		frame.getContentPane().setLayout(null);
		frame.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent arg0) {}
			
			@Override
			public void componentResized(ComponentEvent arg0) {
				Preference.setRecordStarFrame(frame.getBounds());
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) {
				Preference.setRecordStarFrame(frame.getBounds());
			}
			
			@Override
			public void componentHidden(ComponentEvent arg0) {}
		});
		// 去掉标题栏，同时也去掉了边框、拖动事件
//		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setOnCloseListener(new StoppableFrame.OnCloseListener() {
			
			@Override
			public void warningClose() {
				
			}
			
			@Override
			public boolean stopClose() {
				return false;
			}
			
			@Override
			public void onWindowClosing() {
				if (onEditorListener != null) {
					onEditorListener.onEditorChanged(mTableItem);
				}
			}
		});
		
		JButton button = new JButton("添加");
		button.setBounds(462, 31, 93, 23);
		frame.getContentPane().add(button);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				newStar();
			}
		});
		
		rdbtnTop = new JRadioButton("Top");
		rdbtnTop.setBounds(32, 31, 57, 23);
		frame.getContentPane().add(rdbtnTop);
		rdbtnTop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnTop.isSelected()) {
					updateSelectedStar(GDBProperites.VALUE_RELATION_TOP);
				}
			}
		});
		
		rdbtnBottom = new JRadioButton("Bottom");
		rdbtnBottom.setBounds(98, 31, 68, 23);
		frame.getContentPane().add(rdbtnBottom);
		rdbtnBottom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnBottom.isSelected()) {
					updateSelectedStar(GDBProperites.VALUE_RELATION_BOTTOM);
				}
			}
		});
		
		rdbtnMix = new JRadioButton("Mix");
		rdbtnMix.setBounds(177, 31, 48, 23);
		frame.getContentPane().add(rdbtnMix);
		rdbtnMix.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnMix.isSelected()) {
					updateSelectedStar(GDBProperites.VALUE_RELATION_MIX);
				}
			}
		});
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnTop);
		group.add(rdbtnBottom);
		group.add(rdbtnMix);
		
		initTable();
	}

	protected void updateSelectedStar(int type) {
		viewModel.setFileChanged(true);
		mTableItem.setStarChanged(true);
		int row = table.getSelectedRow();
		int column = table.getSelectedColumn();
		if (column == 0) {
			try {
				mTableItem.getStars().get(row).setType(type);
				table.updateUI();
			} catch (Exception e) {}
		}
	}

	private void initTable() {
		table = new JTable();
		table.setBounds(32, 73, 525, 237);
		// 注释掉下面这句，让每个单元格宽度均分
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		table.addMouseListener(new MouseClickListener() {
			
			@Override
			public void onClickRightMouse(MouseEvent event) {
				int row = table.rowAtPoint(event.getPoint());
				if (table.isRowSelected(row)) {
					createPopupMenu(row, event);
				}
			}
			
			@Override
			public void onClickLeftMouse(MouseEvent event) {
			}
		});
		frame.getContentPane().add(table);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(table.getBounds());
		frame.getContentPane().add(scrollPane);
		
		if (mTableItem != null) {
			adapter = new RecordStarAdapter();
			adapter.setValueList(mTableItem.getStars());
			adapter.setOnDataChangedListener(new OnDataChangedListener() {
				
				@Override
				public void onDataChanged(String value, int row, int col) {
					// 能编辑的都是新加的，可以直接设置value
					RecordStar rs = mTableItem.getStars().get(row);
					switch (col) {
					// 0是type, 1是id, 2是starId
					case RecordStarAdapter.COL_STAR_NAME:// name
						rs.getStar().setName(value);
						break;
					case RecordStarAdapter.COL_SCORE:// score
						try {
							rs.setScore(Integer.parseInt(value));
						} catch (Exception e) {}
						break;
					case RecordStarAdapter.COL_SCORE_C:// scoreC
						try {
							rs.setScoreC(Integer.parseInt(value));
						} catch (Exception e) {}
						break;

					default:
						break;
					}
				}
			});
			table.setModel(adapter);
		}
	}
	
	protected void newStar() {
		viewModel.setFileChanged(true);
		mTableItem.setStarChanged(true);
		
		if (mTableItem.getStars() == null) {
			mTableItem.setStars(new ArrayList<RecordStar>());
			adapter.setValueList(mTableItem.getStars());
		}
		int type = GDBProperites.VALUE_RELATION_MIX;
		if (rdbtnTop.isSelected()) {
			type = GDBProperites.VALUE_RELATION_TOP;
		}
		else if (rdbtnBottom.isSelected()) {
			type = GDBProperites.VALUE_RELATION_BOTTOM;
		}
		RecordStar rs = new RecordStar();
		rs.setType(type);
		rs.setStar(new Star());
		mTableItem.getStars().add(rs);

		table.updateUI();
	}

	private void createPopupMenu(final int row, MouseEvent event) {
		mContextMenuRow = row;
		
		JPopupMenu popupMenu = new JPopupMenu();
        
        JMenuItem delMenItem = new JMenuItem();
        delMenItem.setText("删除");
        delMenItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
        		// RecordStar还未被添加至数据库（无论Record是新增还是已存），可以直接从内存删除
            	if (mTableItem.getStars().get(row).getId() == null) {
            		doDelete(row);
				}
            	// RecordStar已被添加至数据库中，提示将即时删除记录以及相关记录
            	else {
                	showConfirmCancel("Relationship已经存在于数据库中，确认将其标记为删除？", "删除", new OnConfirmCancelListener() {
    					
    					@Override
    					public void onConfirm() {
    						doDelete(row);
    					}
    					
    					@Override
    					public void onCancel() {
    						
    					}
    				});
				}
            }
        });
        popupMenu.add(delMenItem);

		RecordStar rs = mTableItem.getStars().get(row);
		// 只运行新建的选择
		if (rs.getId() == null) {
	        JMenuItem selectItem = new JMenuItem();
	        selectItem.setText("选择");
	        selectItem.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					new StarActivity(true, new StarActivity.OnSelectListener() {
						
						@Override
						public void onSelect(Star star) {
							onStarSelected(star);
						}
					});
				}
			});
	        popupMenu.add(selectItem);
		}
        popupMenu.show(table, event.getX(), event.getY());
    }

	/**
	 * 执行内存中的删除
	 * @param row
	 */
	protected void doDelete(int row){
		RecordStar rs = mTableItem.getStars().get(row);
		// RecordStar还未被添加至数据库
    	if (rs.getId() == null) {
    		mTableItem.getStars().remove(row);
    		table.updateUI();
    	}
    	// RecordStar已被添加至数据库中
    	else {
			viewModel.setFileChanged(true);
			mTableItem.setStarChanged(true);
    		mTableItem.getStars().remove(row);
    		table.updateUI();
		}
	}
	
	private void onStarSelected(Star star) {
		RecordStar rs = mTableItem.getStars().get(mContextMenuRow);
		rs.setStar(star);
		rs.setStarId(star.getId());
		table.updateUI();
	}

	public void setViewModel(DataTableViewModel viewModel) {
		this.viewModel = viewModel;
		
	}

}
