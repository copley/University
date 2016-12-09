package GUI.FileGUI;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

import FileBPlusTree.FileBPlusTree;
import FileBPlusTree.FileBPlusTree.Entry;
import FileBPlusTree.FileBPlusTree.ObjectByteParser;

public class FileDNSDB {

	private FileBPlusTree<Integer, String> hostNames;
	private FileBPlusTree<String, Integer> ipAddresses;

	public FileDNSDB() throws IOException {

		ObjectByteParser<String, Integer> StringIntegerParser = new ObjectByteParser<String, Integer>(
				60, 4) {

			@Override
			protected byte[] convertKeyToBytes(String key) {
				byte[] bytes = new byte[numKeyBytes];

				byte[] stringBytes = key.getBytes();

				for (int i = 0; i < stringBytes.length; i++) {
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

				bytes[0] = (byte) (val >>> 24);
				bytes[1] = (byte) (val >>> 16 & 0xff);
				bytes[2] = (byte) (val >>> 8 & 0xff);
				bytes[3] = (byte) (val & 0xff);

				return bytes;
			}

			@Override
			protected Integer convertBytesToValue(byte[] bytes) {
				return (bytes[0] << 24) | ((bytes[1] & 0xff) << 16)
						| ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff);
			}

		};

		ObjectByteParser<Integer, String> IntegerStringParser = new ObjectByteParser<Integer, String>(
				4, 60) {

			@Override
			protected byte[] convertKeyToBytes(Integer key) {
				byte[] bytes = new byte[numKeyBytes];

				bytes[0] = (byte) (key >>> 24);
				bytes[1] = (byte) (key >>> 16 & 0xff);
				bytes[2] = (byte) (key >>> 8 & 0xff);
				bytes[3] = (byte) (key & 0xff);

				return bytes;
			}

			@Override
			protected Integer convertBytesToKey(byte[] bytes) {
				Integer res = (bytes[0] << 24) | ((bytes[1] & 0xff) << 16)
						| ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff);
				
				return res == 0 ? null : res;
			}

			@Override
			protected byte[] convertValueToBytes(String val) {
				byte[] bytes = new byte[numValueBytes];

				if (val == null) {
					return bytes;
				}
				
				byte[] stringBytes = val.getBytes();

				for (int i = 0; i < stringBytes.length; i++) {
					bytes[i] = stringBytes[i];
				}

				return bytes;
			}

			@Override
			protected String convertBytesToValue(byte[] bytes) {
				String key = new String(bytes).replace("\0", "");

				if (!key.equals("")) {
					return key;
				}

				return null;
			}

		};

		hostNames = new FileBPlusTree<Integer, String>("hostnames.txt", 1024, IntegerStringParser);
		ipAddresses = new FileBPlusTree<String, Integer>("ipAddress.txt", 1024, StringIntegerParser);
	}

	/**
	 * Loads all the host-IP pairs into the B+ trees.
	 * 
	 * @param fileName
	 */
	public void load(File file) {
		if (!file.exists()) {
			System.out.println(file + " not found");
			return;
		}
		BufferedReader data;
		System.out.println("Loading....");
		try {
			int i = 0;
			boolean inserted = false;
			
			data = new BufferedReader(new FileReader(file));
			while (true) {
				
				// TODO: Run through test and see what changes so to find bug
				
				if (i == 56894) {
					System.out.println("BREAK");
				}
				
				String line = data.readLine();
				if (line == null) {
					break;
				}
				String[] pair = line.split("\t");
				String host = pair[0];
				int IP = stringToIP(pair[1]);
				
				if (IP == -1826957446) {
					inserted = true;
				}
				
				hostNames.put(IP, host);
				ipAddresses.put(host, IP);
				
				System.out.println(i);
				
				if (inserted && hostNames.find(-1826957446) == null) {
					System.out.println("Missing: " + i);
				}
				
				if (inserted && ipAddresses.find("00-25-64-97-13-a3.centennial.txstate.edu") == null) {
					System.out.println("Missing: " + i);
				}
				
				i++;
				
				//System.out.println(host + " : " + IP);
			}
		} catch (IOException e) {
			System.out.println("Fail: " + e);
		}
		System.out.println("Loading Done");
	}

	/**
	 * Finds an IP address given the host name.
	 * 
	 * @param hostName
	 * @return integer representation of an IP address, null if not found.
	 * @throws IOException 
	 */
	public Integer findIP(String hostName) throws IOException {
		return ipAddresses.find(hostName);
	}

	/**
	 * Finds the host name given the IP address.
	 * 
	 * @param ip
	 * @return null if not found
	 * @throws IOException 
	 */
	public String findHostName(int ip) throws IOException {
		return hostNames.find(ip);
	}

	/**
	 * Tests whether the given IP-name pair is valid.
	 * 
	 * @param ip
	 *            integer representation of an IP address
	 * @param hostName
	 * @return true if valid, false otherwise
	 * @throws IOException 
	 */
	public boolean testPair(int ip, String hostName) throws IOException {
		String host = findHostName(ip);
		if (host == null) {
			Integer foundIP = findIP(hostName);
			if (foundIP == null)
				return false;
			return ip == foundIP;
		} else {
			return host.equals(hostName);
		}
	}

	/**
	 * Tests whether the given name-IP pair is valid.
	 * 
	 * @param hostName
	 * @param ip
	 *            integer representation of an IP address
	 * @return true if valid, false otherwise
	 * @throws IOException 
	 */
	public boolean testPair(String hostName, int ip) throws IOException {
		Integer foundIP = findIP(hostName);
		if (foundIP == null) {
			String foundName = findHostName(ip);
			if (foundName == null)
				return false;
			return hostName.equals(foundName);
		} else {
			return ip == foundIP;
		}
	}

	/**
	 * Adds an host-IP pair to the database (ie, to both B+ trees)
	 * 
	 * @param hostName
	 * @param ip
	 * @return whether successfully added to the database
	 * @throws IOException 
	 */
	public boolean add(String hostName, int ip) throws IOException {
		//return ipAddresses.put(hostName, ip) && hostNames.put(ip, hostName);
		ipAddresses.put(hostName, ip);
		hostNames.put(ip, hostName);
		
		return true;
	}

	/**
	 * Prints (to System.out) all the pairs in the HostNames index.
	 */
	public void iterateAll() {
		// YOUR CODE HERE
		// You will need to add methods to the BPlusTree... classes.
		// JOptionPane.showMessageDialog(null,
		// "You must implement iterateAll()");
		int i = 0;
		for (Entry<String, Integer> e : ipAddresses) {
			System.out.println(e);
			i++;
		}
		
		System.out.println(i);
	}

	// FOR MARKING! PLEASE DO NOT MODIFY.
	/**
	 * Look up all the values in the file. If the value is not present, then it
	 * will print that value to System.out.
	 * @throws IOException 
	 */
	public void testAllPairs(File file) throws IOException {
		try {
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()) {
				String[] line = scan.nextLine().split("\t");
				if (line.length != 2)
					continue;
				int ip = stringToIP(line[1].trim());
				String host = line[0].trim();
				
				System.out.println("Testing: " + ip + " -> " + host);
				
				if (!testPair(ip, host)) {
					System.out.println("Missing: " + IPToString(ip) + " -> "
							+ host);
				}
				if (!testPair(host, ip)) {
					System.out.println("Missing: " + host + " -> "
							+ IPToString(ip));
				}
			}
			scan.close();

			System.out.println("Test Finished");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Utilities

	public static Integer stringToIP(String text) {
		String[] bytes = text.trim().split("\\.");

		if (bytes.length != 4)
			return null;

		try {
			int ip = 0;
			for (int i = 0; i < 4; i++) {
				int b = Integer.parseInt(bytes[i].trim());
				ip |= b << (24 - 8 * i);
			}

			return ip;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static String IPToString(int ip) {
		StringBuilder sb = new StringBuilder();
		for (int i = 3; i >= 0; i--) {
			// sb.append((ip & (0xFF << (i*8))) >> (i*8));
			sb.append(((ip >> (i * 8)) & 0xFF));
			if (i > 0)
				sb.append('.');
		}
		return sb.toString();
	}
}
