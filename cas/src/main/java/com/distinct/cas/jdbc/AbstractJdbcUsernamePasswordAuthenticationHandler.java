package com.distinct.cas.jdbc;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据库抽象类
 * @author huang
 *
 */
public abstract class AbstractJdbcUsernamePasswordAuthenticationHandler
		extends AbstractUsernamePasswordAuthenticationHandler {

	@NotNull
	private JdbcTemplate jdbcTemplate;

	@NotNull
	private DataSource dataSource;

	public final void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.dataSource = dataSource;
	}

	protected final JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	protected final DataSource getDataSource() {
		return this.dataSource;
	}
}