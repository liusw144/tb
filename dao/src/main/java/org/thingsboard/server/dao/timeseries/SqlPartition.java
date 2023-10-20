/**
 * Copyright © 2016-2023 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.server.dao.timeseries;

import lombok.Data;

@Data
public class SqlPartition {

    public static final String TS_KV = "ts_kv";

    private long start;
    private long end;
    private String partitionDate;
    private String query;

    public SqlPartition(String table, long start, long end, String partitionDate) {
        this.start = start;
        this.end = end;
        this.partitionDate = partitionDate;
        this.query = createStatement(table, start, end, partitionDate);
    }

    private String createStatement(String table, long start, long end, String partitionDate) {
        // 暂时未判断 存在该分区的情况下 跳过添加的逻辑
//       return "CREATE TABLE IF NOT EXISTS " + table + "_" + partitionDate + " PARTITION OF " + table + " FOR VALUES FROM (" + start + ") TO (" + end + ")";

        //直接使用存储过程 脚本如下
//        #分区建表的存储过程
//        DELIMITER $
//        CREATE PROCEDURE create_partition_ifNotExists(IN table_name VARCHAR(255), IN table_name_partition_date VARCHAR(255), IN partition_end VARCHAR(255)  )
//        BEGIN
//
//        SET @IF_PRESENT=(SELECT COUNT(*) FROM (SELECT PARTITION_NAME FROM INFORMATION_SCHEMA.PARTITIONS WHERE TABLE_NAME = table_name ) a WHERE a.PARTITION_NAME=table_name_partition_date);
//        IF (@IF_PRESENT='0') THEN
//        ALTER TABLE table_name ADD PARTITION (PARTITION table_name_partition_date VALUES LESS THAN ('partition_end'));
//        END IF;
//        END $
//        DELIMITER;
//
//
//        #调用
//        SET @table_name = 'notification';
//        SET @table_name_partition_date = 'notification_1697068800000';
//        SET @partition_end = '1697673600000';
//        CALL create_partition_ifNotExists(@table_name,@table_name_partition_date, @partition_end);
//        return "ALTER TABLE "+table + " ADD PARTITION ( PARTITION "+table + "_" + partitionDate +" VALUES LESS THAN ("+ end +"))";
//        return "SET @table_name = "+table +";"+"SET @table_name_partition_date = "+table+"_"+partitionDate+";"+"SET @partition_end="+end+";"+"CALL create_partition_ifNotExists(@table_name,@table_name_partition_date, @partition_end);";
        return "CALL create_partition_ifNotExists('"+table+"','"+table+"_"+partitionDate+"'," + end+");";
    }
}