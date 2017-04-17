#!/usr/bin/env python

from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
import json, ast, os, time, hashlib, glob

class handler(BaseHTTPRequestHandler):
	def set_headers(self):
		self.send_response(200)
		self.send_header('Content-type', 'text/html')
		self.end_headers()

	def do_POST(self):
		self.set_headers()
		function = self.raw_requestline.split()[1]
		if function == '/storepic' or function == '/storepic/': #such a call requires a userID and pictureData json object
			try:
				payload = self.rfile.read(int(self.headers.getheader('content-length')))
				json_obj = ast.literal_eval(payload) #json.loads(payload)
				user_id = str(json_obj['userContext']['userID'])
				picture_data = json_obj['userContext']['pictureData']
				pic_name = json_obj['userContext']['pictureName']
				timestamp = str(time.time()) #truncate at ms
				random_hash = hashlib.sha1()
				random_hash.update(str(time.time()))
				#random hash will be a random 12 character string that will be unique to each user ID.
				random_hash = random_hash.hexdigest()[:12]
				if not os.path.isdir(user_id):
					os.mkdir(user_id)
				with open("%s/%s" % (user_id, random_hash), 'wb') as content:
					content.write(pic_name + '\n') #line 0 - name to send back to client upon request
					content.write(timestamp + '\n') #line 1 - timestamp is entry two
					content.write(picture_data.replace('\n', '')) #line 2 - picture data.
				self.wfile.write("Stored [%s] for user with ID [%s] - %s bytes.\nUser has [%s] pictures." % (pic_name, user_id, len(picture_data.decode('base64')), len(os.listdir(user_id)) - 1))
				return
			except KeyError:
				self.wfile.write('[x] Malformed request.\n')
				return
		self.wfile.write('Please specify a function to perform.\n')

	def do_GET(self):
		self.set_headers()
		requested_name = self.raw_requestline.split()[1]
		if requested_name.split('?')[0] == '/getpic':
			identifier = requested_name.split('?')[1].split('=')
			if identifier[0] != 'id':
				self.wfile.write('[x] Malformed request.\n')
				return
			else:
				user_id = identifier[1]
				dict_content = ""
				for stat in	sorted(glob.glob('%s/*' % user_id), key=os.path.getmtime):
					stat = stat.split('/')[1]
					if len(stat) == 12: #we have a something that looks like the random User ID generated above.
						#some error handling / file verification
						try:
							stat.decode('hex')
							with open('%s/%s' % (user_id, stat), 'r') as blob:
								user_image = blob.read()
							(pic_name, timestamp, picture_data) = user_image.splitlines()
							#timestamp = datetime.datetime.fromtimestamp(timestamp).strftime("%A %d. %B %Y")
							dict_content += picture_data + ":::"# + '^^^' + timestamp + ':::')
						except TypeError: #just a file name that happens to be 12 in length, but not one generated above.
							continue
				#print dict_content
				self.wfile.write(json.dumps(dict_content))
		else:
			self.wfile.write('[x] Malformed request.\n')
			return
		return

		
def run():
	http_serv = HTTPServer(('', 4646), handler)
	if not os.path.isdir('userdata'):
		os.mkdir('userdata')
	os.chdir('userdata')
	print 'Starting server'
	http_serv.serve_forever()

if __name__ == "__main__":
	run()