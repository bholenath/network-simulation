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

//main class Network starts

public class Network
{
//Adding static class instance variables so it could be easily accessed by the other class

static int cases_length;
static int max_queue_size;
static int frame_length;
static int frame_length1;
static double[][] cases_trans = new double[5][4], cases_prop = new double[5][4];

static byte [] frame_size = new byte[100 * 1000]; // 1KB = 1000 Bytes  Each frame size(100 KB)
static byte one_byte_data = 13;

static byte [] frame_size1 = new byte[20 * 1000]; // 1KB = 1000 Bytes Each Ack. Size (20 KB)
static byte one_byte_data1 = 21;

public static void main(String args[])
{
try //Taking the whole function for Exception catch 
{
double link_speed = 2 * Math.pow(10, 8);
double bandwidth = Math.pow(10, 6);
double distance = Math.pow(10, 3);

// Adding 4 pre-defined cases to each node for transmission and propagation respectively

float [] node1 = {0.8f,0.5f,1f,2.5f};
float [] node2 = {0.8f,0.5f,1f,2.5f};
float [] node3 = {1.6f,1f,2f,5f};
float [] node4 = {1.6f,1f,2f,5f};
float [] node5 = {1.6f,1f,2f,5f};
//float [] sixth_node = {1.6f,1f,2f,5f};
//float [] seventh_node = {1.6f,1f,2f,5f};
float [] distance1 = {20f,30f,35f,15f};
float [] distance2 = {20f,30f,35f,15f};
float [] distance3 = {20f,30f,35f,15f};
float [] distance4 = {20f,30f,35f,15f};
float [] distance5 = {20f,30f,35f,15f};

for(int i=0;i<frame_size.length;i++) // Adding data to the frame array to make 100KB
frame_size[i] = one_byte_data;

cases_length = node1.length;
frame_length = frame_size.length * 8; // converting Bytes into Bits

for(int i=0;i<frame_size1.length;i++) // Adding data to the ack. array to make 20KB
frame_size1[i] = one_byte_data1;

frame_length1 = frame_size1.length * 8; // converting Bytes into Bits

for(int j=0;j<cases_length;j++)
{
for(int i=0;i<5;i++)
{
int k = i+1;

// Generating two 2D arrays for transmission and propagation time at each node for frame and ack packets

switch(k)
{
case 1:
{
cases_trans[i][j] = frame_length / (node1[j] * bandwidth);
cases_prop[i][j] = (distance1[j] * distance) / link_speed;
break;
}
case 2:
{
cases_trans[i][j] = frame_length / (node2[j] * bandwidth);
cases_prop[i][j] = (distance2[j] * distance) / link_speed;
break;
}
case 3:
{
cases_trans[i][j] = frame_length / (node3[j] * bandwidth);
cases_prop[i][j] = (distance3[j] * distance) / link_speed;
break;
}
case 4:
{
cases_trans[i][j] = frame_length / (node4[j] * bandwidth);
cases_prop[i][j] = (distance4[j] * distance) / link_speed;
break;
}
case 5:
{
cases_trans[i][j] = frame_length1 / (node5[j] * bandwidth);
cases_prop[i][j] = (distance5[j] * distance) / link_speed;
break;
}
default:
break;
}//case loop ends

}//inner for ends
}//outer for ends

System.out.print("\nEnter the queue size limit(in KB) before overflow : ");
Scanner sc = new Scanner(System.in);
max_queue_size = Integer.parseInt(sc.next()); //User Input for max queue size

System.out.println("\n\n\t\t *Table for Queue Size of Every Node*\n");

for(int j=0; j<cases_length; j++) //For loop to run Thread function for each case
{
Running r = new Running(j); //Calling the other(Thread) class constructor
System.out.println("Case "+(j+1)+" =>\n");
System.out.println("\t\tNode 1\tNode 2\tNode 3\tNode 4\tNode 5");
System.out.println("\t\t\t\t\t   (Total Received @ 5)\n");
r.start(); //Calling Thread class run function

//Synchronizing with the other thread in Running class
   
synchronized(r)
{
try
{
r.wait(); //Applied wait to the current thread waiting for run function in Thread class to finish
}
catch(InterruptedException e) // Catch for the wait function
{
e.printStackTrace();
}
}//sync ends
}//for ends
System.exit(0); //Terminating the program after getting values from every node for each case 
}
catch(Exception ioe) //Exception catch for the main function
{
ioe.printStackTrace();
}
}//main ends

}// class Network ends

//Thread class Running starts

class Running extends Thread
{

// Declaring instance variables for this class

int index;

//Array list for queue of each node
ArrayList<byte[]> queue_size1 = new ArrayList<byte[]>(),queue_size2 = new ArrayList<byte[]>(),queue_size3 = new ArrayList<byte[]>(),queue_size4 = new ArrayList<byte[]>(),queue_size5 = new ArrayList<byte[]>();

//Array for collecting transmitted and propagated frames for each second

double cumulative_frames_1 [] = new double[100];
double cumulative_frames_2 [] = new double[100];
double cumulative_frames_3 [] = new double[100];
double cumulative_frames_4 [] = new double[100];
//double cumulative_frames_5 [] = new double[100];

double cumulative_frames_13 [] = new double[100];
double cumulative_frames_23 [] = new double[100];
double cumulative_frames_34 [] = new double[100];
double cumulative_frames_45 [] = new double[100];
//double cumulative_frames_54 [] = new double[100];

public Running(int j) //Argument Constructor called from main in Network class(Receiving the case number)
{
index = j;
}

@Override
public void run() // Thread run function called from Thread.start() from main function
{
int u = 0;

//Adding frames to node 1 and 2 initially

queue_size1.add(Network.frame_size);
queue_size1.add(Network.frame_size);
queue_size1.add(Network.frame_size);

queue_size2.add(Network.frame_size);
queue_size2.add(Network.frame_size);
queue_size2.add(Network.frame_size);
queue_size2.add(Network.frame_size);

synchronized(this) //Synchronizing with the thread of main
{
try
{
boolean check_over = true;

//Checking for the value of the queue size at each node at every second

while(queue_size1.size() < Network.max_queue_size && queue_size2.size() < Network.max_queue_size && check_over==true)
{
long start_time = System.currentTimeMillis();
long end_time = System.currentTimeMillis();
while(end_time - start_time < 1000)
{
end_time = System.currentTimeMillis();
}
u++; //Second for which the calculation is being carried out 
check_over = display(u); //Calling display method which houses all the calculations for frames transmission and propagation
this.sleep(1000); //Sleeping this thread for 1 second to wait for the result from the display function
}//while ends with ending the current case

System.out.println("\n\t       ******** Sorry Queue Overflown ********\n");
System.out.println("\t       ******* Excess Packets Rejected *******\n");
System.out.println("\t       ************* Case "+(index+1)+" Ends *************\n");
System.out.println("\t       ******\t*****\t*****\t*****\t******\n\n");

notify(); //Waking up the thread in main function to continue for the next case
this.wait(); //Stopping this thread for the time 
}
catch(InterruptedException ie) //Catch for the wait exception
{
ie.printStackTrace();
}

}//sync closed

}//run function closed

//Display function the calculating part(hence the most important part for the program)

public boolean display(int u) //Will return true or false to continue or end respectively the current case 
{
Thread t1 = new Thread(); // New thread for this function

//Local variables for calculating transmitted and propagated frames between each node and their respective time

double transmitted_frames_1 = 0.0d;
double transmitted_frames_2 = 0.0d;
double transmitted_frames_3 = 0.0d;
double transmitted_frames_4 = 0.0d;
//double transmitted_frames_5 = 0.0d;

double propagated_frames_13 = 0.0d;
double propagated_frames_23 = 0.0d;
double propagated_frames_34 = 0.0d;
double propagated_frames_45 = 0.0d;
//double propagated_frames_54 = 0.0d;

double remove_frames_1 = 0.0d;
double remove_frames_2 = 0.0d;
double remove_frames_3 = 0.0d;
double remove_frames_4 = 0.0d;
//double remove_frames_5 = 0.0d;

double time_trans_13 = 0.0d;
double time_trans_23 = 0.0d;
double time_trans_45_1 = 0.0d;
double time_trans_45_2 = 0.0d;

double frames_3 = 0.0d;
double frames_4 = 0.0d;
double frames_5 = 0.0d;

double time_ratio = 0.0d;
double time_ratio1 = 0.0d;

double time_reach_431 = 0.0d;
double time_reach_432 = 0.0d;
double time_reach_5431 = 0.0d;
double time_reach_5432 = 0.0d;

//double propagated_frames_45_save = 0.0d;

double time_13 = 0;
double time_43 = 0;

synchronized(t1) //Synchronizing with the function run and of main class for maintaining the concurrency of elements  
{
for(int i=0; i<1; i++)
{
transmitted_frames_1 = Math.floor((double)(u)/Network.cases_trans[i][index]);
time_trans_13 = Network.cases_prop[i][index] + Network.cases_trans[i][index];
propagated_frames_13 = Math.floor((double)(u)/(Network.cases_prop[i][index] + Network.cases_trans[i][index]));
cumulative_frames_1[u-1] = transmitted_frames_1;
cumulative_frames_13[u-1] = propagated_frames_13;

i++;

transmitted_frames_2 = Math.floor((double)(u)/Network.cases_trans[i][index]);
time_trans_23 = Network.cases_prop[i][index] + Network.cases_trans[i][index];
propagated_frames_23 = Math.floor((double)(u)/(Network.cases_prop[i][index] + Network.cases_trans[i][index]));
cumulative_frames_2[u-1] = transmitted_frames_2;
cumulative_frames_23[u-1] = propagated_frames_23;

i++;

time_trans_13 = time_trans_13 + Network.cases_trans[i][index];
time_trans_23 = time_trans_23 + Network.cases_trans[i][index];

time_reach_431 = time_trans_13 + Network.cases_prop[i][index];
time_reach_432 = time_trans_23 + Network.cases_prop[i][index];

if(queue_size3.size() > 0)
{

if(time_trans_13 <= time_trans_23)
{
time_ratio = Math.floor(((double)(u) * time_trans_23) / ((double)(u) * time_trans_13));

time_13 = (double)(u);

while(time_13 > 0)
{

for(int count=0; count<Math.floor(time_ratio); count++)
{
time_13 = time_13 - time_trans_13;

if(time_13 >= 0)
transmitted_frames_3++;

else if(time_13 == 0)
{
transmitted_frames_3++;
break;
}

else
break;
}//(check 3 transmission) for ends

if(time_13 > 0)
{
time_13 = time_13 - time_trans_23;
transmitted_frames_3++;
}//condition check for transmission from 3 ends

}//while ends

}//if time_trans_13 <= time_trans_23 ends

else
{
time_ratio = Math.floor(((double)(u) * time_trans_13) / ((double)(u) * time_trans_23));
time_13 = (double)(u);

while(time_13 > 0)
{

for(int count=0; count<Math.floor(time_ratio); count++)
{
time_13 = time_13 - time_trans_23;

if(time_13 >= 0)
transmitted_frames_3++;

else if(time_13 == 0)
{
transmitted_frames_3++;
break;
}

else
break;
}

if(time_13 > 0)
{
time_13 = time_13 - time_trans_13;
transmitted_frames_3++;
}

}//while ends

}//else time_trans_13 > time_trans_23 ends

cumulative_frames_3[u-1] = transmitted_frames_3;
//frames collected for 3 transmission

//Check for 3 propagation starts

if(time_reach_431 <= time_reach_432)
{
time_ratio1 = Math.floor(((double)(u) * time_reach_432) / ((double)(u) * time_reach_431));

time_43 = (double)(u);

while(time_43 > 0)
{

for(int count1=0; count1<Math.floor(time_ratio1); count1++)
{
time_43 = time_43 - time_reach_431;

if(time_43 >= 0)
propagated_frames_34++;

else if(time_43 == 0)
{
propagated_frames_34++;
break;
}

else
break;
}//(check 3 propagation) for ends

if(time_43 > 0)
{
time_43 = time_43 - time_reach_432;
propagated_frames_34++;
}//condition check for propagation from 3 ends

}//while ends

}//if time_reach_431 <= time_reach_432 ends

else
{
time_ratio1 = Math.floor(((double)(u) * time_reach_431) / ((double)(u) * time_reach_432));
time_43 = (double)(u);

while(time_43 > 0)
{

for(int count1=0; count1<Math.floor(time_ratio1); count1++)
{
time_43 = time_43 - time_reach_432;

if(time_43 >= 0)
propagated_frames_34++;

else if(time_43 == 0)
{
propagated_frames_34++;
break;
}

else
break;
}

if(time_43 > 0)
{
time_43 = time_43 - time_reach_431;
propagated_frames_34++;
}

}//while ends

}//else time_reach_431 > time_reach_432 ends

cumulative_frames_34[u-1] = propagated_frames_34;
//frames collected for 3 propagation or 4 reach

}// if queue_size > 0 ends

else
{
if(u==1)
{
cumulative_frames_3[u-1] = 0;
cumulative_frames_34[u-1] = 0;
}
else
{
cumulative_frames_3[u-1] = cumulative_frames_3[u-2];
cumulative_frames_34[u-1] = cumulative_frames_34[u-2];
}
}// else if queue_size == 0 ends

i++;

time_trans_45_1 = time_reach_431 + Network.cases_trans[i][index];
time_trans_45_2 = time_reach_432 + Network.cases_trans[i][index];

time_reach_5431 = time_trans_45_1 + Network.cases_prop[i][index];
time_reach_5432 = time_trans_45_2 + Network.cases_prop[i][index];

if(queue_size4.size() > 0)
{

if(time_trans_45_1 <= time_trans_45_2)
{
time_ratio = Math.floor(((double)(u) * time_trans_45_2) / ((double)(u) * time_trans_45_1));

time_13 = (double)(u);

while(time_13 > 0)
{

for(int count=0; count<Math.floor(time_ratio); count++)
{
time_13 = time_13 - time_trans_45_1;

if(time_13 >= 0)
transmitted_frames_4++;

else if(time_13 == 0)
{
transmitted_frames_4++;
break;
}

else
break;
}//(check 3 transmission) for ends

if(time_13 > 0)
{
time_13 = time_13 - time_trans_45_2;
transmitted_frames_4++;
}//condition check for transmission from 3 ends

}//while ends

}//if time_trans_45_1 <= time_trans_45_2 ends

else
{
time_ratio = Math.floor(((double)(u) * time_trans_45_1) / ((double)(u) * time_trans_45_2));
time_13 = (double)(u);

while(time_13 > 0)
{

for(int count=0; count<Math.floor(time_ratio); count++)
{
time_13 = time_13 - time_trans_45_2;

if(time_13 >= 0)
transmitted_frames_4++;

else if(time_13 == 0)
{
transmitted_frames_4++;
break;
}

else
break;
}

if(time_13 > 0)
{
time_13 = time_13 - time_trans_45_1;
transmitted_frames_4++;
}

}//while ends

}//else time_trans_45_1 > time_trans_45_2 ends

cumulative_frames_4[u-1] = transmitted_frames_4;
//frames collected for 4 transmission

//Check for 4 propagation starts

if(time_reach_5431 <= time_reach_5432)
{
time_ratio1 = Math.floor(((double)(u) * time_reach_5432) / ((double)(u) * time_reach_5431));

time_43 = (double)(u);

while(time_43 > 0)
{

for(int count1=0; count1<Math.floor(time_ratio1); count1++)
{
time_43 = time_43 - time_reach_5431;

if(time_43 >= 0)
propagated_frames_45++;

else if(time_43 == 0)
{
propagated_frames_45++;
break;
}

else
break;
}//(check 4 propagation) for ends

if(time_43 > 0)
{
time_43 = time_43 - time_reach_5432;
propagated_frames_45++;
}//condition check for propagation from 3 ends

}//while ends

}//if time_reach_5431 <= time_reach_5432 ends

else
{
time_ratio1 = Math.floor(((double)(u) * time_reach_5431) / ((double)(u) * time_reach_5432));
time_43 = (double)(u);

while(time_43 > 0)
{

for(int count1=0; count1<Math.floor(time_ratio1); count1++)
{
time_43 = time_43 - time_reach_5432;

if(time_43 >= 0)
propagated_frames_45++;

else if(time_43 == 0)
{
propagated_frames_45++;
break;
}

else
break;
}

if(time_43 > 0)
{
time_43 = time_43 - time_reach_5431;
propagated_frames_45++;
}

}//while ends

}//else time_reach_5431 > time_reach_5432 ends

cumulative_frames_45[u-1] = propagated_frames_45;
//frames collected for 3 propagation or 4 reach

}// if queue_size > 0 ends

else
{
if(u==1)
{
cumulative_frames_4[u-1] = 0;
cumulative_frames_45[u-1] = 0;
}
else
{
cumulative_frames_4[u-1] = cumulative_frames_4[u-2];
cumulative_frames_45[u-1] = cumulative_frames_45[u-2];
}
}// else if queue_size == 0 ends

}

//Variables to calculate the frames to be removed or added at the current second for each node

if(u!=1) //If the second(time) is greater than one for the current case
{
remove_frames_1 = (cumulative_frames_1[u-1] - cumulative_frames_1[u-2]);
remove_frames_2 = (cumulative_frames_2[u-1] - cumulative_frames_2[u-2]);
remove_frames_3 = (cumulative_frames_3[u-1] - cumulative_frames_3[u-2]);
remove_frames_4 = (cumulative_frames_4[u-1] - cumulative_frames_4[u-2]);
//remove_frames_5 = (cumulative_frames_5[u-1] - cumulative_frames_5[u-2]);

frames_3 = (cumulative_frames_13[u-1] + cumulative_frames_23[u-1]) - (cumulative_frames_13[u-2] + cumulative_frames_23[u-2]);
frames_4 = (cumulative_frames_34[u-1] - cumulative_frames_34[u-2]);
frames_5 = cumulative_frames_45[u-1];
}

else //If this is the first second for the current case
{
remove_frames_1 = cumulative_frames_1[u-1];
remove_frames_2 = cumulative_frames_2[u-1];
remove_frames_3 = cumulative_frames_3[u-1];
remove_frames_4 = cumulative_frames_4[u-1];
//remove_frames_5 = cumulative_frames_5[u-1];

frames_3 = (cumulative_frames_13[u-1] + cumulative_frames_23[u-1]);
frames_4 = cumulative_frames_34[u-1];
frames_5 = cumulative_frames_45[u-1];
}

//First removing the frames for the current second and then adding the received frames for the current second 

//Removing frames from the queue of each nodes and terminating the remove frames loop if no frame left

try
{
for(int y=0; y < Math.floor(remove_frames_1); y++)
{
if(queue_size1.size()>0)
queue_size1.remove(0);
else
break;
}

for(int z=0; z < Math.floor(remove_frames_2); z++)
{
if(queue_size2.size()>0)
queue_size2.remove(0);
else
break;
}

for(int x=0; x < Math.floor(remove_frames_3); x++)
{
if(queue_size3.size()>0)
queue_size3.remove(0);
else
break;
}

for(int w=0; w < Math.floor(remove_frames_4); w++)
{
if(queue_size4.size()>0)
queue_size4.remove(0);
else
break;
}
/*
for(int v=0; v < Math.floor(remove_frames_5); v++)
{
if(queue_size5.size()>0)
queue_size5.remove(0);
else
break;
}
*/
}//try ends
catch(Exception e) //Exception Catch for this removing frames
{}

//Adding frames to the queue for each node and discarding the packets if queue reaches max size

int c=0;

while(queue_size1.size() < Network.max_queue_size && c<3)
{
queue_size1.add(Network.frame_size);
c++;
}

int d=0;

while(queue_size2.size() < Network.max_queue_size && d<4)
{
queue_size2.add(Network.frame_size);
d++;
}

int e=0;

for(; e<Math.floor(frames_3); e++)
{
if(queue_size3.size() < Network.max_queue_size)
queue_size3.add(Network.frame_size);
else
break;
}

int f=0;

for(; f<Math.floor(frames_4); f++)
{
if(queue_size4.size() < Network.max_queue_size)
queue_size4.add(Network.frame_size);
else
break;
}

int g=0;

for(; g<Math.floor(frames_5); g++)
{
if(queue_size5.size() < Network.max_queue_size)
queue_size5.add(Network.frame_size1);
else
break;
}

//Displaying the arrival rate at each node for the current second

System.out.println("\nArrival Rate =>\t   " + "3\t   4\t   " + (int)(Math.floor(frames_3)) + "\t   " + (int)(Math.floor(frames_4)) + "\t   " + (int)(Math.floor(frames_5)) + "\n");

//Displaying the departure rate

System.out.println("Departure Rate =>  " + (int)(Math.floor(remove_frames_1)) + "\t   " + (int)(Math.floor(remove_frames_2)) + "\t   " + (int)(Math.floor(remove_frames_3)) + "\t   " + (int)(Math.floor(remove_frames_4)) + "\t   -\n");

//Display the queue size at the current second on each node

System.out.println("\t\t------\t------\t------\t------\t------");

double ack = Math.floor((queue_size5.size() / 4));

//String ack gives the total number of acknowledgement sent from Node 5 till that second

System.out.println(u+" second/s =>\t   " + queue_size1.size() + "\t   " + queue_size2.size() + "\t   " + queue_size3.size() + "\t   " + queue_size4.size() + "\t   " + queue_size5.size() + "   (Ack. sent : " + Math.round(ack) + ")");

System.out.println("\t\t------\t------\t------\t------\t------\n");

//Checking for the queue overflow(case ends if queue reach its max size at any node)

double rej = 0;

if(c!=3 || d!=4 || e!=Math.floor(frames_3) || f!=Math.floor(frames_4) || g!=Math.floor(frames_5))
{

if(c!=3)
{
rej = 3-c;
System.out.println("Packets Loss => " + "   " + Math.round(rej) + "\t------\t------\t------\t------\n");
}
else if(d!=4)
{
rej = 4-d;
System.out.println("Packets Loss => ------\t" + "   " +  Math.round(rej) + "\t------\t------\t------\n");
}
else if(e!=Math.floor(frames_3))
{
rej = Math.floor(frames_3) - (double)(e);
System.out.println("Packets Loss => ------\t------\t" + "   " +  Math.round(rej) + "\t------\t------\n");
}
else if(f!=Math.floor(frames_4))
{
rej = Math.floor(frames_4) - (double)(f);
System.out.println("Packets Loss => ------\t------\t------\t" + "   " +  Math.round(rej) + "\t------\n");
}
else if(g!=Math.floor(frames_5))
{
rej = Math.floor(frames_5) - (double)(g);
System.out.println("Packets Loss => ------\t------\t------\t------\t" + "   " +  Math.round(rej) + "\n");
}

return false; //If overflown 
}
else
return true; //If not overflown
}//sync ends

}//display function ends

}//class Running ends