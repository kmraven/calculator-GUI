package Calcurator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Calc extends JFrame{
	private static final long serialVersionUID = 1L; // eclipseでwarning出たので付け足した
	public static MathContext MC = new MathContext(9, RoundingMode.HALF_UP);
	public static BigDecimal MAX_LIMIT = new BigDecimal("1E125");
	public static BigDecimal MIN_LIMIT = new BigDecimal("1E-8");
	public static BigDecimal MAX_EXPONENTIAL_LIMIT = new BigDecimal("1E9");
	public static BigDecimal MIN_EXPONENTIAL_LIMIT = new BigDecimal("1E-8");

	public JTextField field = new JTextField();
	public BigDecimal result;
	public BigDecimal tmp_result;
	public int operator; // 0なら初期設定, 1ならadd, 2ならsub, 3ならmul, 4ならdiv
	public int tmp_operator; // 0なら初期設定, 1ならadd, 2ならsub, 3ならmul, 4ならdiv
	public BigDecimal operand;
	public boolean num_flag;
	public boolean error_flag;
	public boolean muldiv_flag; // mul,divからのイコール連打に対応するために必要
	public boolean equals_flag;
	
	public Calc() {
		super("Calcurator");
		//部品の追加
		JPanel pane = (JPanel)getContentPane();
		pane.add(field, BorderLayout.NORTH);
		JPanel keyPanel = new JPanel(new GridLayout(5,4));
		pane.add(keyPanel, BorderLayout.CENTER);
		
		field.setEditable(false); // 直接入力を禁止する
		field.setHorizontalAlignment(JTextField.RIGHT); // 右寄せ表示
		initialize();
		
		keyPanel.add(new JButton(new Funckey("AC", this)));
		keyPanel.add(new JButton(new Funckey("C", this)));
		keyPanel.add(new JButton(new Funckey("+/-", this)));
		keyPanel.add(new JButton(new Funckey("%", this)));
		keyPanel.add(new JButton(new Numkey("7", this)));
		keyPanel.add(new JButton(new Numkey("8", this)));
		keyPanel.add(new JButton(new Numkey("9", this)));
		keyPanel.add(new JButton(new Funckey("÷", this)));
		keyPanel.add(new JButton(new Numkey("4", this)));
		keyPanel.add(new JButton(new Numkey("5", this)));
		keyPanel.add(new JButton(new Numkey("6", this)));
		keyPanel.add(new JButton(new Funckey("×", this)));
		keyPanel.add(new JButton(new Numkey("1", this)));
		keyPanel.add(new JButton(new Numkey("2", this)));
		keyPanel.add(new JButton(new Numkey("3", this)));
		keyPanel.add(new JButton(new Funckey("-", this)));
		keyPanel.add(new JButton(new Numkey("0", this)));
		keyPanel.add(new JButton(new Numkey(".", this)));
		keyPanel.add(new JButton(new Funckey("=", this)));
		keyPanel.add(new JButton(new Funckey("+", this)));
	}
	
	public void initialize() {
		field.setText("0");
		operator = 0;
		tmp_operator = 0;
		result = BigDecimal.ZERO;
		tmp_result = BigDecimal.ZERO;
		operand = BigDecimal.ZERO;
		num_flag = false;
		error_flag = false;
		muldiv_flag = false;
		equals_flag = false;
	}
	
	public static void main(String[] args) {
		Calc calc = new Calc();
		calc.setDefaultCloseOperation(EXIT_ON_CLOSE);
		calc.setSize(500, 500);
		calc.setVisible(true);
	}
}