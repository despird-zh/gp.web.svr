package gp.extra;

import com.fasterxml.jackson.annotation.JsonProperty;

public class bean1 {

	@JsonProperty("prop1")
	public String p1;
	
	@JsonProperty("prop2")
	public String p2;
	
	@JsonProperty("prop3")
	public Integer p3;
	
}
/**
 * 
 DELIMITER $$

DROP PROCEDURE IF EXISTS proc_folder_path$$

CREATE PROCEDURE proc_folder_path(in p_folder_id INTEGER , out p_folder_path varchar(2048))
BEGIN
	
	declare v_fpath varchar(256);
	declare v_ppath varchar(256);
	declare v_pid varchar(256);
	set max_sp_recursion_depth=256;
	select folder_name,folder_pid into v_fpath, v_pid from gp_cab_folders where folder_id = p_folder_id;
	
	if v_pid = -98 then
		select concat('/', v_fpath) into p_folder_path;
	else
		call proc_folder_path(v_pid, v_ppath);
		select concat(v_ppath, '/', v_fpath) into p_folder_path;
	end if;
END $$
DELIMITER ;
 
 */