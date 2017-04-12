/* ���� ī�װ��� �� ī�װ��� ��ϵ� ��ǰ�� �� ������ �����ϴ� �� */

package com.paris.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class UpModel extends AbstractTableModel {
	Vector<String> columnName = new Vector<String>();
	Vector<Vector> data = new Vector<Vector>();
	Connection con;
	
	//���̺� �� ��ü������ �����͸� �޾ƿ��� ������⿡ con�� �޾ƿ;���
	public UpModel(Connection con ) {
		this.con = con;
		getList();
		
	}

	// ��ϰ�������
	public void getList() {
		StringBuffer sql = new StringBuffer();
		sql.append("select s.subcategory_id as subcategory_id, sub_name as ī�װ���,");
		sql.append(" count(product_name) as ����");
		sql.append(" from subcategory s left outer join product p");
		sql.append(" on s.subcategory_id=p.subcategory_id");
		sql.append(" group by s.subcategory_id,sub_name");
		sql.append(" order by s.subcategory_id asc");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			
			
			//���͵� �ʱ�ȭ
			columnName.removeAll(columnName);
			data.removeAll(data);
			
			ResultSetMetaData meta=rs.getMetaData();
			for(int i=1; i<=meta.getColumnCount();i++){
				columnName.add(meta.getColumnName(i));				
			}
			
			while(rs.next()){
				//���ڵ� �Ѱ��� vector�� �Ű� ����! ���⼭ ���ʹ� DTO����..
				Vector vec = new Vector(); //JTable�� �� �Ŵ� String���� �ص���
				vec.add(rs.getInt("subcategory_id"));
				vec.add(rs.getString("ī�װ���"));
				vec.add(rs.getInt("����"));
				
				data.add(vec);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	public int getColumnCount() {
		return columnName.size();
	}
	
	public String getColumnName(int col) {
		return columnName.get(col);
	}

	public int getRowCount() {
		return data.size();
	}

	public Object getValueAt(int row, int col) {
		return data.get(row).get(col);
	}

}
