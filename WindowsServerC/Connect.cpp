#include "Connect.h"

void print_error(char *where, int code) {
	fprintf(stderr, "Error on %s: code %d\n", where, code);
}

BOOL bind_socket(SOCKET local_socket, SOCKADDR_BTH *sock_addr_bth_local) {
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

LPCSADDR_INFO create_addr_info(SOCKADDR_BTH *sock_addr_bth_local) {
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

BOOL advertise_service_accepted(LPCSADDR_INFO addr_info, LPSTR *instance_name) {
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