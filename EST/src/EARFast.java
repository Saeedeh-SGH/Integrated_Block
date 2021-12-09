package SoftwareTesting;

import MyPackage.*;


//Ali Shahbazi

import java.util.Random;

class gIndividualf {
    double[] value;
    double fitness;
    boolean changed;

    public gIndividualf(int dimension){
        this.value=new double[dimension];
    }
}

public class EARFast {//for every dimention

    private SpacialSearch ss=new SpacialSearch();
    private Random rand;

    private void NextPoint(ArrayDouble2D seeds){

        int i,j;
        int P = 20;		// Population Size
	int G = 100;	// # of Generations

	//int k = 2;	// Tournament Selection Criterion

	double pMut = 0.1;	// Probability of Mutation
	double pCross = 0.6; // Probability of crossover
	double e = 0.01;	// size of each mutation

        int Dim=seeds.GetDimension();
        
        gIndividualf[] pop=new gIndividualf[P];
        for(i=0;i<P;i++)
            pop[i]=new gIndividualf(Dim);

        gIndividualf[] newPop=new gIndividualf[P];
        for(i=0;i<P;i++)
            newPop[i]=new gIndividualf(Dim);

        

        // Initialize & evaluate Pop
        for (i=0;i<P;i++) {
            for(j=0;j<Dim;j++)
                pop[i].value[j]=rand.nextDouble();


            pop[i].fitness = ss.Find1NNDist(pop[i].value);
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
                
                    newPop[j].fitness = ss.Find1NNDist(newPop[j].value);
                    newPop[j].changed = false;

                }

            }//end for j,P

            // Switch Populations
            gIndividualf[] ptr;

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
            seeds.Arr[seeds.GetEndIndex()][j]=pop[maxIndex].value[j];
        }
        seeds.SetSize(seeds.GetSize()+1);

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
        int Size=data.GetSize();

        rand=new Random();

        //set first random point
        for(j=0;j<Dim;j++){
            data.Arr[data.GetStartIndex()][j]= rand.nextDouble();
        }
        data.SetSize(1);

        //preprocess
        ss.Preprocess(data,ss.CalcStepNum(Size/2, data.GetDimension()));

        //
        for(i=1;i<Size;i++){
            this.NextPoint(data);
            ss.Add();
        }
        

    }

}
