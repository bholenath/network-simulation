/* ******************************
This program is made and compiled by Harshit Bhatt
with inputs from Jenny Patel and Mahesh Kotapalli
of Group - 6 in the Computer Networking class Fall '14.
********************************** */

import java.io.*;
import java.lang.*;
import java.util.Scanner;
import java.util.*;
import java.lang.Thread;
import java.text.DecimalFormat;

//main class Network starts

public class Network
{
//Adding static class instance variables so it could be easily accessed by the other class

static int cases_length;
static int max_queue_size;
static int frame_length;
static int k = 1;
static int frame_length1;
static double[] time_delay_43 = new double[4];
static double[][] cases_trans = new double[5][4], cases_prop = new double[5][4];

static byte [] frame_size = new byte[100 * 1000]; // 1KB = 1000 Bytes  Each frame size(100 KB)
static byte one_byte_data = 13;

static byte [] frame_size1 = new byte[20 * 1000]; // 1KB = 1000 Bytes Each Ack. Size (20 KB)
static byte one_byte_data1 = 21;

static void ack_check(double a,double b,double c, double d, double e, double x,double y,double z,double w)
{

for(int i=0;i<frame_size.length;i++) // Adding data to the frame array to make 100KB
frame_size[i] = one_byte_data;

frame_length = frame_size.length * 8; // converting Bytes into Bits

for(int i=0;i<frame_size1.length;i++) // Adding data to the ack. array to make 20KB
frame_size1[i] = one_byte_data1;

frame_length1 = frame_size1.length * 8; // converting Bytes into Bits

DecimalFormat f = new DecimalFormat("##.0");

System.out.println("Case " + k + " =>\n");

double node1delay = ((frame_length)/(a*Math.pow(10,6)))+((x*Math.pow(10,6))/(2*Math.pow(10,8)));//Total delay from node 1 to node 3

double node1_out = 1/node1delay; 											

double node2delay = ((frame_length)/(b*Math.pow(10,6)))+((y*Math.pow(10,6))/(2*Math.pow(10,8)));//Total delay from node 1 to node 3

double node2_out = 1/node2delay;
        
double node3_in = node1_out + node2_out;
System.out.println("\nNode 3 Arrival Rate => "+ f.format(node3_in)+"\n");
		
double node3delay = ( ((frame_length * 2)/(d*Math.pow(10,6))) + (2* ( (z* Math.pow(10,3)) / (2*Math.pow(10,8)) )) + ((frame_length1 * 2)/(d*Math.pow(10,6))) ) / 2;

double node3_out = 1/node3delay;

double node4_in = node3_out;
System.out.println("Node 4 Arrival Rate => "+ f.format(node4_in)+"\n");
		
double node4delay = ((frame_length)/(e*Math.pow(10,6))) + ((w*Math.pow(10,3))/(2*Math.pow(10,8)));
double node4_out = 1/node4delay;

double node5_in = node4_out;
System.out.println("Node 5 Arrival Rate => "+ f.format(node5_in)+"\n");
int lastqueueSize = 0;
boolean check= false;

double queue_size1 = 0.0;
double queue_size2 = 0.0;
double queue_size3 = 0.0;
double queue_size4 = 0.0;
double queue_size5 = 0.0;


System.out.println("\n\tNode1\tNode2\tNode3\tNode4\tNode5(Total Packet)\t\n");
for(int i = 1; i <= 10 ; i++)
{
System.out.print(""+i+"\t");

if(queue_size4>=node4_out){
queue_size4 -= node4_out;
queue_size5 += node5_in;
}

if(queue_size3>=node3_out){
queue_size3 -= node3_out;
queue_size4 += node4_in;
}

if(queue_size2!=0 && queue_size1!=0)
queue_size3 += node3_in;

if(queue_size1>=node1_out && queue_size2>=node2_out){
queue_size1 -= node1_out;
queue_size2 -= node2_out;
}
            
queue_size1 +=3.0;
queue_size2 +=4.0;

System.out.print(" "+Math.floor(queue_size1)+"\t");
System.out.print(" "+Math.floor(queue_size2)+"\t");
System.out.print(" "+Math.floor(queue_size3)+"\t");
System.out.print(" "+Math.floor(queue_size4)+"\t");

			
if(!(Math.floor(queue_size5)==0)){
if((Math.floor(queue_size5)%4)==0){

System.out.println(" *"+Math.floor(queue_size5)+"\t");
check=true;
lastqueueSize = (int) Math.floor(queue_size5);
}

else if(((int) Math.floor(queue_size5)-lastqueueSize)>=4 && !check ){
System.out.println(" *"+Math.floor(queue_size5)+"\t");
lastqueueSize = (int) Math.floor(queue_size5);
}

else
{
System.out.println(" "+Math.floor(queue_size5)+"\t");
check = false;
}

}

else
System.out.println(" "+Math.floor(queue_size5)+"\t");

System.out.println("\n");
}
System.out.println("**************** CASE " + k +" ENDS *****************************\n");
k++;
}//ack_check function ends

public static void main(String args[])
{

ack_check(0.8,0.8,1.6,1.6,1.6,20.0,20.0,20.0,20.0);
ack_check(0.5,0.5,1.0,1.0,1.0,30.0,30.0,30.0,30.0);
ack_check(1.0,1.0,2.0,2.0,2.0,35.0,35.0,35.0,35.0);
ack_check(2.5,2.5,5.0,5.0,5.0,15.0,15.0,15.0,15.0);
       
}//main ends

}//class ends