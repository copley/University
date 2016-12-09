package FileBPlusTreeTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import FileBPlusTree.FileBPlusTree;
import FileBPlusTree.FileBPlusTree.Entry;
import FileBPlusTree.FileBPlusTree.ObjectByteParser;

public class FileBPlusTreeAdd {
	@Test
	public void testAdd_01() {
		try {
			ObjectByteParser<String, Integer> parser = new ObjectByteParser<String, Integer>(60, 4) {

				@Override
				protected byte[] convertKeyToBytes(String key) {
					byte[] bytes = new byte[numKeyBytes];
					
					byte[] stringBytes = key.getBytes();
					
					for (int i=0; i<stringBytes.length; i++){
					    bytes[i] = stringBytes[i];
					}
					
					return bytes;
				}

				@Override
				protected String convertBytesToKey(byte[] bytes) {
					String key = new String(bytes).replace("\0", "");
					
					if (!key.equals("")) {
						return key;
					}
					
					return null;
				}

				@Override
				protected byte[] convertValueToBytes(Integer val) {
					
					byte[] bytes = new byte[numValueBytes];
					
					bytes[0]  =  (byte) (val>>>24);
					bytes[1] = (byte) (val>>>16 & 0xff);
					bytes[2] = (byte) (val>>>8 & 0xff);
					bytes[3] = (byte) (val & 0xff);
					
					return bytes;
				}

				@Override
				protected Integer convertBytesToValue(byte[] bytes) {
					return (bytes[0]<<24) | ((bytes[1]&0xff)<<16) | ((bytes[2]&0xff)<<8) | (bytes[3] & 0xff);
				}
				
			};
			
			FileBPlusTree<String, Integer> tree = new FileBPlusTree<String, Integer>("test.bplus", 1024, parser);

			String[] alphabet = new String[] { "a", "b", "c", "d", "e", "f",
					"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
					"s", "t", "u", "v", "w", "x", "y", "z" };

			int index = 1;

			System.out.println("Putting");
			
			for (String s1 : alphabet) {
				for (String s2 : alphabet) {
					try {
						tree.put(s1 + s2, index);
					} catch (Exception e) {
						System.out.println("Failed at: " + index);
						System.out.println(e);
						fail();
					}
					index++;
				}
			}

			System.out.println("Getting");
			
			index = 1;

			for (String s1 : alphabet) {
				for (String s2 : alphabet) {
					int res = 0;
					try {
						res = tree.find(s1 + s2);
					} catch (Exception e) {
						System.out.println("Failed at: " + index);
						System.out.println(e);
						fail();
					}
					
					assertEquals(index, res);
					
					index++;
				}
			}
			
			System.out.println("Itterator");
			
			index = 1;
			
			for (Entry<String, Integer> e : tree) {
				assertEquals(index, e.val.intValue());
				
				index++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
