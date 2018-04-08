#! /usr/bin/python3
import socket

s=socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
s.sendto(('\xff'*6+'\xbc\x5f\xf4\x47\x2b\xbb'*16).encode('utf-8'), ("192.168.1.255",9))
