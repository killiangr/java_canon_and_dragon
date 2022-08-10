package lab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class ScoreDAO {

	private Connection connection;
	
	public ScoreDAO()  {
		try {
			connection = DriverManager.getConnection("jdbc:derby:scoreDB;create=true");
			createTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void save(List<Score> scores) {
		
		try {
			PreparedStatement pstm = connection.prepareStatement("INSERT INTO SCORE (Score, Name) VALUES (?,?)");
			for (Score score: scores) {
				pstm.setInt(1, score.getScore());
				pstm.setString(2, score.getName());
				pstm.execute();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
public List<Score> load() {
	List<Score> result = new LinkedList<>();
		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * from SCORE");
			while(rs.next()) {
				result.add(new Score(rs.getInt("Score"), rs.getString("Name")));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
	private void createTable() throws SQLException {
		try {
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("CREATE TABLE SCORE( Id INT NOT NULL GENERATED ALWAYS AS IDENTITY,"
											+ "   Score INT NOT NULL,"
											+ "   Name VARCHAR(255) NOT NULL,"
											+ "   PRIMARY KEY (Id))");
		} catch (SQLException e) {
			if (!e.getSQLState().equals("X0Y32")) {
				throw e;
			}
		}
	}
	
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
