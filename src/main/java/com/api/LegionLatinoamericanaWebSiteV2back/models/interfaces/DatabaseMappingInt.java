package com.api.LegionLatinoamericanaWebSiteV2back.models.interfaces;

import com.api.LegionLatinoamericanaWebSiteV2back.models.implement.database.GetUserIdByName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseMappingInt<K> {
    List<K> getFromResult(ResultSet resultSet) throws SQLException;
}
