#include <WinSock2.h>
#include <stdio.h>
#include <initguid.h>
#include <ws2bth.h>
#include <strsafe.h>
#include <stdint.h>
#include <Windows.h>
#include <WinUser.h>

#pragma once


#define INSTANCE_STR L"BluetoothWindows"
#define bufferLength 256

//00001101-0000-1000-8000-00805f9b34fb
DEFINE_GUID(g_guidServiceClass, 0x00001101, 0x0000, 0x1000, 0x80, 0x00, 0x00, 0x80, 0x5f, 0x9b, 0x34, 0xfb);

void print_error(char *wher, int code);
BOOL bind_socket(SOCKET local_socket, SOCKADDR_BTH *sock_addr_bth_local);
LPCSADDR_INFO create_addr_info(SOCKADDR_BTH *sock_addr_bth_local);
BOOL advertise_service_accepted(LPCSADDR_INFO addr_info, LPSTR *instance_name);
