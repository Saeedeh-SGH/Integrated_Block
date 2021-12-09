'''
Created on Aug 20, 2019

@author: Saeedeh
'''

import gensim
import nltk
import os

from nltk.tokenize import word_tokenize
from win32comext.shell.test.testShellFolder import num



raw_documents = ["\x47\x65\x74\x20\x45\x6d\x61\x69\x6c\x20\x55\x70\x64\x61\x74\x65\x73\x0a\x43\x6f\x6e\x74\x61\x63\x74\x20\x55\x73",
                 "\x54\x68\x65\x20\x57\x68\x69\x74\x65\x20\x48\x6f\x75\x73\x65\x20\x42\x6c\x6f\x67",
                 "\x50\x68\x6f\x74\x6f\x73\x20\x26\x20\x56\x69\x64\x65\x6f\x73",
                 "\x42\x72\x69\x65\x66\x69\x6e\x67\x20\x52\x6f\x6f\x6d",
                 "\x49\x73\x73\x75\x65\x73",
                 "\x54\x68\x65\x20\x41\x64\x6d\x69\x6e\x69\x73\x74\x72\x61\x74\x69\x6f\x6e",
                 "\x74\x68\x65\x20\x57\x68\x69\x74\x65\x20\x48\x6f\x75\x73\x65",
                 "\x4f\x75\x72\x20\x47\x6f\x76\x65\x72\x6e\x6d\x65\x6e\x74",
                 "\x48\x65\x61\x6c\x74\x68\x20\x52\x65\x66\x6f\x72\x6d\x20\x42\x65\x6e\x65\x66\x69\x74\x73\x20\x57\x6f\x6d\x65\x6e",
                 "\x4c\x65\x61\x72\x6e\x20\x68\x6f\x77\x20\x74\x68\x65\x20\x41\x66\x66\x6f\x72\x64\x61\x62\x6c\x65\x20\x43\x61\x72\x65\x20\x41\x63\x74\x20\x69\x73\x20\x67\x69\x76\x69\x6e\x67\x20\x77\x6f\x6d\x65\x6e\x20\x6d\x6f\x72\x65\x20\x63\x6f\x6e\x74\x72\x6f\x6c\x20\x6f\x76\x65\x72\x20\x74\x68\x65\x69\x72\x20\x68\x65\x61\x6c\x74\x68\x20\x63\x61\x72\x65\x20\x74\x68\x61\x6e\x20\x65\x76\x65\x72\x20\x62\x65\x66\x6f\x72\x65\x2e",
                 "\x50\x4f\x50\x55\x4c\x41\x52\x20\x54\x4f\x50\x49\x43\x53",
                 "\x54\x6f\x67\x65\x74\x68\x65\x72\x20\x77\x65\x20\x63\x61\x6e\x20\x62\x75\x69\x6c\x64\x20\x61\x20\x66\x61\x69\x72\x2c\x20\x65\x66\x66\x65\x63\x74\x69\x76\x65\x20\x61\x6e\x64\x20\x63\x6f\x6d\x6d\x6f\x6e\x20\x73\x65\x6e\x73\x65\x20\x69\x6d\x6d\x69\x67\x72\x61\x74\x69\x6f\x6e\x20\x73\x79\x73\x74\x65\x6d\x2e",
                 "\x49\x6d\x6d\x69\x67\x72\x61\x74\x69\x6f\x6e",
                 "\x50\x72\x65\x70\x61\x72\x69\x6e\x67\x20\x73\x74\x75\x64\x65\x6e\x74\x73\x20\x66\x6f\x72\x20\x74\x68\x65\x20\x6a\x6f\x62\x73\x20\x6f\x66\x20\x74\x68\x65\x20\x66\x75\x74\x75\x72\x65\x20\x73\x74\x61\x72\x74\x73\x20\x77\x69\x74\x68\x20\x61\x20\x73\x74\x72\x6f\x6e\x67\x20\x73\x63\x68\x6f\x6f\x6c\x20\x73\x79\x73\x74\x65\x6d",
                 "\x45\x64\x75\x63\x61\x74\x69\x6f\x6e",
                 "\x4e\x6f\x77\x20\x69\x73\x20\x74\x68\x65\x20\x74\x69\x6d\x65\x20\x74\x6f\x20\x64\x6f\x20\x73\x6f\x6d\x65\x74\x68\x69\x6e\x67\x20\x61\x62\x6f\x75\x74\x20\x67\x75\x6e\x20\x76\x69\x6f\x6c\x65\x6e\x63\x65\x2e",
                 "\x52\x65\x64\x75\x63\x69\x6e\x67\x20\x47\x75\x6e\x20\x56\x69\x6f\x6c\x65\x6e\x63\x65",
                 "\x45\x4e\x47\x41\x47\x45",
                 "\x53\x4f\x43\x49\x41\x4c",
                 "\x4e\x45\x57\x53",
                 "\x49\x4e\x49\x54\x49\x41\x54\x49\x56\x45\x53",
                 "\x54\x4f\x50\x20\x4e\x45\x57\x53",
                 "\x4d\x61\x79\x20\x31\x31\x2c\x20\x35\x3a\x33\x30\x61\x6d",
                 "\x57\x65\x65\x6b\x6c\x79\x20\x41\x64\x64\x72\x65\x73\x73\x3a\x20\x47\x72\x6f\x77\x69\x6e\x67\x20\x74\x68\x65\x20\x48\x6f\x75\x73\x69\x6e\x67\x20\x4d\x61\x72\x6b\x65\x74\x20\x61\x6e\x64\x20\x53\x75\x70\x70\x6f\x72\x74\x69\x6e\x67\x20\x4f\x75\x72\x20\x48\x6f\x6d\x65\x6f\x77\x6e\x65\x72\x73",
                 "\x4d\x61\x79\x20\x31\x33\x2c\x20\x35\x3a\x30\x32\x70\x6d",
                 "\x50\x72\x65\x73\x69\x64\x65\x6e\x74\x20\x4f\x62\x61\x6d\x61\x20\x45\x78\x70\x6c\x61\x69\x6e\x73\x20\x48\x6f\x77\x20\x48\x65\x61\x6c\x74\x68\x20\x52\x65\x66\x6f\x72\x6d\x20\x49\x73\x20\x48\x65\x6c\x70\x69\x6e\x67\x20\x57\x6f\x6d\x65\x6e",
                 "\x40\x57\x48\x49\x54\x45\x48\x4f\x55\x53\x45",
                 "\x52\x54\x20\x73\x6f\x20\x79\x6f\x75\x72\x20\x66\x72\x69\x65\x6e\x64\x73\x20\x6b\x6e\x6f\x77\x20\x61\x62\x6f\x75\x74\x20\x50\x72\x65\x73\x69\x64\x65\x6e\x74\x20\x4f\x62\x61\x6d\x61\x27\x73\x20\x70\x6c\x61\x6e\x20\x74\x6f\x20\x66\x69\x78\x20\x6f\x75\x72\x20\x62\x72\x6f\x6b\x65\x6e\x20\x69\x6d\x6d\x69\x67\x72\x61\x74\x69\x6f\x6e\x20\x73\x79\x73\x74\x65\x6d\x2e\x20\x23\x49\x6d\x6d\x69\x67\x72\x61\x74\x69\x6f\x6e\x4e\x61\x74\x69\x6f\x6e\x2c\x20\x68\x74\x74\x70\x3a\x2f\x2f\x74\x2e\x63\x6f\x2f\x48\x43\x50\x73\x4a\x50\x35\x4d\x47\x4f",
                 "\x50\x48\x4f\x54\x4f\x20\x4f\x46\x20\x54\x48\x45\x20\x44\x41\x59",
                 "\x50\x72\x65\x73\x69\x64\x65\x6e\x74\x20\x42\x61\x72\x61\x63\x6b\x20\x4f\x62\x61\x6d\x61\x20\x67\x65\x73\x74\x75\x72\x65\x73\x20\x64\x75\x72\x69\x6e\x67\x20\x61\x20\x6d\x65\x65\x74\x69\x6e\x67\x20\x69\x6e\x20\x74\x68\x65\x20\x4f\x76\x61\x6c\x20\x4f\x66\x66\x69\x63\x65\x2c\x20\x4d\x61\x79\x20\x31\x34\x2c\x20\x32\x30\x31\x33\x2e\x20\x28\x4f\x66\x66\x69\x63\x69\x61\x6c\x20\x57\x68\x69\x74\x65\x20\x48\x6f\x75\x73\x65\x20\x50\x68\x6f\x74\x6f\x20\x62\x79\x20\x50\x65\x74\x65\x20\x53\x6f\x75\x7a\x61\x29",
                 "\x56\x69\x65\x77\x20\x4d\x6f\x72\x65\x20\x47\x61\x6c\x6c\x65\x72\x69\x65\x73",
                 "\x57\x48\x49\x54\x45\x20\x48\x4f\x55\x53\x45\x20\x44\x41\x49\x4c\x59\x20\x53\x43\x48\x45\x44\x55\x4c\x45",
                 "\x54\x68\x65\x20\x50\x72\x65\x73\x69\x64\x65\x6e\x74\x27\x73\x20\x53\x63\x68\x65\x64\x75\x6c\x65\x0a\x54\x68\x65\x20\x56\x69\x63\x65\x20\x50\x72\x65\x73\x69\x64\x65\x6e\x74\x27\x73\x20\x53\x63\x68\x65\x64\x75\x6c\x65\x0a\x54\x68\x65\x20\x46\x75\x6c\x6c\x20\x57\x68\x69\x74\x65\x20\x48\x6f\x75\x73\x65\x20\x53\x63\x68\x65\x64\x75\x6c\x65",
                 "\x46\x61\x63\x65\x62\x6f\x6f\x6b\x20\x54\x77\x69\x74\x74\x65\x72\x20\x47\x6f\x6f\x67\x6c\x65\x20\x50\x6c\x75\x73\x20\x59\x6f\x75\x74\x75\x62\x65\x20\x46\x6c\x69\x63\x6b\x72\x20\x4c\x69\x6e\x6b\x65\x64\x69\x6e\x20\x46\x6f\x75\x72\x73\x71\x75\x61\x72\x65\x20\x56\x69\x6d\x65\x6f",
                 "\x48\x6f\x6d\x65\x0a\x54\x68\x65\x20\x57\x68\x69\x74\x65\x20\x48\x6f\x75\x73\x65\x20\x42\x6c\x6f\x67\x0a\x50\x68\x6f\x74\x6f\x73\x20\x26\x20\x56\x69\x64\x65\x6f\x73",
                 "\x50\x68\x6f\x74\x6f\x20\x47\x61\x6c\x6c\x65\x72\x69\x65\x73\x0a\x56\x69\x64\x65\x6f\x0a\x50\x65\x72\x66\x6f\x72\x6d\x61\x6e\x63\x65\x73\x0a\x4c\x69\x76\x65\x20\x53\x74\x72\x65\x61\x6d\x73\x0a\x50\x6f\x64\x63\x61\x73\x74\x73",
                 "\x42\x72\x69\x65\x66\x69\x6e\x67\x20\x52\x6f\x6f\x6d",
                 "\x59\x6f\x75\x72\x20\x57\x65\x65\x6b\x6c\x79\x20\x41\x64\x64\x72\x65\x73\x73\x0a\x53\x70\x65\x65\x63\x68\x65\x73\x20\x26\x20\x52\x65\x6d\x61\x72\x6b\x73\x0a\x50\x72\x65\x73\x73\x20\x42\x72\x69\x65\x66\x69\x6e\x67\x73\x0a\x53\x74\x61\x74\x65\x6d\x65\x6e\x74\x73\x20\x26\x20\x52\x65\x6c\x65\x61\x73\x65\x73\x0a\x57\x68\x69\x74\x65\x20\x48\x6f\x75\x73\x65\x20\x53\x63\x68\x65\x64\x75\x6c\x65\x0a\x50\x72\x65\x73\x69\x64\x65\x6e\x74\x69\x61\x6c\x20\x41\x63\x74\x69\x6f\x6e\x73\x0a\x4c\x65\x67\x69\x73\x6c\x61\x74\x69\x6f\x6e\x0a\x4e\x6f\x6d\x69\x6e\x61\x74\x69\x6f\x6e\x73\x20\x26\x20\x41\x70\x70\x6f\x69\x6e\x74\x6d\x65\x6e\x74\x73\x0a\x44\x69\x73\x63\x6c\x6f\x73\x75\x72\x65\x73",
                 "\x49\x73\x73\x75\x65\x73",
                 "\x43\x69\x76\x69\x6c\x20\x52\x69\x67\x68\x74\x73\x0a\x44\x65\x66\x65\x6e\x73\x65\x0a\x44\x69\x73\x61\x62\x69\x6c\x69\x74\x69\x65\x73\x0a\x45\x63\x6f\x6e\x6f\x6d\x79\x0a\x45\x64\x75\x63\x61\x74\x69\x6f\x6e\x0a\x45\x6e\x65\x72\x67\x79\x20\x26\x20\x45\x6e\x76\x69\x72\x6f\x6e\x6d\x65\x6e\x74\x0a\x45\x74\x68\x69\x63\x73\x0a\x46\x6f\x72\x65\x69\x67\x6e\x20\x50\x6f\x6c\x69\x63\x79\x0a\x48\x65\x61\x6c\x74\x68\x20\x43\x61\x72\x65\x0a\x48\x6f\x6d\x65\x6c\x61\x6e\x64\x20\x53\x65\x63\x75\x72\x69\x74\x79\x0a\x49\x6d\x6d\x69\x67\x72\x61\x74\x69\x6f\x6e\x0a\x52\x65\x66\x69\x6e\x61\x6e\x63\x69\x6e\x67\x0a\x52\x75\x72\x61\x6c\x0a\x53\x65\x72\x76\x69\x63\x65\x0a\x53\x65\x6e\x69\x6f\x72\x73\x20\x26\x20\x53\x6f\x63\x69\x61\x6c\x20\x53\x65\x63\x75\x72\x69\x74\x79\x0a\x53\x6e\x61\x70\x73\x68\x6f\x74\x73\x0a\x54\x61\x78\x65\x73\x0a\x54\x65\x63\x68\x6e\x6f\x6c\x6f\x67\x79\x0a\x55\x72\x62\x61\x6e\x20\x50\x6f\x6c\x69\x63\x79\x0a\x56\x65\x74\x65\x72\x61\x6e\x73\x0a\x56\x69\x6f\x6c\x65\x6e\x63\x65\x20\x50\x72\x65\x76\x65\x6e\x74\x69\x6f\x6e\x0a\x57\x6f\x6d\x65\x6e",
                 "\x54\x68\x65\x20\x41\x64\x6d\x69\x6e\x69\x73\x74\x72\x61\x74\x69\x6f\x6e",
                 "\x50\x72\x65\x73\x69\x64\x65\x6e\x74\x20\x42\x61\x72\x61\x63\x6b\x20\x4f\x62\x61\x6d\x61\x0a\x56\x69\x63\x65\x20\x50\x72\x65\x73\x69\x64\x65\x6e\x74\x20\x4a\x6f\x65\x20\x42\x69\x64\x65\x6e\x0a\x46\x69\x72\x73\x74\x20\x4c\x61\x64\x79\x20\x4d\x69\x63\x68\x65\x6c\x6c\x65\x20\x4f\x62\x61\x6d\x61\x0a\x44\x72\x2e\x20\x4a\x69\x6c\x6c\x20\x42\x69\x64\x65\x6e\x0a\x54\x68\x65\x20\x43\x61\x62\x69\x6e\x65\x74\x0a\x57\x68\x69\x74\x65\x20\x48\x6f\x75\x73\x65\x20\x53\x74\x61\x66\x66\x0a\x45\x78\x65\x63\x75\x74\x69\x76\x65\x20\x4f\x66\x66\x69\x63\x65\x20\x6f\x66\x20\x74\x68\x65\x20\x50\x72\x65\x73\x69\x64\x65\x6e\x74\x0a\x4f\x74\x68\x65\x72\x20\x41\x64\x76\x69\x73\x6f\x72\x79\x20\x42\x6f\x61\x72\x64\x73",
                 "\x41\x62\x6f\x75\x74\x20\x74\x68\x65\x20\x57\x68\x69\x74\x65\x20\x48\x6f\x75\x73\x65",
                 "\x49\x6e\x73\x69\x64\x65\x20\x74\x68\x65\x20\x57\x68\x69\x74\x65\x20\x48\x6f\x75\x73\x65\x0a\x50\x72\x65\x73\x69\x64\x65\x6e\x74\x73\x0a\x46\x69\x72\x73\x74\x20\x4c\x61\x64\x69\x65\x73\x0a\x54\x68\x65\x20\x4f\x76\x61\x6c\x20\x4f\x66\x66\x69\x63\x65\x0a\x54\x68\x65\x20\x56\x69\x63\x65\x20\x50\x72\x65\x73\x69\x64\x65\x6e\x74\x27\x73\x20\x52\x65\x73\x69\x64\x65\x6e\x63\x65\x20\x26\x20\x4f\x66\x66\x69\x63\x65\x0a\x45\x69\x73\x65\x6e\x68\x6f\x77\x65\x72\x20\x45\x78\x65\x63\x75\x74\x69\x76\x65\x20\x4f\x66\x66\x69\x63\x65\x20\x42\x75\x69\x6c\x64\x69\x6e\x67\x0a\x43\x61\x6d\x70\x20\x44\x61\x76\x69\x64\x0a\x41\x69\x72\x20\x46\x6f\x72\x63\x65\x20\x4f\x6e\x65\x0a\x57\x68\x69\x74\x65\x20\x48\x6f\x75\x73\x65\x20\x46\x65\x6c\x6c\x6f\x77\x73\x0a\x57\x68\x69\x74\x65\x20\x48\x6f\x75\x73\x65\x20\x49\x6e\x74\x65\x72\x6e\x73\x68\x69\x70\x73\x0a\x54\x6f\x75\x72\x73\x20\x26\x20\x45\x76\x65\x6e\x74\x73\x0a\x4d\x6f\x62\x69\x6c\x65\x20\x41\x70\x70\x73",
                 "\x4f\x75\x72\x20\x47\x6f\x76\x65\x72\x6e\x6d\x65\x6e\x74",
                 "\x54\x68\x65\x20\x45\x78\x65\x63\x75\x74\x69\x76\x65\x20\x42\x72\x61\x6e\x63\x68\x0a\x54\x68\x65\x20\x4c\x65\x67\x69\x73\x6c\x61\x74\x69\x76\x65\x20\x42\x72\x61\x6e\x63\x68\x0a\x54\x68\x65\x20\x4a\x75\x64\x69\x63\x69\x61\x6c\x20\x42\x72\x61\x6e\x63\x68\x0a\x54\x68\x65\x20\x43\x6f\x6e\x73\x74\x69\x74\x75\x74\x69\x6f\x6e\x0a\x46\x65\x64\x65\x72\x61\x6c\x20\x41\x67\x65\x6e\x63\x69\x65\x73\x20\x26\x20\x43\x6f\x6d\x6d\x69\x73\x73\x69\x6f\x6e\x73\x0a\x45\x6c\x65\x63\x74\x69\x6f\x6e\x73\x20\x26\x20\x56\x6f\x74\x69\x6e\x67\x0a\x53\x74\x61\x74\x65\x20\x26\x20\x4c\x6f\x63\x61\x6c\x20\x47\x6f\x76\x65\x72\x6e\x6d\x65\x6e\x74\x0a\x52\x65\x73\x6f\x75\x72\x63\x65\x73",
                 "\x45\x6e\x20\x65\x73\x70\x61\xc3\xb1\x6f\x6c",
                 "\x41\x63\x63\x65\x73\x73\x69\x62\x69\x6c\x69\x74\x79",
                 "\x43\x6f\x70\x79\x72\x69\x67\x68\x74",
                 "\x49\x6e\x66\x6f\x72\x6d\x61\x74\x69\x6f\x6e",
                 "\x50\x72\x69\x76\x61\x63\x79",
                 "\x50\x6f\x6c\x69\x63\x79",
                 "\x43\x6f\x6e\x74\x61\x63\x74",
                 "\x55\x53\x41\x2e\x67\x6f\x76",
                 "\x44\x65\x76\x65\x6c\x6f\x70\x65\x72\x73",
                 "\x41\x70\x70\x6c\x79\x20\x66\x6f\x72\x20\x61\x20\x4a\x6f\x62"
                
                 
                ]


print(raw_documents)



        
#print(files)

print("Number of documents:",len(raw_documents))
print(raw_documents[1])

gen_docs = [[w.lower() for w in word_tokenize(text)] 
            for text in raw_documents]
print(gen_docs)

dictionary = gensim.corpora.Dictionary(gen_docs)
print(dictionary[1])
#print(dictionary.token2id['students'])
print("Number of words in dictionary:",len(dictionary))
for i in range(len(dictionary)):
    print(i, dictionary[i].encode("utf-8"))
    
corpus = [dictionary.doc2bow(gen_doc) for gen_doc in gen_docs]
print("------------  corpus ----------------------")
print(corpus)


tf_idf = gensim.models.TfidfModel(corpus)
print(tf_idf)
s = 0
for i in corpus:
    s += len(i)
print(s)
    


sims = gensim.similarities.Similarity('C:/Users/Saeedeh/eclipse-workspace-python/NLP/nf',tf_idf[corpus],
                                      num_features=len(dictionary))
print(sims)
print(type(sims))



switcher = {
        0: "1"  ,
        1: "2"  ,
        2: "3"  ,
        3: "4"  ,
        4: "5"  ,
        5: "6"  ,
        6: "7"  ,
        7: "8"  ,
        8: "9"  ,
        9: "10" ,
        10: "11",
        11: "12",
        12: "13",
        13: "14",
        14: "15",
        15: "16",
        16: "17",
        17: "18",
        18: "19",
        19: "20",
        20: "21",
        21: "22",
        22: "23",
        23: "24",
        24: "25",
        25: "26",
        26: "27",
        27: "28",
        28: "29",
        29: "30",
        30: "31",
        31: "32",
        32: "33",
        33: "34",
        34: "35",
        35: "36",
        36: "37",
        37: "38",
        38: "39",
        39: "40",
        40: "41",
        41: "42",
        42: "43",
        43: "44",
        44: "45",
        45: "46",
        46: "47",
        47: "48",
        48: "49",
        49: "50",
        50: "51",
        51: "52",
        52: "53",
        53: "54",
        54: "55",
        55: "56",
        56: "57",
        57: "58",
        58: "59",
        59: "60",
        60: "61",
        61: "62",
        62: "63",
        63: "64",
        64: "65",
        65: "66",
        66: "67",
        67: "68",
        68: "69",
        69: "70",
        70: "71",
        71: "72",
        72: "73",
        73: "74",
        74: "75",
        75: "76",
        76: "77",
        77: "78",
        78: "79",
        79: "80",
        80: "81",
        81: "82",
        82: "83",
        83: "84",
        84: "85",
        85: "86",
        86: "87",
        87: "88",
        88: "89",
        89: "90",
        90: "91",
        91: "92",
        92: "93",
        93: "94",
        94: "95",
        95: "96",
        96: "97",
        97: "98",
        98: "99",
        99: "100",
        100: "101",
        101: "102",
        102: "103",
        103: "104",
        104: "105",
        105: "106",
        106: "107",
        107: "108"
        
    }


textFile = open("C:/Users/Saeedeh/Documents/TF_idf_Similarity.txt","w") 

i=0
j=0
while i<6:
        query_doc = [w.lower() for w in word_tokenize(raw_documents[i])]
        print("query_doc ",query_doc)
        query_doc_bow = dictionary.doc2bow(query_doc)
        print("query_doc_bow ",query_doc_bow)
        query_doc_tf_idf = tf_idf[query_doc_bow]
        print("query_doc_tf_idf ", query_doc_tf_idf)


        print("sims[query_doc_tf_idf]", sims[query_doc_tf_idf])
       # print("switch_demo(i)  ", switch_demo(i))
        j=0
        while j<6:
            print("-----------------------------------------------------------------------  ", i," ", j)
            print(switcher[i], " vs ",switcher[j]," -- :  " , sims[query_doc_tf_idf][j],"\n")
            numb = round(sims[query_doc_tf_idf][j], 4)
            textFile.write('{:0.4}\t'.format(numb))
            print(sims[query_doc_tf_idf][j])
            print(round(sims[query_doc_tf_idf][j], 4))
            j = j+1
        i=i+1
        textFile.write('\n')
textFile.close()



        