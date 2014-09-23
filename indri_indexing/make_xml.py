#!/opt/local/bin/python2.7
# -*- coding: utf-8 -*-

import MySQLdb

con = MySQLdb.connect('127.0.0.1', 'root', '', 'did')
cur = con.cursor()

cur.execute("select * from digging_master where type in ('title', 'description', 'subject') order by libid, rid")

# def gettitle(libid, rid):
# 	cur2.execute("select value from digging_master where type = 'title' and libid = '%s' and rid = '%s'" % (libid, rid))
# 	return cur2.fetchone()[0]

last_rid = ""
rid = ""

text = ""

for row in cur:
	libid, rid, field, ftype, value, vc = row
	
	if last_rid != rid and last_rid != "":
		print "<TEXT>\n%s\n</TEXT>" % text
		print "</DOC>\n"
		text = ""
		print "<DOC>"
		print "<DOCNO>%s:%s</DOCNO>" % (libid, rid)
		# print "<TITLE>%s</TITLE>" % gettitle(libid, rid) #manual only
	if last_rid == "":
		print "<DOC>"
		print "<DOCNO>%s:%s</DOCNO>" % (libid, rid)
		# print "<TITLE>%s</TITLE>" % gettitle(libid, rid) #manual only
			
	if ftype == "title" or ftype == "alternative_title1" or ftype == "alternative_title2":
		print "<TITLE>%s</TITLE>" % value
		
	if ftype == "description":
		text += " " + value
		
	if ftype == "subject":
		text += " " + value
		
	last_rid = rid

print "<TEXT>"
print text
print "</TEXT>"
print "</DOC>"
