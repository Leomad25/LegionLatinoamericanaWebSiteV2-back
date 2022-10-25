package com.api.LegionLatinoamericanaWebSiteV2back.models.implement.database;

import com.api.LegionLatinoamericanaWebSiteV2back.models.interfaces.DatabaseMappingInt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetUserIdByName implements DatabaseMappingInt<GetUserIdByName> {
    private int id;

    public GetUserIdByName() {}

    public GetUserIdByName(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public List<GetUserIdByName> getFromResult(ResultSet resultSet) throws SQLException {
        List<GetUserIdByName> list = new ArrayList<>();
        while (resultSet.next()) list.add(
                new GetUserIdByName(resultSet.getInt("RESULT"))
        );
        return list;
    }
}
