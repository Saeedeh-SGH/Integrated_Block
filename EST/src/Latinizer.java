package SoftwareTesting;

import java.util.*;
import MyPackage.*;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ali shahbazi
 */


public class Latinizer{
    
    public void latinize(ArrayDouble2D testSet){
        Comparator<double[]> comprator;
        Random rand=new Random();
        
        for (int d = 0; d < testSet.GetDimension(); d++) {
            comprator = new TestCaseComprator(d);
            Arrays.sort(testSet.Arr, 0, testSet.GetSize(), comprator);
            int N = testSet.GetSize();

            for (int i = 0; i < N; i++) {
                if (!(testSet.Arr[i][d] >= (double) i / N && testSet.Arr[i][d] < (double) (i + 1) / N)) {
                    testSet.Arr[i][d] = (i + rand.nextDouble()) / N;
                }
            }
        }
    }
    
    public void latinize(ArrayDouble2D testSet, double angle)
    {
        this.rotate(angle, testSet);
        double[] min={-Math.sqrt(2)/2, 0};
        double[] max={Math.sqrt(2)/2, Math.sqrt(2)};
            
        this.latinize(testSet, min, max);
        this.rotate(-angle, testSet);  
        
        //bound check
        for(int i=0; i<testSet.GetSize();i++){
            for(int d=0;d<testSet.GetDimension();d++){
                if(testSet.Arr[i][d]<0)
                    testSet.Arr[i][d]=0;
                else if(testSet.Arr[i][d]>=1.0)
                    testSet.Arr[i][d]=0.99999999999999;
            }
        }
    }
    
    private void latinize(ArrayDouble2D testSet, double[] minInEachDimension, double[] maxInEachDimension){
        Comparator<double[]> comprator;
        Random rand=new Random();
        
        for (int d = 0; d < 2; d++) {
            comprator = new TestCaseComprator(d);
            Arrays.sort(testSet.Arr, 0, testSet.GetSize(), comprator);

            double start=minInEachDimension[d];
            double end=maxInEachDimension[d];
            
            for (int i = 0; i < testSet.GetSize()/2; i++) {
                double xbefore=start+Math.sqrt((double)i/testSet.GetSize());
                double xafter=start+Math.sqrt((double)(i+1)/testSet.GetSize());
                
                if (!(testSet.Arr[i][d] >= xbefore && testSet.Arr[i][d] <xafter)) {                    
                    testSet.Arr[i][d]=start+Math.sqrt((double)(i+rand.nextDouble())/testSet.GetSize());
                }
            }
            
            
            for (int i = testSet.GetSize()/2; i < testSet.GetSize(); i++) {               
                int j=testSet.GetSize()-i;
                double xafter=end-Math.sqrt((double)j/testSet.GetSize());
                double xbefore=end-Math.sqrt((double)(j-1)/testSet.GetSize());
                               
                if (!(testSet.Arr[i][d] >= xbefore && testSet.Arr[i][d] <xafter)) {
                    testSet.Arr[i][d]=end-Math.sqrt((double)(j-rand.nextDouble())/testSet.GetSize());
                }
            }
        }
    }
    
 
    
    
    
    
    private void rotate(double angle, ArrayDouble2D testSet){//angle in degree
        double[] polar;
        double[] res;
        angle=angle/180*Math.PI;
        
        for(int i=0;i<testSet.GetSize();i++){
            polar=cartesianToPolar(testSet.Arr[i]);
            polar[1]+=angle;
            res=polarToCartesian(polar);
            testSet.Arr[i][0]=res[0];
            testSet.Arr[i][1]=res[1];
        }
        
        
    }
    
    
    private double[] cartesianToPolar(double[] cart){
        double[] polar=new double[2];
        double[] center={0,0};
        polar[0]=this.getDistance2D(cart, center);//r
        polar[1]=this.getAngle(cart);//teta -p/2...p/2
        
        return polar;
    }
    private double[] polarToCartesian(double[] polar){
        double[] cart=new double[2];
        double[] center={0,0};
        cart[0]=polar[0]*Math.cos(polar[1]);
        cart[1]=polar[0]*Math.sin(polar[1]);
        
        return cart;
    }
    private double getDistance2D(double[] a, double[] b){
        return Math.sqrt((a[0]-b[0])*(a[0]-b[0]) + (a[1]-b[1])*(a[1]-b[1]));
    } 
    private double getAngle(double[] a){
        return Math.atan2(a[1], a[0]);
    }
    
    
    class TestCaseComprator implements Comparator<double[]> {

        private int dimensionIndex;
        public TestCaseComprator(int index){//index indicate to sort test set based on which dimension (0, D-1)
            dimensionIndex=index;
        }
        
        @Override
        public int compare(double[] testa, double[] testb) {
            
            if(testa[dimensionIndex]>testb[dimensionIndex])
                return 1;
            else if(testa[dimensionIndex]<testb[dimensionIndex])
                return -1;
            else 
                return 0;
        }
    }
}