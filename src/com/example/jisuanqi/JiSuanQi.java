package com.example.jisuanqi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class JiSuanQi extends Activity{
	//定义一些变量
	//0~9十个按钮
	private Button[] btn = new Button[10];	
	public static EditText input;		//显示器，用于显示输出结果
	public static TextView mem;		//显示器下方的记忆器，用于记录上一次计算结果
	private TextView _drg;		//三角计算时标志显示：角度还是弧度
	public static TextView tip;		//小提示，用于加强人机交互的弱检测、提示
	private Button
		divide,mul,sub,add,equal,		//÷，×，－，＋，＝
		sin,cos,tan,log,ln,				//函数
		sqrt,square,factorial,bksp,		//根号，平方，阶乘，退格
		left,right,dot,exit,drg,		//（，），.，退出,角度弧度控制键
		mc,c;							//mem清屏键，input清屏键
	
	public static String str_old;		//保存原来的算式样子，为了输出时好看，因计算时算式样子被改变
	public String str_new;		//变换样子后的式子
	public boolean vbegin = true;	//输入控制，true为重新输入，false为接着输入
	public static boolean drg_flag = true;		//控制DRG按键，true为角度，false为弧度
	public static double pi = 4*Math.atan(1);	//∏值：3.14
	public boolean tip_lock = true;		//true表示正确，可以正确输入；false表示有误，输入被锁定
	public boolean equals_flag = true;	//判断是否是按=之后的输入，true表示输入在=之前，false反之
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//获取界面元素
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
		
		//注册监听器
		//为数字按钮绑定监听器
		for(int i=0;i<10;i++){	
			btn[i].setOnClickListener(actionPerformed);
		}
		//为+，-，×，÷等按钮注册监听器
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
	 * 键盘命令捕捉(监听器的实现)
	 */
	//命令缓存，用于检测输入的合法性
	String[] Tipcommand = new String[500];
	int tip_i = 0;		//Tipcommand的指针
	
	private OnClickListener actionPerformed = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String command = ((Button)v).getText().toString();	//按键上的命令捕获
			String str = input.getText().toString();	//显示器上的字符串
			//检测输入是否合法
			//①
			if(equals_flag == false && "0123456789.()sincostanlnlogn!+-×÷√^".indexOf(command) != -1){
				//检测显示器上的字符串是否合法
				if(right(str)){	//合法
					if("+-×÷√^".indexOf(command) != -1){
						for(int i=0;i<str.length();i++){
							Tipcommand[tip_i] = String.valueOf(str.charAt(i));
							tip_i ++;
						}
						vbegin = false;		//false为继续输入
					}
				}else{	//不合法
					input.setText("0");	//将input输入框清零
					vbegin = true;	//true为重新输入
					tip_i = 0;	//指针重置为0
					tip_lock = true;	//输入框可以正常输入
					tip.setText("欢迎使用！");
				}
				equals_flag = true;	//true表示在=之前输入
			}
			//②
			if(tip_i > 0)
				TipChecker(Tipcommand[tip_i-1],command);
			else if(tip_i == 0)
				TipChecker("#",command);
			//③
			if("0123456789.()sincostanlnlogn!+-×÷√^".indexOf(command) != -1 && tip_lock){
				Tipcommand[tip_i] = command;
				tip_i ++;
			}
			
			//若输入正确，则将信息显示到显示器上
			if("0123456789.()sincostanlnlogn!+-×÷√^".indexOf(command) != -1 && tip_lock){
				//共25个按键
				print(command);
			}else if(command.compareTo("DRG") == 0 && tip_lock){
				//如果单击了DRG,则切换当前弧度角度制，并将切换后的结果显示到按键上方
				if(drg_flag == true){
					drg_flag = false;
					_drg.setText("  RAD");
				}else{
					drg_flag = true;
					_drg.setText("  DEG");
				}
			}else if(command.compareTo("Bksp") == 0 && equals_flag){
				//如果输入是退格键，并且是在按=之前
				//一次删除三个字符
				if(TTO(str) == 3){
					if(str.length() > 3)
						input.setText(str.substring(0, str.length() - 3));
					else if(str.length() == 3){
						input.setText("0");
						vbegin = true;
						tip_i = 0;
						tip.setText("欢迎使用！");
					}
				}else if(TTO(str) == 2){	//一次删除两个字符
					if(str.length() > 2)
						input.setText(str.substring(0, str.length() -2));
					else if(str.length() == 2){
						input.setText("0");
						vbegin = true;
						tip_i = 0;
						tip.setText("欢迎使用！");
					}
				}else if(TTO(str) == 1){	//一次删除一个字符
					//若之前输入的字符串合法，则删除一个字符
					if(right(str)){
						if(str.length() > 1)
							input.setText(str.substring(0, str.length() -1));
						else if(str.length() == 1){
							input.setText("0");
							vbegin = true;
							tip_i = 0;
							tip.setText("欢迎使用！");
						}
					}else{	//若之前输入的字符串不合法，则删除全部字符
						input.setText("0");
						vbegin = true;
						tip_i = 0;
						tip.setText("欢迎使用！");
					}
				}
				
				if(input.getText().toString().compareTo("-") == 0 || equals_flag == false){
					input.setText("0");
					vbegin = true;
					tip_i = 0;
					tip.setText("欢迎使用！");
				}
				tip_lock = true;
				if(tip_i > 0)	tip_i--;
			}else if(command.compareTo("Bksp")==0 && equals_flag == false){
				//如果是在按=之后输入退格键
				input.setText("0");	//将显示器内容设置为0
				vbegin = true;
				tip_i = 0;
				tip_lock = true;
				tip.setText("欢迎使用！");
			}else if(command.compareTo("C") == 0){
				//如果输入的是清除键
				input.setText("0");	//将显示器内容设置为0
				vbegin = true;	//重置输入标志为true
				tip_i = 0;	//缓存命令位数清0
				tip_lock = true;	//表明可以继续输入
				equals_flag = true;	//表明输入=之前
				tip.setText("欢迎使用！");
			}else if(command.compareTo("MC") == 0){
				//如果输入的是MC，则将储存器清0
				mem.setText("0");
			}else if(command.compareTo("exit") == 0){
				//如果按Exit则退出程序
				System.exit(0);
			}else if(command.compareTo("=") == 0 && tip_lock && right(str) && equals_flag){
				//如果输入的是等号，并且输入合法
				tip_i = 0;
				tip_lock = false;	//表明不可以继续输入
				equals_flag = false;	//表明输入=之后
				str_old = str;	//保存原来算式的样子
				//替换算式中的运算符，便于计算
				str = str.replaceAll("sin", "s");
				str = str.replaceAll("cos", "c");
				str = str.replaceAll("tan", "t");
				str = str.replaceAll("log", "g");
				str = str.replaceAll("ln", "l");
				str = str.replaceAll("n!", "!");
				//重新输入标志设置为true
				vbegin = true;
				str_new = str.replaceAll("-","-1×");
				new calc().process(str_new);
			}
			//表明可以继续输入
			tip_lock = true;
		}
		
	};

	/*
	 * 判断一个str是否是合法的，返回值为true,false
	 * 只包含0123456789.()sincostanlnlogn!+-×÷√^的是合法的str,返回true
	 * 包含了除0123456789.()sincostanlnlogn!+-×÷√^以外的字符的str为非法的。返回false
	 */
	protected boolean right(String str) {
		// TODO Auto-generated method stub
		int i=0;
		for(i=0;i<str.length();i++){
			if(str.charAt(i)!='0' && str.charAt(i)!='1' && str.charAt(i)!='2' && str.charAt(i)!='3' && str.charAt(i)!='4' && 
					str.charAt(i)!='5' && str.charAt(i)!='6' && str.charAt(i)!='7' && str.charAt(i)!='8' && str.charAt(i)!='9' && 
					str.charAt(i)!='.' && str.charAt(i)!='(' && str.charAt(i)!=')' && str.charAt(i)!='+' && str.charAt(i)!='-' && 
					str.charAt(i)!='×' && str.charAt(i)!='÷' && str.charAt(i)!='√' && str.charAt(i)!='^' && str.charAt(i)!='!' && 
					str.charAt(i)!='s' && str.charAt(i)!='i' && str.charAt(i)!='n' && str.charAt(i)!='c' && str.charAt(i)!='o' && 
					str.charAt(i)!='t' && str.charAt(i)!='a' && str.charAt(i)!='l' && str.charAt(i)!='g' )
				break;
		}
		if(i == str.length())
			return true;	//合法
		else
			return false;	//不合法
	}

	
	/*
	 * 检测函数，返回值为3、2、1，表示应当一次删除几个TTO（即Three Two One）为函数名
	 * 为Bksp按钮的删除方式提供依据
	 * 返回3，表示str尾部为sin,cos,tan,log中的一个，应当一次删除3个
	 * 返回2，表示str尾部为ln,n!中的一个，应当一次删除2个
	 * 返回1，表示除了上述两种其余的情况，只需删除一个（包含非法字符时要另外考虑：应清屏）
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
	 * 向input输入字符
	 */
	protected void print(String str) {
		// TODO Auto-generated method stub
		//清屏后输出
		if(vbegin)
			input.setText(str);
		else	//在屏幕原str后增添字符
			input.append(str);
		vbegin = false;
	}
	


	/*
	 * TipChecker()函数作用：
	 * 	1、用来检测当前输入的字符串是否合法
	 * 	2、对某些函数的使用显示一些帮助信息
	 * 检测函数，对str进行前后语法检测
	 * 为Tip的提示方式提供依据，与TipShow()配合使用
	 * 编号 		字符		其后可以跟随的合法字符
	 *  1		（			数字,(,-,.,函数
	 *  2		 ）			算符，),√ ^
	 *  3		.			数字，算符，)，√ ^
	 *  4		数字			.,数字，算符，），√ ^
	 *  5		算符			数字，（，.，函数
	 *  6		√ ^			(,.,数字
	 *  7		函数			数字，（，.
	 *  
	 * 小数点前后均可省略，表示0
	 * 数字第一位也可为0
	 */
	protected void TipChecker(String tipcommand1, String tipcommand2) {
		// TODO Auto-generated method stub
		int Tipcode1 = 0;	//表示错误类型
		int Tipcode2 = 0;	//表示名词解释类型
		int tiptype1 = 0, tiptype2 = 0;	//命令类型
		int bracket = 0;	//括号数
		//"+-×÷√^"不能作为第一位
		if(tipcommand1.compareTo("#") == 0 && (tipcommand2.compareTo("÷") == 0 || tipcommand2.compareTo("×") == 0 || 
				tipcommand2.compareTo("+") == 0 || tipcommand2.compareTo(")") == 0 || tipcommand2.compareTo("√") == 0 || 
				tipcommand2.compareTo("^") == 0)){
			Tipcode1 = -1;
		}else if(tipcommand1.compareTo("#") != 0){	//定义存储字符串中最后一位的类型
			if(tipcommand1.compareTo("(") == 0){
				tiptype1 = 1;
			}else if(tipcommand1.compareTo(")") == 0){
				tiptype1 = 2;
			}else if(tipcommand1.compareTo(".") == 0){
				tiptype1 = 3;
			}else if("0123456789".indexOf(tipcommand1) != -1){
				tiptype1 = 4;
			}else if("+-×÷".indexOf(tipcommand1) != -1){
				tiptype1 = 5;
			}else if("√^".indexOf(tipcommand1) != -1){
				tiptype1 = 6;
			}else if("sincostanloglnn!".indexOf(tipcommand1) != -1){
				tiptype1 = 7;
			}
			
			//定义想要输入的按键类型
			if(tipcommand2.compareTo("(") == 0){
				tiptype2 = 1;
			}else if(tipcommand2.compareTo(")") == 0){
				tiptype2 = 2;
			}else if(tipcommand2.compareTo(".") == 0){
				tiptype2 = 3;
			}else if("0123456789".indexOf(tipcommand2) != -1){
				tiptype2 = 4;
			}else if("+-×÷".indexOf(tipcommand2) != -1){
				tiptype2 = 5;
			}else if("√^".indexOf(tipcommand2) != -1){
				tiptype2 = 6;
			}else if("sincostanloglnn!".indexOf(tipcommand2) != -1){
				tiptype2 = 7;
			}
			
			switch(tiptype1){
			case 1:	//左括号后面直接右括号,"+x÷"(负号"-"不算),或者"√^"
				if(tiptype2 == 2 || (tiptype2 == 5 && tipcommand2.compareTo("-") != 0) || tiptype2 == 6)
					Tipcode1 = 1;
				break;
			case 2:	//右括号后面接左括号、数字、"+-*/sin^……"
				if(tiptype2 == 1 || tiptype2 == 3 || tiptype2 == 4 || tiptype2 == 7)
					Tipcode1 = 2;
				break;
			case 3:	//"."后面接左括号或者"sincos……"
				if(tiptype2 == 1 || tiptype2 == 7)
					Tipcode1 = 3;
				//连续输入两个"."
				if(tiptype2 == 3)
					Tipcode1 = 8;
				break;
			case 4:	//数字后面直接接左括号或者"sincos……"
				if(tiptype2 == 1 || tiptype2 == 7)
					Tipcode1 = 4;
				break;
			case 5:	//"+-*/"后面直接接右括号、"+-*/√^"
				if(tiptype2 == 2 || tiptype2 == 5 || tiptype2 == 6)
					Tipcode1 = 5;
				break;
			case 6:	//"√^"后面直接接右括号、"+-*/^√"以及"sincos……"
				if(tiptype2 == 2 || tiptype2 == 5 || tiptype2 == 6 || tiptype2 == 7)
					Tipcode1 = 6;
				break;
			case 7:	//"sincos……"后面直接接右括号"+-*/^"以及"sincos……"
				if(tiptype2 == 2 || tiptype2 == 5 || tiptype2 == 6 || tiptype2 == 7)
					Tipcode1 = 7;
				break;
			}
		}
		
		//检测小数点的重复性，Tipcode1 = 0,表明满足前面的规则
		if(Tipcode1 == 0 && tipcommand2.compareTo(".") == 0){
			int tip_point = 0;
			for(int i = 0 ; i < tip_i ; i++){
				//若之前出现一个小数点，则小数点计数加1
				if(Tipcommand[i].compareTo(".") == 0)
					tip_point ++;
				
				//若出现以下几个运算符之一，小数点清零
				if(Tipcommand[i].compareTo("sin") == 0 || Tipcommand[i].compareTo("cos") == 0 || 
						Tipcommand[i].compareTo("tan") == 0 || Tipcommand[i].compareTo("log") == 0 || 
						Tipcommand[i].compareTo("ln") == 0 || Tipcommand[i].compareTo("n!") == 0 || 
						Tipcommand[i].compareTo("√") == 0 || Tipcommand[i].compareTo("^") == 0 || 
						Tipcommand[i].compareTo("÷") == 0 || Tipcommand[i].compareTo("×") == 0 || 
						Tipcommand[i].compareTo("-") == 0 || Tipcommand[i].compareTo("+") == 0 || 
						Tipcommand[i].compareTo("(") == 0 || Tipcommand[i].compareTo(")") == 0)
					tip_point = 0;
			}
			tip_point ++;
			if(tip_point > 1)
				Tipcode1 = 8;
		}
		
		//检测右括号是否匹配
		if(Tipcode1 == 0 && tipcommand2.compareTo(")") == 0){
			int tip_right_bracket = 0;
			for(int i=0 ; i<tip_i; i++){
				//如果出现一个左括号，则计数加1
				if(Tipcommand[i].compareTo("(") == 0){
					tip_right_bracket++;
				}
				//如果出现一个右括号，则计数减1
				if(Tipcommand[i].compareTo(")") == 0){
					tip_right_bracket--;
				}
			}
			//如果右括号计数为0，表明没有相应的左括号与当前右括号匹配
			if(tip_right_bracket == 0)
				Tipcode1 = 10;
		}
		
		//检查输入=的合法性
		if(Tipcode1 == 0 && tipcommand2.compareTo("=") == 0){
			//括号匹配数
			int tip_bracket = 0;
			for(int i=0;i<tip_i;i++){
				if(Tipcommand[i].compareTo("(") == 0)
					tip_bracket ++;
				if(Tipcommand[i].compareTo(")") == 0)
					tip_bracket --;
			}
			//若大于0，表明左括号还有未匹配的
			if(tip_bracket > 0){
				Tipcode1 = 9;
				bracket = tip_bracket;
			}else if(tip_bracket == 0){
				//若前一个字符是以下之一，表明=号不合法
				if("√^sincostanloglnn!".indexOf(tipcommand1) != -1)
					Tipcode1 = 6;
				//若前一个字符是以下之一，表明=号不合法
				if("+-×÷".indexOf(tipcommand1) != -1)
					Tipcode1 = 5;
			}
		}
		
		//若命令是以下之一，则显示相应的帮助信息
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
		if(tipcommand2.compareTo("√") == 0)		Tipcode2 = 11;
		if(tipcommand2.compareTo("^") == 0)		Tipcode2 = 12;
		
		//显示帮助和错误信息
		TipShow(bracket,Tipcode1,Tipcode2,tipcommand1,tipcommand2);
	}

	/*
	 * 反馈Tip信息，加强人机交互，与TipChecker()配合使用
	 * 根据传入的错误代码或者帮助代码的值显示相应信息
	 */
	private void TipShow(int bracket, int tipcode1, int tipcode2,
			String tipcommand1, String tipcommand2) {
		// TODO Auto-generated method stub
		String tipmessage = "";
		if(tipcode1 != 0)	
			tip_lock = false;	//表明输入有误
		switch(tipcode1){
		case -1:
			tipmessage = tipcommand2 + "  不能作为第一个算符  \n";
			break;
		case 1:
			tipmessage = tipcommand2 + "  后应输入：数字/(/./-/函数  \n";
			break;
		case 2:
			tipmessage = tipcommand2 + "  后应输入：)/算符  \n";
			break;
		case 3:
			tipmessage = tipcommand2 + "  后应输入：)/数字/算符  \n";
			break;
		case 4:
			tipmessage = tipcommand2 + "  后应输入：)/./数字/算符  \n";
			break;
		case 5:
			tipmessage = tipcommand2 + "  后应输入：(/./数字/函数  \n";
			break;
		case 6:
			tipmessage = tipcommand2 + "  后应输入：(/./数字  \n";
			break;
		case 7:
			tipmessage = tipcommand2 + "  后应输入：(/./数字  \n";
			break;
		case 8:
			tipmessage = "小数点重复 \n";
			break;
		case 9:
			tipmessage = "不能计算，缺少 " + bracket + "个 )";
			break;
		case 10:
			tipmessage = "不需要  )";
			break;
		}
		switch(tipcode2){
		case 1:
			tipmessage = tipmessage + "[MC 用法：清除记忆 MEM]";
			break;
		case 2:
			tipmessage = tipmessage + "[C 用法：归零]";
			break;
		case 3:
			tipmessage = tipmessage + "[DRG 用法：选择DEG或RAD]";
			break;
		case 4:
			tipmessage = tipmessage + "[Bksp 用法：退格]";
			break;
		case 5:
			tipmessage = tipmessage + "sin函数用法示例：\n" +
					"DEG: sin30 = 0.5     RAD: sin1 = 0.84\n" +
					"注：与其他函数一起使用时要加括号，如：\n" +
					"sin(cos45)，而不是sincos45";
			break;
		case 6:
			tipmessage = tipmessage + "cos函数用法示例：\n" +
					"DEG: cos60 = 0.5     RAD: cos1 = 0.54\n" +
					"注：与其他函数一起使用时要加括号，如：\n" +
					"cos(sin45)，而不是cossin45";
			break;
		case 7:
			tipmessage = tipmessage + "tan函数用法示例：\n" +
					"DEG: tan45 = 1     RAD: tan1 = 1.55\n" +
					"注：与其他函数一起使用时要加括号，如：\n" +
					"tan(cos45)，而不是tancos45";
			break;
		case 8:
			tipmessage = tipmessage + "log函数用法示例：\n" +
					"log10 = log(5+5) = 1\n" +
					"注：与其他函数一起使用时要加括号，如：\n" +
					"log(tan45)，而不是logtan45";
			break;
		case 9:
			tipmessage = tipmessage + "ln函数用法示例：\n" +
					"ln10 = le(5+5) = 2.3  lne = 1 \n" +
					"注：与其他函数一起使用时要加括号，如：\n" +
					"ln(tan45)，而不是lntan45";
			break;
		case 10:
			tipmessage = tipmessage + "n!函数用法示例：\n" +
					"n!3 = n!(1+2) = 3x2x1 = 6 \n" +
					"注：与其他函数一起使用时要加括号，如：\n" +
					"n!(log1000)，而不是n!log1000";
			break;
		case 11:
			tipmessage = tipmessage + "√ 用法示例：开任意次根号\n" +
					"如：27开3次根为 27√3 = 3 \n" +
					"注：与其他函数一起使用时要加括号，如：\n" +
					"（函数）√（函数），(n!3)√(log100) = 2.45";
			break;
		case 12:
			tipmessage = tipmessage + "^ 用法示例：开任意次平方\n" +
					"如：2的3次方为 2^3 = 8 \n" +
					"注：与其他函数一起使用时要加括号，如：\n" +
					"（函数）^（函数），(n!3)^(log100) = 36";
			break;
		}
		//将提示信息显示到tip
		tip.setText(tipmessage);
	}	
}
