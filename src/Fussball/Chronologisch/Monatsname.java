package Fussball.Chronologisch;

import Allgemein.Verwendbare;

public enum Monatsname {
	UNBEKANNT, JANUAR, FEBRUAR, MÄRZ, APRIL, MAI, JUNI, JULI, AUGUST, SEPTEMBER, OKTOBER, NOVEMBER, DEZEMBER;
	
	public String toString() {
		return Verwendbare.zuNomen (super.toString());
	}
}
