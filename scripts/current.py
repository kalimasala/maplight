#!/usr/bin/env python

import urllib
import json


url = 'http://data.maplight.org/FEC/active_incumbents.json'
data = json.load(urllib.urlopen(url))

print("UPDATE Import SET Current = False;")
print("UPDATE Import SET Current = True WHERE RecipientCandidateMLID IN (")
for person in data:
  print("\t{0},".format(person['person_id']))
print("-1);")

