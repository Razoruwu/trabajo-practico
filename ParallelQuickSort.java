package quicksort;

import java.util.Random;

//-----> LINK DEL VIDEO EXPLICATIVO: https://drive.google.com/file/d/12wu7SJow20Rc2_43qP3YjtM9WrjWsKoy/view?usp=drive_link

public class ParallelQuickSort extends Thread{
    // creamos un int con la cantidad de hilos disponibles
    private static int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    private int izquierda;
    private int derecha;
    private static int[] arr = new int[10];

public ParallelQuickSort(int izquierda, int derecha) {
  this.izquierda=izquierda;
  this.derecha=derecha;

}


//Esta clase contiene los métodos necesarios para realizar el ordenamiento QuickSort de forma paralela
    public void run() {

            if (izquierda < derecha) {
            	//sacamos el indice del pivote con el metodo particion. 
                int pivoteIndex = particion(arr, izquierda, derecha);
              
                try {
                	//implementamos un sleep para figurar un proceso mas complejo
                	sleep(3);
                }catch(Exception e ) {}
                //realizamos un quicksort de ambos lados del array
                    quickSort(arr, izquierda, pivoteIndex - 1);
                    quickSort(arr, pivoteIndex + 1, derecha);
            }

   }



    private static int particion(int[] arr, int izquierda, int derecha) {
        int pivote = arr[derecha];
        int i = izquierda - 1;
        for (int j = izquierda; j < derecha; j++) {
            //si el elemento actual es menor al pivote
            if (arr[j] < pivote) {
                //el puntero a la izquierda adelanta una posicion
                i++;
                //se realiza el swap
                //para asegurar que los elementos menores queden del lado izquierdo
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, derecha);
        return i + 1;
    }
    // Este método es una implementación recursiva de QuickSort.
    // Recibe el array y los índices
    // izquierda y derecha del subarray a ordenar.
    private static void quickSort(int[] arr, int izquierda, int derecha) {
        if (izquierda < derecha) {
            // Utiliza el método particion() para encontrar el índice del pivote
            int pivotIndex = particion(arr, izquierda, derecha);
            //luego recursivamente ordena los subarrays izquierdo y derecho.
            quickSort(arr, izquierda, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, derecha);
        }
    }

    private static void swap(int[] arr, int i, int j) {
        //un swap tradicional
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) throws InterruptedException {
    	//Este va a ser el programa comparativo entre un quicksort concurrente y uno secuencial.
    	
    	//Empezamos llenando un array de numeros random
        for ( int c = 0 ; c <arr.length; c++) {
            Random rand = new Random(System.nanoTime());
            arr[c] = rand.nextInt(99);
        }
        int n = arr.length;
        ///usamos hilos como procesadores logicos
        ParallelQuickSort [] hilos = new ParallelQuickSort[MAX_THREADS];
        //Al ser 10 numeros...
        int rango = 10/MAX_THREADS;
        int izquierda = 0 ;
        int derecha = rango;
        //empezamos a  tomar el tiempo del concurrente
        double arranco = System.nanoTime();
        
        for (int i = 0 ; i<MAX_THREADS; i++) {
            if (i == MAX_THREADS - 1) {
                derecha = n ;
            }
            hilos[i] = new ParallelQuickSort(izquierda, derecha - 1); // Crea el hilo con el rango actual
            hilos[i].start(); // Inicia el hilo

            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // calculamos el tiempo que tomo el algoritmo concurrente.
        double termino = System.nanoTime()-arranco;
        System.out.println("Array acomodado:");
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i] + " ");
        }

        System.out.println("Tardo "+ termino/100 + " milis el concurrente");
        double arranco2 = System.nanoTime();

        ordenacionrapida(arr, 0, n-1);
        //calculamops el tiempo que tomo el algoritmo secuencial.
        double termino2 = System.nanoTime()-arranco2;
        System.out.println("Array acomodado:");
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println("Tardo "+ termino2/100 + " milis el comun");
        //vemos el porcentaje de mejoria entre el secuancial al concurrente
        double porcentaje= termino2/termino;
        System.out.println("el porcentaje de mejoria es de un "+String.format("%.2f",porcentaje*100));
    }
    
    
//De aca en adelante el codigo es de https://tutospoo.jimdofree.com/tutoriales-java/m%C3%A9todos-de-ordenaci%C3%B3n/ordenaci%C3%B3n-r%C3%A1pida-quicksort/bbbbbbbb
public static void ordenacionrapida(int vec[], int inicio, int fin){
    if(inicio>=fin) return;
    
    int pivote=vec[inicio];
    int elemIzq=inicio+1;
    int elemDer=fin;
    
    while(elemIzq<=elemDer){
    	try {
    	sleep(3);
    	}catch(Exception e) {}
        //mientras el indice izquierdo no sea fin, y mientras su elemento sea menor al del pivote...
    	//Esta ultima aclaracion es para mantener los elementos menores del lado izquierdo
    	
        while(elemIzq<=fin && vec[elemIzq]<pivote){
            //aumenta una posicion nuestro indice
            elemIzq++;
        }
        while(elemDer>inicio && vec[elemDer]>=pivote){
            //disminuye una posicion nuestro final
            elemDer--;
        }
        
        if(elemIzq<elemDer){
            int temp=vec[elemIzq];
            vec[elemIzq]=vec[elemDer];
            vec[elemDer]=temp;
        }
    }

    if(elemDer>inicio){
        int temp=vec[inicio];
        vec[inicio]=vec[elemDer];
        vec[elemDer]=temp;
    }
    //llamamos recursivamente para ambos lados del vector
    ordenacionrapida(vec, inicio, elemDer-1);
    ordenacionrapida(vec, elemDer+1, fin);
}

}