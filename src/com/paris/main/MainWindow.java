package com.paris.main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import db.DBManager;
import db.SubCategory;
import db.TopCategory;

public class MainWindow extends JFrame implements ItemListener{
	JPanel p_west, p_east, p_center;
	JPanel p_up, p_down;
	JTable table_up, table_down;
	JScrollPane scroll_up, scroll_down;
	
	//���� ����
	Choice ch_top, ch_sub;
	JTextField t_name, t_price;
	Canvas can_west;
	JButton bt_regist;
	
	//���� ����
	Canvas can_east;
	JTextField t_name2, t_price2;
	JButton bt_edit, bt_delete;
	
	//DB���� ����
	DBManager manager;
	Connection con;
	
	//���� ī�װ� list
	ArrayList<TopCategory> topList = new ArrayList<TopCategory>();
	ArrayList<SubCategory> subList = new ArrayList<SubCategory>();
	
	//ĵ������ �� �� �ֵ���
	BufferedImage  image;
	
	
	public MainWindow() {
		p_west = new JPanel();
		p_east = new JPanel();
		p_center = new JPanel();
		p_up = new JPanel();
		p_down = new JPanel();
		table_up = new JTable(3,5);
		table_down = new JTable(3,5);
		scroll_up = new JScrollPane(table_up);
		scroll_down = new JScrollPane(table_down);
		
		ch_top = new Choice();
		ch_sub = new Choice();
		t_name = new JTextField(12);
		t_price = new JTextField(12);
		
		
		try {
			image = ImageIO.read(this.getClass().getResource("/default.png"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		can_west = new Canvas(){
			public void paint(Graphics g) {
				
				g.drawImage(image, 0, 0, 135, 135, this);
			}
		};
		bt_regist = new JButton("���");
		
		can_east = new Canvas(){
			public void paint(Graphics g) {
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, 140, 140);
			}
		};
		t_name2 = new JTextField(12);
		t_price2 = new JTextField(12);
		bt_edit = new JButton("����");
		bt_delete = new JButton("����");
		
		ch_top.setPreferredSize(new Dimension(135, 20));
		ch_sub.setPreferredSize(new Dimension(135, 20));
		ch_top.add("�� ����ī�װ� ����");
		ch_sub.add("�� ����ī�װ� ����");
		can_west.setPreferredSize(new Dimension(135,135));
		can_east.setPreferredSize(new Dimension(135,135));
		
		p_west.add(ch_top);
		p_west.add(ch_sub);
		p_west.add(t_name);
		p_west.add(t_price);
		p_west.add(can_west);
		p_west.add(bt_regist);
		
		p_east.add(can_east);
		p_east.add(t_name2);
		p_east.add(t_price2);
		p_east.add(bt_edit);
		p_east.add(bt_delete);
		
		//�г� �����ֱ�
		p_west.setBackground(Color.PINK);
		p_east.setBackground(Color.PINK);
		p_up.setBackground(Color.WHITE);
		p_down.setBackground(Color.CYAN);
		
		//�� �гε��� ũ�� ����
		p_west.setPreferredSize(new Dimension(150,700));
		p_east.setPreferredSize(new Dimension(150,700));
		p_center.setPreferredSize(new Dimension(50,700));
		
		//���Ϳ� �׸��� �����ϰ� �� �Ʒ� ����
		p_center.setLayout(new GridLayout(2, 1));
		p_center.add(p_up);
		p_center.add(p_down);
		
		//p_up,down border�ֱ�
		p_up.setLayout(new BorderLayout());
		p_down.setLayout(new BorderLayout());
		
		//��ũ�� ����
		p_up.add(scroll_up);
		p_down.add(scroll_down);
		
		add(p_west, BorderLayout.WEST);
		add(p_east, BorderLayout.EAST);
		add(p_center);
		
		//���̽��� ������ ����
		ch_top.addItemListener(this);
		
		setSize(850, 700);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		init(); //�����ͺ��̽� ����
		getTop(); //žī�װ� ���ٳ���
	}
	
	//������ ���̽� Ŀ�ؼ� ���
	public void init(){		
		manager = DBManager.getInstance();
		con = manager.getConnection();
	}
	//�ֻ��� ī�װ� ���
	public void getTop(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from topcategory order by topcategory_id asc";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				TopCategory dto = new TopCategory();
				dto.setTopcategory_id(rs.getInt("topcategory_id"));
				dto.setTop_name(rs.getString("top_name"));
				topList.add(dto); //����Ʈ�� ž��
				ch_top.add(dto.getTop_name());
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//���� ī�װ� ���ϱ�
	//���ε� ����
	public void getSub(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from subcategory where topcategory_id=?";
	
		try {
			pstmt = con.prepareStatement(sql);
			//���ε� ������ ����
			int index=ch_top.getSelectedIndex();
			if(index-1>=0){
				TopCategory dto = topList.get(index-1);
				pstmt.setInt(1, dto.getTopcategory_id());
				
				System.out.println(sql);
				rs=pstmt.executeQuery();
				
				//������� �����
				subList.removeAll(subList);
				ch_sub.removeAll();
				
				//���� ī�װ� ä���!
				while(rs.next()){
					SubCategory vo = new SubCategory();
					vo.setSubcategory_id(rs.getInt("subcategory_id"));
					vo.setTopcategory_id(rs.getInt("topcategory_id"));
					vo.setSub_name(rs.getString("sub_name"));
					subList.add(vo);
					ch_sub.add(vo.getSub_name());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
		//���� ī�װ� ���ϱ�
		getSub();
				
	}

	public static void main(String[] args) {
		new MainWindow();

	}

}
