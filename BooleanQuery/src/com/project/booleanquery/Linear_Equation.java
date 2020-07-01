package com.project.booleanquery;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Linear_Equation {

	String[] L;
	String op[];
	String brakets[];
	int bracket_index=0;
	String R;
	String equation;
	boolean bool_result[][];
	int bracket_n=0;
	enum bool {
		AND, NOT, OR
	};
	String[] equation_string_array;
	int[] equation_string_position_array;
	int[][] results;

	public Linear_Equation(String equation) {
		super();
		this.equation = equation;
		SSL_validation.disableCertificates();
	}

	public String format() throws IOException {
		char[] c = this.equation.toCharArray();
		String l_side = "";
		String search_index="";
		String operation = "";
		for (int i = 0; i < c.length; i++) {
			if (c[i] != '(' && c[i] != ')' && c[i] != ' ') {
				if (i < c.length - 3) {
					if ((c[i] != 'A' && c[i + 1] != 'N' && c[i + 2] != 'D')) {
						if ((c[i] != 'N' && c[i + 1] != 'O' && c[i + 2] != 'T')) {
							if ((c[i] != 'O' && c[i + 1] != 'R')) {
								l_side += c[i];
								search_index+= c[i];
								c[i] = ' ';
							} else if ((c[i] == 'O' && c[i + 1] == 'R')) {
								
								operation += "OR ";

								if(l_side!=""&&l_side.charAt(l_side.toCharArray().length-1)!=' ')
								l_side += " ";
								search_index+= " ";
								c[i] = c[i + 1] = ' ';
								l_side += "OR ";
								i = i + 2;
							}
						}

						else if ((c[i] == 'N' && c[i + 1] == 'O' && c[i + 2] == 'T')) {
							operation += "NOT ";
							if(l_side!=""&&l_side.charAt(l_side.toCharArray().length-1)!=' ') {
							l_side += " ";
							search_index+= " ";}
							c[i] = c[i + 1] = c[i + 2] = ' ';
							i = i + 3;
							l_side += "NOT ";
						}
					} else if ((c[i] == 'A' && c[i + 1] == 'N' && c[i + 2] == 'D')) {
						operation += "AND ";
						if(l_side!=""&&l_side.charAt(l_side.toCharArray().length-1)!=' ') {
						l_side += " ";
						search_index+= " ";}
						c[i] = c[i + 1] = c[i + 2] = ' ';
						l_side += "AND ";
						i = i + 3;
					}
				} else {
					l_side += c[i];
					search_index+= c[i];
					c[i] = ' ';
				}
			} else if (c[i] == ')' || c[i] == '(' || c[i] == ' ') {
				
				if (c[i] == '(')
					l_side+="( ";
					else if (c[i] == ')')
						l_side+=" )";
					c[i] = ' ';
				
			}
		}
		//show formats
		System.out.println("search_indexes: "+search_index);
		System.out.println("operands with brackets: "+l_side);
		System.out.println(Arrays.toString(l_side.split(" ")));
		get_position_array(l_side.split(" "),search_index);
		System.out.println(Arrays.toString(equation_string_position_array));
		L = search_index.split(" ");
		op = operation.split(" ");
		
		
		return l_side;
	}

	private void get_position_array(String[] split, String search_index) {
		int position=0;
		equation_string_position_array=new int[split.length];
		for(int i=0;i<equation_string_position_array.length;i++)
			equation_string_position_array[i]=-1;
		for(int i=0;i<split.length;i++) {
		if(isNotReserved(split[i])) {
			position=0;
			for(int j=0;j<i;j++)
				if(isNotReserved(split[j]))
				position++;
			equation_string_position_array[i]=position;
		}
		}
	}

	private void solve(String[] s) {
		while(isNotSolved(equation_string_array,0,s.length)) {
		int lb_position=0;
		int rb_position=0;
		for(int i=0;i<equation_string_array.length;i++)
			if(equation_string_array[i].equals("("))
				lb_position=i;
		for(int j=lb_position;j<equation_string_array.length;j++)
			if(equation_string_array[j].equals(")")) {
				rb_position=j;
				break;
			}		
		
		if(lb_position+rb_position==0) {

			for(int i=0;i<equation_string_array.length;i++)
				if(equation_string_array[i].equals("NOT")) {					
					for(int j=i+1;j<equation_string_array.length;j++)
						if(equation_string_array[j]!=" ") {
					solve_NOT(equation_string_position_array[j]);
					delete_solved_brackets(j, i, equation_string_array[j]);
					break;
						}
					}
				else if(equation_string_array[i].equals("AND")) {
					int j=i+1;
					for(;j<equation_string_array.length;j++)
						if(equation_string_array[j]!=" ") {
					break;
						}
					int k=i-1;
					for(;k>=0;k--)
						if(equation_string_array[k]!=" ") {
					break;
						}
					solve_AND(equation_string_position_array[j],equation_string_position_array[k]);
					delete_solved_brackets(j, k, equation_string_array[j]);
					break;
				}
				else if(equation_string_array[i].equals("OR")) {

					int j=i+1;
					for(;j<equation_string_array.length;j++)
						if(!equation_string_array[j].equals(" ")) {
					break;
						}
					int k=i-1;
					for(;k>=0;k--)
						if(!equation_string_array[k].equals(" ")) {
					break;
						}

					solve_OR(equation_string_position_array[j],equation_string_position_array[k]);
			     delete_solved_brackets(j, k, equation_string_array[j]);
			     break;
				}

		}				
		else if((rb_position-lb_position)==4) {	
					if(equation_string_array[(rb_position+lb_position)/2].equals("OR")) {
						solve_OR(equation_string_position_array[rb_position-1],equation_string_position_array[lb_position+1]);			
					}
					else if(equation_string_array[(rb_position+lb_position)/2].equals("AND")) {
						solve_AND(equation_string_position_array[rb_position-1],equation_string_position_array[lb_position+1]);			
						
	}
					
					delete_solved_brackets(rb_position,lb_position,equation_string_array[rb_position-1]);
					
		
		}
		else if((rb_position-lb_position)==3){
			
		if(equation_string_array[lb_position+1].equals("NOT")) {
			System.out.println("NOT");
			solve_NOT(equation_string_position_array[rb_position-1]);}
		delete_solved_brackets(rb_position,lb_position,equation_string_array[rb_position-1]);

		}
		else if((rb_position-lb_position)>4){
			for(int i=lb_position;i<rb_position;i++) {
				if(equation_string_array[i].equals("NOT")) {					
					for(int j=i+1;j<rb_position;j++)
						if(equation_string_array[j]!=" ") {
					solve_NOT(equation_string_position_array[j]);
					delete_solved_brackets(j, i, equation_string_array[j]);
					break;
						}
					}
				else if(equation_string_array[i].equals("AND")) {
					int j=i+1;
					for(;j<equation_string_array.length;j++)
						if(equation_string_array[j]!=" ") {
					break;
						}
					int k=i-1;
					for(;k>=0;k--)
						if(equation_string_array[k]!=" ") {
					break;
						}
					solve_AND(equation_string_position_array[j],equation_string_position_array[k]);
					delete_solved_brackets(j, k, equation_string_array[j]);
					break;
				}
				else if(equation_string_array[i].equals("OR")) {
					int j=i+1;
					for(;j<equation_string_array.length;j++)
						if(equation_string_array[j]!=" ") {
					break;
						}
					int k=i-1;
					for(;k>=0;k--)
						if(equation_string_array[k]!=" ") {
					break;
						}
					solve_OR(equation_string_position_array[j],equation_string_position_array[k]);
			        delete_solved_brackets(j, k, equation_string_array[j]);
			        break;
			        }
			}
			if(!isNotSolved(equation_string_array,lb_position,rb_position)) {
	        	equation_string_array[lb_position]=equation_string_array[rb_position]=" ";
	        	System.out.println("round :"+Arrays.toString(equation_string_array));
	    		for (int i = 0; i < results.length; i++)
	    			System.out.println("boolean result for "+Arrays.toString(bool_result[i]));


	        	
	        }
			
		}	
		
		
		
		else {
			
			
		}
		
	//}
		System.out.println("round :"+Arrays.toString(equation_string_array));
		for (int i = 0; i < results.length; i++)
			System.out.println("boolean result for "+Arrays.toString(bool_result[i]));

	}
		
	}

	
	
	private boolean isNotSolved(String[] s,int start,int end) {

		int last_result=0;
		
		for(int i=start;i<end;i++) 
			if(isNotReserved(s[i]))
				last_result++;
			if(last_result>1)
				return true;
			return false;
		}

	private void delete_solved_brackets(int rb_position, int lb_position,String index) {
		
		for(int i=lb_position;i<=rb_position;i++) {
			if(!equation_string_array[i].equals(index))equation_string_array[i]=" ";
		}
	}

	private void solve_OR(int j, int k) {
		for(int i=0;i<3;i++)
			bool_result[j][i]=bool_result[j][i]|bool_result[k][i];

		
	}

	private void solve_AND(int j, int k) {

		for(int i=0;i<3;i++)
			bool_result[j][i]=bool_result[j][i]&bool_result[k][i];
		
		
	}

	private void solve_NOT(int j) {
		System.out.println(j);
		for(int i=0;i<3;i++) {
			bool_result[j][i]=!bool_result[j][i];	
		}
	}

	public boolean isNotReserved(String str) {
		if(str.equals(")")||str.equals("(")||str.equals("OR")||str.equals("AND")||str.equals("NOT")||str.equals(" "))
			return false;
		return true;
	}

	
	
	public void prepare_operands_result(File[] files) throws IOException {
		String index;
		Scanner scanners[]=new Scanner[files.length];
		int n=0;
		System.out.println("operands : "+Arrays.toString(L));
		System.out.println("operations : "+Arrays.toString(op));

		results = new int[L.length][3];

		{
		//	URL file1 = new URL(
		//			"http://www.fileconvoy.com/gf.php?id=g528e74c71b9472821000135288.1235850aab3c7d3958d0d0e&sts=15444783189447107243b24aa427804689cfee065d59b609ef66");
		//	URL file2 = new URL(
		//			"http://www.fileconvoy.com/gf.php?id=gf163a5bf9068cbee1000135288.1235851e0ef26b34b47e5c6&sts=15444783189447107243b24aa427804689cfee065d59b609ef66");
		//	URL file3 = new URL(
			//		"http://www.fileconvoy.com/gf.php?id=g3ce571d00d504e8d1000135288.12358528f5a99be8e4ec6b3&sts=15444783189447107243b24aa427804689cfee065d59b609ef66");
			
			
	//		Scanner s1 = new Scanner(file1.openStream(), "UTF-8");
	//		Scanner s2 = new Scanner(file2.openStream(), "UTF-8");
	//		Scanner s3 = new Scanner(file3.openStream(), "UTF-8");
			for(File file:files) {
				scanners[n]=new Scanner(new FileInputStream(file));
				n++;
			}
			int counter2 = 0;
			for(Scanner sc:scanners) {		
			while (sc.hasNext()) {
				index = sc.next();
				int counter = 0;
				for (String operand : L) {
					if (index.contains(operand))
						results[counter][counter2]++;
					counter++;
				}
			}
			counter2++;
			}
		}
		for (int i = 0; i < results.length; i++)
			System.out.println("result for "+L[i]+" "+Arrays.toString(results[i]));

		bool_result=new boolean[results.length][3];
		for(int i=0;i<bool_result.length;i++)
			for(int j=0;j<3;j++)
				if(results[i][j]>0)
					bool_result[i][j]=true;
				else
					bool_result[i][j]=false;
		for (int i = 0; i < results.length; i++)
			System.out.println("boolean result for "+L[i]+" "+Arrays.toString(bool_result[i]));

	}
public String getResult() {
	int counter=1;
	String result="";
	for(int i=0;i<equation_string_array.length;i++) {
		
		if(equation_string_array[i]!=" ") {
			int result_pos=equation_string_position_array[i];
			boolean r[]=bool_result[result_pos];
			for(boolean bool:r) {
				
				if(bool==true) 
					result+="file "+counter+" true, ";
				else
					result+="file "+counter+" false, ";
				counter++;
			}
			break;
		}
	}
	
	
	
	return result;
	
}
	public static String getFileResults(String equation,File[] files) {
		Linear_Equation linear_Equation = null;
		try {
			linear_Equation=	new Linear_Equation(equation);
			String s=linear_Equation.format();
			linear_Equation.prepare_operands_result(files);
			linear_Equation.solve(linear_Equation.equation_string_array=s.split(" "));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return linear_Equation.getResult();
	}

	}
