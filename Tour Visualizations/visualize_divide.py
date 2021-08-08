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
import numpy as np
import matplotlib.pyplot as plt
import os
import sys
import time
sys.path.insert(0, str(Path(Path(__file__).parents[0] / 'lib')))


# ------ CONFIGURATION -------------------------

CONFIG = {'tsp_file' : 'Workspace/src/com/company/output_divideconquer.tsp'}

# ----------------------------------------------


	#	#	#	#	#	#	#	#	#	#	#
	#										#
	#			VISUALIZE 			 		#
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


	# ------ READ FROM FILE -------------------------

	print("Loading coordinates from .tsp file.")
	with open(CONFIG['tsp_file']) as f:
		coordinates = [x.rstrip() for x in f] 				# REMOVE \n

	coordinatesX, coordinatesY = [], []

	# OPEN FILE TO READ
	for i in range(0, len(coordinates)):

		# CITY 1
		line = coordinates[i].split('\t')
		city1 = {'id':int(line[0]), 'x':float(line[1]), 'y':float(line[2])}

		coordinatesX.append(city1['x'])
		coordinatesY.append(city1['y'])

		#print(str(city1['id']) + " <-> " + str(city2['id']))
		print(city1['id'], end=", ")


	# ------ DRAW GRAPH -----------------------------

	print("Drawing TSP tour graph...")

	#x, y = np.random.random(size=(2,10))
	'''for i in range(0, len(coordinatesX), 2):
		style = 'bo-'										# DEFAULT STYLE
		if i == 0:											# MARK STARTING POINT
			style = 'rD'
		# PLOT
		plt.plot(coordinatesX[i:i+2], coordinatesY[i:i+2], style, clip_on=False)'''

	# RETURN TO STARTING POINT
	coordinatesX.append(float(coordinates[0].split('\t')[1]))
	coordinatesY.append(float(coordinates[0].split('\t')[2]))

	# PLOT
	fig, ax = plt.subplots()
	plt.plot(coordinatesX, coordinatesY, 'bo-', markersize=0.2, linewidth=0.5, clip_on=False)

	# CONFIG
	plt.axis('scaled')
	plt.axis('off')
	plt.tight_layout()

	# DISABLE SCIENTIFIC NOTATION
	ax.ticklabel_format(style='plain')
	ax.format_coord = lambda x,y: f"x={x:.2f}, y={y:.2f}"
	ax.get_xaxis().get_major_formatter().set_scientific(False)
	ax.get_yaxis().get_major_formatter().set_useOffset(False)

	# ----------------------------------------------

	print("TSP tour graph generated.")

	# SHOW
	#plt.show()

	# SAVE TO FILE
	print("Graph saved to PNG file.")
	plt.savefig('graph_divide.png', dpi=400)

	# ROTATE PNG 90-degrees COUNTER-CLOCK WISE
	im = Image.open('graph_divide.png')
	out = im.rotate(90, expand=True)
	out.save('graph_divide.png')



if __name__ == '__main__':
	main()


