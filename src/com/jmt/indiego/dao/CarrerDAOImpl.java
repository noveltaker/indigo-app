package com.jmt.indiego.dao;

import org.apache.ibatis.session.SqlSession;

public class CarrerDAOImpl implements CarrerDAO {

	private SqlSession session;

	public void setSession(SqlSession session) {
		this.session = session;
	}
}
