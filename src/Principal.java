/**
 * Executa os testes na classe Simplex.
 *  
 */
public class Principal {    

    /**
     * Executa o algoritmo solver.
     * 
     * @param A Matriz das restrições.
     * @param b Vetor das constantes de A.
     * @param c Coeficientes da função objetivo.
     */
    public static void teste(double[][] A, double[] b, double[] c) {
        // Instancia o Simplex        
        Simplex lp = new Simplex();        
        
        // Monta o tableau
        lp.montaTableau(A, b, c);
        
        System.out.println("\n>>>> Tableau Inicial");                
        lp.mostraTableau();
        
        // Executa o solver
        lp.solver(A, b, c);
        
        System.out.println("\n>>>> Tableau Final");                
        lp.mostraTableau();
        
        // Imprime a solução
        lp.imprimeSolucao();
    }

    /**
     * Caso 1
     * 
     * MAX 3X1 + 5X2
     * 
     * SUBJECT TO
     * 1X1 + 0X2 <=4
     * 0X1 + 2X2 <=12
     * 3X1 + 2X2 <=18
     * X1 >= 0
     * X2 >= 0
     * END
     * 
     *  3   5   0   0   0   0
     *  1   0   1   0   0   4
     *  0   2   0   1   0   12
     *  3   2   0   0   1   18
     */
    public static void testeCaso1() {
        // Matriz A das equações de restrições
        double[][] A = {
            {1, 0},
            {0, 2},
            {3, 2}
        };        
        // Coeficientes da função objetivo
        double[] c = {3, 5};
        // Constante das equações de A 
        double[] b = {4, 12, 18};
        // Executa o teste para o problema
        teste(A, b, c);
    }
    
    
    /**
     * Caso 2 
     * 
     * Max 1000x1 + 1800x2
     *
     * SUBJECT TO 
     * 20x1 + 30x2 <=1200 
     * x1 <=40 
     * x2 <=30
     * x1 >= 0 x2 >= 0 
     * END
     * 
     *  1000   1800   0    0    0      0
     *    20     30   1    0    0   1200
     *     1      0   0    1    0     40
     *     0      1   0    0    1     30
     */
    public static void testeCaso2() {
        // Matriz A das equações de restrições
        double[][] A = {
            {20, 30},
            {1, 0},
            {0, 1,}
        };        
        // Constante das equações de A 
        double[] b = {1200, 40, 30};
        // Coeficientes da função objetivo
        double[] c = {1000, 1800};
        // Executa o teste para o problema
        teste(A, b, c);
    }
   
    /**
     * Programa principal.
     * 
     * @param args 
     */
    public static void main(String[] args) {
        
        // Executa o teste do caso 1
        testeCaso1();        
        System.out.println("--------------------------------");
        
        // Executa o teste do caso 2
        testeCaso2();        
        System.out.println("--------------------------------");        
    }
}
