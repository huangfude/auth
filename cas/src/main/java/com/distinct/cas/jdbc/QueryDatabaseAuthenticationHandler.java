package com.distinct.cas.jdbc;

import java.security.GeneralSecurityException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.validation.constraints.NotNull;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

/**
 * 通过数据库验证身份
 * 
 * @author huang
 *
 */
public class QueryDatabaseAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {

	@NotNull
	private String sql;

	protected final HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential credential)
			throws GeneralSecurityException, PreventedException {
		//获取用户名
		String username = credential.getUsername();
		System.err.println("======= input username:(" + username + ")");
		//获取加密密码
		String encryptedPassword = getPasswordEncoder().encode(credential.getPassword());
		System.err.println("======= input password:(" + encryptedPassword + ")");
		System.out.println("======= sql:(" + this.sql + ")");
		try {
			String dbPassword = (String) getJdbcTemplate().queryForObject(this.sql, String.class,
					new Object[] { username });

			System.err.println("++++++ dbPassword:(" + dbPassword.trim() + ")");
			if (!(dbPassword.trim().equals(encryptedPassword))) {
				System.err.println("Password not match.");
				throw new FailedLoginException("Password does not match value on record.");
			}
		} catch (IncorrectResultSizeDataAccessException e) {
			if (e.getActualSize() == 0) {
				throw new AccountNotFoundException(username + " not found with SQL query");
			}
			e.printStackTrace();
			throw new FailedLoginException("Multiple records found for " + username);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new PreventedException("SQL exception while executing query for " + username, e);
		}

		return createHandlerResult(credential, new SimplePrincipal(username), null);
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
}
