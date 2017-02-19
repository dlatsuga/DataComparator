package model.dao;

import model.domain.DataBaseTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DaoDataBaseTableImpl implements DaoDataBaseTable {
    Connection conn;

    public DaoDataBaseTableImpl(Connection conn) {
        this.conn = conn;
    }


    public DataBaseTable findTableByName(String schema, String tableName) {
        /*String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = ? and table_name = ?";*/
        String sql =
                "        Select\n" +
                        "        tt.table_schema\n" +
                        "                ,tt.table_name\n" +
                        "                ,count(cc.column_name) fields_cnt\n" +
                        "        from information_schema.tables tt\n" +
                        "        left join information_schema.columns cc on tt.table_schema = cc.table_schema\n" +
                        "        and tt.table_name = cc.table_name\n" +
                        "        where tt.table_schema = ? and tt.table_name = ?\n" +
                        "        group by tt.table_schema, tt.table_name";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1,schema);
            statement.setString(2,tableName);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next() ;
            DataBaseTable dataBaseTable = new DataBaseTable(schema, tableName, resultSet.getInt(3));
            return dataBaseTable;
        } catch (SQLException e) {
            return null;
        }
    }

    public List<DataBaseTable> findAllTablesOfVnT() {
        return null;
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
