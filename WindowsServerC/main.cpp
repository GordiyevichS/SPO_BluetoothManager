#include "Connect.h"
#include "Keyboard.h"
#include "Mouse.h"
#include <conio.h>

#pragma comment(lib, "Ws2_32.lib")

void openMyComputer() {

	HANDLE Handle;

	ShellExecute(NULL, NULL, "::{20D04FE0-3AEA-1069-A2D8-08002B30309D}", NULL,NULL,SW_SHOWNORMAL);
}

BOOL readBuffer(char* &buffer,SOCKET client_socket) {

	int len_read;

	buffer =(char*) calloc(bufferLength, sizeof(char*));
	if (buffer == NULL) {
		print_error("malloc(buffer)", WSAGetLastError());
		return FALSE;
	}

	len_read = recv(client_socket, buffer, bufferLength, 0);
	if (len_read == SOCKET_ERROR) {
		free(buffer);
		print_error("recv()", WSAGetLastError());
		return FALSE;
	}
}

BOOL run_server_mode() {
	LPSTR instance_name = NULL;
	SOCKET local_socket = INVALID_SOCKET;
	SOCKADDR_BTH sock_addr_bth_local = { 0 };
	LPCSADDR_INFO addr_info = NULL;
	BOOL ret = FALSE;
	char end;

	local_socket = socket(AF_BTH, SOCK_STREAM, BTHPROTO_RFCOMM);
	if (local_socket == INVALID_SOCKET) {
		print_error("socket()", WSAGetLastError());
		return FALSE;
	}

	ret = bind_socket(local_socket, &sock_addr_bth_local);
	if (!ret) {
		return FALSE;
	}

	addr_info = create_addr_info(&sock_addr_bth_local);
	if (!addr_info) {
		return FALSE;
	}

	ret = advertise_service_accepted(addr_info, &instance_name);
	if (!ret) {
		free(addr_info);
		if (instance_name) {
			free(instance_name);
		}
		return FALSE;
	}

	if (listen(local_socket, 4) == SOCKET_ERROR) {
		print_error("listen()", WSAGetLastError());
		free(addr_info);
		free(instance_name);
		return FALSE;
	}

	while (1) {
		printf("Waiting for client connection...");
		SOCKET client_socket = accept(local_socket, NULL, NULL);
		if (client_socket == INVALID_SOCKET) {
			print_error("accept()", WSAGetLastError());
			return FALSE;
		}
		printf("Client connected !\n");

		char *buffer = NULL;

		while(1) {

			if(!readBuffer(buffer,client_socket)) {
				return FALSE;
			}

			if(!strcmp(buffer,"openNoGba")){			
				WinExec("E:\\NO$GBA.2.6a\\NO$GBA.exe",1);
				free(buffer);
			}

			else if(!strcmp(buffer,"openMyComp")) {
				openMyComputer();
				free(buffer);
			}

			else if(!strcmp(buffer,"joystick")) {
				free(buffer);
				printf("Start joystick mode\n");
				while(1) {
					if(!readBuffer(buffer,client_socket)) {
						return FALSE;
					}

					joystickCMP(buffer); 

					if(!strcmp(buffer,"endJoystick")){
						free(buffer);
						break;
					}


					free(buffer);
				}
				printf("End joystick mode\n");
				continue;
			}

			else if(!strcmp(buffer,"openPlayer")) {
				free(buffer);
				WinExec("C:\\Program Files (x86)\\AIMP3\\AIMP3.exe",1);
				printf("Start player mode\n");
				while(1) {
					if(!readBuffer(buffer,client_socket)) {
						return FALSE;
					}

					playerCMP(buffer); 

					if(!strcmp(buffer,"endPlayer")){
						free(buffer);
						break;
					}


					free(buffer);
				}
				printf("End player mode\n");
				continue;
			}

			else if(!strcmp(buffer,"end")) {
				free(buffer);
				break;
			}

			else
				mouseCMP(buffer);
		}

		printf("Communication over\n");
		closesocket(client_socket);

		printf("Continue?(press 'n' to exit)");
		if(end = getch() == 'n') {
			break;
		}
	}

	free(addr_info);
	free(instance_name);
	closesocket(local_socket);
	return TRUE;
}

int main(int argc, char **argv) {
	WSADATA WSAData = { 0 };
	int ret = 0;

	(void)argc;
	(void)argv;
	printf("Start the server...\n");
	ret = WSAStartup(MAKEWORD(2, 2), &WSAData);
	if (ret < 0) {
		print_error("WSAStartup()", GetLastError());
		return EXIT_FAILURE;
	}

	run_server_mode();

	WSACleanup();

	system("pause");
	return EXIT_SUCCESS;
}