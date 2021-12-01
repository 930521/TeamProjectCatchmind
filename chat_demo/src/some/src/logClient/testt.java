package logClient;

public class testt {

	public static void main(String[] args) {
		String str = "12|345|432";
		String[] data = str.split("\\|");
		System.out.println(data[0]);
		System.out.println(data[1]);
		System.out.println(data[2]);
	}

}
