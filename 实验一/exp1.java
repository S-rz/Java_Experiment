import java.util.NoSuchElementException;
import java.util.Scanner;
public class exp1{
	public static void main(String args[]) {
    	System.out.println("1.add	2.offer  3.remove");
    	System.out.println("4.poll	5.peek   6.element");
    	Queue<Integer> intQueue = new Queue<Integer>();
    	int flag = 1;
    	Scanner scan = new Scanner(System.in);
    	while(flag == 1) {
    		switch(scan.nextInt()) {
    			case 0:
    				flag = 0;
    				break;
    			case 1:
    				intQueue.add(scan.nextInt());
    				break;
    			case 2:
    				intQueue.offer(scan.nextInt());
    				break;
    			case 3:
    				System.out.println(intQueue.remove());
    				break;
    			case 4:
    				System.out.println(intQueue.poll());
    				break;
    			case 5:
    				System.out.println(intQueue.peek());
    				break;
    			case 6:
    				System.out.println(intQueue.element());
    				break;
    		}
    	}
    	scan.close();
    }
}
