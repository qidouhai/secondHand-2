package cn.chenny3.secondHand.common.typeHandler;

import cn.chenny3.secondHand.common.bean.enums.RoleType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@MappedTypes({RoleType.class})
@MappedJdbcTypes({JdbcType.INTEGER})
public class EnumTypeHandler extends BaseTypeHandler<RoleType>{
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, RoleType roleType, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i,roleType.getValue());
    }

    @Override
    public RoleType getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        int intValue = resultSet.getInt(columnName);
        return RoleType.getRole(intValue);
    }

    @Override
    public RoleType getNullableResult(ResultSet resultSet, int index) throws SQLException {
        int intValue = resultSet.getInt(index);
        return RoleType.getRole(intValue);
    }

    @Override
    public RoleType getNullableResult(CallableStatement callableStatement, int index) throws SQLException {
        int intValue = callableStatement.getInt(index);
        return RoleType.getRole(intValue);
    }
}
