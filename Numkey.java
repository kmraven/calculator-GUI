package Calcurator;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

public class Numkey extends AbstractAction{
	private static final long serialVersionUID = 1L; // eclipseでwarning出たので付け足した
	private Calc thisCalc;
	
	Numkey(String key, Calc thisCalc){
		putValue(Action.NAME, key);
		this.thisCalc = thisCalc;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(!thisCalc.error_flag) {
			String input = (String)getValue(Action.NAME);
			if(thisCalc.equals_flag) {
				thisCalc.initialize();
			}
			if(!thisCalc.num_flag || thisCalc.field.getText().equals("0")) {
				if(input.equals(".")) {
					thisCalc.field.setText("0."); // 0.~にする
				} else {
					thisCalc.field.setText(input);
				}
				thisCalc.num_flag = true;
			} else {
				if(input.equals(".")) {
					if(!thisCalc.field.getText().contains(".")) {
						thisCalc.field.setText(thisCalc.field.getText() + ".");
					}
				} else {
					thisCalc.field.setText(thisCalc.field.getText() + input);
				}
			}
		}
	}
}