/*
 * ����ī�װ��� ��ϵ� ��ǰ ���� ���� ��
 * */
package com.paris.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class DownModel extends AbstractTableModel{
	Vector<String> columnName =new Vector<String>();
	Vector<Vector> data = new Vector<Vector>();
	Connection con;
	
	public DownModel(Connection con) {
		this.con=con;
		columnName.add("product_id");
		columnName.add("subcategory_id");
		columnName.add("product_name");
		columnName.add("price");
		columnName.add("img");
	}
	
	//���콺�� ������ Ŭ���Ҷ����� id ���� �ٲ�Ƿ� �Ʒ��� �޼��带 �׶����� ȣ������!
	public void getList(int subcategory_id){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from product";
		sql+=" where subcategory_id=?";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, subcategory_id);
			rs = pstmt.executeQuery();
			
			//���͵� �ʱ�ȭ
			columnName.removeAll(columnName);
			data.removeAll(data);
			
			System.out.println("getList �÷��� ũ��� "+columnName.size());
			
			while(rs.next()){
				Vector vec = new Vector();
				vec.add(rs.getInt("product_id"));
				vec.add(rs.getInt("subcategory_id"));
				vec.add(rs.getString("product_name"));
				vec.add(rs.getString("price"));
				vec.add(rs.getString("img"));
				
				data.add(vec);
			}
			System.out.println("getList ���ڵ��� ũ��� "+data.size());
			
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
	

	public String getColumnName(int col) {
		System.out.println("getColumnName ȣ��"+columnName.get(col));
		return columnName.get(col);
	}

	public int getColumnCount() {
		System.out.println("getColumnCount ȣ��"+columnName.size());
		return columnName.size();
	}


	public int getRowCount() {
		System.out.println("getRowCount ȣ��"+data.size());
		return data.size();
	}


	public Object getValueAt(int row, int col) {
		Object value = data.get(row).get(col);
		System.out.println("getValueAt ȣ��"+value);
		return data.get(row).get(col);
	}

}
