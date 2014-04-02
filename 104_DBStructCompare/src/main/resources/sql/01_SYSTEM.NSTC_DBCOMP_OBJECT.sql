-- Create table
CREATE TABLE SYSTEM.NSTC_DBCOMP_OBJECT
(
  OP_TYPE   NUMBER(2) default 1 not null,
  SRCSCHEMA VARCHAR2(20) not null,
  TARSCHEMA VARCHAR2(20) not null,
  TABLE_NAME VARCHAR2(40),
  OBJ_NAME  VARCHAR2(40) not null,
  OBJ_TYPE  VARCHAR2(20) not null
);
-- Add comments to the table 
comment on table SYSTEM.NSTC_DBCOMP_OBJECT
  is '���ݿ����ṹ�Ƚ�_����ȱʧ�ǼǱ�';
-- Add comments to the columns 
comment on column SYSTEM.NSTC_DBCOMP_OBJECT.OP_TYPE
  is 'ȱʧ����{1:ȱʧ;2:����}';
comment on column SYSTEM.NSTC_DBCOMP_OBJECT.SRCSCHEMA
  is 'Դ�Ƚ��û�';
comment on column SYSTEM.NSTC_DBCOMP_OBJECT.TABLE_NAME
  is '������,�����ڻ��ڱ�Ķ����������ֶ�\������';
comment on column SYSTEM.NSTC_DBCOMP_OBJECT.TARSCHEMA
  is 'Ŀ��(����)�û�';
comment on column SYSTEM.NSTC_DBCOMP_OBJECT.OBJ_NAME
  is '��������';
comment on column SYSTEM.NSTC_DBCOMP_OBJECT.OBJ_TYPE
  is '������������{FIELD|VIEW|TABLE|FUNC|PROC|TRIG|SEQ|ETC..}';
