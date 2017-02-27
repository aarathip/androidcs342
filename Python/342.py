#!/usr/bin/env python

from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
import json, ast

class handler(BaseHTTPRequestHandler):
    def set_headers(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()

    def do_GET(self):
        self.set_headers()
        requested_name = self.raw_requestline.replace('GET ', '').split(' HTTP')[0][1:] #remove first slash

        try:
            store_or_get = requested_name.split('/')[0]
            name_of_user = requested_name.split('/')[1].lower()
        except IndexError:
            self.wfile.write('<html><body><h1>Query in this format "get/NAME" or "store/NAME" </h1></body></html>\n')
            return

        if store_or_get.lower() == 'get':
            location_file = open('location_data', 'r')
            data = location_file.read()
            location_file.close()

            dictionary = "{\n\t'Locations': {\n"
            dictionary += '\t\t\n'.join(data.lower().splitlines())
            dictionary += '\n\t}\n}'
            dictionary = ast.literal_eval(dictionary)

            try:
                gps_coords = dictionary['Locations'][name_of_user]
                self.wfile.write('<html><body><h1>GPS coordinates for %s: %s</h1></body></html>\n' % (name_of_user, gps_coords))
            except KeyError:
                self.wfile.write('<html><body><h1>The requested user [%s] was not found in the system.</h1></body></html>\n' % (name_of_user))

        elif store_or_get.lower() == 'store':
            (name_of_user, gps_coords) = name_of_user.split(':')
            with open('location_data', 'ab') as content:
                content.write(
                '''"%s": "<%s>"\n,''' % (name_of_user, gps_coords)
                )
            self.wfile.write('<html><body><h1>Stored coords [%s] for [%s] </h1></body></html>\n' % (gps_coords, name_of_user)) 
        else:
            self.wfile.write('<html><body><h1>Query in this format "get/NAME" or "store/NAME:GPS" </h1></body></html>\n')
            return
        return

        
def run():
    http_serv = HTTPServer(('', 8080), handler)
    print 'Starting server'
    http_serv.serve_forever()

if __name__ == "__main__":
    run()
