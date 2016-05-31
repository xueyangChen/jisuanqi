package com.example.jisuanqi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class JiSuanQi extends Activity{
	//����һЩ����
	//0~9ʮ����ť
	private Button[] btn = new Button[10];	
	public static EditText input;		//��ʾ����������ʾ������
	public static TextView mem;		//��ʾ���·��ļ����������ڼ�¼��һ�μ�����
	private TextView _drg;		//���Ǽ���ʱ��־��ʾ���ǶȻ��ǻ���
	public static TextView tip;		//С��ʾ�����ڼ�ǿ�˻�����������⡢��ʾ
	private Button
		divide,mul,sub,add,equal,		//�£���������������
		sin,cos,tan,log,ln,				//����
		sqrt,square,factorial,bksp,		//���ţ�ƽ�����׳ˣ��˸�
		left,right,dot,exit,drg,		//��������.���˳�,�ǶȻ��ȿ��Ƽ�
		mc,c;							//mem��������input������
	
	public static String str_old;		//����ԭ������ʽ���ӣ�Ϊ�����ʱ�ÿ��������ʱ��ʽ���ӱ��ı�
	public String str_new;		//�任���Ӻ��ʽ��
	public boolean vbegin = true;	//������ƣ�trueΪ�������룬falseΪ��������
	public static boolean drg_flag = true;		//����DRG������trueΪ�Ƕȣ�falseΪ����
	public static double pi = 4*Math.atan(1);	//��ֵ��3.14
	public boolean tip_lock = true;		//true��ʾ��ȷ��������ȷ���룻false��ʾ�������뱻����
	public boolean equals_flag = true;	//�ж��Ƿ��ǰ�=֮������룬true��ʾ������=֮ǰ��false��֮
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//��ȡ����Ԫ��
		input = (EditText) findViewById(R.id.input);
		mem = (TextView) findViewById(R.id.mem);
		_drg = (TextView) findViewById(R.id._drg);
		tip = (TextView) findViewById(R.id.tip);
		btn[0] = (Button) findViewById(R.id.zero);
		btn[1] = (Button) findViewById(R.id.one);
		btn[2] = (Button) findViewById(R.id.two);
		btn[3] = (Button) findViewById(R.id.three);
		btn[4] = (Button) findViewById(R.id.four);
		btn[5] = (Button) findViewById(R.id.five);
		btn[6] = (Button) findViewById(R.id.six);
		btn[7] = (Button) findViewById(R.id.seven);
		btn[8] = (Button) findViewById(R.id.eight);
		btn[9] = (Button) findViewById(R.id.nine);
		divide = (Button) findViewById(R.id.divide);
		mul = (Button) findViewById(R.id.mul);
		sub = (Button) findViewById(R.id.sub);
		add = (Button) findViewById(R.id.add);
		equal = (Button) findViewById(R.id.equal);
		sin = (Button) findViewById(R.id.sin);
		cos = (Button) findViewById(R.id.cos);
		tan = (Button) findViewById(R.id.tan);
		log = (Button) findViewById(R.id.log);
		ln = (Button) findViewById(R.id.ln);
		sqrt = (Button) findViewById(R.id.sqrt);
		square = (Button) findViewById(R.id.square);
		factorial = (Button) findViewById(R.id.factorial);
		bksp = (Button) findViewById(R.id.bksp);
		left = (Button) findViewById(R.id.left);
		right = (Button) findViewById(R.id.right);
		dot = (Button) findViewById(R.id.dot);
		exit = (Button) findViewById(R.id.exit);
		drg = (Button) findViewById(R.id.drg);
		mc = (Button) findViewById(R.id.mc);
		c = (Button) findViewById(R.id.c);	
		
		//ע�������
		//Ϊ���ְ�ť�󶨼�����
		for(int i=0;i<10;i++){	
			btn[i].setOnClickListener(actionPerformed);
		}
		//Ϊ+��-�������µȰ�ťע�������
		divide.setOnClickListener(actionPerformed);
		mul.setOnClickListener(actionPerformed);
		sub.setOnClickListener(actionPerformed);
		add.setOnClickListener(actionPerformed);
		equal.setOnClickListener(actionPerformed);
		sin.setOnClickListener(actionPerformed);
		cos.setOnClickListener(actionPerformed);
		tan.setOnClickListener(actionPerformed);
		log.setOnClickListener(actionPerformed);
		ln.setOnClickListener(actionPerformed);
		sqrt.setOnClickListener(actionPerformed);
		square.setOnClickListener(actionPerformed);
		factorial.setOnClickListener(actionPerformed);
		bksp.setOnClickListener(actionPerformed);
		left.setOnClickListener(actionPerformed);
		right.setOnClickListener(actionPerformed);
		dot.setOnClickListener(actionPerformed);
		exit.setOnClickListener(actionPerformed);
		drg.setOnClickListener(actionPerformed);
		mc.setOnClickListener(actionPerformed);
		c.setOnClickListener(actionPerformed);
	}
	
	/*
	 * �������׽(��������ʵ��)
	 */
	//����棬���ڼ������ĺϷ���
	String[] Tipcommand = new String[500];
	int tip_i = 0;		//Tipcommand��ָ��
	
	private OnClickListener actionPerformed = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String command = ((Button)v).getText().toString();	//�����ϵ������
			String str = input.getText().toString();	//��ʾ���ϵ��ַ���
			//��������Ƿ�Ϸ�
			//��
			if(equals_flag == false && "0123456789.()sincostanlnlogn!+-���¡�^".indexOf(command) != -1){
				//�����ʾ���ϵ��ַ����Ƿ�Ϸ�
				if(right(str)){	//�Ϸ�
					if("+-���¡�^".indexOf(command) != -1){
						for(int i=0;i<str.length();i++){
							Tipcommand[tip_i] = String.valueOf(str.charAt(i));
							tip_i ++;
						}
						vbegin = false;		//falseΪ��������
					}
				}else{	//���Ϸ�
					input.setText("0");	//��input���������
					vbegin = true;	//trueΪ��������
					tip_i = 0;	//ָ������Ϊ0
					tip_lock = true;	//����������������
					tip.setText("��ӭʹ�ã�");
				}
				equals_flag = true;	//true��ʾ��=֮ǰ����
			}
			//��
			if(tip_i > 0)
				TipChecker(Tipcommand[tip_i-1],command);
			else if(tip_i == 0)
				TipChecker("#",command);
			//��
			if("0123456789.()sincostanlnlogn!+-���¡�^".indexOf(command) != -1 && tip_lock){
				Tipcommand[tip_i] = command;
				tip_i ++;
			}
			
			//��������ȷ������Ϣ��ʾ����ʾ����
			if("0123456789.()sincostanlnlogn!+-���¡�^".indexOf(command) != -1 && tip_lock){
				//��25������
				print(command);
			}else if(command.compareTo("DRG") == 0 && tip_lock){
				//���������DRG,���л���ǰ���ȽǶ��ƣ������л���Ľ����ʾ�������Ϸ�
				if(drg_flag == true){
					drg_flag = false;
					_drg.setText("  RAD");
				}else{
					drg_flag = true;
					_drg.setText("  DEG");
				}
			}else if(command.compareTo("Bksp") == 0 && equals_flag){
				//����������˸�����������ڰ�=֮ǰ
				//һ��ɾ�������ַ�
				if(TTO(str) == 3){
					if(str.length() > 3)
						input.setText(str.substring(0, str.length() - 3));
					else if(str.length() == 3){
						input.setText("0");
						vbegin = true;
						tip_i = 0;
						tip.setText("��ӭʹ�ã�");
					}
				}else if(TTO(str) == 2){	//һ��ɾ�������ַ�
					if(str.length() > 2)
						input.setText(str.substring(0, str.length() -2));
					else if(str.length() == 2){
						input.setText("0");
						vbegin = true;
						tip_i = 0;
						tip.setText("��ӭʹ�ã�");
					}
				}else if(TTO(str) == 1){	//һ��ɾ��һ���ַ�
					//��֮ǰ������ַ����Ϸ�����ɾ��һ���ַ�
					if(right(str)){
						if(str.length() > 1)
							input.setText(str.substring(0, str.length() -1));
						else if(str.length() == 1){
							input.setText("0");
							vbegin = true;
							tip_i = 0;
							tip.setText("��ӭʹ�ã�");
						}
					}else{	//��֮ǰ������ַ������Ϸ�����ɾ��ȫ���ַ�
						input.setText("0");
						vbegin = true;
						tip_i = 0;
						tip.setText("��ӭʹ�ã�");
					}
				}
				
				if(input.getText().toString().compareTo("-") == 0 || equals_flag == false){
					input.setText("0");
					vbegin = true;
					tip_i = 0;
					tip.setText("��ӭʹ�ã�");
				}
				tip_lock = true;
				if(tip_i > 0)	tip_i--;
			}else if(command.compareTo("Bksp")==0 && equals_flag == false){
				//������ڰ�=֮�������˸��
				input.setText("0");	//����ʾ����������Ϊ0
				vbegin = true;
				tip_i = 0;
				tip_lock = true;
				tip.setText("��ӭʹ�ã�");
			}else if(command.compareTo("C") == 0){
				//���������������
				input.setText("0");	//����ʾ����������Ϊ0
				vbegin = true;	//���������־Ϊtrue
				tip_i = 0;	//��������λ����0
				tip_lock = true;	//�������Լ�������
				equals_flag = true;	//��������=֮ǰ
				tip.setText("��ӭʹ�ã�");
			}else if(command.compareTo("MC") == 0){
				//����������MC���򽫴�������0
				mem.setText("0");
			}else if(command.compareTo("exit") == 0){
				//�����Exit���˳�����
				System.exit(0);
			}else if(command.compareTo("=") == 0 && tip_lock && right(str) && equals_flag){
				//���������ǵȺţ���������Ϸ�
				tip_i = 0;
				tip_lock = false;	//���������Լ�������
				equals_flag = false;	//��������=֮��
				str_old = str;	//����ԭ����ʽ������
				//�滻��ʽ�е�����������ڼ���
				str = str.replaceAll("sin", "s");
				str = str.replaceAll("cos", "c");
				str = str.replaceAll("tan", "t");
				str = str.replaceAll("log", "g");
				str = str.replaceAll("ln", "l");
				str = str.replaceAll("n!", "!");
				//���������־����Ϊtrue
				vbegin = true;
				str_new = str.replaceAll("-","-1��");
				new calc().process(str_new);
			}
			//�������Լ�������
			tip_lock = true;
		}
		
	};

	/*
	 * �ж�һ��str�Ƿ��ǺϷ��ģ�����ֵΪtrue,false
	 * ֻ����0123456789.()sincostanlnlogn!+-���¡�^���ǺϷ���str,����true
	 * �����˳�0123456789.()sincostanlnlogn!+-���¡�^������ַ���strΪ�Ƿ��ġ�����false
	 */
	protected boolean right(String str) {
		// TODO Auto-generated method stub
		int i=0;
		for(i=0;i<str.length();i++){
			if(str.charAt(i)!='0' && str.charAt(i)!='1' && str.charAt(i)!='2' && str.charAt(i)!='3' && str.charAt(i)!='4' && 
					str.charAt(i)!='5' && str.charAt(i)!='6' && str.charAt(i)!='7' && str.charAt(i)!='8' && str.charAt(i)!='9' && 
					str.charAt(i)!='.' && str.charAt(i)!='(' && str.charAt(i)!=')' && str.charAt(i)!='+' && str.charAt(i)!='-' && 
					str.charAt(i)!='��' && str.charAt(i)!='��' && str.charAt(i)!='��' && str.charAt(i)!='^' && str.charAt(i)!='!' && 
					str.charAt(i)!='s' && str.charAt(i)!='i' && str.charAt(i)!='n' && str.charAt(i)!='c' && str.charAt(i)!='o' && 
					str.charAt(i)!='t' && str.charAt(i)!='a' && str.charAt(i)!='l' && str.charAt(i)!='g' )
				break;
		}
		if(i == str.length())
			return true;	//�Ϸ�
		else
			return false;	//���Ϸ�
	}

	
	/*
	 * ��⺯��������ֵΪ3��2��1����ʾӦ��һ��ɾ������TTO����Three Two One��Ϊ������
	 * ΪBksp��ť��ɾ����ʽ�ṩ����
	 * ����3����ʾstrβ��Ϊsin,cos,tan,log�е�һ����Ӧ��һ��ɾ��3��
	 * ����2����ʾstrβ��Ϊln,n!�е�һ����Ӧ��һ��ɾ��2��
	 * ����1����ʾ����������������������ֻ��ɾ��һ���������Ƿ��ַ�ʱҪ���⿼�ǣ�Ӧ������
	 */
	protected int TTO(String str) {
		// TODO Auto-generated method stub
		if((str.charAt(str.length() -1) == 'n' && str.charAt(str.length() -2) == 'i' && str.charAt(str.length() -3) == 's')
				|| (str.charAt(str.length() -1) == 's' && str.charAt(str.length() -2) == 'o' && str.charAt(str.length() -3) == 'c')
				|| (str.charAt(str.length() -1) == 'n' && str.charAt(str.length() -2) == 'a' && str.charAt(str.length() -3) == 't')
				|| (str.charAt(str.length() -1) == 'g' && str.charAt(str.length() -2) == 'o' && str.charAt(str.length() -3) == 'l')){
			return 3;
		}else if((str.charAt(str.length() -1) == 'n' && str.charAt(str.length() - 2) == 'l')
				|| (str.charAt(str.length() -1) == '!' && str.charAt(str.length() -2) == 'n')){
			return 2;
		}else{
			return 1;
		}
	}

	/*
	 * ��input�����ַ�
	 */
	protected void print(String str) {
		// TODO Auto-generated method stub
		//���������
		if(vbegin)
			input.setText(str);
		else	//����Ļԭstr�������ַ�
			input.append(str);
		vbegin = false;
	}
	


	/*
	 * TipChecker()�������ã�
	 * 	1��������⵱ǰ������ַ����Ƿ�Ϸ�
	 * 	2����ĳЩ������ʹ����ʾһЩ������Ϣ
	 * ��⺯������str����ǰ���﷨���
	 * ΪTip����ʾ��ʽ�ṩ���ݣ���TipShow()���ʹ��
	 * ��� 		�ַ�		�����Ը���ĺϷ��ַ�
	 *  1		��			����,(,-,.,����
	 *  2		 ��			�����),�� ^
	 *  3		.			���֣������)���� ^
	 *  4		����			.,���֣������������ ^
	 *  5		���			���֣�����.������
	 *  6		�� ^			(,.,����
	 *  7		����			���֣�����.
	 *  
	 * С����ǰ�����ʡ�ԣ���ʾ0
	 * ���ֵ�һλҲ��Ϊ0
	 */
	protected void TipChecker(String tipcommand1, String tipcommand2) {
		// TODO Auto-generated method stub
		int Tipcode1 = 0;	//��ʾ��������
		int Tipcode2 = 0;	//��ʾ���ʽ�������
		int tiptype1 = 0, tiptype2 = 0;	//��������
		int bracket = 0;	//������
		//"+-���¡�^"������Ϊ��һλ
		if(tipcommand1.compareTo("#") == 0 && (tipcommand2.compareTo("��") == 0 || tipcommand2.compareTo("��") == 0 || 
				tipcommand2.compareTo("+") == 0 || tipcommand2.compareTo(")") == 0 || tipcommand2.compareTo("��") == 0 || 
				tipcommand2.compareTo("^") == 0)){
			Tipcode1 = -1;
		}else if(tipcommand1.compareTo("#") != 0){	//����洢�ַ��������һλ������
			if(tipcommand1.compareTo("(") == 0){
				tiptype1 = 1;
			}else if(tipcommand1.compareTo(")") == 0){
				tiptype1 = 2;
			}else if(tipcommand1.compareTo(".") == 0){
				tiptype1 = 3;
			}else if("0123456789".indexOf(tipcommand1) != -1){
				tiptype1 = 4;
			}else if("+-����".indexOf(tipcommand1) != -1){
				tiptype1 = 5;
			}else if("��^".indexOf(tipcommand1) != -1){
				tiptype1 = 6;
			}else if("sincostanloglnn!".indexOf(tipcommand1) != -1){
				tiptype1 = 7;
			}
			
			//������Ҫ����İ�������
			if(tipcommand2.compareTo("(") == 0){
				tiptype2 = 1;
			}else if(tipcommand2.compareTo(")") == 0){
				tiptype2 = 2;
			}else if(tipcommand2.compareTo(".") == 0){
				tiptype2 = 3;
			}else if("0123456789".indexOf(tipcommand2) != -1){
				tiptype2 = 4;
			}else if("+-����".indexOf(tipcommand2) != -1){
				tiptype2 = 5;
			}else if("��^".indexOf(tipcommand2) != -1){
				tiptype2 = 6;
			}else if("sincostanloglnn!".indexOf(tipcommand2) != -1){
				tiptype2 = 7;
			}
			
			switch(tiptype1){
			case 1:	//�����ź���ֱ��������,"+x��"(����"-"����),����"��^"
				if(tiptype2 == 2 || (tiptype2 == 5 && tipcommand2.compareTo("-") != 0) || tiptype2 == 6)
					Tipcode1 = 1;
				break;
			case 2:	//�����ź���������š����֡�"+-*/sin^����"
				if(tiptype2 == 1 || tiptype2 == 3 || tiptype2 == 4 || tiptype2 == 7)
					Tipcode1 = 2;
				break;
			case 3:	//"."����������Ż���"sincos����"
				if(tiptype2 == 1 || tiptype2 == 7)
					Tipcode1 = 3;
				//������������"."
				if(tiptype2 == 3)
					Tipcode1 = 8;
				break;
			case 4:	//���ֺ���ֱ�ӽ������Ż���"sincos����"
				if(tiptype2 == 1 || tiptype2 == 7)
					Tipcode1 = 4;
				break;
			case 5:	//"+-*/"����ֱ�ӽ������š�"+-*/��^"
				if(tiptype2 == 2 || tiptype2 == 5 || tiptype2 == 6)
					Tipcode1 = 5;
				break;
			case 6:	//"��^"����ֱ�ӽ������š�"+-*/^��"�Լ�"sincos����"
				if(tiptype2 == 2 || tiptype2 == 5 || tiptype2 == 6 || tiptype2 == 7)
					Tipcode1 = 6;
				break;
			case 7:	//"sincos����"����ֱ�ӽ�������"+-*/^"�Լ�"sincos����"
				if(tiptype2 == 2 || tiptype2 == 5 || tiptype2 == 6 || tiptype2 == 7)
					Tipcode1 = 7;
				break;
			}
		}
		
		//���С������ظ��ԣ�Tipcode1 = 0,��������ǰ��Ĺ���
		if(Tipcode1 == 0 && tipcommand2.compareTo(".") == 0){
			int tip_point = 0;
			for(int i = 0 ; i < tip_i ; i++){
				//��֮ǰ����һ��С���㣬��С���������1
				if(Tipcommand[i].compareTo(".") == 0)
					tip_point ++;
				
				//���������¼��������֮һ��С��������
				if(Tipcommand[i].compareTo("sin") == 0 || Tipcommand[i].compareTo("cos") == 0 || 
						Tipcommand[i].compareTo("tan") == 0 || Tipcommand[i].compareTo("log") == 0 || 
						Tipcommand[i].compareTo("ln") == 0 || Tipcommand[i].compareTo("n!") == 0 || 
						Tipcommand[i].compareTo("��") == 0 || Tipcommand[i].compareTo("^") == 0 || 
						Tipcommand[i].compareTo("��") == 0 || Tipcommand[i].compareTo("��") == 0 || 
						Tipcommand[i].compareTo("-") == 0 || Tipcommand[i].compareTo("+") == 0 || 
						Tipcommand[i].compareTo("(") == 0 || Tipcommand[i].compareTo(")") == 0)
					tip_point = 0;
			}
			tip_point ++;
			if(tip_point > 1)
				Tipcode1 = 8;
		}
		
		//����������Ƿ�ƥ��
		if(Tipcode1 == 0 && tipcommand2.compareTo(")") == 0){
			int tip_right_bracket = 0;
			for(int i=0 ; i<tip_i; i++){
				//�������һ�������ţ��������1
				if(Tipcommand[i].compareTo("(") == 0){
					tip_right_bracket++;
				}
				//�������һ�������ţ��������1
				if(Tipcommand[i].compareTo(")") == 0){
					tip_right_bracket--;
				}
			}
			//��������ż���Ϊ0������û����Ӧ���������뵱ǰ������ƥ��
			if(tip_right_bracket == 0)
				Tipcode1 = 10;
		}
		
		//�������=�ĺϷ���
		if(Tipcode1 == 0 && tipcommand2.compareTo("=") == 0){
			//����ƥ����
			int tip_bracket = 0;
			for(int i=0;i<tip_i;i++){
				if(Tipcommand[i].compareTo("(") == 0)
					tip_bracket ++;
				if(Tipcommand[i].compareTo(")") == 0)
					tip_bracket --;
			}
			//������0�����������Ż���δƥ���
			if(tip_bracket > 0){
				Tipcode1 = 9;
				bracket = tip_bracket;
			}else if(tip_bracket == 0){
				//��ǰһ���ַ�������֮һ������=�Ų��Ϸ�
				if("��^sincostanloglnn!".indexOf(tipcommand1) != -1)
					Tipcode1 = 6;
				//��ǰһ���ַ�������֮һ������=�Ų��Ϸ�
				if("+-����".indexOf(tipcommand1) != -1)
					Tipcode1 = 5;
			}
		}
		
		//������������֮һ������ʾ��Ӧ�İ�����Ϣ
		if(tipcommand2.compareTo("MC") == 0)	Tipcode2 = 1;
		if(tipcommand2.compareTo("C") == 0)		Tipcode2 = 2;
		if(tipcommand2.compareTo("DRG") == 0)	Tipcode2 = 3;
		if(tipcommand2.compareTo("Bksp") == 0)	Tipcode2 = 4;
		if(tipcommand2.compareTo("sin") == 0)	Tipcode2 = 5;
		if(tipcommand2.compareTo("cos") == 0)	Tipcode2 = 6;
		if(tipcommand2.compareTo("tan") == 0)	Tipcode2 = 7;
		if(tipcommand2.compareTo("log") == 0)	Tipcode2 = 8;
		if(tipcommand2.compareTo("ln") == 0)	Tipcode2 = 9;
		if(tipcommand2.compareTo("n!") == 0)	Tipcode2 = 10;
		if(tipcommand2.compareTo("��") == 0)		Tipcode2 = 11;
		if(tipcommand2.compareTo("^") == 0)		Tipcode2 = 12;
		
		//��ʾ�����ʹ�����Ϣ
		TipShow(bracket,Tipcode1,Tipcode2,tipcommand1,tipcommand2);
	}

	/*
	 * ����Tip��Ϣ����ǿ�˻���������TipChecker()���ʹ��
	 * ���ݴ���Ĵ��������߰��������ֵ��ʾ��Ӧ��Ϣ
	 */
	private void TipShow(int bracket, int tipcode1, int tipcode2,
			String tipcommand1, String tipcommand2) {
		// TODO Auto-generated method stub
		String tipmessage = "";
		if(tipcode1 != 0)	
			tip_lock = false;	//������������
		switch(tipcode1){
		case -1:
			tipmessage = tipcommand2 + "  ������Ϊ��һ�����  \n";
			break;
		case 1:
			tipmessage = tipcommand2 + "  ��Ӧ���룺����/(/./-/����  \n";
			break;
		case 2:
			tipmessage = tipcommand2 + "  ��Ӧ���룺)/���  \n";
			break;
		case 3:
			tipmessage = tipcommand2 + "  ��Ӧ���룺)/����/���  \n";
			break;
		case 4:
			tipmessage = tipcommand2 + "  ��Ӧ���룺)/./����/���  \n";
			break;
		case 5:
			tipmessage = tipcommand2 + "  ��Ӧ���룺(/./����/����  \n";
			break;
		case 6:
			tipmessage = tipcommand2 + "  ��Ӧ���룺(/./����  \n";
			break;
		case 7:
			tipmessage = tipcommand2 + "  ��Ӧ���룺(/./����  \n";
			break;
		case 8:
			tipmessage = "С�����ظ� \n";
			break;
		case 9:
			tipmessage = "���ܼ��㣬ȱ�� " + bracket + "�� )";
			break;
		case 10:
			tipmessage = "����Ҫ  )";
			break;
		}
		switch(tipcode2){
		case 1:
			tipmessage = tipmessage + "[MC �÷���������� MEM]";
			break;
		case 2:
			tipmessage = tipmessage + "[C �÷�������]";
			break;
		case 3:
			tipmessage = tipmessage + "[DRG �÷���ѡ��DEG��RAD]";
			break;
		case 4:
			tipmessage = tipmessage + "[Bksp �÷����˸�]";
			break;
		case 5:
			tipmessage = tipmessage + "sin�����÷�ʾ����\n" +
					"DEG: sin30 = 0.5     RAD: sin1 = 0.84\n" +
					"ע������������һ��ʹ��ʱҪ�����ţ��磺\n" +
					"sin(cos45)��������sincos45";
			break;
		case 6:
			tipmessage = tipmessage + "cos�����÷�ʾ����\n" +
					"DEG: cos60 = 0.5     RAD: cos1 = 0.54\n" +
					"ע������������һ��ʹ��ʱҪ�����ţ��磺\n" +
					"cos(sin45)��������cossin45";
			break;
		case 7:
			tipmessage = tipmessage + "tan�����÷�ʾ����\n" +
					"DEG: tan45 = 1     RAD: tan1 = 1.55\n" +
					"ע������������һ��ʹ��ʱҪ�����ţ��磺\n" +
					"tan(cos45)��������tancos45";
			break;
		case 8:
			tipmessage = tipmessage + "log�����÷�ʾ����\n" +
					"log10 = log(5+5) = 1\n" +
					"ע������������һ��ʹ��ʱҪ�����ţ��磺\n" +
					"log(tan45)��������logtan45";
			break;
		case 9:
			tipmessage = tipmessage + "ln�����÷�ʾ����\n" +
					"ln10 = le(5+5) = 2.3  lne = 1 \n" +
					"ע������������һ��ʹ��ʱҪ�����ţ��磺\n" +
					"ln(tan45)��������lntan45";
			break;
		case 10:
			tipmessage = tipmessage + "n!�����÷�ʾ����\n" +
					"n!3 = n!(1+2) = 3x2x1 = 6 \n" +
					"ע������������һ��ʹ��ʱҪ�����ţ��磺\n" +
					"n!(log1000)��������n!log1000";
			break;
		case 11:
			tipmessage = tipmessage + "�� �÷�ʾ����������θ���\n" +
					"�磺27��3�θ�Ϊ 27��3 = 3 \n" +
					"ע������������һ��ʹ��ʱҪ�����ţ��磺\n" +
					"���������̣���������(n!3)��(log100) = 2.45";
			break;
		case 12:
			tipmessage = tipmessage + "^ �÷�ʾ�����������ƽ��\n" +
					"�磺2��3�η�Ϊ 2^3 = 8 \n" +
					"ע������������һ��ʹ��ʱҪ�����ţ��磺\n" +
					"��������^����������(n!3)^(log100) = 36";
			break;
		}
		//����ʾ��Ϣ��ʾ��tip
		tip.setText(tipmessage);
	}	
}
