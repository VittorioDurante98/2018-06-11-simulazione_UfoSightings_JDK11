package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.ufo.model.AnnoAvvistamento;
import it.polito.tdp.ufo.model.Arco;
import it.polito.tdp.ufo.model.Sighting;

public class SightingsDAO {
	
	public List<Sighting> getSightings() {
		String sql = "SELECT * FROM sighting" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Sighting> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(new Sighting(res.getInt("id"),
						res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), 
						res.getString("state"), 
						res.getString("country"),
						res.getString("shape"),
						res.getInt("duration"),
						res.getString("duration_hm"),
						res.getString("comments"),
						res.getDate("date_posted").toLocalDate(),
						res.getDouble("latitude"), 
						res.getDouble("longitude"))) ;
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<AnnoAvvistamento> getAnnoAvvistamento() {
		String sql= "SELECT DISTINCT (YEAR(DATETIME)) AS years, COUNT(*) AS tot FROM sighting WHERE country='us' GROUP BY YEAR(DATETIME) ";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<AnnoAvvistamento> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				list.add(new AnnoAvvistamento(res.getInt("years"), res.getInt("tot")));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getStati(int anno) {
		String sql= "SELECT DISTINCT(state) FROM sighting WHERE country='us' AND YEAR(DATETIME)=?";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				list.add(res.getString("state"));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Arco> getArchi(int anno) {
		String sql= "SELECT s1.state, s2.state " + 
				"FROM (SELECT DISTINCT(state), DATETIME, id FROM sighting WHERE country='us' AND YEAR(DATETIME)=?) AS s1, (SELECT DISTINCT(state), DATETIME, id FROM sighting WHERE country='us' AND YEAR(DATETIME)=?) AS s2 " + 
				"WHERE s1.state != s2.state AND s1.id!=s2.id  AND s1.datetime < s2.datetime " + 
				"GROUP BY s1.state, s2.state";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setInt(2, anno);
			List<Arco> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				list.add(new Arco(res.getString("s1.state"), res.getString("s2.state")));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}


}
