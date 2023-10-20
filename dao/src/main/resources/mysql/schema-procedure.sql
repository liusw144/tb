--mysql表分区存储过程
DELIMITER $
CREATE PROCEDURE create_partition_ifNotExists(IN table_name VARCHAR(255), IN table_name_partition_date VARCHAR(255), IN partition_end VARCHAR(255) )
BEGIN
SET @IF_PRESENT=(SELECT COUNT(*) FROM (SELECT PARTITION_NAME FROM INFORMATION_SCHEMA.PARTITIONS WHERE TABLE_NAME = table_name ) a WHERE a.PARTITION_NAME=table_name_partition_date);

IF (@IF_PRESENT= 0) THEN
ALTER TABLE table_name ADD PARTITION (PARTITION table_name_partition_date VALUES LESS THAN ('partition_end'));

END IF;
END $
DELIMITER;

--ts_kv_latest表DUPLICATE的存储过程
DELIMITER $
CREATE PROCEDURE ts_kv_latest_insert_or_update_by_latest_ts (
    IN entity_id VARCHAR(36), IN key_1 INT,IN ts BIGINT, IN bool_v VARCHAR(5) , IN str_v TEXT, IN long_v BIGINT, IN dbl_v FLOAT, IN json_v JSON )

BEGIN
SET @IF_PRESENT=(SELECT COUNT(*) FROM ts_kv_latest WHERE entity_id = entity_id AND `key`=key_1 );

IF (@IF_PRESENT= 0) THEN
INSERT INTO ts_kv_latest (entity_id, `key`, ts, bool_v, str_v, long_v, dbl_v, json_v) VALUES (entity_id,key_1, ts, bool_v, str_v, long_v, dbl_v, cast(json_v AS json));


ELSEIF (@IF_PRESENT >=1) THEN
UPDATE ts_kv_latest SET ts = ts,bool_v = bool_v, str_v = str_v, long_v = long_v, dbl_v = long_v, json_v = cast(json_v AS json) WHERE entity_id = entity_id AND `key`=key_1 AND ts <=ts ;

END IF;
END $
DELIMITER;

--ts_kv表DUPLICATE的存储过程
DELIMITER $
CREATE PROCEDURE ts_kv_insert_on_conflict_do_update(IN entity_id VARCHAR(36), IN key_1 INT,IN ts BIGINT, IN bool_v VARCHAR(5) , IN str_v TEXT, IN long_v BIGINT, IN dbl_v FLOAT, IN json_v JSON )
BEGIN
SET @IF_PRESENT=(SELECT COUNT(*) FROM ts_kv WHERE entity_id = entity_id AND `key`=key_1 AND ts=ts );

IF (@IF_PRESENT= 0) THEN
INSERT INTO ts_kv (entity_id, `key`, ts, bool_v, str_v, long_v, dbl_v, json_v) VALUES (entity_id,key_1, ts, bool_v, str_v, long_v, dbl_v, cast(json_v AS json));

ELSEIF (@IF_PRESENT >=1) THEN
UPDATE ts_kv SET bool_v = bool_v, str_v = str_v, long_v = long_v, dbl_v = long_v, json_v = cast(json_v AS json) WHERE entity_id = entity_id AND `key`=key_1 AND ts=ts ;

END IF;
END $
DELIMITER;

