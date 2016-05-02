#include <WinSock2.h>
#include <stdio.h>
#include <initguid.h>
#include <ws2bth.h>
#include <strsafe.h>
#include <stdint.h>
#include <Windows.h>

#pragma comment(lib, "Ws2_32.lib")

//00001101-0000-1000-8000-00805f9b34fb
DEFINE_GUID(g_guidServiceClass, 0x00001101, 0x0000, 0x1000, 0x80, 0x00, 0x00, 0x80, 0x5f, 0x9b, 0x34, 0xfb);

#define INSTANCE_STR L"BluetoothWindows"

#define bufferLength 256

void print_error(char const *where, int code)
{
	fprintf(stderr, "Error on %s: code %d\n", where, code);
}

BOOL bind_socket(SOCKET local_socket, SOCKADDR_BTH *sock_addr_bth_local)
{
	int addr_len = sizeof(SOCKADDR_BTH);

	sock_addr_bth_local->addressFamily = AF_BTH;
	sock_addr_bth_local->port = BT_PORT_ANY;

	if (bind(local_socket, (struct sockaddr *) sock_addr_bth_local, sizeof(SOCKADDR_BTH)) == SOCKET_ERROR) {
		print_error("bind()", WSAGetLastError());
		return FALSE;
	}

	if (getsockname(local_socket, (struct sockaddr *)sock_addr_bth_local, &addr_len) == SOCKET_ERROR) {
		print_error("getsockname()", WSAGetLastError());
		return FALSE;
	}
	return TRUE;
}

LPCSADDR_INFO create_addr_info(SOCKADDR_BTH *sock_addr_bth_local)
{
	LPCSADDR_INFO addr_info = (LPCSADDR_INFO)calloc(1, sizeof(CSADDR_INFO));

	if (addr_info == NULL) {
		print_error("malloc(addr_info)", WSAGetLastError());
		return NULL;
	}

	addr_info[0].LocalAddr.iSockaddrLength = sizeof(SOCKADDR_BTH);
	addr_info[0].LocalAddr.lpSockaddr = (LPSOCKADDR)sock_addr_bth_local;
	addr_info[0].RemoteAddr.iSockaddrLength = sizeof(SOCKADDR_BTH);
	addr_info[0].RemoteAddr.lpSockaddr = (LPSOCKADDR)&sock_addr_bth_local;
	addr_info[0].iSocketType = SOCK_STREAM;
	addr_info[0].iProtocol = BTHPROTO_RFCOMM;
	return addr_info;
}

BOOL advertise_service_accepted(LPCSADDR_INFO addr_info, LPSTR *instance_name)
{
	WSAQUERYSET wsa_query_set = { 0 };
	size_t instance_name_size = 0;
	HRESULT res;

	instance_name_size += sizeof(INSTANCE_STR) + 1;
	*instance_name = (LPSTR) malloc(instance_name_size);
	if (*instance_name == NULL) {
		print_error("malloc(instance_name)", WSAGetLastError());
		return FALSE;
	}

	ZeroMemory(&wsa_query_set, sizeof(wsa_query_set));
    wsa_query_set.dwSize = sizeof(wsa_query_set);
    wsa_query_set.lpServiceClassId = (LPGUID)&g_guidServiceClass;

	wsa_query_set.lpszServiceInstanceName = "MyService";
    wsa_query_set.dwNameSpace = NS_BTH;
    wsa_query_set.dwNumberOfCsAddrs = 1;
    wsa_query_set.lpcsaBuffer = addr_info;


	if (WSASetService(&wsa_query_set, RNRSERVICE_REGISTER, 0) == SOCKET_ERROR) {
		//free(instance_name);
		print_error("WSASetService()", WSAGetLastError());
		return FALSE;
	}
	return TRUE;
}

DWORD sendScanCode(WORD scan, BOOL up) {
    INPUT inp = {0};
    inp.type = INPUT_KEYBOARD;
    inp.ki.wScan = scan;
    inp.ki.dwFlags = KEYEVENTF_SCANCODE | (up ? KEYEVENTF_KEYUP : 0); 
    return SendInput(1, &inp, sizeof(inp)) ? NO_ERROR : GetLastError();
}

DWORD sendVirtualKey(UINT vk, BOOL up) {
    UINT scan = MapVirtualKey(vk, MAPVK_VK_TO_VSC);

    return scan ? sendScanCode(scan, up) : ERROR_NO_UNICODE_TRANSLATION;
}

BOOL run_server_mode()
{
	LPSTR instance_name = NULL;
	SOCKET local_socket = INVALID_SOCKET;
	SOCKADDR_BTH sock_addr_bth_local = { 0 };
	LPCSADDR_INFO addr_info = NULL;
	BOOL ret = FALSE;

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
		int len_read = 0;

		while(1) {

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

			if(!strcmp(buffer,"ADMIN")){			
				WinExec("E:\\NO$GBA.2.6a\\NO$Zoomer.exe",1);
			}

			else if(!strcmp(buffer,"up")) {
				sendVirtualKey(VK_UP,FALSE);
				Sleep(100);
				sendVirtualKey(VK_UP,TRUE);
			}
			
			else if(!strcmp(buffer,"down")) {
				sendVirtualKey(VK_DOWN,FALSE);
				Sleep(100);
				sendVirtualKey(VK_DOWN,TRUE);
			}

			else if(!strcmp(buffer,"left")) {
				sendVirtualKey(VK_LEFT,FALSE);
				Sleep(100);
				sendVirtualKey(VK_LEFT,TRUE);
			}

			else if(!strcmp(buffer,"right")) {
				sendVirtualKey(VK_RIGHT,FALSE);
				Sleep(100);
				sendVirtualKey(VK_RIGHT,TRUE);
			}
			
			else if(!strcmp(buffer,"A")) {
				sendVirtualKey(0x5A,FALSE);
				Sleep(100);
				sendVirtualKey(0x5A,TRUE);
			}

			else if(!strcmp(buffer,"B")) {
				sendVirtualKey(0x58,FALSE);
				Sleep(100);
				sendVirtualKey(0x58,TRUE);
			}

			else if(!strcmp(buffer,"Y")) {
				sendVirtualKey(0x53,FALSE);
				Sleep(100);
				sendVirtualKey(0x53,TRUE);
			}

			else if(!strcmp(buffer,"X")) {
				sendVirtualKey(VK_RETURN,FALSE);
				Sleep(100);
				sendVirtualKey(VK_RETURN,TRUE);
			}

			else if(!strcmp(buffer,"start")) {
				sendVirtualKey(0x41,FALSE);
				Sleep(100);
				sendVirtualKey(0x41,TRUE);
			}

			else if(!strcmp(buffer,"select")) {
				sendVirtualKey(0x44,FALSE);
				Sleep(100);
				sendVirtualKey(0x44,TRUE);
			}

			else if(!strcmp(buffer,"end")) {
				free(buffer);
				break;
			}

			else if (len_read == 0) {
				free(buffer);
				fprintf(stderr, "Nothing read, end of communication\n");
				break;
			}

			free(buffer);
		}

		printf("Communication over\n");
		closesocket(client_socket);
	}

	free(addr_info);
	free(instance_name);
	closesocket(local_socket);
	return TRUE;
}

int main(int argc, char **argv)
{
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