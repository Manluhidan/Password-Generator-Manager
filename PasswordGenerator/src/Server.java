import java.io.*;
import java.net.*;

public class Server {
	public static void main(String[] args) throws Exception {

		ServerSocket serverSocket = null;
		Socket connectionSocket = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		DataInputStream dis = null;
		PasswordGenerator pg = null;
		String option = null;
		PrintWriter socketOut = null;

		try {
			serverSocket = new ServerSocket(3333); // create socket
			connectionSocket = serverSocket.accept(); // accept client connection

			System.out.println("Connection Established");

			inputStreamReader = new InputStreamReader(connectionSocket.getInputStream());
			dis = new DataInputStream(connectionSocket.getInputStream()); // read form client
			bufferedReader = new BufferedReader(inputStreamReader); // read from client
			socketOut = new PrintWriter(connectionSocket.getOutputStream(), true); // send to client

			pg = new PasswordGenerator(); // create object of brachMethods and initialize array

		} catch (Exception e) {
			System.out.println("Connection can not be established! Error: " + e);
			return;
		}

		while (true) {
			option = bufferedReader.readLine(); // receive user choice

			if (connectionSocket.isConnected() && option.equalsIgnoreCase("generate")) { 
																							
				while (true) {
					socketOut.println(pg.generatePassword());
					break;
				}
			} else if (connectionSocket.isConnected() && option.equalsIgnoreCase("create")) {
				try {
					File data = new File("Data.txt");
					data.createNewFile();
				} catch (Exception e) {

				}
				pg.readToFile();
				while (true) {
					String username = bufferedReader.readLine();
					String password = bufferedReader.readLine();
					String website = bufferedReader.readLine();
					while (pg.searchName(username, website, true)) {
						socketOut.println("This username already exists in this website!");
						username = bufferedReader.readLine();
						password = bufferedReader.readLine();
						website = bufferedReader.readLine();
					}
					if (pg.createPassword(username, password, website))
						socketOut.println("Success!");
					else
						socketOut.println("Failed!");
					break;
				}
			}

			else if (connectionSocket.isConnected() && option.equalsIgnoreCase("display")) {
				while (true) {
					String choice = bufferedReader.readLine();
					String website = bufferedReader.readLine();
					int flag = -1;

					while (pg.display(choice, website).equalsIgnoreCase("Website doesn't exist.")) {
						socketOut.println(pg.display(choice, website));
						choice = bufferedReader.readLine();
						website = bufferedReader.readLine();
						if (!pg.display(choice, website).equalsIgnoreCase("Website doesn't exist.")) {
							flag = -1;
							break;
						}
						flag = 1;
					}
					if (flag == -1)
						socketOut.println("Success!");
					if (choice.equalsIgnoreCase("1")) {
						for (int i = 0; i < pg.getNum(); i++)
							for (int j = 0; j < pg.getWebsites(i).numOfData; j++) {
								socketOut.println(pg.getWebsites(i).accounts[j].username + " "
										+ pg.getWebsites(i).accounts[j].password + " " + pg.getWebsites(i).name);
							}
					}
					if (choice.equalsIgnoreCase("2")) {
						for (int i = 0; i < pg.getNum(); i++)
							if (pg.getWebsites(i).name.equalsIgnoreCase(website))
								for (int j = 0; j < pg.getWebsites(i).numOfData; j++) {
									socketOut.println(pg.getWebsites(i).accounts[j].username + " "
											+ pg.getWebsites(i).accounts[j].password + " " + pg.getWebsites(i).name);
								}
					}
					socketOut.println(" ");
					break;
				}
			} else if (connectionSocket.isConnected() && option.equalsIgnoreCase("update")) {
				while (true) {
					String choice = "";
					String website = "";
					String username = "";
					String newUsername = "";
					String newPassword = "";
					int flag = -1;
					choice = bufferedReader.readLine();
					website = bufferedReader.readLine(); // Website checkpoint

					while (!pg.searchWebsite(website)) {
						socketOut.println("Website doesn't exist.");
						website = bufferedReader.readLine();
						if (pg.searchWebsite(website)) {
							flag = -1;
							break;
						}
						flag = 1;
					}
					if (flag == -1)
						socketOut.println("Success!");
					username = bufferedReader.readLine(); // Username checkpoint

					while (!pg.searchName(username, website, true)) {
						socketOut.println("Username doesn't exist in this website");
						username = bufferedReader.readLine();
						if (pg.searchName(username, website, true)) {
							flag = -1;
							break;
						}
						flag = 1;
					}
					if (flag == -1)
						socketOut.println("Success!");
					if (choice.equalsIgnoreCase("1")) {
						newUsername = bufferedReader.readLine();
						while (pg.searchName(newUsername, website, true)) { // new username checkpoint
							socketOut.println("This username already exists in this website!");
							newUsername = bufferedReader.readLine();
						}
					}
					if (choice.equalsIgnoreCase("2"))
						newPassword = bufferedReader.readLine();
					socketOut.println(pg.updateInformation(choice, website, username, newUsername, newPassword));
					break;
				}
			} else if (connectionSocket.isConnected() && option.equalsIgnoreCase("delete")) {
				while (true) {
					pg.readToFile();
					if (pg.getNum() == 0) {
						socketOut.println("There are no passwords!");
					}
					String website = "";
					String username = "";
					int flag = -1;
					if (flag == -1)
						socketOut.println("Success!");

					website = bufferedReader.readLine(); // Website checkpoint
					while (!pg.searchWebsite(website)) {
						socketOut.println("Website doesn't exist.");
						website = bufferedReader.readLine();
						if (pg.searchWebsite(website)) {
							flag = -1;
							break;
						}
						flag = 1;
					}
					if (flag == -1)
						socketOut.println("Success!");

					username = bufferedReader.readLine(); // Username checkpoint
					while (!pg.searchName(username, website, true)) {
						socketOut.println("Username doesn't exist in this website");
						username = bufferedReader.readLine();
						if (pg.searchName(username, website, true)) {
							flag = -1;
							break;
						}
						flag = 1;
					}
					socketOut.println(pg.Delete(website, username));
					break;
				}
			}

			else if (option.equalsIgnoreCase("exit")) {
				break;
			}
		}
		try {
			bufferedReader.close();
			dis.close();
			inputStreamReader.close();
			connectionSocket.close();
			serverSocket.close();
		} catch (Exception e) {

		}
	}
}