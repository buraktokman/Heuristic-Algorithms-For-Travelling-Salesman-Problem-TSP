#! usr/bin/env python3
# -*- coding: utf-8 -*-
'''
#-------------------------------------------------------------------------------
Course		: CMP3004 Formal Languages and Automata Theory
Purpose   	: Term Project - Travelling Salesman Problem

Created   	: 2021 May 31
Modified   	: 2021 May 31
Author(s)	: Ercument Burak Tokman (1315490)
Reference	:
Licence   	: Unauthorized copying of this file, via any medium is strictly prohibited
			  Proprietary and confidential
#-------------------------------------------------------------------------------
'''

from pathlib import Path
from PIL import Image
from matplotlib.ticker import ScalarFormatter, FormatStrFormatter
import numpy as np
import matplotlib.pyplot as plt
import os
import sys
import time
sys.path.insert(0, str(Path(Path(__file__).parents[0] / 'lib')))


# ------ CONFIGURATION -------------------------

CONFIG = {'tsp_file' : 'Workspace/src/com/company/output_greedy.tsp'}

# ----------------------------------------------

# ------ DFETCH USERS ---------------------------

	#	#	#	#	#	#	#	#	#	#	#
	#										#
	#			DRAW 			 			#
	#										#
	#	#	#	#	#	#	#	#	#	#	#


def main():
	"""
	A segment has two cities
	segment1 = [ {id, x, y}, {id2, x2, y} ]

	Returned list have multiple segments
	segments = [segment1, segments2, ...]
	"""
	global CONFIG

	# ------ READ FILE ----------------------------

	print("Loading coordinates from .tsp file.")

	with open(CONFIG['tsp_file']) as f:
		coordinates = [x.rstrip() for x in f] 				# REMOVE \n


	coordinatesX, coordinatesY = [], []

	citiesGreedy = []

	# OPEN FILE TO READ
	for i in range(0, len(coordinates)):

		# SPLIT TO id1,x1,y1,id2,x2,y2
		line = coordinates[i].split('\t')

		# CITY 1
		city1 = {'id':int(line[0]), 'x':float(line[1]), 'y':float(line[2])}

		# CITY 2
		city2 = {'id':int(line[3]), 'x':float(line[4]), 'y':float(line[5])}

		citiesGreedy.append([city1, city2])

		# ADD TO COORDS.
		coordinatesX.append(city1['x'])
		coordinatesY.append(city1['y'])
		coordinatesX.append(city2['x'])
		coordinatesY.append(city2['y'])

		# print(str(city1) + " <-> " + str(city2))


	# ------ DRAW GRAPH ----------------------------

	print("Drawing TSP tour graph...")

	fig, ax = plt.subplots()
	ax.get_xaxis().get_major_formatter().set_scientific(False)
	for i in range(0, len(coordinatesX), 2):
		style = 'bo-'										# DEFAULT STYLE
		if i == 0:											# MARK STARTING POINT
			style = 'rD-'
		# PLOT
		#plt.plot(coordinatesX[i:i+2], coordinatesY[i:i+2], style, linewidth=0.5, clip_on=False)
		ax.plot(coordinatesX[i:i+2], coordinatesY[i:i+2], style, markersize=0.2, linewidth=0.5, clip_on=False)

	# CONFIG
	plt.axis('scaled')
	plt.axis('off')
	plt.tight_layout()

	# ROTATE COUNTER CLOCKW. 90
	#ax.set_ylabel("x", rotation=270)
	#ax.yaxis.set_label_position("right")
	#ax.yaxis.tick_right()
	#ax.set_xlabel("y", rotation=270)
	#ax.invert_xaxis()
	#ax.invert_yaxis()

	# DISABLE SCIENTIFIC NOTATION
	ax.ticklabel_format(style='plain')
	ax.format_coord = lambda x,y: f"x={x:.2f}, y={y:.2f}"
	ax.get_xaxis().get_major_formatter().set_scientific(False)
	# ax.xaxis.set_major_formatter(FormatStrFormatter('%.0f'))
	ax.get_yaxis().get_major_formatter().set_useOffset(False)

	print("TSP tour graph generated.")
	# SHOW
	#plt.show()

	# SAVE TO FILE
	print("Graph saved to PNG file.")
	plt.savefig('visualize_greedy.png', dpi=300)

	# ROTATE PNG 90-degrees COUNTER-CLOCK WISE
	im = Image.open('visualize_greedy.png')
	out = im.rotate(90, expand=True)
	out.save('visualize_greedy.png')


	# ------ DRAW GRAPH ----------------------------

	print("Tour path: ")

	citiesVisited = []
	startCity = citiesGreedy[0][0]['id']
	citiesVisited.append(startCity)
	currentCityId = startCity

	for x in range(0, len(citiesGreedy)):
		for link in citiesGreedy:
			cityRight, cityLeft = link[0], link[1]

			if currentCityId == cityRight['id']:
				citiesVisited.append(cityLeft['id'])
				currentCityId = cityLeft['id']
				cityLeft['id'] = -1
				cityRight['id'] = -1
				break
			elif currentCityId == cityLeft['id']:
				citiesVisited.append(cityRight['id'])
				currentCityId = cityRight['id']
				cityLeft['id'] = -1
				cityRight['id'] = -1
				break

	print(citiesVisited)



if __name__ == '__main__':
	main()


