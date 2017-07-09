package com.globosmart.productcalalogue.dao;

import com.globosmart.productcalalogue.domain.Product;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ProductDao {

    private NamedParameterJdbcTemplate jdbcTemplate;

    private static Map<String, String> whereClauses = new HashMap<>();

    static {
        whereClauses.put("countryCode", "C.COUNTRY_CODE=:countryCode");
        whereClauses.put("type", "PT.NAME=:type");
        whereClauses.put("name", "P.NAME=:name");
        whereClauses.put("id", "P.ID=:id");
    }

    @Autowired
    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
    }

    public List<Product> getProducts(Map<String, String> filters) {
        StringBuilder sql = new StringBuilder("select P.ID,P.NAME,P.DISPLAY_NAME, C.COUNTRY_CODE, PT.NAME as TYPE from PRODUCT P")
                .append(" JOIN PRODUCT_COUNTRY_XREF CPX ON CPX.PRODUCT_ID = P.ID ")
                .append(" JOIN COUNTRY C ON CPX.COUNTRY_ID = C.ID")
                .append(" JOIN PRODUCT_TYPE PT ON PT.ID = P.TYPE_ID");
        sql = addWhereClause(filters, sql);
        Map<Integer, Product> products = new HashMap<>();
        jdbcTemplate.query(sql.toString(), filters, rs -> {
            Product product = getProduct(products, rs);
            product.addCountries(rs.getString("COUNTRY_CODE"));
        });
        return new ArrayList<>(products.values());
    }

    private Product getProduct(Map<Integer, Product> products, ResultSet rs) throws SQLException {
        if (products.get(rs.getInt("ID")) != null) {
            return products.get(rs.getInt("ID"));
        } else {
            Product product = new Product();
            product.setId(rs.getInt("ID"));
            product.setName(rs.getString("NAME"));
            product.setDisplayName(rs.getString("DISPLAY_NAME"));
            product.setType(rs.getString("TYPE"));
            products.put(product.getId(), product);
            return product;
        }
    }

    private StringBuilder addWhereClause(Map<String, String> filters, StringBuilder sql) {
        if (!filters.isEmpty()) {
            sql.append(where());
            StringJoiner joiner = new StringJoiner(" AND ");
            filters.forEach((key, value) -> {
                joiner.add(whereClauses.get(key));
            });
            sql.append(joiner.toString());
        }
        return sql;
    }

    private String where() {
        return " WHERE ";
    }

    public int addProduct(Product product) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("name", product.getName());
        parameterSource.addValue("displayName", product.getDisplayName());
        parameterSource.addValue("type", product.getType());
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update("insert into PRODUCT (name, DISPLAY_NAME, TYPE_ID) " +
                "values  (:name, :displayName,(SELECT ID FROM PRODUCT_TYPE WHERE NAME = :type))", parameterSource, holder);
        return holder.getKey().intValue();
    }

    public void mapProductToCountries(Product product) {
        String sql = "insert into PRODUCT_COUNTRY_XREF (PRODUCT_ID,COUNTRY_ID) values ( SELECT ID FROM PRODUCT WHERE NAME = :name, SELECT ID FROM COUNTRY WHERE COUNTRY_CODE = :countryCode)";
        List<MapSqlParameterSource> collect = product.getCountries()
                .stream().map(c -> {
                    MapSqlParameterSource params = new MapSqlParameterSource();
                    params.addValue("name", product.getName());
                    params.addValue("countryCode", c);
                    return params;
                })
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sql, collect.toArray(new SqlParameterSource[collect.size()]));

    }

    public Product getProductById(int id) {
        return getProducts(ImmutableMap.of("id", String.valueOf(id)))
                .stream()
                .findFirst()
                .orElse(null);
    }
}
