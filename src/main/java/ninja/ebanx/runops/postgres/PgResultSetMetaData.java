package ninja.ebanx.runops.postgres;

import ninja.ebanx.runops.RunopsConnection;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

public class PgResultSetMetaData implements ResultSetMetaData {

    private final Field[] fields;

    public PgResultSetMetaData(String[] fields) {
        this.fields = new Field[fields.length];
        for (var i = 0; i < fields.length; i++) {
            this.fields[i] = new Field(fields[i], Oid.VARCHAR);
            this.fields[i].setSQLType(Types.VARCHAR);
        }
    } 

    @Override
    public int getColumnCount() {
        return fields.length;
    }

    @Override
    public boolean isAutoIncrement(int column) {
        return false;
    }

    @Override
    public boolean isCaseSensitive(int column) throws SQLException {
        return false;
    }

    @Override
    public boolean isSearchable(int column) throws SQLException {
        return false;
    }

    @Override
    public boolean isCurrency(int column) throws SQLException {
        return false;
    }

    @Override
    public int isNullable(int column) throws SQLException {
        return columnNullable;
    }

    @Override
    public boolean isSigned(int column) throws SQLException {
        return false;
    }

    @Override
    public int getColumnDisplaySize(int column) throws SQLException {
        return 10;
    }

    @Override
    public String getColumnLabel(int column) {
        return fields[column-1].getColumnLabel();
    }

    @Override
    public String getColumnName(int column) {
        return fields[column-1].getColumnLabel();
    }

    @Override
    public String getSchemaName(int column) {
        return "";
    }

    @Override
    public int getPrecision(int column) throws SQLException {
        return 0;
    }

    @Override
    public int getScale(int column) throws SQLException {
        return 0;
    }

    @Override
    public String getTableName(int column) {
        return "";
    }

    @Override
    public String getCatalogName(int column) {
        return "";
    }

    @Override
    public int getColumnType(int column) {
        return fields[column-1].getSQLType();
    }

    @Override
    public String getColumnTypeName(int column) {
        return fields[column-1].getPGType();
    }

    @Override
    public boolean isReadOnly(int column) {
        return true;
    }

    @Override
    public boolean isWritable(int column) {
        return false;
    }

    @Override
    public boolean isDefinitelyWritable(int column) {
        return false;
    }

    @Override
    public String getColumnClassName(int column) throws SQLException {
        return PGobject.class.getName();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
