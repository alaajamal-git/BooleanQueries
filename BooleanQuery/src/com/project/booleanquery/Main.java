package com.project.booleanquery;
import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class Main {
	public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException {
		File[] files_of_search=new File[3];
		files_of_search[0]=	new File("D:\\e\\file1.txt");
		files_of_search[1]=	new File("D:\\e\\file2.txt");
		files_of_search[2]=new File("D:\\e\\file3.txt");
		
		// String result= Linear_Equation.getFileResults("alaa OR (apple OR zina)");
		// String result=	Linear_Equation.getFileResults("(alaa OR apple OR zina)");
		//String result=	Linear_Equation.getFileResults("alaa OR apple OR zina");
		//String result=	Linear_Equation.getFileResults("(alaa OR apple) OR zina");
		//String result=  Linear_Equation.getFileResults("alaa OR (apple AND zina)");	  
	     //String result= Linear_Equation.getFileResults("alaa OR (apple AND zina)");
		//String result=	Linear_Equation.getFileResults("(alaa OR apple) AND zina");		
		//String result=	Linear_Equation.getFileResults("(alaa OR (apple OR zina)) AND apple");
		//String result=	Linear_Equation.getFileResults("apple AND (alaa OR (apple OR zina)) ");	
		//String result=Linear_Equation.getFileResults("(search AND query) OR (search AND retrieve)");
		
		String result=	Linear_Equation.getFileResults("(search AND query) OR (search AND retrieve)",files_of_search);
		//System.out.println("-----------------------------------------");
		System.out.println("final result :\n"+result);
	}
	
}
