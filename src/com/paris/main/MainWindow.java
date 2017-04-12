package com.paris.main;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import db.DBManager;
import db.SubCategory;
import db.TopCategory;

public class MainWindow extends JFrame implements ItemListener, ActionListener{
	JPanel p_west, p_east, p_center;
	JPanel p_up, p_down;
	JTable table_up, table_down;
	JScrollPane scroll_up, scroll_down;
	
	//서쪽 영역
	Choice ch_top, ch_sub;
	JTextField t_name, t_price;
	Canvas can_west;
	JButton bt_regist;
	
	//동쪽 영역
	Canvas can_east;
	JTextField t_id, t_name2, t_price2;
	JButton bt_edit, bt_delete;
	
	//DB접속 관련
	DBManager manager;
	Connection con;
	
	//상위 카테고리 list
	ArrayList<TopCategory> topList = new ArrayList<TopCategory>();
	ArrayList<SubCategory> subList = new ArrayList<SubCategory>();
	
	//캔버스가 쓸 수 있도록
	BufferedImage  image;
	BufferedImage image2;
	
	//Table 모델 객체들
	UpModel upModel;
	DownModel downModel;
	
	//파일추져
	JFileChooser chooser;
	File file; //chooser로 가져온 파일
	
	
	
	public MainWindow() {
		p_west = new JPanel();
		p_east = new JPanel();
		p_center = new JPanel();
		p_up = new JPanel();
		p_down = new JPanel();
		table_up = new JTable();
		table_down = new JTable();
		scroll_up = new JScrollPane(table_up);
		scroll_down = new JScrollPane(table_down);
		
		ch_top = new Choice();
		ch_sub = new Choice();
		t_name = new JTextField(12);
		t_price = new JTextField(12);
		
		chooser = new JFileChooser("C:/html_workspace/images");
		
		
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
		bt_regist = new JButton("등록");
		
		can_east = new Canvas(){
			public void paint(Graphics g) {
				g.drawImage(image2, 0, 0, 135, 135, this);
			}
		};
		t_id = new JTextField(12);
		t_name2 = new JTextField(12);
		t_price2 = new JTextField(12);
		bt_edit = new JButton("수정");
		bt_delete = new JButton("삭제");
		
		ch_top.setPreferredSize(new Dimension(135, 20));
		ch_sub.setPreferredSize(new Dimension(135, 20));
		ch_top.add("▼ 상위카테고리 선택");
		ch_sub.add("▼ 하위카테고리 선택");
		can_west.setPreferredSize(new Dimension(135,135));
		can_east.setPreferredSize(new Dimension(135,135));
		
		p_west.add(ch_top);
		p_west.add(ch_sub);
		p_west.add(t_name);
		p_west.add(t_price);
		p_west.add(can_west);
		p_west.add(bt_regist);
	
		t_id.setEditable(false);
		p_east.add(t_id);
		p_east.add(t_name2);
		p_east.add(t_price2);
		p_east.add(can_east);
		p_east.add(bt_edit);
		p_east.add(bt_delete);
		
		//패널 색상주기
		p_west.setBackground(Color.PINK);
		p_east.setBackground(Color.PINK);
		p_up.setBackground(Color.WHITE);
		p_down.setBackground(Color.CYAN);
		
		//각 패널들의 크기 지정
		p_west.setPreferredSize(new Dimension(150,700));
		p_east.setPreferredSize(new Dimension(150,700));
		p_center.setPreferredSize(new Dimension(50,700));
		
		//센터에 그리드 적용하고 위 아래 구성
		p_center.setLayout(new GridLayout(2, 1));
		p_center.add(p_up);
		p_center.add(p_down);
		
		//p_up,down border주기
		p_up.setLayout(new BorderLayout());
		p_down.setLayout(new BorderLayout());
		
		//스크롤 부착
		p_up.add(scroll_up);
		p_down.add(scroll_down);
		
		add(p_west, BorderLayout.WEST);
		add(p_east, BorderLayout.EAST);
		add(p_center);
		
		//초이스와 리스너 연결
		ch_top.addItemListener(this);
		
		//버튼에 리스너 연결
		bt_regist.addActionListener(this);
		
		//캔버스에 마우스 리스너연결
		can_west.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				preView();
			}
		});
		
		//업 테이블과 리스너 연결(테이블은 클릭 안먹고 마우스로!)
		table_up.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				JTable table = (JTable)e.getSource();
				int row = table.getSelectedRow();
				int col=0;
				int subcategory_id=(int)table.getValueAt(row, col);
				downModel.getList(subcategory_id);	
				table_down.updateUI();
			}
		});
		
		//다운 테이블과 리스너 연결
		table_down.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = table_down.getSelectedRow();
				//이차원 벡터에 들어있는 벡터를 얻어오자! 이 벡터가 레코드니깐!
				Vector vec = downModel.data.get(row);
				getDetail(vec);				
			}
		});
		
		setSize(850, 700);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		init(); //데이터베이스 연결
		getTop(); //탑카테고리 얻어다놓기
		getUpList(); //위쪽 테이블 모델적용
		getDownList(); //아래 테이블 모델적용
	}
	
	//데이터 베이스 커넥션 얻기
	public void init(){		
		manager = DBManager.getInstance();
		con = manager.getConnection();
	}
	//최상위 카테고리 얻기
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
				topList.add(dto); //리스트에 탑재
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
	
	//위쪽 테이블 데이터 처리
	public void getUpList(){
		upModel = new UpModel(con);
		table_up.setModel(upModel);
		table_up.updateUI();
	}
	
	//아래 테이블 데이터 처리
	public void getDownList(){
		downModel = new DownModel(con);
		table_down.setModel(downModel);
		table_down.updateUI();
	}
	
	//하위 카테고리 구하기
	//바인드 변수
	public void getSub(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from subcategory where topcategory_id=?";
	
		try {
			pstmt = con.prepareStatement(sql);
			//바인드 변수값 지정
			int index=ch_top.getSelectedIndex();
			if(index-1>=0){
				TopCategory dto = topList.get(index-1);
				pstmt.setInt(1, dto.getTopcategory_id());
				
				System.out.println(sql);
				rs=pstmt.executeQuery();
				
				//담기전에 지우기
				subList.removeAll(subList);
				ch_sub.removeAll();
				
				//하위 카테고리 채우기!
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
		//하위 카테고리 구하기
		getSub();
	}
	
	/*-------------------------
	상품등록
	------------------------------- */
	public void regist(){
		PreparedStatement pstmt = null;
		String sql="insert into product(product_id, subcategory_id, product_name, price, img)";
		sql+= "values(seq_product.nextval,?,?,?,?)";
		
		try {
			pstmt=con.prepareStatement(sql);
			int sub_id=subList.get(ch_sub.getSelectedIndex()).getSubcategory_id();//선택된 서브아이디 선택
			String product_name=t_name.getText(); //입력한 상품명
			int product_price=0;
			try {
				product_price = Integer.parseInt(t_price.getText()); //입력한 가격
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "가격은 숫자만 입력해주세요!");
				e.printStackTrace();
			}
			String img = file.getName();
			pstmt.setInt(1, sub_id);
			pstmt.setString(2, product_name);
			pstmt.setInt(3, product_price);
			pstmt.setString(4, img);
			
			//insert문의 경우 성공했다면 무조건 1, update문, delete문은 여러개 가능
			int result = pstmt.executeUpdate();
			if(result!=0){
				JOptionPane.showMessageDialog(this, "등록성공");
				upModel.getList(); // db를 새로 가져와서 이차원 벡터 변경
				table_up.updateUI();
				//이미지 파일 복사
				copy();
				
			}else{
				JOptionPane.showMessageDialog(this, "등록실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//캔버스에 이미지 반영하기
	public void preView(){
		int result = chooser.showOpenDialog(this);
		if(result==JFileChooser.APPROVE_OPTION){
			//캔버스에 이미지 그리자!
			file = chooser.getSelectedFile();
			//얻어진 파일을 기존의 이미지로 대체하여 다시 그리기만 하면 될듯~!
			try {
				image=ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			can_west.repaint();
		}
	}
	
	//복사 메서드 정의
	public void copy(){
		FileInputStream fis=null;
		FileOutputStream fos=null;
		
		try {
			fis = new FileInputStream(file);
			fos = new FileOutputStream("C:/java_workspace2/BreadProject/data/"+file.getName());
			
			byte[] b = new byte[1024];
			
			int flag;
			while(true){
				flag=fis.read(b);
			if(flag==-1) break;
				fos.write(b);
			}
			System.out.println("이미지복사완료");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
	}
	
	//상세정보 보여주기
	public void getDetail(Vector vec){
		t_id.setText(vec.get(0).toString());
		t_name2.setText(vec.get(2).toString());
		t_price2.setText(vec.get(3).toString());
		try {
			image2=ImageIO.read(new File("C:/java_workspace2/BreadProject/data/"+vec.get(4)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		can_east.repaint();
		
	}
	

	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		if(obj==bt_regist){
			regist();
		}
		
	}

	public static void main(String[] args) {
		new MainWindow();

	}

}
