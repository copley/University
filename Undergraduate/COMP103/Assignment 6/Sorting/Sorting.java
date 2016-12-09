// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP 103, Assignment 6
 * Name:
 * Usercode:
 * ID:
 */

//import ecs100.*;
import java.util.*;
import java.io.*;
import javax.swing.JOptionPane;

/** Code for Sorting Experiment
 *   - testing code
 *   - sorting algorithms
 *   - utility methods for creating, testing, printing, copying arrays
 */

public class Sorting{

    /* Example method for testing and timing sorting algorithms.
     *  You will need to modify and extend this heavily to do your
     *  performance testing. It should probably run tests on each of the algorithms,
     *  on different sized arrays, and multiple times on each size.
     *  Make sure you create a new array each time you sort - it is not a good test if
     *  you resort the same array after it has been sorted.
     *  Hint: if you want to copy an array, use copyArray (below)
     */
    public void testSorts(){
        String[] data;
        int initialSize = 100;
        int size = initialSize;
        int itterations = 1;
        int limit = 1000000;
        int increaseFactor = 10;

        //data = createArray(size);
        //timSort(data);
        //System.out.println(testSorted(data));

        //boolean moderfied = UI.askBoolean("Use moderfied sorts?: ");
        boolean moderfied = true;

        System.out.println("Random Lists");
        System.out.println(String.format("%-15s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |", "", "Selection Sort", "Insersion Sort", "Merge Sort", "Quick Sort" , "Quick Sort2", "Arrays Sort"));
        for( int i = 0; i < (7 * 15); i++ ) System.out.print("-");
        System.out.println();

        while( size <= limit )
        {
            float[] times = moderfied ? runModerfiedSorts(size, itterations, false, false) : runSorts(size, itterations, false, false);

            System.out.println(String.format("%-15s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |", String.format("%d", size), String.format("%.3f", times[0]), String.format("%.3f", times[1]), String.format("%.3f", times[2]), String.format("%.3f", times[3]), String.format("%.3f", times[4]), String.format("%.3f", times[5])));
            for( int i = 0; i < (7 * 15); i++ ) System.out.print("-");
            System.out.println();

            size = size * increaseFactor;
        }
        System.out.println(String.format("NOTE: Times averaged over %d itterations\n", itterations));

        size = initialSize;

        System.out.println("Sorted Lists");
        System.out.println(String.format("%-15s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |", "", "Selection Sort", "Insersion Sort", "Merge Sort", "Quick Sort" , "Quick Sort2", "Arrays Sort"));
        for( int i = 0; i < (7 * 15); i++ ) System.out.print("-");
        System.out.println();

        while( size <= limit )
        {
            //float[] times = runSorts(size, itterations, true, false);
            float[] times = moderfied ? runModerfiedSorts(size, itterations, true, false) : runSorts(size, itterations, true, false);

            System.out.println(String.format("%-15s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |", String.format("%d", size), String.format("%.3f", times[0]), String.format("%.3f", times[1]), String.format("%.3f", times[2]), String.format("%.3f", times[3]), String.format("%.3f", times[4]), String.format("%.3f", times[5])));
            for( int i = 0; i < (7 * 15); i++ ) System.out.print("-");
            System.out.println();

            size = size * increaseFactor;
        }
        System.out.println(String.format("NOTE: Times averaged over %d itterations\n", itterations));

        size = initialSize;

        System.out.println("Reverse Sorted Lists");
        System.out.println(String.format("%-15s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |", "", "Selection Sort", "Insersion Sort", "Merge Sort", "Quick Sort" , "Quick Sort2", "Arrays Sort"));
        for( int i = 0; i < (7 * 15); i++ ) System.out.print("-");
        System.out.println();

        while( size <= limit )
        {
            //float[] times = runSorts(size, itterations, true, true);
            float[] times = moderfied ? runModerfiedSorts(size, itterations, false, false) : runSorts(size, itterations, true, true);

            System.out.println(String.format("%-15s | %-15s | %-15s | %-15s | %-15s | %-15s | %-15s |", String.format("%d", size), String.format("%.3f", times[0]), String.format("%.3f", times[1]), String.format("%.3f", times[2]), String.format("%.3f", times[3]), String.format("%.3f", times[4]), String.format("%.3f", times[5])));
            for( int i = 0; i < (7 * 15); i++ ) System.out.print("-");
            System.out.println();

            size = size * increaseFactor;
        }
        System.out.println(String.format("NOTE: Times averaged over %d itterations\n", itterations));

    }

    public float[] runSorts(int size, int itter, boolean sorted, boolean reversed)
    {
        long start;
        long time;
        String[] data = null;
        String[] tmp = null;

        float[] aveTimes = new float[6];

        //data = createArray(size);
        //if( sorted ) mergeSort( data );
        //if( reversed ) reverseArray( data );

        for( int i = 0; i < itter; i++ )
        {
            //System.out.println("\n\n======Selection Sort=======\n");

            // Selection Sort
            if ( size <= 100000 )
            {
                data = createArray(size);
                if( sorted ) mergeSort( data );
                if( reversed ) reverseArray( data );

                start = System.currentTimeMillis();
                selectionSort(data);
                aveTimes[0] +=  (System.currentTimeMillis() - start)/1000.0;

                if( data != null && !testSorted(data) ) System.out.println("ERROR: Selection Sort Not Sorting Propperly !");
            }
            else aveTimes[0] += 60;

            // Insersioin Sort
           	if( size <= 100000 )
			{	
            	data = createArray(size);
            	if( sorted ) mergeSort( data );
            	if( reversed ) reverseArray( data );

            	start = System.currentTimeMillis();
            	insertionSort(data);
            	aveTimes[1] +=  (System.currentTimeMillis() - start)/1000.0;

            	if( data != null && !testSorted(data) ) System.out.println("ERROR: Insertion Sort Not Sorting Propperly !");
			}
			else aveTimes[1] += 60;

            // Merge Sort
            data = createArray(size);
            if( sorted ) mergeSort( data );
            if( reversed ) reverseArray( data );

            start = System.currentTimeMillis();
            mergeSort(data);
            aveTimes[2] +=  (System.currentTimeMillis() - start)/1000.0;

            if( data != null && !testSorted(data) ) System.out.println("ERROR: Merge Sort Not Sorting Propperly !");

            // Quick Sort
            data = createArray(size);
            if( sorted ) mergeSort( data );
            if( reversed ) reverseArray( data );

            start = System.currentTimeMillis();
            quickSort(data);
            aveTimes[3] +=  (System.currentTimeMillis() - start)/1000.0;

            if( data != null && !testSorted(data) ) System.out.println("ERROR: Quick Sort Not Sorting Propperly !");

            // Quick Sort 2
            data = createArray(size);
            if( sorted ) mergeSort( data );
            if( reversed ) reverseArray( data );

            start = System.currentTimeMillis();
            quickSort2(data);
            aveTimes[4] +=  (System.currentTimeMillis() - start)/1000.0;

            if( data != null && !testSorted(data) ) System.out.println("ERROR: Quick Sort 2 Not Sorting Propperly !");

            // Arrays Sort
            data = createArray(size);
            if( sorted ) mergeSort( data );
            if( reversed ) reverseArray( data );

            start = System.currentTimeMillis();
            Arrays.sort(data);
            aveTimes[5] += (System.currentTimeMillis() - start)/1000.0;
        }

        // Average the times 
        for( int i = 0; i < aveTimes.length; i++ ) aveTimes[i] = aveTimes[i] / itter;
        return aveTimes;
    }

    public float[] runModerfiedSorts(int size, int itter, boolean sorted, boolean reversed)
    {
        long start;
        long time;
        String[] data = null;
        String[] tmp = null;

        float[] aveTimes = new float[6];

        //data = createArray(size);
        //if( sorted ) mergeSort( data );
        //if( reversed ) reverseArray( data );

        for( int i = 0; i < itter; i++ )
        {
            //System.out.println("\n\n======Selection Sort=======\n");

            // Selection Sort
            if ( size <= 100000 )
            {
                data = createArray(size);
                if( sorted ) mergeSort( data );
                if( reversed ) reverseArray( data );

                start = System.currentTimeMillis();
                mSelectionSort(data);
                aveTimes[0] +=  (System.currentTimeMillis() - start)/1000.0;

                if( data != null && !testSorted(data) ) System.out.println("ERROR: Selection Sort Not Sorting Propperly !");
            }
            else aveTimes[0] += 60;

			// Insersion Sort

            data = createArray(size);
            if( sorted ) mergeSort( data );
            if( reversed ) reverseArray( data );

            start = System.currentTimeMillis();
            mInsertionSort(data);
            aveTimes[1] +=  (System.currentTimeMillis() - start)/1000.0;

            if( data != null && !testSorted(data) ) System.out.println("ERROR: Insertion Sort Not Sorting Propperly !");
            

            // Merge Sort
            data = createArray(size);
            if( sorted ) mergeSort( data );
            if( reversed ) reverseArray( data );

            start = System.currentTimeMillis();
            mMergeSort(data);
            aveTimes[2] +=  (System.currentTimeMillis() - start)/1000.0;

            if( data != null && !testSorted(data) ) System.out.println("ERROR: Merge Sort Not Sorting Propperly !");

            // Quick Sort
            data = createArray(size);
            if( sorted ) mergeSort( data );
            if( reversed ) reverseArray( data );

            start = System.currentTimeMillis();
            mQuickSort(data);
            aveTimes[3] +=  (System.currentTimeMillis() - start)/1000.0;

            if( data != null && !testSorted(data) ) System.out.println("ERROR: Quick Sort Not Sorting Propperly !");

            // Quick Sort 2
            aveTimes[4] += 0;

            // Arrays Sort
            aveTimes[5] += 0;
        }

        // Average the times 
        for( int i = 0; i < aveTimes.length; i++ ) aveTimes[i] = aveTimes[i] / itter;
        return aveTimes;
    }

    /* =============== SWAP ================= */
    /** Swaps the specified elements of an array.
     *  Used in several of the sorting algorithms
     */
    private  void swap(String[ ] data, int index1, int index2){
        if (index1==index2) return;
        String temp = data[index1];
        data[index1] = data[index2];
        data[index2] = temp;
    }

    /* ===============SELECTION SORT================= */

    /** Sorts the elements of an array of String using selection sort */
    public  void selectionSort(String[ ] data){
        // for each position, from 0 up, find the next smallest item 
        // and swap it into place
        for (int place=0; place<data.length-1; place++){
            int minIndex = place;
            for (int sweep=place+1; sweep<data.length; sweep++){
                if (data[sweep].compareTo(data[minIndex]) < 0)
                    minIndex=sweep;
            }
            swap(data, place, minIndex);
        }
    }

    public  void mSelectionSort(String[ ] data){
        // for each position, from 0 up, find the next smallest item 
        // and swap it into place
        for (int place=0; place<data.length-1; place++)
        {
            int minIndex = place;
            int maxIndex = data.length-1-place;
            for (int sweep=place+1; sweep<data.length; sweep++)
            {
                if (data[sweep].compareTo(data[minIndex]) < 0)
                    minIndex=sweep;

                if( data[sweep].compareTo(data[maxIndex]) > 0 && sweep < data.length-1-place )
                    maxIndex = sweep;
            }
            swap(data, place, minIndex);
            swap(data, data.length-1-place, maxIndex);
        }
    }

    /* ===============INSERTION SORT================= */
    /** Sorts the  elements of an array of String using insertion sort */
    public  void insertionSort(String[] data){
        // for each item, from 0, insert into place in the sorted region (0..i-1)
        for (int i=1; i<data.length; i++){
            String item = data[i];
            int place = i;
            while (place > 0  &&  item.compareTo(data[place-1]) < 0){
                data[place] = data[place-1];       // move up
                place--;
            }
            data[place]= item;
        }
    }

    public  void insertionSort(String[] data, int low, int high){
        // for each item, from 0, insert into place in the sorted region (0..i-1)
        for (int i=low; i<data.length && i < high; i++){
            String item = data[i];
            int place = i;
            while (place > 0  &&  item.compareTo(data[place-1]) < 0){
                data[place] = data[place-1];       // move up
                place--;
            }
            data[place]= item;
        }
    }

    public  void mInsertionSort(String[] data){
        // for each item, from 0, insert into place in the sorted region (0..i-1)
        for (int i=1; i<data.length; i++){
            String item = data[i];
            int p = binarySeach(data, item, i);

            // Move everything up
            for(int j = i; j > p; j--) 
            {
                data[j] = data[j-1];
            }

            data[p]= item;
        }
    } 

    public  void mInsertionSort(String[] data, int low, int high){
        // for each item, from 0, insert into place in the sorted region (0..i-1)
        for (int i=1+low; i<data.length && i < high; i++){
            String item = data[i];
            int p = binarySeach(data, item, low, i);

            // Move everything up
            for(int j = i; j > p; j--) 
            {
                data[j] = data[j-1];
            }

            data[p]= item;
        }
    } 

    public int binarySeach(String[] data, String item, int end)
    {
        int low = 0;
        int high = end;

        while(low != high)
        {
            int mid = (low + high)/2;

            int c = item.compareTo(data[mid]);
            if( c < 0 ) high = mid;
            else if( c > 0 ) low = mid + 1;
            else low = high = mid;
        }

        return low;
    }

    /* ===============MERGE SORT================= */
    /** non-recursive, wrapper method
     *  copy data array into a temporary array 
     *  call recursive mergeSort method     
     */
    public  void mergeSort(String[] data) {
        String[] other = new String[data.length];
        for(int i=0; i<data.length; i++)
            other[i]=data[i];
        mergeSort(data, other, 0, data.length); //call to recursive merge sort method
    }

    /** Recursive mergeSort method */
    public void mergeSort(String[] data, String[] temp, int low, int high) {
        if(low < high-1) {
            int mid = ( low + high ) / 2;
            mergeSort(temp, data, low, mid);
            mergeSort(temp, data, mid, high);
            merge(temp, data, low, mid, high);
        }
    }

    /** Merge method
     *  Merge from[low..mid-1] with from[mid..high-1] into to[low..high-1]
     *  Print data array after merge using printData
     */
    public void merge(String[] from, String[] to, int low, int mid, int high) {
        int index = low;      //where we will put the item into "to"
        int indxLeft = low;   //index into the lower half of the "from" range
        int indxRight = mid; // index into the upper half of the "from" range
        while (indxLeft<mid && indxRight < high) {
            if ( from[indxLeft].compareTo(from[indxRight]) <=0 )
                to[index++] = from[indxLeft++];
            else
                to[index++] = from[indxRight++];
        }
        // copy over the remainder. Note only one loop will do anything.
        while (indxLeft<mid)
            to[index++] = from[indxLeft++];
        while (indxRight<high)
            to[index++] = from[indxRight++];
    }

    public void mMergeSort(String[] data)
    {
        String[] other = new String[data.length];
        String[] current = data;

        for(int range = 1; ; range *= 2)
        {
            for( int start = 0; start < data.length; start = start+2*range)
            {
                int upper = start+2*range > data.length ? data.length : start+2*range;
                int mid = start+range < data.length-1 ? start+range : data.length-1;

                merge(current, other, start, mid, upper);

            }
            
            String[] temp = other;
            other = current;
            current = temp;
            
            if( range > data.length/2 ) break;
        }

        if( data != current )
        {
            for( int i = 0; i < data.length; i++ )
            {
                data[i] = current[i];
            }
        }

    }

    /*===============QUICK SORT=================*/
    /** Sort data using QuickSort
     *  Print time taken by Quick sort
     *  Print number of times partition gets called
     */

    /** Quick sort recursive call */
    public  void quickSort(String[ ] data) {
        quickSort(data, 0, data.length);
    }

    public  void quickSort(String[ ] data, int low, int high) {
        if (high-low < 2)      // only one item to sort.
            return;
        else {     // split into two parts, mid = index of boundary
            int mid = partition(data, low, high);
            quickSort(data, low, mid);
            quickSort(data, mid, high);
        }
    }

    public  void mQuickSort(String[ ] data) {
        mQuickSort(data, 0, data.length);
    }

    public void mQuickSort(String[ ] data, int low, int high) 
    {
        if (high-low <= 5)      // only one item to sort.
        {
            insertionSort(data, low, high);
            return;
        }
        else {     // split into two parts, mid = index of boundary
            int mid = mPartition(data, low, high);
            quickSort(data, low, mid);
            quickSort(data, mid, high);
        }
    }

    private int mPartition(String[] data, int low, int high) {
        String pivot = getMedian(data, low, high);
        int left = low-1;
        int right = high;
        while( left < right ) {
            do { 
                left++;       // just skip over items on the left < pivot
            } 
            while (left<high && data[left].compareTo(pivot) < 0);

            do { 
                right--;     // just skip over items on the right > pivot
            } 
            while (right>=low &&data[right].compareTo(pivot) > 0);

            if (left < right) 
                swap(data, left, right);
        }
        return left;
    }

    public String getMedian(String[] data, int low, int high)
    {
        if( high >= data.length ) high = data.length-1;
        String[] points = new String[3];

        points[0] = data[low];
        points[1] = data[(low+high)/2];
        points[2] = data[high];

        Arrays.sort(points);

        return points[1];
        //return binarySeach(data, points[1], low, high);
    }

    public int binarySeach(String[] data, String item, int start, int end)
    {
        int low = start;
        int high = end;

        while(low != high)
        {
            int mid = (low + high)/2;

            //int comp = item.compareTo(data[mid]);
            int c = item.compareTo(data[mid]);
            if( c < 0 ) high = mid;
            else if( c > 0 ) low = mid + 1;
            else low = high = mid;
        }

        return low;
    }

    /** Partition into small items (low..mid-1) and large items (mid..high-1) 
     *  Print data array after partition
     */
    private int partition(String[] data, int low, int high) {
        String pivot = data[(low+high)/2];
        int left = low-1;
        int right = high;
        while( left < right ) {
            do { 
                left++;       // just skip over items on the left < pivot
            } 
            while (left<high && data[left].compareTo(pivot) < 0);

            do { 
                right--;     // just skip over items on the right > pivot
            } 
            while (right>=low &&data[right].compareTo(pivot) > 0);

            if (left < right) 
                swap(data, left, right);
        }
        return left;
    }

    /** Quick sort, second version:  simpler partition method
     *   faster or slower?  */
    public  void quickSort2(String[ ] data) {
        quickSort2(data, 0, data.length);
    }

    public  void quickSort2(String[ ] data, int low, int high) {
        if (low+1 >= high) // no items to sort.
            return;
        else {     // split into two parts, mid = index of pivot
            int mid = partition2(data, low, high);
            quickSort2(data, low, mid);
            quickSort2(data, mid+1, high);
        }
    }

    public int partition2(String[] data, int low, int high){
        swap(data, low, (low+high)/2);    // choose pivot and put at position low
        String pivot = data[low];
        int mid = low;
        for(int i = low+1; i < high; i++){  // for each item after the pivot
            if ( data[i].compareTo(pivot) <0 ){
                mid++;                      // move mid point up
                swap(data, i, mid);
            }
        }
        swap(data, low, mid);   // move pivot to the mid point
        return mid;
    }

    /* ===== Tim Sort =============================================*/
    public void timSort(String[] data)
    {
        String[] other = new String[data.length];

        boolean desc = true;
        boolean asc = true;

        int MINRUN_SIZE = 8;//getMinrun(data.length);

        int start = 0;
        int end = 1;

        Stack<Integer[]> runs = new Stack<Integer[]>();

        while( end != data.length )
        {
            if( desc && data[end].compareTo(data[end+1]) <= 0 ) 
            {
                asc = false;
                end++;
            }
            else if( asc && data[end].compareTo(data[end+1]) > 0 )
            {
                desc = false;
                end++;
            }
            else 
            {
                // If the length of the current run is more or equal to the
                // minimum run size
                if( end - start >= MINRUN_SIZE ) 
                {
                    runs.push(new Integer[] {start, end});
                    start = end+1;
                    end += 2;
                }
                // Else the run is to short so extend it and use insersion sort
                // on it
                else
                {
                    end += MINRUN_SIZE - ( end - start );

                    runs.push(new Integer[] {start, end});
                    mInsertionSort(data, start, end);

                    start = end+1;
                    end += 2;

                }

                desc = true;
                asc = true;
            }
        }

        while( runs.size() >= 3 )
        {
            Integer[] X = runs.pop();
            Integer[] Y = runs.pop();
            Integer[] Z = runs.pop();

            int lenX = X[1] - X[0];
            int lenY = Y[1] - Y[0];
            int lenZ = Z[1] - Z[0];

            if( lenX + lenY > lenZ )
            {
                runs.push(new Integer[] {Y[0] < Z[0] ? Y[0] : Z[0], Y[1] > Z[1] ? Y[1] : Z[1]});
                timsort_merge(data, Y, Z);
                runs.push(X);
            }
            else if( lenX > lenY )
            {
                runs.push(Z);
                runs.push(new Integer[] {X[0] < Y[0] ? X[0] : Y[0], X[1] > Y[1] ? X[1] : Y[1]});
                timsort_merge(data, X, Y);
            }
            else break;
        }

        // Merge the runs
        /*for( int i = 0; i < runs.size(); i++ )
        {
        Integer[] run1 = runs.pop();
        Integer[] run2 = runs.pop();

        merge(data, other, run1[0], run1[1], run2[1]);

        //String[] tmp = other;
        //other = data;
        //data = tmp;
        }*/

    }

    public void timsort_merge(String[] data, Integer[] run1, Integer[] run2)
    {
        String[] sorted = new String[(run1[1] - run1[0]) + (run2[1] - run2[0])];

        int pointer1 = run1[0];
        int pointer2 = run2[0];

        for( int i = 0; i < sorted.length; i++ )
        {
            if( pointer1 >= run1[1]) 
            {
                sorted[i] = data[pointer2];
                pointer2++;
            }
            else if( pointer2 >= run2[1] )
            {
                sorted[i] = data[pointer1];
                pointer1++;
            }
            else if( data[pointer1].compareTo(data[pointer2]) < 0 )
            {
                sorted[i] = data[pointer1];
                pointer1++;
            }
            else 
            {
                sorted[i] = data[pointer2];
                pointer2++;
            }
        }

        System.out.println(testSorted(sorted));

        for( int i = 0; i < sorted.length; i++ )
        {
            data[run1[0]+i] = sorted[i];
        }
    }

    /* =====   Utility methods ============================================ */
    /** Tests whether an array is in sorted order
     */
    public boolean testSorted(String[] data) {
        for (int i=1; i<data.length; i++){
            if (data[i].compareTo(data[i-1]) < 0)
                return false;
        }
        return true;
    }

    public void printData(String[] data){
        for (String str : data){
            System.out.println(str);
        }
    }

    /** Constructs an array of Strings by making random String values */
    public String[] createArray(int size) {
        Random randGenerator = new Random();
        String[] data = new String[size];
        for (int i=0; i<size; i++){
            char[] chars = new char[5];
            for (int c=0; c<chars.length; c++){
                chars[c] = (char) ('a' + randGenerator.nextInt(26));
            }
            String str = new String(chars);
            data[i] = str;
        }
        return data;
    }

    /** Constructs an array of Strings by reading a file
     * The size of the array will be the specified size, unless the
     * file is too short, or size is -ve, in which cases the array will
     * contain all the tokens in the file. */
    public String[] readArrayFromFile(int size, String filename) {
        File file = new File(filename);
        if (!file.exists()){
            System.out.println("file "+filename+" does not exist");
            return null;
        }
        List<String> temp = new ArrayList<String> ();
        try {
            Scanner scan = new Scanner(new File(filename));
            while (scan.hasNext()) 
                temp.add(scan.next());
            scan.close();
        }
        catch(IOException ex) {   // what to do if there is an io error.
            System.out.println("File reading failed: " + ex);
        }
        if (temp.size() < size || size<0)
            size = temp.size();

        String[] data = new String[size];
        for (int i =0; i<size; i++){
            data[i] = temp.get(i);
        }
        return data;
    }

    /** Create a new copy of an array of data */
    public String[] copyArray(String[] data){
        String[] newData = new String[data.length];
        for (int i=0; i<data.length; i++){
            newData[i] = data[i];
        }
        return newData;
    }

    /** Create a new copy of the first size elements of an array of data */
    public String[] copyArray(String[] data, int size){
        if (size> data.length) size = data.length;
        String[] newData = new String[size];
        for (int i=0; i<size; i++){
            newData[i] = data[i];
        }
        return newData;
    }

    public void reverseArray(String[] data){
        int bot = 0;
        int top = data.length-1;
        while (bot<top){
            swap(data, bot++, top--);
        }
    }

    public static void main(String[] args){
        Sorting sorter = new Sorting();
        sorter.testSorts();
    }

}
