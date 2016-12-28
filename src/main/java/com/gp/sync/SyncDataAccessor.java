package com.gp.sync;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.gp.dao.impl.DAOSupport;
import com.gp.sync.SyncMeta.SyncType;

public class SyncDataAccessor extends DAOSupport{

	@Override
	protected void initialJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<SyncOutInfo> querySyncOutList(String batchId){
		
		String SQL = "select * from gp_sync_out where sync_batch= ?";
		
		JdbcTemplate jdbcTemplate = getJdbcTemplate(JdbcTemplate.class);
		
		List<SyncOutInfo> syncoutList = jdbcTemplate.query(SQL, 
                new Object[]{batchId}, SyncOutRowMapper);
		
		return syncoutList;		
	}
	
	public List<SyncInInfo> querySyncInList(String batchId){
		
		String SQL = "select * from gp_sync_in where sync_batch= ?";
		
		JdbcTemplate jdbcTemplate = getJdbcTemplate(JdbcTemplate.class);
		
		List<SyncInInfo> syncoutList = jdbcTemplate.query(SQL, 
                new Object[]{batchId}, SyncInRowMapper);
		
		return syncoutList;		
	}
	
	public void saveSyncOutInfo(SyncOutInfo syncout){
		
		
	}
	
	public void saveSyncInInfo(SyncInInfo syncin){
		
		
	}
	
	public static RowMapper<SyncOutInfo> SyncOutRowMapper = new RowMapper<SyncOutInfo>(){

		@Override
		public SyncOutInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			SyncOutInfo info = new SyncOutInfo();
			info.setSyncId(rs.getString("sync_id"));
			info.setSyncName(rs.getString("sync_name"));
			info.setSourceEntity(rs.getString("src_entity_id"));
			info.setSourceNode(rs.getString("src_node_id"));
			info.setTargetEntity(rs.getString("tgt_entity_id"));
			info.setTargetNode(rs.getString("tgt_node_id"));
			SyncType synctype = SyncType.valueOf(rs.getString("sync_type"));
			info.setSyncType(synctype);
			info.setAuditId(rs.getString("audit_id"));
			info.setSyncSeq(rs.getInt("sync_seq"));
			info.setSyncBatch(rs.getString("sync_batch"));
			info.setSyncData(rs.getString("sync_data"));
			info.setCreateDate(rs.getDate("create_date"));
			info.setSentDate(rs.getDate("sent_date"));
			info.setConfirmDate(rs.getDate("sent_date"));
			return info;
		}		
		
	};
	
	public static RowMapper<SyncInInfo> SyncInRowMapper = new RowMapper<SyncInInfo>(){

		@Override
		public SyncInInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			SyncInInfo info = new SyncInInfo();
			info.setSyncId(rs.getString("sync_id"));
			info.setSyncName(rs.getString("sync_name"));
			info.setSourceEntity(rs.getString("src_entity_id"));
			info.setSourceNode(rs.getString("src_node_id"));
			info.setTargetEntity(rs.getString("tgt_entity_id"));
			info.setTargetNode(rs.getString("tgt_node_id"));
			SyncType synctype = SyncType.valueOf(rs.getString("sync_type"));
			info.setSyncType(synctype);
			info.setSyncSeq(rs.getInt("sync_seq"));
			info.setSyncBatch(rs.getString("sync_batch"));
			info.setSyncData(rs.getString("sync_data"));
			info.setReceiveDate(rs.getDate("receive_date"));
			info.setProcessDate(rs.getDate("process_date"));
			info.setConfirmDate(rs.getDate("confirm_date"));
			return info;
		}		
		
	};
}
