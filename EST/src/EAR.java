package SoftwareTesting;

import MyPackage.*;


//eAR: evolutionary adaptive random testing, Andrew
//translated to java by Ali Shahbazi

import java.util.Random;

class gIndividual {
    double[] value;
    double fitness;
    boolean changed;

    public gIndividual(int dimension){
        this.value=new double[dimension];
    }
}

public class EAR {//for every dimention

    private Random rand;

    private void NextPoint(ArrayDouble2D seeds, int n){ //n: number of filled points

        int i,j;
        int P = 20;		// Population Size
	int G = 100;	// # of Generations

	//int k = 2;	// Tournament Selection Criterion

	double pMut = 0.1;	// Probability of Mutation
	double pCross = 0.6; // Probability of crossover
	double e = 0.01;	// size of each mutation

        int Dim=seeds.GetDimension();
        
        gIndividual[] pop=new gIndividual[P];
        for(i=0;i<P;i++)
            pop[i]=new gIndividual(Dim);

        gIndividual[] newPop=new gIndividual[P];
        for(i=0;i<P;i++)
            newPop[i]=new gIndividual(Dim);

        //Random rand=new Random();

        // Initialize & evaluate Pop
        for (i=0;i<P;i++) {
            for(j=0;j<Dim;j++)
                pop[i].value[j]=rand.nextDouble();


            double dist;
            double min = 2.0;

            for(int k=seeds.GetStartIndex();k<(n+seeds.GetStartIndex());k++) {
                dist = this.Distance(pop[i].value, seeds.Arr[k]);
                if (dist < min)
                    min=dist;
            }
            pop[i].fitness = min;
            pop[i].changed = false;
        }


        // Loop until termination //loop for all generation, stoping criteria: 100 Generation
	for(i=0;i<G;i++) {

            // Generate new individuals
            for (j=0;j<P;j++) {

                //find two random parent in population
                int parent1,parent2;

                int a,b;
                a = (int)( P * rand.nextDouble());
                if(a>=P)    a=P-1;
                b = (int)( P * rand.nextDouble());
                if(b>=P)    b=P-1;

                if(pop[a].fitness>pop[b].fitness)   parent1=a;
                else                                parent1=b;

                a = (int)( P * rand.nextDouble());
                if(a>=P)    a=P-1;
                b = (int)( P * rand.nextDouble());
                if(b>=P)    b=P-1;

                if(pop[a].fitness>pop[b].fitness)   parent2=a;
                else                                parent2=b;
                

                // Crossover
                if(rand.nextDouble()<pCross) {
                    newPop[j].value[0] = pop[parent1].value[0];/////////////////////////////////////////////////////////////////////////////////
                    newPop[j].value[1] = pop[parent2].value[1];
                    if(pop[parent1].value[0]!=pop[parent2].value[0] || pop[parent1].value[1]!=pop[parent2].value[1])
                        newPop[j].changed = true;
                }
                else {
                    System.arraycopy(pop[parent1].value, 0, newPop[j].value, 0, Dim);
                    newPop[j].fitness = pop[parent1].fitness;
                    newPop[j].changed = pop[parent1].changed;
                }

                // Mutate
                for(int d=0;d<Dim;d++){
                    if(rand.nextDouble()< pMut){
                        if(rand.nextDouble()<0.5) {/////////////////////??????????????????????
                            if (newPop[j].value[d]-e >= 0) {
                                newPop[j].value[d]-=e;
                                newPop[j].changed=true;
                            }
                            else if (newPop[j].value[d]+e <= 1) {
                                newPop[j].value[d]+=e;
                                newPop[j].changed=true;
                            }
                        }
                    }
                }//end for

               
                // Evaluate individuals
                if (newPop[j].changed) {
                
                    double dist;
                    double min = 2.0;

                    for(int k=seeds.GetStartIndex();k<(n+seeds.GetStartIndex());k++) {
                        dist=this.Distance(newPop[j].value, seeds.Arr[k]);
                        if (dist < min)
                            min=dist;
                    }
                    newPop[j].fitness = min;
                    newPop[j].changed = false;

                }

            }//end for j,P

            // Switch Populations
            gIndividual[] ptr;

            ptr =  pop;
            pop = newPop;
            newPop = ptr;

        }//end for i,G


        // Output best individual
        int maxIndex=0;
        double max = 0;

        for (i=0;i<P;i++) {
            if (pop[i].fitness >= max) {
                max = pop[i].fitness;
                maxIndex = i;
            }
        }


        //insert new point into array
        for(j=0;j<Dim;j++){
            seeds.Arr[n+seeds.GetStartIndex()][j]=pop[maxIndex].value[j];
        }

    }

    private double Distance(double[] x, double[] y){//distance in n dimension
        int dim=x.length;
        double dist=0;
        for(int i=0;i<dim;i++){
            dist+=((x[i]-y[i])*(x[i]-y[i]));
        }

        return (Math.sqrt(dist));

    }

    public void SetPoints(ArrayDouble2D data){//

        int i,j;
        int Dim=data.GetDimension();
        rand=new Random();

        //set first random point
        for(j=0;j<Dim;j++){
            data.Arr[data.GetStartIndex()][j]= rand.nextDouble();
        }

        //
        for(i=1;i<data.GetSize();i++)
            this.NextPoint(data, i);

    }

    

}
