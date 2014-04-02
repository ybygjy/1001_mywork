package org.ybygjy.dbcompare.report;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ybygjy.dbcompare.TaskReport;
import org.ybygjy.dbcompare.model.AbstractObjectFieldMeta;
import org.ybygjy.dbcompare.model.AbstractObjectMeta;
import org.ybygjy.dbcompare.model.ConstraintMeta;
import org.ybygjy.dbcompare.model.ContextModel;
import org.ybygjy.dbcompare.model.MetaConstant;
import org.ybygjy.dbcompare.model.MetaType;
import org.ybygjy.dbcompare.model.TriggerMeta;
import org.ybygjy.dbcompare.model.TypeMeta;


/**
 * �����з�ʽ��������
 * @author WangYanCheng
 * @version 2011-10-9
 */
public class CommandReport implements TaskReport {
    /** ������������� */
    private PrintWriter commandPrintWriter;
    private String srcUser;
    private String targetUser;
    /** ת�������� */
    private PrintStream restoreStream;
    private Map<MetaType, InnerWork> innerReportObj;
    private int defStep = 20;
    /**����*/
    private static String lineFeed = "\r\n";

    /**
     * Constructor
     */
    public CommandReport() {
        commandPrintWriter = new PrintWriter(System.out);
        innerReportObj = new HashMap<MetaType, InnerWork>();
        innerReportObj.put(MetaType.TABLE_OBJ, new TableObjCompReport());
        innerReportObj.put(MetaType.VIEW_OBJ, new ViewObjCompReport());
        innerReportObj.put(MetaType.TABLE_FIELDOBJ, new TableFieldObjCompReport());
        innerReportObj.put(MetaType.SEQ_OBJ, new SequenceObjCompReport());
        innerReportObj.put(MetaType.TRIG_OBJ, new TriggerObjCompReport());
        innerReportObj.put(MetaType.PROC_OBJ, new ProcedureObjCompReport());
        innerReportObj.put(MetaType.FUNC_OBJ, new FunctionObjCompReport());
        innerReportObj.put(MetaType.TYPE_OBJ, new TypeObjCompReport());
        innerReportObj.put(MetaType.CONS_OBJ, new ConstraintObjCompReport());
        innerReportObj.put(MetaType.INVALID_OBJ, new InvalidObjCompReport());
    }

    /**
     * {@inheritDoc}
     */
    public void setReOutputStream(OutputStream ous) {
        if (null == ous) {
            return;
        }
        restoreStream = new PrintStream(ous);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setSrcUser(String srcUser) {
    	this.srcUser = srcUser.concat(".");
	}
    
    /**
     * {@inheritDoc}
     */
	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser.concat(".");
	}

	/**
     * {@inheritDoc}
     */
    public void generateReport(ContextModel[] commonModels) {
        for (ContextModel contextModel : commonModels) {
            if (!contextModel.getTaskInfo().isFinished()) {
                outputTaskInfo(contextModel);
                continue;
            }
            switch (contextModel.getTaskInfo().getTaskType()) {
	            case CONS_OBJ:
	            	innerReportObj.get(MetaType.CONS_OBJ).doWork(contextModel);
	            	break;
                case TABLE_OBJ:
                    innerReportObj.get(MetaType.TABLE_OBJ).doWork(contextModel);
                    break;
                case VIEW_OBJ:
                    innerReportObj.get(MetaType.VIEW_OBJ).doWork(contextModel);
                    break;
                case TABLE_FIELDOBJ:
                    innerReportObj.get(MetaType.TABLE_FIELDOBJ).doWork(contextModel);
                    break;
                case SEQ_OBJ:
                    innerReportObj.get(MetaType.SEQ_OBJ).doWork(contextModel);
                    break;
                case TRIG_OBJ:
                    innerReportObj.get(MetaType.TRIG_OBJ).doWork(contextModel);
                    break;
                case PROC_OBJ:
                    innerReportObj.get(MetaType.PROC_OBJ).doWork(contextModel);
                    break;
                case FUNC_OBJ:
                    innerReportObj.get(MetaType.FUNC_OBJ).doWork(contextModel);
                    break;
                case TYPE_OBJ:
                    innerReportObj.get(MetaType.TYPE_OBJ).doWork(contextModel);
                    break;
                case INVALID_OBJ:
                	innerReportObj.get(MetaType.INVALID_OBJ).doWork(contextModel);
                	break;
                default:
                    break;
            }
            outputTaskInfo(contextModel);
            flush();
        }
    }
    private void outputSeperator() {
    	StringBuilder sbud = new StringBuilder();
    	for (int i = 0; i < 100; i++) {
    		sbud.append("*");
    	}
    	outputLine(sbud.toString());
    }
    /**
     * ע�⻻�з���֧������\nֻ֧����Window�������ǻ��еģ���Other OS��Ҫ\r\n
     */
    private void outputLine() {
        print("", "\r\n");
    }

    private void print(String msg, String fmt) {
        commandPrintWriter.printf(fmt, msg);
        if (restoreStream != null) {
            restoreStream.printf(fmt, msg);
        }
    }

    private void flush() {
        commandPrintWriter.flush();
        if (null != restoreStream) {
            restoreStream.flush();
        }
    }

    /**
     * ���������
     * @param str str
     */
    private void outputLine(String str) {
        outputMsg(str);
        outputLine();
    }

    private void outputMsg(String message) {
        print("%-40s", (null == message) ? " " : message);
    }

    private void outputMsg(int stepSize, String message) {
        print(("%-40s"), "  ".concat((null == message) ? "" : message));
    }

    private void outputMapEntry(Map<String, String> tmpMap) {
        for (Iterator<Map.Entry<String, String>> iterator = tmpMap.entrySet().iterator(); iterator
            .hasNext();) {
            Map.Entry<String, String> entry = iterator.next();
            outputMsg(entry.getKey().toString());
            outputMsg(defStep, entry.getValue().toString());
            outputLine();
        }
    }

    private void outputTaskInfo(ContextModel cm) {
        StringBuilder sbud = new StringBuilder();
        sbud.append(cm.getTaskInfo().getTaskName())
            .append(cm.getTaskInfo().isFinished() ? "����ִ�����" : "��������").append("\n");
        outputMsg(sbud.toString());
        outputSeperator();
    }

    private void outputList(List<AbstractObjectMeta> tmpArr, String prefix) {
        for (Iterator<AbstractObjectMeta> iterator = tmpArr.iterator(); iterator.hasNext();) {
            AbstractObjectMeta tableObj = iterator.next();
            outputMsg(defStep, prefix.concat(tableObj.getObjectName()));
            outputLine();
        }
    }

    /**
     * ��֤�ֶ�����ԣ����Կո񡢻��е��ַ�
     * @param srcStr Դ�ַ���
     * @return true/false
     */
    private boolean testEquals4FieldDefValue(String srcStr) {
    	String[] srcStrArr = srcStr.split("!=");
    	if (srcStrArr.length != 2) {
    		return false;
    	}
    	srcStrArr[0] = srcStrArr[0].trim();
    	srcStrArr[1] = srcStrArr[1].trim();
    	if (srcStrArr[0].equals(srcStrArr[1])) {
    		return true;
    	}
    	return false;
    }
    /**
     * �ڲ���ǽӿ�
     * @author WangYanCheng
     * @version 2011-10-9
     */
    interface InnerWork {
        /**
         * doWork
         * @param contextModel ����������
         */
        public void doWork(ContextModel contextModel);
    }

    /**
     * �����
     * @author WangYanCheng
     * @version 2011-10-9
     */
    class TableObjCompReport implements InnerWork {
        /**
         * {@inheritDoc}
         */
        public void doWork(ContextModel contextModel) {
            Map<String, String> countMap = (Map<String, String>) contextModel.getRawData(MetaConstant.OBJ_COUNT);
            outputLine("���������");
            outputMapEntry(countMap);
            outputLine();
            Map<String, List<AbstractObjectMeta>> lostAndExcessMap = (Map<String, List<AbstractObjectMeta>>) contextModel.getRawData(MetaConstant.OBJ_LOSTANDEXCESS);
            List<AbstractObjectMeta> tmpObjs = lostAndExcessMap.get(MetaConstant.OBJ_LOST);
            outputLine("�����ȱʧ\t\t" + tmpObjs.size());
            outputLine("\tȱʧ��������");
            innerOutputList(tmpObjs, targetUser);
            tmpObjs = lostAndExcessMap.get(MetaConstant.OBJ_EXCESS);
            outputLine("��������\t\t" + tmpObjs.size());
            outputLine("\t�����������");
            innerOutputList(tmpObjs, srcUser);
            outputLine();
            flush();
        }

        private void innerOutputList(List<AbstractObjectMeta> tabArr, String prefix) {
            for (Iterator<AbstractObjectMeta> iterator = tabArr.iterator(); iterator.hasNext();) {
                outputMsg(defStep, prefix + (iterator.next().getObjectName()));
                outputLine();
            }
        }
    }

    /**
     * ���ֶζ���
     * @author WangYanCheng
     * @version 2011-10-9
     */
    class TableFieldObjCompReport implements InnerWork {
        /**
         * {@inheritDoc}
         */
        public void doWork(ContextModel contextModel) {
            Map<String, Map<String, AbstractObjectMeta>> lostAndExcessMap = (Map<String, Map<String, AbstractObjectMeta>>) contextModel.getRawData(MetaConstant.OBJ_LOSTANDEXCESS);
            if (null == lostAndExcessMap) {
            	return;
            }
            Map<String, AbstractObjectMeta> tmpMap = lostAndExcessMap.get(MetaConstant.OBJ_LOST);
            outputLine();
            outputLine("�ֶζ���ȱʧ��Ϣ.......................................................");
            innerOutput(lostAndExcessMap.get(MetaConstant.OBJ_LOST), true);
            innerOutput(lostAndExcessMap.get(MetaConstant.OBJ_EXCESS), false);
            tmpMap = (Map<String, AbstractObjectMeta>) contextModel.getRawData(MetaConstant.OBJ_COMPAREDETAIL);
            outputLine();
            if (null == tmpMap) {
            	return;
            }
            outputLine("�ֶζ���ṹ�Ƚ���ϸ...................................................");
            innerOutput(tmpMap);
            lostAndExcessMap = null;
            tmpMap = null;
            flush();
        }

        private void innerOutput(Map<String, AbstractObjectMeta> tmpMap) {
            for (Iterator<Map.Entry<String, AbstractObjectMeta>> iterator = tmpMap.entrySet().iterator(); iterator
                .hasNext();) {
                Map.Entry<String, AbstractObjectMeta> tmpEntry = iterator.next();
                AbstractObjectMeta tmpTab = tmpEntry.getValue();
                List<AbstractObjectFieldMeta> tableFields = tmpTab.getObjectFieldArr();
                StringBuilder sbud = new StringBuilder();
                sbud.append("��/��ͼ����\t\t".concat(srcUser).concat(tmpEntry.getKey().toString()).concat("\t\t�ֶβ�ƥ������\t".concat(String.valueOf(tableFields.size()))))
                	.append(lineFeed);
                if (tableFields.size() < 1) {
                    continue;
                }
                sbud.append("�ֶ���ϸ��");
                
                // ����list
                StringBuilder sbud2 = new StringBuilder();
                for (Iterator<AbstractObjectFieldMeta> iterator1 = tableFields.iterator(); iterator1.hasNext();) {
                    AbstractObjectFieldMeta tfoInst = iterator1.next();
                    String tmpStr = outputFieldDetail(tfoInst);
                    if (tmpStr != null) {
                    	sbud2.append(tmpStr).append(lineFeed);
                    }
                }
                if (sbud2.length() > 0) {
                	outputLine(sbud.toString());
                	outputLine(sbud2.toString());
                }
            }
        }

        private void innerOutput(Map<String, AbstractObjectMeta> tmpMap, boolean lostOrExcess) {
            for (Iterator<Map.Entry<String, AbstractObjectMeta>> iterator = tmpMap.entrySet().iterator(); iterator
                .hasNext();) {
                Map.Entry<String, AbstractObjectMeta> tmpEntry = iterator.next();
                AbstractObjectMeta tmpTab = tmpEntry.getValue();
                List<AbstractObjectFieldMeta> tableFields = tmpTab.getObjectFieldArr();
                outputLine("��/��ͼ����\t".concat(tmpEntry.getKey().toString()).concat((lostOrExcess ? "\tȱʧ" : "\t����").concat("�ֶ����� [").concat(
                		tableFields.size() + "]")));
                if (tableFields.size() < 1) {
                    continue;
                }
                // ����list
                for (Iterator<AbstractObjectFieldMeta> iterator1 = tableFields.iterator(); iterator1.hasNext();) {
                	outputLine("\t\t\t".concat(iterator1.next().getFieldCode()));
                }
            }
        }

        private String outputFieldDetail(AbstractObjectFieldMeta tfoInst) {
            String tmp = tfoInst.getFieldType();
            StringBuilder sbud = new StringBuilder();
            if (null != tmp) {
            	sbud.append("\t\t�ֶ����ͣ�").append(tmp);
            }
            tmp = tfoInst.getFieldLen();
            if (tmp != null) {
            	sbud.append("\t\t�ֶγ��ȣ�").append(tmp);
            }
            tmp = tfoInst.getFieldPre();
            if (null != tmp) {
            	sbud.append("\t\t�ֶξ��ȣ�").append(tmp);
            }
            tmp = tfoInst.getFieldSca();
            if (null != tmp) {
            	sbud.append("\t\t�ֶ�С��λ����").append(tmp);
            }
            tmp = tfoInst.getFieldNull();
            if (null != tmp) {
            	sbud.append("\t\t�ֶηǿձ�ǣ�").append(tmp);
            }
            tmp = tfoInst.getFieldDefValue();
            if (null != tmp && !testEquals4FieldDefValue(tmp)) {
            	/*
            	if (null != tfoInst.getFieldDefvLen()) {
             		sbud.append("\t\t�ֶ�Ĭ��ֵ���ȣ�").append(tfoInst.getFieldDefvLen());
            	}
            	*/
            	sbud.append("\t\t�ֶ�Ĭ��ֵ��").append(tmp);
            }
            tmp = tfoInst.getFieldOrder();
            if (null != tmp) {
            	sbud.append("\t\t�ֶ�˳��").append(tmp);
            }
            if (sbud.length() > 0) {
            	return ("\t\t\t�ֶα��룺" + tfoInst.getFieldCode().concat("\t\t").concat(sbud.toString()));
            }
            return null;
        }
    }

    /**
     * ���ж���
     * @author WangYanCheng
     * @version 2011-10-9
     */
    class SequenceObjCompReport implements InnerWork {
        /**
         * {@inheritDoc}
         */
        public void doWork(ContextModel contextModel) {
            Map<String, List<AbstractObjectMeta>> tmpMap = (Map<String, List<AbstractObjectMeta>>) contextModel.getRawData(MetaConstant.OBJ_LOSTANDEXCESS);
            List<AbstractObjectMeta> tmpList = tmpMap.get(MetaConstant.OBJ_LOST);
            if (tmpList.size() > 0) {
            	outputMsg("���ж���ȱʧ��" + tmpList.size());
            	outputLine();
            	outputLine("ȱʧ��ϸ��");
            	doPrintGeneralObj(tmpList, targetUser);
            }
            tmpList = tmpMap.get(MetaConstant.OBJ_EXCESS);
            if (tmpList.size() > 0) {
            	outputMsg("���ж�����ࣺ" + tmpList.size());
            	outputLine();
            	outputLine("������ϸ��");
            	doPrintGeneralObj(tmpList, srcUser);
            }
            flush();
        }

        private void doPrintGeneralObj(List<AbstractObjectMeta> objList, String prefix) {
            for (Iterator<AbstractObjectMeta> iterator = objList.iterator(); iterator.hasNext();) {
                AbstractObjectMeta go = iterator.next();
                outputMsg(defStep, prefix.concat(go.getObjectName()));
                outputLine();
            }
        }
    }

    /**
     * ����������
     * @author WangYanCheng
     * @version 2011-10-10
     */
    class TriggerObjCompReport implements InnerWork {

        /**
         * {@inheritDoc}
         */
        public void doWork(ContextModel contextModel) {
            Map<String, Map<String, AbstractObjectMeta>> lostAndExcessMap = (Map<String, Map<String, AbstractObjectMeta>>) contextModel.getRawData(MetaConstant.OBJ_LOSTANDEXCESS);
            Map<String, AbstractObjectMeta> tmpMap = lostAndExcessMap.get(MetaConstant.OBJ_LOST);
            if (null != tmpMap) {
            	outputLine("ȱʧ��������ϸ");
            	outputMsg(defStep, "�����������\t\t\t����������\t\t\t������Ϣ");
            	outputLine();
            	innerOutput(tmpMap, targetUser);
            }
            tmpMap = lostAndExcessMap.get(MetaConstant.OBJ_EXCESS);
            if (null != tmpMap) {
            	outputLine("���ഥ������ϸ");
            	outputMsg(defStep, "�����������\t\t\t����������\t\t\t������Ϣ");
            	outputLine();
            	innerOutput(tmpMap, srcUser);
            }
            tmpMap = (Map<String, AbstractObjectMeta>) contextModel.getRawData(MetaConstant.OBJ_COMPAREDETAIL);
            if (null != tmpMap) {
            	outputLine("�������������ݱȽ���ϸ");
            	outputMsg(defStep, "�����������\t\t\t����������\t\t\t������Ϣ");
            	outputLine();
            	innerOutput(tmpMap, "");
            }
            flush();
        }

        private void innerOutput(Map<String, AbstractObjectMeta> tmpMap, String prefix) {
            for (Iterator<String> iterator = tmpMap.keySet().iterator(); iterator.hasNext();) {
                String tableCode = iterator.next();
                AbstractObjectMeta tmpTOInst = tmpMap.get(tableCode);
                List<TriggerMeta> tmpList = tmpTOInst.getTriggerObjArr();
                outputMsg(defStep, tableCode + " [" + tmpList.size() + "]");
                outputLine();
                for (Iterator<TriggerMeta> iterator1 = tmpList.iterator(); iterator1.hasNext();) {
                    TriggerMeta triObj = iterator1.next();
                    outputMsg("\t\t\t\t");
                    outputMsg(prefix.concat(triObj.getTriggName()).concat("\t\t").concat(triObj.getTriggType() == null ? "" : triObj.getTriggType()));
                    outputLine();
                }
            }
            outputLine();
        }
    }

    /**
     * ��ͼ���󱨱�
     * @author WangYanCheng
     * @version 2011-10-10
     */
    class ViewObjCompReport implements InnerWork {
        /**
         * {@inheritDoc}
         */
        public void doWork(ContextModel contextModel) {
        	Map<String, String> countMap = (Map<String, String>) contextModel.getRawData(MetaConstant.OBJ_COUNT);
        	if (null != countMap) {
        		outputLine("��ͼ��������ͳ�ƣ�");
        		outputMapEntry(countMap);
        		outputLine();
        	}
            Map<String, List<AbstractObjectMeta>> lostAndExcessArr = (Map<String, List<AbstractObjectMeta>>) contextModel.getRawData(MetaConstant.OBJ_LOSTANDEXCESS);
            if (null != lostAndExcessArr) {
            	List<AbstractObjectMeta> tmpArr = lostAndExcessArr.get(MetaConstant.OBJ_LOST);
            	outputLine("��ͼ����ȱʧ\t������".concat(String.valueOf(tmpArr.size())));
            	outputList(tmpArr, targetUser);
            	outputLine();
            	tmpArr = lostAndExcessArr.get(MetaConstant.OBJ_EXCESS);
            	outputLine("��ͼ�������\t������".concat(String.valueOf(tmpArr.size())));
            	outputLine();
            	outputList(tmpArr, srcUser);
            }
            flush();
        }
    }

    /**
     * �洢���̶���Ƚϱ���
     * @author WangYanCheng
     * @version 2011-10-10
     */
    class ProcedureObjCompReport implements InnerWork {
        /**
         * {@inheritDoc}
         */
        public void doWork(ContextModel contextModel) {
            Map<String, AbstractObjectMeta> lostAndExcessColl = (Map<String, AbstractObjectMeta>) contextModel.getRawData(MetaConstant.OBJ_LOSTANDEXCESS);
            List<AbstractObjectMeta> tmpArr = (List<AbstractObjectMeta>) lostAndExcessColl.get(MetaConstant.OBJ_LOST);
            outputLine("�洢���̶���ȱʧ\t������".concat(String.valueOf(tmpArr.size())));
            outputList(tmpArr, targetUser);
            tmpArr = (List<AbstractObjectMeta>) lostAndExcessColl.get(MetaConstant.OBJ_EXCESS);
            outputLine("�洢���̶������\t������".concat(String.valueOf(tmpArr.size())));
            outputList(tmpArr, srcUser);
            outputLine();
            flush();
        }
    }

    /**
     * ��������Ƚϱ���
     * @author WangYanCheng
     * @version 2011-10-10
     */
    class FunctionObjCompReport implements InnerWork {
        /**
         * {@inheritDoc}
         */
        public void doWork(ContextModel contextModel) {
            Map<String, AbstractObjectMeta> lostAndExcessColl = (Map<String, AbstractObjectMeta>) contextModel.getRawData(MetaConstant.OBJ_LOSTANDEXCESS);
            List<AbstractObjectMeta> tmpArr = (List<AbstractObjectMeta>) lostAndExcessColl.get(MetaConstant.OBJ_LOST);
            outputLine("��������ȱʧ\t������".concat(String.valueOf(tmpArr.size())));
            outputList(tmpArr, srcUser);
            tmpArr = (List<AbstractObjectMeta>) lostAndExcessColl.get(MetaConstant.OBJ_EXCESS);
            outputLine("�����������\t������".concat(String.valueOf(tmpArr.size())));
            outputList(tmpArr, targetUser);
            outputLine();
            flush();
        }
    }

    /**
     * ���Ͷ���Ƚϱ���
     * @author WangYanCheng
     * @version 2011-10-10
     */
    class TypeObjCompReport implements InnerWork {
        /**
         * {@inheritDoc}
         */
        public void doWork(ContextModel contextModel) {
            Map<String, TypeMeta> lostAndExcessColl = (Map<String, TypeMeta>) contextModel.getRawData(MetaConstant.OBJ_LOSTANDEXCESS);
            List<TypeMeta> tmpArr = (List<TypeMeta>) lostAndExcessColl.get(MetaConstant.OBJ_LOST);
            outputLine("���Ͷ���ȱʧ\t������".concat(String.valueOf(tmpArr.size())));
            outputMsg(defStep, "��������\t\t\t\t�������");
            outputLine();
            outputList(tmpArr, targetUser);
            tmpArr = (List<TypeMeta>) lostAndExcessColl.get(MetaConstant.OBJ_EXCESS);
            outputLine("���Ͷ������\t������".concat(String.valueOf(tmpArr.size())));
            if (tmpArr.size() > 0) {
            	outputMsg(defStep, "��������\t\t\t\t�������");
            	outputLine();
            	outputList(tmpArr, srcUser);
            }
            outputLine();
            flush();
        }

        private void outputList(List<TypeMeta> tmpArr, String prefix) {
            for (Iterator<TypeMeta> iterator = tmpArr.iterator(); iterator.hasNext();) {
                TypeMeta tableObj = iterator.next();
                outputMsg(defStep, prefix.concat(tableObj.getTypeName()).concat("\t\t\t\t").concat(tableObj.getTypeCode()));
                outputLine();
            }
        }
    }

    /**
     * ����Լ���ṹ�Ƚϱ���
     * @author WangYanCheng
     * @version 2011-10-17
     */
    class ConstraintObjCompReport implements InnerWork {
    	/**
    	 * {@inheritDoc}
    	 */
		public void doWork(ContextModel contextModel) {
			Map<String, List<AbstractObjectMeta>> lostAndExcessColl = (Map<String, List<AbstractObjectMeta>>) contextModel.getRawData(MetaConstant.OBJ_LOSTANDEXCESS);
			List<AbstractObjectMeta> tmpList = lostAndExcessColl.get(MetaConstant.OBJ_LOST);
			outputLine("����Լ��ȱʧ��[" +  tmpList.size() + "]");
			if (tmpList != null && tmpList.size() > 0) {
				innerOutput(tmpList);
			}
			tmpList = lostAndExcessColl.get(MetaConstant.OBJ_EXCESS);
			outputLine("����Լ�����ࣺ[" + tmpList.size() + "]");
			if (tmpList != null && tmpList.size() > 0) {
				innerOutput(tmpList);
			}
			outputLine();
			flush();
		}

		/**
		 * innerOutput
		 * @param innerList �����б�
		 */
		private void innerOutput(List<AbstractObjectMeta> innerList) {
			outputLine("�����������\t\tԼ������\t");
			for (Iterator<AbstractObjectMeta> iterator = innerList.iterator(); iterator.hasNext();) {
				AbstractObjectMeta objInst = iterator.next();
				List<ConstraintMeta> tmpList = objInst.getConstraintArr();
				outputLine(objInst.getObjectName().concat(" [" + tmpList.size() + "]"));
				if (tmpList.size() > 0) {
					for (Iterator<ConstraintMeta> iterator1 = tmpList.iterator(); iterator1.hasNext();) {
						ConstraintMeta dbcInst = iterator1.next();
						outputLine("\t\t\t\t".concat(dbcInst.getConsName()));
					}
				}
			}
			outputLine();
		}
    }

    /**
     * ״̬�Ƿ�����
     * @author WangYanCheng
     * @version 2011-10-19
     */
    class InvalidObjCompReport implements InnerWork {
    	/**
    	 * {@inheritDoc}
    	 */
		public void doWork(ContextModel contextModel) {
			Map<MetaType, List<AbstractObjectMeta>> invalidObjArr = (Map<MetaType, List<AbstractObjectMeta>>) contextModel.getRawData(MetaConstant.OBJ_INVALIDDETAIL);
			if (null == invalidObjArr) {
				return;
			}
			List<AbstractObjectMeta> tmpArr = null;
			for (Iterator<MetaType> iterator = invalidObjArr.keySet().iterator();iterator.hasNext();) {
				MetaType metaType = iterator.next();
				tmpArr = invalidObjArr.get(metaType);
				if (tmpArr.size() <= 0) {
					continue;
				}
				switch (metaType) {
					case FUNC_OBJ:
						innerPrint("����", tmpArr);
						break;
					case PROC_OBJ:
						innerPrint("����", tmpArr);
						break;
					case VIEW_OBJ:
						innerPrint("��ͼ", tmpArr);
						break;
					case TRIG_OBJ:
						innerPrint("������", tmpArr);
						break;
					case PACKAGE_OBJ:
						innerPrint("������", tmpArr);
						break;
					default:
						innerPrint("δ֪����", tmpArr);
						break;
				}
			}
		}

		/**
		 * �ڲ����
		 * @param title ͷ��Ϣ
		 * @param tmpArr ʵ����
		 */
		public void innerPrint(String title, List<AbstractObjectMeta> tmpArr) {
			outputLine(title.concat("����Ƿ������� " + tmpArr.size()));
			outputLine("\t\t".concat(title).concat("��������"));
			for (Iterator<AbstractObjectMeta> iterator = tmpArr.iterator(); iterator.hasNext();) {
				AbstractObjectMeta dbomInst = iterator.next();
				outputLine("\t\t\t".concat(srcUser).concat(dbomInst.getObjectName()));
			}
		}
    }
}
