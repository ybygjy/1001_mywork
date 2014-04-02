USE [master]
--查所有对象，包括表、视图、函数、序列、etc..
--查当前用户下的所有对象
SELECT * FROM SYS.ALL_OBJECTS
SELECT TYPE, TYPE_DESC, COUNT(*) FROM SYS.ALL_OBJECTS GROUP BY TYPE, TYPE_DESC ORDER BY TYPE, TYPE_DESC
--查
SELECT * FROM NSTEST45.SYS.ALL_OBJECTS WHERE TYPE = 'U'
SELECT NAME,OBJECT_ID FROM NSTEST46.SYS.ALL_OBJECTS WHERE TYPE = 'U'

SELECT * FROM MASTER.SYS.SCHEMAS WHERE NAME IN ('NSTEST45', 'NSTEST46');

--查表对象
SELECT * FROM NSTEST45.SYS.TABLES;
--查多余表对象
SELECT '2' TYPE, A.OBJECT_ID,UPPER(A.NAME) AS TABLE_NAME FROM NSTEST45.SYS.TABLES A WHERE NOT EXISTS(SELECT 1 FROM NSTEST46.SYS.TABLES B WHERE UPPER(B.NAME) = UPPER(A.NAME))
--查缺失表对象
UNION ALL
SELECT '1' TYPE, A.OBJECT_ID,UPPER(A.NAME) AS TABLE_NAME FROM NSTEST46.SYS.TABLES A WHERE NOT EXISTS(SELECT 1 FROM NSTEST45.SYS.TABLES B WHERE UPPER(B.NAME) = UPPER(A.NAME))
--查表对象数量
SELECT * FROM (SELECT COUNT(1) A_TABLE FROM NSTEST45.SYS.TABLES)A, (SELECT COUNT(1) B_TABLE FROM NSTEST46.SYS.TABLES) B;
--查询表字段缺失
SELECT '2' TYPE, UPPER(A.TABLE_NAME) AS TABLE_NAME,UPPER(A.COLUMN_NAME) AS COLUMN_NAME FROM NSTEST45.INFORMATION_SCHEMA.COLUMNS A WHERE NOT EXISTS(SELECT 1 FROM NSTEST46.INFORMATION_SCHEMA.COLUMNS B WHERE B.TABLE_NAME = A.TABLE_NAME AND B.COLUMN_NAME = A.COLUMN_NAME)
UNION ALL
SELECT '2' TYPE, UPPER(A.TABLE_NAME) AS TABLE_NAME,UPPER(A.COLUMN_NAME) AS COLUMN_NAME FROM NSTEST46.INFORMATION_SCHEMA.COLUMNS A WHERE NOT EXISTS(SELECT 1 FROM NSTEST45.INFORMATION_SCHEMA.COLUMNS B WHERE B.TABLE_NAME = A.TABLE_NAME AND B.COLUMN_NAME = A.COLUMN_NAME)
--查询表字段比较明细
---查询A用户与B用户共有的字段
SELECT * FROM (
SELECT A.TABLE_NAME,A.COLUMN_NAME,
 (CASE WHEN A.A_DATATYPE <> A.B_DATATYPE THEN (UPPER(A.A_DATATYPE) + '!='+ UPPER(A.B_DATATYPE)) ELSE NULL END) FIELD_TYPE,
 (CASE WHEN A.A_ORD_POSI <> A.B_ORD_POSI THEN (UPPER(A.A_ORD_POSI) + '!='+ UPPER(A.B_ORD_POSI)) ELSE NULL END) FIELD_ORID,
 (CASE WHEN A.A_COL_DEF <> A.B_COL_DEF THEN (UPPER(A.A_COL_DEF) + '!=' + UPPER(A.B_COL_DEF)) ELSE NULL END) FIELD_DEFVALUE,
 (CASE WHEN A.A_ISNULL <> A.B_ISNULL THEN (UPPER(A.A_ISNULL) + '!=' + UPPER(A.B_ISNULL)) ELSE NULL END ) FIELD_ISNULL,
 (CASE WHEN A.A_CMAXLEN <> A.B_CMAXLEN THEN (UPPER(A.A_CMAXLEN) + '!=' + UPPER(A.B_CMAXLEN)) ELSE NULL END) FIELD_LEN,
 (CASE WHEN A.A_NUM_PREC <> A.B_NUM_PREC THEN (UPPER(A.A_NUM_PREC) + '!=' + UPPER(A.B_NUM_PREC)) ELSE NULL END) FIELD_PRE,
 (CASE WHEN A.A_NUMSCAL <> A.B_NUMSCAL THEN (UPPER(A.A_NUMSCAL) + '!=' + UPPER(A.B_NUMSCAL)) ELSE NULL END) FIELD_SCA
FROM (
SELECT  UPPER(A.TABLE_NAME) AS TABLE_NAME,
        UPPER(A.COLUMN_NAME) AS COLUMN_NAME,
        A.ORDINAL_POSITION AS A_ORD_POSI,
        A.COLUMN_DEFAULT AS A_COL_DEF,
        UPPER(A.DATA_TYPE) A_DATATYPE,
        A.IS_NULLABLE AS A_ISNULL,
        A.CHARACTER_MAXIMUM_LENGTH AS A_CMAXLEN,
        A.NUMERIC_PRECISION AS A_NUM_PREC,
        A.NUMERIC_SCALE AS A_NUMSCAL,
        B.ORDINAL_POSITION AS B_ORD_POSI,
        B.COLUMN_DEFAULT AS B_COL_DEF,
		UPPER(B.DATA_TYPE) B_DATATYPE,
        B.IS_NULLABLE AS B_ISNULL,
        B.CHARACTER_MAXIMUM_LENGTH AS B_CMAXLEN,
        B.NUMERIC_PRECISION AS B_NUM_PREC,
        B.NUMERIC_SCALE AS B_NUMSCAL
FROM    MSSQL_2005.INFORMATION_SCHEMA.COLUMNS A,
        MSSQL_TAG.INFORMATION_SCHEMA.COLUMNS B
WHERE   A.TABLE_NAME = B.TABLE_NAME
        AND UPPER(A.COLUMN_NAME) = UPPER(B.COLUMN_NAME)
        ) A) B WHERE B.FIELD_TYPE IS NOT NULL OR B.FIELD_DEFVALUE IS NOT NULL OR B.FIELD_ORID IS NOT NULL OR B.FIELD_ISNULL IS NOT NULL OR B.FIELD_LEN IS NOT NULL OR B.FIELD_PRE IS NOT NULL OR B.FIELD_SCA IS NOT NULL
--查视图对象
SELECT TABLE_TYPE,COUNT(*) FROM NSTEST45.INFORMATION_SCHEMA.TABLES GROUP BY TABLE_TYPE
--版本通用方式查询缺失\多余视图明细
SELECT '1' TYPE,A.OBJECT_ID, UPPER(A.NAME) AS TABLE_NAME FROM NSTEST46.SYS.VIEWS A WHERE NOT EXISTS(SELECT 1 FROM NSTEST45.SYS.VIEWS B WHERE B.NAME = A.NAME)
UNION ALL
SELECT '2' TYPE,A.OBJECT_ID, UPPER(A.NAME) AS TABLE_NAME FROM NSTEST45.SYS.VIEWS A WHERE NOT EXISTS(SELECT 1 FROM NSTEST46.SYS.VIEWS B WHERE B.NAME = A.NAME)
--使用INFORMATION_SCHEMA方式统计表/视图缺失\多余明细
SELECT '1' TYPE, TABLE_NAME,TABLE_TYPE FROM NSTEST46.INFORMATION_SCHEMA.TABLES A WHERE NOT EXISTS(SELECT 1 FROM NSTEST45.INFORMATION_SCHEMA.TABLES B WHERE B.TABLE_NAME = A.TABLE_NAME AND B.TABLE_TYPE = A.TABLE_TYPE)
UNION ALL
SELECT '2' TYPE, TABLE_NAME,TABLE_TYPE FROM NSTEST45.INFORMATION_SCHEMA.TABLES A WHERE NOT EXISTS(SELECT 1 FROM NSTEST46.INFORMATION_SCHEMA.TABLES B WHERE B.TABLE_NAME = A.TABLE_NAME AND B.TABLE_TYPE = A.TABLE_TYPE)
--查触发器对象
SELECT * FROM NSTEST45.SYS.TRIGGERS
--查触发器的激发事件
SELECT COUNT(*), OBJECT_ID FROM NSTEST45.SYS.TRIGGER_EVENTS GROUP BY OBJECT_ID
SELECT * FROM NSTEST45.SYS.EVENTS
--查触发器的定义
---触发器的SQL语句
SELECT * FROM NSTEST45.SYS.SQL_MODULES

---触发器逻辑定义
SELECT * FROM NSTEST45.SYS.ALL_OBJECTS WHERE TYPE='TR'
SELECT * FROM NSTEST45.SYS.EVENT_NOTIFICATIONS
SELECT * FROM NSTEST45.SYS.EVENT_NOTIFICATION_EVENT_TYPES

--触发器对象与其它对象的关联关系
SELECT * FROM NSTEST45.SYS.SQL_DEPENDENCIES A
SELECT * FROM NSTEST45.SYS.TABLES
--查触发器明细
SELECT  UPPER(B.NAME) TABLE_NAME,
        UPPER(A.NAME) TRIGGER_NAME,
        C.TYPE,
        C.TYPE_DESC
FROM    NSTEST45.SYS.TRIGGERS A
        LEFT JOIN NSTEST45.SYS.TABLES B ON B.OBJECT_ID = A.PARENT_ID
        LEFT JOIN NSTEST45.SYS.TRIGGER_EVENTS C ON C.OBJECT_ID = A.OBJECT_ID
ORDER BY TABLE_NAME,
        TRIGGER_NAME,
        TYPE_DESC
--触发器缺失明细
SELECT TABLE_NAME,TRIGGER_NAME, TYPE FROM (
SELECT 
(CASE WHEN A.TABLE_NAME IS NULL THEN B.TABLE_NAME WHEN B.TABLE_NAME IS NULL THEN A.TABLE_NAME ELSE A.TABLE_NAME END) TABLE_NAME,
(CASE WHEN A.TRIGGER_NAME IS NULL THEN B.TRIGGER_NAME WHEN B.TRIGGER_NAME IS NULL THEN A.TRIGGER_NAME ELSE A.TRIGGER_NAME END) TRIGGER_NAME,
(CASE WHEN A.TABLE_NAME IS NULL THEN '1' WHEN B.TABLE_NAME IS NULL THEN '2' ELSE '-1' END) TYPE
FROM 
(
SELECT UPPER(B.NAME) TABLE_NAME, UPPER(A.NAME) TRIGGER_NAME FROM MSSQL_2005.SYS.TRIGGERS A LEFT JOIN MSSQL_2005.SYS.TABLES B ON A.PARENT_ID = B.OBJECT_ID
) A FULL JOIN 
(
SELECT UPPER(B.NAME) TABLE_NAME, UPPER(A.NAME) TRIGGER_NAME FROM MSSQL_TAG.SYS.TRIGGERS A LEFT JOIN MSSQL_TAG.SYS.TABLES B ON A.PARENT_ID = B.OBJECT_ID
) B ON A.TABLE_NAME = B.TABLE_NAME AND A.TRIGGER_NAME = B.TRIGGER_NAME
) C WHERE C.TYPE <> '-1'
SELECT * FROM MSSQL_2005.SYS.TRIGGERS A WHERE NOT EXISTS(SELECT 1 FROM MSSQL_TAG.SYS.TRIGGERS B WHERE A.NAME = B.NAME)
---查与之关联的表对象
---查与之关联的事件对象关联信息
----最终结果就是某个表下的某些触发器
--查存储过程/函数
SELECT COUNT(*),ROUTINE_TYPE FROM NSTEST45.INFORMATION_SCHEMA.ROUTINES GROUP BY ROUTINE_TYPE
SELECT '1' TYPE, UPPER(A.TABLE_NAME) AS TABLE_NAME,UPPER(A.COLUMN_NAME) AS COLUMN_NAME FROM NSTEST46.INFORMATION_SCHEMA.COLUMNS A WHERE NOT EXISTS(SELECT 1 FROM NSTEST45.INFORMATION_SCHEMA.COLUMNS B WHERE B.TABLE_NAME = A.TABLE_NAME AND B.COLUMN_NAME = A.COLUMN_NAME) UNION ALL SELECT '2' TYPE, UPPER(A.TABLE_NAME) AS TABLE_NAME,UPPER(A.COLUMN_NAME) AS COLUMN_NAME FROM NSTEST45.INFORMATION_SCHEMA.COLUMNS A WHERE NOT EXISTS(SELECT 1 FROM NSTEST46.INFORMATION_SCHEMA.COLUMNS B WHERE B.TABLE_NAME = A.TABLE_NAME AND B.COLUMN_NAME = A.COLUMN_NAME)
SELECT * FROM (SELECT A.TABLE_NAME,A.COLUMN_NAME,(CASE WHEN A.A_DATATYPE <> A.B_DATATYPE THEN (UPPER(A.A_DATATYPE) + '!='+ UPPER(A.B_DATATYPE)) ELSE NULL END) FIELD_TYPE,(CASE WHEN A.A_ORD_POSI <> A.B_ORD_POSI THEN (UPPER(A.A_ORD_POSI) + '!='+ UPPER(A.B_ORD_POSI)) ELSE NULL END) FIELD_ORID,(CASE WHEN A.A_COL_DEF <> A.B_COL_DEF THEN (UPPER(A.A_COL_DEF) + '!=' + UPPER(A.B_COL_DEF)) ELSE NULL END) FIELD_DEFVALUE,(CASE WHEN A.A_ISNULL <> A.B_ISNULL THEN (UPPER(A.A_ISNULL) + '!=' + UPPER(A.B_ISNULL)) ELSE NULL END ) FIELD_ISNULL,(CASE WHEN A.A_CMAXLEN <> A.B_CMAXLEN THEN (UPPER(A.A_CMAXLEN) + '!=' + UPPER(A.B_CMAXLEN)) ELSE NULL END) FIELD_LEN,(CASE WHEN A.A_NUM_PREC <> A.B_NUM_PREC THEN (UPPER(A.A_NUM_PREC) + '!=' + UPPER(A.B_NUM_PREC)) ELSE NULL END) FIELD_PRE,(CASE WHEN A.A_NUMSCAL <> A.B_NUMSCAL THEN (UPPER(A.A_NUMSCAL) + '!=' + UPPER(A.B_NUMSCAL)) ELSE NULL END) FIELD_SCA FROM (SELECT  UPPER(A.TABLE_NAME) AS TABLE_NAME,UPPER(A.COLUMN_NAME) AS COLUMN_NAME,A.ORDINAL_POSITION AS A_ORD_POSI,A.COLUMN_DEFAULT AS A_COL_DEF,UPPER(A.DATA_TYPE) A_DATATYPE,A.IS_NULLABLE AS A_ISNULL,A.CHARACTER_MAXIMUM_LENGTH AS A_CMAXLEN,A.NUMERIC_PRECISION AS A_NUM_PREC,A.NUMERIC_SCALE AS A_NUMSCAL,B.ORDINAL_POSITION AS B_ORD_POSI,B.COLUMN_DEFAULT AS B_COL_DEF,UPPER(B.DATA_TYPE) B_DATATYPE,B.IS_NULLABLE AS B_ISNULL,B.CHARACTER_MAXIMUM_LENGTH AS B_CMAXLEN,B.NUMERIC_PRECISION AS B_NUM_PREC,B.NUMERIC_SCALE AS B_NUMSCAL FROM NSTEST45.INFORMATION_SCHEMA.COLUMNS A,NSTEST46.INFORMATION_SCHEMA.COLUMNS B WHERE   A.TABLE_NAME = B.TABLE_NAME AND A.COLUMN_NAME = B.COLUMN_NAME) A) B WHERE B.FIELD_TYPE IS NOT NULL OR B.FIELD_DEFVALUE IS NOT NULL OR B.FIELD_ORID IS NOT NULL OR B.FIELD_ISNULL IS NOT NULL OR B.FIELD_LEN IS NOT NULL OR B.FIELD_PRE IS NOT NULL OR B.FIELD_SCA IS NOT NULL

--过程
---过程缺失/多余
SELECT C.TYPE,C.PRO_NAME FROM (SELECT (CASE WHEN A.NAME IS NULL THEN '1' WHEN B.NAME IS NULL THEN '2' ELSE '-1' END) TYPE,(CASE WHEN A.NAME IS NULL THEN UPPER(B.NAME) WHEN B.NAME IS NULL THEN UPPER(A.NAME) ELSE UPPER(A.NAME) END) PRO_NAME FROM NSTEST45.SYS.PROCEDURES A FULL JOIN (SELECT UPPER(NAME) NAME FROM NSTEST46.SYS.PROCEDURES ) B ON A.NAME = B.NAME) C WHERE C.TYPE <> '-1'
--函数
SELECT DEFINITION,TYPE FROM NSTEST45.SYS.SQL_MODULES A LEFT JOIN NSTEST45.SYS.OBJECTS B ON A.OBJECT_ID = B.OBJECT_ID AND TYPE IN('FN', 'IF', 'TF');
SELECT ROUTINE_TYPE,COUNT(*) FROM NSTEST45.INFORMATION_SCHEMA.ROUTINES GROUP BY ROUTINE_TYPE
SELECT COUNT(*) FROM NSTEST45.SYS.PROCEDURES
SELECT * FROM (SELECT (CASE WHEN A.FUN_NAME IS NULL THEN '1' WHEN B.FUN_NAME IS NULL THEN '2' ELSE -1 END) TYPE, (CASE WHEN A.FUN_NAME IS NULL THEN B.FUN_NAME WHEN B.FUN_NAME IS NULL THEN A.FUN_NAME ELSE A.FUN_NAME END) FUN_NAME FROM (SELECT UPPER(NAME) AS FUN_NAME FROM NSTEST45.SYS.OBJECTS WHERE TYPE = 'FN') A FULL JOIN (SELECT UPPER(NAME) AS FUN_NAME FROM NSTEST46.SYS.OBJECTS WHERE TYPE = 'FN')B ON A.FUN_NAME = B.FUN_NAME) C WHERE C.TYPE <> '-1'

SELECT TYPE, TYPE_DESC,COUNT(*) FROM NSTEST45.SYS.OBJECTS GROUP BY TYPE, TYPE_DESC HAVING TYPE IN('FN','FS','FT','IF')

SELECT COUNT(*), TYPE FROM NSTEST45.SYS.OBJECTS GROUP BY TYPE
--CHECK约束
SELECT * FROM NSTEST45.SYS.OBJECTS WHERE TYPE = 'C'
SELECT * FROM NSTEST45.INFORMATION_SCHEMA.CHECK_CONSTRAINTS
--查询定义表上的约束包括主键约束、外键约束、CHECK约束
SELECT * FROM (
SELECT
(CASE WHEN A.A_TABLE_NAME IS NULL THEN B.B_TABLE_NAME ELSE A.A_TABLE_NAME END) TABLE_NAME,
(CASE WHEN A.A_CONS_NAME IS NULL THEN B.B_CONS_NAME ELSE A.A_CONS_NAME END) CONS_NAME,
(CASE WHEN A.A_CONS_NAME IS NULL THEN '1' WHEN B.B_CONS_NAME IS NULL THEN '2' ELSE '-1' END) TYPE
FROM (
SELECT UPPER(TABLE_NAME) A_TABLE_NAME, UPPER(CONSTRAINT_NAME) A_CONS_NAME FROM MSSQL_2005.INFORMATION_SCHEMA.CONSTRAINT_TABLE_USAGE ) A FULL JOIN(
SELECT UPPER(TABLE_NAME) B_TABLE_NAME, UPPER(CONSTRAINT_NAME) B_CONS_NAME FROM MSSQL_TAG.INFORMATION_SCHEMA.CONSTRAINT_TABLE_USAGE ) B
ON A.A_TABLE_NAME = B.B_TABLE_NAME AND A.A_CONS_NAME = B.B_CONS_NAME
) C WHERE C.TYPE <> '-1'

SELECT UPPER(A.NAME) AS FUN_NAME FROM MSSQL_TAG.SYS.OBJECTS A WHERE TYPE = 'FN' AND NOT EXISTS(SELECT 1 FROM MSSQL_2005.SYS.OBJECTS B WHERE TYPE='FN' AND B.NAME = A.NAME)

SELECT * FROM MSSQL_2005.INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'VW_FB_LEDGERS' AND UPPER(COLUMN_NAME) = 'SURETY_TYPES'