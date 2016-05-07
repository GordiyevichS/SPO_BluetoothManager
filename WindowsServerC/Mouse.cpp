#include "Mouse.h"

void mouseCMP(char* buffer) {

	POINT point;

	if(!strcmp(buffer,"mouseUp")) {
		GetCursorPos(&point);
		SetCursorPos(point.x,point.y+3);
		Sleep(10);
	}

	else if(!strcmp(buffer,"mouseDown")) {
		GetCursorPos(&point);
		SetCursorPos(point.x,point.y-3);
		Sleep(10);
	}

	else if(!strcmp(buffer,"mouseLeft")) {
		GetCursorPos(&point);
		SetCursorPos(point.x-3,point.y);
		Sleep(10);
	}

	else if(!strcmp(buffer,"mouseRight")) {
		GetCursorPos(&point);
		SetCursorPos(point.x+3,point.y);
		Sleep(10);
	}

	else if(!strcmp(buffer,"mouseUpRight")) {
		GetCursorPos(&point);
		SetCursorPos(point.x+3,point.y+3);
		Sleep(10);
	}

	else if(!strcmp(buffer,"mouseUpLeft")) {
		GetCursorPos(&point);
		SetCursorPos(point.x+3,point.y-3);
		Sleep(10);
	}

	else if(!strcmp(buffer,"mouseDownRight")) {
		GetCursorPos(&point);
		SetCursorPos(point.x-3,point.y+3);
		Sleep(10);
	}

	else if(!strcmp(buffer,"mouseDownLeft")) {
		GetCursorPos(&point);
		SetCursorPos(point.x-3,point.y-3);
		Sleep(10);
	}

	else if (!strcmp(buffer,"mouseRightClick")) {
		GetCursorPos(&point);
		mouse_event(MOUSEEVENTF_RIGHTDOWN,point.x, point.y, 0, 0);
		mouse_event(MOUSEEVENTF_RIGHTUP, point.x, point.y, 0, 0);
	}

	else if (!strcmp(buffer,"mouseLeftClick")) {
		GetCursorPos(&point);
		mouse_event(MOUSEEVENTF_LEFTDOWN,point.x, point.y, 0, 0);
		mouse_event(MOUSEEVENTF_LEFTUP, point.x, point.y, 0, 0);
	}

	else if (!strcmp(buffer,"mouseWheelUp")) {
		mouse_event(MOUSEEVENTF_WHEEL,0,0,120,0);
	}

	else if (!strcmp(buffer,"mouseWheelDown")) {
		mouse_event(MOUSEEVENTF_WHEEL,0,0,(DWORD)-120,0);
	}
}