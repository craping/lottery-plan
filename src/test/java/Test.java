import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Test {

	public static void main(String[] args) {
		readFileByLines("C:/Users/62632/Desktop/new.txt");
	}

	public static void readFileByLines(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				String[] strarr = tempString.split("_");
				String str = "";
				for (String s : strarr) {
					str += s.substring(0, 1).toUpperCase() + s.substring(1);
				}
				String xml = "<table tableName='" + tempString + "' domainObjectName='" + str + "' "
						+ " enableCountByExample='false' enableSelectByExample='false' "
						+ " enableUpdateByExample='false' enableDeleteByExample='false'>" + "</table>";
				System.out.println(xml);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
}
