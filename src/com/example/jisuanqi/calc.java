package com.example.jisuanqi;

import java.text.DecimalFormat;
import java.util.StringTokenizer;

import android.util.Log;

/*
 * ����������ģ�ֻҪ�����ʽ�������ַ�������calc().process()�Ϳ���ʵ�м�����
 * �㷨�������¼�������
 * 1�����㲿�� 		process(String str)	���ǽ����ڲ���޴���������
 * 2�����ݸ�ʽ��	FP(double n)		ʹ�������൱�ľ�ȷ��
 * 3���׳��㷨		N(double n)			����n!,���������
 * 4��������ʾ		showError(int code,String str)	�����󷵻�
 */
public class calc {
	public calc(){}
	final int MAXLEN = 500;
	//public boolean drg_flag = JiSunQi.drg_flag;
	/*
	 * ������ʽ
	 * ������ɨ�裬���ֽ�numberջ���������operatorջ
	 * +-�������ȼ�Ϊ1�����»������ȼ�Ϊ2��log,ln,sin,cos,tan,n!�������ȼ�Ϊ3����^�������ȼ�Ϊ4
	 * �����ڲ���������ȼ����������ͬ����������ȼ���4
	 * ��ǰ��������ȼ�����ջ��ѹջ������ջ������һ�����������������������
	 * �ظ�ֱ����ǰ���������ջ��
	 * ɨ������ʣ�µ���������������μ���
	 */
	public void process(String str) {
		/*
		 * ����һЩ���������ڴ洢���֡�������Լ�����������ȼ�
		 * weightPlusΪͬһ�����µĻ������ȼ���weightTemp��ʱ��¼���ȼ��ı仯
		 * topOpΪweight[],operator[]�ļ�������topNumΪnumber[]�ļ�������flagΪ��������������1Ϊ������-1Ϊ����
		 */
		int weightPlus = 0,weightTemp = 0;
		int topOp = 0,topNum = 0,flag = 1;
		int weight[];		//����operatorջ������������ȼ�����topOp����
		double number[];	//�������֣���topNum����
		char ch,ch_gai,operator[];	//operator[]�������������topOp����
		String num;	//��¼���֣�str_new��+-����()sctgl!��^�ֶΣ�str_new��+-����()sctgl!��^�ַ�֮����ַ�����Ϊ����
		weight = new int[MAXLEN];
		number = new double[MAXLEN];
		operator = new char[MAXLEN];
		String expression = str;
		StringTokenizer expToken = new StringTokenizer(expression,"+-����()sctgl!��^");	//�ָ�String��Ӧ����
		int i=0;
		while(i<expression.length()){
			/*
			 * �����ʽ�еĸ�����ȡ���������ݸ�falge
			 */
			ch = expression.charAt(i);
			//�ж�������
			if(i==0){
				if(ch == '-')
					flag = -1;
			}else if(expression.charAt(i-1) == '(' && ch == '-')
				flag = -1;
			
			/*
			 * ��ȡ��ʽ�е��������֣�������������ת�Ƹ����֣����Դ˽����ջ
			 */
			if(ch <= '9' && ch >= '0' || ch == '.' || ch == 'E'){
				num = expToken.nextToken();	//nextToken()���������شӵ�ǰλ�õ���һ���ָ������ַ���
				ch_gai = ch;
				Log.d("jsq",ch+"--->"+i);
				//ȡ����������
				while(i<expression.length() && (ch_gai <= '9' && ch_gai >= '0' || ch_gai == '.' || ch_gai == 'E')){
					ch_gai = expression.charAt(i++);
					Log.d("jsq","i��ֵΪ��"+i);
				}
				//��ָ���˻�֮ǰ��λ��
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
			 * Ϊ�����ȷ������ʽ�е����ȼ�������������ȼ��������־������������ȼ��ͱ䶯���ȼ�
			 */
			if(ch == '(')	weightPlus += 4;
			if(ch == ')')	weightPlus -= 4;
			if(ch == '-' && flag == 1 || ch == '+' || ch == '��' || ch =='��' || ch == 's' || ch == 'c' 
					|| ch == 't' ||ch == 'g' || ch == 'l' || ch == '!' || ch == '^' || ch == '��'){
				switch(ch){
				//+-�����ȼ���ͣ�Ϊ1
				case '+':
				case '-':
					weightTemp = 1 + weightPlus;
					break;
				//���µ����ȼ��Ըߣ�Ϊ2
				case '��':
				case '��':
					weightTemp = 2 + weightPlus;
				//sin,cos,����֮������ȼ�Ϊ3
				case 's':
				case 'c':
				case 't':
				case 'g':
				case 'l':
				case '!':
					weightTemp = 3 + weightPlus;
				//�������ȼ�Ϊ4(case '^':  case '��';)
				default:
					weightTemp = 4 + weightPlus;
					break;
				}
				
				//�����ǰ���ȼ����ڶ�ջ����Ԫ�أ���ֱ����ջ
				if(topOp == 0 || weight[topOp-1] < weightTemp){
					weight[topOp] = weightTemp;
					operator[topOp] = ch;
					topOp ++ ;
				}else{
				//���򽫶�ջ�е���������ȡ����ֱ����ǰ��ջ��������������ȼ�С�ڵ�ǰ�������
					while(topOp > 0 && weight[topOp-1] >= weightTemp){
						switch(operator[topOp-1]){
						//ȡ�����������ӦԪ�ؽ�������
						case '+':
							number[topNum-2] += number[topNum-1];
							break;
						case '-':
							number[topNum-2] -= number[topNum-1];
							break;
						case '��':
							number[topNum-2] *= number[topNum-1];
							break;
						case '��':
							//�жϳ���Ϊ0�����
							if(number[topNum-1] == 0){
								showError(1,JiSuanQi.str_old);	//1��0������Ϊ����
								return;
							}
							number[topNum-2] /= number[topNum-1];
							break;
						case '��':
							if(number[topNum-1] == 0 || (number[topNum-2]<0 && number[topNum-1] % 2 == 0)){
								showError(2,JiSuanQi.str_old);	//2��������ʽ����
								return;
							}
							number[topNum-2] = Math.pow(number[topNum-2], 1/number[topNum-1]);
							break;
						case '^':
							number[topNum-2] = Math.pow(number[topNum-2], number[topNum-1]);
							break;
							
						//����ʱ���нǶȻ��ȵ��жϼ�ת��
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
									showError(2,JiSuanQi.str_old);	//3��ֵ̫�󣬳�����Χ
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
						//�׳�
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
						//����ȡ��ջ����һ��Ԫ�ؽ����ж�
						topNum--;
						topOp--;
					}
					//�������ѹջ
					weight[topOp] = weightTemp;
					operator[topOp] = ch;
					topOp++;		
				}
			}
			i++;
		}
		//����ȡ����ջ���������������
		while(topOp > 0){
			//+-xֱ�ӽ�����ĺ���λ��ȡ������
			switch(operator[topOp-1]){
			case '+':
				number[topNum-2] += number[topNum-1];
				break;
			case '-':
				number[topNum-2] -= number[topNum-1];
				break;
			case '��':
				number[topNum-2] *= number[topNum-1];
				break;
			//�漰����ʱ��Ҫ���ǳ�����Ϊ������
			case '��':
				if(number[topNum-1] == 0){
					showError(1,JiSuanQi.str_old);	//1��0����������
					return;
				}
				number[topNum-2] /= number[topNum-1];
				break;
			case '��':
				if(number[topNum-1] == 0 || (number[topNum-2]<0 && number[topNum-1] % 2 == 0)){
					showError(2,JiSuanQi.str_old);	//2��������ʽ����
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
							showError(3,JiSuanQi.str_old);	//3��ֵ̫�󣬳�����Χ
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
				//�׳�
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
				//ȡ��ջ��һ��Ԫ�ؼ���
				topNum--;
				topOp--;
			}
			//�������̫����ʾ������Ϣ
			if(number[0] > 7.3E306){
				showError(3,JiSuanQi.str_old);
				return;
			}
			//������ս��
			JiSuanQi.input.setText(String.valueOf(FP(number[0])));
			JiSuanQi.tip.setText("������ϣ�Ҫ�����밴����� C");
			JiSuanQi.mem.setText(JiSuanQi.str_old + "=" +String.valueOf(FP(number[0])));
		}
	
	/*
	 * FP = floating point ����С����λ�����ﵽ����
	 * ��������0.6-0.2 = 0.39999999999997���������FP���ɽ����ʹ����Ϊ0.4
	 * ����ʽ����Ϊ15λ
	 */
	private double FP(double n) {
		// TODO Auto-generated method stub
		//NumberFormat format = NumberFormat.getInstance();	//����һ����ʽ����
		//format.setMaximumFractionDigits(18);	//����С��λ�ĸ�ʽ
		DecimalFormat format = new DecimalFormat("0.#############");
		return Double.parseDouble(format.format(n));
	}
	
/*
	 * �׳��㷨
	 */
	private double N(double n) {
		// TODO Auto-generated method stub
		int i=0;
		double sum=1;
		//���ν�С�ڵ���n��ֵ���
		for(i=1;i<=n;i++){
			sum = sum*i;
		}
		return sum;
	}

	/*
	 * ������ʾ�����ˡ�=��֮��������ʽ��process()�����г��ִ����������ʾ
	 */
	private void showError(int code, String str) {
		// TODO Auto-generated method stub
		String message = "";
		switch(code){
		case 1:
			message = "0������Ϊ����";
			break;
		case 2:
			message = "������ʽ����";
			break;
		case 3:
			message = "ֵ̫�󣬳�����Χ";
		}
		JiSuanQi.input.setText("\" "+ str + "\" " + ": "+message );
		JiSuanQi.tip.setText(message+"\n" + "������ϣ�Ҫ�����밴����� C");
	}

}
