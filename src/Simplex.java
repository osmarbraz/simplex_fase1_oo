
/*************************************************************************
 * Considera somente problemas que possam ser resolvidos com a fase 1 
 * do Algoritmo Simplex.
 * 
 * Dado uma matriz A m por n , um vetor B de tamanho M e um
 * vetor c de tamanho n, resolve um problema de programação linear
 * { max cx : Ax <= b, x >= 0 }. Assume que b >= 0 e que x = 0 é a solução
 * básica factivel.
 *
 * Cria um tableau simplex (M+1)por (N+M+1) com a coluna RHS M+N, a função
 * objetivo na linha M, e as variáveis de folga nas colunas M a M+N+1
 * 
 *************************************************************************/

public class Simplex {

    private double[][] a;   // tableau
    private int M;          // número de restrições
    private int N;          // número de variáveis base
    private int iteracoes ; // número de iterações
    private int[] base;     // base[i] = variável básica correspondente a linha i
                            // Necessário somente para imprimir a solução.
      
    /**
     * Monta tableau original.
     * 
     * @param A Matriz das restrições.
     * @param b Vetor das constantes de A.
     * @param c Coeficientes da função objetivo.
     */    
    public void montaTableau(double[][] A, double[] b, double[] c){
        //Quantidade de variáveis não básicas ou seja a quantidade de restrições do problema
        M = b.length;
        //Quantidade de variáveis não básicas
        N = c.length;
        //Uma linha e coluna adicional para função objetivo
        a = new double[M + 1][N + M + 1];
        //Copia as restrições para a matriz A para a
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                a[i][j] = A[i][j];
            }
        }
        //Adiciona 1 para a diagonal principal das variaveis básicas
        //Considera a partir de N+1 onde N é a quantidade de variáveis 
        //não básicas até M que é a quantidade de variáveis básicas
        for (int i = 0; i < M; i++) {
            a[i][N + i] = 1.0;
        }
        //Adiciona a constante da função objetivo para a linha M
        for (int j = 0; j < N; j++) {
            //Inverte o sinal do coeficiente c
            a[M][j] = -c[j];
        }
        
        //Adiciona constante das restrições a coluna M+N
        for (int i = 0; i < M; i++) {
            a[i][M + N] = b[i];
        }

        //Vetor auxiliar para imprimir a solução das variáveis básicas
        base = new int[M];
        for (int i = 0; i < M; i++) {
            base[i] = N + i;            
        }
    }
       
    /** Roda o algoritmo simplex apartir de uma solução inicial BFS.
     * 
     * @param A Matriz das restrições.
     * @param b Vetor das constantes.
     * @param c Coeficientes da função objetivo.
     */
    public void solver(double[][] A, double[] b, double[] c) {
        //Passo 1 - Monta o Tableau
        montaTableau(A,b,c);
        
        //Conta as iterações
        iteracoes = 0;
        // Passo 2 - Verifica condições de otimalidade
        while (testarOtimalidade(c)==true) {
            
            //Passo 2.1 Regra de Entrada 
            //encontra a coluna que deve entrar na base
            int q = encontraVariavelEntrada();
            
            //Passo 2.2 Regra de Saída
            // encontra a linha que deve sair considerando a variável de entrada
            int p = encontraVariavelSaida(q);
            
            //Passo 3 Recalcula a base
            recalculaBase(p, q);
            
            // atualiza a base guardando a variável que entrou na base.
            if (p!=-1){
                base[p] = q;
            }
            
            //Incrementa o contador de iterações
            iteracoes = iteracoes + 1;            
        }                     
    }
      
    /**
     * Indice de uma coluna não básica com o maior custo.
     * 
     * @return a coluna da variável de entrada ou -1.
     */    
    private int encontraVariavelEntrada() {
        double maior = 0;
        // ótimo
        int colunaMaior = -1;
        //Procura somente nas variáveis não base
        for (int j = 0; j < M; j++) {
            //Maior maior absoluto
            if (Math.abs(a[M][j]) > maior) {
                maior =  Math.abs(a[M][j]);
                colunaMaior = j;
            }
        }        
        return colunaMaior;  
    }

  
    /**
     * Encontra linha p usando a regra da menor razão.(-1 se não houver a linha).
     * 
     * @param k coluna do pivô.
     * @return A linha da variável de saída ou -1.
     */      
    private int encontraVariavelSaida(int k) {
        double menor = 0;
        int linhaMenor = -1;        
        // Posição do pivo deve ser diferente de -1
        if (k != -1){
            for (int i = 0; i < M; i++) {
                //aik deve ser positivo
                if (a[i][k] > 0) {
                    //Usa o primeiro elemento maior que 0 como menor para inicializar p
                    if (linhaMenor==-1){
                        linhaMenor = i;
                        menor = a[i][M + N] / a[i][k];
                    } else {
                        if ((a[i][M + N] / a[i][k]) < menor) {
                            linhaMenor = i;
                            menor =  a[i][M + N] / a[i][k];
                        }
                    }               
                } 
            }
        }
        return linhaMenor;
    }

    /**
     * Calcula o pivot com a entrada(p, q) usando o método de eliminação de Gauss-Jordan.
     * 
     * @param p Linha do pivô.
     * @param q Coluna do pivô.
     */
    private void recalculaBase(int p, int q) {
        //Calcula somente se existir um pivô        
        // p e q devem ser diferente de -1
        if ((p!=-1) && (q != -1)){    
            
            //Calcula a linha do pivo
            //Guarda o valor do pivo
            double valorPivo = a[p][q];
            //Percorre a coluna
            for (int j = 0; j <= M + N; j++) {
                //Recalcula o elemento da linha a[p][j]
                a[p][j] = a[p][j]/valorPivo;
            }
            
            // Recalcula as outras linhas da matriz a menos a linha p            
            //Percorre as linhas
            for (int i = 0; i <= M; i++) {
                //Menos para linha do pivo
                if (i != p) {      
                    //Guarda o coeficiente da coluna do pivô
                    double coeficienteColunaPivo = a[i][q];
                    //Percorre a coluna
                    for (int j = 0; j <= M + N; j++) {
                        //Recalcula o elemento
                        a[i][j] = a[i][j] - (coeficienteColunaPivo * a[p][j]);
                    }
                }
            }
        }
    }

    /**
     * Retorna o valor ótimo do objetivo.
     * 
     * @return O valor ótima do objetivo.
     */
    public double getValor() {
        //Última linha e coluna da matriz a
        return a[M][M + N];
    }

     /**
     * Testa a otimalidade verificando se é a solução básica fáctivel.
     * 
     * Procura algum valor negativo em c(linha M).
     * 
     * Como os coeficientes de x1 e x2  são negativos na linha M, 
     *  a SBF(Solução Básica Factível) atual não é ótima, pois um 
     *  incremento positivo em x1 ou x2  resultará em SBF adjacente 
     *  melhor do que a SBF atual.
    */
    private boolean testarOtimalidade(double[] c) {
        int k = 0;
        boolean temNegativo = false;
        //Se existir um elemento negativo interrompe o laço
        while ((k < c.length) && (temNegativo==false)){
            // verifica se a[M][k] < 0
            // M é a última linha da matriz a
            if (a[M][k] < 0.0){
                temNegativo = true;                
            }
            k = k + 1;
        }        
        return temNegativo;
    }    
    
    /**
     * Mostra o tableau.
     */
    public void mostraTableau() {
        System.out.println("M = " + M + " e N = " + N);        
        //Cabeçalho do tableau
        System.out.printf("\t\t");
        for (int i = 0; i < M + N; i++) {
            System.out.printf("   x[%d]\t\t",i);
        }
        System.out.printf("      b");
        System.out.println();

        //Matriz a        
        for (int i = 0; i <= M; i++) {
            if (i>=M){
                System.out.printf("z =\t");
            } else {
                System.out.printf("x["+(N+i)+"] =\t");
            }
            for (int j = 0; j <= M + N; j++) {
                System.out.printf("\t%7.2f ", a[i][j]);
            }
            System.out.println();
        }
    
    }    
    
    /**
     * Retorna o vetor da solução primal.
     * 
     * @return um vetor com a solução do problema.
     */
    public double[] getPrimal() {
        double[] x = new double[N];
        for (int i = 0; i < M; i++) {
            if (base[i] < N) {
                x[base[i]] = a[i][M + N];
            }
        }
        return x;
    }
   
    /**
     * Imprime a solução do problema.
     */
    public void imprimeSolucao(){
        //Função objetivo        
        System.out.println("A solução foi encontrada com " + getIteracoes() + " iterações");
        System.out.println("   z = " + getValor());
        //Variáveis
        double[] x = getPrimal();
        for (int i = 0; i < x.length; i++) {
            System.out.println("x[" + (i+1) + "] = " + x[i]);
        }                
    }

    /**
     * Retorna a quantidade de iterações.
     * 
     * @return Um inteiro com a quantidade de iterações
     */
    public int getIteracoes() {
        return iteracoes;
    }
}
