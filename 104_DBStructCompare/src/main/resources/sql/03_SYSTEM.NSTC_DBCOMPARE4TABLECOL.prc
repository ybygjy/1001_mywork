CREATE OR REPLACE PROCEDURE SYSTEM.NSTC_DBCOMPARE4TABLECOL(SRCSCHEMA    IN VARCHAR2,
                                                           TARGETSCHEMA IN VARCHAR2) IS
  INSERT_FLAG BOOLEAN := FALSE;
  COMM_FLAG   BOOLEAN := FALSE;
BEGIN
  --删除原始信息
  BEGIN
    DELETE FROM SYSTEM.NSTC_DBCOMP_TABLECOL_DETAIL;
    DELETE FROM SYSTEM.NSTC_DBCOMP_OBJECT;
    COMMIT;
  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('SQLERRM: ' || SQLERRM);
      DBMS_OUTPUT.PUT_LINE('SQLCODE: ' || SQLCODE);
  END;
  --字段结构比较
  DECLARE
    NDTD SYSTEM.NSTC_DBCOMP_TABLECOL_DETAIL%ROWTYPE := NULL;
  BEGIN
    FOR C IN (SELECT A.TABLE_NAME,
       A.COLUMN_NAME,
       A.DATA_TYPE      A_DT,
       B.DATA_TYPE      B_DT,
       A.DATA_LENGTH    A_DLEN,
       B.DATA_LENGTH    B_DLEN,
       A.DATA_PRECISION A_DPREC,
       B.DATA_PRECISION B_DPREC,
       A.DATA_SCALE     A_DSCA,
       B.DATA_SCALE     B_DSCA,
       A.NULLABLE       A_NULLA,
       B.NULLABLE       B_NULLA,
       A.DEFAULT_LENGTH A_DEFLEN,
       B.DEFAULT_LENGTH B_DEFLEN,
       A.DATA_DEFAULT   A_DATADF,
       B.DATA_DEFAULT   B_DATADF
  FROM (SELECT ATT.TABLE_NAME,
               ACT1.COLUMN_NAME,
               ACT1.DATA_TYPE,
               ACT1.DATA_LENGTH,
               ACT1.DATA_PRECISION,
               ACT1.DATA_SCALE,
               ACT1.NULLABLE,
               ACT1.DEFAULT_LENGTH,
               ACT1.DATA_DEFAULT
          FROM ALL_TAB_COLS ACT1 LEFT JOIN ALL_TABLES ATT ON ACT1.TABLE_NAME = ATT.TABLE_NAME
         WHERE ACT1.OWNER = SRCSCHEMA AND ATT.OWNER = SRCSCHEMA
           AND EXISTS (SELECT 1
                  FROM ALL_TAB_COLS ATC2
                 WHERE ATC2.OWNER = TARGETSCHEMA
                   AND ATC2.TABLE_NAME = ACT1.TABLE_NAME AND ATC2.COLUMN_NAME = ACT1.COLUMN_NAME)) A,
       (SELECT ATT.TABLE_NAME,
               COLUMN_NAME,
               DATA_TYPE,
               DATA_LENGTH,
               DATA_PRECISION,
               DATA_SCALE,
               NULLABLE,
               DEFAULT_LENGTH,
               DATA_DEFAULT
          FROM ALL_TAB_COLS ACT1 LEFT JOIN ALL_TABLES ATT ON ACT1.TABLE_NAME = ATT.TABLE_NAME
         WHERE ACT1.OWNER = TARGETSCHEMA AND ATT.OWNER=TARGETSCHEMA
           AND EXISTS (SELECT 1
                  FROM ALL_TAB_COLS ATC2
                 WHERE ATC2.OWNER = SRCSCHEMA
                   AND ATC2.TABLE_NAME = ACT1.TABLE_NAME AND ATC2.COLUMN_NAME = ACT1.COLUMN_NAME)) B
 WHERE A.TABLE_NAME = B.TABLE_NAME
   AND A.COLUMN_NAME = B.COLUMN_NAME) LOOP
      IF (C.A_DT <> C.B_DT) THEN
        INSERT_FLAG     := TRUE;
        NDTD.FIELD_TYPE := C.A_DT || '!=' || C.B_DT;
      END IF;
      IF (C.A_DLEN <> C.B_DLEN) THEN
        INSERT_FLAG    := TRUE;
        NDTD.FIELD_LEN := C.A_DLEN || '!=' || C.B_DLEN;
      END IF;
      IF (C.A_DPREC <> C.B_DPREC) THEN
        INSERT_FLAG    := TRUE;
        NDTD.FIELD_PRE := C.A_DPREC || '!=' || C.B_DPREC;
      END IF;
      IF (C.A_DSCA <> C.B_DSCA) THEN
        INSERT_FLAG    := TRUE;
        NDTD.FIELD_SCA := C.A_DSCA || '!=' || C.B_DSCA;
      END IF;
      IF (C.A_NULLA <> C.B_NULLA) THEN
        INSERT_FLAG     := TRUE;
        NDTD.FIELD_NULL := C.A_NULLA || '!=' || C.B_NULLA;
      END IF;
      /*
      IF (C.A_DEFLEN <> C.B_DEFLEN) THEN
        INSERT_FLAG        := TRUE;
        NDTD.FIELD_DEFVLEN := C.A_DEFLEN || '!=' || C.B_DEFLEN;
      END IF;
      */
      IF (C.A_DATADF <> C.B_DATADF) THEN
        INSERT_FLAG := TRUE;
        IF (LENGTH(C.A_DATADF) > 1500 OR LENGTH(C.B_DATADF) > 1500) THEN
            NDTD.FIELD_DEFVALUE := '字段超长，不具备打印条件.';
        ELSE
            NDTD.FIELD_DEFVALUE := C.A_DATADF || '!=' || C.B_DATADF;
        END IF;
      END IF;
      IF (INSERT_FLAG = TRUE) THEN
        NDTD.TABLE_NAME := C.TABLE_NAME;
        NDTD.FIELD_NAME := C.COLUMN_NAME;
        --插入表字段比较明细
        INSERT INTO SYSTEM.NSTC_DBCOMP_TABLECOL_DETAIL
          (TABLE_NAME,
           FIELD_NAME,
           SRCSCHEMA,
           TARSCHEMA,
           FIELD_TYPE,
           FIELD_LEN,
           FIELD_PRE,
           FIELD_SCA,
           FIELD_NULL,
           FIELD_DEFVLEN,
           FIELD_DEFVALUE)
        VALUES
          (NDTD.TABLE_NAME,
           NDTD.FIELD_NAME,
           SRCSCHEMA,
           TARGETSCHEMA,
           NDTD.FIELD_TYPE,
           NDTD.FIELD_LEN,
           NDTD.FIELD_PRE,
           NDTD.FIELD_SCA,
           NDTD.FIELD_NULL,
           NDTD.FIELD_DEFVLEN,
           NDTD.FIELD_DEFVALUE);
        COMM_FLAG   := TRUE;
        INSERT_FLAG := FALSE;
        NDTD        := NULL;
      END IF;
    END LOOP;
    IF (COMM_FLAG = TRUE) THEN
      COMMIT;
    END IF;
  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('SQLERRM: ' || SQLERRM);
      DBMS_OUTPUT.PUT_LINE('SQLCODE: ' || SQLCODE);
      NULL;
  END;
END NSTC_DBCOMPARE4TABLECOL;
/
