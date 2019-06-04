package com.king.desk.gdata;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.king.desk.gdata.adapter.HeadRenderer;
import com.king.desk.gdata.adapter.StarAdapter;
import com.king.desk.gdata.adapter.StarRecordsAdapter;
import com.king.desk.gdata.model.FileUtil;
import com.king.desk.gdata.model.ScreenUtil;
import com.king.desk.gdata.model.live.LiveObserver;
import com.king.desk.gdata.view.IndexView;
import com.king.desk.gdata.view.JImagePane;
import com.king.desk.gdata.view.MouseClickListener;
import com.king.desk.gdata.viewmodel.StarViewModel;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.Star;

import javax.swing.JButton;

import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class StarActivity extends BaseActivity {

	private JFrame frame;
	
	private JImagePane imagePane;
	private JImagePane recImgPane1;
	private JImagePane recImgPane2;
	
	private JLabel nameLable;
	private JLabel idLable;
	private JLabel lblVideos;
	private JButton btnCount;
	private JButton btnDelete;
	private JList<String> starList;
	
	private StarViewModel viewModel;
	private JTable table;
	
	private StarRecordsAdapter recordsAdapter;
	private StarAdapter starAdapter;
	
	private boolean mSelectMode;
	private OnSelectListener onSelectListener;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new StarActivity();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public interface OnSelectListener {
		void onSelect(Star star);
	}

	public StarActivity(boolean selectMode, OnSelectListener listener) {
		mSelectMode = selectMode;
		onSelectListener = listener;
		viewModel = new StarViewModel();
		initView();
		initObservers();
		viewModel.loadCoverImage();
		viewModel.loadStars();
	}
	/**
	 * Create the application.
	 */
	public StarActivity() {
		this(false, null);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initView() {
		frame = new JFrame();
		frame.getContentPane().setLayout(null);
		frame.setTitle("Actors");
		int width = 820;
		int height = 890;
		int left = (ScreenUtil.getScreenWidth() - width) / 2;
		int top = (ScreenUtil.getScreenHeight() - height) / 2;
		frame.setBounds(left, top, width, height);
		// EXIT_ON_CLOSE关闭全部窗体，DISPOSE_ON_CLOSE关闭当前窗体
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);

		IndexView indexView = new IndexView();
		indexView.setOnCharSelectListener(new IndexView.OnCharSelectListener() {
			
			@Override
			public void onSelectChar(char index) {
				int position = viewModel.getCharPosition(index);
				if (position != -1) {
					starList.ensureIndexIsVisible(position);
				}
			}
		});
		indexView.create(frame.getContentPane(), 28, 25, 20, 813);

		starList = new JList<>();
		starList.setBounds(48, 25, 190, 813);
		starList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		starList.addMouseListener(new MouseClickListener() {

			@Override
			protected void onDoubleClick(MouseEvent event) {
				if (mSelectMode && onSelectListener != null) {
					int index = starList.locationToIndex(event.getPoint());
					onSelectListener.onSelect(viewModel.getStar(index));
					frame.dispose();
				}
			}
			
			@Override
			public void onClickRightMouse(MouseEvent event) {
				int index = starList.locationToIndex(event.getPoint());
				showStarPopup(index, event);
			}
			
			@Override
			public void onClickLeftMouse(MouseEvent event) {
				btnCount.setVisible(true);
				btnDelete.setVisible(false);
				int index = starList.getSelectedIndex();
				System.out.println("index " + index);
				nameLable.setText(viewModel.getStarName(index));
				idLable.setText("ID " + viewModel.getStar(index).getId());
				viewModel.loadVideoInfo(index);
				viewModel.loadStarImage(index);
				viewModel.loadStarRecords(index);
			}
		});
		frame.getContentPane().add(starList);
		JScrollPane s = new JScrollPane(starList);
//	    s.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    s.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		s.setBounds(starList.getBounds().x, starList.getBounds().y, starList.getBounds().width, starList.getBounds().height);
		frame.getContentPane().add(s);
		
		imagePane = new JImagePane(JImagePane.SCALED);
		imagePane.setBounds(268, 25, 520, 300);
		frame.getContentPane().add(imagePane);
		
		nameLable = new JLabel("Star Name");
		nameLable.setFont(new Font("Header", Font.BOLD, 26));
		nameLable.setBounds(268, 340, 402, 35);
		frame.getContentPane().add(nameLable);
		
		lblVideos = new JLabel("Video Info");
		lblVideos.setBounds(268, 385, 277, 18);
		frame.getContentPane().add(lblVideos);
		
		btnCount = new JButton("Re-Count");
		btnCount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				viewModel.recount(starList.getSelectedIndex());
			}
		});
		btnCount.setBounds(675, 381, 113, 27);
		btnCount.setVisible(false);
		frame.getContentPane().add(btnCount);
		
		table = new JTable();
		table.setBounds(268, 427, StarRecordsAdapter.TABLE_WIDTH, 231);
		// 禁止每个单元格宽度均分
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		frame.getContentPane().add(table);
		// 只对单元格有效，表头的事件通过后面updateColumnWidth里的方法实现
		table.addMouseListener(new MouseClickListener() {
			
			@Override
			public void onClickRightMouse(MouseEvent event) {
				int row = table.rowAtPoint(event.getPoint());
				int column = table.columnAtPoint(event.getPoint());
				if (table.isRowSelected(row)) {
					showPopup(row, event);
				}
			}
			
			@Override
			public void onClickLeftMouse(MouseEvent event) {
				int row = table.rowAtPoint(event.getPoint());
				int column = table.columnAtPoint(event.getPoint());
				viewModel.loadRecordImages(row);
			}
		});
		JScrollPane tableScrollPane = new JScrollPane(table);
		tableScrollPane.setBounds(table.getBounds().x, table.getBounds().y, table.getBounds().width, table.getBounds().height);
		frame.getContentPane().add(tableScrollPane);
		recordsAdapter = new StarRecordsAdapter();
		table.setModel(recordsAdapter);
		
		updateColumnWidth();
		
		recImgPane1 = new JImagePane(JImagePane.SCALED);
		recImgPane1.setBounds(268, 671, 260, 161);
		frame.getContentPane().add(recImgPane1);

		recImgPane2 = new JImagePane(JImagePane.SCALED);
		recImgPane2.setBounds(528, 671, 260, 161);
		frame.getContentPane().add(recImgPane2);
		
		idLable = new JLabel("id");
		idLable.setBounds(716, 352, 72, 18);
		frame.getContentPane().add(idLable);
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				viewModel.deleteStar(starList.getSelectedIndex());
			}
		});
		btnDelete.setBounds(375, 381, 113, 27);
		frame.getContentPane().add(btnDelete);
		btnDelete.setVisible(false);
	}

	protected void showPopup(final int row, MouseEvent event) {
		JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem openItem = new JMenuItem();
        openItem.setText("打开所在目录");
        openItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String dir = viewModel.getRecordDirectory(row);
				try {
					FileUtil.openFile(dir);
				} catch (Exception e) {
					e.printStackTrace();
					showConfirm("打开" + dir + "失败");
				}
			}
		});
        popupMenu.add(openItem);
        popupMenu.show(table, event.getX(), event.getY());
	}

	protected void showStarPopup(final int position, MouseEvent event) {
		JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem openItem = new JMenuItem();
        openItem.setText("复制名字");
        openItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = viewModel.getStarName(position);
				StringSelection selection = new StringSelection(name);
				frame.getToolkit().getSystemClipboard().setContents(selection, null);
			}
		});
        popupMenu.add(openItem);
        popupMenu.show(starList, event.getX(), event.getY());
	}

	private void initObservers() {
		viewModel.messageObserver.observe(new LiveObserver<String>() {
			
			@Override
			public void onChanged(String value) {
				showConfirm(value);
			}
		});
		viewModel.starsObserver.observe(new LiveObserver<List<Star>>() {

			@Override
			public void onChanged(List<Star> value) {
				btnDelete.setVisible(false);
				if (starAdapter == null) {
					starAdapter = new StarAdapter();
					starAdapter.setList(value);
					starList.setModel(starAdapter);
				}
				else {
					starAdapter.setList(value);
					starList.updateUI();
				}
			}
		});
		viewModel.imageObserver.observe(new LiveObserver<Image>() {

			@Override
			public void onChanged(Image value) {
				imagePane.setBackgroundImage(value);
			}
		});
		viewModel.rec1ImageObserver.observe(new LiveObserver<Image>() {

			@Override
			public void onChanged(Image value) {
				recImgPane1.setBackgroundImage(value);
			}
		});
		viewModel.rec2ImageObserver.observe(new LiveObserver<Image>() {

			@Override
			public void onChanged(Image value) {
				recImgPane2.setBackgroundImage(value);
			}
		});
		viewModel.videoInfoObserver.observe(new LiveObserver<String>() {
			
			@Override
			public void onChanged(String value) {
				lblVideos.setText(value);
			}
		});
		viewModel.noVideoObserver.observe(new LiveObserver<Boolean>() {
			
			@Override
			public void onChanged(Boolean value) {
				btnDelete.setVisible(true);
			}
		});
		viewModel.recordsObserver.observe(new LiveObserver<List<Record>>() {

			@Override
			public void onChanged(List<Record> value) {
				recordsAdapter.setList(value);
				table.updateUI();
			}
		});
	}

	protected void updateColumnWidth() {
		TableColumnModel cm = table.getColumnModel();
		for (int i = 0; i < 4; i++) {
			// 得到第i个列对象
			TableColumn column = cm.getColumn(i);
			// column.setMinWidth(preferedWidth);
			// column.setMaxWidth(maxWidth);
			column.setPreferredWidth(recordsAdapter.getColumnWidth(i));
		}
		table.getTableHeader().setFont(new Font("Header", Font.PLAIN, 10));

		// 设置表头文字自动换行
		HeadRenderer renderer = new HeadRenderer();
		for (int i = 0; i < cm.getColumnCount(); i++) {
			cm.getColumn(i).setHeaderRenderer(renderer);
		}
		
		// listener
//		table.getTableHeader().addMouseListener(new MouseClickListener() {
//			
//			@Override
//			public void onClickRightMouse(MouseEvent event) {
//				int position = table.columnAtPoint(event.getPoint());
//				DebugLog.e("column " + position);
//			}
//			
//			@Override
//			public void onClickLeftMouse(MouseEvent event) {
//				int position = table.columnAtPoint(event.getPoint());
//				DebugLog.e("column " + position);
//			}
//		});
	}
}
