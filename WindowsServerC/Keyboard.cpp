#include "Keyboard.h"

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

void oneClickButton(unsigned key) {
	sendVirtualKey(key,FALSE);
	Sleep(100);
	sendVirtualKey(key,TRUE);
}

void doubleClickButton(unsigned key,unsigned key2) {
	sendVirtualKey(key,FALSE);
	sendVirtualKey(key2,FALSE);
	Sleep(100);
	sendVirtualKey(key,TRUE);
	sendVirtualKey(key2,TRUE);
}

void joystickCMP(char* buffer) {
	
	if(!strcmp(buffer,"up")) {
		oneClickButton(VK_UP);
	}
			
	else if(!strcmp(buffer,"down")) {
		oneClickButton(VK_DOWN);
	}

	else if(!strcmp(buffer,"left")) {
		oneClickButton(VK_LEFT);
	}

	else if(!strcmp(buffer,"right")) {
		oneClickButton(VK_RIGHT);
	}
			
	else if(!strcmp(buffer,"A")) {
		oneClickButton(0x5A);
	}

	else if(!strcmp(buffer,"B")) {
		oneClickButton(0x58);
	}

	else if(!strcmp(buffer,"Y")) {
		oneClickButton(0x53);
	}

	else if(!strcmp(buffer,"X")) {
		oneClickButton(VK_RETURN);
	}

	else if(!strcmp(buffer,"start")) {
		oneClickButton(0x41);
	}

	else if(!strcmp(buffer,"select")) {
		oneClickButton(0x44);
	}
}

void playerCMP(char* buffer) {

	if(!strcmp(buffer,"playerPlay")) {
		oneClickButton(VK_SPACE);
	}

	else if(!strcmp(buffer,"playerPause")) {
		oneClickButton(VK_SPACE);
	}

	else if(!strcmp(buffer,"playerStop")) {
		oneClickButton(0x42);
	}

	else if(!strcmp(buffer,"playerRev")) {
		oneClickButton(VK_F1);
	}

	else if(!strcmp(buffer,"playerFwd")) {
		oneClickButton(VK_F2);
	}

	else if(!strcmp(buffer,"playerForward5")) {
		doubleClickButton(VK_CONTROL,VK_RIGHT);
	}

	else if(!strcmp(buffer,"playerBack5")) {
		doubleClickButton(VK_CONTROL,VK_LEFT);
	}

	else if(!strcmp(buffer,"playerVolUp")) {
		doubleClickButton(VK_CONTROL,VK_UP);
	}

	else if(!strcmp(buffer,"playerVolDown")) {
		doubleClickButton(VK_CONTROL,VK_DOWN);
	}
}