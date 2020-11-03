/*
package com.atuguigu.shortMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

public class Main02 {

	private BufferedReader read;

	String a = "(";
	String b = ")";
	String c = "{";
	String d = "}";
	String e = "[";
	String f = "]";

	HashMap<String, Integer> map = new HashMap<>();

	public boolean readFromConsole() throws IOException {
		read = new BufferedReader(new InputStreamReader(System.in));
		String readLine = read.readLine();
		for (int i = 0; i < readLine.length(); i++) {
			String key = readLine.charAt(i) + "";
			if (b.equals(key) || d.equals(key) || f.equals(key)) {
				if (!map.containsKey(key)) {
					return false;
				} else if (map.get(key) < 1) {
					return false;
				} else {
					map.put(key, map.get(key) - 1);
				}
			} else if (a.equals(key)) {
				if(map.containsKey(b)){
					map.put(b,map.get(b)+1);
				}else{
					map.put(b,1);
				}
			}else if(c.equals(key)){
				if(map.containsKey(d)){
					map.put(d,map.get(d)+1);
				}else{
					map.put(d,1);
				}

			}else if(e.equals(key)){
				if(map.containsKey(f)){
					map.put(f,map.get(f)+1);
				}else{
					map.put(f,1);
				}
			}
		}
		Set<String> keySet = map.keySet();
		System.out.println(keySet);
		for (String key : keySet) {
			System.out.println(key+"-->"+map.get(key));
			if (map.get(key) % 2 != 0) {
				return false;
			}
		}
		return true;
	}


	public static void main(String[] args) throws IOException {
		Main02 main = new Main02();
		boolean result = main.readFromConsole();
		System.out.println(result);

	}
}
*/
