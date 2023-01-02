import java.io.*;
import java.util.*;

public class PasswordGenerator {

	private Website[] websites;
	private int num; // Number of web sites
	public Scanner input = new Scanner(System.in);

	public PasswordGenerator() {

		websites = new Website[100];
		num = 0;
	}

//  1. Generate password
	public String generatePassword() {

		return getAlphaNumericString();

	}

	// function to generate a random string
	private String getAlphaNumericString() {
		// chose a Character random from this String
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
		// create StringBuffer size of AlphaNumericString
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 15; i++) {
			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index = (int) (AlphaNumericString.length() * Math.random());
			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}

//    2. Create password
	public boolean createPassword(String username, String password, String website) { // EXCEPTION NEEDED
		try {
			File data = new File("Data.txt");
			PrintWriter writer = new PrintWriter(new FileOutputStream(data, true));

			while (searchName(username, website, true)) {
				System.out.println("This username already exists in this website!");

				System.out.print("Enter Username for the website: ");

				username = (String) input.nextLine();

				System.out.print("Enter Password: ");

				password = (String) input.nextLine();

				System.out.print("Enter Website: "); // .com

				website = (String) input.nextLine();

			}
			writer.append(username + "," + password + "," + website + "\n");
			writer.close();
			Data d = new Data(username, password);
			Website web = new Website(d, website);
			websites[num++] = web;

			return true;

		} catch (

		Exception e) {
			System.out.println("Something wrong happened. Please try again. Error: " + e);
		}

		return false;
	}

//    3. Display password

	public String display(String choice, String website) {
		String str = "";
		readToFile();
		try {
			if (num == 0) {
				return "There are no passwords!";
			}
			if (choice.equalsIgnoreCase("1")) { // Display all passwords
				for (int i = 0; i < num; i++)
					for (int j = 0; j < websites[i].numOfData; j++) {
						str += websites[i].accounts[j].username + " " + websites[i].accounts[j].password + " "
								+ websites[i].name + "\n";
					}
			} else if (choice.equalsIgnoreCase("2")) { // By website

				if (!searchWebsite(website))
					return "Website doesn't exist.";
				for (int i = 0; i < num; i++)
					if (websites[i].name.equalsIgnoreCase(website)) {
						for (int j = 0; j < websites[i].numOfData; j++) {
							str += websites[i].accounts[j].username + " " + websites[i].accounts[j].password + " "
									+ websites[i].name + "\n";
						}
					}
			}

		} catch (Exception e) {
			return "Something wrong happened. Please try again. Error: " + e;
		}

		return str;

	}

	public boolean displayUsername(String website) {

		if (!searchWebsite(website))
			return false;

		for (int i = 0; i < num; i++)
			if (websites[i].name.equalsIgnoreCase(website))
				for (int j = 0; j < websites[i].numOfData; j++) {
					System.out.println(websites[i].accounts[j].username);
				}
		return true;

	}

//    4. Update password
	public String updateInformation(String choice, String website, String username, String newUsername,
			String newPassword) {
		readToFile();
		String output = "";

		try {

			if (num == 0)
				return output;

			if (choice.equalsIgnoreCase("1")) { // Update Username

				output = "Success!";

				for (int i = 0; i < num; i++)
					if (websites[i].name.equalsIgnoreCase(website)) {
						for (int j = 0; j < websites[i].numOfData; j++)
							if (websites[i].accounts[j].username.equalsIgnoreCase(username)) {
								websites[i].accounts[j].username = newUsername;

							}
					}

			} else if (choice.equalsIgnoreCase("2")) { // Update password

				output = "Success!";

				for (int i = 0; i < num; i++)
					if (websites[i].name.equalsIgnoreCase(website))
						for (int j = 0; j < websites[i].numOfData; j++)
							if (websites[i].accounts[j].username.equalsIgnoreCase(username)) {
								websites[i].accounts[j].password = newPassword;

							}
			}

		} catch (Exception e) {
			System.out.println("Something wrong happened. Please try again. Error: " + e);
		}
		writeToFile();
		appendToFile();
		return output;

	}

	public boolean searchName(String name, String website, boolean flag) {
		for (int i = 0; i < num; i++)
			if (websites[i].name.equalsIgnoreCase(website))
				for (int j = 0; j < websites[i].numOfData; j++) {
					if (websites[i].accounts[j].username.equalsIgnoreCase(name))
						return true;
				}
		if (!flag) {

		}
		return false;
	}

	public boolean searchWebsite(String website) {
		for (int i = 0; i < num; i++)
			if (websites[i].name.equalsIgnoreCase(website))
				return true;

		return false;
	}

	public Website getWebsites(int i) {
		return websites[i];
	}

	public int getNum() {
		return num;
	}

//    5. Delete password

	public String Delete(String website, String username) {
		readToFile();
		String str = "";

		if (num == 0)
			return "There are no passwords!";
		
		for (int i = 0; i < num; i++)
			if (websites[i].name.equalsIgnoreCase(website))
				for (int j = 0; j < websites[i].numOfData; j++) {
					if (websites[i].accounts[j].username.equalsIgnoreCase(username)
							&& websites[i].name.equalsIgnoreCase(website)) {
						websites[i].accounts[j] = websites[i].accounts[websites[i].numOfData - 1];
						websites[i].numOfData--; // Reduce number of accounts in the website.
						if (websites[i].numOfData == 0) {
							websites[i] = websites[num-1];
							num--;
						}
						str = "Account deleted successfully!";
						break;
					}
				}
		writeToFile();
		appendToFile();
		return str;
	}

	public void readToFile() {
		try {
			BufferedReader read = new BufferedReader(new FileReader("Data.txt"));
			String str = "";
			String username = "";
			String password = "";
			String website = "";
			while ((str = read.readLine()) != null) {
				String data[] = new String[3];
				data = str.split(",");
				for (int i = 0; i < 3; i++) {
					if (i == 0)
						username = data[i];

					if (i == 1)
						password = data[i];

					if (i == 2)
						website = data[i];

				}
				createAccount(username, password, website);
			}
		} catch (Exception e) {
			System.out.println("Something wrong happened. Please try again. Error: " + e);
		}
	}

	public void writeToFile() {
		try {
			File data = new File("Data.txt");
			FileWriter writer = new FileWriter(data);
			writer.write("");
			writer.close();
		} catch (Exception e) {

		}
	}

	public void appendToFile() {
		try {
			File data = new File("Data.txt");
			PrintWriter writer = new PrintWriter(new FileOutputStream(data, true));
			String username = "";
			String password = "";
			String website = "";
			for (int i = 0; i < num; i++) {
				for (int j = 0; j < websites[i].numOfData; j++) {
					username = websites[i].accounts[j].username;
					password = websites[i].accounts[j].password;
					website = websites[i].name;

					writer.append(username + "," + password + "," + website + "\n");
				}
			}
			writer.close();
		} catch (Exception e) {

		}
	}

	public void createAccount(String username, String password, String website) { // EXCEPTION NEEDED
		if (!searchName(username, website, false)) {
			Data d = new Data(username, password);
			Website web = new Website(d, website);
			websites[num++] = web;
		}
	}

}
