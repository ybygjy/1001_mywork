-- Create table
create table SYSTEM.NSTC_DBCOMP_TABLECOL_DETAIL
(
  TABLE_NAME    VARCHAR2(40) not null,
  FIELD_NAME    VARCHAR2(40) not null,
  SRCSCHEMA     VARCHAR2(20) not null,
  TARSCHEMA     VARCHAR2(20) not null,
  FIELD_TYPE    VARCHAR2(40),
  FIELD_LEN     VARCHAR2(40),
  FIELD_PRE     VARCHAR2(40),
  FIELD_SCA     VARCHAR2(40),
  FIELD_NULL    VARCHAR2(40),
  FIELD_DEFVLEN VARCHAR2(40),
  FIELD_DEFVALUE VARCHAR2(2000)
);
-- Add comments to the table 
comment on table SYSTEM.NSTC_DBCOMP_TABLECOL_DETAIL
  is '���ݿ����ṹ�Ƚ�_���ֶαȽϽ��';
-- Add comments to the columns 
comment on column SYSTEM.NSTC_DBCOMP_TABLECOL_DETAIL.TABLE_NAME
  is '�����';
comment on column SYSTEM.NSTC_DBCOMP_TABLECOL_DETAIL.FIELD_NAME
  is '�ֶα���';
comment on column SYSTEM.NSTC_DBCOMP_TABLECOL_DETAIL.SRCSCHEMA
  is 'Դ�û�';
comment on column SYSTEM.NSTC_DBCOMP_TABLECOL_DETAIL.TARSCHEMA
  is '�����û�';
comment on column SYSTEM.NSTC_DBCOMP_TABLECOL_DETAIL.FIELD_TYPE
  is '�ֶ�����';
comment on column SYSTEM.NSTC_DBCOMP_TABLECOL_DETAIL.FIELD_LEN
  is '�ֶγ���';
comment on column SYSTEM.NSTC_DBCOMP_TABLECOL_DETAIL.FIELD_PRE
  is '�ֶξ���';
comment on column SYSTEM.NSTC_DBCOMP_TABLECOL_DETAIL.FIELD_SCA
  is '�ֶξ���';
comment on column SYSTEM.NSTC_DBCOMP_TABLECOL_DETAIL.FIELD_NULL
  is '�ֶηǿձ��';
comment on column SYSTEM.NSTC_DBCOMP_TABLECOL_DETAIL.FIELD_DEFVLEN
  is '�ֶ�Ĭ��ֵ����';
COMMENT ON COLUMN system.NSTC_DBCOMP_TABLECOL_DETAIL.FIELD_DEFVALUE
  IS '�ֶ�Ĭ��ֵ����';
