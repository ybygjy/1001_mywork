CREATE TABLE SYSTEM.NSTC_DBCOMP_TRIGG(
       TABLE_NAME VARCHAR2(30) NOT NULL,
       TRIGGER_NAME VARCHAR2(30) NOT NULL,
       TRIGGER_TYPE VARCHAR2(40),
       TRIGGER_EVENT VARCHAR2(512),
       TRIGGER_BOT VARCHAR2(40),
       TRIGGER_WCLAUSE VARCHAR2(4000),
       TRIGGER_STATUS VARCHAR2(30)
);
-- Add comments to the table 
comment on table SYSTEM.NSTC_DBCOMP_TRIGG
  is '����������ṹ�ȽϽ��';
-- Add comments to the columns 
comment on column SYSTEM.NSTC_DBCOMP_TRIGG.TABLE_NAME
  is '���������';
comment on column SYSTEM.NSTC_DBCOMP_TRIGG.TRIGGER_NAME
  is '����������';
comment on column SYSTEM.NSTC_DBCOMP_TRIGG.TRIGGER_TYPE
  is '����������';
comment on column SYSTEM.NSTC_DBCOMP_TRIGG.TRIGGER_EVENT
  is '�����������¼�����';
comment on column SYSTEM.NSTC_DBCOMP_TRIGG.TRIGGER_BOT
  is '������������{TABLE:VIEW}';
comment on column SYSTEM.NSTC_DBCOMP_TRIGG.TRIGGER_WCLAUSE
  is '������ִ������';
comment on column SYSTEM.NSTC_DBCOMP_TRIGG.TRIGGER_STATUS
  is '������״̬{ENABLE:DISENABLE}';
