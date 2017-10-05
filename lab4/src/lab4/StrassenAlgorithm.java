package lab4;

import java.util.*;

public class StrassenAlgorithm
{
	private static long startTime, finishTime;
	static Random rand;
	
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the number of rows & columns (1 number will be both): ");
		int size = input.nextInt();
		
		int[][] arrayA = new int[size][size];
		int[][] arrayB = new int[size][size];
		
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
//				arrayA[i][j] = (int)(Math.random() * ((50 - 1) + 1))+1;		Generates random #s in arrays
//				arrayB[i][j] = (int)(Math.random() * ((100 - 1) + 1))+1;	typically disabled to better test speeds
				
				arrayA[i][j] = 2;
				arrayB[i][j] = 2;
			}
		}
		
		int x = arrayA.length;
		if(!isPowerofTwo(arrayA.length)) //handles padding 0's to make arrays be power of Two
		{
			while(!isPowerofTwo(x))
			{
				x++;
			}
		}
		//handles the actual copying of arrays into new arrays
		if(x != arrayA.length)
		{
			int[][] new_array1 = new int[x][x];
			int[][] new_array2 = new int[x][x];
				
			for(int i = 0; i < x; i++)
			{
				if(i < arrayA.length)
				{
					new_array1[i] = Arrays.copyOf(arrayA[i], x);
					new_array2[i] = Arrays.copyOf(arrayB[i], x);
				}
				else
				{
					for(int j = 0; j < x; j++)
					{
						new_array1[i][j] = 0;
						new_array2[i][j] = 0;
					}
				}
			}
			new StrassenAlgorithm(new_array1, new_array2);
		}
		else
		{
			new StrassenAlgorithm(arrayA, arrayB);
		}
		
	}
	
	private int s = 256, n;
	private boolean modifier;
	
	private StrassenAlgorithm(int[][] arrayA, int[][] arrayB)
	{
		n = arrayA.length;
		
		startTime = System.currentTimeMillis();
		
		int[][] arrayC = strassenAux(arrayA, arrayB, s);

		finishTime = System.currentTimeMillis();
		
//		System.out.println("The time in milliseconds to sort is: " +  (finishTime - startTime));
		
		printAnswer(arrayC, modifier);
		
	}
	//recursive method that breaks down the array by powers of 2
	private int[][] strassenAux(int[][] arrayA,int[][] arrayB, int s)
	{
		//if s is less than or equal to n (size of array) then performs brute force
		if(s >= n)
		{
			int[][] arrayC = bruteForce(arrayA, arrayB);
			modifier = false;
			return arrayC;
		}
		//base case
		int N = arrayA[0].length;
		if(N == 2)
		{
			int[][] arrayC = calculateC(arrayA, arrayB);
			return arrayC;
		}
		
		//decided on using a method to divide arrays
		int[][] A1 = divideArray(arrayA, 0, 0);
		int[][] A2 = divideArray(arrayA, 0, N/2);
		int[][] A3 = divideArray(arrayA, N/2, 0);
		int[][] A4 = divideArray(arrayA, N/2, N/2);
		
		int[][] B1 = divideArray(arrayB, 0, 0);
		int[][] B2 = divideArray(arrayB, 0, N/2);
		int[][] B3 = divideArray(arrayB, N/2, 0);
		int[][] B4 = divideArray(arrayB, N/2, N/2);

		int[][] C1 = strassenAux(A1, B1, s);
		int[][] C2 = strassenAux(A2, B2, s);
		int[][] C3 = strassenAux(A3, B3, s);
		int[][] C4 = strassenAux(A4, B4, s);
	
		
		
		modifier = true;

		return(combineArray(C1, C2, C3, C4, N/2, N/2, N/2, N/2));
		
		
	}
	//This method has been provided by Professor Indika provided in 'Divide & Conquer' slides D2L
	private int[][] bruteForce(int[][] arrayA,int[][] arrayB)
	{
		int[][] arrayC = new int[arrayA.length][arrayA[0].length];
		
		int N = arrayA.length;
		
		for(int i = 0; i < N; i++)
		{
			for(int j = 0; j < N; j++)
			{
				arrayC[i][j] = 0;
				for(int k = 0; k < N; k++)
				{
					arrayC[i][j] += arrayA[i][k] * arrayB[k][j];
				}
			}
		}
		return arrayC;
	}
	//algorithm provided by Professor Indika's slides on Strassen's Algorithm
	private int[][] calculateC(int[][] arrayA, int[][] arrayB)
	{
		int[][] arrayC = new int[2][2];
		int m1 = (arrayA[0][0] + arrayA[1][1]) * (arrayB[0][0] + arrayB[1][1]);
		int m2 = (arrayA[1][0] + arrayA[1][1]) * arrayB[0][0];
		int m3 = arrayA[0][0] * java.lang.Math.abs(arrayB[0][1] - arrayB[1][1]);
		int m4 = arrayA[1][1] * java.lang.Math.abs(arrayB[1][0] - arrayB[0][0]);
		int m5 = (arrayA[0][0] + arrayA[0][1]) * arrayB[1][1];
		int m6 = java.lang.Math.abs(arrayA[1][0] - arrayA[0][0]) * (arrayB[0][0] + arrayB[0][1]);
		int m7 = java.lang.Math.abs(arrayA[0][1] - arrayA[1][1]) * (arrayB[1][0] + arrayB[1][1]);
		
		arrayC[0][0] = (m1 + m4)-(m5 + m7);
		arrayC[1][0] = (m3 + m5);
		arrayC[0][1] = (m2 + m4);
		arrayC[1][1] = (m1 + m3)-(m2 + m6);
		
		return arrayC;
	}
	//method that handles combining the final array from four separate arrays
	private int[][] combineArray(int[][] C1, int[][] C2, int[][] C3, int[][] C4, int column, int row1, int row2, int column2)
	{
		int[][] arrayC = new int[C1.length*2][C1[0].length*2];
		
		for(int i = 0, i2 = 0, i3 = row1, i4 = row2; i < C1.length; i++, i2++, i3++, i4++)
		{
			for(int j = 0, j2 = column, j3 = 0, j4 = column2; j < C1[0].length; j++, j2++, j3++, j4++)
			{
				arrayC[i][j] = C1[i][j];
				arrayC[i2][j2] = C2[i][j];
				arrayC[i3][j3] = C3[i][j];
				arrayC[i4][j4] = C4[i][j];
			}
		}
		return arrayC;
	}
	//Prints the answer array & time, currently printing the array is disabled w/ comments
	private void printAnswer(int[][] arrayC, boolean modifier)
	{
//		for(int i = 0; i < arrayC.length; i++)
//		{
//			System.out.print("| ");
//			for(int j = 0; j < arrayC[0].length; j++)
//			{
//				if(modifier)
//				{
//					System.out.print((n/2) * arrayC[i][j] + " ");
//				}
//				else
//				{
//					System.out.print(arrayC[i][j] + " ");
//				}
//			}
//			System.out.println("|");
//		}
		System.out.println("The time in milliseconds to sort is: " +  (finishTime - startTime));
		System.exit(0);
	}
	//generic method of dividing array
	private int[][] divideArray(int[][] parent, int row, int column)
	{
		int[][] child = new int[parent.length/2][parent[0].length/2];
		
		for(int i = 0, i2 = row; i < child.length; i++, i2++)
		{
			for(int j = 0, j2 = column; j < child[0].length; j++, j2++)
			{
				child[i][j] = parent[i2][j2];
			}
		}
		return child;
	}
	//found in online documentation
	private static boolean isPowerofTwo(int size)
	{
		return (size & (size - 1)) == 0;
	}
}
