import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws Exception {
		Socket clientSocket = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		Scanner in = null;
		PrintWriter socketOut = null;
		DataOutputStream dos = null;
		Scanner scanner = new Scanner(System.in);

		try {
			clientSocket = new Socket("localhost", 3333); // create socket and connect to the server

			System.out.println("Connection Established");

			inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
			dos = new DataOutputStream(clientSocket.getOutputStream()); // send to server
			socketOut = new PrintWriter(clientSocket.getOutputStream(), true); // send to server

			bufferedReader = new BufferedReader(inputStreamReader); // receive from server
			in = new Scanner(System.in); // input form user

		} catch (Exception e) {
			System.out.println("Connection can not be established! Error: " + e);
			return;
		}

		int option = 6;

		do {

			System.out.println("------------------------------------------------------------");
			System.out.println("|            Password Generator & Manager v1.0              |");
			System.out.println("|                                                           |");
			System.out.println("| 1- Generate password                                      |");
			System.out.println("| 2- Save password                                          |");
			System.out.println("| 3- Display passwords or usernames                         |");
			System.out.println("| 4- Update password or username                            |");
			System.out.println("| 5- Delete                                                 |");
			System.out.println("| 6- Exit                                                   |");
			System.out.println("------------------------------------------------------------");

			try {
				System.out.print(">>> ");
				option = in.nextInt();

			} catch (Exception e) { // String input
				System.out.println("Please enter a valid input");
			}

			if (option == 1) {
				socketOut.println("generate");
				String fromServer = bufferedReader.readLine();
																
				System.out.println(fromServer);

			} else if (option == 2) {
				socketOut.println("create");

				System.out.print("Enter Username for the website: ");
				String username = (String) scanner.nextLine();
				socketOut.println(username);

				System.out.print("Enter Password: ");
				String password = (String) scanner.nextLine();
				socketOut.println(password);

				System.out.print("Enter Website: ");
				String website = (String) scanner.nextLine();
				socketOut.println(website);

				String fromServer = bufferedReader.readLine(); // Result from server
				boolean flag;

				if (fromServer.equalsIgnoreCase("This username already exists in this website!")) {
					flag = false;
					while (!flag) {
						System.out.println(fromServer);

						System.out.print("Enter Username for the website: ");
						username = (String) scanner.nextLine();
						socketOut.println(username);

						System.out.print("Enter Password: ");
						password = (String) scanner.nextLine();
						socketOut.println(password);

						System.out.print("Enter Website: "); 
						website = (String) scanner.nextLine();
						socketOut.println(website);

						fromServer = bufferedReader.readLine();
						if (!fromServer.equalsIgnoreCase("This username already exists in this website!"))
							flag = true;
					}
				}
				// if username exists in the same website. retry again
				System.out.println(fromServer);
			} else if (option == 3) { 
				socketOut.println("display");
				System.out.println("1. Display all passwords");
				System.out.println("2. Display usernames by website");
				System.out.print(">>> ");
				try {
					String choice = scanner.nextLine();
					while (!choice.equalsIgnoreCase("1") && !choice.equalsIgnoreCase("2")) {
						System.out.println("Please enter 1 (1st choice) or 2 (second choice).");
						choice = scanner.nextLine();
					}
					String website = ""; // user input
					if (choice.equalsIgnoreCase("1")) {
						socketOut.println(choice);
						socketOut.println(website);
						String fromServer = bufferedReader.readLine();
						String line;

						while ((line = bufferedReader.readLine()) != null) {
							System.out.println(line);
							if (line.equalsIgnoreCase(" "))
								break;
						}
					} // end of choice 1
					else if (choice.equalsIgnoreCase("2")) {
						System.out.print("Enter the website: ");
						website = scanner.nextLine(); // user input
						socketOut.println(choice);
						socketOut.println(website);
						String fromServer = bufferedReader.readLine();

						while (fromServer.equalsIgnoreCase("Website doesn't exist.")) {
							System.out.println(fromServer);
							System.out.print("Enter the website: ");
							website = scanner.nextLine(); // user input
							socketOut.println(choice);
							socketOut.println(website);
							fromServer = bufferedReader.readLine();
						}
						String line;
						while ((line = bufferedReader.readLine()) != null) {
							System.out.println(line);
							if (line.equalsIgnoreCase(" "))
								break;
						}
					} // end of choice 2

				} catch (Exception e) {
					System.out.println("Please enter numbers");
				}

			} else if (option == 4) {
				socketOut.println("update");
				System.out.println("1. Update username");
				System.out.println("2. Update password");
				System.out.print(">>> ");
				String choice = scanner.nextLine();

				while (!choice.equalsIgnoreCase("1") && !choice.equalsIgnoreCase("2")) {
					System.out.println("Please enter 1 (1st choice) or 2 (second choice).");
					choice = scanner.nextLine();
				}
				socketOut.println(choice);
				System.out.print("Enter website where the Username belongs to: "); // Website
				String website = scanner.nextLine(); // user input
				socketOut.println(website);
				String fromServer = bufferedReader.readLine();

				while (fromServer.equalsIgnoreCase("Website doesn't exist.")) { // Website checkpoint
					System.out.println(fromServer);
					System.out.print("Enter the website: ");
					website = scanner.nextLine(); // user input
					socketOut.println(website);
					fromServer = bufferedReader.readLine();
				}
				System.out.print("Enter the username you want to change: "); // Username
				String username = scanner.nextLine();
				socketOut.println(username);
				fromServer = bufferedReader.readLine();

				while (fromServer.equalsIgnoreCase("Username doesn't exist in this website")) { // Username checkpoint
					System.out.println(fromServer);
					System.out.print("Enter the username you want to change: "); // Username
					username = scanner.nextLine();
					socketOut.println(username);
					fromServer = bufferedReader.readLine();
				}
				if (choice.equalsIgnoreCase("1")) {
					System.out.print("Enter the new username: "); // Username
					String newUsername = scanner.nextLine();
					socketOut.println(newUsername);
					fromServer = bufferedReader.readLine();

					while (fromServer.equalsIgnoreCase("This username already exists in this website!")) {
						System.out.println(fromServer);
						System.out.print("Enter the new username: "); // Username
						newUsername = scanner.nextLine();
						socketOut.println(newUsername);
						fromServer = bufferedReader.readLine();
					}
				} else if (choice.equalsIgnoreCase("2")) {
					System.out.print("Enter new password: ");
					String newPassword = scanner.nextLine();
					socketOut.println(newPassword);
				}
				System.out.println(fromServer);
			} else if (option == 5) {
				socketOut.println("delete");
				String fromServer = bufferedReader.readLine();
				if (fromServer.equalsIgnoreCase("There are no passwords!")) {
					System.out.println("There are no passwords!");
					break;
				}
				System.out.print("Enter website where the Username belongs to: "); // Website
				String website = scanner.nextLine(); // user input
				socketOut.println(website);

				fromServer = bufferedReader.readLine();
				while (fromServer.equalsIgnoreCase("Website doesn't exist.")) { // Website checkpoint
					System.out.println(fromServer);
					System.out.print("Enter the website: ");
					website = scanner.nextLine(); // user input
					socketOut.println(website);
					fromServer = bufferedReader.readLine();
				}
				System.out.print("Enter the username you want to delete: "); // Username
				String username = scanner.nextLine();
				socketOut.println(username);
				fromServer = bufferedReader.readLine();

				while (fromServer.equalsIgnoreCase("Username doesn't exist in this website")) { // Username checkpoint
					System.out.println(fromServer);
					System.out.print("Enter the username you want to delete: "); // Username
					username = scanner.nextLine();
					socketOut.println(username);
					fromServer = bufferedReader.readLine();
				}

				System.out.println(fromServer);
			}

			else if (option == 6) {
				socketOut.println("exit");
			} else
				System.out.println("Please enter a valid option.");

		} while (option != 6 && clientSocket.isConnected());

		System.out.println("Thanks. Goodbye!");

		try {
			socketOut.close();
			bufferedReader.close();
			dos.close();
			inputStreamReader.close();
			in.close();
			clientSocket.close();
		} catch (Exception e) {

		}
	}
}
