package com.king.desk.gdata;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.BadLocationException;

import com.king.desk.gdata.adapter.BodyRenderer;
import com.king.desk.gdata.adapter.HeadRenderer;
import com.king.desk.gdata.adapter.TableAdapter;
import com.king.desk.gdata.model.DebugLog;
import com.king.desk.gdata.model.FileUtil;
import com.king.desk.gdata.model.Preference;
import com.king.desk.gdata.model.ScreenUtil;
import com.king.desk.gdata.model.ViewUtil;
import com.king.desk.gdata.model.bean.TableItem;
import com.king.desk.gdata.model.live.LiveObserver;
import com.king.desk.gdata.res.R;
import com.king.desk.gdata.view.MouseClickListener;
import com.king.desk.gdata.view.OnConfirmCancelListener;
import com.king.desk.gdata.view.OnDataChangedListener;
import com.king.desk.gdata.view.StoppableFrame;
import com.king.desk.gdata.viewmodel.DataTableViewModel;

public class MainActivity extends BaseActivity {

	private StoppableFrame frame;
	private JTable table;
	private JComboBox<String> comboBox;
	private JLabel lableLoading;
	private JCheckBox cbVersion;
	private JTextArea textAreaVersion;
	private JCheckBox cbDeleteStarOption;
	private JScrollPane tableScrollPane;
	private JTextField textSearchField;
	
	private JPanel tablePanel;
	private JPanel toolbarPanel;
	private JPanel searchPanel;
	
	private final String CARD_MAIN = "card_main";

	private final String CARD_LOADING = "card_loading";

	private DataTableViewModel viewModel;

	private TableAdapter tableAdapter;

	private JPanel loadingPane;
	
	private DocumentListener mTextWatcher;

	/**
	 * 表格的单元格渲染器
	 */
	private BodyRenderer tableCellRenderer = new BodyRenderer();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainActivity window = new MainActivity();
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
	public MainActivity() {
		Preference.create();
		viewModel = new DataTableViewModel();
		initialize();
		initData();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		// 为了使控件能随窗口大小变化而变化，content使用GridBagLayout与BoxLayout结合使用的方法
		// 为了又能支持loading界面，只好用CardLayout包裹一层
		// 缺点是loadingPane只能整个显示在content纸上，无法背景透明
		
		initFrame();
		initLoading();
		initMainContent();
	}

	private void initFrame() {
		frame = new StoppableFrame();
		frame.setBounds(Preference.getMainActivityFrame());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(getTitle(false));
		frame.setResizable(true);
		frame.setVisible(true);
		frame.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent arg0) {}
			
			@Override
			public void componentResized(ComponentEvent arg0) {
				Preference.setMainActivityFrame(frame.getBounds());
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) {
				Preference.setMainActivityFrame(frame.getBounds());
			}
			
			@Override
			public void componentHidden(ComponentEvent arg0) {}
		});
		
		frame.setLayout(new CardLayout());
		frame.setOnCloseListener(new StoppableFrame.OnCloseListener() {

			@Override
			public boolean stopClose() {
				return viewModel.isFileChanged();
			}
			
			@Override
			public void warningClose() {
				showConfirmCancel("当前文件有改动，继续退出将会放弃修改，是否继续？"
						, "警告", new OnConfirmCancelListener() {
							
							@Override
							public void onConfirm() {
								viewModel.destroy();
								frame.dispose();
							}
							
							@Override
							public void onCancel() {
								
							}
						});
			}

			@Override
			public void onWindowClosing() {
				viewModel.destroy();
			}

		});

		JPanel root = new JPanel();
		root.setLayout(new CardLayout());
		frame.setContentPane(root);
	}

	private void initMainContent() {
		
		JPanel mainPanel = new JPanel();
		// panelContainer 的布局为 GridBagLayout
		mainPanel.setLayout(new GridBagLayout());
		frame.getContentPane().add(CARD_MAIN, mainPanel);

		createToolbar();
		createSearchbar();
		createTable();

		GridBagConstraints gbcToolbar = new GridBagConstraints(); 
		gbcToolbar.gridx = 0; 
		gbcToolbar.gridy = 0;// 行
		gbcToolbar.weightx = 1.0; 
		gbcToolbar.weighty = 0; 
		gbcToolbar.fill = GridBagConstraints.HORIZONTAL ; 
		mainPanel.add(toolbarPanel, gbcToolbar); 

		GridBagConstraints gbcSearch = new GridBagConstraints(); 
		gbcSearch.gridx = 0; 
		gbcSearch.gridy = 1; // 行
		gbcSearch.weightx = 1.0; 
		gbcSearch.weighty = 0; 
		gbcSearch.fill = GridBagConstraints.HORIZONTAL ; 
		mainPanel.add(searchPanel, gbcSearch);
		
		GridBagConstraints gbcTable = new GridBagConstraints(); 
		gbcTable.gridx = 0; 
		gbcTable.gridy = 2; // 行
		gbcTable.weightx = 1.0; 
		gbcTable.weighty = 1.0; 
		gbcTable.fill = GridBagConstraints.BOTH ; 
		mainPanel.add(tablePanel, gbcTable); 
	}

	/**
	 * 整个布局理论上很合理，但运用起来十分诡异
	 * BoxLayout的优势在于在vertical于horizontal方向上易于控制，可以加入类似margin和match_parent的功能
	 * 但是在运用时遇到以下情况
	 * 1.一开始horPanel中横向排列所有子控件，btnSave与textAreaVersion中加入glue(match剩余部分)
	 *   --这种情况下出现的问题：
	 *   	(1)btnNew显示不全，无论如何设置setPreferredSize/max/min，都改变不了其宽度
	 *   	(2)textAreaVersion充满了本该是glue填充的部分，直达btnSave边界
	 *   原因应该是，BoxLayout属于控制子控件占据控件的布局管理器，由于对所有编辑框控件采取了默认最大填充的策略，
	 *   导致btnNew也因编辑框的最大填充策略而相应调整为被挤压宽度。
	 *   这样就只能用setMaximumSize来控制其宽度
	 * 2.用setMaximumSize控制编辑框控件的宽度后出现的问题：
	 * 	 	(1)编辑框仍然不是setMaximumSize的宽度，而变成了wrap_content的最小宽度
	 * 		(2)btnNew仍然显示不全，情况同1
	 *   glue到是填充了剩余的控件。
	 *   猜想这样设置可能让glue占了上风，左侧所有的控件仍被迫调整为最小宽度。
	 *   然后猜测可以对左侧所有空间再包裹一层BoxLayout，并设置其setPreferredSize
	 * 3.经过2最后的设置后果然生效了，btnNew显示全了，编辑框也按照setMaximumSize的宽度显示了
	 *   在这种思路下，再尝试对编辑框进行setMaximumSize/setPreferredSize的试验，发现还只有setMaximumSize才能控制住，setPreferredSize或者什么都不设置又会让编辑框最大化填充
	 *   这样算是基本上解决了这种排版，不过还有一个问题：
	 *   btnNew只是将内容显示完整了，仍然不是setPreferredSize的宽度
	 *   同样的，btnSave也只是wrap_content，仍然不是setPreferredSize的宽度
	 *   setMinimumSize也没有用
	 *   难道在这种布局下，所有控件都不能按照setPreferredSize来排版？
	 */
	private void createToolbar() {
		toolbarPanel = new JPanel();
		toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.Y_AXIS));
		
		JPanel horPanel = new JPanel();
		horPanel.setLayout(new BoxLayout(horPanel, BoxLayout.X_AXIS));
		horPanel.add(Box.createHorizontalStrut(R.dimen.padding_hor));
		
		JPanel leftContainer = new JPanel();
		leftContainer.setPreferredSize(new Dimension(600, R.dimen.toolbar_height));
		leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.X_AXIS));

		comboBox = new JComboBox<String>(viewModel.getSource());
		comboBox.setMaximumSize(new Dimension(140, R.dimen.toolbar_height));
		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {
					int position = comboBox.getSelectedIndex();
					viewModel.filterByType(position);
				}
			}
		});
		leftContainer.add(comboBox);

		leftContainer.add(Box.createHorizontalStrut(R.dimen.toolbar_item_margin));
		
		JButton btnNew = new JButton("New record");
		ViewUtil.setIcon(btnNew, R.drawable.ic_add, 20, 20);
//		btnNew.setPreferredSize(new Dimension(140, R.dimen.toolbar_height));
		btnNew.setHorizontalTextPosition(JButton.RIGHT);
		btnNew.setVerticalTextPosition(JButton.CENTER);
		btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewModel.newRecord();
				int row = table.getRowCount() - 1;
//				table.setRowSelectionInterval(row, row);
				table.scrollRectToVisible(table.getCellRect(row, 0, true));
			}
		});
		leftContainer.add(btnNew);

		leftContainer.add(Box.createHorizontalStrut(R.dimen.toolbar_item_margin));
		
		cbDeleteStarOption = new JCheckBox("删除多余Star");
		leftContainer.add(cbDeleteStarOption);

		leftContainer.add(Box.createHorizontalStrut(R.dimen.toolbar_item_margin));
		
		cbVersion = new JCheckBox("修改版本号");
		cbVersion.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (cbVersion.isSelected()) {
					viewModel.setFileChanged(true);
					textAreaVersion.setVisible(true);
					textAreaVersion.setText(viewModel.getVersion());
				}
				else {
					textAreaVersion.setVisible(false);
				}
			}
		});
		leftContainer.add(cbVersion);

		textAreaVersion = new JTextArea();
		// boxlayout中，对JTextArea及Filed之类的会忽略setPreferredSize直接填充剩余空间，用setMaximumSize可以控制住
//		textAreaVersion.setPreferredSize(new Dimension(100, R.dimen.toolbar_height));
		textAreaVersion.setMaximumSize(new Dimension(50, R.dimen.toolbar_height));
		textAreaVersion.setVisible(false);
		leftContainer.add(textAreaVersion);
		horPanel.add(leftContainer);
		
		horPanel.add(Box.createHorizontalGlue());

		JButton btnStar = new JButton("Stars");
		btnStar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new StarActivity();
			}
		});
		horPanel.add(btnStar);

		horPanel.add(Box.createHorizontalStrut(R.dimen.toolbar_item_margin));
		
		JButton btnServer = new JButton("Server dir");
		btnServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					FileUtil.openFile(Conf.DIR_SERVER);
				} catch (IOException ioe) {
					ioe.printStackTrace();
					showConfirm("打开server dir失败：" + ioe.getMessage());
				}
			}
		});
		horPanel.add(btnServer);

		horPanel.add(Box.createHorizontalStrut(R.dimen.toolbar_item_margin));
		
		JButton btnCreator = new JButton("Creator dir");
		btnCreator.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					FileUtil.openFile(Conf.DIR_APP);
				} catch (IOException ioe) {
					ioe.printStackTrace();
					showConfirm("打开Creator dir失败：" + ioe.getMessage());
				}
			}
		});
		horPanel.add(btnCreator);

		horPanel.add(Box.createHorizontalStrut(R.dimen.toolbar_item_margin));
		
		JButton btnSave = new JButton("保存");
		int width = 80;
		btnNew.setPreferredSize(new Dimension(width, R.dimen.toolbar_height));
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				viewModel.setDeleteStarWithoutRecords(cbDeleteStarOption.isSelected());
				viewModel.setVersion(cbVersion.isSelected() ? textAreaVersion.getText() : null);
				viewModel.save();
			}
		});
		horPanel.add(btnSave);
		
		horPanel.add(Box.createHorizontalStrut(R.dimen.padding_hor));
		
		toolbarPanel.add(Box.createVerticalStrut(R.dimen.padding_ver));
		toolbarPanel.add(horPanel);
		toolbarPanel.add(Box.createVerticalStrut(20));
	}

	private void createTable() {
		tablePanel = new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));

		JPanel horPanel = new JPanel();
		horPanel.setLayout(new BoxLayout(horPanel, BoxLayout.X_AXIS));
		
		// 必须这样设置renderer，使用setDefaultRenderer不管用
		table = new JTable() {
			@Override
			public TableCellRenderer getDefaultRenderer(Class<?> arg0) {
			
				return tableCellRenderer;
			}
			
		};
		// 禁止每个单元格宽度均分
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// 只对单元格有效，表头的事件通过后面updateColumnWidth里的方法实现
		table.addMouseListener(new MouseClickListener() {
			
			@Override
			public void onClickRightMouse(MouseEvent event) {
				int row = table.rowAtPoint(event.getPoint());
				int column = table.columnAtPoint(event.getPoint());
				if (column == Constants.CommonColumn.DATE.ordinal()) {
					if (table.isColumnSelected(column)) {
						showReadDatePopup(row, event);
					}
				}
				else {
					if (table.isRowSelected(row)) {
						showDeletePopup(row, event);
					}
				}
			}
			
			@Override
			public void onClickLeftMouse(MouseEvent event) {
				int row = table.rowAtPoint(event.getPoint());
				int column = table.columnAtPoint(event.getPoint());
				if (viewModel.isStarRelatedCell(column)) {
					editStar(row);
				}
				else if (viewModel.isDetailRelatedCell(column)) {
					editDetail(row);
				}
			}
		});
		
		tableScrollPane = new JScrollPane(table);

		horPanel.add(Box.createHorizontalStrut(R.dimen.padding_hor));
		horPanel.add(tableScrollPane);
		horPanel.add(Box.createHorizontalStrut(R.dimen.padding_hor));

		tablePanel.add(Box.createVerticalStrut(10));
		tablePanel.add(horPanel);
		tablePanel.add(Box.createVerticalStrut(R.dimen.padding_ver));
	}

	private void createSearchbar() {
		searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));

		searchPanel.add(Box.createHorizontalStrut(R.dimen.padding_hor));
		
		JLabel label = new JLabel("搜索文件名或Star");
		searchPanel.add(label);

		searchPanel.add(Box.createHorizontalStrut(R.dimen.toolbar_item_margin));
		
		textSearchField = new JTextField();
		mTextWatcher = new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent event) {
				try {
					String text = event.getDocument().getText(0, event.getDocument().getLength());
					viewModel.searchTable(text);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent event) {
				try {
					String text = event.getDocument().getText(0, event.getDocument().getLength());
					viewModel.searchTable(text);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent event) {
			}
		};
		textSearchField.getDocument().addDocumentListener(mTextWatcher);
		
		searchPanel.add(textSearchField);

		searchPanel.add(Box.createHorizontalStrut(R.dimen.toolbar_item_margin));
		
		final JCheckBox cbModify = new JCheckBox("只显示待更新数据");
		cbModify.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if (cbModify.isSelected()) {
					viewModel.filterModify(true);
				}
				else {
					viewModel.filterModify(false);
				}
			}
		});
		searchPanel.add(cbModify);

		searchPanel.add(Box.createHorizontalStrut(R.dimen.padding_hor));
	}

	protected void editStar(int row) {
		RecordStarActivity activity = new RecordStarActivity(viewModel.getTableItem(row));
		activity.setViewModel(viewModel);
		activity.setOnEditorListener(new RecordStarActivity.OnEditorListener() {
			
			@Override
			public void onEditorChanged(TableItem item) {
				if (item.isStarChanged()) {
					viewModel.refreshStarInfo(item);
					notifyDataSetChanged();
				}
			}
		});
	}

	protected void editDetail(int row) {
		DetailActivity activity = new DetailActivity(viewModel.getTableItem(row));
		activity.setViewModel(viewModel);
		activity.setOnEditorListener(new DetailActivity.OnEditorListener() {
			
			@Override
			public void onEditorChanged(TableItem item) {
				notifyDataSetChanged();
			}
		});
	}

	private void initData() {
		viewModel.messageObserver.observe(new LiveObserver<String>() {

			@Override
			public void onChanged(String message) {
				showConfirm(message);
			}
		});
		viewModel.loadingObserver.observe(new LiveObserver<Boolean>() {

			@Override
			public void onChanged(Boolean show) {
				showLoading(show);
			}
		});
		viewModel.fileChangeObserver.observe(new LiveObserver<Boolean>() {

			@Override
			public void onChanged(Boolean changed) {
				frame.setTitle(getTitle(changed));
			}
		});
		viewModel.tableUpdateObserver.observe(new LiveObserver<Boolean>() {

			@Override
			public void onChanged(Boolean show) {
				if (show) {
					notifyDataSetChanged();
				}
			}
		});
		viewModel.saveSuccessObserver.observe(new LiveObserver<Boolean>() {

			@Override
			public void onChanged(Boolean success) {
				if (success) {
					frame.setTitle(getTitle(false));
					cbVersion.setSelected(false);
					cbDeleteStarOption.setSelected(false);
					
					// 清空输入框，会触发removeUpdate，所以要先删除listener
					textSearchField.getDocument().removeDocumentListener(mTextWatcher);
					textSearchField.setText("");
					textSearchField.getDocument().addDocumentListener(mTextWatcher);
				}
			}
		});
		viewModel.filterDataObserver.observe(new LiveObserver<List<TableItem>>() {

			@Override
			public void onChanged(List<TableItem> value) {
				tableAdapter.setTableItemList(value);
				tableCellRenderer.setDataList(value);
				notifyDataSetChanged();
			}
		});
		viewModel.tableDataObserver
				.observe(new LiveObserver<List<TableItem>>() {

					@Override
					public void onChanged(List<TableItem> value) {
						tableCellRenderer.setDataList(value);
						
						tableAdapter = new TableAdapter();
						tableAdapter.setColumnList(viewModel.getColumnList());
						tableAdapter.setTableItemList(value);
						tableAdapter.setOnDataChangedListener(new OnDataChangedListener() {
							
							@Override
							public void onDataChanged(String value, int row, int col) {
								viewModel.changeItemValue(value, row, col);
								viewModel.setFileChanged(true);
							}

						});
						table.setModel(tableAdapter);
						updateColumnWidth();
					}
				});

		if (new File(Conf.FILE_DB).exists()) {
			// 每次启动后自动备份到history目录下
			viewModel.backupHistoryDatabase();
			viewModel.loadTableData(viewModel.getSelectedSource(0));
		}
		else {
			showConfirm(Conf.FILE_DB + " 不存在");
		}
	}

	protected void showLoading(Boolean show) {
		// CardLayout只能显示一个子页面
		CardLayout layout = (CardLayout) frame.getContentPane().getLayout();
		if (show) {
			layout.show(frame.getContentPane(), CARD_LOADING);
		}
		else {
			layout.show(frame.getContentPane(), CARD_MAIN);
		}
	}

	protected void updateColumnWidth() {
		if (viewModel.getColumnList() != null) {
			TableColumnModel cm = table.getColumnModel();
			for (int i = 0; i < viewModel.getColumnList().size(); i++) {
				// 得到第i个列对象
				TableColumn column = cm.getColumn(i);
				// column.setMinWidth(preferedWidth);
				// column.setMaxWidth(maxWidth);
				column.setPreferredWidth(viewModel.getColumnList().get(i)
						.getWidth());
			}
			table.getTableHeader().setFont(new Font("Header", Font.PLAIN, 10));

			// 设置表头文字自动换行
			HeadRenderer renderer = new HeadRenderer();
			for (int i = 0; i < cm.getColumnCount(); i++) {
				cm.getColumn(i).setHeaderRenderer(renderer);
			}
			
			// listener
			table.getTableHeader().addMouseListener(new MouseClickListener() {
				
				@Override
				public void onClickRightMouse(MouseEvent event) {
					int position = table.columnAtPoint(event.getPoint());
					DebugLog.e("column " + position);
				}
				
				@Override
				public void onClickLeftMouse(MouseEvent event) {
					int position = table.columnAtPoint(event.getPoint());
					DebugLog.e("column " + position);
					viewModel.sortRecords(Constants.convert(position));
				}
			});
		}
	}
	
	private void showDeletePopup(final int row, MouseEvent event) {
		JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem openItem = new JMenuItem();
        openItem.setText("打开所在目录");
        openItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String dir = viewModel.getTableItem(row).getBean().getDirectory();
				try {
					FileUtil.openFile(dir);
				} catch (Exception e) {
					e.printStackTrace();
					showConfirm("打开" + dir + "失败");
				}
			}
		});
        popupMenu.add(openItem);
        
        JMenuItem delMenItem = new JMenuItem();
        if (viewModel.getTableItem(row).isDeleted()) {
            delMenItem.setText("取消删除");
		}
        else {
            delMenItem.setText("删除行");
		}
        delMenItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

            	// 取消删除
                if (viewModel.getTableItem(row).isDeleted()) {
                	viewModel.getTableItem(row).setDeleted(false);
    				notifyDataSetChanged();
                }
                else {
                	showConfirmCancel("确认删除？", "删除", new OnConfirmCancelListener() {
    					
    					@Override
    					public void onConfirm() {
    						viewModel.deleteItem(row);
    						notifyDataSetChanged();
    					}
    					
    					@Override
    					public void onCancel() {
    						
    					}
    				});
				}
            }
        });
        popupMenu.add(delMenItem);
        popupMenu.show(table, event.getX(), event.getY());
    }

	protected void showReadDatePopup(final int row, MouseEvent event) {
		JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem delMenItem = new JMenuItem();
        delMenItem.setText("读取文件最后修改时间");
        delMenItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	viewModel.readFileDate(row);
            }
        });
        popupMenu.add(delMenItem);
        popupMenu.show(table, event.getX(), event.getY());
    }

	protected void notifyDataSetChanged() {
		// 改变数据集中某个变量的属性后
		// 各种刷新措施都不完美，需要滑动列表或者点击其他单元格才刷新过来
//		tableAdapter.fireTableDataChanged();
		table.updateUI();
//		table.invalidate();
	}

	private String getTitle(boolean isChanged) {
		String title = "gdata.db  version(" + viewModel.getVersion() + ")";
		if (isChanged) {
			title = title + "*";
		}
		return title;
	}

	private void initLoading() {
		loadingPane = new JPanel();
		loadingPane.setLayout(null);
		frame.getContentPane().add(CARD_LOADING, loadingPane);
		
		lableLoading = new JLabel(new ImageIcon(R.drawable.loading));
		int margin = (ScreenUtil.getScreenWidth() - 800) / 2;
		lableLoading.setBounds(margin, R.dimen.table_top
				, 800, 600);
		loadingPane.add(lableLoading);
		loadingPane.setVisible(false);
	}

}
