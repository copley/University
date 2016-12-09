package LookAheadScanner;

import java.util.ArrayList;
import java.util.Scanner;

public class LookAheadScanner {
	private Scanner scanner;
	private ArrayList<String> lookahead;
	
	public LookAheadScanner(Scanner s) {
		scanner = s;
		
		lookahead = new ArrayList<String>();
		lookahead.add(s.next());
	}
	
	public boolean hasNext() {
		return lookahead.size() != 0;
	}
	
	public boolean hasNext(String pattern, int ahead) {
		if (lookahead.size() == 0 && !scanner.hasNext()) {
			return false;
		}
		
		while ((lookahead.size() <= ahead && scanner.hasNext())) {
			String next = scanner.next();
			lookahead.add(next);
		}
		
		return lookahead.size() < ahead ? false : lookahead.get(ahead).matches(pattern);
	}
	
	public boolean hasNext(String pattern) {
		return hasNext(pattern, 0);
	}
	
	public String next() {
		if (lookahead.size() == 1 && scanner.hasNext()) {
			lookahead.add(scanner.next());
		}
		
		return lookahead.remove(0);
	}
}
