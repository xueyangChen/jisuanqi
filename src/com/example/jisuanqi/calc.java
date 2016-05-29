package com.example.jisuanqi;

import java.text.DecimalFormat;
import java.util.StringTokenizer;

import android.util.Log;

/*
 * 整个计算核心，只要将表达式的整个字符串传入calc().process()就可以实行计算了
 * 算法包括以下几个部分
 * 1、计算部分 		process(String str)	这是建立在查错无错误的情况下
 * 2、数据格式化	FP(double n)		使数据有相当的精确度
 * 3、阶乘算法		N(double n)			计算n!,将结果返回
 * 4、错误提示		showError(int code,String str)	将错误返回
 */
public class calc {
	public calc(){}
	final int MAXLEN = 500;
	//public boolean drg_flag = JiSunQi.drg_flag;
	/*
	 * 计算表达式
	 * 从左到右扫描，数字进number栈，运算符进operator栈
	 * +-基本优先级为1，×÷基本优先级为2，log,ln,sin,cos,tan,n!基本优先级为3，√^基本优先级为4
	 * 括号内层运算符优先级比括号外层同级运算符优先级高4
	 * 当前运算符优先级高于栈顶压栈，低于栈顶弹出一个运算符与两个数进行运算
	 * 重复直到当前运算符大于栈顶
	 * 扫描完后对剩下的运算符与数字依次计算
	 */
	public void process(String str) {
		/*
		 * 定义一些变量，用于存储数字、运算符以及运算符的优先级
		 * weightPlus为同一（）下的基本优先级，weightTemp临时记录优先级的变化
		 * topOp为weight[],operator[]的计数器；topNum为number[]的计数器；flag为正负数计数器，1为正数，-1为负数
		 */
		int weightPlus = 0,weightTemp = 0;
		int topOp = 0,topNum = 0,flag = 1;
		int weight[];		//保存operator栈中运算符的优先级，以topOp计数
		double number[];	//保存数字，以topNum计数
		char ch,ch_gai,operator[];	//operator[]保存运算符，以topOp计数
		String num;	//记录数字，str_new以+-×÷()sctgl!√^分段，str_new以+-×÷()sctgl!√^字符之间的字符串即为数字
		weight = new int[MAXLEN];
		number = new double[MAXLEN];
		operator = new char[MAXLEN];
		String expression = str;
		StringTokenizer expToken = new StringTokenizer(expression,"+-×÷()sctgl!√^");	//分割String的应用类
		int i=0;
		while(i<expression.length()){
			/*
			 * 将表达式中的负号提取出来，传递给falge
			 */
			ch = expression.charAt(i);
			//判断正负数
			if(i==0){
				if(ch == '-')
					flag = -1;
			}else if(expression.charAt(i-1) == '(' && ch == '-')
				flag = -1;
			
			/*
			 * 获取算式中的所有数字，，将正负符号转移给数字，并以此进入堆栈
			 */
			if(ch <= '9' && ch >= '0' || ch == '.' || ch == 'E'){
				num = expToken.nextToken();	//nextToken()函数：返回从当前位置到下一个分隔符的字符串
				ch_gai = ch;
				Log.d("jsq",ch+"--->"+i);
				//取得整个数字
				while(i<expression.length() && (ch_gai <= '9' && ch_gai >= '0' || ch_gai == '.' || ch_gai == 'E')){
					ch_gai = expression.charAt(i++);
					Log.d("jsq","i的值为："+i);
				}
				//将指针退回之前的位置
				if(i >= expression.length())
					i -= 1;	
				else
					i -= 2;
				if(num.compareTo(".") == 0)
					number[topNum++] = 0;
				else{
					try{
						number[topNum++] = Double.parseDouble(num) * flag;
						flag = 1;
					}catch(NumberFormatException e){
						e.printStackTrace();
					}
				}
			}
				
			/*
			 * 为运算符确定在算式中的优先级，运算符的优先级由两部分决定：基本优先级和变动优先级
			 */
			if(ch == '(')	weightPlus += 4;
			if(ch == ')')	weightPlus -= 4;
			if(ch == '-' && flag == 1 || ch == '+' || ch == '×' || ch =='÷' || ch == 's' || ch == 'c' 
					|| ch == 't' ||ch == 'g' || ch == 'l' || ch == '!' || ch == '^' || ch == '√'){
				switch(ch){
				//+-的优先级最低，为1
				case '+':
				case '-':
					weightTemp = 1 + weightPlus;
					break;
				//×÷的优先级稍高，为2
				case '×':
				case '÷':
					weightTemp = 2 + weightPlus;
				//sin,cos,……之类的优先级为3
				case 's':
				case 'c':
				case 't':
				case 'g':
				case 'l':
				case '!':
					weightTemp = 3 + weightPlus;
				//其余优先级为4(case '^':  case '√';)
				default:
					weightTemp = 4 + weightPlus;
					break;
				}
				
				//如果当前优先级大于堆栈顶部元素，则直接入栈
				if(topOp == 0 || weight[topOp-1] < weightTemp){
					weight[topOp] = weightTemp;
					operator[topOp] = ch;
					topOp ++ ;
				}else{
				//否则将堆栈中的运算符逐个取出，直到当前堆栈顶部运算符的优先级小于当前的运算符
					while(topOp > 0 && weight[topOp-1] >= weightTemp){
						switch(operator[topOp-1]){
						//取出数字组的相应元素进行运算
						case '+':
							number[topNum-2] += number[topNum-1];
							break;
						case '-':
							number[topNum-2] -= number[topNum-1];
							break;
						case '×':
							number[topNum-2] *= number[topNum-1];
							break;
						case '÷':
							//判断除数为0的情况
							if(number[topNum-1] == 0){
								showError(1,JiSuanQi.str_old);	//1、0不能作为除数
								return;
							}
							number[topNum-2] /= number[topNum-1];
							break;
						case '√':
							if(number[topNum-1] == 0 || (number[topNum-2]<0 && number[topNum-1] % 2 == 0)){
								showError(2,JiSuanQi.str_old);	//2、函数格式错误
								return;
							}
							number[topNum-2] = Math.pow(number[topNum-2], 1/number[topNum-1]);
							break;
						case '^':
							number[topNum-2] = Math.pow(number[topNum-2], number[topNum-1]);
							break;
							
						//计算时进行角度弧度的判断及转换
						//sin
						case 's':
							if(JiSuanQi.drg_flag == true){
								number[topNum-1] = Math.sin((number[topNum-1]/180)*JiSuanQi.pi);
							}else{
								number[topNum-1] = Math.sin(number[topNum-1]);
							}
							topNum++;
							break;
						//cos
						case 'c':
							if(JiSuanQi.drg_flag == true){
								number[topNum-1] = Math.cos((number[topNum-1]/180)*JiSuanQi.pi);
							}else{
								number[topNum-1] = Math.cos(number[topNum-1]);
							}
							topNum++;
							break;
						//tan
						case 't':
							if(JiSuanQi.drg_flag == true){
								if((Math.abs(number[topNum-1])/90)%2 == 1){
									showError(2,JiSuanQi.str_old);	//3、值太大，超出范围
									return;
								}
								number[topNum-1] = Math.tan((number[topNum-1]/180)*JiSuanQi.pi);
							}else{
								if(Math.abs(number[topNum-1]/(JiSuanQi.pi/2))%2 == 1){
									showError(2,JiSuanQi.str_old);
									return;
								}
								number[topNum-1] = Math.tan(number[topNum-1]);
							}
							topNum++;
							break;
						//log
						case 'g':
							if(number[topNum-1] <= 0){
								showError(2,JiSuanQi.str_old);
								return;
							}
							number[topNum-1] = Math.log10(number[topNum-1]);
							topNum++;
							break;
						//ln
						case 'l':
							if(number[topNum-1] <= 0){
								showError(2,JiSuanQi.str_old);
								return;
							}
							number[topNum-1] = Math.log(number[topNum-1]);
							topNum++;
							break;
						//阶乘
						case '!':
							if(number[topNum-1] > 170){
								showError(3,JiSuanQi.str_old);
								return;
							}else if(number[topNum-1] < 0){
								showError(2,JiSuanQi.str_old);
								return;
							}
							number[topNum-1] = N(number[topNum-1]);
							topNum++;
							break;
						}
						//继续取堆栈的下一个元素进行判断
						topNum--;
						topOp--;
					}
					//将运算符压栈
					weight[topOp] = weightTemp;
					operator[topOp] = ch;
					topOp++;		
				}
			}
			i++;
		}
		//依次取出堆栈的运算符进行运算
		while(topOp > 0){
			//+-x直接将数组的后两位数取出运算
			switch(operator[topOp-1]){
			case '+':
				number[topNum-2] += number[topNum-1];
				break;
			case '-':
				number[topNum-2] -= number[topNum-1];
				break;
			case '×':
				number[topNum-2] *= number[topNum-1];
				break;
			//涉及除法时，要考虑除法不为零的情况
			case '÷':
				if(number[topNum-1] == 0){
					showError(1,JiSuanQi.str_old);	//1、0不能作除数
					return;
				}
				number[topNum-2] /= number[topNum-1];
				break;
			case '√':
				if(number[topNum-1] == 0 || (number[topNum-2]<0 && number[topNum-1] % 2 == 0)){
					showError(2,JiSuanQi.str_old);	//2、函数格式错误
					return;
				}
				number[topNum-2] = Math.pow(number[topNum-2], 1/number[topNum-1]);
				break;
			case '^':
				number[topNum-2] = Math.pow(number[topNum-2], number[topNum-1]);
				break;
				//sin
				case 's':
					if(JiSuanQi.drg_flag == true){
						number[topNum-1] = Math.sin((number[topNum-1]/180)*JiSuanQi.pi);
					}else{
						number[topNum-1] = Math.sin(number[topNum-1]);
					}
					topNum++;
					break;
				//cos
				case 'c':
					if(JiSuanQi.drg_flag == true){
						number[topNum-1] = Math.cos((number[topNum-1]/180)*JiSuanQi.pi);
					}else{
						number[topNum-1] = Math.cos(number[topNum-1]);
					}
					topNum++;
					break;
				//tan
				case 't':
					if(JiSuanQi.drg_flag == true){
						if((Math.abs(number[topNum-1])/90)%2 == 1){
							showError(3,JiSuanQi.str_old);	//3、值太大，超出范围
							return;
						}
						number[topNum-1] = Math.tan((number[topNum-1]/180)*JiSuanQi.pi);
					}else{
						if(Math.abs(number[topNum-1]/(JiSuanQi.pi/2))%2 == 1){
							showError(2,JiSuanQi.str_old);
							return;
						}
						number[topNum-1] = Math.tan(number[topNum-1]);
					}
					topNum++;
					break;
				//log
				case 'g':
					if(number[topNum-1] <= 0){
						showError(2,JiSuanQi.str_old);
						return;
					}
					number[topNum-1] = Math.log10(number[topNum-1]);
					topNum++;
					break;
				//ln
				case 'l':
					if(number[topNum-1] <= 0){
						showError(2,JiSuanQi.str_old);
						return;
					}
					number[topNum-1] = Math.log(number[topNum-1]);
					topNum++;
					break;
				//阶乘
				case '!':
					if(number[topNum-1] > 170){
						showError(3,JiSuanQi.str_old);
						return;
					}else if(number[topNum-1] < 0){
						showError(2,JiSuanQi.str_old);
						return;
					}
					number[topNum-1] = N(number[topNum-1]);
					topNum++;
					break;
				}
				//取堆栈下一个元素计算
				topNum--;
				topOp--;
			}
			//如果数字太大，提示错误信息
			if(number[0] > 7.3E306){
				showError(3,JiSuanQi.str_old);
				return;
			}
			//输出最终结果
			JiSuanQi.input.setText(String.valueOf(FP(number[0])));
			JiSuanQi.tip.setText("计算完毕，要继续请按归零键 C");
			JiSuanQi.mem.setText(JiSuanQi.str_old + "=" +String.valueOf(FP(number[0])));
		}
	
	/*
	 * FP = floating point 控制小数点位数，达到精度
	 * 否则会出现0.6-0.2 = 0.39999999999997的情况，用FP即可解决，使得数为0.4
	 * 本格式精度为15位
	 */
	private double FP(double n) {
		// TODO Auto-generated method stub
		//NumberFormat format = NumberFormat.getInstance();	//创建一个格式化类
		//format.setMaximumFractionDigits(18);	//设置小数位的格式
		DecimalFormat format = new DecimalFormat("0.#############");
		return Double.parseDouble(format.format(n));
	}
	
/*
	 * 阶乘算法
	 */
	private double N(double n) {
		// TODO Auto-generated method stub
		int i=0;
		double sum=1;
		//依次将小于等于n的值相乘
		for(i=1;i<=n;i++){
			sum = sum*i;
		}
		return sum;
	}

	/*
	 * 错误提示：按了“=”之后，若计算式在process()过程中出现错误，则进行提示
	 */
	private void showError(int code, String str) {
		// TODO Auto-generated method stub
		String message = "";
		switch(code){
		case 1:
			message = "0不能作为除数";
			break;
		case 2:
			message = "函数格式错误";
			break;
		case 3:
			message = "值太大，超出范围";
		}
		JiSuanQi.input.setText("\" "+ str + "\" " + ": "+message );
		JiSuanQi.tip.setText(message+"\n" + "计算完毕，要继续请按归零键 C");
	}

}
