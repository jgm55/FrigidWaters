package edu.drexel.cci.hiyh.has.driver.insteon;

import java.util.Arrays;

/**
 * Utilities. These should probably be moved up a few levels.
 */
class Util {
    public static byte[] concat(byte[] a, byte[] b) {
        byte[] rv = Arrays.copyOf(a, a.length + b.length);
        System.arraycopy(b, 0, rv, a.length, b.length);
        return rv;
    }
    
    public static byte toX10(char house, int id) {
    	byte addr = 0;
    	if (house == 'A' || house == 'a') {
    		addr += 0x60;
    	}
    	else if (house == 'B' || house == 'b') {
    		addr += 0xE0;
    	}
    	else if (house == 'C' || house == 'c') {
    		addr += 0x20;
    	}
    	else if (house == 'D' || house == 'd') {
    		addr += 0xA0;
    	}
    	else if (house == 'E' || house == 'e') {
    		addr += 0x10;
    	}
    	else if (house == 'F' || house == 'f') {
    		addr += 0x90;
    	}
    	else if (house == 'G' || house == 'g') {
    		addr += 0x50;
    	}
    	else if (house == 'H' || house == 'h') {
    		addr += 0xD0;
    	}
    	else if (house == 'I' || house == 'i') {
    		addr += 0x70;
    	}
    	else if (house == 'J' || house == 'j') {
    		addr += 0xF0;
    	}
    	else if (house == 'K' || house == 'k') {
    		addr += 0x30;
    	}
    	else if (house == 'L' || house == 'l') {
    		addr += 0xB0;
    	}
    	else if (house == 'M' || house == 'm') {
    		addr += 0x00;
    	}
    	else if (house == 'N' || house == 'n') {
    		addr += 0x80;
    	}
    	else if (house == 'O' || house == 'o') {
    		addr += 0x40;
    	}
    	else if (house == 'P' || house == 'p') {
    		addr += 0xC0;
    	}
    	
    	if (id == 1) {
    		addr += 0x06;
    	}
    	else if (id == 2) {
    		addr += 0x0E;
    	}
    	else if (id == 3) {
    		addr += 0x02;
    	}
    	else if (id == 4) {
    		addr += 0x0A;
    	}
    	else if (id == 5) {
    		addr += 0x01;
    	}
    	else if (id == 6) {
    		addr += 0x09;
    	}
    	else if (id == 7) {
    		addr += 0x05;
    	}
    	else if (id == 8) {
    		addr += 0x0D;
    	}
    	else if (id == 9) {
    		addr += 0x07;
    	}
    	else if (id == 10) {
    		addr += 0x0F;
    	}
    	else if (id == 11) {
    		addr += 0x03;
    	}
    	else if (id == 12) {
    		addr += 0x0B;
    	}
    	else if (id == 13) {
    		addr += 0x00;
    	}
    	else if (id == 14) {
    		addr += 0x08;
    	}
    	else if (id == 15) {
    		addr += 0x04;
    	}
    	else if (id == 16) {
    		addr += 0x0C;
    	}
    	return addr;
    }

}
