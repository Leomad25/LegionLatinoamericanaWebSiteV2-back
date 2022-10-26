package com.api.LegionLatinoamericanaWebSiteV2back.models.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseMappingInt<K> {
    List<K> getFromResult(ResultSet resultSet) throws SQLException;
}
