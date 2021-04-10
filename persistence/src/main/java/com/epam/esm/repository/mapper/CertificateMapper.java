package com.epam.esm.repository.mapper;

import com.epam.esm.model.Certificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class CertificateMapper implements RowMapper<Certificate> {

    @Override
    public Certificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        int price = rs.getInt("price");
        int duration = rs.getInt("duration");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate createDate = rs.getObject("create_date", LocalDate.class);
        LocalDate lastUpdateDate = rs.getObject("last_update_date", LocalDate.class);

        return new Certificate.Builder()
                .setId(id)
                .setName(name)
                .setDescription(description)
                .setDuration(duration)
                .setPrice(price)
                .setCreateDate(createDate)
                .setLastUpdateDate(lastUpdateDate)
                .build();
    }
}
