/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MachineLearning.Kernel;


/**
 *
 * @author Ali
 */
public class Kernel_Linear extends AbstractKernelM  {
    
    public Kernel_Linear(){

    }
    
    @Override
    public double getK(Object objA, Object objB){
        if(!(objA instanceof double[] && objB instanceof double[])){
            throw new IllegalArgumentException("The input object to getK in Kernel_Linear is not a double[] instance");
        }
        return this.dot((double[])objA, (double[])objB);
    }
    
    double dot(double[] x, double[] y) {
        double sum = 0;
        int len = x.length;
        for(int i=0;i<len;i++){
            sum+=(x[i]*y[i]);
        }
        
        return sum;
    }
    
    @Override
    public String getName(){
        return "Linear";
    }
}
