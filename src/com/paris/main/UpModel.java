/* 하위 카테고리와 그 카테고리에 등록된 상품의 수 정보를 제공하는 모델 */

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
	
	//테이블 모델 자체적으로 데이터를 받아오록 만들었기에 con도 받아와야함
	public UpModel(Connection con ) {
		this.con = con;
		getList();
		
	}

	// 목록가져오기
	public void getList() {
		StringBuffer sql = new StringBuffer();
		sql.append("select s.subcategory_id as subcategory_id, sub_name as 카테고리명,");
		sql.append(" count(product_name) as 갯수");
		sql.append(" from subcategory s left outer join product p");
		sql.append(" on s.subcategory_id=p.subcategory_id");
		sql.append(" group by s.subcategory_id,sub_name");
		sql.append(" order by s.subcategory_id asc");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			
			
			//벡터들 초기화
			columnName.removeAll(columnName);
			data.removeAll(data);
			
			ResultSetMetaData meta=rs.getMetaData();
			for(int i=1; i<=meta.getColumnCount();i++){
				columnName.add(meta.getColumnName(i));				
			}
			
			while(rs.next()){
				//레코드 한건을 vector에 옮겨 심자! 여기서 백터는 DTO역할..
				Vector vec = new Vector(); //JTable에 들어갈 거는 String으로 해도됨
				vec.add(rs.getInt("subcategory_id"));
				vec.add(rs.getString("카테고리명"));
				vec.add(rs.getInt("갯수"));
				
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
