package gp.extra;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonPropertyTester {

	public static void main(String[] args) throws IOException{
		
		ObjectMapper OMAPPER = new ObjectMapper();
		
		bean1 b1 = new bean1();
		b1.p1 = "ttt-p1";
		b1.p2 = "ttt-p2";
		b1.p3 = 5;
		
		String jstr = OMAPPER.writeValueAsString(b1);
		
		System.out.println(jstr);
		
		bean1 b2 = OMAPPER.readValue(jstr, bean1.class);
		
		System.out.println("p1 : " + b2.p1);
		System.out.println("p2 : " + b2.p2);
		System.out.println("p3 : " + b2.p3);
	}
}
