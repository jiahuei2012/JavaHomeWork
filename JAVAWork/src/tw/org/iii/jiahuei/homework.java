package tw.org.iii.jiahuei;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

public class homework {
	
	private static JFrame frm = new JFrame("myFramWork");
	private static JTextArea ta = new JTextArea();
	private static JPanel contentPane = new JPanel();

	public static void main(String[] args) {
		frm.setLayout(null);
		frm.setSize(800,500);
		contentPane.setBounds(0, 150, 770, 350);
		
		ta.setBounds(0,0,600,150);
		ta.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		//�w�]�d�ߦr��
		ta.setText("SELECT A.OrderID,A.OrderDate,A.CustomerID,B.ProductID,C.ProductName,B.Quantity, B.UnitPrice, \r\n" + 
				"                B.Quantity*B.UnitPrice �p�p\r\n" + 
				"FROM ORDERS A \r\n" + 
				"LEFT OUTER JOIN [Order Details] B ON A.OrderID = B.OrderID\r\n" + 
				"LEFT OUTER JOIN PRODUCTS C ON B.ProductID = C.ProductID\r\n" + 
				"ORDER BY 1");
		frm.add(ta);
		
		JButton btn = new JButton("�d��");
		btn.setBounds(620, 50, 100, 50);
		btn.addActionListener(new btnListener());
		frm.add(btn);
		
		frm.add(contentPane);
		frm.setVisible(true);
	}
	
	
	public static class btnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JTable jt= getSqlData();
		    jt.setPreferredScrollableViewportSize(new Dimension(750,270));
		    changeView(jt);		    
		}
		
	}
	
	private static JTable getSqlData() {
		//Sql�s���r��
		String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
					            "database=Northwind;" +
					            "user=sa;" +
					            "password=as;" ;
		//���oSql���O
		String selectSql = ta.getText();
		//�s�����W�ٸ��
		List<String> columnNames = new ArrayList<String>();
		//�s����줺�e���
		List<Object[]> columnDatas = new ArrayList<Object[]>();
		try (Connection connection = DriverManager.getConnection(connectionUrl);
				 Statement statement = connection.createStatement();) {
			//����Sql���O
			ResultSet resultSet = statement.executeQuery(selectSql);
			ResultSetMetaData metadata = resultSet.getMetaData(); 
			int columns = metadata.getColumnCount();
			//���o���W�ٸ��
			for(int i = 1; i <= columns; i++) {				
				columnNames.add(metadata.getColumnName(i));
			}
			//���o�Ҧ����
			while (resultSet.next()) {
				List<Object> rowdata = new ArrayList<Object>();
				for(int i = 1; i <= columns; i++) {
					rowdata.add(resultSet.getString(i));
				}
				columnDatas.add(rowdata.toArray());
			}
			//�^��JTable�������
			return new JTable(columnDatas.toArray(new Object[columnDatas.size()][columnDatas.get(1).length]),
					          columnNames.toArray(new String[columnNames.size()]));
		}
		catch(SQLException e) {
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.toString());
			return new JTable(new Object[0][0],new Object[0]);
		}

	}
	
	private static void changeView(JTable jt) {
		contentPane.removeAll();
	    contentPane.add(new JScrollPane(jt),BorderLayout.CENTER);
	    
	    frm.invalidate();
	    frm.validate();
	    frm.repaint();
	}
	
}
