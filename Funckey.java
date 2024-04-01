package Calcurator;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.swing.AbstractAction;
import javax.swing.Action;

public class Funckey extends AbstractAction{
	private static final long serialVersionUID = 1L; // eclipseでwarning出たので付け足した
	private Calc thisCalc;
	private static DecimalFormat fmt = new DecimalFormat("0.#########E0"); // 指数表記

	Funckey(String key, Calc thisCalc){
		putValue(Action.NAME, key);
		this.thisCalc = thisCalc;
	}
	
	public void actionPerformed(ActionEvent e) {
		String input = (String)getValue(Action.NAME);
		switch(input) {
		case "AC":
			thisCalc.initialize();
			break;
		case "C":
			if(!thisCalc.error_flag) {thisCalc.field.setText("0");}
			break;
		case "+/-":
			if(!thisCalc.error_flag) {
				String tmp = thisCalc.field.getText();
				if(thisCalc.equals_flag) {thisCalc.initialize();}
				dispBigDecimal(new BigDecimal(tmp).negate());
				thisCalc.num_flag = true;
			}
			break;
		case "%":
			if(!thisCalc.error_flag) {
				String tmp = thisCalc.field.getText();
				if(thisCalc.equals_flag) {thisCalc.initialize();}
				dispBigDecimal(new BigDecimal(tmp).scaleByPowerOfTen(-2));
				thisCalc.num_flag = true;
			}
			break;
		case "÷":
			execCalc(4);
			break;
		case "×":
			execCalc(3);
			break;
		case "-":
			execCalc(2);
			break;
		case "+":
			execCalc(1);
			break;
		case "=":
			thisCalc.equals_flag = true;
			execCalc(thisCalc.operator);
			break;
		}
	}
	
	public void execCalc(int switch_to) {
		if(!thisCalc.error_flag) {
			// 計算後に数字が入力されていない場合はoperatorの更新のみ行う(num_flag = false)
			if(thisCalc.num_flag) {
				thisCalc.operand = new BigDecimal(thisCalc.field.getText());
			}
			// ただし、=がきた場合はnum_flag = falseでもoperandの更新は行わずに計算は行う
			if(thisCalc.num_flag || thisCalc.equals_flag) {
				// (1,2)→(3,4)ならresultとoperatorをtmpに退避
				if((thisCalc.operator == 1 || thisCalc.operator == 2) && (switch_to == 3 || switch_to == 4)) {
					BigDecimal tmp_r = thisCalc.tmp_result;
					thisCalc.tmp_result = thisCalc.result;
					thisCalc.result = tmp_r;
					int tmp_o = thisCalc.tmp_operator;
					thisCalc.tmp_operator = thisCalc.operator;
					thisCalc.operator = tmp_o;
					thisCalc.muldiv_flag = true;
				}
				switch(thisCalc.operator) {
				case 0:
					thisCalc.result = thisCalc.operand;
					break;
				case 1:
					thisCalc.result = thisCalc.result.add(thisCalc.operand);
					break;
				case 2:
					thisCalc.result = thisCalc.result.subtract(thisCalc.operand);
					break;
				case 3:
					thisCalc.result = thisCalc.result.multiply(thisCalc.operand);
					break;
				case 4:
					if(thisCalc.operand.equals(BigDecimal.ZERO)) {
						thisCalc.error_flag = true;
					} else {
						thisCalc.result = thisCalc.result.divide(thisCalc.operand, Calc.MC);
					}
					break;
				}
				// *か/で終わってイコールを連打しても、初回以降はこのflagで通らない
				if(thisCalc.muldiv_flag) {
					// (3,4)→(1,2) ならtmp_operatorに従ってresultを元に戻す
					if((thisCalc.operator == 3 || thisCalc.operator == 4) && (switch_to == 1 || switch_to == 2 || thisCalc.equals_flag)) {
						thisCalc.operator = thisCalc.tmp_operator;
						switch(thisCalc.operator) {
						case 1:
							thisCalc.result = thisCalc.tmp_result.add(thisCalc.result);
							break;
						case 2:
							thisCalc.result = thisCalc.tmp_result.subtract(thisCalc.result);
							break;
						}
						thisCalc.tmp_operator = 0;
						thisCalc.tmp_result = BigDecimal.ZERO;
						thisCalc.muldiv_flag = false;
					}
				}
				if(thisCalc.error_flag) {
					thisCalc.field.setText("エラー");
				} else {
					dispBigDecimal(thisCalc.result);
				}
			}
			thisCalc.operator = switch_to;
			thisCalc.num_flag = false;
		}
	}
	
	public void dispBigDecimal(BigDecimal result) {
		if(result.compareTo(Calc.MAX_LIMIT) == 1 || result.compareTo(Calc.MAX_LIMIT.negate()) == -1) {
			thisCalc.field.setText("エラー");
			thisCalc.error_flag = true;
		} else if(result.compareTo(Calc.MIN_LIMIT) == -1 && result.compareTo(Calc.MIN_LIMIT.negate()) == 1) {
			thisCalc.field.setText("0");
		} else {
			result = result.round(Calc.MC); // 有効数字は9桁まで、超えると四捨五入
			if(result.compareTo(Calc.MAX_EXPONENTIAL_LIMIT.negate()) != 1 || (result.compareTo(Calc.MIN_EXPONENTIAL_LIMIT.negate()) != -1 && result.compareTo(Calc.MIN_EXPONENTIAL_LIMIT) != 1 && result.compareTo(BigDecimal.ZERO) != 0) || result.compareTo(Calc.MAX_EXPONENTIAL_LIMIT) != -1) {
				// 〜-1E9,-1E-8〜1E-8,1E9〜の数字は指数表記(0を除く)
				thisCalc.field.setText(fmt.format(result));
			} else {
				thisCalc.field.setText(result.toPlainString());
			}
		}
	}
}